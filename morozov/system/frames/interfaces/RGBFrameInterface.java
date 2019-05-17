// (c) 2019 Alexei A. Morozov

package morozov.system.frames.interfaces;

public interface RGBFrameInterface extends DataFrameInterface {
	public void setNativeImage(java.awt.image.BufferedImage image);
	public java.awt.image.BufferedImage getNativeImage();
	public byte[] getByteArray();
	public int getWidth();
	public int getHeight();
}
