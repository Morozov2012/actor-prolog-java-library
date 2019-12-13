// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax;

import target.*;

import morozov.run.*;
import morozov.syntax.data.*;
import morozov.syntax.data.subgoals.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.syntax.scanner.*;
import morozov.syntax.scanner.errors.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.ArrayList;

public class MetaTermParser extends ElementaryParser {
	//
	protected boolean useSecondVariableNameRegister= false;
	//
	protected HashMap<String,Integer> firstVariableNameRegister= new HashMap<>();
	protected HashMap<String,Integer> secondVariableNameRegister= new HashMap<>();
	//
	protected HashMap<Integer,String> firstInvertedVariableNameRegister= new HashMap<>();
	protected HashMap<Integer,String> secondInvertedVariableNameRegister= new HashMap<>();
	//
	protected int firstRecentVariableNumber= 0;
	protected int secondRecentVariableNumber= 0;
	//
	protected HashMap<Integer,VariableRole> firstVariableRoleRegister= new HashMap<>();
	protected HashMap<Integer,VariableRole> secondVariableRoleRegister= new HashMap<>();
	//
	protected ArrayList<Integer> firstAnonymousVariableRegister= new ArrayList<>();
	protected ArrayList<Integer> secondAnonymousVariableRegister= new ArrayList<>();
	//
	protected ArrayList<FunctionCallDefinition> firstFunctionCallDefinitionArray= new ArrayList<>();
	protected ArrayList<FunctionCallDefinition> secondFunctionCallDefinitionArray= new ArrayList<>();
	//
	protected static final String anonymousVariableName= "_";
	protected static final int anonymousVariableNumber= 0;
	protected static final int noVariableNumber= -1;
	//
	protected static final Term metaTermEmptyList= new PrologSymbol(SymbolCodes.symbolCode_E_empty_list);
	protected static final Term metaTermEmptySet= new PrologSymbol(SymbolCodes.symbolCode_E_empty_set);
	protected static final Term metaTermAnonymousVariable= new PrologStructure(SymbolCodes.symbolCode_E_var,new Term[]{new PrologInteger(anonymousVariableNumber),new PrologSymbol(SymbolCodes.symbolCode_E_plain)});
	//
	protected static final String auxiliaryVariableNameFormat= "_V%d";
	//
	///////////////////////////////////////////////////////////////
	//
	public MetaTermParser(ParserMasterInterface m, boolean rememberPositions) {
		super(m,rememberPositions);
	}
	public MetaTermParser(ParserMasterInterface m, boolean rememberPositions, boolean implementRobustMode) {
		super(m,rememberPositions,implementRobustMode);
	}
	public MetaTermParser(ParserMasterInterface m) {
		super(m);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setUseSecondVariableNameRegister(boolean mode) {
		useSecondVariableNameRegister= mode;
	}
	public boolean useSecondVariableNameRegister() {
		return useSecondVariableNameRegister;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public HashMap<String,Integer> getFirstVariableNameRegister() {
		return firstVariableNameRegister;
	}
	public HashMap<String,Integer> getSecondVariableNameRegister() {
		return secondVariableNameRegister;
	}
	//
	public String[] getParsedVariableNames() {
		String[] names;
		int counter= 0;
		if (useSecondVariableNameRegister) {
			names= new String[secondRecentVariableNumber-anonymousVariableNumber];
			for (int k=anonymousVariableNumber+1; k <= secondRecentVariableNumber; k++) {
				String name= secondInvertedVariableNameRegister.get(k);
				if (name==null) {
					name= "";
				};
				names[counter++]= name;
			}
		} else {
			names= new String[firstRecentVariableNumber-anonymousVariableNumber];
			for (int k=anonymousVariableNumber+1; k <= firstRecentVariableNumber; k++) {
				String name= firstInvertedVariableNameRegister.get(k);
				if (name==null) {
					name= "";
				};
				names[counter++]= name;
			}
		};
		return names;
	}
	//
	public Term getTermParsedVariableNames() {
		String[] names= getParsedVariableNames();
		BalancedNameTreeNode tree= GeneralConverters.stringArrayToBalancedNameTree(names);
		return tree.toTerm();
	}
	//
	public void forgetFirstAndSecondParsedVariableNames() {
		setUseSecondVariableNameRegister(false);
		forgetParsedVariableNames();
		setUseSecondVariableNameRegister(true);
		forgetParsedVariableNames();
	}
	//
	public void forgetParsedVariableNames() {
		if (useSecondVariableNameRegister) {
			secondVariableNameRegister.clear();
			secondInvertedVariableNameRegister.clear();
			secondRecentVariableNumber= 0;
		} else {
			firstVariableNameRegister.clear();
			firstInvertedVariableNameRegister.clear();
			firstRecentVariableNumber= 0;
		};
		forgetVariableRoles();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public HashMap<Integer,String> getFirstInvertedVariableNameRegister() {
		return firstInvertedVariableNameRegister;
	}
	public HashMap<Integer,String> getSecondInvertedVariableNameRegister() {
		return secondInvertedVariableNameRegister;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setFirstRecentVariableNumber(int value) {
		firstRecentVariableNumber= value;
	}
	public int getFirstRecentVariableNumber() {
		return firstRecentVariableNumber;
	}
	//
	public void setSecondRecentVariableNumber(int value) {
		secondRecentVariableNumber= value;
	}
	public int getSecondRecentVariableNumber() {
		return secondRecentVariableNumber;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public HashMap<Integer,VariableRole> getFirstVariableRoleRegister() {
		return firstVariableRoleRegister;
	}
	public HashMap<Integer,VariableRole> getSecondVariableRoleRegister() {
		return secondVariableRoleRegister;
	}
	//
	public void forgetVariableRoles() {
		if (useSecondVariableNameRegister) {
			secondAnonymousVariableRegister.clear();
			secondVariableRoleRegister.clear();
		} else {
			firstAnonymousVariableRegister.clear();
			firstVariableRoleRegister.clear();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public ArrayList<Integer> getFirstAnonymousVariableRegister() {
		return firstAnonymousVariableRegister;
	}
	public ArrayList<Integer> getSecondAnonymousVariableRegister() {
		return secondAnonymousVariableRegister;
	}
	//
	public int getFirstAnonymousVariableRegisterSize() {
		return firstAnonymousVariableRegister.size();
	}
	public int getSecondAnonymousVariableRegisterSize() {
		return secondAnonymousVariableRegister.size();
	}
	//
	public void clearAnonymousVariableRegister() {
		if (useSecondVariableNameRegister) {
			secondAnonymousVariableRegister.clear();
		} else {
			firstAnonymousVariableRegister.clear();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public ArrayList<FunctionCallDefinition> getFirstFunctionCallDefinitions() {
		return firstFunctionCallDefinitionArray;
	}
	public ArrayList<FunctionCallDefinition> getSecondFunctionCallDefinitions() {
		return secondFunctionCallDefinitionArray;
	}
	//
	public int getFirstFunctionCallDefinitionArraySize() {
		return firstFunctionCallDefinitionArray.size();
	}
	public int getSecondFunctionCallDefinitionArraySize() {
		return secondFunctionCallDefinitionArray.size();
	}
	//
	public FunctionCallDefinition[] getFunctionCallTableArray() {
		ArrayList<FunctionCallDefinition> functionCallDefinitionArray;
		if (useSecondVariableNameRegister) {
			functionCallDefinitionArray= secondFunctionCallDefinitionArray;
		} else {
			functionCallDefinitionArray= firstFunctionCallDefinitionArray;
		};
		return functionCallDefinitionArray.toArray(new FunctionCallDefinition[functionCallDefinitionArray.size()]);
	}
	//
	public int functionCallDefinitionArraySize() {
		if (useSecondVariableNameRegister) {
			return secondFunctionCallDefinitionArray.size();
		} else {
			return firstFunctionCallDefinitionArray.size();
		}
	}
	//
	public Term getTermFunctionCallTable() {
		Term list= PrologEmptyList.instance;
		if (useSecondVariableNameRegister) {
			for (int k=secondFunctionCallDefinitionArray.size()-1; k >= 0; k--) {
				FunctionCallDefinition definition= secondFunctionCallDefinitionArray.get(k);
				list= new PrologList(definition.toActorPrologTerm(),list);
			}
		} else {
			for (int k=firstFunctionCallDefinitionArray.size()-1; k >= 0; k--) {
				FunctionCallDefinition definition= firstFunctionCallDefinitionArray.get(k);
				list= new PrologList(definition.toActorPrologTerm(),list);
			}
		};
		return list;
	}
	//
	public void clearFunctionCallTable() {
		if (useSecondVariableNameRegister) {
			secondFunctionCallDefinitionArray.clear();
		} else {
			firstFunctionCallDefinitionArray.clear();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void modifyTermAsterisk(ArrayList<Term> terms, int textPosition, ChoisePoint iX) throws SyntaxError {
		if (!previousExpressionWasAVariable) {
			master.handleError(new OnlyAVariableCanBeMarkedByTheAsterisk(textPosition),iX);
		};
		Term lastElement= terms.get(terms.size()-1);
		checkTheTermMarkedByTheAsterisk(lastElement,textPosition,iX);
		Term[] internalArray= new Term[1];
		internalArray[0]= lastElement;
		lastElement= new PrologStructure(SymbolCodes.symbolCode_E_asterisk,internalArray);
		if (rememberTextPositions) {
			lastElement= attachTermPosition(lastElement,textPosition);
		};
		terms.set(terms.size()-1,lastElement);
		lastTermHasAsterisk= true;
	}
	//
	@Override
	protected Term parseStructure(long functorCode, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		ArrayList<Term> internalTermArray= parseMetaTerms(iX);
		Term[] arguments= new Term[2];
		arguments[0]= new PrologSymbol(functorCode);
		arguments[1]= arrayListToStructureArguments(internalTermArray);
		if (position < numberOfTokens) {
			PrologToken secondToken= tokens[position];
			if (secondToken.getType()==PrologTokenType.R_ROUND_BRACKET) {
				position++;
			} else {
				master.handleError(new RightRoundBracketIsExpected(secondToken.getPosition()),iX);
				position++;
			};
			Term result= new PrologStructure(SymbolCodes.symbolCode_E_structure,arguments);
			if (rememberTextPositions) {
				result= attachTermPosition(result,beginningOfTerm);
			};
			return result;
		} else {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		}
	}
	//
	protected Term arrayListToStructureArguments(ArrayList<Term> array) {
		Term result= PrologEmptyList.instance;
		for (int n=array.size()-1; n >= 0; n--) {
			Term internalTerm= array.get(n);
			result= new PrologList(internalTerm,result);
		};
		return result;
	}
	//
	@Override
	protected Term parseSymbol(long functorCode, int beginningOfTerm) {
		Term[] termArray= new Term[1];
		termArray[0]= new PrologSymbol(functorCode);
		Term result= new PrologStructure(SymbolCodes.symbolCode_E_symbol,termArray);
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	@Override
	protected Term parseSlot(long functorCode, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		if (!robustMode) {
			if (!symbolIsASlot(functorCode)) {
				master.handleError(new UndefinedSlotName(beginningOfTerm),iX);
			}
		};
		Term[] termArray= new Term[1];
		termArray[0]= new PrologSymbol(functorCode);
		Term result= new PrologStructure(SymbolCodes.symbolCode_E_slot,termArray);
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	@Override
	protected Term parseInteger(PrologToken token, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		Term result= new PrologInteger(token.getIntegerValue(master,iX));
		if (token instanceof TokenIntegerR) {
			TokenIntegerR tokenR= (TokenIntegerR)token;
			BigInteger radix= tokenR.getRadix();
			Term[] termArray= new Term[2];
			termArray[0]= new PrologInteger(radix);
			termArray[1]= result;
			result= new PrologStructure(SymbolCodes.symbolCode_E_radix,termArray);
		};
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	protected Term parseInteger(boolean negateValue, PrologToken token, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		BigInteger integerValue= token.getIntegerValue(master,iX);
		if (negateValue) {
			integerValue= integerValue.negate();
		};
		Term result= new PrologInteger(integerValue);
		if (token instanceof TokenIntegerR) {
			TokenIntegerR tokenR= (TokenIntegerR)token;
			BigInteger radix= tokenR.getRadix();
			Term[] termArray= new Term[2];
			termArray[0]= new PrologInteger(radix);
			termArray[1]= result;
			result= new PrologStructure(SymbolCodes.symbolCode_E_radix,termArray);
		};
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	@Override
	protected Term parseReal(PrologToken token, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		Term result= new PrologReal(token.getRealValue(master,iX));
		if (token instanceof TokenRealR) {
			TokenRealR tokenR= (TokenRealR)token;
			BigInteger radix= tokenR.getRadix();
			Term[] termArray= new Term[2];
			termArray[0]= new PrologInteger(radix);
			termArray[1]= result;
			result= new PrologStructure(SymbolCodes.symbolCode_E_radix,termArray);
		};
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	protected Term parseReal(boolean negateValue, PrologToken token, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		double realValue= token.getRealValue(master,iX);
		if (negateValue) {
			realValue= -realValue;
		};
		Term result= new PrologReal(realValue);
		if (token instanceof TokenRealR) {
			TokenRealR tokenR= (TokenRealR)token;
			BigInteger radix= tokenR.getRadix();
			Term[] termArray= new Term[2];
			termArray[0]= new PrologInteger(radix);
			termArray[1]= result;
			result= new PrologStructure(SymbolCodes.symbolCode_E_radix,termArray);
		};
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	@Override
	protected Term createTermEmptyList() {
		return metaTermEmptyList;
	}
	//
	@Override
	protected Term parseListTail(int textPosition, ChoisePoint iX) throws SyntaxError {
		return parseTail(false,iX);
	}
	//
	@Override
	public Term createTermListElement(Term internalTerm, Term tail) {
		Term[] termArray= new Term[2];
		termArray[0]= internalTerm;
		termArray[1]= tail;
		return new PrologStructure(SymbolCodes.symbolCode_E_e,termArray);
	}
	//
	@Override
	protected Term parseWorld(PrologToken token, ChoisePoint iX) throws SyntaxError {
		master.handleError(new UnexpectedToken(token),iX);
		Term result= PrologUnknownValue.instance;
		if (rememberTextPositions) {
			result= attachTermPosition(result,token.getPosition());
		};
		return result;
	}
	//
	@Override
	protected Term parseVariable(String variableName, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			PrologTokenType frontTokenType= frontToken.getType();
			switch (frontTokenType) {
			case L_BRACE:
				{
///////////////////////////////////////////////////////////////////////
previousExpressionWasAVariable= false;
position++;
ArrayList<LabeledTerm> arguments= new ArrayList<>();
Term firstPairValue= parseIsolatedVariable(variableName,beginningOfTerm,iX);
arguments.add(new LabeledTerm(0,false,firstPairValue,beginningOfTerm));
return parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
///////////////////////////////////////////////////////////////////////
				}
			case L_SQUARE_BRACKET:
				{
///////////////////////////////////////////////////////////////////////
checkWhetherVariableIsNamed(variableName,beginningOfTerm,iX);
position++;
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
Term[] indices= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
declareNestedFunctionCalls(functionCallDefinitionMarker);
ActorPrologAtom simpleAtomOne= new ActorPrologAtom(
	SymbolCodes.symbolCode_E_element,
	indices,
	false,
	beginningOfTerm,
	false,
	-1,
	true,
	false,
	true);
int firstVariableNumber= registerRegularVariable(variableName,beginningOfTerm,iX);
if (!robustMode) {
	if (firstVariableNumber==anonymousVariableNumber) {
		master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
	}
};
Term firstVariableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(firstVariableNumber,beginningOfTerm,iX);
ActorPrologFunctionCall functionCallOne= new ActorPrologFunctionCall(
	false,
	-1,
	firstVariableNumber,
	firstVariableRoleTerm,
	simpleAtomOne,
	beginningOfTerm);
if (position < numberOfTokens) {
	PrologToken secondToken= tokens[position];
	PrologTokenType secondTokenType= secondToken.getType();
	if (secondTokenType==PrologTokenType.QUESTION_MARK) {
		position++;
		functionCallDefinitionMarker= functionCallDefinitionArraySize();
		ActorPrologAtom simpleAtomTwo= parseSimpleAtom(true,secondToken.getPosition(),iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		int secondVariableNumber= registerFunctionCall(functionCallOne,iX);
		Term secondVariableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(secondVariableNumber,beginningOfTerm,iX);
		ActorPrologFunctionCall functionCallTwo= new ActorPrologFunctionCall(
			false,
			-1,
			secondVariableNumber,
			secondVariableRoleTerm,
			simpleAtomTwo,
			beginningOfTerm);
		Term auxiliaryVariable= registerFunctionCallAndCreateTerm(functionCallTwo,iX);
		previousExpressionWasAVariable= false;
		return auxiliaryVariable;
	}
};
Term auxiliaryVariable= registerFunctionCallAndCreateTerm(functionCallOne,iX);
previousExpressionWasAVariable= false;
return auxiliaryVariable;
///////////////////////////////////////////////////////////////////////
				}
			case QUESTION_MARK:
				{
///////////////////////////////////////////////////////////////////////
checkWhetherVariableIsNamed(variableName,beginningOfTerm,iX);
position++;
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
ActorPrologAtom simpleAtom= parseSimpleAtom(true,frontToken.getPosition(),iX);
declareNestedFunctionCalls(functionCallDefinitionMarker);
int variableNumber= registerRegularVariable(variableName,beginningOfTerm,iX);
if (!robustMode) {
	if (variableNumber==anonymousVariableNumber) {
		master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
	}
};
Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
	false,
	-1,
	variableNumber,
	variableRoleTerm,
	simpleAtom,
	beginningOfTerm);
Term auxiliaryVariable= registerFunctionCallAndCreateTerm(functionCall,iX);
previousExpressionWasAVariable= false;
return auxiliaryVariable;
///////////////////////////////////////////////////////////////////////
				}
			}
		};
		previousExpressionWasAVariable= true;
		return parseIsolatedVariable(variableName,beginningOfTerm,iX);
	}
	//
	protected Term parseIsolatedVariable(String variableName, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		int variableNumber= registerRegularVariable(variableName,beginningOfTerm,iX);
		Term[] termArray= new Term[2];
		termArray[0]= new PrologInteger(variableNumber);
		termArray[1]= declareUnconstrainedVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
		Term value= new PrologStructure(SymbolCodes.symbolCode_E_var,termArray);
		if (rememberTextPositions) {
			value= attachTermPosition(value,beginningOfTerm);
		};
		return value;
	}
	//
	protected Term parsePrimaryFunctionVariable(int variableNumber, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		Term[] termArray= new Term[2];
		termArray[0]= new PrologInteger(variableNumber);
		termArray[1]= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
		Term result= new PrologStructure(SymbolCodes.symbolCode_E_var,termArray);
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	protected Term parseSecondaryFunctionVariable(int variableNumber, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		Term[] termArray= new Term[2];
		termArray[0]= new PrologInteger(variableNumber);
		termArray[1]= declareSecondaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
		Term result= new PrologStructure(SymbolCodes.symbolCode_E_var,termArray);
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	@Override
	protected Term createTermEmptySet() {
		return metaTermEmptySet;
	}
	//
	@Override
	protected Term parseSetTail(int textPosition, ChoisePoint iX) throws SyntaxError {
		return parseTail(initializerIsParsed(),iX);
	}
	//
	protected boolean initializerIsParsed() {
		return false;
	}
	//
	@Override
	protected Term createTermSetElement(LabeledTerm pair, Term tail) {
		Term[] termArray= new Term[3];
		termArray[0]= pair.getKeyTerm();
		termArray[1]= pair.getValue();
		termArray[2]= tail;
		return new PrologStructure(SymbolCodes.symbolCode_E_i,termArray);
	}
	//
	@Override
	protected LabeledTerm parseInestimablePairValue(long pairNameCode, boolean isInQuotes, int pairPosition, ChoisePoint iX) throws SyntaxError {
		if (isInQuotes) {
			registerAnonymousVariable(pairPosition);
			return new LabeledTerm(pairNameCode,true,metaTermAnonymousVariable,pairPosition);
		} else if (symbolIsASlot(pairNameCode)) {
			Term[] termArray= new Term[1];
			termArray[0]= new PrologSymbol(pairNameCode);
			Term pairValue= new PrologStructure(SymbolCodes.symbolCode_E_slot,termArray);
			return new LabeledTerm(pairNameCode,true,pairValue,pairPosition);
		} else {
			if (!robustMode) {
				master.handleError(new SymbolShouldBeEnclosedInApostrophesHere(pairPosition),iX);
			};
			return new LabeledTerm(pairNameCode,true,metaTermAnonymousVariable,pairPosition);
		}
	}
	@Override
	protected LabeledTerm parseInestimablePairValue(BigInteger key, int pairPosition, ChoisePoint iX) {
		long pairNameCode= Arithmetic.toLong(key);
		registerAnonymousVariable(pairPosition);
		return new LabeledTerm(pairNameCode,false,metaTermAnonymousVariable,pairPosition);
	}
	//
	protected Term parseTail(boolean prohibitSlotUsage, ChoisePoint iX) throws SyntaxError {
		previousExpressionWasAVariable= false;
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			int beginningOfTerm= frontToken.getPosition();
			PrologTokenType frontTokenType= frontToken.getType();
			switch (frontTokenType) {
			case SYMBOL:
				{
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
if (p2 < numberOfTokens) {
	PrologToken secondToken= tokens[p2];
	PrologTokenType secondTokenType= secondToken.getType();
	if (secondTokenType==PrologTokenType.QUESTION_MARK) {
		position= position + 2;
		return parseFunctionCallInSlot(frontToken.getSymbolCode(master,iX),beginningOfTerm,secondToken.getPosition(),iX);
	}
};
if (!robustMode) {
	if (prohibitSlotUsage) {
		master.handleError(new SlotCannotBeASetTailInInitializers(frontToken.getPosition()),iX);
	}
};
if (frontToken.isInQuotes(master,iX) && !robustMode) {
	master.handleError(new SlotNameCannotBeEnclosedInApostrophes(frontToken.getPosition()),iX);
};
position++;
Term[] termArray= new Term[1];
termArray[0]= new PrologSymbol(frontToken.getSymbolCode(master,iX));
Term result= new PrologStructure(SymbolCodes.symbolCode_E_slot,termArray);
if (rememberTextPositions) {
	result= attachTermPosition(result,beginningOfTerm);
};
previousExpressionWasAVariable= false;
return result;
///////////////////////////////////////////////////////////////////////
				}
				// break;
			case VARIABLE:
				{
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
if (p2 < numberOfTokens) {
	PrologToken secondToken= tokens[p2];
	PrologTokenType secondTokenType= secondToken.getType();
	if (secondTokenType==PrologTokenType.QUESTION_MARK) {
		position= position + 1;
		return parseVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
	}
};
position++;
return parseIsolatedVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
///////////////////////////////////////////////////////////////////////
				}
				// break;
			case QUESTION_MARK:
				{
///////////////////////////////////////////////////////////////////////
// position++;
ActorPrologFunctionCall functionCall= parseFunctionCall(iX);
Term auxiliaryVariable= registerFunctionCallAndCreateTerm(functionCall,iX);
previousExpressionWasAVariable= false;
return auxiliaryVariable;
///////////////////////////////////////////////////////////////////////
				}
				// break;
			default:
				master.handleError(new UnexpectedToken(frontToken),iX);
				position++;
				break;
			};
			return parseIsolatedVariable(anonymousVariableName,beginningOfTerm,iX);
		} else {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		}
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE EXPRESSION *******************************************
//*********************************************************************
//*********************************************************************
	//
	public Term continueExpressionParsing(Term multiplier1, int functionCallDefinitionMarker, ChoisePoint iX) throws SyntaxError {
		previousExpressionWasAVariable= false;
		Term addend1= parseAddend(multiplier1,functionCallDefinitionMarker,iX);
		return parseExpression(addend1,functionCallDefinitionMarker,iX);
	}
	//
	@Override
	public Term parseExpression(boolean insertMinus, int positionOfMinus, ChoisePoint iX) throws SyntaxError {
		previousExpressionWasAVariable= false;
		int functionCallDefinitionMarker= functionCallDefinitionArraySize();
		Term addend1= parseAddend(insertMinus,positionOfMinus,iX);
		return parseExpression(addend1,functionCallDefinitionMarker,iX);
	}
	public Term parseExpression(Term addend1, int functionCallDefinitionMarker, ChoisePoint iX) throws SyntaxError {
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			int beginningOfTerm= frontToken.getPosition();
			PrologTokenType frontTokenType= frontToken.getType();
			long functionNameCode;
			switch (frontTokenType) {
			case PLUS:
				functionNameCode= SymbolCodes.symbolCode__add;
				break;
			case MINUS:
				functionNameCode= SymbolCodes.symbolCode__sub;
				break;
			default:
				return addend1;
			};
			previousExpressionWasAVariable= false;
///////////////////////////////////////////////////////////////////////
position++;
Term addend2= parseAddend(false,0,iX);
declareNestedFunctionCalls(functionCallDefinitionMarker);
ActorPrologSubgoal subgoal= new ActorPrologNearFunctionCall(
	functionNameCode,
	new Term[]{addend1,addend2},
	beginningOfTerm);
Term auxiliaryVariable= registerFunctionCallAndCreateTerm(subgoal,iX);
return parseExpression(auxiliaryVariable,functionCallDefinitionMarker,iX);
///////////////////////////////////////////////////////////////////////
		} else {
			return addend1;
		}
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE ADDEND ***********************************************
//*********************************************************************
//*********************************************************************
	//
	public Term parseAddend(boolean insertMinus, int positionOfMinus, ChoisePoint iX) throws SyntaxError {
		int functionCallDefinitionMarker= functionCallDefinitionArraySize();
		Term multiplier1= parseMultiplier(insertMinus,positionOfMinus,iX);
		return parseAddend(multiplier1,functionCallDefinitionMarker,iX);
	}
	public Term parseAddend(Term multiplier1, int functionCallDefinitionMarker, ChoisePoint iX) throws SyntaxError {
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			int beginningOfTerm= frontToken.getPosition();
			PrologTokenType frontTokenType= frontToken.getType();
			long functionNameCode;
			switch (frontTokenType) {
			case MULTIPLY:
				functionNameCode= SymbolCodes.symbolCode__mult;
				int p2= position + 1;
				if (p2 < numberOfTokens) {
					PrologToken secondToken= tokens[p2];
					PrologTokenType secondTokenType= secondToken.getType();
					if (secondTokenType==PrologTokenType.R_ROUND_BRACKET) {
						return multiplier1;
					}
				} else {
					return multiplier1;
				};
				break;
			case DIVIDE:
				functionNameCode= SymbolCodes.symbolCode__slash;
				break;
			default:
				return multiplier1;
			};
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
PrologToken secondToken= tokens[p2];
PrologTokenType secondTokenType= frontToken.getType();
if (frontTokenType==PrologTokenType.R_ROUND_BRACKET) {
	return multiplier1;
} else {
	previousExpressionWasAVariable= false;
	position++;
	Term multiplier2= parseMultiplier(false,0,iX);
	declareNestedFunctionCalls(functionCallDefinitionMarker);
	ActorPrologSubgoal subgoal= new ActorPrologNearFunctionCall(
		functionNameCode,
		new Term[]{multiplier1,multiplier2},
		beginningOfTerm);
	Term auxiliaryVariable= registerFunctionCallAndCreateTerm(subgoal,iX);
	return parseAddend(auxiliaryVariable,functionCallDefinitionMarker,iX);
}
///////////////////////////////////////////////////////////////////////
		} else {
			return multiplier1;
		}
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE MULTIPLIER *******************************************
//*********************************************************************
//*********************************************************************
	//
	public Term parseMultiplier(boolean insertMinus, int positionOfMinus, ChoisePoint iX) throws SyntaxError {
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			int beginningOfTerm= frontToken.getPosition();
			PrologTokenType frontTokenType= frontToken.getType();
			switch (frontTokenType) {
			case L_ROUND_BRACKET:
				{
///////////////////////////////////////////////////////////////////////
if (insertMinus) {
	return parseMultiplierAfterMinus(positionOfMinus,iX);
} else {
	position++;
	Term expression= parseExpression(false,0,iX);
	parseRightRoundBracket(iX);
	previousExpressionWasAVariable= false;
	return expression;
}
///////////////////////////////////////////////////////////////////////
				}
				// break;
			case MINUS:
				{
///////////////////////////////////////////////////////////////////////
position++;
return parseMultiplierAfterMinus(beginningOfTerm,iX);
///////////////////////////////////////////////////////////////////////
				}
				// break;
			default:
				return parseTermAfterMinusIfNecessary(insertMinus,positionOfMinus,iX);
			}
		} else {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		}
	}
	//
	public Term parseMultiplierAfterMinus(int positionOfMinus, ChoisePoint iX) throws SyntaxError {
		if (position < numberOfTokens) {
			PrologTokenType frontTokenType= tokens[position].getType();
			if (frontTokenType==PrologTokenType.L_ROUND_BRACKET) {
				position++;
				int functionCallDefinitionMarker= functionCallDefinitionArraySize();
				Term expression= parseExpression(false,0,iX);
				declareNestedFunctionCalls(functionCallDefinitionMarker);
				ActorPrologSubgoal subgoal= new ActorPrologNearFunctionCall(
					SymbolCodes.symbolCode__sub,
					new Term[]{expression},
					positionOfMinus);
				parseRightRoundBracket(iX);
				Term auxiliaryVariable= registerFunctionCallAndCreateTerm(subgoal,iX);
				previousExpressionWasAVariable= false;
				return auxiliaryVariable;
			} else {
				return parseTermAfterMinusIfNecessary(true,positionOfMinus,iX);
			}
		} else {
			return parseTermAfterMinusIfNecessary(true,positionOfMinus,iX);
		}
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE SIMPLE ATOM ******************************************
//*********************************************************************
//*********************************************************************
	//
	public ActorPrologAtom parseRootSimpleAtom(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		return parseSimpleAtom(false,-1,iX);
	}
	//
	public ActorPrologAtom parseSimpleAtom(boolean atomBeginsWithTheQuestionMark, int questionMarkPosition, ChoisePoint iX) throws SyntaxError {
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			int beginningOfTerm= frontToken.getPosition();
			PrologTokenType frontTokenType= frontToken.getType();
			switch (frontTokenType) {
			case SYMBOL:
///////////////////////////////////////////////////////////////////////
if (!symbolIsASlot(frontToken.getSymbolCode(master,iX),frontToken.isInQuotes(master,iX))) {
	int p2= position + 1;
	if (p2 < numberOfTokens) {
		PrologToken secondToken= tokens[p2];
		PrologTokenType secondTokenType= secondToken.getType();
		switch (secondTokenType) {
		case L_ROUND_BRACKET:
			int p3= p2 + 1;
			if (p3 < numberOfTokens) {
				PrologToken thirdToken= tokens[p3];
				PrologTokenType thirdTokenType= thirdToken.getType();
				if (thirdTokenType==PrologTokenType.R_ROUND_BRACKET) {
					position= position + 3;
					return new ActorPrologAtom(
						frontToken.getSymbolCode(master,iX),
						emptyTermArray,
						false,
						beginningOfTerm,
						atomBeginsWithTheQuestionMark,
						questionMarkPosition,
						false,
						false,
						true);
				} else {
					position= position + 2;
					Term[] termArray= parseTermSequence(PrologTokenType.R_ROUND_BRACKET,iX);
					return new ActorPrologAtom(
						frontToken.getSymbolCode(master,iX),
						termArray,
						lastTermHasAsterisk(),
						beginningOfTerm,
						atomBeginsWithTheQuestionMark,
						questionMarkPosition,
						termArray.length>0,
						false,
						true);
				}
			} else {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			}
		case L_BRACE:
			position= position + 2;
			Term firstPairValue= new PrologSymbol(frontToken.getSymbolCode(master,iX));
			if (rememberTextPositions) {
				firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
			};
			return parseUnderdeterminedSetSimpleAtom(firstPairValue,beginningOfTerm,iX);
		default:
			position++;
			return new ActorPrologAtom(
				frontToken.getSymbolCode(master,iX),
				emptyTermArray,
				false,
				beginningOfTerm,
				atomBeginsWithTheQuestionMark,
				questionMarkPosition,
				frontToken.isInQuotes(master,iX),
				false,
				true);
		}
	} else {
		return new ActorPrologAtom(
			frontToken.getSymbolCode(master,iX),
			emptyTermArray,
			false,
			beginningOfTerm,
			atomBeginsWithTheQuestionMark,
			questionMarkPosition,
			frontToken.isInQuotes(master,iX),
			false,
			true);
	}
} else {
	int p2= position + 1;
	if (p2 < numberOfTokens) {
		PrologToken secondToken= tokens[p2];
		PrologTokenType secondTokenType= secondToken.getType();
		if (secondTokenType==PrologTokenType.L_BRACE) {
			position= position + 2;
			Term firstPairValue= new PrologSymbol(frontToken.getSymbolCode(master,iX));
			if (rememberTextPositions) {
				firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
			};
			return parseUnderdeterminedSetSimpleAtom(firstPairValue,beginningOfTerm,iX);
		} else {
			master.handleError(new SlotNameIsNotAllowedHere(beginningOfTerm),iX);
			position++;
		}
	} else {
		master.handleError(new SlotNameIsNotAllowedHere(beginningOfTerm),iX);
		position++;
	}
};
///////////////////////////////////////////////////////////////////////
				break;
			case L_BRACE:
///////////////////////////////////////////////////////////////////////
position++;
return parseUnderdeterminedSetSimpleAtom(beginningOfTerm,iX);
///////////////////////////////////////////////////////////////////////
			case INTEGER:
				{
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
if (p2 < numberOfTokens && tokens[p2].getType()==PrologTokenType.L_BRACE) {
	position= position + 2;
	Term firstPairValue= new PrologInteger(frontToken.getIntegerValue(master,iX));
	if (rememberTextPositions) {
		firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
	};
	return parseUnderdeterminedSetSimpleAtom(firstPairValue,beginningOfTerm,iX);
} else {
	master.handleError(new SimpleAtomIsExpected(beginningOfTerm),iX);
	position++;
}
///////////////////////////////////////////////////////////////////////
				};
				break;
			case REAL:
				{
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
if (p2 < numberOfTokens && tokens[p2].getType()==PrologTokenType.L_BRACE) {
	position= position + 2;
	Term firstPairValue= new PrologReal(frontToken.getRealValue(master,iX));
	if (rememberTextPositions) {
		firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
	};
	return parseUnderdeterminedSetSimpleAtom(firstPairValue,beginningOfTerm,iX);
} else {
	master.handleError(new SimpleAtomIsExpected(beginningOfTerm),iX);
	position++;
}
///////////////////////////////////////////////////////////////////////
				};
				break;
			case STRING_SEGMENT:
				{
///////////////////////////////////////////////////////////////////////
position++;
String stringContent= parseString(frontToken.getStringValue(master,iX),iX);
if (position < numberOfTokens && tokens[position].getType()==PrologTokenType.L_BRACE) {
	position++;
	Term firstPairValue= new PrologString(stringContent);
	if (rememberTextPositions) {
		firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
	};
	return parseUnderdeterminedSetSimpleAtom(firstPairValue,beginningOfTerm,iX);
} else {
	master.handleError(new SimpleAtomIsExpected(beginningOfTerm),iX);
}
///////////////////////////////////////////////////////////////////////
				};
				break;
			case BINARY_SEGMENT:
				{
///////////////////////////////////////////////////////////////////////
position++;
byte[] binaryContent= parseBinary(frontToken.getBinaryValue(master,iX),iX);
if (position < numberOfTokens && tokens[position].getType()==PrologTokenType.L_BRACE) {
	position++;
	Term firstPairValue= new PrologBinary(binaryContent);
	if (rememberTextPositions) {
		firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
	};
	return parseUnderdeterminedSetSimpleAtom(firstPairValue,beginningOfTerm,iX);
} else {
	master.handleError(new SimpleAtomIsExpected(beginningOfTerm),iX);
}
///////////////////////////////////////////////////////////////////////
				};
				break;
			case NUMBER_SIGN:
				{
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
if (p2 < numberOfTokens && tokens[p2].getType()==PrologTokenType.L_BRACE) {
	position= position + 2;
	Term firstPairValue= PrologUnknownValue.instance;
	if (rememberTextPositions) {
		firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
	};
	return parseUnderdeterminedSetSimpleAtom(firstPairValue,beginningOfTerm,iX);
} else {
	master.handleError(new SimpleAtomIsExpected(beginningOfTerm),iX);
	position++;
}
///////////////////////////////////////////////////////////////////////
				};
				break;
			case VARIABLE:
				{
///////////////////////////////////////////////////////////////////////
String variableName= frontToken.getVariableName(master,iX);
int p2= position + 1;
if (p2 < numberOfTokens) {
	PrologToken secondToken= tokens[p2];
	PrologTokenType secondTokenType= secondToken.getType();
	switch (secondTokenType) {
	case L_BRACE:
		{
			position= position + 2;
			Term firstPairValue= parseIsolatedVariable(variableName,beginningOfTerm,iX);
			return parseUnderdeterminedSetSimpleAtom(firstPairValue,beginningOfTerm,iX);
		}
		// break;
	case L_ROUND_BRACKET:
		{
			int p3= p2 + 1;
			if (p3 >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			PrologToken thirdToken= tokens[p3];
			PrologTokenType thirdTokenType= thirdToken.getType();
			if (thirdTokenType==PrologTokenType.R_ROUND_BRACKET) {
				position= position + 3;
				int variableNumber= registerMetaFunctorVariable(variableName,beginningOfTerm,iX);
				return new ActorPrologAtom(
					true,
					variableNumber,
					emptyTermArray,
					false,
					beginningOfTerm,
					atomBeginsWithTheQuestionMark,
					questionMarkPosition,
					false,
					false,
					true);
			} else {
				int variableNumber= registerMetaFunctorVariable(variableName,beginningOfTerm,iX);
				position= position + 2;
				Term[] termArray= parseTermSequence(PrologTokenType.R_ROUND_BRACKET,iX);
				return new ActorPrologAtom(
					true,
					variableNumber,
					termArray,
					lastTermHasAsterisk(),
					beginningOfTerm,
					atomBeginsWithTheQuestionMark,
					questionMarkPosition,
					false,
					false,
					true);
			}
		}
	default:
		break;
	}
};
position++;
int variableNumber= registerMetaPredicateVariable(variableName,beginningOfTerm,iX);
return new ActorPrologAtom(variableNumber,beginningOfTerm,atomBeginsWithTheQuestionMark,questionMarkPosition);
///////////////////////////////////////////////////////////////////////
				}
				// break;
			default:
				master.handleError(new SimpleAtomIsExpected(beginningOfTerm),iX);
				position++;
				break;
			};
			return new ActorPrologAtom(anonymousVariableNumber,beginningOfTerm,atomBeginsWithTheQuestionMark,questionMarkPosition);
		} else {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		}
	}
	//
	public ActorPrologAtom parseUnderdeterminedSetSimpleAtom(int position, ChoisePoint iX) throws SyntaxError {
		ArrayList<LabeledTerm> arguments= new ArrayList<>();
		Term underdeterminedSet= parseUnderdeterminedSet(arguments,position,iX);
		return new ActorPrologAtom(
			SymbolCodes.symbolCode_E_,
			new Term[]{underdeterminedSet},
			false,
			position,
			false,
			-1,
			true,
			true,
			true);
	}
	public ActorPrologAtom parseUnderdeterminedSetSimpleAtom(Term firstPairValue, int position, ChoisePoint iX) throws SyntaxError {
		ArrayList<LabeledTerm> arguments= new ArrayList<>();
		arguments.add(new LabeledTerm(0,false,firstPairValue,position));
		Term underdeterminedSet= parseUnderdeterminedSet(arguments,position,iX);
		return new ActorPrologAtom(
			SymbolCodes.symbolCode_E_,
			new Term[]{underdeterminedSet},
			false,
			position,
			false,
			-1,
			true,
			true,
			true);
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE FUNCTION CALL ****************************************
//*********************************************************************
//*********************************************************************
	//
	public ActorPrologFunctionCall parseRootFunctionCall(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		return parseFunctionCall(iX);
	}
	//
	public ActorPrologFunctionCall parseFunctionCall(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken frontToken= tokens[position];
		int beginningOfTerm= frontToken.getPosition();
		PrologTokenType frontTokenType= frontToken.getType();
		boolean targetParameterIsASlot;
		long targetSlotNameCode= -1;
		int targetVariableNumber= -1;
		switch (frontTokenType) {
		case SYMBOL:
			position++;
			long symbolCode= parseTargetSlot(frontToken,iX);
			targetParameterIsASlot= true;
			targetSlotNameCode= symbolCode;
			break;
		case VARIABLE:
			position++;
			targetParameterIsASlot= false;
			String variableName= frontToken.getVariableName(master,iX);
			checkWhetherVariableIsNamed(variableName,beginningOfTerm,iX);
			targetVariableNumber= registerRegularVariable(variableName,beginningOfTerm,iX);
			break;
		default:
			targetParameterIsASlot= true;
			targetSlotNameCode= SymbolCodes.symbolCode_E_self;
		};
		if (position < numberOfTokens) {
			PrologToken secondToken= tokens[position];
			PrologTokenType secondTokenType= secondToken.getType();
			if (secondTokenType==PrologTokenType.L_SQUARE_BRACKET) {
///////////////////////////////////////////////////////////////////////
position++;
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
Term[] indices= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
declareNestedFunctionCalls(functionCallDefinitionMarker);
ActorPrologAtom simpleAtom= new ActorPrologAtom(
	SymbolCodes.symbolCode_E_element,
	indices,
	false,
	beginningOfTerm,
	false,
	-1,
	true,
	false,
	true);
if (targetVariableNumber==anonymousVariableNumber && !robustMode) {
	master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
};
Term targetVariableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(targetVariableNumber,beginningOfTerm,iX);
return new ActorPrologFunctionCall(
	targetParameterIsASlot,
	targetSlotNameCode,
	targetVariableNumber,
	targetVariableRoleTerm,
	simpleAtom,
	beginningOfTerm);
///////////////////////////////////////////////////////////////////////
			}
		};
		int questionMarkPosition= parseQuestionMark(iX);
		int functionCallDefinitionMarker= functionCallDefinitionArraySize();
		ActorPrologAtom simpleAtom= parseSimpleAtom(true,questionMarkPosition,iX);
		simpleAtom.checkWhetherFunctorIsNotAnonymousVariable(master,iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		if (targetVariableNumber==anonymousVariableNumber && !robustMode) {
			master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
		};
		Term targetVariableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(targetVariableNumber,beginningOfTerm,iX);
		return new ActorPrologFunctionCall(
			targetParameterIsASlot,
			targetSlotNameCode,
			targetVariableNumber,
			targetVariableRoleTerm,
			simpleAtom,
			beginningOfTerm);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected Term parseFunctionCallInSlot(long symbolCode, int beginningOfTerm, int questionMarkPosition, ChoisePoint iX) throws SyntaxError {
		int functionCallDefinitionMarker= functionCallDefinitionArraySize();
		ActorPrologAtom simpleAtom= parseSimpleAtom(true,questionMarkPosition,iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			true,
			symbolCode,
			-1,
			PrologUnknownValue.instance,
			simpleAtom,
			beginningOfTerm);
		Term auxiliaryVariable= registerFunctionCallAndCreateTerm(functionCall,iX);
		previousExpressionWasAVariable= false;
		return auxiliaryVariable;
	}
	//
	@Override
	protected Term parseTheElementFunctionCall(long symbolCode, int beginningOfTerm, int questionMarkPosition, ChoisePoint iX) throws SyntaxError {
		int functionCallDefinitionMarker= functionCallDefinitionArraySize();
		Term[] indices= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologAtom simpleAtomOne= new ActorPrologAtom(
			SymbolCodes.symbolCode_E_element,
			indices,
			false,
			beginningOfTerm,
			false,
			-1,
			true,
			false,
			true);
		ActorPrologFunctionCall functionCallOne= new ActorPrologFunctionCall(
			true,
			symbolCode,
			-1,
			PrologUnknownValue.instance,
			simpleAtomOne,
			beginningOfTerm);
		//
		if (position < numberOfTokens) {
			PrologToken secondToken= tokens[position];
			PrologTokenType secondTokenType= secondToken.getType();
			if (secondTokenType==PrologTokenType.QUESTION_MARK) {
				position++;
				functionCallDefinitionMarker= functionCallDefinitionArraySize();
				ActorPrologAtom simpleAtomTwo= parseSimpleAtom(true,secondToken.getPosition(),iX);
				declareNestedFunctionCalls(functionCallDefinitionMarker);
				int variableNumber= registerFunctionCall(functionCallOne,iX);
				Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
				ActorPrologFunctionCall functionCallTwo= new ActorPrologFunctionCall(
					false,
					-1,
					variableNumber,
					variableRoleTerm,
					simpleAtomTwo,
					beginningOfTerm);
				Term auxiliaryVariable= registerFunctionCallAndCreateTerm(functionCallTwo,iX);
				previousExpressionWasAVariable= false;
				return auxiliaryVariable;
			}
		};
		Term auxiliaryVariable= registerFunctionCallAndCreateTerm(functionCallOne,iX);
		previousExpressionWasAVariable= false;
		return auxiliaryVariable;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term registerFunctionCallAndCreateTerm(ActorPrologSubgoal mainSubgoal, ChoisePoint iX) throws SyntaxError {
		int beginningOfTerm= mainSubgoal.getPosition();
		if (iX != null) {
			int variableNumber1= registerFunctionCall(mainSubgoal,iX);
			return parsePrimaryFunctionVariable(variableNumber1,beginningOfTerm,iX);
		} else {
			master.handleError(new GroundTermCannotContainFunctionCalls(beginningOfTerm),iX);
			Term result= PrologUnknownValue.instance;
			if (rememberTextPositions) {
				result= attachTermPosition(result,beginningOfTerm);
			};
			return result;
		}
	}
	//
	protected int registerFunctionCall(ActorPrologSubgoal mainSubgoal, ChoisePoint iX) throws SyntaxError {
		int beginningOfTerm= mainSubgoal.getPosition();
		int variableNumber1= registerPrimaryFunctionVariable(beginningOfTerm,iX);
		if (useSecondVariableNameRegister) {
			secondFunctionCallDefinitionArray.add(
				new FunctionCallDefinition(
					mainSubgoal,
					variableNumber1,
					isClauseHeadingElement(),
					beginningOfTerm));
		} else {
			firstFunctionCallDefinitionArray.add(
				new FunctionCallDefinition(
					mainSubgoal,
					variableNumber1,
					isClauseHeadingElement(),
					beginningOfTerm));
		};
		return variableNumber1;
	}
	//
	protected boolean isClauseHeadingElement() {
		return false;
	}
	//
	protected void declareNestedFunctionCalls(int functionCallDefinitionMarker) throws SyntaxError {
		if (useSecondVariableNameRegister) {
			for (int k=functionCallDefinitionMarker; k < secondFunctionCallDefinitionArray.size(); k++) {
				secondFunctionCallDefinitionArray.get(k).setIsNested(true);
			}
		} else {
			for (int k=functionCallDefinitionMarker; k < firstFunctionCallDefinitionArray.size(); k++) {
				firstFunctionCallDefinitionArray.get(k).setIsNested(true);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term declareUnconstrainedVariableAndCreateRoleTerm(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			return VariableRole.getTermPlain();
		} else {
			VariableRole role;
			if (useSecondVariableNameRegister) {
				role= secondVariableRoleRegister.get(variableNumber);
				if (role == null) {
					role= new VariableRole(variableNumber,p);
					secondVariableRoleRegister.put(variableNumber,role);
				} else {
					role.checkPosition(p);
				}
			} else {
				role= firstVariableRoleRegister.get(variableNumber);
				if (role == null) {
					role= new VariableRole(variableNumber,p);
					firstVariableRoleRegister.put(variableNumber,role);
				} else {
					role.checkPosition(p);
				}
			};
			return role.toTerm();
		}
	}
	protected void declareUnconstrainedVariable(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			return;
		};
		if (useSecondVariableNameRegister) {
			VariableRole role= secondVariableRoleRegister.get(variableNumber);
			if (role == null) {
				role= new VariableRole(variableNumber,p);
				secondVariableRoleRegister.put(variableNumber,role);
			} else {
				role.checkPosition(p);
			}
		} else {
			VariableRole role= firstVariableRoleRegister.get(variableNumber);
			if (role == null) {
				role= new VariableRole(variableNumber,p);
				firstVariableRoleRegister.put(variableNumber,role);
			} else {
				role.checkPosition(p);
			}
		}
	}
	//
	protected Term declarePrimaryFunctionVariableAndCreateRoleTerm(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			return VariableRole.getTermPrimaryFunction();
		} else {
			VariableRole role;
			if (useSecondVariableNameRegister) {
				role= secondVariableRoleRegister.get(variableNumber);
				if (role == null) {
					role= new VariableRole(variableNumber,p);
					secondVariableRoleRegister.put(variableNumber,role);
				} else {
					role.checkPosition(p);
				}
			} else {
				role= firstVariableRoleRegister.get(variableNumber);
				if (role == null) {
					role= new VariableRole(variableNumber,p);
					firstVariableRoleRegister.put(variableNumber,role);
				} else {
					role.checkPosition(p);
				}
			};
			role.declarePrimaryFunctionVariable(p,master,iX);
			return role.toTerm();
		}
	}
	protected void declarePrimaryFunctionVariable(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			return;
		};
		VariableRole role;
		if (useSecondVariableNameRegister) {
			role= secondVariableRoleRegister.get(variableNumber);
			if (role == null) {
				role= new VariableRole(variableNumber,p);
				secondVariableRoleRegister.put(variableNumber,role);
			} else {
				role.checkPosition(p);
			}
		} else {
			role= firstVariableRoleRegister.get(variableNumber);
			if (role == null) {
				role= new VariableRole(variableNumber,p);
				firstVariableRoleRegister.put(variableNumber,role);
			} else {
				role.checkPosition(p);
			}
		};
		role.declarePrimaryFunctionVariable(p,master,iX);
	}
	//
	protected Term declareSecondaryFunctionVariableAndCreateRoleTerm(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			return VariableRole.getTermSecondaryFunction();
		} else {
			VariableRole role;
			if (useSecondVariableNameRegister) {
				role= secondVariableRoleRegister.get(variableNumber);
				if (role == null) {
					role= new VariableRole(variableNumber,p);
					secondVariableRoleRegister.put(variableNumber,role);
				} else {
					role.checkPosition(p);
				}
			} else {
				role= firstVariableRoleRegister.get(variableNumber);
				if (role == null) {
					role= new VariableRole(variableNumber,p);
					firstVariableRoleRegister.put(variableNumber,role);
				} else {
					role.checkPosition(p);
				}
			};
			role.declareSecondaryFunctionVariable(p,master,iX);
			return role.toTerm();
		}
	}
	//
	protected void declareMetaFunctorVariable(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		VariableRole role;
		if (useSecondVariableNameRegister) {
			role= secondVariableRoleRegister.get(variableNumber);
			if (role == null) {
				role= new VariableRole(variableNumber,p);
				secondVariableRoleRegister.put(variableNumber,role);
			} else {
				role.checkPosition(p);
			}
		} else {
			role= firstVariableRoleRegister.get(variableNumber);
			if (role == null) {
				role= new VariableRole(variableNumber,p);
				firstVariableRoleRegister.put(variableNumber,role);
			} else {
				role.checkPosition(p);
			}
		};
		role.declareMetaFunctorVariable(p,master,iX);
	}
	//
	protected void declareMetaPredicateVariable(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		VariableRole role;
		if (useSecondVariableNameRegister) {
			role= secondVariableRoleRegister.get(variableNumber);
			if (role == null) {
				role= new VariableRole(variableNumber,p);
				secondVariableRoleRegister.put(variableNumber,role);
			} else {
				role.checkPosition(p);
			}
		} else {
			role= firstVariableRoleRegister.get(variableNumber);
			if (role == null) {
				role= new VariableRole(variableNumber,p);
				firstVariableRoleRegister.put(variableNumber,role);
			} else {
				role.checkPosition(p);
			}
		};
		role.declareMetaPredicateVariable(p,master,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected int registerRegularVariable(String name, int p, ChoisePoint iX) throws SyntaxError {
		if (name.equals(anonymousVariableName)) {
			registerAnonymousVariable(p);
			return anonymousVariableNumber;
		} else {
			if (useSecondVariableNameRegister) {
				Integer value= secondVariableNameRegister.get(name);
				if (value==null) {
					secondRecentVariableNumber++;
					secondVariableNameRegister.put(name,secondRecentVariableNumber);
					secondInvertedVariableNameRegister.put(secondRecentVariableNumber,name);
					declareUnconstrainedVariable(secondRecentVariableNumber,p,iX);
					return secondRecentVariableNumber;
				} else {
					if (value==anonymousVariableNumber) {
						registerAnonymousVariable(p);
					} else {
						declareUnconstrainedVariable(value,p,iX);
					};
					return value;
				}
			} else {
				Integer value= firstVariableNameRegister.get(name);
				if (value==null) {
					firstRecentVariableNumber++;
					firstVariableNameRegister.put(name,firstRecentVariableNumber);
					firstInvertedVariableNameRegister.put(firstRecentVariableNumber,name);
					declareUnconstrainedVariable(firstRecentVariableNumber,p,iX);
					return firstRecentVariableNumber;
				} else {
					if (value==anonymousVariableNumber) {
						registerAnonymousVariable(p);
					} else {
						declareUnconstrainedVariable(value,p,iX);
					};
					return value;
				}
			}
		}
	}
	//
	protected int registerPrimaryFunctionVariable(int p, ChoisePoint iX) throws SyntaxError {
		for (int k=anonymousVariableNumber+1; k <= Integer.MAX_VALUE; k++) {
			String name= String.format(auxiliaryVariableNameFormat,k);
			if (useSecondVariableNameRegister) {
				if (!secondVariableNameRegister.containsKey(name)) {
					secondRecentVariableNumber++;
					secondVariableNameRegister.put(name,secondRecentVariableNumber);
					secondInvertedVariableNameRegister.put(secondRecentVariableNumber,name);
					declarePrimaryFunctionVariable(secondRecentVariableNumber,p,iX);
					return secondRecentVariableNumber;
				}
			} else {
				if (!firstVariableNameRegister.containsKey(name)) {
					firstRecentVariableNumber++;
					firstVariableNameRegister.put(name,firstRecentVariableNumber);
					firstInvertedVariableNameRegister.put(firstRecentVariableNumber,name);
					declarePrimaryFunctionVariable(firstRecentVariableNumber,p,iX);
					return firstRecentVariableNumber;
				}
			}
		};
		master.handleError(new AuxiliaryVariableRegisterOverflow(p),iX);
		return anonymousVariableNumber;
	}
	//
	protected int registerMetaFunctorVariable(String name, int p, ChoisePoint iX) throws SyntaxError {
		int number= registerRegularVariable(name,p,iX);
		declareMetaFunctorVariable(number,p,iX);
		return number;
	}
	//
	protected int registerMetaPredicateVariable(String name, int p, ChoisePoint iX) throws SyntaxError {
		int number= registerRegularVariable(name,p,iX);
		declareMetaPredicateVariable(number,p,iX);
		return number;
	}
	//
	protected void checkWhetherVariableIsNamed(String variableName, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		if (variableName.equals(anonymousVariableName)) {
			master.handleError(new AnonymousVariableIsNotAllowedHere(beginningOfTerm),iX);
		}
	}
	//
	protected void registerAnonymousVariable(int p) {
		if (useSecondVariableNameRegister) {
			secondAnonymousVariableRegister.add(p);
		} else {
			firstAnonymousVariableRegister.add(p);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void checkTheTermMarkedByTheAsterisk(Term argument, int p, ChoisePoint iX) throws SyntaxError {
		try {
			long functor= argument.getStructureFunctor(iX);
			if (functor==SymbolCodes.symbolCode_E_p) {
				Term[] terms= argument.getStructureArguments(iX);
				if (terms.length==2) {
					argument= terms[0];
					Term termPosition= terms[1];
					p= termPosition.getSmallIntegerValue(iX);
				}
			}
		} catch (TermIsNotAStructure e) {
		} catch (TermIsNotAnInteger e) {
		};
		try {
			long functor= argument.getStructureFunctor(iX);
			if (functor==SymbolCodes.symbolCode_E_var) {
///////////////////////////////////////////////////////////////////////
Term[] terms= argument.getStructureArguments(iX);
if (terms.length==2) {
	Term term= terms[0];
	int variableNumber= term.getSmallIntegerValue(iX);
	VariableRole role;
	if (useSecondVariableNameRegister) {
		role= secondVariableRoleRegister.get(variableNumber);
	} else {
		role= firstVariableRoleRegister.get(variableNumber);
	};
	if (role != null) {
		if (role.mustBeMetaFunctor()) {
			master.handleError(new MetaFunctorCannotBeMarkedByTheAsterisk(p),iX);
		} else if (role.mustBeMetaPredicate()) {
			master.handleError(new MetaPredicateCannotBeMarkedByTheAsterisk(p),iX);
		}
	};
	checkClauseHeadingAsteriskVariable(variableNumber,p,iX);
}
///////////////////////////////////////////////////////////////////////
			} else {
				if (!robustMode) {
					master.handleError(new OnlyAVariableCanBeMarkedByTheAsterisk(p),iX);
				}
			}
		} catch (TermIsNotAStructure e) {
			if (!robustMode) {
				master.handleError(new OnlyAVariableCanBeMarkedByTheAsterisk(p),iX);
			}
		} catch (TermIsNotAnInteger e) {
			if (!robustMode) {
				master.handleError(new OnlyAVariableCanBeMarkedByTheAsterisk(p),iX);
			}
		}
	}
	//
	protected void checkClauseHeadingAsteriskVariable(int variableNumber, int position, ChoisePoint iX) throws SyntaxError {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term attachTermPosition(Term argument, long p) {
		Term[] termArray= new Term[2];
		termArray[0]= argument;
		termArray[1]= new PrologInteger(p);
		return new PrologStructure(SymbolCodes.symbolCode_E_p,termArray);
	}
}
