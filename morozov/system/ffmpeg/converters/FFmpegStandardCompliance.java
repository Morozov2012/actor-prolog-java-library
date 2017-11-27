// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpegStandardCompliance {
	//
	protected FFmpegStandardComplianceName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegStandardCompliance(FFmpegStandardComplianceName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegStandardCompliance(int v) {
		value= v;
		isNamed= false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int toInteger() {
		if (isNamed) {
			return name.toInteger();
		} else {
			return value;
		}
	}
	//
	public Term toTerm() {
		if (isNamed) {
			return name.toTerm();
		} else {
			return new PrologInteger(value);
		}
	}
	//
	public boolean isNamed() {
		return isNamed;
	}
	//
	public FFmpegStandardComplianceName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegStandardCompliance argumentToFFmpegStandardCompliance(Term value, ChoisePoint iX) {
		try {
			FFmpegStandardComplianceName name= FFmpegStandardComplianceName.argumentToFFmpegStandardComplianceName(value,iX);
			return new FFmpegStandardCompliance(name);
		} catch (TermIsNotFFmpegStandardComplianceName e1) {
			try {
				return new FFmpegStandardCompliance(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegStandardCompliance(value);
			}
		}
	}
	//
	public static FFmpegStandardCompliance integerToFFmpegStandardCompliance(int value) {
		if (value == avcodec.AVCodecContext.FF_COMPLIANCE_VERY_STRICT) {
			return new FFmpegStandardCompliance(FFmpegStandardComplianceName.FF_COMPLIANCE_VERY_STRICT);
		};
		if (value == avcodec.AVCodecContext.FF_COMPLIANCE_STRICT) {
			return new FFmpegStandardCompliance(FFmpegStandardComplianceName.FF_COMPLIANCE_STRICT);
		};
		if (value == avcodec.AVCodecContext.FF_COMPLIANCE_NORMAL) {
			return new FFmpegStandardCompliance(FFmpegStandardComplianceName.FF_COMPLIANCE_NORMAL);
		};
		if (value == avcodec.AVCodecContext.FF_COMPLIANCE_UNOFFICIAL) {
			return new FFmpegStandardCompliance(FFmpegStandardComplianceName.FF_COMPLIANCE_UNOFFICIAL);
		};
		if (value == avcodec.AVCodecContext.FF_COMPLIANCE_EXPERIMENTAL) {
			return new FFmpegStandardCompliance(FFmpegStandardComplianceName.FF_COMPLIANCE_EXPERIMENTAL);
		};
		return new FFmpegStandardCompliance(value);
	}
}
