// (c) 2017 IRE RAS Alexei A. Morozov.
// Thanks to Alex Andres for the JavaAV project
// (https://github.com/hoary/JavaAV).
// Thanks to Fabrice Bellard for his libavformat API example
// and all the FFmpeg project team (https://ffmpeg.org).
// Thanks to Stephen Dranger, Martin Bohme, and
// Michael Penkov for tutorials on FFmpeg.
// Thanks to Samuel Audet for the JavaCPP project
// (https://github.com/bytedeco/javacpp).

package morozov.system.ffmpeg;

import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.avcodec.*;
import static org.bytedeco.javacpp.swscale.*;
import org.bytedeco.javacpp.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.ffmpeg.errors.*;
import morozov.system.ffmpeg.interfaces.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ByteOrder;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class FFmpegFrameReadingTask extends Thread {
	//
	protected FFmpegInterface owner;
	//
	protected AtomicBoolean stopThisThread= new AtomicBoolean(true);
	protected AtomicBoolean stopAfterSingleReading= new AtomicBoolean(false);
	protected AtomicReference<Double> slowMotionCoefficient= new AtomicReference<>(-1.0);
	protected AtomicBoolean inputIsOpen= new AtomicBoolean(false);
	protected AtomicBoolean internalLoopIsValid= new AtomicBoolean(false);
	//
	public static long defaultMaximalFrameDelay= 1000;
	public AtomicLong maximalFrameDelay= new AtomicLong(defaultMaximalFrameDelay);
	//
	protected long beginningRecordTime_InMilliseconds= 0;
	protected long beginningRecordNumber= 0;
	//
	protected long initialRecordTime_InTimeBaseUnits= -1;
	protected long initialRecordTime_InMilliseconds= -1;
	protected long initialRealTime_InMilliseconds= -1;
	protected long totalNumberOfFrames= 0;
	protected int copyOfFrameCounter= -1;
	//
	protected int internalImageMode= AV_PIX_FMT_0RGB32;
	//
	protected ExtendedFileName extendedFileName;
	//
	protected AVFormatContext pFormatCtx;
	protected boolean haveVideo= false;
	protected int videoStreamNumber= -1;
	protected AVStream stream;
	protected FFmpegStreamDefinition streamDefinition;
	protected AVCodecParameters pCodecPrm;
	protected AVCodec pCodec;
	protected AVCodecContext pCodecCtx;
	protected AVFrame pFrameInitial;
	protected AVFrame pFrameRGB;
	protected BytePointer imageBuffer;
	protected SwsContext sws_ctx;
	protected AVPacket packet= new AVPacket();
	//
	protected ReentrantLock lock= new ReentrantLock();
	protected Condition condition= lock.newCondition();
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
	///////////////////////////////////////////////////////////////
	//
	public void openReading(ExtendedFileName fileName, int timeout, CharacterSet characterSet, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[] options, StaticContext staticContext) {
		if (!inputIsOpen.get()) {
			synchronized (this) {
				if (!inputIsOpen.get()) {
					openInputFile(fileName,timeout,characterSet,formatName,streams,options,staticContext);
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
		if (!isAlive()) {
			start();
		} else {
			notify();
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
	protected void openInputFile(ExtendedFileName fileName, int timeout, CharacterSet characterSet, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[] options, StaticContext staticContext) {
		closeReading();
		synchronized (this) {
			inputIsOpen.set(false);
			resetCounters();
			extendedFileName= fileName;
			if (fileName.doesExist(false,timeout,characterSet,staticContext)) {
				String textURL= fileName.getPathOfLocalResource().toString();
				prepareVideoFile(textURL,formatName,streams,options);
				inputIsOpen.set(true);
			} else {
				throw new FileIsNotFound(fileName.toString());
			}
		}
	}
	//
	protected void resetCounters() {
		synchronized (this) {
			beginningRecordTime_InMilliseconds= 0;
			beginningRecordNumber= 0;
			initialRecordTime_InTimeBaseUnits= -1;
			initialRecordTime_InMilliseconds= -1;
			initialRealTime_InMilliseconds= -1;
			totalNumberOfFrames= 0;
			internalLoopIsValid.set(false);
			copyOfFrameCounter= -1;
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
				// * @param pkt The packet to be
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
			/**/
			if (pFrameRGB != null) {
				// Free the frame and any
				// dynamically allocated objects
				// in it, e.g. extended_data. If
				// the frame is reference counted,
				// it will be unreferenced first.
				av_frame_free(pFrameRGB);
				pFrameRGB= null;
			};
			if (pFrameInitial != null) {
				// Free the frame and any
				// dynamically allocated objects
				// in it, e.g. extended_data. If
				// the frame is reference counted,
				// it will be unreferenced first.
				av_frame_free(pFrameInitial);
				pFrameInitial= null;
			};
			/**/
			if (pCodecCtx != null) {
				// Free the codec context and everything
				// associated with it and write NULL to the
				// provided pointer.
				// DO NOT USE IT:
				// IT IS NOT NECESSARY,
				// ALSO IT CRASHES THE PROGRAM UNDER W10
				// avcodec_free_context(pCodecCtx);
				pCodecCtx= null;
			};
			if (pCodec != null) {
				// av_freep(pCodec);
				pCodec= null;
			};
			if (pCodecPrm != null) {
				// av_freep(pCodecPrm);
				pCodecPrm= null;
			};
			haveVideo= false;
			if (stream != null) {
				// av_freep(stream);
				stream= null;
			};
			if (streamDefinition != null) {
				streamDefinition= null;
			};
			videoStreamNumber= -1;
			if (pFormatCtx != null) {
				// avformat_close_input:
				// Close an opened input AVFormatContext.
				// Free it and all its contents and
				// set *s to NULL.
				avformat_close_input(pFormatCtx);
				pFormatCtx= null;
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
	protected void prepareVideoFile(String textURL, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[] options) {
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
		AVDictionary avformatOpenArguments= FFmpegTools.createAVDistionary(options);
		// avformat_open_input:
		// Open an input stream and read the header.
		// The codecs are not opened. The stream must
		// be closed with avformat_close_input().
		// @param ps Pointer to user-supplied
		// AVFormatContext (allocated by
		// avformat_alloc_context). May be a pointer to
		// NULL, in which case an AVFormatContext is
		// allocated by this function and written into ps.
		// Note that a user-supplied AVFormatContext will
		// be freed on failure.
		// @param url URL of the stream to open.
		// @param fmt If non-NULL, this parameter forces
		// a specific input format.
		// Otherwise the format is autodetected.
		// @param options A dictionary filled with
		// AVFormatContext and demuxer-private options.
		// On return this parameter will be destroyed and
		// replaced with a dict containing
		// options that were not found. May be NULL.
		// @return 0 on success, a negative AVERROR on
		// failure. Note. If you want to use custom IO,
		// preallocate the format context and set its
		// pb field.
		int fileOpeningFlag= avformat_open_input(pFormatCtx,textURL,inputFormat,avformatOpenArguments);
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
		// Print detailed information about the input or output
		// format, such as duration, bitrate, streams, container,
		// programs, metadata, side data, codec, and time base.
		av_dump_format(pFormatCtx,0,textURL,0);
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
		for (int n=0; n < pFormatCtx.nb_streams(); n++) {
			if (pFormatCtx.streams(n).codec().codec_type()==AVMEDIA_TYPE_VIDEO) {
				videoStreamNumber= n;
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
		stream= pFormatCtx.streams(videoStreamNumber);
		// System.err.printf("Codec: %s\n",FFmpegTools.retrieveCodecInformation(stream));
		pCodecPrm= stream.codecpar();
		// pCodecCtx= stream.codec();
		// Find a registered decoder with a matching codec ID.
		// avcodec extends org.bytedeco.javacpp.presets.avcodec.
		// org.bytedeco.javacpp.presets.avcodec implements InfoMapper.
		pCodec= avcodec_find_decoder(pCodecPrm.codec_id());
		if (pCodec==null) {
			// Codec not found.
			String codecInfo= "";
			try {
				codecInfo= FFmpegTools.retrieveCodecInformation(stream);
			} catch (Throwable e) {
			};
			throw new FFmpegCannotFindCodec(codecInfo);
		};
		pCodecCtx= stream.codec();
		AVDictionary avcodecOpenArguments= FFmpegTools.createAVDistionary(options);
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
		// @param avctx The context to initialize.
		// @param codec The codec to open this context for.
		// If a non-NULL codec has been previously passed
		// to avcodec_alloc_context3() or for this context,
		// then this parameter MUST be either NULL or
		// equal to the previously passed codec.
		// @param options A dictionary filled with
		// AVCodecContext and codec-private options.
		// On return this object will be filled with
		// options that were not found.
		// @return zero on success, a negative value on
		// error.
		synchronized (FFmpegTools.codecOpeningGuard) {
			int codecOpeningFlag= avcodec_open2(pCodecCtx,pCodec,avcodecOpenArguments);
			if (codecOpeningFlag < 0) {
				String codecInfo= "";
				try {
					codecInfo= FFmpegTools.retrieveCodecInformation(stream);
				} catch (Throwable e) {
				};
				throw new FFmpegCodecContextInitializationError(codecInfo);
			}
		};
		for (int n=0; n < streams.length; n++) {
			FFmpegStreamDefinition definition= streams[n];
			if (definition.isVideoStream() && !haveVideo) {
				streamDefinition= definition;
				definition.setReadingAttributes(pCodecCtx,pCodec,null);
				haveVideo= true;
			}
		};
		if (pFrameInitial==null) {
			// Allocate an AVFrame and set its fields to
			// default values. The resulting struct must be
			// freed using av_frame_free().
			pFrameInitial= av_frame_alloc();
			if (pFrameInitial==null) {
				throw new FFmpegCannotAllocateAVFrame();
			}
		};
		if (pFrameRGB==null) {
			pFrameRGB= av_frame_alloc();
			if (pFrameRGB==null) {
				throw new FFmpegCannotAllocateAVFrame();
			}
		};
		// Return the size in bytes of the amount of data required to
		// store an image with the given parameters.
		int numBytes= av_image_get_buffer_size(internalImageMode,pCodecCtx.width(),pCodecCtx.height(),1);
		// av_malloc:
		// Allocate a memory block with alignment suitable
		// for all memory accesses (including vectors if
		// available on the CPU).
		// @param size Size in bytes for the memory block
		// to be allocated
		// @return Pointer to the allocated block, or
		// {@code NULL} if the block cannot be allocated
		Pointer imageByfferBytes= av_malloc(numBytes);
		// BytePointer is the peer class to native pointers and
		// arrays of signed char, including strings.
		imageBuffer= new BytePointer(imageByfferBytes);
		// Setup the data pointers and linesizes based on the
		// specified image parameters and the provided array.
		int flag= av_image_fill_arrays(
			pFrameRGB.data(),
			pFrameRGB.linesize(),
			imageBuffer,
			internalImageMode,
			// pCodecCtx.width(),
			// pCodecCtx.height(),
			streamDefinition.getDestinationImageWidth(pCodecCtx.width()),
			streamDefinition.getDestinationImageHeight(pCodecCtx.height()),
			1);
		if (flag < 0) {
			throw new FFmpegAVImageFillArraysError();
		};
		// Allocate and return an SwsContext.
		sws_ctx= sws_getContext(
			pCodecCtx.width(),
			pCodecCtx.height(),
			pCodecCtx.pix_fmt(),
			streamDefinition.getDestinationImageWidth(pCodecCtx.width()),
			streamDefinition.getDestinationImageHeight(pCodecCtx.height()),
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
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void run() {
		while (true) {
			try {
				synchronized (this) {
					if (stopThisThread.get()) {
						wait();
						continue;
					};
					if (inputIsOpen.get()) {
						boolean isEOF= readOnePacket(pCodecCtx,pFrameInitial,pFrameRGB,sws_ctx,false);
						if (isEOF) {
							// closeReading();
							stopThisThread.set(true);
							owner.completeDataReading(computeCurrentFrameNumber());
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
				owner.completeDataReading(computeCurrentFrameNumber());
				return;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	//
	protected long computeCurrentFrameNumber() {
		return beginningRecordNumber + totalNumberOfFrames - 1;
	}
	//
	protected boolean readOnePacket(AVCodecContext pCodecCtx, AVFrame pFrameInitial, AVFrame pFrameRGB, SwsContext sws_ctx, boolean isDummyReading) throws InterruptedException {
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
				return readPacketFrames(pCodecCtx,pFrameInitial,pFrameRGB,sws_ctx,isDummyReading);
			} catch (Throwable e) {
				e.printStackTrace();
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
	protected boolean readPacketFrames(AVCodecContext pCodecCtx, AVFrame pFrameInitial, AVFrame pFrameRGB, SwsContext sws_ctx, boolean isDummyReading) throws InterruptedException {
		boolean isEOF= false;
		if (packet.stream_index()==videoStreamNumber) {
			// Supply raw packet data as input to a
			// decoder. It returns 0 on success,
			// otherwise a negative error code.
			int ret= avcodec_send_packet(pCodecCtx,packet);
			if (ret==AVERROR_EOF) {
				return true;
			} else if (ret < 0) {
				return false;
			};
			internalLoopIsValid.set(true);
			AVRational timeBase= stream.time_base();
			AVRational averageFrameRate= stream.avg_frame_rate();
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
				// decoder.
				ret= avcodec_receive_frame(pCodecCtx,pFrameInitial);
				int updatedFrameNumber= pCodecCtx.frame_number();
				try {
					if (ret >= 0) {
						totalNumberOfFrames++;
						if (!isDummyReading) {
///////////////////////////////////////////////////////////////////////
long currentRealTime_InMilliseconds= System.currentTimeMillis();
double coefficient= slowMotionCoefficient.get();
if (coefficient > 0.0) {
	currentRealTime_InMilliseconds= (long)(currentRealTime_InMilliseconds / coefficient);
};
long currentRecordTime_InTimeBaseUnits= pFrameInitial.pts();
// Allocate and return an SwsContext. You need it to perform
// scaling/conversion operations using sws_scale().
// This function is to be removed after a saner
// alternative is written.
sws_scale(
	sws_ctx,
	pFrameInitial.data(),
	pFrameInitial.linesize(),
	0,
	pCodecCtx.height(),
	pFrameRGB.data(),
	pFrameRGB.linesize());
long currentRecordTime_InMilliseconds;
if (totalNumberOfFrames <= 1) {
	initialRealTime_InMilliseconds= currentRealTime_InMilliseconds;
	if (currentRecordTime_InTimeBaseUnits==AV_NOPTS_VALUE) {
		currentRecordTime_InMilliseconds=
			FFmpegTools.computeTimeOfFrameInMilliseconds(
				beginningRecordNumber,
				averageFrameRate);
		currentRecordTime_InTimeBaseUnits=
			FFmpegTools.computeRelativeTime(
				currentRecordTime_InMilliseconds,
				timeBase);
		initialRecordTime_InMilliseconds=
			currentRecordTime_InMilliseconds;
		initialRecordTime_InTimeBaseUnits=
			currentRecordTime_InTimeBaseUnits;
	} else {
		initialRecordTime_InTimeBaseUnits=
			currentRecordTime_InTimeBaseUnits;
		currentRecordTime_InMilliseconds=
			(long)(FFmpegTools.computeTime(
				currentRecordTime_InTimeBaseUnits,
				timeBase)*1000);
		initialRecordTime_InMilliseconds=
			currentRecordTime_InMilliseconds;
	}
} else {
	long realTimeDelta_InMilliseconds= currentRealTime_InMilliseconds - initialRealTime_InMilliseconds;
	if (currentRecordTime_InTimeBaseUnits==AV_NOPTS_VALUE || initialRecordTime_InTimeBaseUnits==AV_NOPTS_VALUE) {
		currentRecordTime_InMilliseconds= FFmpegTools.computeTimeOfFrameInMilliseconds(
			computeCurrentFrameNumber(),
			averageFrameRate);
		currentRecordTime_InTimeBaseUnits=
			FFmpegTools.computeRelativeTime(
				currentRecordTime_InMilliseconds,
				timeBase);
	} else {
		currentRecordTime_InMilliseconds= (long)(FFmpegTools.computeTime(
			currentRecordTime_InTimeBaseUnits,
			timeBase)*1000);
	};
	long recordTimeDelta_InMilliseconds= currentRecordTime_InMilliseconds - initialRecordTime_InMilliseconds;
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
useFrame(currentRecordTime_InTimeBaseUnits,timeBase,averageFrameRate);
continue;
///////////////////////////////////////////////////////////////////////
						}
					} else if (ret==AVERROR_EOF) {
						isEOF= true;
						break;
					} else if (copyOfFrameCounter==updatedFrameNumber) {
						isEOF= false;
						break;
					} else {
						continue;
					}
				} finally {
					copyOfFrameCounter= updatedFrameNumber;
				}
			}
		};
		return isEOF;
	}
	//
	protected void useFrame(long time, AVRational timeBase, AVRational averageFrameRate) {
		int width= streamDefinition.getDestinationImageWidth(pCodecCtx.width());
		int height= streamDefinition.getDestinationImageHeight(pCodecCtx.height());
		BytePointer data= pFrameRGB.data(0);
		int rowLength= width * 4;
		int numBytes= height * rowLength;
		byte[] pixels= new byte[numBytes];
		int linesize= pFrameRGB.linesize(0);
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
		owner.sendFFmpegFrame(new FFmpegFrame(bufferedImage,time,timeBase,averageFrameRate,computeCurrentFrameNumber()));
	}
	//
	public void seekFrameNumber(long seekTarget) {
		AVRational timeBase= stream.time_base();
		long targetTime_InMilliseconds= FFmpegTools.computeTimeOfFrameInMilliseconds(seekTarget,timeBase);
		double targetTime= targetTime_InMilliseconds / 1000.0;
		seekFrame(seekTarget,targetTime,targetTime_InMilliseconds);
	}
	//
	public void seekFrameTime(double targetTime) {
		long targetTime_InMilliseconds= (long)(targetTime * 1000);
		AVRational timeBase= stream.time_base();
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
		int delay= pCodecCtx.delay();
		seekTarget= seekTarget - delay;
		boolean state1= stopThisThread.get();
		stopThisThread.set(true);
		synchronized (this) {
			resetCounters();
			// av_seek_frame:
			// Seek to the keyframe at timestamp.
			// 'timestamp' in 'stream_index'.
			// @param s media file handle
			// @param stream_index
			// If stream_index is (-1), a default
			// stream is selected, and timestamp is
			// automatically converted
			// from AV_TIME_BASE units to the stream
			// specific time_base.
			// @param timestamp Timestamp in
			// AVStream.time_base units
			// or, if no stream is specified,
			// in AV_TIME_BASE units.
			// @param flags flags which select direction
			// and seeking mode
			// @return >= 0 on success
			//
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
			avcodec_flush_buffers(pCodecCtx);
			owner.resetCounters();
			beginningRecordTime_InMilliseconds=
				targetTime_InMilliseconds;
			beginningRecordNumber= seekTarget;
			boolean state2= stopThisThread.get();
			boolean state3= state1 && state2;
			stopThisThread.set(state3);
			notify();
		}
	}
}
