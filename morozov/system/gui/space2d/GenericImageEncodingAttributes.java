// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.gui.space2d.errors.*;
import morozov.terms.*;
import morozov.terms.errors.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class GenericImageEncodingAttributes implements Serializable {
	//
	public static GenericImageEncodingAttributes instance= new GenericImageEncodingAttributes();
	//
	private static final long serialVersionUID= 0x18A81D5FA4198581L; // 1776702349609895297L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.gui.space2d","GenericImageEncodingAttributes");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public GenericImageEncodingAttributes() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public ImageFileFormat getFormat() {
		return ImageFileFormat.UNIVERSAL;
	}
	//
	public void setWriterAttributes(Space2DWriter writer) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static GenericImageEncodingAttributes argumentToImageEncodingAttributes(Term attributes, ChoisePoint iX) {
		if (attributes==null) {
			return new GenericImageEncodingAttributes();
		} else {
			HashMap<Long,Term> setPositiveMap= new HashMap<Long,Term>();
			Term setEnd= attributes.exploreSetPositiveElements(setPositiveMap,iX);
			setEnd= setEnd.dereferenceValue(iX);
			if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
				ImageFileFormat format= null;
				NumericalValue compressionQuality= null;
				Boolean progressiveMode= null;
				Boolean interlacing= null;
				String comment= null;
				Set<Long> nameList= setPositiveMap.keySet();
				Iterator<Long> iterator= nameList.iterator();
				while(iterator.hasNext()) {
					long key= iterator.next();
					long pairName= - key;
					Term pairValue= setPositiveMap.get(key);
					if (pairName==SymbolCodes.symbolCode_E_format) {
						format= ImageFileFormat.argumentToImageFileFormat(pairValue,iX);
					} else if (pairName==SymbolCodes.symbolCode_E_compressionQuality) {
						compressionQuality= NumericalValueConverters.argumentToNumericalValue(pairValue,iX);
					} else if (pairName==SymbolCodes.symbolCode_E_progressiveMode) {
						progressiveMode= OnOffConverters.termOnOff2Boolean(pairValue,iX);
					} else if (pairName==SymbolCodes.symbolCode_E_interlacing) {
						interlacing= OnOffConverters.termOnOff2Boolean(pairValue,iX);
					} else if (pairName==SymbolCodes.symbolCode_E_comment) {
						comment= pairValue.toString(iX);
					} else {
						throw new WrongArgumentIsUnknownImageAttribute(key);
					}
                		};
				return new ExtendedImageEncodingAttributes(format,compressionQuality,progressiveMode,interlacing,comment);
			} else {
				throw new WrongArgumentIsNotEndedSetOfAttributes(setEnd);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		return PrologEmptySet.instance;
	}
}
