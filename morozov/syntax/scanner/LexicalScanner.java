// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.signals.*;
import morozov.terms.*;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.ArrayList;

public class LexicalScanner {
	protected boolean robustMode= false;
	protected int position;
	protected int braceLevel;
	protected boolean afterComment;
	protected int tokenPosition;
	protected int previousPosition;
	protected boolean previousCharacterIsSupplementary;
	public LexicalScanner() {
		robustMode= true;
	}
	public LexicalScanner(boolean rM) {
		robustMode= rM;
	}
	public PrologToken[] analyse(String text) {
		return analyse(text,false);
	}
	public PrologToken[] analyse(String text, boolean extractFrontTokenOnly) {
		position= 0;
		boolean processSupplementaryCharacter= false;
		braceLevel= 0;
		afterComment= true;
		char[] characters= text.toCharArray();
		// System.out.printf(">>>%s<<<\n",text);
		// for (int nn=0; nn < characters.length; nn++) {
		//	System.out.printf("%d) %4x (%d) %s\n",nn,(int)characters[nn],(int)characters[nn],characters[nn]);
		// }
		int textLength= characters.length;
		ArrayList<PrologToken> tokens= new ArrayList<PrologToken>();
		char c1;
		char c2;
		int code;
		while(true) { // Loop: Extract tokens
			if (position >= textLength) {
				if (processSupplementaryCharacter) {
					position= position - 2;
				} else {
					position--;
				};
				break;
			} else {
				// c= characters[position];
				c1= characters[position];
				if (	(position + 1 <= textLength - 1) &&
					Character.isSurrogatePair(c1,characters[position+1])) {
					c2= characters[position+1];
					code= Character.toCodePoint(c1,c2);
					processSupplementaryCharacter= true;
				} else {
					c2= 0;
					code= c1;
					processSupplementaryCharacter= false;
				}
			};
			try {
				previousPosition= position;
				previousCharacterIsSupplementary= processSupplementaryCharacter;
///////////////////////////////////////////////////////////////////////
// Recognize Token (Beginning)                                       //
///////////////////////////////////////////////////////////////////////
			// is_special_code_letter('b',"\8"). +
			// is_special_code_letter('t',"\9"). +
			// is_special_code_letter('n',"\10"). +
			// is_special_code_letter('v',"\11"). *
			// is_special_code_letter('f',"\12"). +
			// is_special_code_letter('r',"\13"). +
			// if (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f' || c == '\b' || c == 11) {
			// if (c == ' ' || ( c >= 8 && c <= 13)) {
			if (Character.isWhitespace(code) || Character.isSpaceChar(code) || Character.isISOControl(code)) {
				// Skip white space.
			} else if (code == '`') {
				tokenPosition= position;
				afterComment= false;
				// position++;
				if (processSupplementaryCharacter) {
					position= position + 2;
				} else {
					position++;
				};
				if (position >= textLength) {
					if (robustMode) {
						throw TokenIsNotRecognized.instance;
					} else {
						throw new UnexpectedEndOfText(tokenPosition);
					}
				} else {
					// c= characters[position];
					c1= characters[position];
					if (	(position + 1 <= textLength - 1) &&
						Character.isSurrogatePair(c1,characters[position+1])) {
						c2= characters[position+1];
						code= Character.toCodePoint(c1,c2);
						processSupplementaryCharacter= true;
					} else {
						c2= 0;
						code= c1;
						processSupplementaryCharacter= false;
					}
				};
				// if (c == ' ' || ( c >= 8 && c <= 13)) {
				if (Character.isWhitespace(code) || Character.isSpaceChar(code) || Character.isISOControl(code)) {
					if (robustMode) {
						throw TokenIsNotRecognized.instance;
					} else {
						throw new SpaceAndControlCharactersAreNotAllowedHere(position);
					}
				} else {
					PrologToken numericalToken= new TokenInteger(BigInteger.valueOf(code),tokenPosition);
					tokens.add(numericalToken);
				}
			// } else if (c >= '0' && c <= '9') {
			} else if (Character.isDigit(code)) {
				tokenPosition= position;
				afterComment= false;
				int beginningOfNumber= position;
				boolean lastCharacterIsSharp= false;
				boolean numberContainsExtraSymbols= false;
				StringBuilder buffer= new StringBuilder();
				int radix= 10;
				// int scale= 0;
				BigInteger scale= BigInteger.ZERO;
				boolean afterDigit= false;
				boolean afterDot= false;
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: NUMERICAL LITERAL (BEGINNING)                                :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
while (true) { // Loop: Scan significand of the numerical token
	// if (c >= '0' && c <= '9') {
	if (Character.isDigit(code)) {
		if (numberContainsExtraSymbols) {
			if (processSupplementaryCharacter) {
				buffer.append(c1);
				buffer.append(c2);
			} else {
				buffer.append(c1);
			}
		};
		if (afterDot) {
			// scale++;
			scale= scale.add(BigInteger.ONE);
		};
		afterDigit= true;
	} else if (code == '_') {
		if (afterDigit) {
			if (!numberContainsExtraSymbols) {
				if (beginningOfNumber < position) {
					int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber; // + 1;
					String initialContent= new String(characters,beginningOfNumber,numberLength);
					buffer.append(initialContent);
				};
				numberContainsExtraSymbols= true;
			};
			afterDigit= false;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new UnderscoreCharacterIsNotAllowedHere(position);
			}
		}
	} else if (afterDigit && !afterDot) {
		if (code == '#') {
			// position++;
			if (processSupplementaryCharacter) {
				position= position + 2;
			} else {
				position++;
			};
			if (position >= textLength) {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					throw new UnexpectedEndOfText(tokenPosition);
				}
			} else {
				// c= characters[position];
				c1= characters[position];
				if (	(position + 1 <= textLength - 1) &&
					Character.isSurrogatePair(c1,characters[position+1])) {
					c2= characters[position+1];
					code= Character.toCodePoint(c1,c2);
					processSupplementaryCharacter= true;
				} else {
					c2= 0;
					code= c1;
					processSupplementaryCharacter= false;
				}
			};
// ====================================================================
// === Extended Number                                              ===
// ====================================================================
BigInteger numericalValue;
if (numberContainsExtraSymbols) {
	try {
		numericalValue= new BigInteger(buffer.toString(),radix);
	} catch (NumberFormatException e) {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new BigIntegerFormatException(tokenPosition);
		}
	}
} else {
	int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber - 1; // + 1;
	String stringRepresentation= new String(characters,beginningOfNumber,numberLength);
	try {
		numericalValue= new BigInteger(stringRepresentation,radix);
	} catch (NumberFormatException e) {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new BigIntegerFormatException(tokenPosition);
		}
	}
};
if (numericalValue.compareTo(BigInteger.valueOf(Character.MAX_RADIX)) > 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		throw new IntegerRadixIsTooBig(tokenPosition);
	}
} else if (numericalValue.compareTo(BigInteger.valueOf(Character.MIN_RADIX)) < 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		throw new IntegerRadixIsTooSmall(tokenPosition);
	}
} else {
	radix= numericalValue.intValue();
};
beginningOfNumber= position;
numberContainsExtraSymbols= false;
buffer= new StringBuilder();
// scale= 0;
scale= BigInteger.ZERO;
afterDigit= false;
afterDot= false;
while (true) { // Loop: Scan extended number
	// if (c >= '0' && c <= '9') {
	if (Character.isDigit(code)) {
		if (numberContainsExtraSymbols) {
			// buffer.append((char)c);
			if (processSupplementaryCharacter) {
				buffer.append(c1);
				buffer.append(c2);
			} else {
				buffer.append(c1);
			}
		};
		if (afterDot) {
			// scale++;
			scale= scale.add(BigInteger.ONE);
		};
		afterDigit= true;
	} else if (code >= 'a' && code <= 'z') {
		if (numberContainsExtraSymbols) {
			// buffer.append((char)c);
			if (processSupplementaryCharacter) {
				buffer.append(c1);
				buffer.append(c2);
			} else {
				buffer.append(c1);
			}
		};
		if (afterDot) {
			// scale++;
			scale= scale.add(BigInteger.ONE);
		};
		afterDigit= true;
	} else if (code >= 'A' && code <= 'Z') {
		if (numberContainsExtraSymbols) {
			// buffer.append((char)c);
			if (processSupplementaryCharacter) {
				buffer.append(c1);
				buffer.append(c2);
			} else {
				buffer.append(c1);
			}
		};
		if (afterDot) {
			// scale++;
			scale= scale.add(BigInteger.ONE);
		};
		afterDigit= true;
	} else if (code == '.') {
		if (afterDigit && !afterDot) {
			if (!numberContainsExtraSymbols) {
				if (beginningOfNumber < position) {
					int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber; // + 1;
					String initialContent= new String(characters,beginningOfNumber,numberLength);
					buffer.append(initialContent);
				};
				numberContainsExtraSymbols= true;
			};
			afterDigit= false;
			afterDot= true;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new DotIsNotAllowedHere(position);
			}
		}
	} else if (code == '_') {
		if (afterDigit) {
			if (!numberContainsExtraSymbols) {
				if (beginningOfNumber < position) {
					int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber; // + 1;
					String initialContent= new String(characters,beginningOfNumber,numberLength);
					buffer.append(initialContent);
				};
				numberContainsExtraSymbols= true;
			};
			afterDigit= false;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new UnderscoreCharacterIsNotAllowedHere(position);
			}
		}
	} else if (code == '#') {
		if (afterDigit) {
			lastCharacterIsSharp= true;
			break;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new NumberSignIsNotAllowedHere(position);
			}
		}
	} else {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new ExtendedDigitExpected(position);
		}
	};
	// position++;
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position >= textLength) {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new UnexpectedEndOfText(tokenPosition);
		}
	} else {
		// c= characters[position];
		c1= characters[position];
		if (	(position + 1 <= textLength - 1) &&
			Character.isSurrogatePair(c1,characters[position+1])) {
			c2= characters[position+1];
			code= Character.toCodePoint(c1,c2);
			processSupplementaryCharacter= true;
		} else {
			c2= 0;
			code= c1;
			processSupplementaryCharacter= false;
		}
	}
}; // End of loop: Scan extended number
// ====================================================================
			// System.out.printf("%d: End of loop: Scan extended number: %c\nafterDigit=%s,afterDot=%s\n",position,(char)c,afterDigit,afterDot);
			if (afterDigit) {
				break;
			} else {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					throw new ExtendedDigitExpected(position);
				}
			}
		} else if (code == '.') {
			if (!numberContainsExtraSymbols) {
				if (beginningOfNumber < position) {
					int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber; // + 1;
					String initialContent= new String(characters,beginningOfNumber,numberLength);
					buffer.append(initialContent);
				};
				numberContainsExtraSymbols= true;
			};
			afterDigit= false;
			afterDot= true;
		} else {
			// position--;
			if (processSupplementaryCharacter) {
				position= position - 2;
			} else {
				position--;
			};
			break;
		}
	} else if (afterDigit) {
		// position--;
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
		break;
	} else {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new ExtendedDigitExpected(position);
		}
	};
	// position++;
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position >= textLength) {
		if (afterDigit) {
			break;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new UnexpectedEndOfText(tokenPosition);
			}
		}
	} else {
		// c= characters[position];
		c1= characters[position];
		if (	(position + 1 <= textLength - 1) &&
			Character.isSurrogatePair(c1,characters[position+1])) {
			c2= characters[position+1];
			code= Character.toCodePoint(c1,c2);
			processSupplementaryCharacter= true;
		} else {
			c2= 0;
			code= c1;
			processSupplementaryCharacter= false;
		}
	}
}; // End of loop: Scan significand of the numerical token
String stringRepresentation;
if (numberContainsExtraSymbols) {
	stringRepresentation= buffer.toString();
} else {
	int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber + 1;
	if (lastCharacterIsSharp) {
		// numberLength--;
		if (processSupplementaryCharacter) {
			numberLength= numberLength - 2;
		} else {
			numberLength--;
		}
	};
	stringRepresentation= new String(characters,beginningOfNumber,numberLength);
};
// position++;
if (processSupplementaryCharacter) {
	position= position + 2;
} else {
	position++;
};
if (position < textLength) {
	// c= characters[position];
	c1= characters[position];
	if (	(position + 1 <= textLength - 1) &&
		Character.isSurrogatePair(c1,characters[position+1])) {
		c2= characters[position+1];
		code= Character.toCodePoint(c1,c2);
		processSupplementaryCharacter= true;
	} else {
		c2= 0;
		code= c1;
		processSupplementaryCharacter= false;
	};
	if (code == 'E' || code == 'e') { // Block: Scan exponent
		// position++;
		if (processSupplementaryCharacter) {
			position= position + 2;
		} else {
			position++;
		};
		if (position >= textLength) {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new UnexpectedEndOfText(tokenPosition);
			}
		} else {
			// c= characters[position];
			c1= characters[position];
			if (	(position + 1 <= textLength - 1) &&
				Character.isSurrogatePair(c1,characters[position+1])) {
				c2= characters[position+1];
				code= Character.toCodePoint(c1,c2);
				processSupplementaryCharacter= true;
			} else {
				c2= 0;
				code= c1;
				processSupplementaryCharacter= false;
			};
			beginningOfNumber= position;
			numberContainsExtraSymbols= false;
			buffer= new StringBuilder();
			afterDigit= false;
			if (code == '-') {
				// Accept sign
			} else if (code == '+') {
				// Skip sign
				// beginningOfNumber++;
				if (processSupplementaryCharacter) {
					beginningOfNumber= beginningOfNumber + 2;
				} else {
					beginningOfNumber++;
				}
			} else {
				// position--;
				if (processSupplementaryCharacter) {
					position= position - 2;
				} else {
					position--;
				}
			};
			// position++;
			if (processSupplementaryCharacter) {
				position= position + 2;
			} else {
				position++;
			};
			if (position >= textLength) {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					throw new UnexpectedEndOfText(tokenPosition);
				}
			} else {
				// c= characters[position];
				c1= characters[position];
				if (	(position + 1 <= textLength - 1) &&
					Character.isSurrogatePair(c1,characters[position+1])) {
					c2= characters[position+1];
					code= Character.toCodePoint(c1,c2);
					processSupplementaryCharacter= true;
				} else {
					c2= 0;
					code= c1;
					processSupplementaryCharacter= false;
				}
			};
			// if (c >= '0' && c <= '9') {
			if (Character.isDigit(code)) {
// ====================================================================
// === Exponent                                                     ===
// ====================================================================
afterDigit= false;
while (true) { // Loop: Scan exponent
	// if (c >= '0' && c <= '9') {
	if (Character.isDigit(code)) {
		if (numberContainsExtraSymbols) {
			// buffer.append((char)c);
			if (processSupplementaryCharacter) {
				buffer.append(c1);
				buffer.append(c2);
			} else {
				buffer.append(c1);
			}
		};
		afterDigit= true;
	} else if (code == '_') {
		if (afterDigit) {
			if (!numberContainsExtraSymbols) {
				if (beginningOfNumber < position) {
					int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber; // + 1;
					String initialContent= new String(characters,beginningOfNumber,numberLength);
					buffer.append(initialContent);
				};
				numberContainsExtraSymbols= true;
			};
			afterDigit= false;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new UnderscoreCharacterIsNotAllowedHere(position);
			}
		}
	} else {
		if (afterDigit) {
			// position--;
			if (processSupplementaryCharacter) {
				position= position - 2;
			} else {
				position--;
			};
			break;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new DigitExpected(position);
			}
		}
	};
	// position++;
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	}
	if (position >= textLength) {
		if (afterDigit) {
			break;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new UnexpectedEndOfText(tokenPosition);
			}
		}
	} else {
		// c= characters[position];
		c1= characters[position];
		if (	(position + 1 <= textLength - 1) &&
			Character.isSurrogatePair(c1,characters[position+1])) {
			c2= characters[position+1];
			code= Character.toCodePoint(c1,c2);
			processSupplementaryCharacter= true;
		} else {
			c2= 0;
			code= c1;
			processSupplementaryCharacter= false;
		}
	}
} // End of loop: Scan exponent
// ====================================================================
			} else {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					throw new DigitExpected(position);
				}
			};
			String stringExponent;
			if (numberContainsExtraSymbols) {
				stringExponent= buffer.toString();
			} else {
				int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber + 1;
				stringExponent= new String(characters,beginningOfNumber,numberLength);
			};
			BigInteger exponent;
			try {
				exponent= new BigInteger(stringExponent);
			} catch (NumberFormatException e) {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					throw new BigIntegerFormatException(tokenPosition);
				}
			};
			// exponent= BigInteger.valueOf(scale).subtract(exponent);
			exponent= scale.subtract(exponent);
			// if (exponent.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
			//	if (robustMode) {
			//		throw TokenIsNotRecognized.instance;
			//	} else {
			//		throw new IntegerExponentIsTooBig(tokenPosition);
			//	}
			// } else if (exponent.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
			//	if (robustMode) {
			//		throw TokenIsNotRecognized.instance;
			//	} else {
			//		throw new IntegerExponentIsTooSmall(tokenPosition);
			//	}
			// } else {
				// scale= exponent.intValue();
				scale= exponent;
			// }
		}
	} else { // End of block: Scan exponent
		// position--;
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
	}
};
PrologToken numericalToken;
if (afterDot) {
	BigInteger integerRepresentation;
	try {
		integerRepresentation= new BigInteger(stringRepresentation,radix);
	} catch (NumberFormatException e) {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new BigIntegerFormatException(tokenPosition);
		}
	};
	if (radix==10 && PrologInteger.isSmallInteger(scale)) {
		BigDecimal decimalRepresentation;
		try {
			decimalRepresentation= new BigDecimal(integerRepresentation,scale.intValue());
		} catch (NumberFormatException e) {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new BigDecimalFormatException(tokenPosition);
			}
		};
		numericalToken= new TokenReal(decimalRepresentation.doubleValue(),tokenPosition);
	} else {
		double doubleValue= integerRepresentation.doubleValue();
		// doubleValue= doubleValue * StrictMath.pow(radix,-scale);
		doubleValue= doubleValue * StrictMath.pow(radix,scale.negate().doubleValue());
		numericalToken= new TokenReal(doubleValue,tokenPosition);
	}
} else {
	if ((scale.compareTo(BigInteger.ZERO) < 0)) {
		BigInteger integerRepresentation;
		try {
			integerRepresentation= new BigInteger(stringRepresentation,radix);
		} catch (NumberFormatException e) {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new BigIntegerFormatException(tokenPosition);
			}
		};
		if (radix==10 && PrologInteger.isSmallInteger(scale)) {
		// if (false) { // Debug 2011.04.17
			BigDecimal decimalRepresentation;
			try {
				decimalRepresentation= new BigDecimal(integerRepresentation,scale.intValue());
			} catch (NumberFormatException e) {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					throw new BigDecimalFormatException(tokenPosition);
				}
			};
			numericalToken= new TokenInteger(decimalRepresentation.toBigInteger(),tokenPosition);
		} else {
			BigInteger bigExponent;
			BigInteger bigRadix= BigInteger.valueOf(radix);
			BigInteger positiveScale= scale.negate();
			if (PrologInteger.isSmallInteger(positiveScale)) {
			// if (false) { // Debug 2011.04.17
				bigExponent= bigRadix.pow(positiveScale.intValue());
			} else {
				bigExponent= BigInteger.ONE;
				int increment= Integer.MAX_VALUE;
				// int increment= 3; // Debug 2011.04.17
				BigInteger bigMaxPoveredRadix= bigRadix.pow(increment);
				BigInteger bigMaxInteger= BigInteger.valueOf(increment);
				while(positiveScale.compareTo(bigMaxInteger) >= 0) {
					// System.out.printf("(i)bigExponent=%s; positiveScale=%s\n",bigExponent,positiveScale);
					bigExponent= bigExponent.multiply(bigMaxPoveredRadix);
					positiveScale= positiveScale.subtract(bigMaxInteger);
				};
				// System.out.printf("(*)bigExponent=%s; positiveScale=%s\n",bigExponent,positiveScale);
				bigExponent= bigExponent.multiply(bigRadix.pow(positiveScale.intValue()));
			};
			integerRepresentation= integerRepresentation.multiply(bigExponent);
			numericalToken= new TokenInteger(integerRepresentation,tokenPosition);
		}
	// } else if (scale > 0) {
	} else if (scale.compareTo(BigInteger.ZERO) > 0) {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new IntegerCannotHaveNegativeExponent(tokenPosition);
		}
	} else { // (scale == 0)
		try {
			BigInteger bigInteger= new BigInteger(stringRepresentation,radix);
			numericalToken= new TokenInteger(bigInteger,tokenPosition);
		} catch (NumberFormatException e) {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new BigIntegerFormatException(tokenPosition);
			}
		}
	}
};
tokens.add(numericalToken);
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: NUMERICAL LITERAL (END)                                      :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			} else if (code == '"' || code == '\'') {
				tokenPosition= position;
				afterComment= false;
				boolean isSymbol;
				if (code == '\'') {
					isSymbol= true;
				} else {
					isSymbol= false;
				};
				// position++;
				if (processSupplementaryCharacter) {
					position= position + 2;
				} else {
					position++;
				};
				if (position >= textLength) {
					if (robustMode) {
						throw TokenIsNotRecognized.instance;
					} else {
						throw new UnexpectedEndOfText(tokenPosition);
					}
				} else {
					// c= characters[position];
					c1= characters[position];
					if (	(position + 1 <= textLength - 1) &&
						Character.isSurrogatePair(c1,characters[position+1])) {
						c2= characters[position+1];
						code= Character.toCodePoint(c1,c2);
						processSupplementaryCharacter= true;
					} else {
						c2= 0;
						code= c1;
						processSupplementaryCharacter= false;
					}
				};
				int beginningOfStringSegment= position;
				boolean segmentContainsExtraSymbols= false;
				StringBuilder buffer= new StringBuilder();
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: STRING SEGMENT / SYMBOLIC LITERAL (BEGINNING)                :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
while(true) { // Loop: Scan string segment token
	if (code == '"' && !isSymbol) {
		break;
	} else if (code == '\'' && isSymbol) {
		break;
	} else if (code == '\\') {
		if (!segmentContainsExtraSymbols) {
			if (beginningOfStringSegment < position) {
				int segmentLength= StrictMath.min(position,textLength-1) - beginningOfStringSegment; // + 1;
				String initialContent= new String(characters,beginningOfStringSegment,segmentLength);
				buffer.append(initialContent);
			};
			segmentContainsExtraSymbols= true;
		};
		// position++;
		if (processSupplementaryCharacter) {
			position= position + 2;
		} else {
			position++;
		};
		if (position >= textLength) {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new StringOrSymbolicLiteralIsNotTerminated(tokenPosition);
			}
		} else {
			// c= characters[position];
			c1= characters[position];
			if (	(position + 1 <= textLength - 1) &&
				Character.isSurrogatePair(c1,characters[position+1])) {
				c2= characters[position+1];
				code= Character.toCodePoint(c1,c2);
				processSupplementaryCharacter= true;
			} else {
				c2= 0;
				code= c1;
				processSupplementaryCharacter= false;
			};
			if (code == 'b') {
				buffer.append((char)'\b');
			} else if (code == 't') {
				buffer.append((char)'\t');
			} else if (code == 'n') {
				buffer.append((char)'\n');
			} else if (code == 'v') {
				buffer.append((char)11);
			} else if (code == 'f') {
				buffer.append((char)'\f');
			} else if (code == 'r') {
				buffer.append((char)'\r');
			// } else if (c >= '0' && c <= '9') {
			} else if (Character.isDigit(code)) {
				// tokenPosition= position;
				int beginningOfNumber= position;
				boolean lastCharacterIsSharp= false;
				boolean numberContainsExtraSymbols= false;
				StringBuilder auxiliaryBuffer= new StringBuilder();
				int radix= 10;
				// int scale= 0;
				boolean afterDigit= false;
				// boolean afterDot= false;
				// System.out.printf("%d: %c >= '0' && %c <= '9'\n",position,(char)c,(char)c);
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: NUMERICAL LITERAL INSIDE STRING / SYMBOL (BEGINNING)         :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
while (true) { // Loop: Scan significand of the numerical token
	// if (c >= '0' && c <= '9') {
	if (Character.isDigit(code)) {
		if (numberContainsExtraSymbols) {
			// auxiliaryBuffer.append((char)c);
			if (processSupplementaryCharacter) {
				auxiliaryBuffer.append(c1);
				auxiliaryBuffer.append(c2);
			} else {
				auxiliaryBuffer.append(c1);
			}
		};
		// if (afterDot) {
		//	scale++;
		// };
		afterDigit= true;
	} else if (code == '_') {
		if (afterDigit) {
			if (!numberContainsExtraSymbols) {
				if (beginningOfNumber < position) {
					int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber; // + 1;
					String initialContent= new String(characters,beginningOfNumber,numberLength);
					auxiliaryBuffer.append(initialContent);
				};
				numberContainsExtraSymbols= true;
			};
			afterDigit= false;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new UnderscoreCharacterIsNotAllowedHere(position);
			}
		}
	} else if (afterDigit) {
		if (code == '#') {
			// position++;
			if (processSupplementaryCharacter) {
				position= position + 2;
			} else {
				position++;
			};
			if (position >= textLength) {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					throw new UnexpectedEndOfText(tokenPosition);
				}
			} else {
				// c= characters[position];
				c1= characters[position];
				if (	(position + 1 <= textLength - 1) &&
					Character.isSurrogatePair(c1,characters[position+1])) {
					c2= characters[position+1];
					code= Character.toCodePoint(c1,c2);
					processSupplementaryCharacter= true;
				} else {
					c2= 0;
					code= c1;
					processSupplementaryCharacter= false;
				}
			};
// ====================================================================
// === Extended Number Inside String Segment / Symbolic Literal     ===
// ====================================================================
BigInteger numericalValue;
if (numberContainsExtraSymbols) {
	try {
		numericalValue= new BigInteger(auxiliaryBuffer.toString(),radix);
	} catch (NumberFormatException e) {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new BigIntegerFormatException(tokenPosition);
		}
	}
} else {
	int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber - 1; // + 1;
	String stringRepresentation= new String(characters,beginningOfNumber,numberLength);
	try {
		numericalValue= new BigInteger(stringRepresentation,radix);
	} catch (NumberFormatException e) {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new BigIntegerFormatException(tokenPosition);
		}
	}
};
if (numericalValue.compareTo(BigInteger.valueOf(Character.MAX_RADIX)) > 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		throw new IntegerRadixIsTooBig(beginningOfNumber);
	}
} else if (numericalValue.compareTo(BigInteger.valueOf(Character.MIN_RADIX)) < 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		throw new IntegerRadixIsTooSmall(beginningOfNumber);
	}
} else {
	radix= numericalValue.intValue();
};
beginningOfNumber= position;
numberContainsExtraSymbols= false;
auxiliaryBuffer= new StringBuilder();
// scale= 0;
afterDigit= false;
// afterDot= false;
while (true) { // Loop: Scan extended number
	// if (c >= '0' && c <= '9') {
	if (Character.isDigit(code)) {
		if (numberContainsExtraSymbols) {
			// auxiliaryBuffer.append((char)c);
			if (processSupplementaryCharacter) {
				auxiliaryBuffer.append(c1);
				auxiliaryBuffer.append(c2);
			} else {
				auxiliaryBuffer.append(c1);
			}
		};
		// if (afterDot) {
		//	scale++;
		// };
		afterDigit= true;
	} else if (code >= 'a' && code <= 'z') {
		if (numberContainsExtraSymbols) {
			// auxiliaryBuffer.append((char)c);
			if (processSupplementaryCharacter) {
				auxiliaryBuffer.append(c1);
				auxiliaryBuffer.append(c2);
			} else {
				auxiliaryBuffer.append(c1);
			}
		};
		// if (afterDot) {
		//	scale++;
		// };
		afterDigit= true;
	} else if (code >= 'A' && code <= 'Z') {
		if (numberContainsExtraSymbols) {
			// auxiliaryBuffer.append((char)c);
			if (processSupplementaryCharacter) {
				auxiliaryBuffer.append(c1);
				auxiliaryBuffer.append(c2);
			} else {
				auxiliaryBuffer.append(c1);
			}
		};
		// if (afterDot) {
		//	scale++;
		// };
		afterDigit= true;
	} else if (code == '.') {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new DotIsNotAllowedHere(position);
		}
	} else if (code == '_') {
		if (afterDigit) {
			if (!numberContainsExtraSymbols) {
				if (beginningOfNumber < position) {
					int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber; // + 1;
					String initialContent= new String(characters,beginningOfNumber,numberLength);
					auxiliaryBuffer.append(initialContent);
				};
				numberContainsExtraSymbols= true;
			};
			afterDigit= false;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new UnderscoreCharacterIsNotAllowedHere(position);
			}
		}
	} else if (code == '#') {
		if (afterDigit) {
			lastCharacterIsSharp= true;
			break;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new NumberSignIsNotAllowedHere(position);
			}
		}
	} else {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new ExtendedDigitExpected(position);
		}
	};
	// position++;
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position >= textLength) {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new UnexpectedEndOfText(tokenPosition);
		}
	} else {
		// c= characters[position];
		c1= characters[position];
		if (	(position + 1 <= textLength - 1) &&
			Character.isSurrogatePair(c1,characters[position+1])) {
			c2= characters[position+1];
			code= Character.toCodePoint(c1,c2);
			processSupplementaryCharacter= true;
		} else {
			c2= 0;
			code= c1;
			processSupplementaryCharacter= false;
		}
	}
}; // End of loop: Scan extended number
// ====================================================================
			if (afterDigit) {
				break;
			} else {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					throw new ExtendedDigitExpected(position);
				}
			}
		} else if (code == '.') {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new DotIsNotAllowedHere(position);
			}
		} else {
			// position--;
			if (processSupplementaryCharacter) {
				position= position - 2;
			} else {
				position--;
			};
			break;
		}
	} else if (afterDigit) {
		// position--;
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
		break;
	} else {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new ExtendedDigitExpected(position);
		}
	};
	// position++;
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position >= textLength) {
		if (afterDigit) {
			break;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				throw new UnexpectedEndOfText(tokenPosition);
			}
		}
	} else {
		// c= characters[position];
		c1= characters[position];
		if (	(position + 1 <= textLength - 1) &&
			Character.isSurrogatePair(c1,characters[position+1])) {
			c2= characters[position+1];
			code= Character.toCodePoint(c1,c2);
			processSupplementaryCharacter= true;
		} else {
			c2= 0;
			code= c1;
			processSupplementaryCharacter= false;
		}
	}
}; // End of loop: Scan significand of the numerical token
String stringRepresentation;
if (numberContainsExtraSymbols) {
	stringRepresentation= auxiliaryBuffer.toString();
} else {
	int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber + 1;
	if (lastCharacterIsSharp) {
		// numberLength--;
		if (processSupplementaryCharacter) {
			numberLength= numberLength - 2;
		} else {
			numberLength--;
		}
	};
	stringRepresentation= new String(characters,beginningOfNumber,numberLength);
};
BigInteger numericalValue;
try {
	numericalValue= new BigInteger(stringRepresentation,radix);
} catch (NumberFormatException e) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		throw new BigIntegerFormatException(tokenPosition);
	}
};
if (numericalValue.compareTo(BigInteger.valueOf(Character.MAX_VALUE)) > 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		throw new CharacterCodeIsTooBig(beginningOfNumber);
	}
} else if (numericalValue.compareTo(BigInteger.valueOf(Character.MIN_VALUE)) < 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		throw new CharacterCodeIsTooSmall(beginningOfNumber);
	}
} else {
	buffer.append((char)numericalValue.intValue());
}
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: NUMERICAL LITERAL INSIDE STRING / SYMBOL (END)               :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			} else if (code == '\n') {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					throw new StringOrSymbolicLiteralIsNotTerminated(tokenPosition);
				}
			// } else if ( c >= 8 && c <= 13) {
			} else if (Character.isISOControl(code)) {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					throw new ControlCharactersAreNotAllowedHere(position);
				}
			} else {
				// buffer.append((char)c);
				if (processSupplementaryCharacter) {
					buffer.append(c1);
					buffer.append(c2);
				} else {
					buffer.append(c1);
				}
			}
		}
	} else if (code == '\n') {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new StringOrSymbolicLiteralIsNotTerminated(tokenPosition);
		}
	// } else if ( c >= 8 && c <= 13) {
	} else if (Character.isISOControl(code)) {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			// System.out.printf("ControlCharactersAreNotAllowedHere=%4x (%d)\n",code,code);
			throw new ControlCharactersAreNotAllowedHere(position);
		}
	} else {
		if (segmentContainsExtraSymbols) {
			// buffer.append((char)c);
			if (processSupplementaryCharacter) {
				buffer.append(c1);
				buffer.append(c2);
			} else {
				buffer.append(c1);
			}
		}
	};
	// position++;
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position >= textLength) {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw new StringOrSymbolicLiteralIsNotTerminated(tokenPosition);
		}
	} else {
		// c= characters[position];
		c1= characters[position];
		if (	(position + 1 <= textLength - 1) &&
			Character.isSurrogatePair(c1,characters[position+1])) {
			c2= characters[position+1];
			code= Character.toCodePoint(c1,c2);
			processSupplementaryCharacter= true;
		} else {
			c2= 0;
			code= c1;
			processSupplementaryCharacter= false;
		}
	}
}; // End of loop: Scan string segment token
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: STRING SEGMENT / SYMBOLIC LITERAL (END)                      :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				String stringRepresentation;
				if (segmentContainsExtraSymbols) {
					stringRepresentation= buffer.toString();
				} else {
					int numberLength= StrictMath.min(position,textLength-1) - beginningOfStringSegment; // + 1;
					stringRepresentation= new String(characters,beginningOfStringSegment,numberLength);
				};
				PrologToken textToken;
				if (isSymbol) {
					textToken= new TokenSymbol(SymbolNames.insertSymbolName(stringRepresentation),true,tokenPosition);
				} else {
					textToken= new TokenString(stringRepresentation,tokenPosition);
				};
				tokens.add(textToken);
			// } else if (	(c >= 'a' && c <= 'z') ||
			//		(c >= '†' && c <= 'Ø') ||	// DOS coding
			//		(c >= '‡' && c <= 'Ô') ||	// DOS coding
			//		(c == 'Ò') ) {			// DOS coding
			} else if (Character.isLetter(code) && Character.isLowerCase(code) ) {
				tokenPosition= position;
				afterComment= false;
				int beginningOfSymbol= position;
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: SYMBOL LITERAL (BEGINNING)                                   :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
while (true) { // Loop: Scan symbol token
	// if (c >= '0' && c <= '9') {
	if (Character.isLetterOrDigit(code)) {
		// Skip character
	// } else if (c >= 'a' && c <= 'z') {
	//	// Skip character
	// } else if (c >= 'A' && c <= 'Z') {
	//	// Skip character
	// } else if (c >= '†' && c <= 'Ø') { // DOS coding
	//	// Skip character
	// } else if (c >= '‡' && c <= 'Ô') { // DOS coding
	//	// Skip character
	// } else if (c >= 'Ä' && c <= 'è') { // DOS coding
	//	// Skip character
	// } else if (c >= 'ê' && c <= 'ü') { // DOS coding
	//	// Skip character
	// } else if (c == 'Ò') { // DOS coding
	// } else if (c == '') { // DOS coding
	} else if (code == '_') {
		// Skip character
	} else if (Character.isIdentifierIgnorable(code)) {
		// Skip character
	} else {
		// position--;
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
		break;
	};
	// position++;
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position >= textLength) {
		break;
	} else {
		// c= characters[position];
		c1= characters[position];
		if (	(position + 1 <= textLength - 1) &&
			Character.isSurrogatePair(c1,characters[position+1])) {
			c2= characters[position+1];
			code= Character.toCodePoint(c1,c2);
			processSupplementaryCharacter= true;
		} else {
			c2= 0;
			code= c1;
			processSupplementaryCharacter= false;
		}
	}
}; // End of loop: Scan symbol
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: SYMBOL LITERAL (END)                                         :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				int symbolLength= StrictMath.min(position,textLength-1) - beginningOfSymbol + 1; // + 1;
				String stringRepresentation= new String(characters,beginningOfSymbol,symbolLength);
				PrologToken textToken= new TokenSymbol(SymbolNames.insertSymbolName(stringRepresentation),false,tokenPosition);
				tokens.add(textToken);
			// } else if (	(c >= 'A' && c <= 'Z') ||
			//		(c >= 'Ä' && c <= 'è') ||	// DOS coding
			//		(c >= 'ê' && c <= 'ü') ||	// DOS coding
			//		(c == '') ||			// DOS coding
			//		(c == '_') ) {
			} else if (	(Character.isLetter(code) && (Character.isUpperCase(code) || Character.isTitleCase(code))) ||
					code == '_') {
				tokenPosition= position;
				afterComment= false;
				int beginningOfVariable= position;
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: VARIABLE / DOMAIN NAME LITERAL (BEGINNING)                   :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
while (true) { // Loop: Scan variable / domain name token
	// if (c >= '0' && c <= '9') {
	if (Character.isLetterOrDigit(code)) {
		// Skip character
	// } else if (c >= 'a' && c <= 'z') {
	//	// Skip character
	// } else if (c >= 'A' && c <= 'Z') {
	//	// Skip character
	// } else if (c >= '†' && c <= 'Ø') { // DOS coding
	//	// Skip character
	// } else if (c >= '‡' && c <= 'Ô') { // DOS coding
	//	// Skip character
	// } else if (c >= 'Ä' && c <= 'è') { // DOS coding
	//	// Skip character
	// } else if (c >= 'ê' && c <= 'ü') { // DOS coding
	//	// Skip character
	// } else if (c == 'Ò') { // DOS coding
	// } else if (c == '') { // DOS coding
	} else if (code == '_') {
		// Skip character
	} else {
		// position--;
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
		break;
	};
	// position++;
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position >= textLength) {
		break;
	} else {
		// c= characters[position];
		c1= characters[position];
		if (	(position + 1 <= textLength - 1) &&
			Character.isSurrogatePair(c1,characters[position+1])) {
			c2= characters[position+1];
			code= Character.toCodePoint(c1,c2);
			processSupplementaryCharacter= true;
		} else {
			c2= 0;
			code= c1;
			processSupplementaryCharacter= false;
		}
	}
}; // End of loop: Scan variable / domain name token
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: VARIABLE / DOMAIN NAME LITERAL (END)                         :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				int variableLength= StrictMath.min(position,textLength-1) - beginningOfVariable + 1; // + 1;
				String stringRepresentation= new String(characters,beginningOfVariable,variableLength);
				PrologToken textToken= new TokenVariable(stringRepresentation,tokenPosition);
				tokens.add(textToken);
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: MULTILINE COMMENT (BEGINNING)                                :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			} else if (	code == '/' &&
					(position + 1 < textLength) &&
					characters[position+1]=='*') {
				tokenPosition= position;
				while(true) {
					// position++;
					if (processSupplementaryCharacter) {
						position= position + 2;
					} else {
						position++;
					};
					if (position + 1 < textLength) {
						// c= characters[position];
						c1= characters[position];
						if (	(position + 1 <= textLength - 1) &&
							Character.isSurrogatePair(c1,characters[position+1])) {
							c2= characters[position+1];
							code= Character.toCodePoint(c1,c2);
							processSupplementaryCharacter= true;
						} else {
							c2= 0;
							code= c1;
							processSupplementaryCharacter= false;
						};
						char secondCharacter= characters[position+1];
						if (	secondCharacter == '/' &&
							code == '*') {
							position++;
							afterComment= true;
							break;
						} else {
							continue;
						}
					} else {
						if (robustMode) {
							throw TokenIsNotRecognized.instance;
						} else {
							throw new MultilineCommentIsNotTerminated(tokenPosition);
						}
					}
				}
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: MULTILINE COMMENT (END)                                      :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: SINGLE-LINE COMMENT (BEGINNING)                              :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			} else if (	code == '-' &&
					(position + 1 < textLength) &&
					characters[position+1]=='-') {
				tokenPosition= position;
				if (!afterComment && tokens.size() > 0) {
					PrologToken previousToken= tokens.get(tokens.size()-1);
					if (previousToken.getType() == PrologTokenType.MINUS) {
						if (robustMode) {
							throw TokenIsNotRecognized.instance;
						} else {
							throw new SingleLineCommentIsNotAllowedAfterMinusSign(tokenPosition);
						}
					}
				};
				if (position > 0) {
					char previousCharacter1= characters[position-1];
					if (previousCharacter1==':') {
						if (robustMode) {
							throw TokenIsNotRecognized.instance;
						} else {
							throw new SeparatorIsRequiredBeforeThisSingleLineComment(tokenPosition);
						}
					} else if (previousCharacter1=='<') {
						if (robustMode) {
							throw TokenIsNotRecognized.instance;
						} else {
							throw new SeparatorIsRequiredBeforeThisSingleLineComment(tokenPosition);
						}
					} else {
						if (position > 1) {
							char previousCharacter2= characters[position-2];
							if (	previousCharacter2==':' &&
								previousCharacter1=='-') {
								if (robustMode) {
									throw TokenIsNotRecognized.instance;
								} else {
									throw new SeparatorIsRequiredBeforeThisSingleLineComment(tokenPosition);
								}
							} else if (	previousCharacter2=='<' &&
									previousCharacter1=='-') {
								if (robustMode) {
									throw TokenIsNotRecognized.instance;
								} else {
									throw new SeparatorIsRequiredBeforeThisSingleLineComment(tokenPosition);
								}
							}
						}
					}
				};
				// position++;
				// if (processSupplementaryCharacter) {
				//	position= position + 2;
				// } else {
				//	position++;
				// };
				while(true) {
					// position++;
					if (processSupplementaryCharacter) {
						position= position + 2;
					} else {
						position++;
					};
					if (position < textLength) {
						// c= characters[position];
						c1= characters[position];
						if (	(position + 1 <= textLength - 1) &&
							Character.isSurrogatePair(c1,characters[position+1])) {
							c2= characters[position+1];
							code= Character.toCodePoint(c1,c2);
							processSupplementaryCharacter= true;
						} else {
							c2= 0;
							code= c1;
							processSupplementaryCharacter= false;
						};
						if (code == '\n') {
							// position++;
							// if (processSupplementaryCharacter) {
							//	position= position + 2;
							// } else {
							//	position++;
							// };
							afterComment= true;
							break;
						} else {
							continue;
						}
					} else {
						if (robustMode) {
							throw TokenIsNotRecognized.instance;
						} else {
							throw new SingleLineCommentIsNotTerminatedByNewline(tokenPosition);
						}
					}
				}
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: SINGLE-LINE COMMENT (END)                                    :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: PLAIN TOKENS (BEGINNING)                                     :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			} else {
				tokenPosition= position;
				afterComment= false;
				PrologTokenType plainTokenType;
				switch (code) {
				case ',':
					plainTokenType= PrologTokenType.COMMA;
					break;
				case '.':
					if (position + 1 < textLength) {
						char secondCharacter= characters[position+1];
						if (secondCharacter == '.') {
							position++;
							code= secondCharacter;
							plainTokenType= PrologTokenType.RANGE;
							break;
						}
					};
					plainTokenType= PrologTokenType.DOT;
					break;
				case '!':
					plainTokenType= PrologTokenType.EXCLAM;
					break;
				case ':':
					if (position + 1 < textLength) {
						char secondCharacter= characters[position+1];
						if (secondCharacter == '-' && braceLevel <= 0) {
							position++;
							code= secondCharacter;
							plainTokenType= PrologTokenType.IMPLICATION;
							break;
						} else if (secondCharacter == '=') {
							position++;
							code= secondCharacter;
							plainTokenType= PrologTokenType.ASSIGNMENT;
							break;
						}
					};
					plainTokenType= PrologTokenType.COLON;
					break;
				case ';':
					plainTokenType= PrologTokenType.SEMICOLON;
					break;
				case '?':
					if (position + 1 < textLength) {
						char secondCharacter= characters[position+1];
						if (secondCharacter == '?') {
							position++;
							code= secondCharacter;
							plainTokenType= PrologTokenType.RESIDENT;
							break;
						}
					};
					plainTokenType= PrologTokenType.QUESTION_MARK;
					break;
				case '#':
					plainTokenType= PrologTokenType.NUMBER_SIGN;
					break;
				case '(':
					plainTokenType= PrologTokenType.L_ROUND_BRACKET;
					break;
				case ')':
					plainTokenType= PrologTokenType.R_ROUND_BRACKET;
					break;
				case '|':
					plainTokenType= PrologTokenType.BAR;
					break;
				case '{':
					plainTokenType= PrologTokenType.L_BRACE;
					braceLevel++;
					break;
				case '}':
					plainTokenType= PrologTokenType.R_BRACE;
					braceLevel--;
					break;
				case '[':
					plainTokenType= PrologTokenType.L_SQUARE_BRACKET;
					break;
				case ']':
					plainTokenType= PrologTokenType.R_SQUARE_BRACKET;
					break;
				case '*':
					plainTokenType= PrologTokenType.MULTIPLY;
					break;
				case '+':
					plainTokenType= PrologTokenType.PLUS;
					break;
				case '-':
					if (position > 0) {
						char previousCharacter1= characters[position-1];
						if (previousCharacter1=='-') {
							if (robustMode) {
								throw TokenIsNotRecognized.instance;
							} else {
								throw new SeparatorIsRequiredBeforeMinusSign(tokenPosition);
							}
						}
					};
					plainTokenType= PrologTokenType.MINUS;
					break;
				case '/':
					plainTokenType= PrologTokenType.DIVIDE;
					break;
				case '<':
					if (position + 1 < textLength) {
						char secondCharacter= characters[position+1];
						if (secondCharacter == '<') {
							position++;
							code= secondCharacter;
							plainTokenType= PrologTokenType.INFORMATIONAL_MESSAGE;
							break;
						} else if (secondCharacter == '-') {
							position++;
							code= secondCharacter;
							plainTokenType= PrologTokenType.CONTROL_MESSAGE;
							break;
						} else if (secondCharacter == '>') {
							position++;
							code= secondCharacter;
							plainTokenType= PrologTokenType.NE;
							break;
						} else if (secondCharacter == '=') {
							position++;
							code= secondCharacter;
							plainTokenType= PrologTokenType.LE;
							break;
						}
					};
					plainTokenType= PrologTokenType.LT;
					break;
				case '=':
					if (position + 1 < textLength) {
						char secondCharacter= characters[position+1];
						if (secondCharacter == '=') {
							position++;
							code= secondCharacter;
							plainTokenType= PrologTokenType.EQUALITY;
							break;
						}
					};
					plainTokenType= PrologTokenType.EQ;
					break;
				case '>':
					if (position + 1 < textLength) {
						char secondCharacter= characters[position+1];
						if (secondCharacter == '=') {
							position++;
							code= secondCharacter;
							plainTokenType= PrologTokenType.GE;
							break;
						}
					};
					plainTokenType= PrologTokenType.GT;
					break;
				default:
					if (robustMode) {
						throw TokenIsNotRecognized.instance;
					} else {
						throw new UnexpectedCharacter(position);
					}
				};
				tokens.add(new TokenPlain(plainTokenType,tokenPosition));
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: PLAIN TOKENS (END)                                           :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			};
///////////////////////////////////////////////////////////////////////
// Recognize Token (End)                                             //
///////////////////////////////////////////////////////////////////////
			} catch (TokenIsNotRecognized e) {
				position= previousPosition;
				processSupplementaryCharacter= previousCharacterIsSupplementary;
				if (position >= textLength) {
					// if (processSupplementaryCharacter) {
					//	position= position - 2;
					// } else {
					//	position--;
					// };
					tokens.add(new TokenPlain(PrologTokenType.END_OF_TEXT,position));
					break;
				} else {
					c1= characters[position];
					if (	(position + 1 <= textLength - 1) &&
						Character.isSurrogatePair(c1,characters[position+1])) {
						c2= characters[position+1];
						code= Character.toCodePoint(c1,c2);
						processSupplementaryCharacter= true;
						tokens.add(new TokenString(new String(characters,position,2),position));
					} else {
						c2= 0;
						code= c1;
						processSupplementaryCharacter= false;
						tokens.add(new TokenString(new String(characters,position,1),position));
					}
				}
			};
			if (position >= textLength) {
				// if (processSupplementaryCharacter) {
				//	position= position - 2;
				// } else {
				//	position--;
				// };
				tokens.add(new TokenPlain(PrologTokenType.END_OF_TEXT,position));
				break;
			} else {
				// position++;
				if (processSupplementaryCharacter) {
					position= position + 2;
				} else {
					position++;
				};
				if (position >= textLength) {
					// if (processSupplementaryCharacter) {
					//	position= position - 2;
					// } else {
					//	position--;
					// };
					tokens.add(new TokenPlain(PrologTokenType.END_OF_TEXT,position));
					break;
				} else {
					if (extractFrontTokenOnly && tokens.size() > 0) {
						tokens.add(new TokenPlain(PrologTokenType.REST_OF_TEXT,position));
						break;
					};
					// c= characters[position];
					c1= characters[position];
					if (	(position + 1 <= textLength - 1) &&
						Character.isSurrogatePair(c1,characters[position+1])) {
						c2= characters[position+1];
						code= Character.toCodePoint(c1,c2);
						processSupplementaryCharacter= true;
					} else {
						c2= 0;
						code= c1;
						processSupplementaryCharacter= false;
					}
				}
			}
		}; // End of loop: Extract tokens
		return tokens.toArray(new PrologToken[0]);
	}
	//
	public static String skipFrontSpaces(String text) {
		int currentPosition= 0;
		boolean processSupplementaryCharacter= false;
		char[] characters= text.toCharArray();
		int textLength= characters.length;
		char c1;
		char c2;
		int code;
		while(true) {
			if (currentPosition >= textLength) {
				return "";
			} else {
				c1= characters[currentPosition];
				if (	(currentPosition + 1 <= textLength - 1) &&
					Character.isSurrogatePair(c1,characters[currentPosition+1])) {
					c2= characters[currentPosition+1];
					code= Character.toCodePoint(c1,c2);
					processSupplementaryCharacter= true;
				} else {
					c2= 0;
					code= c1;
					processSupplementaryCharacter= false;
				}
			};
			if (Character.isWhitespace(code) || Character.isSpaceChar(code) || Character.isISOControl(code)) {
				// Skip white space.
				if (processSupplementaryCharacter) {
					currentPosition= currentPosition + 2;
				} else {
					currentPosition++;
				};
				continue;
			} else {
				return new String(characters,currentPosition,textLength-currentPosition);
			}
		}
	}
	//
	public static boolean isSafeIdentifier(String text) {
		char[] characters= text.toCharArray();
		int c;
		int p= 0;
		int textLength= characters.length;
		if (p > textLength - 1) {
			return false;
		} else {
			if ((p + 1 <= textLength - 1) && Character.isSurrogatePair(characters[p],characters[p+1])) {
				c= Character.toCodePoint(characters[p],characters[p+1]);
				p= p + 2;
			} else {
				c= characters[p];
				p++;
			};
			if (Character.isLetter(c) && Character.isLowerCase(c)) {
				while (true) {
					if (p <= textLength - 1) {
						if ((p + 1 <= textLength - 1) && Character.isSurrogatePair(characters[p],characters[p+1])) {
							c= Character.toCodePoint(characters[p],characters[p+1]);
							p= p + 2;
						} else {
							c= characters[p];
							p++;
						};
						if (Character.isLetterOrDigit(c)) {
							continue;
						} else if (c == '_') {
							continue;
						} else if (Character.isIdentifierIgnorable(c)) {
							continue;
						} else {
							return false;
						}
					} else {
						return true;
					}
				}
			} else {
				return false;
			}
		}
	}
	//
	public static boolean isAnyIdentifier(String text) {
		char[] characters= text.toCharArray();
		int c;
		int p= 0;
		int textLength= characters.length;
		if (p > textLength - 1) {
			return false;
		} else {
			if ((p + 1 <= textLength - 1) && Character.isSurrogatePair(characters[p],characters[p+1])) {
				c= Character.toCodePoint(characters[p],characters[p+1]);
				p= p + 2;
			} else {
				c= characters[p];
				p++;
			};
			if (Character.isLetter(c) || c=='_') {
				while (true) {
					if (p <= textLength - 1) {
						if ((p + 1 <= textLength - 1) && Character.isSurrogatePair(characters[p],characters[p+1])) {
							c= Character.toCodePoint(characters[p],characters[p+1]);
							p= p + 2;
						} else {
							c= characters[p];
							p++;
						};
						if (Character.isLetterOrDigit(c)) {
							continue;
						} else if (c == '_') {
							continue;
						} else if (Character.isIdentifierIgnorable(c)) {
							continue;
						} else {
							return false;
						}
					} else {
						return true;
					}
				}
			} else {
				return false;
			}
		}
	}
}
