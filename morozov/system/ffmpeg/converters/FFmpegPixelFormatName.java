// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avutil;

import target.*;

import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public enum FFmpegPixelFormatName {
	//
	AV_PIX_FMT_NONE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_NONE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_NONE;
		}
	},
	AV_PIX_FMT_YUV420P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV420P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV420P;
		}
	},
	AV_PIX_FMT_YUYV422 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUYV422;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUYV422;
		}
	},
	AV_PIX_FMT_RGB24 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB24;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB24;
		}
	},
	AV_PIX_FMT_BGR24 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR24;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR24;
		}
	},
	AV_PIX_FMT_YUV422P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV422P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV422P;
		}
	},
	AV_PIX_FMT_YUV444P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV444P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV444P;
		}
	},
	AV_PIX_FMT_YUV410P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV410P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV410P;
		}
	},
	AV_PIX_FMT_YUV411P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV411P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV411P;
		}
	},
	AV_PIX_FMT_GRAY8 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GRAY8;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GRAY8;
		}
	},
	AV_PIX_FMT_MONOWHITE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_MONOWHITE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_MONOWHITE;
		}
	},
	AV_PIX_FMT_MONOBLACK {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_MONOBLACK;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_MONOBLACK;
		}
	},
	AV_PIX_FMT_PAL8 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_PAL8;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_PAL8;
		}
	},
	AV_PIX_FMT_YUVJ420P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVJ420P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVJ420P;
		}
	},
	AV_PIX_FMT_YUVJ422P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVJ422P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVJ422P;
		}
	},
	AV_PIX_FMT_YUVJ444P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVJ444P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVJ444P;
		}
	},
	AV_PIX_FMT_XVMC_MPEG2_MC {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_XVMC_MPEG2_MC;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_XVMC_MPEG2_MC;
		}
	},
	AV_PIX_FMT_XVMC_MPEG2_IDCT {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_XVMC_MPEG2_IDCT;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_XVMC_MPEG2_IDCT;
		}
	},
	AV_PIX_FMT_UYVY422 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_UYVY422;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_UYVY422;
		}
	},
	AV_PIX_FMT_UYYVYY411 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_UYYVYY411;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_UYYVYY411;
		}
	},
	AV_PIX_FMT_BGR8 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR8;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR8;
		}
	},
	AV_PIX_FMT_BGR4 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR4;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR4;
		}
	},
	AV_PIX_FMT_BGR4_BYTE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR4_BYTE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR4_BYTE;
		}
	},
	AV_PIX_FMT_RGB8 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB8;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB8;
		}
	},
	AV_PIX_FMT_RGB4 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB4;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB4;
		}
	},
	AV_PIX_FMT_RGB4_BYTE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB4_BYTE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB4_BYTE;
		}
	},
	AV_PIX_FMT_NV12 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_NV12;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_NV12;
		}
	},
	AV_PIX_FMT_NV21 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_NV21;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_NV21;
		}
	},
	AV_PIX_FMT_ARGB {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_ARGB;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_ARGB;
		}
	},
	AV_PIX_FMT_RGBA {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGBA;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGBA;
		}
	},
	AV_PIX_FMT_ABGR {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_ABGR;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_ABGR;
		}
	},
	AV_PIX_FMT_BGRA {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGRA;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGRA;
		}
	},
	AV_PIX_FMT_GRAY16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GRAY16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GRAY16BE;
		}
	},
	AV_PIX_FMT_GRAY16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GRAY16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GRAY16LE;
		}
	},
	AV_PIX_FMT_YUV440P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV440P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV440P;
		}
	},
	AV_PIX_FMT_YUVJ440P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVJ440P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVJ440P;
		}
	},
	AV_PIX_FMT_YUVA420P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA420P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA420P;
		}
	},
	AV_PIX_FMT_VDPAU_H264 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VDPAU_H264;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VDPAU_H264;
		}
	},
	AV_PIX_FMT_VDPAU_MPEG1 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VDPAU_MPEG1;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VDPAU_MPEG1;
		}
	},
	AV_PIX_FMT_VDPAU_MPEG2 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VDPAU_MPEG2;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VDPAU_MPEG2;
		}
	},
	AV_PIX_FMT_VDPAU_WMV3 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VDPAU_WMV3;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VDPAU_WMV3;
		}
	},
	AV_PIX_FMT_VDPAU_VC1 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VDPAU_VC1;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VDPAU_VC1;
		}
	},
	AV_PIX_FMT_RGB48BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB48BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB48BE;
		}
	},
	AV_PIX_FMT_RGB48LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB48LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB48LE;
		}
	},
	AV_PIX_FMT_RGB565BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB565BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB565BE;
		}
	},
	AV_PIX_FMT_RGB565LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB565LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB565LE;
		}
	},
	AV_PIX_FMT_RGB555BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB555BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB555BE;
		}
	},
	AV_PIX_FMT_RGB555LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB555LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB555LE;
		}
	},
	AV_PIX_FMT_BGR565BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR565BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR565BE;
		}
	},
	AV_PIX_FMT_BGR565LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR565LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR565LE;
		}
	},
	AV_PIX_FMT_BGR555BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR555BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR555BE;
		}
	},
	AV_PIX_FMT_BGR555LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR555LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR555LE;
		}
	},
	AV_PIX_FMT_VAAPI_MOCO {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VAAPI_MOCO;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VAAPI_MOCO;
		}
	},
	AV_PIX_FMT_VAAPI_IDCT {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VAAPI_IDCT;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VAAPI_IDCT;
		}
	},
	AV_PIX_FMT_VAAPI_VLD {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VAAPI_VLD;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VAAPI_VLD;
		}
	},
	AV_PIX_FMT_VAAPI {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VAAPI;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VAAPI;
		}
	},
	AV_PIX_FMT_YUV420P16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV420P16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV420P16LE;
		}
	},
	AV_PIX_FMT_YUV420P16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV420P16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV420P16BE;
		}
	},
	AV_PIX_FMT_YUV422P16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV422P16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV422P16LE;
		}
	},
	AV_PIX_FMT_YUV422P16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV422P16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV422P16BE;
		}
	},
	AV_PIX_FMT_YUV444P16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV444P16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV444P16LE;
		}
	},
	AV_PIX_FMT_YUV444P16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV444P16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV444P16BE;
		}
	},
	AV_PIX_FMT_VDPAU_MPEG4 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VDPAU_MPEG4;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VDPAU_MPEG4;
		}
	},
	AV_PIX_FMT_DXVA2_VLD {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_DXVA2_VLD;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_DXVA2_VLD;
		}
	},
	AV_PIX_FMT_RGB444LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB444LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB444LE;
		}
	},
	AV_PIX_FMT_RGB444BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB444BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB444BE;
		}
	},
	AV_PIX_FMT_BGR444LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR444LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR444LE;
		}
	},
	AV_PIX_FMT_BGR444BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR444BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR444BE;
		}
	},
	AV_PIX_FMT_YA8 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YA8;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YA8;
		}
	},
	AV_PIX_FMT_Y400A {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_Y400A;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_Y400A;
		}
	},
	AV_PIX_FMT_GRAY8A {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GRAY8A;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GRAY8A;
		}
	},
	AV_PIX_FMT_BGR48BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR48BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR48BE;
		}
	},
	AV_PIX_FMT_BGR48LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR48LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR48LE;
		}
	},
	AV_PIX_FMT_YUV420P9BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV420P9BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV420P9BE;
		}
	},
	AV_PIX_FMT_YUV420P9LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV420P9LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV420P9LE;
		}
	},
	AV_PIX_FMT_YUV420P10BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV420P10BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV420P10BE;
		}
	},
	AV_PIX_FMT_YUV420P10LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV420P10LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV420P10LE;
		}
	},
	AV_PIX_FMT_YUV422P10BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV422P10BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV422P10BE;
		}
	},
	AV_PIX_FMT_YUV422P10LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV422P10LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV422P10LE;
		}
	},
	AV_PIX_FMT_YUV444P9BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV444P9BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV444P9BE;
		}
	},
	AV_PIX_FMT_YUV444P9LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV444P9LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV444P9LE;
		}
	},
	AV_PIX_FMT_YUV444P10BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV444P10BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV444P10BE;
		}
	},
	AV_PIX_FMT_YUV444P10LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV444P10LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV444P10LE;
		}
	},
	AV_PIX_FMT_YUV422P9BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV422P9BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV422P9BE;
		}
	},
	AV_PIX_FMT_YUV422P9LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV422P9LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV422P9LE;
		}
	},
	AV_PIX_FMT_VDA_VLD {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VDA_VLD;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VDA_VLD;
		}
	},
	AV_PIX_FMT_GBRP {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRP;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRP;
		}
	},
	AV_PIX_FMT_GBR24P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBR24P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBR24P;
		}
	},
	AV_PIX_FMT_GBRP9BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRP9BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRP9BE;
		}
	},
	AV_PIX_FMT_GBRP9LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRP9LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRP9LE;
		}
	},
	AV_PIX_FMT_GBRP10BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRP10BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRP10BE;
		}
	},
	AV_PIX_FMT_GBRP10LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRP10LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRP10LE;
		}
	},
	AV_PIX_FMT_GBRP16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRP16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRP16BE;
		}
	},
	AV_PIX_FMT_GBRP16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRP16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRP16LE;
		}
	},
	AV_PIX_FMT_YUVA422P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA422P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA422P;
		}
	},
	AV_PIX_FMT_YUVA444P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA444P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA444P;
		}
	},
	AV_PIX_FMT_YUVA420P9BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA420P9BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA420P9BE;
		}
	},
	AV_PIX_FMT_YUVA420P9LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA420P9LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA420P9LE;
		}
	},
	AV_PIX_FMT_YUVA422P9BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA422P9BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA422P9BE;
		}
	},
	AV_PIX_FMT_YUVA422P9LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA422P9LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA422P9LE;
		}
	},
	AV_PIX_FMT_YUVA444P9BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA444P9BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA444P9BE;
		}
	},
	AV_PIX_FMT_YUVA444P9LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA444P9LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA444P9LE;
		}
	},
	AV_PIX_FMT_YUVA420P10BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA420P10BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA420P10BE;
		}
	},
	AV_PIX_FMT_YUVA420P10LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA420P10LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA420P10LE;
		}
	},
	AV_PIX_FMT_YUVA422P10BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA422P10BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA422P10BE;
		}
	},
	AV_PIX_FMT_YUVA422P10LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA422P10LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA422P10LE;
		}
	},
	AV_PIX_FMT_YUVA444P10BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA444P10BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA444P10BE;
		}
	},
	AV_PIX_FMT_YUVA444P10LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA444P10LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA444P10LE;
		}
	},
	AV_PIX_FMT_YUVA420P16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA420P16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA420P16BE;
		}
	},
	AV_PIX_FMT_YUVA420P16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA420P16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA420P16LE;
		}
	},
	AV_PIX_FMT_YUVA422P16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA422P16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA422P16BE;
		}
	},
	AV_PIX_FMT_YUVA422P16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA422P16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA422P16LE;
		}
	},
	AV_PIX_FMT_YUVA444P16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA444P16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA444P16BE;
		}
	},
	AV_PIX_FMT_YUVA444P16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVA444P16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVA444P16LE;
		}
	},
	AV_PIX_FMT_VDPAU {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VDPAU;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VDPAU;
		}
	},
	AV_PIX_FMT_XYZ12LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_XYZ12LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_XYZ12LE;
		}
	},
	AV_PIX_FMT_XYZ12BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_XYZ12BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_XYZ12BE;
		}
	},
	AV_PIX_FMT_NV16 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_NV16;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_NV16;
		}
	},
	AV_PIX_FMT_NV20LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_NV20LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_NV20LE;
		}
	},
	AV_PIX_FMT_NV20BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_NV20BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_NV20BE;
		}
	},
	AV_PIX_FMT_RGBA64BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGBA64BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGBA64BE;
		}
	},
	AV_PIX_FMT_RGBA64LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGBA64LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGBA64LE;
		}
	},
	AV_PIX_FMT_BGRA64BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGRA64BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGRA64BE;
		}
	},
	AV_PIX_FMT_BGRA64LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGRA64LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGRA64LE;
		}
	},
	AV_PIX_FMT_YVYU422 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YVYU422;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YVYU422;
		}
	},
	AV_PIX_FMT_VDA {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VDA;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VDA;
		}
	},
	AV_PIX_FMT_YA16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YA16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YA16BE;
		}
	},
	AV_PIX_FMT_YA16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YA16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YA16LE;
		}
	},
	AV_PIX_FMT_GBRAP {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRAP;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRAP;
		}
	},
	AV_PIX_FMT_GBRAP16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRAP16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRAP16BE;
		}
	},
	AV_PIX_FMT_GBRAP16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRAP16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRAP16LE;
		}
	},
	AV_PIX_FMT_QSV {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_QSV;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_QSV;
		}
	},
	AV_PIX_FMT_MMAL {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_MMAL;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_MMAL;
		}
	},
	AV_PIX_FMT_D3D11VA_VLD {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_D3D11VA_VLD;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_D3D11VA_VLD;
		}
	},
	AV_PIX_FMT_CUDA {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_CUDA;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_CUDA;
		}
	},
	AV_PIX_FMT_0RGB {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_0RGB;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_0RGB;
		}
	},
	AV_PIX_FMT_RGB0 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_RGB0;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_RGB0;
		}
	},
	AV_PIX_FMT_0BGR {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_0BGR;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_0BGR;
		}
	},
	AV_PIX_FMT_BGR0 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BGR0;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BGR0;
		}
	},
	AV_PIX_FMT_YUV420P12BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV420P12BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV420P12BE;
		}
	},
	AV_PIX_FMT_YUV420P12LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV420P12LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV420P12LE;
		}
	},
	AV_PIX_FMT_YUV420P14BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV420P14BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV420P14BE;
		}
	},
	AV_PIX_FMT_YUV420P14LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV420P14LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV420P14LE;
		}
	},
	AV_PIX_FMT_YUV422P12BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV422P12BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV422P12BE;
		}
	},
	AV_PIX_FMT_YUV422P12LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV422P12LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV422P12LE;
		}
	},
	AV_PIX_FMT_YUV422P14BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV422P14BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV422P14BE;
		}
	},
	AV_PIX_FMT_YUV422P14LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV422P14LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV422P14LE;
		}
	},
	AV_PIX_FMT_YUV444P12BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV444P12BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV444P12BE;
		}
	},
	AV_PIX_FMT_YUV444P12LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV444P12LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV444P12LE;
		}
	},
	AV_PIX_FMT_YUV444P14BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV444P14BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV444P14BE;
		}
	},
	AV_PIX_FMT_YUV444P14LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV444P14LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV444P14LE;
		}
	},
	AV_PIX_FMT_GBRP12BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRP12BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRP12BE;
		}
	},
	AV_PIX_FMT_GBRP12LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRP12LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRP12LE;
		}
	},
	AV_PIX_FMT_GBRP14BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRP14BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRP14BE;
		}
	},
	AV_PIX_FMT_GBRP14LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRP14LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRP14LE;
		}
	},
	AV_PIX_FMT_YUVJ411P {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUVJ411P;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUVJ411P;
		}
	},
	AV_PIX_FMT_BAYER_BGGR8 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BAYER_BGGR8;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BAYER_BGGR8;
		}
	},
	AV_PIX_FMT_BAYER_RGGB8 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BAYER_RGGB8;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BAYER_RGGB8;
		}
	},
	AV_PIX_FMT_BAYER_GBRG8 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BAYER_GBRG8;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BAYER_GBRG8;
		}
	},
	AV_PIX_FMT_BAYER_GRBG8 {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BAYER_GRBG8;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BAYER_GRBG8;
		}
	},
	AV_PIX_FMT_BAYER_BGGR16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BAYER_BGGR16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BAYER_BGGR16LE;
		}
	},
	AV_PIX_FMT_BAYER_BGGR16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BAYER_BGGR16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BAYER_BGGR16BE;
		}
	},
	AV_PIX_FMT_BAYER_RGGB16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BAYER_RGGB16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BAYER_RGGB16LE;
		}
	},
	AV_PIX_FMT_BAYER_RGGB16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BAYER_RGGB16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BAYER_RGGB16BE;
		}
	},
	AV_PIX_FMT_BAYER_GBRG16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BAYER_GBRG16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BAYER_GBRG16LE;
		}
	},
	AV_PIX_FMT_BAYER_GBRG16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BAYER_GBRG16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BAYER_GBRG16BE;
		}
	},
	AV_PIX_FMT_BAYER_GRBG16LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BAYER_GRBG16LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BAYER_GRBG16LE;
		}
	},
	AV_PIX_FMT_BAYER_GRBG16BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_BAYER_GRBG16BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_BAYER_GRBG16BE;
		}
	},
	AV_PIX_FMT_XVMC {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_XVMC;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_XVMC;
		}
	},
	AV_PIX_FMT_YUV440P10LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV440P10LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV440P10LE;
		}
	},
	AV_PIX_FMT_YUV440P10BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV440P10BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV440P10BE;
		}
	},
	AV_PIX_FMT_YUV440P12LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV440P12LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV440P12LE;
		}
	},
	AV_PIX_FMT_YUV440P12BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_YUV440P12BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_YUV440P12BE;
		}
	},
	AV_PIX_FMT_AYUV64LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_AYUV64LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_AYUV64LE;
		}
	},
	AV_PIX_FMT_AYUV64BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_AYUV64BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_AYUV64BE;
		}
	},
	AV_PIX_FMT_VIDEOTOOLBOX {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_VIDEOTOOLBOX;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_VIDEOTOOLBOX;
		}
	},
	AV_PIX_FMT_P010LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_P010LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_P010LE;
		}
	},
	AV_PIX_FMT_P010BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_P010BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_P010BE;
		}
	},
	AV_PIX_FMT_GBRAP12BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRAP12BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRAP12BE;
		}
	},
	AV_PIX_FMT_GBRAP12LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRAP12LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRAP12LE;
		}
	},
	AV_PIX_FMT_GBRAP10BE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRAP10BE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRAP10BE;
		}
	},
	AV_PIX_FMT_GBRAP10LE {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_GBRAP10LE;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_GBRAP10LE;
		}
	},
	AV_PIX_FMT_MEDIACODEC {
		@Override
		public int toInteger() {
			return avutil.AV_PIX_FMT_MEDIACODEC;
		}
		@Override
		public Term toTerm() {
			return term_AV_PIX_FMT_MEDIACODEC;
		}
	};
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegPixelFormatName argumentToFFmpegPixelFormatName(Term value, ChoisePoint iX) throws TermIsNotFFmpegPixelFormatName {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_NONE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_NONE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV420P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUYV422) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUYV422;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB24) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB24;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR24) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR24;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV422P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV444P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV410P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV410P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV411P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV411P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GRAY8) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GRAY8;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_MONOWHITE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_MONOWHITE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_MONOBLACK) {
				return FFmpegPixelFormatName.AV_PIX_FMT_MONOBLACK;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_PAL8) {
				return FFmpegPixelFormatName.AV_PIX_FMT_PAL8;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVJ420P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVJ420P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVJ422P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVJ422P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVJ444P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVJ444P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_XVMC_MPEG2_MC) {
				return FFmpegPixelFormatName.AV_PIX_FMT_XVMC_MPEG2_MC;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_XVMC_MPEG2_IDCT) {
				return FFmpegPixelFormatName.AV_PIX_FMT_XVMC_MPEG2_IDCT;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_UYVY422) {
				return FFmpegPixelFormatName.AV_PIX_FMT_UYVY422;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_UYYVYY411) {
				return FFmpegPixelFormatName.AV_PIX_FMT_UYYVYY411;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR8) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR8;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR4) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR4;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR4_BYTE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR4_BYTE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB8) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB8;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB4) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB4;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB4_BYTE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB4_BYTE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_NV12) {
				return FFmpegPixelFormatName.AV_PIX_FMT_NV12;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_NV21) {
				return FFmpegPixelFormatName.AV_PIX_FMT_NV21;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_ARGB) {
				return FFmpegPixelFormatName.AV_PIX_FMT_ARGB;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGBA) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGBA;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_ABGR) {
				return FFmpegPixelFormatName.AV_PIX_FMT_ABGR;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGRA) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGRA;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GRAY16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GRAY16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GRAY16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GRAY16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV440P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV440P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVJ440P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVJ440P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU_H264) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VDPAU_H264;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU_MPEG1) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VDPAU_MPEG1;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU_MPEG2) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VDPAU_MPEG2;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU_WMV3) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VDPAU_WMV3;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU_VC1) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VDPAU_VC1;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB48BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB48BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB48LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB48LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB565BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB565BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB565LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB565LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB555BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB555BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB555LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB555LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR565BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR565BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR565LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR565LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR555BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR555BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR555LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR555LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VAAPI_MOCO) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VAAPI_MOCO;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VAAPI_IDCT) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VAAPI_IDCT;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VAAPI_VLD) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VAAPI_VLD;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VAAPI) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VAAPI;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV420P16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV420P16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV422P16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV422P16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV444P16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV444P16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU_MPEG4) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VDPAU_MPEG4;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_DXVA2_VLD) {
				return FFmpegPixelFormatName.AV_PIX_FMT_DXVA2_VLD;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB444LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB444LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB444BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB444BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR444LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR444LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR444BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR444BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YA8) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YA8;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_Y400A) {
				return FFmpegPixelFormatName.AV_PIX_FMT_Y400A;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GRAY8A) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GRAY8A;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR48BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR48BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR48LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR48LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P9BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV420P9BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P9LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV420P9LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P10BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV420P10BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P10LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV420P10LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P10BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV422P10BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P10LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV422P10LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P9BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV444P9BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P9LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV444P9LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P10BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV444P10BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P10LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV444P10LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P9BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV422P9BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P9LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV422P9LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VDA_VLD) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VDA_VLD;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRP;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBR24P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBR24P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP9BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRP9BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP9LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRP9LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP10BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRP10BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP10LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRP10LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRP16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRP16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P9BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P9BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P9LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P9LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P9BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P9BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P9LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P9LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P9BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P9BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P9LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P9LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P10BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P10BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P10LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P10LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P10BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P10BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P10LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P10LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P10BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P10BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P10LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P10LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VDPAU;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_XYZ12LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_XYZ12LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_XYZ12BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_XYZ12BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_NV16) {
				return FFmpegPixelFormatName.AV_PIX_FMT_NV16;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_NV20LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_NV20LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_NV20BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_NV20BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGBA64BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGBA64BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGBA64LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGBA64LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGRA64BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGRA64BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGRA64LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGRA64LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YVYU422) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YVYU422;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VDA) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VDA;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YA16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YA16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YA16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YA16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRAP;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRAP16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRAP16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_QSV) {
				return FFmpegPixelFormatName.AV_PIX_FMT_QSV;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_MMAL) {
				return FFmpegPixelFormatName.AV_PIX_FMT_MMAL;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_D3D11VA_VLD) {
				return FFmpegPixelFormatName.AV_PIX_FMT_D3D11VA_VLD;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_CUDA) {
				return FFmpegPixelFormatName.AV_PIX_FMT_CUDA;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_0RGB) {
				return FFmpegPixelFormatName.AV_PIX_FMT_0RGB;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB0) {
				return FFmpegPixelFormatName.AV_PIX_FMT_RGB0;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_0BGR) {
				return FFmpegPixelFormatName.AV_PIX_FMT_0BGR;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR0) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BGR0;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P12BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV420P12BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P12LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV420P12LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P14BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV420P14BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P14LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV420P14LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P12BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV422P12BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P12LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV422P12LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P14BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV422P14BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P14LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV422P14LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P12BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV444P12BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P12LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV444P12LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P14BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV444P14BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P14LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV444P14LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP12BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRP12BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP12LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRP12LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP14BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRP14BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP14LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRP14LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVJ411P) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUVJ411P;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_BGGR8) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BAYER_BGGR8;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_RGGB8) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BAYER_RGGB8;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_GBRG8) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BAYER_GBRG8;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_GRBG8) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BAYER_GRBG8;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_BGGR16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BAYER_BGGR16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_BGGR16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BAYER_BGGR16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_RGGB16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BAYER_RGGB16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_RGGB16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BAYER_RGGB16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_GBRG16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BAYER_GBRG16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_GBRG16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BAYER_GBRG16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_GRBG16LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BAYER_GRBG16LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_GRBG16BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_BAYER_GRBG16BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_XVMC) {
				return FFmpegPixelFormatName.AV_PIX_FMT_XVMC;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV440P10LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV440P10LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV440P10BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV440P10BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV440P12LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV440P12LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV440P12BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_YUV440P12BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_AYUV64LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_AYUV64LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_AYUV64BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_AYUV64BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_VIDEOTOOLBOX) {
				return FFmpegPixelFormatName.AV_PIX_FMT_VIDEOTOOLBOX;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_P010LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_P010LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_P010BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_P010BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP12BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRAP12BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP12LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRAP12LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP10BE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRAP10BE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP10LE) {
				return FFmpegPixelFormatName.AV_PIX_FMT_GBRAP10LE;
			} else if (code==SymbolCodes.symbolCode_E_AV_PIX_FMT_MEDIACODEC) {
				return FFmpegPixelFormatName.AV_PIX_FMT_MEDIACODEC;
			} else {
				throw TermIsNotFFmpegPixelFormatName.instance;
			}
		} catch (TermIsNotASymbol e) {
			throw TermIsNotFFmpegPixelFormatName.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term term_AV_PIX_FMT_NONE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_NONE);
	protected static Term term_AV_PIX_FMT_YUV420P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P);
	protected static Term term_AV_PIX_FMT_YUYV422= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUYV422);
	protected static Term term_AV_PIX_FMT_RGB24= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB24);
	protected static Term term_AV_PIX_FMT_BGR24= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR24);
	protected static Term term_AV_PIX_FMT_YUV422P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P);
	protected static Term term_AV_PIX_FMT_YUV444P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P);
	protected static Term term_AV_PIX_FMT_YUV410P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV410P);
	protected static Term term_AV_PIX_FMT_YUV411P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV411P);
	protected static Term term_AV_PIX_FMT_GRAY8= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GRAY8);
	protected static Term term_AV_PIX_FMT_MONOWHITE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_MONOWHITE);
	protected static Term term_AV_PIX_FMT_MONOBLACK= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_MONOBLACK);
	protected static Term term_AV_PIX_FMT_PAL8= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_PAL8);
	protected static Term term_AV_PIX_FMT_YUVJ420P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVJ420P);
	protected static Term term_AV_PIX_FMT_YUVJ422P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVJ422P);
	protected static Term term_AV_PIX_FMT_YUVJ444P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVJ444P);
	protected static Term term_AV_PIX_FMT_XVMC_MPEG2_MC= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_XVMC_MPEG2_MC);
	protected static Term term_AV_PIX_FMT_XVMC_MPEG2_IDCT= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_XVMC_MPEG2_IDCT);
	protected static Term term_AV_PIX_FMT_UYVY422= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_UYVY422);
	protected static Term term_AV_PIX_FMT_UYYVYY411= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_UYYVYY411);
	protected static Term term_AV_PIX_FMT_BGR8= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR8);
	protected static Term term_AV_PIX_FMT_BGR4= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR4);
	protected static Term term_AV_PIX_FMT_BGR4_BYTE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR4_BYTE);
	protected static Term term_AV_PIX_FMT_RGB8= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB8);
	protected static Term term_AV_PIX_FMT_RGB4= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB4);
	protected static Term term_AV_PIX_FMT_RGB4_BYTE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB4_BYTE);
	protected static Term term_AV_PIX_FMT_NV12= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_NV12);
	protected static Term term_AV_PIX_FMT_NV21= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_NV21);
	protected static Term term_AV_PIX_FMT_ARGB= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_ARGB);
	protected static Term term_AV_PIX_FMT_RGBA= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGBA);
	protected static Term term_AV_PIX_FMT_ABGR= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_ABGR);
	protected static Term term_AV_PIX_FMT_BGRA= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGRA);
	protected static Term term_AV_PIX_FMT_GRAY16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GRAY16BE);
	protected static Term term_AV_PIX_FMT_GRAY16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GRAY16LE);
	protected static Term term_AV_PIX_FMT_YUV440P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV440P);
	protected static Term term_AV_PIX_FMT_YUVJ440P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVJ440P);
	protected static Term term_AV_PIX_FMT_YUVA420P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P);
	protected static Term term_AV_PIX_FMT_VDPAU_H264= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU_H264);
	protected static Term term_AV_PIX_FMT_VDPAU_MPEG1= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU_MPEG1);
	protected static Term term_AV_PIX_FMT_VDPAU_MPEG2= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU_MPEG2);
	protected static Term term_AV_PIX_FMT_VDPAU_WMV3= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU_WMV3);
	protected static Term term_AV_PIX_FMT_VDPAU_VC1= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU_VC1);
	protected static Term term_AV_PIX_FMT_RGB48BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB48BE);
	protected static Term term_AV_PIX_FMT_RGB48LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB48LE);
	protected static Term term_AV_PIX_FMT_RGB565BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB565BE);
	protected static Term term_AV_PIX_FMT_RGB565LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB565LE);
	protected static Term term_AV_PIX_FMT_RGB555BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB555BE);
	protected static Term term_AV_PIX_FMT_RGB555LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB555LE);
	protected static Term term_AV_PIX_FMT_BGR565BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR565BE);
	protected static Term term_AV_PIX_FMT_BGR565LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR565LE);
	protected static Term term_AV_PIX_FMT_BGR555BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR555BE);
	protected static Term term_AV_PIX_FMT_BGR555LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR555LE);
	protected static Term term_AV_PIX_FMT_VAAPI_MOCO= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VAAPI_MOCO);
	protected static Term term_AV_PIX_FMT_VAAPI_IDCT= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VAAPI_IDCT);
	protected static Term term_AV_PIX_FMT_VAAPI_VLD= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VAAPI_VLD);
	protected static Term term_AV_PIX_FMT_VAAPI= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VAAPI);
	protected static Term term_AV_PIX_FMT_YUV420P16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P16LE);
	protected static Term term_AV_PIX_FMT_YUV420P16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P16BE);
	protected static Term term_AV_PIX_FMT_YUV422P16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P16LE);
	protected static Term term_AV_PIX_FMT_YUV422P16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P16BE);
	protected static Term term_AV_PIX_FMT_YUV444P16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P16LE);
	protected static Term term_AV_PIX_FMT_YUV444P16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P16BE);
	protected static Term term_AV_PIX_FMT_VDPAU_MPEG4= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU_MPEG4);
	protected static Term term_AV_PIX_FMT_DXVA2_VLD= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_DXVA2_VLD);
	protected static Term term_AV_PIX_FMT_RGB444LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB444LE);
	protected static Term term_AV_PIX_FMT_RGB444BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB444BE);
	protected static Term term_AV_PIX_FMT_BGR444LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR444LE);
	protected static Term term_AV_PIX_FMT_BGR444BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR444BE);
	protected static Term term_AV_PIX_FMT_YA8= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YA8);
	protected static Term term_AV_PIX_FMT_Y400A= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_Y400A);
	protected static Term term_AV_PIX_FMT_GRAY8A= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GRAY8A);
	protected static Term term_AV_PIX_FMT_BGR48BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR48BE);
	protected static Term term_AV_PIX_FMT_BGR48LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR48LE);
	protected static Term term_AV_PIX_FMT_YUV420P9BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P9BE);
	protected static Term term_AV_PIX_FMT_YUV420P9LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P9LE);
	protected static Term term_AV_PIX_FMT_YUV420P10BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P10BE);
	protected static Term term_AV_PIX_FMT_YUV420P10LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P10LE);
	protected static Term term_AV_PIX_FMT_YUV422P10BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P10BE);
	protected static Term term_AV_PIX_FMT_YUV422P10LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P10LE);
	protected static Term term_AV_PIX_FMT_YUV444P9BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P9BE);
	protected static Term term_AV_PIX_FMT_YUV444P9LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P9LE);
	protected static Term term_AV_PIX_FMT_YUV444P10BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P10BE);
	protected static Term term_AV_PIX_FMT_YUV444P10LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P10LE);
	protected static Term term_AV_PIX_FMT_YUV422P9BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P9BE);
	protected static Term term_AV_PIX_FMT_YUV422P9LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P9LE);
	protected static Term term_AV_PIX_FMT_VDA_VLD= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VDA_VLD);
	protected static Term term_AV_PIX_FMT_GBRP= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP);
	protected static Term term_AV_PIX_FMT_GBR24P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBR24P);
	protected static Term term_AV_PIX_FMT_GBRP9BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP9BE);
	protected static Term term_AV_PIX_FMT_GBRP9LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP9LE);
	protected static Term term_AV_PIX_FMT_GBRP10BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP10BE);
	protected static Term term_AV_PIX_FMT_GBRP10LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP10LE);
	protected static Term term_AV_PIX_FMT_GBRP16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP16BE);
	protected static Term term_AV_PIX_FMT_GBRP16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP16LE);
	protected static Term term_AV_PIX_FMT_YUVA422P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P);
	protected static Term term_AV_PIX_FMT_YUVA444P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P);
	protected static Term term_AV_PIX_FMT_YUVA420P9BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P9BE);
	protected static Term term_AV_PIX_FMT_YUVA420P9LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P9LE);
	protected static Term term_AV_PIX_FMT_YUVA422P9BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P9BE);
	protected static Term term_AV_PIX_FMT_YUVA422P9LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P9LE);
	protected static Term term_AV_PIX_FMT_YUVA444P9BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P9BE);
	protected static Term term_AV_PIX_FMT_YUVA444P9LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P9LE);
	protected static Term term_AV_PIX_FMT_YUVA420P10BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P10BE);
	protected static Term term_AV_PIX_FMT_YUVA420P10LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P10LE);
	protected static Term term_AV_PIX_FMT_YUVA422P10BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P10BE);
	protected static Term term_AV_PIX_FMT_YUVA422P10LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P10LE);
	protected static Term term_AV_PIX_FMT_YUVA444P10BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P10BE);
	protected static Term term_AV_PIX_FMT_YUVA444P10LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P10LE);
	protected static Term term_AV_PIX_FMT_YUVA420P16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P16BE);
	protected static Term term_AV_PIX_FMT_YUVA420P16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA420P16LE);
	protected static Term term_AV_PIX_FMT_YUVA422P16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P16BE);
	protected static Term term_AV_PIX_FMT_YUVA422P16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA422P16LE);
	protected static Term term_AV_PIX_FMT_YUVA444P16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P16BE);
	protected static Term term_AV_PIX_FMT_YUVA444P16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVA444P16LE);
	protected static Term term_AV_PIX_FMT_VDPAU= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VDPAU);
	protected static Term term_AV_PIX_FMT_XYZ12LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_XYZ12LE);
	protected static Term term_AV_PIX_FMT_XYZ12BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_XYZ12BE);
	protected static Term term_AV_PIX_FMT_NV16= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_NV16);
	protected static Term term_AV_PIX_FMT_NV20LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_NV20LE);
	protected static Term term_AV_PIX_FMT_NV20BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_NV20BE);
	protected static Term term_AV_PIX_FMT_RGBA64BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGBA64BE);
	protected static Term term_AV_PIX_FMT_RGBA64LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGBA64LE);
	protected static Term term_AV_PIX_FMT_BGRA64BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGRA64BE);
	protected static Term term_AV_PIX_FMT_BGRA64LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGRA64LE);
	protected static Term term_AV_PIX_FMT_YVYU422= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YVYU422);
	protected static Term term_AV_PIX_FMT_VDA= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VDA);
	protected static Term term_AV_PIX_FMT_YA16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YA16BE);
	protected static Term term_AV_PIX_FMT_YA16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YA16LE);
	protected static Term term_AV_PIX_FMT_GBRAP= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP);
	protected static Term term_AV_PIX_FMT_GBRAP16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP16BE);
	protected static Term term_AV_PIX_FMT_GBRAP16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP16LE);
	protected static Term term_AV_PIX_FMT_QSV= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_QSV);
	protected static Term term_AV_PIX_FMT_MMAL= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_MMAL);
	protected static Term term_AV_PIX_FMT_D3D11VA_VLD= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_D3D11VA_VLD);
	protected static Term term_AV_PIX_FMT_CUDA= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_CUDA);
	protected static Term term_AV_PIX_FMT_0RGB= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_0RGB);
	protected static Term term_AV_PIX_FMT_RGB0= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_RGB0);
	protected static Term term_AV_PIX_FMT_0BGR= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_0BGR);
	protected static Term term_AV_PIX_FMT_BGR0= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BGR0);
	protected static Term term_AV_PIX_FMT_YUV420P12BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P12BE);
	protected static Term term_AV_PIX_FMT_YUV420P12LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P12LE);
	protected static Term term_AV_PIX_FMT_YUV420P14BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P14BE);
	protected static Term term_AV_PIX_FMT_YUV420P14LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV420P14LE);
	protected static Term term_AV_PIX_FMT_YUV422P12BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P12BE);
	protected static Term term_AV_PIX_FMT_YUV422P12LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P12LE);
	protected static Term term_AV_PIX_FMT_YUV422P14BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P14BE);
	protected static Term term_AV_PIX_FMT_YUV422P14LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV422P14LE);
	protected static Term term_AV_PIX_FMT_YUV444P12BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P12BE);
	protected static Term term_AV_PIX_FMT_YUV444P12LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P12LE);
	protected static Term term_AV_PIX_FMT_YUV444P14BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P14BE);
	protected static Term term_AV_PIX_FMT_YUV444P14LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV444P14LE);
	protected static Term term_AV_PIX_FMT_GBRP12BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP12BE);
	protected static Term term_AV_PIX_FMT_GBRP12LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP12LE);
	protected static Term term_AV_PIX_FMT_GBRP14BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP14BE);
	protected static Term term_AV_PIX_FMT_GBRP14LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRP14LE);
	protected static Term term_AV_PIX_FMT_YUVJ411P= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUVJ411P);
	protected static Term term_AV_PIX_FMT_BAYER_BGGR8= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_BGGR8);
	protected static Term term_AV_PIX_FMT_BAYER_RGGB8= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_RGGB8);
	protected static Term term_AV_PIX_FMT_BAYER_GBRG8= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_GBRG8);
	protected static Term term_AV_PIX_FMT_BAYER_GRBG8= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_GRBG8);
	protected static Term term_AV_PIX_FMT_BAYER_BGGR16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_BGGR16LE);
	protected static Term term_AV_PIX_FMT_BAYER_BGGR16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_BGGR16BE);
	protected static Term term_AV_PIX_FMT_BAYER_RGGB16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_RGGB16LE);
	protected static Term term_AV_PIX_FMT_BAYER_RGGB16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_RGGB16BE);
	protected static Term term_AV_PIX_FMT_BAYER_GBRG16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_GBRG16LE);
	protected static Term term_AV_PIX_FMT_BAYER_GBRG16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_GBRG16BE);
	protected static Term term_AV_PIX_FMT_BAYER_GRBG16LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_GRBG16LE);
	protected static Term term_AV_PIX_FMT_BAYER_GRBG16BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_BAYER_GRBG16BE);
	protected static Term term_AV_PIX_FMT_XVMC= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_XVMC);
	protected static Term term_AV_PIX_FMT_YUV440P10LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV440P10LE);
	protected static Term term_AV_PIX_FMT_YUV440P10BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV440P10BE);
	protected static Term term_AV_PIX_FMT_YUV440P12LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV440P12LE);
	protected static Term term_AV_PIX_FMT_YUV440P12BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_YUV440P12BE);
	protected static Term term_AV_PIX_FMT_AYUV64LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_AYUV64LE);
	protected static Term term_AV_PIX_FMT_AYUV64BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_AYUV64BE);
	protected static Term term_AV_PIX_FMT_VIDEOTOOLBOX= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_VIDEOTOOLBOX);
	protected static Term term_AV_PIX_FMT_P010LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_P010LE);
	protected static Term term_AV_PIX_FMT_P010BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_P010BE);
	protected static Term term_AV_PIX_FMT_GBRAP12BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP12BE);
	protected static Term term_AV_PIX_FMT_GBRAP12LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP12LE);
	protected static Term term_AV_PIX_FMT_GBRAP10BE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP10BE);
	protected static Term term_AV_PIX_FMT_GBRAP10LE= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_GBRAP10LE);
	protected static Term term_AV_PIX_FMT_MEDIACODEC= new PrologSymbol(SymbolCodes.symbolCode_E_AV_PIX_FMT_MEDIACODEC);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public int toInteger();
	abstract public Term toTerm();
}
