// (c) 2010-2019 IRE RAS Alexei A. Morozov

package morozov.syntax;

import target.*;

import morozov.run.*;
import morozov.syntax.data.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.syntax.scanner.*;
import morozov.syntax.scanner.errors.*;
import morozov.system.converters.*;
import morozov.system.datum.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class GroundTermParser extends ElementaryParser {
	//
	public GroundTermParser(ParserMasterInterface m, boolean rememberPositions) {
		super(m,rememberPositions);
	}
	public GroundTermParser(ParserMasterInterface m, boolean rememberPositions, boolean implementRobustMode) {
		super(m,rememberPositions,implementRobustMode);
	}
	public GroundTermParser(ParserMasterInterface m) {
		super(m);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void modifyTermAsterisk(ArrayList<Term> terms, int textPosition, ChoisePoint iX) throws SyntaxError {
		master.handleError(new TermCannotContainTheAsterisk(textPosition),iX);
	}
	//
	protected Term parseStructure(long functorCode, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		Term[] arguments= parseMetaTerms(iX).toArray(emptyTermArray);
		if (position < numberOfTokens) {
			PrologToken secondToken= tokens[position];
			if (secondToken.getType()==PrologTokenType.R_ROUND_BRACKET) {
				position++;
			} else {
				master.handleError(new RightRoundBracketIsExpected(tokens[position].getPosition()),iX);
				position++;
			};
			Term result= new PrologStructure(functorCode,arguments);
			if (rememberTextPositions) {
				result= attachTermPosition(result,beginningOfTerm);
			};
			return result;
		} else {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		}
	}
	//
	protected Term parseSymbol(long functorCode, int beginningOfTerm) {
		Term result= new PrologSymbol(functorCode);
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	protected Term parseSlot(long functorCode, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		master.handleError(new SymbolShouldBeEnclosedInApostrophesHere(beginningOfTerm),iX);
		Term result= new PrologSymbol(functorCode);
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	protected Term parseInteger(PrologToken token, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		Term result= new PrologInteger(token.getIntegerValue(master,iX));
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	protected Term parseReal(PrologToken token, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		Term result= new PrologReal(token.getRealValue(master,iX));
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	protected Term createTermEmptyList() {
		return PrologEmptyList.instance;
	}
	//
	protected Term parseListTail(int textPosition, ChoisePoint iX) throws SyntaxError {
		master.handleError(new GroundListCannotContainATail(textPosition),iX);
		Term result= PrologEmptyList.instance;
		if (rememberTextPositions) {
			result= attachTermPosition(result,textPosition);
		};
		return result;
	}
	//
	protected Term createTermListElement(Term internalTerm, Term tail) {
		return new PrologList(internalTerm,tail);
	}
	//
	protected Term parseWorld(PrologToken token, ChoisePoint iX) throws SyntaxError {
		int beginningOfTerm= token.getPosition();
		int p2= position + 1;
		if (p2 >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		long symbolCode;
		PrologToken nextToken= tokens[position];
		Term result;
		if (nextToken.getType()==PrologTokenType.SYMBOL) {
///////////////////////////////////////////////////////////////////////
if (nextToken.isInQuotes(master,iX)) {
	symbolCode= nextToken.getSymbolCode(master,iX);
} else {
	master.handleError(new SymbolShouldBeEnclosedInApostrophesHere(nextToken.getPosition()),iX);
	symbolCode= nextToken.getSymbolCode(master,iX);
};
PrologToken closingToken= tokens[p2];
if (closingToken.getType()==PrologTokenType.R_ROUND_BRACKET) {
	position= position + 2;
} else {
	master.handleError(new RightRoundBracketIsExpected(closingToken.getPosition()),iX);
	position++;
};
// position= position + 2;
String symbolText= SymbolNames.retrieveSymbolName(symbolCode).toRawString(null);
byte[] byteArray= GeneralConverters.string2ByteArray(symbolText);
InputStream inputStream= new ByteArrayInputStream(byteArray);
try {
	try {
		ObjectInputStream objectInputStream= new DataStoreInputStream(inputStream/*,false*/);
		try {
			result= (AbstractWorld)objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			master.handleError(new WorldDeserializingError(nextToken.getPosition(),e),iX);
			result= PrologUnknownValue.instance;
		} finally {
			objectInputStream.close();
		}
	} finally {
		inputStream.close();
	}
} catch (IOException e) {
	master.handleError(new WorldDeserializingError(nextToken.getPosition(),e),iX);
	result= PrologUnknownValue.instance;
}
///////////////////////////////////////////////////////////////////////
		} else {
			master.handleError(new SymbolIsExpected(nextToken.getPosition()),iX);
			position++;
			result= PrologUnknownValue.instance;
		};
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	protected Term parseVariable(String variableName, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		master.handleError(new GroundTermCannotBeAVariable(beginningOfTerm),iX);
		Term result= new PrologString(variableName);
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	protected Term createTermEmptySet() {
		return PrologEmptySet.instance;
	}
	//
	protected Term createTermSetTail(int textPosition, ChoisePoint iX) throws SyntaxError {
		master.handleError(new GroundSetCannotContainATail(textPosition),iX);
		Term result= PrologEmptySet.instance;
		if (rememberTextPositions) {
			result= attachTermPosition(result,textPosition);
		};
		return result;
	}
	//
	protected Term createTermSetElement(NamedTerm pair, Term tail) {
		return new PrologSet(pair.getKeyCode(),pair.getValue(),tail);
	}
	//
	protected NamedTerm parseInestimablePairValue(long pairNameCode, boolean isInQuotes, int pairPosition, ChoisePoint iX) throws SyntaxError {
		if (isInQuotes) {
			master.handleError(new ColonIsExpected(pairPosition),iX);
			return new NamedTerm(pairNameCode,true,PrologUnknownValue.instance,pairPosition);
		} else if (symbolIsASlot(pairNameCode)) {
			Term[] termArray= new Term[1];
			termArray[0]= new PrologSymbol(pairNameCode);
			Term pairValue= new PrologStructure(SymbolCodes.symbolCode_E_slot,termArray);
			return new NamedTerm(pairNameCode,true,pairValue,pairPosition);
		} else {
			master.handleError(new SymbolShouldBeEnclosedInApostrophesHere(pairPosition),iX);
			return new NamedTerm(pairNameCode,true,PrologUnknownValue.instance,pairPosition);
		}
	}
	//
	protected NamedTerm parseInestimablePairValue(BigInteger key, int pairPosition, ChoisePoint iX) throws SyntaxError {
		master.handleError(new ColonIsExpected(pairPosition),iX);
		long pairNameCode= PrologInteger.toLong(key);
		return new NamedTerm(pairNameCode,false,PrologUnknownValue.instance,pairPosition);
	}
	//
	public Term parseExpression(boolean insertMinus, int minusPosition, ChoisePoint iX) throws SyntaxError {
		previousExpressionWasAVariable= false;
		return parseTermAfterMinusIfNecessary(insertMinus,minusPosition,iX);
	}
	//
	protected Term parseFunctionCallInSlot(long symbolCode, int beginningOfTerm, int questionMarkPosition, ChoisePoint iX) throws SyntaxError {
		master.handleError(new GroundTermCannotContainFunctionCalls(questionMarkPosition),iX);
		Term result= PrologUnknownValue.instance;
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	protected Term parseTheElementFunctionCall(long symbolCode, int beginningOfTerm, int questionMarkPosition, ChoisePoint iX) throws SyntaxError {
		master.handleError(new GroundTermCannotContainFunctionCalls(questionMarkPosition),iX);
		Term result= PrologUnknownValue.instance;
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		return result;
	}
	//
	protected Term attachTermPosition(Term argument, long position) {
		return new TermPosition(argument,position);
	}
}
