// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.scanner.interfaces.*;
import morozov.syntax.scanner.signals.*;
import morozov.system.*;

import java.util.Arrays;
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
	protected HashSet<String> lowerCaseNameHash;
	protected HashSet<String> upperCaseNameHash;
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
	public LexicalScanner(LexicalScannerMasterInterface m, boolean rM, String[] keywordArray) {
		master= m;
		robustMode= rM;
		HashSet<String> hash1= new HashSet<>();
		HashSet<String> hash2= new HashSet<>();
		HashSet<String> hash3= new HashSet<>();
		for (int k=0; k < keywordArray.length; k++) {
			String keyword= keywordArray[k];
			hash1.add(keyword);
			if (keyword.length() > 0) {
				if (Character.isLowerCase(keyword.codePointAt(0))) {
					hash2.add(keyword.toLowerCase());
				} else {
					hash3.add(keyword.toUpperCase());
				}
			}
		};
		keywordHash= hash1;
		lowerCaseNameHash= hash2;
		upperCaseNameHash= hash3;
	}
	public LexicalScanner(LexicalScannerMasterInterface m, boolean rM, HashSet<String> hash1, HashSet<String> hash2, HashSet<String> hash3) {
		master= m;
		robustMode= rM;
		keywordHash= hash1;
		lowerCaseNameHash= hash2;
		upperCaseNameHash= hash3;
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
		ArrayList<PrologToken> tokens= new ArrayList<>();
		char c1;
		char c2;
		int code;
		ExtractTokens: while (true) { // Loop: Extract tokens
			if (position >= textLength) {
				tokens.add(new TokenPlain(PrologTokenType.END_OF_TEXT,position));
				break ExtractTokens;
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
			};
			try {
				previousPosition= position;
				previousCharacterIsSupplementary= processSupplementaryCharacter;
///////////////////////////////////////////////////////////////////////
// Recognize Token (Beginning)                                       //
///////////////////////////////////////////////////////////////////////
			if (code == '\n') {
				if (recognizeEndOfLine) {
					tokens.add(new TokenPlain(PrologTokenType.END_OF_LINE,tokenPosition));
				}
			} else if (Character.isWhitespace(code) || Character.isSpaceChar(code) || Character.isISOControl(code)) {
				// Skip white space.
			} else if (code == '`') {
				tokenPosition= position;
				afterComment= false;
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
				if (Character.isWhitespace(code) || Character.isSpaceChar(code) || Character.isISOControl(code)) {
					if (!robustMode) {
						master.handleError(new SpaceAndControlCharactersAreNotAllowedHere(position),iX);
					}
				};
				PrologToken numericalToken= new TokenCharacter(code,tokenPosition);
				tokens.add(numericalToken);
			} else if (Character.isDigit(code)) {
				tokenPosition= position;
				afterComment= false;
				int beginningOfNumber= position;
				boolean lastCharacterIsSharp= false;
				boolean numberContainsExtraSymbols= false;
				StringBuilder buffer= new StringBuilder();
				int radix= 10;
				BigInteger scale= BigInteger.ZERO;
				boolean afterDigit= false;
				boolean afterDot= false;
				boolean isExtendedNumber= false;
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: NUMERICAL LITERAL (BEGINNING)                                :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
ScanSignificand: while (true) { // Loop: Scan significand of the numerical token
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
			scale= scale.add(BigInteger.ONE);
		};
		afterDigit= true;
	} else if (code == '_') {
		if (!afterDigit) {
			if (!robustMode) {
				master.handleError(new UnderscoreCharacterIsNotAllowedHere(position),iX);
			}
		};
		if (!numberContainsExtraSymbols) {
			if (beginningOfNumber < position) {
				int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber;
				String initialContent= new String(characters,beginningOfNumber,numberLength);
				buffer.append(initialContent);
			};
			numberContainsExtraSymbols= true;
		};
		afterDigit= false;
	} else if (afterDigit && !afterDot) {
		if (code == '#') {
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
	int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber - 1;
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
scale= BigInteger.ZERO;
afterDigit= false;
afterDot= false;
ScanExtendedNumber: while (true) { // Loop: Scan extended number
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
			scale= scale.add(BigInteger.ONE);
		};
		afterDigit= true;
	} else if (code >= 'a' && code <= 'z') {
		if (numberContainsExtraSymbols) {
			if (processSupplementaryCharacter) {
				buffer.append(c1);
				buffer.append(c2);
			} else {
				buffer.append(c1);
			}
		};
		if (afterDot) {
			scale= scale.add(BigInteger.ONE);
		};
		afterDigit= true;
	} else if (code >= 'A' && code <= 'Z') {
		if (numberContainsExtraSymbols) {
			if (processSupplementaryCharacter) {
				buffer.append(c1);
				buffer.append(c2);
			} else {
				buffer.append(c1);
			}
		};
		if (afterDot) {
			scale= scale.add(BigInteger.ONE);
		};
		afterDigit= true;
	} else if (code == '.') {
		if (!afterDigit || afterDot) {
			if (!robustMode) {
				master.handleError(new DotIsNotAllowedHere(position),iX);
			}
		};
		if (!numberContainsExtraSymbols) {
			if (beginningOfNumber < position) {
				int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber;
				String initialContent= new String(characters,beginningOfNumber,numberLength);
				buffer.append(initialContent);
			};
			numberContainsExtraSymbols= true;
		};
		afterDigit= false;
		afterDot= true;
	} else if (code == '_') {
		if (!afterDigit) {
			if (!robustMode) {
				master.handleError(new UnderscoreCharacterIsNotAllowedHere(position),iX);
			}
		};
		if (!numberContainsExtraSymbols) {
			if (beginningOfNumber < position) {
				int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber;
				String initialContent= new String(characters,beginningOfNumber,numberLength);
				buffer.append(initialContent);
			};
			numberContainsExtraSymbols= true;
		};
		afterDigit= false;
	} else {
		if (code == '#') {
			lastCharacterIsSharp= true;
		} else {
			if (!robustMode) {
				master.handleError(new ExtendedDigitIsExpected(position),iX);
			}
		};
		if (!afterDigit) {
			if (!robustMode) {
				master.handleError(new NumberSignIsNotAllowedHere(position),iX);
			}
		};
		break ScanExtendedNumber;
	};
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
			if (!afterDigit) {
				if (!robustMode) {
					master.handleError(new ExtendedDigitIsExpected(position),iX);
				}
			};
			break ScanSignificand;
		} else if (code == '.') {
			int p2;
			if (processSupplementaryCharacter) {
				p2= position + 2;
			} else {
				p2= position + 1;
			};
			if (p2 >= textLength) {
				if (processSupplementaryCharacter) {
					position= position - 2;
				} else {
					position--;
				};
				break ScanSignificand;
			} else {
				char nextC1= characters[p2];
				char nextC2;
				int nextCode;
				boolean nextProcessSupplementaryCharacter;
				if (	(p2 + 1 <= textLength - 1) &&
					Character.isSurrogatePair(nextC1,characters[p2+1])) {
					nextC2= characters[p2+1];
					nextCode= Character.toCodePoint(nextC1,nextC2);
					nextProcessSupplementaryCharacter= true;
				} else {
					nextC2= 0;
					nextCode= nextC1;
					nextProcessSupplementaryCharacter= false;
				};
				if (Character.isDigit(nextCode)) {
					if (!numberContainsExtraSymbols) {
						if (beginningOfNumber < position) {
							int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber;
							String initialContent= new String(characters,beginningOfNumber,numberLength);
							buffer.append(initialContent);
						};
						numberContainsExtraSymbols= true;
					};
					afterDigit= false;
					afterDot= true;
					position= p2;
					c1= nextC1;
					c2= nextC2;
					code= nextCode;
					processSupplementaryCharacter= nextProcessSupplementaryCharacter;
					continue ScanSignificand;
				} else {
					if (processSupplementaryCharacter) {
						position= position - 2;
					} else {
						position--;
					};
					break ScanSignificand;
				}
			}
		} else {
			if (processSupplementaryCharacter) {
				position= position - 2;
			} else {
				position--;
			};
			break ScanSignificand;
		}
	} else {
		if (!afterDigit) {
			if (!robustMode) {
				master.handleError(new DigitIsExpected(position),iX);
			}
		};
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
		break ScanSignificand;
	};
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position >= textLength) {
		if (!afterDigit) {
			if (!robustMode) {
				throw master.handleUnexpectedEndOfText(tokenPosition,iX);
			}
		};
		break ScanSignificand;
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
}; // End of loop: Scan significand of the numerical token
String stringRepresentation;
if (numberContainsExtraSymbols) {
	stringRepresentation= buffer.toString();
} else {
	int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber + 1;
	if (lastCharacterIsSharp) {
		if (processSupplementaryCharacter) {
			numberLength= numberLength - 2;
		} else {
			numberLength--;
		}
	};
	stringRepresentation= new String(characters,beginningOfNumber,numberLength);
};
if (processSupplementaryCharacter) {
	position= position + 2;
} else {
	position++;
};
if (position < textLength) {
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
				if (processSupplementaryCharacter) {
					beginningOfNumber= beginningOfNumber + 2;
				} else {
					beginningOfNumber++;
				}
			} else {
				if (processSupplementaryCharacter) {
					position= position - 2;
				} else {
					position--;
				}
			};
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
// === Exponent                                                     ===
// ====================================================================
if (!Character.isDigit(code)) {
	if (!robustMode) {
		master.handleError(new DigitIsExpected(position),iX);
	}
};
afterDigit= false;
ScanExponent: while (true) { // Loop: Scan exponent
	if (Character.isDigit(code)) {
		if (numberContainsExtraSymbols) {
			if (processSupplementaryCharacter) {
				buffer.append(c1);
				buffer.append(c2);
			} else {
				buffer.append(c1);
			}
		};
		afterDigit= true;
	} else if (code == '_') {
		if (!afterDigit) {
			if (!robustMode) {
				master.handleError(new UnderscoreCharacterIsNotAllowedHere(position),iX);
			}
		};
		if (!numberContainsExtraSymbols) {
			if (beginningOfNumber < position) {
				int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber;
				String initialContent= new String(characters,beginningOfNumber,numberLength);
				buffer.append(initialContent);
			};
			numberContainsExtraSymbols= true;
		};
		afterDigit= false;
	} else {
		if (!afterDigit) {
			if (!robustMode) {
				master.handleError(new DigitIsExpected(position),iX);
			}
		};
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
		break ScanExponent;
	};
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	}
	if (position >= textLength) {
		if (!afterDigit) {
			if (!robustMode) {
				throw master.handleUnexpectedEndOfText(tokenPosition,iX);
			}
		};
		break ScanExponent;
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
} // End of loop: Scan exponent
// ====================================================================
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
			exponent= scale.subtract(exponent);
			scale= exponent;
		}
	} else { // End of block: Scan exponent
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
		if (Arithmetic.isSmallInteger(scale)) {
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
			if (Arithmetic.isSmallInteger(scale)) {
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
				if (Arithmetic.isSmallInteger(positiveScale)) {
					bigExponent= bigRadix.pow(positiveScale.intValue());
				} else {
					bigExponent= BigInteger.ONE;
					int increment= Integer.MAX_VALUE;
					BigInteger bigMaxPoveredRadix= bigRadix.pow(increment);
					BigInteger bigMaxInteger= BigInteger.valueOf(increment);
					while (positiveScale.compareTo(bigMaxInteger) >= 0) {
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
			if (Arithmetic.isSmallInteger(positiveScale)) {
				bigExponent= bigRadix.pow(positiveScale.intValue());
			} else {
				bigExponent= BigInteger.ONE;
				int increment= Integer.MAX_VALUE;
				BigInteger bigMaxPoveredRadix= bigRadix.pow(increment);
				BigInteger bigMaxInteger= BigInteger.valueOf(increment);
				while (positiveScale.compareTo(bigMaxInteger) >= 0) {
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
	} else if (scale.compareTo(BigInteger.ZERO) > 0) {
		if (!robustMode) {
			master.handleError(new IntegerCannotHaveNegativeExponent(tokenPosition),iX);
		};
		// ROBUST MODE ONLY:
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
			if (Arithmetic.isSmallInteger(scale)) {
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
ScanStringSegmentToken: while (true) { // Loop: Scan string segment token
	if (code == '"' && !isSymbol) {
		break ScanStringSegmentToken;
	} else if (code == '\'' && isSymbol) {
		break ScanStringSegmentToken;
	} else if (code == '\\') {
		if (!segmentContainsExtraSymbols) {
			if (beginningOfStringSegment < position) {
				int segmentLength= StrictMath.min(position,textLength-1) - beginningOfStringSegment;
				String initialContent= new String(characters,beginningOfStringSegment,segmentLength);
				buffer.append(initialContent);
			};
			segmentContainsExtraSymbols= true;
		};
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
			} else if (Character.isDigit(code)) {
				int beginningOfInternalNumericalLiteral= position;
				int beginningOfNumber= position;
				boolean lastCharacterIsSharp= false;
				boolean numberContainsExtraSymbols= false;
				StringBuilder auxiliaryBuffer= new StringBuilder();
				int radix= 10;
				BigInteger scale= BigInteger.ZERO;
				boolean afterDigit= false;
				boolean afterDot= false;
				try {
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: NUMERICAL LITERAL INSIDE STRING / SYMBOL (BEGINNING)         :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
ScanSignificand: while (true) { // Loop: Scan significand of the numerical token
	if (Character.isDigit(code)) {
		if (numberContainsExtraSymbols) {
			if (processSupplementaryCharacter) {
				auxiliaryBuffer.append(c1);
				auxiliaryBuffer.append(c2);
			} else {
				auxiliaryBuffer.append(c1);
			}
		};
		if (afterDot) {
			scale= scale.add(BigInteger.ONE);
		};
		afterDigit= true;
	} else if (code == '_') {
		if (!afterDigit) {
			if (!robustMode) {
				throw InternalNumericalLiteralIsNotRecognized.instance;
			}
		};
		if (!numberContainsExtraSymbols) {
			if (beginningOfNumber < position) {
				int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber;
				String initialContent= new String(characters,beginningOfNumber,numberLength);
				auxiliaryBuffer.append(initialContent);
			};
			numberContainsExtraSymbols= true;
		};
		afterDigit= false;
	} else if (afterDigit && !afterDot) {
		if (code == '#') {
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
		throw InternalNumericalLiteralIsNotRecognized.instance;
	}
} else {
	int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber - 1;
	String stringRepresentation= new String(characters,beginningOfNumber,numberLength);
	try {
		numericalValue= new BigInteger(stringRepresentation,radix);
	} catch (NumberFormatException e) {
		throw InternalNumericalLiteralIsNotRecognized.instance;
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
scale= BigInteger.ZERO;
afterDigit= false;
afterDot= false;
ScanExtendedNumber: while (true) { // Loop: Scan extended number
	if (Character.isDigit(code)) {
		if (numberContainsExtraSymbols) {
			if (processSupplementaryCharacter) {
				auxiliaryBuffer.append(c1);
				auxiliaryBuffer.append(c2);
			} else {
				auxiliaryBuffer.append(c1);
			}
		};
		if (afterDot) {
			scale= scale.add(BigInteger.ONE);
		};
		afterDigit= true;
	} else if (code >= 'a' && code <= 'z') {
		if (numberContainsExtraSymbols) {
			if (processSupplementaryCharacter) {
				auxiliaryBuffer.append(c1);
				auxiliaryBuffer.append(c2);
			} else {
				auxiliaryBuffer.append(c1);
			}
		};
		if (afterDot) {
			scale= scale.add(BigInteger.ONE);
		};
		afterDigit= true;
	} else if (code >= 'A' && code <= 'Z') {
		if (numberContainsExtraSymbols) {
			if (processSupplementaryCharacter) {
				auxiliaryBuffer.append(c1);
				auxiliaryBuffer.append(c2);
			} else {
				auxiliaryBuffer.append(c1);
			}
		};
		if (afterDot) {
			scale= scale.add(BigInteger.ONE);
		};
		afterDigit= true;
	} else if (code == '.') {
		if (!afterDigit || afterDot) {
			if (!robustMode) {
				throw InternalNumericalLiteralIsNotRecognized.instance;
			}
		};
		if (!numberContainsExtraSymbols) {
			if (beginningOfNumber < position) {
				int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber;
				String initialContent= new String(characters,beginningOfNumber,numberLength);
				buffer.append(initialContent);
			};
			numberContainsExtraSymbols= true;
		};
		afterDigit= false;
		afterDot= true;
	} else if (code == '_') {
		if (!afterDigit) {
			if (!robustMode) {
				throw InternalNumericalLiteralIsNotRecognized.instance;
			}
		};
		if (!numberContainsExtraSymbols) {
			if (beginningOfNumber < position) {
				int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber;
				String initialContent= new String(characters,beginningOfNumber,numberLength);
				auxiliaryBuffer.append(initialContent);
			};
			numberContainsExtraSymbols= true;
		};
		afterDigit= false;
	} else {
		if (code == '#') {
			lastCharacterIsSharp= true;
		} else {
			if (!robustMode) {
				throw InternalNumericalLiteralIsNotRecognized.instance;
			}
		};
		if (!afterDigit) {
			if (!robustMode) {
				throw InternalNumericalLiteralIsNotRecognized.instance;
			}
		};
		break ScanExtendedNumber;
	};
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
			if (!afterDigit) {
				if (!robustMode) {
					throw InternalNumericalLiteralIsNotRecognized.instance;
				}
			};
			break ScanSignificand;
		} else if (code == '.') {
			int p2;
			if (processSupplementaryCharacter) {
				p2= position + 2;
			} else {
				p2= position + 1;
			};
			if (p2 >= textLength) {
				if (processSupplementaryCharacter) {
					position= position - 2;
				} else {
					position--;
				};
				break ScanSignificand;
			} else {
				char nextC1= characters[p2];
				char nextC2;
				int nextCode;
				boolean nextProcessSupplementaryCharacter;
				if (	(p2 + 1 <= textLength - 1) &&
					Character.isSurrogatePair(nextC1,characters[p2+1])) {
					nextC2= characters[p2+1];
					nextCode= Character.toCodePoint(nextC1,nextC2);
					nextProcessSupplementaryCharacter= true;
				} else {
					nextC2= 0;
					nextCode= nextC1;
					nextProcessSupplementaryCharacter= false;
				};
				if (Character.isDigit(nextCode)) {
					if (!numberContainsExtraSymbols) {
						if (beginningOfNumber < position) {
							int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber;
							String initialContent= new String(characters,beginningOfNumber,numberLength);
							buffer.append(initialContent);
						};
						numberContainsExtraSymbols= true;
					};
					afterDigit= false;
					afterDot= true;
					position= p2;
					c1= nextC1;
					c2= nextC2;
					code= nextCode;
					processSupplementaryCharacter= nextProcessSupplementaryCharacter;
					continue ScanSignificand;
				} else {
					if (processSupplementaryCharacter) {
						position= position - 2;
					} else {
						position--;
					};
					break ScanSignificand;
				}
			}
		} else {
			if (processSupplementaryCharacter) {
				position= position - 2;
			} else {
				position--;
			};
			break ScanSignificand;
		}
	} else {
		if (!afterDigit) {
			if (!robustMode) {
				throw InternalNumericalLiteralIsNotRecognized.instance;
			}
		};
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
		break ScanSignificand;
	};
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position >= textLength) {
		if (!afterDigit) {
			if (!robustMode) {
				throw master.handleUnexpectedEndOfText(tokenPosition,iX);
			}
		};
		break ScanSignificand;
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
}; // End of loop: Scan significand of the numerical token
if (afterDot) {
	if (robustMode) {
		throw TokenIsNotRecognized.instance;
	} else {
		master.handleError(new RealNumberCannotBeACharacterCode(beginningOfInternalNumericalLiteral),iX);
	}
};
String stringRepresentation;
if (numberContainsExtraSymbols) {
	stringRepresentation= auxiliaryBuffer.toString();
} else {
	int numberLength= StrictMath.min(position,textLength-1) - beginningOfNumber + 1;
	if (lastCharacterIsSharp) {
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
	throw InternalNumericalLiteralIsNotRecognized.instance;
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
				} catch (InternalNumericalLiteralIsNotRecognized e) {
					buffer.append(Arrays.copyOfRange(characters,beginningOfInternalNumericalLiteral,position+1));
				}
			} else {
				if (code == '\n') {
					if (!robustMode) {
						master.handleError(new StringOrSymbolicLiteralIsNotTerminated(tokenPosition),iX);
					}
				};
				if (Character.isISOControl(code)) {
					if (!robustMode) {
						master.handleError(new ControlCharactersAreNotAllowedHere(position),iX);
					}
				};
				if (processSupplementaryCharacter) {
					buffer.append(c1);
					buffer.append(c2);
				} else {
					buffer.append(c1);
				}
			}
		}
	} else {
		if (code == '\n') {
			if (!robustMode) {
				master.handleError(new StringOrSymbolicLiteralIsNotTerminated(tokenPosition),iX);
			}
		};
		if (Character.isISOControl(code)) {
			if (!robustMode) {
				master.handleError(new ControlCharactersAreNotAllowedHere(position),iX);
			}
		};
		if (segmentContainsExtraSymbols) {
			if (processSupplementaryCharacter) {
				buffer.append(c1);
				buffer.append(c2);
			} else {
				buffer.append(c1);
			}
		}
	};
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
					int numberLength= StrictMath.min(position,textLength-1) - beginningOfStringSegment;
					stringRepresentation= new String(characters,beginningOfStringSegment,numberLength);
				};
				PrologToken textToken;
				if (isSymbol) {
					textToken= new TokenSymbol(SymbolNames.insertSymbolName(stringRepresentation),true,tokenPosition);
				} else {
					textToken= new TokenStringSegment(stringRepresentation,tokenPosition);
				};
				tokens.add(textToken);
			} else if (Character.isLetter(code) && Character.isLowerCase(code) ) {
				tokenPosition= position;
				afterComment= false;
				int beginningOfSymbol= position;
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: SYMBOL LITERAL (BEGINNING)                                   :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
boolean afterUnderscore= false;
int recentCode= code;
ScanSymbolToken: while (true) { // Loop: Scan symbol token
	if (Character.isLetterOrDigit(code)) {
		afterUnderscore= false;
		// Skip character
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
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
		afterUnderscore= false;
		break ScanSymbolToken;
	};
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position >= textLength) {
		break ScanSymbolToken;
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
	};
	recentCode= code;
}; // End of loop: Scan symbol
if (!robustMode) {
	if (recentCode=='_' && afterUnderscore) {
		master.handleError(new SymbolCannotBeCompletedByUnderscore(position),iX);
	}
};
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
int symbolLength= StrictMath.min(position,textLength-1) - beginningOfSymbol + 1;
String stringRepresentation= new String(characters,beginningOfSymbol,symbolLength);
boolean isKeyword= false;
if (lowerCaseNameHash != null) {
	String lowerCaseText= stringRepresentation.toLowerCase();
	if (lowerCaseNameHash.contains(lowerCaseText)) {
		isKeyword= true;
		if (keywordHash != null) {
			if (!keywordHash.contains(stringRepresentation)) {
				if (!robustMode) {
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
			} else if (code == '~') {
// ====================================================================
// === Binary                                                       ===
// ====================================================================
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
ScanExtendedNumber: while (true) { // Loop: Scan extended number
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
		if (!afterDigit) {
			if (!robustMode) {
				master.handleError(new UnderscoreCharacterIsNotAllowedHere(temporaryPosition),iX);
			}
		};
		afterDigit= false;
	} else {
		if (!afterDigit) {
			if (!robustMode) {
				master.handleError(new ExtendedDigitIsExpected(temporaryPosition),iX);
			}
		};
		break ScanExtendedNumber;
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
		break ScanExtendedNumber;
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
ScanExtendedNumber: while (true) { // Loop: Scan extended number
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
		if (!afterDigit) {
			if (!robustMode) {
				master.handleError(new UnderscoreCharacterIsNotAllowedHere(position),iX);
			}
		};
		afterDigit= false;
	} else {
		if (!afterDigit) {
			if (!robustMode) {
				master.handleError(new ExtendedDigitIsExpected(position),iX);
			}
		};
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
		break ScanExtendedNumber;
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
		break ScanExtendedNumber;
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
				tokens.add(new TokenBinarySegment(byteArray,tokenPosition));
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
ScanVariable: while (true) { // Loop: Scan variable / domain name token
	if (Character.isLetterOrDigit(code)) {
		afterUnderscore= false;
		// Skip character
	} else if (code == '_') {
		if (!robustMode) {
			if (afterUnderscore) {
				master.handleError(new DoubleUnderscoresAreNotAllowed(position),iX);
			}
		};
		afterUnderscore= true;
		// Skip character
	} else {
		if (processSupplementaryCharacter) {
			position= position - 2;
		} else {
			position--;
		};
		afterUnderscore= false;
		break ScanVariable;
	};
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position >= textLength) {
		break ScanVariable;
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
	};
	recentCode= code;
}; // End of loop: Scan variable / domain name token
int variableLength= StrictMath.min(position,textLength-1) - beginningOfVariable + 1;
if (!robustMode) {
	if (recentCode=='_' && variableLength > 1 && afterUnderscore) {
		master.handleError(new VariableCannotBeCompletedByUnderscore(position),iX);
	}
};
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
String stringRepresentation= new String(characters,beginningOfVariable,variableLength);
boolean isKeyword= false;
if (upperCaseNameHash != null) {
	String upperCaseText= stringRepresentation.toUpperCase();
	if (upperCaseNameHash.contains(upperCaseText)) {
		isKeyword= true;
		if (keywordHash != null) {
			if (!keywordHash.contains(stringRepresentation)) {
				if (!robustMode) {
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
				ScanMultilineComment: while (true) {
					if (processSupplementaryCharacter) {
						position= position + 2;
					} else {
						position++;
					};
					if (position + 1 < textLength) {
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
							break ScanMultilineComment;
						} else {
							continue ScanMultilineComment;
						}
					} else {
						if (!robustMode) {
							master.handleError(new MultilineCommentIsNotTerminated(tokenPosition),iX);
						};
						afterComment= true;
						break ScanMultilineComment;
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
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
tokenPosition= position;
if (!afterComment && tokens.size() > 0) {
	PrologToken previousToken= tokens.get(tokens.size()-1);
	if (previousToken.getType() == PrologTokenType.MINUS) {
		if (!robustMode) {
			master.handleError(new SingleLineCommentIsNotAllowedAfterMinusSign(tokenPosition),iX);
		}
	}
};
if (position > 0) {
	char previousCharacter1= characters[position-1];
	if (previousCharacter1==':') {
		if (!robustMode) {
			master.handleError(new SeparatorIsRequiredBeforeThisSingleLineComment(tokenPosition),iX);
		}
	} else if (previousCharacter1=='<') {
		if (!robustMode) {
			master.handleError(new SeparatorIsRequiredBeforeThisSingleLineComment(tokenPosition),iX);
		}
	} else {
		if (position > 1) {
			char previousCharacter2= characters[position-2];
			if (	previousCharacter2==':' &&
				previousCharacter1=='-') {
				if (!robustMode) {
					master.handleError(new SeparatorIsRequiredBeforeThisSingleLineComment(tokenPosition),iX);
				}
			} else if (	previousCharacter2=='<' &&
					previousCharacter1=='-') {
				if (!robustMode) {
					master.handleError(new SeparatorIsRequiredBeforeThisSingleLineComment(tokenPosition),iX);
				}
			}
		}
	}
};
ScanSingleLineComment: while (true) {
	if (processSupplementaryCharacter) {
		position= position + 2;
	} else {
		position++;
	};
	if (position < textLength) {
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
			afterComment= true;
			break ScanSingleLineComment;
		} else {
			continue ScanSingleLineComment;
		}
	} else {
		if (!robustMode) {
			master.handleError(new SingleLineCommentIsNotTerminatedByNewline(tokenPosition),iX);
		};
		afterComment= true;
		break ScanSingleLineComment;
	}
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
				}
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: SINGLE-LINE COMMENT (END)                                    :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// ::: PLAIN TOKENS (BEGINNING)                                     :::
// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			} else {
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
tokenPosition= position;
afterComment= false;
PrologTokenType plainTokenType;
PlainTokenType: switch (code) {
case ',':
	plainTokenType= PrologTokenType.COMMA;
	break PlainTokenType;
case '.':
	if (position + 1 < textLength) {
		char secondCharacter= characters[position+1];
		if (secondCharacter == '.') {
			position++;
			code= secondCharacter;
			plainTokenType= PrologTokenType.RANGE;
			break PlainTokenType;
		}
	};
	plainTokenType= PrologTokenType.DOT;
	break PlainTokenType;
case '!':
	plainTokenType= PrologTokenType.EXCLAM;
	break PlainTokenType;
case ':':
	if (position + 1 < textLength) {
		char secondCharacter= characters[position+1];
		if (secondCharacter == '-' && braceLevel <= 0) {
			position++;
			code= secondCharacter;
			plainTokenType= PrologTokenType.IMPLICATION;
			break PlainTokenType;
		} else if (secondCharacter == '=') {
			position++;
			code= secondCharacter;
			plainTokenType= PrologTokenType.ASSIGNMENT;
			break PlainTokenType;
		}
	};
	plainTokenType= PrologTokenType.COLON;
	break PlainTokenType;
case ';':
	plainTokenType= PrologTokenType.SEMICOLON;
	break PlainTokenType;
case '?':
	if (position + 1 < textLength) {
		char secondCharacter= characters[position+1];
		if (secondCharacter == '?') {
			position++;
			code= secondCharacter;
			plainTokenType= PrologTokenType.RESIDENT;
			break PlainTokenType;
		}
	};
	plainTokenType= PrologTokenType.QUESTION_MARK;
	break PlainTokenType;
case '#':
	plainTokenType= PrologTokenType.NUMBER_SIGN;
	break PlainTokenType;
case '(':
	plainTokenType= PrologTokenType.L_ROUND_BRACKET;
	break PlainTokenType;
case ')':
	plainTokenType= PrologTokenType.R_ROUND_BRACKET;
	break PlainTokenType;
case '|':
	plainTokenType= PrologTokenType.BAR;
	break PlainTokenType;
case '{':
	plainTokenType= PrologTokenType.L_BRACE;
	braceLevel++;
	break PlainTokenType;
case '}':
	plainTokenType= PrologTokenType.R_BRACE;
	braceLevel--;
	break PlainTokenType;
case '[':
	plainTokenType= PrologTokenType.L_SQUARE_BRACKET;
	break PlainTokenType;
case ']':
	plainTokenType= PrologTokenType.R_SQUARE_BRACKET;
	break PlainTokenType;
case '*':
	plainTokenType= PrologTokenType.MULTIPLY;
	break PlainTokenType;
case '+':
	plainTokenType= PrologTokenType.PLUS;
	break PlainTokenType;
case '-':
	if (position > 0) {
		char previousCharacter1= characters[position-1];
		if (previousCharacter1=='-') {
			if (!robustMode) {
				master.handleError(new SeparatorIsRequiredBeforeMinusSign(tokenPosition),iX);
			}
		}
	};
	plainTokenType= PrologTokenType.MINUS;
	break PlainTokenType;
case '/':
	plainTokenType= PrologTokenType.DIVIDE;
	break PlainTokenType;
case '<':
	if (position + 1 < textLength) {
		char secondCharacter= characters[position+1];
		if (secondCharacter == '<') {
			position++;
			code= secondCharacter;
			plainTokenType= PrologTokenType.DATA_MESSAGE;
			break PlainTokenType;
		} else if (secondCharacter == '-') {
			position++;
			code= secondCharacter;
			plainTokenType= PrologTokenType.CONTROL_MESSAGE;
			break PlainTokenType;
		} else if (secondCharacter == '>') {
			position++;
			code= secondCharacter;
			plainTokenType= PrologTokenType.NE;
			break PlainTokenType;
		} else if (secondCharacter == '=') {
			position++;
			code= secondCharacter;
			plainTokenType= PrologTokenType.LE;
			break PlainTokenType;
		}
	};
	plainTokenType= PrologTokenType.LT;
	break PlainTokenType;
case '=':
	if (position + 1 < textLength) {
		char secondCharacter= characters[position+1];
		if (secondCharacter == '=') {
			position++;
			code= secondCharacter;
			plainTokenType= PrologTokenType.EQUALITY;
			break PlainTokenType;
		}
	};
	plainTokenType= PrologTokenType.EQ;
	break PlainTokenType;
case '>':
	if (position + 1 < textLength) {
		char secondCharacter= characters[position+1];
		if (secondCharacter == '=') {
			position++;
			code= secondCharacter;
			plainTokenType= PrologTokenType.GE;
			break PlainTokenType;
		}
	};
	plainTokenType= PrologTokenType.GT;
	break PlainTokenType;
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
					tokens.add(new TokenPlain(PrologTokenType.END_OF_TEXT,position));
					break ExtractTokens;
				} else {
					c1= characters[position];
					if (	(position + 1 <= textLength - 1) &&
						Character.isSurrogatePair(c1,characters[position+1])) {
						c2= characters[position+1];
						code= Character.toCodePoint(c1,c2);
						processSupplementaryCharacter= true;
						tokens.add(new TokenStringSegment(new String(characters,position,2),position));
					} else {
						c2= 0;
						code= c1;
						processSupplementaryCharacter= false;
						tokens.add(new TokenStringSegment(new String(characters,position,1),position));
					}
				}
			};
			if (position >= textLength) {
				tokens.add(new TokenPlain(PrologTokenType.END_OF_TEXT,position));
				break ExtractTokens;
			} else {
				if (processSupplementaryCharacter) {
					position= position + 2;
				} else {
					position++;
				};
				if (position >= textLength) {
					tokens.add(new TokenPlain(PrologTokenType.END_OF_TEXT,position));
					break ExtractTokens;
				} else {
					if (extractFrontTokenOnly && tokens.size() > 0) {
						tokens.add(new TokenPlain(PrologTokenType.REST_OF_TEXT,position));
						break ExtractTokens;
					};
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
		return tokens.toArray(new PrologToken[tokens.size()]);
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
		ScanFrontSpaces: while (true) {
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
				continue ScanFrontSpaces;
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
				ScanSafeIdentifier: while (true) {
					if (p <= textLength - 1) {
						if ((p + 1 <= textLength - 1) && Character.isSurrogatePair(characters[p],characters[p+1])) {
							c= Character.toCodePoint(characters[p],characters[p+1]);
							p= p + 2;
						} else {
							c= characters[p];
							p++;
						};
						if (Character.isLetterOrDigit(c)) {
							continue ScanSafeIdentifier;
						} else if (c == '_') {
							continue ScanSafeIdentifier;
						} else if (Character.isIdentifierIgnorable(c)) {
							continue ScanSafeIdentifier;
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
				ScanIdentifier: while (true) {
					if (p <= textLength - 1) {
						if ((p + 1 <= textLength - 1) && Character.isSurrogatePair(characters[p],characters[p+1])) {
							c= Character.toCodePoint(characters[p],characters[p+1]);
							p= p + 2;
						} else {
							c= characters[p];
							p++;
						};
						if (Character.isLetterOrDigit(c)) {
							continue ScanIdentifier;
						} else if (c == '_') {
							continue ScanIdentifier;
						} else if (Character.isIdentifierIgnorable(c)) {
							continue ScanIdentifier;
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
