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

import morozov.system.*;
import morozov.system.files.*;
import morozov.system.ffmpeg.errors.*;
import morozov.system.ffmpeg.interfaces.*;
import morozov.system.frames.tools.*;

import java.io.PipedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FFmpegFrameRecordingTask extends Thread {
	//
	protected FFmpegInterface owner;
	//
	protected PipedOutputStream pipedOutputStream= new PipedOutputStream();
	protected AudioStreamExport audioStreamExport= new AudioStreamExport(pipedOutputStream);
	//
	protected Object nativeLibraryGuard= new Object();
	protected AtomicBoolean acceptFrames= new AtomicBoolean(false);
	protected AtomicBoolean recordFrames= new AtomicBoolean(false);
	protected AtomicInteger outputDebugInformation= new AtomicInteger(0);
	//
	protected ExtendedFileName extendedFileName;
	//
	protected AtomicLong videoFrameCounter= new AtomicLong(0);
	//
	protected AtomicBoolean haveVideo= new AtomicBoolean(false);
	protected AtomicBoolean haveAudio= new AtomicBoolean(false);
	protected AtomicBoolean encodeVideo= new AtomicBoolean(false);
	protected AtomicBoolean encodeAudio= new AtomicBoolean(false);
	//
	protected FFmpegOutputStreamState videoStreamState;
	protected FFmpegOutputStreamState audioStreamState;
	protected AVCodec audioCodec;
	protected AVCodec videoCodec;
	//
	protected AVFormatContext pFormatCtx;
	protected AVIOContext bytestreamIOContext;
	//
	protected ByteBuffer imageBuffer;
	protected BytePointer imageBufferData;
	//
	protected LinkedList<ExportedData> history= new LinkedList<>();
	protected long frameNumber;
	protected int defaultWriteBufferSize= 100;
	protected AtomicInteger writeBufferSize= new AtomicInteger(defaultWriteBufferSize);
	protected boolean bufferOverflow= false;
	//
	protected long defaultAudioDataDelayCorrection= 0;
	//
	protected AtomicLong audioDataDelayCorrection= new AtomicLong(defaultAudioDataDelayCorrection);
	protected AtomicBoolean automaticFrameRateCorrection= new AtomicBoolean(false);
	//
	protected static int technicalPixelFormat= avutil.AV_PIX_FMT_ARGB;
	protected static int nativeAudioFormat= avutil.AV_SAMPLE_FMT_S16;
	//
	protected static String metadataTagComment= "comment";
	protected static String metadataTagCopyright= "copyright";
	protected static String metadataTagTime= "creation_time";
	protected static String metadataTagDate= "date";
	//
	protected static int reportCriticalErrorsLevel= 1;
	protected static int reportAdmissibleErrorsLevel= 2;
	protected static int reportWarningsLevel= 3;
	//
	protected static final long emergencyTimeout= 1000;
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegFrameRecordingTask(FFmpegInterface o) {
		owner= o;
		setDaemon(true);
		setPriority(Thread.MAX_PRIORITY);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getWriteBufferSize() {
		return writeBufferSize.get();
	}
	public void setWriteBufferSize(int length) {
		writeBufferSize.set(length);
	}
	public void resetWriteBufferSize() {
		writeBufferSize.set(defaultWriteBufferSize);
	}
	//
	public long getDefaultAudioDataDelayCorrection() {
		return defaultAudioDataDelayCorrection;
	}
	//
	public void setAudioDataDelayCorrection(long value) {
		audioDataDelayCorrection.set(value);
	}
	public void setAudioDataDelayCorrection(IntegerAttribute value) {
		audioDataDelayCorrection.set(value.getValue(getDefaultAudioDataDelayCorrection()));
	}
	//
	public void setAutomaticFrameRateCorrection(boolean mode) {
		automaticFrameRateCorrection.set(mode);
	}
	//
	public int getOutputDebugInformation() {
		return outputDebugInformation.get();
	}
	public void setOutputDebugInformation(int value) {
		outputDebugInformation.set(value);
		audioStreamExport.setOutputDebugInformation(value);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void openWriting(ExtendedFileName fileName, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[][] optionGroups, MetadataDescription metadataDescription) {
		startProcessIfNecessary();
		if (!acceptFrames.get()) {
			synchronized (nativeLibraryGuard) {
				openOutputFile(fileName,formatName,streams,optionGroups,metadataDescription);
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
	public boolean outputIsOpen() {
		return acceptFrames.get();
	}
	//
	public boolean eof() {
		return !recordFrames.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void openOutputFile(ExtendedFileName fileName, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[][] optionGroups, MetadataDescription metadataDescription) {
		closeWriting();
		synchronized (nativeLibraryGuard) {
			acceptFrames.set(false);
			recordFrames.set(false);
			extendedFileName= fileName;
			String textURL= fileName.getPathOfLocalResource().toString();
			prepareVideoFile(textURL,formatName,streams,optionGroups,metadataDescription);
			videoFrameCounter.set(0);
			try {
				pipedOutputStream.flush();
			} catch (IOException e) {
				if (reportWarnings()) {
					// ControlPanelBasicProcedures.writeLater(String.format("DataFrameExportingTask:pipedOutputStream:IOException:%s\n",e));
					System.err.printf(String.format("FFmpegFrameRecordingTask:pipedOutputStream:IOException:%s\n",e));
				}
			};
			recordFrames.set(true);
			acceptFrames.set(true);
		}
	}
	//
	public void closeWriting() {
		acceptFrames.set(false);
		flushWriting();
		synchronized (nativeLibraryGuard) {
			recordFrames.set(false);
			completeVideoFile();
			audioCodec= null;
			videoCodec= null;
		}
	}
	//
	public void flushWriting() {
		try {
			while (true) {
				synchronized (history) {
					if (history.isEmpty() || !recordFrames.get()) {
						frameNumber= 0;
						break;
					} else {
						history.wait(emergencyTimeout);
					}
				}
			}
		} catch (InterruptedException e) {
		} catch (ThreadDeath e) {
		} catch (Throwable e) {
			if (reportCriticalErrors()) {
				e.printStackTrace();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@SuppressWarnings("deprecation")
	protected void prepareVideoFile(String fileName, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[][] optionGroups, MetadataDescription metadataDescription) {
		// Initialize libavformat and register all the muxers,
		// demuxers and protocols.
		// av_register_all();
		FFmpegTools.initializeFFmpegIfNecessary();
		// Allocate Format I/O context.
		// AVFormatContext extends Pointer.
		// Pointer implements AutoCloseable.
		pFormatCtx= new AVFormatContext(null);
		// avformat_alloc_output_context2:
		// Allocate an AVFormatContext for an output format.
		// avformat_free_context() can be used to free the
		// context and everything allocated by the framework
		// within it.
		// *ctx - is set to the created format context, or
		// to NULL in case of failure.
		// oformat - format to use for allocating the context,
		// if NULL format_name and filename are used instead.
		// format_name - the name of output format to use for
		// allocating the context, if NULL filename is used
		// instead.
		// filename - the name of the filename to use for
		// allocating the context, may be NULL.
		// return >= 0 in case of success, a negative AVERROR
		// code in case of failure.
		int fileOpeningFlag= avformat_alloc_output_context2(pFormatCtx,null,formatName,fileName);
		if (fileOpeningFlag < 0) {
			// Couldn't open file.
			throw new FFmpegAVFormatOpenOutputError(fileName);
		};
		AVDictionary metadata= new AVDictionary(null);
		int flag1= av_dict_set(metadata,metadataTagComment,metadataDescription.getDescription(),0);
		if (flag1 < 0) {
			throw new FFmpegCannotSetDictionaryEntry(metadataTagComment,metadataDescription.getDescription());
		};
		int flag2= av_dict_set(metadata,metadataTagCopyright,metadataDescription.getCopyright(),0);
		if (flag2 < 0) {
			throw new FFmpegCannotSetDictionaryEntry(metadataTagCopyright,metadataDescription.getCopyright());
		};
		int flag3= av_dict_set(metadata,metadataTagTime,metadataDescription.getRegistrationTime(),0);
		if (flag3 < 0) {
			throw new FFmpegCannotSetDictionaryEntry(metadataTagTime,metadataDescription.getRegistrationTime());
		};
		int flag4= av_dict_set(metadata,metadataTagDate,metadataDescription.getRegistrationDate(),0);
		if (flag4 < 0) {
			throw new FFmpegCannotSetDictionaryEntry(metadataTagDate,metadataDescription.getRegistrationDate());
		};
		// metadata:
		// Metadata that applies to the whole file.
		// - demuxing: set by libavformat in
		// avformat_open_input().
		// - muxing: may be set by the caller before
		// avformat_write_header().
		// Freed by libavformat in avformat_free_context().
		pFormatCtx.metadata(metadata);
		// oformat:
		// The output container format. Muxing only, must be set by
		// the caller before avformat_write_header().
		AVOutputFormat outputFormat= pFormatCtx.oformat();
		// Add the audio and video streams using the default
		// format codecs and initialize the codecs.
		haveVideo.set(false);
		haveAudio.set(false);
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
		for (int n=0; n < streams.length; n++) {
			FFmpegStreamDefinition streamDefinition= streams[n];
			if (streamDefinition.isVideoStream() && (outputFormat.video_codec()!=AV_CODEC_ID_NONE) && !haveVideo.get()) {
				videoStreamState= new FFmpegOutputStreamState();
				videoCodec= addStream(streamDefinition,videoStreamState,pFormatCtx,outputFormat.video_codec(),videoOptions);
				haveVideo.set(true);
				encodeVideo.set(true);
			} else if (streamDefinition.isAudioStream() && (outputFormat.audio_codec()!=AV_CODEC_ID_NONE) && !haveAudio.get()) {
				audioStreamState= new FFmpegOutputStreamState();
				audioCodec= addStream(streamDefinition,audioStreamState,pFormatCtx,outputFormat.audio_codec(),audioOptions);
				haveAudio.set(true);
				encodeAudio.set(true);
			}
		};
		// Now that all the parameters are set, we can open
		// the audio and video codecs and allocate the necessary
		// encode buffers.
		if (haveVideo.get()) {
			openVideo(videoCodec,videoStreamState,videoOptions);
		};
		if (haveAudio.get()) {
			int audioFrameSize= openAudio(audioCodec,audioStreamState,audioOptions);
			audioStreamExport.setAVFormatContext(pFormatCtx);
			audioStreamExport.setFFmpegOutputStreamState(audioStreamState);
			audioStreamExport.setAudioFrameSize(audioFrameSize);
			audioStreamExport.resetAudioFrameCounter();
		};
		// av_dump_format:
		// Print detailed information about the input or output
		// format, such as duration, bitrate, streams,
		// container, programs, metadata, side data, codec and
		// time base.
		// ic - the context to analyze.
		// index - index of the stream to dump
		// information about.
		// url - the URL to print, such as source or
		// destination file.
		// is_output - select whether the specified
		// context is an input(0) or output(1).
		av_dump_format(pFormatCtx,0,fileName,1);
		// Open the output file, if needed:
		// flags:
		// can use flags: AVFMT_NOFILE, AVFMT_NEEDNUMBER,
		// AVFMT_GLOBALHEADER, AVFMT_NOTIMESTAMPS,
		// AVFMT_VARIABLE_FPS, AVFMT_NODIMENSIONS,
		// AVFMT_NOSTREAMS, AVFMT_ALLOW_FLUSH,
		// AVFMT_TS_NONSTRICT, AVFMT_TS_NEGATIVE
		if ((outputFormat.flags() & AVFMT_NOFILE) == 0) {
			// avio_open:
			// Create and initialize a AVIOContext for
			// accessing the resource indicated by url.
			// Note: When the resource indicated by url
			// has been opened in read+write mode,
			// the AVIOContext can be used only for
			// writing.
			// s - used to return the pointer to
			// the created AVIOContext.
			// In case of failure the pointed to value
			// is set to NULL.
			// url - resource to access.
			// flags - flags which control how the
			// resource indicated by url is to be opened.
			// return >= 0 in case of success, a negative
			// value corresponding to an AVERROR code in
			// case of failure.
			//
			// pb:
			// I/O context.
			// - demuxing: either set by the user before
			// avformat_open_input() (then the user must
			// close it manually) or set by
			// avformat_open_input().
			// - muxing: set by the user before
			// avformat_write_header(). The caller must
			// take care of closing / freeing the IO
			// context. Do NOT set this field if
			// AVFMT_NOFILE flag is set
			// in iformat/oformat.flags. In such a case,
			// the (de)muxer will handle I/O in some other
			// way and this field will be NULL.
			//
			// AVIOContext:
			// Bytestream IO Context.
			// New fields can be added to the end with
			// minor version bumps. Removal, reordering
			// and changes to existing fields require a
			// major version bump. sizeof(AVIOContext)
			// must not be used outside libav*.
			// Note: None of the function pointers in
			// AVIOContext should be called directly, they
			// should only be set by the client application
			// when implementing custom I/O. Normally these
			// are set to the function pointers specified
			// in avio_alloc_context()
			bytestreamIOContext= new AVIOContext(null);
			int openingFlag= avio_open(bytestreamIOContext,fileName,AVIO_FLAG_WRITE);
			if (openingFlag < 0) {
				// av_err2str:
				// Fill the provided buffer with
				// a string containing an error string
				// corresponding to the AVERROR code
				// errnum.
				throw new FFmpegAVIOOpenError(fileName,FFmpegTools.av_err2str(openingFlag));
			};
			pFormatCtx.pb(bytestreamIOContext);
		};
		// avformat_write_header:
		// Allocate the stream private data and write the
		// stream header to an output media file.
		// s - Media file handle, must be allocated with
		// avformat_alloc_context().
		// Its oformat field must be set to the desired output
		// format; Its pb field must be set to an already
		// opened AVIOContext.
		// options - An AVDictionary filled with
		// AVFormatContext and muxer-private options.
		// On return this parameter will be destroyed and
		// replaced with a dict containing options that were
		// not found. May be NULL.
		// return - AVSTREAM_INIT_IN_WRITE_HEADER on success if
		// the codec had not already been fully initialized in
		// avformat_init, AVSTREAM_INIT_IN_INIT_OUTPUT on
		// success if the codec had already been fully
		// initialized in avformat_init, negative AVERROR on
		// failure.
		// Write the stream header, if any:
		int writingFlag= avformat_write_header(pFormatCtx,FFmpegTools.createAVDistionary(videoOptions));
		if (writingFlag < 0) {
			// av_err2str:
			// Fill the provided buffer with a string
			// containing an error string corresponding
			// to the AVERROR code errnum.
			throw new FFmpegHeaderWritingError(fileName,FFmpegTools.av_err2str(writingFlag));
		}
	}
	//
	// Add an output stream:
	//
	protected static AVCodec addStream(FFmpegStreamDefinition streamDefinition, FFmpegOutputStreamState outputStreamState, AVFormatContext oc, int codecIdentifier, FFmpegCodecOption[] options) {
		// Find the encoder.
		// avcodec_find_encoder:
		// Find a registered encoder with a matching codec ID.
		// id - AVCodecID of the requested encoder.
		// return - an encoder if one was found, NULL otherwise.
		AVCodec codec= avcodec_find_encoder(codecIdentifier);
		if (codec==null) {
			// avcodec_get_name:
			// Get the name of a codec.
			// return - a static string identifying
			// the codec; never NULL.
			throw new FFmpegCannotFindCodec(avcodec_get_name(codecIdentifier).getString());
		};
		// avformat_new_stream:
		// Add a new stream to a media file.
		// When demuxing, it is called by the demuxer in
		// read_header(). If the flag AVFMTCTX_NOHEADER
		// is set in s.ctx_flags, then it may also be
		// called in read_packet().
		// When muxing, should be called by the user before
		// avformat_write_header(). User is required to call
		// avcodec_close() and avformat_free_context() to
		// clean up the allocation by avformat_new_stream().
		// s - media file handle.
		// c - if non-NULL, the AVCodecContext corresponding
		// to the new stream will be initialized to use this
		// codec. This is needed for e.g. codec-specific
		// defaults to be set, so codec should be provided
		// if it is known.
		// return - newly created stream or NULL on error.
		AVStream newStream= avformat_new_stream(oc,null);
		if (newStream==null) {
			throw new FFmpegCannotAllocateAVStream();
		};
		outputStreamState.st= newStream;
		// id:
		// Format-specific stream ID.
		// decoding: set by libavformat
		// encoding: set by the user, replaced by libavformat
		// if left unset
		//
		// nb_streams:
		// Number of elements in AVFormatContext.streams.
		// Set by avformat_new_stream(), must not be modified
		// by any other code.
		newStream.id(oc.nb_streams()-1);
		// avcodec_alloc_context3:
		// Allocate an AVCodecContext and set its fields to
		// default values. The resulting struct should be
		// freed with avcodec_free_context().
		// codec - if non-NULL, allocate private data
		// and initialize defaults for the given codec. It
		// is illegal to then call avcodec_open2() with a
		// different codec. If NULL, then the codec-specific
		// defaults won't be initialized, which may result in
		// suboptimal default settings (this is important
		// mainly for encoders, e.g. libx264).
		// return - an AVCodecContext filled with default
		// values or NULL on failure.
		AVCodecContext c= avcodec_alloc_context3(codec);
		if (c==null) {
			throw new FFmpegCannotAllocateAVCodecContext();
		};
		outputStreamState.enc= c;
		streamDefinition.setRecordingAttributes(c,codec,outputStreamState);
		return codec;
	}
	//
	@SuppressWarnings("deprecation")
	protected static void openVideo(AVCodec videoCodec, FFmpegOutputStreamState outputStreamState, FFmpegCodecOption[] options) {
		AVCodecContext c= outputStreamState.enc;
		AVDictionary arguments= FFmpegTools.createAVDistionary(options);
		// avcodec_open2:
		// Initialize the AVCodecContext to use the given
		// AVCodec. Prior to using this function the context
		// has to be allocated with avcodec_alloc_context3().
		// The functions avcodec_find_decoder_by_name(),
		// avcodec_find_encoder_by_name(),
		// avcodec_find_decoder() and avcodec_find_encoder()
		// provide an easy way for retrieving a codec.
		// Warning: This function is not thread safe!
		// Note: Always call this function before using
		// decoding routines (such as avcodec_receive_frame()).
		// avctx - the context to initialize.
		// codec - the codec to open this context for.
		// If a non-NULL codec has been previously passed to
		// avcodec_alloc_context3() or for this context, then
		// this parameter MUST be either NULL or equal to the
		// previously passed codec.
		// options - a dictionary filled with
		// AVCodecContext and codec-private options. On return
		// this object will be filled with options that were
		// not found.
		// return - zero on success, a negative value on error.
		// Open the codec:
		synchronized (FFmpegTools.codecOpeningGuard) {
			int flag1= avcodec_open2(c,videoCodec,arguments);
			if (flag1 < 0) {
				// av_err2str:
				// Fill the provided buffer with a string
				// containing an error string corresponding
				// to the AVERROR code errnum.
				throw new FFmpegCodecContextInitializationError(FFmpegTools.av_err2str(flag1));
			}
		};
		// Allocate and init a re-usable frame:
		outputStreamState.frame= allocPicture(
			c.pix_fmt(),
			c.width(),
			c.height());
		if (outputStreamState.frame==null) {
			throw new FFmpegCannotAllocateAVFrame();
		};
		// If the output format is not YUV420P, then a
		// temporary YUV420P picture is needed too. It is
		// then converted to the required output format.
		outputStreamState.tmp_frame= null;
		// outputStreamState.tmp_picture= null;
		// if (c.pix_fmt() != AV_PIX_FMT_YUV420P) {
		// outputStreamState.tmp_frame= allocPicture(AV_PIX_FMT_YUV420P,c.width(),c.height());
		int temporaryImageWidth= outputStreamState.getSourceImageWidth(c.width());
		int temporaryImageHeight= outputStreamState.getSourceImageHeight(c.height());
		outputStreamState.tmp_frame= allocPicture(
			technicalPixelFormat,
			temporaryImageWidth,
			temporaryImageHeight);
		if (outputStreamState.tmp_frame==null) {
			throw new FFmpegCannotAllocateAVFrame();
		};
		// codecpar:
		// Codec parameters associated with this stream.
		// Allocated and freed by libavformat in
		// avformat_new_stream() and
		// avformat_free_context() respectively.
		// - demuxing: filled by libavformat on stream
		// creation or in avformat_find_stream_info().
		// - muxing: filled by the caller before
		// avformat_write_header().
		//
		// avcodec_parameters_from_context:
		// Fill the parameters struct based on the values
		// from the supplied codec context. Any allocated
		// fields in par are freed and replaced with
		// duplicates of the corresponding fields in codec.
		// return >= 0 on success, a negative AVERROR
		// code on failure.
		// Copy the stream parameters to the muxer:
		int flag2= avcodec_parameters_from_context(outputStreamState.st.codecpar(),c);
		if (flag2 < 0) {
			throw new FFmpegCannotCopyStreamParameters();
		}
	}
	//
	protected static AVFrame allocPicture(int pixelFormat, int width, int height) {
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
		AVFrame picture= av_frame_alloc();
		if (picture==null) {
			return null;
		};
		// format:
		// format of the frame, -1 if unknown or unset
		// Values correspond to enum AVPixelFormat for video
		// frames, enum AVSampleFormat for audio)
		picture.format(pixelFormat);
		// width, height:
		// width and height of the video frame
		picture.width(width);
		picture.height(height);
		// av_frame_get_buffer:
		// Allocate new buffer(s) for audio or video data.
		// The following fields must be set on frame before
		// calling this function:
		// - format (pixel format for video, sample format
		// for audio);
		// - width and height for video;
		// - nb_samples and channel_layout for audio.
		// This function will fill AVFrame.data and
		// AVFrame.buf arrays and, if necessary, allocate
		// and fill AVFrame.extended_data and
		// AVFrame.extended_buf.
		// For planar formats, one buffer will be allocated
		// for each plane.
		// Warning: if frame already has been allocated,
		// calling this function will leak memory.
		// In addition, undefined behavior can occur in
		// certain cases.
		// frame - frame in which to store the new
		// buffers.
		// align - required buffer size alignment.
		// return - 0 on success, a negative AVERROR on
		// error.
		// Allocate the buffers for the frame data:
		int flag= av_frame_get_buffer(picture,32); // 64);
		if (flag < 0) {
			throw new FFmpegCannotAllocateFrameDataBuffers();
		};
		return picture;
	}
	//
	protected static int openAudio(AVCodec audioCodec, FFmpegOutputStreamState outputStreamState, FFmpegCodecOption[] options) {
		AVCodecContext c= outputStreamState.enc;
		AVDictionary arguments= FFmpegTools.createAVDistionary(options);
		// avcodec_open2:
		// Initialize the AVCodecContext to use the given
		// AVCodec. Prior to using this function the context
		// has to be allocated with avcodec_alloc_context3().
		// The functions avcodec_find_decoder_by_name(),
		// avcodec_find_encoder_by_name(),
		// avcodec_find_decoder() and avcodec_find_encoder()
		// provide an easy way for retrieving a codec.
		// Warning: This function is not thread safe!
		// Note: Always call this function before using
		// decoding routines (such as avcodec_receive_frame()).
		// avctx - the context to initialize.
		// codec - the codec to open this context for.
		// If a non-NULL codec has been previously passed
		// to avcodec_alloc_context3() or for this context,
		// then this parameter MUST be either NULL or equal
		// to the previously passed codec.
		// options - a dictionary filled with
		// AVCodecContext and codec-private options. On
		// return this object will be filled with options
		// that were not found.
		// return = zero on success, a negative value on
		// error.
		// Open the codec:
		synchronized (FFmpegTools.codecOpeningGuard) {
			int flag1= avcodec_open2(c,audioCodec,arguments);
			if (flag1 < 0) {
				// av_err2str:
				// Fill the provided buffer with a string
				// containing an error string corresponding
				// to the AVERROR code errnum.
				throw new FFmpegCodecContextInitializationError(FFmpegTools.av_err2str(flag1));
			}
		};
		// Init signal generator:
		// outputStreamState.t= 0;
		// outputStreamState.tincr= (float)(2 * StrictMath.PI * 110.0 / c.sample_rate());
		// Increment frequency by 110 Hz per second:
		// outputStreamState.tincr2= (float)(2 * StrictMath.PI * 110.0 / c.sample_rate() / c.sample_rate());
		// codec:
		// public native AVCodec codec();
		// capabilities:
		// Codec capabilities.
		int nbSamples;
		if ((c.codec().capabilities() & AV_CODEC_CAP_VARIABLE_FRAME_SIZE) != 0) {
			Integer userDefinedNBSamples= outputStreamState.nbSamples;
			if (userDefinedNBSamples != null) {
				nbSamples= userDefinedNBSamples; // 10000;
			} else {
				nbSamples= c.frame_size();
			}
		} else {
			nbSamples= c.frame_size();
		};
		// channel_layout:
		// Audio channel layout.
		// - encoding: set by user.
		// - decoding: set by user, may be overwritten by
		// libavcodec.
		outputStreamState.frame= FFmpegTools.allocAudioFrame(
			c.sample_fmt(),
			c.channel_layout(),
			c.sample_rate(),
			nbSamples);
		outputStreamState.tmp_frame= FFmpegTools.allocAudioFrame(
			nativeAudioFormat,
			c.channel_layout(),
			c.sample_rate(),
			nbSamples);
		// avcodec_parameters_from_context:
		// Fill the parameters struct based on the values
		// from the supplied codec context. Any allocated
		// fields in par are freed and replaced with
		// duplicates of the corresponding fields in codec.
		// return >= 0 on success, a negative AVERROR code
		// on failure.
		// Copy the stream parameters to the muxer:
		int flag2= avcodec_parameters_from_context(outputStreamState.st.codecpar(),c);
		if (flag2 < 0) {
			throw new FFmpegCannotCopyStreamParameters();
		};
		// swr_alloc:
		// Allocate SwrContext.
		// If you use this function you will need to set
		// the parameters (manually or with
		// swr_alloc_set_opts()) before calling swr_init().
		// return - NULL on error, allocated context
		// otherwise.
		// Create resampler context:
		outputStreamState.swr_ctx= swr_alloc();
		if (outputStreamState.swr_ctx==null) {
			throw new FFmpegCannotAllocateSWRContext();
		};
		// av_opt_set_int:
		// Option setting functions
		// Those functions set the field of obj with the given
		// name to value.
		// [in] obj - a struct whose first element is
		// a pointer to an AVClass.
		// [in] name - the name of the field to set.
		// [in] val - the value to set. In case of
		// av_opt_set() if the field is not of a string type,
		// then the given string is parsed. SI postfixes and
		// some named scalars are supported.
		// If the field is of a numeric type, it has to be
		// a numeric or named scalar. Behavior with more than
		// one scalar and +- infix operators is undefined.
		// If the field is of a flags type, it has to be a
		// sequence of numeric scalars or named flags
		// separated by '+' or '-'. Prefixing a flag with
		// '+' causes it to be set without affecting the
		// other flags; similarly, '-' unsets a flag.
		// search_flags - flags passed to av_opt_find2.
		// I.e. if AV_OPT_SEARCH_CHILDREN is passed here,
		// then the option may be set on a child of obj.
		// return - 0 if the value has been set, or an
		// AVERROR code in case of error:
		// AVERROR_OPTION_NOT_FOUND if no matching option
		// exists AVERROR(ERANGE) if the value is out of
		// range AVERROR(EINVAL) if the value is not valid
		// Set options:
		av_opt_set_int(		outputStreamState.swr_ctx,	"in_channel_count",	c.channels(),		0);
		av_opt_set_int(		outputStreamState.swr_ctx,	"in_sample_rate",	c.sample_rate(),	0);
		av_opt_set_sample_fmt(	outputStreamState.swr_ctx,	"in_sample_fmt",	nativeAudioFormat,	0);
		av_opt_set_int(		outputStreamState.swr_ctx,	"out_channel_count",	c.channels(),		0);
		av_opt_set_int(		outputStreamState.swr_ctx,	"out_sample_rate",	c.sample_rate(),	0);
		av_opt_set_sample_fmt(	outputStreamState.swr_ctx,	"out_sample_fmt",	c.sample_fmt(),		0);
		// swr_init:
		// Initialize context after user parameters have been
		// set. Note. The context must be configured using
		// the AVOption API.
		// [in,out] s - SWR context to initialize.
		// return - AVERROR error code in case of failure.
		// Initialize the resampling context:
		int flag3= swr_init(outputStreamState.swr_ctx);
		if (flag3 < 0) {
			throw new FFmpegCannotInitializeSWRContext();
		};
		return outputStreamState.frame.linesize(0);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void completeVideoFile() {
		if (videoStreamState != null) {
			if (haveVideo.get()) {
				writeAVFrame(null,pFormatCtx,videoStreamState,videoFrameCounter);
			}
		};
		if (audioStreamState != null) {
			if (haveAudio.get()) {
				audioStreamExport.stopDataTransfer();
				audioStreamExport.writeEOFAudioFrame();
			}
		};
		// av_write_trailer:
		// Write the stream trailer to an output media file
		// and free the file private data.
		// May only be called after a successful call to
		// avformat_write_header.
		// s - media file handle.
		// return  - 0 if OK, AVERROR_xxx on error.
		//
		// Write the trailer, if any. The trailer must be
		// written before you close the CodecContexts open
		// when you wrote the header; otherwise
		// av_write_trailer() may try to use memory that was
		// freed on av_codec_close().
		if (pFormatCtx != null) {
			av_write_trailer(pFormatCtx);
		};
		// Close each codec:
		if (videoStreamState != null) {
			if (haveVideo.get()) {
				closeStream(videoStreamState);
			};
			videoStreamState= null;
		};
		if (audioStreamState != null) {
			if (haveAudio.get()) {
				closeStream(audioStreamState);
			};
			audioStreamState= null;
		};
		// if (bytestreamIOContext != null) {
		//	// avio_flush:
		//	// Force flushing of buffered data.
		//	// For write streams, force the buffered
		//	// data to be immediately written to the
		//	// output, without to wait to fill the
		//	// internal buffer.
		//	// For read streams, discard all currently
		//	// buffered data, and advance the reported
		//	// file position to that of the underlying
		//	// stream. This does not read new data,
		//	// and does not perform any seeks.
		//	avio_flush(bytestreamIOContext);
		// };
		if (pFormatCtx != null) {
			// flags:
			// can use flags: AVFMT_NOFILE,
			// AVFMT_NEEDNUMBER, AVFMT_GLOBALHEADER,
			// AVFMT_NOTIMESTAMPS, AVFMT_VARIABLE_FPS,
			// AVFMT_NODIMENSIONS, AVFMT_NOSTREAMS,
			// AVFMT_ALLOW_FLUSH, AVFMT_TS_NONSTRICT,
			// AVFMT_TS_NEGATIVE
			AVOutputFormat outputFormat= pFormatCtx.oformat();
			if ((outputFormat.flags() & AVFMT_NOFILE) == 0) {
				// pb:
				// I/O context.
				// - demuxing: either set by the user
				// before avformat_open_input() (then
				// the user must close it manually) or
				// set by avformat_open_input().
				// - muxing: set by the user before
				// avformat_write_header(). The caller
				// must take care of closing / freeing
				// the IO context. Do NOT set this
				// field if AVFMT_NOFILE flag is set
				// in iformat/oformat.flags. In such a
				// case, the (de)muxer will handle I/O
				// in some other way and this field
				// will be NULL.
				//
				// avio_closep:
				// Close the resource accessed by the
				// AVIOContext s and free it. This
				// function can only be used if s was
				// opened by avio_open().
				// The internal buffer is automatically
				// flushed before closing the resource.
				// return - 0 on success, an
				// AVERROR < 0 on error.
				// Close the output file:
				avio_closep(pFormatCtx.pb());
			};
			// avformat_free_context:
			// Free an AVFormatContext and all its streams.
			// s - context to free.
			// Free the stream:
			avformat_free_context(pFormatCtx);
			pFormatCtx= null;
			bytestreamIOContext= null;
			imageBuffer= null;
			imageBufferData= null;
			System.runFinalization();
			System.gc();
		}
	}
	//
	protected static void closeStream(FFmpegOutputStreamState outputStreamState) {
		if (outputStreamState==null) {
			return;
		};
		// avcodec_flush_buffers:
		// Reset the internal decoder state / flush internal
		// buffers. Should be called e.g. when seeking or when
		// switching to a different stream.
		// Note: when refcounted frames are not used (i.e.
		// avctx->refcounted_frames is 0), this invalidates
		// the frames previously returned from the decoder.
		// When refcounted frames are used, the decoder just
		// releases any references it might keep internally,
		// but the caller's reference remains valid.
		avcodec_flush_buffers(outputStreamState.enc);
		// avcodec_free_context:
		// Free the codec context and everything associated
		// with it and write NULL to the provided pointer.
		avcodec_free_context(outputStreamState.enc);
		outputStreamState.enc= null;
		// av_frame_free:
		// Free the frame and any dynamically allocated
		// objects in it, e.g. extended_data. If the frame
		// is reference counted, it will be unreferenced
		// first.
		// frame - frame to be freed. The pointer will
		// be set to NULL.
		av_frame_free(outputStreamState.frame);
		outputStreamState.frame= null;
		av_frame_free(outputStreamState.tmp_frame);
		outputStreamState.tmp_frame= null;
		// outputStreamState.tmp_picture= null;
		// sws_freeContext:
		// Free the swscaler context swsContext.
		// If swsContext is NULL, then does nothing.
		sws_freeContext(outputStreamState.sws_ctx);
		outputStreamState.sws_ctx= null;
		// swr_free:
		// Free the given SwrContext and set the
		// pointer to NULL.
		// [in] s - a pointer to a pointer to Swr
		// context.
		if (outputStreamState.swr_ctx != null) {
			swr_free(outputStreamState.swr_ctx);
			outputStreamState.swr_ctx= null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@SuppressWarnings("CallToThreadDumpStack")
	protected void writeFrame(ExportedData frame) {
		if (frame instanceof ExportedVideoFrame) {
			ExportedVideoFrame videoFrame = (ExportedVideoFrame)frame;
			writeBufferedImage(videoFrame);
		} else if (frame instanceof ExportedAudioData) {
			if (haveAudio.get()) {
				ExportedAudioData audioData= (ExportedAudioData)frame;
				try {
					pipedOutputStream.write(audioData.getAudioData());
				} catch (IOException e) {
					if (reportCriticalErrors()) {
						// ControlPanelBasicProcedures.writeLater(String.format("DataFrameExportingTask:pipedOutputStream:IOException%s\n",e));
						e.printStackTrace();
					}
				};
				audioStreamExport.flushBuffer();
			}
		}
	}
	//
	protected void writeBufferedImage(ExportedVideoFrame frame) {
		AVCodecContext avContext= videoStreamState.enc;
		AVFrame avFrame= videoStreamState.frame;
		java.awt.image.BufferedImage source= FFmpegTools.convertToType(frame.getImage(),java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
		imageBuffer= FFmpegTools.createImageBuffer(source);
		int width= source.getWidth();
		int height= source.getHeight();
		imageBufferData= new BytePointer(imageBuffer);
		// av_frame_make_writable:
		// Ensure that the frame data is writable,
		// avoiding data copy if possible.
		// Do nothing if the frame is writable,
		// allocate new buffers and copy the data
		// if it is not.
		// return - 0 on success, a negative AVERROR
		// on error.
		// When we pass a frame to the encoder, it ma
		// keep a reference to it internally; make sure
		// we do not overwrite it here.
		if (av_frame_make_writable(avFrame) < 0) {
			throw new FFmpegAVFrameMakeWritableError();
		};
		// sws_getCachedContext:
		// Check if context can be reused, otherwise
		// reallocate a new one. If context is NULL,
		// just calls sws_getContext() to get a new
		// context. Otherwise, checks if the parameters
		// are the ones already saved in context. If that
		// is the case, returns the current context.
		// Otherwise, frees context and gets a new context
		// with the new parameters. Be warned that srcFilter
		// and dstFilter are not checked, they are assumed
		// to remain the same.
		videoStreamState.sws_ctx= sws_getCachedContext(
			videoStreamState.sws_ctx,
			width,
			height,
			technicalPixelFormat,
			avContext.width(),
			avContext.height(),
			avContext.pix_fmt(),
			SWS_BILINEAR,
			null,
			null,
			(double[])null);
		// int codecWidth= avContext.width();
		// int codecHeight= avContext.height();
		// Deprecated; use av_image_fill_arrays() instead.
		// avpicture_fill(videoStreamState.tmp_picture,imageBufferData,technicalPixelFormat,width,height);
		//
		// av_image_fill_arrays:
		// Setup the data pointers and linesizes based
		// on the specified image parameters and the
		// provided array.
		// The fields of the given image are filled in
		// by using the src address which points to the
		// image data buffer. Depending on the specified
		// pixel format, one or multiple image data pointers
		// and line sizes will be set. If a planar format is
		// specified, several pointers will be set pointing
		// to the different picture planes and the line
		// sizes of the different planes will be stored in
		// the lines_sizes array. Call with src == NULL to
		// get the required size for the src buffer.
		// To allocate the buffer and fill in the dst_data
		// and dst_linesize in one call, use
		// av_image_alloc().
		// dst_data - data pointers to be filled in.
		// dst_linesizes - linesizes for the image
		// in dst_data to be filled in.
		// src - buffer which will contain or
		// contains the actual image data, can be NULL.
		// pix_fmt - the pixel format of the image.
		// width - the width of the image in pixels.
		// height - the height of the image in pixels.
		// align - the value used in src for linesize
		// alignment.
		// return - the size in bytes required for src, a
		// negative error code in case of failure.
		int flag= av_image_fill_arrays(
			videoStreamState.tmp_frame.data(),
			videoStreamState.tmp_frame.linesize(),
			imageBufferData,
			technicalPixelFormat,
			width,
			height,
			1); // 64);
		if (flag < 0) {
			throw new FFmpegAVImageFillArraysError();
		};
		// sws_scale:
		// Scale the image slice in srcSlice and put
		// the resulting scaled slice in the image in dst.
		// A slice is a sequence of consecutive rows in
		// an image.
		// Slices have to be provided in sequential order,
		// either in top-bottom or bottom-top order. If
		// slices are provided in non-sequential order the
		// behavior of the function is undefined.
		// c - the scaling context previously
		// created with sws_getContext().
		// srcSlice - the array containing the
		// pointers to the planes of the source slice.
		// srcStride the array containing the
		// strides for each plane of the source image.
		// srcSliceY the position in the source
		// image of the slice to process, that is the
		// number (counted starting from zero) in the
		// image of the first row of the slice.
		// srcSliceH the height of the source slice,
		// that is the number of rows in the slice.
		// dst - the array containing the pointers
		// to the planes of the destination image.
		// dstStride the array containing the strides
		// for each plane of the destination image.
		// return - the height of the output slice.
		sws_scale(
			videoStreamState.sws_ctx,
			videoStreamState.tmp_frame.data(),
			videoStreamState.tmp_frame.linesize(),
			0,
			height,
			avFrame.data(),
			avFrame.linesize());
		if (automaticFrameRateCorrection.get()) {
			long controlFlag= 1;
			long timeOfCurrentFrame= frame.getTime();
			if (timeOfCurrentFrame >= 0) {
				while (controlFlag > 0) {
					controlFlag= videoStreamState.increaseFrameCounter(timeOfCurrentFrame);
					if (controlFlag < 0) {
						break;
					};
					writeAVFrame(avFrame,pFormatCtx,videoStreamState,videoFrameCounter);
				}
			} else {
				writeAVFrame(avFrame,pFormatCtx,videoStreamState,videoFrameCounter);
			}
		} else {
			writeAVFrame(avFrame,pFormatCtx,videoStreamState,videoFrameCounter);
		}
	}
	//
	// Encode one video frame and send it to the muxer
	// return 1 when encoding is finished, 0 otherwise:
	//
	protected boolean writeAVFrame(AVFrame frame, AVFormatContext oc, FFmpegOutputStreamState outputStreamState, AtomicLong frameCounter) {
		AVCodecContext c= outputStreamState.enc;
		AVPacket pkt= new AVPacket();
		// av_init_packet:
		// Initialize optional fields of a packet with default
		// values. Note, this does not touch the data and size
		// members, which have to be initialized separately.
		// pkt - packet.
		av_init_packet(pkt);
		// avcodec_send_frame:
		// Supply a raw video or audio frame to the encoder.
		// Use avcodec_receive_packet()
		// to retrieve buffered output packets.
		// avctx - codec context.
		// [in] frame - AVFrame containing the raw
		// audio or video frame to be encoded. Ownership of
		// the frame remains with the caller, and the encoder
		// will not write to the frame. The encoder may create
		// a reference to the frame data (or copy it if the
		// frame is not reference-counted). It can be NULL,
		// in which case it is considered a flush packet. This
		// signals the end of the stream. If the encoder still
		// has packets buffered, it will return them after
		// this call. Once flushing mode has been entered,
		// additional flush packets are ignored, and sending
		// frames will return AVERROR_EOF.
		// For audio:
		// If AV_CODEC_CAP_VARIABLE_FRAME_SIZE is set, then
		// each frame can have any number of samples.
		// If it is not set, frame->nb_samples must be equal
		// to avctx->frame_size for all frames except the
		// last. The final frame may be smaller than
		// avctx->frame_size.
		// return - 0 on success, otherwise negative error.
		// code:
		// AVERROR(EAGAIN): input is not accepted right now
		// - the frame must be resent after trying to read
		// output packets.
		// AVERROR_EOF: the encoder has been flushed, and
		// no new frames can be sent to it.
		// AVERROR(EINVAL): codec not opened,
		// refcounted_frames not set, it is a decoder, or
		// requires flush.
		// AVERROR(ENOMEM): failed to add packet to internal
		// queue, or similar other errors: legitimate decoding
		// errors.
		// Encode the image:
		int sendingFlag= avcodec_send_frame(c,frame);
		if (sendingFlag==AVERROR_EOF) {
			return true;
		} else if (sendingFlag < 0) {
			// av_err2str:
			// Fill the provided buffer with a string
			// containing an error string corresponding
			// to the AVERROR code errnum.
			throw new FFmpegVideoFrameEncodingError(FFmpegTools.av_err2str(sendingFlag));
		};
		// frame_number:
		// Frame counter, set by libavcodec.
		// - decoding: total number of frames returned from
		// the decoder so far.
		// - encoding: total number of frames passed to
		// the encoder so far.
		// Note: the counter is not incremented if
		// encoding/decoding resulted in an error.
		int auxiliaryCounter= c.frame_number() - 1;
		while (true) {
			// avcodec_receive_packet:
			// Read encoded data from the encoder.
			// avctx - codec context.
			// avpkt - this will be set to a
			// reference-counted packet allocated by the
			// encoder. Note that the function will always
			// call av_frame_unref(frame) before doing
			// anything else.
			// return - 0 on success, otherwise negative
			// error code:
			// AVERROR(EAGAIN): output is not available
			// right now - user must try to send input.
			// AVERROR_EOF: the encoder has been fully
			// flushed, and there will be no more output
			// packets.
			// AVERROR(EINVAL): codec not opened, or it
			// is an encoder other errors: legitimate
			// decoding errors.
			int receivingFlag= avcodec_receive_packet(c,pkt);
			// frame_number:
			// Frame counter, set by libavcodec.
			// - decoding: total number of frames returned
			// from the decoder so far.
			// - encoding: total number of frames passed
			// to the encoder so far. Note: the counter is
			// not incremented if encoding/decoding
			// resulted in an error.
			int updatedFrameNumber= c.frame_number();
			// System.err.printf(
			//	"pkt_pts:%s pkt_pts_time:%s pkt_dts:%s pkt_dts_time:%s\n",
			//	FFmpegTools.av_ts2str(pkt.pts()),
			//	FFmpegTools.av_ts2timestr(pkt.pts(),c.time_base()),
			//	FFmpegTools.av_ts2str(pkt.dts()),
			//	FFmpegTools.av_ts2timestr(pkt.dts(),c.time_base()));
			if (receivingFlag >= 0) {
///////////////////////////////////////////////////////////////////////
long delayInNumberOfFrames= FFmpegTools.computeRelativeTime(audioDataDelayCorrection.get(),c.time_base());
long counterValue= frameCounter.getAndIncrement();
long currentFrameNumber= delayInNumberOfFrames + counterValue;
// dts:
// Decompression timestamp in AVStream->time_base
// units; the time at which the packet is
// decompressed. Can be AV_NOPTS_VALUE if it is not
// stored in the file.
pkt.dts(currentFrameNumber);
// pts:
// Presentation timestamp in AVStream->time_base units;
// the time at which the decompressed packet will be
// presented to the user. Can be AV_NOPTS_VALUE if it
// is not stored in the file. pts MUST be larger or
// equal to dts as presentation cannot happen before
// decompression, unless one wants to view hex dumps.
// Some formats misuse the terms dts and pts/cts to
// mean something different. Such timestamps must be
// converted to true pts/dts before they are stored
// in AVPacket.
pkt.pts(currentFrameNumber);
auxiliaryCounter++;
int writingFlag= writeSingleFrame(oc,c.time_base(),outputStreamState.st,pkt);
if (writingFlag < 0) {
	// av_err2str:
	// Fill the provided buffer with a string containing
	// an error string corresponding to the AVERROR code
	// errnum.
	throw new FFmpegVideoFrameWritingError(FFmpegTools.av_err2str(writingFlag));
}
///////////////////////////////////////////////////////////////////////
			// } else if (receivingFlag == AVERROR(EAGAIN)) {
			//	break;
			} else if (receivingFlag==AVERROR_EOF) {
				break;
			} else if (auxiliaryCounter >= updatedFrameNumber) {
				break;
			} else {
				// av_err2str:
				// Fill the provided buffer with a
				// string containing an error string
				// corresponding to the AVERROR code
				// errnum.
				break;
			};
			// sync_opts:
			// output frame counter, could be changed to
			// some true timestamp
			// FIXME look at frame_number
			// if (pkt.pts == AV_NOPTS_VALUE && !(enc->codec->capabilities & AV_CODEC_CAP_DELAY))
			//	pkt.pts = ost->sync_opts;
			// };
		};
		if (frame==null) {
			return true;
		} else {
			return false;
		}
	}
	//
	protected static int writeSingleFrame(AVFormatContext fmt_ctx, AVRational time_base, AVStream st, AVPacket pkt) {
		// av_packet_rescale_ts:
		// Convert valid timing fields (timestamps / durations)
		// in a packet from one timebase to another. Timestamps
		// with unknown values (AV_NOPTS_VALUE) will be
		// ignored.
		// pkt - packet on which the conversion will be
		// performed.
		// tb_src - source timebase, in which the timing
		// fields in pkt are expressed.
		// tb_dst - destination timebase, to which the
		// timing fields will be converted.
		//
		// Rescale output packet timestamp values from codec
		// to stream timebase:
		av_packet_rescale_ts(pkt,time_base,st.time_base());
		pkt.stream_index(st.index());
		// Write the compressed frame to the media file:
		// FFmpegTools.logPacket(fmt_ctx,pkt);
		return av_interleaved_write_frame(fmt_ctx,pkt);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void store(java.awt.image.BufferedImage frame) {
		if (acceptFrames.get()) {
			synchronized (history) {
				long currentRealTime= System.currentTimeMillis();
				history.addFirst(new ExportedVideoFrame(frame,currentRealTime));
				history.notifyAll();
			}
		}
	}
	public void store(java.awt.image.BufferedImage frame, long timeInMilliseconds) {
		if (acceptFrames.get()) {
			synchronized (history) {
				history.addFirst(new ExportedVideoFrame(frame,timeInMilliseconds));
				history.notifyAll();
			}
		}
	}
	//
	public void store(byte[] autioData) {
		if (acceptFrames.get()) {
			synchronized (history) {
				long currentRealTime= System.currentTimeMillis();
				history.addFirst(new ExportedAudioData(autioData,currentRealTime));
				history.notifyAll();
			}
		}
	}
	public void store(byte[] autioData, long timeInMilliseconds) {
		if (acceptFrames.get()) {
			synchronized (history) {
				history.addFirst(new ExportedAudioData(autioData,timeInMilliseconds));
				history.notifyAll();
			}
		}
	}
	//
	@Override
	@SuppressWarnings("CallToThreadDumpStack")
	public void run() {
		while (true) {
			try {
				synchronized (history) {
					history.wait(emergencyTimeout);
				};
				recordFrames();
			} catch (InterruptedException e) {
			} catch (ThreadDeath e) {
				return;
			} catch (Throwable e) {
				if (reportCriticalErrors()) {
					e.printStackTrace();
				}
			}
		}
	}
	//
	protected void recordFrames() {
		while (recordFrames.get()) {
			int length;
			ExportedData currentFrame;
			synchronized (history) {
				length= history.size();
				if (length > 0) {
					currentFrame= history.getLast();
				} else {
					break;
				}
			};
			frameNumber++;
			try {
				synchronized (nativeLibraryGuard) {
					if (recordFrames.get()) {
						writeFrame(currentFrame);
					}
				}
			} catch (Throwable e) {
				recordFrames.set(false);
				closeWriting();
				owner.completeDataWriting(frameNumber,e);
				throw e;
			};
			synchronized (history) {
				length= history.size();
				if (length > 0) {
					try {
						history.removeLast();
						length= history.size();
					} finally {
						history.notifyAll();
					};
					int superfluousLength= length - writeBufferSize.get();
					if (superfluousLength > 0) {
						bufferOverflow= true;
						owner.reportBufferOverflow();
						Iterator<ExportedData> iteratorHistory= history.iterator();
						int step= length / superfluousLength;
						int currentLength= superfluousLength;
						int quantityOfRemovedItems= 0;
						int auxiliaryCounter= step;
						try {
///////////////////////////////////////////////////////////////////////
while (iteratorHistory.hasNext() && currentLength > 0) {
	ExportedData previousFrame= iteratorHistory.next();
	if (auxiliaryCounter >= step && previousFrame.isVideoFrame()) {
		auxiliaryCounter= 0;
		iteratorHistory.remove();
		currentLength--;
		quantityOfRemovedItems++;
	} else {
		auxiliaryCounter++;
	}
}
///////////////////////////////////////////////////////////////////////
						} finally {
							history.notifyAll();
						}
					} else {
						if (bufferOverflow) {
							bufferOverflow= false;
							owner.annulBufferOverflow();
						}
					}
				}
			}
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
}
