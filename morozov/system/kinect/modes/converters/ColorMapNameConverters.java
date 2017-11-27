// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.errors.*;
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
		if (code==SymbolCodes.symbolCode_E_AUTUMN) {
			return ColorMapName.AUTUMN;
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
		} else if (code==SymbolCodes.symbolCode_E_HOT) {
			return ColorMapName.HOT;
		} else if (code==SymbolCodes.symbolCode_E_HSV) {
			return ColorMapName.HSV;
		} else if (code==SymbolCodes.symbolCode_E_JET) {
			return ColorMapName.JET;
		} else if (code==SymbolCodes.symbolCode_E_OCEAN) {
			return ColorMapName.OCEAN;
		} else if (code==SymbolCodes.symbolCode_E_PINK) {
			return ColorMapName.PINK;
		} else if (code==SymbolCodes.symbolCode_E_PRISM) {
			return ColorMapName.PRISM;
		} else if (code==SymbolCodes.symbolCode_E_SOFT_RAINBOW) {
			return ColorMapName.SOFT_RAINBOW;
		} else if (code==SymbolCodes.symbolCode_E_SPRING) {
			return ColorMapName.SPRING;
		} else if (code==SymbolCodes.symbolCode_E_SUMMER) {
			return ColorMapName.SUMMER;
		} else if (code==SymbolCodes.symbolCode_E_WINTER) {
			return ColorMapName.WINTER;
		} else {
			throw new WrongArgumentIsNotColorMapName(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termAutumn= new PrologSymbol(SymbolCodes.symbolCode_E_AUTUMN);
	protected static Term termBone= new PrologSymbol(SymbolCodes.symbolCode_E_BONE);
	protected static Term termBrightRainbow= new PrologSymbol(SymbolCodes.symbolCode_E_BRIGHT_RAINBOW);
	protected static Term termCool= new PrologSymbol(SymbolCodes.symbolCode_E_COOL);
	protected static Term termCopper= new PrologSymbol(SymbolCodes.symbolCode_E_COPPER);
	protected static Term termGray= new PrologSymbol(SymbolCodes.symbolCode_E_GRAY);
	protected static Term termHot= new PrologSymbol(SymbolCodes.symbolCode_E_HOT);
	protected static Term termHSV= new PrologSymbol(SymbolCodes.symbolCode_E_HSV);
	protected static Term termJet= new PrologSymbol(SymbolCodes.symbolCode_E_JET);
	protected static Term termOcean= new PrologSymbol(SymbolCodes.symbolCode_E_OCEAN);
	protected static Term termPink= new PrologSymbol(SymbolCodes.symbolCode_E_PINK);
	protected static Term termPrism= new PrologSymbol(SymbolCodes.symbolCode_E_PRISM);
	protected static Term termSoftRainbow= new PrologSymbol(SymbolCodes.symbolCode_E_SOFT_RAINBOW);
	protected static Term termSpring= new PrologSymbol(SymbolCodes.symbolCode_E_SPRING);
	protected static Term termSummer= new PrologSymbol(SymbolCodes.symbolCode_E_SUMMER);
	protected static Term termWinter= new PrologSymbol(SymbolCodes.symbolCode_E_WINTER);
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(ColorMapName name) {
		switch (name) {
		case AUTUMN:
			return termAutumn;
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
		case HOT:
			return termHot;
		case HSV:
			return termHSV;
		case JET:
			return termJet;
		case OCEAN:
			return termOcean;
		case PINK:
			return termPink;
		case PRISM:
			return termPrism;
		case SOFT_RAINBOW:
			return termSoftRainbow;
		case SPRING:
			return termSpring;
		case SUMMER:
			return termSummer;
		case WINTER:
			return termWinter;
		};
		throw new UnknownColorMapName(name);
	}
}
