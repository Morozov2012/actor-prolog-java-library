// (c) 2018 Alexei A. Morozov

package morozov.system.ffmpeg.interfaces;

import static org.bytedeco.javacpp.avutil.*;

public interface FFmpegFrameInterface {
	public java.awt.image.BufferedImage getImage();
	public long getTime();
	public AVRational getTimeBase();
	public AVRational getAverageFrameRate();
	public long getNumber();
	public int getWidth();
	public int getHeight();
}
