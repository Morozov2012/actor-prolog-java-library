// (c) 2019 Alexei A. Morozov

package morozov.system.ffmpeg;

import morozov.system.sound.*;

import morozov.system.ffmpeg.errors.*;

import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.avcodec.*;
import static org.bytedeco.javacpp.swresample.*;
import org.bytedeco.javacpp.*;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.IOException;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AudioStreamExport {
	//
	protected AVFormatContext pFormatCtx;
	protected FFmpegOutputStreamState audioStreamState;
	protected int audioFrameSize= SoundFramingTask.getBufferSize();
	//
	protected int inputStreamBufferSize= audioFrameSize*5;
	protected PipedInputStream pipedInputStream;
	protected PipedOutputStream pipedOutputStream;
	//
	protected byte[] m_buf;
	//
	protected AtomicLong audioFrameCounter= new AtomicLong(0);
	//
	protected AtomicInteger outputDebugInformation= new AtomicInteger(0);
	//
	protected static int reportCriticalErrorsLevel= 1;
	protected static int reportAdmissibleErrorsLevel= 2;
	protected static int reportWarningsLevel= 3;
	//
	///////////////////////////////////////////////////////////////
	//
	public AudioStreamExport(PipedOutputStream outputStream) {
		pipedOutputStream= outputStream;
		try {
			pipedInputStream= new PipedInputStream(pipedOutputStream,inputStreamBufferSize);
		} catch (IOException e) {
			// ControlPanelBasicProcedures.writeLater(String.format("AudioStreamExportingTask:CannotCreatePipedInputStream:%s\n",e));
			System.err.printf(String.format("AudioStreamExport:CannotCreatePipedInputStream:%s\n",e));
		}
	}
	//
	///////////////////////////////////////////////////////////////
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
	public AVFormatContext getAVFormatContext() {
		return pFormatCtx;
	}
	public void setAVFormatContext(AVFormatContext ctx) {
		pFormatCtx= ctx;
	}
	//
	public FFmpegOutputStreamState getFFmpegOutputStreamState() {
		return audioStreamState;
	}
	public void setFFmpegOutputStreamState(FFmpegOutputStreamState state) {
		audioStreamState= state;
	}
	//
	public int getAudioFrameSize() {
		return audioFrameSize;
	}
	public void setAudioFrameSize(int size) {
		audioFrameSize= size;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void resetAudioFrameCounter() {
		audioFrameCounter.set(0);
	}
	//
	public void stopDataTransfer() {
		flushBuffer();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void flushBuffer() {
		try {
			while (true) {
				int availableBytes= pipedInputStream.available();
				if (availableBytes >= audioFrameSize) {
					if (	m_buf==null ||
						m_buf.length != audioFrameSize) {
						m_buf= new byte[audioFrameSize];
					} else {
						Arrays.fill(m_buf,(byte)0);
					};
					pipedInputStream.read(m_buf,0,audioFrameSize);
					writeAudioFrame(m_buf,pFormatCtx,audioStreamState,audioFrameCounter);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			// ControlPanelBasicProcedures.writeLater(String.format("AudioStreamExportingTask:IOException:%s\n",e));
			e.printStackTrace();
		}
	}
	//
	public boolean writeEOFAudioFrame() {
		return writeFilledAudioFrame(null,pFormatCtx,audioStreamState,audioFrameCounter);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	/*
	Encode one audio frame and send it to the muxer
	return 1 when encoding is finished, 0 otherwise:
	*/
	protected boolean writeAudioFrame(byte[] m_buf, AVFormatContext oc, FFmpegOutputStreamState ost, AtomicLong frameCounter) {
		// avFrame= get_audio_frame(ost);
		AVFrame avFrame= fill_audio_frame(m_buf,ost);
		return writeFilledAudioFrame(avFrame,oc,ost,frameCounter);
	}
	protected boolean writeFilledAudioFrame(AVFrame avFrame, AVFormatContext oc, FFmpegOutputStreamState ost, AtomicLong frameCounter) {
		// AVPacket pkt= new AVPacket(0);	// data and size must be 0;
		AVPacket pkt= new AVPacket();	// data and size must be 0;
		/*
		av_init_packet:
		Initialize optional fields of a packet with default values.
		Note, this does not touch the data and size members,
		which have to be initialized separately.
		pkt - packet.
		IS ACTUAL!
		*/
		av_init_packet(pkt);
		AVCodecContext c= ost.enc;
		if (avFrame != null) {
			/*
			swr_get_delay:
			Gets the delay the next input sample will
			experience relative to the next output sample.
			Swresample can buffer data if more input has
			been provided than available output space, also
			converting between sample rates needs a delay.
			This function returns the sum of all such delays.
			The exact delay is not necessarily an integer
			value in either input or output sample rate.
			Especially when downsampling by a large value, the
			output sample rate may be a poor choice to represent
			the delay, similarly for upsampling and the input
			sample rate.
			s - swr context.
			base - timebase in which the returned delay
			will be:
			(1) if it's set to 1 the returned delay is in seconds
			(2) if it's set to 1000 the returned delay is in
			milliseconds
			(3) if it's set to the input sample rate then the
			returned delay is in input samples
			(4) if it's set to the output sample rate then the
			returned delay is in output samples
			(5) if it's the least common multiple of
			in_sample_rate and out_sample_rate then an exact
			rounding-free delay will be returned
			return - the delay in 1 / c base units.
			IS ACTIVE!
			*/
			long delay= swr_get_delay(ost.swr_ctx,c.sample_rate());
			/*
			av_rescale_rnd:
			Rescale a 64-bit integer with specified rounding.
			The operation is mathematically equivalent to
			a * b / c, but writing that directly can
			overflow, and does not support different rounding
			methods.
			IS ACTIVE!
			*/
			/*
			Convert samples from native format to destination
			codec format, using the resampler
			compute destination number of samples:
			*/
			int dst_nb_samples= (int)av_rescale_rnd(delay+avFrame.nb_samples(),c.sample_rate(),c.sample_rate(),AV_ROUND_UP);
			/*
			av_assert0:
			*/
			// FFmpegTools.av_assert0(dst_nb_samples==avFrame.nb_samples());
			if (dst_nb_samples!=avFrame.nb_samples()) {
				if (reportWarnings()) {
					System.err.printf("dst_nb_samples(%s)!=avFrame.nb_samples(%s)\n",dst_nb_samples,avFrame.nb_samples());
				}
			};
			/*
			av_frame_make_writable:
			Ensure that the frame data is writable,
			avoiding data copy if possible.
			Do nothing if the frame is writable, allocate
			new buffers and copy the data if it is not.
			return - 0 on success, a negative AVERROR
			on error.
			IS ACTIVE!
			*/
			/*
			When we pass a frame to the encoder, it may
			keep a reference to it internally;
			make sure we do not overwrite it here:
			*/
			int flag1= av_frame_make_writable(ost.frame);
			if (flag1 < 0) {
				throw new FFmpegCannotInitializeConversionContext();
			};
			/*
			swr_convert:
			Convert audio.
			in and in_count can be set to 0 to flush the last
			few samples out at the end.
			If more input is provided than output space,
			then the input will be buffered.
			You can avoid this buffering by using
			swr_get_out_samples() to retrieve an upper
			bound on the required number of output samples
			for the given number of input samples.
			Conversion will run directly without copying
			whenever possible.
			s - allocated Swr context, with parameters set.
			out - output buffers, only the first one need
			be set in case of packed audio.
			out_count - amount of space available for
			output in samples per channel.
			in - input buffers, only the first one need
			to be set in case of packed audio.
			in_count - number of input samples available
			in one channel.
			return - number of samples output per channel,
			negative value on error.
			IS ACTIVE!
			*/
			int flag2= swr_convert(
				ost.swr_ctx,
				ost.frame.data(),
				dst_nb_samples,
				avFrame.data(),
				avFrame.nb_samples());
			if (flag2 < 0) {
				throw new FFmpegConversionError();
			};
			avFrame= ost.frame;
			/*
			av_rescale_q:
			Rescale a 64-bit integer by 2 rational numbers.
			The operation is mathematically equivalent to
			a * bq / cq.
			This function is equivalent to av_rescale_q_rnd()
			with #AV_ROUND_NEAR_INF.
			IS ACTIVE!
			*/
			AVRational avRational= av_make_q(1,c.sample_rate());
			long pts= av_rescale_q(ost.samples_count,avRational,c.time_base());
			// avFrame.pts(ost.samples_count);
			avFrame.pts(pts);
			ost.samples_count= ost.samples_count + dst_nb_samples;
		};
		/*
		avcodec_send_frame:
		Supply a raw video or audio frame to the encoder.
		Use avcodec_receive_packet()
		to retrieve buffered output packets.
		avctx - codec context.
		[in] frame - AVFrame containing the raw audio or
		video frame to be encoded. Ownership of the frame remains
		with the caller, and the encoder will not write to
		the frame. The encoder may create a reference to the frame
		data (or copy it if the frame is not reference-counted).
		It can be NULL, in which case it is considered a flush
		packet. This signals the end of the stream. If the encoder
		still has packets buffered, it will return them after this
		call. Once flushing mode has been entered, additional flush
		packets are ignored, and sending frames will return
		AVERROR_EOF.
		For audio:
		If AV_CODEC_CAP_VARIABLE_FRAME_SIZE is set, then each frame
		can have any number of samples.
		If it is not set, frame->nb_samples must be equal to
		avctx->frame_size for all frames except the last.
		The final frame may be smaller than avctx->frame_size.
		return - 0 on success, otherwise negative error code:
		AVERROR(EAGAIN): input is not accepted right now - the
		frame must be resent after trying to read output packets
		AVERROR_EOF: the encoder has been flushed, and no new
		frames can be sent to it
		AVERROR(EINVAL): codec not opened, refcounted_frames
		not set, it is a decoder, or requires flush
		AVERROR(ENOMEM): failed to add packet to internal queue,
		or similar other errors: legitimate decoding errors
		IS ACTUAL!
		*/
		int flag3= avcodec_send_frame(c,avFrame);
		if (flag3==AVERROR_EOF) {
			return true;
		} else if (flag3 < 0) {
			/*
			av_err2str:
			Fill the provided buffer with a string containing
			an error string corresponding to the AVERROR code
			errnum.
			IS ACTUAL
			*/
			throw new FFmpegAudioFrameEncodingError(FFmpegTools.av_err2str(flag3));
		};
		/*
		frame_number:
		Frame counter, set by libavcodec.
		- decoding: total number of frames returned from
		the decoder so far.
		- encoding: total number of frames passed to
		the encoder so far.
		Note: the counter is not incremented if
		encoding/decoding resulted in an error.
		IS ACTUAL!
		*/
		int auxiliaryCounter= c.frame_number();
		while (true) {
			/*
			avcodec_receive_packet:
			Read encoded data from the encoder.
			avctx - codec context.
			avpkt - this will be set to a reference-counted
			packet allocated by the encoder. Note that
			the function will always call
			av_frame_unref(frame) before doing anything else.
			return - 0 on success, otherwise negative error code:
			AVERROR(EAGAIN): output is not available right
			now - user must try to send input
			AVERROR_EOF: the encoder has been fully flushed,
			and there will be no more output packets
			AVERROR(EINVAL): codec not opened, or it is an encoder
			other errors: legitimate decoding errors
			IS ACTUAL!
			*/
			int flag4= avcodec_receive_packet(c,pkt);
			/*
			frame_number:
			Frame counter, set by libavcodec.
			- decoding: total number of frames returned from
			the decoder so far.
			- encoding: total number of frames passed to
			the encoder so far.
			Note: the counter is not incremented if
			encoding/decoding resulted in an error.
			IS ACTUAL!
			*/
			int updatedFrameNumber= c.frame_number();
			if (flag4 >= 0) {
///////////////////////////////////////////////////////////////////////
if (pkt.duration() <= 0) {
	continue;
};
long frameNumber= frameCounter.getAndAdd(pkt.duration());
// dts:
// Decompression timestamp in AVStream->time_base
// units; the time at which the packet is
// decompressed. Can be AV_NOPTS_VALUE if it is not
// stored in the file.
pkt.dts(frameNumber);
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
pkt.pts(frameNumber);
auxiliaryCounter++;
int flag5= writeAudioFrame(oc,c.time_base(),ost.st,pkt);
if (flag5 < 0) {
	/*
	av_err2str:
	Fill the provided buffer with a string containing
	an error string corresponding to the AVERROR code
	errnum.
	IS ACTUAL
	*/
	throw new FFmpegAudioFrameWritingError(FFmpegTools.av_err2str(flag5));
}
///////////////////////////////////////////////////////////////////////
			//} else if (flag4 == AVERROR(EAGAIN)) {
			//	break;
			} else if (flag4==AVERROR_EOF) {
				break;
			} else if (auxiliaryCounter >= updatedFrameNumber) {
				break;
			} else {
				/*
				av_err2str:
				Fill the provided buffer with a string containing
				an error string corresponding to the AVERROR code
				errnum.
				IS ACTUAL
				*/
				throw new FFmpegAudioFrameEncodingError(FFmpegTools.av_err2str(flag4));
			};
			/*
			sync_opts:
			output frame counter, could be changed to some
			true timestamp
			FIXME look at frame_number
			*/
			// if (pkt.pts == AV_NOPTS_VALUE && !(enc->codec->capabilities & AV_CODEC_CAP_DELAY))
			//	pkt.pts = ost->sync_opts;
			// };
		};
		if (avFrame==null) {
			return true;
		} else {
			return false;
		}
	}
	//
	protected static int writeAudioFrame(AVFormatContext fmt_ctx, AVRational time_base, AVStream st, AVPacket pkt) {
		/*
		av_packet_rescale_ts:
		Convert valid timing fields (timestamps / durations) in
		a packet from one timebase to another. Timestamps with
		unknown values (AV_NOPTS_VALUE) will be ignored.
		pkt - packet on which the conversion will be performed.
		tb_src - source timebase, in which the timing fields
		in pkt are expressed.
		tb_dst - destination timebase, to which the timing
		fields will be converted.
		IS ACTIVE!
		*/
		/*
		Rescale output packet timestamp values from codec
		to stream timebase:
		*/
		av_packet_rescale_ts(pkt,time_base,st.time_base());
		/*
		av_write_frame:
		Packet's \ref AVPacket.stream_index "stream_index"
		field must be set to the index of the
		corresponding stream in AVFormatContext.streams
		"s->streams".
		*/
		pkt.stream_index(st.index());
		/* Write the compressed frame to the media file. */
		// FFmpegTools.logPacket(fmt_ctx,pkt);
		return av_interleaved_write_frame(fmt_ctx,pkt);
	}
	//
	protected static AVFrame fill_audio_frame(byte[] sound, FFmpegOutputStreamState ost) {
		AVFrame tmpFrame= ost.tmp_frame;
		ShortPointer q= new ShortPointer(tmpFrame.data(0));
		int nb_samples= tmpFrame.nb_samples();
		int channels= ost.enc.channels();
		int vectorLength= nb_samples*channels;
		short[] vector= new short[vectorLength];
		ByteBuffer byteBuffer= ByteBuffer.wrap(sound);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		ShortBuffer shortBuffer= byteBuffer.asShortBuffer();
		int index= 0;
		for (int j=0; j < nb_samples; j++) {
			for (int i=0; i < channels; i++) {
				short v= shortBuffer.get(index);
				vector[index]= v;
				index++;
			}
		};
		q.put(vector);
		ost.next_pts= ost.next_pts + tmpFrame.nb_samples();
		return tmpFrame;
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
