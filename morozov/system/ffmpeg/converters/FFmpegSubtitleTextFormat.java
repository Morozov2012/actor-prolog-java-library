// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class FFmpegSubtitleTextFormat {
	//
	protected FFmpegSubtitleTextFormatName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegSubtitleTextFormat(FFmpegSubtitleTextFormatName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegSubtitleTextFormat(int v) {
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
	public FFmpegSubtitleTextFormatName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegSubtitleTextFormat argumentToFFmpegSubtitleTextFormat(Term value, ChoisePoint iX) {
		try {
			FFmpegSubtitleTextFormatName name= FFmpegSubtitleTextFormatName.argumentToFFmpegSubtitleTextFormatName(value,iX);
			return new FFmpegSubtitleTextFormat(name);
		} catch (TermIsNotFFmpegSubtitleTextFormatName e1) {
			try {
				return new FFmpegSubtitleTextFormat(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegSubtitleTextFormat(value);
			}
		}
	}
	//
	public static FFmpegSubtitleTextFormat integerToFFmpegSubtitleTextFormat(int value) {
		if (value == avcodec.AVCodecContext.FF_SUB_TEXT_FMT_ASS) {
			return new FFmpegSubtitleTextFormat(FFmpegSubtitleTextFormatName.FF_SUB_TEXT_FMT_ASS);
		};
		if (value == avcodec.AVCodecContext.FF_SUB_TEXT_FMT_ASS_WITH_TIMINGS) {
			return new FFmpegSubtitleTextFormat(FFmpegSubtitleTextFormatName.FF_SUB_TEXT_FMT_ASS_WITH_TIMINGS);
		};
		return new FFmpegSubtitleTextFormat(value);
	}
}
