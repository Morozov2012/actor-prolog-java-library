// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.system.*;
import morozov.terms.*;

public class ExtendedImageEncodingAttributes extends GenericImageEncodingAttributes {
	public ImageFileFormat format;
	public NumericalValue compressionQuality;
	public Boolean progressiveMode;
	public Boolean interlacing;
	public String comment;
	//
	///////////////////////////////////////////////////////////////
	//
	public ExtendedImageEncodingAttributes(ImageFileFormat f, NumericalValue cQ, Boolean pM, Boolean i, String c) {
		format= f;
		compressionQuality= cQ;
		progressiveMode= pM;
		interlacing= i;
		comment= c;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public ImageFileFormat getFormat() {
		if (format != null) {
			return format;
		} else {
			return ImageFileFormat.UNIVERSAL;
		}
	}
	//
	public void setWriterAttributes(Space2DWriter writer) {
		if (compressionQuality != null) {
			writer.setCompressionQuality(compressionQuality.toDouble());
		};
		if (progressiveMode != null) {
			writer.setProgressiveMode(progressiveMode);
		};
		if (interlacing != null) {
			writer.setInterlacing(interlacing);
		};
		if (comment != null) {
			writer.setComment(comment);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (format==null && compressionQuality==null && progressiveMode==null && interlacing==null && comment==null) {
			return PrologEmptySet.instance;
		} else {
			Term rest= PrologEmptySet.instance;
			if (comment != null) {
				rest= new PrologSet(-SymbolCodes.symbolCode_E_comment,new PrologString(comment),rest);
			};
			if (interlacing != null) {
				rest= new PrologSet(-SymbolCodes.symbolCode_E_interlacing,OnOff.boolean2TermOnOff(interlacing),rest);
			};
			if (progressiveMode != null) {
				rest= new PrologSet(-SymbolCodes.symbolCode_E_progressiveMode,OnOff.boolean2TermOnOff(progressiveMode),rest);
			};
			if (compressionQuality != null) {
				rest= new PrologSet(-SymbolCodes.symbolCode_E_compressionQuality,compressionQuality.toTerm(),rest);
			};
			if (format != null) {
				rest= new PrologSet(-SymbolCodes.symbolCode_E_format,format.toTerm(),rest);
			};
			return rest;
		}
	}
}
