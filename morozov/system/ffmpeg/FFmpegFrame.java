// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg;

import static org.bytedeco.javacpp.avutil.*;

public class FFmpegFrame {
	//
	protected java.awt.image.BufferedImage image;
	protected long time;
	protected AVRational timeBase;
	protected AVRational averageFrameRate;
	// protected long beginningTime;
	//
	public FFmpegFrame(java.awt.image.BufferedImage i, long t, AVRational base, AVRational frameRate) {
		image= i;
		time= t;
		timeBase= base;
		averageFrameRate= frameRate;
		// beginningTime= beginning;
	}
	//
	public java.awt.image.BufferedImage getImage() {
		return image;
	}
	public long getTime() {
		return time;
	}
	public AVRational getTimeBase() {
		return timeBase;
	}
	public AVRational getAverageFrameRate() {
		return averageFrameRate;
	}
	// public long getBeginningTime() {
	//	return beginningTime;
	// }
	//
	public double getTimeInSeconds() {
		return FFmpegTools.computeTime(time,timeBase);
	}
	//
	public int getWidth() {
		return image.getWidth();
	}
	public int getHeight() {
		return image.getHeight();
	}
}
