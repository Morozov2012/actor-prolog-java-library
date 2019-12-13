// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax;

import target.*;

import morozov.run.*;
import morozov.syntax.data.*;
import morozov.syntax.data.domains.*;
import morozov.syntax.data.initializers.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.syntax.scanner.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.signals.*;
import morozov.system.*;
import morozov.terms.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.math.BigInteger;

public class ClassParser extends ClauseParser {
	//
	protected long classNameCode= -1;
	protected long ancestorNameCode= -1;
	protected long[] ancestorNameCodes= emptyLongArray;
	protected boolean isMetaInterface= false;
	protected ActorPrologSlotDeclaration[] slotDeclarations= emptySlotDeclarationArray;
	protected ActorPrologSlotDefinition[] slotDefinitions= emptySlotDefinitionArray;
	protected boolean slotDefinitionsContainDomains= false;
	protected ActorPrologArgumentDomain[] slotDomains= emptyArgumentDomainArray;
	protected ActorPrologClause[] actingClauses= emptyClauses;
	protected ActorPrologClause[] modelClauses= emptyClauses;
	protected ActorPrologDomainDefinition[] domainDefinitions= emptyDomainDefinitionArray;
	protected ActorPrologPredicateDeclaration[] predicateDeclarations= emptyPredicateDeclarationArray;
	protected String classSource;
	protected int classSourcePosition= -1;
	protected ActorPrologInterface interfaceDefinition= null;
	//
	protected static long[] emptyLongArray= new long[0];
	protected static ActorPrologSlotDeclaration[] emptySlotDeclarationArray= new ActorPrologSlotDeclaration[0];
	protected static ActorPrologSlotDefinition[] emptySlotDefinitionArray= new ActorPrologSlotDefinition[0];
	protected static ActorPrologClause[] emptyClauses= new ActorPrologClause[0];
	protected static ActorPrologArgumentDomain[] emptyArgumentDomainArray= new ActorPrologArgumentDomain[0];
	protected static ActorPrologDomainDefinition[] emptyDomainDefinitionArray= new ActorPrologDomainDefinition[0];
	protected static ActorPrologPredicateDeclaration[] emptyPredicateDeclarationArray= new ActorPrologPredicateDeclaration[0];
	//
	protected ActorPrologDomainDeclarator currentDomainDeclarator= ActorPrologDomainDeclarator.GROUND;
	protected ActorPrologPredicateDeterminancy currentPredicateDeterminancy= ActorPrologPredicateDeterminancy.DETERMINISTIC;
	protected ActorPrologVisibility currentPredicateVisibility= ActorPrologVisibility.UNDEFINED;
	//
	protected ActorPrologPortVariety currentSlotPortVariety= ActorPrologPortVariety.UNRESTRICTED;
	protected ActorPrologVisibility currentSlotVisibility= ActorPrologVisibility.UNDEFINED;
	//
	protected boolean initializerIsParsed= false;
	//
	protected static ActorPrologPredicateFlowDirection[][] noEmptyPredicateFlowPatterns= new ActorPrologPredicateFlowDirection[0][0];
	protected static ActorPrologPredicateFlowDirection[][] singleEmptyPredicateFlowPattern= new ActorPrologPredicateFlowDirection[1][0];
	//
	protected static ActorPrologPredicateArgumentDeclaration[] emptyPredicateArgumentDeclarationArray= new ActorPrologPredicateArgumentDeclaration[0];
	//
	protected static Term termOne= PrologInteger.ONE;
	//
	///////////////////////////////////////////////////////////////
	//
	public ClassParser(ParserMasterInterface m, boolean rememberPositions) {
		super(m,rememberPositions);
	}
	public ClassParser(ParserMasterInterface m, boolean rememberPositions, boolean implementRobustMode) {
		super(m,rememberPositions,implementRobustMode);
	}
	public ClassParser(ParserMasterInterface m) {
		super(m);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getClassNameCode() {
		return classNameCode;
	}
	public long getAncestorNameCode() {
		return ancestorNameCode;
	}
	public Term getTermAncestor() {
		return ActorPrologUnit.getTermAncestorCode(ancestorNameCode);
	}
	public long[] getAncestorNameCodes() {
		return ancestorNameCodes;
	}
	public boolean isMetaInterface() {
		return isMetaInterface;
	}
	public ActorPrologSlotDeclaration[] getSlotDeclarations() {
		return slotDeclarations;
	}
	public ActorPrologSlotDefinition[] getSlotDefinitions() {
		return slotDefinitions;
	}
	public ActorPrologArgumentDomain[] getSlotDomains() {
		return slotDomains;
	}
	public ActorPrologClause[] getActingClauses() {
		return actingClauses;
	}
	public ActorPrologClause[] getModelClauses() {
		return modelClauses;
	}
	public ActorPrologDomainDefinition[] getDomainDefinitions() {
		return domainDefinitions;
	}
	public ActorPrologPredicateDeclaration[] getPredicateDeclarations() {
		return predicateDeclarations;
	}
	public String getClassSource() {
		return classSource;
	}
	public int getClassSourcePosition() {
		return classSourcePosition;
	}
	public Term getTermClassSource() {
		Term termClassSource;
		if (classSource==null) {
			termClassSource= PrologUnknownValue.instance;
		} else {
			termClassSource= new PrologString(classSource);
			if (rememberTextPositions) {
				termClassSource= attachTermPosition(termClassSource,classSourcePosition);
			}
		};
		return termClassSource;
	}
	public ActorPrologInterface getInterface() {
		return interfaceDefinition;
	}
	public Term getTermInterface() {
		Term termInterface;
		if (interfaceDefinition != null) {
			termInterface= interfaceDefinition.toActorPrologTerm();
		} else {
			termInterface= ActorPrologInterface.getTermNoInterface();
		};
		return termInterface;
	}
	//
	@Override
	protected boolean initializerIsParsed() {
		return initializerIsParsed;
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE DOMAIN DEFINITION                                   //
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologDomainDefinition[] parseDomainDefinitions(ChoisePoint iX) throws SyntaxError {
		ArrayList<ActorPrologDomainDefinition> domainDefinitionArray= new ArrayList<>();
		parseDomainDefinitions(domainDefinitionArray,iX);
		return domainDefinitionArray.toArray(new ActorPrologDomainDefinition[domainDefinitionArray.size()]);
	}
	public void parseDomainDefinitions(ArrayList<ActorPrologDomainDefinition> domainDefinitionArray, ChoisePoint iX) throws SyntaxError {
		DomainDefinitionLoop: while (true) {
			if (position >= numberOfTokens) {
				break DomainDefinitionLoop;
			};
			PrologToken firstToken= tokens[position];
			if (firstToken.isFinalToken()) {
				break DomainDefinitionLoop;
			};
			int firstTokenPosition= firstToken.getPosition();
			PrologTokenType firstTokenType= firstToken.getType();
			switch (firstTokenType) {
			case KEYWORD:
				if (firstToken.isInQuotes(master,iX)) {
					break DomainDefinitionLoop;
				};
				long firstTokenCode= firstToken.getSymbolCode(master,iX);
				if (firstTokenCode==SymbolCodes.symbolCode_E_reference) {
					position++;
					parseColon(iX);
					currentDomainDeclarator= ActorPrologDomainDeclarator.REFERENCE;
				} else if (firstTokenCode==SymbolCodes.symbolCode_E_lazy) {
					position++;
					parseColon(iX);
					currentDomainDeclarator= ActorPrologDomainDeclarator.LAZY;
				} else if (firstTokenCode==SymbolCodes.symbolCode_E_ground) {
					position++;
					parseColon(iX);
					currentDomainDeclarator= ActorPrologDomainDeclarator.GROUND;
				} else if (firstTokenCode==SymbolCodes.symbolCode_E_mixed) {
					position++;
					parseColon(iX);
					currentDomainDeclarator= ActorPrologDomainDeclarator.MIXED;
				} else {
					break DomainDefinitionLoop;
				};
				continue DomainDefinitionLoop;
			case R_SQUARE_BRACKET:
				break DomainDefinitionLoop;
			};
			parseDomainDefinition(domainDefinitionArray,iX);
		}
	}
	//
	public ActorPrologDomainDefinition[] parseDomainDefinition(ChoisePoint iX) throws SyntaxError {
		ArrayList<ActorPrologDomainDefinition> domainDefinitionArray= new ArrayList<>();
		currentDomainDeclarator= ActorPrologDomainDeclarator.GROUND;
		parseDomainDefinition(domainDefinitionArray,iX);
		return domainDefinitionArray.toArray(new ActorPrologDomainDefinition[domainDefinitionArray.size()]);
	}
	//
	public void parseDomainDefinition(ArrayList<ActorPrologDomainDefinition> domainDefinitionArray, ChoisePoint iX) throws SyntaxError {
		ArrayList<InternalName> domainNameArray= new ArrayList<>();
		DomainNameLoop: while (true) {
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			PrologToken firstToken= tokens[position];
			int firstTokenPosition= firstToken.getPosition();
			PrologTokenType firstTokenType= firstToken.getType();
			if (firstTokenType==PrologTokenType.VARIABLE) {
				position++;
				String domainName= firstToken.getVariableName(master,iX);
				checkWhetherDomainNameCanBeDefined(domainName,firstTokenPosition,iX);
				if (!robustMode) {
					Iterator<InternalName> domainNameArrayIterator= domainNameArray.iterator();
					while (domainNameArrayIterator.hasNext()) {
						InternalName element= domainNameArrayIterator.next();
						String name= element.getName();
						if (name.equals(domainName)) {
							master.handleError(new ThisDomainIsAlreadyDefined(domainName,firstTokenPosition),iX);
						}
					}
				};
				domainNameArray.add(new InternalName(domainName,firstTokenPosition));
				if (position >= numberOfTokens) {
					throw master.handleUnexpectedEndOfTokenList(tokens,iX);
				};
				PrologToken secondToken= tokens[position];
				int secondTokenPosition= secondToken.getPosition();
				PrologTokenType secondTokenType= secondToken.getType();
				switch (secondTokenType) {
				case COMMA:
					position++;
					continue DomainNameLoop;
				case EQ:
					position++;
					break DomainNameLoop;
				default:
					master.handleError(new EquationIsExpected(secondTokenPosition),iX);
					position++;
				}
			} else {
				master.handleError(new DomainNameIsExpected(firstTokenPosition),iX);
				position++;
			}
		};
		ArrayList<ActorPrologDomainAlternative> alternatives= new ArrayList<>();
		DomainAlternativeLoop: while (true) {
///////////////////////////////////////////////////////////////////////
ActorPrologDomainAlternative alternative= parseDomainAlternative(iX);
Iterator<ActorPrologDomainAlternative> alternativesIterator1= alternatives.iterator();
while (alternativesIterator1.hasNext()) {
	ActorPrologDomainAlternative element= alternativesIterator1.next();
	if (alternative.equals(element) && !robustMode) {
		master.handleError(new ThisAlternativeIsAlreadyDefined(alternative.getPosition()),iX);
	}
};
if (alternative instanceof ActorPrologDomainStructure) {
	ActorPrologDomainStructure structure1= (ActorPrologDomainStructure)alternative;
	Iterator<ActorPrologDomainAlternative> alternativesIterator2= alternatives.iterator();
	while (alternativesIterator2.hasNext()) {
		ActorPrologDomainAlternative element= alternativesIterator2.next();
		if (element instanceof ActorPrologDomainStructure) {
			ActorPrologDomainStructure structure2= (ActorPrologDomainStructure)element;
			if (structure1.getFunctor()==structure2.getFunctor() && !robustMode) {
				master.handleError(new ThisFunctorIsAlreadyUsedInTheDomain(structure1.getFunctor(),alternative.getPosition()),iX);
			}
		}
	}
};
if (alternative instanceof ActorPrologDomainList) {
	ActorPrologDomainList list1= (ActorPrologDomainList)alternative;
	Iterator<ActorPrologDomainAlternative> alternativesIterator3= alternatives.iterator();
	while (alternativesIterator3.hasNext()) {
		ActorPrologDomainAlternative element= alternativesIterator3.next();
		if (element instanceof ActorPrologDomainList && !robustMode) {
			master.handleError(new ListAlternativeIsAlreadyUsedInTheDomain(alternative.getPosition()),iX);
		}
	}
};
if (alternative instanceof ActorPrologDomainSet) {
	ActorPrologDomainSet set1= (ActorPrologDomainSet)alternative;
	LabeledDomain[] pairs1= set1.getPairs();
	Iterator<ActorPrologDomainAlternative> alternativesIterator4= alternatives.iterator();
	while (alternativesIterator4.hasNext()) {
		ActorPrologDomainAlternative element= alternativesIterator4.next();
		if (element instanceof ActorPrologDomainSet) {
			ActorPrologDomainSet set2= (ActorPrologDomainSet)element;
			LabeledDomain[] pairs2= set2.getPairs();
			for (int k=0; k < pairs1.length; k++) {
				LabeledDomain pair1= pairs1[k];
				long keyCode1= pair1.getKeyCode();
				for (int m=0; m < pairs2.length; m++) {
					LabeledDomain pair2= pairs2[m];
					long keyCode2= pair2.getKeyCode();
					if (keyCode1==keyCode2 && !robustMode) {
						master.handleError(new PairNameIsAlreadyUsedInTheSet(
							pair1.getIntegerKey(),
							pair1.hasSymbolicName(),
							pair1.getPosition()),iX);
					}
				}
			}
		}
	}
};
alternatives.add(alternative);
PrologToken thirdToken= tokens[position];
int thirdTokenPosition= thirdToken.getPosition();
PrologTokenType thirdTokenType= thirdToken.getType();
switch (thirdTokenType) {
case SEMICOLON:
	position++;
	continue DomainAlternativeLoop;
case DOT:
	position++;
	break DomainAlternativeLoop;
default:
	master.handleError(new SemicolonIsExpected(thirdTokenPosition),iX);
	position++;
}
///////////////////////////////////////////////////////////////////////
		};
		ActorPrologDomainAlternative[] alternativeArray= alternatives.toArray(new ActorPrologDomainAlternative[alternatives.size()]);
		Iterator<InternalName> domainNameArrayIterator= domainNameArray.iterator();
		while (domainNameArrayIterator.hasNext()) {
			InternalName element= domainNameArrayIterator.next();
			String name= element.getName();
			for (int k=0; k < alternativeArray.length; k++) {
				ActorPrologDomainAlternative alternative= alternativeArray[k];
				if (alternative.isDomainName(name) && !robustMode) {
					master.handleError(new DomainCannotBeAnAlternativeOfItself(alternative.getPosition()),iX);
				}
			};
			if (!robustMode) {
				checkWhetherDomainIsAlreadyDefined(domainDefinitionArray,name,element.getPosition(),iX);
			};
			domainDefinitionArray.add(new ActorPrologDomainDefinition(
				name,
				currentDomainDeclarator,
				alternativeArray,
				element.getPosition()));
		}
	}
	//
	protected void checkWhetherDomainNameCanBeDefined(String domainName, int tokenPosition, ChoisePoint iX) throws ParserError {
		if (robustMode) {
			return;
		};
		if (domainName.equals(anonymousVariableName)) {
			master.handleError(new AnonymousDomainCannotBeRedefined(tokenPosition),iX);
		} else if (domainName.equals(ActorPrologDomainAnInteger.getStaticName())) {
			master.handleError(new TheIntegerDomainCannotBeRedefined(tokenPosition),iX);
		} else if (domainName.equals(ActorPrologDomainAReal.getStaticName())) {
			master.handleError(new TheRealDomainCannotBeRedefined(tokenPosition),iX);
		} else if (domainName.equals(ActorPrologDomainAString.getStaticName())) {
			master.handleError(new TheStringDomainCannotBeRedefined(tokenPosition),iX);
		} else if (domainName.equals(ActorPrologDomainASymbol.getStaticName())) {
			master.handleError(new TheSymbolDomainCannotBeRedefined(tokenPosition),iX);
		} else if (domainName.equals(ActorPrologDomainABinary.getStaticName())) {
			master.handleError(new TheBinaryDomainCannotBeRedefined(tokenPosition),iX);
		}
	}
	//
	protected void checkWhetherDomainIsAlreadyDefined(ArrayList<ActorPrologDomainDefinition> domainDefinitionArray, String name, int p, ChoisePoint iX) throws ParserError {
		Iterator<ActorPrologDomainDefinition> domainDefinitionArrayIterator= domainDefinitionArray.iterator();
		while (domainDefinitionArrayIterator.hasNext()) {
			ActorPrologDomainDefinition definition= domainDefinitionArrayIterator.next();
			if (name.equals(definition.getName())) {
				master.handleError(new ThisDomainIsAlreadyDefined(name,p),iX);
				break;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE DOMAIN ALTERNATIVE                                  //
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologDomainAlternative parseDomainAlternative(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		int firstTokenPosition= firstToken.getPosition();
		PrologTokenType firstTokenType= firstToken.getType();
		switch (firstTokenType) {
		case SYMBOL:
			{
///////////////////////////////////////////////////////////////////////
position++;
long firstTokenCode= firstToken.getSymbolCode(master,iX);
if (position >= numberOfTokens) {
	checkWhetherSymbolConstantIsEnclosedInApostrophes(firstToken,iX);
	return new ActorPrologDomainSymbolConstant(firstTokenCode,firstTokenPosition);
};
PrologToken secondToken= tokens[position];
// int secondTokenPosition= secondToken.getPosition();
PrologTokenType secondTokenType= secondToken.getType();
switch (secondTokenType) {
case L_ROUND_BRACKET:
	position++;
	ActorPrologArgumentDomain[] domains= parseArgumentDomains(iX);
	// parseRightRoundBracket(iX);
	return new ActorPrologDomainStructure(firstTokenCode,domains,firstTokenPosition);
case KEYWORD:
	long secondTokenCode= secondToken.getSymbolCode(master,iX);
	if (secondTokenCode==SymbolCodes.symbolCode_E_of) {
		int p2= position + 1;
		if (p2 >= numberOfTokens) {
			checkWhetherClassNameIsEnclosedInApostrophes(firstToken,iX);
			return new ActorPrologDomainWorldConstant(firstTokenCode,firstTokenPosition);
		};
		PrologToken thirdToken= tokens[p2];
		PrologTokenType thirdTokenType= thirdToken.getType();
		if (thirdTokenType==PrologTokenType.SYMBOL) {
			position= position + 2;
			long thirdTokenCode= thirdToken.getSymbolCode(master,iX);
			if (secondToken.isInQuotes(master,iX) && !robustMode) {
				master.handleError(new SymbolEnclosedInApostrophesIsNotExpectedHere(secondToken.getPosition()),iX);
			};
			checkWhetherClassNameIsEnclosedInApostrophes(thirdToken,iX);
			return new ActorPrologDomainArrayOfWorlds(firstTokenCode,thirdTokenCode,firstTokenPosition);
		}
	}
};
checkWhetherSymbolConstantIsEnclosedInApostrophes(firstToken,iX);
return new ActorPrologDomainSymbolConstant(firstTokenCode,firstTokenPosition);
///////////////////////////////////////////////////////////////////////
			}
		case VARIABLE:
			{
///////////////////////////////////////////////////////////////////////
position++;
String domainName= firstToken.getVariableName(master,iX);
if (position >= numberOfTokens) {
	if (domainName.equals(anonymousVariableName) && !robustMode) {
		master.handleError(new AnonymousDomainCannotBeADomainAlternative(firstTokenPosition),iX);
	};
	return variableToActorPrologDomainName(domainName,firstTokenPosition);
};
PrologToken secondToken= tokens[position];
PrologTokenType secondTokenType= secondToken.getType();
switch (secondTokenType) {
case L_BRACE:
	{
	position++;
	ArrayList<LabeledDomain> arguments= new ArrayList<>();
	ActorPrologArgumentDomain domain= variableToActorPrologDomainName(domainName,firstTokenPosition);
	arguments.add(new LabeledDomain(0,false,domain,firstTokenPosition));
	return parseUnderdeterminedSetDomain(arguments,firstTokenPosition,iX);
	}
case MULTIPLY:
	{
	position++;
	ActorPrologDomainName domain= variableToActorPrologDomainName(domainName,firstTokenPosition);
	return new ActorPrologDomainList(domain,firstTokenPosition);
	}
};
if (domainName.equals(anonymousVariableName) && !robustMode) {
	master.handleError(new AnonymousDomainCannotBeADomainAlternative(firstTokenPosition),iX);
};
return variableToActorPrologDomainName(domainName,firstTokenPosition);
///////////////////////////////////////////////////////////////////////
			}
		case L_BRACE:
			{
///////////////////////////////////////////////////////////////////////
position++;
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken secondToken= tokens[position];
PrologTokenType secondTokenType= secondToken.getType();
switch (secondTokenType) {
case R_BRACE:
	position++;
	return new ActorPrologDomainEmptySet(firstTokenPosition);
case VARIABLE:
	position++;
	String domainName= secondToken.getVariableName(master,iX);
	if (domainName.equals(anonymousVariableName)) {
		parseRightBrace(iX);
		return new ActorPrologDomainAnySet(firstTokenPosition);
	} else {
		if (!robustMode) {
			master.handleError(new AnonymousDomainIsExpected(secondToken.getPosition()),iX);
		}
	};
	break;
};
ArrayList<LabeledDomain> arguments= new ArrayList<>();
return parseUnderdeterminedSetDomain(arguments,firstTokenPosition,iX);
///////////////////////////////////////////////////////////////////////
			}
		case NUMBER_SIGN:
///////////////////////////////////////////////////////////////////////
position++;
return new ActorPrologDomainUnknownValue(firstTokenPosition);
///////////////////////////////////////////////////////////////////////
		case INTEGER:
///////////////////////////////////////////////////////////////////////
position++;
return parseIntegerConstant(firstToken,false,iX);
///////////////////////////////////////////////////////////////////////
		case REAL:
///////////////////////////////////////////////////////////////////////
position++;
return parseRealConstant(firstToken,false,iX);
///////////////////////////////////////////////////////////////////////
		case MINUS:
///////////////////////////////////////////////////////////////////////
position++;
return parseConstantAfterMinus(iX);
///////////////////////////////////////////////////////////////////////
		case STRING_SEGMENT:
///////////////////////////////////////////////////////////////////////
position++;
String stringValue= parseString(firstToken.getStringValue(master,iX),iX);
return new ActorPrologDomainStringConstant(stringValue,firstTokenPosition);
///////////////////////////////////////////////////////////////////////
		case BINARY_SEGMENT:
///////////////////////////////////////////////////////////////////////
position++;
byte[] binaryValue= parseBinary(firstToken.getBinaryValue(master,iX),iX);
return new ActorPrologDomainBinaryConstant(binaryValue,firstTokenPosition);
///////////////////////////////////////////////////////////////////////
		case L_ROUND_BRACKET:
			{
///////////////////////////////////////////////////////////////////////
position++;
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
try {
	long nameCode= parseClassName(iX);
	parseRightRoundBracket(iX);
	return new ActorPrologDomainWorldConstant(nameCode,firstTokenPosition);
} catch (NoClassNameIsFound e) {
	position++;
}
///////////////////////////////////////////////////////////////////////
			};
			break;
		case L_SQUARE_BRACKET:
			{
///////////////////////////////////////////////////////////////////////
position++;
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken secondToken= tokens[position];
PrologTokenType secondTokenType= secondToken.getType();
boolean negateLeftBound= false;
if (secondTokenType==PrologTokenType.MINUS) {
	negateLeftBound= true;
	position++;
	if (position >= numberOfTokens) {
		throw master.handleUnexpectedEndOfTokenList(tokens,iX);
	};
	secondToken= tokens[position];
	secondTokenType= secondToken.getType();
	if (!robustMode && secondToken.isExtendedNumber(master,iX)) {
		master.handleError(new MinusIsNotAllowedBeforeExtendedNumbers(secondToken.getPosition()),iX);
	}
};
switch (secondTokenType) {
case INTEGER:
	{
	position++;
	ActorPrologDomainIntegerConstant leftBound= parseIntegerConstant(secondToken,negateLeftBound,iX);
	parseRange(iX);
	if (position >= numberOfTokens) {
		throw master.handleUnexpectedEndOfTokenList(tokens,iX);
	};
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	boolean negateRightBound= false;
	if (thirdTokenType==PrologTokenType.MINUS) {
		negateRightBound= true;
		position++;
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		thirdToken= tokens[position];
		thirdTokenType= thirdToken.getType();
		if (!robustMode && thirdToken.isExtendedNumber(master,iX)) {
			master.handleError(new MinusIsNotAllowedBeforeExtendedNumbers(thirdToken.getPosition()),iX);
		}
	};
	if (thirdTokenType==PrologTokenType.INTEGER) {
		position++;
		ActorPrologDomainIntegerConstant rightBound= parseIntegerConstant(thirdToken,negateRightBound,iX);
		parseRightSquareBracket(iX);
		return new ActorPrologDomainIntegerRange(leftBound,rightBound,firstTokenPosition);
	} else {
		master.handleError(new AnIntegerIsExpected(thirdToken.getPosition()),iX);
		position++;
	}
	};
	break;
case REAL:
	{
	position++;
	ActorPrologDomainRealConstant leftBound= parseRealConstant(secondToken,negateLeftBound,iX);
	parseRange(iX);
	if (position >= numberOfTokens) {
		throw master.handleUnexpectedEndOfTokenList(tokens,iX);
	};
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	boolean negateRightBound= false;
	if (thirdTokenType==PrologTokenType.MINUS) {
		negateRightBound= true;
		position++;
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		thirdToken= tokens[position];
		thirdTokenType= thirdToken.getType();
		if (!robustMode && thirdToken.isExtendedNumber(master,iX)) {
			master.handleError(new MinusIsNotAllowedBeforeExtendedNumbers(thirdToken.getPosition()),iX);
		}
	};
	if (thirdTokenType==PrologTokenType.REAL) {
		position++;
		ActorPrologDomainRealConstant rightBound= parseRealConstant(thirdToken,negateRightBound,iX);
		parseRightSquareBracket(iX);
		return new ActorPrologDomainRealRange(leftBound,rightBound,firstTokenPosition);
	} else {
		master.handleError(new ARealIsExpected(thirdToken.getPosition()),iX);
		position++;
	}
	};
	break;
default:
	master.handleError(new AnIntegerOrARealIsExpected(secondToken.getPosition()),iX);
	position++;
}
///////////////////////////////////////////////////////////////////////
			};
			break;
		default:
///////////////////////////////////////////////////////////////////////
master.handleError(new DomainAlternativeIsExpected(firstTokenPosition),iX);
position++;
///////////////////////////////////////////////////////////////////////
		};
		return new ActorPrologDomainUnknownValue(firstTokenPosition);
	}
	//
	protected ActorPrologDomainIntegerConstant parseIntegerConstant(PrologToken token, boolean negate, ChoisePoint iX) throws SyntaxError {
		BigInteger value= token.getIntegerValue(master,iX);
		if (negate) {
			value= value.negate();
		};
		if (token instanceof TokenIntegerR) {
			TokenIntegerR tokenR= (TokenIntegerR)token;
			BigInteger radix= tokenR.getRadix();
			return new ActorPrologDomainIntegerConstant(value,radix,token.getPosition());
		} else {
			return new ActorPrologDomainIntegerConstant(value,token.getPosition());
		}
	}
	//
	protected ActorPrologDomainRealConstant parseRealConstant(PrologToken token, boolean negate, ChoisePoint iX) throws SyntaxError {
		double value= token.getRealValue(master,iX);
		if (negate) {
			value= - value;
		};
		if (token instanceof TokenRealR) {
			TokenRealR tokenR= (TokenRealR)token;
			BigInteger radix= tokenR.getRadix();
			return new ActorPrologDomainRealConstant(value,radix,token.getPosition());
		} else {
			return new ActorPrologDomainRealConstant(value,token.getPosition());
		}
	}
	//
	protected ActorPrologDomainAlternative parseConstantAfterMinus(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		PrologTokenType firstTokenType= firstToken.getType();
		switch (firstTokenType) {
		case INTEGER:
			{
///////////////////////////////////////////////////////////////////////
if (!robustMode && firstToken.isExtendedNumber(master,iX)) {
	master.handleError(new MinusIsNotAllowedBeforeExtendedNumbers(firstToken.getPosition()),iX);
};
position++;
return parseIntegerConstant(firstToken,true,iX);
///////////////////////////////////////////////////////////////////////
			}
		case REAL:
			{
///////////////////////////////////////////////////////////////////////
if (!robustMode && firstToken.isExtendedNumber(master,iX)) {
	master.handleError(new MinusIsNotAllowedBeforeExtendedNumbers(firstToken.getPosition()),iX);
};
position++;
return parseRealConstant(firstToken,true,iX);
///////////////////////////////////////////////////////////////////////
			}
		default:
			master.handleError(new UnexpectedToken(firstToken),iX);
			position++;
			return new ActorPrologDomainIntegerConstant(BigInteger.ZERO,firstToken.getPosition());
		}
	}
	//
	protected ActorPrologArgumentDomain[] parseArgumentDomains(ChoisePoint iX) throws SyntaxError {
		ArrayList<ActorPrologArgumentDomain> structureArgumentDomainArray= new ArrayList<>();
		StructureArgumentDomainLoop: while (true) {
			ActorPrologArgumentDomain domain= parseArgumentDomain(iX);
			structureArgumentDomainArray.add(domain);
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			PrologToken firstToken= tokens[position];
			PrologTokenType firstTokenType= firstToken.getType();
			switch (firstTokenType) {
			case COMMA:
				position++;
				continue StructureArgumentDomainLoop;
			case R_ROUND_BRACKET:
				position++;
				break StructureArgumentDomainLoop;
			default:
				master.handleError(new CommaOrRightRoundBracketIsExpected(firstToken.getPosition()),iX);
				position++;
				break StructureArgumentDomainLoop;
			}
		};
		return structureArgumentDomainArray.toArray(new ActorPrologArgumentDomain[structureArgumentDomainArray.size()]);
	}
	//
	protected ActorPrologArgumentDomain parseArgumentDomain(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		int firstTokenPosition= firstToken.getPosition();
		PrologTokenType firstTokenType= firstToken.getType();
		switch (firstTokenType) {
		case SYMBOL:
///////////////////////////////////////////////////////////////////////
position++;
long firstTokenCode= firstToken.getSymbolCode(master,iX);
if (position >= numberOfTokens) {
	checkWhetherClassNameIsEnclosedInApostrophes(firstToken,iX);
	return new ActorPrologDomainWorldConstant(firstTokenCode,firstTokenPosition);
};
PrologToken secondToken= tokens[position];
PrologTokenType secondTokenType= secondToken.getType();
if (secondTokenType==PrologTokenType.KEYWORD) {
	long secondTokenCode= secondToken.getSymbolCode(master,iX);
	if (secondTokenCode==SymbolCodes.symbolCode_E_of) {
		int p2= position + 1;
		if (p2 >= numberOfTokens) {
			checkWhetherClassNameIsEnclosedInApostrophes(firstToken,iX);
			return new ActorPrologDomainWorldConstant(firstTokenCode,firstTokenPosition);
		};
		PrologToken thirdToken= tokens[p2];
		PrologTokenType thirdTokenType= thirdToken.getType();
		if (thirdTokenType==PrologTokenType.SYMBOL) {
			position= position + 2;
			long thirdTokenCode= thirdToken.getSymbolCode(master,iX);
			if (secondToken.isInQuotes(master,iX) && !robustMode) {
				master.handleError(new SymbolEnclosedInApostrophesIsNotExpectedHere(secondToken.getPosition()),iX);
			};
			checkWhetherClassNameIsEnclosedInApostrophes(thirdToken,iX);
			return new ActorPrologDomainArrayOfWorlds(firstTokenCode,thirdTokenCode,firstTokenPosition);
		}
	}
};
checkWhetherClassNameIsEnclosedInApostrophes(firstToken,iX);
return new ActorPrologDomainWorldConstant(firstTokenCode,firstTokenPosition);
///////////////////////////////////////////////////////////////////////
		case VARIABLE:
///////////////////////////////////////////////////////////////////////
position++;
String domainName= firstToken.getVariableName(master,iX);
return variableToActorPrologDomainName(domainName,firstTokenPosition);
///////////////////////////////////////////////////////////////////////
		case NUMBER_SIGN:
///////////////////////////////////////////////////////////////////////
position++;
return new ActorPrologDomainUnknownValue(firstTokenPosition);
///////////////////////////////////////////////////////////////////////
		default:
///////////////////////////////////////////////////////////////////////
master.handleError(new ArgumentDomainIsExpected(firstTokenPosition),iX);
position++;
///////////////////////////////////////////////////////////////////////
		};
		return new ActorPrologDomainUnknownValue(firstTokenPosition);
	}
	//
	protected ActorPrologDomainSet parseUnderdeterminedSetDomain(ArrayList<LabeledDomain> arguments, int tokenPosition, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken secondToken= tokens[position];
		int secontTokenPosition= secondToken.getPosition();
		PrologTokenType secondTokenType= secondToken.getType();
		if (secondTokenType==PrologTokenType.R_BRACE) {
			position++;
			if (arguments.size() > 0) {
				return new ActorPrologDomainSet(arguments.toArray(new LabeledDomain[arguments.size()]),tokenPosition);
			} else {
				return new ActorPrologDomainEmptySet(tokenPosition);
			}
		};
		parseDomainPairs(arguments,iX);
		parseRightBrace(iX);
		return new ActorPrologDomainSet(arguments.toArray(new LabeledDomain[arguments.size()]),tokenPosition);
	}
	//
	protected void parseDomainPairs(ArrayList<LabeledDomain> domains, ChoisePoint iX) throws SyntaxError {
		boolean commaExpected= false;
		while (true) {
			if (commaExpected) {
				if (position >= numberOfTokens) {
					throw master.handleUnexpectedEndOfTokenList(tokens,iX);
				};
				PrologToken firstToken= tokens[position];
				PrologTokenType firstTokenType= firstToken.getType();
				if (firstTokenType != PrologTokenType.COMMA) {
					return;
				};
				position++;
			} else {
				commaExpected= true;
			};
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			PrologToken secondToken= tokens[position];
			PrologTokenType secondTokenType= secondToken.getType();
			switch (secondTokenType) {
			case SYMBOL:
				{
///////////////////////////////////////////////////////////////////////
int pairPosition= secondToken.getPosition();
position++;
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken thirdToken= tokens[position];
PrologTokenType thirdTokenType= thirdToken.getType();
if (thirdTokenType==PrologTokenType.COLON) {
	position++;
	long pairNameCode= secondToken.getSymbolCode(master,iX);
	if (pairNameCodeIsAlreadyDefined(pairNameCode,true,domains) && !robustMode) {
		master.handleError(new PairNameIsAlreadyUsedInTheSet(pairNameCode,true,secondToken.getPosition()),iX);
	};
	ActorPrologArgumentDomain pairDomain= parseArgumentDomain(iX);
	domains.add(new LabeledDomain(pairNameCode,true,pairDomain,pairPosition));
} else {
	master.handleError(new ColonIsExpected(thirdToken.getPosition()),iX);
	position++;
}
///////////////////////////////////////////////////////////////////////
				};
				break;
			case INTEGER:
				{
///////////////////////////////////////////////////////////////////////
int pairPosition= secondToken.getPosition();
position++;
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken thirdToken= tokens[position];
PrologTokenType thirdTokenType= thirdToken.getType();
if (thirdTokenType==PrologTokenType.COLON) {
	position++;
	BigInteger key= secondToken.getIntegerValue(master,iX);
	if (key.compareTo(BigInteger.ZERO) < 0) {
		master.handleError(new NegativeNumbersAreNotAllowedHere(secondToken.getPosition()),iX);
	} else {
		long pairNameCode= Arithmetic.toLong(key);
		if (pairNameCodeIsAlreadyDefined(pairNameCode,false,domains) && !robustMode) {
			master.handleError(new PairNameIsAlreadyUsedInTheSet(pairNameCode,false,secondToken.getPosition()),iX);
		};
		ActorPrologArgumentDomain pairDomain= parseArgumentDomain(iX);
		domains.add(new LabeledDomain(pairNameCode,false,pairDomain,pairPosition));
	};
} else {
	master.handleError(new ColonIsExpected(thirdToken.getPosition()),iX);
	position++;
}
///////////////////////////////////////////////////////////////////////
				};
				break;
			default:
				master.handleError(new SymbolOrNonNegativeIntegerIsExpected(secondToken.getPosition()),iX);
				position++;
			}
		}
	}
	//
	protected boolean pairNameCodeIsAlreadyDefined(long pairNameCode, boolean isSymbolicName, ArrayList<LabeledDomain> domains) {
		Iterator<LabeledDomain>	domainsIterator= domains.iterator();
		while (domainsIterator.hasNext()) {
			LabeledDomain element= domainsIterator.next();
			if (	element.hasSymbolicName()==isSymbolicName &&
				element.getIntegerKey()==pairNameCode) {
				return true;
			}
		};
		return false;
	}
	//
	protected ActorPrologDomainName variableToActorPrologDomainName(String name, int tokenPosition) {
		if (name.equals(anonymousVariableName)) {
			return new ActorPrologDomainAny(tokenPosition);
		} else {
			return new ActorPrologDomainName(name,tokenPosition);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE PREDICATE DECLARATIONS                              //
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologPredicateDeclaration[] parsePredicateDeclarations(boolean parseImperatives, ChoisePoint iX) throws SyntaxError {
		ArrayList<ActorPrologPredicateDeclaration> predicateDeclarationArray= new ArrayList<>();
		parsePredicateDeclarations(predicateDeclarationArray,parseImperatives,iX);
		return predicateDeclarationArray.toArray(new ActorPrologPredicateDeclaration[predicateDeclarationArray.size()]);
	}
	public void parsePredicateDeclarations(ArrayList<ActorPrologPredicateDeclaration> predicateDeclarationArray, boolean isTheImperativesSection, ChoisePoint iX) throws SyntaxError {
		if (isTheImperativesSection) {
			currentPredicateDeterminancy= ActorPrologPredicateDeterminancy.IMPERATIVE;
		} else {
			currentPredicateDeterminancy= ActorPrologPredicateDeterminancy.DETERMINISTIC;
		};
		currentPredicateVisibility= ActorPrologVisibility.UNDEFINED;
		boolean thisIsTheFirstDeclaration= true;
		PredicateDeclarationLoop: while (true) {
			if (position >= numberOfTokens) {
				break PredicateDeclarationLoop;
			};
			PrologToken firstToken= tokens[position];
			if (firstToken.isFinalToken()) {
				break PredicateDeclarationLoop;
			};
			int firstTokenPosition= firstToken.getPosition();
			PrologTokenType firstTokenType= firstToken.getType();
			switch (firstTokenType) {
			case KEYWORD:
///////////////////////////////////////////////////////////////////////
if (firstToken.isInQuotes(master,iX)) {
	break PredicateDeclarationLoop;
};
long firstTokenCode= firstToken.getSymbolCode(master,iX);
if (firstTokenCode==SymbolCodes.symbolCode_E_imperative) {
	position++;
	parseColon(iX);
	if (!isTheImperativesSection) {
		currentPredicateDeterminancy= ActorPrologPredicateDeterminancy.IMPERATIVE;
	} else {
		if (!robustMode) {
			master.handleError(new ThisDeclaratorIsNotAllowedInTheImperativesSection(firstToken),iX);
		}
	}
} else if (firstTokenCode==SymbolCodes.symbolCode_E_determ) {
	position++;
	parseColon(iX);
	if (!isTheImperativesSection) {
		currentPredicateDeterminancy= ActorPrologPredicateDeterminancy.DETERMINISTIC;
	} else {
		if (!robustMode) {
			master.handleError(new ThisDeclaratorIsNotAllowedInTheImperativesSection(firstToken),iX);
		}
	}
} else if (firstTokenCode==SymbolCodes.symbolCode_E_nondeterm) {
	position++;
	parseColon(iX);
	if (!isTheImperativesSection) {
		currentPredicateDeterminancy= ActorPrologPredicateDeterminancy.NONDETERMINISTIC;
	} else {
		if (!robustMode) {
			master.handleError(new ThisDeclaratorIsNotAllowedInTheImperativesSection(firstToken),iX);
		}
	}
} else if (firstTokenCode==SymbolCodes.symbolCode_E_visible) {
	position++;
	parseColon(iX);
	currentPredicateVisibility= ActorPrologVisibility.VISIBLE;
} else if (firstTokenCode==SymbolCodes.symbolCode_E_private) {
	position++;
	parseColon(iX);
	currentPredicateVisibility= ActorPrologVisibility.PRIVATE;
} else {
	break PredicateDeclarationLoop;
};
continue PredicateDeclarationLoop;
///////////////////////////////////////////////////////////////////////
			case R_SQUARE_BRACKET:
				break PredicateDeclarationLoop;
			};
			try {
				ActorPrologPredicateDeclaration declaration= parsePredicateDeclaration(iX);
				if (!robustMode) {
					checkWhetherPredicateIsGroupedProperly(predicateDeclarationArray,declaration,thisIsTheFirstDeclaration,iX);
				};
				predicateDeclarationArray.add(declaration);
				thisIsTheFirstDeclaration= false;
			} catch (ParserError error) {
				break PredicateDeclarationLoop;
			}
		}
	}
	//
	protected void checkWhetherPredicateIsGroupedProperly(ArrayList<ActorPrologPredicateDeclaration> array, ActorPrologPredicateDeclaration declaration1, boolean thisIsTheFirstDeclaration, ChoisePoint iX) throws ParserError {
		long nameCode1= declaration1.getNameCode();
		int arity1= declaration1.getArity();
		int p= declaration1.getPosition();
		Iterator<ActorPrologPredicateDeclaration> arrayIterator= array.iterator();
		if (thisIsTheFirstDeclaration) {
			while (arrayIterator.hasNext()) {
				ActorPrologPredicateDeclaration declaration2= arrayIterator.next();
				long nameCode2= declaration2.getNameCode();
				if (nameCode1==nameCode2) {
					master.handleError(new PredicatesWithTheSameNameShouldBeGrouped(p),iX);
				}
			}
		} else {
			boolean nameIsFound= false;
			boolean nameAndArityAreFound= false;
			while (arrayIterator.hasNext()) {
				ActorPrologPredicateDeclaration declaration2= arrayIterator.next();
				long nameCode2= declaration2.getNameCode();
				int arity2= declaration2.getArity();
				if (nameCode1==nameCode2) {
					nameIsFound= true;
					if (arity1==arity2) {
						nameAndArityAreFound= true;
					} else if (nameAndArityAreFound) {
						master.handleError(new PredicatesWithTheSameNameAndArityShouldBeGrouped(p),iX);
						break;
					}
				} else if (nameIsFound) {
					master.handleError(new PredicatesWithTheSameNameShouldBeGrouped(p),iX);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE PREDICATE DECLARATION                               //
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologPredicateDeclaration parseRootPredicateDeclaration(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		currentPredicateDeterminancy= ActorPrologPredicateDeterminancy.DETERMINISTIC;
		currentPredicateVisibility= ActorPrologVisibility.UNDEFINED;
		return parsePredicateDeclaration(iX);
	}
	public ActorPrologPredicateDeclaration parsePredicateDeclaration(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		int firstTokenPosition= firstToken.getPosition();
		PrologTokenType firstTokenType= firstToken.getType();
		switch (firstTokenType) {
		case SYMBOL:
			{
///////////////////////////////////////////////////////////////////////
position++;
long firstTokenCode= firstToken.getSymbolCode(master,iX);
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
boolean lastArgumentHasAsterisk= false;
boolean isFunctionDeclaration= false;
ActorPrologPredicateArgumentDeclaration[] arguments;
PrologToken secondToken= tokens[position];
int secondTokenPosition= secondToken.getPosition();
PrologTokenType secondTokenType= secondToken.getType();
ScanArgumentDeclaratrions: switch (secondTokenType) {
case SEMICOLON:
	position++;
	return new ActorPrologPredicateDeclaration(
		firstTokenCode,
		false,
		false,
		currentPredicateDeterminancy,
		emptyPredicateArgumentDeclarationArray,
		singleEmptyPredicateFlowPattern,
		currentPredicateVisibility,
		firstTokenPosition);
case L_ROUND_BRACKET:
	position++;
	if (position >= numberOfTokens) {
		throw master.handleUnexpectedEndOfTokenList(tokens,iX);
	};
	ArrayList<ActorPrologPredicateArgumentDeclaration> predicateArgumentDeclarationArray= new ArrayList<>();
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	if (thirdTokenType==PrologTokenType.R_ROUND_BRACKET) {
		position++;
	} else {
		ScanSingleArgumentDeclaration: while (true) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
ActorPrologArgumentDomain domain= parseArgumentDomain(iX);
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken fourthToken= tokens[position];
PrologTokenType fourthTokenType= fourthToken.getType();
switch (fourthTokenType) {
case COMMA:
	position++;
	predicateArgumentDeclarationArray.add(new ActorPrologPredicateArgumentDeclaration(domain,false));
	continue ScanSingleArgumentDeclaration;
case R_ROUND_BRACKET:
	position++;
	predicateArgumentDeclarationArray.add(new ActorPrologPredicateArgumentDeclaration(domain,false));
	break ScanSingleArgumentDeclaration;
case MULTIPLY:
	position++;
	lastArgumentHasAsterisk= true;
	predicateArgumentDeclarationArray.add(new ActorPrologPredicateArgumentDeclaration(domain,true));
	parseRightRoundBracket(iX);
	break ScanSingleArgumentDeclaration;
default:
	master.handleError(new CommaOrRightRoundBracketIsExpected(fourthToken.getPosition()),iX);
	position++;
	break ScanSingleArgumentDeclaration;
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		}
	};
	if (position < numberOfTokens) {
		PrologToken fifthToken= tokens[position];
		PrologTokenType fifthTokenType= fifthToken.getType();
		if (fifthTokenType==PrologTokenType.EQ) {
			position++;
			isFunctionDeclaration= true;
			ActorPrologArgumentDomain domain= parseArgumentDomain(iX);
			predicateArgumentDeclarationArray.add(0,new ActorPrologPredicateArgumentDeclaration(domain,false));
		}
	};
	arguments= predicateArgumentDeclarationArray.toArray(new ActorPrologPredicateArgumentDeclaration[predicateArgumentDeclarationArray.size()]);
	break ScanArgumentDeclaratrions;
case EQ:
	position++;
	isFunctionDeclaration= true;
	ActorPrologArgumentDomain domain= parseArgumentDomain(iX);
	arguments= new ActorPrologPredicateArgumentDeclaration[]{new ActorPrologPredicateArgumentDeclaration(domain,false)};
	break ScanArgumentDeclaratrions;
case MINUS:
	arguments= emptyPredicateArgumentDeclarationArray;
	break ScanArgumentDeclaratrions;
default:
	master.handleError(new SemicolonIsExpected(secondTokenPosition),iX);
	return new ActorPrologPredicateDeclaration(
		firstTokenCode,
		false,
		false,
		currentPredicateDeterminancy,
		emptyPredicateArgumentDeclarationArray,
		singleEmptyPredicateFlowPattern,
		currentPredicateVisibility,
		firstTokenPosition);
};
PrologToken sixthToken= tokens[position];
PrologTokenType sixthTokenType= sixthToken.getType();
switch (sixthTokenType) {
case MINUS:
	{
	position++;
	int arity= arguments.length;
	if (isFunctionDeclaration) {
		arity--;
	};
	ActorPrologPredicateFlowDirection[][] flowPatterns= parseFlowPatterns(arity,lastArgumentHasAsterisk,iX);
	return new ActorPrologPredicateDeclaration(
		firstTokenCode,
		lastArgumentHasAsterisk,
		isFunctionDeclaration,
		currentPredicateDeterminancy,
		arguments,
		flowPatterns,
		currentPredicateVisibility,
		firstTokenPosition);
	}
case SEMICOLON:
	{
	position++;
	int arity= arguments.length;
	if (isFunctionDeclaration) {
		arity--;
	};
	ActorPrologPredicateFlowDirection[] pattern= createDefaultPattern(arity,lastArgumentHasAsterisk);
	ActorPrologPredicateFlowDirection[][] flowPatterns= new ActorPrologPredicateFlowDirection[][]{pattern};
	return new ActorPrologPredicateDeclaration(
		firstTokenCode,
		lastArgumentHasAsterisk,
		isFunctionDeclaration,
		currentPredicateDeterminancy,
		arguments,
		flowPatterns,
		currentPredicateVisibility,
		firstTokenPosition);
	}
default:
	{
	master.handleError(new SemicolonIsExpected(secondTokenPosition),iX);
	int arity= arguments.length;
	if (isFunctionDeclaration) {
		arity--;
	};
	ActorPrologPredicateFlowDirection[] pattern= createDefaultPattern(arity,lastArgumentHasAsterisk);
	ActorPrologPredicateFlowDirection[][] flowPatterns= new ActorPrologPredicateFlowDirection[][]{pattern};
	return new ActorPrologPredicateDeclaration(
		firstTokenCode,
		lastArgumentHasAsterisk,
		isFunctionDeclaration,
		currentPredicateDeterminancy,
		arguments,
		flowPatterns,
		currentPredicateVisibility,
		firstTokenPosition);
	}
}
///////////////////////////////////////////////////////////////////////
			}
		case L_BRACE:
			{
///////////////////////////////////////////////////////////////////////
position++;
boolean lastArgumentHasAsterisk= false;
boolean isFunctionDeclaration= false;
ArrayList<ActorPrologPredicateArgumentDeclaration> predicateArgumentDeclarationArray= new ArrayList<>();
ActorPrologArgumentDomain argumentDomain= parseArgumentDomain(iX);
predicateArgumentDeclarationArray.add(0,new ActorPrologPredicateArgumentDeclaration(argumentDomain,false));
parseRightBrace(iX);
if (position < numberOfTokens) {
	PrologToken secondToken= tokens[position];
	PrologTokenType secondTokenType= secondToken.getType();
	if (secondTokenType==PrologTokenType.EQ) {
		position++;
		isFunctionDeclaration= true;
		ActorPrologArgumentDomain resultDomain= parseArgumentDomain(iX);
		predicateArgumentDeclarationArray.add(0,new ActorPrologPredicateArgumentDeclaration(resultDomain,false));
	}
};
ActorPrologPredicateArgumentDeclaration[] arguments= predicateArgumentDeclarationArray.toArray(new ActorPrologPredicateArgumentDeclaration[predicateArgumentDeclarationArray.size()]);
int arity= arguments.length;
if (isFunctionDeclaration) {
	arity--;
};
ActorPrologPredicateFlowDirection[][] flowPatterns;
if (position < numberOfTokens) {
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	if (thirdTokenType==PrologTokenType.MINUS) {
		position++;
		flowPatterns= parseFlowPatterns(arity,lastArgumentHasAsterisk,iX);
	} else {
		parseSemicolon(iX);
		ActorPrologPredicateFlowDirection[] pattern= createDefaultPattern(arity,lastArgumentHasAsterisk);
		flowPatterns= new ActorPrologPredicateFlowDirection[][]{pattern};
	}
} else {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
return new ActorPrologPredicateDeclaration(
	SymbolCodes.symbolCode_E_,
	lastArgumentHasAsterisk,
	isFunctionDeclaration,
	currentPredicateDeterminancy,
	arguments,
	flowPatterns,
	currentPredicateVisibility,
	firstTokenPosition);
///////////////////////////////////////////////////////////////////////
			}
		default:
			throw new SymbolIsExpected(firstTokenPosition);
		}
	}
	//
	protected ActorPrologPredicateFlowDirection[][] parseFlowPatterns(int arity, boolean lastArgumentHasAsterisk, ChoisePoint iX) throws SyntaxError {
		ArrayList<ActorPrologPredicateFlowDirection[]> flowPatternsArray= new ArrayList<>();
		ScanFlowPatterns: while (true) {
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			PrologToken seventhToken= tokens[position];
			PrologTokenType seventhTokenType= seventhToken.getType();
			switch (seventhTokenType) {
			case L_ROUND_BRACKET:
				position++;
				ActorPrologPredicateFlowDirection[] pattern= new ActorPrologPredicateFlowDirection[arity];
				int counter= 0;
				ScanSignlePattern: while (true) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
ActorPrologPredicateFlowDirection flowDirection;
PrologToken eighthToken= tokens[position];
PrologTokenType eighthTokenType= eighthToken.getType();
ScanSingleFlowDirection: switch (eighthTokenType) {
case SYMBOL:
	long eighthTokenCode= eighthToken.getSymbolCode(master,iX);
	if (eighthTokenCode==SymbolCodes.symbolCode_E_i) {
		position++;
		if (counter==arity-1 && lastArgumentHasAsterisk) {
			flowDirection= ActorPrologPredicateFlowDirection.LIST_IN;
		} else {
			flowDirection= ActorPrologPredicateFlowDirection.IN;
		}
	} else if (eighthTokenCode==SymbolCodes.symbolCode_E_o) {
		position++;
		if (counter==arity-1 && lastArgumentHasAsterisk) {
			flowDirection= ActorPrologPredicateFlowDirection.LIST_OUT;
		} else {
			flowDirection= ActorPrologPredicateFlowDirection.OUT;
		}
	} else {
		master.handleError(new FlowDirectionIsExpected(eighthToken.getPosition()),iX);
		if (counter==arity-1 && lastArgumentHasAsterisk) {
			flowDirection= ActorPrologPredicateFlowDirection.LIST_IN;
		} else {
			flowDirection= ActorPrologPredicateFlowDirection.IN;
		};
		position++;
	};
	break ScanSingleFlowDirection;
case VARIABLE:
	String name= eighthToken.getVariableName(master,iX);
	if (name.equals(anonymousVariableName)) {
		position++;
		if (counter==arity-1 && lastArgumentHasAsterisk) {
			flowDirection= ActorPrologPredicateFlowDirection.LIST_ANY;
		} else {
			flowDirection= ActorPrologPredicateFlowDirection.ANY;
		}
	} else {
		master.handleError(new FlowDirectionIsExpected(eighthToken.getPosition()),iX);
		if (counter==arity-1 && lastArgumentHasAsterisk) {
			flowDirection= ActorPrologPredicateFlowDirection.LIST_ANY;
		} else {
			flowDirection= ActorPrologPredicateFlowDirection.ANY;
		};
		position++;
	};
	break ScanSingleFlowDirection;
case R_ROUND_BRACKET:
	position++;
	if (counter > 0) {
		master.handleError(new FlowDirectionIsExpected(eighthToken.getPosition()),iX);
	};
	if (counter != arity) {
		master.handleError(new FlowPatternHasWrongArity(counter,arity,eighthToken.getPosition()),iX);
	};
	flowPatternsArray.add(pattern);
	break ScanSignlePattern;
default:
	master.handleError(new FlowDirectionIsExpected(eighthToken.getPosition()),iX);
	if (counter==arity-1 && lastArgumentHasAsterisk) {
		flowDirection= ActorPrologPredicateFlowDirection.LIST_IN;
	} else {
		flowDirection= ActorPrologPredicateFlowDirection.IN;
	};
	position++;
};
if (counter < arity) {
	pattern[counter]= flowDirection;
};
counter++;
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken ninthToken= tokens[position];
PrologTokenType ninthTokenType= ninthToken.getType();
switch (ninthTokenType) {
case COMMA:
	position++;
	continue ScanSignlePattern;
case R_ROUND_BRACKET:
	position++;
	if (counter != arity) {
		master.handleError(new FlowPatternHasWrongArity(counter,arity,ninthToken.getPosition()),iX);
	};
	flowPatternsArray.add(pattern);
	break ScanSignlePattern;
default:
	if (counter==arity) {
		master.handleError(new RightRoundBracketIsExpected(ninthToken.getPosition()),iX);
		position++;
		flowPatternsArray.add(pattern);
		break ScanSignlePattern;
	} else {
		master.handleError(new CommaIsExpected(ninthToken.getPosition()),iX);
		position++;
		continue ScanSignlePattern;
	}
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				};
				break;
			case SEMICOLON:
				{
				position++;
				ActorPrologPredicateFlowDirection[][] flowPatterns= flowPatternsArray.toArray(noEmptyPredicateFlowPatterns);
				return flowPatterns;
				}
			default:
				{
				master.handleError(new SemicolonIsExpected(seventhToken.getPosition()),iX);
				position++;
				ActorPrologPredicateFlowDirection[][] flowPatterns= flowPatternsArray.toArray(noEmptyPredicateFlowPatterns);
				return flowPatterns;
				}
			}
		}
	}
	//
	protected ActorPrologPredicateFlowDirection[] createDefaultPattern(int arity, boolean lastArgumentHasAsterisk) {
		ActorPrologPredicateFlowDirection[] pattern= new ActorPrologPredicateFlowDirection[arity];
		for (int k=0; k < arity; k++) {
			ActorPrologPredicateFlowDirection flowDirection;
			if (k==arity-1 && lastArgumentHasAsterisk) {
				flowDirection= ActorPrologPredicateFlowDirection.LIST_IN;
			} else {
				flowDirection= ActorPrologPredicateFlowDirection.IN;
			};
			pattern[k]= flowDirection;
		};
		return pattern;
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE INITIALIZER                                         //
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologInitializer parseRootInitializer(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		initializerIsParsed= true;
		try {
			ActorPrologInitializer initializer= parseInitializer(iX);
			checkFunctionVariables(ProhibitedFunctionCallContext.INITIALIZER,iX);
			return initializer;
		} finally {
			initializerIsParsed= false;
		}
	}
	//
	public ActorPrologInitializer parseInitializer(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		int firstTokenPosition= firstToken.getPosition();
		PrologTokenType firstTokenType= firstToken.getType();
		switch (firstTokenType) {
		case L_ROUND_BRACKET:
			{
///////////////////////////////////////////////////////////////////////
position++;
return parseConstructorOrResident(ConstructorParsingMode.CONSTRUCTOR_OR_RESIDENT,firstTokenPosition,iX);
///////////////////////////////////////////////////////////////////////
			}
		case SYMBOL:
			{
///////////////////////////////////////////////////////////////////////
long functorCode= firstToken.getSymbolCode(master,iX);
if (symbolIsASlot(functorCode)) {
	position++;
	return createActorPrologSlotOrResident(functorCode,firstTokenPosition,iX);
} else {
	int p2= position + 1;
	if (p2 >= numberOfTokens) {
		Term term= parseTerm(iX);
		return new ActorPrologTerm(term,firstTokenPosition);
	};
	PrologToken fifthToken= tokens[p2];
	PrologTokenType fifthTokenType= fifthToken.getType();
	if (fifthTokenType==PrologTokenType.L_SQUARE_BRACKET) {
		position= position + 2;
		checkWhetherClassNameIsEnclosedInApostrophes(firstToken,iX);
		Term ranges= parseArrayIndexRanges(iX);
		ActorPrologInitializer arrayItemConstructor= null;
		if (position < numberOfTokens) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
PrologToken sixthToken= tokens[position];
PrologTokenType sixthTokenType= sixthToken.getType();
if (sixthTokenType==PrologTokenType.KEYWORD) {
	long keywordCode= sixthToken.getSymbolCode(master,iX);
	if (keywordCode==SymbolCodes.symbolCode_E_of) {
		position++;
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		int arrayItemConstructorPosition= tokens[position].getPosition();
		parseLeftRoundBracket(iX);
		arrayItemConstructor= parseConstructorOrResident(ConstructorParsingMode.ARRAY_ELEMENT_PROTOTYPE,arrayItemConstructorPosition,iX);
	} else if (!robustMode) {
		master.handleError(new TheIfKeywordIsExpected(sixthToken.getPosition()),iX);
	}
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		};
		if (arrayItemConstructor==null) {
			arrayItemConstructor= new ActorPrologNoInitializer(firstTokenPosition);
		};
		return new ActorPrologArrayOfWorlds(functorCode,ranges,arrayItemConstructor,firstTokenPosition);
	} else {
		Term term= parseTerm(iX);
		return new ActorPrologTerm(term,firstTokenPosition);
	}
}
///////////////////////////////////////////////////////////////////////
			}
		case VARIABLE:
			{
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
if (p2 >= numberOfTokens) {
	Term term= parseTerm(iX);
	return new ActorPrologTerm(term,firstTokenPosition);
};
PrologToken seventhToken= tokens[p2];
PrologTokenType seventhTokenType= seventhToken.getType();
switch (seventhTokenType) {
case RESIDENT:
	position= position + 2;
	String variableName= firstToken.getVariableName(master,iX);
	Term termVariable= parseIsolatedVariable(variableName,firstTokenPosition,iX);
	ActorPrologInitializer initializer= new ActorPrologTerm(termVariable,firstTokenPosition);
	ActorPrologAtom atom= parseSimpleAtom(true,seventhToken.getPosition(),iX);
	checkResidentAtom(atom,iX);
	return new ActorPrologResident(initializer,atom);
default:
	Term term= parseTerm(iX);
	return new ActorPrologTerm(term,firstTokenPosition);
}
///////////////////////////////////////////////////////////////////////
			}
		case RESIDENT:
			{
			position++;
			ActorPrologSlot target= new ActorPrologSelf(firstTokenPosition);
			ActorPrologAtom atom= parseSimpleAtom(true,firstTokenPosition,iX);
			checkResidentAtom(atom,iX);
			return new ActorPrologResident(target,atom);
			}
		default:
			{
			Term term= parseTerm(iX);
			return new ActorPrologTerm(term,firstTokenPosition);
			}
		}
	}
	//
	public ActorPrologInitializer createActorPrologSlotOrResident(long nameCode, int p, ChoisePoint iX) throws SyntaxError {
		ActorPrologSlot target;
		if (nameCode==SymbolCodes.symbolCode_E_self) {
			target= new ActorPrologSelf(p);
		} else {
			target= new ActorPrologSlot(nameCode,p);
		};
		if (position >= numberOfTokens) {
			return target;
		};
		PrologToken firstToken= tokens[position];
		PrologTokenType firstTokenType= firstToken.getType();
		switch (firstTokenType) {
		case RESIDENT:
			position++;
			ActorPrologAtom atom= parseSimpleAtom(true,firstToken.getPosition(),iX);
			checkResidentAtom(atom,iX);
			return new ActorPrologResident(target,atom);
		default:
			return target;
		}
	}
	//
	protected Term parseArrayIndexRanges(ChoisePoint iX) throws SyntaxError {
		ArrayList<Term> rangeArray= new ArrayList<>();
		boolean previousTokenWasLeftSquareBracket= true;
		boolean previousTokenWasComma= false;
		RangesLoop: while (true) {
///////////////////////////////////////////////////////////////////////
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken firstToken= tokens[position];
int beginningOfRange= firstToken.getPosition();
PrologTokenType firstTokenType= firstToken.getType();
if (firstTokenType==PrologTokenType.R_SQUARE_BRACKET) {
	position++;
	if (previousTokenWasComma && !robustMode) {
		master.handleError(new ArrayIndexBoundIsExpected(firstToken.getPosition()),iX);
	};
	// previousTokenWasLeftSquareBracket= false;
	// previousTokenWasComma= false;
	break RangesLoop;
};
Term leftBound;
try {
	leftBound= parseArrayIndexBound(iX);
	previousTokenWasLeftSquareBracket= false;
	previousTokenWasComma= false;
} catch (NoIndexValueIsFound e) {
	leftBound= parseSymbol(SymbolCodes.symbolCode_E_none,beginningOfRange);
};
PrologToken secondToken= tokens[position];
PrologTokenType secondTokenType= secondToken.getType();
ScanDelimiter: switch (secondTokenType) {
case RANGE:
	{
	position++;
	previousTokenWasLeftSquareBracket= false;
	previousTokenWasComma= false;
	Term rightBound;
	try {
		rightBound= parseArrayIndexBound(iX);
		previousTokenWasLeftSquareBracket= false;
		previousTokenWasComma= false;
	} catch (NoIndexValueIsFound e) {
		rightBound= parseSymbol(SymbolCodes.symbolCode_E_none,secondToken.getPosition());
	};
	Term range= createMetaTermRange(leftBound,rightBound);
	rangeArray.add(range);
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	switch (thirdTokenType) {
	case COMMA:
		position++;
		if (previousTokenWasLeftSquareBracket && !robustMode) {
			master.handleError(new ArrayIndexBoundIsExpected(thirdToken.getPosition()),iX);
		} else if (previousTokenWasComma && !robustMode) {
			master.handleError(new ArrayIndexBoundIsExpected(thirdToken.getPosition()),iX);
		};
		previousTokenWasLeftSquareBracket= false;
		previousTokenWasComma= true;
		continue RangesLoop;
	case R_SQUARE_BRACKET:
		position++;
		if (previousTokenWasComma && !robustMode) {
			master.handleError(new ArrayIndexBoundIsExpected(thirdToken.getPosition()),iX);
		};
		// previousTokenWasLeftSquareBracket= false;
		// previousTokenWasComma= false;
		break RangesLoop;
	default:
		master.handleError(new RightSquareBracketIsExpected(thirdToken.getPosition()),iX);
		// previousTokenWasLeftSquareBracket= false;
		// previousTokenWasComma= false;
		break RangesLoop;
	}
	}
case COMMA:
	{
	position++;
	if (previousTokenWasLeftSquareBracket && !robustMode) {
		master.handleError(new ArrayIndexBoundIsExpected(secondToken.getPosition()),iX);
	} else if (previousTokenWasComma && !robustMode) {
		master.handleError(new ArrayIndexBoundIsExpected(secondToken.getPosition()),iX);
	};
	Term range= createMetaTermRange(createDefaultIndexBound(beginningOfRange),leftBound);
	rangeArray.add(range);
	previousTokenWasLeftSquareBracket= false;
	previousTokenWasComma= true;
	continue RangesLoop;
	}
case R_SQUARE_BRACKET:
	{
	position++;
	if (previousTokenWasComma && !robustMode) {
		master.handleError(new ArrayIndexBoundIsExpected(secondToken.getPosition()),iX);
	};
	Term range= createMetaTermRange(createDefaultIndexBound(beginningOfRange),leftBound);
	rangeArray.add(range);
	// previousTokenWasLeftSquareBracket= false;
	// previousTokenWasComma= false;
	break RangesLoop;
	}
default:
	{
	master.handleError(new RightSquareBracketIsExpected(secondToken.getPosition()),iX);
	Term range= createMetaTermRange(createDefaultIndexBound(beginningOfRange),leftBound);
	rangeArray.add(range);
	// previousTokenWasLeftSquareBracket= false;
	// previousTokenWasComma= false;
	break RangesLoop;
	}
}
///////////////////////////////////////////////////////////////////////
		};
		Term result= metaTermEmptyList;
		for (int k=rangeArray.size()-1; k >= 0; k--) {
			Term[] termArray= new Term[2];
			termArray[0]= rangeArray.get(k);
			termArray[1]= result;
			result= new PrologStructure(SymbolCodes.symbolCode_E_e,termArray);
		};
		return result;
	}
	//
	protected Term parseArrayIndexBound(ChoisePoint iX) throws SyntaxError, NoIndexValueIsFound {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		Term indexBound;
		boolean negateBound= false;
		PrologToken firstToken= tokens[position];
		PrologTokenType firstTokenType= firstToken.getType();
		ScanFirstToken: switch (firstTokenType) {
		case MINUS:
			position++;
			negateBound= true;
			break ScanFirstToken;
		case INTEGER:
			break ScanFirstToken;
		case SYMBOL:
			long functorCode= firstToken.getSymbolCode(master,iX);
			if (symbolIsASlot(functorCode)) {
				position++;
				return parseSlot(functorCode,firstToken.getPosition(),iX);
			} else if (functorCode==SymbolCodes.symbolCode_E_none) {
				position++;
				return parseSymbol(SymbolCodes.symbolCode_E_none,firstToken.getPosition());
			} else {
				master.handleError(new UndefinedSlotName(firstToken.getPosition()),iX);
				throw NoIndexValueIsFound.instance;
			}
		case RANGE:
		case COMMA:
		case R_SQUARE_BRACKET:
			throw NoIndexValueIsFound.instance;
		default:
			master.handleError(new AnIntegerIsExpected(firstToken.getPosition()),iX);
			throw NoIndexValueIsFound.instance;
		};
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken secondToken= tokens[position];
		PrologTokenType secondTokenType= secondToken.getType();
		if (!robustMode && secondToken.isExtendedNumber(master,iX)) {
			master.handleError(new MinusIsNotAllowedBeforeExtendedNumbers(secondToken.getPosition()),iX);
		};
		if (secondTokenType==PrologTokenType.INTEGER) {
			position++;
			return parseInteger(negateBound,secondToken,firstToken.getPosition(),iX);
		} else {
			master.handleError(new AnIntegerIsExpected(firstToken.getPosition()),iX);
			throw NoIndexValueIsFound.instance;
		}
	}
	//
	protected Term createDefaultIndexBound(int p) {
		Term defaultLeftBound= termOne;
		if (rememberTextPositions) {
			defaultLeftBound= attachTermPosition(defaultLeftBound,p);
		};
		return defaultLeftBound;
	}
	//
	protected Term createMetaTermRange(Term leftBound, Term rightBound) {
		Term range= metaTermEmptyList;
		Term[] termArray= new Term[2];
		termArray[0]= rightBound;
		termArray[1]= range;
		range= new PrologStructure(SymbolCodes.symbolCode_E_e,termArray);
		termArray= new Term[2];
		termArray[0]= leftBound;
		termArray[1]= range;
		range= new PrologStructure(SymbolCodes.symbolCode_E_e,termArray);
		return range;
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE CONSTRUCTOR                                         //
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologInitializer parseRootConstructor(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		initializerIsParsed= true;
		try {
			ActorPrologInitializer constructor= parseConstructor(iX);
			checkFunctionVariables(ProhibitedFunctionCallContext.INITIALIZER,iX);
			return constructor;
		} finally {
			initializerIsParsed= false;
		}
	}
	//
	public ActorPrologInitializer parseConstructor(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		int firstTokenPosition= firstToken.getPosition();
		PrologTokenType firstTokenType= firstToken.getType();
		switch (firstTokenType) {
		case L_ROUND_BRACKET:
			position++;
			return parseConstructorOrResident(ConstructorParsingMode.ANY_CONSTRUCTOR,firstTokenPosition,iX);
		default:
			master.handleError(new LeftRoundBracketIsExpected(firstTokenPosition),iX);
			throw new ConstructorIsExpected(firstTokenPosition);
		}
	}
	//
	public ActorPrologInitializer parseConstructorOrResident(ConstructorParsingMode mode, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		boolean isProcessConstructor= false;
		if (mode==ConstructorParsingMode.PROJECT_CONSTRUCTOR) {
			parseLeftRoundBracket(iX);
			isProcessConstructor= true;
		} else {
			if (position < numberOfTokens) {
				PrologToken firstToken= tokens[position];
				PrologTokenType firstTokenType= firstToken.getType();
				if (firstTokenType==PrologTokenType.L_ROUND_BRACKET) {
					position++;
					isProcessConstructor= true;
				}
			}
		};
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken secondToken= tokens[position];
		PrologTokenType secondTokenType= secondToken.getType();
		if (secondTokenType==PrologTokenType.SYMBOL) {
			position++;
			checkWhetherClassNameIsEnclosedInApostrophes(secondToken,iX);
			ActorPrologPortVariety currentArgumentPortVariety= ActorPrologPortVariety.UNRESTRICTED;
			boolean commaIsExpected= true;
			boolean argumentIsExpected= false;
			ArrayList<LabeledConstructorArgument> argumentArray= new ArrayList<>();
			ScanConstructorArguments: while (true) {
				if (position >= numberOfTokens) {
					throw master.handleUnexpectedEndOfTokenList(tokens,iX);
				};
				PrologToken thirdToken= tokens[position];
				PrologTokenType thirdTokenType= thirdToken.getType();
				ScanSingleConstructorArgument: switch (thirdTokenType) {
				case COMMA:
					{
///////////////////////////////////////////////////////////////////////
if (commaIsExpected) {
	position++;
	commaIsExpected= false;
	argumentIsExpected= true;
	continue ScanConstructorArguments;
} else {
	if (argumentIsExpected) {
		master.handleError(new ConstructorArgumentIsExpected(thirdToken.getPosition()),iX);
	} else {
		master.handleError(new RightRoundBracketIsExpected(thirdToken.getPosition()),iX);
	};
	position++;
	continue ScanConstructorArguments;
}
///////////////////////////////////////////////////////////////////////
					}
				case KEYWORD:
					{
///////////////////////////////////////////////////////////////////////
if (commaIsExpected) {
	master.handleError(new CommaIsExpected(thirdToken.getPosition()),iX);
	position++;
	continue ScanConstructorArguments;
};
if (!argumentIsExpected) {
	master.handleError(new RightRoundBracketIsExpected(thirdToken.getPosition()),iX);
	position++;
	continue ScanConstructorArguments;
};
position++;
long keywordCode= thirdToken.getSymbolCode(master,iX);
if (keywordCode==SymbolCodes.symbolCode_E_constant) {
	currentArgumentPortVariety= ActorPrologPortVariety.CONSTANT;
} else if (keywordCode==SymbolCodes.symbolCode_E_internal) {
	currentArgumentPortVariety= ActorPrologPortVariety.INTERNAL;
} else if (keywordCode==SymbolCodes.symbolCode_E_variable) {
	currentArgumentPortVariety= ActorPrologPortVariety.VARIABLE;
} else if (keywordCode==SymbolCodes.symbolCode_E_suspending) {
	currentArgumentPortVariety= ActorPrologPortVariety.SUSPENDING;
} else if (keywordCode==SymbolCodes.symbolCode_E_protecting) {
	currentArgumentPortVariety= ActorPrologPortVariety.PROTECTING;
} else {
	master.handleError(new PortVarietyIsExpected(thirdToken.getPosition()),iX);
};
parseColon(iX);
commaIsExpected= false;
argumentIsExpected= true;
continue ScanConstructorArguments;
///////////////////////////////////////////////////////////////////////
					}
				case R_ROUND_BRACKET:
					{
///////////////////////////////////////////////////////////////////////
if (argumentIsExpected) {
	master.handleError(new ConstructorArgumentIsExpected(thirdToken.getPosition()),iX);
};
position++;
if (isProcessConstructor) {
	parseRightRoundBracket(iX);
};
long nameCode= secondToken.getSymbolCode(master,iX);
LabeledConstructorArgument[] arguments= argumentArray.toArray(new LabeledConstructorArgument[argumentArray.size()]);
if (!robustMode) {
	for (int k=0; k < arguments.length; k++) {
		arguments[k].checkWhetherThereAreNoArrays(master,iX);
	}
};
switch (mode) {
case ARRAY_ELEMENT_PROTOTYPE:
	if (isProcessConstructor && !robustMode) {
		for (int k=0; k < arguments.length; k++) {
			arguments[k].checkWhetherThereAreNoSimpleConstructors(master,iX);
		}
	};
	return new ActorPrologConstructor(
		nameCode,
		arguments,
		isProcessConstructor,
		beginningOfTerm);
case ANY_CONSTRUCTOR:
case PROJECT_CONSTRUCTOR:
	return new ActorPrologConstructor(
		nameCode,
		arguments,
		isProcessConstructor,
		beginningOfTerm);
default:
	return createConstructorOrResident(
		nameCode,
		arguments,
		isProcessConstructor,
		beginningOfTerm,
		iX);
}
///////////////////////////////////////////////////////////////////////
					}
				default:
					{
///////////////////////////////////////////////////////////////////////
if (commaIsExpected) {
	master.handleError(new CommaIsExpected(thirdToken.getPosition()),iX);
	position++;
	continue ScanConstructorArguments;
};
if (argumentIsExpected) {
	LabeledConstructorArgument argument= parseConstructorArgument(currentArgumentPortVariety,iX);
	argumentArray.add(argument);
	commaIsExpected= true;
	argumentIsExpected= false;
	continue ScanConstructorArguments;
} else {
	master.handleError(new RightRoundBracketIsExpected(thirdToken.getPosition()),iX);
	position++;
	if (isProcessConstructor) {
		parseRightRoundBracket(iX);
	};
	long nameCode= secondToken.getSymbolCode(master,iX);
	LabeledConstructorArgument[] arguments= argumentArray.toArray(new LabeledConstructorArgument[argumentArray.size()]);
	if (!robustMode) {
		for (int k=0; k < arguments.length; k++) {
			arguments[k].checkWhetherThereAreNoArrays(master,iX);
		}
	};
	switch (mode) {
	case ARRAY_ELEMENT_PROTOTYPE:
		if (isProcessConstructor && !robustMode) {
			for (int k=0; k < arguments.length; k++) {
				arguments[k].checkWhetherThereAreNoSimpleConstructors(master,iX);
			}
		};
		return new ActorPrologConstructor(
			nameCode,
			arguments,
			isProcessConstructor,
			beginningOfTerm);
	case ANY_CONSTRUCTOR:
	case PROJECT_CONSTRUCTOR:
		return new ActorPrologConstructor(
			nameCode,
			arguments,
			isProcessConstructor,
			beginningOfTerm);
	default:
		return createConstructorOrResident(
			nameCode,
			arguments,
			isProcessConstructor,
			beginningOfTerm,
			iX);
	}
}
///////////////////////////////////////////////////////////////////////
					}
				}
			} // End of ScanConstructorArguments
		} else {
			master.handleError(new SymbolIsExpected(secondToken.getPosition()),iX);
			position++;
			return new ActorPrologNoInitializer(beginningOfTerm);
		}
	}
	//
	public LabeledConstructorArgument parseConstructorArgument(ActorPrologPortVariety portVariety, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		PrologTokenType firstTokenType= firstToken.getType();
		switch (firstTokenType) {
		case SYMBOL:
			position++;
			long argumentCode= firstToken.getSymbolCode(master,iX);
			if (position >= numberOfTokens) {
				ActorPrologInitializer argumentInitializer= createActorPrologSlotOrResident(argumentCode,firstToken.getPosition(),iX);
				checkWhetherSymbolIsASlot(firstToken,iX);
				return new LabeledConstructorArgument(argumentCode,argumentInitializer,portVariety,firstToken.getPosition());
			};
			PrologToken secondToken= tokens[position];
			PrologTokenType secondTokenType= secondToken.getType();
			if (secondTokenType==PrologTokenType.EQ) {
				position++;
				ActorPrologInitializer argumentInitializer= parseInitializer(iX);
				return new LabeledConstructorArgument(argumentCode,argumentInitializer,portVariety,firstToken.getPosition());
			} else {
				ActorPrologInitializer argumentInitializer= createActorPrologSlotOrResident(argumentCode,firstToken.getPosition(),iX);
				checkWhetherSymbolIsASlot(firstToken,iX);
				return new LabeledConstructorArgument(argumentCode,argumentInitializer,portVariety,firstToken.getPosition());
			}
		default:
			throw new SymbolIsExpected(firstToken.getPosition());
		}
	}
	//
	public ActorPrologInitializer createConstructorOrResident(long nameCode, LabeledConstructorArgument[] constructorArguments, boolean isProcessConstructor, int p, ChoisePoint iX) throws SyntaxError {
		ActorPrologConstructor constructor= new ActorPrologConstructor(nameCode,constructorArguments,isProcessConstructor,p);
		if (position >= numberOfTokens) {
			return constructor;
		};
		PrologToken firstToken= tokens[position];
		PrologTokenType firstTokenType= firstToken.getType();
		switch (firstTokenType) {
		case RESIDENT:
			position++;
			if (!isProcessConstructor && !robustMode) {
				master.handleError(new SimpleConstructorCannotBeATargetOfAResident(firstToken.getPosition()),iX);
			};
			ActorPrologAtom atom= parseSimpleAtom(true,firstToken.getPosition(),iX);
			checkResidentAtom(atom,iX);
			return new ActorPrologResident(constructor,atom);
		default:
			return constructor;
		}
	}
	//
	protected void checkResidentAtom(ActorPrologAtom atom, ChoisePoint iX) throws SyntaxError {
		if (robustMode) {
			return;
		};
		if (atom.lastArgumentHasAsterisk()) {
			int p= atom.getPositionOfLastArgument(iX);
			master.handleError(new AsteriskIsNotAllowedInResidents(p),iX);
		} else if (atom.metavariableIsMetaFunctor()) {
			master.handleError(new MetaFunctorsAreNotAllowedInResidents(atom.getAtomPosition()),iX);
		} else if (atom.metavariableIsMetaPredicate()) {
			master.handleError(new MetaPredicatesAreNotAllowedInResidents(atom.getAtomPosition()),iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE SLOT DECLARATIONS                                   //
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologSlotDeclaration[] parseRootSlotDeclarations(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		return parseAndCheckSlotDeclarations(iX);
	}
	//
	public ActorPrologSlotDeclaration[] parseAndCheckSlotDeclarations(ChoisePoint iX) throws SyntaxError {
		forgetVariableRoles();
		subgoalArray.clear();
		clearFunctionCallTable();
		initializerIsParsed= true;
		try {
			ActorPrologSlotDeclaration[] declarations= parseSlotDeclarations(iX);
			checkSingleVariables(iX);
			checkFunctionVariables(ProhibitedFunctionCallContext.INITIALIZER,iX);
			return declarations;
		} finally {
			initializerIsParsed= false;
		}
	}
	//
	public ActorPrologSlotDeclaration[] parseSlotDeclarations(ChoisePoint iX) throws SyntaxError {
		int initialPosition= position;
		slotNameHash.clear();
		SlotDeclarationsLoop: while (true) {
			if (position >= numberOfTokens) {
				break SlotDeclarationsLoop;
			};
			PrologToken firstToken= tokens[position];
			if (firstToken.isFinalToken()) {
				break SlotDeclarationsLoop;
			};
			int firstTokenPosition= firstToken.getPosition();
			PrologTokenType firstTokenType= firstToken.getType();
			ScanSlotDeclaration: switch (firstTokenType) {
			case SYMBOL:
				{
///////////////////////////////////////////////////////////////////////
position++;
long nameCode= firstToken.getSymbolCode(master,iX);
if (firstToken.isInQuotes(master,iX) && !robustMode) {
	master.handleError(new SlotNameCannotBeEnclosedInApostrophes(firstTokenPosition),iX);
};
if (slotNameHash.contains(nameCode) && !robustMode) {
	master.handleError(new SlotWithThisNameIsAlreadyDefined(firstTokenPosition),iX);
};
slotNameHash.add(nameCode);
while (true) {
	if (position >= numberOfTokens) {
		break SlotDeclarationsLoop;
	};
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	if (thirdTokenType==PrologTokenType.SEMICOLON) {
		position++;
		break ScanSlotDeclaration;
	};
	position++;
}
///////////////////////////////////////////////////////////////////////
				}
			case KEYWORD:
				position++;
				parseColon(iX);
				break ScanSlotDeclaration;
			default:
				break SlotDeclarationsLoop;
			}
		};
		position= initialPosition;
		ArrayList<ActorPrologSlotDeclaration> slotDeclarationArray= new ArrayList<>();
		currentSlotPortVariety= ActorPrologPortVariety.UNRESTRICTED;
		currentSlotVisibility= ActorPrologVisibility.UNDEFINED;
		SlotDeclarationsLoop: while (true) {
			ActorPrologArgumentDomain domain= null;
			if (position >= numberOfTokens) {
				break SlotDeclarationsLoop;
			};
			PrologToken secondToken= tokens[position];
			if (secondToken.isFinalToken()) {
				break SlotDeclarationsLoop;
			};
			int secondTokenPosition= secondToken.getPosition();
			PrologTokenType secondTokenType= secondToken.getType();
			ScanSlotDeclaration: switch (secondTokenType) {
			case SYMBOL:
				{
///////////////////////////////////////////////////////////////////////
position++;
long nameCode= secondToken.getSymbolCode(master,iX);
if (!slotNameHash.contains(nameCode)) {
	if (secondToken.isInQuotes(master,iX) && !robustMode) {
		master.handleError(new SlotNameCannotBeEnclosedInApostrophes(secondTokenPosition),iX);
	}
};
if (position >= numberOfTokens) {
	if (domain==null) {
		domain= new ActorPrologDomainNone(secondTokenPosition);
	};
	slotDeclarationArray.add(new ActorPrologSlotDeclaration(
		nameCode,
		currentSlotPortVariety,
		domain,
		currentSlotVisibility,
		secondTokenPosition));
	break SlotDeclarationsLoop;
};
PrologToken thirdToken= tokens[position];
PrologTokenType thirdTokenType= thirdToken.getType();
if (thirdTokenType==PrologTokenType.COLON) {
	position++;
	domain= parseArgumentDomain(iX);
};
if (domain==null) {
	domain= new ActorPrologDomainNone(secondTokenPosition);
};
slotDeclarationArray.add(new ActorPrologSlotDeclaration(
	nameCode,
	currentSlotPortVariety,
	domain,
	currentSlotVisibility,
	secondTokenPosition));
parseSemicolon(iX);
///////////////////////////////////////////////////////////////////////
				};
				break ScanSlotDeclaration;
			case KEYWORD:
				{
///////////////////////////////////////////////////////////////////////
position++;
long nameCode= secondToken.getSymbolCode(master,iX);
if (nameCode==SymbolCodes.symbolCode_E_visible) {
	currentSlotVisibility= ActorPrologVisibility.VISIBLE;
} else if (nameCode==SymbolCodes.symbolCode_E_private) {
	currentSlotVisibility= ActorPrologVisibility.PRIVATE;
} else if (nameCode==SymbolCodes.symbolCode_E_constant) {
	currentSlotPortVariety= ActorPrologPortVariety.CONSTANT;
} else if (nameCode==SymbolCodes.symbolCode_E_internal) {
	currentSlotPortVariety= ActorPrologPortVariety.INTERNAL;
} else if (nameCode==SymbolCodes.symbolCode_E_variable) {
	currentSlotPortVariety= ActorPrologPortVariety.VARIABLE;
} else if (nameCode==SymbolCodes.symbolCode_E_suspending) {
	currentSlotPortVariety= ActorPrologPortVariety.SUSPENDING;
} else if (nameCode==SymbolCodes.symbolCode_E_protecting) {
	currentSlotPortVariety= ActorPrologPortVariety.PROTECTING;
} else {
	master.handleError(new IncorrectKeyword(secondToken),iX);
};
parseColon(iX);
///////////////////////////////////////////////////////////////////////
				};
				break ScanSlotDeclaration;
			default:
				break SlotDeclarationsLoop;
			}
		};
		return slotDeclarationArray.toArray(new ActorPrologSlotDeclaration[slotDeclarationArray.size()]);
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE SLOT DEFINITIONS                                    //
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologSlotDefinition[] parseRootSlotDefinitions(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		return parseAndCheckSlotDefinitions(iX);
	}
	//
	public ActorPrologSlotDefinition[] parseAndCheckSlotDefinitions(ChoisePoint iX) throws SyntaxError {
		forgetVariableRoles();
		subgoalArray.clear();
		clearFunctionCallTable();
		slotDefinitionsContainDomains= false;
		initializerIsParsed= true;
		try {
			ActorPrologSlotDefinition[] definitions= parseSlotDefinitions(iX);
			checkSingleVariables(iX);
			checkFunctionVariables(ProhibitedFunctionCallContext.INITIALIZER,iX);
			return definitions;
		} finally {
			initializerIsParsed= false;
		}
	}
	//
	public ActorPrologSlotDefinition[] parseSlotDefinitions(ChoisePoint iX) throws SyntaxError {
		int initialPosition= position;
		slotNameHash.clear();
		SlotDefinitionsLoop: while (true) {
			if (position >= numberOfTokens) {
				break SlotDefinitionsLoop;
			};
			PrologToken firstToken= tokens[position];
			if (firstToken.isFinalToken()) {
				break SlotDefinitionsLoop;
			};
			int firstTokenPosition= firstToken.getPosition();
			PrologTokenType firstTokenType= firstToken.getType();
			ScanSlotDefinition: switch (firstTokenType) {
			case SYMBOL:
				{
///////////////////////////////////////////////////////////////////////
position++;
long nameCode= firstToken.getSymbolCode(master,iX);
if (firstToken.isInQuotes(master,iX) && !robustMode) {
	master.handleError(new SlotNameCannotBeEnclosedInApostrophes(firstTokenPosition),iX);
};
if (slotNameHash.contains(nameCode) && !robustMode) {
	master.handleError(new SlotWithThisNameIsAlreadyDefined(firstTokenPosition),iX);
};
slotNameHash.add(nameCode);
while (true) {
	if (position >= numberOfTokens) {
		break SlotDefinitionsLoop;
	};
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	if (thirdTokenType==PrologTokenType.SEMICOLON) {
		position++;
		break ScanSlotDefinition;
	};
	position++;
}
///////////////////////////////////////////////////////////////////////
				}
			case KEYWORD:
				position++;
				parseColon(iX);
				break ScanSlotDefinition;
			default:
				break SlotDefinitionsLoop;
			}
		};
		position= initialPosition;
		ArrayList<ActorPrologSlotDefinition> slotDefinitionArray= new ArrayList<>();
		ArrayList<ActorPrologArgumentDomain> domainArray= new ArrayList<>();
		currentSlotPortVariety= ActorPrologPortVariety.UNRESTRICTED;
		currentSlotVisibility= ActorPrologVisibility.UNDEFINED;
		SlotDefinitionsLoop: while (true) {
			ActorPrologArgumentDomain domain= null;
			if (position >= numberOfTokens) {
				break SlotDefinitionsLoop;
			};
			PrologToken secondToken= tokens[position];
			if (secondToken.isFinalToken()) {
				break SlotDefinitionsLoop;
			};
			int secondTokenPosition= secondToken.getPosition();
			PrologTokenType secondTokenType= secondToken.getType();
			ScanSlotDefinition: switch (secondTokenType) {
			case SYMBOL:
				{
///////////////////////////////////////////////////////////////////////
position++;
long nameCode= secondToken.getSymbolCode(master,iX);
if (!slotNameHash.contains(nameCode)) {
	if (secondToken.isInQuotes(master,iX) && !robustMode) {
		master.handleError(new SlotNameCannotBeEnclosedInApostrophes(secondTokenPosition),iX);
	}
};
if (position >= numberOfTokens) {
	slotDefinitionArray.add(new ActorPrologSlotDefinition(
		nameCode,
		currentSlotPortVariety,
		new ActorPrologNoInitializer(secondTokenPosition),
		currentSlotVisibility,
		secondTokenPosition));
	if (domain==null) {
		domain= new ActorPrologDomainNone(secondTokenPosition);
	} else {
		slotDefinitionsContainDomains= true;
	};
	domainArray.add(domain);
	break SlotDefinitionsLoop;
};
PrologToken thirdToken= tokens[position];
PrologTokenType thirdTokenType= thirdToken.getType();
if (thirdTokenType==PrologTokenType.COLON) {
	position++;
	domain= parseArgumentDomain(iX);
	slotDefinitionsContainDomains= true;
};
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken fourthToken= tokens[position];
PrologTokenType fourthTokenType= fourthToken.getType();
if (fourthTokenType==PrologTokenType.EQ) {
	position++;
	ActorPrologInitializer initializer= parseInitializer(iX);
	slotDefinitionArray.add(new ActorPrologSlotDefinition(
		nameCode,
		currentSlotPortVariety,
		initializer,
		currentSlotVisibility,
		secondTokenPosition));
	if (domain==null) {
		domain= new ActorPrologDomainNone(secondTokenPosition);
	} else {
		slotDefinitionsContainDomains= true;
	};
	domainArray.add(domain);
} else {
	slotDefinitionArray.add(new ActorPrologSlotDefinition(
		nameCode,
		currentSlotPortVariety,
		new ActorPrologNoInitializer(secondTokenPosition),
		currentSlotVisibility,
		secondTokenPosition));
	if (domain==null) {
		domain= new ActorPrologDomainNone(secondTokenPosition);
	} else {
		slotDefinitionsContainDomains= true;
	};
	domainArray.add(domain);
};
parseSemicolon(iX);
///////////////////////////////////////////////////////////////////////
				};
				break ScanSlotDefinition;
			case KEYWORD:
				{
///////////////////////////////////////////////////////////////////////
position++;
long nameCode= secondToken.getSymbolCode(master,iX);
if (nameCode==SymbolCodes.symbolCode_E_visible) {
	currentSlotVisibility= ActorPrologVisibility.VISIBLE;
} else if (nameCode==SymbolCodes.symbolCode_E_private) {
	currentSlotVisibility= ActorPrologVisibility.PRIVATE;
} else if (nameCode==SymbolCodes.symbolCode_E_constant) {
	currentSlotPortVariety= ActorPrologPortVariety.CONSTANT;
} else if (nameCode==SymbolCodes.symbolCode_E_internal) {
	currentSlotPortVariety= ActorPrologPortVariety.INTERNAL;
} else if (nameCode==SymbolCodes.symbolCode_E_variable) {
	currentSlotPortVariety= ActorPrologPortVariety.VARIABLE;
} else if (nameCode==SymbolCodes.symbolCode_E_suspending) {
	currentSlotPortVariety= ActorPrologPortVariety.SUSPENDING;
} else if (nameCode==SymbolCodes.symbolCode_E_protecting) {
	currentSlotPortVariety= ActorPrologPortVariety.PROTECTING;
} else {
	master.handleError(new IncorrectKeyword(secondToken),iX);
};
parseColon(iX);
///////////////////////////////////////////////////////////////////////
				};
				break ScanSlotDefinition;
			default:
				break SlotDefinitionsLoop;
			}
		};
		slotDomains= domainArray.toArray(new ActorPrologArgumentDomain[domainArray.size()]);
		return slotDefinitionArray.toArray(new ActorPrologSlotDefinition[slotDefinitionArray.size()]);
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE INTERFACE                                           //
	///////////////////////////////////////////////////////////////
	//
	public void parseInterface(ChoisePoint iX) throws SyntaxError {
		resetClassAttributes();
		isMetaInterface= parseTheInterfaceOrMetainterfaceKeyword(iX);
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		int classNamePosition= tokens[position].getPosition();
		try {
			classNameCode= parseClassName(iX);
		} catch (NoClassNameIsFound e) {
			master.handleError(new NoInterfaceIsFound(classNamePosition),iX);
			position++;
		};
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		int firstTokenPosition= firstToken.getPosition();
		PrologTokenType firstTokenType= firstToken.getType();
		if (firstTokenType==PrologTokenType.L_ROUND_BRACKET) {
			position++;
			parseTheSpecializedKeyword(iX);
			ArrayList<Long> ancestorNameCodeArray= new ArrayList<>();
			ScanAncestors: while (true) {
///////////////////////////////////////////////////////////////////////
try {
	long nameCode= parseClassName(iX);
	ancestorNameCodeArray.add(nameCode);
} catch (NoClassNameIsFound e) {
	master.handleError(new NoInterfaceIsFound(firstToken.getPosition()),iX);
	position++;
};
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken secondToken= tokens[position];
int secondTokenPosition= secondToken.getPosition();
PrologTokenType secondTokenType= secondToken.getType();
switch (secondTokenType) {
case COMMA:
	position++;
	continue ScanAncestors;
case R_ROUND_BRACKET:
	position++;
	break ScanAncestors;
default:
	master.handleError(new CommaOrRightRoundBracketIsExpected(secondToken.getPosition()),iX);
	position++;
	break ScanAncestors;
}
///////////////////////////////////////////////////////////////////////
			};
			int numberOfAncestors= ancestorNameCodeArray.size();
			ancestorNameCodes= new long[numberOfAncestors];
			for (int k=0; k < numberOfAncestors; k++) {
				ancestorNameCodes[k]= ancestorNameCodeArray.get(k);
			}
		};
		parseColon(iX);
		slotDeclarations= parseSlotDeclarations(iX);
		parseLeftSquareBracket(iX);
		ArrayList<ActorPrologDomainDefinition> domainDefinitionArray= new ArrayList<>();
		ArrayList<ActorPrologPredicateDeclaration> predicateDeclarationArray= new ArrayList<>();
		boolean isBeginningOfSection= true;
		ScanInterfaceSections: while (true) {
///////////////////////////////////////////////////////////////////////
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken secondToken= tokens[position];
PrologTokenType secondTokenType= secondToken.getType();
switch (secondTokenType) {
case KEYWORD:
	long secondTokenCode= secondToken.getSymbolCode(master,iX);
	if (secondTokenCode==SymbolCodes.symbolCode_E_DOMAINS) {
		position++;
		isBeginningOfSection= false;
		parseColon(iX);
		parseDomainDefinitions(domainDefinitionArray,iX);
	} else if (secondTokenCode==SymbolCodes.symbolCode_E_PREDICATES) {
		position++;
		isBeginningOfSection= false;
		parseColon(iX);
		parsePredicateDeclarations(predicateDeclarationArray,false,iX);
	} else if (secondTokenCode==SymbolCodes.symbolCode_E_IMPERATIVES) {
		position++;
		isBeginningOfSection= false;
		parseColon(iX);
		parsePredicateDeclarations(predicateDeclarationArray,true,iX);
	} else {
		if (isBeginningOfSection) {
			parsePredicateDeclarations(predicateDeclarationArray,false,iX);
			isBeginningOfSection= false;
			continue ScanInterfaceSections;
		} else {
			// isBeginningOfSection= false;
			master.handleError(new RightSquareBracketIsExpected(secondToken.getPosition()),iX);
			position++;
			break ScanInterfaceSections;
		}
	};
	continue ScanInterfaceSections;
case R_SQUARE_BRACKET:
	position++;
	// isBeginningOfSection= false;
	break ScanInterfaceSections;
default:
	if (isBeginningOfSection) {
		parsePredicateDeclarations(predicateDeclarationArray,false,iX);
		isBeginningOfSection= false;
		continue ScanInterfaceSections;
	} else {
		// isBeginningOfSection= false;
		master.handleError(new RightSquareBracketIsExpected(secondToken.getPosition()),iX);
		position++;
		break ScanInterfaceSections;
	}
}
///////////////////////////////////////////////////////////////////////
		};
		domainDefinitions= domainDefinitionArray.toArray(new ActorPrologDomainDefinition[domainDefinitionArray.size()]);
		predicateDeclarations= predicateDeclarationArray.toArray(new ActorPrologPredicateDeclaration[predicateDeclarationArray.size()]);
	}
	//
	protected void resetClassAttributes() {
		setUseSecondVariableNameRegister(true);
		forgetParsedVariableNames();
		subgoalArray.clear();
		clearFunctionCallTable();
		slotNameHash.clear();
		classNameCode= -1;
		ancestorNameCode= -1;
		ancestorNameCodes= emptyLongArray;
		isMetaInterface= false;
		slotDeclarations= emptySlotDeclarationArray;
		slotDefinitions= emptySlotDefinitionArray;
		slotDefinitionsContainDomains= false;
		slotDomains= emptyArgumentDomainArray;
		actingClauses= emptyClauses;
		modelClauses= emptyClauses;
		domainDefinitions= emptyDomainDefinitionArray;
		predicateDeclarations= emptyPredicateDeclarationArray;
		classSource= null;
		classSourcePosition= -1;
		interfaceDefinition= null;
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE CLASS                                               //
	///////////////////////////////////////////////////////////////
	//
	public void parseClass(ChoisePoint iX) throws SyntaxError {
		resetClassAttributes();
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		int classPosition= tokens[position].getPosition();
		parseTheClassKeyword(iX);
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		int classNamePosition= tokens[position].getPosition();
		try {
			classNameCode= parseClassName(iX);
		} catch (NoClassNameIsFound e) {
			master.handleError(new NoClassIsFound(classNamePosition),iX);
			position++;
		};
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		int firstTokenPosition= firstToken.getPosition();
		PrologTokenType firstTokenType= firstToken.getType();
		if (firstTokenType==PrologTokenType.L_ROUND_BRACKET) {
			position++;
			parseTheSpecializedKeyword(iX);
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			int ancestorNamePosition= tokens[position].getPosition();
			try {
				ancestorNameCode= parseClassName(iX);
			} catch (NoClassNameIsFound e) {
				master.handleError(new ClassNameIsExpected(ancestorNamePosition),iX);
				position++;
			};
			parseRightRoundBracket(iX);
		};
		parseColon(iX);
		slotDefinitions= parseAndCheckSlotDefinitions(iX);
		parseLeftSquareBracket(iX);
		ArrayList<ActorPrologClause> actingClauseArray= new ArrayList<>();
		ArrayList<ActorPrologClause> modelClauseArray= new ArrayList<>();
		ArrayList<ActorPrologDomainDefinition> domainDefinitionArray= new ArrayList<>();
		ArrayList<ActorPrologPredicateDeclaration> predicateDeclarationArray= new ArrayList<>();
		boolean thereAreInterfaceSections= false;
		boolean isBeginningOfSection= true;
		ScanClassSections: while (true) {
///////////////////////////////////////////////////////////////////////
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken secondToken= tokens[position];
int secondTokenPosition= secondToken.getPosition();
PrologTokenType secondTokenType= secondToken.getType();
switch (secondTokenType) {
case KEYWORD:
	isBeginningOfSection= false;
	long secondTokenCode= secondToken.getSymbolCode(master,iX);
	if (secondTokenCode==SymbolCodes.symbolCode_E_CLAUSES) {
		position++;
		parseColon(iX);
		parseClauses(actingClauseArray,false,iX);
	} else if (secondTokenCode==SymbolCodes.symbolCode_E_MODEL) {
		position++;
		parseColon(iX);
		parseClauses(modelClauseArray,true,iX);
	} else if (secondTokenCode==SymbolCodes.symbolCode_E_SOURCE) {
		position++;
		parseColon(iX);
		parseSource(secondToken.getPosition(),iX);
	} else if (secondTokenCode==SymbolCodes.symbolCode_E_DOMAINS) {
		position++;
		parseColon(iX);
		thereAreInterfaceSections= true;
		parseDomainDefinitions(domainDefinitionArray,iX);
	} else if (secondTokenCode==SymbolCodes.symbolCode_E_PREDICATES) {
		position++;
		parseColon(iX);
		thereAreInterfaceSections= true;
		parsePredicateDeclarations(predicateDeclarationArray,false,iX);
	} else if (secondTokenCode==SymbolCodes.symbolCode_E_IMPERATIVES) {
		position++;
		parseColon(iX);
		thereAreInterfaceSections= true;
		parsePredicateDeclarations(predicateDeclarationArray,true,iX);
	} else {
		master.handleError(new TheClausesKeywordIsExpected(secondToken.getPosition()),iX);
		position++;
	};
	continue ScanClassSections;
case R_SQUARE_BRACKET:
	position++;
	// isBeginningOfSection= false;
	break ScanClassSections;
default:
	if (isBeginningOfSection) {
		parseClauses(actingClauseArray,false,iX);
		isBeginningOfSection= false;
		continue ScanClassSections;
	} else {
		// isBeginningOfSection= false;
		master.handleError(new RightSquareBracketIsExpected(secondToken.getPosition()),iX);
		position++;
		break ScanClassSections;
	}
}
///////////////////////////////////////////////////////////////////////
		};
		actingClauses= actingClauseArray.toArray(new ActorPrologClause[actingClauseArray.size()]);
		modelClauses= modelClauseArray.toArray(new ActorPrologClause[modelClauseArray.size()]);
		if (slotDefinitionsContainDomains || thereAreInterfaceSections) {
			if (ancestorNameCode < 0) {
				ancestorNameCodes= emptyLongArray;
			} else {
				ancestorNameCodes= new long[]{ancestorNameCode};
			};
			domainDefinitions= domainDefinitionArray.toArray(new ActorPrologDomainDefinition[domainDefinitionArray.size()]);
			predicateDeclarations= predicateDeclarationArray.toArray(new ActorPrologPredicateDeclaration[predicateDeclarationArray.size()]);
			int numberOfSlots= slotDefinitions.length;
			slotDeclarations= new ActorPrologSlotDeclaration[numberOfSlots];
			for (int k=0; k < numberOfSlots; k++) {
				slotDeclarations[k]= slotDefinitions[k].toSlotDeclaration(slotDomains[k]);
			};
			interfaceDefinition= new ActorPrologInterface(
				classNameCode,
				ancestorNameCodes,
				isMetaInterface,
				slotDeclarations,
				domainDefinitions,
				predicateDeclarations,
				classPosition);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE SOURCE                                              //
	///////////////////////////////////////////////////////////////
	//
	protected void parseSource(int beginningOfSection, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		if (classSource!=null) {
			master.handleError(new TheSourceSectionIsAlreadyDefinedInTheClass(beginningOfSection),iX);
		};
		PrologToken firstToken= tokens[position];
		int firstTokenPosition= firstToken.getPosition();
		PrologTokenType firstTokenType= firstToken.getType();
		if (firstTokenType==PrologTokenType.STRING_SEGMENT) {
			position++;
			classSource= parseString(firstToken.getStringValue(master,iX),iX);
			classSourcePosition= firstTokenPosition;
			parseSemicolon(iX);
		} else {
			master.handleError(new StringIsExpected(firstTokenPosition),iX);
			position++;
		}
	}
}
