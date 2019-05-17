// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.syntax.scanner.signals.*;
import morozov.terms.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.math.BigInteger;
import java.math.BigDecimal;

public class LexicalScanner {
	//
	protected LexicalScannerMasterInterface master;
	//
	protected boolean robustMode= false;
	protected HashSet<String> keywordHash;
	protected HashSet<String> reservedNameHash;
	//
	protected int position;
	protected int braceLevel;
	protected boolean afterComment;
	protected int tokenPosition;
	protected int previousPosition;
	protected boolean previousCharacterIsSupplementary;
	//
	///////////////////////////////////////////////////////////////
	//
	public LexicalScanner(LexicalScannerMasterInterface m) {
		master= m;
		robustMode= true;
	}
	public LexicalScanner(LexicalScannerMasterInterface m, boolean rM) {
		master= m;
		robustMode= rM;
	}
	public LexicalScanner(LexicalScannerMasterInterface m, boolean rM, HashSet<String> hash1, HashSet<String> hash2) {
		master= m;
		robustMode= rM;
		keywordHash= hash1;
		reservedNameHash= hash2;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologToken[] analyse(String text, ChoisePoint iX) throws LexicalScannerError {
		return analyse(text,false,false,iX);
	}
	public PrologToken[] analyse(String text, boolean extractFrontTokenOnly, ChoisePoint iX) throws LexicalScannerError {
		return analyse(text,extractFrontTokenOnly,false,iX);
	}
	public PrologToken[] analyse(String text, boolean extractFrontTokenOnly, boolean recognizeEndOfLine, ChoisePoint iX) throws LexicalScannerError {
		position= 0;
		boolean processSupplementaryCharacter= false;
		braceLevel= 0;
		afterComment= true;
		char[] characters= text.toCharArray();
		int textLength= characters.length;
		ArrayList<PrologToken> tokens= new ArrayList<PrologToken>();
		char c1;
		char c2;
		int code;
		while (true) { // Loop: Extract tokens
			if (position >= textLength) {
				// if (processSupplementaryCharacter) {
				//	position= position - 2;
				// } else {
				//	position--;
				// };
				tokens.add(new TokenPlain(PrologTokenType.END_OF_TEXT,position));
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
			if (code == '\n') {
				if (recognizeEndOfLine) {
					tokens.add(new TokenPlain(PrologTokenType.END_OF_LINE,tokenPosition));
				}
			} else if (Character.isWhitespace(code) || Character.isSpaceChar(code) || Character.isISOControl(code)) {
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
						throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
						master.handleError(new SpaceAndControlCharactersAreNotAllowedHere(position),iX);
					}
				} else {
					PrologToken numericalToken= new TokenCharacter(code,tokenPosition);
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
				boolean isExtendedNumber= false;
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
				master.handleError(new UnderscoreCharacterIsNotAllowedHere(position),iX);
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
					throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
			isExtendedNumber= true;
// ====================================================================
// === Extended Number                                              ===
// ====================================================================
BigInteger numericalValue;
if (numberContainsExtraSymbols) {
	String stringRepresentation= buffer.toString();
	try {
		numericalValue= new BigInteger(stringRepresentation,radix);
	} catch (NumberFormatException e) {
		if (!robustMode) {
			master.handleError(new BigIntegerFormatException(stringRepresentation,radix,tokenPosition),iX);
		};
		throw TokenIsNotRecognized.instance;
	}
} else {
	int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber - 1; // + 1;
	String stringRepresentation= new String(characters,beginningOfNumber,numberLength);
	try {
		numericalValue= new BigInteger(stringRepresentation,radix);
	} catch (NumberFormatException e) {
		if (!robustMode) {
			master.handleError(new BigIntegerFormatException(stringRepresentation,radix,tokenPosition),iX);
		};
		throw TokenIsNotRecognized.instance;
	}
};
if (numericalValue.compareTo(BigInteger.valueOf(Character.MAX_RADIX)) > 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		master.handleError(new IntegerRadixIsTooBig(numericalValue,tokenPosition),iX);
	}
} else if (numericalValue.compareTo(BigInteger.valueOf(Character.MIN_RADIX)) < 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		master.handleError(new IntegerRadixIsTooSmall(numericalValue,tokenPosition),iX);
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
				master.handleError(new DotIsNotAllowedHere(position),iX);
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
				master.handleError(new UnderscoreCharacterIsNotAllowedHere(position),iX);
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
				master.handleError(new NumberSignIsNotAllowedHere(position),iX);
			}
		}
	} else {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			master.handleError(new ExtendedDigitIsExpected(position),iX);
			break; // 2019-05-10
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
			throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
					master.handleError(new ExtendedDigitIsExpected(position),iX);
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
			// 2019-05-10
			master.handleError(new DigitIsExpected(position),iX);
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
				throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
				throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
					throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
				master.handleError(new UnderscoreCharacterIsNotAllowedHere(position),iX);
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
				master.handleError(new DigitIsExpected(position),iX);
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
				throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
					master.handleError(new DigitIsExpected(position),iX);
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
				if (!robustMode) {
					master.handleError(new BigIntegerFormatException(stringExponent,10,tokenPosition),iX);
				};
				throw TokenIsNotRecognized.instance;
			};
			// exponent= BigInteger.valueOf(scale).subtract(exponent);
			exponent= scale.subtract(exponent);
			// if (exponent.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
			//	if (robustMode) {
			//		throw TokenIsNotRecognized.instance;
			//	} else {
			//		master.handleError(new IntegerExponentIsTooBig(tokenPosition),iX);
			//	}
			// } else if (exponent.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
			//	if (robustMode) {
			//		throw TokenIsNotRecognized.instance;
			//	} else {
			//		master.handleError(new IntegerExponentIsTooSmall(tokenPosition),iX);
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
		if (!robustMode) {
			master.handleError(new BigIntegerFormatException(stringRepresentation,radix,tokenPosition),iX);
		};
		throw TokenIsNotRecognized.instance;
	};
	if (radix==10) {
		if (PrologInteger.isSmallInteger(scale)) {
			BigDecimal decimalRepresentation;
			try {
				decimalRepresentation= new BigDecimal(integerRepresentation,scale.intValue());
			} catch (NumberFormatException e) {
				if (!robustMode) {
					master.handleError(new BigDecimalFormatException(integerRepresentation,scale,tokenPosition),iX);
				};
				throw TokenIsNotRecognized.instance;
			};
			numericalToken= new TokenReal10(
				decimalRepresentation.doubleValue(),
				isExtendedNumber,
				tokenPosition);
		} else {
			double doubleValue= integerRepresentation.doubleValue();
			BigInteger positiveScale= scale.negate();
			doubleValue= doubleValue * StrictMath.pow(radix,positiveScale.doubleValue());
			numericalToken= new TokenReal10(
				doubleValue,
				isExtendedNumber,
				tokenPosition);
			}
	} else {
		double doubleValue= integerRepresentation.doubleValue();
		BigInteger bigRadix= BigInteger.valueOf(radix);
		BigInteger positiveScale= scale.negate();
		doubleValue= doubleValue * StrictMath.pow(radix,positiveScale.doubleValue());
		numericalToken= new TokenRealR(
			bigRadix,
			doubleValue,
			isExtendedNumber,
			tokenPosition);
	}
} else {
	if ((scale.compareTo(BigInteger.ZERO) < 0)) {
		BigInteger integerRepresentation;
		try {
			integerRepresentation= new BigInteger(stringRepresentation,radix);
		} catch (NumberFormatException e) {
			if (!robustMode) {
				master.handleError(new BigIntegerFormatException(stringRepresentation,radix,tokenPosition),iX);
			};
			throw TokenIsNotRecognized.instance;
		};
		if (radix==10) {
			if (PrologInteger.isSmallInteger(scale)) {
				BigDecimal decimalRepresentation;
				int exponent= scale.intValue();
				try {
					decimalRepresentation= new BigDecimal(integerRepresentation,exponent);
				} catch (NumberFormatException e) {
					if (!robustMode) {
						master.handleError(new BigDecimalFormatException(integerRepresentation,exponent,tokenPosition),iX);
					};
					throw TokenIsNotRecognized.instance;
				};
				numericalToken= new TokenInteger10(
					decimalRepresentation.toBigInteger(),
					isExtendedNumber,
					tokenPosition);
			} else {
				BigInteger bigExponent;
				BigInteger bigRadix= BigInteger.valueOf(radix);
				BigInteger positiveScale= scale.negate();
				if (PrologInteger.isSmallInteger(positiveScale)) {
					bigExponent= bigRadix.pow(positiveScale.intValue());
				} else {
					bigExponent= BigInteger.ONE;
					int increment= Integer.MAX_VALUE;
					BigInteger bigMaxPoveredRadix= bigRadix.pow(increment);
					BigInteger bigMaxInteger= BigInteger.valueOf(increment);
					while(positiveScale.compareTo(bigMaxInteger) >= 0) {
						bigExponent= bigExponent.multiply(bigMaxPoveredRadix);
						positiveScale= positiveScale.subtract(bigMaxInteger);
					};
					bigExponent= bigExponent.multiply(bigRadix.pow(positiveScale.intValue()));
				};
				numericalToken= new TokenInteger10(
					integerRepresentation.multiply(bigExponent),
					isExtendedNumber,
					tokenPosition);
				}
		} else {
			BigInteger bigExponent;
			BigInteger bigRadix= BigInteger.valueOf(radix);
			BigInteger positiveScale= scale.negate();
			if (PrologInteger.isSmallInteger(positiveScale)) {
				bigExponent= bigRadix.pow(positiveScale.intValue());
			} else {
				bigExponent= BigInteger.ONE;
				int increment= Integer.MAX_VALUE;
				BigInteger bigMaxPoveredRadix= bigRadix.pow(increment);
				BigInteger bigMaxInteger= BigInteger.valueOf(increment);
				while(positiveScale.compareTo(bigMaxInteger) >= 0) {
					bigExponent= bigExponent.multiply(bigMaxPoveredRadix);
					positiveScale= positiveScale.subtract(bigMaxInteger);
				};
				bigExponent= bigExponent.multiply(bigRadix.pow(positiveScale.intValue()));
			};
			numericalToken= new TokenIntegerR(
				bigRadix,
				integerRepresentation.multiply(bigExponent),
				isExtendedNumber,
				tokenPosition);
		}
	// } else if (scale > 0) {
	} else if (scale.compareTo(BigInteger.ZERO) > 0) {
		if (!robustMode) {
			master.handleError(new IntegerCannotHaveNegativeExponent(tokenPosition),iX);
		};
		throw TokenIsNotRecognized.instance;
	} else { // (scale == 0)
		BigInteger integerRepresentation;
		try {
			integerRepresentation= new BigInteger(stringRepresentation,radix);
		} catch (NumberFormatException e) {
			if (!robustMode) {
				master.handleError(new BigIntegerFormatException(stringRepresentation,radix,tokenPosition),iX);
			};
			throw TokenIsNotRecognized.instance;
		};
		if (radix==10) {
			numericalToken= new TokenInteger10(
				integerRepresentation,
				isExtendedNumber,
				tokenPosition);
		} else {
			numericalToken= new TokenIntegerR(
				BigInteger.valueOf(radix),
				integerRepresentation,
				isExtendedNumber,
				tokenPosition);
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
						throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
while (true) { // Loop: Scan string segment token
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
				master.handleError(new StringOrSymbolicLiteralIsNotTerminated(tokenPosition),iX);
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
				master.handleError(new UnderscoreCharacterIsNotAllowedHere(position),iX);
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
					throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
	String stringRepresentation= auxiliaryBuffer.toString();
	try {
		numericalValue= new BigInteger(stringRepresentation,radix);
	} catch (NumberFormatException e) {
		if (!robustMode) {
			master.handleError(new BigIntegerFormatException(stringRepresentation,radix,tokenPosition),iX);
		};
		throw TokenIsNotRecognized.instance;
	}
} else {
	int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber - 1; // + 1;
	String stringRepresentation= new String(characters,beginningOfNumber,numberLength);
	try {
		numericalValue= new BigInteger(stringRepresentation,radix);
	} catch (NumberFormatException e) {
		if (!robustMode) {
			master.handleError(new BigIntegerFormatException(stringRepresentation,radix,tokenPosition),iX);
		};
		throw TokenIsNotRecognized.instance;
	}
};
if (numericalValue.compareTo(BigInteger.valueOf(Character.MAX_RADIX)) > 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		master.handleError(new IntegerRadixIsTooBig(numericalValue,beginningOfNumber),iX);
	}
} else if (numericalValue.compareTo(BigInteger.valueOf(Character.MIN_RADIX)) < 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		master.handleError(new IntegerRadixIsTooSmall(numericalValue,beginningOfNumber),iX);
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
			master.handleError(new DotIsNotAllowedHere(position),iX);
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
				master.handleError(new UnderscoreCharacterIsNotAllowedHere(position),iX);
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
				master.handleError(new NumberSignIsNotAllowedHere(position),iX);
			}
		}
	} else {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			master.handleError(new ExtendedDigitIsExpected(position),iX);
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
			throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
					master.handleError(new ExtendedDigitIsExpected(position),iX);
				}
			}
		} else if (code == '.') {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				master.handleError(new DotIsNotAllowedHere(position),iX);
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
			master.handleError(new ExtendedDigitIsExpected(position),iX);
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
				throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
	if (!robustMode) {
		master.handleError(new BigIntegerFormatException(stringRepresentation,radix,tokenPosition),iX);
	};
	throw TokenIsNotRecognized.instance;
};
if (numericalValue.compareTo(BigInteger.valueOf(Character.MAX_VALUE)) > 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		master.handleError(new CharacterCodeIsTooBig(beginningOfNumber),iX);
	}
} else if (numericalValue.compareTo(BigInteger.valueOf(Character.MIN_VALUE)) < 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		master.handleError(new CharacterCodeIsTooSmall(beginningOfNumber),iX);
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
					master.handleError(new StringOrSymbolicLiteralIsNotTerminated(tokenPosition),iX);
				}
			// } else if ( c >= 8 && c <= 13) {
			} else if (Character.isISOControl(code)) {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					master.handleError(new ControlCharactersAreNotAllowedHere(position),iX);
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
			master.handleError(new StringOrSymbolicLiteralIsNotTerminated(tokenPosition),iX);
		}
	// } else if ( c >= 8 && c <= 13) {
	} else if (Character.isISOControl(code)) {
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			master.handleError(new ControlCharactersAreNotAllowedHere(position),iX);
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
			master.handleError(new StringOrSymbolicLiteralIsNotTerminated(tokenPosition),iX);
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
boolean afterUnderscore= false;
int recentCode= code;
while (true) { // Loop: Scan symbol token
	// if (c >= '0' && c <= '9') {
	if (Character.isLetterOrDigit(code)) {
		afterUnderscore= false;
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
		if (!robustMode) {
			if (afterUnderscore) {
				master.handleError(new DoubleUnderscoresAreNotAllowed(position),iX);
			}
		};
		afterUnderscore= true;
		// Skip character
	} else if (Character.isIdentifierIgnorable(code)) {
		afterUnderscore= false;
		// Skip character
	} else {
		// position--;
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
		afterUnderscore= false;
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
	};
	recentCode= code;
}; // End of loop: Scan symbol
if (!robustMode) {
	if (recentCode=='_' && afterUnderscore) {
		master.handleError(new SymbolCannotBeCompletedByUnderscore(position),iX);
	}
};
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
int symbolLength= StrictMath.min(position,textLength-1) - beginningOfSymbol + 1; // + 1;
String stringRepresentation= new String(characters,beginningOfSymbol,symbolLength);
boolean isKeyword= false;
if (reservedNameHash != null) {
	String lowerCaseText= stringRepresentation.toLowerCase();
	if (reservedNameHash.contains(lowerCaseText)) {
		isKeyword= true;
		if (keywordHash != null) {
			if (!keywordHash.contains(stringRepresentation)) {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					master.handleError(new KeywordContainsLettersOfIllegalCase(tokenPosition),iX);
				}
			}
		}
	}
};
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: SYMBOL LITERAL (END)                                         :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				int symbolCode= SymbolNames.insertSymbolName(stringRepresentation);
				PrologToken textToken;
				if (!isKeyword) {
					textToken= new TokenSymbol(symbolCode,false,tokenPosition);
				} else {
					textToken= new TokenKeyword(symbolCode,tokenPosition);
				};
				tokens.add(textToken);
			// } else if (	(c >= 'A' && c <= 'Z') ||
			//		(c >= 'Ä' && c <= 'è') ||	// DOS coding
			//		(c >= 'ê' && c <= 'ü') ||	// DOS coding
			//		(c == '') ||			// DOS coding
			//		(c == '_') ) {
			} else if (code == '~') {
// ====================================================================
// === Binary                                                       ===
// ====================================================================
// position++;
tokenPosition= position;
if (processSupplementaryCharacter) {
	position= position + 2;
} else {
	position++;
};
if (position >= textLength) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
int numberOfDigits= 0;
boolean afterDigit= false;
int temporaryPosition= position;
while (true) { // Loop: Scan extended number
	if (Character.isDigit(code)) {
		numberOfDigits++;
		afterDigit= true;
	} else if (code >= 'a' && code <= 'f') {
		numberOfDigits++;
		afterDigit= true;
	} else if (code >= 'A' && code <= 'F') {
		numberOfDigits++;
		afterDigit= true;
	} else if (code == '_') {
		if (afterDigit) {
			afterDigit= false;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				master.handleError(new UnderscoreCharacterIsNotAllowedHere(temporaryPosition),iX);
			}
		}
	} else {
		if (afterDigit) {
			break;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				master.handleError(new ExtendedDigitIsExpected(temporaryPosition),iX);
			}
		}
	};
	if (processSupplementaryCharacter) {
		temporaryPosition= temporaryPosition + 2;
	} else {
		temporaryPosition++;
	};
	if (temporaryPosition >= textLength) {
		if (!afterDigit && !robustMode) {
			throw master.handleUnexpectedEndOfText(tokenPosition,iX);
		};
		break;
	} else {
		c1= characters[temporaryPosition];
		if (	(temporaryPosition + 1 <= textLength - 1) &&
			Character.isSurrogatePair(c1,characters[temporaryPosition+1])) {
			c2= characters[temporaryPosition+1];
			code= Character.toCodePoint(c1,c2);
			processSupplementaryCharacter= true;
		} else {
			c2= 0;
			code= c1;
			processSupplementaryCharacter= false;
		}
	}
};
if ((numberOfDigits % 2) != 0) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		master.handleError(new BinaryMustContainEvenNumberOfDigits(tokenPosition),iX);
	}
};
int arrayLength= numberOfDigits / 2;
byte[] byteArray= new byte[arrayLength];
if (position >= textLength) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		throw master.handleUnexpectedEndOfText(tokenPosition,iX);
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
int firstDigit= 0;
boolean isSecondDigit= false;
int index= -1;
afterDigit= false;
while (true) { // Loop: Scan extended number
	int value= 0;
	if (Character.isDigit(code)) {
		value= code - '0';
		afterDigit= true;
	} else if (code >= 'a' && code <= 'f') {
		value= code - 'a' + 10;
		afterDigit= true;
	} else if (code >= 'A' && code <= 'F') {
		value= code - 'A' + 10;
		afterDigit= true;
	} else if (code == '_') {
		if (afterDigit) {
			afterDigit= false;
		} else {
			if (robustMode) {
				throw TokenIsNotRecognized.instance;
			} else {
				master.handleError(new UnderscoreCharacterIsNotAllowedHere(position),iX);
			}
		}
	} else {
		if (afterDigit) {
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
				master.handleError(new ExtendedDigitIsExpected(position),iX);
			}
		}
	};
	if (afterDigit) {
		if (isSecondDigit) {
			int element= firstDigit << 4 | value;
			index++;
			byteArray[index]= (byte)element;
			isSecondDigit= false;
		} else {
			firstDigit= value;
			isSecondDigit= true;
		}
	};
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position >= textLength) {
		/*
		if (robustMode) {
			throw TokenIsNotRecognized.instance;
		} else {
			throw master.handleUnexpectedEndOfText(tokenPosition,iX);
		}
		*/
		break;
	} else {
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
}; // End of loop: Scan binary
// ====================================================================
				tokens.add(new TokenBinary(byteArray,tokenPosition));
			} else if (	(Character.isLetter(code) && (Character.isUpperCase(code) || Character.isTitleCase(code))) ||
					code == '_') {
				tokenPosition= position;
				afterComment= false;
				int beginningOfVariable= position;
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: VARIABLE / DOMAIN NAME LITERAL (BEGINNING)                   :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
boolean afterUnderscore= false;
int recentCode= code;
while (true) { // Loop: Scan variable / domain name token
	// if (c >= '0' && c <= '9') {
	if (Character.isLetterOrDigit(code)) {
		afterUnderscore= false;
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
		if (!robustMode) {
			if (afterUnderscore) {
				master.handleError(new DoubleUnderscoresAreNotAllowed(position),iX);
			}
		};
		afterUnderscore= true;
		// Skip character
	} else {
		// position--;
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
		afterUnderscore= false;
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
	};
	recentCode= code;
}; // End of loop: Scan variable / domain name token
int variableLength= StrictMath.min(position,textLength-1) - beginningOfVariable + 1; // + 1;
if (!robustMode) {
	if (recentCode=='_' && variableLength > 1 && afterUnderscore) {
		master.handleError(new VariableCannotBeCompletedByUnderscore(position),iX);
	}
};
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
String stringRepresentation= new String(characters,beginningOfVariable,variableLength);
boolean isKeyword= false;
if (reservedNameHash != null) {
	String lowerCaseText= stringRepresentation.toLowerCase();
	if (reservedNameHash.contains(lowerCaseText)) {
		isKeyword= true;
		if (keywordHash != null) {
			if (!keywordHash.contains(stringRepresentation)) {
				if (robustMode) {
					throw TokenIsNotRecognized.instance;
				} else {
					master.handleError(new KeywordContainsLettersOfIllegalCase(tokenPosition),iX);
				}
			}
		}
	}
};
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: VARIABLE / DOMAIN NAME LITERAL (END)                         :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				PrologToken textToken;
				if (!isKeyword) {
					textToken= new TokenVariable(stringRepresentation,tokenPosition);
				} else {
					int symbolCode= SymbolNames.insertSymbolName(stringRepresentation);
					textToken= new TokenKeyword(symbolCode,tokenPosition);
				};
				tokens.add(textToken);
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: MULTILINE COMMENT (BEGINNING)                                :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			} else if (	code == '/' &&
					(position + 1 < textLength) &&
					characters[position+1]=='*') {
				tokenPosition= position;
				while (true) {
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
							master.handleError(new MultilineCommentIsNotTerminated(tokenPosition),iX);
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
							master.handleError(new SingleLineCommentIsNotAllowedAfterMinusSign(tokenPosition),iX);
						}
					}
				};
				if (position > 0) {
					char previousCharacter1= characters[position-1];
					if (previousCharacter1==':') {
						if (robustMode) {
							throw TokenIsNotRecognized.instance;
						} else {
							master.handleError(new SeparatorIsRequiredBeforeThisSingleLineComment(tokenPosition),iX);
						}
					} else if (previousCharacter1=='<') {
						if (robustMode) {
							throw TokenIsNotRecognized.instance;
						} else {
							master.handleError(new SeparatorIsRequiredBeforeThisSingleLineComment(tokenPosition),iX);
						}
					} else {
						if (position > 1) {
							char previousCharacter2= characters[position-2];
							if (	previousCharacter2==':' &&
								previousCharacter1=='-') {
								if (robustMode) {
									throw TokenIsNotRecognized.instance;
								} else {
									master.handleError(new SeparatorIsRequiredBeforeThisSingleLineComment(tokenPosition),iX);
								}
							} else if (	previousCharacter2=='<' &&
									previousCharacter1=='-') {
								if (robustMode) {
									throw TokenIsNotRecognized.instance;
								} else {
									master.handleError(new SeparatorIsRequiredBeforeThisSingleLineComment(tokenPosition),iX);
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
				while (true) {
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
							master.handleError(new SingleLineCommentIsNotTerminatedByNewline(tokenPosition),iX);
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
								master.handleError(new SeparatorIsRequiredBeforeMinusSign(tokenPosition),iX);
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
							plainTokenType= PrologTokenType.DATA_MESSAGE;
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
					if (!robustMode) {
						master.handleError(new UnexpectedCharacter(code,position),iX);
					};
					throw TokenIsNotRecognized.instance;
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
		while (true) {
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
