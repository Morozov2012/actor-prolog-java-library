// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import target.*;

import morozov.system.*;
import morozov.terms.*;

import javax.swing.UIManager;
import java.awt.*;
import java.math.BigInteger;

public class GUI_Utils {
	//
	public static final Color colorRed= Color.red;
	//
	public static final Color colorGreen= Color.green;
	//
	// public static final Color colorDkGreen= new Color(0x2F4F2F); // Dark Green (N), http://web.njit.edu/~kevin/rgb.txt.html
	// public static final Color colorDkGreen= new Color(0x006400); // Dark Green (X), http://web.njit.edu/~kevin/rgb.txt.html
	// public static final Color colorDkGreen= (Color.green).darker();
	public static final Color colorDkGreen= new Color(0x0000B200); // Photoshop: (Color.green).darker();
	//
	public static final Color colorBlue= Color.blue;
	//
	public static final Color colorCyan= Color.cyan;
	//
	public static final Color colorMagenta= Color.magenta;
	//
	// public static final Color colorYellow= Color.yellow; // JDK 1.6.0_14
	// public static final Color colorYellow= new Color(0x00FFC800); // Photoshop: Color.orange, JDK 1.6.0_14
	// public static final Color colorYellow= new Color(0x00FFCC33); // Web color (Photoshop)
	public static final Color colorYellow= new Color(0x00FEF037); // TOYO 10524** (Photoshop)
	//
	public static final Color colorBlack= Color.black;
	//
	public static final Color colorDkGray= Color.darkGray;
	public static final Color colorGray= Color.gray;
	public static final Color colorLtGray= Color.lightGray;
	//
	public static final Color colorSystemControl= UIManager.getColor("control");
	//
	public static final Color colorWhite= Color.white;
	//
	// public static final Color colorOrange= Color.orange; // JDK 1.6.0_14
	public static final Color colorOrange= new Color(0x00FF6F00); // Orange, http://www.brobstsystems.com/colors1.htm
	//
	public static final Color colorPink= Color.pink;
	//
	// public static final Color colorViolet= new Color(0x008D38C9); // Violet, http://www.computerhope.com/htmcolor.htm
	// public static final Color colorViolet= Color.magenta.darker();
	public static final Color colorViolet= new Color(0x00990099); // Photoshop: Color.magenta.darker();
	//
	// public static final Color colorBrown= Color.orange.darker(); // Photoshop: 00B28C00
	// public static final Color colorBrown= new Color(0x00963939); // Brown, http://www.brobstsystems.com/colors1.htm
	// public static final Color colorBrown= new Color(0x00804000); // Brown, http://www.computerhope.com/htmcolor.htm
	public static final Color colorBrown= new Color(0x006C3306); // Chocolate(Baker's), http://www.brobstsystems.com/colors1.htm
	//
	// public static final Color colorSilver= new Color(0x00C0C0C0); // Silver, http://www.december.com/html/spec/color0.html
	// public static final Color colorSilver= new Color(0x00969485); // Photo, Expert opinion
	// public static final Color colorSilver= new Color(0x00C8C6B8); // Photo, Expert opinion
	public static final Color colorSilver= new Color(0x00C5BEB1); // Photo, Expert opinion
	//
	// public static final Color colorMaroon= new Color(0x00B03060);
	// public static final Color colorMaroon= new Color(0x00810541); // Maroon, http://www.computerhope.com/htmcolor.htm
	public static final Color colorMaroon= new Color(0x00660044); // Expert opinion
	//
	// public static final Color colorPurple= new Color(0x00A020F0); // Purple (X), http://web.njit.edu/~kevin/rgb.txt.html
	// public static final Color colorPurple= new Color(0x008E35EF); // Purple, http://www.computerhope.com/htmcolor.htm
	public static final Color colorPurple= new Color(0x00990033); // Expert opinion
	//
	// public static final Color colorFuchsia= new Color(0x00FF00FF); // Fuchsia (V), http://web.njit.edu/~kevin/rgb.txt.html
	public static final Color colorFuchsia= new Color(0x00FF00AA); // Fuchsia2(Hex3), http://www.december.com/html/spec/color4.html
	//
	// public static final Color colorLime= new Color(0x0032CD32);
	// public static final Color colorLime= new Color(0x0041A317);
	public static final Color colorLime= new Color(0x00BFFF00); // http://en.wikipedia.org/wiki/Lime_(color)
	//
	// public static final Color colorOlive= new Color(0x00667C26); // Dark Olive Green4, http://www.computerhope.com/htmcolor.htm
	// public static final Color colorOlive= new Color(0x00808000); // Olive (V), http://web.njit.edu/~kevin/rgb.txt.html
	// public static final Color colorOlive= new Color(0x004F4F2F); // Dark Olive Green (N), http://web.njit.edu/~kevin/rgb.txt.html
	public static final Color colorOlive= new Color(0x007A7D54); // Expert opinion
	//
	// public static final Color colorNavy= new Color(0x0033339F); // Blue(Navy), http://www.december.com/html/spec/color3.html
	public static final Color colorNavy= new Color(0x00000080); // Navy, http://www.december.com/html/spec/color3.html
	//
	// public static final Color colorTeal= new Color(0x00008080); // Teal (V), http://web.njit.edu/~kevin/rgb.txt.html
	public static final Color colorTeal= new Color(0x00009090); // Teal, http://www.brobstsystems.com/colors1.htm
	//
	// public static final Color colorAqua= new Color(0x0000FFFF); // Teal (V), http://web.njit.edu/~kevin/rgb.txt.html
	public static final Color colorAqua= new Color(0x0066CCCC); // Aqua(Safe Hex3), http://www.december.com/html/spec/color3.html
	//
	public static final Color colorTerracotta= new Color(0x009F3333); // Firebrick (N), http://web.njit.edu/~kevin/rgb.txt.html
	//
	// public static final Color colorLilac= new Color(0x00663366); // Expert opinion
	public static final Color colorLilac= new Color(0x00996699); // Expert opinion
	//
	// public static final Color colorGold= new Color(0x00CD7F32); // Gold (N), http://web.njit.edu/~kevin/rgb.txt.html
	// public static final Color colorGold= new Color(0x00CFB53B); // Old Gold (N), http://web.njit.edu/~kevin/rgb.txt.html
	public static final Color colorGold= new Color(0x00DB9146); // Photo, Expert opinion
	//
	// public static final Color colorIndigo= new Color(0x004B0082); // Indigo(SVG), http://www.december.com/html/spec/color3.html
	public static final Color colorIndigo= new Color(0x002E0854); // Indigo, http://www.december.com/html/spec/color3.html
	//
	public static final Color colorRoyalBlue= new Color(0x0015317E); // Royal Blue4, http://www.computerhope.com/htmcolor.htm
	//
	// public static final Color colorLemon= Color.yellow; // JKD 1.6.0_14
	public static final Color colorLemon= new Color(0x00FFFF00); // Photoshop: Color.yellow, JKD 1.6.0_14
	//
	// public static final Color colorEmerald= new Color(0x00009900); // irish flag(Safe Hex3), http://www.december.com/html/spec/color2.html
	public static final Color colorEmerald= new Color(0x0000C957); // emeraldgreen, http://www.december.com/html/spec/color2.html
	//
	public static final Color colorSkyBlue= new Color(0x006698FF); // Sky Blue, http://www.computerhope.com/htmcolor.htm
	//
	// public static long oneDayLengthLong= 24 * 60 * 60 * 1000;
	// public static BigInteger oneDayLengthBig= BigInteger.valueOf(oneDayLengthLong);
	//
	public static Color termToColorSafe(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw new TermIsSymbolDefault();
		} else {
			return termToColor(value,iX);
		}
	}
	public static Color termToColor(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			long code= value.getSymbolValue(iX);
			try {
				return symbolCodeToColor(code);
			} catch (IsNotColorSymbolCode e1) {
				throw new WrongTermIsNotAColor(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return new Color(value.getIntegerValue(iX).intValue());
			} catch (TermIsNotAnInteger e2) {
				try {
					return new Color((int)StrictMath.round(value.getRealValue(iX)));
				} catch (TermIsNotAReal e3) {
					try {
						String colorName= value.getStringValue(iX);
						return stringToColor(colorName);
					} catch (TermIsNotAString e4) {
						throw new WrongTermIsNotAColor(value);
					}
				}
			}
		}
	}
	public static Color symbolCodeToColor(long code) throws TermIsSymbolDefault, IsNotColorSymbolCode {
		if (code==SymbolCodes.symbolCode_E_default) {
			throw new TermIsSymbolDefault();
		} else if (code==SymbolCodes.symbolCode_E_Red) {
			return colorRed;
		} else if (code==SymbolCodes.symbolCode_E_Green) {
			return colorGreen;
		} else if (code==SymbolCodes.symbolCode_E_DkGreen) {
			return colorDkGreen;
		} else if (code==SymbolCodes.symbolCode_E_Blue) {
			return colorBlue;
		} else if (code==SymbolCodes.symbolCode_E_Cyan) {
			return colorCyan;
		} else if (code==SymbolCodes.symbolCode_E_Magenta) {
			return colorMagenta;
		} else if (code==SymbolCodes.symbolCode_E_Yellow) {
			// return Color.yellow;
			return colorYellow;
		} else if (code==SymbolCodes.symbolCode_E_Black) {
			return colorBlack;
		} else if (code==SymbolCodes.symbolCode_E_DkGray) {
			return colorDkGray;
		} else if (code==SymbolCodes.symbolCode_E_Gray) {
			return colorGray;
		} else if (code==SymbolCodes.symbolCode_E_LtGray) {
			return colorLtGray;
		} else if (code==SymbolCodes.symbolCode_E_SystemControl) {
			return colorSystemControl;
		} else if (code==SymbolCodes.symbolCode_E_White) {
			return colorWhite;
		} else if (code==SymbolCodes.symbolCode_E_Orange) {
			// return Color.orange;
			return colorOrange;
		} else if (code==SymbolCodes.symbolCode_E_Pink) {
			return colorPink;
		} else if (code==SymbolCodes.symbolCode_E_Violet) {
			return colorViolet;
		} else if (code==SymbolCodes.symbolCode_E_Brown) {
			return colorBrown;
		} else if (code==SymbolCodes.symbolCode_E_Silver) {
			return colorSilver;
		} else if (code==SymbolCodes.symbolCode_E_Maroon) {
			return colorMaroon;
		} else if (code==SymbolCodes.symbolCode_E_Purple) {
			return colorPurple;
		} else if (code==SymbolCodes.symbolCode_E_Fuchsia) {
			return colorFuchsia;
		} else if (code==SymbolCodes.symbolCode_E_Lime) {
			return colorLime;
		} else if (code==SymbolCodes.symbolCode_E_Olive) {
			return colorOlive;
		} else if (code==SymbolCodes.symbolCode_E_Navy) {
			return colorNavy;
		} else if (code==SymbolCodes.symbolCode_E_Teal) {
			return colorTeal;
		} else if (code==SymbolCodes.symbolCode_E_Aqua) {
			return colorAqua;
		} else if (code==SymbolCodes.symbolCode_E_Terracotta) {
			return colorTerracotta;
		} else if (code==SymbolCodes.symbolCode_E_Lilac) {
			return colorLilac;
		} else if (code==SymbolCodes.symbolCode_E_Gold) {
			return colorGold;
		} else if (code==SymbolCodes.symbolCode_E_Indigo) {
			return colorIndigo;
		} else if (code==SymbolCodes.symbolCode_E_RoyalBlue) {
			return colorRoyalBlue;
		} else if (code==SymbolCodes.symbolCode_E_Lemon) {
			return colorLemon;
		} else if (code==SymbolCodes.symbolCode_E_Emerald) {
			return colorEmerald;
		} else if (code==SymbolCodes.symbolCode_E_SkyBlue) {
			return colorSkyBlue;
		} else {
			throw new IsNotColorSymbolCode();
		}
	}
	//
	public static Color stringToColor(String colorName) throws TermIsSymbolDefault {
		try {
			BigInteger number= Converters.stringToRoundInteger(colorName);
			return new Color(number.intValue());
		// } catch (NumberFormatException e1) {
		} catch (TermIsNotAnInteger e1) {
			try {
				return symbolCodeToColor(nameToColorSymbolCode(colorName));
			} catch (IsNotColorName e2) {
				throw new WrongTermIsNotAColor(new PrologString(colorName));
			} catch (IsNotColorSymbolCode e2) {
				throw new WrongTermIsNotAColor(new PrologString(colorName));
			}
		}
	}
	public static long nameToColorSymbolCode(String colorName) throws TermIsSymbolDefault, IsNotColorName {
		colorName= colorName.toUpperCase();
		if (colorName.equals("DEFAULT")) {
			throw new TermIsSymbolDefault();
		} else if (colorName.equals("RED")) {
			return SymbolCodes.symbolCode_E_Red;
		} else if (colorName.equals("GREEN")) {
			return SymbolCodes.symbolCode_E_Green;
		} else if (colorName.equals("DKGREEN")) {
			return SymbolCodes.symbolCode_E_DkGreen;
		} else if (colorName.equals("BLUE")) {
			return SymbolCodes.symbolCode_E_Blue;
		} else if (colorName.equals("CYAN")) {
			return SymbolCodes.symbolCode_E_Cyan;
		} else if (colorName.equals("MAGENTA")) {
			return SymbolCodes.symbolCode_E_Magenta;
		} else if (colorName.equals("YELLOW")) {
			return SymbolCodes.symbolCode_E_Yellow;
		} else if (colorName.equals("BLACK")) {
			return SymbolCodes.symbolCode_E_Black;
		} else if (colorName.equals("DKGRAY")) {
			return SymbolCodes.symbolCode_E_DkGray;
		} else if (colorName.equals("GRAY")) {
			return SymbolCodes.symbolCode_E_Gray;
		} else if (colorName.equals("LTGRAY")) {
			return SymbolCodes.symbolCode_E_LtGray;
		} else if (colorName.equals("SYSTEMCONTROL")) {
			return SymbolCodes.symbolCode_E_SystemControl;
		} else if (colorName.equals("WHITE")) {
			return SymbolCodes.symbolCode_E_White;
		} else if (colorName.equals("ORANGE")) {
			return SymbolCodes.symbolCode_E_Orange;
		} else if (colorName.equals("PINK")) {
			return SymbolCodes.symbolCode_E_Pink;
		} else if (colorName.equals("VIOLET")) {
			return SymbolCodes.symbolCode_E_Violet;
		} else if (colorName.equals("BROWN")) {
			return SymbolCodes.symbolCode_E_Brown;
		} else if (colorName.equals("SILVER")) {
			return SymbolCodes.symbolCode_E_Silver;
		} else if (colorName.equals("MAROON")) {
			return SymbolCodes.symbolCode_E_Maroon;
		} else if (colorName.equals("PURPLE")) {
			return SymbolCodes.symbolCode_E_Purple;
		} else if (colorName.equals("FUCHSIA")) {
			return SymbolCodes.symbolCode_E_Fuchsia;
		} else if (colorName.equals("LIME")) {
			return SymbolCodes.symbolCode_E_Lime;
		} else if (colorName.equals("OLIVE")) {
			return SymbolCodes.symbolCode_E_Olive;
		} else if (colorName.equals("NAVY")) {
			return SymbolCodes.symbolCode_E_Navy;
		} else if (colorName.equals("TEAL")) {
			return SymbolCodes.symbolCode_E_Teal;
		} else if (colorName.equals("AQUA")) {
			return SymbolCodes.symbolCode_E_Aqua;
		} else if (colorName.equals("TERRACOTTA")) {
			return SymbolCodes.symbolCode_E_Terracotta;
		} else if (colorName.equals("LILAC")) {
			return SymbolCodes.symbolCode_E_Lilac;
		} else if (colorName.equals("GOLD")) {
			return SymbolCodes.symbolCode_E_Gold;
		} else if (colorName.equals("INDIGO")) {
			return SymbolCodes.symbolCode_E_Indigo;
		} else if (colorName.equals("ROYALBLUE")) {
			return SymbolCodes.symbolCode_E_RoyalBlue;
		} else if (colorName.equals("LEMON")) {
			return SymbolCodes.symbolCode_E_Lemon;
		} else if (colorName.equals("EMERALD")) {
			return SymbolCodes.symbolCode_E_Emerald;
		} else if (colorName.equals("SKYBLUE")) {
			return SymbolCodes.symbolCode_E_SkyBlue;
		} else {
			throw new IsNotColorName();
		}
	}
	//
	public static String termToFontNameSafe(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw new TermIsSymbolDefault();
		} else {
			return termToFontName(value,iX);
		}
	}
	public static String termToFontName(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			long code= value.getSymbolValue(iX);
			try {
				return symbolCodeToFontName(code);
			} catch (IsNotFontNameSymbolCode e1) {
				throw new WrongTermIsNotFontName(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return value.getStringValue(iX);
			} catch (TermIsNotAString e2) {
				throw new WrongTermIsNotFontName(value);
			}
		}
	}
	public static String symbolCodeToFontName(long code) throws TermIsSymbolDefault, IsNotFontNameSymbolCode {
		if (code==SymbolCodes.symbolCode_E_default) {
			throw new TermIsSymbolDefault();
		} else if (code==SymbolCodes.symbolCode_E_system) {
			return Font.DIALOG;
		} else if (code==SymbolCodes.symbolCode_E_fixed) {
			return Font.MONOSPACED;
		} else if (code==SymbolCodes.symbolCode_E_times) {
			return Font.SERIF;
		} else if (code==SymbolCodes.symbolCode_E_helvetica) {
			return Font.SANS_SERIF;
		} else {
			throw new IsNotFontNameSymbolCode();
		}
	}
	//
	public static int termToFontSizeSafe(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw new TermIsSymbolDefault();
		} else {
			return termToFontSize(value,iX);
		}
	}
	public static int termToFontSize(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			return value.getIntegerValue(iX).intValue();
		} catch (TermIsNotAnInteger e1) {
			try {
				return (int)StrictMath.round(value.getRealValue(iX));
			} catch (TermIsNotAReal e2) {
				try {
					long code= value.getSymbolValue(iX);
					if (code==SymbolCodes.symbolCode_E_default) {
						throw new TermIsSymbolDefault();
					} else {
						throw new WrongTermIsNotFontSize(value);
					}
				} catch (TermIsNotASymbol e3) {
					throw new WrongTermIsNotFontSize(value);
				}
			}
		}
	}
	//
	public static int termToFontStyleSafe(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw new TermIsSymbolDefault();
		} else {
			try {
				return termToFontStyle(value,iX);
			} catch (IsNotFontStyle e) {
				throw new WrongTermIsNotFontStyle(value);
			}
		}
	}
	public static int termToFontStyle(Term value, ChoisePoint iX) throws TermIsSymbolDefault, IsNotFontStyle {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				throw new TermIsSymbolDefault();
			} else if (code==SymbolCodes.symbolCode_E_bold) {
				return Font.BOLD;
			} else if (code==SymbolCodes.symbolCode_E_italic) {
				return Font.ITALIC;
			} else if (code==SymbolCodes.symbolCode_E_underlined) {
				return Font.PLAIN;
			} else {
				throw new IsNotFontStyle();
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
							throw new TermIsSymbolDefault();
						} else if (code==SymbolCodes.symbolCode_E_bold) {
							isBold= true;
						} else if (code==SymbolCodes.symbolCode_E_italic) {
							isItalic= true;
						} else if (code==SymbolCodes.symbolCode_E_underlined) {
						} else {
							throw new IsNotFontStyle();
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
				throw new IsNotFontStyle();
			} catch (TermIsNotASymbol e4) {
				throw new IsNotFontStyle();
			}
		}
	}
	//
	public static boolean fontIsUnderlined(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				throw new TermIsSymbolDefault();
			} else if (code==SymbolCodes.symbolCode_E_bold) {
			} else if (code==SymbolCodes.symbolCode_E_italic) {
			} else if (code==SymbolCodes.symbolCode_E_underlined) {
				return true;
			} else {
				throw new WrongTermIsNotFontStyle(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				try {
					while (true) {
						Term head= value.getNextListHead(iX);
						try {
							long code= head.getSymbolValue(iX);
							if (code==SymbolCodes.symbolCode_E_default) {
								throw new TermIsSymbolDefault();
							} else if (code==SymbolCodes.symbolCode_E_bold) {
							} else if (code==SymbolCodes.symbolCode_E_italic) {
							} else if (code==SymbolCodes.symbolCode_E_underlined) {
								return true;
							} else {
								throw new WrongTermIsNotFontStyle(head);
							};
							value= value.getNextListTail(iX);
						} catch (TermIsNotASymbol e2) {
							throw new WrongTermIsNotFontStyle(head);
						}
					}
				} catch (EndOfList e2) {
				};
				return false;
			} catch (TermIsNotAList e3) {
				throw new WrongTermIsNotFontStyle(value);
			}
		};
		return false;
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
			Term result= new PrologEmptyList();
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
	public static ExtendedCoordinate termToCoordinateSafe(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			ExtendedCoordinate coordinate= new ExtendedCoordinate();
			coordinate.useDefaultLocation();
			return coordinate;
		} else {
			try {
				return termToCoordinate(value,iX);
			} catch (RuntimeException e) {
				ExtendedCoordinate coordinate= new ExtendedCoordinate();
				coordinate.useDefaultLocation();
				return coordinate;
			}
		}
	}
	public static ExtendedCoordinate termToCoordinate(Term value, ChoisePoint iX) {
		ExtendedCoordinate coordinate= new ExtendedCoordinate();
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				coordinate.useDefaultLocation();
				return coordinate;
			} else if (code==SymbolCodes.symbolCode_E_centered) {
				coordinate.centreFigure();
				return coordinate;
			} else {
				throw new WrongTermIsNotACoordinate(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				coordinate.setValue(value.getIntegerValue(iX).doubleValue());
				return coordinate;
			} catch (TermIsNotAnInteger e2) {
				try {
					coordinate.setValue(value.getRealValue(iX));
					return coordinate;
				} catch (TermIsNotAReal e3) {
					throw new WrongTermIsNotACoordinate(value);
				}
			}
		}
	}
	//
	public static ExtendedSize termToSize(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_default) {
				return new ExtendedSize();
			} else {
				throw new WrongTermIsNotASize(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return new ExtendedSize(value.getIntegerValue(iX).doubleValue());
			} catch (TermIsNotAnInteger e2) {
				try {
					return new ExtendedSize(value.getRealValue(iX));
				} catch (TermIsNotAReal e3) {
					throw new WrongTermIsNotASize(value);
				}
			}
		}

	}
}
