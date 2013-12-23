// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.run.*;
import morozov.syntax.scanner.*;
import morozov.system.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;

public class Converters {
	//
	public static final BigDecimal oneMilliBig	= BigDecimal.valueOf(1000);
	public static final BigDecimal oneMillionBig	= BigDecimal.valueOf(1_000_000);
	public static final BigDecimal oneMicroBig	= BigDecimal.valueOf(1_000_000);
	public static final BigDecimal oneNanoBig	= BigDecimal.valueOf(1_000_000_000);
	public static final BigDecimal onePicoBig	= BigDecimal.valueOf(1_000_000_000_000L);
	public static final BigDecimal oneFemtoBig	= BigDecimal.valueOf(1_000_000_000_000_000L);
	public static long oneDayLengthLong		= 86400000; // 24 * 60 * 60 * 1000;
	public static BigInteger oneDayLengthBigInteger	= BigInteger.valueOf(86400000);
	public static BigDecimal oneDayLengthBigDecimal	= BigDecimal.valueOf(86400000);
	protected static final BigDecimal lengthOfYear	= new BigDecimal(31556925.9747);
	protected static final BigDecimal big12		= new BigDecimal(12);
	//
	public static boolean term2OnOffDefault(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_on) {
				return true;
			} else if (code==SymbolCodes.symbolCode_E_off) {
				return false;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				throw TermIsSymbolDefault.instance;
			} else {
				throw new WrongTermIsNotTheOnOffDefaultSwitch(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongTermIsNotTheOnOffDefaultSwitch(value);
		}
	}
	//
	public static boolean term2OnOff(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_on) {
				return true;
			} else if (code==SymbolCodes.symbolCode_E_off) {
				return false;
			} else {
				throw new WrongTermIsNotTheOnOffSwitch(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongTermIsNotTheOnOffSwitch(value);
		}
	}
	//
	public static boolean term2YesNo(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_yes) {
				return true;
			} else if (code==SymbolCodes.symbolCode_E_no) {
				return false;
			} else {
				throw new WrongTermIsNotTheYesNoSwitch(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongTermIsNotTheYesNoSwitch(value);
		}
	}
	//
	public static Term boolean2YesNoTerm(boolean value) {
		if (value) {
			return new PrologSymbol(SymbolCodes.symbolCode_E_yes);
		} else {
			return new PrologSymbol(SymbolCodes.symbolCode_E_no);
		}
	}
	//
	public static boolean termToCollectingMode(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_set) {
				return true;
			} else if (code==SymbolCodes.symbolCode_E_bag) {
				return false;
			} else {
				throw new WrongTermIsNotCollectingMode(value);
			}
		} catch (TermIsNotASymbol e) {
			throw new WrongTermIsNotCollectingMode(value);
		}
	}
	//
	public static BigInteger termToStrictInteger(Term value, ChoisePoint iX, boolean acceptDateAndTime) throws TermIsNotAnInteger {
		try {
			return value.getIntegerValue(iX);
		} catch (TermIsNotAnInteger b1) {
			try {
				String text= value.getStringValue(iX);
				return stringToStrictInteger(text);
			} catch (TermIsNotAString b2) {
				if (acceptDateAndTime) {
					return termToDateOrTime(value,iX);
				} else {
					throw TermIsNotAnInteger.instance;
				}
			}
		}
	}
	public static BigInteger termToRoundInteger(Term value, ChoisePoint iX, boolean acceptDateAndTime) throws TermIsNotAnInteger {
		BigInteger result;
		try {
			result= termToStrictInteger(value,iX,acceptDateAndTime);
		} catch (TermIsNotAnInteger b1) {
			try {
				result= Converters.doubleToBigInteger(value.getRealValue(iX));
			} catch (TermIsNotAReal b2) {
				try {
					String text= value.getStringValue(iX);
					result= stringToRoundInteger(text);
				} catch (TermIsNotAString b3) {
					throw TermIsNotAnInteger.instance;
				}
			}
		};
		return result;
	}
	//
	public static BigInteger argumentToRoundInteger(Term value, ChoisePoint iX) {
		try {
			return termToRoundInteger(value,iX,false);
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(value);
		}
	}
	//
	public static int argumentToSmallRoundInteger(Term value, ChoisePoint iX) {
		try {
			return PrologInteger.toInteger(termToRoundInteger(value,iX,false));
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(value);
		}
	}
	//
	public static BigInteger stringToStrictInteger(String text) throws TermIsNotAnInteger {
		BigInteger result;
		try {
			result= new BigInteger(text);
		} catch (NumberFormatException nfe) {
			LexicalScanner scanner= new LexicalScanner(true);
			PrologToken[] tokens= scanner.analyse(text);
			if (tokens.length==2) {
				if (tokens[0].getType()==PrologTokenType.INTEGER) {
					result= tokens[0].getIntegerValue();
				} else {
					throw TermIsNotAnInteger.instance;
				}
			} else if (tokens.length==3) {
				if (tokens[0].getType()==PrologTokenType.MINUS) {
					if (tokens[1].getType()==PrologTokenType.INTEGER) {
						result= tokens[1].getIntegerValue().negate();
					} else {
						throw TermIsNotAnInteger.instance;
					}
				} else if (tokens[0].getType()==PrologTokenType.PLUS) {
					if (tokens[1].getType()==PrologTokenType.INTEGER) {
						result= tokens[1].getIntegerValue();
					} else {
						throw TermIsNotAnInteger.instance;
					}
				} else {
					throw TermIsNotAnInteger.instance;
				}
			} else {
				throw TermIsNotAnInteger.instance;
			}
		};
		return result;
	}
	public static BigInteger stringToRoundInteger(String text) throws TermIsNotAnInteger {
		// String text= value.getStringValue(iX);
		BigInteger result;
		try {
			result= new BigInteger(text);
		} catch (NumberFormatException nfe) {
			LexicalScanner scanner= new LexicalScanner(true);
			PrologToken[] tokens= scanner.analyse(text);
			if (tokens.length==2) {
				if (tokens[0].getType()==PrologTokenType.INTEGER) {
					result= tokens[0].getIntegerValue();
				} else if (tokens[0].getType()==PrologTokenType.REAL) {
					double realValue= tokens[0].getRealValue();
					try {
						result= Converters.doubleToBigInteger(realValue);
					} catch (TermIsNotAReal b3) {
						throw TermIsNotAnInteger.instance;
					}
				} else {
					throw TermIsNotAnInteger.instance;
				}
			} else if (tokens.length==3) {
				if (tokens[0].getType()==PrologTokenType.MINUS) {
					if (tokens[1].getType()==PrologTokenType.INTEGER) {
						result= tokens[1].getIntegerValue().negate();
					} else if (tokens[1].getType()==PrologTokenType.REAL) {
						double realValue= tokens[1].getRealValue();
						try {
							result= Converters.doubleToBigInteger(realValue).negate();
						} catch (TermIsNotAReal b3) {
							throw TermIsNotAnInteger.instance;
						}
					} else {
						throw TermIsNotAnInteger.instance;
					}
				} else if (tokens[0].getType()==PrologTokenType.PLUS) {
					if (tokens[1].getType()==PrologTokenType.INTEGER) {
						result= tokens[1].getIntegerValue();
					} else if (tokens[1].getType()==PrologTokenType.REAL) {
						double realValue= tokens[1].getRealValue();
						try {
							result= Converters.doubleToBigInteger(realValue);
						} catch (TermIsNotAReal b3) {
							throw TermIsNotAnInteger.instance;
						}
					} else {
						throw TermIsNotAnInteger.instance;
					}
				} else {
					throw TermIsNotAnInteger.instance;
				}
			} else {
				throw TermIsNotAnInteger.instance;
			}
		};
		return result;
	}
	//
	public static long termSecondsToMilliseconds(Term value, ChoisePoint iX) {
		try {
			BigDecimal n= termToTimeInterval(value,iX).divide(oneMillionBig,MathContext.DECIMAL128);
			n= n.round(MathContext.DECIMAL128);
			return PrologInteger.toLong(n);
			// BigInteger bi= value.getIntegerValue(iX).multiply(oneThousand);
			// delay= PrologInteger.toLong(bi);
		} catch (TermIsNotTimeInterval a1) {
			throw new WrongArgumentIsNotTimeInterval(value);
		}
	}
	public static long termMillisecondsToMilliseconds(Term value, ChoisePoint iX) {
		try {
			BigDecimal n= termToTimeInterval(value,oneMilliBig,iX).divide(oneMillionBig,MathContext.DECIMAL128);
			n= n.round(MathContext.DECIMAL128);
			return PrologInteger.toLong(n);
			// BigInteger bi= value.getIntegerValue(iX).multiply(oneThousand);
			// delay= PrologInteger.toLong(bi);
		} catch (TermIsNotTimeInterval a1) {
			throw new WrongArgumentIsNotTimeInterval(value);
		}
	}
	//
	public static BigDecimal termToTimeInterval(Term value, ChoisePoint iX) throws TermIsNotTimeInterval {
		return termToTimeInterval(value,BigDecimal.ONE,iX);
	}
	public static BigDecimal termToTimeInterval(Term value, BigDecimal k1, ChoisePoint iX) throws TermIsNotTimeInterval {
		try {
			return termToNanosDivided(value,k1,iX);
		} catch (TermIsNotTimeInterval e1) {
			try {
				long functor= value.getStructureFunctor(iX);
				Term[] arguments= value.getStructureArguments(iX);
				if (arguments.length > 1 || arguments.length < 1) {
					throw TermIsNotTimeInterval.instance;
				};
				if (functor == SymbolCodes.symbolCode_E_seconds) {
					return termToNanosMultiplied(arguments[0],1,iX);
				} else if (functor == SymbolCodes.symbolCode_E_milliseconds) {
					return termToNanosDivided(arguments[0],oneMilliBig,iX);
				} else if (functor == SymbolCodes.symbolCode_E_microseconds) {
					return termToNanosDivided(arguments[0],oneMicroBig,iX);
				} else if (functor == SymbolCodes.symbolCode_E_nanoseconds) {
					return termToNanosDivided(arguments[0],oneNanoBig,iX);
				} else if (functor == SymbolCodes.symbolCode_E_picoseconds) {
					return termToNanosDivided(arguments[0],onePicoBig,iX);
				} else if (functor == SymbolCodes.symbolCode_E_femtoseconds) {
					return termToNanosDivided(arguments[0],oneFemtoBig,iX);
				} else if (functor == SymbolCodes.symbolCode_E_minutes) {
					return termToNanosMultiplied(arguments[0],60,iX);
				} else if (functor == SymbolCodes.symbolCode_E_hours) {
					return termToNanosMultiplied(arguments[0],3600,iX);
				} else if (functor == SymbolCodes.symbolCode_E_days) {
					return termToNanosMultiplied(arguments[0],86400,iX);
				} else if (functor == SymbolCodes.symbolCode_E_weeks) {
					return termToNanosMultiplied(arguments[0],604800,iX);
				} else if (functor == SymbolCodes.symbolCode_E_months) {
					BigDecimal k2= lengthOfYear.divide(big12,MathContext.DECIMAL128);
					return termToNanosMultiplied(arguments[0],k2,iX);
				} else if (functor == SymbolCodes.symbolCode_E_years) {
					return termToNanosMultiplied(arguments[0],lengthOfYear,iX);
				} else {
					throw TermIsNotTimeInterval.instance;
				}
			} catch (TermIsNotAStructure e2) {
				throw TermIsNotTimeInterval.instance;
			}
		}
	}
	//
	public static BigDecimal termToNanosMultiplied(Term value, long k, ChoisePoint iX) throws TermIsNotTimeInterval {
		BigDecimal result;
		try {
			result= new BigDecimal(value.getIntegerValue(iX));
			result= result.multiply(oneNanoBig).multiply(BigDecimal.valueOf(k));
			return result;
		} catch (TermIsNotAnInteger b1) {
			try {
				result= Converters.doubleToBigDecimal(value.getRealValue(iX));
				result= result.multiply(oneNanoBig).multiply(BigDecimal.valueOf(k));
				return result;
			} catch (TermIsNotAReal b2) {
				throw TermIsNotTimeInterval.instance;
			}
		}
	}
	public static BigDecimal termToNanosMultiplied(Term value, BigDecimal k, ChoisePoint iX) throws TermIsNotTimeInterval {
		BigDecimal result;
		try {
			result= new BigDecimal(value.getIntegerValue(iX));
			result= result.multiply(oneNanoBig).multiply(k);
			return result;
		} catch (TermIsNotAnInteger b1) {
			try {
				result= Converters.doubleToBigDecimal(value.getRealValue(iX));
				result= result.multiply(oneNanoBig).multiply(k);
				return result;
			} catch (TermIsNotAReal b2) {
				throw TermIsNotTimeInterval.instance;
			}
		}
	}
	public static BigDecimal termToNanosDivided(Term value, BigDecimal k, ChoisePoint iX) throws TermIsNotTimeInterval {
		BigDecimal result;
		try {
			result= new BigDecimal(value.getIntegerValue(iX));
			result= result.multiply(oneNanoBig).divide(k,MathContext.DECIMAL128);
			return result;
		} catch (TermIsNotAnInteger b1) {
			try {
				result= Converters.doubleToBigDecimal(value.getRealValue(iX));
				result= result.multiply(oneNanoBig).divide(k,MathContext.DECIMAL128);
				return result;
			} catch (TermIsNotAReal b2) {
				throw TermIsNotTimeInterval.instance;
			}
		}
	}
	//
	public static BigInteger termToDateOrTime(Term value, ChoisePoint iX) throws TermIsNotAnInteger {
		try {
			return termToDate(value,iX);
		} catch (TermIsNotADate b1) {
			try {
				return termToTime(value,iX);
			} catch (TermIsNotATime b2) {
				throw TermIsNotAnInteger.instance;
			}
		}
	}
	public static BigInteger termToDate(Term value, ChoisePoint iX) throws TermIsNotADate {
		try {
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_date,3,iX);
			long timeInMillis= termsToDate(arguments[0],arguments[1],arguments[2],value,iX);
			return BigInteger.valueOf(timeInMillis);
		} catch (Backtracking b) {
			throw TermIsNotADate.instance;
		}
	}
	public static long termsToDate(Term a1, Term a2, Term a3, Term term, ChoisePoint iX) throws TermIsNotADate {
		try {
			int year= a1.getSmallIntegerValue(iX);
			int month= monthToInteger(a2,iX); // .getSmallIntegerValue(iX);
			int day= a3.getSmallIntegerValue(iX);
			Calendar calendar= Calendar.getInstance();
			calendar.set(year,month-1,day);
			long timeInMillis= calendar.getTimeInMillis();
			return timeInMillis;
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotADate(term);
		} catch (Backtracking b) {
			throw TermIsNotADate.instance;
		}
	}
	public static long argumentsToDate(Term a1, Term a2, Term a3, Term term, ChoisePoint iX) {
		try {
			return termsToDate(a1,a2,a3,term,iX);
		} catch (TermIsNotADate e) {
			throw new WrongArgumentIsNotADate(term);
		}
	}
	public static BigInteger termToTime(Term value, ChoisePoint iX) throws TermIsNotATime {
		try {
			try {
				Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_time,4,iX);
				long timeInMillis= termsToTime(arguments[0],arguments[1],arguments[2],arguments[3],value,iX);
				return BigInteger.valueOf(timeInMillis);
			} catch (Backtracking b2) {
				Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_time,3,iX);
				long timeInMillis= termsToTime(arguments[0],arguments[1],arguments[2],value,iX);
				return BigInteger.valueOf(timeInMillis);
			}
		} catch (Backtracking b1) {
			throw TermIsNotATime.instance;
		}
	}
	public static long termsToTime(Term a1, Term a2, Term a3, Term a4, Term term, ChoisePoint iX) throws TermIsNotATime {
		try {
			int hours= a1.getSmallIntegerValue(iX);
			int minutes= a2.getSmallIntegerValue(iX);
			int seconds= a3.getSmallIntegerValue(iX);
			int milliseconds= a4.getSmallIntegerValue(iX);
			Calendar calendar= Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY,hours);
			calendar.set(Calendar.MINUTE,minutes);
			calendar.set(Calendar.SECOND,seconds);
			calendar.set(Calendar.MILLISECOND,milliseconds);
			return calendar.getTimeInMillis();
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotATime(term);
		}
	}
	public static long termsToTime(Term a1, Term a2, Term a3, Term term, ChoisePoint iX) throws TermIsNotATime {
		try {
			int hours= a1.getSmallIntegerValue(iX);
			int minutes= a2.getSmallIntegerValue(iX);
			int seconds= a3.getSmallIntegerValue(iX);
			int milliseconds= 0; // arguments[3].getSmallIntegerValue(iX);
			Calendar calendar= Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY,hours);
			calendar.set(Calendar.MINUTE,minutes);
			calendar.set(Calendar.SECOND,seconds);
			calendar.set(Calendar.MILLISECOND,milliseconds);
			return calendar.getTimeInMillis();
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotATime(term);
		}
	}
	public static long argumentsToTime(Term a1, Term a2, Term a3, Term a4, Term term, ChoisePoint iX) {
		try {
			return termsToTime(a1,a2,a3,a4,term,iX);
		} catch (TermIsNotATime e) {
			throw new WrongArgumentIsNotATime(term);
		}
	}
	public static long argumentsToTime(Term a1, Term a2, Term a3, Term term, ChoisePoint iX) {
		try {
			return termsToTime(a1,a2,a3,term,iX);
		} catch (TermIsNotATime e) {
			throw new WrongArgumentIsNotATime(term);
		}
	}
	//
	public static Term integerToDate(BigInteger value) {
		Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(PrologInteger.toLong(value));
		int year= calendar.get(Calendar.YEAR);
		int month= calendar.get(Calendar.MONTH) + 1;
		int day= calendar.get(Calendar.DAY_OF_MONTH);
		Term[] arguments= new Term[3];
		arguments[0]= new PrologInteger(BigInteger.valueOf(year));
		arguments[1]= new PrologInteger(BigInteger.valueOf(month));
		arguments[2]= new PrologInteger(BigInteger.valueOf(day));
		return new PrologStructure(SymbolCodes.symbolCode_E_date,arguments);
	}
	public static Term integerToTime(BigInteger value) {
		Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(PrologInteger.toLong(value));
		int hours= calendar.get(Calendar.HOUR_OF_DAY);
		int minutes= calendar.get(Calendar.MINUTE);
		int seconds= calendar.get(Calendar.SECOND);
		int milliseconds= calendar.get(Calendar.MILLISECOND);
		Term[] arguments= new Term[4];
		arguments[0]= new PrologInteger(BigInteger.valueOf(hours));
		arguments[1]= new PrologInteger(BigInteger.valueOf(minutes));
		arguments[2]= new PrologInteger(BigInteger.valueOf(seconds));
		arguments[3]= new PrologInteger(BigInteger.valueOf(milliseconds));
		return new PrologStructure(SymbolCodes.symbolCode_E_time,arguments);
	}
	public static Term integerToDays(BigInteger timeInMillis) {
		// return new PrologInteger(BigInteger.valueOf((long)StrictMath.round(timeInMillis.doubleValue()/oneDayLengthLong)));
		BigDecimal interval= (new BigDecimal(timeInMillis)).divide(oneDayLengthBigDecimal,MathContext.DECIMAL128);
		return new PrologInteger(interval.round(MathContext.DECIMAL128).toBigInteger());
	}
	//
	public static BigInteger doubleToBigInteger(double value) throws TermIsNotAReal {
		value= StrictMath.round(value);
		String text= String.format("%1f",value);
		int indexBound= text.length() - 1;
		int p1= 0;
		while(true) {
			if (p1 <= indexBound) {
				int code= text.codePointAt(p1);
				if (code >= '0' && code <= '9') {
					p1++;
					continue;
				} else if (code=='-' || code=='+') {
					p1++;
					continue;
				} else {
					break;
				}
			} else {
				break;
			}
		};
		try {
			return new BigInteger(text.substring(0,p1));
		} catch (NumberFormatException nfe) {
			throw TermIsNotAReal.instance;
		}
	}
	public static BigInteger doubleArgumentToBigInteger(double value, Term term) {
		try {
			return doubleToBigInteger(value);
		} catch (TermIsNotAReal e) {
			throw new WrongArgumentIsNotAReal(term);
		}
	}
	public static BigInteger doubleValueToBigInteger(double value) {
		try {
			return doubleToBigInteger(value);
		} catch (TermIsNotAReal e) {
			throw new WrongArgumentIsNotAReal(new PrologReal(value));
		}
	}
	public static BigDecimal doubleToBigDecimal(double value) throws TermIsNotAReal {
		try {
			return new BigDecimal(value);
		} catch (NumberFormatException nfe) {
			throw TermIsNotAReal.instance;
		}
	}
	//
	public static double termToReal(Term value, ChoisePoint iX) throws TermIsNotAReal {
		double result;
		try {
			result= value.getRealValue(iX);
		} catch (TermIsNotAReal b1) {
			try {
				BigInteger number= value.getIntegerValue(iX);
				result= number.doubleValue();
			} catch (TermIsNotAnInteger b2) {
				try {
					String text= value.getStringValue(iX);
					result= stringToReal(text);
				} catch (TermIsNotAString b5) {
					throw TermIsNotAReal.instance;
				}
			}
		};
		return result;
	}
	//
	public static double argumentToReal(Term value, ChoisePoint iX) {
		try {
			return Converters.termToReal(value,iX);
		} catch (TermIsNotAReal e) {
			throw new WrongArgumentIsNotNumeric(value);
		}
	}
	//
	public static double stringToReal(String text) throws TermIsNotAReal {
		double result;
		// try {
		//	result= new Double(text);
		// } catch (NumberFormatException nfe) {
			LexicalScanner scanner= new LexicalScanner(true);
			PrologToken[] tokens= scanner.analyse(text);
			if (tokens.length==2) {
				if (tokens[0].getType()==PrologTokenType.INTEGER) {
					result= tokens[0].getIntegerValue().doubleValue();
				} else if (tokens[0].getType()==PrologTokenType.REAL) {
					result= tokens[0].getRealValue();
				} else {
					throw TermIsNotAReal.instance;
				}
			} else if (tokens.length==3) {
				if (tokens[0].getType()==PrologTokenType.MINUS) {
					if (tokens[1].getType()==PrologTokenType.INTEGER) {
						result= tokens[1].getIntegerValue().negate().doubleValue();
					} else if (tokens[1].getType()==PrologTokenType.REAL) {
						result= - tokens[1].getRealValue();
					} else {
						throw TermIsNotAReal.instance;
					}
				} else if (tokens[0].getType()==PrologTokenType.PLUS) {
					if (tokens[1].getType()==PrologTokenType.INTEGER) {
						result= tokens[1].getIntegerValue().doubleValue();
					} else if (tokens[1].getType()==PrologTokenType.REAL) {
						result= tokens[1].getRealValue();
					} else {
						throw TermIsNotAReal.instance;
					}
				} else {
					throw TermIsNotAReal.instance;
				}
			} else {
				throw TermIsNotAReal.instance;
			};
		// };
		return result;
	}
	//
	public static Term termToNumerical(Term value, ChoisePoint iX, boolean acceptDateAndTime) throws Backtracking {
		Term result;
		try {
			value.getIntegerValue(iX);
			result= value;
		} catch (TermIsNotAnInteger b1) {
			try {
				value.getRealValue(iX);
				result= value;
			} catch (TermIsNotAReal b2) {
				try {
					result= new PrologInteger(termToDateOrTime(value,iX));
				} catch (TermIsNotAnInteger b3) {
					try {
						String text= value.getStringValue(iX);
						result= stringToNumerical(text);
					} catch (TermIsNotAString b4) {
						throw Backtracking.instance;
					}
				}
			}
		};
		return result;
	}
	//
	public static Term stringToNumerical(String text) throws Backtracking {
		Term result;
		try {
			result= new PrologInteger(new BigInteger(text));
		} catch (NumberFormatException nfe) {
			LexicalScanner scanner= new LexicalScanner(true);
			PrologToken[] tokens= scanner.analyse(text);
			if (tokens.length==2) {
				if (tokens[0].getType()==PrologTokenType.INTEGER) {
					result= new PrologInteger(tokens[0].getIntegerValue());
				} else if (tokens[0].getType()==PrologTokenType.REAL) {
					result= new PrologReal(tokens[0].getRealValue());
				} else {
					// throw new TermIsNotNumerical();
					throw Backtracking.instance;
				}
			} else if (tokens.length==3) {
				if (tokens[0].getType()==PrologTokenType.MINUS) {
					if (tokens[1].getType()==PrologTokenType.INTEGER) {
						result= new PrologInteger(tokens[1].getIntegerValue().negate());
					} else if (tokens[1].getType()==PrologTokenType.REAL) {
						result= new PrologReal(-tokens[1].getRealValue());
					} else {
						// throw new TermIsNotNumerical();
						throw Backtracking.instance;
					}
				} else if (tokens[0].getType()==PrologTokenType.PLUS) {
					if (tokens[1].getType()==PrologTokenType.INTEGER) {
						result= new PrologInteger(tokens[1].getIntegerValue());
					} else if (tokens[1].getType()==PrologTokenType.REAL) {
						result= new PrologReal(tokens[1].getRealValue());
					} else {
						// throw new TermIsNotNumerical();
						throw Backtracking.instance;
					}
				} else {
					// throw new TermIsNotNumerical();
					throw Backtracking.instance;
				}
			} else {
				// throw new TermIsNotNumerical();
				throw Backtracking.instance;
			}
		};
		return result;
	}
	//
	public static int monthToInteger(Term argument, ChoisePoint iX) throws Backtracking {
		try {
			return argument.getSmallIntegerValue(iX);
		} catch (TermIsNotAnInteger e1) {
			try {
				String name= argument.getStringValue(iX);
				name= name.toUpperCase();
				int month;
				if ("JANUARY".startsWith(name)) {
					month= 1;
				} else if ("FEBRUARY".startsWith(name)) {
					month= 2;
				} else if ("MARCH".startsWith(name)) {
					month= 3;
				} else if ("APRIL".startsWith(name)) {
					month= 4;
				} else if ("MAY".startsWith(name)) {
					month= 5;
				} else if ("JUNE".startsWith(name)) {
					month= 6;
				} else if ("JULY".startsWith(name)) {
					month= 7;
				} else if ("AUGUST".startsWith(name)) {
					month= 8;
				} else if ("SEPTEMBER".startsWith(name)) {
					month= 9;
				} else if ("OCTOBER".startsWith(name)) {
					month= 10;
				} else if ("NOVEMBER".startsWith(name)) {
					month= 11;
				} else if ("DECEMBER".startsWith(name)) {
					month= 12;
				} else {
					throw Backtracking.instance;
				};
				return month;
			} catch (TermIsNotAString e2) {
				throw Backtracking.instance;
			}
		}
	}
	//
	public static String concatenateStringList(Term value, String infix, ChoisePoint iX) {
		StringBuilder buffer= new StringBuilder();
		try {
			boolean insertInfix= false;
			while (true) {
				Term head= value.getNextListHead(iX);
				if (insertInfix) {
					buffer.append(infix);
				} else {
					insertInfix= true;
				};
				buffer.append(head.getStringValue(iX));
				value= value.getNextListTail(iX);
			}
		} catch (EndOfList e1) {
			return buffer.toString();
		} catch (TermIsNotAList e2) {
			throw new WrongArgumentIsNotAList(value);
		} catch (TermIsNotAString e3) {
			throw new WrongArgumentIsNotAString(value);
		}
	}
	//
	public static String[] termToStrings(Term value, ChoisePoint iX) {
		return termToStrings(value,iX,false);
	}
	public static String[] termToStrings(Term value, ChoisePoint iX, boolean makeUpperCaseStrings) {
		ArrayList<String> stringList= new ArrayList<String>();
		termToStrings(stringList,value,iX,makeUpperCaseStrings);
		return stringList.toArray(new String[0]);
	}
	public static void termToStrings(ArrayList<String> stringList, Term value, ChoisePoint iX, boolean makeUpperCaseStrings) {
		Term nextHead;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				termToStrings(stringList,nextHead,iX,makeUpperCaseStrings);
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			String string= currentTail.toString(iX);
			if (makeUpperCaseStrings) {
				string= string.toUpperCase();
			};
			stringList.add(string);
		} catch (Throwable e) {
		}
	}
	//
	public static BigInteger[] termToIntegers(Term value, ChoisePoint iX) {
		ArrayList<BigInteger> integerList= new ArrayList<BigInteger>();
		termToIntegers(integerList,value,iX);
		return integerList.toArray(new BigInteger[0]);
	}
	public static void termToIntegers(ArrayList<BigInteger> integerList, Term value, ChoisePoint iX) {
		Term nextHead;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				termToIntegers(integerList,nextHead,iX);
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e1) {
		} catch (TermIsNotAList e1) {
			try {
				BigInteger result= termToRoundInteger(currentTail,iX,true);
				integerList.add(result);
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotAnInteger(currentTail);
			}
		} catch (Throwable e1) {
			throw new WrongArgumentIsNotIntegerList(currentTail);
		}
	}
	//
	public static String codesToString(BigInteger[] numbers) {
		char[] codes= new char[numbers.length];
		for (int n=0; n < numbers.length; n++) {
			codes[n]= PrologInteger.toCharacter(numbers[n]);
		};
		return new String(codes);
	}
	//
	public static Term[] listToArray(Term tail, ChoisePoint iX) {
		ArrayList<Term> buffer= new ArrayList<Term>();
		try {
			while(true) {
				Term value= tail.getNextListHead(iX);
				buffer.add(value);
				tail= tail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
			return buffer.toArray(new Term[buffer.size()]);
		} catch (TermIsNotAList e) {
			throw new WrongArgumentIsNotAList(tail);
		}
	}
	//
	public static Term arrayToList(Term[] array) {
		Term buffer= PrologEmptyList.instance;
		for (int n=array.length-1; n >= 0; n--) {
			buffer= new PrologList(array[n],buffer);
		};
		return buffer;
	}
	//
	public static Term arrayListToTerm(ArrayList<Term> array) {
		Term result= PrologEmptyList.instance;
		for (int n=array.size()-1; n >= 0; n--) {
			result= new PrologList(array.get(n),result);
		};
		return result;
	}
	//
	public static Term stringArrayToList(ArrayList<String> array) {
		Term result= PrologEmptyList.instance;
		for (int n=array.size()-1; n >= 0; n--) {
			result= new PrologList(new PrologString(array.get(n)),result);
		};
		return result;
	}
	public static Term stringArrayToListOfList(ArrayList<ArrayList<String>> array) {
		Term result= PrologEmptyList.instance;
		for (int n=array.size()-1; n >= 0; n--) {
			result= new PrologList(stringArrayToList(array.get(n)),result);
		};
		return result;
	}
	//
	public static Term[] termsToArray(ChoisePoint iX, Term... args) {
		ArrayList<Term> argumentList= new ArrayList<Term>();
		for(int i= 0; i < args.length; i++) {
			Term item= args[i];
			if (item.thisIsArgumentNumber()) {
				Term extraItems= args[i+1];
				for (int j= 0; j < item.getNumber(); j++) {
					if (j > 0) {
						extraItems= extraItems.getExistentTail();
					};
					Term extraItem= extraItems.getExistentHead();
					argumentList.add(extraItem);
				};
				break;
			} else {
				argumentList.add(item);
			}
		};
		return argumentList.toArray(new Term[0]);
	}
	//
	public static double[][] argumentToMatrix(Term value, ChoisePoint iX) {
		Term[] termArray= Converters.listToArray(value,iX);
		int numberOfRows= termArray.length;
		double[][] rows= new double[numberOfRows][];
		int numberOfColumns= -1;
		for (int n=0; n < numberOfRows; n++) {
			Term[] columnArray= Converters.listToArray(termArray[n],iX);
			if (numberOfColumns >= 0) {
				if (numberOfColumns != columnArray.length) {
					throw new WrongArgumentIsNotAMatrix(value);
				}
			} else {
				numberOfColumns= columnArray.length;
			};
			rows[n]= new double[numberOfColumns];
			for (int m=0; m < numberOfColumns; m++) {
				rows[n][m]= Converters.argumentToReal(columnArray[m],iX);
			}
		};
		return rows;
	}
	//
	public static Term doubleMatrixToListOfList(double[][] matrix) {
		Term result= PrologEmptyList.instance;
		for (int n=matrix.length-1; n >= 0; n--) {
			result= new PrologList(doubleArrayToList(matrix[n]),result);
		};
		return result;
	}
	public static Term doubleArrayToList(double[] array) {
		Term result= PrologEmptyList.instance;
		for (int n=array.length-1; n >= 0; n--) {
			result= new PrologList(new PrologReal(array[n]),result);
		};
		return result;
	}
	//
	public static int termToProcessPriority(Term value, ChoisePoint iX) throws TermIsNotProcessPriority {
		try {
			int priority= value.getSmallIntegerValue(iX);
			priority= StrictMath.max(priority,Thread.MIN_PRIORITY);
			priority= StrictMath.min(priority,Thread.MAX_PRIORITY);
			return priority;
		} catch (TermIsNotAnInteger e1) {
			try {
				long functor= value.getSymbolValue(iX);
				if (functor == SymbolCodes.symbolCode_E_NORM_PRIORITY) {
					return Thread.NORM_PRIORITY;
				} else if (functor == SymbolCodes.symbolCode_E_MIN_PRIORITY) {
					return Thread.MIN_PRIORITY;
				} else if (functor == SymbolCodes.symbolCode_E_MAX_PRIORITY) {
					return Thread.MAX_PRIORITY;
				} else {
					throw TermIsNotProcessPriority.instance;
				}
			} catch (TermIsNotASymbol e2) {
				throw TermIsNotProcessPriority.instance;
			}
		}
	}
}
