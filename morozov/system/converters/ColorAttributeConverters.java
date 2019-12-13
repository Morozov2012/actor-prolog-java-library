// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.system.converters.signals.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Color;
import javax.swing.UIManager;
import java.math.BigInteger;

public class ColorAttributeConverters {
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
	///////////////////////////////////////////////////////////////
	//
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static ColorAttribute argumentToColorAttributeSafe(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			return new ColorAttribute();
		} else {
			try {
				return argumentToColorAttribute(value,iX);
			} catch (RuntimeException e) {
				return new ColorAttribute();
			}
		}
	}
	//
	public static Term argumentToColorAttributeOrFail(Term value, ChoisePoint iX) throws Backtracking {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable()) {
			throw Backtracking.instance;
		} else if (value.thisIsUnknownValue()) {
			return termDefault;
		} else {
			try {
				return colorToTerm(argumentToColor(value,iX));
			} catch (TermIsSymbolDefault e) {
				return termDefault;
			} catch (RuntimeException e) {
				return termDefault;
			}
		}
	}
	//
	public static ColorAttribute argumentToColorAttribute(Term value, ChoisePoint iX) {
		try {
			return new ColorAttribute(argumentToColor(value,iX));
		} catch (TermIsSymbolDefault e) {
			return new ColorAttribute();
		}
	}
	//
	public static Color argumentToColorSafe(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable() || value.thisIsUnknownValue()) {
			throw TermIsSymbolDefault.instance;
		} else {
			try {
				return argumentToColor(value,iX);
			} catch (RuntimeException e) {
				throw TermIsSymbolDefault.instance;
			}
		}
	}
	//
	public static Color argumentToColor(Term value, ChoisePoint iX) throws TermIsSymbolDefault {
		try {
			long code= value.getSymbolValue(iX);
			try {
				return symbolCodeToColor(code);
			} catch (IsNotColorSymbolCode e1) {
				throw new WrongArgumentIsNotAColor(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				return new Color(value.getIntegerValue(iX).intValue());
			} catch (TermIsNotAnInteger e2) {
				try {
					return new Color(Arithmetic.toInteger(value.getRealValue(iX)));
				} catch (TermIsNotAReal e3) {
					try {
						String colorName= value.getStringValue(iX);
						return stringToColor(colorName);
					} catch (TermIsNotAString e4) {
						try { // color3
							Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_color3,3,iX);
							float r= (float)GeneralConverters.argumentToReal(arguments[0],iX);
							float g= (float)GeneralConverters.argumentToReal(arguments[1],iX);
							float b= (float)GeneralConverters.argumentToReal(arguments[2],iX);
							return new Color(r,g,b);
						} catch (Backtracking b1) {
							try { // color4
								Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_color4,4,iX);
								float r= (float)GeneralConverters.argumentToReal(arguments[0],iX);
								float g= (float)GeneralConverters.argumentToReal(arguments[1],iX);
								float b= (float)GeneralConverters.argumentToReal(arguments[2],iX);
								float a= (float)GeneralConverters.argumentToReal(arguments[3],iX);
								return new Color(r,g,b,a);
							} catch (Backtracking b2) {
								throw new WrongArgumentIsNotAColor(value);
							}
						}
					}
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term standardizeColorValue(Term value, ChoisePoint iX) throws RejectValue {
		value= value.dereferenceValue(iX);
		if (value.thisIsUnknownValue()) {
			return termDefault;
		} else {
			try {
				long code= value.getSymbolValue(iX);
				try {
					symbolCodeToColor(code);
					return new PrologSymbol(code);
				} catch (TermIsSymbolDefault e1) {
					return termDefault;
				} catch (IsNotColorSymbolCode e1) {
					throw RejectValue.instance;
				}
			} catch (TermIsNotASymbol e1) {
				try {
					BigInteger bigInteger= value.getIntegerValue(iX);
					return new PrologInteger(bigInteger);
				} catch (TermIsNotAnInteger e2) {
					try {
						double number= StrictMath.round(value.getRealValue(iX));
						BigInteger bigInteger= GeneralConverters.doubleToBigInteger(number);
						return new PrologInteger(bigInteger.intValue());
					} catch (TermIsNotAReal e3) {
						try {
							String colorName= value.getStringValue(iX);
							try {
								BigInteger number= GeneralConverters.stringToRoundInteger(colorName);
								return new PrologInteger(number);
							} catch (TermIsNotAnInteger e4) {
								try {
									long code= nameToColorSymbolCode(colorName);
									symbolCodeToColor(code);
									return new PrologSymbol(code);
								} catch (TermIsSymbolDefault e5) {
									return termDefault;
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
	///////////////////////////////////////////////////////////////
	// Auxiliary operations
	///////////////////////////////////////////////////////////////
	//
	protected static Color symbolCodeToColor(long code) throws TermIsSymbolDefault, IsNotColorSymbolCode {
		if (code==SymbolCodes.symbolCode_E_default) {
			throw TermIsSymbolDefault.instance;
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
			throw IsNotColorSymbolCode.instance;
		}
	}
	//
	protected static Color stringToColor(String colorName) throws TermIsSymbolDefault {
		try {
			BigInteger number= GeneralConverters.stringToRoundInteger(colorName);
			return new Color(number.intValue());
		} catch (TermIsNotAnInteger e1) {
			try {
				return symbolCodeToColor(nameToColorSymbolCode(colorName));
			} catch (IsNotColorName e2) {
				throw new WrongArgumentIsNotAColor(new PrologString(colorName));
			} catch (IsNotColorSymbolCode e2) {
				throw new WrongArgumentIsNotAColor(new PrologString(colorName));
			}
		}
	}
	//
	protected static long nameToColorSymbolCode(String colorName) throws TermIsSymbolDefault, IsNotColorName {
		colorName= colorName.toUpperCase();
		switch (colorName) {
		case "DEFAULT":
			throw TermIsSymbolDefault.instance;
		case "RED":
			return SymbolCodes.symbolCode_E_Red;
		case "GREEN":
			return SymbolCodes.symbolCode_E_Green;
		case "DKGREEN":
			return SymbolCodes.symbolCode_E_DkGreen;
		case "BLUE":
			return SymbolCodes.symbolCode_E_Blue;
		case "CYAN":
			return SymbolCodes.symbolCode_E_Cyan;
		case "MAGENTA":
			return SymbolCodes.symbolCode_E_Magenta;
		case "YELLOW":
			return SymbolCodes.symbolCode_E_Yellow;
		case "BLACK":
			return SymbolCodes.symbolCode_E_Black;
		case "DKGRAY":
			return SymbolCodes.symbolCode_E_DkGray;
		case "GRAY":
			return SymbolCodes.symbolCode_E_Gray;
		case "LTGRAY":
			return SymbolCodes.symbolCode_E_LtGray;
		case "SYSTEMCONTROL":
			return SymbolCodes.symbolCode_E_SystemControl;
		case "WHITE":
			return SymbolCodes.symbolCode_E_White;
		case "ORANGE":
			return SymbolCodes.symbolCode_E_Orange;
		case "PINK":
			return SymbolCodes.symbolCode_E_Pink;
		case "VIOLET":
			return SymbolCodes.symbolCode_E_Violet;
		case "BROWN":
			return SymbolCodes.symbolCode_E_Brown;
		case "SILVER":
			return SymbolCodes.symbolCode_E_Silver;
		case "MAROON":
			return SymbolCodes.symbolCode_E_Maroon;
		case "PURPLE":
			return SymbolCodes.symbolCode_E_Purple;
		case "FUCHSIA":
			return SymbolCodes.symbolCode_E_Fuchsia;
		case "LIME":
			return SymbolCodes.symbolCode_E_Lime;
		case "OLIVE":
			return SymbolCodes.symbolCode_E_Olive;
		case "NAVY":
			return SymbolCodes.symbolCode_E_Navy;
		case "TEAL":
			return SymbolCodes.symbolCode_E_Teal;
		case "AQUA":
			return SymbolCodes.symbolCode_E_Aqua;
		case "TERRACOTTA":
			return SymbolCodes.symbolCode_E_Terracotta;
		case "LILAC":
			return SymbolCodes.symbolCode_E_Lilac;
		case "GOLD":
			return SymbolCodes.symbolCode_E_Gold;
		case "INDIGO":
			return SymbolCodes.symbolCode_E_Indigo;
		case "ROYALBLUE":
			return SymbolCodes.symbolCode_E_RoyalBlue;
		case "LEMON":
			return SymbolCodes.symbolCode_E_Lemon;
		case "EMERALD":
			return SymbolCodes.symbolCode_E_Emerald;
		case "SKYBLUE":
			return SymbolCodes.symbolCode_E_SkyBlue;
		default:
			throw IsNotColorName.instance;
		}
	}
	//
	public static Term colorToTerm(Color value) {
		int code= 0;
		boolean isStandardColor= true;
		if (value.equals(colorRed)) {
			code= SymbolCodes.symbolCode_E_Red;
		} else if (value.equals(colorGreen)) {
			code= SymbolCodes.symbolCode_E_Green;
		} else if (value.equals(colorDkGreen)) {
			code= SymbolCodes.symbolCode_E_DkGreen;
		} else if (value.equals(colorBlue)) {
			code= SymbolCodes.symbolCode_E_Blue;
		} else if (value.equals(colorCyan)) {
			code= SymbolCodes.symbolCode_E_Cyan;
		} else if (value.equals(colorMagenta)) {
			code= SymbolCodes.symbolCode_E_Magenta;
		} else if (value.equals(colorYellow)) {
			code= SymbolCodes.symbolCode_E_Yellow;
		} else if (value.equals(colorBlack)) {
			code= SymbolCodes.symbolCode_E_Black;
		} else if (value.equals(colorDkGray)) {
			code= SymbolCodes.symbolCode_E_DkGray;
		} else if (value.equals(colorGray)) {
			code= SymbolCodes.symbolCode_E_Gray;
		} else if (value.equals(colorLtGray)) {
			code= SymbolCodes.symbolCode_E_LtGray;
		} else if (value.equals(colorSystemControl)) {
			code= SymbolCodes.symbolCode_E_SystemControl;
		} else if (value.equals(colorWhite)) {
			code= SymbolCodes.symbolCode_E_White;
		} else if (value.equals(colorOrange)) {
			code= SymbolCodes.symbolCode_E_Orange;
		} else if (value.equals(colorPink)) {
			code= SymbolCodes.symbolCode_E_Pink;
		} else if (value.equals(colorViolet)) {
			code= SymbolCodes.symbolCode_E_Violet;
		} else if (value.equals(colorBrown)) {
			code= SymbolCodes.symbolCode_E_Brown;
		} else if (value.equals(colorSilver)) {
			code= SymbolCodes.symbolCode_E_Silver;
		} else if (value.equals(colorMaroon)) {
			code= SymbolCodes.symbolCode_E_Maroon;
		} else if (value.equals(colorPurple)) {
			code= SymbolCodes.symbolCode_E_Purple;
		} else if (value.equals(colorFuchsia)) {
			code= SymbolCodes.symbolCode_E_Fuchsia;
		} else if (value.equals(colorLime)) {
			code= SymbolCodes.symbolCode_E_Lime;
		} else if (value.equals(colorOlive)) {
			code= SymbolCodes.symbolCode_E_Olive;
		} else if (value.equals(colorNavy)) {
			code= SymbolCodes.symbolCode_E_Navy;
		} else if (value.equals(colorTeal)) {
			code= SymbolCodes.symbolCode_E_Teal;
		} else if (value.equals(colorAqua)) {
			code= SymbolCodes.symbolCode_E_Aqua;
		} else if (value.equals(colorTerracotta)) {
			code= SymbolCodes.symbolCode_E_Terracotta;
		} else if (value.equals(colorLilac)) {
			code= SymbolCodes.symbolCode_E_Lilac;
		} else if (value.equals(colorGold)) {
			code= SymbolCodes.symbolCode_E_Gold;
		} else if (value.equals(colorIndigo)) {
			code= SymbolCodes.symbolCode_E_Indigo;
		} else if (value.equals(colorRoyalBlue)) {
			code= SymbolCodes.symbolCode_E_RoyalBlue;
		} else if (value.equals(colorLemon)) {
			code= SymbolCodes.symbolCode_E_Lemon;
		} else if (value.equals(colorEmerald)) {
			code= SymbolCodes.symbolCode_E_Emerald;
		} else if (value.equals(colorSkyBlue)) {
			code= SymbolCodes.symbolCode_E_SkyBlue;
		} else {
			isStandardColor= false;
		};
		if (isStandardColor) {
			return new PrologSymbol(code);
		} else {
			int red= value.getRed();
			int green= value.getGreen();
			int blue= value.getBlue();
			int alpha= value.getAlpha();
			if (alpha==255) {
				Term[] arguments= new Term[3];
				arguments[0]= new PrologInteger(red);
				arguments[1]= new PrologInteger(green);
				arguments[2]= new PrologInteger(blue);
				return new PrologStructure(SymbolCodes.symbolCode_E_color3,arguments);
			} else {
				Term[] arguments= new Term[4];
				arguments[0]= new PrologInteger(red);
				arguments[1]= new PrologInteger(green);
				arguments[2]= new PrologInteger(blue);
				arguments[3]= new PrologInteger(alpha);
				return new PrologStructure(SymbolCodes.symbolCode_E_color4,arguments);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(ColorAttribute attribute) {
		try {
			return colorToTerm(attribute.getValue());
		} catch (UseDefaultColor e) {
			return termDefault;
		}
	}
}
