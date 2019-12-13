// (c) 2018 Alexei A. Morozov

package morozov.system.ffmpeg.interfaces;

public interface FFmpegFrameInterface extends FFmpegDataInterface {
	public java.awt.image.BufferedImage getImage();
	public int getWidth();
	public int getHeight();
}
