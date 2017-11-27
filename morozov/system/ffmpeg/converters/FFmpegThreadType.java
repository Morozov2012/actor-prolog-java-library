// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpegThreadType {
	//
	protected FFmpegThreadTypeName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegThreadType(FFmpegThreadTypeName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegThreadType(int v) {
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
	public FFmpegThreadTypeName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegThreadType argumentToFFmpegThreadType(Term value, ChoisePoint iX) {
		try {
			FFmpegThreadTypeName name= FFmpegThreadTypeName.argumentToFFmpegThreadTypeName(value,iX);
			return new FFmpegThreadType(name);
		} catch (TermIsNotFFmpegThreadTypeName e1) {
			try {
				return new FFmpegThreadType(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegThreadType(value);
			}
		}
	}
	//
	public static FFmpegThreadType integerToFFmpegThreadType(int value) {
		if (value == avcodec.AVCodecContext.FF_THREAD_FRAME) {
			return new FFmpegThreadType(FFmpegThreadTypeName.FF_THREAD_FRAME);
		};
		if (value == avcodec.AVCodecContext.FF_THREAD_SLICE) {
			return new FFmpegThreadType(FFmpegThreadTypeName.FF_THREAD_SLICE);
		};
		return new FFmpegThreadType(value);
	}
}
