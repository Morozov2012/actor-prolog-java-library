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
	// public AVPicture tmp_picture;
	// public float t, tincr, tincr2;
	public SwsContext sws_ctx;
	public SwrContext swr_ctx;
	public Integer nbSamples;
	public Integer sourceImageWidth;
	public Integer sourceImageHeight;
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
}
