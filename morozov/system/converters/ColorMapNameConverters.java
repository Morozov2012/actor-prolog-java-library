// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class ColorMapNameConverters {
	//
	public static ColorMapName argumentToColorMapName(Term value, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			return symbolToColorMapName(code,value);
		} catch (TermIsNotASymbol e) {
			throw new WrongArgumentIsNotColorMapName(value);
		}
	}
	public static ColorMapName symbolToColorMapName(long code, Term value) {
		if (code==SymbolCodes.symbolCode_E_AQUA) {
			return ColorMapName.AQUA;
		} else if (code==SymbolCodes.symbolCode_E_AUTUMN) {
			return ColorMapName.AUTUMN;
		} if (code==SymbolCodes.symbolCode_E_BLACKHOT) {
			return ColorMapName.BLACKHOT;
		} else if (code==SymbolCodes.symbolCode_E_BLAZE) {
			return ColorMapName.BLAZE;
		} else if (code==SymbolCodes.symbolCode_E_BLUERED) {
			return ColorMapName.BLUERED;
		} else if (code==SymbolCodes.symbolCode_E_BONE) {
			return ColorMapName.BONE;
		} else if (code==SymbolCodes.symbolCode_E_BRIGHT_RAINBOW) {
			return ColorMapName.BRIGHT_RAINBOW;
		} else if (code==SymbolCodes.symbolCode_E_COOL) {
			return ColorMapName.COOL;
		} else if (code==SymbolCodes.symbolCode_E_COPPER) {
			return ColorMapName.COPPER;
		} else if (code==SymbolCodes.symbolCode_E_GRAY) {
			return ColorMapName.GRAY;
		} else if (code==SymbolCodes.symbolCode_E_GREEN) {
			return ColorMapName.GREEN;
		} else if (code==SymbolCodes.symbolCode_E_HOT) {
			return ColorMapName.HOT;
		} else if (code==SymbolCodes.symbolCode_E_HSV) {
			return ColorMapName.HSV;
		} else if (code==SymbolCodes.symbolCode_E_IRON) {
			return ColorMapName.IRON;
		} else if (code==SymbolCodes.symbolCode_E_JET) {
			return ColorMapName.JET;
		} else if (code==SymbolCodes.symbolCode_E_LIGHTJET) {
			return ColorMapName.LIGHTJET;
		} else if (code==SymbolCodes.symbolCode_E_MEDICAL) {
			return ColorMapName.MEDICAL;
		} else if (code==SymbolCodes.symbolCode_E_OCEAN) {
			return ColorMapName.OCEAN;
		} else if (code==SymbolCodes.symbolCode_E_PARULA) {
			return ColorMapName.PARULA;
		} else if (code==SymbolCodes.symbolCode_E_PINK) {
			return ColorMapName.PINK;
		} else if (code==SymbolCodes.symbolCode_E_PRISM) {
			return ColorMapName.PRISM;
		} else if (code==SymbolCodes.symbolCode_E_PURPLE) {
			return ColorMapName.PURPLE;
		} else if (code==SymbolCodes.symbolCode_E_RED) {
			return ColorMapName.RED;
		} else if (code==SymbolCodes.symbolCode_E_REPTILOID) {
			return ColorMapName.REPTILOID;
		} else if (code==SymbolCodes.symbolCode_E_SOFT_RAINBOW) {
			return ColorMapName.SOFT_RAINBOW;
		} else if (code==SymbolCodes.symbolCode_E_SPRING) {
			return ColorMapName.SPRING;
		} else if (code==SymbolCodes.symbolCode_E_SUMMER) {
			return ColorMapName.SUMMER;
		} else if (code==SymbolCodes.symbolCode_E_WINTER) {
			return ColorMapName.WINTER;
		} else if (code==SymbolCodes.symbolCode_E_OPTICAL) {
			return ColorMapName.OPTICAL;
		} else if (code==SymbolCodes.symbolCode_E_NONE) {
			return ColorMapName.NONE;
		} else {
			throw new WrongArgumentIsNotColorMapName(value);
		}
	}
	public static ColorMapName stringToColorMapName(String name, Term value) {
		name= name.toUpperCase();
		switch (name) {
		case "AQUA":
			return ColorMapName.AQUA;
		case "AUTUMN":
			return ColorMapName.AUTUMN;
		case "BLACKHOT":
			return ColorMapName.BLACKHOT;
		case "BLAZE":
			return ColorMapName.BLAZE;
		case "BLUERED":
			return ColorMapName.BLUERED;
		case "BONE":
			return ColorMapName.BONE;
		case "BRIGHTRAINBOW":
			return ColorMapName.BRIGHT_RAINBOW;
		case "BRIGHT RAINBOW":
			return ColorMapName.BRIGHT_RAINBOW;
		case "BRIGHT_RAINBOW":
			return ColorMapName.BRIGHT_RAINBOW;
		case "COOL":
			return ColorMapName.COOL;
		case "COPPER":
			return ColorMapName.COPPER;
		case "GRAY":
			return ColorMapName.GRAY;
		case "GREEN":
			return ColorMapName.GREEN;
		case "HOT":
			return ColorMapName.HOT;
		case "HSV":
			return ColorMapName.HSV;
		case "IRON":
			return ColorMapName.IRON;
		case "JET":
			return ColorMapName.JET;
		case "LIGHTJET":
			return ColorMapName.LIGHTJET;
		case "MEDICAL":
			return ColorMapName.MEDICAL;
		case "OCEAN":
			return ColorMapName.OCEAN;
		case "PARULA":
			return ColorMapName.PARULA;
		case "PINK":
			return ColorMapName.PINK;
		case "PRISM":
			return ColorMapName.PRISM;
		case "PURPLE":
			return ColorMapName.PURPLE;
		case "RED":
			return ColorMapName.RED;
		case "REPTILOID":
			return ColorMapName.REPTILOID;
		case "SOFTRAINBOW":
			return ColorMapName.SOFT_RAINBOW;
		case "SOFT RAINBOW":
			return ColorMapName.SOFT_RAINBOW;
		case "SOFT_RAINBOW":
			return ColorMapName.SOFT_RAINBOW;
		case "SPRING":
			return ColorMapName.SPRING;
		case "SUMMER":
			return ColorMapName.SUMMER;
		case "WINTER":
			return ColorMapName.WINTER;
		case "OPTICAL":
			return ColorMapName.OPTICAL;
		case "NONE":
			return ColorMapName.NONE;
		default:
			throw new WrongArgumentIsNotColorMapName(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termAqua= new PrologSymbol(SymbolCodes.symbolCode_E_AQUA);
	protected static Term termAutumn= new PrologSymbol(SymbolCodes.symbolCode_E_AUTUMN);
	protected static Term termBlackhot= new PrologSymbol(SymbolCodes.symbolCode_E_BLACKHOT);
	protected static Term termBlaze= new PrologSymbol(SymbolCodes.symbolCode_E_BLAZE);
	protected static Term termBlueRed= new PrologSymbol(SymbolCodes.symbolCode_E_BLUERED);
	protected static Term termBone= new PrologSymbol(SymbolCodes.symbolCode_E_BONE);
	protected static Term termBrightRainbow= new PrologSymbol(SymbolCodes.symbolCode_E_BRIGHT_RAINBOW);
	protected static Term termCool= new PrologSymbol(SymbolCodes.symbolCode_E_COOL);
	protected static Term termCopper= new PrologSymbol(SymbolCodes.symbolCode_E_COPPER);
	protected static Term termGray= new PrologSymbol(SymbolCodes.symbolCode_E_GRAY);
	protected static Term termGreen= new PrologSymbol(SymbolCodes.symbolCode_E_GREEN);
	protected static Term termHot= new PrologSymbol(SymbolCodes.symbolCode_E_HOT);
	protected static Term termHSV= new PrologSymbol(SymbolCodes.symbolCode_E_HSV);
	protected static Term termIron= new PrologSymbol(SymbolCodes.symbolCode_E_IRON);
	protected static Term termJet= new PrologSymbol(SymbolCodes.symbolCode_E_JET);
	protected static Term termLightJet= new PrologSymbol(SymbolCodes.symbolCode_E_LIGHTJET);
	protected static Term termMedical= new PrologSymbol(SymbolCodes.symbolCode_E_MEDICAL);
	protected static Term termOcean= new PrologSymbol(SymbolCodes.symbolCode_E_OCEAN);
	protected static Term termParula= new PrologSymbol(SymbolCodes.symbolCode_E_PARULA);
	protected static Term termPink= new PrologSymbol(SymbolCodes.symbolCode_E_PINK);
	protected static Term termPrism= new PrologSymbol(SymbolCodes.symbolCode_E_PRISM);
	protected static Term termPurple= new PrologSymbol(SymbolCodes.symbolCode_E_PURPLE);
	protected static Term termRed= new PrologSymbol(SymbolCodes.symbolCode_E_RED);
	protected static Term termReptiloid= new PrologSymbol(SymbolCodes.symbolCode_E_REPTILOID);
	protected static Term termSoftRainbow= new PrologSymbol(SymbolCodes.symbolCode_E_SOFT_RAINBOW);
	protected static Term termSpring= new PrologSymbol(SymbolCodes.symbolCode_E_SPRING);
	protected static Term termSummer= new PrologSymbol(SymbolCodes.symbolCode_E_SUMMER);
	protected static Term termWinter= new PrologSymbol(SymbolCodes.symbolCode_E_WINTER);
	protected static Term termOptical= new PrologSymbol(SymbolCodes.symbolCode_E_OPTICAL);
	protected static Term termNone= new PrologSymbol(SymbolCodes.symbolCode_E_NONE);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(ColorMapName name) {
		switch (name) {
		case AQUA:
			return termAqua;
		case AUTUMN:
			return termAutumn;
		case BLACKHOT:
			return termBlackhot;
		case BLAZE:
			return termBlaze;
		case BLUERED:
			return termBlueRed;
		case BONE:
			return termBone;
		case BRIGHT_RAINBOW:
			return termBrightRainbow;
		case COOL:
			return termCool;
		case COPPER:
			return termCopper;
		case GRAY:
			return termGray;
		case GREEN:
			return termGreen;
		case HOT:
			return termHot;
		case HSV:
			return termHSV;
		case IRON:
			return termIron;
		case JET:
			return termJet;
		case LIGHTJET:
			return termLightJet;
		case MEDICAL:
			return termMedical;
		case OCEAN:
			return termOcean;
		case PARULA:
			return termParula;
		case PINK:
			return termPink;
		case PRISM:
			return termPrism;
		case PURPLE:
			return termPurple;
		case RED:
			return termRed;
		case REPTILOID:
			return termReptiloid;
		case SOFT_RAINBOW:
			return termSoftRainbow;
		case SPRING:
			return termSpring;
		case SUMMER:
			return termSummer;
		case WINTER:
			return termWinter;
		case OPTICAL:
			return termOptical;
		case NONE:
			return termNone;
		};
		throw new UnknownColorMapName(name);
	}
}
