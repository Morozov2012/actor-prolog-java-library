// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpeg_IDCT_Algorithm {
	//
	protected FFmpeg_IDCT_AlgorithmName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName n) {
		name= n;
		isNamed= true;
	}
	public FFmpeg_IDCT_Algorithm(int v) {
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
	public FFmpeg_IDCT_AlgorithmName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpeg_IDCT_Algorithm argumentToFFmpeg_IDCT_Algorithm(Term value, ChoisePoint iX) {
		try {
			FFmpeg_IDCT_AlgorithmName name= FFmpeg_IDCT_AlgorithmName.argumentToFFmpeg_IDCT_AlgorithmName(value,iX);
			return new FFmpeg_IDCT_Algorithm(name);
		} catch (TermIsNotFFmpeg_IDCT_AlgorithmName e1) {
			try {
				return new FFmpeg_IDCT_Algorithm(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpeg_IDCT_Algorithm(value);
			}
		}
	}
	//
	public static FFmpeg_IDCT_Algorithm integerToFFmpeg_IDCT_Algorithm(int value) {
		if (value == avcodec.AVCodecContext.FF_IDCT_AUTO) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_AUTO);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_INT) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_INT);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_SIMPLE) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLE);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_SIMPLEMMX) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEMMX);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_ARM) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_ARM);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_ALTIVEC) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_ALTIVEC);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_SH4) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_SH4);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_SIMPLEARM) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEARM);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_IPP) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_IPP);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_XVID) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_XVID);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_XVIDMMX) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_XVIDMMX);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_SIMPLEARMV5TE) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEARMV5TE);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_SIMPLEARMV6) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEARMV6);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_SIMPLEVIS) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEVIS);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_FAAN) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_FAAN);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_SIMPLENEON) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLENEON);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_SIMPLEALPHA) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEALPHA);
		};
		if (value == avcodec.AVCodecContext.FF_IDCT_SIMPLEAUTO) {
			return new FFmpeg_IDCT_Algorithm(FFmpeg_IDCT_AlgorithmName.FF_IDCT_SIMPLEAUTO);
		};
		return new FFmpeg_IDCT_Algorithm(value);
	}
}
