// (c) 2017 IRE RAS Alexei A. Morozov.
// Thanks to Alex Andres for the JavaAV project
// (https://github.com/hoary/JavaAV).
// Thanks to Fabrice Bellard for his libavformat API example
// and all the FFmpeg project team (https://ffmpeg.org).
// Thanks to Stephen Dranger, Martin Bohme, and
// Michael Penkov for tutorials on FFmpeg.
// Thanks to Samuel Audet for the JavaCPP project.
// (https://github.com/bytedeco/javacpp).

package morozov.system.ffmpeg;

import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.avcodec.*;
import static org.bytedeco.javacpp.swscale.*;
import static org.bytedeco.javacpp.swresample.*;
import org.bytedeco.javacpp.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.ffmpeg.errors.*;
import morozov.system.ffmpeg.interfaces.*;
import morozov.system.sound.frames.data.*;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;
import java.nio.ByteOrder;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class FFmpegFrameReadingTask extends Thread {
	//
	///////////////////////////////////////////////////////////////
	// Reading Mode
	///////////////////////////////////////////////////////////////
	//
	protected FFmpegInterface owner;
	//
	protected AtomicBoolean stopThisThread= new AtomicBoolean(true);
	//
	protected AtomicBoolean inputIsOpen= new AtomicBoolean(false);
	protected AtomicBoolean internalLoopIsValid= new AtomicBoolean(false);
	//
	///////////////////////////////////////////////////////////////
	// Audio Format
	///////////////////////////////////////////////////////////////
	//
	protected AtomicReference<AudioFormatBaseAttributes> currentAudioFormat= new AtomicReference<>(null);
	protected int audioFormatChannels;
	protected float audioFormatFrameRate;
	protected float audioFormatSampleRate;
	//
	///////////////////////////////////////////////////////////////
	// Reading Attributes
	///////////////////////////////////////////////////////////////
	//
	protected AtomicBoolean stopAfterSingleReading= new AtomicBoolean(false);
	protected AtomicReference<Double> slowMotionCoefficient= new AtomicReference<>(-1.0);
	protected AtomicInteger outputDebugInformation= new AtomicInteger(0);
	//
	public static long defaultMaximalFrameDelay= 1000;
	public AtomicLong maximalFrameDelay= new AtomicLong(defaultMaximalFrameDelay);
	//
	///////////////////////////////////////////////////////////////
	// Reading Counters
	///////////////////////////////////////////////////////////////
	//
	protected long beginningVideoRecordNumber= 0;
	protected long beginningAudioRecordNumber= 0;
	//
	protected long initialVideoRecordTime_InTimeBaseUnits= -1;
	protected long initialAudioRecordTime_InTimeBaseUnits= -1;
	//
	protected long initialVideoRecordTime_InMilliseconds= -1;
	protected long initialAudioRecordTime_InMilliseconds= -1;
	//
	protected long initialRealTime_InMilliseconds= -1;
	//
	protected long totalNumberOfVideoFrames= 0;
	protected long totalNumberOfAudioFrames= 0;
	protected int copyOfVideoFrameCounter= -1;
	protected int copyOfAudioFrameCounter= -1;
	//
	///////////////////////////////////////////////////////////////
	// Reading Internal Variables
	///////////////////////////////////////////////////////////////
	//
	protected ExtendedFileName extendedFileName;
	//
	protected AVFormatContext pFormatCtx;
	protected boolean hasVideo= false;
	protected boolean hasAudio= false;
	protected int videoStreamNumber= -1;
	protected int audioStreamNumber= -1;
	protected AVStream videoStream;
	protected AVStream audioStream;
	protected FFmpegStreamDefinition videoStreamDefinition;
	protected FFmpegStreamDefinition audioStreamDefinition;
	protected AVCodecParameters pVideoCodecPrm;
	protected AVCodecParameters pAudioCodecPrm;
	protected AVCodec pVideoCodec;
	protected AVCodec pAudioCodec;
	protected AVCodecContext pVideoCodecCtx;
	protected AVCodecContext pAudioCodecCtx;
	protected AVFrame pVideoFrameInitial;
	protected AVFrame pVideoFrameRGB;
	protected AVFrame pAudioFrameInitial;
	protected AVFrame pAudioFrameTmp;
	protected BytePointer imageBuffer;
	protected SwsContext sws_ctx;
	protected SwrContext swr_ctx;
	protected AVPacket packet= new AVPacket();
	//
	///////////////////////////////////////////////////////////////
	// Reading Lock
	///////////////////////////////////////////////////////////////
	//
	protected ReentrantLock lock= new ReentrantLock();
	protected Condition condition= lock.newCondition();
	//
	///////////////////////////////////////////////////////////////
	// Reading Constants
	///////////////////////////////////////////////////////////////
	//
	protected static int internalImageMode= avutil.AV_PIX_FMT_0RGB32;
	//
	protected static int nativeAudioFormat= avutil.AV_SAMPLE_FMT_S16;
	protected static String nativeAudioFormatName= "AV_SAMPLE_FMT_S16";
	//
	protected static int reportCriticalErrorsLevel= 1;
	protected static int reportAdmissibleErrorsLevel= 2;
	protected static int reportWarningsLevel= 3;
	protected static int reportFormatLevel= 4;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegFrameReadingTask(FFmpegInterface buffer) {
		owner= buffer;
		setDaemon(true);
	}
	//
	public void setStopAfterSingleReading(boolean mode) {
		stopAfterSingleReading.set(mode);
	}
	//
	public void setSlowMotionCoefficient(NumericalValue coefficient) {
		if (coefficient != null) {
			slowMotionCoefficient.set(NumericalValueConverters.toDouble(coefficient));
		}
	}
	//
	public static long getDefaultMaximalFrameDelay() {
		return defaultMaximalFrameDelay;
	}
	//
	public long getMaximalFrameDelay() {
		return maximalFrameDelay.get();
	}
	//
	public void setMaximalFrameDelay(long number) {
		maximalFrameDelay.set(number);
	}
	public void setMaximalFrameDelay(IntegerAttribute delay) {
		maximalFrameDelay.set(delay.getValue(getDefaultMaximalFrameDelay()));
	}
	//
	public int getOutputDebugInformation() {
		return outputDebugInformation.get();
	}
	public void setOutputDebugInformation(int value) {
		outputDebugInformation.set(value);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void openReading(ExtendedFileName fileName, int timeout, CharacterSet characterSet, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[][] optionGroups, StaticContext staticContext) {
		if (!inputIsOpen.get()) {
			synchronized (this) {
				if (!inputIsOpen.get()) {
					openInputFile(fileName,timeout,characterSet,formatName,streams,optionGroups,staticContext);
				};
				resetCounters();
			}
		}
	}
	//
	public void activateReading() {
		if (stopThisThread.get()) {
			synchronized (this) {
				if (stopThisThread.get()) {
					stopThisThread.set(false);
					startProcessIfNecessary();
				}
			}
		}
	}
	//
	protected void startProcessIfNecessary() {
		synchronized (this) {
			if (!isAlive()) {
				start();
			};
			notifyAll();
		}
	}
	//
	public void suspendReading() {
		stopThisThread.set(true);
	}
	//
	public boolean isActive() {
		return !stopThisThread.get();
	}
	//
	public boolean isSuspended() {
		return inputIsOpen() && stopThisThread.get();
	}
	//
	public boolean inputIsOpen() {
		return inputIsOpen.get();
	}
	public boolean stopThisThread() {
		return stopThisThread.get();
	}
	public boolean eof() {
		return !inputIsOpen.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void openInputFile(ExtendedFileName fileName, int timeout, CharacterSet characterSet, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[][] optionGroups, StaticContext staticContext) {
		closeReading();
		synchronized (this) {
			inputIsOpen.set(false);
			resetCounters();
			extendedFileName= fileName;
			if (fileName.doesExist(false,timeout,characterSet,staticContext)) {
				String textURL= fileName.getPathOfLocalResource().toString();
				prepareVideoFile(textURL,formatName,streams,optionGroups);
				inputIsOpen.set(true);
			} else {
				throw new FileIsNotFound(fileName.toString());
			}
		}
	}
	//
	protected void resetCounters() {
		synchronized (this) {
			beginningVideoRecordNumber= 0;
			beginningAudioRecordNumber= 0;
			initialVideoRecordTime_InTimeBaseUnits= -1;
			initialAudioRecordTime_InTimeBaseUnits= -1;
			initialVideoRecordTime_InMilliseconds= -1;
			initialAudioRecordTime_InMilliseconds= -1;
			initialRealTime_InMilliseconds= -1;
			totalNumberOfVideoFrames= 0;
			totalNumberOfAudioFrames= 0;
			internalLoopIsValid.set(false);
			copyOfVideoFrameCounter= -1;
			copyOfAudioFrameCounter= -1;
		}
	}
	//
	public void closeReading() {
		suspendReading();
		synchronized (this) {
			inputIsOpen.set(false);
			/*
			if (packet != null) {
				// av_packet_unref:
				// Wipe the packet.
				// Unreference the buffer referenced
				// by the packet and reset the
				// remaining packet fields to their
				// default values.
				// pkt - the packet to be
				// unreferenced.
				av_packet_unref(packet);
				packet= null;
			};
			*/
			if (sws_ctx != null) {
				// Free the swscaler context swsContext.
				// If swsContext is NULL, then does nothing.
				sws_freeContext(sws_ctx);
				sws_ctx= null;
			};
			if (imageBuffer != null) {
				// av_freep(imageBuffer);
				imageBuffer= null;
			};
			if (pVideoFrameInitial != null) {
				// Free the frame and any
				// dynamically allocated objects
				// in it, e.g. extended_data. If
				// the frame is reference counted,
				// it will be unreferenced first.
				av_frame_free(pVideoFrameInitial);
				pVideoFrameInitial= null;
			};
			if (pVideoFrameRGB != null) {
				// Free the frame and any
				// dynamically allocated objects
				// in it, e.g. extended_data. If
				// the frame is reference counted,
				// it will be unreferenced first.
				av_frame_free(pVideoFrameRGB);
				pVideoFrameRGB= null;
			};
			if (pAudioFrameInitial != null) {
				av_frame_free(pAudioFrameInitial);
				pAudioFrameInitial= null;
			};
			if (pAudioFrameTmp != null) {
				av_frame_free(pAudioFrameTmp);
				pAudioFrameTmp= null;
			};
			if (pVideoCodecCtx != null) {
				// Free the codec context and everything
				// associated with it and write NULL to the
				// provided pointer.
				// DO NOT USE IT:
				// IT IS NOT NECESSARY,
				// ALSO IT CRASHES THE PROGRAM UNDER W10
				// avcodec_free_context(pVideoCodecCtx);
				pVideoCodecCtx= null;
			};
			if (pAudioCodecCtx != null) {
				pAudioCodecCtx= null;
			};
			if (pVideoCodec != null) {
				// av_freep(pVideoCodec);
				pVideoCodec= null;
			};
			if (pAudioCodec != null) {
				// av_freep(pAudioCodec);
				pAudioCodec= null;
			};
			if (pVideoCodecPrm != null) {
				// av_freep(pVideoCodecPrm);
				pVideoCodecPrm= null;
			};
			if (pAudioCodecPrm != null) {
				// av_freep(pAudioCodecPrm);
				pAudioCodecPrm= null;
			};
			hasVideo= false;
			hasAudio= false;
			if (videoStream != null) {
				// av_freep(videoStream);
				videoStream= null;
			};
			if (audioStream != null) {
				// av_freep(audioStream);
				audioStream= null;
			};
			if (videoStreamDefinition != null) {
				videoStreamDefinition= null;
			};
			if (audioStreamDefinition != null) {
				audioStreamDefinition= null;
			};
			videoStreamNumber= -1;
			audioStreamNumber= -1;
			if (pFormatCtx != null) {
				// avformat_close_input:
				// Close an opened input AVFormatContext.
				// Free it and all its contents and
				// set *s to NULL.
				avformat_close_input(pFormatCtx);
				pFormatCtx= null;
			};
			if (swr_ctx != null) {
				// Free the given SwrContext and set
				// the pointer to NULL.
				// [in] s - a pointer to a
				// pointer to Swr context.
				swr_free(swr_ctx);
				swr_ctx= null;
			};
			internalLoopIsValid.set(false);
			// System.runFinalization();
			System.gc();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@SuppressWarnings("deprecation")
	protected void prepareVideoFile(String textURL, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[][] optionGroups) {
		// Initialize libavformat and register all the muxers,
		// demuxers and protocols.
		// av_register_all();
		FFmpegTools.initializeFFmpegIfNecessary();
		if (pFormatCtx==null) {
			// Allocate Format I/O context.
			// AVFormatContext extends Pointer.
			// Pointer implements AutoCloseable.
			pFormatCtx= new AVFormatContext(null);
		};
		AVInputFormat inputFormat= null;
		if (formatName != null) {
			// av_find_input_format:
			// Find AVInputFormat based on the short
			// name of the input format.
			inputFormat= av_find_input_format(formatName);
		};
		FFmpegCodecOption[] videoOptions;
		FFmpegCodecOption[] audioOptions;
		if (optionGroups != null && optionGroups.length > 0) {
			videoOptions= optionGroups[0];
			if (optionGroups.length > 1) {
				audioOptions= optionGroups[1];
			} else {
				audioOptions= new FFmpegCodecOption[0];
			}
		} else {
			videoOptions= new FFmpegCodecOption[0];
			audioOptions= new FFmpegCodecOption[0];
		};
		AVDictionary videoAVFormatOpenArguments= FFmpegTools.createAVDistionary(videoOptions);
		AVDictionary audioAVFormatOpenArguments= FFmpegTools.createAVDistionary(audioOptions);
		// avformat_open_input:
		// Open an input stream and read the header.
		// The codecs are not opened. The stream must
		// be closed with avformat_close_input().
		// ps - pointer to user-supplied
		// AVFormatContext (allocated by
		// avformat_alloc_context). May be a pointer to
		// NULL, in which case an AVFormatContext is
		// allocated by this function and written into ps.
		// Note that a user-supplied AVFormatContext will
		// be freed on failure.
		// url - URL of the stream to open.
		// fmt - if non-NULL, this parameter forces
		// a specific input format.
		// Otherwise the format is autodetected.
		// options - a dictionary filled with
		// AVFormatContext and demuxer-private options.
		// On return this parameter will be destroyed and
		// replaced with a dict containing
		// options that were not found. May be NULL.
		// return - 0 on success, a negative AVERROR on
		// failure. Note. If you want to use custom IO,
		// preallocate the format context and set its
		// pb field.
		int fileOpeningFlag= avformat_open_input(pFormatCtx,textURL,inputFormat,videoAVFormatOpenArguments);
		if (fileOpeningFlag < 0) {
			// Couldn't open file.
			throw new FFmpegAVFormatOpenInputError(textURL);
		};
		// Read packets of a media file to get stream information.
		// This is useful for file formats with no headers such
		// as MPEG. This function also computes the real framerate
		// in case of MPEG-2 repeat frame mode. The logical file
		// position is not changed by this function; examined
		// packets may be buffered for later processing.
		int streamInfoFindingFlag= avformat_find_stream_info(pFormatCtx,(PointerPointer)null);
		if (streamInfoFindingFlag < 0) {
			// Couldn't find stream information.
			throw new FFmpegAVFormatFindStreamInfoError(textURL);
		};
		if (reportFormat()) {
			// Print detailed information about the input or output
			// format, such as duration, bitrate, streams, container,
			// programs, metadata, side data, codec, and time base.
			av_dump_format(pFormatCtx,0,textURL,0);
		};
		AVDictionary metadata= pFormatCtx.metadata();
		AVDictionaryEntry entry= null;
		for (int k=0; k <= Integer.MAX_VALUE; k++) {
			entry= av_dict_get(metadata,"",entry,AV_DICT_IGNORE_SUFFIX);
			if (entry==null) {
				break;
			};
			String key= entry.key().getString();
			String value= entry.value().getString();
			if (key.equalsIgnoreCase(FFmpegFrameRecordingTask.metadataTagComment)) {
				owner.setDeliveredDescription(value);
			} else if (key.equalsIgnoreCase(FFmpegFrameRecordingTask.metadataTagCopyright)) {
				owner.setDeliveredCopyright(value);
			} else if (key.equalsIgnoreCase(FFmpegFrameRecordingTask.metadataTagTime)) {
				owner.setDeliveredRegistrationTime(value);
			} else if (key.equalsIgnoreCase(FFmpegFrameRecordingTask.metadataTagDate)) {
				owner.setDeliveredRegistrationDate(value);
			}
		};
		// Find the first video stream:
		videoStreamNumber= -1;
		audioStreamNumber= -1;
		for (int n=0; n < pFormatCtx.nb_streams(); n++) {
			if (pFormatCtx.streams(n).codec().codec_type()==AVMEDIA_TYPE_VIDEO) {
				videoStreamNumber= n;
				break;
			}
		};
		for (int n=0; n < pFormatCtx.nb_streams(); n++) {
			if (pFormatCtx.streams(n).codec().codec_type()==AVMEDIA_TYPE_AUDIO) {
				audioStreamNumber= n;
				break;
			}
		};
		if (videoStreamNumber < 0) {
			// Didn't find a video stream.
			throw new FFmpegCannotFindAVideoStream(textURL);
		};
		// Get a pointer to the codec context for the video stream.
		// AVCodecContext extends Pointer.
		// Pointer implements AutoCloseable.
		videoStream= pFormatCtx.streams(videoStreamNumber);
		if (audioStreamNumber >= 0) {
			audioStream= pFormatCtx.streams(audioStreamNumber);
		};
		pVideoCodecPrm= videoStream.codecpar();
		if (audioStreamNumber >= 0) {
			pAudioCodecPrm= audioStream.codecpar();
		};
		// pVideoCodecCtx= videoStream.codec();
		// Find a registered decoder with a matching codec ID.
		// avcodec extends org.bytedeco.javacpp.presets.avcodec.
		// org.bytedeco.javacpp.presets.avcodec implements InfoMapper.
		pVideoCodec= avcodec_find_decoder(pVideoCodecPrm.codec_id());
		if (audioStreamNumber >= 0) {
			pAudioCodec= avcodec_find_decoder(pAudioCodecPrm.codec_id());
		};
		if (pVideoCodec==null) {
			// Codec not found.
			String codecInfo= "";
			try {
				codecInfo= FFmpegTools.retrieveCodecInformation(videoStream);
			} catch (Throwable e) {
			};
			throw new FFmpegCannotFindCodec(codecInfo);
		};
		if (audioStreamNumber >= 0) {
			if (pAudioCodec==null) {
				// Codec not found.
				String codecInfo= "";
				try {
					codecInfo= FFmpegTools.retrieveCodecInformation(audioStream);
				} catch (Throwable e) {
				};
				throw new FFmpegCannotFindCodec(codecInfo);
			}
		};
		pVideoCodecCtx= videoStream.codec();
		if (audioStreamNumber >= 0) {
			pAudioCodecCtx= audioStream.codec();
		};
		AVDictionary videoAVCodecOpenArguments= FFmpegTools.createAVDistionary(videoOptions);
		AVDictionary audioAVCodecOpenArguments= FFmpegTools.createAVDistionary(videoOptions);
		// avcodec_open2:
		// Initialize the AVCodecContext to use the given
		// AVCodec. Prior to using this function the context
		// has to be allocated with avcodec_alloc_context3().
		// The functions avcodec_find_decoder_by_name(),
		// avcodec_find_encoder_by_name(),
		// avcodec_find_decoder() and avcodec_find_encoder()
		// provide an easy way for retrieving a codec.
		// Warning. This function is not thread safe!
		// Note. Always call this function before using
		// decoding routines (such as \ref
		// avcodec_receive_frame()).
		// avctx - the context to initialize.
		// codec - the codec to open this context for.
		// If a non-NULL codec has been previously passed
		// to avcodec_alloc_context3() or for this context,
		// then this parameter MUST be either NULL or
		// equal to the previously passed codec.
		// options - a dictionary filled with
		// AVCodecContext and codec-private options.
		// On return this object will be filled with
		// options that were not found.
		// return - zero on success, a negative value on
		// error.
		synchronized (FFmpegTools.codecOpeningGuard) {
			int videoCodecOpeningFlag= avcodec_open2(pVideoCodecCtx,pVideoCodec,videoAVCodecOpenArguments);
			if (videoCodecOpeningFlag < 0) {
				String codecInfo= "";
				try {
					codecInfo= FFmpegTools.retrieveCodecInformation(videoStream);
				} catch (Throwable e) {
				};
				throw new FFmpegCodecContextInitializationError(codecInfo);
			};
			if (audioStreamNumber >= 0) {
				int audioCodecOpeningFlag= avcodec_open2(pAudioCodecCtx,pAudioCodec,audioAVCodecOpenArguments);
				if (audioCodecOpeningFlag < 0) {
					String codecInfo= "";
					try {
						codecInfo= FFmpegTools.retrieveCodecInformation(audioStream);
					} catch (Throwable e) {
					};
					throw new FFmpegCodecContextInitializationError(codecInfo);
				}
			}
		};
		for (int n=0; n < streams.length; n++) {
			FFmpegStreamDefinition definition= streams[n];
			if (definition.isVideoStream() && !hasVideo) {
				videoStreamDefinition= definition;
				definition.setReadingAttributes(pVideoCodecCtx,pVideoCodec,null);
				hasVideo= true;
			};
			if (definition.isAudioStream() && !hasAudio) {
				audioStreamDefinition= definition;
				definition.setReadingAttributes(pAudioCodecCtx,pAudioCodec,null);
				hasAudio= true;
			}
		};
		if (pVideoFrameInitial==null) {
			// Allocate an AVFrame and set its fields to
			// default values. The resulting struct must be
			// freed using av_frame_free().
			pVideoFrameInitial= av_frame_alloc();
			if (pVideoFrameInitial==null) {
				throw new FFmpegCannotAllocateAVFrame();
			}
		};
		if (pVideoFrameRGB==null) {
			// Allocate an AVFrame and set its fields to
			// default values. The resulting struct must be
			// freed using av_frame_free().
			pVideoFrameRGB= av_frame_alloc();
			if (pVideoFrameRGB==null) {
				throw new FFmpegCannotAllocateAVFrame();
			}
		};
		if (audioStreamNumber >= 0) {
			int nbSamples= pAudioCodecCtx.frame_size();
			/*
			if ((pAudioCodecCtx.codec().capabilities() & AV_CODEC_CAP_VARIABLE_FRAME_SIZE) != 0) {
				Integer userDefinedNBSamples= outputStreamState.nbSamples;
				if (userDefinedNBSamples != null) {
					nbSamples= userDefinedNBSamples; // 10000;
				} else {
					nbSamples= pAudioCodecCtx.frame_size();
				}
			} else {
				nbSamples= pAudioCodecCtx.frame_size();
			};
			*/
			if (pAudioFrameInitial==null) {
				pAudioFrameInitial= FFmpegTools.allocAudioFrame(
					pAudioCodecCtx.sample_fmt(),
					pAudioCodecCtx.channel_layout(),
					pAudioCodecCtx.sample_rate(),
					nbSamples);
			};
			if (pAudioFrameTmp==null) {
				pAudioFrameTmp= FFmpegTools.allocAudioFrame(
					nativeAudioFormat,
					pAudioCodecCtx.channel_layout(),
					pAudioCodecCtx.sample_rate(),
					nbSamples);
			}
		};
		// Return the size in bytes of the amount of data required to
		// store an image with the given parameters.
		int numBytes= av_image_get_buffer_size(
			internalImageMode,
			pVideoCodecCtx.width(),
			pVideoCodecCtx.height(),
			1); // 32);
		// av_malloc:
		// Allocate a memory block with alignment suitable
		// for all memory accesses (including vectors if
		// available on the CPU).
		// size - size in bytes for the memory block
		// to be allocated.
		// return - pointer to the allocated block, or
		// NULL if the block cannot be allocated.
		Pointer imageByfferBytes= av_malloc(numBytes);
		// BytePointer is the peer class to native pointers and
		// arrays of signed char, including strings.
		imageBuffer= new BytePointer(imageByfferBytes);
		// av_image_fill_arrays:
		// Setup the data pointers and linesizes based on
		// the specified image parameters and the provided
		// array.
		// The fields of the given image are filled in by
		// using the src address which points to the image
		// data buffer. Depending on the specified pixel
		// format, one or multiple image data pointers and
		// line sizes will be set. If a planar format is
		// specified, several pointers will be set pointing
		// to the different picture planes and the line
		// sizes of the different planes will be stored in
		// the lines_sizes array. Call with src == NULL
		// to get the required size for the src buffer.
		// To allocate the buffer and fill in the dst_data
		// and dst_linesize in one call, use av_image_alloc().
		// dst_data - data pointers to be filled in.
		// dst_linesizes - linesizes for the image in
		// dst_data to be filled in.
		// src - buffer which will contain or contains
		// the actual image data, can be NULL.
		// pix_fmt - the pixel format of the image.
		// width - the width of the image in pixels.
		// height - the height of the image in pixels.
		// align - the value used in src for linesize
		// alignment.
		// return - the size in bytes required for src, a
		// negative error code in case of failure.
		int flag1= av_image_fill_arrays(
			pVideoFrameRGB.data(),
			pVideoFrameRGB.linesize(),
			imageBuffer,
			internalImageMode,
			videoStreamDefinition.getDestinationImageWidth(pVideoCodecCtx.width()),
			videoStreamDefinition.getDestinationImageHeight(pVideoCodecCtx.height()),
			1); // 32);
		if (flag1 < 0) {
			throw new FFmpegAVImageFillArraysError();
		};
		// Allocate and return an SwsContext:
		sws_ctx= sws_getContext(
			pVideoCodecCtx.width(),
			pVideoCodecCtx.height(),
			pVideoCodecCtx.pix_fmt(),
			videoStreamDefinition.getDestinationImageWidth(pVideoCodecCtx.width()),
			videoStreamDefinition.getDestinationImageHeight(pVideoCodecCtx.height()),
			internalImageMode,
			SWS_BILINEAR,
			null,
			null,
			(DoublePointer)null);
		if (sws_ctx==null) {
			throw new FFmpegCannotInitializeSWSContext();
		}
		// The AVPacket structure stores compressed data. It is
		// typically exported by demuxers and then passed as input
		// to decoders, or received as output from encoders and
		// then passed to muxers. For video, it should typically
		// contain one compressed frame. For audio it may contain
		// several compressed frames. Encoders are allowed to output
		// empty packets, with no compressed data, containing only
		// side data (e.g. to update some stream parameters at the
		// end of encoding).
		// if (packet==null) {
		//	packet= new AVPacket();
		// }
		if (audioStreamNumber >= 0) {
			// swr_alloc:
			// Allocate SwrContext.
			// If you use this function you will need to set
			// the parameters (manually or with
			// swr_alloc_set_opts()) before calling swr_init().
			// return - NULL on error, allocated context
			// otherwise.
			// Create resampler context:
			swr_ctx= swr_alloc();
			if (swr_ctx==null) {
				throw new FFmpegCannotAllocateSWRContext();
			};
			// av_opt_set_int:
			// Option setting functions
			// Those functions set the field of obj with the
			// given name to value.
			// [in] obj - a struct whose first element
			// is a pointer to an AVClass.
			// [in] name - the name of the field to set.
			// [in] val - the value to set. In case of
			// av_opt_set() if the field is not of a string
			// type, then the given string is parsed. SI
			// postfixes and some named scalars are
			// supported. If the field is of a numeric type,
			// it has to be a numeric or named scalar.
			// Behavior with more than one scalar and +-
			// infix operators is undefined.
			// If the field is of a flags type, it has to be
			// a sequence of numeric scalars or named flags
			// separated by '+' or '-'. Prefixing a flag with
			// '+' causes it to be set without affecting the
			// other flags; similarly, '-' unsets a flag.
			// search_flags - flags passed to
			// av_opt_find2. I.e. if AV_OPT_SEARCH_CHILDREN
			// is passed here, then the option may be set on
			// a child of obj.
			// return - 0 if the value has been set, or an
			// AVERROR code in case of error:
			// AVERROR_OPTION_NOT_FOUND if no matching option
			// exists AVERROR(ERANGE) if the value is out of
			// range AVERROR(EINVAL) if the value is not valid
			// Set options:
			av_opt_set_int(		swr_ctx,	"out_channel_count",	pAudioCodecCtx.channels(),	0);
			av_opt_set_int(		swr_ctx,	"out_sample_rate",	pAudioCodecCtx.sample_rate(),	0);
			av_opt_set_sample_fmt(	swr_ctx,	"out_sample_fmt",	nativeAudioFormat,		0);
			av_opt_set_int(		swr_ctx,	"in_channel_count",	pAudioCodecCtx.channels(),	0);
			av_opt_set_int(		swr_ctx,	"in_sample_rate",	pAudioCodecCtx.sample_rate(),	0);
			av_opt_set_sample_fmt(	swr_ctx,	"in_sample_fmt",	pAudioCodecCtx.sample_fmt(),	0);
			// swr_init:
			// Initialize context after user parameters have
			// been set. Note. The context must be configured
			// using the AVOption API.
			// [in,out] s - SWR context to initialize.
			// return - AVERROR error code in case of failure.
			// Initialize the resampling context:
			int flag2= swr_init(swr_ctx);
			if (flag2 < 0) {
				throw new FFmpegCannotInitializeSWRContext();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void run() {
		while (true) {
			try {
				synchronized (this) {
					if (stopThisThread.get()) {
						wait();
						continue;
					};
					if (inputIsOpen.get()) {
						boolean isEOF= readOnePacket(false);
						if (isEOF) {
							stopThisThread.set(true);
							owner.completeDataReading(computeCurrentVideoFrameNumber());
							continue;
						}
					} else {
						stopThisThread.set(true);
						continue;
					}
				}
			} catch (InterruptedException e) {
				closeReading();
				stopThisThread.set(true);
				owner.completeDataReading(computeCurrentVideoFrameNumber());
				return;
			} catch (Throwable e) {
				if (reportCriticalErrors()) {
					e.printStackTrace();
				}
			}
		}
	}
	//
	protected long computeCurrentVideoFrameNumber() {
		return beginningVideoRecordNumber + totalNumberOfVideoFrames - 1;
	}
	protected long computeCurrentAudioFrameNumber() {
		return beginningAudioRecordNumber + totalNumberOfAudioFrames - 1;
	}
	//
	protected boolean readOnePacket(boolean isDummyReading) throws InterruptedException {
		// The av_read_frame function returns the next frame of
		// a stream. This function returns what is stored in the
		// file, and does not validate that what is there are
		// valid frames for the decoder. It will split what is
		// stored in the file into frames and return one for each
		// call. It will not omit invalid data between valid frames
		// so as to give the decoder the maximum information
		// possible for decoding. If pkt->buf is NULL, then the
		// packet is valid until the next av_read_frame() or until
		// avformat_close_input(). Otherwise the packet is valid
		// indefinitely. In both cases the packet must be freed
		// with av_packet_unref when it is no longer needed. For
		// video, the packet contains exactly one frame. For audio,
		// it contains an integer number of frames if each frame
		// has a known fixed size (e.g. PCM or ADPCM data).
		// If the audio frames have a variable size (e.g. MPEG
		// audio), then it contains one frame.
		int frameReadingFlag= av_read_frame(pFormatCtx,packet);
		if (frameReadingFlag >= 0) {
			try {
				int packetStreamIndex= packet.stream_index();
				if (packetStreamIndex==videoStreamNumber && videoStreamNumber >= 0) {
					return readVideoPacketFrames(isDummyReading);
				} else if (packetStreamIndex==audioStreamNumber && audioStreamNumber >= 0) {
					return readAudioPacketFrames(isDummyReading);
				} else {
					return false;
				}
			} catch (Throwable e) {
				if (reportCriticalErrors()) {
					e.printStackTrace();
				};
				return true;
			} finally {
				av_packet_unref(packet);
			}
		} else if (frameReadingFlag==AVERROR_EOF) {
			return true;
		} else {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected boolean readVideoPacketFrames(boolean isDummyReading) throws InterruptedException {
		boolean isEOF= false;
		// if (packet.stream_index()==videoStreamNumber) {
		// Supply raw packet data as input to a
		// decoder. It returns 0 on success,
		// otherwise a negative error code.
		int flag1= avcodec_send_packet(pVideoCodecCtx,packet);
		if (flag1==AVERROR_EOF) {
			return true;
		} else if (flag1 < 0) {
			return false;
		};
		internalLoopIsValid.set(true);
		AVRational timeBase= videoStream.time_base();
		AVRational averageFrameRate= videoStream.avg_frame_rate();
		while (true) {
			if (stopThisThread.get() && !isDummyReading) {
				wait();
				continue;
			};
			if (!inputIsOpen.get()) {
				stopThisThread.set(true);
				return false;
			};
			if (!internalLoopIsValid.get()) {
				return false;
			};
			// Return decoded output data from a
			// decoder:
			int flag2= avcodec_receive_frame(pVideoCodecCtx,pVideoFrameInitial);
			int updatedFrameNumber= pVideoCodecCtx.frame_number();
			try {
				if (flag2 >= 0) {
					totalNumberOfVideoFrames++;
					if (!isDummyReading) {
///////////////////////////////////////////////////////////////////////
long currentRealTime_InMilliseconds= System.currentTimeMillis();
double coefficient= slowMotionCoefficient.get();
if (coefficient > 0.0) {
	currentRealTime_InMilliseconds= (long)(currentRealTime_InMilliseconds / coefficient);
};
if (initialRealTime_InMilliseconds < 0) {
	initialRealTime_InMilliseconds= currentRealTime_InMilliseconds;
};
long currentVideoRecordTime_InTimeBaseUnits= pVideoFrameInitial.pts();
// sws_scale:
// Scale the image slice in srcSlice and put the resulting scaled slice
// in the image in dst. A slice is a sequence of consecutive rows in an
// image. Slices have to be provided in sequential order, either in
// top-bottom or bottom-top order. If slices are provided in
// non-sequential order the behavior of the function is undefined.
// c - the scaling context previously created with sws_getContext().
// srcSlice - the array containing the pointers to the planes of the
// source slice.
// srcStride - the array containing the strides for each plane of the
// source image.
// srcSliceY - the position in the source image of the slice to
// process, that is the number (counted starting from zero) in the
// image of the first row of the slice.
// srcSliceH - the height of the source slice, that is the number of
// rows in the slice.
// dst - the array containing the pointers to the planes of the
// destination image.
// dstStride the array containing the strides for each plane of the
// destination image.
// return - the height of the output slice.
sws_scale(
	sws_ctx,
	pVideoFrameInitial.data(),
	pVideoFrameInitial.linesize(),
	0,
	pVideoCodecCtx.height(),
	pVideoFrameRGB.data(),
	pVideoFrameRGB.linesize());
long currentVideoRecordTime_InMilliseconds;
if (totalNumberOfVideoFrames <= 1) {
	if (currentVideoRecordTime_InTimeBaseUnits==AV_NOPTS_VALUE) {
		currentVideoRecordTime_InMilliseconds=
			FFmpegTools.computeTimeOfFrameInMilliseconds(
				beginningVideoRecordNumber,
				averageFrameRate);
		currentVideoRecordTime_InTimeBaseUnits=
			FFmpegTools.computeRelativeTime(
				currentVideoRecordTime_InMilliseconds,
				timeBase);
		initialVideoRecordTime_InMilliseconds=
			currentVideoRecordTime_InMilliseconds;
		initialVideoRecordTime_InTimeBaseUnits=
			currentVideoRecordTime_InTimeBaseUnits;
	} else {
		initialVideoRecordTime_InTimeBaseUnits=
			currentVideoRecordTime_InTimeBaseUnits;
		currentVideoRecordTime_InMilliseconds=
			(long)(FFmpegTools.computeTime(
				currentVideoRecordTime_InTimeBaseUnits,
				timeBase)*1000);
		initialVideoRecordTime_InMilliseconds=
			currentVideoRecordTime_InMilliseconds;
	}
} else {
	long realTimeDelta_InMilliseconds= currentRealTime_InMilliseconds - initialRealTime_InMilliseconds;
	if (currentVideoRecordTime_InTimeBaseUnits==AV_NOPTS_VALUE) {
		currentVideoRecordTime_InMilliseconds= FFmpegTools.computeTimeOfFrameInMilliseconds(
			computeCurrentVideoFrameNumber(),
			averageFrameRate);
		currentVideoRecordTime_InTimeBaseUnits=
			FFmpegTools.computeRelativeTime(
				currentVideoRecordTime_InMilliseconds,
				timeBase);
	} else {
		currentVideoRecordTime_InMilliseconds= (long)(FFmpegTools.computeTime(
			currentVideoRecordTime_InTimeBaseUnits,
			timeBase)*1000);
	};
	long recordTimeDelta_InMilliseconds= currentVideoRecordTime_InMilliseconds - initialVideoRecordTime_InMilliseconds;
	long waitingPeriod_InMilliseconds= recordTimeDelta_InMilliseconds - realTimeDelta_InMilliseconds;
	if (	waitingPeriod_InMilliseconds > 0 &&
		waitingPeriod_InMilliseconds <= maximalFrameDelay.get() &&
		!stopAfterSingleReading.get()) {
		try {
			long nanosTimeout= waitingPeriod_InMilliseconds * 1_000_000;
			lock.lock();
			try {
				WaitingLoop: while (true) {
					nanosTimeout= condition.awaitNanos(nanosTimeout);
					if (nanosTimeout <= 0) {
						break WaitingLoop;
					}
				}
			} finally {
				lock.unlock();
			}
		} catch (InterruptedException e) {
		} catch (ThreadDeath e) {
			return true;
		}
	}
};
if (!inputIsOpen.get()) {
	stopThisThread.set(true);
	return false;
};
if (!internalLoopIsValid.get()) {
	return false;
};
if (stopAfterSingleReading.get()) {
	stopThisThread.set(true);
};
useFrame(currentVideoRecordTime_InMilliseconds,currentVideoRecordTime_InTimeBaseUnits,timeBase,averageFrameRate);
continue;
///////////////////////////////////////////////////////////////////////
					}
				} else if (flag2==AVERROR_EOF) {
					isEOF= true;
					break;
				} else if (copyOfVideoFrameCounter==updatedFrameNumber) {
					isEOF= false;
					break;
				} else {
					continue;
				}
			} finally {
				copyOfVideoFrameCounter= updatedFrameNumber;
			}
		};
		return isEOF;
	}
	//
	protected void useFrame(long timeInMilliseconds, long timeInBaseUnits, AVRational timeBase, AVRational averageFrameRate) {
		int width= videoStreamDefinition.getDestinationImageWidth(pVideoCodecCtx.width());
		int height= videoStreamDefinition.getDestinationImageHeight(pVideoCodecCtx.height());
		BytePointer data= pVideoFrameRGB.data(0);
		int rowLength= width * 4;
		int numBytes= height * rowLength;
		byte[] pixels= new byte[numBytes];
		int linesize= pVideoFrameRGB.linesize(0);
		int offset= 0;
		for (int y=0; y < height; y++) {
			data.position(y*linesize).get(pixels,offset,rowLength);
			offset= offset + rowLength;
		};
		ByteBuffer byteBuffer= ByteBuffer.wrap(pixels);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		IntBuffer intBuffer= byteBuffer.asIntBuffer();
		int iArrayLength= width*height;
		int[] iArray= new int[iArrayLength];
		intBuffer.get(iArray);
		java.awt.image.BufferedImage bufferedImage= new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_ARGB);
		bufferedImage.setRGB(0,0,width,height,iArray,0,width);
		owner.sendFFmpegFrame(new FFmpegFrame(bufferedImage,timeInMilliseconds,timeInBaseUnits,timeBase,averageFrameRate,computeCurrentVideoFrameNumber()));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected boolean readAudioPacketFrames(boolean isDummyReading) throws InterruptedException {
		boolean isEOF= false;
		// Supply raw packet data as input to a
		// decoder. It returns 0 on success,
		// otherwise a negative error code.
		int flag1= avcodec_send_packet(pAudioCodecCtx,packet);
		if (flag1==AVERROR_EOF) {
			return true;
		} else if (flag1 < 0) {
			return false;
		};
		internalLoopIsValid.set(true);
		AVRational timeBase= audioStream.time_base();
		AVRational averageFrameRate= audioStream.avg_frame_rate();
		while (true) {
			if (stopThisThread.get() && !isDummyReading) {
				wait();
				continue;
			};
			if (!inputIsOpen.get()) {
				stopThisThread.set(true);
				return false;
			};
			if (!internalLoopIsValid.get()) {
				return false;
			};
			// Return decoded output data from a
			// decoder:
			int flag2= avcodec_receive_frame(pAudioCodecCtx,pAudioFrameInitial);
			int updatedFrameNumber= pAudioCodecCtx.frame_number();
			try {
				if (flag2 >= 0) {
					totalNumberOfAudioFrames++;
					if (!isDummyReading) {
///////////////////////////////////////////////////////////////////////
long currentRealTime_InMilliseconds= System.currentTimeMillis();
double coefficient= slowMotionCoefficient.get();
if (coefficient > 0.0) {
	currentRealTime_InMilliseconds= (long)(currentRealTime_InMilliseconds / coefficient);
};
if (initialRealTime_InMilliseconds < 0) {
	initialRealTime_InMilliseconds= currentRealTime_InMilliseconds;
};
long currentAudioRecordTime_InTimeBaseUnits= pAudioFrameInitial.pts();
// swr_convert:
// Convert audio.
// in and in_count can be set to 0 to flush the last few samples
// out at the end.
// If more input is provided than output space, then the input will
// be buffered.
// You can avoid this buffering by using swr_get_out_samples() to
// retrieve an upper bound on the required number of output samples
// for the given number of input samples. Conversion will run
// directly without copying whenever possible.
// s - allocated Swr context, with parameters set.
// out - output buffers, only the first one need be set in case of
// packed audio.
// out_count - amount of space available for output in samples
// per channel.
// in - input buffers, only the first one need to be set in case
// of packed audio.
// in_count - number of input samples available in one channel.
// return - number of samples output per channel, negative value
// on error.
int flag3= swr_convert(
	swr_ctx,
	pAudioFrameTmp.data(),
	pAudioFrameInitial.nb_samples(),
	pAudioFrameInitial.data(),
	pAudioFrameInitial.nb_samples());
if (flag3 < 0) {
	throw new FFmpegConversionError();
};
long currentAudioRecordTime_InMilliseconds;
if (totalNumberOfAudioFrames <= 1) {
	if (currentAudioRecordTime_InTimeBaseUnits==AV_NOPTS_VALUE) {
		currentAudioRecordTime_InMilliseconds=
			FFmpegTools.computeTimeOfFrameInMilliseconds(
				beginningAudioRecordNumber,
				averageFrameRate);
		currentAudioRecordTime_InTimeBaseUnits=
			FFmpegTools.computeRelativeTime(
				currentAudioRecordTime_InMilliseconds,
				timeBase);
		initialAudioRecordTime_InMilliseconds=
			currentAudioRecordTime_InMilliseconds;
		initialAudioRecordTime_InTimeBaseUnits=
			currentAudioRecordTime_InTimeBaseUnits;
	} else {
		initialAudioRecordTime_InTimeBaseUnits=
			currentAudioRecordTime_InTimeBaseUnits;
		currentAudioRecordTime_InMilliseconds=
			(long)(FFmpegTools.computeTime(
				currentAudioRecordTime_InTimeBaseUnits,
				timeBase)*1000);
		initialAudioRecordTime_InMilliseconds=
			currentAudioRecordTime_InMilliseconds;
	}
} else {
	long realTimeDelta_InMilliseconds= currentRealTime_InMilliseconds - initialRealTime_InMilliseconds;
	if (currentAudioRecordTime_InTimeBaseUnits==AV_NOPTS_VALUE) {
		currentAudioRecordTime_InMilliseconds= FFmpegTools.computeTimeOfFrameInMilliseconds(
			computeCurrentAudioFrameNumber(),
			averageFrameRate);
		currentAudioRecordTime_InTimeBaseUnits=
			FFmpegTools.computeRelativeTime(
				currentAudioRecordTime_InMilliseconds,
				timeBase);
	} else {
		currentAudioRecordTime_InMilliseconds= (long)(FFmpegTools.computeTime(
			currentAudioRecordTime_InTimeBaseUnits,
			timeBase)*1000);
	}
};
if (!inputIsOpen.get()) {
	stopThisThread.set(true);
	return false;
};
if (!internalLoopIsValid.get()) {
	return false;
};
if (stopAfterSingleReading.get()) {
	stopThisThread.set(true);
};
useAudioData(currentAudioRecordTime_InMilliseconds,currentAudioRecordTime_InTimeBaseUnits,timeBase,averageFrameRate);
continue;
///////////////////////////////////////////////////////////////////////
					}
				} else if (flag2==AVERROR_EOF) {
					isEOF= true;
					break;
				} else if (copyOfAudioFrameCounter==updatedFrameNumber) {
					isEOF= false;
					break;
				} else {
					continue;
				}
			} finally {
				copyOfAudioFrameCounter= updatedFrameNumber;
			}
		};
		return isEOF;
	}
	//
	protected void useAudioData(long timeInMilliseconds, long timeInBaseUnits, AVRational timeBase, AVRational averageFrameRate) {
		ShortPointer q= new ShortPointer(pAudioFrameTmp.data(0));
		int nb_samples= pAudioFrameTmp.nb_samples();
		int channels= pAudioCodecCtx.channels();
		// av_samples_get_buffer_size:
		// Get the required buffer size for the given
		// audio parameters.
		// out - linesize calculated linesize, may be NULL.
		// nb_channels - the number of channels.
		// nb_samples - the number of samples in
		// a single channel.
		// sample_fmt - the sample format.
		// align - buffer size alignment (0 = default,
		// 1 = no alignment).
		// return - required buffer size, or negative error
		// code on failure.
		int bufferSize= av_samples_get_buffer_size(
			(IntPointer)null,
			channels,
			nb_samples,
			pAudioCodecCtx.sample_fmt(),
			1);
		short[] shortVector= new short[bufferSize/2];
		q.get(shortVector);
		byte[] sound= new byte[bufferSize];
		ByteBuffer byteBuffer= ByteBuffer.wrap(sound);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		ShortBuffer shortBuffer= byteBuffer.asShortBuffer();
		int index= 0;
		for (int j=0; j < nb_samples; j++) {
			for (int i=0; i < channels; i++) {
				short v= shortVector[index];
				shortBuffer.put(index,v);
				index++;
			}
		};
		AudioFormatBaseAttributes audioFormat= currentAudioFormat.get();
		boolean createNewAudioFormatAttributes= false;
		if (audioFormat==null) {
			createNewAudioFormatAttributes= true;
		} else if (
			audioFormatChannels != audioFormat.getChannels() ||
			audioFormatFrameRate != audioFormat.getFrameRate() ||
			audioFormatSampleRate != audioFormat.getSampleRate()
			) {
			createNewAudioFormatAttributes= true;
		};
		if (createNewAudioFormatAttributes) {
			String encoding= nativeAudioFormatName;
			float frameRate= pAudioCodecCtx.sample_rate();
			int frameSize= 4;
			float sampleRate= pAudioCodecCtx.sample_rate();
			int sampleSizeInBits= 16;
			boolean isBigEndian= false;
			audioFormatChannels= channels;
			audioFormatFrameRate= frameRate;
			audioFormatSampleRate= sampleRate;
			audioFormat= new AudioFormatBaseAttributes(
				channels,
				encoding,
				frameRate,
				frameSize,
				sampleRate,
				sampleSizeInBits,
				isBigEndian);
			currentAudioFormat.set(audioFormat);
		};
		owner.sendFFmpegAudioData(new FFmpegAudioData(
			sound,
			timeInMilliseconds,
			timeInBaseUnits,
			timeBase,
			averageFrameRate,
			audioFormat,
			computeCurrentAudioFrameNumber()));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void seekFrameNumber(long seekTarget) {
		AVRational timeBase= videoStream.time_base();
		long targetTime_InMilliseconds= FFmpegTools.computeTimeOfFrameInMilliseconds(seekTarget,timeBase);
		double targetTime= targetTime_InMilliseconds / 1000.0;
		seekFrame(seekTarget,targetTime,targetTime_InMilliseconds);
	}
	//
	public void seekFrameTime(double targetTime) {
		long targetTime_InMilliseconds= (long)(targetTime * 1000);
		AVRational timeBase= videoStream.time_base();
		long seekTarget= FFmpegTools.computeRelativeTime(targetTime_InMilliseconds,timeBase);
		seekFrame(seekTarget,targetTime,targetTime_InMilliseconds);
	}
	//
	public void seekFrame(long seekTarget, double targetTime, long targetTime_InMilliseconds) {
		// delay:
		// Codec delay.
		// Encoding: Number of frames delay there will be from
		// the encoder input to the decoder output. (we assume
		// the decoder matches the spec)
		// Decoding: Number of frames delay in addition to what
		// a standard decoder as specified in the spec would
		// produce.
		// Video:
		// Number of frames the decoded output will be delayed
		// relative to the encoded input.
		// Audio:
		// For encoding, this field is unused (see
		// initial_padding).
		// For decoding, this is the number of samples the
		// decoder needs to output before the decoder's output
		// is valid. When seeking, you should start decoding
		// this many samples prior to your desired seek point.
		// - encoding: Set by libavcodec.
		// - decoding: Set by libavcodec.
		int delay= pVideoCodecCtx.delay();
		seekTarget= seekTarget - delay;
		boolean state1= stopThisThread.get();
		stopThisThread.set(true);
		synchronized (this) {
			resetCounters();
			// av_seek_frame:
			// Seek to the keyframe at timestamp.
			// 'timestamp' in 'stream_index'.
			// s - media file handle.
			// stream_index - if stream_index is (-1),
			// a default stream is selected, and
			// timestamp is automatically converted
			// from AV_TIME_BASE units to the stream
			// specific time_base.
			// timestamp - timestamp in
			// AVStream.time_base units
			// or, if no stream is specified,
			// in AV_TIME_BASE units.
			// flags - flags which select direction
			// and seeking mode
			// return >= 0 on success.
			// Internal time base represented as integer:
			// public static final int
			// AV_TIME_BASE = 1000000;
			//
			int flag= av_seek_frame(pFormatCtx,videoStreamNumber,seekTarget,0);
			if (flag < 0) {
				throw new FFmpegCannotSeekFrame(extendedFileName.toString(),seekTarget,targetTime);
			};
			// avcodec_flush_buffers:
			// Reset the internal decoder state / flush
			// internal buffers. Should be called
			// e.g. when seeking or when switching to a
			// different stream.
			// Note: when refcounted frames are not used
			// (i.e. avctx->refcounted_frames is 0),
			// this invalidates the frames previously
			// returned from the decoder. When
			// refcounted frames are used, the decoder
			// just releases any references it might
			// keep internally, but the caller's
			// reference remains valid.
			avcodec_flush_buffers(pVideoCodecCtx);
			if (audioStreamNumber >= 0) {
				avcodec_flush_buffers(pAudioCodecCtx);
			};
			owner.resetCounters();
			beginningVideoRecordNumber= seekTarget;
			beginningAudioRecordNumber= 0;
			boolean state2= stopThisThread.get();
			boolean state3= state1 && state2;
			stopThisThread.set(state3);
			notifyAll();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean reportCriticalErrors() {
		return outputDebugInformation.get() >= reportCriticalErrorsLevel;
	}
	public boolean reportAdmissibleErrors() {
		return outputDebugInformation.get() >= reportAdmissibleErrorsLevel;
	}
	public boolean reportWarnings() {
		return outputDebugInformation.get() >= reportWarningsLevel;
	}
	public boolean reportFormat() {
		return outputDebugInformation.get() >= reportFormatLevel;
	}
	//
	public int getReportCriticalErrorsLevel() {
		return reportCriticalErrorsLevel;
	}
	public int getReportAdmissibleErrorsLevel() {
		return reportAdmissibleErrorsLevel;
	}
	public int getReportWarningsLevel() {
		return reportWarningsLevel;
	}
	public int getReportFormatLevel() {
		return reportFormatLevel;
	}
}
