// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avutil;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpegChannelLayout {
	//
	protected FFmpegChannelLayoutName name;
	protected long value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegChannelLayout(FFmpegChannelLayoutName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegChannelLayout(long v) {
		value= v;
		isNamed= false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long toLong() {
		if (isNamed) {
			return name.toLong();
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
	public FFmpegChannelLayoutName getName() {
		return name;
	}
	//
	public long getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegChannelLayout argumentToFFmpegChannelLayout(Term value, ChoisePoint iX) {
		try {
			FFmpegChannelLayoutName name= FFmpegChannelLayoutName.argumentToFFmpegChannelLayoutName(value,iX);
			return new FFmpegChannelLayout(name);
		} catch (TermIsNotFFmpegChannelLayoutName e1) {
			try {
				return new FFmpegChannelLayout(value.getLongIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegChannelLayout(value);
			}
		}
	}
	//
	public static FFmpegChannelLayout longToFFmpegChannelLayout(long value) {
		if (value == avutil.AV_CH_LAYOUT_MONO) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_MONO);
		};
		if (value == avutil.AV_CH_LAYOUT_STEREO) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_STEREO);
		};
		if (value == avutil.AV_CH_LAYOUT_2POINT1) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_2POINT1);
		};
		if (value == avutil.AV_CH_LAYOUT_2_1) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_2_1);
		};
		if (value == avutil.AV_CH_LAYOUT_SURROUND) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_SURROUND);
		};
		if (value == avutil.AV_CH_LAYOUT_3POINT1) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_3POINT1);
		};
		if (value == avutil.AV_CH_LAYOUT_4POINT0) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_4POINT0);
		};
		if (value == avutil.AV_CH_LAYOUT_4POINT1) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_4POINT1);
		};
		if (value == avutil.AV_CH_LAYOUT_2_2) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_2_2);
		};
		if (value == avutil.AV_CH_LAYOUT_QUAD) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_QUAD);
		};
		if (value == avutil.AV_CH_LAYOUT_5POINT0) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_5POINT0);
		};
		if (value == avutil.AV_CH_LAYOUT_5POINT1) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_5POINT1);
		};
		if (value == avutil.AV_CH_LAYOUT_5POINT0_BACK) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_5POINT0_BACK);
		};
		if (value == avutil.AV_CH_LAYOUT_5POINT1_BACK) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_5POINT1_BACK);
		};
		if (value == avutil.AV_CH_LAYOUT_6POINT0) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT0);
		};
		if (value == avutil.AV_CH_LAYOUT_6POINT0_FRONT) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT0_FRONT);
		};
		if (value == avutil.AV_CH_LAYOUT_HEXAGONAL) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_HEXAGONAL);
		};
		if (value == avutil.AV_CH_LAYOUT_6POINT1) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT1);
		};
		if (value == avutil.AV_CH_LAYOUT_6POINT1_BACK) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT1_BACK);
		};
		if (value == avutil.AV_CH_LAYOUT_6POINT1_FRONT) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT1_FRONT);
		};
		if (value == avutil.AV_CH_LAYOUT_7POINT0) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT0);
		};
		if (value == avutil.AV_CH_LAYOUT_7POINT0_FRONT) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT0_FRONT);
		};
		if (value == avutil.AV_CH_LAYOUT_7POINT1) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT1);
		};
		if (value == avutil.AV_CH_LAYOUT_7POINT1_WIDE) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT1_WIDE);
		};
		if (value == avutil.AV_CH_LAYOUT_7POINT1_WIDE_BACK) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT1_WIDE_BACK);
		};
		if (value == avutil.AV_CH_LAYOUT_OCTAGONAL) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_OCTAGONAL);
		};
		if (value == avutil.AV_CH_LAYOUT_HEXADECAGONAL) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_HEXADECAGONAL);
		};
		if (value == avutil.AV_CH_LAYOUT_STEREO_DOWNMIX) {
			return new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_STEREO_DOWNMIX);
		};
		return new FFmpegChannelLayout(value);
	}
	//
	public static FFmpegChannelLayout[] extractFFmpegChannelLayoutArray(int value) {
		ArrayList<FFmpegChannelLayout> flags= new ArrayList<>();
		if ((value & avutil.AV_CH_LAYOUT_MONO) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_MONO));
			value&= ~avutil.AV_CH_LAYOUT_MONO;
		};
		if ((value & avutil.AV_CH_LAYOUT_STEREO) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_STEREO));
			value&= ~avutil.AV_CH_LAYOUT_STEREO;
		};
		if ((value & avutil.AV_CH_LAYOUT_2POINT1) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_2POINT1));
			value&= ~avutil.AV_CH_LAYOUT_2POINT1;
		};
		if ((value & avutil.AV_CH_LAYOUT_2_1) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_2_1));
			value&= ~avutil.AV_CH_LAYOUT_2_1;
		};
		if ((value & avutil.AV_CH_LAYOUT_SURROUND) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_SURROUND));
			value&= ~avutil.AV_CH_LAYOUT_SURROUND;
		};
		if ((value & avutil.AV_CH_LAYOUT_3POINT1) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_3POINT1));
			value&= ~avutil.AV_CH_LAYOUT_3POINT1;
		};
		if ((value & avutil.AV_CH_LAYOUT_4POINT0) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_4POINT0));
			value&= ~avutil.AV_CH_LAYOUT_4POINT0;
		};
		if ((value & avutil.AV_CH_LAYOUT_4POINT1) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_4POINT1));
			value&= ~avutil.AV_CH_LAYOUT_4POINT1;
		};
		if ((value & avutil.AV_CH_LAYOUT_2_2) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_2_2));
			value&= ~avutil.AV_CH_LAYOUT_2_2;
		};
		if ((value & avutil.AV_CH_LAYOUT_QUAD) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_QUAD));
			value&= ~avutil.AV_CH_LAYOUT_QUAD;
		};
		if ((value & avutil.AV_CH_LAYOUT_5POINT0) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_5POINT0));
			value&= ~avutil.AV_CH_LAYOUT_5POINT0;
		};
		if ((value & avutil.AV_CH_LAYOUT_5POINT1) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_5POINT1));
			value&= ~avutil.AV_CH_LAYOUT_5POINT1;
		};
		if ((value & avutil.AV_CH_LAYOUT_5POINT0_BACK) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_5POINT0_BACK));
			value&= ~avutil.AV_CH_LAYOUT_5POINT0_BACK;
		};
		if ((value & avutil.AV_CH_LAYOUT_5POINT1_BACK) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_5POINT1_BACK));
			value&= ~avutil.AV_CH_LAYOUT_5POINT1_BACK;
		};
		if ((value & avutil.AV_CH_LAYOUT_6POINT0) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT0));
			value&= ~avutil.AV_CH_LAYOUT_6POINT0;
		};
		if ((value & avutil.AV_CH_LAYOUT_6POINT0_FRONT) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT0_FRONT));
			value&= ~avutil.AV_CH_LAYOUT_6POINT0_FRONT;
		};
		if ((value & avutil.AV_CH_LAYOUT_HEXAGONAL) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_HEXAGONAL));
			value&= ~avutil.AV_CH_LAYOUT_HEXAGONAL;
		};
		if ((value & avutil.AV_CH_LAYOUT_6POINT1) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT1));
			value&= ~avutil.AV_CH_LAYOUT_6POINT1;
		};
		if ((value & avutil.AV_CH_LAYOUT_6POINT1_BACK) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT1_BACK));
			value&= ~avutil.AV_CH_LAYOUT_6POINT1_BACK;
		};
		if ((value & avutil.AV_CH_LAYOUT_6POINT1_FRONT) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_6POINT1_FRONT));
			value&= ~avutil.AV_CH_LAYOUT_6POINT1_FRONT;
		};
		if ((value & avutil.AV_CH_LAYOUT_7POINT0) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT0));
			value&= ~avutil.AV_CH_LAYOUT_7POINT0;
		};
		if ((value & avutil.AV_CH_LAYOUT_7POINT0_FRONT) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT0_FRONT));
			value&= ~avutil.AV_CH_LAYOUT_7POINT0_FRONT;
		};
		if ((value & avutil.AV_CH_LAYOUT_7POINT1) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT1));
			value&= ~avutil.AV_CH_LAYOUT_7POINT1;
		};
		if ((value & avutil.AV_CH_LAYOUT_7POINT1_WIDE) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT1_WIDE));
			value&= ~avutil.AV_CH_LAYOUT_7POINT1_WIDE;
		};
		if ((value & avutil.AV_CH_LAYOUT_7POINT1_WIDE_BACK) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_7POINT1_WIDE_BACK));
			value&= ~avutil.AV_CH_LAYOUT_7POINT1_WIDE_BACK;
		};
		if ((value & avutil.AV_CH_LAYOUT_OCTAGONAL) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_OCTAGONAL));
			value&= ~avutil.AV_CH_LAYOUT_OCTAGONAL;
		};
		if ((value & avutil.AV_CH_LAYOUT_HEXADECAGONAL) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_HEXADECAGONAL));
			value&= ~avutil.AV_CH_LAYOUT_HEXADECAGONAL;
		};
		if ((value & avutil.AV_CH_LAYOUT_STEREO_DOWNMIX) != 0) {
			flags.add(new FFmpegChannelLayout(FFmpegChannelLayoutName.AV_CH_LAYOUT_STEREO_DOWNMIX));
			value&= ~avutil.AV_CH_LAYOUT_STEREO_DOWNMIX;
		};
		if (value != 0) {
			flags.add(new FFmpegChannelLayout(value));
		};
		return flags.toArray(new FFmpegChannelLayout[0]);
	}
}
