// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpegDebugOption {
	//
	protected FFmpegDebugOptionName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegDebugOption(FFmpegDebugOptionName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegDebugOption(int v) {
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
	public FFmpegDebugOptionName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegDebugOption argumentToFFmpegDebugOption(Term value, ChoisePoint iX) {
		try {
			FFmpegDebugOptionName name= FFmpegDebugOptionName.argumentToFFmpegDebugOptionName(value,iX);
			return new FFmpegDebugOption(name);
		} catch (TermIsNotFFmpegDebugOptionName e1) {
			try {
				return new FFmpegDebugOption(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegDebugOption(value);
			}
		}
	}
	//
	public static FFmpegDebugOption integerToFFmpegDebugOption(int value) {
		if (value == avcodec.AVCodecContext.FF_DEBUG_PICT_INFO) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_PICT_INFO);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_RC) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_RC);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_BITSTREAM) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_BITSTREAM);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_MB_TYPE) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_MB_TYPE);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_QP) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_QP);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_DCT_COEFF) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_DCT_COEFF);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_SKIP) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_SKIP);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_STARTCODE) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_STARTCODE);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_PTS) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_PTS);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_ER) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_ER);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_MMCO) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_MMCO);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_BUGS) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_BUGS);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_BUFFERS) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_BUFFERS);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_THREADS) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_THREADS);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_GREEN_MD) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_GREEN_MD);
		};
		if (value == avcodec.AVCodecContext.FF_DEBUG_NOMC) {
			return new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_NOMC);
		};
		return new FFmpegDebugOption(value);
	}
	//
	public static FFmpegDebugOption[] extractFFmpegDebugOptionArray(int value) {
		ArrayList<FFmpegDebugOption> flags= new ArrayList<>();
		if ((value & avcodec.AVCodecContext.FF_DEBUG_PICT_INFO) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_PICT_INFO));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_PICT_INFO;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_RC) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_RC));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_RC;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_BITSTREAM) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_BITSTREAM));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_BITSTREAM;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_MB_TYPE) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_MB_TYPE));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_MB_TYPE;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_QP) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_QP));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_QP;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_DCT_COEFF) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_DCT_COEFF));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_DCT_COEFF;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_SKIP) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_SKIP));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_SKIP;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_STARTCODE) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_STARTCODE));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_STARTCODE;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_PTS) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_PTS));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_PTS;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_ER) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_ER));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_ER;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_MMCO) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_MMCO));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_MMCO;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_BUGS) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_BUGS));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_BUGS;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_BUFFERS) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_BUFFERS));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_BUFFERS;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_THREADS) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_THREADS));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_THREADS;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_GREEN_MD) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_GREEN_MD));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_GREEN_MD;
		};
		if ((value & avcodec.AVCodecContext.FF_DEBUG_NOMC) != 0) {
			flags.add(new FFmpegDebugOption(FFmpegDebugOptionName.FF_DEBUG_NOMC));
			value&= ~avcodec.AVCodecContext.FF_DEBUG_NOMC;
		};
		if (value != 0) {
			flags.add(new FFmpegDebugOption(value));
		};
		return flags.toArray(new FFmpegDebugOption[0]);
	}
}
