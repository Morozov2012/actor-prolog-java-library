// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.tools;

public class TunedBufferedImage {
	//
	protected java.awt.image.BufferedImage image;
	protected int horizontalCorrection;
	protected int verticalCorrection;
	//
	public TunedBufferedImage(java.awt.image.BufferedImage i, int x, int y) {
		image= i;
		horizontalCorrection= x;
		verticalCorrection= y;
	}
	//
	public java.awt.image.BufferedImage getImage() {
		return image;
	}
	public int getHorizontalCorrection() {
		return horizontalCorrection;
	}
	public int getVerticalCorrection() {
		return verticalCorrection;
	}
}
