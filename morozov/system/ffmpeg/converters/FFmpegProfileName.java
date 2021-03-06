// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avcodec;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegProfileName {
	//
	FF_PROFILE_UNKNOWN {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_UNKNOWN;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_UNKNOWN;
		}
	},
	FF_PROFILE_RESERVED {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_RESERVED;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_RESERVED;
		}
	},
	FF_PROFILE_AAC_MAIN {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_AAC_MAIN;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_AAC_MAIN;
		}
	},
	FF_PROFILE_AAC_LOW {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_AAC_LOW;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_AAC_LOW;
		}
	},
	FF_PROFILE_AAC_SSR {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_AAC_SSR;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_AAC_SSR;
		}
	},
	FF_PROFILE_AAC_LTP {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_AAC_LTP;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_AAC_LTP;
		}
	},
	FF_PROFILE_AAC_HE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_AAC_HE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_AAC_HE;
		}
	},
	FF_PROFILE_AAC_HE_V2 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_AAC_HE_V2;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_AAC_HE_V2;
		}
	},
	FF_PROFILE_AAC_LD {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_AAC_LD;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_AAC_LD;
		}
	},
	FF_PROFILE_AAC_ELD {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_AAC_ELD;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_AAC_ELD;
		}
	},
	FF_PROFILE_MPEG2_AAC_LOW {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG2_AAC_LOW;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG2_AAC_LOW;
		}
	},
	FF_PROFILE_MPEG2_AAC_HE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG2_AAC_HE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG2_AAC_HE;
		}
	},
	FF_PROFILE_DNXHD {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_DNXHD;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_DNXHD;
		}
	},
	FF_PROFILE_DNXHR_LB {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_DNXHR_LB;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_DNXHR_LB;
		}
	},
	FF_PROFILE_DNXHR_SQ {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_DNXHR_SQ;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_DNXHR_SQ;
		}
	},
	FF_PROFILE_DNXHR_HQ {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_DNXHR_HQ;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_DNXHR_HQ;
		}
	},
	FF_PROFILE_DNXHR_HQX {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_DNXHR_HQX;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_DNXHR_HQX;
		}
	},
	FF_PROFILE_DNXHR_444 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_DNXHR_444;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_DNXHR_444;
		}
	},
	FF_PROFILE_DTS {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_DTS;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_DTS;
		}
	},
	FF_PROFILE_DTS_ES {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_DTS_ES;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_DTS_ES;
		}
	},
	FF_PROFILE_DTS_96_24 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_DTS_96_24;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_DTS_96_24;
		}
	},
	FF_PROFILE_DTS_HD_HRA {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_DTS_HD_HRA;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_DTS_HD_HRA;
		}
	},
	FF_PROFILE_DTS_HD_MA {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_DTS_HD_MA;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_DTS_HD_MA;
		}
	},
	FF_PROFILE_DTS_EXPRESS {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_DTS_EXPRESS;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_DTS_EXPRESS;
		}
	},
	FF_PROFILE_MPEG2_422 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG2_422;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG2_422;
		}
	},
	FF_PROFILE_MPEG2_HIGH {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG2_HIGH;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG2_HIGH;
		}
	},
	FF_PROFILE_MPEG2_SS {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG2_SS;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG2_SS;
		}
	},
	FF_PROFILE_MPEG2_SNR_SCALABLE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG2_SNR_SCALABLE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG2_SNR_SCALABLE;
		}
	},
	FF_PROFILE_MPEG2_MAIN {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG2_MAIN;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG2_MAIN;
		}
	},
	FF_PROFILE_MPEG2_SIMPLE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG2_SIMPLE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG2_SIMPLE;
		}
	},
	FF_PROFILE_H264_CONSTRAINED {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_CONSTRAINED;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_CONSTRAINED;
		}
	},
	FF_PROFILE_H264_INTRA {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_INTRA;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_INTRA;
		}
	},
	FF_PROFILE_H264_BASELINE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_BASELINE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_BASELINE;
		}
	},
	FF_PROFILE_H264_CONSTRAINED_BASELINE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_CONSTRAINED_BASELINE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_CONSTRAINED_BASELINE;
		}
	},
	FF_PROFILE_H264_MAIN {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_MAIN;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_MAIN;
		}
	},
	FF_PROFILE_H264_EXTENDED {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_EXTENDED;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_EXTENDED;
		}
	},
	FF_PROFILE_H264_HIGH {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_HIGH;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_HIGH;
		}
	},
	FF_PROFILE_H264_HIGH_10 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_10;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_HIGH_10;
		}
	},
	FF_PROFILE_H264_HIGH_10_INTRA {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_10_INTRA;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_HIGH_10_INTRA;
		}
	},
	FF_PROFILE_H264_MULTIVIEW_HIGH {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_MULTIVIEW_HIGH;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_MULTIVIEW_HIGH;
		}
	},
	FF_PROFILE_H264_HIGH_422 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_422;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_HIGH_422;
		}
	},
	FF_PROFILE_H264_HIGH_422_INTRA {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_422_INTRA;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_HIGH_422_INTRA;
		}
	},
	FF_PROFILE_H264_STEREO_HIGH {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_STEREO_HIGH;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_STEREO_HIGH;
		}
	},
	FF_PROFILE_H264_HIGH_444 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_444;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_HIGH_444;
		}
	},
	FF_PROFILE_H264_HIGH_444_PREDICTIVE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_444_PREDICTIVE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_HIGH_444_PREDICTIVE;
		}
	},
	FF_PROFILE_H264_HIGH_444_INTRA {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_444_INTRA;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_HIGH_444_INTRA;
		}
	},
	FF_PROFILE_H264_CAVLC_444 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_H264_CAVLC_444;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_H264_CAVLC_444;
		}
	},
	FF_PROFILE_VC1_SIMPLE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_VC1_SIMPLE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_VC1_SIMPLE;
		}
	},
	FF_PROFILE_VC1_MAIN {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_VC1_MAIN;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_VC1_MAIN;
		}
	},
	FF_PROFILE_VC1_COMPLEX {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_VC1_COMPLEX;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_VC1_COMPLEX;
		}
	},
	FF_PROFILE_VC1_ADVANCED {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_VC1_ADVANCED;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_VC1_ADVANCED;
		}
	},
	FF_PROFILE_MPEG4_SIMPLE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_SIMPLE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_SIMPLE;
		}
	},
	FF_PROFILE_MPEG4_SIMPLE_SCALABLE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_SIMPLE_SCALABLE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_SIMPLE_SCALABLE;
		}
	},
	FF_PROFILE_MPEG4_CORE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_CORE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_CORE;
		}
	},
	FF_PROFILE_MPEG4_MAIN {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_MAIN;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_MAIN;
		}
	},
	FF_PROFILE_MPEG4_N_BIT {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_N_BIT;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_N_BIT;
		}
	},
	FF_PROFILE_MPEG4_SCALABLE_TEXTURE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_SCALABLE_TEXTURE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_SCALABLE_TEXTURE;
		}
	},
	FF_PROFILE_MPEG4_SIMPLE_FACE_ANIMATION {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_SIMPLE_FACE_ANIMATION;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_SIMPLE_FACE_ANIMATION;
		}
	},
	FF_PROFILE_MPEG4_BASIC_ANIMATED_TEXTURE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_BASIC_ANIMATED_TEXTURE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_BASIC_ANIMATED_TEXTURE;
		}
	},
	FF_PROFILE_MPEG4_HYBRID {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_HYBRID;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_HYBRID;
		}
	},
	FF_PROFILE_MPEG4_ADVANCED_REAL_TIME {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_ADVANCED_REAL_TIME;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_ADVANCED_REAL_TIME;
		}
	},
	FF_PROFILE_MPEG4_CORE_SCALABLE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_CORE_SCALABLE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_CORE_SCALABLE;
		}
	},
	FF_PROFILE_MPEG4_ADVANCED_CODING {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_ADVANCED_CODING;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_ADVANCED_CODING;
		}
	},
	FF_PROFILE_MPEG4_ADVANCED_CORE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_ADVANCED_CORE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_ADVANCED_CORE;
		}
	},
	FF_PROFILE_MPEG4_ADVANCED_SCALABLE_TEXTURE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_ADVANCED_SCALABLE_TEXTURE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_ADVANCED_SCALABLE_TEXTURE;
		}
	},
	FF_PROFILE_MPEG4_SIMPLE_STUDIO {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_SIMPLE_STUDIO;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_SIMPLE_STUDIO;
		}
	},
	FF_PROFILE_MPEG4_ADVANCED_SIMPLE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_MPEG4_ADVANCED_SIMPLE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_MPEG4_ADVANCED_SIMPLE;
		}
	},
	FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_0 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_0;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_0;
		}
	},
	FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_1 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_1;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_1;
		}
	},
	FF_PROFILE_JPEG2000_CSTREAM_NO_RESTRICTION {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_JPEG2000_CSTREAM_NO_RESTRICTION;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_JPEG2000_CSTREAM_NO_RESTRICTION;
		}
	},
	FF_PROFILE_JPEG2000_DCINEMA_2K {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_JPEG2000_DCINEMA_2K;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_JPEG2000_DCINEMA_2K;
		}
	},
	FF_PROFILE_JPEG2000_DCINEMA_4K {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_JPEG2000_DCINEMA_4K;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_JPEG2000_DCINEMA_4K;
		}
	},
	FF_PROFILE_VP9_0 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_VP9_0;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_VP9_0;
		}
	},
	FF_PROFILE_VP9_1 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_VP9_1;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_VP9_1;
		}
	},
	FF_PROFILE_VP9_2 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_VP9_2;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_VP9_2;
		}
	},
	FF_PROFILE_VP9_3 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_VP9_3;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_VP9_3;
		}
	},
	FF_PROFILE_HEVC_MAIN {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_HEVC_MAIN;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_HEVC_MAIN;
		}
	},
	FF_PROFILE_HEVC_MAIN_10 {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_HEVC_MAIN_10;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_HEVC_MAIN_10;
		}
	},
	FF_PROFILE_HEVC_MAIN_STILL_PICTURE {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_HEVC_MAIN_STILL_PICTURE;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_HEVC_MAIN_STILL_PICTURE;
		}
	},
	FF_PROFILE_HEVC_REXT {
		@Override
		public int toInteger() {
			return avcodec.AVCodecContext.FF_PROFILE_HEVC_REXT;
		}
		@Override
		public Term toTerm() {
			return term_FF_PROFILE_HEVC_REXT;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegProfileName argumentToFFmpegProfileName(Term value, ChoisePoint iX) throws TermIsNotFFmpegProfileName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_FF_PROFILE_UNKNOWN) {
				return FFmpegProfileName.FF_PROFILE_UNKNOWN;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_RESERVED) {
				return FFmpegProfileName.FF_PROFILE_RESERVED;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_AAC_MAIN) {
				return FFmpegProfileName.FF_PROFILE_AAC_MAIN;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_AAC_LOW) {
				return FFmpegProfileName.FF_PROFILE_AAC_LOW;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_AAC_SSR) {
				return FFmpegProfileName.FF_PROFILE_AAC_SSR;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_AAC_LTP) {
				return FFmpegProfileName.FF_PROFILE_AAC_LTP;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_AAC_HE) {
				return FFmpegProfileName.FF_PROFILE_AAC_HE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_AAC_HE_V2) {
				return FFmpegProfileName.FF_PROFILE_AAC_HE_V2;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_AAC_LD) {
				return FFmpegProfileName.FF_PROFILE_AAC_LD;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_AAC_ELD) {
				return FFmpegProfileName.FF_PROFILE_AAC_ELD;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_AAC_LOW) {
				return FFmpegProfileName.FF_PROFILE_MPEG2_AAC_LOW;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_AAC_HE) {
				return FFmpegProfileName.FF_PROFILE_MPEG2_AAC_HE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_DNXHD) {
				return FFmpegProfileName.FF_PROFILE_DNXHD;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_DNXHR_LB) {
				return FFmpegProfileName.FF_PROFILE_DNXHR_LB;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_DNXHR_SQ) {
				return FFmpegProfileName.FF_PROFILE_DNXHR_SQ;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_DNXHR_HQ) {
				return FFmpegProfileName.FF_PROFILE_DNXHR_HQ;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_DNXHR_HQX) {
				return FFmpegProfileName.FF_PROFILE_DNXHR_HQX;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_DNXHR_444) {
				return FFmpegProfileName.FF_PROFILE_DNXHR_444;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_DTS) {
				return FFmpegProfileName.FF_PROFILE_DTS;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_DTS_ES) {
				return FFmpegProfileName.FF_PROFILE_DTS_ES;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_DTS_96_24) {
				return FFmpegProfileName.FF_PROFILE_DTS_96_24;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_DTS_HD_HRA) {
				return FFmpegProfileName.FF_PROFILE_DTS_HD_HRA;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_DTS_HD_MA) {
				return FFmpegProfileName.FF_PROFILE_DTS_HD_MA;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_DTS_EXPRESS) {
				return FFmpegProfileName.FF_PROFILE_DTS_EXPRESS;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_422) {
				return FFmpegProfileName.FF_PROFILE_MPEG2_422;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_HIGH) {
				return FFmpegProfileName.FF_PROFILE_MPEG2_HIGH;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_SS) {
				return FFmpegProfileName.FF_PROFILE_MPEG2_SS;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_SNR_SCALABLE) {
				return FFmpegProfileName.FF_PROFILE_MPEG2_SNR_SCALABLE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_MAIN) {
				return FFmpegProfileName.FF_PROFILE_MPEG2_MAIN;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_SIMPLE) {
				return FFmpegProfileName.FF_PROFILE_MPEG2_SIMPLE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_CONSTRAINED) {
				return FFmpegProfileName.FF_PROFILE_H264_CONSTRAINED;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_INTRA) {
				return FFmpegProfileName.FF_PROFILE_H264_INTRA;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_BASELINE) {
				return FFmpegProfileName.FF_PROFILE_H264_BASELINE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_CONSTRAINED_BASELINE) {
				return FFmpegProfileName.FF_PROFILE_H264_CONSTRAINED_BASELINE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_MAIN) {
				return FFmpegProfileName.FF_PROFILE_H264_MAIN;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_EXTENDED) {
				return FFmpegProfileName.FF_PROFILE_H264_EXTENDED;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH) {
				return FFmpegProfileName.FF_PROFILE_H264_HIGH;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_10) {
				return FFmpegProfileName.FF_PROFILE_H264_HIGH_10;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_10_INTRA) {
				return FFmpegProfileName.FF_PROFILE_H264_HIGH_10_INTRA;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_MULTIVIEW_HIGH) {
				return FFmpegProfileName.FF_PROFILE_H264_MULTIVIEW_HIGH;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_422) {
				return FFmpegProfileName.FF_PROFILE_H264_HIGH_422;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_422_INTRA) {
				return FFmpegProfileName.FF_PROFILE_H264_HIGH_422_INTRA;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_STEREO_HIGH) {
				return FFmpegProfileName.FF_PROFILE_H264_STEREO_HIGH;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_444) {
				return FFmpegProfileName.FF_PROFILE_H264_HIGH_444;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_444_PREDICTIVE) {
				return FFmpegProfileName.FF_PROFILE_H264_HIGH_444_PREDICTIVE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_444_INTRA) {
				return FFmpegProfileName.FF_PROFILE_H264_HIGH_444_INTRA;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_H264_CAVLC_444) {
				return FFmpegProfileName.FF_PROFILE_H264_CAVLC_444;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_VC1_SIMPLE) {
				return FFmpegProfileName.FF_PROFILE_VC1_SIMPLE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_VC1_MAIN) {
				return FFmpegProfileName.FF_PROFILE_VC1_MAIN;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_VC1_COMPLEX) {
				return FFmpegProfileName.FF_PROFILE_VC1_COMPLEX;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_VC1_ADVANCED) {
				return FFmpegProfileName.FF_PROFILE_VC1_ADVANCED;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_SIMPLE) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_SIMPLE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_SIMPLE_SCALABLE) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_SIMPLE_SCALABLE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_CORE) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_CORE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_MAIN) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_MAIN;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_N_BIT) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_N_BIT;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_SCALABLE_TEXTURE) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_SCALABLE_TEXTURE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_SIMPLE_FACE_ANIMATION) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_SIMPLE_FACE_ANIMATION;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_BASIC_ANIMATED_TEXTURE) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_BASIC_ANIMATED_TEXTURE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_HYBRID) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_HYBRID;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_ADVANCED_REAL_TIME) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_ADVANCED_REAL_TIME;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_CORE_SCALABLE) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_CORE_SCALABLE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_ADVANCED_CODING) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_ADVANCED_CODING;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_ADVANCED_CORE) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_ADVANCED_CORE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_ADVANCED_SCALABLE_TEXTURE) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_ADVANCED_SCALABLE_TEXTURE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_SIMPLE_STUDIO) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_SIMPLE_STUDIO;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_ADVANCED_SIMPLE) {
				return FFmpegProfileName.FF_PROFILE_MPEG4_ADVANCED_SIMPLE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_0) {
				return FFmpegProfileName.FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_0;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_1) {
				return FFmpegProfileName.FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_1;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_JPEG2000_CSTREAM_NO_RESTRICTION) {
				return FFmpegProfileName.FF_PROFILE_JPEG2000_CSTREAM_NO_RESTRICTION;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_JPEG2000_DCINEMA_2K) {
				return FFmpegProfileName.FF_PROFILE_JPEG2000_DCINEMA_2K;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_JPEG2000_DCINEMA_4K) {
				return FFmpegProfileName.FF_PROFILE_JPEG2000_DCINEMA_4K;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_VP9_0) {
				return FFmpegProfileName.FF_PROFILE_VP9_0;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_VP9_1) {
				return FFmpegProfileName.FF_PROFILE_VP9_1;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_VP9_2) {
				return FFmpegProfileName.FF_PROFILE_VP9_2;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_VP9_3) {
				return FFmpegProfileName.FF_PROFILE_VP9_3;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_HEVC_MAIN) {
				return FFmpegProfileName.FF_PROFILE_HEVC_MAIN;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_HEVC_MAIN_10) {
				return FFmpegProfileName.FF_PROFILE_HEVC_MAIN_10;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_HEVC_MAIN_STILL_PICTURE) {
				return FFmpegProfileName.FF_PROFILE_HEVC_MAIN_STILL_PICTURE;
			} else if (code==SymbolCodes.symbolCode_E_FF_PROFILE_HEVC_REXT) {
				return FFmpegProfileName.FF_PROFILE_HEVC_REXT;
			} else {
				throw TermIsNotFFmpegProfileName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegProfileName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_FF_PROFILE_UNKNOWN= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_UNKNOWN);
	protected static Term term_FF_PROFILE_RESERVED= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_RESERVED);
	protected static Term term_FF_PROFILE_AAC_MAIN= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_AAC_MAIN);
	protected static Term term_FF_PROFILE_AAC_LOW= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_AAC_LOW);
	protected static Term term_FF_PROFILE_AAC_SSR= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_AAC_SSR);
	protected static Term term_FF_PROFILE_AAC_LTP= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_AAC_LTP);
	protected static Term term_FF_PROFILE_AAC_HE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_AAC_HE);
	protected static Term term_FF_PROFILE_AAC_HE_V2= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_AAC_HE_V2);
	protected static Term term_FF_PROFILE_AAC_LD= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_AAC_LD);
	protected static Term term_FF_PROFILE_AAC_ELD= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_AAC_ELD);
	protected static Term term_FF_PROFILE_MPEG2_AAC_LOW= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_AAC_LOW);
	protected static Term term_FF_PROFILE_MPEG2_AAC_HE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_AAC_HE);
	protected static Term term_FF_PROFILE_DNXHD= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_DNXHD);
	protected static Term term_FF_PROFILE_DNXHR_LB= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_DNXHR_LB);
	protected static Term term_FF_PROFILE_DNXHR_SQ= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_DNXHR_SQ);
	protected static Term term_FF_PROFILE_DNXHR_HQ= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_DNXHR_HQ);
	protected static Term term_FF_PROFILE_DNXHR_HQX= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_DNXHR_HQX);
	protected static Term term_FF_PROFILE_DNXHR_444= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_DNXHR_444);
	protected static Term term_FF_PROFILE_DTS= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_DTS);
	protected static Term term_FF_PROFILE_DTS_ES= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_DTS_ES);
	protected static Term term_FF_PROFILE_DTS_96_24= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_DTS_96_24);
	protected static Term term_FF_PROFILE_DTS_HD_HRA= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_DTS_HD_HRA);
	protected static Term term_FF_PROFILE_DTS_HD_MA= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_DTS_HD_MA);
	protected static Term term_FF_PROFILE_DTS_EXPRESS= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_DTS_EXPRESS);
	protected static Term term_FF_PROFILE_MPEG2_422= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_422);
	protected static Term term_FF_PROFILE_MPEG2_HIGH= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_HIGH);
	protected static Term term_FF_PROFILE_MPEG2_SS= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_SS);
	protected static Term term_FF_PROFILE_MPEG2_SNR_SCALABLE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_SNR_SCALABLE);
	protected static Term term_FF_PROFILE_MPEG2_MAIN= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_MAIN);
	protected static Term term_FF_PROFILE_MPEG2_SIMPLE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG2_SIMPLE);
	protected static Term term_FF_PROFILE_H264_CONSTRAINED= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_CONSTRAINED);
	protected static Term term_FF_PROFILE_H264_INTRA= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_INTRA);
	protected static Term term_FF_PROFILE_H264_BASELINE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_BASELINE);
	protected static Term term_FF_PROFILE_H264_CONSTRAINED_BASELINE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_CONSTRAINED_BASELINE);
	protected static Term term_FF_PROFILE_H264_MAIN= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_MAIN);
	protected static Term term_FF_PROFILE_H264_EXTENDED= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_EXTENDED);
	protected static Term term_FF_PROFILE_H264_HIGH= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH);
	protected static Term term_FF_PROFILE_H264_HIGH_10= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_10);
	protected static Term term_FF_PROFILE_H264_HIGH_10_INTRA= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_10_INTRA);
	protected static Term term_FF_PROFILE_H264_MULTIVIEW_HIGH= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_MULTIVIEW_HIGH);
	protected static Term term_FF_PROFILE_H264_HIGH_422= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_422);
	protected static Term term_FF_PROFILE_H264_HIGH_422_INTRA= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_422_INTRA);
	protected static Term term_FF_PROFILE_H264_STEREO_HIGH= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_STEREO_HIGH);
	protected static Term term_FF_PROFILE_H264_HIGH_444= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_444);
	protected static Term term_FF_PROFILE_H264_HIGH_444_PREDICTIVE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_444_PREDICTIVE);
	protected static Term term_FF_PROFILE_H264_HIGH_444_INTRA= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_HIGH_444_INTRA);
	protected static Term term_FF_PROFILE_H264_CAVLC_444= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_H264_CAVLC_444);
	protected static Term term_FF_PROFILE_VC1_SIMPLE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_VC1_SIMPLE);
	protected static Term term_FF_PROFILE_VC1_MAIN= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_VC1_MAIN);
	protected static Term term_FF_PROFILE_VC1_COMPLEX= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_VC1_COMPLEX);
	protected static Term term_FF_PROFILE_VC1_ADVANCED= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_VC1_ADVANCED);
	protected static Term term_FF_PROFILE_MPEG4_SIMPLE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_SIMPLE);
	protected static Term term_FF_PROFILE_MPEG4_SIMPLE_SCALABLE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_SIMPLE_SCALABLE);
	protected static Term term_FF_PROFILE_MPEG4_CORE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_CORE);
	protected static Term term_FF_PROFILE_MPEG4_MAIN= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_MAIN);
	protected static Term term_FF_PROFILE_MPEG4_N_BIT= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_N_BIT);
	protected static Term term_FF_PROFILE_MPEG4_SCALABLE_TEXTURE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_SCALABLE_TEXTURE);
	protected static Term term_FF_PROFILE_MPEG4_SIMPLE_FACE_ANIMATION= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_SIMPLE_FACE_ANIMATION);
	protected static Term term_FF_PROFILE_MPEG4_BASIC_ANIMATED_TEXTURE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_BASIC_ANIMATED_TEXTURE);
	protected static Term term_FF_PROFILE_MPEG4_HYBRID= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_HYBRID);
	protected static Term term_FF_PROFILE_MPEG4_ADVANCED_REAL_TIME= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_ADVANCED_REAL_TIME);
	protected static Term term_FF_PROFILE_MPEG4_CORE_SCALABLE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_CORE_SCALABLE);
	protected static Term term_FF_PROFILE_MPEG4_ADVANCED_CODING= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_ADVANCED_CODING);
	protected static Term term_FF_PROFILE_MPEG4_ADVANCED_CORE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_ADVANCED_CORE);
	protected static Term term_FF_PROFILE_MPEG4_ADVANCED_SCALABLE_TEXTURE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_ADVANCED_SCALABLE_TEXTURE);
	protected static Term term_FF_PROFILE_MPEG4_SIMPLE_STUDIO= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_SIMPLE_STUDIO);
	protected static Term term_FF_PROFILE_MPEG4_ADVANCED_SIMPLE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_MPEG4_ADVANCED_SIMPLE);
	protected static Term term_FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_0= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_0);
	protected static Term term_FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_1= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_JPEG2000_CSTREAM_RESTRICTION_1);
	protected static Term term_FF_PROFILE_JPEG2000_CSTREAM_NO_RESTRICTION= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_JPEG2000_CSTREAM_NO_RESTRICTION);
	protected static Term term_FF_PROFILE_JPEG2000_DCINEMA_2K= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_JPEG2000_DCINEMA_2K);
	protected static Term term_FF_PROFILE_JPEG2000_DCINEMA_4K= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_JPEG2000_DCINEMA_4K);
	protected static Term term_FF_PROFILE_VP9_0= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_VP9_0);
	protected static Term term_FF_PROFILE_VP9_1= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_VP9_1);
	protected static Term term_FF_PROFILE_VP9_2= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_VP9_2);
	protected static Term term_FF_PROFILE_VP9_3= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_VP9_3);
	protected static Term term_FF_PROFILE_HEVC_MAIN= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_HEVC_MAIN);
	protected static Term term_FF_PROFILE_HEVC_MAIN_10= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_HEVC_MAIN_10);
	protected static Term term_FF_PROFILE_HEVC_MAIN_STILL_PICTURE= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_HEVC_MAIN_STILL_PICTURE);
	protected static Term term_FF_PROFILE_HEVC_REXT= new PrologSymbol(SymbolCodes.symbolCode_E_FF_PROFILE_HEVC_REXT);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
