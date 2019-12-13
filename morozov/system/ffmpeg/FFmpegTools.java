// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg;

import target.*;

import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.avcodec.*;
import static org.bytedeco.javacpp.swscale.*;
import org.bytedeco.javacpp.*;

import morozov.system.ffmpeg.converters.*;
import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.ffmpeg.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.awt.Graphics2D;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class FFmpegTools {
	//
	protected static AtomicBoolean theFFmpegLibraryIsInitialized= new AtomicBoolean(false);
	protected static long STREAM_DURATION= 10;
	protected static int SCALE_FLAGS= SWS_BICUBIC;
	//
	protected static Object codecOpeningGuard= new Object();
	//
	protected static int maximumAllowedNumeratorAndDenominator= (1 << 16) - 1;
	//
	///////////////////////////////////////////////////////////////
	//
	public static void initializeFFmpegIfNecessary() {
		if (!theFFmpegLibraryIsInitialized.get()) {
			synchronized (theFFmpegLibraryIsInitialized) {
				if (!theFFmpegLibraryIsInitialized.get()) {
					synchronized (codecOpeningGuard) {
						// Initialize libavformat and register all the muxers,
						// demuxers and protocols:
						av_register_all();
					}
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term listInputFormats() {
		initializeFFmpegIfNecessary();
		ArrayList<AVInputFormat> formats= new ArrayList<>();
		AVInputFormat format1= av_iformat_next(null);
		while (true) {
			if (format1 != null) {
				formats.add(format1);
				format1= av_iformat_next(format1);
			} else {
				break;
			}
		};
		AVInputFormat[] formatArray= formats.toArray(new AVInputFormat[formats.size()]);
		Term result1= PrologEmptyList.instance;
		for (int n=formatArray.length-1; n >= 0; n--) {
			AVInputFormat format2= formatArray[n];
			String name= replaceNull(format2.name(),"");
			String longName= replaceNull(format2.long_name(),"");
			String mimeType= replaceNull(format2.mime_type(),"");
			String extensions= replaceNull(format2.extensions(),"");
			int flags= format2.flags();
			FFmpegFormatFlag[] flagArray= FFmpegFormatFlag.extractFFmpegFormatFlagArray(flags);
			Term termFlags= PrologEmptyList.instance;
			for (int k=flagArray.length-1; k >= 0; k--) {
				termFlags= new PrologList(flagArray[k].toTerm(),termFlags);
			};
			PointerPointer tags= format2.codec_tag();
			Term termTags= listTags(tags);
			Term result2= PrologEmptySet.instance;
			result2= new PrologSet(-SymbolCodes.symbolCode_E_tags,termTags,result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_flags,termFlags,result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_extensions,new PrologString(extensions),result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_mime_type,new PrologString(mimeType),result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_long_name,new PrologString(longName),result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_name,new PrologString(name),result2);
			result1= new PrologList(result2,result1);
		};
		return result1;
	}
	//
	public static Term listOutputFormats() {
		initializeFFmpegIfNecessary();
		ArrayList<AVOutputFormat> formats= new ArrayList<>();
		AVOutputFormat format1= av_oformat_next(null);
		while (true) {
			if (format1 != null) {
				formats.add(format1);
				format1= av_oformat_next(format1);
			} else {
				break;
			}
		};
		AVOutputFormat[] formatArray= formats.toArray(new AVOutputFormat[formats.size()]);
		Term result1= PrologEmptyList.instance;
		for (int n=formatArray.length-1; n >= 0; n--) {
			AVOutputFormat format2= formatArray[n];
			String name= replaceNull(format2.name(),"");
			String longName= replaceNull(format2.long_name(),"");
			String mimeType= replaceNull(format2.mime_type(),"");
			String extensions= replaceNull(format2.extensions(),"");
			int audio_codec= format2.audio_codec();
			Term audioCodecTerm= codecToTerm(audio_codec);
			int video_codec= format2.video_codec();
			Term videoCodecTerm= codecToTerm(video_codec);
			int subtitle_codec= format2.subtitle_codec();
			Term subtitleCodecTerm= codecToTerm(subtitle_codec);
			int flags= format2.flags();
			FFmpegFormatFlag[] flagArray= FFmpegFormatFlag.extractFFmpegFormatFlagArray(flags);
			Term termFlags= PrologEmptyList.instance;
			for (int k=flagArray.length-1; k >= 0; k--) {
				termFlags= new PrologList(flagArray[k].toTerm(),termFlags);
			};
			PointerPointer tags= format2.codec_tag();
			Term termTags= listTags(tags);
			Term result2= PrologEmptySet.instance;
			result2= new PrologSet(-SymbolCodes.symbolCode_E_tags,termTags,result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_flags,termFlags,result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_subtitle_codec,subtitleCodecTerm,result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_video_codec,videoCodecTerm,result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_audio_codec,audioCodecTerm,result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_extensions,new PrologString(extensions),result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_mime_type,new PrologString(mimeType),result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_long_name,new PrologString(longName),result2);
			result2= new PrologSet(-SymbolCodes.symbolCode_E_name,new PrologString(name),result2);
			result1= new PrologList(result2,result1);
		};
		return result1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term listTags(PointerPointer tags) {
		if (tags != null) {
			ArrayList<Term> tagArray= new ArrayList<>();
			int index1= 0;
			while (true) {
				Pointer item1= tags.get(index1);
				if (item1==null) {
					break;
				} else {
					IntPointer item2= new IntPointer(item1);
					if (item2 != null) {
						int index2= 0;
						while (true) {
//---------------------------------------------------------------------
int id= item2.get(index2);
if (id <= 0) {
	break;
} else {
	int tag= item2.get(index2+1);
	AVCodec currentCodec= avcodec_find_encoder(id);
	int type;
	if (currentCodec != null) {
		type= currentCodec.type();
	} else {
		type= avutil.AVMEDIA_TYPE_UNKNOWN;
	};
	FFmpegMediaType mediaType= FFmpegMediaType.integerToFFmpegMediaType(type);
	Term[] arguments= new Term[]{
		new PrologInteger(id),
		new PrologInteger(tag),
		mediaType.toTerm()};
	Term item= new PrologStructure(SymbolCodes.symbolCode_E_tag,arguments);
	tagArray.add(item);
	index2= index2 + 2;
}
//---------------------------------------------------------------------
						}
					}
				};
				index1++;
			};
			return GeneralConverters.arrayListToTerm(tagArray);
		} else {
			return PrologEmptyList.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term codecToTerm(int codecIdentifier) {
		if (codecIdentifier != AV_CODEC_ID_NONE) {
			AVCodec avCodec= avcodec_find_encoder(codecIdentifier);
			if (avCodec != null) {
//---------------------------------------------------------------------
// Name of the codec implementation.
// The name is globally unique among encoders and among decoders
// (but an encoder and a decoder can share the same name).
// This is the primary way to find a codec from the user perspective.
String name= replaceNull(avCodec.name(),"");
// Descriptive name for the codec, meant to be more human readable
// than name. You should use the NULL_IF_CONFIG_SMALL() macro to
// define it.
String longName= replaceNull(avCodec.long_name(),"");
FFmpegMediaType mediaType= FFmpegMediaType.integerToFFmpegMediaType(avCodec.type());
int id= avCodec.id();
// Array of supported pixel formats, or NULL if unknown,
// array is terminated by -1.
IntPointer pixelFormats= avCodec.pix_fmts();
Term termPixelFormats= PrologEmptyList.instance;
if (pixelFormats != null) {
	ArrayList<FFmpegPixelFormat> formatArray= new ArrayList<>();
	int counterFMTS= 0;
	while (true) {
		int fmt= pixelFormats.get(counterFMTS);
		if (fmt == -1) {
			break;
		} else {
			FFmpegPixelFormat format= FFmpegPixelFormat.integerToFFmpegPixelFormat(fmt);
			formatArray.add(format);
			counterFMTS++;
		}
	};
	for (int k=formatArray.size()-1; k >= 0; k--) {
		termPixelFormats= new PrologList(formatArray.get(k).toTerm(),termPixelFormats);
	}
};
// Array of supported audio samplerates, or NULL if unknown,
// array is terminated by 0.
IntPointer srts= avCodec.supported_samplerates();
Term termSampleRates= PrologEmptyList.instance;
if (srts != null) {
	ArrayList<Integer> sampleRateArray= new ArrayList<>();
	int counterSRTS= 0;
	while (true) {
		int srt= srts.get(counterSRTS);
		if (srt == 0) {
			break;
		} else {
			sampleRateArray.add(srt);
			counterSRTS++;
		}
	};
	for (int k=sampleRateArray.size()-1; k >= 0; k--) {
		termSampleRates= new PrologList(new PrologInteger(sampleRateArray.get(k)),termSampleRates);
	}
};
// Array of supported sample formats, or NULL if unknown,
// array is terminated by -1.
IntPointer sampleFormats= avCodec.sample_fmts();
Term termAudioSampleFormats= PrologEmptyList.instance;
if (sampleFormats != null) {
	ArrayList<FFmpegAudioSampleFormat> formatArray= new ArrayList<>();
	int counterFMTS= 0;
	while (true) {
		int fmt= sampleFormats.get(counterFMTS);
		if (fmt == -1) {
			break;
		} else {
			FFmpegAudioSampleFormat format= FFmpegAudioSampleFormat.integerToFFmpegAudioSampleFormat(fmt);
			formatArray.add(format);
			counterFMTS++;
		}
	};
	for (int k=formatArray.size()-1; k >= 0; k--) {
		termAudioSampleFormats= new PrologList(formatArray.get(k).toTerm(),termAudioSampleFormats);
	}
};
// Array of support channel layouts, or NULL if unknown,
// array is terminated by 0.
LongPointer channelLayouts= avCodec.channel_layouts();
Term termChannelLayouts= PrologEmptyList.instance;
if (channelLayouts != null) {
	ArrayList<FFmpegChannelLayout> layoutArray= new ArrayList<>();
	int counterCHLS= 0;
	while (true) {
		long lt= channelLayouts.get(counterCHLS);
		if (lt == 0) {
			break;
		} else {
			FFmpegChannelLayout layout= FFmpegChannelLayout.longToFFmpegChannelLayout(lt);
			layoutArray.add(layout);
			counterCHLS++;
		}
	};
	for (int k=layoutArray.size()-1; k >= 0; k--) {
		termChannelLayouts= new PrologList(layoutArray.get(k).toTerm(),termChannelLayouts);
	}
};
Term result= PrologEmptySet.instance;
result= new PrologSet(-SymbolCodes.symbolCode_E_channel_layouts,termChannelLayouts,result);
result= new PrologSet(-SymbolCodes.symbolCode_E_sample_fmts,termAudioSampleFormats,result);
result= new PrologSet(-SymbolCodes.symbolCode_E_supported_samplerates,termSampleRates,result);
result= new PrologSet(-SymbolCodes.symbolCode_E_pix_fmts,termPixelFormats,result);
result= new PrologSet(-SymbolCodes.symbolCode_E_id,new PrologInteger(id),result);
result= new PrologSet(-SymbolCodes.symbolCode_E_type,mediaType.toTerm(),result);
result= new PrologSet(-SymbolCodes.symbolCode_E_long_name,new PrologString(longName),result);
result= new PrologSet(-SymbolCodes.symbolCode_E_name,new PrologString(name),result);
return result;
//---------------------------------------------------------------------
			} else {
				return PrologEmptySet.instance;
			}
		} else {
			return PrologEmptySet.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@SuppressWarnings("deprecation")
	public static String retrieveCodecInformation(AVStream stream) {
		// AVCodecParameters codecParameters= stream.codecpar();
		// AVCodecContext codecContext= new AVCodecContext();
		AVCodecContext codecContext= stream.codec();
		// avcodec_parameters_to_context(codecContext,codecParameters);
		// codecContext.properties(stream.codec().properties());
		// codecContext.qmin(stream.codec().qmin());
		// codecContext.qmax(stream.codec().qmax());
		// codecContext.coded_width(stream.codec().coded_width());
		// codecContext.coded_height(stream.codec().coded_height());
		int bufferSize= 256*4;
		byte[] buffer= new byte[bufferSize];
		for (int k=0; k < bufferSize; k++) {
			buffer[k]= 0;
		};
		avcodec_string(buffer,buffer.length,codecContext,0);
		int textLength= 0;
		for (int k=0; k < bufferSize; k++) {
			if (buffer[k]==0) {
				textLength= k;
				break;
			}
		};
		return new String(buffer,0,textLength);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String replaceNull(BytePointer p, String x) {
		if (p != null) {
			return p.getString();
		} else {
			return x;
		}
	}
	public static String replaceNull(String p, String x) {
		if (p != null) {
			return p;
		} else {
			return x;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static AVRational argumentToAVRational(Term value, ChoisePoint iX) {
		try {
			Term[] arguments= value.isStructure(SymbolCodes.symbolCode_E_q,2,iX);
			int givenNumerator= GeneralConverters.argumentToSmallInteger(arguments[0],iX);
			int givenDenominator= GeneralConverters.argumentToSmallInteger(arguments[1],iX);
			AVRational rational= new AVRational();
			rational.num(givenNumerator);
			rational.den(givenDenominator);
			return rational;
		} catch (Backtracking b1) {
			try {
				double doubleValue= value.getRealValue(iX);
				// av_d2q:
				// Convert a double precision floating point
				// number to a rational.
				// In case of infinity, the returned value
				// is expressed as {1, 0} or
				// {-1, 0} depending on the sign.
				// d - double to convert.
				// max - maximum allowed numerator and
				// denominator.
				// return - d in AVRational form.
				AVRational rational= av_d2q(doubleValue,maximumAllowedNumeratorAndDenominator);
				return rational;
			} catch (TermIsNotAReal e2) {
				throw new WrongArgumentIsNotAVRational(value);
			}
		}
	}
	//
	public static double computeTime(long time, AVRational base) {
		return (double)time * base.num() / base.den();
	}
	//
	public static long computeRelativeTime(long time, AVRational base) {
		return time * base.den() / base.num() / 1000;
	}
	//
	public static long computeTimeOfFrameInMilliseconds(long numberOfFrames, AVRational base) {
		return (long)(numberOfFrames * 1000.0 * base.den() / base.num());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegWorkAroundBug[] argumentToFFmpegWorkAroundBugs(Term value, ChoisePoint iX) {
		if (value != null) {
			value= value.dereferenceValue(iX);
			if (value.thisIsEmptyList()) {
				return new FFmpegWorkAroundBug[0];
			} else {
				try {
					value.getNextListHead(iX);
					return listToFFmpegWorkAroundBugs(value,iX);
				} catch (EndOfList e) {
					return new FFmpegWorkAroundBug[0];
				} catch (TermIsNotAList e) {
					FFmpegWorkAroundBug option= FFmpegWorkAroundBug.argumentToFFmpegWorkAroundBug(value,iX);
					return new FFmpegWorkAroundBug[]{option};
				}
			}
		} else {
			return new FFmpegWorkAroundBug[0];
		}
	}
	//
	public static FFmpegWorkAroundBug[] listToFFmpegWorkAroundBugs(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		ArrayList<FFmpegWorkAroundBug> optionArray= new ArrayList<>();
		Term nextHead= null;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				FFmpegWorkAroundBug option= FFmpegWorkAroundBug.argumentToFFmpegWorkAroundBug(nextHead,iX);
				optionArray.add(option);
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e1) {
		} catch (TermIsNotAList e2) {
			FFmpegWorkAroundBug option= FFmpegWorkAroundBug.argumentToFFmpegWorkAroundBug(currentTail,iX);
			optionArray.add(option);
		};
		return optionArray.toArray(new FFmpegWorkAroundBug[optionArray.size()]);
	}
	//
	public static int convertFFmpegWorkAroundBugsToInteger(FFmpegWorkAroundBug[] flags) {
		int value= 0;
		for (int n=0; n < flags.length; n++) {
			value |= flags[n].toInteger();
		};
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static FFmpegDebugOption[] argumentToFFmpegDebugOptions(Term value, ChoisePoint iX) {
		if (value != null) {
			value= value.dereferenceValue(iX);
			if (value.thisIsEmptyList()) {
				return new FFmpegDebugOption[0];
			} else {
				try {
					value.getNextListHead(iX);
					return listToFFmpegDebugOptions(value,iX);
				} catch (EndOfList e) {
					return new FFmpegDebugOption[0];
				} catch (TermIsNotAList e) {
					FFmpegDebugOption option= FFmpegDebugOption.argumentToFFmpegDebugOption(value,iX);
					return new FFmpegDebugOption[]{option};
				}
			}
		} else {
			return new FFmpegDebugOption[0];
		}
	}
	//
	public static FFmpegDebugOption[] listToFFmpegDebugOptions(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		ArrayList<FFmpegDebugOption> optionArray= new ArrayList<>();
		Term nextHead= null;
		Term currentTail= value;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(iX);
				FFmpegDebugOption option= FFmpegDebugOption.argumentToFFmpegDebugOption(nextHead,iX);
				optionArray.add(option);
				currentTail= currentTail.getNextListTail(iX);
			}
		} catch (EndOfList e1) {
		} catch (TermIsNotAList e2) {
			FFmpegDebugOption option= FFmpegDebugOption.argumentToFFmpegDebugOption(currentTail,iX);
			optionArray.add(option);
		};
		return optionArray.toArray(new FFmpegDebugOption[optionArray.size()]);
	}
	//
	public static int convertFFmpegDebugOptionsToInteger(FFmpegDebugOption[] flags) {
		int value= 0;
		for (int n=0; n < flags.length; n++) {
			value |= flags[n].toInteger();
		};
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static AVDictionary createAVDistionary(FFmpegCodecOption[] options) {
		AVDictionary dictionary= new AVDictionary(null);
		if (options != null) {
			for (int n=0; n < options.length; n++) {
				FFmpegCodecOption option= options[n];
				// av_dict_set:
				// Set the given entry in *pm,
				// overwriting an existing entry.
				// Note: If AV_DICT_DONT_STRDUP_KEY
				// or AV_DICT_DONT_STRDUP_VAL is set,
				// these arguments will be freed on
				// error.
				// Warning: Adding a new entry to a
				// dictionary invalidates all existing
				// entries previously returned with
				// av_dict_get.
				// pm - pointer to a pointer to a
				// dictionary struct. If *pm is NULL a
				// dictionary struct is allocated and
				// put in *pm.
				// key - entry key to add to *pm
				// (will either be av_strduped or added
				// as a new key depending on flags).
				// value - entry value to add to.
				// *pm (will be av_strduped or added as
				// a new key depending on flags).
				// Passing a NULL value will cause an
				// existing entry to be deleted.
				// return >= 0 on success otherwise an
				// error code < 0.
				int flag= av_dict_set(dictionary,option.getName(),option.getValue(),0);
				if (flag < 0) {
					throw new FFmpegCannotSetDictionaryEntry(option.getName(),option.getValue());
				}
			}
		};
		return dictionary;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String av_err2str(int errnum) {
		// av_make_error_string:
		// Fill the provided buffer with a string containing
		// an error string corresponding to the AVERROR code
		// errnum.
		// errbuf - a buffer.
		// errbuf_size - size in bytes of errbuf.
		// errnum - error code to describe.
		// return - the buffer in input, filled with the error
		// description.
		return new String(av_make_error_string(new byte[AV_ERROR_MAX_STRING_SIZE],AV_ERROR_MAX_STRING_SIZE,errnum));
	}
	//
	public static String av_ts2str(long ts) {
		// av_ts_make_string:
		// Fill the provided buffer with a string containing
		// a timestamp representation.
		// buf - a buffer with size in bytes of at
		// least AV_TS_MAX_STRING_SIZE.
		// ts - the timestamp to represent.
		// return - the buffer in input.
		return av_ts_make_string(ts);
	}
	public static String av_ts_make_string(long ts) {
		if (ts==AV_NOPTS_VALUE) {
			return "NOPTS";
		} else {
			return Long.toString(ts);
		}
	}
	//
	// Convenience macro, the return value should be used only
	// directly in function arguments but never stand-alone.
	//
	public static String av_ts2timestr(long ts, AVRational tb) {
		// av_ts_make_time_string:
		// Fill the provided buffer with a string containing
		// a timestamp time representation.
		// buf - a buffer with size in bytes of at least
		// AV_TS_MAX_STRING_SIZE.
		// ts - the timestamp to represent.
		// tb - the timebase of the timestamp.
		// return - the buffer in input.
		return av_ts_make_time_string(ts,tb);
	}
	public static String av_ts_make_time_string(long ts, AVRational tb) {
		if (ts==AV_NOPTS_VALUE) {
			return "NOPTS";
		} else {
			// av_q2d:
			// Convert an AVRational to a double.
			// a - AVRational to convert.
			// return - a in floating-point form.
			return String.format("%1.6g",av_q2d(tb)*ts);
		}
	}
	//
	public static void logPacket(AVFormatContext fmt_ctx, AVPacket pkt) {
		// streams:
		// A list of all streams in the file. New streams are
		// created with avformat_new_stream().
		// - demuxing: streams are created by libavformat in
		// avformat_open_input().
		// If AVFMTCTX_NOHEADER is set in ctx_flags, then new
		// streams may also appear in av_read_frame().
		// - muxing: streams are created by the user before
		// avformat_write_header().
		// Freed by libavformat in avformat_free_context().
		AVStream stream= fmt_ctx.streams(pkt.stream_index());
		// time_base:
		// This is the fundamental unit of time (in seconds)
		// in terms of which frame timestamps are represented.
		// decoding: set by libavformat
		// encoding: May be set by the caller before
		// avformat_write_header() to provide a hint to the
		// muxer about the desired timebase. In
		// avformat_write_header(), the muxer will overwrite
		// this field with the timebase that will actually be
		// used for the timestamps written into the file
		// (which may or may not be related to the
		// user-provided one, depending on the format).
		AVRational time_base= stream.time_base();
		// av_ts2str:
		// Convenience macro, the return value should be used
		// only directly in function arguments but never
		// stand-alone.
		//
		// av_ts2timestr:
		// Convenience macro, the return value should be used
		// only directly in function arguments but never
		// stand-alone.
		//
		// pts:
		// Presentation timestamp in AVStream->time_base
		// units; the time at which the decompressed packet
		// will be presented to the user.
		// Can be AV_NOPTS_VALUE if it is not stored in the
		// file. pts MUST be larger or equal to dts as
		// presentation cannot happen before decompression,
		// unless one wants to view hex dumps. Some formats
		// misuse the terms dts and pts/cts to mean something
		// different. Such timestamps must be converted to
		// true pts/dts before they are stored in AVPacket.
		//
		// dts:
		// Decompression timestamp in AVStream->time_base
		// units; the time at which the packet is decompressed.
		// Can be AV_NOPTS_VALUE if it is not stored in the
		// file.
		System.err.printf(
			"pts:%s pts_time:%s dts:%s dts_time:%s duration:%s duration_time:%s stream_index:%d\n",
			av_ts2str(pkt.pts()),
			av_ts2timestr(pkt.pts(),time_base),
			av_ts2str(pkt.dts()),
			av_ts2timestr(pkt.dts(),time_base),
			av_ts2str(pkt.duration()),
			av_ts2timestr(pkt.duration(),time_base),
			pkt.stream_index());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static java.awt.image.BufferedImage convertToType(java.awt.image.BufferedImage sourceImage, int targetType) {
		java.awt.image.BufferedImage image;
		// If the source image is already the target type,
		// return the source image:
		if (sourceImage.getType() == targetType) {
			image= sourceImage;
		} else {
			// Otherwise create a new image of the target
			// type and draw the new image:
			image= new java.awt.image.BufferedImage(sourceImage.getWidth(),sourceImage.getHeight(),targetType);
			Graphics2D g2= image.createGraphics();
			try {
				g2.drawImage(sourceImage,0,0,null);
			} finally {
				g2.dispose();
			}
		};
		return image;
	}
	//
	public static ByteBuffer createImageBuffer(java.awt.image.BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		ByteBuffer byteBuffer= ByteBuffer.allocate(width*height*4);
		IntBuffer intBuffer= byteBuffer.asIntBuffer();
		// getRGB:
		// Returns an array of integer pixels in the default
		// RGB color model (TYPE_INT_ARGB) and default sRGB
		// color space, from a portion of the image data.
		// Color conversion takes place if the default model
		// does not match the image ColorModel. There are only
		// 8-bits of precision for each color component in the
		// returned data when using this method. With a
		// specified coordinate (x, y) in the image, the ARGB
		// pixel can be accessed in this way:
		// pixel = rgbArray[offset + (y-startY)*scansize +
		// (x-startX)];
		// An ArrayOutOfBoundsException may be thrown if the
		// region is not in bounds. However, explicit bounds
		// checking is not guaranteed.
		int[] rgbArray= image.getRGB(0,0,image.getWidth(),image.getHeight(),null,0,image.getWidth());
		intBuffer.put(rgbArray);
		return byteBuffer;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static AVFrame allocAudioFrame(int sampleFormat, long channelLayout, int sampleRate, int nbSamples) {
		// av_frame_alloc:
		// Allocate an AVFrame and set its fields to default
		// values. The resulting struct must be freed using
		// av_frame_free().
		// return - an AVFrame filled with default values or
		// NULL on failure.
		// Note: this only allocates the AVFrame itself, not
		// the data buffers. Those must be allocated through
		// other means, e.g. with av_frame_get_buffer() or
		// manually.
		AVFrame frame= av_frame_alloc();
		if (frame==null) {
			throw new FFmpegCannotAllocateAVFrame();
		};
		frame.format(sampleFormat);
		frame.channel_layout(channelLayout);
		frame.sample_rate(sampleRate);
		frame.nb_samples(nbSamples);
		if (nbSamples != 0) {
			// av_frame_get_buffer:
			// Allocate new buffer(s) for audio or video
			// data. The following fields must be set on
			// frame before calling this function:
			// - format (pixel format for video, sample
			// format for audio);
			// - width and height for video;
			// - nb_samples and channel_layout for audio.
			// This function will fill AVFrame.data and
			// AVFrame.buf arrays and, if necessary,
			// allocate and fill AVFrame.extended_data
			// and AVFrame.extended_buf.
			// For planar formats, one buffer will be
			// allocated for each plane.
			// Warning: if frame already has been
			// allocated, calling this function will leak
			// memory. In addition, undefined behavior
			// can occur in certain cases.
			// frame - frame in which to store the
			// new buffers.
			// align - required buffer size alignment.
			// return - 0 on success, a negative AVERROR
			// on error.
			int flag= av_frame_get_buffer(frame,0);
			// int flag= av_frame_get_buffer(frame,64);
			if (flag < 0) {
				throw new FFmpegCannotAllocateFrameDataBuffers();
			}
		};
		return frame;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static AVFrame getVideoFrame(FFmpegOutputStreamState ost) {
		AVCodecContext c= ost.enc;
		// Check if we want to generate more frames:
		AVRational avRational= av_make_q(1,1);
		// av_compare_ts:
		// Compare two timestamps each in its own time base.
		// return - one of the following values:
		// (a) -1 if ts_a is before ts_b;
		// (b) 1 if ts_a is after ts_b;
		// (c) 0 if they represent the same position.
		// Warning: The result of the function is undefined
		// if one of the timestamps is outside
		// the int64_t range when represented
		// in the other's timebase.
		if (av_compare_ts(ost.next_pts,c.time_base(),STREAM_DURATION,avRational) >= 0) {
			return null;
		};
		// av_frame_make_writable:
		// Ensure that the frame data is writable, avoiding
		// data copy if possible.
		// Do nothing if the frame is writable, allocate new
		// buffers and copy the data if it is not.
		// return - 0 on success, a negative AVERROR on error.
		// When we pass a frame to the encoder, it may keep
		// a reference to it internally; make sure we do not
		// overwrite it here.
		if (av_frame_make_writable(ost.frame) < 0) {
			throw new FFmpegCannotMakeFrameWritable();
		};
		if (c.pix_fmt() != AV_PIX_FMT_YUV420P) {
			// As we only generate a YUV420P picture, we
			// must convert it to the codec pixel format
			// if needed.
			if (ost.sws_ctx==null) {
				// sws_getContext:
				// Allocate and return an SwsContext.
				// You need it to perform
				// scaling/conversion operations
				// using sws_scale().
				// srcW - the width of the source
				// image.
				// srcH - the height of the source
				// image.
				// srcFormat - the source image
				// format.
				// dstW - the width of the
				// destination image.
				// dstH - the height of the
				// destination image.
				// dstFormat - the destination
				// image format.
				// flags - specify which
				// algorithm and options to use for
				// rescaling.
				// param - extra parameters to
				// tune the used scaler.
				// For SWS_BICUBIC param[0] and [1]
				// tune the shape of the basis
				// function, param[0] tunes f(1) and
				// param[1] fB(1)
				// For SWS_GAUSS param[0] tunes the
				// exponent and thus cutoff frequency
				// For SWS_LANCZOS param[0] tunes the
				// width of the window function
				// return - a pointer to an allocated
				// context, or NULL in case of error.
				// Note: this function is to be
				// removed after a saner alternative
				// is written.
				ost.sws_ctx= sws_getContext(
					c.width(),c.height(),
					AV_PIX_FMT_YUV420P,
					c.width(),c.height(),
					c.pix_fmt(),
					SCALE_FLAGS,
					null,null,(DoublePointer)null);
				if (ost.sws_ctx==null) {
					throw new FFmpegCannotInitializeSWSContext();
				}
			};
			fill_YUV_Image(ost.tmp_frame,ost.next_pts,c.width(),c.height());
			// sws_scale:
			// Scale the image slice in srcSlice and put
			// the resulting scaled slice in the image in
			// dst. A slice is a sequence of consecutive
			// rows in an image.
			// Slices have to be provided in sequential
			// order, either in top-bottom or bottom-top
			// order. If slices are provided in
			// non-sequential order the behavior of the
			// function is undefined.
			// c - the scaling context previously
			// created with sws_getContext().
			// srcSlice - the array containing
			// the pointers to the planes of the source
			// slice.
			// srcStride - the array containing the
			// strides for each plane of the source image.
			// srcSliceY - the position in the source
			// image of the slice to process, that is the
			// number (counted starting from zero) in the
			// image of the first row of the slice.
			// srcSliceH - the height of the source slice,
			// that is the number of rows in the slice.
			// dst - the array containing the
			// pointers to the planes of the destination
			// image.
			// dstStride - the array
			// containing the strides for each plane of
			// the destination image.
			// return - the height of the output slice.
			sws_scale(
				ost.sws_ctx,
				ost.tmp_frame.data(),
				ost.tmp_frame.linesize(),
				0,
				c.height(),
				ost.frame.data(),
				ost.frame.linesize());
		} else {
			fill_YUV_Image(ost.frame,ost.next_pts,c.width(),c.height());
		};
		ost.frame.pts(ost.next_pts++);
		return ost.frame;
	}
	//
	// Prepare a dummy image:
	//
	public static void fill_YUV_Image(AVFrame pict, long frame_index, int width, int height) {
		long i= frame_index;
		// linesize:
		// For video, size in bytes of each picture line.
		// For audio, size in bytes of each plane.
		// For audio, only linesize[0] may be set. For planar
		// audio, each channel plane must be the same size.
		// For video the linesizes should be multiples of the
		// CPUs alignment preference, this is 16 or 32 for
		// modern desktop CPUs. Some code requires such
		// alignment other code can be slower without correct
		// alignment, for yet other it makes no difference.
		// Note. The linesize may be larger than the size of
		// usable data - there may be extra padding present
		// for performance reasons.
		BytePointer pictData0= pict.data(0);
		// Y:
		int linesize0= pict.linesize(0);
		int arrayLength0= (height)*linesize0;
		byte[] data0= new byte[arrayLength0];
		for (int y=0; y < height; y++) {
			for (int x=0; x < width; x++) {
				data0[y*linesize0+x]= (byte)(x + y + i * 3);
			}
		};
		pictData0.put(data0);
		// Cb:
		BytePointer pictData1= pict.data(1);
		int linesize1= pict.linesize(1);
		int arrayLength1= (height/2)*linesize1;
		byte[] data1= new byte[arrayLength1];
		for (int y=0; y < height / 2; y++) {
			for (int x=0; x < width / 2; x++) {
				data1[y*linesize1+x]= (byte)(128 + y + i * 2);
			}
		};
		pictData1.put(data1);
		// Cr:
		BytePointer pictData2= pict.data(2);
		int linesize2= pict.linesize(2);
		int arrayLength2= (height/2)*linesize2;
		byte[] data2= new byte[arrayLength2];
		for (int y=0; y < height / 2; y++) {
			for (int x=0; x < width / 2; x++) {
				data2[y*linesize2+x]= (byte)(64 + x + i * 5);
			}
		};
		pictData2.put(data2);
	}
}
