// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avutil;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class FFmpegAudioSampleFormat {
	//
	protected FFmpegAudioSampleFormatName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegAudioSampleFormat(int v) {
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
	public FFmpegAudioSampleFormatName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegAudioSampleFormat argumentToFFmpegAudioSampleFormat(Term value, ChoisePoint iX) {
		try {
			FFmpegAudioSampleFormatName name= FFmpegAudioSampleFormatName.argumentToFFmpegAudioSampleFormatName(value,iX);
			return new FFmpegAudioSampleFormat(name);
		} catch (TermIsNotFFmpegAudioSampleFormatName e1) {
			try {
				return new FFmpegAudioSampleFormat(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegAudioSampleFormat(value);
			}
		}
	}
	//
	public static FFmpegAudioSampleFormat integerToFFmpegAudioSampleFormat(int value) {
		if (value == avutil.AV_SAMPLE_FMT_NONE) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_NONE);
		};
		if (value == avutil.AV_SAMPLE_FMT_U8) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_U8);
		};
		if (value == avutil.AV_SAMPLE_FMT_S16) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_S16);
		};
		if (value == avutil.AV_SAMPLE_FMT_S32) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_S32);
		};
		if (value == avutil.AV_SAMPLE_FMT_FLT) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_FLT);
		};
		if (value == avutil.AV_SAMPLE_FMT_DBL) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_DBL);
		};
		if (value == avutil.AV_SAMPLE_FMT_U8P) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_U8P);
		};
		if (value == avutil.AV_SAMPLE_FMT_S16P) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_S16P);
		};
		if (value == avutil.AV_SAMPLE_FMT_S32P) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_S32P);
		};
		if (value == avutil.AV_SAMPLE_FMT_FLTP) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_FLTP);
		};
		if (value == avutil.AV_SAMPLE_FMT_DBLP) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_DBLP);
		};
		if (value == avutil.AV_SAMPLE_FMT_S64) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_S64);
		};
		if (value == avutil.AV_SAMPLE_FMT_S64P) {
			return new FFmpegAudioSampleFormat(FFmpegAudioSampleFormatName.AV_SAMPLE_FMT_S64P);
		};
		return new FFmpegAudioSampleFormat(value);
	}
}
