// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avformat;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpegFormatFlag {
	//
	protected FFmpegFormatFlagName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegFormatFlag(FFmpegFormatFlagName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegFormatFlag(int v) {
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
	public FFmpegFormatFlagName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegFormatFlag argumentToFFmpegFormatFlag(Term value, ChoisePoint iX) {
		try {
			FFmpegFormatFlagName name= FFmpegFormatFlagName.argumentToFFmpegFormatFlagName(value,iX);
			return new FFmpegFormatFlag(name);
		} catch (TermIsNotFFmpegFormatFlagName e1) {
			try {
				return new FFmpegFormatFlag(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegFormatFlag(value);
			}
		}
	}
	//
	public static FFmpegFormatFlag[] extractFFmpegFormatFlagArray(int value) {
		ArrayList<FFmpegFormatFlag> flags= new ArrayList<>();
		if ((value & avformat.AVFMT_NOFILE) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_NOFILE));
			value&= ~avformat.AVFMT_NOFILE;
		};
		if ((value & avformat.AVFMT_NEEDNUMBER) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_NEEDNUMBER));
			value&= ~avformat.AVFMT_NEEDNUMBER;
		};
		if ((value & avformat.AVFMT_SHOW_IDS) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_SHOW_IDS));
			value&= ~avformat.AVFMT_SHOW_IDS;
		};
		if ((value & avformat.AVFMT_RAWPICTURE) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_RAWPICTURE));
			value&= ~avformat.AVFMT_RAWPICTURE;
		};
		if ((value & avformat.AVFMT_GLOBALHEADER) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_GLOBALHEADER));
			value&= ~avformat.AVFMT_GLOBALHEADER;
		};
		if ((value & avformat.AVFMT_NOTIMESTAMPS) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_NOTIMESTAMPS));
			value&= ~avformat.AVFMT_NOTIMESTAMPS;
		};
		if ((value & avformat.AVFMT_GENERIC_INDEX) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_GENERIC_INDEX));
			value&= ~avformat.AVFMT_GENERIC_INDEX;
		};
		if ((value & avformat.AVFMT_TS_DISCONT) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_TS_DISCONT));
			value&= ~avformat.AVFMT_TS_DISCONT;
		};
		if ((value & avformat.AVFMT_VARIABLE_FPS) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_VARIABLE_FPS));
			value&= ~avformat.AVFMT_VARIABLE_FPS;
		};
		if ((value & avformat.AVFMT_NODIMENSIONS) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_NODIMENSIONS));
			value&= ~avformat.AVFMT_NODIMENSIONS;
		};
		if ((value & avformat.AVFMT_NOSTREAMS) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_NOSTREAMS));
			value&= ~avformat.AVFMT_NOSTREAMS;
		};
		if ((value & avformat.AVFMT_NOBINSEARCH) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_NOBINSEARCH));
			value&= ~avformat.AVFMT_NOBINSEARCH;
		};
		if ((value & avformat.AVFMT_NOGENSEARCH) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_NOGENSEARCH));
			value&= ~avformat.AVFMT_NOGENSEARCH;
		};
		if ((value & avformat.AVFMT_NO_BYTE_SEEK) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_NO_BYTE_SEEK));
			value&= ~avformat.AVFMT_NO_BYTE_SEEK;
		};
		if ((value & avformat.AVFMT_ALLOW_FLUSH) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_ALLOW_FLUSH));
			value&= ~avformat.AVFMT_ALLOW_FLUSH;
		};
		if ((value & avformat.AVFMT_TS_NONSTRICT) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_TS_NONSTRICT));
			value&= ~avformat.AVFMT_TS_NONSTRICT;
		};
		if ((value & avformat.AVFMT_TS_NEGATIVE) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_TS_NEGATIVE));
			value&= ~avformat.AVFMT_TS_NEGATIVE;
		};
		if ((value & avformat.AVFMT_SEEK_TO_PTS) != 0) {
			flags.add(new FFmpegFormatFlag(FFmpegFormatFlagName.AVFMT_SEEK_TO_PTS));
			value&= ~avformat.AVFMT_SEEK_TO_PTS;
		};
		if (value != 0) {
			flags.add(new FFmpegFormatFlag(value));
		};
		return flags.toArray(new FFmpegFormatFlag[0]);
	}
}
