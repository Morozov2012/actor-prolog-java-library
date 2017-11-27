// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avutil;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpegMediaType {
	//
	protected FFmpegMediaTypeName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegMediaType(FFmpegMediaTypeName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegMediaType(int v) {
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
	public FFmpegMediaTypeName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegMediaType argumentToFFmpegMediaType(Term value, ChoisePoint iX) {
		try {
			FFmpegMediaTypeName name= FFmpegMediaTypeName.argumentToFFmpegMediaTypeName(value,iX);
			return new FFmpegMediaType(name);
		} catch (TermIsNotFFmpegMediaTypeName e1) {
			try {
				return new FFmpegMediaType(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegMediaType(value);
			}
		}
	}
	//
	public static FFmpegMediaType integerToFFmpegMediaType(int value) {
		if (value == avutil.AVMEDIA_TYPE_UNKNOWN) {
			return new FFmpegMediaType(FFmpegMediaTypeName.AVMEDIA_TYPE_UNKNOWN);
		};
		if (value == avutil.AVMEDIA_TYPE_VIDEO) {
			return new FFmpegMediaType(FFmpegMediaTypeName.AVMEDIA_TYPE_VIDEO);
		};
		if (value == avutil.AVMEDIA_TYPE_AUDIO) {
			return new FFmpegMediaType(FFmpegMediaTypeName.AVMEDIA_TYPE_AUDIO);
		};
		if (value == avutil.AVMEDIA_TYPE_DATA) {
			return new FFmpegMediaType(FFmpegMediaTypeName.AVMEDIA_TYPE_DATA);
		};
		if (value == avutil.AVMEDIA_TYPE_SUBTITLE) {
			return new FFmpegMediaType(FFmpegMediaTypeName.AVMEDIA_TYPE_SUBTITLE);
		};
		if (value == avutil.AVMEDIA_TYPE_ATTACHMENT) {
			return new FFmpegMediaType(FFmpegMediaTypeName.AVMEDIA_TYPE_ATTACHMENT);
		};
		if (value == avutil.AVMEDIA_TYPE_NB) {
			return new FFmpegMediaType(FFmpegMediaTypeName.AVMEDIA_TYPE_NB);
		};
		return new FFmpegMediaType(value);
	}
}
