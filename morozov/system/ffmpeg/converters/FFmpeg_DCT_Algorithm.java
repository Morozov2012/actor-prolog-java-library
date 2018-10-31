// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class FFmpeg_DCT_Algorithm {
	//
	protected FFmpeg_DCT_AlgorithmName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpeg_DCT_Algorithm(FFmpeg_DCT_AlgorithmName n) {
		name= n;
		isNamed= true;
	}
	public FFmpeg_DCT_Algorithm(int v) {
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
	public FFmpeg_DCT_AlgorithmName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpeg_DCT_Algorithm argumentToFFmpeg_DCT_Algorithm(Term value, ChoisePoint iX) {
		try {
			FFmpeg_DCT_AlgorithmName name= FFmpeg_DCT_AlgorithmName.argumentToFFmpeg_DCT_AlgorithmName(value,iX);
			return new FFmpeg_DCT_Algorithm(name);
		} catch (TermIsNotFFmpeg_DCT_AlgorithmName e1) {
			try {
				return new FFmpeg_DCT_Algorithm(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpeg_DCT_Algorithm(value);
			}
		}
	}
	//
	public static FFmpeg_DCT_Algorithm integerToFFmpeg_DCT_Algorithm(int value) {
		if (value == avcodec.AVCodecContext.FF_DCT_AUTO) {
			return new FFmpeg_DCT_Algorithm(FFmpeg_DCT_AlgorithmName.FF_DCT_AUTO);
		};
		if (value == avcodec.AVCodecContext.FF_DCT_FASTINT) {
			return new FFmpeg_DCT_Algorithm(FFmpeg_DCT_AlgorithmName.FF_DCT_FASTINT);
		};
		if (value == avcodec.AVCodecContext.FF_DCT_INT) {
			return new FFmpeg_DCT_Algorithm(FFmpeg_DCT_AlgorithmName.FF_DCT_INT);
		};
		if (value == avcodec.AVCodecContext.FF_DCT_MMX) {
			return new FFmpeg_DCT_Algorithm(FFmpeg_DCT_AlgorithmName.FF_DCT_MMX);
		};
		if (value == avcodec.AVCodecContext.FF_DCT_ALTIVEC) {
			return new FFmpeg_DCT_Algorithm(FFmpeg_DCT_AlgorithmName.FF_DCT_ALTIVEC);
		};
		if (value == avcodec.AVCodecContext.FF_DCT_FAAN) {
			return new FFmpeg_DCT_Algorithm(FFmpeg_DCT_AlgorithmName.FF_DCT_FAAN);
		};
		return new FFmpeg_DCT_Algorithm(value);
	}
}
