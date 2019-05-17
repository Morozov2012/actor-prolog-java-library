// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg;

import static org.bytedeco.javacpp.avutil.*;

import morozov.system.ffmpeg.interfaces.*;

public class FFmpegFrame implements FFmpegFrameInterface {
	//
	protected java.awt.image.BufferedImage image;
	protected long time;
	protected AVRational timeBase;
	protected AVRational averageFrameRate;
	protected long number;
	//
	public FFmpegFrame(java.awt.image.BufferedImage i, long t, AVRational base, AVRational frameRate, long n) {
		image= i;
		time= t;
		timeBase= base;
		averageFrameRate= frameRate;
		number= n;
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
	public long getNumber() {
		return number;
	}
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
