// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.errors.*;
import morozov.system.converters.signals.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class ColorSubstitutionModeConverters {
	//
	public static ColorSubstitutionMode argumentToColorSubstitutionMode(Term attributes, ChoisePoint iX) {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
		setEnd= setEnd.dereferenceValue(iX);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			ColorChannelSubstitution red= ColorChannelSubstitution.instanceDefault;
			ColorChannelSubstitution green= ColorChannelSubstitution.instanceDefault;
			ColorChannelSubstitution blue= ColorChannelSubstitution.instanceDefault;
			ColorChannelSubstitution hue= ColorChannelSubstitution.instanceDefault;
			ColorChannelSubstitution saturation= ColorChannelSubstitution.instanceDefault;
			ColorChannelSubstitution brightness= ColorChannelSubstitution.instanceDefault;
			boolean use_RGB_mode= false;
			boolean use_HSB_mode= false;
			Set<Long> nameList= setPositiveMap.keySet();
			Iterator<Long> iterator= nameList.iterator();
			while(iterator.hasNext()) {
				long key= iterator.next();
				long pairName= - key;
				Term pairValue= setPositiveMap.get(key);
				if (pairName==SymbolCodes.symbolCode_E_red) {
					red= argumentToColorChannelSubstitution(pairValue,iX);
					if (use_HSB_mode) {
						throw new TheHSBColorSubstitutionModeIsAlreadyAssigned(pairValue);
					};
					use_RGB_mode= true;
				} else if (pairName==SymbolCodes.symbolCode_E_green) {
					green= argumentToColorChannelSubstitution(pairValue,iX);
					if (use_HSB_mode) {
						throw new TheHSBColorSubstitutionModeIsAlreadyAssigned(pairValue);
					};
					use_RGB_mode= true;
				} else if (pairName==SymbolCodes.symbolCode_E_blue) {
					blue= argumentToColorChannelSubstitution(pairValue,iX);
					if (use_HSB_mode) {
						throw new TheHSBColorSubstitutionModeIsAlreadyAssigned(pairValue);
					};
					use_RGB_mode= true;
				} else if (pairName==SymbolCodes.symbolCode_E_hue) {
					hue= argumentToColorChannelSubstitution(pairValue,iX);
					if (use_RGB_mode) {
						throw new TheRGBColorSubstitutionModeIsAlreadyAssigned(pairValue);
					};
					use_HSB_mode= true;
				} else if (pairName==SymbolCodes.symbolCode_E_saturation) {
					saturation= argumentToColorChannelSubstitution(pairValue,iX);
					if (use_RGB_mode) {
						throw new TheRGBColorSubstitutionModeIsAlreadyAssigned(pairValue);
					};
					use_HSB_mode= true;
				} else if (pairName==SymbolCodes.symbolCode_E_brightness) {
					brightness= argumentToColorChannelSubstitution(pairValue,iX);
					if (use_RGB_mode) {
						throw new TheRGBColorSubstitutionModeIsAlreadyAssigned(pairValue);
					};
					use_HSB_mode= true;
				} else {
					throw new WrongArgumentIsUnknownColorSubstitutionModeAttribute(key);
				}
			};
			if (use_RGB_mode) {
				return new ColorSubstitutionMode(
					true,
					red,
					green,
					blue);
			} else if (use_HSB_mode) {
				return new ColorSubstitutionMode(
					false,
					hue,
					saturation,
					brightness);
			} else {
				return new ColorSubstitutionMode(
					true,
					red,
					green,
					blue);
			}
		} else {
			throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
		}
	}
	//
	public static ColorChannelSubstitution argumentToColorChannelSubstitution(Term value, ChoisePoint iX) {
		try {
			ColorChannelSubstitutionName name= termToColorChannelSubstitutionName(value,iX);
			return new ColorChannelSubstitution(name);
		} catch (TermIsNotColorChannelSubstitutionName e1) {
			try {
				return new ColorChannelSubstitution(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotColorChannelSubstitution(value);
			}
		}
	}
	//
	public static ColorChannelSubstitutionName termToColorChannelSubstitutionName(Term value, ChoisePoint iX) throws TermIsNotColorChannelSubstitutionName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_Red1) {
				return ColorChannelSubstitutionName.RED1;
			} else if (code==SymbolCodes.symbolCode_E_Green1) {
				return ColorChannelSubstitutionName.GREEN1;
			} else if (code==SymbolCodes.symbolCode_E_Blue1) {
				return ColorChannelSubstitutionName.BLUE1;
			} else if (code==SymbolCodes.symbolCode_E_Red2) {
				return ColorChannelSubstitutionName.RED2;
			} else if (code==SymbolCodes.symbolCode_E_Green2) {
				return ColorChannelSubstitutionName.GREEN2;
			} else if (code==SymbolCodes.symbolCode_E_Blue2) {
				return ColorChannelSubstitutionName.BLUE2;
			} else if (code==SymbolCodes.symbolCode_E_Hue1) {
				return ColorChannelSubstitutionName.HUE1;
			} else if (code==SymbolCodes.symbolCode_E_Saturation1) {
				return ColorChannelSubstitutionName.SATURATION1;
			} else if (code==SymbolCodes.symbolCode_E_Brightness1) {
				return ColorChannelSubstitutionName.BRIGHTNESS1;
			} else if (code==SymbolCodes.symbolCode_E_Hue2) {
				return ColorChannelSubstitutionName.HUE2;
			} else if (code==SymbolCodes.symbolCode_E_Saturation2) {
				return ColorChannelSubstitutionName.SATURATION2;
			} else if (code==SymbolCodes.symbolCode_E_Brightness2) {
				return ColorChannelSubstitutionName.BRIGHTNESS2;
			} else if (code==SymbolCodes.symbolCode_E_Zero) {
				return ColorChannelSubstitutionName.ZERO;
			} else if (code==SymbolCodes.symbolCode_E_Full) {
				return ColorChannelSubstitutionName.FULL;
			} else if (code==SymbolCodes.symbolCode_E_Half) {
				return ColorChannelSubstitutionName.HALF;
			} else if (code==SymbolCodes.symbolCode_E_default) {
				return ColorChannelSubstitutionName.DEFAULT;
			} else {
				throw TermIsNotColorChannelSubstitutionName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotColorChannelSubstitutionName.instance;
		}
	}
	//
	public static ColorChannelSubstitutionName stringToColorChannelSubstitutionName(String name, Term value) {
		name= name.toUpperCase();
		switch (name) {
		case "RED1":
			return ColorChannelSubstitutionName.RED1;
		case "GREEN1":
			return ColorChannelSubstitutionName.GREEN1;
		case "BLUE1":
			return ColorChannelSubstitutionName.BLUE1;
		case "RED2":
			return ColorChannelSubstitutionName.RED2;
		case "GREEN2":
			return ColorChannelSubstitutionName.GREEN2;
		case "BLUE2":
			return ColorChannelSubstitutionName.BLUE2;
		case "HUE1":
			return ColorChannelSubstitutionName.HUE1;
		case "SATURATION1":
			return ColorChannelSubstitutionName.SATURATION1;
		case "BRIGHTNESS1":
			return ColorChannelSubstitutionName.BRIGHTNESS1;
		case "HUE2":
			return ColorChannelSubstitutionName.HUE2;
		case "SATURATION2":
			return ColorChannelSubstitutionName.SATURATION2;
		case "BRIGHTNESS2":
			return ColorChannelSubstitutionName.BRIGHTNESS2;
		case "ZERO":
			return ColorChannelSubstitutionName.ZERO;
		case "FULL":
			return ColorChannelSubstitutionName.FULL;
		case "HALF":
			return ColorChannelSubstitutionName.HALF;
		case "DEFAULT":
			return ColorChannelSubstitutionName.DEFAULT;
		default:
			throw new WrongArgumentIsNotColorChannelSubstitutionName(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termRed1= new PrologSymbol(SymbolCodes.symbolCode_E_Red1);
	protected static Term termGreen1= new PrologSymbol(SymbolCodes.symbolCode_E_Green1);
	protected static Term termBlue1= new PrologSymbol(SymbolCodes.symbolCode_E_Blue1);
	protected static Term termRed2= new PrologSymbol(SymbolCodes.symbolCode_E_Red2);
	protected static Term termGreen2= new PrologSymbol(SymbolCodes.symbolCode_E_Green2);
	protected static Term termBlue2= new PrologSymbol(SymbolCodes.symbolCode_E_Blue2);
	protected static Term termHue1= new PrologSymbol(SymbolCodes.symbolCode_E_Hue1);
	protected static Term termSaturation1= new PrologSymbol(SymbolCodes.symbolCode_E_Saturation1);
	protected static Term termBrightness1= new PrologSymbol(SymbolCodes.symbolCode_E_Brightness1);
	protected static Term termHue2= new PrologSymbol(SymbolCodes.symbolCode_E_Hue2);
	protected static Term termSaturation2= new PrologSymbol(SymbolCodes.symbolCode_E_Saturation2);
	protected static Term termBrightness2= new PrologSymbol(SymbolCodes.symbolCode_E_Brightness2);
	protected static Term termZero= new PrologSymbol(SymbolCodes.symbolCode_E_Zero);
	protected static Term termFull= new PrologSymbol(SymbolCodes.symbolCode_E_Full);
	protected static Term termHalf= new PrologSymbol(SymbolCodes.symbolCode_E_Half);
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(ColorSubstitutionMode mode) {
		if (mode.isEmpty()) {
			return PrologEmptySet.instance;
		} else {
			Term result= PrologEmptySet.instance;
			if (mode.useRGBMode()) {
				ColorChannelSubstitution substitution= mode.getThirdChannelSubstitution();
				if (!substitution.isDefault()) {
					result= new PrologSet(-SymbolCodes.symbolCode_E_blue,toTerm(substitution),result);
				};
				substitution= mode.getSecondChannelSubstitution();
				if (!substitution.isDefault()) {
					result= new PrologSet(-SymbolCodes.symbolCode_E_green,toTerm(substitution),result);
				};
				substitution= mode.getFirstChannelSubstitution();
				if (!substitution.isDefault()) {
					result= new PrologSet(-SymbolCodes.symbolCode_E_red,toTerm(substitution),result);
				}
			} else {
				ColorChannelSubstitution substitution= mode.getThirdChannelSubstitution();
				if (!substitution.isDefault()) {
					result= new PrologSet(-SymbolCodes.symbolCode_E_brightness,toTerm(substitution),result);
				};
				substitution= mode.getSecondChannelSubstitution();
				if (!substitution.isDefault()) {
					result= new PrologSet(-SymbolCodes.symbolCode_E_saturation,toTerm(substitution),result);
				};
				substitution= mode.getFirstChannelSubstitution();
				if (!substitution.isDefault()) {
					result= new PrologSet(-SymbolCodes.symbolCode_E_hue,toTerm(substitution),result);
				}
			};
			return result;
		}
	}
	//
	public static Term toTerm(ColorChannelSubstitution substitution) {
		if (substitution.isNamed()) {
			return toTerm(substitution.getName());
		} else {
			return new PrologInteger(substitution.getValue());
		}
	}
	//
	public static Term toTerm(ColorChannelSubstitutionName value) {
		switch (value) {
		case RED1:
			return termRed1;
		case GREEN1:
			return termGreen1;
		case BLUE1:
			return termBlue1;
		case RED2:
			return termRed2;
		case GREEN2:
			return termGreen2;
		case BLUE2:
			return termBlue2;
		case HUE1:
			return termHue1;
		case SATURATION1:
			return termSaturation1;
		case BRIGHTNESS1:
			return termBrightness1;
		case HUE2:
			return termHue2;
		case SATURATION2:
			return termSaturation2;
		case BRIGHTNESS2:
			return termBrightness2;
		case ZERO:
			return termZero;
		case FULL:
			return termFull;
		case HALF:
			return termHalf;
		case DEFAULT:
			return termDefault;
		};
		throw new UnknownColorChannelSubstitutionName(value);
	}
}
