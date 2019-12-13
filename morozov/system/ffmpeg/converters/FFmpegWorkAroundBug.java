// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpegWorkAroundBug {
	//
	protected FFmpegWorkAroundBugName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegWorkAroundBug(FFmpegWorkAroundBugName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegWorkAroundBug(int v) {
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
	public FFmpegWorkAroundBugName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegWorkAroundBug argumentToFFmpegWorkAroundBug(Term value, ChoisePoint iX) {
		try {
			FFmpegWorkAroundBugName name= FFmpegWorkAroundBugName.argumentToFFmpegWorkAroundBugName(value,iX);
			return new FFmpegWorkAroundBug(name);
		} catch (TermIsNotFFmpegWorkAroundBugName e1) {
			try {
				return new FFmpegWorkAroundBug(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegWorkAroundBug(value);
			}
		}
	}
	//
	public static FFmpegWorkAroundBug integerToFFmpegWorkAroundBug(int value) {
		if (value == avcodec.AVCodecContext.FF_BUG_AUTODETECT) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_AUTODETECT);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_OLD_MSMPEG4) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_OLD_MSMPEG4);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_XVID_ILACE) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_XVID_ILACE);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_UMP4) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_UMP4);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_NO_PADDING) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_NO_PADDING);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_AMV) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_AMV);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_QPEL_CHROMA) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_QPEL_CHROMA);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_STD_QPEL) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_STD_QPEL);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_QPEL_CHROMA2) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_QPEL_CHROMA2);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_DIRECT_BLOCKSIZE) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_DIRECT_BLOCKSIZE);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_EDGE) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_EDGE);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_HPEL_CHROMA) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_HPEL_CHROMA);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_DC_CLIP) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_DC_CLIP);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_MS) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_MS);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_TRUNCATED) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_TRUNCATED);
		};
		if (value == avcodec.AVCodecContext.FF_BUG_IEDGE) {
			return new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_IEDGE);
		};
		return new FFmpegWorkAroundBug(value);
	}
	//
	public static FFmpegWorkAroundBug[] extractFFmpegWorkAroundBugArray(int value) {
		ArrayList<FFmpegWorkAroundBug> flags= new ArrayList<>();
		if ((value & avcodec.AVCodecContext.FF_BUG_AUTODETECT) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_AUTODETECT));
			value&= ~avcodec.AVCodecContext.FF_BUG_AUTODETECT;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_OLD_MSMPEG4) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_OLD_MSMPEG4));
			value&= ~avcodec.AVCodecContext.FF_BUG_OLD_MSMPEG4;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_XVID_ILACE) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_XVID_ILACE));
			value&= ~avcodec.AVCodecContext.FF_BUG_XVID_ILACE;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_UMP4) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_UMP4));
			value&= ~avcodec.AVCodecContext.FF_BUG_UMP4;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_NO_PADDING) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_NO_PADDING));
			value&= ~avcodec.AVCodecContext.FF_BUG_NO_PADDING;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_AMV) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_AMV));
			value&= ~avcodec.AVCodecContext.FF_BUG_AMV;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_QPEL_CHROMA) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_QPEL_CHROMA));
			value&= ~avcodec.AVCodecContext.FF_BUG_QPEL_CHROMA;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_STD_QPEL) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_STD_QPEL));
			value&= ~avcodec.AVCodecContext.FF_BUG_STD_QPEL;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_QPEL_CHROMA2) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_QPEL_CHROMA2));
			value&= ~avcodec.AVCodecContext.FF_BUG_QPEL_CHROMA2;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_DIRECT_BLOCKSIZE) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_DIRECT_BLOCKSIZE));
			value&= ~avcodec.AVCodecContext.FF_BUG_DIRECT_BLOCKSIZE;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_EDGE) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_EDGE));
			value&= ~avcodec.AVCodecContext.FF_BUG_EDGE;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_HPEL_CHROMA) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_HPEL_CHROMA));
			value&= ~avcodec.AVCodecContext.FF_BUG_HPEL_CHROMA;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_DC_CLIP) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_DC_CLIP));
			value&= ~avcodec.AVCodecContext.FF_BUG_DC_CLIP;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_MS) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_MS));
			value&= ~avcodec.AVCodecContext.FF_BUG_MS;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_TRUNCATED) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_TRUNCATED));
			value&= ~avcodec.AVCodecContext.FF_BUG_TRUNCATED;
		};
		if ((value & avcodec.AVCodecContext.FF_BUG_IEDGE) != 0) {
			flags.add(new FFmpegWorkAroundBug(FFmpegWorkAroundBugName.FF_BUG_IEDGE));
			value&= ~avcodec.AVCodecContext.FF_BUG_IEDGE;
		};
		if (value != 0) {
			flags.add(new FFmpegWorkAroundBug(value));
		};
		return flags.toArray(new FFmpegWorkAroundBug[flags.size()]);
	}
}
