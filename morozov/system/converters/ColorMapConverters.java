// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.interfaces.*;
import morozov.system.signals.*;
import morozov.system.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class ColorMapConverters {
	//
	public static IterativeDetailedColorMap argumentToIterativeDetailedColorMap(Term attributes, ChoisePoint iX) {
		try {
			long code= attributes.getSymbolValue(iX);
			ColorMapName name= ColorMapNameConverters.symbolToColorMapName(code,attributes);
			return new IterativeDetailedColorMap(
				name,
				ColorMapSize.instanceDefault,
				YesNoDefault.DEFAULT,
				YesNoDefault.DEFAULT,
				RealAttribute.instanceDefault,
				RealAttribute.instanceDefault,
				YesNoDefault.DEFAULT,
				YesNoDefault.DEFAULT,
				IntegerAttribute.instanceDefault,
				YesNoDefault.DEFAULT,
				YesNoDefault.DEFAULT,
				TincturingCoefficient.instanceDefault,
				ColorAttribute.instanceDefault);
		} catch (TermIsNotASymbol e1) {
			try {
				Color[] colors= listToColors(attributes,iX);
				return new IterativeDetailedColorMap(colors);
			} catch (TermIsNotAList e4) {
				HashMap<Long,Term> setPositiveMap= new HashMap<>();
				Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
				setEnd= setEnd.dereferenceValue(iX);
				if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
					ColorMapName name= ColorMapName.getDefaultColorMapName();
					ColorMapSize size= ColorMapSize.instanceDefault;
					YesNoDefault reverseScale= YesNoDefault.DEFAULT;
					YesNoDefault reverseColors= YesNoDefault.DEFAULT;
					RealAttribute lowerQuantile= RealAttribute.instanceDefault;
					RealAttribute upperQuantile= RealAttribute.instanceDefault;
					YesNoDefault lowerBoundIsZero= YesNoDefault.DEFAULT;
					YesNoDefault upperBoundIsZero= YesNoDefault.DEFAULT;
					IntegerAttribute paletteIterations= IntegerAttribute.instanceDefault;
					YesNoDefault reverseMinimalValue= YesNoDefault.DEFAULT;
					YesNoDefault reverseMaximalValue= YesNoDefault.DEFAULT;
					TincturingCoefficient tincturingCoefficient= TincturingCoefficient.instanceDefault;
					ColorAttribute backgroundColor= ColorAttribute.instanceDefault;
					Color[] colors= null;
					Set<Long> nameList= setPositiveMap.keySet();
					Iterator<Long> iterator= nameList.iterator();
					while(iterator.hasNext()) {
						long key= iterator.next();
						long pairName= - key;
						Term pairValue= setPositiveMap.get(key);
						if (pairName==SymbolCodes.symbolCode_E_name) {
							name= ColorMapNameConverters.argumentToColorMapName(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_size) {
							size= ColorMapSizeConverters.argumentToColorMapSize(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_reverse_scale) {
							reverseScale= YesNoDefaultConverters.argument2YesNoDefault(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_reverse_colors) {
							reverseColors= YesNoDefaultConverters.argument2YesNoDefault(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_lower_quantile) {
							lowerQuantile= RealAttributeConverters.argumentToRealAttribute(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_upper_quantile) {
							upperQuantile= RealAttributeConverters.argumentToRealAttribute(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_lower_bound_is_zero) {
							lowerBoundIsZero= YesNoDefaultConverters.argument2YesNoDefault(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_upper_bound_is_zero) {
							upperBoundIsZero= YesNoDefaultConverters.argument2YesNoDefault(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_palette_iterations) {
							paletteIterations= IntegerAttributeConverters.argumentToIntegerAttribute(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_reverse_minimal_value) {
							reverseMinimalValue= YesNoDefaultConverters.argument2YesNoDefault(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_reverse_maximal_value) {
							reverseMaximalValue= YesNoDefaultConverters.argument2YesNoDefault(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_tincturing_coefficient) {
							tincturingCoefficient= TincturingCoefficientConverters.argumentToTincturingCoefficient(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_background_color) {
							backgroundColor= ColorAttributeConverters.argumentToColorAttribute(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_palette) {
							colors= argumentToColorPalette(pairValue,iX);
						} else {
							throw new WrongArgumentIsUnknownColorMapAttribute(key);
						}
					};
					if (colors != null) {
						return new IterativeDetailedColorMap(
							colors,
							size,
							reverseScale,
							reverseColors,
							lowerQuantile,
							upperQuantile,
							lowerBoundIsZero,
							upperBoundIsZero,
							paletteIterations,
							reverseMinimalValue,
							reverseMaximalValue,
							tincturingCoefficient,
							backgroundColor);
					} else {
						return new IterativeDetailedColorMap(
							name,
							size,
							reverseScale,
							reverseColors,
							lowerQuantile,
							upperQuantile,
							lowerBoundIsZero,
							upperBoundIsZero,
							paletteIterations,
							reverseMinimalValue,
							reverseMaximalValue,
							tincturingCoefficient,
							backgroundColor);
					}
				} else {
					throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
				}
			}
		}
	}
	//
	public static Color[] argumentToColorPalette(Term value, ChoisePoint iX) {
		try {
			return listToColors(value,iX);
		} catch (TermIsNotAList e4) {
			throw new WrongArgumentIsNotColorPalette(value);
		}
	}
	public static Color[] listToColors(Term value, ChoisePoint iX) throws TermIsNotAList {
		Term nextHead;
		Term currentTail= value;
		ArrayList<Color> array= new ArrayList<>();
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				try {
					Color color= ColorAttributeConverters.argumentToColor(nextHead,iX);
					array.add(color);
				} catch (TermIsSymbolDefault e2) {
				};
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
			return array.toArray(new Color[array.size()]);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(IterativeDetailedColorMapInterface colorMap) {
		if (colorMap.getUseCustomMap()) {
			Color[] colors= colorMap.getColors();
			Term value= PrologEmptyList.instance;
			for (int n=0; n < colors.length; n++) {
				value= new PrologList(ColorAttributeConverters.colorToTerm(colors[n]),value);
			};
			return value;
		} else {
			ColorMapName name= colorMap.getName();
			ColorMapSize size= colorMap.getSize();
			YesNoDefault reverseScale= colorMap.getReverseScale();
			YesNoDefault reverseColors= colorMap.getReverseColors();
			RealAttribute lowerQuantile= colorMap.getLowerQuantile();
			RealAttribute upperQuantile= colorMap.getUpperQuantile();
			YesNoDefault lowerBoundIsZero= colorMap.getLowerBoundIsZero();
			YesNoDefault upperBoundIsZero= colorMap.getUpperBoundIsZero();
			IntegerAttribute paletteIterations= colorMap.getPaletteIterations();
			YesNoDefault reverseMinimalValue= colorMap.getReverseMinimalValue();
			YesNoDefault reverseMaximalValue= colorMap.getReverseMaximalValue();
			TincturingCoefficient tincturingCoefficient= colorMap.getTincturingCoefficient();
			ColorAttribute backgroundColor= colorMap.getBackgroundColor();
			Term value= PrologEmptySet.instance;
			value= new PrologSet(-SymbolCodes.symbolCode_E_background_color,ColorAttributeConverters.toTerm(backgroundColor),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_tincturing_coefficient,TincturingCoefficientConverters.toTerm(tincturingCoefficient),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_reverse_maximal_value,YesNoDefaultConverters.toTerm(reverseMaximalValue),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_reverse_minimal_value,YesNoDefaultConverters.toTerm(reverseMinimalValue),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_palette_iterations,IntegerAttributeConverters.toTerm(paletteIterations),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_upper_bound_is_zero,YesNoDefaultConverters.toTerm(upperBoundIsZero),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_lower_bound_is_zero,YesNoDefaultConverters.toTerm(lowerBoundIsZero),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_upper_quantile,RealAttributeConverters.toTerm(upperQuantile),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_lower_quantile,RealAttributeConverters.toTerm(lowerQuantile),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_reverse_colors,YesNoDefaultConverters.toTerm(reverseColors),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_reverse_scale,YesNoDefaultConverters.toTerm(reverseScale),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_size,ColorMapSizeConverters.toTerm(size),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_name,ColorMapNameConverters.toTerm(name),value);
			return value;
		}
	}
	//
	public static Term detailedColorMapToTerm(DetailedColorMap colorMap) {
		if (colorMap instanceof IterativeDetailedColorMapInterface) {
			return toTerm((IterativeDetailedColorMapInterface)colorMap);
		};
		if (colorMap.getUseCustomMap()) {
			Color[] colors= colorMap.getColors();
			Term value= PrologEmptyList.instance;
			for (int n=0; n < colors.length; n++) {
				value= new PrologList(ColorAttributeConverters.colorToTerm(colors[n]),value);
			};
			return value;
		} else {
			ColorMapName name= colorMap.getColorMapName();
			ColorMapSize size= colorMap.getSize();
			YesNoDefault reverseScale= colorMap.getReverseScale();
			YesNoDefault reverseMinimalValue= colorMap.getReverseMinimalValue();
			YesNoDefault reverseMaximalValue= colorMap.getReverseMaximalValue();
			TincturingCoefficient tincturingCoefficient= colorMap.getTincturingCoefficient();
			Term value= PrologEmptySet.instance;
			value= new PrologSet(-SymbolCodes.symbolCode_E_tincturing_coefficient,TincturingCoefficientConverters.toTerm(tincturingCoefficient),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_reverse_maximal_value,YesNoDefaultConverters.toTerm(reverseMaximalValue),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_reverse_minimal_value,YesNoDefaultConverters.toTerm(reverseMinimalValue),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_reverse_scale,YesNoDefaultConverters.toTerm(reverseScale),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_size,ColorMapSizeConverters.toTerm(size),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_name,ColorMapNameConverters.toTerm(name),value);
			return value;
		}
	}
}
