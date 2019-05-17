// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

import target.*;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.gui.*;
import morozov.system.gui.signals.*;
import morozov.system.gui.dialogs.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Font;
import java.awt.Color;
import java.awt.font.TextAttribute;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DialogUtils {
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	public static int calculateAbsoluteCoordinate(ExtendedCoordinate lZ, int spaceBeginning, int spaceLimit, double gridZ, double rDZ)
			throws UseDefaultLocation {
		try {
			return PrologInteger.toInteger(spaceBeginning + lZ.getDoubleValue() / gridZ * spaceLimit );
		} catch (CentreFigure e) {
			return PrologInteger.toInteger(spaceBeginning + ((double)spaceLimit - rDZ) / 2);
		}
	}
	public static int calculateAbsoluteCoordinate(ExtendedCoordinate lZ, int spaceBeginning, int spaceLimit, double rDZ)
			throws UseDefaultLocation {
		try {
			return PrologInteger.toInteger(spaceBeginning + lZ.getDoubleValue());
		} catch (CentreFigure e) {
			return PrologInteger.toInteger(spaceBeginning + ((double)spaceLimit - rDZ) / 2);
		}
	}
	//
	public static ArrayList<String> listToStringVector(Term tail, ChoisePoint iX) {
		ArrayList<String> array= new ArrayList<String>();
		listToStringVector(array,tail,iX);
		return array;
	}
	//
	public static void listToStringVector(ArrayList<String> array, Term tail, ChoisePoint iX) {
		tail= tail.dereferenceValue(iX);
		if (tail.thisIsFreeVariable()) {
			return;
		};
		try {
			while (true) {
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
	//
	public static void listToStringArray(ArrayList<String> array, Term tail, ChoisePoint iX) {
		tail= tail.dereferenceValue(iX);
		if (tail.thisIsFreeVariable()) {
			return;
		};
		try {
			while (true) {
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
	//
	public static void tableToStringArray(ArrayList<ArrayList<String>> array, Term tail, ChoisePoint iX) {
		tail= tail.dereferenceValue(iX);
		if (tail.thisIsFreeVariable()) {
			return;
		};
		try {
			while (true) {
				Term rowTail= tail.getNextListHead(iX);
				ArrayList<String> columns= new ArrayList<String>();
				try {
					while (true) {
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
	//
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
				BigInteger bigInteger= GeneralConverters.doubleToBigInteger(number);
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
							while (true) {
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
	//
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
				BigInteger bigInteger= GeneralConverters.doubleToBigInteger(number);
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
							while (true) {
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
	//
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
				BigInteger bigInteger= GeneralConverters.doubleToBigInteger(number);
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
	//
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
				BigInteger bigInteger= GeneralConverters.doubleToBigInteger(number);
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
	//
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
	public static int termToSmallIntegerOrReject(Term value, ChoisePoint iX) throws RejectValue {
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
				BigInteger bigInteger= GeneralConverters.doubleToBigInteger(number);
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
	///////////////////////////////////////////////////////////////
	//
	public static Font refineTextAndSpaceColors(
			Font font,
			Color individualSpaceColor,
			Color spaceColor,
			Color supervisoryTextColor,
			Color individualTextColor,
			Color textColor) {
		if (supervisoryTextColor != null) {
			Map<TextAttribute,Object> map= new HashMap<TextAttribute,Object>();
			map.put(TextAttribute.FOREGROUND,supervisoryTextColor);
			font= font.deriveFont(map);
		} else if (individualSpaceColor != null) {
			Map<TextAttribute,Object> map= new HashMap<TextAttribute,Object>();
			map.put(TextAttribute.BACKGROUND,individualSpaceColor);
			refineTextColor(map,individualTextColor,textColor);
			font= font.deriveFont(map);
		} else if (spaceColor != null) {
			Map<TextAttribute,Object> map= new HashMap<TextAttribute,Object>();
			map.put(TextAttribute.BACKGROUND,spaceColor);
			refineTextColor(map,individualTextColor,textColor);
			font= font.deriveFont(map);
		} else if (individualTextColor != null) {
			Map<TextAttribute,Object> map= new HashMap<TextAttribute,Object>();
			map.put(TextAttribute.FOREGROUND,individualTextColor);
			font= font.deriveFont(map);
		} else if (textColor != null) {
			Map<TextAttribute,Object> map= new HashMap<TextAttribute,Object>();
			map.put(TextAttribute.FOREGROUND,textColor);
			font= font.deriveFont(map);
		};
		return font;
	}
	//
	public static void refineTextColor(
			Map<TextAttribute,Object> map,
			Color individualTextColor,
			Color textColor) {
		if (individualTextColor != null) {
			map.put(TextAttribute.FOREGROUND,individualTextColor);
		} else if (textColor != null) {
			map.put(TextAttribute.FOREGROUND,textColor);
		}
	}
}
