// (c) 2010-2019 IRE RAS Alexei A. Morozov

package morozov.syntax;

import target.*;

import morozov.run.*;
import morozov.syntax.data.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.syntax.scanner.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.signals.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.terms.*;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class ElementaryParser {
	//
	protected ParserMasterInterface master;
	//
	protected PrologToken[] tokens;
	protected int numberOfTokens;
	protected int position;
	protected boolean rememberTextPositions;
	protected boolean robustMode= false;
	//
	protected HashSet<Long> slotNameHash;
	//
	protected boolean previousExpressionWasAVariable= false;
	protected boolean lastTermHasAsterisk= false;
	//
	protected static Term[] emptyTermArray= new Term[0];
	//
	///////////////////////////////////////////////////////////////
	//
	public ElementaryParser(ParserMasterInterface m, boolean rememberPositions) {
		master= m;
		rememberTextPositions= rememberPositions;
	}
	public ElementaryParser(ParserMasterInterface m, boolean rememberPositions, boolean implementRobustMode) {
		master= m;
		rememberTextPositions= rememberPositions;
		robustMode= implementRobustMode;
	}
	public ElementaryParser(ParserMasterInterface m) {
		master= m;
		rememberTextPositions= false;
		robustMode= false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setTokens(PrologToken[] array, int p, boolean keepTextPositions) {
		tokens= array;
		numberOfTokens= tokens.length;
		position= p;
		rememberTextPositions= keepTextPositions;
	}
	public void setTokens(PrologToken[] array) {
		tokens= array;
	}
	public PrologToken[] getTokens() {
		return tokens;
	}
	//
	public void setPosition(int p) {
		position= p;
	}
	public int getPosition() {
		return position;
	}
	//
	public void setSlotNameHash(HashSet<Long> set) {
		slotNameHash= set;
	}
	public HashSet<Long> getSlotNameHash() {
		return slotNameHash;
	}
	//
	public boolean lastTermHasAsterisk() {
		return lastTermHasAsterisk;
	}
	//
	public Term getTermParsedSlotNames() {
		Term list= PrologEmptyList.instance;
		Iterator<Long> slotNameSetIterator= slotNameHash.iterator();
		while (slotNameSetIterator.hasNext()) {
			Long currentName= slotNameSetIterator.next();
			list= new PrologList(new PrologSymbol(currentName),list);
		};
		return list;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term[] stringToTerms(String text, ChoisePoint iX) throws SyntaxError {
		LexicalScanner scanner= new LexicalScanner(master,robustMode);
		tokens= scanner.analyse(text,iX);
		numberOfTokens= tokens.length;
		if (numberOfTokens > 0) {
			position= 0;
			Term[] terms= parseTermSequence(null,iX);
			return terms;
		} else {
			return emptyTermArray;
		}
	}
	//
	public Term[] parseTermSequence(PrologTokenType closingTokenType, ChoisePoint iX) throws SyntaxError {
		boolean termsAreInsideBrackets= false;
		if (closingTokenType != null) {
			termsAreInsideBrackets= true;
		};
		lastTermHasAsterisk= false;
		ArrayList<Term> terms= new ArrayList<>();
		while (true) {
			if (position < numberOfTokens) {
				PrologToken frontToken= tokens[position];
				PrologTokenType frontTokenType= frontToken.getType();
				if (termsAreInsideBrackets) {
					if (frontTokenType==closingTokenType) {
						position++;
						return terms.toArray(new Term[terms.size()]);
					}
				} else if (frontTokenType==PrologTokenType.END_OF_TEXT) {
					return terms.toArray(new Term[terms.size()]);
				};
				terms.add(parseExpression(false,0,iX));
				if (position < numberOfTokens) {
///////////////////////////////////////////////////////////////////////
if (termsAreInsideBrackets) {
	PrologToken secondToken= tokens[position];
	PrologTokenType secondTokenType= secondToken.getType();
	switch (secondTokenType) {
	case COMMA:
		position++;
		break;
	case MULTIPLY:
		position++;
		modifyTermAsterisk(terms,secondToken.getPosition(),iX);
		lastTermHasAsterisk= true;
		parseRightRoundBracket(iX);
		return terms.toArray(new Term[terms.size()]);
	default:
		if (secondTokenType==closingTokenType) {
			position++;
			return terms.toArray(new Term[terms.size()]);
		} else {
			master.handleError(new CommaIsExpected(secondToken.getPosition()),iX);
			position++;
		}
	}
} else {
	PrologToken secondToken= tokens[position];
	PrologTokenType secondTokenType= secondToken.getType();
	switch (secondTokenType) {
	case SEMICOLON:
	case COMMA:
	case DOT:
		position++;
		break;
	default:
		break;
	}
}
///////////////////////////////////////////////////////////////////////
				} else {
					throw master.handleUnexpectedEndOfTokenList(tokens,iX);
				}
			} else {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			}
		}
	}
	//
	abstract protected void modifyTermAsterisk(ArrayList<Term> terms, int textPosition, ChoisePoint iX) throws SyntaxError;
	//
	///////////////////////////////////////////////////////////////
	//
	public ArrayList<InternalTerm> parseInternalMetaTerms(ChoisePoint iX) throws SyntaxError {
		ArrayList<InternalTerm> terms= new ArrayList<>();
		boolean commaExpected= false;
		while (true) {
			if (commaExpected) {
				if (position < numberOfTokens) {
					if (tokens[position].getType()==PrologTokenType.COMMA) {
						position++;
					} else {
						previousExpressionWasAVariable= false;
						return terms;
					}
				} else {
					throw master.handleUnexpectedEndOfTokenList(tokens,iX);
				}
			} else {
				commaExpected= true;
			};
			if (position < numberOfTokens) {
				int currentPosition= tokens[position].getPosition();
				terms.add(new InternalTerm(parseExpression(false,0,iX),currentPosition));
			} else {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			}
		}
	}
	//
	public ArrayList<Term> parseMetaTerms(ChoisePoint iX) throws SyntaxError {
		ArrayList<Term> terms= new ArrayList<>();
		boolean commaExpected= false;
		while (true) {
			if (commaExpected) {
				if (position < numberOfTokens) {
					if (tokens[position].getType()==PrologTokenType.COMMA) {
						position++;
					} else {
						previousExpressionWasAVariable= false;
						return terms;
					}
				} else {
					throw master.handleUnexpectedEndOfTokenList(tokens,iX);
				}
			} else {
				commaExpected= true;
			};
			terms.add(parseExpression(false,0,iX));
		}
	}
	//
//*********************************************************************
//*********************************************************************
//******** PARSE TERM *************************************************
//*********************************************************************
//*********************************************************************
	//
	public Term parseRootTerm(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		return parseTerm(iX);
	}
	//
	public Term parseTerm(ChoisePoint iX) throws SyntaxError {
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			int beginningOfTerm= frontToken.getPosition();
			PrologTokenType frontTokenType= frontToken.getType();
			switch (frontTokenType) {
			case SYMBOL:
				{
///////////////////////////////////////////////////////////////////////
previousExpressionWasAVariable= false;
int p2= position + 1;
if (p2 < numberOfTokens) {
	PrologToken secondToken= tokens[p2];
	PrologTokenType secondTokenType= secondToken.getType();
	if (secondTokenType==PrologTokenType.L_ROUND_BRACKET) {
		checkWhetherSymbolIsNotASlot(frontToken,iX);
		position= position + 2;
		return parseStructure(frontToken.getSymbolCode(master,iX),beginningOfTerm,iX);
	} else if (secondTokenType==PrologTokenType.L_BRACE) {
		position= position + 2;
		ArrayList<LabeledTerm> arguments= new ArrayList<>();
		Term result= new PrologSymbol(frontToken.getSymbolCode(master,iX));
		if (rememberTextPositions) {
			result= attachTermPosition(result,beginningOfTerm);
		};
		arguments.add(new LabeledTerm(0,false,result,beginningOfTerm));
		return parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
	} else if (secondTokenType==PrologTokenType.QUESTION_MARK) {
		checkWhetherSymbolIsASlot(frontToken,iX);
		position= position + 2;
		return parseFunctionCallInSlot(frontToken.getSymbolCode(master,iX),beginningOfTerm,secondToken.getPosition(),iX);
	} else if (secondTokenType==PrologTokenType.L_SQUARE_BRACKET) {
		checkWhetherSymbolIsASlot(frontToken,iX);
		position= position + 2;
		return parseTheElementFunctionCall(frontToken.getSymbolCode(master,iX),beginningOfTerm,secondToken.getPosition(),iX);
	}
};
position++;
return parseSymbolOrSlot(frontToken,beginningOfTerm,iX);
///////////////////////////////////////////////////////////////////////
				}
				// break;
			case L_SQUARE_BRACKET:
///////////////////////////////////////////////////////////////////////
previousExpressionWasAVariable= false;
position++;
return parseList(beginningOfTerm,iX);
///////////////////////////////////////////////////////////////////////
				// break;
			case L_BRACE:
				{
///////////////////////////////////////////////////////////////////////
previousExpressionWasAVariable= false;
position++;
ArrayList<LabeledTerm> arguments= new ArrayList<>();
return parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
///////////////////////////////////////////////////////////////////////
				}
				// break;
			case INTEGER:
				{
///////////////////////////////////////////////////////////////////////
previousExpressionWasAVariable= false;
int p2= position + 1;
if (p2 < numberOfTokens && tokens[p2].getType()==PrologTokenType.L_BRACE) {
	position= position + 2;
	ArrayList<LabeledTerm> arguments= new ArrayList<>();
	Term firstPairValue= parseInteger(frontToken,beginningOfTerm,iX);
	arguments.add(new LabeledTerm(0,false,firstPairValue,beginningOfTerm));
	return parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
} else {
	position++;
	return parseInteger(frontToken,beginningOfTerm,iX);
}
///////////////////////////////////////////////////////////////////////
				}
				// break;
			case REAL:
				{
///////////////////////////////////////////////////////////////////////
previousExpressionWasAVariable= false;
int p2= position + 1;
if (p2 < numberOfTokens && tokens[p2].getType()==PrologTokenType.L_BRACE) {
	position= position + 2;
	ArrayList<LabeledTerm> arguments= new ArrayList<>();
	Term firstPairValue= parseReal(frontToken,beginningOfTerm,iX);
	arguments.add(new LabeledTerm(0,false,firstPairValue,beginningOfTerm));
	return parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
} else {
	position++;
	return parseReal(frontToken,beginningOfTerm,iX);
}
///////////////////////////////////////////////////////////////////////
				}
				// break;
			case STRING_SEGMENT:
				{
///////////////////////////////////////////////////////////////////////
previousExpressionWasAVariable= false;
position++;
String stringContent= parseString(frontToken.getStringValue(master,iX),iX);
if (position < numberOfTokens && tokens[position].getType()==PrologTokenType.L_BRACE) {
	position++;
	ArrayList<LabeledTerm> arguments= new ArrayList<>();
	Term firstPairValue= new PrologString(stringContent);
	if (rememberTextPositions) {
		firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
	};
	arguments.add(new LabeledTerm(0,false,firstPairValue,beginningOfTerm));
	return parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
} else {
	Term result= new PrologString(stringContent);
	if (rememberTextPositions) {
		result= attachTermPosition(result,beginningOfTerm);
	};
	return result;
}
///////////////////////////////////////////////////////////////////////
				}
				// break;
			case BINARY_SEGMENT:
				{
///////////////////////////////////////////////////////////////////////
previousExpressionWasAVariable= false;
position++;
byte[] binaryContent= parseBinary(frontToken.getBinaryValue(master,iX),iX);
if (position < numberOfTokens && tokens[position].getType()==PrologTokenType.L_BRACE) {
	position++;
	ArrayList<LabeledTerm> arguments= new ArrayList<>();
	Term firstPairValue= new PrologBinary(binaryContent);
	if (rememberTextPositions) {
		firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
	};
	arguments.add(new LabeledTerm(0,false,firstPairValue,beginningOfTerm));
	return parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
} else {
	Term result= new PrologBinary(binaryContent);
	if (rememberTextPositions) {
		result= attachTermPosition(result,beginningOfTerm);
	};
	return result;
}
///////////////////////////////////////////////////////////////////////
				}
				// break;
			case NUMBER_SIGN:
				{
///////////////////////////////////////////////////////////////////////
previousExpressionWasAVariable= false;
int p2= position + 1;
if (p2 < numberOfTokens && tokens[p2].getType()==PrologTokenType.L_BRACE) {
	position= position + 2;
	ArrayList<LabeledTerm> arguments= new ArrayList<>();
	Term firstPairValue= PrologUnknownValue.instance;
	if (rememberTextPositions) {
		firstPairValue= attachTermPosition(firstPairValue,beginningOfTerm);
	};
	arguments.add(new LabeledTerm(0,false,firstPairValue,beginningOfTerm));
	return parseUnderdeterminedSet(arguments,beginningOfTerm,iX);
} else {
	position++;
	Term result= PrologUnknownValue.instance;
	if (rememberTextPositions) {
		result= attachTermPosition(result,beginningOfTerm);
	};
	return result;
}
///////////////////////////////////////////////////////////////////////
				}
				// break;
			case MINUS:
///////////////////////////////////////////////////////////////////////
previousExpressionWasAVariable= false;
position++;
return parseTermAfterMinus(beginningOfTerm,iX);
///////////////////////////////////////////////////////////////////////
				// break;
			case L_ROUND_BRACKET:
///////////////////////////////////////////////////////////////////////
previousExpressionWasAVariable= false;
position++;
return parseWorld(frontToken,iX);
///////////////////////////////////////////////////////////////////////
				// break;
			case VARIABLE:
///////////////////////////////////////////////////////////////////////
position++;
return parseVariable(frontToken.getVariableName(master,iX),beginningOfTerm,iX);
///////////////////////////////////////////////////////////////////////
				// break;
			case QUESTION_MARK:
///////////////////////////////////////////////////////////////////////
position++;
return parseFunctionCallInSlot(SymbolCodes.symbolCode_E_self,beginningOfTerm,beginningOfTerm,iX);
///////////////////////////////////////////////////////////////////////
			default:
///////////////////////////////////////////////////////////////////////
master.handleError(new UnexpectedToken(frontToken),iX);
position++;
Term result= PrologUnknownValue.instance;
if (rememberTextPositions) {
	result= attachTermPosition(result,beginningOfTerm);
};
return result;
///////////////////////////////////////////////////////////////////////
			}
		} else {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term parseSymbolOrSlot(PrologToken token, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		if (token.isInQuotes(master,iX)) {
			return parseSymbol(token.getSymbolCode(master,iX),beginningOfTerm);
		} else {
			return parseSlot(token.getSymbolCode(master,iX),beginningOfTerm,iX);
		}
	}
	//
	protected String parseString(String firstSegment, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			return firstSegment;
		} else {
			PrologToken secondToken= tokens[position];
			PrologTokenType secondTokenType= secondToken.getType();
			if (secondTokenType==PrologTokenType.STRING_SEGMENT) {
				StringBuilder buffer= new StringBuilder();
				buffer.append(firstSegment);
				position++;
				String secondSegment= secondToken.getStringValue(master,iX);
				buffer.append(secondSegment);
				while (true) {
					if (position >= numberOfTokens) {
						return buffer.toString();
					};
					PrologToken thirdToken= tokens[position];
					PrologTokenType thirdTokenType= thirdToken.getType();
					if (thirdTokenType==PrologTokenType.STRING_SEGMENT) {
						position++;
						String thirdSegment= thirdToken.getStringValue(master,iX);
						buffer.append(thirdSegment);
					} else {
						return buffer.toString();
					}
				}
			} else {
				return firstSegment;
			}
		}
	}
	//
	protected byte[] parseBinary(byte[] firstSegment, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			return firstSegment;
		} else {
			PrologToken secondToken= tokens[position];
			PrologTokenType secondTokenType= secondToken.getType();
			if (secondTokenType==PrologTokenType.BINARY_SEGMENT) {
				ArrayList<byte[]> arrayList= new ArrayList<>();
				arrayList.add(firstSegment);
				position++;
				byte[] secondSegment= secondToken.getBinaryValue(master,iX);
				arrayList.add(secondSegment);
				while (true) {
					if (position >= numberOfTokens) {
						return GeneralConverters.arrayListToByteArray(arrayList);
					};
					PrologToken thirdToken= tokens[position];
					PrologTokenType thirdTokenType= thirdToken.getType();
					if (thirdTokenType==PrologTokenType.BINARY_SEGMENT) {
						position++;
						byte[] thirdSegment= thirdToken.getBinaryValue(master,iX);
						arrayList.add(thirdSegment);
					} else {
						return GeneralConverters.arrayListToByteArray(arrayList);
					}
				}
			} else {
				return firstSegment;
			}
		}
	}
	//
	abstract public void setUseSecondVariableNameRegister(boolean mode);
	abstract protected Term parseStructure(long functorCode, int beginningOfTerm, ChoisePoint iX) throws SyntaxError;
	abstract protected Term parseSymbol(long functorCode, int beginningOfTerm);
	abstract protected Term parseSlot(long functorCode, int beginningOfTerm, ChoisePoint iX) throws SyntaxError;
	abstract protected Term parseInteger(PrologToken token, int beginningOfTerm, ChoisePoint iX) throws SyntaxError;
	abstract protected Term parseReal(PrologToken token, int beginningOfTerm, ChoisePoint iX) throws SyntaxError;
	abstract protected Term createTermEmptyList();
	abstract protected Term parseListTail(int textPosition, ChoisePoint iX) throws SyntaxError;
	abstract public Term createTermListElement(Term internalTerm, Term tail);
	abstract protected Term parseWorld(PrologToken token, ChoisePoint iX) throws SyntaxError;
	abstract protected Term parseVariable(String variableName, int beginningOfTerm, ChoisePoint iX) throws SyntaxError;
	abstract protected Term parseFunctionCallInSlot(long symbolCode, int beginningOfTerm, int questionMarkPosition, ChoisePoint iX) throws SyntaxError;
	abstract protected Term parseTheElementFunctionCall(long symbolCode, int beginningOfTerm, int questionMarkPosition, ChoisePoint iX) throws SyntaxError;
	//
	///////////////////////////////////////////////////////////////
	//
	public Term parseTermAfterMinusIfNecessary(boolean insertMinus, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		if (insertMinus) {
			return parseTermAfterMinus(beginningOfTerm,iX);
		} else {
			return parseTerm(iX);
		}
	}
	protected Term parseTermAfterMinus(int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken secondToken= tokens[position];
		PrologTokenType secondTokenType= secondToken.getType();
		switch (secondTokenType) {
		case INTEGER:
			{
///////////////////////////////////////////////////////////////////////
if (!robustMode && secondToken.isExtendedNumber(master,iX)) {
	master.handleError(new MinusIsNotAllowedBeforeExtendedNumbers(secondToken.getPosition()),iX);
};
position++;
Term result= new PrologInteger(secondToken.getIntegerValue(master,iX).negate());
if (rememberTextPositions) {
	result= attachTermPosition(result,beginningOfTerm);
};
return result;
///////////////////////////////////////////////////////////////////////
			}
		case REAL:
			{
///////////////////////////////////////////////////////////////////////
if (!robustMode && secondToken.isExtendedNumber(master,iX)) {
	master.handleError(new MinusIsNotAllowedBeforeExtendedNumbers(secondToken.getPosition()),iX);
};
position++;
Term result= new PrologReal(-secondToken.getRealValue(master,iX));
if (rememberTextPositions) {
	result= attachTermPosition(result,beginningOfTerm);
};
return result;
///////////////////////////////////////////////////////////////////////
			}
		default:
			master.handleError(new UnexpectedToken(secondToken),iX);
			position++;
			Term result= new PrologInteger(0);
			if (rememberTextPositions) {
				result= attachTermPosition(result,beginningOfTerm);
			};
			return result;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term parseList(int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken secondToken= tokens[position];
		PrologTokenType secondTokenType= secondToken.getType();
		if (secondTokenType==PrologTokenType.R_SQUARE_BRACKET) {
			position++;
			Term result= createTermEmptyList();
			if (rememberTextPositions) {
				result= attachTermPosition(result,beginningOfTerm);
			};
			return result;
		} else {
			ArrayList<InternalTerm> arguments= parseInternalMetaTerms(iX);
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			PrologToken thirdToken= tokens[position];
			PrologTokenType thirdTokenType= thirdToken.getType();
			Term result;
			switch (thirdTokenType) {
			case R_SQUARE_BRACKET:
				result= createTermEmptyList();
				if (rememberTextPositions) {
					result= attachTermPosition(result,tokens[position].getPosition());
				};
				position++;
				break;
			case BAR:
				position++;
				result= parseListTail(thirdToken.getPosition(),iX);
				parseRightSquareBracket(iX);
				break;
			default:
				master.handleError(new RightSquareBracketIsExpected(thirdToken.getPosition()),iX);
				position++;
				result= createTermEmptyList();
				if (rememberTextPositions) {
					result= attachTermPosition(result,tokens[position].getPosition());
				};
				break;
			};
			for (int k=arguments.size()-1; k >= 0; k--) {
				InternalTerm internalTerm= arguments.get(k);
				result= createTermListElement(internalTerm.getValue(),result);
				if (rememberTextPositions) {
					result= attachTermPosition(result,internalTerm.getPosition());
				}
			};
			return result;
		}
	}
	//
	protected ListElements parseListElements(int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken secondToken= tokens[position];
		PrologTokenType secondTokenType= secondToken.getType();
		if (secondTokenType==PrologTokenType.R_SQUARE_BRACKET) {
			position++;
			Term result= createTermEmptyList();
			if (rememberTextPositions) {
				result= attachTermPosition(result,beginningOfTerm);
			};
			return new ListElements(null,false,result,-1);
		} else {
			ArrayList<InternalTerm> arguments= parseInternalMetaTerms(iX);
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			PrologToken thirdToken= tokens[position];
			PrologTokenType thirdTokenType= thirdToken.getType();
			Term result;
			boolean hasTail= false;
			int barPosition= -1;
			switch (thirdTokenType) {
			case R_SQUARE_BRACKET:
				result= createTermEmptyList();
				if (rememberTextPositions) {
					result= attachTermPosition(result,tokens[position].getPosition());
				};
				position++;
				break;
			case BAR:
				position++;
				result= parseListTail(thirdToken.getPosition(),iX);
				parseRightSquareBracket(iX);
				hasTail= true;
				barPosition= thirdToken.getPosition();
				break;
			default:
				master.handleError(new RightSquareBracketIsExpected(thirdToken.getPosition()),iX);
				position++;
				result= createTermEmptyList();
				if (rememberTextPositions) {
					result= attachTermPosition(result,tokens[position].getPosition());
				};
				break;
			};
			return new ListElements(arguments,hasTail,result,barPosition);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term parseUnderdeterminedSet(ArrayList<LabeledTerm> arguments, int beginningOfTerm, ChoisePoint iX) throws SyntaxError {
		Term result;
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken secondToken= tokens[position];
		int secontTokenPosition= secondToken.getPosition();
		PrologTokenType secondTokenType= secondToken.getType();
		switch (secondTokenType) {
		case R_BRACE:
			result= createTermEmptySet();
			if (rememberTextPositions) {
				result= attachTermPosition(result,beginningOfTerm);
			};
			position++;
			break;
		case BAR:
			if (arguments.size() > 0 || robustMode) {
				position++;
				result= parseSetTail(secontTokenPosition,iX);
				parseRightBrace(iX);
			} else {
				master.handleError(new EmptySetCannotContainATail(secontTokenPosition),iX);
				position++;
				result= createTermEmptySet();
				if (rememberTextPositions) {
					result= attachTermPosition(result,beginningOfTerm);
				}
			};
			break;
		default:
			result= parsePairs(arguments,iX);
			previousExpressionWasAVariable= false;
			parseRightBrace(iX);
			break;
		};
		for (int n=arguments.size()-1; n >= 0; n--) {
			LabeledTerm pair= arguments.get(n);
			long key= pair.getKeyCode();
			for (int k=0; k < n; k++) {
				if (arguments.get(k).getKeyCode()==key) {
					master.handleError(new UnderdeterminedSetCannotContainElementsWithEqualNames(pair.getPosition()),iX);
				}
			};
			result= createTermSetElement(pair,result);
			if (rememberTextPositions) {
				result= attachTermPosition(result,pair.getPosition());
			}
		};
		previousExpressionWasAVariable= false;
		return result;
	}
	//
	abstract protected Term createTermEmptySet();
	abstract protected Term parseSetTail(int textPosition, ChoisePoint iX) throws SyntaxError;
	abstract protected Term createTermSetElement(LabeledTerm pair, Term tail);
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term parsePairs(ArrayList<LabeledTerm> terms, ChoisePoint iX) throws SyntaxError {
		boolean commaExpected= false;
		while (true) {
			if (commaExpected) {
				if (position >= numberOfTokens) {
					throw master.handleUnexpectedEndOfTokenList(tokens,iX);
				};
				PrologToken secondToken= tokens[position];
				int secontTokenPosition= secondToken.getPosition();
				PrologTokenType secondTokenType= secondToken.getType();
				switch (secondTokenType) {
				case COMMA:
					position++;
					break;
				case BAR:
					{
					position++;
					Term tail= parseSetTail(secontTokenPosition,iX);
					return tail;
					}
				default:
					{
					Term tail= createTermEmptySet();
					if (rememberTextPositions) {
						tail= attachTermPosition(tail,secontTokenPosition);
					};
					return tail;
					}
				}
			} else {
				commaExpected= true;
			};
			if (position >= numberOfTokens) {
				throw master.handleUnexpectedEndOfTokenList(tokens,iX);
			};
			PrologToken frontToken= tokens[position];
			PrologTokenType frontTokenType= frontToken.getType();
			switch (frontTokenType) {
			case SYMBOL:
				{
///////////////////////////////////////////////////////////////////////
int pairPosition= frontToken.getPosition();
position++;
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken secondToken= tokens[position];
PrologTokenType secondTokenType= secondToken.getType();
switch (secondTokenType) {
case COLON:
	{
		checkWhetherSymbolIsNotASlot(frontToken,iX);
		position++;
		long pairNameCode= frontToken.getSymbolCode(master,iX);
		Term pairValue= parseExpression(false,0,iX);
		terms.add(new LabeledTerm(pairNameCode,true,pairValue,pairPosition));
	};
	break;
case IMPLICATION:
	{
		checkWhetherSymbolIsNotASlot(frontToken,iX);
		position++;
		long pairNameCode= frontToken.getSymbolCode(master,iX);
		Term pairValue= parseTermAfterMinus(pairPosition,iX);
		terms.add(new LabeledTerm(pairNameCode,true,pairValue,pairPosition));
	};
	break;
default:
	terms.add(parseInestimablePairValue(frontToken.getSymbolCode(master,iX),frontToken.isInQuotes(master,iX),pairPosition,iX));
	break;
}
///////////////////////////////////////////////////////////////////////
				};
				break;
			case INTEGER:
				{
///////////////////////////////////////////////////////////////////////
int pairPosition= frontToken.getPosition();
position++;
if (position >= numberOfTokens) {
	throw master.handleUnexpectedEndOfTokenList(tokens,iX);
};
PrologToken secondToken= tokens[position];
PrologTokenType secondTokenType= secondToken.getType();
switch (secondTokenType) {
case COLON:
	{
		position++;
		BigInteger key= frontToken.getIntegerValue(master,iX);
		if (key.compareTo(BigInteger.ZERO) < 0) {
			master.handleError(new NegativeNumbersAreNotAllowedHere(frontToken.getPosition()),iX);
		} else {
			long pairNameCode= Arithmetic.toLong(key);
			Term pairValue= parseExpression(false,0,iX);
			terms.add(new LabeledTerm(pairNameCode,false,pairValue,pairPosition));
		}
	};
	break;
case IMPLICATION:
	{
		position++;
		BigInteger key= frontToken.getIntegerValue(master,iX);
		if (key.compareTo(BigInteger.ZERO) < 0) {
			master.handleError(new NegativeNumbersAreNotAllowedHere(frontToken.getPosition()),iX);
		} else {
			long pairNameCode= Arithmetic.toLong(key);
			Term pairValue= parseTermAfterMinus(pairPosition,iX);
			terms.add(new LabeledTerm(pairNameCode,false,pairValue,pairPosition));
		}
	};
	break;
default:
	terms.add(parseInestimablePairValue(frontToken.getIntegerValue(master,iX),pairPosition,iX));
	break;
}
///////////////////////////////////////////////////////////////////////
				};
				break;
			default:
				master.handleError(new SymbolOrNonNegativeIntegerIsExpected(frontToken.getPosition()),iX);
				position++;
			}
		}
	}
	//
	abstract protected LabeledTerm parseInestimablePairValue(long pairNameCode, boolean isInQuotes, int pairPosition, ChoisePoint iX) throws SyntaxError;
	abstract protected LabeledTerm parseInestimablePairValue(BigInteger key, int pairPosition, ChoisePoint iX) throws SyntaxError;
	//
	///////////////////////////////////////////////////////////////
	//
	public Term parseRootExpression(ChoisePoint iX) throws SyntaxError {
		setUseSecondVariableNameRegister(true);
		return parseExpression(iX);
	}
	//
	public Term parseExpression(ChoisePoint iX) throws SyntaxError {
		return parseExpression(false,0,iX);
	}
	//
	abstract public Term parseExpression(boolean insertMinus, int minusPosition, ChoisePoint iX) throws SyntaxError;
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isNumericalLiteral(PrologTokenType tokenType) {
		if (	tokenType==PrologTokenType.INTEGER ||
			tokenType==PrologTokenType.REAL) {
			return true;
		} else {
			return false;
		}
	}
	//
	public long parseClassName(ChoisePoint iX) throws SyntaxError, NoClassNameIsFound {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken firstToken= tokens[position];
		PrologTokenType firstTokenType= firstToken.getType();
		if (firstTokenType==PrologTokenType.SYMBOL) {
			position++;
			checkWhetherClassNameIsEnclosedInApostrophes(firstToken,iX);
			return firstToken.getSymbolCode(master,iX);
		} else {
			master.handleError(new ClassNameIsExpected(firstToken.getPosition()),iX);
			throw NoClassNameIsFound.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void checkWhetherSymbolConstantIsEnclosedInApostrophes(PrologToken token, ChoisePoint iX) throws SyntaxError {
		if (robustMode) {
			return;
		};
		if (!token.isInQuotes(master,iX)) {
			master.handleError(new SymbolConstantIsToBeEnclosedInApostrophes(token.getPosition()),iX);
		}
	}
	//
	protected void checkWhetherClassNameIsEnclosedInApostrophes(PrologToken token, ChoisePoint iX) throws SyntaxError {
		if (robustMode) {
			return;
		};
		if (!token.isInQuotes(master,iX)) {
			master.handleError(new ClassNameIsToBeEnclosedInApostrophes(token.getPosition()),iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int parseGivenToken(PrologTokenType givenTokenType, ChoisePoint iX) throws AnotherTokenIsExpected, SyntaxError {
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			PrologTokenType frontTokenType= frontToken.getType();
			if (frontTokenType==givenTokenType) {
				position++;
				return frontToken.getPosition();
			} else {
				throw AnotherTokenIsExpected.instance;
			}
		} else {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		}
	}
	//
	public void parseLeftRoundBracket(ChoisePoint iX) throws SyntaxError {
		try {
			parseGivenToken(PrologTokenType.L_ROUND_BRACKET,iX);
		} catch (AnotherTokenIsExpected e) {
			master.handleError(new LeftRoundBracketIsExpected(tokens[position].getPosition()),iX);
			position++;
		}
	}
	//
	public void parseRightRoundBracket(ChoisePoint iX) throws SyntaxError {
		try {
			parseGivenToken(PrologTokenType.R_ROUND_BRACKET,iX);
		} catch (AnotherTokenIsExpected e) {
			master.handleError(new RightRoundBracketIsExpected(tokens[position].getPosition()),iX);
			position++;
		}
	}
	//
	public void parseLeftSquareBracket(ChoisePoint iX) throws SyntaxError {
		try {
			parseGivenToken(PrologTokenType.L_SQUARE_BRACKET,iX);
		} catch (AnotherTokenIsExpected e) {
			master.handleError(new LeftSquareBracketIsExpected(tokens[position].getPosition()),iX);
			position++;
		}
	}
	//
	public void parseRightSquareBracket(ChoisePoint iX) throws SyntaxError {
		try {
			parseGivenToken(PrologTokenType.R_SQUARE_BRACKET,iX);
		} catch (AnotherTokenIsExpected e) {
			master.handleError(new RightSquareBracketIsExpected(tokens[position].getPosition()),iX);
			position++;
		}
	}
	//
	public void parseLeftBrace(ChoisePoint iX) throws SyntaxError {
		try {
			parseGivenToken(PrologTokenType.L_BRACE,iX);
		} catch (AnotherTokenIsExpected e) {
			master.handleError(new LeftBraceIsExpected(tokens[position].getPosition()),iX);
			position++;
		}
	}
	//
	public void parseRightBrace(ChoisePoint iX) throws SyntaxError {
		try {
			parseGivenToken(PrologTokenType.R_BRACE,iX);
		} catch (AnotherTokenIsExpected e) {
			master.handleError(new RightBraceIsExpected(tokens[position].getPosition()),iX);
			position++;
		}
	}
	//
	public int parseQuestionMark(ChoisePoint iX) throws SyntaxError {
		try {
			return parseGivenToken(PrologTokenType.QUESTION_MARK,iX);
		} catch (AnotherTokenIsExpected e) {
			int tokenPosition= tokens[position].getPosition();
			master.handleError(new QuestionMarkIsExpected(tokenPosition),iX);
			position++;
			return tokenPosition;
		}
	}
	//
	public void parseDot(ChoisePoint iX) throws SyntaxError {
		try {
			parseGivenToken(PrologTokenType.DOT,iX);
		} catch (AnotherTokenIsExpected e) {
			master.handleError(new DotIsExpected(tokens[position].getPosition()),iX);
			position++;
		}
	}
	//
	public void parseRange(ChoisePoint iX) throws SyntaxError {
		try {
			parseGivenToken(PrologTokenType.RANGE,iX);
		} catch (AnotherTokenIsExpected e) {
			master.handleError(new RangeIsExpected(tokens[position].getPosition()),iX);
			position++;
		}
	}
	//
	public void parseColon(ChoisePoint iX) throws SyntaxError {
		try {
			parseGivenToken(PrologTokenType.COLON,iX);
		} catch (AnotherTokenIsExpected e) {
			master.handleError(new ColonIsExpected(tokens[position].getPosition()),iX);
			position++;
		}
	}
	//
	public void parseSemicolon(ChoisePoint iX) throws SyntaxError {
		try {
			parseGivenToken(PrologTokenType.SEMICOLON,iX);
		} catch (AnotherTokenIsExpected e) {
			master.handleError(new SemicolonIsExpected(tokens[position].getPosition()),iX);
			position++;
		}
	}
	//
	public void parseEquation(ChoisePoint iX) throws SyntaxError {
		try {
			parseGivenToken(PrologTokenType.EQ,iX);
		} catch (AnotherTokenIsExpected e) {
			master.handleError(new EquationIsExpected(tokens[position].getPosition()),iX);
			position++;
		}
	}
	//
	public void parseTheExternalKeyword(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken nextToken= tokens[position];
		if (nextToken.getType()==PrologTokenType.KEYWORD) {
			if (!nextToken.isInQuotes(master,iX)) {
				long symbolCode= nextToken.getSymbolCode(master,iX);
				if (symbolCode != SymbolCodes.symbolCode_E_external) {
					master.handleError(new TheExternalKeywordIsExpected(nextToken.getPosition()),iX);
				};
				position++;
			} else {
				master.handleError(new SymbolEnclosedInApostrophesIsNotExpectedHere(nextToken.getPosition()),iX);
				position++;
			}
		} else {
			master.handleError(new TheExternalKeywordIsExpected(nextToken.getPosition()),iX);
			position++;
		}
	}
	//
	public boolean parseTheInterfaceOrMetainterfaceKeyword(ChoisePoint iX) throws SyntaxError {
		boolean isMetainterface= false;
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken nextToken= tokens[position];
		if (nextToken.getType()==PrologTokenType.KEYWORD) {
			if (!nextToken.isInQuotes(master,iX)) {
				long symbolCode= nextToken.getSymbolCode(master,iX);
				if (symbolCode==SymbolCodes.symbolCode_E_metainterface) {
					isMetainterface= true;
				} else if (symbolCode != SymbolCodes.symbolCode_E_interface) {
					master.handleError(new TheInterfaceKeywordIsExpected(nextToken.getPosition()),iX);
				};
				position++;
			} else {
				master.handleError(new SymbolEnclosedInApostrophesIsNotExpectedHere(nextToken.getPosition()),iX);
				position++;
			}
		} else {
			master.handleError(new TheInterfaceKeywordIsExpected(nextToken.getPosition()),iX);
			position++;
		};
		return isMetainterface;
	}
	//
	public void parseTheClassKeyword(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken nextToken= tokens[position];
		if (nextToken.getType()==PrologTokenType.KEYWORD) {
			if (!nextToken.isInQuotes(master,iX)) {
				long symbolCode= nextToken.getSymbolCode(master,iX);
				if (symbolCode != SymbolCodes.symbolCode_E_class) {
					master.handleError(new TheClassKeywordIsExpected(nextToken.getPosition()),iX);
				};
				position++;
			} else {
				master.handleError(new SymbolEnclosedInApostrophesIsNotExpectedHere(nextToken.getPosition()),iX);
				position++;
			}
		} else {
			master.handleError(new TheClassKeywordIsExpected(nextToken.getPosition()),iX);
			position++;
		}
	}
	//
	public void parseTheSpecializedKeyword(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken nextToken= tokens[position];
		if (nextToken.getType()==PrologTokenType.KEYWORD) {
			if (!nextToken.isInQuotes(master,iX)) {
				long symbolCode= nextToken.getSymbolCode(master,iX);
				if (symbolCode != SymbolCodes.symbolCode_E_specialized) {
					master.handleError(new TheSpecializedKeywordIsExpected(nextToken.getPosition()),iX);
				};
				position++;
			} else {
				master.handleError(new SymbolEnclosedInApostrophesIsNotExpectedHere(nextToken.getPosition()),iX);
				position++;
			}
		} else {
			master.handleError(new TheSpecializedKeywordIsExpected(nextToken.getPosition()),iX);
			position++;
		}
	}
	//
	public void parseTheFromKeyword(ChoisePoint iX) throws SyntaxError {
		if (position >= numberOfTokens) {
			throw master.handleUnexpectedEndOfTokenList(tokens,iX);
		};
		PrologToken nextToken= tokens[position];
		if (nextToken.getType()==PrologTokenType.KEYWORD) {
			if (!nextToken.isInQuotes(master,iX)) {
				long symbolCode= nextToken.getSymbolCode(master,iX);
				if (symbolCode != SymbolCodes.symbolCode_E_from) {
					master.handleError(new TheFromKeywordIsExpected(nextToken.getPosition()),iX);
				};
				position++;
			} else {
				master.handleError(new SymbolEnclosedInApostrophesIsNotExpectedHere(nextToken.getPosition()),iX);
				position++;
			}
		} else {
			master.handleError(new TheFromKeywordIsExpected(nextToken.getPosition()),iX);
			position++;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void checkEndOfText(ChoisePoint iX) throws SyntaxError {
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			PrologTokenType frontTokenType= frontToken.getType();
			switch (frontTokenType) {
			case END_OF_TEXT:
			case REST_OF_TEXT:
			case END_OF_LINE:
				break;
			default:
				master.handleError(new EndOfTextIsExpected(frontToken.getPosition()),iX);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isAControlMessage(int p1) {
		int p2= p1 + 1;
		if (p2 >= numberOfTokens) {
			return true;
		};
		PrologToken TokenP2= tokens[p2];
		PrologTokenType TokenTypeP2= TokenP2.getType();
		switch (TokenTypeP2) {
		case INTEGER:
		case REAL:
			int p3= p2 + 1;
			if (p3 >= numberOfTokens) {
				return false;
			};
			PrologToken TokenP3= tokens[p3];
			PrologTokenType TokenTypeP3= TokenP3.getType();
			if (TokenTypeP3==PrologTokenType.L_BRACE) {
				return true;
			};
			return false;
		case L_ROUND_BRACKET:
			return false;
		default:
			return true;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected boolean symbolIsASlot(long symbolNameCode) {
		if (symbolNameCode==SymbolCodes.symbolCode_E_self) {
			return true;
		} else if (slotNameHash != null) {
			return slotNameHash.contains(symbolNameCode);
		} else {
			return false;
		}
	}
	//
	protected boolean symbolIsASlot(long symbolNameCode, boolean isInQuotes) {
		if (isInQuotes) {
			return false;
		} else {
			return symbolIsASlot(symbolNameCode);
		}
	}
	//
	protected void checkWhetherSymbolIsASlot(PrologToken token, ChoisePoint iX) throws SyntaxError {
		if (robustMode) {
			return;
		};
		if (token.isInQuotes(master,iX)) {
			master.handleError(new SlotNameIsExpected(token.getPosition()),iX);
		} else if (!symbolIsASlot(token.getSymbolCode(master,iX))) {
			master.handleError(new UndefinedSlotName(token.getPosition()),iX);
		}
	}
	//
	protected void checkWhetherSymbolIsNotASlot(PrologToken token, ChoisePoint iX) throws SyntaxError {
		if (robustMode) {
			return;
		};
		if (!token.isInQuotes(master,iX) && symbolIsASlot(token.getSymbolCode(master,iX))) {
			master.handleError(new SlotNameIsNotAllowedHere(token.getPosition()),iX);
		}
	}
	//
	protected long parseTargetSlot(PrologToken token, ChoisePoint iX) throws SyntaxError {
		long symbolCode= token.getSymbolCode(master,iX);
		if (!robustMode) {
			if (token.isInQuotes(master,iX)) {
				master.handleError(new SlotNameIsExpected(token.getPosition()),iX);
			} else if (!symbolIsASlot(symbolCode)) {
				master.handleError(new UndefinedSlotName(token.getPosition()),iX);
			}
		};
		return symbolCode;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term attachTermPositionIfNecessary(Term argument, long position) {
		if (rememberTextPositions) {
			return new TermPosition(argument,position);
		} else {
			return argument;
		}
	}
	//
	abstract public Term attachTermPosition(Term argument, long position);
	//
	public void handleError(ParserError exception, ChoisePoint iX) throws ParserError {
		master.handleError(exception,iX);
	}
}
