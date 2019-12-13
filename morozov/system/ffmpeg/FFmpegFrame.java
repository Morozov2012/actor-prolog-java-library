// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg;

import static org.bytedeco.javacpp.avutil.*;

import morozov.system.ffmpeg.interfaces.*;

public class FFmpegFrame extends FFmpegData implements FFmpegFrameInterface {
	//
	protected java.awt.image.BufferedImage image;
	//
	public FFmpegFrame(java.awt.image.BufferedImage i, long t1, long t2, AVRational base, AVRational frameRate, long n) {
		super(t1,t2,base,frameRate,n);
		image= i;
	}
	//
	@Override
	public java.awt.image.BufferedImage getImage() {
		return image;
	}
	//
	@Override
	public int getWidth() {
		return image.getWidth();
	}
	@Override
	public int getHeight() {
		return image.getHeight();
	}
}
