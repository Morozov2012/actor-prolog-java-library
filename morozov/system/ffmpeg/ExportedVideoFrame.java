// (c) 2019 Alexei A. Morozov

package morozov.system.ffmpeg;

public class ExportedVideoFrame extends ExportedData {
	//
	protected java.awt.image.BufferedImage image;
	//
	public ExportedVideoFrame(java.awt.image.BufferedImage givenImage, long time) {
		super(time);
		image= givenImage;
	}
	//
	public java.awt.image.BufferedImage getImage() {
		return image;
	}
	//
	@Override
	public boolean isVideoFrame() {
		return true;
	}
}
