// (c) 2018 Alexei A. Morozov

package morozov.system.frames.tools;

import java.awt.image.BufferedImage;

public class AttachedImage {
	//
	protected BufferedImage image;
	protected int positionX;
	protected int positionY;
	//
	public AttachedImage(BufferedImage i, int x, int y) {
		image= i;
		positionX= x;
		positionY= y;
	}
	//
	public BufferedImage getImage() {
		return image;
	}
	public int getPositionX() {
		return positionX;
	}
	public int getPositionY() {
		return positionY;
	}
	public int getWidth() {
		return image.getWidth();
	}
	public int getHeight() {
		return image.getHeight();
	}
}
