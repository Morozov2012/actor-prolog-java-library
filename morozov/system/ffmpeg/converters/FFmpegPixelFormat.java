// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters;

import org.bytedeco.javacpp.avutil;

import morozov.system.ffmpeg.converters.errors.*;
import morozov.system.ffmpeg.converters.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

public class FFmpegPixelFormat {
	//
	protected FFmpegPixelFormatName name;
	protected int value;
	protected boolean isNamed;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegPixelFormat(FFmpegPixelFormatName n) {
		name= n;
		isNamed= true;
	}
	public FFmpegPixelFormat(int v) {
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
	public FFmpegPixelFormatName getName() {
		return name;
	}
	//
	public int getValue() {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegPixelFormat argumentToFFmpegPixelFormat(Term value, ChoisePoint iX) {
		try {
			FFmpegPixelFormatName name= FFmpegPixelFormatName.argumentToFFmpegPixelFormatName(value,iX);
			return new FFmpegPixelFormat(name);
		} catch (TermIsNotFFmpegPixelFormatName e1) {
			try {
				return new FFmpegPixelFormat(value.getSmallIntegerValue(iX));
			} catch (TermIsNotAnInteger e2) {
				throw new WrongArgumentIsNotFFmpegPixelFormat(value);
			}
		}
	}
	//
	public static FFmpegPixelFormat integerToFFmpegPixelFormat(int value) {
		if (value == avutil.AV_PIX_FMT_NONE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_NONE);
		};
		if (value == avutil.AV_PIX_FMT_YUV420P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV420P);
		};
		if (value == avutil.AV_PIX_FMT_YUYV422) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUYV422);
		};
		if (value == avutil.AV_PIX_FMT_RGB24) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB24);
		};
		if (value == avutil.AV_PIX_FMT_BGR24) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR24);
		};
		if (value == avutil.AV_PIX_FMT_YUV422P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV422P);
		};
		if (value == avutil.AV_PIX_FMT_YUV444P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV444P);
		};
		if (value == avutil.AV_PIX_FMT_YUV410P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV410P);
		};
		if (value == avutil.AV_PIX_FMT_YUV411P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV411P);
		};
		if (value == avutil.AV_PIX_FMT_GRAY8) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GRAY8);
		};
		if (value == avutil.AV_PIX_FMT_MONOWHITE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_MONOWHITE);
		};
		if (value == avutil.AV_PIX_FMT_MONOBLACK) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_MONOBLACK);
		};
		if (value == avutil.AV_PIX_FMT_PAL8) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_PAL8);
		};
		if (value == avutil.AV_PIX_FMT_YUVJ420P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVJ420P);
		};
		if (value == avutil.AV_PIX_FMT_YUVJ422P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVJ422P);
		};
		if (value == avutil.AV_PIX_FMT_YUVJ444P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVJ444P);
		};
		if (value == avutil.AV_PIX_FMT_XVMC_MPEG2_MC) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_XVMC_MPEG2_MC);
		};
		if (value == avutil.AV_PIX_FMT_XVMC_MPEG2_IDCT) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_XVMC_MPEG2_IDCT);
		};
		if (value == avutil.AV_PIX_FMT_UYVY422) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_UYVY422);
		};
		if (value == avutil.AV_PIX_FMT_UYYVYY411) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_UYYVYY411);
		};
		if (value == avutil.AV_PIX_FMT_BGR8) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR8);
		};
		if (value == avutil.AV_PIX_FMT_BGR4) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR4);
		};
		if (value == avutil.AV_PIX_FMT_BGR4_BYTE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR4_BYTE);
		};
		if (value == avutil.AV_PIX_FMT_RGB8) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB8);
		};
		if (value == avutil.AV_PIX_FMT_RGB4) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB4);
		};
		if (value == avutil.AV_PIX_FMT_RGB4_BYTE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB4_BYTE);
		};
		if (value == avutil.AV_PIX_FMT_NV12) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_NV12);
		};
		if (value == avutil.AV_PIX_FMT_NV21) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_NV21);
		};
		if (value == avutil.AV_PIX_FMT_ARGB) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_ARGB);
		};
		if (value == avutil.AV_PIX_FMT_RGBA) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGBA);
		};
		if (value == avutil.AV_PIX_FMT_ABGR) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_ABGR);
		};
		if (value == avutil.AV_PIX_FMT_BGRA) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGRA);
		};
		if (value == avutil.AV_PIX_FMT_GRAY16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GRAY16BE);
		};
		if (value == avutil.AV_PIX_FMT_GRAY16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GRAY16LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV440P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV440P);
		};
		if (value == avutil.AV_PIX_FMT_YUVJ440P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVJ440P);
		};
		if (value == avutil.AV_PIX_FMT_YUVA420P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P);
		};
		if (value == avutil.AV_PIX_FMT_VDPAU_H264) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VDPAU_H264);
		};
		if (value == avutil.AV_PIX_FMT_VDPAU_MPEG1) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VDPAU_MPEG1);
		};
		if (value == avutil.AV_PIX_FMT_VDPAU_MPEG2) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VDPAU_MPEG2);
		};
		if (value == avutil.AV_PIX_FMT_VDPAU_WMV3) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VDPAU_WMV3);
		};
		if (value == avutil.AV_PIX_FMT_VDPAU_VC1) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VDPAU_VC1);
		};
		if (value == avutil.AV_PIX_FMT_RGB48BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB48BE);
		};
		if (value == avutil.AV_PIX_FMT_RGB48LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB48LE);
		};
		if (value == avutil.AV_PIX_FMT_RGB565BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB565BE);
		};
		if (value == avutil.AV_PIX_FMT_RGB565LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB565LE);
		};
		if (value == avutil.AV_PIX_FMT_RGB555BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB555BE);
		};
		if (value == avutil.AV_PIX_FMT_RGB555LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB555LE);
		};
		if (value == avutil.AV_PIX_FMT_BGR565BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR565BE);
		};
		if (value == avutil.AV_PIX_FMT_BGR565LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR565LE);
		};
		if (value == avutil.AV_PIX_FMT_BGR555BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR555BE);
		};
		if (value == avutil.AV_PIX_FMT_BGR555LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR555LE);
		};
		if (value == avutil.AV_PIX_FMT_VAAPI_MOCO) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VAAPI_MOCO);
		};
		if (value == avutil.AV_PIX_FMT_VAAPI_IDCT) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VAAPI_IDCT);
		};
		if (value == avutil.AV_PIX_FMT_VAAPI_VLD) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VAAPI_VLD);
		};
		if (value == avutil.AV_PIX_FMT_VAAPI) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VAAPI);
		};
		if (value == avutil.AV_PIX_FMT_YUV420P16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV420P16LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV420P16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV420P16BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV422P16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV422P16LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV422P16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV422P16BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV444P16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV444P16LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV444P16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV444P16BE);
		};
		if (value == avutil.AV_PIX_FMT_VDPAU_MPEG4) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VDPAU_MPEG4);
		};
		if (value == avutil.AV_PIX_FMT_DXVA2_VLD) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_DXVA2_VLD);
		};
		if (value == avutil.AV_PIX_FMT_RGB444LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB444LE);
		};
		if (value == avutil.AV_PIX_FMT_RGB444BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB444BE);
		};
		if (value == avutil.AV_PIX_FMT_BGR444LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR444LE);
		};
		if (value == avutil.AV_PIX_FMT_BGR444BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR444BE);
		};
		if (value == avutil.AV_PIX_FMT_YA8) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YA8);
		};
		if (value == avutil.AV_PIX_FMT_Y400A) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_Y400A);
		};
		if (value == avutil.AV_PIX_FMT_GRAY8A) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GRAY8A);
		};
		if (value == avutil.AV_PIX_FMT_BGR48BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR48BE);
		};
		if (value == avutil.AV_PIX_FMT_BGR48LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR48LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV420P9BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV420P9BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV420P9LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV420P9LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV420P10BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV420P10BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV420P10LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV420P10LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV422P10BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV422P10BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV422P10LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV422P10LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV444P9BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV444P9BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV444P9LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV444P9LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV444P10BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV444P10BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV444P10LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV444P10LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV422P9BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV422P9BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV422P9LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV422P9LE);
		};
		if (value == avutil.AV_PIX_FMT_VDA_VLD) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VDA_VLD);
		};
		if (value == avutil.AV_PIX_FMT_GBRP) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRP);
		};
		if (value == avutil.AV_PIX_FMT_GBR24P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBR24P);
		};
		if (value == avutil.AV_PIX_FMT_GBRP9BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRP9BE);
		};
		if (value == avutil.AV_PIX_FMT_GBRP9LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRP9LE);
		};
		if (value == avutil.AV_PIX_FMT_GBRP10BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRP10BE);
		};
		if (value == avutil.AV_PIX_FMT_GBRP10LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRP10LE);
		};
		if (value == avutil.AV_PIX_FMT_GBRP16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRP16BE);
		};
		if (value == avutil.AV_PIX_FMT_GBRP16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRP16LE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA422P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P);
		};
		if (value == avutil.AV_PIX_FMT_YUVA444P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P);
		};
		if (value == avutil.AV_PIX_FMT_YUVA420P9BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P9BE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA420P9LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P9LE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA422P9BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P9BE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA422P9LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P9LE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA444P9BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P9BE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA444P9LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P9LE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA420P10BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P10BE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA420P10LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P10LE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA422P10BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P10BE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA422P10LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P10LE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA444P10BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P10BE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA444P10LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P10LE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA420P16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P16BE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA420P16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA420P16LE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA422P16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P16BE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA422P16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA422P16LE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA444P16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P16BE);
		};
		if (value == avutil.AV_PIX_FMT_YUVA444P16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVA444P16LE);
		};
		if (value == avutil.AV_PIX_FMT_VDPAU) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VDPAU);
		};
		if (value == avutil.AV_PIX_FMT_XYZ12LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_XYZ12LE);
		};
		if (value == avutil.AV_PIX_FMT_XYZ12BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_XYZ12BE);
		};
		if (value == avutil.AV_PIX_FMT_NV16) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_NV16);
		};
		if (value == avutil.AV_PIX_FMT_NV20LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_NV20LE);
		};
		if (value == avutil.AV_PIX_FMT_NV20BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_NV20BE);
		};
		if (value == avutil.AV_PIX_FMT_RGBA64BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGBA64BE);
		};
		if (value == avutil.AV_PIX_FMT_RGBA64LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGBA64LE);
		};
		if (value == avutil.AV_PIX_FMT_BGRA64BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGRA64BE);
		};
		if (value == avutil.AV_PIX_FMT_BGRA64LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGRA64LE);
		};
		if (value == avutil.AV_PIX_FMT_YVYU422) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YVYU422);
		};
		if (value == avutil.AV_PIX_FMT_VDA) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VDA);
		};
		if (value == avutil.AV_PIX_FMT_YA16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YA16BE);
		};
		if (value == avutil.AV_PIX_FMT_YA16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YA16LE);
		};
		if (value == avutil.AV_PIX_FMT_GBRAP) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRAP);
		};
		if (value == avutil.AV_PIX_FMT_GBRAP16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRAP16BE);
		};
		if (value == avutil.AV_PIX_FMT_GBRAP16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRAP16LE);
		};
		if (value == avutil.AV_PIX_FMT_QSV) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_QSV);
		};
		if (value == avutil.AV_PIX_FMT_MMAL) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_MMAL);
		};
		if (value == avutil.AV_PIX_FMT_D3D11VA_VLD) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_D3D11VA_VLD);
		};
		if (value == avutil.AV_PIX_FMT_CUDA) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_CUDA);
		};
		if (value == avutil.AV_PIX_FMT_0RGB) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_0RGB);
		};
		if (value == avutil.AV_PIX_FMT_RGB0) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_RGB0);
		};
		if (value == avutil.AV_PIX_FMT_0BGR) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_0BGR);
		};
		if (value == avutil.AV_PIX_FMT_BGR0) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BGR0);
		};
		if (value == avutil.AV_PIX_FMT_YUV420P12BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV420P12BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV420P12LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV420P12LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV420P14BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV420P14BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV420P14LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV420P14LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV422P12BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV422P12BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV422P12LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV422P12LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV422P14BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV422P14BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV422P14LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV422P14LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV444P12BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV444P12BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV444P12LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV444P12LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV444P14BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV444P14BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV444P14LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV444P14LE);
		};
		if (value == avutil.AV_PIX_FMT_GBRP12BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRP12BE);
		};
		if (value == avutil.AV_PIX_FMT_GBRP12LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRP12LE);
		};
		if (value == avutil.AV_PIX_FMT_GBRP14BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRP14BE);
		};
		if (value == avutil.AV_PIX_FMT_GBRP14LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRP14LE);
		};
		if (value == avutil.AV_PIX_FMT_YUVJ411P) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUVJ411P);
		};
		if (value == avutil.AV_PIX_FMT_BAYER_BGGR8) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BAYER_BGGR8);
		};
		if (value == avutil.AV_PIX_FMT_BAYER_RGGB8) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BAYER_RGGB8);
		};
		if (value == avutil.AV_PIX_FMT_BAYER_GBRG8) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BAYER_GBRG8);
		};
		if (value == avutil.AV_PIX_FMT_BAYER_GRBG8) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BAYER_GRBG8);
		};
		if (value == avutil.AV_PIX_FMT_BAYER_BGGR16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BAYER_BGGR16LE);
		};
		if (value == avutil.AV_PIX_FMT_BAYER_BGGR16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BAYER_BGGR16BE);
		};
		if (value == avutil.AV_PIX_FMT_BAYER_RGGB16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BAYER_RGGB16LE);
		};
		if (value == avutil.AV_PIX_FMT_BAYER_RGGB16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BAYER_RGGB16BE);
		};
		if (value == avutil.AV_PIX_FMT_BAYER_GBRG16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BAYER_GBRG16LE);
		};
		if (value == avutil.AV_PIX_FMT_BAYER_GBRG16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BAYER_GBRG16BE);
		};
		if (value == avutil.AV_PIX_FMT_BAYER_GRBG16LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BAYER_GRBG16LE);
		};
		if (value == avutil.AV_PIX_FMT_BAYER_GRBG16BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_BAYER_GRBG16BE);
		};
		if (value == avutil.AV_PIX_FMT_XVMC) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_XVMC);
		};
		if (value == avutil.AV_PIX_FMT_YUV440P10LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV440P10LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV440P10BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV440P10BE);
		};
		if (value == avutil.AV_PIX_FMT_YUV440P12LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV440P12LE);
		};
		if (value == avutil.AV_PIX_FMT_YUV440P12BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_YUV440P12BE);
		};
		if (value == avutil.AV_PIX_FMT_AYUV64LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_AYUV64LE);
		};
		if (value == avutil.AV_PIX_FMT_AYUV64BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_AYUV64BE);
		};
		if (value == avutil.AV_PIX_FMT_VIDEOTOOLBOX) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_VIDEOTOOLBOX);
		};
		if (value == avutil.AV_PIX_FMT_P010LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_P010LE);
		};
		if (value == avutil.AV_PIX_FMT_P010BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_P010BE);
		};
		if (value == avutil.AV_PIX_FMT_GBRAP12BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRAP12BE);
		};
		if (value == avutil.AV_PIX_FMT_GBRAP12LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRAP12LE);
		};
		if (value == avutil.AV_PIX_FMT_GBRAP10BE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRAP10BE);
		};
		if (value == avutil.AV_PIX_FMT_GBRAP10LE) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_GBRAP10LE);
		};
		if (value == avutil.AV_PIX_FMT_MEDIACODEC) {
			return new FFmpegPixelFormat(FFmpegPixelFormatName.AV_PIX_FMT_MEDIACODEC);
		};
		return new FFmpegPixelFormat(value);
	}
}
