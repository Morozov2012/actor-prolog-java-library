// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.gui.signals.*;
import morozov.system.gui.dialogs.errors.*;
import morozov.system.gui.dialogs.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.math.BigInteger;
import java.util.ArrayList;

public class DialogUtils {
	public static String termToDialogIdentifier(Term value, ChoisePoint iX) throws TermIsSymbolAuto {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_auto) {
				throw TermIsSymbolAuto.instance;
			} else {
				throw new WrongTermIsNotDialogIdentifier(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return value.getStringValue(iX);
			} catch (TermIsNotAString e2) {
				throw new WrongTermIsNotDialogIdentifier(value);
			}
		}
	}
	//
	public static int calculateRealCoordinate(ExtendedCoordinate lZ, int spaceBeginning, int spaceLimit, double gridZ, double rDZ)
		throws UseDefaultLocation {
		try {
			return (int)StrictMath.round(spaceBeginning + (double)lZ.getValue() / gridZ * spaceLimit );
		} catch (CentreFigure e) {
			return (int)StrictMath.round(spaceBeginning + ((double)spaceLimit - rDZ) / 2);
		}
	}
	//
	public static ArrayList<String> listToStringVector(Term tail, ChoisePoint iX) {
		ArrayList<String> array= new ArrayList<String>();
		listToStringVector(array,tail,iX);
		return array;
	}
	public static void listToStringVector(ArrayList<String> array, Term tail, ChoisePoint iX) {
		tail= tail.dereferenceValue(iX);
		if (tail.thisIsFreeVariable()) {
			return;
		};
		try {
			while(true) {
				Term value= tail.getNextListHead(iX);
				listToStringVector(array,value,iX);
				tail= tail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			tail= tail.dereferenceValue(iX);
			if (tail.thisIsFreeVariable()) {
				return;
			};
			if (!tail.thisIsUnknownValue()) {
				array.add(tail.toString(iX));
			}
		}
	}
	//
	public static ArrayList<String> listToStringArray(Term tail, ChoisePoint iX) {
		ArrayList<String> array= new ArrayList<String>();
		listToStringArray(array,tail,iX);
		return array;
	}
	public static void listToStringArray(ArrayList<String> array, Term tail, ChoisePoint iX) {
		tail= tail.dereferenceValue(iX);
		if (tail.thisIsFreeVariable()) {
			return;
		};
		try {
			while(true) {
				Term value= tail.getNextListHead(iX);
				listToStringArray(array,value,iX);
				tail= tail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			tail= tail.dereferenceValue(iX);
			if (tail.thisIsFreeVariable()) {
				return;
			};
			if (!tail.thisIsUnknownValue()) {
				array.add(tail.toString(iX));
			}
		}
	}
	//
	public static ArrayList<ArrayList<String>> tableToStringArray(Term tail, ChoisePoint iX) {
		ArrayList<ArrayList<String>> array= new ArrayList<ArrayList<String>>();
		tableToStringArray(array,tail,iX);
		return array;
	}
	public static void tableToStringArray(ArrayList<ArrayList<String>> array, Term tail, ChoisePoint iX) {
		tail= tail.dereferenceValue(iX);
		if (tail.thisIsFreeVariable()) {
			return;
		};
		try {
			while(true) {
				Term rowTail= tail.getNextListHead(iX);
				ArrayList<String> columns= new ArrayList<String>();
				try {
					while(true) {
						Term cell= rowTail.getNextListHead(iX);
						cell= cell.dereferenceValue(iX);
						if (	!cell.thisIsFreeVariable() &&
							!cell.thisIsUnknownValue()) {
							columns.add(cell.toString(iX));
						};
						rowTail= rowTail.getNextListTail(iX);
					}
				} catch (EndOfList e) {
				} catch (TermIsNotAList e) {
					rowTail= rowTail.dereferenceValue(iX);
					if (	!rowTail.thisIsFreeVariable() &&
						!rowTail.thisIsUnknownValue()) {
						columns.add(rowTail.toString(iX));
					}
				};
				// listToStringArray(columns,value,iX);
				array.add(columns);
				tail= tail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
		} catch (TermIsNotAList e) {
			tail= tail.dereferenceValue(iX);
			if (tail.thisIsFreeVariable()) {
				return;
			};
			if (!tail.thisIsUnknownValue()) {
				ArrayList<String> columns= new ArrayList<String>();
				columns.add(tail.toString(iX));
				array.add(columns);
			}
		}
	}
	//
	public static ArrayList<Term> listToTermArray(Term tail, ChoisePoint iX) {
		ArrayList<Term> array= new ArrayList<Term>();
		listToTermArray(array,tail,iX);
		return array;
	}
	public static void listToTermArray(ArrayList<Term> array, Term tail, ChoisePoint iX) {
		tail= tail.dereferenceValue(iX);
		if (tail.thisIsFreeVariable()) {
			return;
		};
		try {
			BigInteger bigInteger= tail.getIntegerValue(iX);
			if (PrologInteger.isSmallInteger(bigInteger)) {
				array.add(new PrologInteger(bigInteger));
			} else {
				return;
			}
		} catch (TermIsNotAnInteger e1) {
			try {
				double number= StrictMath.round(tail.getRealValue(iX));
				BigInteger bigInteger= Converters.doubleToBigInteger(number);
				if (PrologInteger.isSmallInteger(bigInteger)) {
					array.add(new PrologInteger(bigInteger));
				} else {
					return;
				}
			} catch (TermIsNotAReal e2) {
				try {
					long code= tail.getSymbolValue(iX);
					array.add(new PrologSymbol(code));
				} catch (TermIsNotASymbol e3) {
					try {
						String text= tail.getStringValue(iX);
						array.add(new PrologString(text));
					} catch (TermIsNotAString e4) {
						try {
							while(true) {
								Term value= tail.getNextListHead(iX);
								listToTermArray(array,value,iX);
								tail= tail.getNextListTail(iX);
							}
						} catch (EndOfList e5) {
						} catch (TermIsNotAList e5) {
							// listToTermArray(array,tail,iX);
						}
					}
				}
			}
		}
	}
	//
	public static ArrayList<Term> tableToTermArray(Term tail, ChoisePoint iX) {
		ArrayList<Term> array= new ArrayList<Term>();
		tableToTermArray(array,tail,iX);
		return array;
	}
	public static void tableToTermArray(ArrayList<Term> array, Term tail, ChoisePoint iX) {
		tail= tail.dereferenceValue(iX);
		if (tail.thisIsFreeVariable()) {
			return;
		};
		if (tail.thisIsUnknownValue()) {
			array.add(tail);
			return;
		};
		try {
			BigInteger bigInteger= tail.getIntegerValue(iX);
			if (PrologInteger.isSmallInteger(bigInteger)) {
				array.add(new PrologInteger(bigInteger));
			} else {
				return;
			}
		} catch (TermIsNotAnInteger e1) {
			try {
				double number= StrictMath.round(tail.getRealValue(iX));
				BigInteger bigInteger= Converters.doubleToBigInteger(number);
				if (PrologInteger.isSmallInteger(bigInteger)) {
					array.add(new PrologInteger(bigInteger));
				} else {
					return;
				}
			} catch (TermIsNotAReal e2) {
				try {
					String text= tail.getStringValue(iX);
					array.add(new PrologString(text));
				} catch (TermIsNotAString e3) {
					try {
						long code= tail.getSymbolValue(iX);
						SymbolName name= SymbolNames.retrieveSymbolName(code);
						array.add(new PrologString(name.identifier));
					} catch (TermIsNotASymbol e4) {
						try {
							while(true) {
								Term currentRow= tail.getNextListHead(iX);
								rowToTermArray(array,currentRow,iX);
								// listToTermArray(array,value,iX);
								tail= tail.getNextListTail(iX);
							}
						} catch (EndOfList e5) {
						} catch (TermIsNotAList e5) {
							// listToTermArray(array,tail,iX);
						}
					}
				}
			}
		}
	}
	protected static void rowToTermArray(ArrayList<Term> array, Term tail, ChoisePoint iX) {
		tail= tail.dereferenceValue(iX);
		if (tail.thisIsFreeVariable()) {
			return;
		};
		// if (tail.thisIsUnknownValue()) {
		//	array.add(PrologUnknownValue.instance);
		//	return;
		// };
		try {
			BigInteger bigInteger= tail.getIntegerValue(iX);
			if (PrologInteger.isSmallInteger(bigInteger)) {
				array.add(new PrologInteger(bigInteger));
			} else {
				return;
			}
		} catch (TermIsNotAnInteger e1) {
			try {
				double number= StrictMath.round(tail.getRealValue(iX));
				BigInteger bigInteger= Converters.doubleToBigInteger(number);
				if (PrologInteger.isSmallInteger(bigInteger)) {
					array.add(new PrologInteger(bigInteger));
				} else {
					return;
				}
			} catch (TermIsNotAReal e2) {
				try {
					String text= tail.getStringValue(iX);
					array.add(new PrologString(text));
				} catch (TermIsNotAString e3) {
					try {
						long code= tail.getSymbolValue(iX);
						SymbolName name= SymbolNames.retrieveSymbolName(code);
						array.add(new PrologString(name.identifier));
					} catch (TermIsNotASymbol e4) {
						try {
							Term firstCell= tail.getNextListHead(iX);
							try {
								firstCell= frontCell(firstCell,iX);
								tail= tail.getNextListTail(iX);
								Term rowTail= rowTailToTerm(tail,iX);
								array.add(new PrologList(firstCell,rowTail));
							} catch (TermIsNotFrontCell e5) {
							}
						} catch (EndOfList e5) {
						} catch (TermIsNotAList e5) {
							// listToTermArray(array,tail,iX);
						}
					}
				}
			}
		}
	}
	protected static Term frontCell(Term value, ChoisePoint iX) throws TermIsNotFrontCell {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable()) {
			throw TermIsNotFrontCell.instance;
		};
		if (value.thisIsUnknownValue()) {
			return value;
		};
		try {
			BigInteger bigInteger= value.getIntegerValue(iX);
			if (PrologInteger.isSmallInteger(bigInteger)) {
				return value;
			} else {
				throw TermIsNotFrontCell.instance;
			}
		} catch (TermIsNotAnInteger e1) {
			try {
				double number= StrictMath.round(value.getRealValue(iX));
				BigInteger bigInteger= Converters.doubleToBigInteger(number);
				if (PrologInteger.isSmallInteger(bigInteger)) {
					return new PrologInteger(bigInteger);
				} else {
					throw TermIsNotFrontCell.instance;
				}
			} catch (TermIsNotAReal e2) {
				throw TermIsNotFrontCell.instance;
			}
		}
	}
	protected static Term rowTailToTerm(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable()) {
			return PrologEmptyList.instance;
		};
		try {
			Term frontCell= value.getNextListHead(iX);
			frontCell= frontCell.dereferenceValue(iX);
			if (!frontCell.thisIsUnknownValue()) {
				try {
					String text= frontCell.getStringValue(iX);
				} catch (TermIsNotAString e1) {
					frontCell= new PrologString(frontCell.toString(iX));
				}
			};
			Term rest= value.getNextListTail(iX);
			rest= rowTailToTerm(rest,iX);
			return new PrologList(frontCell,rest);
		} catch (EndOfList e5) {
			return PrologEmptyList.instance;
		} catch (TermIsNotAList e5) {
			return PrologEmptyList.instance;
		}
	}
	//
	public static int termToSmallInteger(Term value, ChoisePoint iX) throws RejectValue {
		try {
			BigInteger bigInteger= value.getIntegerValue(iX);
			if (PrologInteger.isSmallInteger(bigInteger)) {
				return bigInteger.intValue();
			} else {
				throw RejectValue.instance;
			}
		} catch (TermIsNotAnInteger e1) {
			try {
				double number= StrictMath.round(value.getRealValue(iX));
				BigInteger bigInteger= Converters.doubleToBigInteger(number);
				if (PrologInteger.isSmallInteger(bigInteger)) {
					return bigInteger.intValue();
				} else {
					throw RejectValue.instance;
				}
			} catch (TermIsNotAReal e2) {
				// return PrologUnknownValue.instance;
				throw RejectValue.instance;
			}
		}
	}
	//
	public static Term standardizeCoordinateValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			return new PrologSymbol(SymbolCodes.symbolCode_E_default);
		} else {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_default) {
					return new PrologSymbol(code);
				} else if (code==SymbolCodes.symbolCode_E_centered) {
					return new PrologSymbol(code);
				} else {
					throw RejectValue.instance;
				}
			} catch (TermIsNotASymbol e1) {
				try {
					double number= value.getIntegerValue(iX).doubleValue();
					return new PrologReal(number);
				} catch (TermIsNotAnInteger e2) {
					try {
						double number= value.getRealValue(iX);
						return new PrologReal(number);
					} catch (TermIsNotAReal e3) {
						throw RejectValue.instance;
					}
				}
			}
		}
	}
	//
	public static Term standardizeColorValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			return new PrologSymbol(SymbolCodes.symbolCode_E_default);
		} else {
			try {
				long code= value.getSymbolValue(iX);
				try {
					GUI_Utils.symbolCodeToColor(code);
					return new PrologSymbol(code);
				} catch (TermIsSymbolDefault e1) {
					return new PrologSymbol(SymbolCodes.symbolCode_E_default);
				} catch (IsNotColorSymbolCode e1) {
					throw RejectValue.instance;
				}
			} catch (TermIsNotASymbol e1) {
				try {
					BigInteger bigInteger= value.getIntegerValue(iX);
					// if (PrologInteger.isSmallInteger(bigInteger)) {
						return new PrologInteger(bigInteger);
					// } else {
					//	throw RejectValue.instance;
					// }
				} catch (TermIsNotAnInteger e2) {
					try {
						double number= StrictMath.round(value.getRealValue(iX));
						BigInteger bigInteger= Converters.doubleToBigInteger(number);
						// if (PrologInteger.isSmallInteger(bigInteger)) {
							return new PrologInteger(bigInteger.intValue());
						// } else {
						//	throw RejectValue.instance;
						// }
					} catch (TermIsNotAReal e3) {
						try {
							String colorName= value.getStringValue(iX);
							try {
								BigInteger number= Converters.stringToRoundInteger(colorName);
								return new PrologInteger(number);
							// } catch (NumberFormatException e4) {
							} catch (TermIsNotAnInteger e4) {
								try {
									long code= GUI_Utils.nameToColorSymbolCode(colorName);
									GUI_Utils.symbolCodeToColor(code);
									return new PrologSymbol(code);
								} catch (TermIsSymbolDefault e5) {
									return new PrologSymbol(SymbolCodes.symbolCode_E_default);
								} catch (IsNotColorName e5) {
									throw RejectValue.instance;
								} catch (IsNotColorSymbolCode e5) {
									throw RejectValue.instance;
								}
							}
						} catch (TermIsNotAString e4) {
							throw RejectValue.instance;
						}
					}
				}
			}
		}
	}
	//
	public static Term standardizeFontNameValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			return new PrologSymbol(SymbolCodes.symbolCode_E_default);
		} else {
			try {
				long code= value.getSymbolValue(iX);
				try {
					GUI_Utils.symbolCodeToFontName(code);
					return new PrologSymbol(code);
				} catch (TermIsSymbolDefault e1) {
					return new PrologSymbol(SymbolCodes.symbolCode_E_default);
				} catch (IsNotFontNameSymbolCode e1) {
					throw RejectValue.instance;
				}
			} catch (TermIsNotASymbol e1) {
				try {
					String fontName= value.getStringValue(iX);
					return new PrologString(fontName);
				} catch (TermIsNotAString e4) {
					throw RejectValue.instance;
				}
			}
		}
	}
	//
	public static Term standardizeFontSizeValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			return new PrologSymbol(SymbolCodes.symbolCode_E_default);
		} else {
			try {
				BigInteger number= value.getIntegerValue(iX);
				return new PrologInteger(number);
			} catch (TermIsNotAnInteger e1) {
				try {
					double number= value.getRealValue(iX);
					// BigInteger bigInteger= Converters.doubleToBigInteger(number);
					return new PrologReal(number);
				} catch (TermIsNotAReal e2) {
					try {
						long code= value.getSymbolValue(iX);
						if (code==SymbolCodes.symbolCode_E_default) {
							return new PrologSymbol(code);
						} else {
							throw RejectValue.instance;
						}
					} catch (TermIsNotASymbol e3) {
						throw RejectValue.instance;
					}
				}
			}
		}
	}
	//
	public static Term standardizeFontStyleValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			return new PrologSymbol(SymbolCodes.symbolCode_E_default);
		} else {
			try {
				GUI_Utils.termToFontStyle(value,iX);
				return value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
			} catch (TermIsSymbolDefault e) {
				return new PrologSymbol(SymbolCodes.symbolCode_E_default);
			} catch (IsNotFontStyle e) {
				throw RejectValue.instance;
			}
		}
	}
}
