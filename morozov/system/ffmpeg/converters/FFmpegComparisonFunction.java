// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpegComparisonFunction {
	//
	protected FFmpegComparisonFunctionName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegComparisonFunction(FFmpegComparisonFunctionName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegComparisonFunction(int v) {
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
	public FFmpegComparisonFunctionName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegComparisonFunction argumentToFFmpegComparisonFunction(Term value, ChoisePoint iX) {
		try {
			FFmpegComparisonFunctionName name= FFmpegComparisonFunctionName.argumentToFFmpegComparisonFunctionName(value,iX);
			return new FFmpegComparisonFunction(name);
		} catch (TermIsNotFFmpegComparisonFunctionName e1) {
			try {
				return new FFmpegComparisonFunction(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegComparisonFunction(value);
			}
		}
	}
	//
	public static FFmpegComparisonFunction integerToFFmpegComparisonFunction(int value) {
		if (value == avcodec.AVCodecContext.FF_CMP_SAD) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_SAD);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_SSE) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_SSE);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_SATD) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_SATD);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_DCT) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_DCT);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_PSNR) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_PSNR);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_BIT) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_BIT);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_RD) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_RD);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_ZERO) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_ZERO);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_VSAD) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_VSAD);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_VSSE) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_VSSE);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_NSSE) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_NSSE);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_W53) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_W53);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_W97) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_W97);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_DCTMAX) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_DCTMAX);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_DCT264) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_DCT264);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_MEDIAN_SAD) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_MEDIAN_SAD);
		};
		if (value == avcodec.AVCodecContext.FF_CMP_CHROMA) {
			return new FFmpegComparisonFunction(FFmpegComparisonFunctionName.FF_CMP_CHROMA);
		};
		return new FFmpegComparisonFunction(value);
	}
}
