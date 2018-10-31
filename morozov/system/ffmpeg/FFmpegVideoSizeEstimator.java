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

import morozov.run.*;
import morozov.system.files.*;

public class FFmpegVideoSizeEstimator extends FFmpegFrameReadingTask {
	//
	///////////////////////////////////////////////////////////////
	//
	public FFmpegVideoSizeEstimator(FFmpegInterface buffer) {
		super(buffer);
		stopAfterSingleReading.set(false);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long estimateVideoSize(ExtendedFileName fileName, int timeout, CharacterSet characterSet, String formatName, FFmpegStreamDefinition[] streams, FFmpegCodecOption[] options, StaticContext staticContext) {
		openReading(fileName,timeout,characterSet,formatName,streams,options,staticContext);
		try {
			readAllFrames();
		} finally {
			closeReading();
		};
		return totalNumberOfFrames;
	}
	//
	protected void readAllFrames() {
		try {
			synchronized (this) {
				while (true) {
					boolean isEOF= readOnePacket(pCodecCtx,pFrameInitial,pFrameRGB,sws_ctx,true);
					if (isEOF) {
						break;
					}
				}
			}
		} catch (InterruptedException e) {
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}