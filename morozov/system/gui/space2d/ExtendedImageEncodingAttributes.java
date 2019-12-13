// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d;

import target.*;

import morozov.system.*;
import morozov.system.converters.*;
import morozov.terms.*;

public class ExtendedImageEncodingAttributes extends GenericImageEncodingAttributes {
	//
	protected ImageFileFormat format;
	protected NumericalValue compressionQuality;
	protected Boolean progressiveMode;
	protected Boolean interlacing;
	protected String comment;
	//
	///////////////////////////////////////////////////////////////
	//
	private static final long serialVersionUID= 0x8E6F65D5282DC995L; // -8183210031732373099L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.gui.space2d","ExtendedImageEncodingAttributes");
	// }
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
	@Override
	public ImageFileFormat getFormat() {
		if (format != null) {
			return format;
		} else {
			return ImageFileFormat.UNIVERSAL;
		}
	}
	//
	@Override
	public void setWriterAttributes(Space2DWriter writer) {
		if (compressionQuality != null) {
			writer.setCompressionQuality(NumericalValueConverters.toDouble(compressionQuality));
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
	@Override
	public Term toTerm() {
		if (format==null && compressionQuality==null && progressiveMode==null && interlacing==null && comment==null) {
			return PrologEmptySet.instance;
		} else {
			Term rest= PrologEmptySet.instance;
			if (comment != null) {
				rest= new PrologSet(-SymbolCodes.symbolCode_E_comment,new PrologString(comment),rest);
			};
			if (interlacing != null) {
				rest= new PrologSet(-SymbolCodes.symbolCode_E_interlacing,OnOffConverters.boolean2TermOnOff(interlacing),rest);
			};
			if (progressiveMode != null) {
				rest= new PrologSet(-SymbolCodes.symbolCode_E_progressiveMode,OnOffConverters.boolean2TermOnOff(progressiveMode),rest);
			};
			if (compressionQuality != null) {
				rest= new PrologSet(-SymbolCodes.symbolCode_E_compressionQuality,NumericalValueConverters.toTerm(compressionQuality),rest);
			};
			if (format != null) {
				rest= new PrologSet(-SymbolCodes.symbolCode_E_format,format.toTerm(),rest);
			};
			return rest;
		}
	}
}
