// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax;

import target.*;

import morozov.syntax.errors.*;
import morozov.syntax.scanner.*;
import morozov.system.*;
import morozov.system.datum.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class Parser {
	protected PrologToken[] tokens;
	protected int numberOfTokens;
	protected int position;
	protected boolean rememberTextPositions;
	public Parser(boolean mode) {
		rememberTextPositions= mode;
	}
	public Parser() {
		rememberTextPositions= false;
	}
	public Term[] stringToTerms(String text) {
		LexicalScanner scanner= new LexicalScanner(false);
		tokens= scanner.analyse(text);
		numberOfTokens= tokens.length;
		if (numberOfTokens > 0) {
			position= 0;
			Term[] terms= parseTerms();
			return terms;
		} else {
			return new Term[0];
		}
	}
	public Term[] parseTerms() {
		ArrayList<Term> terms= new ArrayList<Term>();
		while(true) {
			if (position < numberOfTokens) {
				if (tokens[position].getType()==PrologTokenType.END_OF_TEXT) {
					return terms.toArray(new Term[0]);
				} else {
					terms.add(parseTerm());
					if (position < numberOfTokens) {
						if (tokens[position].getType()==PrologTokenType.SEMICOLON) {
							position++;
						} else if (tokens[position].getType()==PrologTokenType.COMMA) {
							position++;
						} else if (tokens[position].getType()==PrologTokenType.DOT) {
							position++;
						}
					} else {
						throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
					}
				}
			} else {
				throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
			}
		}
	}
	public ArrayList<Term> parseInternalTerms() {
		ArrayList<Term> terms= new ArrayList<Term>();
		boolean commaExpected= false;
		while(true) {
			if (commaExpected) {
				if (position < numberOfTokens) {
					if (tokens[position].getType()==PrologTokenType.COMMA) {
						position++;
					} else {
						return terms;
					}
				} else {
					throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
				}
			} else {
				commaExpected= true;
			};
			terms.add(parseTerm());
		}
	}
	public Term parseTerm() {
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			int beginningOfTerm= frontToken.getPosition();
			PrologTokenType frontTokenType= frontToken.getType();
			if (frontTokenType==PrologTokenType.SYMBOL) {
				if (position + 1 < numberOfTokens && tokens[position+1].getType()==PrologTokenType.L_ROUND_BRACKET) {
					position= position + 2;
					Term[] arguments= parseInternalTerms().toArray(new Term[0]);
					if (position < numberOfTokens) {
						if (tokens[position].getType()==PrologTokenType.R_ROUND_BRACKET) {
							position++;
							Term result= new PrologStructure(frontToken.getSymbolCode(),arguments);
							if (!rememberTextPositions) {
								return result;
							} else {
								return new TermPosition(result,beginningOfTerm);
							}
						} else {
							throw new RightRoundBracketExcpected(tokens[position].getPosition());
						}
					} else {
						throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
					}
				} else if (position + 1 < numberOfTokens && tokens[position+1].getType()==PrologTokenType.L_BRACE) {
					position= position + 2;
					ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
					Term result= new PrologSymbol(frontToken.getSymbolCode());
					if (rememberTextPositions) {
						result= new TermPosition(result,beginningOfTerm);
					};
					arguments.add(new NamedTerm(0,result,beginningOfTerm));
					return parseUnderdeterminedSet(arguments,beginningOfTerm);
				} else if (frontToken.isIncludedIntoApostrophes()) {
					position++;
					Term result= new PrologSymbol(frontToken.getSymbolCode());
					if (!rememberTextPositions) {
						return result;
					} else {
						return new TermPosition(result,beginningOfTerm);
					}
				} else {
					throw new SymbolShouldBeEnclosedInApostrophesHere(frontToken.getPosition());
				}
			} else if (frontTokenType==PrologTokenType.L_SQUARE_BRACKET) {
				position++;
				if (position < numberOfTokens && tokens[position].getType()==PrologTokenType.R_SQUARE_BRACKET) {
					position++;
					// return PrologEmptyList.instance;
					Term result= PrologEmptyList.instance;
					if (!rememberTextPositions) {
						return result;
					} else {
						return new TermPosition(result,beginningOfTerm);
					}
				} else {
					ArrayList<Term> arguments= parseInternalTerms();
					if (position < numberOfTokens) {
						if (tokens[position].getType()==PrologTokenType.R_SQUARE_BRACKET) {
							Term result= PrologEmptyList.instance;
							if (rememberTextPositions) {
								result= new TermPosition(result,tokens[position].getPosition());
							};
							position++;
							// Term result= PrologEmptyList.instance;
							for (int n=arguments.size()-1; n >= 0; n--) {
								// result= new PrologList(arguments.get(n),result);
								Term internalTerm= arguments.get(n);
								result= new PrologList(internalTerm,result);
								if (rememberTextPositions) {
									long internalTermPosition= internalTerm.getPosition();
									result= new TermPosition(result,internalTermPosition);
								}
							};
							return result;
						} else {
							throw new RightSquareBracketExcpected(tokens[position].getPosition());
						}
					} else {
						throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
					}
				}
			} else if (frontTokenType==PrologTokenType.L_BRACE) {
				position++;
				ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
				return parseUnderdeterminedSet(arguments,beginningOfTerm);
			} else if (frontTokenType==PrologTokenType.INTEGER) {
				if (position + 1 < numberOfTokens && tokens[position+1].getType()==PrologTokenType.L_BRACE) {
					position= position + 2;
					ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
					// arguments.add(new NamedTerm(0,new PrologInteger(frontToken.getIntegerValue())));
					Term result= new PrologInteger(frontToken.getIntegerValue());
					if (rememberTextPositions) {
						result= new TermPosition(result,beginningOfTerm);
					};
					arguments.add(new NamedTerm(0,result,beginningOfTerm));
					return parseUnderdeterminedSet(arguments,beginningOfTerm);
				} else {
					position++;
					// return new PrologInteger(frontToken.getIntegerValue());
					Term result= new PrologInteger(frontToken.getIntegerValue());
					if (!rememberTextPositions) {
						return result;
					} else {
						return new TermPosition(result,beginningOfTerm);
					}
				}
			} else if (frontTokenType==PrologTokenType.REAL) {
				if (position + 1 < numberOfTokens && tokens[position+1].getType()==PrologTokenType.L_BRACE) {
					position= position + 2;
					ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
					Term result= new PrologReal(frontToken.getRealValue());
					if (rememberTextPositions) {
						result= new TermPosition(result,beginningOfTerm);
					};
					arguments.add(new NamedTerm(0,result,beginningOfTerm));
					return parseUnderdeterminedSet(arguments,beginningOfTerm);
				} else {
					position++;
					Term result= new PrologReal(frontToken.getRealValue());
					if (!rememberTextPositions) {
						return result;
					} else {
						return new TermPosition(result,beginningOfTerm);
					}
				}
			} else if (frontTokenType==PrologTokenType.STRING) {
				if (position + 1 < numberOfTokens && tokens[position+1].getType()==PrologTokenType.L_BRACE) {
					position= position + 2;
					ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
					// arguments.add(new NamedTerm(0,new PrologString(frontToken.getStringValue())));
					Term result= new PrologString(frontToken.getStringValue());
					if (rememberTextPositions) {
						result= new TermPosition(result,beginningOfTerm);
					};
					arguments.add(new NamedTerm(0,result,beginningOfTerm));
					return parseUnderdeterminedSet(arguments,beginningOfTerm);
				} else {
					position++;
					// return new PrologString(frontToken.getStringValue());
					Term result= new PrologString(frontToken.getStringValue());
					if (!rememberTextPositions) {
						return result;
					} else {
						return new TermPosition(result,beginningOfTerm);
					}
				}
			} else if (frontTokenType==PrologTokenType.NUMBER_SIGN) {
				if (position + 1 < numberOfTokens && tokens[position+1].getType()==PrologTokenType.L_BRACE) {
					position= position + 2;
					ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
					// arguments.add(new NamedTerm(0,PrologUnknownValue.instance));
					Term result= PrologUnknownValue.instance;
					if (rememberTextPositions) {
						result= new TermPosition(result,beginningOfTerm);
					};
					arguments.add(new NamedTerm(0,result,beginningOfTerm));
					return parseUnderdeterminedSet(arguments,beginningOfTerm);
				} else {
					position++;
					// return PrologUnknownValue.instance;
					Term result= PrologUnknownValue.instance;
					if (!rememberTextPositions) {
						return result;
					} else {
						return new TermPosition(result,beginningOfTerm);
					}
				}
			} else if (frontTokenType==PrologTokenType.MINUS) {
				position++;
				if (position < numberOfTokens) {
					PrologToken secondToken= tokens[position];
					if (secondToken.getType()==PrologTokenType.INTEGER) {
						position++;
						// return new PrologInteger(secondToken.getIntegerValue().negate());
						Term result= new PrologInteger(secondToken.getIntegerValue().negate());
						if (!rememberTextPositions) {
							return result;
						} else {
							return new TermPosition(result,beginningOfTerm);
						}
					} else if (secondToken.getType()==PrologTokenType.REAL) {
						position++;
						Term result= new PrologReal(-secondToken.getRealValue());
						if (!rememberTextPositions) {
							return result;
						} else {
							return new TermPosition(result,beginningOfTerm);
						}
					} else {
						throw new UnexpectedToken(secondToken.getPosition());
					}
				} else {
					throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
				}
			} else if (frontTokenType==PrologTokenType.L_ROUND_BRACKET) {
				if (position + 2 < numberOfTokens) {
					int symbolCode;
					PrologToken nextToken= tokens[position+1];
					if (nextToken.getType()==PrologTokenType.SYMBOL) {
						if (nextToken.isIncludedIntoApostrophes()) {
							symbolCode= nextToken.getSymbolCode();
						} else {
							throw new SymbolShouldBeEnclosedInApostrophesHere(nextToken.getPosition());
						}
					} else {
						throw new SymbolExpected(frontToken.getPosition());
					};
					PrologToken closingToken= tokens[position+2];
					if (closingToken.getType()==PrologTokenType.R_ROUND_BRACKET) {
						position= position + 3;
						String symbolText= SymbolNames.retrieveSymbolName(symbolCode).toRawString(null);
						byte[] byteArray= Converters.string2ByteArray(symbolText);
						Term result;
						InputStream inputStream= new ByteArrayInputStream(byteArray);
						try {
							try {
								ObjectInputStream objectInputStream= new DataStoreInputStream(inputStream,false);
								try {
									result= (AbstractWorld)objectInputStream.readObject();
								} catch (ClassNotFoundException e) {
									throw new WorldDeserializingError(nextToken.getPosition(),e);
								} finally {
									objectInputStream.close();
								}
							} finally {
								inputStream.close();
							}
						} catch (IOException e) {
							throw new WorldDeserializingError(nextToken.getPosition(),e);
						}
						if (!rememberTextPositions) {
							return result;
						} else {
							return new TermPosition(result,beginningOfTerm);
						}
					} else {
						throw new RightRoundBracketExcpected(closingToken.getPosition());
					}
				} else {
					throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
				}
			} else {
				throw new UnexpectedToken(frontToken.getPosition());
			}
		} else {
			throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
		}
	}
	public Term parseUnderdeterminedSet(ArrayList<NamedTerm> arguments, int beginningOfTerm) {
		Term result;
		if (position < numberOfTokens && tokens[position].getType()==PrologTokenType.R_BRACE) {
			result= PrologEmptySet.instance;
			if (rememberTextPositions) {
				result= new TermPosition(result,tokens[position].getPosition());
			};
			position++;
			// return PrologEmptySet.instance;
		} else {
			// ArrayList<NamedTerm> arguments= parsePairs();
			parsePairs(arguments);
			if (position < numberOfTokens) {
				if(tokens[position].getType()==PrologTokenType.R_BRACE) {
					result= PrologEmptySet.instance;
					if (rememberTextPositions) {
						result= new TermPosition(result,tokens[position].getPosition());
					};
					position++;
					// return result;
				} else {
					throw new RightBraceExcpected(tokens[position].getPosition());
				}
			} else {
				throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
			}
		};
		// Term result= PrologEmptySet.instance;
		for (int n=arguments.size()-1; n >= 0; n--) {
			NamedTerm pair= arguments.get(n);
			long currentNameCode= pair.nameCode;
			for (int k=0; k < n; k++) {
				if (arguments.get(k).nameCode==currentNameCode) {
					throw new UnderdeterminedSetCannotContainElementsWithEqualNames(pair.getPosition());
				}
			};
			// result= new PrologSet(currentNameCode,pair.value,result);
			result= new PrologSet(currentNameCode,pair.value,result);
			if (rememberTextPositions) {
				long pairPosition= pair.getPosition();
				result= new TermPosition(result,pairPosition);
			}
		};
		return result;
	}
	public ArrayList<NamedTerm> parsePairs(ArrayList<NamedTerm> terms) {
		boolean commaExpected= false;
		while(true) {
			if (commaExpected) {
				if (position < numberOfTokens) {
					if (tokens[position].getType()==PrologTokenType.COMMA) {
						position++;
					} else {
						// return terms.toArray(new Term[0]);
						return terms;
					}
				} else {
					throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
				}
			} else {
				commaExpected= true;
			};
			if (position < numberOfTokens) {
				PrologToken frontToken= tokens[position];
				PrologTokenType frontTokenType= frontToken.getType();
				if (frontTokenType==PrologTokenType.SYMBOL) {
					int pairPosition= frontToken.getPosition();
					position++;
					if (position < numberOfTokens) {
						if (tokens[position].getType()==PrologTokenType.COLON) {
							position++;
							Term pairValue= parseTerm();
							long pairNameCode= - frontToken.getSymbolCode();
							terms.add(new NamedTerm(pairNameCode,pairValue,pairPosition));
						} else {
							throw new ColonExpected(tokens[position].getPosition());
						}
					} else {
						throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
					}
				} else if (frontTokenType==PrologTokenType.INTEGER) {
					int pairPosition= frontToken.getPosition();
					position++;
					if (position < numberOfTokens) {
						if (tokens[position].getType()==PrologTokenType.COLON) {
							position++;
							BigInteger key= frontToken.getIntegerValue();
							if (key.compareTo(BigInteger.ZERO) < 0) {
								throw new NegativeNumbersAreNotAllowedHere(frontToken.getPosition());
							} else {
								long pairNameCode= PrologInteger.toLong(key);
								Term pairValue= parseTerm();
								terms.add(new NamedTerm(pairNameCode,pairValue,pairPosition));
							}
						} else {
							throw new ColonExpected(tokens[position].getPosition());
						}
					} else {
						throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
					}
				} else {
					throw new SymbolOrNonNegativeIntegerExpected(frontToken.getPosition());
				}
			} else {
				throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
			}
		}
	}
}
