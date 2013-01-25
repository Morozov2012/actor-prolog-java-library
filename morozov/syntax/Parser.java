// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax;

import morozov.terms.*;
import morozov.syntax.scanner.*;

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
		// for (int j= 0; j < numberOfTokens; j++) {
		//	System.out.printf("Token #%d: %s; ",j,tokens[j].toString());
		// };
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
			// System.out.printf("Parser:: position=%s; numberOfTokens=%s\n",position,numberOfTokens);
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
		// System.out.printf("%d: parseInternalTerms: %s\n",position,tokens[position]);
		ArrayList<Term> terms= new ArrayList<Term>();
		boolean commaExpected= false;
		while(true) {
			// System.out.printf("%d: parseInternalTerms{1}: %s\n",position,tokens[position]);
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
			// System.out.printf("%d: parseInternalTerms{2}: %s\n",position,tokens[position]);
			terms.add(parseTerm());
		}
	}
	public Term parseTerm() {
		if (position < numberOfTokens) {
			PrologToken frontToken= tokens[position];
			int beginningOfTerm= frontToken.getPosition();
			// System.out.printf("%d: parseTerm: %s\n",position,frontToken);
			if (frontToken.getType()==PrologTokenType.SYMBOL) {
				if (position + 1 < numberOfTokens && tokens[position+1].getType()==PrologTokenType.L_ROUND_BRACKET) {
					position= position + 2;
					Term[] arguments= parseInternalTerms().toArray(new Term[0]);
					if (position < numberOfTokens) {
						if (tokens[position].getType()==PrologTokenType.R_ROUND_BRACKET) {
							// System.out.printf("%d: Get Symbol Code [1]: %s\n",position,frontToken);
							position++;
							// System.out.printf("%d: I will return from ParseTerm{1}: %s\n",position,tokens[position]);
							// return new PrologStructure(frontToken.getSymbolCode(),arguments);
							Term result= new PrologStructure(frontToken.getSymbolCode(),arguments);
							if (!rememberTextPositions) {
								return result;
							} else {
								return new TermPosition(result,beginningOfTerm);
							}
						} else {
							// System.out.printf("%d: RightRoundBracketExcpected: %s\n",position,tokens[position]);
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
					// System.out.printf("%d: Get Symbol Code [2]: %s\n",position,frontToken);
					position++;
					// System.out.printf("%d: I will return from ParseTerm{2}: %s\n",position,tokens[position]);
					// return new PrologSymbol(frontToken.getSymbolCode());
					Term result= new PrologSymbol(frontToken.getSymbolCode());
					if (!rememberTextPositions) {
						return result;
					} else {
						return new TermPosition(result,beginningOfTerm);
					}
				} else {
					// System.out.printf("%d: SymbolShouldBeEnclosedInApostrophesHere: %s\n",position,tokens[position]);
					throw new SymbolShouldBeEnclosedInApostrophesHere(frontToken.getPosition());
				}
			} else if (frontToken.getType()==PrologTokenType.L_SQUARE_BRACKET) {
				position++;
				if (position < numberOfTokens && tokens[position].getType()==PrologTokenType.R_SQUARE_BRACKET) {
					position++;
					// return new PrologEmptyList();
					Term result= new PrologEmptyList();
					if (!rememberTextPositions) {
						return result;
					} else {
						return new TermPosition(result,beginningOfTerm);
					}
				} else {
					ArrayList<Term> arguments= parseInternalTerms();
					if (position < numberOfTokens) {
						if (tokens[position].getType()==PrologTokenType.R_SQUARE_BRACKET) {
							Term result= new PrologEmptyList();
							if (rememberTextPositions) {
								result= new TermPosition(result,tokens[position].getPosition());
							};
							position++;
							// Term result= new PrologEmptyList();
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
							// System.out.printf("%d: RightRoundBracketExcpected: %s\n",position,tokens[position]);
							throw new RightSquareBracketExcpected(tokens[position].getPosition());
						}
					} else {
						throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
					}
				}
			} else if (frontToken.getType()==PrologTokenType.L_BRACE) {
				position++;
				ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
				return parseUnderdeterminedSet(arguments,beginningOfTerm);
			} else if (frontToken.getType()==PrologTokenType.INTEGER) {
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
			} else if (frontToken.getType()==PrologTokenType.REAL) {
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
			} else if (frontToken.getType()==PrologTokenType.STRING) {
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
			} else if (frontToken.getType()==PrologTokenType.NUMBER_SIGN) {
				if (position + 1 < numberOfTokens && tokens[position+1].getType()==PrologTokenType.L_BRACE) {
					position= position + 2;
					ArrayList<NamedTerm> arguments= new ArrayList<NamedTerm>();
					// arguments.add(new NamedTerm(0,new PrologUnknownValue()));
					Term result= new PrologUnknownValue();
					if (rememberTextPositions) {
						result= new TermPosition(result,beginningOfTerm);
					};
					arguments.add(new NamedTerm(0,result,beginningOfTerm));
					return parseUnderdeterminedSet(arguments,beginningOfTerm);
				} else {
					position++;
					// return new PrologUnknownValue();
					Term result= new PrologUnknownValue();
					if (!rememberTextPositions) {
						return result;
					} else {
						return new TermPosition(result,beginningOfTerm);
					}
				}
			} else if (frontToken.getType()==PrologTokenType.MINUS) {
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
						// System.out.printf("%d: UnexpectedToken: %s\n",position,secondToken);
						throw new UnexpectedToken(secondToken.getPosition());
					}
				} else {
					throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
				}
			// } else if (frontToken.getType()==PrologTokenType.VARIABLE) {
			//	position++;
			//	// System.out.printf("%d: I will return from ParseTerm{4}: %s\n",position,tokens[position]);
			//	return new PrologVariable(); // frontToken.getVariableName();
			} else {
				// System.out.printf("%d: UnexpectedToken: %s\n",position,frontToken);
				throw new UnexpectedToken(frontToken.getPosition());
			}
		} else {
			throw new UnexpectedEndOfTokenList(tokens[numberOfTokens-1].getPosition());
		}
	}
	public Term parseUnderdeterminedSet(ArrayList<NamedTerm> arguments, int beginningOfTerm) {
		Term result;
		if (position < numberOfTokens && tokens[position].getType()==PrologTokenType.R_BRACE) {
			result= new PrologEmptySet();
			if (rememberTextPositions) {
				result= new TermPosition(result,tokens[position].getPosition());
			};
			position++;
			// return new PrologEmptySet();
		} else {
			// ArrayList<NamedTerm> arguments= parsePairs();
			parsePairs(arguments);
			if (position < numberOfTokens) {
				if(tokens[position].getType()==PrologTokenType.R_BRACE) {
					result= new PrologEmptySet();
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
		// Term result= new PrologEmptySet();
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
		// System.out.printf("%d: parsePairs: %s\n",position,tokens[position]);
		// ArrayList<NamedTerm> terms= new ArrayList<NamedTerm>();
		boolean commaExpected= false;
		while(true) {
			// System.out.printf("%d: parsePairs{1}: %s\n",position,tokens[position]);
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
			// System.out.printf("%d: parsePairs{2}: %s\n",position,tokens[position]);
			if (position < numberOfTokens) {
				PrologToken frontToken= tokens[position];
				if (frontToken.getType()==PrologTokenType.SYMBOL) {
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
				} else if (frontToken.getType()==PrologTokenType.INTEGER) {
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
								// if (DefaultOptions.integerOverflowCheck) {
								//	if (key.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
								//		throw new IntegerValueIsTooBig();
								//	} else {
								//		pairNameCode= key.longValue();
								//	}
								// } else {
								//	pairNameCode= key.longValue();
								// };
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
