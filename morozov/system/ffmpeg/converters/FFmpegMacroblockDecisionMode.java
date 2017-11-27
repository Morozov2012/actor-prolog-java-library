// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpegMacroblockDecisionMode {
	//
	protected FFmpegMacroblockDecisionModeName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegMacroblockDecisionMode(FFmpegMacroblockDecisionModeName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegMacroblockDecisionMode(int v) {
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
	public FFmpegMacroblockDecisionModeName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegMacroblockDecisionMode argumentToFFmpegMacroblockDecisionMode(Term value, ChoisePoint iX) {
		try {
			FFmpegMacroblockDecisionModeName name= FFmpegMacroblockDecisionModeName.argumentToFFmpegMacroblockDecisionModeName(value,iX);
			return new FFmpegMacroblockDecisionMode(name);
		} catch (TermIsNotFFmpegMacroblockDecisionModeName e1) {
			try {
				return new FFmpegMacroblockDecisionMode(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegMacroblockDecisionMode(value);
			}
		}
	}
	//
	public static FFmpegMacroblockDecisionMode integerToFFmpegMacroblockDecisionMode(int value) {
		if (value == avcodec.AVCodecContext.FF_MB_DECISION_SIMPLE) {
			return new FFmpegMacroblockDecisionMode(FFmpegMacroblockDecisionModeName.FF_MB_DECISION_SIMPLE);
		};
		if (value == avcodec.AVCodecContext.FF_MB_DECISION_BITS) {
			return new FFmpegMacroblockDecisionMode(FFmpegMacroblockDecisionModeName.FF_MB_DECISION_BITS);
		};
		if (value == avcodec.AVCodecContext.FF_MB_DECISION_RD) {
			return new FFmpegMacroblockDecisionMode(FFmpegMacroblockDecisionModeName.FF_MB_DECISION_RD);
		};
		return new FFmpegMacroblockDecisionMode(value);
	}
}
