// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpegProfile {
	//
	protected FFmpegProfileName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegProfile(FFmpegProfileName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegProfile(int v) {
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
	public FFmpegProfileName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegProfile argumentToFFmpegProfile(Term value, ChoisePoint iX) {
		try {
			FFmpegProfileName name= FFmpegProfileName.argumentToFFmpegProfileName(value,iX);
			return new FFmpegProfile(name);
		} catch (TermIsNotFFmpegProfileName e1) {
			try {
				return new FFmpegProfile(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegProfile(value);
			}
		}
	}
	//
	public static FFmpegProfile integerToFFmpegProfile(int value) {
		if (value == avcodec.AVCodecContext.FF_PROFILE_UNKNOWN) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_UNKNOWN);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_RESERVED) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_RESERVED);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_AAC_MAIN) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_AAC_MAIN);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_AAC_LOW) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_AAC_LOW);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_AAC_SSR) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_AAC_SSR);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_AAC_LTP) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_AAC_LTP);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_AAC_HE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_AAC_HE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_AAC_HE_V2) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_AAC_HE_V2);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_AAC_LD) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_AAC_LD);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_AAC_ELD) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_AAC_ELD);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG2_AAC_LOW) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG2_AAC_LOW);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG2_AAC_HE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG2_AAC_HE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_DNXHD) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_DNXHD);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_DNXHR_LB) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_DNXHR_LB);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_DNXHR_SQ) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_DNXHR_SQ);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_DNXHR_HQ) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_DNXHR_HQ);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_DNXHR_HQX) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_DNXHR_HQX);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_DNXHR_444) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_DNXHR_444);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_DTS) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_DTS);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_DTS_ES) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_DTS_ES);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_DTS_96_24) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_DTS_96_24);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_DTS_HD_HRA) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_DTS_HD_HRA);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_DTS_HD_MA) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_DTS_HD_MA);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_DTS_EXPRESS) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_DTS_EXPRESS);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG2_422) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG2_422);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG2_HIGH) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG2_HIGH);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG2_SS) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG2_SS);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG2_SNR_SCALABLE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG2_SNR_SCALABLE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG2_MAIN) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG2_MAIN);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG2_SIMPLE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG2_SIMPLE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_CONSTRAINED) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_CONSTRAINED);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_INTRA) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_INTRA);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_BASELINE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_BASELINE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_CONSTRAINED_BASELINE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_CONSTRAINED_BASELINE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_MAIN) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_MAIN);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_EXTENDED) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_EXTENDED);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_HIGH) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_HIGH);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_10) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_HIGH_10);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_10_INTRA) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_HIGH_10_INTRA);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_MULTIVIEW_HIGH) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_MULTIVIEW_HIGH);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_422) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_HIGH_422);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_422_INTRA) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_HIGH_422_INTRA);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_STEREO_HIGH) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_STEREO_HIGH);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_444) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_HIGH_444);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_444_PREDICTIVE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_HIGH_444_PREDICTIVE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_444_INTRA) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_HIGH_444_INTRA);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_H264_CAVLC_444) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_H264_CAVLC_444);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_VC1_SIMPLE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_VC1_SIMPLE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_VC1_MAIN) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_VC1_MAIN);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_VC1_COMPLEX) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_VC1_COMPLEX);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_VC1_ADVANCED) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_VC1_ADVANCED);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_SIMPLE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_SIMPLE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_SIMPLE_SCALABLE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_SIMPLE_SCALABLE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_CORE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_CORE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_MAIN) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_MAIN);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_N_BIT) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_N_BIT);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_SCALABLE_TEXTURE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_SCALABLE_TEXTURE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_SIMPLE_FACE_ANIMATION) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_SIMPLE_FACE_ANIMATION);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_BASIC_ANIMATED_TEXTURE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_BASIC_ANIMATED_TEXTURE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_HYBRID) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_HYBRID);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_ADVANCED_REAL_TIME) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_ADVANCED_REAL_TIME);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_CORE_SCALABLE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_CORE_SCALABLE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_ADVANCED_CODING) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_ADVANCED_CODING);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_ADVANCED_CORE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_ADVANCED_CORE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_ADVANCED_SCALABLE_TEXTURE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_ADVANCED_SCALABLE_TEXTURE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_SIMPLE_STUDIO) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_SIMPLE_STUDIO);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_MPEG4_ADVANCED_SIMPLE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_MPEG4_ADVANCED_SIMPLE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_0) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_0);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_1) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_1);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_JPEG2000_CSTREAM_NO_RESTRICTION) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_JPEG2000_CSTREAM_NO_RESTRICTION);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_JPEG2000_DCINEMA_2K) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_JPEG2000_DCINEMA_2K);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_JPEG2000_DCINEMA_4K) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_JPEG2000_DCINEMA_4K);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_VP9_0) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_VP9_0);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_VP9_1) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_VP9_1);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_VP9_2) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_VP9_2);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_VP9_3) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_VP9_3);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_HEVC_MAIN) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_HEVC_MAIN);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_HEVC_MAIN_10) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_HEVC_MAIN_10);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_HEVC_MAIN_STILL_PICTURE) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_HEVC_MAIN_STILL_PICTURE);
		};
		if (value == avcodec.AVCodecContext.FF_PROFILE_HEVC_REXT) {
			return new FFmpegProfile(FFmpegProfileName.FF_PROFILE_HEVC_REXT);
		};
		return new FFmpegProfile(value);
	}
}
