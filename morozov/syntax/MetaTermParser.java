// (c) 2010-2019 IRE RAS Alexei A. Morozov

package morozov.syntax;

import target.*;

import morozov.run.*;
import morozov.syntax.data.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.syntax.scanner.*;
import morozov.syntax.scanner.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MetaTermParser extends ElementaryParser {
	//
	protected HashMap<String,Integer> variableNameRegister= new HashMap<>();
	protected HashMap<Integer,String> invertedVariableNameRegister= new HashMap<>();
	protected HashMap<Integer,VariableRole> variableRoleRegister= new HashMap<>();
	protected int recentVariableNumber= 0;
	//
	protected ArrayList<Integer> anonymousVariableRegister= new ArrayList<>();
	//
	protected ArrayList<FunctionCallDefinition> functionCallDefinitionArray= new ArrayList<>();
	//
	protected boolean checkClauseVariables= false;
	protected ArrayList<ActorPrologSubgoal> subgoalArray= new ArrayList<>();
	protected boolean isClauseHeadingElement= false;
	protected int headingAsteriskVariableNumber= noVariableNumber;
	//
	protected static String anonymousVariableName= "_";
	protected static int anonymousVariableNumber= 0;
	protected static int noVariableNumber= -1;
	//
	protected static Term metaTermEmptyList= new PrologSymbol(SymbolCodes.symbolCode_E_empty_list);
	protected static Term metaTermEmptySet= new PrologSymbol(SymbolCodes.symbolCode_E_empty_set);
	protected static Term metaTermAnonymousVariable= new PrologStructure(SymbolCodes.symbolCode_E_var,new Term[]{new PrologInteger(anonymousVariableNumber),new PrologSymbol(SymbolCodes.symbolCode_E_plain)});
	//
	protected static String auxiliaryVariableNameFormat= "_V%d";
	protected static Term[] emptyTermArray= new Term[0];
	//
	///////////////////////////////////////////////////////////////
	//
	public MetaTermParser(ParserMasterInterface m, boolean rememberPositions) {
		super(m,rememberPositions);
		// initializeParser();
	}
	public MetaTermParser(ParserMasterInterface m, boolean rememberPositions, boolean implementRobustMode) {
		super(m, rememberPositions,implementRobustMode);
		// initializeParser();
	}
	public MetaTermParser(ParserMasterInterface m) {
		super(m);
		// initializeParser();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// protected void initializeParser() {
	//	variableNameRegister.put(anonymousVariableName,anonymousVariableNumber);
	//	invertedVariableNameRegister.put(anonymousVariableNumber,anonymousVariableName);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public HashMap<String,Integer> getVariableNameRegister() {
		return variableNameRegister;
	}
	public HashMap<Integer,String> getInvertedVariableNameRegister() {
		return invertedVariableNameRegister;
	}
	public HashMap<Integer,VariableRole> getVariableRoleRegister() {
		return variableRoleRegister;
	}
	//
	public void setRecentVariableNumber(int value) {
		recentVariableNumber= value;
	}
	public int getRecentVariableNumber() {
		return recentVariableNumber;
	}
	//
	public ArrayList<FunctionCallDefinition> getFunctionCallDefinitions() {
		return functionCallDefinitionArray;
	}
	public int getFunctionCallDefinitionArraySize() {
		return functionCallDefinitionArray.size();
	}
	//
	public Term getVariableNamesTerm() {
		Term list= PrologEmptyList.instance;
		for (int k=recentVariableNumber; k > anonymousVariableNumber; k--) {
			String name= invertedVariableNameRegister.get(k);
			if (name==null) {
				name= "";
			};
			list= new PrologList(new PrologString(name),list);
		};
		return list;
	}
	//
	public void forgetVariableNames() {
		variableNameRegister.clear();
		invertedVariableNameRegister.clear();
		recentVariableNumber= 0;
		anonymousVariableRegister.clear();
		variableRoleRegister.clear();
	}
	//
	public Term getFunctionCallTableTerm() {
		Term list= PrologEmptyList.instance;
		for (int k=0; k < functionCallDefinitionArray.size(); k++) {
			FunctionCallDefinition definition= functionCallDefinitionArray.get(k);
			list= new PrologList(definition.toActorPrologTerm(),list);
		};
		return list;
	}
	//
	public void clearFunctionCallTable() {
		functionCallDefinitionArray.clear();
	}
	//
	///////////////////////////////////////////////////////////////
	//
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
	protected Term parseStructure(long functorCode, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		// checkWhetherSymbolIsNotASlot(functorCode);
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
			// if (rememberTextPositions) {
			//	result= attachTermPosition(result,internalTerm.getPosition());
			// }
		};
		return result;
	}
	//
	protected Term arrayToMetaTerm(Term[] array) {
		Term result= metaTermEmptyList;
		for (int n=array.length-1; n >= 0; n--) {
			Term internalTerm= array[n];
			Term[] termArray= new Term[2];
			termArray[0]= internalTerm;
			termArray[1]= result;
			result= new PrologStructure(SymbolCodes.symbolCode_E_e,termArray);
			// if (rememberTextPositions) {
			//	result= attachTermPosition(result,internalTerm.getPosition());
			// }
		};
		return result;
	}
	//
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
	protected Term parseSlot(long functorCode, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		if (!symbolIsASlot(functorCode)) {
			master.handleError(new SlotNameIsExpected(beginningOfTerm),iX);
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
	//
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
	//
	protected Term createTermEmptyList() {
		return metaTermEmptyList;
	}
	//
	protected Term parseListTail(int textPosition, ChoisePoint iX) throws SyntaxError {
		return parseVariableOrSlot(iX);
	}
	//
	protected Term createTermListElement(Term internalTerm, Term tail) {
		Term[] termArray= new Term[2];
		termArray[0]= internalTerm;
		termArray[1]= tail;
		return new PrologStructure(SymbolCodes.symbolCode_E_e,termArray);
	}
	//
	protected Term parseWorld(PrologToken token, ChoisePoint iX) throws SyntaxError {
		master.handleError(new UnexpectedToken(token),iX);
		Term result= PrologUnknownValue.instance;
		if (rememberTextPositions) {
			result= attachTermPosition(result,token.getPosition());
		};
		return result;
	}
	//
	protected Term parseVariable(String variableName, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			PrologTokenType frontTokenType= frontToken.getType();
			switch (frontTokenType) {
			case L_BRACE:
				{
///////////////////////////////////////////////////////////////////////
// checkWhetherVariableIsNamed(variableName,beginningOfTerm,iX);
previousExpressionWasAVariable= false;
position++;
ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
Term firstPairValue= parseIsolatedVariable(variableName,beginningOfTerm,iX);
arguments.add(new NamedTerm(0,false,firstPairValue,beginningOfTerm));
return parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
///////////////////////////////////////////////////////////////////////
				}
			case L_SQUARE_BRACKET:
				{
///////////////////////////////////////////////////////////////////////
checkWhetherVariableIsNamed(variableName,beginningOfTerm,iX);
position++;
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
Term[] indices= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
declareNestedFunctionCalls(functionCallDefinitionMarker);
ActorPrologAtom simpleAtom= new ActorPrologAtom(
	SymbolCodes.symbolCode_E_element,
	indices,
	false,
	beginningOfTerm,
	true,
	false,
	true);
int variableNumber= registerRegularVariable(variableName,beginningOfTerm,iX);
if (variableNumber==anonymousVariableNumber) {
	master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
};
ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
	false,
	-1,
	variableNumber,
	simpleAtom,
	beginningOfTerm);
Term auxiliaryVariable= registerFunctionCall(functionCall,iX);
previousExpressionWasAVariable= false;
return auxiliaryVariable;
///////////////////////////////////////////////////////////////////////
				}
			case QUESTION_MARK:
				{
///////////////////////////////////////////////////////////////////////
checkWhetherVariableIsNamed(variableName,beginningOfTerm,iX);
position++;
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
ActorPrologAtom simpleAtom= parseSimpleAtom(iX);
declareNestedFunctionCalls(functionCallDefinitionMarker);
int variableNumber= registerRegularVariable(variableName,beginningOfTerm,iX);
if (variableNumber==anonymousVariableNumber) {
	master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
};
ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
	false,
	-1,
	variableNumber,
	simpleAtom,
	beginningOfTerm);
Term auxiliaryVariable= registerFunctionCall(functionCall,iX);
previousExpressionWasAVariable= false;
return auxiliaryVariable;
///////////////////////////////////////////////////////////////////////
				}
			}
		};
		// if (variableName.equals(anonymousVariableName)) {
		//	previousExpressionWasANamedVariable= false;
		// } else {
		previousExpressionWasAVariable= true;
		// };
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
	protected Term createTermEmptySet() {
		return metaTermEmptySet;
	}
	//
	protected Term createTermSetTail(int textPosition, ChoisePoint iX) throws SyntaxError {
		return parseVariableOrSlot(iX);
	}
	//
	protected Term createTermSetElement(NamedTerm pair, Term tail) {
		Term[] termArray= new Term[3];
		termArray[0]= pair.getKeyTerm();
		termArray[1]= pair.getValue();
		termArray[2]= tail;
		return new PrologStructure(SymbolCodes.symbolCode_E_i,termArray);
	}
	//
	protected NamedTerm parseInestimablePairValue(long pairNameCode, boolean isInQuotes, int pairPosition, ChoisePoint iX) throws SyntaxError {
		if (isInQuotes) {
			registerAnonymousVariable(pairPosition);
			return new NamedTerm(pairNameCode,true,metaTermAnonymousVariable,pairPosition);
		} else if (symbolIsASlot(pairNameCode)) {
			Term[] termArray= new Term[1];
			termArray[0]= new PrologSymbol(pairNameCode);
			Term pairValue= new PrologStructure(SymbolCodes.symbolCode_E_slot,termArray);
			return new NamedTerm(pairNameCode,true,pairValue,pairPosition);
		} else {
			master.handleError(new SymbolShouldBeEnclosedInApostrophesHere(pairPosition),iX);
			return new NamedTerm(pairNameCode,true,metaTermAnonymousVariable,pairPosition);
		}
	}
	protected NamedTerm parseInestimablePairValue(BigInteger key, int pairPosition, ChoisePoint iX) {
		long pairNameCode= PrologInteger.toLong(key);
		registerAnonymousVariable(pairPosition);
		return new NamedTerm(pairNameCode,false,metaTermAnonymousVariable,pairPosition);
	}
	//
	protected Term parseVariableOrSlot(ChoisePoint iX) throws SyntaxError {
		previousExpressionWasAVariable= false;
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			int beginningOfTerm= frontToken.getPosition();
			PrologTokenType frontTokenType= frontToken.getType();
			switch (frontTokenType) {
			case SYMBOL:
///////////////////////////////////////////////////////////////////////
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
return result;
///////////////////////////////////////////////////////////////////////
				// break;
			case VARIABLE:
///////////////////////////////////////////////////////////////////////
position++;
return parseIsolatedVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
///////////////////////////////////////////////////////////////////////
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
	///////////////////////////////////////////////////////////////
	//
	public Term continueExpressionParsing(Term multiplier1, int functionCallDefinitionMarker, ChoisePoint iX) throws SyntaxError {
		previousExpressionWasAVariable= false;
		Term addend1= parseAddend(multiplier1,functionCallDefinitionMarker,iX);
		return parseExpression(addend1,functionCallDefinitionMarker,iX);
	}
	//
	public Term parseExpression(boolean insertMinus, int positionOfMinus, ChoisePoint iX) throws SyntaxError {
		previousExpressionWasAVariable= false;
		int functionCallDefinitionMarker= functionCallDefinitionArray.size();
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
Term auxiliaryVariable= registerFunctionCall(subgoal,iX);
return parseExpression(auxiliaryVariable,functionCallDefinitionMarker,iX);
///////////////////////////////////////////////////////////////////////
		} else {
			return addend1;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term parseAddend(boolean insertMinus, int positionOfMinus, ChoisePoint iX) throws SyntaxError {
		int functionCallDefinitionMarker= functionCallDefinitionArray.size();
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
	Term auxiliaryVariable= registerFunctionCall(subgoal,iX);
	return parseAddend(auxiliaryVariable,functionCallDefinitionMarker,iX);
}
///////////////////////////////////////////////////////////////////////
		} else {
			return multiplier1;
		}
	}
	//
	///////////////////////////////////////////////////////////////
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
				// return parseExpression(iX);
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
				int functionCallDefinitionMarker= functionCallDefinitionArray.size();
				Term expression= parseExpression(false,0,iX);
				declareNestedFunctionCalls(functionCallDefinitionMarker);
				ActorPrologSubgoal subgoal= new ActorPrologNearFunctionCall(
					SymbolCodes.symbolCode__sub,
					new Term[]{expression},
					positionOfMinus);
				parseRightRoundBracket(iX);
				Term auxiliaryVariable= registerFunctionCall(subgoal,iX);
				previousExpressionWasAVariable= false;
				return auxiliaryVariable;
			} else {
				return parseTermAfterMinusIfNecessary(true,positionOfMinus,iX);
				// return parseExpression(iX);
			}
		} else {
			return parseTermAfterMinusIfNecessary(true,positionOfMinus,iX);
			// return parseExpression(iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologAtom parseRootSimpleAtom(ChoisePoint iX) throws SyntaxError {
		return parseSimpleAtom(iX);
	}
	//
	public ActorPrologAtom parseSimpleAtom(ChoisePoint iX) throws SyntaxError {
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
			case STRING:
				{
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
if (p2 < numberOfTokens && tokens[p2].getType()==PrologTokenType.L_BRACE) {
	position= position + 2;
	Term firstPairValue= new PrologString(frontToken.getStringValue(master,iX));
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
			case BINARY:
				{
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
if (p2 < numberOfTokens && tokens[p2].getType()==PrologTokenType.L_BRACE) {
	position= position + 2;
	Term firstPairValue= new PrologBinary(frontToken.getBinaryValue(master,iX));
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
			// checkWhetherVariableIsNamed(variableName,beginningOfTerm,iX);
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
				// checkWhetherVariableIsNamed(variableName,beginningOfTerm,iX);
				position= position + 3;
				int variableNumber= registerMetaFunctorVariable(variableName,beginningOfTerm,iX);
				return new ActorPrologAtom(
					true,
					variableNumber,
					emptyTermArray,
					false,
					beginningOfTerm,
					false,
					false,
					true);
			} else {
				// checkWhetherVariableIsNamed(variableName,beginningOfTerm,iX);
				int variableNumber= registerMetaFunctorVariable(variableName,beginningOfTerm,iX);
				position= position + 2;
				Term[] termArray= parseTermSequence(PrologTokenType.R_ROUND_BRACKET,iX);
				return new ActorPrologAtom(
					true,
					variableNumber,
					termArray,
					lastTermHasAsterisk(),
					beginningOfTerm,
					false,
					false,
					true);
			}
		}
	default:
		break;
	}
};
// checkWhetherVariableIsNamed(variableName,beginningOfTerm,iX);
position++;
int variableNumber= registerMetaPredicateVariable(variableName,beginningOfTerm,iX);
return new ActorPrologAtom(variableNumber,beginningOfTerm);
///////////////////////////////////////////////////////////////////////
				}
				// break;
			default:
				master.handleError(new SimpleAtomIsExpected(beginningOfTerm),iX);
				position++;
				break;
			};
			return new ActorPrologAtom(anonymousVariableNumber,beginningOfTerm);
		} else {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		}
	}
	//
	public ActorPrologAtom parseUnderdeterminedSetSimpleAtom(int position, ChoisePoint iX) throws SyntaxError {
		ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
		Term underdeterminedSet= parseUnderdeterminedSet(arguments,position,iX);
		return new ActorPrologAtom(
			SymbolCodes.symbolCode_E_,
			new Term[]{underdeterminedSet},
			false,
			position,
			true,
			true,
			true);
	}
	public ActorPrologAtom parseUnderdeterminedSetSimpleAtom(Term firstPairValue, int position, ChoisePoint iX) throws SyntaxError {
		ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
		// Term firstPairValue= PrologUnknownValue.instance;
		if (rememberTextPositions) {
			firstPairValue= attachTermPosition(firstPairValue,position);
		};
		arguments.add(new NamedTerm(0,false,firstPairValue,position));
		Term underdeterminedSet= parseUnderdeterminedSet(arguments,position,iX);
		return new ActorPrologAtom(
			SymbolCodes.symbolCode_E_,
			new Term[]{underdeterminedSet},
			false,
			position,
			true,
			true,
			true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term parseFunctionCallInSlot(long symbolCode, int beginningOfTerm, int questionMarkPosition, ChoisePoint iX) throws SyntaxError {
		int functionCallDefinitionMarker= functionCallDefinitionArray.size();
		ActorPrologAtom simpleAtom= parseSimpleAtom(iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			true,
			symbolCode,
			-1,
			simpleAtom,
			beginningOfTerm);
		Term auxiliaryVariable= registerFunctionCall(functionCall,iX);
		previousExpressionWasAVariable= false;
		return auxiliaryVariable;
	}
	//
	protected Term parseTheElementFunctionCall(long symbolCode, int beginningOfTerm, int questionMarkPosition, ChoisePoint iX) throws SyntaxError {
		int functionCallDefinitionMarker= functionCallDefinitionArray.size();
		Term[] indices= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologAtom simpleAtom= new ActorPrologAtom(
			SymbolCodes.symbolCode_E_element,
			indices,
			false,
			beginningOfTerm,
			true,
			false,
			true);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			true,
			symbolCode,
			-1,
			simpleAtom,
			beginningOfTerm);
		Term auxiliaryVariable= registerFunctionCall(functionCall,iX);
		previousExpressionWasAVariable= false;
		return auxiliaryVariable;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologFunctionCall parseRootFunctionCall(ChoisePoint iX) throws SyntaxError {
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
		boolean targetParameterIsASlot= false;
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
			targetVariableNumber= registerRegularVariable(variableName,frontToken.getPosition(),iX);
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
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
Term[] indices= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
declareNestedFunctionCalls(functionCallDefinitionMarker);
// parseRightSquareBracket(iX);
ActorPrologAtom simpleAtom= new ActorPrologAtom(
	SymbolCodes.symbolCode_E_element,
	indices,
	false,
	beginningOfTerm,
	true,
	false,
	true);
if (targetVariableNumber==anonymousVariableNumber) {
	master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
};
return new ActorPrologFunctionCall(
	targetParameterIsASlot,
	targetSlotNameCode,
	targetVariableNumber,
	simpleAtom,
	beginningOfTerm);
///////////////////////////////////////////////////////////////////////
			}
		};
		parseQuestionMark(iX);
		int functionCallDefinitionMarker= functionCallDefinitionArray.size();
		ActorPrologAtom simpleAtom= parseSimpleAtom(iX);
		simpleAtom.checkWhetherFunctorIsNotAnonymousVariable(master,iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		if (targetVariableNumber==anonymousVariableNumber) {
			master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
		};
		return new ActorPrologFunctionCall(
			targetParameterIsASlot,
			targetSlotNameCode,
			targetVariableNumber,
			simpleAtom,
			beginningOfTerm);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologAtom parseRootBinaryRelation(ChoisePoint iX) throws SyntaxError {
		return parseBinaryRelation(iX).toActorPrologAtom();
	}
	//
	public ActorPrologNearSubroutineCall parseBinaryRelation(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken frontToken= tokens[position];
		int beginningOfTerm= frontToken.getPosition();
		Term leftArgument= parseExpression(false,0,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
	}
	public ActorPrologNearSubroutineCall parseBinaryRelation(Term leftArgument, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken frontToken= tokens[position];
		PrologTokenType frontTokenType= frontToken.getType();
		long functorNameCode;
		boolean insertMinus= false;
		switch (frontTokenType) {
		case EQUALITY:
			functorNameCode= SymbolCodes.symbolCode__equality;
			break;
		case ASSIGNMENT:
			functorNameCode= SymbolCodes.symbolCode__assignment;
			break;
		case LT:
			functorNameCode= SymbolCodes.symbolCode__lt;
			break;
		case CONTROL_MESSAGE:
			if (!isAControlMessage(position)) {
				functorNameCode= SymbolCodes.symbolCode__lt;
				insertMinus= true;
				break;
			};
			master.handleError(new RelationalOperatorIsExpected(frontToken.getPosition()),iX);
			functorNameCode= SymbolCodes.symbolCode__lt;
			break;
		case GT:
			functorNameCode= SymbolCodes.symbolCode__gt;
			break;
		case NE:
			functorNameCode= SymbolCodes.symbolCode__ne;
			break;
		case LE:
			functorNameCode= SymbolCodes.symbolCode__le;
			break;
		case GE:
			functorNameCode= SymbolCodes.symbolCode__ge;
			break;
		default:
			master.handleError(new RelationalOperatorIsExpected(frontToken.getPosition()),iX);
			functorNameCode= SymbolCodes.symbolCode__equality;
			break;
		};
		position++;
		Term rightArgument= parseExpression(insertMinus,frontToken.getPosition(),iX);
		return new ActorPrologNearSubroutineCall(
			functorNameCode,
			new Term[]{leftArgument,rightArgument},
			beginningOfTerm,
			false,
			false,
			false);
	}
	//
	public ActorPrologNearSubroutineCall parseBinaryRelationAfterLEandMinus(Term leftArgument, int positionOfMinus, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		Term rightArgument= parseExpression(true,positionOfMinus,iX);
		return new ActorPrologNearSubroutineCall(
			SymbolCodes.symbolCode__lt,
			new Term[]{leftArgument,rightArgument},
			beginningOfTerm,
			false,
			false,
			false);
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE ATOM                                                //
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologAtom parseAtom(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
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
	switch (secondTokenType) {
	case L_SQUARE_BRACKET:
		{
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(iX);
		return relation.toActorPrologAtom();
		}
	case CONTROL_MESSAGE:	// symbol<-...
		if (isAControlMessage(p2)) {
			// master.handleError(new UnexpectedToken(secondToken),iX);
			checkWhetherSymbolIsNotASlot(frontToken,iX);
			return new ActorPrologAtom(
				frontToken.getSymbolCode(master,iX),
				emptyTermArray,
				false,
				beginningOfTerm,
				frontToken.isInQuotes(master,iX),
				false,
				true);
		} else {
			position= position + 2;
			Term leftArgument= parseSymbolOrSlot(frontToken,beginningOfTerm,iX);
			ActorPrologNearSubroutineCall relation= parseBinaryRelationAfterLEandMinus(leftArgument,1+secondToken.getPosition(),beginningOfTerm,iX);
			return relation.toActorPrologAtom();
		}
		// break;
	case QUESTION_MARK:	// symbol?...
		{
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
position= position + 2;
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
ActorPrologAtom leftAtom= parseSimpleAtom(iX);
if (position < numberOfTokens) {
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	switch (thirdTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		long symbolCode= parseTargetSlot(frontToken,iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			true,
			symbolCode,
			-1,
			leftAtom,
			beginningOfTerm);
		Term leftMultiplier= registerFunctionCall(functionCall,iX);
		Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	case EQUALITY:
	case ASSIGNMENT:
	case LT:
	case GT:
	case NE:
	case LE:
	case GE:
		{
		long symbolCode= parseTargetSlot(frontToken,iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			true,
			symbolCode,
			-1,
			leftAtom,
			beginningOfTerm);
		Term leftArgument= registerFunctionCall(functionCall,iX);
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			long symbolCode= parseTargetSlot(frontToken,iX);
			position++;
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
				true,
				symbolCode,
				-1,
				leftAtom,
				beginningOfTerm);
			Term leftArgument= registerFunctionCall(functionCall,iX);
			ActorPrologNearSubroutineCall relation= parseBinaryRelationAfterLEandMinus(leftArgument,1+thirdToken.getPosition(),beginningOfTerm,iX);
			return relation.toActorPrologAtom();
		};
		break;
	default:
		break;
	};
	master.handleError(new RelationalOperatorIsExpected(thirdToken.getPosition()),iX);
	position++;
} else {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		};
		break;
	case MULTIPLY:		// symbol*...
	case DIVIDE:		// symbol/...
	case PLUS:		// symbol+...
	case MINUS:		// symbol-...
		{
		position= position + 1;
		int functionCallDefinitionMarker= functionCallDefinitionArray.size();
		Term leftMultiplier= parseSymbolOrSlot(frontToken,beginningOfTerm,iX);
		Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	case EQUALITY:		// symbol==...
	case ASSIGNMENT:	// symbol:=...
	case LT:		// symbol<...
	case GT:		// symbol>...
	case NE:		// symbol<>...
	case LE:		// symbol<=...
	case GE:		// symbol>=...
		{
		position= position + 1;
		Term leftArgument= parseSymbolOrSlot(frontToken,beginningOfTerm,iX);
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	default:
		break;
	};
	ActorPrologNearSubroutineCall relation= parseSimpleAtomOrSpecialCaseOfBinaryRelation(iX);
	return relation.toActorPrologAtom();
};
checkWhetherSymbolIsNotASlot(frontToken,iX);
return new ActorPrologAtom(
	frontToken.getSymbolCode(master,iX),
	emptyTermArray,
	false,
	beginningOfTerm,
	frontToken.isInQuotes(master,iX),
	false,
	true);
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
	switch (secondTokenType) {
	case L_SQUARE_BRACKET:
		{
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(iX);
		return relation.toActorPrologAtom();
		}
	case CONTROL_MESSAGE:	// Variable<-...
		{
			if (isAControlMessage(p2)) {
				int variableNumber= registerMetaPredicateVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
				return new ActorPrologAtom(variableNumber,beginningOfTerm);
			} else {
				int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
				position= position + 2;
				Term leftArgument= declareUnconstrainedVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
				ActorPrologNearSubroutineCall relation= parseBinaryRelationAfterLEandMinus(leftArgument,1+secondToken.getPosition(),beginningOfTerm,iX);
				return relation.toActorPrologAtom();
			}
		}
		// break;
	case QUESTION_MARK:
		{
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
position= position + 2;
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
ActorPrologAtom leftAtom= parseSimpleAtom(iX);
if (position < numberOfTokens) {
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	switch (thirdTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),frontToken.getPosition(),iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		if (variableNumber==anonymousVariableNumber) {
			master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
		};
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			false,
			-1,
			variableNumber,
			leftAtom,
			beginningOfTerm);
		Term leftMultiplier= registerFunctionCall(functionCall,iX);
		Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	case EQUALITY:
	case ASSIGNMENT:
	case LT:
	case GT:
	case NE:
	case LE:
	case GE:
		{
		int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),frontToken.getPosition(),iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		if (variableNumber==anonymousVariableNumber) {
			master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
		};
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			false,
			-1,
			variableNumber,
			leftAtom,
			beginningOfTerm);
		Term leftArgument= registerFunctionCall(functionCall,iX);
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),frontToken.getPosition(),iX);
			position++;
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			if (variableNumber==anonymousVariableNumber) {
				master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
			};
			ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
				false,
				-1,
				variableNumber,
				leftAtom,
				beginningOfTerm);
			Term leftArgument= registerFunctionCall(functionCall,iX);
			ActorPrologNearSubroutineCall relation= parseBinaryRelationAfterLEandMinus(leftArgument,1+thirdToken.getPosition(),beginningOfTerm,iX);
			return relation.toActorPrologAtom();
		};
		break;
	default:
		break;
	};
	master.handleError(new RelationalOperatorIsExpected(thirdToken.getPosition()),iX);
	position++;
} else {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		};
		break;
	default:
		break;
	};
	ActorPrologNearSubroutineCall relation= parseSimpleAtomOrSpecialCaseOfBinaryRelation(iX);
	return relation.toActorPrologAtom();
};
int variableNumber= registerMetaPredicateVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
return new ActorPrologAtom(variableNumber,beginningOfTerm);
///////////////////////////////////////////////////////////////////////
			}
			// break;
		case QUESTION_MARK:
			{
///////////////////////////////////////////////////////////////////////
position++;
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
ActorPrologAtom leftAtom= parseSimpleAtom(iX);
if (position < numberOfTokens) {
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	switch (thirdTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			true,
			SymbolCodes.symbolCode_E_self,
			-1,
			leftAtom,
			beginningOfTerm);
		Term leftMultiplier= registerFunctionCall(functionCall,iX);
		Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	case EQUALITY:
	case ASSIGNMENT:
	case LT:
	case GT:
	case NE:
	case LE:
	case GE:
		{
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			true,
			SymbolCodes.symbolCode_E_self,
			-1,
			leftAtom,
			beginningOfTerm);
		Term leftArgument= registerFunctionCall(functionCall,iX);
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			position++;
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
				true,
				SymbolCodes.symbolCode_E_self,
				-1,
				leftAtom,
				beginningOfTerm);
			Term leftArgument= registerFunctionCall(functionCall,iX);
			ActorPrologNearSubroutineCall relation= parseBinaryRelationAfterLEandMinus(leftArgument,1+thirdToken.getPosition(),beginningOfTerm,iX);
			return relation.toActorPrologAtom();
		};
		break;
	default:
		break;
	}
};
return leftAtom;
///////////////////////////////////////////////////////////////////////
			}
			// break;
		case L_BRACE:
			{
///////////////////////////////////////////////////////////////////////
position++;
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
Term leftArgument= parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
if (position < numberOfTokens) {
	PrologToken secondToken= tokens[position];
	PrologTokenType secondTokenType= secondToken.getType();
	switch (secondTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		leftArgument= continueExpressionParsing(leftArgument,functionCallDefinitionMarker,iX);
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	case EQUALITY:
	case ASSIGNMENT:
	case LT:
	case GT:
	case NE:
	case LE:
	case GE:
		{
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			position++;
			ActorPrologNearSubroutineCall relation= parseBinaryRelationAfterLEandMinus(leftArgument,1+secondToken.getPosition(),beginningOfTerm,iX);
			return relation.toActorPrologAtom();
		};
		break;
	default:
		break;
	}
};
ActorPrologAtom atom= new ActorPrologAtom(
	SymbolCodes.symbolCode_E_,
	new Term[]{leftArgument},
	false,
	beginningOfTerm,
	true,
	true,
	true);
return atom;
///////////////////////////////////////////////////////////////////////
			}
			// break;
		default:
			break;
		};
		int p2= position + 1;
		if (p2 < numberOfTokens) {
			PrologToken secondToken= tokens[p2];
			if (secondToken.getType()==PrologTokenType.L_BRACE) {
///////////////////////////////////////////////////////////////////////
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
PrologToken keyToken= tokens[position];
int beginningOfKeyToken= keyToken.getPosition();
PrologTokenType keyTokenType= keyToken.getType();
Term firstPairValue;
switch (keyTokenType) {
case SYMBOL:
	firstPairValue= new PrologSymbol(keyToken.getSymbolCode(master,iX));
	break;
case INTEGER:
	firstPairValue= new PrologInteger(keyToken.getIntegerValue(master,iX));
	break;
case REAL:
	firstPairValue= new PrologReal(keyToken.getRealValue(master,iX));
	break;
case STRING:
	firstPairValue= new PrologString(keyToken.getStringValue(master,iX));
	break;
case BINARY:
	firstPairValue= new PrologBinary(keyToken.getBinaryValue(master,iX));
	break;
case NUMBER_SIGN:
	firstPairValue= PrologUnknownValue.instance;
	break;
case VARIABLE:
	String variableName= keyToken.getVariableName(master,iX);
	// checkWhetherVariableIsNamed(variableName,beginningOfKeyToken,iX);
	firstPairValue= parseIsolatedVariable(variableName,beginningOfKeyToken,iX);
	break;
default:
	master.handleError(new UnexpectedToken(keyToken),iX);
	position++;
	firstPairValue= PrologUnknownValue.instance;
	break;
};
if (rememberTextPositions) {
	firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
};
position= position + 2;
ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
arguments.add(new NamedTerm(0,false,firstPairValue,beginningOfTerm));
Term leftArgument= parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
if (position < numberOfTokens) {
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	switch (thirdTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		leftArgument= continueExpressionParsing(leftArgument,functionCallDefinitionMarker,iX);
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	case EQUALITY:
	case ASSIGNMENT:
	case LT:
	case GT:
	case NE:
	case LE:
	case GE:
		{
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			position++;
			ActorPrologNearSubroutineCall relation= parseBinaryRelationAfterLEandMinus(leftArgument,1+thirdToken.getPosition(),beginningOfTerm,iX);
			return relation.toActorPrologAtom();
		};
		break;
	default:
		break;
	}
};
ActorPrologAtom atom= new ActorPrologAtom(
	SymbolCodes.symbolCode_E_,
	new Term[]{leftArgument},
	false,
	beginningOfTerm,
	true,
	true,
	true);
return atom;
///////////////////////////////////////////////////////////////////////
			}
		};
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(iX);
		return relation.toActorPrologAtom();
	}
	//
	///////////////////////////////////////////////////////////////
	// PARSE SUBGOAL                                             //
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologSubgoal parseSubgoal(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken frontToken= tokens[position];
		int beginningOfTerm= frontToken.getPosition();
		PrologTokenType frontTokenType= frontToken.getType();
		switch (frontTokenType) {
		case EXCLAM:
			{
///////////////////////////////////////////////////////////////////////
position++;
return new ActorPrologBuiltInCommand(
	SymbolCodes.symbolCode__exclam,
	emptyTermArray,
	beginningOfTerm);
///////////////////////////////////////////////////////////////////////
			}
			// break;
		case L_SQUARE_BRACKET:	// [...
			{
///////////////////////////////////////////////////////////////////////
position++;
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
int p3= position + 2;
if (p3 < numberOfTokens) {
	int p2= position + 1;
	// [*]...
	if (tokens[p2].getType()==PrologTokenType.R_SQUARE_BRACKET) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
boolean thisIsAsyncMessage= false;
boolean thisIsControlMessage= false;
PrologToken tokenP3= tokens[p3];
PrologTokenType tokenTypeP3= tokenP3.getType();
switch (tokenTypeP3) {
case L_SQUARE_BRACKET:	// [*][...
	int p5= position + 4;
	if (p5 < numberOfTokens) {
		// [*][*]...
		if (tokens[p5].getType()==PrologTokenType.R_SQUARE_BRACKET) {
//=====================================================================
int p4= position + 3;
PrologToken tokenP4= tokens[p4];
PrologTokenType tokenTypeP4= tokenP4.getType();
switch (tokenTypeP4) {
case DATA_MESSAGE:	// [*][<<]...
	thisIsAsyncMessage= true;
	thisIsControlMessage= false;
	break;
case CONTROL_MESSAGE:	// [*][<-]...
	thisIsAsyncMessage= true;
	thisIsControlMessage= true;
	break;
default:
	break;
};
if (thisIsAsyncMessage) {
	PrologToken tokenP1= tokens[position];
	PrologTokenType tokenTypeP1= tokenP1.getType();
	switch (tokenTypeP1) {
	case SYMBOL:
		{
		long symbolCode= parseTargetSlot(tokenP1,iX);
		position= position + 5;
		ActorPrologAtom simpleAtom= parseSimpleAtom(iX);
		return new ActorPrologSubroutineCall(
			true,
			symbolCode,
			-1,
			new ActorPrologMessageType(thisIsControlMessage,false,true),
			simpleAtom,
			beginningOfTerm);
		}
	case VARIABLE:
		{
		int variableNumber= registerRegularVariable(tokenP1.getVariableName(master,iX),tokenP1.getPosition(),iX);
		position= position + 5;
		ActorPrologAtom simpleAtom= parseSimpleAtom(iX);
		if (variableNumber==anonymousVariableNumber) {
			master.handleError(new AnonymousVariableCannotBeTargetParameter(tokenP1.getPosition()),iX);
		};
		return new ActorPrologSubroutineCall(
			false,
			-1,
			variableNumber,
			new ActorPrologMessageType(thisIsControlMessage,false,true),
			simpleAtom,
			beginningOfTerm);
		}
	default:
		master.handleError(new TargetParameterIsExpected(tokenP1.getPosition()),iX);
		position++;
		break;
	}
}
//=====================================================================
		}
	};
	thisIsAsyncMessage= false;
	thisIsControlMessage= false;
	break;
case DATA_MESSAGE:	// [*]<<...
	thisIsAsyncMessage= true;
	thisIsControlMessage= false;
	break;
case CONTROL_MESSAGE:	// [*]<-...
	if (isAControlMessage(p3)) {
		thisIsAsyncMessage= true;
		thisIsControlMessage= true;
	} else {
		thisIsAsyncMessage= false;
		thisIsControlMessage= false;
	};
	break;
default:
	break;
};
if (thisIsAsyncMessage) {
	PrologToken tokenP1= tokens[position];
	PrologTokenType tokenTypeP1= tokenP1.getType();
	switch (tokenTypeP1) {
	case SYMBOL:
		{
		long symbolCode= parseTargetSlot(tokenP1,iX);
		position= position + 3;
		ActorPrologAtom simpleAtom= parseSimpleAtom(iX);
		return new ActorPrologSubroutineCall(
			true,
			symbolCode,
			-1,
			new ActorPrologMessageType(thisIsControlMessage,false,false),
			simpleAtom,
			beginningOfTerm);
		}
	case VARIABLE:
		{
		int variableNumber= registerRegularVariable(tokenP1.getVariableName(master,iX),tokenP1.getPosition(),iX);
		position= position + 3;
		ActorPrologAtom simpleAtom= parseSimpleAtom(iX);
		if (variableNumber==anonymousVariableNumber) {
			master.handleError(new AnonymousVariableCannotBeTargetParameter(tokenP1.getPosition()),iX);
		};
		return new ActorPrologSubroutineCall(
			false,
			-1,
			variableNumber,
			new ActorPrologMessageType(thisIsControlMessage,false,false),
			simpleAtom,
			beginningOfTerm);
		}
	default:
		master.handleError(new TargetParameterIsExpected(tokenP1.getPosition()),iX);
		position++;
		break;
	}
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	}
};
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
Term[] arguments= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
if (position < numberOfTokens) {
	PrologToken secondToken= tokens[position];
	PrologTokenType secondTokenType= secondToken.getType();
	switch (secondTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		Term multiplier1= arrayToMetaTerm(arguments);
		Term leftArgument= continueExpressionParsing(multiplier1,functionCallDefinitionMarker,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	case EQUALITY:
	case ASSIGNMENT:
	case LT:
	case GT:
	case NE:
	case LE:
	case GE:
		{
		Term leftArgument= arrayToMetaTerm(arguments);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			position++;
			Term leftArgument= arrayToMetaTerm(arguments);
			return parseBinaryRelationAfterLEandMinus(leftArgument,1+secondToken.getPosition(),beginningOfTerm,iX);
		};
		break;
	default:
		break;
	}
};
return new ActorPrologBuiltInCommand(
	SymbolCodes.symbolCode_E_copy,
	arguments,
	beginningOfTerm);
///////////////////////////////////////////////////////////////////////
			}
			// break;
		case SYMBOL:
			{
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
if (p2 < numberOfTokens) {
	boolean thisIsAsyncMessage= false;
	boolean thisIsControlMessage= false;
	PrologToken secondToken= tokens[p2];
	PrologTokenType secondTokenType= secondToken.getType();
	switch (secondTokenType) {
	case L_SQUARE_BRACKET:	// symbol[...
		int p4= position + 3;
		if (p4 < numberOfTokens) {
			// symbol[*]...
			if (tokens[p4].getType()==PrologTokenType.R_SQUARE_BRACKET) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
int p3= position + 2;
PrologToken tokenP3= tokens[p3];
PrologTokenType tokenTypeP3= tokenP3.getType();
switch (tokenTypeP3) {
case DATA_MESSAGE:	// symbol[<<]...
	thisIsAsyncMessage= true;
	thisIsControlMessage= false;
	break;
case CONTROL_MESSAGE:	// symbol[<-]...
	thisIsAsyncMessage= true;
	thisIsControlMessage= true;
	break;
default:
	break;
};
if (thisIsAsyncMessage) {
	long symbolCode= parseTargetSlot(frontToken,iX);
	position= position + 4;
	ActorPrologAtom simpleAtom= parseSimpleAtom(iX);
	return new ActorPrologSubroutineCall(
		true,
		symbolCode,
		-1,
		new ActorPrologMessageType(thisIsControlMessage,true,true),
		simpleAtom,
		beginningOfTerm);
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			}
		};
		thisIsAsyncMessage= false;
		thisIsControlMessage= false;
		return parseBinaryRelation(iX);
	case DATA_MESSAGE:	// symbol<<...
		thisIsAsyncMessage= true;
		thisIsControlMessage= false;
		break;
	case CONTROL_MESSAGE:	// symbol<-...
		if (isAControlMessage(p2)) {
			thisIsAsyncMessage= true;
			thisIsControlMessage= true;
		} else {
			/*
			thisIsAsyncMessage= false;
			thisIsControlMessage= false;
			Test #115:
			Text: my_slot<-100
			Error: class morozov.syntax.errors.SlotNameIsNotAllowedHere(position:0)
			False negative!
			*/
			position= position + 2;
			Term leftArgument= parseSymbolOrSlot(frontToken,beginningOfTerm,iX);
			return parseBinaryRelationAfterLEandMinus(leftArgument,1+secondToken.getPosition(),beginningOfTerm,iX);
		};
		break;
	case QUESTION_MARK:	// symbol?...
		{
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
position= position + 2;
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
ActorPrologAtom leftAtom= parseSimpleAtom(iX);
if (position < numberOfTokens) {
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	switch (thirdTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		long symbolCode= parseTargetSlot(frontToken,iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			true,
			symbolCode,
			-1,
			leftAtom,
			beginningOfTerm);
		Term leftMultiplier= registerFunctionCall(functionCall,iX);
		Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	case EQUALITY:
	case ASSIGNMENT:
	case LT:
	case GT:
	case NE:
	case LE:
	case GE:
		{
		long symbolCode= parseTargetSlot(frontToken,iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			true,
			symbolCode,
			-1,
			leftAtom,
			beginningOfTerm);
		Term leftArgument= registerFunctionCall(functionCall,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			long symbolCode= parseTargetSlot(frontToken,iX);
			position++;
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
				true,
				symbolCode,
				-1,
				leftAtom,
				beginningOfTerm);
			Term leftArgument= registerFunctionCall(functionCall,iX);
			return parseBinaryRelationAfterLEandMinus(leftArgument,1+thirdToken.getPosition(),beginningOfTerm,iX);
		};
		break;
	default:
		break;
	}
};
long symbolCode= parseTargetSlot(frontToken,iX);
return new ActorPrologSubroutineCall(
	true,
	symbolCode,
	-1,
	new ActorPrologPlainSubgoalType(false),
	leftAtom,
	leftAtom.getPosition());
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		}
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		position= position + 1;
		int functionCallDefinitionMarker= functionCallDefinitionArray.size();
		Term leftMultiplier= parseSymbolOrSlot(frontToken,beginningOfTerm,iX);
		Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	case EQUALITY:
	case ASSIGNMENT:
	case LT:
	case GT:
	case NE:
	case LE:
	case GE:
		{
		position= position + 1;
		Term leftArgument= parseSymbolOrSlot(frontToken,beginningOfTerm,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	default:
		break;
	};
	if (thisIsAsyncMessage) {
		long symbolCode= parseTargetSlot(frontToken,iX);
		position= position + 2;
		ActorPrologAtom simpleAtom= parseSimpleAtom(iX);
		return new ActorPrologSubroutineCall(
			true,
			symbolCode,
			-1,
			new ActorPrologMessageType(thisIsControlMessage,true,false),
			simpleAtom,
			simpleAtom.getPosition());
	} else {
		return parseSimpleAtomOrSpecialCaseOfBinaryRelation(iX);
	}
}
///////////////////////////////////////////////////////////////////////
			};
			break;
		case VARIABLE:
			{
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
if (p2 < numberOfTokens) {
	boolean thisIsAsyncMessage= false;
	boolean thisIsControlMessage= false;
	PrologToken secondToken= tokens[p2];
	PrologTokenType secondTokenType= secondToken.getType();
	switch (secondTokenType) {
	case L_SQUARE_BRACKET:
		int p4= position + 3;
		if (p4 < numberOfTokens) {
			if (tokens[p4].getType()==PrologTokenType.R_SQUARE_BRACKET) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
int p3= position + 2;
PrologToken tokenP3= tokens[p3];
PrologTokenType tokenTypeP3= tokenP3.getType();
switch (tokenTypeP3) {
case DATA_MESSAGE:	// Variable[<<]...
	thisIsAsyncMessage= true;
	thisIsControlMessage= false;
	break;
case CONTROL_MESSAGE:	// Variable[<-]...
	thisIsAsyncMessage= true;
	thisIsControlMessage= true;
	break;
default:
	break;
};
if (thisIsAsyncMessage) {
	int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),frontToken.getPosition(),iX);
	position= position + 4;
	ActorPrologAtom simpleAtom= parseSimpleAtom(iX);
	if (variableNumber==anonymousVariableNumber) {
		master.handleError(new AnonymousVariableCannotBeTargetParameter(frontToken.getPosition()),iX);
	};
	return new ActorPrologSubroutineCall(
		false,
		-1,
		variableNumber,
		new ActorPrologMessageType(thisIsControlMessage,true,true),
		simpleAtom,
		beginningOfTerm);
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			}
		};
		thisIsAsyncMessage= false;
		thisIsControlMessage= false;
		return parseBinaryRelation(iX);
	case DATA_MESSAGE:	// Variable<<...
		thisIsAsyncMessage= true;
		thisIsControlMessage= false;
		break;
	case CONTROL_MESSAGE:	// Variable<-...
		if (isAControlMessage(p2)) {
			thisIsAsyncMessage= true;
			thisIsControlMessage= true;
		} else {
			thisIsAsyncMessage= false;
			thisIsControlMessage= false;
		};
		break;
	case QUESTION_MARK:
		{
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
position= position + 2;
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
ActorPrologAtom leftAtom= parseSimpleAtom(iX);
if (position < numberOfTokens) {
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	switch (thirdTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),frontToken.getPosition(),iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		if (variableNumber==anonymousVariableNumber) {
			master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
		};
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			false,
			-1,
			variableNumber,
			leftAtom,
			beginningOfTerm);
		Term leftMultiplier= registerFunctionCall(functionCall,iX);
		Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	case EQUALITY:
	case ASSIGNMENT:
	case LT:
	case GT:
	case NE:
	case LE:
	case GE:
		{
		int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),frontToken.getPosition(),iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		if (variableNumber==anonymousVariableNumber) {
			master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
		};
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			false,
			-1,
			variableNumber,
			leftAtom,
			beginningOfTerm);
		Term leftArgument= registerFunctionCall(functionCall,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),frontToken.getPosition(),iX);
			position++;
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			if (variableNumber==anonymousVariableNumber) {
				master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
			};
			ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
				false,
				-1,
				variableNumber,
				leftAtom,
				beginningOfTerm);
			Term leftArgument= registerFunctionCall(functionCall,iX);
			return parseBinaryRelationAfterLEandMinus(leftArgument,1+thirdToken.getPosition(),beginningOfTerm,iX);
		};
		break;
	default:
		break;
	}
};
int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),frontToken.getPosition(),iX);
if (variableNumber==anonymousVariableNumber) {
	master.handleError(new AnonymousVariableCannotBeTargetParameter(frontToken.getPosition()),iX);
};
return new ActorPrologSubroutineCall(
	false,
	-1,
	variableNumber,
	new ActorPrologPlainSubgoalType(false),
	leftAtom,
	leftAtom.getPosition());
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		}
	default:
		break;
	};
	if (thisIsAsyncMessage) {
		int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),frontToken.getPosition(),iX);
		position= position + 2;
		ActorPrologAtom simpleAtom= parseSimpleAtom(iX);
		if (variableNumber==anonymousVariableNumber) {
			master.handleError(new AnonymousVariableCannotBeTargetParameter(frontToken.getPosition()),iX);
		};
		return new ActorPrologSubroutineCall(
			false,
			-1,
			variableNumber,
			new ActorPrologMessageType(thisIsControlMessage,true,false),
			simpleAtom,
			simpleAtom.getPosition());
	} else {
		ActorPrologNearSubroutineCall relation= parseSimpleAtomOrSpecialCaseOfBinaryRelation(iX);
		relation.checkWhetherFunctorIsNotAnonymousVariable(master,iX);
		return relation;
	}
}
///////////////////////////////////////////////////////////////////////
			};
			break;
		case QUESTION_MARK:
			{
///////////////////////////////////////////////////////////////////////
position++;
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
ActorPrologAtom leftAtom= parseSimpleAtom(iX);
if (position < numberOfTokens) {
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	switch (thirdTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			true,
			SymbolCodes.symbolCode_E_self,
			-1,
			leftAtom,
			beginningOfTerm);
		Term leftMultiplier= registerFunctionCall(functionCall,iX);
		Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	case EQUALITY:
	case ASSIGNMENT:
	case LT:
	case GT:
	case NE:
	case LE:
	case GE:
		{
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			true,
			SymbolCodes.symbolCode_E_self,
			-1,
			leftAtom,
			beginningOfTerm);
		Term leftArgument= registerFunctionCall(functionCall,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			position++;
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
				true,
				SymbolCodes.symbolCode_E_self,
				-1,
				leftAtom,
				beginningOfTerm);
			Term leftArgument= registerFunctionCall(functionCall,iX);
			return parseBinaryRelationAfterLEandMinus(leftArgument,1+thirdToken.getPosition(),beginningOfTerm,iX);
		};
		break;
	default:
		break;
	}
};
return new ActorPrologNearSubroutineCall(leftAtom);
///////////////////////////////////////////////////////////////////////
			}
			// break;
		case L_BRACE:
			{
///////////////////////////////////////////////////////////////////////
position++;
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
Term leftArgument= parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
if (position < numberOfTokens) {
	PrologToken secondToken= tokens[position];
	PrologTokenType secondTokenType= secondToken.getType();
	switch (secondTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		leftArgument= continueExpressionParsing(leftArgument,functionCallDefinitionMarker,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
	case EQUALITY:
	case ASSIGNMENT:
	case LT:
	case GT:
	case NE:
	case LE:
	case GE:
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			position++;
			return parseBinaryRelationAfterLEandMinus(leftArgument,1+secondToken.getPosition(),beginningOfTerm,iX);
		};
		break;
	default:
		break;
	}
};
ActorPrologAtom atom= new ActorPrologAtom(
	SymbolCodes.symbolCode_E_,
	new Term[]{leftArgument},
	false,
	beginningOfTerm,
	true,
	true,
	true);
return new ActorPrologNearSubroutineCall(atom);
///////////////////////////////////////////////////////////////////////
			}
			// break;
		default:
			break;
		};
		int p2= position + 1;
		if (p2 < numberOfTokens) {
			PrologToken secondToken= tokens[p2];
			if (secondToken.getType()==PrologTokenType.L_BRACE) {
///////////////////////////////////////////////////////////////////////
int functionCallDefinitionMarker= functionCallDefinitionArray.size();
PrologToken keyToken= tokens[position];
int beginningOfKeyToken= keyToken.getPosition();
PrologTokenType keyTokenType= keyToken.getType();
Term firstPairValue;
switch (keyTokenType) {
case SYMBOL:
	firstPairValue= new PrologSymbol(keyToken.getSymbolCode(master,iX));
	break;
case INTEGER:
	firstPairValue= new PrologInteger(keyToken.getIntegerValue(master,iX));
	break;
case REAL:
	firstPairValue= new PrologReal(keyToken.getRealValue(master,iX));
	break;
case STRING:
	firstPairValue= new PrologString(keyToken.getStringValue(master,iX));
	break;
case BINARY:
	firstPairValue= new PrologBinary(keyToken.getBinaryValue(master,iX));
	break;
case NUMBER_SIGN:
	firstPairValue= PrologUnknownValue.instance;
	break;
case VARIABLE:
	String variableName= keyToken.getVariableName(master,iX);
	// checkWhetherVariableIsNamed(variableName,beginningOfKeyToken,iX);
	firstPairValue= parseIsolatedVariable(variableName,beginningOfKeyToken,iX);
	break;
default:
	master.handleError(new UnexpectedToken(keyToken),iX);
	position++;
	firstPairValue= PrologUnknownValue.instance;
	break;
};
if (rememberTextPositions) {
	firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
};
position= position + 2;
ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
arguments.add(new NamedTerm(0,false,firstPairValue,beginningOfTerm));
Term leftArgument= parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
if (position < numberOfTokens) {
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	switch (thirdTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		leftArgument= continueExpressionParsing(leftArgument,functionCallDefinitionMarker,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
	case EQUALITY:
	case ASSIGNMENT:
	case LT:
	case GT:
	case NE:
	case LE:
	case GE:
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			position++;
			return parseBinaryRelationAfterLEandMinus(leftArgument,1+thirdToken.getPosition(),beginningOfTerm,iX);
		};
		break;
	default:
		break;
	}
};
ActorPrologAtom atom= new ActorPrologAtom(
	SymbolCodes.symbolCode_E_,
	new Term[]{leftArgument},
	false,
	beginningOfTerm,
	true,
	true,
	true);
return new ActorPrologNearSubroutineCall(atom);
///////////////////////////////////////////////////////////////////////
			}
		};
		return parseBinaryRelation(iX);
	}
	//
	protected ActorPrologNearSubroutineCall parseSimpleAtomOrSpecialCaseOfBinaryRelation(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken frontToken= tokens[position];
		int beginningOfTerm= frontToken.getPosition();
		int functionCallDefinitionMarker= functionCallDefinitionArray.size();
		ActorPrologAtom leftAtom= parseSimpleAtom(iX);
		if (position < numberOfTokens) {
			PrologToken secondToken= tokens[position];
			PrologTokenType secondTokenType= secondToken.getType();
			switch (secondTokenType) {
			case MULTIPLY:
			case DIVIDE:
			case PLUS:
			case MINUS:
				{
				Term leftMultiplier= leftAtom.toPlainTerm(master,iX);
				if (rememberTextPositions) {
					leftMultiplier= attachTermPosition(leftMultiplier,beginningOfTerm);
				};
				Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
				return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
				}
			case EQUALITY:
			case ASSIGNMENT:
			case LT:
			case GT:
			case NE:
			case LE:
			case GE:
				{
				Term leftArgument= leftAtom.toPlainTerm(master,iX);
				if (rememberTextPositions) {
					leftArgument= attachTermPosition(leftArgument,beginningOfTerm);
				};
				return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
				}
			case CONTROL_MESSAGE:
				if (!isAControlMessage(position)) {
					position++;
					Term leftArgument= leftAtom.toPlainTerm(master,iX);
					if (rememberTextPositions) {
						leftArgument= attachTermPosition(leftArgument,beginningOfTerm);
					};
					return parseBinaryRelationAfterLEandMinus(leftArgument,1+secondToken.getPosition(),beginningOfTerm,iX);
				};
				break;
			default:
				break;
			}
		};
		return new ActorPrologNearSubroutineCall(leftAtom);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public ActorPrologClause parseClause(ChoisePoint iX) throws SyntaxError {
		try {
			checkClauseVariables= true;
			headingAsteriskVariableNumber= noVariableNumber;
			return doParseClause(iX);
		} finally {
			checkClauseVariables= false;
			isClauseHeadingElement= false;
		}
	}
	public ActorPrologClause doParseClause(ChoisePoint iX) throws SyntaxError {
		anonymousVariableRegister.clear();
		variableRoleRegister.clear();
		subgoalArray.clear();
		functionCallDefinitionArray.clear();
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		int beginningOfTheClause= position;
		isClauseHeadingElement= true;
		ActorPrologAtom headingSimpleAtom= parseAtom(iX);
		isClauseHeadingElement= false;
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		boolean isFunction= false;
		Term functionResult= null;
		if (headingSimpleAtom.isSimple()) {
			PrologToken secondToken= tokens[position];
			PrologTokenType secondTokenType= secondToken.getType();
			if (secondTokenType==PrologTokenType.EQ) {
				position++;
				functionResult= parseExpression(false,0,iX);
				headingSimpleAtom.insertArgument(functionResult,master,iX);
				isFunction= true;
			}
		};
		implementFunctionCallDefinitions(iX);
		// functionCallDefinitionArray.clear();
		ActorPrologSubgoal[] restSubgoals= subgoalArray.toArray(new ActorPrologSubgoal[0]);
		subgoalArray.clear();
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken thirdToken= tokens[position];
		PrologTokenType thirdTokenType= thirdToken.getType();
		switch (thirdTokenType) {
		case DOT:
			position++;
			for (int k=0; k < restSubgoals.length; k++) {
				subgoalArray.add(restSubgoals[k]);
			};
			checkSingleVariables(iX);
			return new ActorPrologClause(
				isFunction,
				headingSimpleAtom,
				subgoalArray.toArray(new ActorPrologSubgoal[0]),
				beginningOfTheClause);
		case IMPLICATION:
			position++;
			while (true) {
				ActorPrologSubgoal subgoal= parseSubgoal(iX);
				implementFunctionCallDefinitions(iX);
				// functionCallDefinitionArray.clear();
				subgoalArray.add(subgoal);
				if (position >= numberOfTokens) {
					throw master.handleUnexpectedEndOfTokenList(tokens,iX);
				};
				PrologToken fourthToken= tokens[position];
				PrologTokenType fourthTokenType= fourthToken.getType();
				switch (fourthTokenType) {
				case DOT:
					position++;
					for (int k=0; k < restSubgoals.length; k++) {
						subgoalArray.add(restSubgoals[k]);
					};
					checkSingleVariables(iX);
					return new ActorPrologClause(
						isFunction,
						headingSimpleAtom,
						subgoalArray.toArray(new ActorPrologSubgoal[0]),
						beginningOfTheClause);
				case COMMA:
					position++;
					continue;
				default:
					master.handleError(new CommaIsExpected(fourthToken.getPosition()),iX);
					position++;
					break;
				}
			}
		case COLON:
			position++;
			parseLeftSquareBracket(iX);
			parseExternal(iX);
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			String externalProcedureName;
			PrologToken nextToken= tokens[position];
			if (nextToken.getType()==PrologTokenType.STRING) {
				position++;
				externalProcedureName= nextToken.getStringValue(master,iX);
			} else {
				externalProcedureName= "";
			};
			parseRightSquareBracket(iX);
			parseDot(iX);
			checkExternalClauseIterantAndFunctionVariables(iX);
			checkExternalClauseAnonymousVariables(iX);
			checkWhetherArgumentsAreVariables(headingSimpleAtom,iX);
			return new ActorPrologClause(
				isFunction,
				headingSimpleAtom,
				externalProcedureName,
				beginningOfTheClause);
		default:
			master.handleError(new ImplicationIsExpected(thirdToken.getPosition()),iX);
			position++;
			for (int k=0; k < restSubgoals.length; k++) {
				subgoalArray.add(restSubgoals[k]);
			};
			checkSingleVariables(iX);
			return new ActorPrologClause(
				isFunction,
				headingSimpleAtom,
				subgoalArray.toArray(new ActorPrologSubgoal[0]),
				beginningOfTheClause);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term registerFunctionCall(ActorPrologSubgoal mainSubgoal, ChoisePoint iX) throws SyntaxError {
		int beginningOfTerm= mainSubgoal.getPosition();
		if (iX != null) {
			// iX.pushTrail(new MetaTermParserState(this));
			int variableNumber1= registerPrimaryFunctionVariable(beginningOfTerm,iX);
			// mainSubgoal.assignFunctionVariable(variableNumber1);
			functionCallDefinitionArray.add(
				new FunctionCallDefinition(
					mainSubgoal,
					variableNumber1,
					isClauseHeadingElement,
					beginningOfTerm));
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
	protected void declareNestedFunctionCalls(int functionCallDefinitionMarker) throws SyntaxError {
		for (int k=functionCallDefinitionMarker; k < functionCallDefinitionArray.size(); k++) {
			functionCallDefinitionArray.get(k).setIsNested(true);
		}
	}
	//
	protected void implementFunctionCallDefinitions(ChoisePoint iX) throws SyntaxError {
		for (int k=0; k < functionCallDefinitionArray.size(); k++) {
			FunctionCallDefinition definition= functionCallDefinitionArray.get(k);
			if (definition.isImplemented()) {
				continue;
			};
			definition.setIsImplemented(true);
			ActorPrologSubgoal subgoal= definition.getSubgoal();
			int variableNumber1= definition.getVariableNumber();
			boolean isClauseHeadingElement= definition.isClauseHeadingElement();
			int position= definition.getPosition();
			boolean isNested= definition.isNested();
			if (isClauseHeadingElement && !isNested) {
				int variableNumber2= registerPrimaryFunctionVariable(position,iX);
				subgoal.assignPrimaryFunctionVariable(variableNumber2,master,iX);
				subgoalArray.add(subgoal);
				Term[] auxiliarySubgoalArguments= new Term[2];
				Term variable1= parseSecondaryFunctionVariable(variableNumber1,position,iX);
				Term variable2= parsePrimaryFunctionVariable(variableNumber2,position,iX);
				auxiliarySubgoalArguments[0]= variable1;
				auxiliarySubgoalArguments[1]= variable2;
				ActorPrologSubgoal auxiliarySubgoal=
					new ActorPrologBuiltInCommand(
						SymbolCodes.symbolCode__equality,
						auxiliarySubgoalArguments,
						position);
				subgoalArray.add(auxiliarySubgoal);
			} else {
				subgoal.assignPrimaryFunctionVariable(variableNumber1,master,iX);
				subgoalArray.add(subgoal);
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
			VariableRole role= variableRoleRegister.get(variableNumber);
			if (role == null) {
				role= new VariableRole(variableNumber,p);
				variableRoleRegister.put(variableNumber,role);
			} else {
				role.checkPosition(p);
			};
			return role.toTerm();
		}
	}
	protected void declareUnconstrainedVariable(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			return;
		};
		VariableRole role= variableRoleRegister.get(variableNumber);
		if (role == null) {
			role= new VariableRole(variableNumber,p);
			variableRoleRegister.put(variableNumber,role);
		} else {
			role.checkPosition(p);
		}
		// return role.toTerm();
	}
	//
	protected Term declarePrimaryFunctionVariableAndCreateRoleTerm(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			return VariableRole.getTermPrimaryFunction();
		} else {
			VariableRole role= variableRoleRegister.get(variableNumber);
			if (role == null) {
				role= new VariableRole(variableNumber,p);
				variableRoleRegister.put(variableNumber,role);
			} else {
				role.checkPosition(p);
			};
			role.declarePrimaryFunctionVariable(p,master,iX);
			return role.toTerm();
		}
	}
	protected void declarePrimaryFunctionVariable(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			return;
		};
		VariableRole role= variableRoleRegister.get(variableNumber);
		if (role == null) {
			role= new VariableRole(variableNumber,p);
			variableRoleRegister.put(variableNumber,role);
		} else {
			role.checkPosition(p);
		};
		role.declarePrimaryFunctionVariable(p,master,iX);
	}
	//
	protected Term declareSecondaryFunctionVariableAndCreateRoleTerm(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			return VariableRole.getTermSecondaryFunction();
		} else {
			VariableRole role= variableRoleRegister.get(variableNumber);
			if (role == null) {
				role= new VariableRole(variableNumber,p);
				variableRoleRegister.put(variableNumber,role);
			} else {
				role.checkPosition(p);
			};
			role.declareSecondaryFunctionVariable(p,master,iX);
			return role.toTerm();
		}
	}
	//
	protected void declareMetaFunctorVariable(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			if (checkClauseVariables) {
				if (!isClauseHeadingElement) {
					master.handleError(new AnonymousVariableCannotBeMetaFunctorInClauseBody(p),iX);
				}
			};
			return;
		};
		VariableRole role= variableRoleRegister.get(variableNumber);
		if (role == null) {
			role= new VariableRole(variableNumber,p);
			variableRoleRegister.put(variableNumber,role);
		} else {
			role.checkPosition(p);
		};
		role.declareMetaFunctorVariable(p,master,iX);
		// return role.toTerm();
	}
	//
	protected void declareMetaPredicateVariable(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			if (checkClauseVariables) {
				if (!isClauseHeadingElement) {
					master.handleError(new AnonymousVariableCannotBeMetaPredicateInClauseBody(p),iX);
				}
			};
			return;
		};
		VariableRole role= variableRoleRegister.get(variableNumber);
		if (role == null) {
			role= new VariableRole(variableNumber,p);
			variableRoleRegister.put(variableNumber,role);
		} else {
			role.checkPosition(p);
		};
		role.declareMetaPredicateVariable(p,master,iX);
		// return role.toTerm();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected int registerRegularVariable(String name, int p, ChoisePoint iX) throws SyntaxError {
		if (name.equals(anonymousVariableName)) {
			registerAnonymousVariable(p);
			return anonymousVariableNumber;
		} else {
			Integer value= variableNameRegister.get(name);
			if (value==null) {
				recentVariableNumber++;
				variableNameRegister.put(name,recentVariableNumber);
				invertedVariableNameRegister.put(recentVariableNumber,name);
				declareUnconstrainedVariable(recentVariableNumber,p,iX);
				return recentVariableNumber;
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
	//
	protected int registerPrimaryFunctionVariable(int p, ChoisePoint iX) throws SyntaxError {
		for (int k=anonymousVariableNumber+1; k <= Integer.MAX_VALUE; k++) {
			String name= String.format(auxiliaryVariableNameFormat,k);
			if (!variableNameRegister.containsKey(name)) {
				recentVariableNumber++;
				variableNameRegister.put(name,recentVariableNumber);
				invertedVariableNameRegister.put(recentVariableNumber,name);
				declarePrimaryFunctionVariable(recentVariableNumber,p,iX);
				return recentVariableNumber;
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
		anonymousVariableRegister.add(p);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void checkSingleVariables(ChoisePoint iX) throws SyntaxError {
		Collection<VariableRole> roles= variableRoleRegister.values();
		Iterator<VariableRole> iteratorOfRoles= roles.iterator();
		while (iteratorOfRoles.hasNext()) {
			VariableRole role= iteratorOfRoles.next();
			if (role.mustBeFunctionVariable()) {
				continue;
			};
			if (!role.isUsedManyTimes()) {
				int p= role.getFirstPosition();
				master.handleError(new TheVariableIsUsedOnlyOnce(p),iX);
			}
		}
	}
	//
	protected void checkExternalClauseIterantAndFunctionVariables(ChoisePoint iX) throws SyntaxError {
		Collection<VariableRole> roles= variableRoleRegister.values();
		Iterator<VariableRole> iteratorOfRoles= roles.iterator();
		while (iteratorOfRoles.hasNext()) {
			VariableRole role= iteratorOfRoles.next();
			if (role.mustBeFunctionVariable()) {
				int p= role.getFirstPosition();
				master.handleError(new FunctionCallsAreNotAllowedInExternalClauses(p),iX);
			};
			if (role.isUsedManyTimes()) {
				int p1= role.getFirstPosition();
				int p2= role.getSecondPosition();
				master.handleError(new TheVariableIsNotUnique(p1,p2),iX);
			}
		}
	}
	//
	protected void checkExternalClauseAnonymousVariables(ChoisePoint iX) throws SyntaxError {
		if (anonymousVariableRegister.size() > 0) {
			master.handleError(new AnonymousVariablesAreNotAllowedInExternalClauses(anonymousVariableRegister.get(0)),iX);
		}
	}
	//
	protected void checkWhetherArgumentsAreVariables(ActorPrologAtom heading, ChoisePoint iX) throws SyntaxError {
		Term[] arguments= heading.getArguments();
		int p= heading.getPosition();
		for (int k=0; k < arguments.length; k++) {
			Term argument= arguments[k];
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
				if (functor != SymbolCodes.symbolCode_E_var) {
					master.handleError(new AllArgumentsOfExternalClauseAreToBeVariables(p),iX);
				}
			} catch (TermIsNotAStructure e) {
				master.handleError(new AllArgumentsOfExternalClauseAreToBeVariables(p),iX);
			}
		}
	}
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
	VariableRole role= variableRoleRegister.get(variableNumber);
	if (role != null) {
		if (role.mustBeMetaFunctor()) {
			master.handleError(new MetaFunctorCannotBeMarkedByTheAsterisk(p),iX);
		} else if (role.mustBeMetaPredicate()) {
			master.handleError(new MetaPredicateCannotBeMarkedByTheAsterisk(p),iX);
		}
	};
	if (checkClauseVariables) {
		if (isClauseHeadingElement) {
			headingAsteriskVariableNumber= variableNumber;
		} else {
			if (variableNumber==anonymousVariableNumber) {
				master.handleError(new AnonymousVariableCannotBeMarkedByTheAsteriskInClauseBody(p),iX);
			};
			if (headingAsteriskVariableNumber!=variableNumber) {
				master.handleError(new ThisVariableIsNotMarkedByTheAsteriskInClauseHeading(p),iX);
			}
		}
	}
}
///////////////////////////////////////////////////////////////////////
			} else {
				master.handleError(new OnlyAVariableCanBeMarkedByTheAsterisk(p),iX);
			}
		} catch (TermIsNotAStructure e) {
			master.handleError(new OnlyAVariableCanBeMarkedByTheAsterisk(p),iX);
		} catch (TermIsNotAnInteger e) {
			master.handleError(new OnlyAVariableCanBeMarkedByTheAsterisk(p),iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term attachTermPosition(Term argument, long p) {
		Term[] termArray= new Term[2];
		termArray[0]= argument;
		termArray[1]= new PrologInteger(p);
		return new PrologStructure(SymbolCodes.symbolCode_E_p,termArray);
	}
}
