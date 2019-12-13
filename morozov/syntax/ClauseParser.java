// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax;

import target.*;

import morozov.run.*;
import morozov.syntax.data.*;
import morozov.syntax.data.subgoals.*;
import morozov.syntax.data.subgoals.modes.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.syntax.scanner.*;
import morozov.syntax.scanner.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class ClauseParser extends MetaTermParser {
	//
	protected boolean checkClauseVariables= false;
	protected boolean isClauseHeadingElement= false;
	protected int headingAsteriskVariableNumber= noVariableNumber;
	//
	protected ArrayList<ActorPrologSubgoal> subgoalArray= new ArrayList<>();
	//
	///////////////////////////////////////////////////////////////
	//
	public ClauseParser(ParserMasterInterface m, boolean rememberPositions) {
		super(m,rememberPositions);
	}
	public ClauseParser(ParserMasterInterface m, boolean rememberPositions, boolean implementRobustMode) {
		super(m,rememberPositions,implementRobustMode);
	}
	public ClauseParser(ParserMasterInterface m) {
		super(m);
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE BINARY RELATION **************************************
//*********************************************************************
//*********************************************************************
	//
	public ActorPrologAtom parseRootBinaryRelation(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		return parseBinaryRelation(iX).toActorPrologAtom();
	}
	//
	public ActorPrologNearSubroutineCall parseBinaryRelation(ChoisePoint iX) throws SyntaxError {
		return parseBinaryRelation(true,iX);
	}
	public ActorPrologNearSubroutineCall parseBinaryRelation(boolean enablePredefinedPredicates, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken frontToken= tokens[position];
		int beginningOfTerm= frontToken.getPosition();
		Term leftArgument= parseExpression(false,0,iX);
		return parseBinaryRelation(leftArgument,enablePredefinedPredicates,beginningOfTerm,iX);
	}
	public ActorPrologNearSubroutineCall parseBinaryRelation(Term leftArgument, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		return parseBinaryRelation(leftArgument,true,beginningOfTerm,iX);
	}
	public ActorPrologNearSubroutineCall parseBinaryRelation(Term leftArgument, boolean enablePredefinedPredicates, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken frontToken= tokens[position];
		PrologTokenType frontTokenType= frontToken.getType();
		long functorNameCode;
		boolean insertMinus= false;
		switch (frontTokenType) {
		case EQUALITY:
			if (!enablePredefinedPredicates) {
				master.handleError(new PredefinedPredicateIsNotAllowedHere(frontToken.getPosition()),iX);
			};
			functorNameCode= SymbolCodes.symbolCode__equality;
			break;
		case ASSIGNMENT:
			if (!enablePredefinedPredicates) {
				master.handleError(new PredefinedPredicateIsNotAllowedHere(frontToken.getPosition()),iX);
			};
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
//*********************************************************************
//*********************************************************************
//******** PARSE ATOM *************************************************
//*********************************************************************
//*********************************************************************
	//
	public ActorPrologAtom parseRootAtom(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		return parseAtom(iX);
	}
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
	case L_SQUARE_BRACKET:	// symbol[...
		{
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(iX);
		return relation.toActorPrologAtom();
		}
	case CONTROL_MESSAGE:	// symbol<-...
		if (isAControlMessage(p2)) {
			checkWhetherSymbolIsNotASlot(frontToken,iX);
			return new ActorPrologAtom(
				frontToken.getSymbolCode(master,iX),
				emptyTermArray,
				false,
				beginningOfTerm,
				false,
				-1,
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
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
ActorPrologAtom leftAtom= parseSimpleAtom(true,secondToken.getPosition(),iX);
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
			PrologUnknownValue.instance,
			leftAtom,
			beginningOfTerm);
		Term leftMultiplier= registerFunctionCallAndCreateTerm(functionCall,iX);
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
			PrologUnknownValue.instance,
			leftAtom,
			beginningOfTerm);
		Term leftArgument= registerFunctionCallAndCreateTerm(functionCall,iX);
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
				PrologUnknownValue.instance,
				leftAtom,
				beginningOfTerm);
			Term leftArgument= registerFunctionCallAndCreateTerm(functionCall,iX);
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
		int functionCallDefinitionMarker= functionCallDefinitionArraySize();
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
	false,
	-1,
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
	case L_SQUARE_BRACKET:	// Variable[...
		{
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(iX);
		return relation.toActorPrologAtom();
		}
	case CONTROL_MESSAGE:	// Variable<-...
		{
			if (isAControlMessage(p2)) {
				int variableNumber= registerMetaPredicateVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
				return new ActorPrologAtom(variableNumber,beginningOfTerm,false,-1);
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
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
ActorPrologAtom leftAtom= parseSimpleAtom(true,secondToken.getPosition(),iX);
if (position < numberOfTokens) {
	PrologToken thirdToken= tokens[position];
	PrologTokenType thirdTokenType= thirdToken.getType();
	switch (thirdTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		if (variableNumber==anonymousVariableNumber && !robustMode) {
			master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
		};
		Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			false,
			-1,
			variableNumber,
			variableRoleTerm,
			leftAtom,
			beginningOfTerm);
		Term leftMultiplier= registerFunctionCallAndCreateTerm(functionCall,iX);
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
		int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		if (variableNumber==anonymousVariableNumber && !robustMode) {
			master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
		};
		Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
		ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
			false,
			-1,
			variableNumber,
			variableRoleTerm,
			leftAtom,
			beginningOfTerm);
		Term leftArgument= registerFunctionCallAndCreateTerm(functionCall,iX);
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		return relation.toActorPrologAtom();
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
			position++;
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			if (variableNumber==anonymousVariableNumber && !robustMode) {
				master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
			};
			Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
			ActorPrologFunctionCall functionCall= new ActorPrologFunctionCall(
				false,
				-1,
				variableNumber,
				variableRoleTerm,
				leftAtom,
				beginningOfTerm);
			Term leftArgument= registerFunctionCallAndCreateTerm(functionCall,iX);
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
return new ActorPrologAtom(variableNumber,beginningOfTerm,false,-1);
///////////////////////////////////////////////////////////////////////
			}
			// break;
		case QUESTION_MARK:
			{
///////////////////////////////////////////////////////////////////////
position++;
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
ActorPrologAtom leftAtom= parseSimpleAtom(true,beginningOfTerm,iX);
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
			PrologUnknownValue.instance,
			leftAtom,
			beginningOfTerm);
		Term leftMultiplier= registerFunctionCallAndCreateTerm(functionCall,iX);
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
			PrologUnknownValue.instance,
			leftAtom,
			beginningOfTerm);
		Term leftArgument= registerFunctionCallAndCreateTerm(functionCall,iX);
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
				PrologUnknownValue.instance,
				leftAtom,
				beginningOfTerm);
			Term leftArgument= registerFunctionCallAndCreateTerm(functionCall,iX);
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
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
ArrayList<LabeledTerm> arguments= new ArrayList<>();
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
	false,
	-1,
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
		int functionCallDefinitionMarker= functionCallDefinitionArraySize();
		boolean thisIsAnUnderdeterminedSet= false;
		Term firstPairValue= null;
		if (frontTokenType==PrologTokenType.STRING_SEGMENT) {
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
while (true) {
	if (p2 >= numberOfTokens) {
		break;
	};
	PrologToken secondToken= tokens[p2];
	if (secondToken.getType()==PrologTokenType.STRING_SEGMENT) {
		p2++;
		continue;
	} else {
		break;
	}
};
PrologToken thirdToken= tokens[p2];
if (thirdToken.getType()==PrologTokenType.L_BRACE) {
	thisIsAnUnderdeterminedSet= true;
	position++;
	String stringContent= parseString(frontToken.getStringValue(master,iX),iX);
	firstPairValue= new PrologString(stringContent);
	if (rememberTextPositions) {
		firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
	};
	parseLeftBrace(iX);
}
///////////////////////////////////////////////////////////////////////
		} else if (frontTokenType==PrologTokenType.BINARY_SEGMENT) {
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
while (true) {
	if (p2 >= numberOfTokens) {
		break;
	};
	PrologToken secondToken= tokens[p2];
	if (secondToken.getType()==PrologTokenType.BINARY_SEGMENT) {
		p2++;
		continue;
	} else {
		break;
	}
};
PrologToken thirdToken= tokens[p2];
if (thirdToken.getType()==PrologTokenType.L_BRACE) {
	thisIsAnUnderdeterminedSet= true;
	position++;
	byte[] binaryContent= parseBinary(frontToken.getBinaryValue(master,iX),iX);
	firstPairValue= new PrologBinary(binaryContent);
	if (rememberTextPositions) {
		firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
	};
	parseLeftBrace(iX);
}
///////////////////////////////////////////////////////////////////////
		} else {
			int p2= position + 1;
			if (p2 < numberOfTokens) {
				PrologToken secondToken= tokens[p2];
				if (secondToken.getType()==PrologTokenType.L_BRACE) {
///////////////////////////////////////////////////////////////////////
thisIsAnUnderdeterminedSet= true;
PrologToken keyToken= tokens[position];
int beginningOfKeyToken= keyToken.getPosition();
PrologTokenType keyTokenType= keyToken.getType();
boolean doAddTermPosition= true;
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
case NUMBER_SIGN:
	firstPairValue= PrologUnknownValue.instance;
	break;
case VARIABLE:
	String variableName= keyToken.getVariableName(master,iX);
	firstPairValue= parseIsolatedVariable(variableName,beginningOfKeyToken,iX);
	doAddTermPosition= false;
	break;
default:
	master.handleError(new UnexpectedToken(keyToken),iX);
	position++;
	firstPairValue= PrologUnknownValue.instance;
	break;
};
if (rememberTextPositions && doAddTermPosition) {
	firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
};
position= position + 2;
///////////////////////////////////////////////////////////////////////
				}
			}
		};
		if (thisIsAnUnderdeterminedSet) {
///////////////////////////////////////////////////////////////////////
ArrayList<LabeledTerm> arguments= new ArrayList<>();
arguments.add(new LabeledTerm(0,false,firstPairValue,beginningOfTerm));
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
	false,
	-1,
	true,
	true,
	true);
return atom;
///////////////////////////////////////////////////////////////////////
		};
		ActorPrologNearSubroutineCall relation= parseBinaryRelation(iX);
		return relation.toActorPrologAtom();
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE SUBGOAL **********************************************
//*********************************************************************
//*********************************************************************
	//
	public ActorPrologSubgoal parseRootSubgoal(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		return parseSubgoal(iX);
	}
	//
	public ActorPrologSubgoal parseSubgoal(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken frontToken= tokens[position];
		int beginningOfTerm= frontToken.getPosition();
		PrologTokenType frontTokenType= frontToken.getType();
		switch (frontTokenType) {
		case EXCLAM:	// PARSE SUBGOAL: !
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
		case L_SQUARE_BRACKET:	// PARSE SUBGOAL: [...
			{
///////////////////////////////////////////////////////////////////////
position++;
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
int p3= position + 2;
boolean mayBeAnAsyncMessage= false;
int squareBracketCounter= 0;
ScanMessageTarget: while (true) {
	if (p3 >= numberOfTokens) {
		break ScanMessageTarget;
	};
	int p2= p3 - 1;
	PrologTokenType tokenTypeP2= tokens[p2].getType();
	TokenP2Check: switch (tokenTypeP2) {
	case R_SQUARE_BRACKET:
		if (squareBracketCounter <= 0) {
			// [*]...
			mayBeAnAsyncMessage= true;
			break ScanMessageTarget;
		} else {
			squareBracketCounter--;
			break TokenP2Check;
		}
	case L_SQUARE_BRACKET:
		squareBracketCounter++;
		break TokenP2Check;
	};
	p3++;
};
if (mayBeAnAsyncMessage) {
	//
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
boolean thisIsAsyncMessage= false;
boolean thisIsControlMessage= false;
PrologToken tokenP3= tokens[p3];
PrologTokenType tokenTypeP3= tokenP3.getType();
switch (tokenTypeP3) {
case L_SQUARE_BRACKET:	// [*][...
	int p5= p3 + 2;
	if (p5 < numberOfTokens) {
		// [*][*]...
		if (tokens[p5].getType()==PrologTokenType.R_SQUARE_BRACKET) {
//=====================================================================
int p4= p3 + 1;
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
		if (p5==position+4) {
			long symbolCode= parseTargetSlot(tokenP1,iX);
			position= position + 5;
			ActorPrologAtom simpleAtom= parseSimpleAtom(false,-1,iX);
			return new ActorPrologSubroutineCall(
				true,
				symbolCode,
				-1,
				PrologUnknownValue.instance,
				new ActorPrologMessageType(thisIsControlMessage,false,true),
				simpleAtom,
				beginningOfTerm);
		} else {
			position= position + 2;
			int functionCallDefinitionMarker= functionCallDefinitionArraySize();
			Term[] indices= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			parseRightSquareBracket(iX);
			if (position + 2 != p5 && !robustMode) {
				master.handleError(new UnexpectedToken(tokens[position]),iX);
				position= p5 - 2;
			};
			position= position + 3;
			ActorPrologAtom simpleAtomTwo= parseSimpleAtom(false,-1,iX);
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
			long symbolCode= parseTargetSlot(tokenP1,iX);
			ActorPrologFunctionCall functionCallOne= new ActorPrologFunctionCall(
				true,
				symbolCode,
				-1,
				PrologUnknownValue.instance,
				simpleAtomOne,
				beginningOfTerm);
			int firstVariableNumber= registerFunctionCall(functionCallOne,iX);
			Term firstVariableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(firstVariableNumber,functionCallOne.getPosition(),iX);
			return new ActorPrologSubroutineCall(
				false,
				-1,
				firstVariableNumber,
				firstVariableRoleTerm,
				new ActorPrologMessageType(thisIsControlMessage,false,true),
				simpleAtomTwo,
				beginningOfTerm);
		}
		}
	case VARIABLE:
		{
		if (p5==position+4) {
			int variableNumber= registerRegularVariable(tokenP1.getVariableName(master,iX),tokenP1.getPosition(),iX);
			position= position + 5;
			ActorPrologAtom simpleAtom= parseSimpleAtom(false,-1,iX);
			if (variableNumber==anonymousVariableNumber && !robustMode) {
				master.handleError(new AnonymousVariableCannotBeTargetParameter(tokenP1.getPosition()),iX);
			};
			Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,tokenP1.getPosition(),iX);
			return new ActorPrologSubroutineCall(
				false,
				-1,
				variableNumber,
				variableRoleTerm,
				new ActorPrologMessageType(thisIsControlMessage,false,true),
				simpleAtom,
				beginningOfTerm);
		} else {
			position= position + 2;
			int functionCallDefinitionMarker= functionCallDefinitionArraySize();
			Term[] indices= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			parseRightSquareBracket(iX);
			if (position + 2 != p5 && !robustMode) {
				master.handleError(new UnexpectedToken(tokens[position]),iX);
				position= p5 - 2;
			};
			position= position + 3;
			ActorPrologAtom simpleAtomTwo= parseSimpleAtom(false,-1,iX);
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
			int secondVariableNumber= registerRegularVariable(tokenP1.getVariableName(master,iX),tokenP1.getPosition(),iX);
			if (!robustMode) {
				if (secondVariableNumber==anonymousVariableNumber) {
					master.handleError(new AnonymousVariableCannotBeTargetParameter(tokenP1.getPosition()),iX);
				}
			};
			Term secondVariableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(secondVariableNumber,tokenP1.getPosition(),iX);
			ActorPrologFunctionCall functionCallOne= new ActorPrologFunctionCall(
				false,
				-1,
				secondVariableNumber,
				secondVariableRoleTerm,
				simpleAtomOne,
				beginningOfTerm);
			int firstVariableNumber= registerFunctionCall(functionCallOne,iX);
			Term firstVariableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(firstVariableNumber,functionCallOne.getPosition(),iX);
			return new ActorPrologSubroutineCall(
				false,
				-1,
				firstVariableNumber,
				firstVariableRoleTerm,
				new ActorPrologMessageType(thisIsControlMessage,false,true),
				simpleAtomTwo,
				beginningOfTerm);
		}
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
		if (p3==position+2) {
			long symbolCode= parseTargetSlot(tokenP1,iX);
			position= position + 3;
			ActorPrologAtom simpleAtom= parseSimpleAtom(false,-1,iX);
			return new ActorPrologSubroutineCall(
				true,
				symbolCode,
				-1,
				PrologUnknownValue.instance,
				new ActorPrologMessageType(thisIsControlMessage,false,false),
				simpleAtom,
				beginningOfTerm);
		} else {
			position= position + 2;
			int functionCallDefinitionMarker= functionCallDefinitionArraySize();
			Term[] indices= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			parseRightSquareBracket(iX);
			if (position != p3 && !robustMode) {
				master.handleError(new UnexpectedToken(tokens[position]),iX);
				position= p3;
			};
			position= position + 1;
			ActorPrologAtom simpleAtomTwo= parseSimpleAtom(false,-1,iX);
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
			long symbolCode= parseTargetSlot(tokenP1,iX);
			ActorPrologFunctionCall functionCallOne= new ActorPrologFunctionCall(
				true,
				symbolCode,
				-1,
				PrologUnknownValue.instance,
				simpleAtomOne,
				beginningOfTerm);
			int firstVariableNumber= registerFunctionCall(functionCallOne,iX);
			Term firstVariableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(firstVariableNumber,functionCallOne.getPosition(),iX);
			return new ActorPrologSubroutineCall(
				false,
				-1,
				firstVariableNumber,
				firstVariableRoleTerm,
				new ActorPrologMessageType(thisIsControlMessage,false,false),
				simpleAtomTwo,
				beginningOfTerm);
		}
		}
	case VARIABLE:
		{
		if (p3==position+2) {
			int variableNumber= registerRegularVariable(tokenP1.getVariableName(master,iX),tokenP1.getPosition(),iX);
			position= position + 3;
			ActorPrologAtom simpleAtom= parseSimpleAtom(false,-1,iX);
			if (variableNumber==anonymousVariableNumber && !robustMode) {
				master.handleError(new AnonymousVariableCannotBeTargetParameter(tokenP1.getPosition()),iX);
			};
			Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,tokenP1.getPosition(),iX);
			return new ActorPrologSubroutineCall(
				false,
				-1,
				variableNumber,
				variableRoleTerm,
				new ActorPrologMessageType(thisIsControlMessage,false,false),
				simpleAtom,
				beginningOfTerm);
		} else {
			position= position + 2;
			int functionCallDefinitionMarker= functionCallDefinitionArraySize();
			Term[] indices= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			parseRightSquareBracket(iX);
			if (position != p3 && !robustMode) {
				master.handleError(new UnexpectedToken(tokens[position]),iX);
				position= p3;
			};
			position= position + 1;
			ActorPrologAtom simpleAtomTwo= parseSimpleAtom(false,-1,iX);
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
			int secondVariableNumber= registerRegularVariable(tokenP1.getVariableName(master,iX),tokenP1.getPosition(),iX);
			if (!robustMode) {
				if (secondVariableNumber==anonymousVariableNumber) {
					master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
				}
			};
			Term secondVariableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(secondVariableNumber,tokenP1.getPosition(),iX);
			ActorPrologFunctionCall functionCallOne= new ActorPrologFunctionCall(
				false,
				-1,
				secondVariableNumber,
				secondVariableRoleTerm,
				simpleAtomOne,
				beginningOfTerm);
			int firstVariableNumber= registerFunctionCall(functionCallOne,iX);
			Term firstVariableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(firstVariableNumber,functionCallOne.getPosition(),iX);
			return new ActorPrologSubroutineCall(
				false,
				-1,
				firstVariableNumber,
				firstVariableRoleTerm,
				new ActorPrologMessageType(thisIsControlMessage,false,false),
				simpleAtomTwo,
				beginningOfTerm);
		}
		}
	default:
		master.handleError(new TargetParameterIsExpected(tokenP1.getPosition()),iX);
		position++;
		break;
	}
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	//
};
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
ListElements listElements= parseListElements(beginningOfTerm,iX);
if (position < numberOfTokens) {
	PrologToken secondToken= tokens[position];
	PrologTokenType secondTokenType= secondToken.getType();
	switch (secondTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		Term multiplier1= listElements.toMetaTerm(this);
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
		Term leftArgument= listElements.toMetaTerm(this);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			position++;
			Term leftArgument= listElements.toMetaTerm(this);
			return parseBinaryRelationAfterLEandMinus(leftArgument,1+secondToken.getPosition(),beginningOfTerm,iX);
		};
		break;
	default:
		break;
	}
};
return new ActorPrologBuiltInCommand(
	SymbolCodes.symbolCode_E_copy,
	listElements.toTerms(this,iX),
	beginningOfTerm);
///////////////////////////////////////////////////////////////////////
			}
			// break;
		case SYMBOL:	// PARSE SUBGOAL: symbol...
			{
///////////////////////////////////////////////////////////////////////
int p20= position + 1;
if (p20 < numberOfTokens) {
	boolean arrayElementAccessIsDetected= false;
	boolean arrayElementPredicateInvocationIsDetected= false;
	ActorPrologFunctionCall functionCallOne= null;
	PrologToken secondToken= tokens[p20];
	PrologTokenType secondTokenType= secondToken.getType();
	if (secondTokenType==PrologTokenType.L_SQUARE_BRACKET) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
int expectedNextPosition= p20;
int p3= p20 + 1;
int squareBracketCounter= 0;
SearchArrayIndices: while (true) {
	if (p3 >= numberOfTokens) {
		break SearchArrayIndices;
	};
	PrologTokenType tokenTypeP3= tokens[p3].getType();
	TokenP3Check: switch (tokenTypeP3) {
	case R_SQUARE_BRACKET:
		if (squareBracketCounter <= 0) {
			// [*]...
			arrayElementAccessIsDetected= true;
			break SearchArrayIndices;
		} else {
			squareBracketCounter--;
			break TokenP3Check;
		}
	case L_SQUARE_BRACKET:
		squareBracketCounter++;
		break TokenP3Check;
	case DATA_MESSAGE:
	case CONTROL_MESSAGE:
		break SearchArrayIndices;
	};
	p3++;
};
if (arrayElementAccessIsDetected) {
	int p4= p3 + 1;
	expectedNextPosition= p4;
	if (p4 < numberOfTokens) {
		PrologTokenType tokenTypeP4= tokens[p4].getType();
		TokenP4Check: switch (tokenTypeP4) {
		case DATA_MESSAGE:
		case CONTROL_MESSAGE:
		case QUESTION_MARK:
			arrayElementPredicateInvocationIsDetected= true;
			break TokenP4Check;
		case L_SQUARE_BRACKET:
			int p5= p4 + 1;
			PrologTokenType tokenTypeP5= tokens[p5].getType();
			TokenP5Check: switch (tokenTypeP5) {
			case DATA_MESSAGE:
			case CONTROL_MESSAGE:
				arrayElementPredicateInvocationIsDetected= true;
				break TokenP5Check;
			};
			break TokenP4Check;
		}
	}
};
if (arrayElementAccessIsDetected) {
	position= position + 2;
	int functionCallDefinitionMarker= functionCallDefinitionArraySize();
	Term[] indices= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
	declareNestedFunctionCalls(functionCallDefinitionMarker);
	if (position != expectedNextPosition && !robustMode) {
		master.handleError(new UnexpectedToken(tokens[position]),iX);
		position= expectedNextPosition;
	};
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
	long symbolCode= parseTargetSlot(frontToken,iX);
	functionCallOne= new ActorPrologFunctionCall(
		true,
		symbolCode,
		-1,
		PrologUnknownValue.instance,
		simpleAtomOne,
		beginningOfTerm);
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	};
	boolean thisIsAsyncMessage= false;
	boolean thisIsControlMessage= false;
	int p21;
	if (arrayElementAccessIsDetected) {
		p21= position;
	} else {
		p21= position + 1;
	};
	if (p21 < numberOfTokens) {
		PrologToken thirdToken= tokens[p21];
		PrologTokenType thirdTokenType= thirdToken.getType();
		switch (thirdTokenType) {
		case L_SQUARE_BRACKET:	// symbol[...
			int p4= p21 + 2;
			if (p4 < numberOfTokens) {
				if (tokens[p4].getType()==PrologTokenType.R_SQUARE_BRACKET) {
					// symbol[*]...
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
int p3= p4 - 1;
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
	if (arrayElementPredicateInvocationIsDetected) {
		position= position + 3;
		ActorPrologAtom simpleAtom= parseSimpleAtom(false,-1,iX);
		int variableArrayElement= registerFunctionCall(functionCallOne,iX);
		Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
		return new ActorPrologSubroutineCall(
			false,
			-1,
			variableArrayElement,
			variableArrayElementRoleTerm,
			new ActorPrologMessageType(thisIsControlMessage,true,true),
			simpleAtom,
			beginningOfTerm);
	} else {
		long symbolCode= parseTargetSlot(frontToken,iX);
		position= position + 4;
		ActorPrologAtom simpleAtom= parseSimpleAtom(false,-1,iX);
		return new ActorPrologSubroutineCall(
			true,
			symbolCode,
			-1,
			PrologUnknownValue.instance,
			new ActorPrologMessageType(thisIsControlMessage,true,true),
			simpleAtom,
			beginningOfTerm);
	}
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				}
			};
			// This branch is error situation:
			if (arrayElementAccessIsDetected) {
				int functionCallDefinitionMarker= functionCallDefinitionArraySize();
				int variableArrayElement= registerFunctionCall(functionCallOne,iX);
				Term leftMultiplier= parsePrimaryFunctionVariable(variableArrayElement,beginningOfTerm,iX);
				Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
				return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
			} else {
				// This branch is impossible:
				return parseBinaryRelation(iX);
			}
		case DATA_MESSAGE:	// symbol<<...
			thisIsAsyncMessage= true;
			thisIsControlMessage= false;
			break;
		case CONTROL_MESSAGE:	// symbol<-...
			int p22;
			if (arrayElementAccessIsDetected) {
				p22= position;
			} else {
				p22= p20;
			};
			if (isAControlMessage(p22)) {
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
				if (arrayElementAccessIsDetected) {
					int functionCallDefinitionMarker= functionCallDefinitionArraySize();
					int variableArrayElement= registerFunctionCall(functionCallOne,iX);
					Term leftMultiplier= parsePrimaryFunctionVariable(variableArrayElement,beginningOfTerm,iX);
					Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
					return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
				} else {
					position= position + 2;
					Term leftArgument= parseSymbolOrSlot(frontToken,beginningOfTerm,iX);
					return parseBinaryRelationAfterLEandMinus(leftArgument,1+thirdToken.getPosition(),beginningOfTerm,iX);
				}
			};
			break;
		case QUESTION_MARK:	// symbol?...
			{
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if (arrayElementAccessIsDetected) {
	position= position + 1;
} else {
	position= position + 2;
};
if (position < numberOfTokens) {
	PrologToken fourthToken= tokens[position];
	PrologTokenType fourthTokenType= fourthToken.getType();
	if (fourthTokenType==PrologTokenType.L_ROUND_BRACKET) {
		position++;
		ActorPrologNearSubroutineCall binaryRelation= parseBinaryRelation(false,iX);
		parseRightRoundBracket(iX);
		if (arrayElementAccessIsDetected) {
			int variableArrayElement= registerFunctionCall(functionCallOne,iX);
			Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
			return new ActorPrologSubroutineCall(
				false,
				-1,
				variableArrayElement,
				variableArrayElementRoleTerm,
				new ActorPrologPlainSubgoalType(false),
				binaryRelation.toActorPrologAtom(),
				binaryRelation.getPosition());
		} else {
			long symbolCode= parseTargetSlot(frontToken,iX);
			return new ActorPrologSubroutineCall(
				true,
				symbolCode,
				-1,
				PrologUnknownValue.instance,
				new ActorPrologPlainSubgoalType(false),
				binaryRelation.toActorPrologAtom(),
				binaryRelation.getPosition());
		}
	}
};
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
ActorPrologAtom leftAtom= parseSimpleAtom(true,thirdToken.getPosition(),iX);
if (position < numberOfTokens) {
	PrologToken fourthToken= tokens[position];
	PrologTokenType fourthTokenType= fourthToken.getType();
	switch (fourthTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologFunctionCall functionCall;
		if (arrayElementAccessIsDetected) {
			int variableArrayElement= registerFunctionCall(functionCallOne,iX);
			Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
			functionCall= new ActorPrologFunctionCall(
				false,
				-1,
				variableArrayElement,
				variableArrayElementRoleTerm,
				leftAtom,
				beginningOfTerm);
		} else {
			long symbolCode= parseTargetSlot(frontToken,iX);
			functionCall= new ActorPrologFunctionCall(
				true,
				symbolCode,
				-1,
				PrologUnknownValue.instance,
				leftAtom,
				beginningOfTerm);
		};
		Term leftMultiplier= registerFunctionCallAndCreateTerm(functionCall,iX);
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
		ActorPrologFunctionCall functionCall;
		if (arrayElementAccessIsDetected) {
			int variableArrayElement= registerFunctionCall(functionCallOne,iX);
			Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
			functionCall= new ActorPrologFunctionCall(
				false,
				-1,
				variableArrayElement,
				variableArrayElementRoleTerm,
				leftAtom,
				beginningOfTerm);
		} else {
			long symbolCode= parseTargetSlot(frontToken,iX);
			functionCall= new ActorPrologFunctionCall(
				true,
				symbolCode,
				-1,
				PrologUnknownValue.instance,
				leftAtom,
				beginningOfTerm);
		};
		Term leftArgument= registerFunctionCallAndCreateTerm(functionCall,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			position++;
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			ActorPrologFunctionCall functionCall;
			if (arrayElementAccessIsDetected) {
				int variableArrayElement= registerFunctionCall(functionCallOne,iX);
				Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
				functionCall= new ActorPrologFunctionCall(
					false,
					-1,
					variableArrayElement,
					variableArrayElementRoleTerm,
					leftAtom,
					beginningOfTerm);
			} else {
				long symbolCode= parseTargetSlot(frontToken,iX);
				functionCall= new ActorPrologFunctionCall(
					true,
					symbolCode,
					-1,
					PrologUnknownValue.instance,
					leftAtom,
					beginningOfTerm);
			};
			Term leftArgument= registerFunctionCallAndCreateTerm(functionCall,iX);
			return parseBinaryRelationAfterLEandMinus(leftArgument,1+fourthToken.getPosition(),beginningOfTerm,iX);
		};
		break;
	default:
		break;
	}
};
if (arrayElementAccessIsDetected) {
	int variableArrayElement= registerFunctionCall(functionCallOne,iX);
	Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
	return new ActorPrologSubroutineCall(
		false,
		-1,
		variableArrayElement,
		variableArrayElementRoleTerm,
		new ActorPrologPlainSubgoalType(false),
		leftAtom,
		leftAtom.getAtomPosition());
} else {
	long symbolCode= parseTargetSlot(frontToken,iX);
	return new ActorPrologSubroutineCall(
		true,
		symbolCode,
		-1,
		PrologUnknownValue.instance,
		new ActorPrologPlainSubgoalType(false),
		leftAtom,
		leftAtom.getAtomPosition());
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			}
		case MULTIPLY:
		case DIVIDE:
		case PLUS:
		case MINUS:
			{
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if (!arrayElementAccessIsDetected) {
	position= position + 1;
};
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
Term leftMultiplier;
if (arrayElementAccessIsDetected) {
	int variableArrayElement= registerFunctionCall(functionCallOne,iX);
	leftMultiplier= parsePrimaryFunctionVariable(variableArrayElement,beginningOfTerm,iX);
} else {
	leftMultiplier= parseSymbolOrSlot(frontToken,beginningOfTerm,iX);
};
Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			}
		case EQUALITY:
		case ASSIGNMENT:
		case LT:
		case GT:
		case NE:
		case LE:
		case GE:
			{
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if (!arrayElementAccessIsDetected) {
	position= position + 1;
};
Term leftArgument;
if (arrayElementAccessIsDetected) {
	int variableArrayElement= registerFunctionCall(functionCallOne,iX);
	leftArgument= parsePrimaryFunctionVariable(variableArrayElement,beginningOfTerm,iX);
} else {
	leftArgument= parseSymbolOrSlot(frontToken,beginningOfTerm,iX);
};
return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			}
		default:
			break;
		}
	};
	if (thisIsAsyncMessage) {
		if (arrayElementAccessIsDetected) {
			position= position + 1;
			ActorPrologAtom simpleAtom= parseSimpleAtom(false,-1,iX);
			int variableArrayElement= registerFunctionCall(functionCallOne,iX);
			Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
			return new ActorPrologSubroutineCall(
				false,
				-1,
				variableArrayElement,
				variableArrayElementRoleTerm,
				new ActorPrologMessageType(thisIsControlMessage,true,false),
				simpleAtom,
				simpleAtom.getAtomPosition());
		} else {
			position= position + 2;
			long symbolCode= parseTargetSlot(frontToken,iX);
			ActorPrologAtom simpleAtom= parseSimpleAtom(false,-1,iX);
			return new ActorPrologSubroutineCall(
				true,
				symbolCode,
				-1,
				PrologUnknownValue.instance,
				new ActorPrologMessageType(thisIsControlMessage,true,false),
				simpleAtom,
				simpleAtom.getAtomPosition());
		}
	} else {
		if (arrayElementAccessIsDetected) {
			return functionCallOne;
		} else {
			return parseSimpleAtomOrSpecialCaseOfBinaryRelation(iX);
		}
	}
}
///////////////////////////////////////////////////////////////////////
			};
			break;
		case VARIABLE:
			{
///////////////////////////////////////////////////////////////////////
int p20= position + 1;
if (p20 < numberOfTokens) {
	boolean arrayElementAccessIsDetected= false;
	boolean arrayElementPredicateInvocationIsDetected= false;
	ActorPrologFunctionCall functionCallOne= null;
	PrologToken secondToken= tokens[p20];
	PrologTokenType secondTokenType= secondToken.getType();
	if (secondTokenType==PrologTokenType.L_SQUARE_BRACKET) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
int expectedNextPosition= p20;
int p3= p20 + 1;
int squareBracketCounter= 0;
SearchArrayIndices: while (true) {
	if (p3 >= numberOfTokens) {
		break SearchArrayIndices;
	};
	PrologTokenType tokenTypeP3= tokens[p3].getType();
	TokenP3Check: switch (tokenTypeP3) {
	case R_SQUARE_BRACKET:
		if (squareBracketCounter <= 0) {
			// [*]...
			arrayElementAccessIsDetected= true;
			break SearchArrayIndices;
		} else {
			squareBracketCounter--;
			break TokenP3Check;
		}
	case L_SQUARE_BRACKET:
		squareBracketCounter++;
		break TokenP3Check;
	case DATA_MESSAGE:
	case CONTROL_MESSAGE:
		break SearchArrayIndices;
	};
	p3++;
};
if (arrayElementAccessIsDetected) {
	int p4= p3 + 1;
	expectedNextPosition= p4;
	if (p4 < numberOfTokens) {
		PrologTokenType tokenTypeP4= tokens[p4].getType();
		TokenP4Check: switch (tokenTypeP4) {
		case DATA_MESSAGE:
		case CONTROL_MESSAGE:
		case QUESTION_MARK:
			arrayElementPredicateInvocationIsDetected= true;
			break TokenP4Check;
		case L_SQUARE_BRACKET:
			int p5= p4 + 1;
			PrologTokenType tokenTypeP5= tokens[p5].getType();
			TokenP5Check: switch (tokenTypeP5) {
			case DATA_MESSAGE:
			case CONTROL_MESSAGE:
				arrayElementPredicateInvocationIsDetected= true;
				break TokenP5Check;
			};
			break TokenP4Check;
		}
	}
};
if (arrayElementAccessIsDetected) {
	position= position + 2;
	int functionCallDefinitionMarker= functionCallDefinitionArraySize();
	Term[] indices= parseTermSequence(PrologTokenType.R_SQUARE_BRACKET,iX);
	declareNestedFunctionCalls(functionCallDefinitionMarker);
	if (position != expectedNextPosition && !robustMode) {
		master.handleError(new UnexpectedToken(tokens[position]),iX);
		position= expectedNextPosition;
	};
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
	int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
	if (variableNumber==anonymousVariableNumber && !robustMode) {
		master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
	};
	Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
	functionCallOne= new ActorPrologFunctionCall(
		false,
		-1,
		variableNumber,
		variableRoleTerm,
		simpleAtomOne,
		beginningOfTerm);
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	};
	boolean thisIsAsyncMessage= false;
	boolean thisIsControlMessage= false;
	int p21;
	if (arrayElementAccessIsDetected) {
		p21= position;
	} else {
		p21= position + 1;
	};
	if (p21 < numberOfTokens) {
		PrologToken thirdToken= tokens[p21];
		PrologTokenType thirdTokenType= thirdToken.getType();
		switch (thirdTokenType) {
		case L_SQUARE_BRACKET:	// Variable[...
			int p4= p21 + 2;
			if (p4 < numberOfTokens) {
				if (tokens[p4].getType()==PrologTokenType.R_SQUARE_BRACKET) {
					// Variable[*]...
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
int p3= p4 - 1;
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
	if (arrayElementPredicateInvocationIsDetected) {
		position= position + 3;
		ActorPrologAtom simpleAtom= parseSimpleAtom(false,-1,iX);
		int variableArrayElement= registerFunctionCall(functionCallOne,iX);
		Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
		return new ActorPrologSubroutineCall(
			false,
			-1,
			variableArrayElement,
			variableArrayElementRoleTerm,
			new ActorPrologMessageType(thisIsControlMessage,true,true),
			simpleAtom,
			beginningOfTerm);
	} else {
		position= position + 4;
		ActorPrologAtom simpleAtom= parseSimpleAtom(false,-1,iX);
		String variableName= frontToken.getVariableName(master,iX);
		int variableNumber= registerRegularVariable(variableName,beginningOfTerm,iX);
		if (variableNumber==anonymousVariableNumber && !robustMode) {
			master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
		};
		Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
		return new ActorPrologSubroutineCall(
			false,
			-1,
			variableNumber,
			variableRoleTerm,
			new ActorPrologMessageType(thisIsControlMessage,true,true),
			simpleAtom,
			beginningOfTerm);
	}
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				}
			};
			if (arrayElementAccessIsDetected) {
				int functionCallDefinitionMarker= functionCallDefinitionArraySize();
				int variableArrayElement= registerFunctionCall(functionCallOne,iX);
				Term leftMultiplier= parsePrimaryFunctionVariable(variableArrayElement,beginningOfTerm,iX);
				Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
				return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
			} else {
				// This branch is impossible:
				return parseBinaryRelation(iX);
			}
		case DATA_MESSAGE:	// Variable<<...
			thisIsAsyncMessage= true;
			thisIsControlMessage= false;
			break;
		case CONTROL_MESSAGE:	// Variable<-...
			int p22;
			if (arrayElementAccessIsDetected) {
				p22= position;
			} else {
				p22= p20;
			};
			if (isAControlMessage(p22)) {
				thisIsAsyncMessage= true;
				thisIsControlMessage= true;
			} else {
				if (arrayElementAccessIsDetected) {
					int functionCallDefinitionMarker= functionCallDefinitionArraySize();
					int variableArrayElement= registerFunctionCall(functionCallOne,iX);
					Term leftMultiplier= parsePrimaryFunctionVariable(variableArrayElement,beginningOfTerm,iX);
					Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
					return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
				} else {
					position= position + 2;
					Term leftArgument= parseVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
					return parseBinaryRelationAfterLEandMinus(leftArgument,1+thirdToken.getPosition(),beginningOfTerm,iX);
				}
			};
			break;
		case QUESTION_MARK:
			{
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if (arrayElementAccessIsDetected) {
	position= position + 1;
} else {
	position= position + 2;
};
if (position < numberOfTokens) {
	PrologToken fourthToken= tokens[position];
	PrologTokenType fourthTokenType= fourthToken.getType();
	if (fourthTokenType==PrologTokenType.L_ROUND_BRACKET) {
		position++;
		ActorPrologNearSubroutineCall binaryRelation= parseBinaryRelation(false,iX);
		parseRightRoundBracket(iX);
		if (arrayElementAccessIsDetected) {
			int variableArrayElement= registerFunctionCall(functionCallOne,iX);
			Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
			return new ActorPrologSubroutineCall(
				false,
				-1,
				variableArrayElement,
				variableArrayElementRoleTerm,
				new ActorPrologPlainSubgoalType(false),
				binaryRelation.toActorPrologAtom(),
				binaryRelation.getPosition());
		} else {
			int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
			if (variableNumber==anonymousVariableNumber && !robustMode) {
				master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
			};
			Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
			return new ActorPrologSubroutineCall(
				false,
				-1,
				variableNumber,
				variableRoleTerm,
				new ActorPrologPlainSubgoalType(false),
				binaryRelation.toActorPrologAtom(),
				binaryRelation.getPosition());
		}
	}
};
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
ActorPrologAtom leftAtom= parseSimpleAtom(true,thirdToken.getPosition(),iX);
if (position < numberOfTokens) {
	PrologToken fourthToken= tokens[position];
	PrologTokenType fourthTokenType= fourthToken.getType();
	switch (fourthTokenType) {
	case MULTIPLY:
	case DIVIDE:
	case PLUS:
	case MINUS:
		{
		declareNestedFunctionCalls(functionCallDefinitionMarker);
		ActorPrologFunctionCall functionCall;
		if (arrayElementAccessIsDetected) {
			int variableArrayElement= registerFunctionCall(functionCallOne,iX);
			Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
			functionCall= new ActorPrologFunctionCall(
				false,
				-1,
				variableArrayElement,
				variableArrayElementRoleTerm,
				leftAtom,
				beginningOfTerm);
		} else {
			int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
			if (variableNumber==anonymousVariableNumber && !robustMode) {
				master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
			};
			Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
			functionCall= new ActorPrologFunctionCall(
				false,
				-1,
				variableNumber,
				variableRoleTerm,
				leftAtom,
				beginningOfTerm);
		};
		Term leftMultiplier= registerFunctionCallAndCreateTerm(functionCall,iX);
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
		ActorPrologFunctionCall functionCall;
		if (arrayElementAccessIsDetected) {
			int variableArrayElement= registerFunctionCall(functionCallOne,iX);
			Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
			functionCall= new ActorPrologFunctionCall(
				false,
				-1,
				variableArrayElement,
				variableArrayElementRoleTerm,
				leftAtom,
				beginningOfTerm);
		} else {
			int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
			if (variableNumber==anonymousVariableNumber && !robustMode) {
				master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
			};
			Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
			functionCall= new ActorPrologFunctionCall(
				false,
				-1,
				variableNumber,
				variableRoleTerm,
				leftAtom,
				beginningOfTerm);
		};
		Term leftArgument= registerFunctionCallAndCreateTerm(functionCall,iX);
		return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
		}
	case CONTROL_MESSAGE:
		if (!isAControlMessage(position)) {
			position++;
			declareNestedFunctionCalls(functionCallDefinitionMarker);
			ActorPrologFunctionCall functionCall;
			if (arrayElementAccessIsDetected) {
				int variableArrayElement= registerFunctionCall(functionCallOne,iX);
				Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
				functionCall= new ActorPrologFunctionCall(
					false,
					-1,
					variableArrayElement,
					variableArrayElementRoleTerm,
					leftAtom,
					beginningOfTerm);
			} else {
				int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
				if (variableNumber==anonymousVariableNumber && !robustMode) {
					master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
				};
				Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
				functionCall= new ActorPrologFunctionCall(
					false,
					-1,
					variableNumber,
					variableRoleTerm,
					leftAtom,
					beginningOfTerm);
			};
			Term leftArgument= registerFunctionCallAndCreateTerm(functionCall,iX);
			return parseBinaryRelationAfterLEandMinus(leftArgument,1+fourthToken.getPosition(),beginningOfTerm,iX);
		};
		break;
	default:
		break;
	}
};
if (arrayElementAccessIsDetected) {
	int variableArrayElement= registerFunctionCall(functionCallOne,iX);
	Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
	return new ActorPrologSubroutineCall(
		false,
		-1,
		variableArrayElement,
		variableArrayElementRoleTerm,
		new ActorPrologPlainSubgoalType(false),
		leftAtom,
		leftAtom.getAtomPosition());
} else {
	int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
	if (variableNumber==anonymousVariableNumber && !robustMode) {
		master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
	};
	Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
	return new ActorPrologSubroutineCall(
		false,
		-1,
		variableNumber,
		variableRoleTerm,
		new ActorPrologPlainSubgoalType(false),
		leftAtom,
		leftAtom.getAtomPosition());
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			}
		case MULTIPLY:
		case DIVIDE:
		case PLUS:
		case MINUS:
			{
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if (!arrayElementAccessIsDetected) {
	position= position + 1;
};
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
Term leftMultiplier;
if (arrayElementAccessIsDetected) {
	int variableArrayElement= registerFunctionCall(functionCallOne,iX);
	leftMultiplier= parsePrimaryFunctionVariable(variableArrayElement,beginningOfTerm,iX);
} else {
	leftMultiplier= parseVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
};
Term leftArgument= continueExpressionParsing(leftMultiplier,functionCallDefinitionMarker,iX);
return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			}
		case EQUALITY:
		case ASSIGNMENT:
		case LT:
		case GT:
		case NE:
		case LE:
		case GE:
			{
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if (!arrayElementAccessIsDetected) {
	position= position + 1;
};
Term leftArgument;
if (arrayElementAccessIsDetected) {
	int variableArrayElement= registerFunctionCall(functionCallOne,iX);
	leftArgument= parsePrimaryFunctionVariable(variableArrayElement,beginningOfTerm,iX);
} else {
	leftArgument= parseVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
};
return parseBinaryRelation(leftArgument,beginningOfTerm,iX);
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			}
		default:
			break;
		}
	};
	if (thisIsAsyncMessage) {
		if (arrayElementAccessIsDetected) {
			position= position + 1;
			ActorPrologAtom simpleAtom= parseSimpleAtom(false,-1,iX);
			int variableArrayElement= registerFunctionCall(functionCallOne,iX);
			Term variableArrayElementRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableArrayElement,functionCallOne.getPosition(),iX);
			return new ActorPrologSubroutineCall(
				false,
				-1,
				variableArrayElement,
				variableArrayElementRoleTerm,
				new ActorPrologMessageType(thisIsControlMessage,true,false),
				simpleAtom,
				simpleAtom.getAtomPosition());
		} else {
			position= position + 2;
			ActorPrologAtom simpleAtom= parseSimpleAtom(false,-1,iX);
			int variableNumber= registerRegularVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
			if (variableNumber==anonymousVariableNumber && !robustMode) {
				master.handleError(new AnonymousVariableCannotBeTargetParameter(beginningOfTerm),iX);
			};
			Term variableRoleTerm= declarePrimaryFunctionVariableAndCreateRoleTerm(variableNumber,beginningOfTerm,iX);
			return new ActorPrologSubroutineCall(
				false,
				-1,
				variableNumber,
				variableRoleTerm,
				new ActorPrologMessageType(thisIsControlMessage,true,false),
				simpleAtom,
				simpleAtom.getAtomPosition());
		}
	} else {
		if (arrayElementAccessIsDetected) {
			return functionCallOne;
		} else {
			ActorPrologNearSubroutineCall relation= parseSimpleAtomOrSpecialCaseOfBinaryRelation(iX);
			relation.checkWhetherFunctorIsNotAnonymousVariable(master,iX);
			return relation;
		}
	}
}
///////////////////////////////////////////////////////////////////////
			};
			break;
		case QUESTION_MARK:
			{
///////////////////////////////////////////////////////////////////////
position++;
// ???
if (position < numberOfTokens) {
	PrologToken fourthToken= tokens[position];
	PrologTokenType fourthTokenType= fourthToken.getType();
	if (fourthTokenType==PrologTokenType.L_ROUND_BRACKET) {
		position++;
		ActorPrologNearSubroutineCall binaryRelation= parseBinaryRelation(false,iX);
		parseRightRoundBracket(iX);
		return binaryRelation;
	}
};
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
ActorPrologAtom leftAtom= parseSimpleAtom(true,beginningOfTerm,iX);
if (position < numberOfTokens) {
	PrologToken fourthToken= tokens[position];
	PrologTokenType fourthTokenType= fourthToken.getType();
	switch (fourthTokenType) {
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
			PrologUnknownValue.instance,
			leftAtom,
			beginningOfTerm);
		Term leftMultiplier= registerFunctionCallAndCreateTerm(functionCall,iX);
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
			PrologUnknownValue.instance,
			leftAtom,
			beginningOfTerm);
		Term leftArgument= registerFunctionCallAndCreateTerm(functionCall,iX);
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
				PrologUnknownValue.instance,
				leftAtom,
				beginningOfTerm);
			Term leftArgument= registerFunctionCallAndCreateTerm(functionCall,iX);
			return parseBinaryRelationAfterLEandMinus(leftArgument,1+fourthToken.getPosition(),beginningOfTerm,iX);
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
int functionCallDefinitionMarker= functionCallDefinitionArraySize();
ArrayList<LabeledTerm> arguments= new ArrayList<>();
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
	false,
	-1,
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
		int functionCallDefinitionMarker= functionCallDefinitionArraySize();
		boolean thisIsAnUnderdeterminedSet= false;
		Term firstPairValue= null;
		if (frontTokenType==PrologTokenType.STRING_SEGMENT) {
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
while (true) {
	if (p2 >= numberOfTokens) {
		break;
	};
	PrologToken thirdToken= tokens[p2];
	if (thirdToken.getType()==PrologTokenType.STRING_SEGMENT) {
		p2++;
		continue;
	} else {
		break;
	}
};
PrologToken fourthToken= tokens[p2];
if (fourthToken.getType()==PrologTokenType.L_BRACE) {
	thisIsAnUnderdeterminedSet= true;
	position++;
	String stringContent= parseString(frontToken.getStringValue(master,iX),iX);
	firstPairValue= new PrologString(stringContent);
	if (rememberTextPositions) {
		firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
	};
	parseLeftBrace(iX);
}
///////////////////////////////////////////////////////////////////////
		} else if (frontTokenType==PrologTokenType.BINARY_SEGMENT) {
///////////////////////////////////////////////////////////////////////
int p2= position + 1;
while (true) {
	if (p2 >= numberOfTokens) {
		break;
	};
	PrologToken thirdToken= tokens[p2];
	if (thirdToken.getType()==PrologTokenType.BINARY_SEGMENT) {
		p2++;
		continue;
	} else {
		break;
	}
};
PrologToken fourthToken= tokens[p2];
if (fourthToken.getType()==PrologTokenType.L_BRACE) {
	thisIsAnUnderdeterminedSet= true;
	position++;
	byte[] binaryContent= parseBinary(frontToken.getBinaryValue(master,iX),iX);
	firstPairValue= new PrologBinary(binaryContent);
	if (rememberTextPositions) {
		firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
	};
	parseLeftBrace(iX);
}
///////////////////////////////////////////////////////////////////////
		} else {
			int p2= position + 1;
			if (p2 < numberOfTokens) {
				PrologToken thirdToken= tokens[p2];
				if (thirdToken.getType()==PrologTokenType.L_BRACE) {
///////////////////////////////////////////////////////////////////////
thisIsAnUnderdeterminedSet= true;
PrologToken keyToken= tokens[position];
int beginningOfKeyToken= keyToken.getPosition();
PrologTokenType keyTokenType= keyToken.getType();
boolean doAddTermPosition= true;
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
case NUMBER_SIGN:
	firstPairValue= PrologUnknownValue.instance;
	break;
case VARIABLE:
	String variableName= keyToken.getVariableName(master,iX);
	firstPairValue= parseIsolatedVariable(variableName,beginningOfKeyToken,iX);
	doAddTermPosition= false;
	break;
default:
	master.handleError(new UnexpectedToken(keyToken),iX);
	position++;
	firstPairValue= PrologUnknownValue.instance;
	break;
};
if (rememberTextPositions && doAddTermPosition) {
	firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
};
position= position + 2;
///////////////////////////////////////////////////////////////////////
				}
			}
		};
		if (thisIsAnUnderdeterminedSet) {
///////////////////////////////////////////////////////////////////////
ArrayList<LabeledTerm> arguments= new ArrayList<>();
arguments.add(new LabeledTerm(0,false,firstPairValue,beginningOfTerm));
Term leftArgument= parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
if (position < numberOfTokens) {
	PrologToken fourthToken= tokens[position];
	PrologTokenType fourthTokenType= fourthToken.getType();
	switch (fourthTokenType) {
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
			return parseBinaryRelationAfterLEandMinus(leftArgument,1+fourthToken.getPosition(),beginningOfTerm,iX);
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
	false,
	-1,
	true,
	true,
	true);
return new ActorPrologNearSubroutineCall(atom);
///////////////////////////////////////////////////////////////////////
		};
		return parseBinaryRelation(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected ActorPrologNearSubroutineCall parseSimpleAtomOrSpecialCaseOfBinaryRelation(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken frontToken= tokens[position];
		int beginningOfTerm= frontToken.getPosition();
		int functionCallDefinitionMarker= functionCallDefinitionArraySize();
		ActorPrologAtom leftAtom= parseSimpleAtom(false,-1,iX);
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
//*********************************************************************
//*********************************************************************
//******** PARSE CLAUSES **********************************************
//*********************************************************************
//*********************************************************************
	//
	public ActorPrologClause[] parseRootClauses(boolean parseModel, ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		ArrayList<ActorPrologClause> clauseArray= new ArrayList<>();
		parseClauses(clauseArray,parseModel,iX);
		return clauseArray.toArray(new ActorPrologClause[clauseArray.size()]);
	}
	public void parseClauses(ArrayList<ActorPrologClause> clauseArray, boolean isTheModelSection, ChoisePoint iX) throws SyntaxError {
		boolean isTheFirstClause= true;
		ActorPrologClausesLoop: while (true) {
			if (position >= numberOfTokens) {
				break ActorPrologClausesLoop;
			};
			PrologToken frontToken= tokens[position];
			if (frontToken.isFinalToken()) {
				break ActorPrologClausesLoop;
			};
			PrologTokenType frontTokenType= frontToken.getType();
			switch (frontTokenType) {
			case R_SQUARE_BRACKET:
			case KEYWORD:
				break ActorPrologClausesLoop;
			};
			ActorPrologClause clause= parseClause(isTheModelSection,iX);
			if (!robustMode) {
				checkWhetherClauseIsGroupedProperly(clauseArray,clause,isTheFirstClause,iX);
			};
			clauseArray.add(clause);
			isTheFirstClause= false;
		}
	}
	//
	protected void checkWhetherClauseIsGroupedProperly(ArrayList<ActorPrologClause> array, ActorPrologClause clause1, boolean isTheFirstClause, ChoisePoint iX) throws ParserError {
		if (clause1.hasNoName() || clause1.lastArgumentHasAsterisk()) {
			return;
		};
		long nameCode1= clause1.getFunctorName();
		int arity1= clause1.getArity();
		int p1= clause1.getPosition();
		Iterator<ActorPrologClause> arrayIterator= array.iterator();
		if (isTheFirstClause) {
			while (arrayIterator.hasNext()) {
				ActorPrologClause clause2= arrayIterator.next();
				if (clause2.hasNoName() || clause2.lastArgumentHasAsterisk()) {
					return;
				};
				long nameCode2= clause2.getFunctorName();
				if (nameCode1==nameCode2) {
					master.handleError(new ClausesWithTheSameNameShouldBeGrouped(p1),iX);
				}
			}
		} else {
			boolean nameIsFound= false;
			boolean nameAndArityAreFound= false;
			while (arrayIterator.hasNext()) {
				ActorPrologClause clause2= arrayIterator.next();
				if (clause2.hasNoName() || clause2.lastArgumentHasAsterisk()) {
					return;
				};
				long nameCode2= clause2.getFunctorName();
				int arity2= clause2.getArity();
				if (nameCode1==nameCode2) {
					nameIsFound= true;
					if (arity1==arity2) {
						nameAndArityAreFound= true;
					} else if (nameAndArityAreFound) {
						master.handleError(new ClausesOfTheSameProcedureShouldBeGrouped(p1),iX);
						break;
					}
				} else if (nameIsFound) {
					master.handleError(new ClausesWithTheSameNameShouldBeGrouped(p1),iX);
				}
			}
		}
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE CLAUSE ***********************************************
//*********************************************************************
//*********************************************************************
	//
	public ActorPrologClause parseRootClause(boolean isTheModelSection, ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		return parseClause(isTheModelSection,iX);
	}
	//
	public ActorPrologClause parseClause(boolean isTheModelSection, ChoisePoint iX) throws SyntaxError {
		try {
			checkClauseVariables= true;
			headingAsteriskVariableNumber= noVariableNumber;
			return doParseClause(isTheModelSection,iX);
		} finally {
			checkClauseVariables= false;
			isClauseHeadingElement= false;
		}
	}
	//
	protected ActorPrologClause doParseClause(boolean isTheModelSection, ChoisePoint iX) throws SyntaxError {
		forgetVariableRoles();
		subgoalArray.clear();
		clearFunctionCallTable();
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		int beginningOfTheClause= tokens[position].getPosition();
		isClauseHeadingElement= true;
		ActorPrologAtom headingSimpleAtom= parseAtom(iX);
		isClauseHeadingElement= false;
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		boolean isFunction= false;
		if (headingSimpleAtom.isSimple()) {
			PrologToken secondToken= tokens[position];
			PrologTokenType secondTokenType= secondToken.getType();
			if (secondTokenType==PrologTokenType.EQ) {
				position++;
				Term functionResult= parseExpression(false,0,iX);
				headingSimpleAtom.insertArgument(functionResult,master,iX);
				isFunction= true;
			}
		};
		implementFunctionCallDefinitions(iX);
		ActorPrologSubgoal[] restSubgoals= subgoalArray.toArray(new ActorPrologSubgoal[subgoalArray.size()]);
		subgoalArray.clear();
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken thirdToken= tokens[position];
		PrologTokenType thirdTokenType= thirdToken.getType();
		switch (thirdTokenType) {
		case DOT:
///////////////////////////////////////////////////////////////////////
position++;
if (headingSimpleAtom.atomBeginsWithTheQuestionMark()) {
	if (!isTheModelSection && !robustMode) {
		master.handleError(new ExternalCallIsToBePlacedInTheModelSection(beginningOfTheClause),iX);
	};
	checkIterantVariables(iX);
	checkFunctionVariables(ProhibitedFunctionCallContext.EXTERNAL_CALL,iX);
	checkAnonymousVariables(ProhibitedFunctionCallContext.EXTERNAL_CALL,iX);
	checkWhetherArgumentsAreVariables(headingSimpleAtom,ProhibitedFunctionCallContext.EXTERNAL_CALL,iX);
	return new ActorPrologClause(
		isFunction,
		headingSimpleAtom,
		beginningOfTheClause);
} else {
	subgoalArray.addAll(Arrays.asList(restSubgoals));
	checkSingleVariables(iX);
	return new ActorPrologClause(
		isFunction,
		headingSimpleAtom,
		subgoalArray.toArray(new ActorPrologSubgoal[subgoalArray.size()]),
		getFunctionCallTableArray(),
		beginningOfTheClause);
}
///////////////////////////////////////////////////////////////////////
		case IMPLICATION:
///////////////////////////////////////////////////////////////////////
position++;
if (headingSimpleAtom.atomBeginsWithTheQuestionMark() && !robustMode) {
	master.handleError(new QuestionMarkIsNotExpectedHere(headingSimpleAtom.getQuestionMarkPosition()),iX);
};
while (true) {
	ActorPrologSubgoal subgoal= parseSubgoal(iX);
	implementFunctionCallDefinitions(iX);
	subgoalArray.add(subgoal);
	if (position >= numberOfTokens) {
		throw master.handleUnexpectedEndOfTokenList(tokens,iX);
	};
	PrologToken fourthToken= tokens[position];
	PrologTokenType fourthTokenType= fourthToken.getType();
	switch (fourthTokenType) {
	case DOT:
		position++;
		subgoalArray.addAll(Arrays.asList(restSubgoals));
		checkSingleVariables(iX);
		return new ActorPrologClause(
			isFunction,
			headingSimpleAtom,
			subgoalArray.toArray(new ActorPrologSubgoal[subgoalArray.size()]),
			getFunctionCallTableArray(),
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
///////////////////////////////////////////////////////////////////////
		case COLON:
///////////////////////////////////////////////////////////////////////
position++;
parseLeftSquareBracket(iX);
parseTheExternalKeyword(iX);
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
String externalProcedureName;
PrologToken nextToken= tokens[position];
if (nextToken.getType()==PrologTokenType.STRING_SEGMENT) {
	position++;
	externalProcedureName= parseString(nextToken.getStringValue(master,iX),iX);
} else {
	externalProcedureName= "";
};
parseRightSquareBracket(iX);
parseDot(iX);
if (isTheModelSection && !robustMode) {
	master.handleError(new ExternalClauseIsToBePlacedInTheClausesSection(beginningOfTheClause),iX);
};
checkIterantVariables(iX);
checkFunctionVariables(ProhibitedFunctionCallContext.EXTERNAL_CLAUSE,iX);
checkAnonymousVariables(ProhibitedFunctionCallContext.EXTERNAL_CLAUSE,iX);
checkWhetherArgumentsAreVariables(headingSimpleAtom,ProhibitedFunctionCallContext.EXTERNAL_CLAUSE,iX);
return new ActorPrologClause(
	isFunction,
	headingSimpleAtom,
	externalProcedureName,
	beginningOfTheClause);
///////////////////////////////////////////////////////////////////////
		default:
///////////////////////////////////////////////////////////////////////
master.handleError(new ImplicationIsExpected(thirdToken.getPosition()),iX);
position++;
subgoalArray.addAll(Arrays.asList(restSubgoals));
checkSingleVariables(iX);
return new ActorPrologClause(
	isFunction,
	headingSimpleAtom,
	subgoalArray.toArray(new ActorPrologSubgoal[subgoalArray.size()]),
	getFunctionCallTableArray(),
	beginningOfTheClause);
///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected boolean isClauseHeadingElement() {
		return isClauseHeadingElement;
	}
	//
	@Override
	protected void checkClauseHeadingAsteriskVariable(int variableNumber, int position, ChoisePoint iX) throws SyntaxError {
		if (checkClauseVariables) {
			if (isClauseHeadingElement) {
				headingAsteriskVariableNumber= variableNumber;
			} else {
				if (variableNumber==anonymousVariableNumber && !robustMode) {
					master.handleError(new AnonymousVariableCannotBeMarkedByTheAsteriskInClauseBody(position),iX);
				};
				if (headingAsteriskVariableNumber!=variableNumber && !robustMode) {
					master.handleError(new ThisVariableIsNotMarkedByTheAsteriskInClauseHeading(position),iX);
				}
			}
		}
	}
	//
	@Override
	protected void declareMetaFunctorVariable(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			if (checkClauseVariables) {
				if (!isClauseHeadingElement && !robustMode) {
					master.handleError(new AnonymousVariableCannotBeMetaFunctorInClauseBody(p),iX);
				}
			};
			return;
		};
		super.declareMetaFunctorVariable(variableNumber,p,iX);
	}
	//
	@Override
	protected void declareMetaPredicateVariable(int variableNumber, int p, ChoisePoint iX) throws SyntaxError {
		if (variableNumber==anonymousVariableNumber) {
			if (checkClauseVariables) {
				if (!isClauseHeadingElement && !robustMode) {
					master.handleError(new AnonymousVariableCannotBeMetaPredicateInClauseBody(p),iX);
				}
			};
			return;
		};
		super.declareMetaPredicateVariable(variableNumber,p,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void implementFunctionCallDefinitions(ChoisePoint iX) throws SyntaxError {
		ArrayList<FunctionCallDefinition> functionCallDefinitionArray;
		if (useSecondVariableNameRegister) {
			functionCallDefinitionArray= secondFunctionCallDefinitionArray;
		} else {
			functionCallDefinitionArray= firstFunctionCallDefinitionArray;
		};
		for (int k=0; k < functionCallDefinitionArray.size(); k++) {
			FunctionCallDefinition definition;
			definition= functionCallDefinitionArray.get(k);
			if (definition.isImplemented()) {
				continue;
			};
			definition.setIsImplemented(true);
			ActorPrologSubgoal subgoal= definition.getSubgoal();
			int variableNumber1= definition.getVariableNumber();
			boolean currentIsClauseHeadingElement= definition.isClauseHeadingElement();
			int currentPosition= definition.getPosition();
			boolean isNested= definition.isNested();
			if (currentIsClauseHeadingElement && !isNested) {
				int variableNumber2= registerPrimaryFunctionVariable(currentPosition,iX);
				subgoal.assignPrimaryFunctionVariable(variableNumber2,master,iX);
				subgoalArray.add(subgoal);
				Term[] auxiliarySubgoalArguments= new Term[2];
				Term variable1= parseSecondaryFunctionVariable(variableNumber1,currentPosition,iX);
				Term variable2= parsePrimaryFunctionVariable(variableNumber2,currentPosition,iX);
				auxiliarySubgoalArguments[0]= variable1;
				auxiliarySubgoalArguments[1]= variable2;
				ActorPrologSubgoal auxiliarySubgoal=
					new ActorPrologBuiltInCommand(
						SymbolCodes.symbolCode__equality,
						auxiliarySubgoalArguments,
						currentPosition);
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
	protected void checkSingleVariables(ChoisePoint iX) throws SyntaxError {
		if (robustMode) {
			return;
		};
		Collection<VariableRole> roles;
		if (useSecondVariableNameRegister) {
			roles= secondVariableRoleRegister.values();
		} else {
			roles= firstVariableRoleRegister.values();
		};
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
	protected void checkIterantVariables(ChoisePoint iX) throws SyntaxError {
		if (robustMode) {
			return;
		};
		Collection<VariableRole> roles;
		if (useSecondVariableNameRegister) {
			roles= secondVariableRoleRegister.values();
		} else {
			roles= firstVariableRoleRegister.values();
		};
		Iterator<VariableRole> iteratorOfRoles= roles.iterator();
		while (iteratorOfRoles.hasNext()) {
			VariableRole role= iteratorOfRoles.next();
			if (role.isUsedManyTimes()) {
				int p1= role.getFirstPosition();
				int p2= role.getSecondPosition();
				master.handleError(new TheVariableIsNotUnique(p1,p2),iX);
			}
		}
	}
	//
	protected void checkFunctionVariables(ProhibitedFunctionCallContext context, ChoisePoint iX) throws SyntaxError {
		if (robustMode) {
			return;
		};
		Collection<VariableRole> roles;
		if (useSecondVariableNameRegister) {
			roles= secondVariableRoleRegister.values();
		} else {
			roles= firstVariableRoleRegister.values();
		};
		Iterator<VariableRole> iteratorOfRoles= roles.iterator();
		while (iteratorOfRoles.hasNext()) {
			VariableRole role= iteratorOfRoles.next();
			if (role.mustBeFunctionVariable()) {
				int p= role.getFirstPosition();
				switch (context) {
				case EXTERNAL_CLAUSE:
					master.handleError(new FunctionCallsAreNotAllowedInExternalClauses(p),iX);
					break;
				case EXTERNAL_CALL:
					master.handleError(new FunctionCallsAreNotAllowedInExternalCalls(p),iX);
					break;
				case INITIALIZER:
					master.handleError(new FunctionCallsAreNotAllowedInInitializers(p),iX);
					break;
				}
			}
		}
	}
	//
	protected void checkAnonymousVariables(ProhibitedFunctionCallContext context, ChoisePoint iX) throws SyntaxError {
		if (robustMode) {
			return;
		};
		if (useSecondVariableNameRegister) {
			if (secondAnonymousVariableRegister.size() > 0) {
				switch (context) {
				case EXTERNAL_CLAUSE:
					master.handleError(new AnonymousVariablesAreNotAllowedInExternalClauses(secondAnonymousVariableRegister.get(0)),iX);
					break;
				case EXTERNAL_CALL:
					master.handleError(new AnonymousVariablesAreNotAllowedInExternalCalls(secondAnonymousVariableRegister.get(0)),iX);
					break;
				}
			}
		} else {
			if (firstAnonymousVariableRegister.size() > 0) {
				switch (context) {
				case EXTERNAL_CLAUSE:
					master.handleError(new AnonymousVariablesAreNotAllowedInExternalClauses(firstAnonymousVariableRegister.get(0)),iX);
					break;
				case EXTERNAL_CALL:
					master.handleError(new AnonymousVariablesAreNotAllowedInExternalCalls(firstAnonymousVariableRegister.get(0)),iX);
					break;
				}
			}
		}
	}
	//
	protected void checkWhetherArgumentsAreVariables(ActorPrologAtom heading, ProhibitedFunctionCallContext context, ChoisePoint iX) throws SyntaxError {
		if (robustMode) {
			return;
		};
		Term[] arguments= heading.getArguments();
		int p= heading.getAtomPosition();
		for (int k=0; k < arguments.length; k++) {
			Term argument= arguments[k];
			try {
				AnalyseDataStructure: while (true) {
					long functor= argument.getStructureFunctor(iX);
					if (functor==SymbolCodes.symbolCode_E_p) {
						Term[] terms= argument.getStructureArguments(iX);
						if (terms.length==2) {
							argument= terms[0];
							Term termPosition= terms[1];
							p= termPosition.getSmallIntegerValue(iX);
						}
					} else if (functor==SymbolCodes.symbolCode_E_asterisk) {
						if (context==ProhibitedFunctionCallContext.EXTERNAL_CALL) {
							master.handleError(new AsteriskIsNotAllowedInExternalCalls(p),iX);
						};
						Term[] terms= argument.getStructureArguments(iX);
						if (terms.length==1) {
							argument= terms[0];
						}
					} else {
						break AnalyseDataStructure;
					}
				}
			} catch (TermIsNotAStructure e) {
			} catch (TermIsNotAnInteger e) {
			};
			try {
				long functor= argument.getStructureFunctor(iX);
				if (functor != SymbolCodes.symbolCode_E_var) {
					switch (context) {
					case EXTERNAL_CLAUSE:
						master.handleError(new AllArgumentsOfExternalClauseAreToBeVariables(p),iX);
						break;
					case EXTERNAL_CALL:
						master.handleError(new AllArgumentsOfExternalCallAreToBeVariables(p),iX);
						break;
					}
				}
			} catch (TermIsNotAStructure e) {
				switch (context) {
				case EXTERNAL_CLAUSE:
					master.handleError(new AllArgumentsOfExternalClauseAreToBeVariables(p),iX);
					break;
				case EXTERNAL_CALL:
					master.handleError(new AllArgumentsOfExternalCallAreToBeVariables(p),iX);
					break;
				}
			}
		}
	}
}
