// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpegLevel {
	//
	protected FFmpegLevelName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegLevel(FFmpegLevelName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegLevel(int v) {
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
	public FFmpegLevelName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegLevel argumentToFFmpegLevel(Term value, ChoisePoint iX) {
		try {
			FFmpegLevelName name= FFmpegLevelName.argumentToFFmpegLevelName(value,iX);
			return new FFmpegLevel(name);
		} catch (TermIsNotFFmpegLevelName e1) {
			try {
				return new FFmpegLevel(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegLevel(value);
			}
		}
	}
	//
	public static FFmpegLevel integerToFFmpegLevel(int value) {
		if (value == avcodec.AVCodecContext.FF_LEVEL_UNKNOWN) {
			return new FFmpegLevel(FFmpegLevelName.FF_LEVEL_UNKNOWN);
		};
		return new FFmpegLevel(value);
	}
}
