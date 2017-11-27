// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.converters.errors.*;
import morozov.system.kinect.modes.tools.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class ColorMapConverters {
	//
	public static ColorMap argumentToColorMap(Term attributes, ChoisePoint iX) {
		try {
			long code= attributes.getSymbolValue(iX);
			ColorMapName name= ColorMapNameConverters.symbolToColorMapName(code,attributes);
			return new ColorMap(name,ColorMapSizeTools.instanceDefault,YesNoDefault.DEFAULT,YesNoDefault.DEFAULT,YesNoDefault.DEFAULT,TincturingCoefficientTools.instanceDefault);
		} catch (TermIsNotASymbol e1) {
			try {
				Color[] colors= listToColors(attributes,iX);
				return new ColorMap(colors);
			} catch (TermIsNotAList e4) {
				HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
				Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
				setEnd= setEnd.dereferenceValue(iX);
				if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
					ColorMapName name= ColorMapTools.getDefaultColorMapName();
					ColorMapSize size= ColorMapSizeTools.instanceDefault;
					YesNoDefault reverseScale= YesNoDefault.DEFAULT;
					YesNoDefault reverseMinimalValue= YesNoDefault.DEFAULT;
					YesNoDefault reverseMaximalValue= YesNoDefault.DEFAULT;
					TincturingCoefficient tincturingCoefficient= TincturingCoefficientTools.instanceDefault;
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
							reverseScale= YesNoDefault.term2YesNoDefault(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_reverse_minimal_value) {
							reverseMinimalValue= YesNoDefault.term2YesNoDefault(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_reverse_maximal_value) {
							reverseMaximalValue= YesNoDefault.term2YesNoDefault(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_tincturing_coefficient) {
							tincturingCoefficient= TincturingCoefficientConverters.argumentToTincturingCoefficient(pairValue,iX);
						} else if (pairName==SymbolCodes.symbolCode_E_palette) {
							colors= argumentToColorPalette(pairValue,iX);
						} else {
							throw new WrongArgumentIsUnknownColorMapAttribute(key);
						}
					};
					if (colors != null) {
						return new ColorMap(colors,size,reverseScale,reverseMinimalValue,reverseMaximalValue,tincturingCoefficient);
					} else {
						return new ColorMap(name,size,reverseScale,reverseMinimalValue,reverseMaximalValue,tincturingCoefficient);
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
					Color color= ExtendedColor.argumentToColor(nextHead,iX);
					array.add(color);
				} catch (TermIsSymbolDefault e2) {
				};
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e) {
			return array.toArray(new Color[0]);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term toTerm(ColorMapInterface colorMap) {
		if (colorMap.getUseCustomMap()) {
			Color[] colors= colorMap.getColors();
			Term value= PrologEmptyList.instance;
			for (int n=0; n < colors.length; n++) {
				value= new PrologList(ExtendedColor.colorToTerm(colors[n]),value);
			};
			return value;
		} else {
			ColorMapName name= colorMap.getColorMapName();
			ColorMapSizeInterface size= colorMap.getSize();
			YesNoDefault reverseScale= colorMap.getReverseScale();
			YesNoDefault reverseMinimalValue= colorMap.getReverseMinimalValue();
			YesNoDefault reverseMaximalValue= colorMap.getReverseMaximalValue();
			TincturingCoefficientInterface tincturingCoefficient= colorMap.getTincturingCoefficient();
			Term value= PrologEmptySet.instance;
			value= new PrologSet(-SymbolCodes.symbolCode_E_tincturing_coefficient,TincturingCoefficientConverters.toTerm(tincturingCoefficient),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_reverse_maximal_value,reverseMaximalValue.toTerm(),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_reverse_minimal_value,reverseMinimalValue.toTerm(),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_reverse_scale,reverseScale.toTerm(),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_size,ColorMapSizeConverters.toTerm(size),value);
			value= new PrologSet(-SymbolCodes.symbolCode_E_name,ColorMapNameConverters.toTerm(name),value);
			return value;
		}
	}
}
