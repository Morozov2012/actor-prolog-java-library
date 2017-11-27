// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.domains.*;
import morozov.domains.errors.*;
import morozov.run.*;
import morozov.system.errors.*;
import morozov.system.datum.*;
import morozov.syntax.scanner.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;

import java.awt.Dimension;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Calendar;

public class Converters {
	//
	protected static Term termNormal = new PrologSymbol(SymbolCodes.symbolCode_E_normal);
	protected static Term termMinimal = new PrologSymbol(SymbolCodes.symbolCode_E_minimal);
	protected static Term termMaximal = new PrologSymbol(SymbolCodes.symbolCode_E_maximal);
	//
	///////////////////////////////////////////////////////////////
	//
	public static BigInteger argumentToStrictInteger(Term value, ChoisePoint iX) {
		try {
			return termToStrictInteger(value,iX,false);
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(value);
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
					return termToDateOrTimeInMilliseconds(value,iX);
				} else {
					throw TermIsNotAnInteger.instance;
				}
			}
		}
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
	public static int argumentToSmallInteger(Term value, ChoisePoint iX) {
		try {
			return value.getSmallIntegerValue(iX);
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
	//
	public static long argumentToLongInteger(Term value, ChoisePoint iX) {
		try {
			return value.getLongIntegerValue(iX);
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotAnInteger(value);
		}
	}
	//
	public static BigInteger stringToRoundInteger(String text) throws TermIsNotAnInteger {
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
	///////////////////////////////////////////////////////////////
	//
	public static BigInteger termToDateOrTimeInMilliseconds(Term value, ChoisePoint iX) throws TermIsNotAnInteger {
		try {
			return termToDateInMilliseconds(value,iX);
		} catch (TermIsNotADate b1) {
			try {
				return termToTimeInMilliseconds(value,iX);
			} catch (TermIsNotATime b2) {
				throw TermIsNotAnInteger.instance;
			}
		}
	}
	//
	public static BigInteger argumentToDateInMilliseconds(Term value, ChoisePoint iX) {
		try {
			return termToDateInMilliseconds(value,iX);
		} catch (TermIsNotADate e) {
			throw new WrongArgumentIsNotADate(value);
		}
	}
	public static void argumentToDateInMilliseconds(Term value, Calendar calendar, ChoisePoint iX) {
		try {
			termToDateInMilliseconds(value,calendar,iX);
		} catch (TermIsNotADate e) {
			throw new WrongArgumentIsNotADate(value);
		}
	}
	//
	public static BigInteger termToDateInMilliseconds(Term value, ChoisePoint iX) throws TermIsNotADate {
		try {
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_date,3,iX);
			long timeInMillis= termsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],value,iX);
			return BigInteger.valueOf(timeInMillis);
		} catch (Backtracking b) {
			throw TermIsNotADate.instance;
		}
	}
	public static void termToDateInMilliseconds(Term value, Calendar calendar, ChoisePoint iX) throws TermIsNotADate {
		try {
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_date,3,iX);
			termsToDateInMilliseconds(arguments[0],arguments[1],arguments[2],value,calendar,iX);
		} catch (Backtracking b) {
			throw TermIsNotADate.instance;
		}
	}
	//
	public static long argumentsToDateInMilliseconds(Term a1, Term a2, Term a3, Term term, ChoisePoint iX) {
		try {
			return termsToDateInMilliseconds(a1,a2,a3,term,iX);
		} catch (TermIsNotADate e) {
			throw new WrongArgumentIsNotADate(term);
		}
	}
	//
	public static long termsToDateInMilliseconds(Term a1, Term a2, Term a3, Term term, ChoisePoint iX) throws TermIsNotADate {
		try {
			int year= a1.getSmallIntegerValue(iX);
			int month= monthToInteger(a2,iX);
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
	public static void termsToDateInMilliseconds(Term a1, Term a2, Term a3, Term term, Calendar calendar, ChoisePoint iX) throws TermIsNotADate {
		try {
			int year= a1.getSmallIntegerValue(iX);
			int month= monthToInteger(a2,iX);
			int day= a3.getSmallIntegerValue(iX);
			// Calendar calendar= Calendar.getInstance();
			calendar.set(year,month-1,day);
			// long timeInMillis= calendar.getTimeInMillis();
			// return timeInMillis;
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotADate(term);
		} catch (Backtracking b) {
			throw TermIsNotADate.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static BigInteger argumentToTimeInMilliseconds(Term value, ChoisePoint iX) {
		try {
			return termToTimeInMilliseconds(value,iX);
		} catch (TermIsNotATime e) {
			throw new WrongArgumentIsNotATime(value);
		}
	}
	public static void argumentToTimeInMilliseconds(Term value, Calendar calendar, ChoisePoint iX) {
		try {
			termToTimeInMilliseconds(value,calendar,iX);
		} catch (TermIsNotATime e) {
			throw new WrongArgumentIsNotATime(value);
		}
	}
	//
	public static BigInteger termToTimeInMilliseconds(Term value, ChoisePoint iX) throws TermIsNotATime {
		try {
			try {
				Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_time,4,iX);
				long timeInMillis= termsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],value,iX);
				return BigInteger.valueOf(timeInMillis);
			} catch (Backtracking b2) {
				Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_time,3,iX);
				long timeInMillis= termsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],value,iX);
				return BigInteger.valueOf(timeInMillis);
			}
		} catch (Backtracking b1) {
			throw TermIsNotATime.instance;
		}
	}
	public static void termToTimeInMilliseconds(Term value, Calendar calendar, ChoisePoint iX) throws TermIsNotATime {
		try {
			try {
				Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_time,4,iX);
				termsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],arguments[3],value,calendar,iX);
			} catch (Backtracking b2) {
				Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_time,3,iX);
				termsToTimeInMilliseconds(arguments[0],arguments[1],arguments[2],value,calendar,iX);
			}
		} catch (Backtracking b1) {
			throw TermIsNotATime.instance;
		}
	}
	//
	public static long argumentsToTimeInMilliseconds(Term a1, Term a2, Term a3, Term a4, Term term, ChoisePoint iX) {
		try {
			return termsToTimeInMilliseconds(a1,a2,a3,a4,term,iX);
		} catch (TermIsNotATime e) {
			throw new WrongArgumentIsNotATime(term);
		}
	}
	//
	public static long termsToTimeInMilliseconds(Term a1, Term a2, Term a3, Term a4, Term term, ChoisePoint iX) throws TermIsNotATime {
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
	public static void termsToTimeInMilliseconds(Term a1, Term a2, Term a3, Term a4, Term term, Calendar calendar, ChoisePoint iX) throws TermIsNotATime {
		try {
			int hours= a1.getSmallIntegerValue(iX);
			int minutes= a2.getSmallIntegerValue(iX);
			int seconds= a3.getSmallIntegerValue(iX);
			int milliseconds= a4.getSmallIntegerValue(iX);
			// Calendar calendar= Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY,hours);
			calendar.set(Calendar.MINUTE,minutes);
			calendar.set(Calendar.SECOND,seconds);
			calendar.set(Calendar.MILLISECOND,milliseconds);
			// return calendar.getTimeInMillis();
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotATime(term);
		}
	}
	//
	public static long argumentsToTimeInMilliseconds(Term a1, Term a2, Term a3, Term term, ChoisePoint iX) {
		try {
			return termsToTimeInMilliseconds(a1,a2,a3,term,iX);
		} catch (TermIsNotATime e) {
			throw new WrongArgumentIsNotATime(term);
		}
	}
	//
	public static long termsToTimeInMilliseconds(Term a1, Term a2, Term a3, Term term, ChoisePoint iX) throws TermIsNotATime {
		try {
			int hours= a1.getSmallIntegerValue(iX);
			int minutes= a2.getSmallIntegerValue(iX);
			int seconds= a3.getSmallIntegerValue(iX);
			int milliseconds= 0;
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
	public static void termsToTimeInMilliseconds(Term a1, Term a2, Term a3, Term term, Calendar calendar, ChoisePoint iX) throws TermIsNotATime {
		try {
			int hours= a1.getSmallIntegerValue(iX);
			int minutes= a2.getSmallIntegerValue(iX);
			int seconds= a3.getSmallIntegerValue(iX);
			int milliseconds= 0;
			// Calendar calendar= Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY,hours);
			calendar.set(Calendar.MINUTE,minutes);
			calendar.set(Calendar.SECOND,seconds);
			calendar.set(Calendar.MILLISECOND,milliseconds);
			// return calendar.getTimeInMillis();
		} catch (TermIsNotAnInteger e) {
			throw new WrongArgumentIsNotATime(term);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term millisecondsToDate(BigInteger value) {
		Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(PrologInteger.toLong(value));
		return formDate(calendar);
	}
	//
	public static Term formDate(Calendar calendar) {
		int year= calendar.get(Calendar.YEAR);
		int month= calendar.get(Calendar.MONTH) + 1;
		int day= calendar.get(Calendar.DAY_OF_MONTH);
		return new PrologStructure(
			SymbolCodes.symbolCode_E_date,
			new Term[]{
				new PrologInteger(year),
				new PrologInteger(month),
				new PrologInteger(day)}
			);
	}
	//
	public static Term millisecondsToTime(BigInteger value) {
		Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(PrologInteger.toLong(value));
		return formTime(calendar);
	}
	//
	public static Term formTime(Calendar calendar) {
		int hours= calendar.get(Calendar.HOUR);
		int minutes= calendar.get(Calendar.MINUTE);
		int seconds= calendar.get(Calendar.SECOND);
		long milliseconds= calendar.get(Calendar.MILLISECOND);
		return new PrologStructure(
			SymbolCodes.symbolCode_E_time,
			new Term[]{
				new PrologInteger(hours),
				new PrologInteger(minutes),
				new PrologInteger(seconds),
				new PrologInteger(milliseconds)}
			);
	}
	//
	///////////////////////////////////////////////////////////////
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
	///////////////////////////////////////////////////////////////
	//
	public static BigInteger doubleArgumentToBigInteger(double value, Term term) {
		try {
			return doubleToBigInteger(value);
		} catch (TermIsNotAReal e) {
			throw new WrongArgumentIsNotAReal(term);
		}
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
	//
	public static BigInteger doubleValueToBigInteger(double value) {
		try {
			return doubleToBigInteger(value);
		} catch (TermIsNotAReal e) {
			throw new WrongArgumentIsNotAReal(new PrologReal(value));
		}
	}
	//
	public static BigDecimal doubleToBigDecimal(double value) throws TermIsNotAReal {
		try {
			return new BigDecimal(value);
		} catch (NumberFormatException nfe) {
			throw TermIsNotAReal.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static double argumentToReal(Term value, ChoisePoint iX) {
		try {
			return Converters.termToReal(value,iX);
		} catch (TermIsNotAReal e) {
			throw new WrongArgumentIsNotNumerical(value);
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
	public static double stringToReal(String text) throws TermIsNotAReal {
		double result;
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
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
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
					result= new PrologInteger(termToDateOrTimeInMilliseconds(value,iX));
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
					throw Backtracking.instance;
				}
			} else if (tokens.length==3) {
				if (tokens[0].getType()==PrologTokenType.MINUS) {
					if (tokens[1].getType()==PrologTokenType.INTEGER) {
						result= new PrologInteger(tokens[1].getIntegerValue().negate());
					} else if (tokens[1].getType()==PrologTokenType.REAL) {
						result= new PrologReal(-tokens[1].getRealValue());
					} else {
						throw Backtracking.instance;
					}
				} else if (tokens[0].getType()==PrologTokenType.PLUS) {
					if (tokens[1].getType()==PrologTokenType.INTEGER) {
						result= new PrologInteger(tokens[1].getIntegerValue());
					} else if (tokens[1].getType()==PrologTokenType.REAL) {
						result= new PrologReal(tokens[1].getRealValue());
					} else {
						throw Backtracking.instance;
					}
				} else {
					throw Backtracking.instance;
				}
			} else {
				throw Backtracking.instance;
			}
		};
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String argumentToString(Term value, ChoisePoint iX) {
		try {
			return value.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(value);
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
	//
	public static String[] termToStrings(Term value, ChoisePoint iX, boolean makeUpperCaseStrings) {
		ArrayList<String> stringList= new ArrayList<String>();
		termToStrings(stringList,value,iX,makeUpperCaseStrings);
		return stringList.toArray(new String[0]);
	}
	//
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
	///////////////////////////////////////////////////////////////
	//
	public static BigInteger[] argumentToIntegers(Term value, ChoisePoint iX) {
		ArrayList<BigInteger> integerList= new ArrayList<BigInteger>();
		argumentToIntegers(integerList,value,iX);
		return integerList.toArray(new BigInteger[0]);
	}
	//
	public static void argumentToIntegers(ArrayList<BigInteger> integerList, Term value, ChoisePoint iX) {
		Term nextHead;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				argumentToIntegers(integerList,nextHead,iX);
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
	public static long[] argumentToLongIntegers(Term value, ChoisePoint iX) {
		ArrayList<Long> integerList= new ArrayList<Long>();
		argumentToLongIntegers(integerList,value,iX);
		long[] array= new long[integerList.size()];
		for (int n=0; n < array.length; n++) {
			array[n]= integerList.get(n);
		};
		return array;
	}
	//
	public static void argumentToLongIntegers(ArrayList<Long> integerList, Term value, ChoisePoint iX) {
		Term nextHead;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				argumentToLongIntegers(integerList,nextHead,iX);
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e1) {
		} catch (TermIsNotAList e1) {
			try {
				long result= PrologInteger.toLong(termToRoundInteger(currentTail,iX,true));
				integerList.add(result);
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotAnInteger(currentTail);
			}
		} catch (Throwable e1) {
			throw new WrongArgumentIsNotIntegerList(currentTail);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String codesToString(BigInteger[] numbers) {
		char[] codes= new char[numbers.length];
		for (int n=0; n < numbers.length; n++) {
			codes[n]= PrologInteger.toCharacter(numbers[n]);
		};
		return new String(codes);
	}
	//
	///////////////////////////////////////////////////////////////
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
		Term result= PrologEmptyList.instance;
		for (int n=array.length-1; n >= 0; n--) {
			result= new PrologList(array[n],result);
		};
		return result;
	}
	public static Term arrayToList(Term[] array, Term result) {
		// Term result= PrologEmptyList.instance;
		for (int n=array.length-1; n >= 0; n--) {
			result= new PrologList(array[n],result);
		};
		return result;
	}
	//
	public static Term sparseArrayToList(Term[] array) {
		Term buffer= PrologEmptyList.instance;
		for (int n=array.length-1; n >= 0; n--) {
			Term term= array[n];
			if (term != null) {
				buffer= new PrologList(term,buffer);
			}
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
	public static Term arrayListToTerm(ArrayList<Term> array, Term result) {
		// Term result= PrologEmptyList.instance;
		for (int n=array.size()-1; n >= 0; n--) {
			result= new PrologList(array.get(n),result);
		};
		return result;
	}
	//
	public static Term stringArrayToList(String[] array) {
		Term result= PrologEmptyList.instance;
		for (int n=array.length-1; n >= 0; n--) {
			result= new PrologList(new PrologString(array[n]),result);
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
	//
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
	//
	public static Term doubleArrayToList(double[] array) {
		Term result= PrologEmptyList.instance;
		for (int n=array.length-1; n >= 0; n--) {
			result= new PrologList(new PrologReal(array[n]),result);
		};
		return result;
	}
	//
	public static Term dimensionArrayToList(Dimension[] array) {
		long symbolSize= SymbolCodes.symbolCode_E_size;
		Term result= PrologEmptyList.instance;
		for (int n=array.length-1; n >= 0; n--) {
			Dimension dimension= array[n];
			Term[] arguments= new Term[2];
			arguments[0]= new PrologInteger(dimension.width);
			arguments[1]= new PrologInteger(dimension.height);
			Term size= new PrologStructure(symbolSize,arguments);
			result= new PrologList(size,result);
		};
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static byte[] string2ByteArray(String text) {
		String targetString;
		if (text.charAt(1)=='e') {
			targetString= "-" + text.substring(4);
		} else {
			targetString= text.substring(4);
		};
		BigInteger bigInteger= new BigInteger(targetString,36);
		byte[] byteArray= bigInteger.toByteArray();
		return byteArray;
	}
	//
	public static String byteArray2String(byte[] byteArray) {
		BigInteger bigInteger= new BigInteger(byteArray);
		String text= bigInteger.toString(36);
		if (text.charAt(0)=='-') {
			text= "feff" + text.substring(1);
		} else {
			text= "fffe" + text;
		};
		return text;
	}
	//
	///////////////////////////////////////////////////////////////
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
				if (functor == SymbolCodes.symbolCode_E_normal) {
					return Thread.NORM_PRIORITY;
				} else if (functor == SymbolCodes.symbolCode_E_minimal) {
					return Thread.MIN_PRIORITY;
				} else if (functor == SymbolCodes.symbolCode_E_maximal) {
					return Thread.MAX_PRIORITY;
				} else {
					throw TermIsNotProcessPriority.instance;
				}
			} catch (TermIsNotASymbol e2) {
				throw TermIsNotProcessPriority.instance;
			}
		}
	}
	//
	public static Term ProcessPriorityToTerm(int value) {
		switch (value) {
		case Thread.NORM_PRIORITY:
			return termNormal;
		case Thread.MIN_PRIORITY:
			return termMinimal;
		case Thread.MAX_PRIORITY:
			return termMaximal;
		default:
			return new PrologInteger(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static byte[] serializeArgument(Term argument) {
		ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectStream= new DataStoreOutputStream(outputStream);
			try {
				objectStream.writeObject(argument);
			} finally {
				objectStream.close();
			}
		} catch (IOException e) {
			throw new DataSerializingError(e);
		};
		byte[] byteArray= outputStream.toByteArray();
		return byteArray;
	}
	//
	public static byte[] serializeMatrix(double[][] matrix) {
		ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectStream= new ObjectOutputStream(outputStream);
			try {
				objectStream.writeObject(matrix);
			} finally {
				objectStream.close();
			}
		} catch (IOException e) {
			throw new DataSerializingError(e);
		};
		byte[] byteArray= outputStream.toByteArray();
		return byteArray;
	}
	//
	public static byte[] serializeArguments(Term[] arguments) {
		ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectStream= new DataStoreOutputStream(outputStream);
			try {
				objectStream.writeObject(arguments);
			} finally {
				objectStream.close();
			}
		} catch (IOException e) {
			throw new DataSerializingError(e);
		};
		byte[] byteArray= outputStream.toByteArray();
		return byteArray;
	}
	//
	public static byte[] serializeDomainTable(HashMap<String,PrologDomain> table) {
		ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectStream= new DataStoreOutputStream(outputStream);
			try {
				objectStream.writeObject(table);
			} finally {
				objectStream.close();
			}
		} catch (IOException e) {
			throw new DataSerializingError(e);
		};
		byte[] byteArray= outputStream.toByteArray();
		return byteArray;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term deserializeArgument(byte[] argumentByteArray, long domainSignatureNumber) {
		Term argument;
		ByteArrayInputStream inputStream= new ByteArrayInputStream(argumentByteArray);
		try {
			DataStoreInputStream objectStream= new DataStoreInputStream(inputStream,false);
			try {
				argument= (Term)objectStream.readObject();
				if (objectStream.worldsAreDetected()) {
					MethodSignature ownSignature= MethodSignatures.getSignature(domainSignatureNumber);
					MethodArgument[] signatureArguments= ownSignature.arguments;
					if (signatureArguments.length > 0) {
						if (!signatureArguments[0].domain.coversTerm(argument,null,false)) {
							throw new WrongTermDoesNotBelongToDomain(argument);
						}
					} else {
						throw new UnexpectedNumberOfArguments();
					}
				}
			} finally {
				objectStream.close();
			}
		} catch (ClassNotFoundException e) {
			throw new DataDeserializingError(e);
		} catch (IOException e) {
			throw new DataDeserializingError(e);
		};
		return argument;
	}
	//
	public static Term deserializeArgument(byte[] argumentByteArray) {
		Term argument;
		ByteArrayInputStream inputStream= new ByteArrayInputStream(argumentByteArray);
		try {
			DataStoreInputStream objectStream= new DataStoreInputStream(inputStream,false);
			try {
				argument= (Term)objectStream.readObject();
				if (objectStream.worldsAreDetected()) {
					throw new AWorldIsFoundInTheDataItem(argument);
				}
			} finally {
				objectStream.close();
			}
		} catch (ClassNotFoundException e) {
			throw new DataDeserializingError(e);
		} catch (IOException e) {
			throw new DataDeserializingError(e);
		};
		return argument;
	}
	//
	public static double[][] deserializeMatrix(byte[] argumentByteArray) {
		double[][] matrix;
		ByteArrayInputStream inputStream= new ByteArrayInputStream(argumentByteArray);
		try {
			ObjectInputStream objectStream= new ObjectInputStream(inputStream);
			try {
				matrix= (double[][])objectStream.readObject();
			} finally {
				objectStream.close();
			}
		} catch (ClassNotFoundException e) {
			throw new DataDeserializingError(e);
		} catch (IOException e) {
			throw new DataDeserializingError(e);
		};
		return matrix;
	}
	//
	public static Term[] deserializeArguments(byte[] argumentByteArray, long domainSignatureNumber) {
		ChoisePoint iX= null;
		Term[] arguments;
		ByteArrayInputStream inputStream= new ByteArrayInputStream(argumentByteArray);
		try {
			DataStoreInputStream objectStream= new DataStoreInputStream(inputStream,false);
			try {
				arguments= (Term[])objectStream.readObject();
				if (objectStream.worldsAreDetected()) {
					MethodSignature ownSignature= MethodSignatures.getSignature(domainSignatureNumber);
					MethodArgument[] signatureArguments= ownSignature.arguments;
					if (signatureArguments.length == arguments.length) {
						for (int n=0; n < arguments.length; n++) {
							if (!signatureArguments[n].domain.coversTerm(arguments[n],iX,false)) {
								throw new WrongTermDoesNotBelongToDomain(arguments[n]);
							}
						}
					} else {
						throw new UnexpectedNumberOfArguments();
					}
				}
			} finally {
				objectStream.close();
			}
		} catch (ClassNotFoundException e) {
			throw new DataDeserializingError(e);
		} catch (IOException e) {
			throw new DataDeserializingError(e);
		};
		return arguments;
	}
	//
	@SuppressWarnings("unchecked")
	public static HashMap<String,PrologDomain> deserializeDomainTable(byte[] argumentByteArray) {
		HashMap<String,PrologDomain> localDomainTable;
		ByteArrayInputStream inputStream= new ByteArrayInputStream(argumentByteArray);
		try {
			DataStoreInputStream objectStream= new DataStoreInputStream(inputStream,false);
			try {
				localDomainTable= (HashMap<String,PrologDomain>)objectStream.readObject();
			} finally {
				objectStream.close();
			}
		} catch (ClassNotFoundException e) {
			throw new DataDeserializingError(e);
		} catch (IOException e) {
			throw new DataDeserializingError(e);
		};
		Set<String> keys= localDomainTable.keySet();
		Iterator<String> iterator= keys.iterator();
		while (iterator.hasNext()) {
			String key= iterator.next();
			PrologDomain localDomain= localDomainTable.get(key);
			localDomain.acceptLocalDomainTable(localDomainTable);
		};
		return localDomainTable;
	}
}
