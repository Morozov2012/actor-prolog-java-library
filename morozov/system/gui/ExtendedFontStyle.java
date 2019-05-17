// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.run.*;
import morozov.system.gui.errors.*;
import morozov.system.gui.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Font;

public class ExtendedFontStyle {
	//
	private boolean useDefaultFontStyle= true;
	private boolean isBold= false;
	private boolean isItalic= false;
	private boolean isUnderlined= false;
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	public ExtendedFontStyle() {
	}
	public ExtendedFontStyle(boolean b, boolean i, boolean u) {
		useDefaultFontStyle= false;
		isBold= b;
		isItalic= i;
		isUnderlined= u;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void useDefaultFontStyle() {
		useDefaultFontStyle= true;
	}
	//
	public boolean isDefault() {
		return useDefaultFontStyle;
	}
	//
	public void setFontStyle(boolean b, boolean i, boolean u) {
		isBold= b;
		isItalic= i;
		isUnderlined= u;
		useDefaultFontStyle= false;
	}
	//
	public int getValue() throws UseDefaultFontStyle {
		if (useDefaultFontStyle) {
			throw UseDefaultFontStyle.instance;
		} else {
			int style= Font.PLAIN;
			if (isBold) {
				style= style + Font.BOLD;
			};
			if (isItalic) {
				style= style + Font.ITALIC;
			};
			return style;
		}
	}
	//
	public boolean isBold() throws UseDefaultFontStyle {
		if (useDefaultFontStyle) {
			throw UseDefaultFontStyle.instance;
		} else {
			return isBold;
		}
	}
	//
	public boolean isItalic() throws UseDefaultFontStyle {
		if (useDefaultFontStyle) {
			throw UseDefaultFontStyle.instance;
		} else {
			return isItalic;
		}
	}
	//
	public boolean isUnderlined() throws UseDefaultFontStyle {
		if (useDefaultFontStyle) {
			throw UseDefaultFontStyle.instance;
		} else {
			return isUnderlined;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static ExtendedFontStyle argumentToExtendedFontStyleSafe(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return new ExtendedFontStyle();
		} else {
			try {
				return argumentToExtendedFontStyle(value,iX);
			// } catch (IsNotFontStyle e) {
			//	return new ExtendedFontStyle();
			} catch (RuntimeException e) {
				return new ExtendedFontStyle();
			}
		}
	}
	//
	public static Term argumentToExtendedFontStyleOrFail(Term value, ChoisePoint iX) throws Backtracking {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable()) {
			throw Backtracking.instance;
		} else if (value.thisIsUnknownValue()) {
			return termDefault;
		} else {
			try {
				return argumentToExtendedFontStyle(value,iX).toTerm();
			} catch (RuntimeException e) {
				return termDefault;
			}
		}
	}
	//
	public static ExtendedFontStyle argumentToExtendedFontStyle(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new ExtendedFontStyle();
			} else if (code==SymbolCodes.symbolCode_E_bold) {
				return new ExtendedFontStyle(true,false,false);
			} else if (code==SymbolCodes.symbolCode_E_italic) {
				return new ExtendedFontStyle(false,true,false);
			} else if (code==SymbolCodes.symbolCode_E_underlined) {
				return new ExtendedFontStyle(false,false,true);
			} else {
				throw new WrongArgumentIsNotFontStyle(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				boolean isBold= false;
				boolean isItalic= false;
				boolean isUnderlined= false;
				try {
					while (true) {
						Term head= value.getNextListHead(iX);
						long code= head.getSymbolValue(iX);
						if (code==SymbolCodes.symbolCode_E_default) {
							return new ExtendedFontStyle();
						} else if (code==SymbolCodes.symbolCode_E_bold) {
							isBold= true;
						} else if (code==SymbolCodes.symbolCode_E_italic) {
							isItalic= true;
						} else if (code==SymbolCodes.symbolCode_E_underlined) {
							isUnderlined= true;
						} else {
							throw new WrongArgumentIsNotFontStyle(value);
						};
						value= value.getNextListTail(iX);
					}
				} catch (EndOfList e2) {
				};
				return new ExtendedFontStyle(isBold,isItalic,isUnderlined);
			} catch (TermIsNotAList e3) {
				throw new WrongArgumentIsNotFontStyle(value);
			} catch (TermIsNotASymbol e4) {
				throw new WrongArgumentIsNotFontStyle(value);
			}
		}
	}
	//
	public static int argumentToFontStyleSafe(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw TermIsSymbolDefault.instance;
		} else {
			try {
				return termToFontStyle(value,iX);
			} catch (IsNotFontStyle e) {
				// throw new WrongArgumentIsNotFontStyle(value);
				throw TermIsSymbolDefault.instance;
			} catch (RuntimeException e) {
				throw TermIsSymbolDefault.instance;
			}
		}
	}
	//
	public static boolean fontIsUnderlinedSafe(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw TermIsSymbolDefault.instance;
		} else {
			try {
				return fontIsUnderlined(value,iX);
			} catch (IsNotFontStyle e) {
				// throw new WrongArgumentIsNotFontStyle(value);
				throw TermIsSymbolDefault.instance;
			} catch (RuntimeException e) {
				throw TermIsSymbolDefault.instance;
			}
		}
	}
	//
	public static Term fontStyleToTerm(boolean isBold, boolean isItalic, boolean isUnderlined) {
		if (isBold && !isItalic && !isUnderlined) {
			return new PrologSymbol(SymbolCodes.symbolCode_E_bold);
		} else if (!isBold && isItalic && !isUnderlined) {
			return new PrologSymbol(SymbolCodes.symbolCode_E_italic);
		} else if (!isBold && !isItalic && isUnderlined) {
			return new PrologSymbol(SymbolCodes.symbolCode_E_underlined);
		} else {
			Term result= PrologEmptyList.instance;
			if (isUnderlined) {
				result= new PrologList(new PrologSymbol(SymbolCodes.symbolCode_E_underlined),result);
			};
			if (isItalic) {
				result= new PrologList(new PrologSymbol(SymbolCodes.symbolCode_E_italic),result);
			};
			if (isBold) {
				result= new PrologList(new PrologSymbol(SymbolCodes.symbolCode_E_bold),result);
			};
			return result;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term standardizeFontStyleValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			return termDefault;
		} else {
			try {
				termToFontStyle(value,iX);
				return value.copyValue(iX,TermCircumscribingMode.CIRCUMSCRIBE_FREE_VARIABLES);
			} catch (TermIsSymbolDefault e) {
				return termDefault;
			} catch (IsNotFontStyle e) {
				throw RejectValue.instance;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// Auxiliary operations
	///////////////////////////////////////////////////////////////
	//
	protected static int termToFontStyle(Term value, ChoisePoint iX) throws TermIsSymbolDefault, IsNotFontStyle {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				throw TermIsSymbolDefault.instance;
			} else if (code==SymbolCodes.symbolCode_E_bold) {
				return Font.BOLD;
			} else if (code==SymbolCodes.symbolCode_E_italic) {
				return Font.ITALIC;
			} else if (code==SymbolCodes.symbolCode_E_underlined) {
				return Font.PLAIN;
			} else {
				throw IsNotFontStyle.instance;
			}
		} catch (TermIsNotASymbol e1) {
			try {
				int numberOfItems= 0;
				boolean isBold= false;
				boolean isItalic= false;
				try {
					while (true) {
						Term head= value.getNextListHead(iX);
						numberOfItems= numberOfItems + 1;
						long code= head.getSymbolValue(iX);
						if (code==SymbolCodes.symbolCode_E_default) {
							throw TermIsSymbolDefault.instance;
						} else if (code==SymbolCodes.symbolCode_E_bold) {
							isBold= true;
						} else if (code==SymbolCodes.symbolCode_E_italic) {
							isItalic= true;
						} else if (code==SymbolCodes.symbolCode_E_underlined) {
						} else {
							throw IsNotFontStyle.instance;
						};
						value= value.getNextListTail(iX);
					}
				} catch (EndOfList e2) {
				};
				if (numberOfItems==0) {
					return Font.PLAIN;
				} else if (isBold && isItalic) {
					return Font.BOLD + Font.ITALIC;
				} else if (isBold) {
					return Font.BOLD;
				} else if (isItalic) {
					return Font.ITALIC;
				} else {
					return Font.PLAIN;
				}
			} catch (TermIsNotAList e3) {
				throw IsNotFontStyle.instance;
			} catch (TermIsNotASymbol e4) {
				throw IsNotFontStyle.instance;
			}
		}
	}
	//
	protected static boolean fontIsUnderlined(Term value, ChoisePoint iX) throws TermIsSymbolDefault, IsNotFontStyle {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				throw TermIsSymbolDefault.instance;
			} else if (code==SymbolCodes.symbolCode_E_bold) {
			} else if (code==SymbolCodes.symbolCode_E_italic) {
			} else if (code==SymbolCodes.symbolCode_E_underlined) {
				return true;
			} else {
				throw IsNotFontStyle.instance;
			}
		} catch (TermIsNotASymbol e1) {
			try {
				try {
					while (true) {
						Term head= value.getNextListHead(iX);
						try {
							long code= head.getSymbolValue(iX);
							if (code==SymbolCodes.symbolCode_E_default) {
								throw TermIsSymbolDefault.instance;
							} else if (code==SymbolCodes.symbolCode_E_bold) {
							} else if (code==SymbolCodes.symbolCode_E_italic) {
							} else if (code==SymbolCodes.symbolCode_E_underlined) {
								return true;
							} else {
								throw IsNotFontStyle.instance;
							};
							value= value.getNextListTail(iX);
						} catch (TermIsNotASymbol e2) {
							throw IsNotFontStyle.instance;
						}
					}
				} catch (EndOfList e2) {
				};
				return false;
			} catch (TermIsNotAList e3) {
				throw IsNotFontStyle.instance;
			}
		};
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (useDefaultFontStyle) {
			return termDefault;
		} else {
			return fontStyleToTerm(isBold,isItalic,isUnderlined);
		}
	}
	//
	public String toString() {
		return "(" +
			String.format("%B,",useDefaultFontStyle) +
			String.format("%B,",isBold) +
			String.format("%B,",isItalic) +
			String.format("%B",isUnderlined) +
			")";
	}
}
