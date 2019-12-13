// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg;

import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.avcodec.*;
import static org.bytedeco.javacpp.swscale.*;
import static org.bytedeco.javacpp.swresample.*;

// A wrapper around a single output AVStream:

public class FFmpegOutputStreamState {
	public AVStream st;
	public AVCodecContext enc;
	/* pts of the next frame that will be generated */
	public long next_pts;		// int64_t next_pts;
	public long samples_count;	// int
	public AVFrame frame;
	public AVFrame tmp_frame;
	public SwsContext sws_ctx;
	public SwrContext swr_ctx;
	public Integer nbSamples;
	public Integer sourceImageWidth;
	public Integer sourceImageHeight;
	//
	protected long quantityOfRecordedFrames= -1;
	protected long initialTime= -1;
	//
	public int getSourceImageWidth(int defaultWidth) {
		if (sourceImageWidth != null) {
			return sourceImageWidth;
		} else {
			return defaultWidth;
		}
	}
	public int getSourceImageHeight(int defaultHeight) {
		if (sourceImageHeight != null) {
			return sourceImageHeight;
		} else {
			return defaultHeight;
		}
	}
	public long increaseFrameCounter(long frameTime) {
		long currentRealTime= frameTime;
		if (quantityOfRecordedFrames < 0) {
			quantityOfRecordedFrames= 1;
			initialTime= currentRealTime;
			return 0;
		};
		long duration= currentRealTime - initialTime;
		long proposedQuantityOfRecordedFrames= quantityOfRecordedFrames + 1;
		if (duration > 0) {
			AVRational streamTimeBase= st.time_base();
			double doubleExpectedNewQuantityOfRecordedFrames=
				((double)duration / 1000) * ((double)streamTimeBase.den() / streamTimeBase.num());
			long longExpectedNewQuantityOfRecordedFrames=
				(long)StrictMath.round(doubleExpectedNewQuantityOfRecordedFrames);
			long delta= longExpectedNewQuantityOfRecordedFrames - proposedQuantityOfRecordedFrames;
			if (delta >= 0) {
				quantityOfRecordedFrames= proposedQuantityOfRecordedFrames;
			};
			return delta;
		};
		quantityOfRecordedFrames= proposedQuantityOfRecordedFrames;
		return 0;
	}
}
