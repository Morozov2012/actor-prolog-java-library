// (c) 2019 Alexei A. Morozov

package morozov.system.frames;

import morozov.system.frames.data.interfaces.*;
import morozov.system.frames.interfaces.*;
import morozov.system.modes.*;

public class RGBFrame extends DataFrame implements RGBFrameInterface {
	//
	protected transient java.awt.image.BufferedImage nativeImage;
	protected byte[] byteArray;
	protected int width;
	protected int height;
	//
	private static final long serialVersionUID= 0x36152A98EA5CF72EL; // 3897067888814389038L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.frames","RGBFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public RGBFrame(java.awt.image.BufferedImage givenImage,
			byte[] givenByteArray,
			long givenNumber,
			long givenTime,
			DataFrameBaseAttributesInterface givenAttributes) {
		super(givenNumber,givenTime,givenAttributes);
		nativeImage= givenImage;
		byteArray= givenByteArray;
		width= givenImage.getWidth();
		height= givenImage.getHeight();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public DataArrayType getDataArrayType() {
		return DataArrayType.RGB_FRAME;
	}
	//
	@Override
	public int getDataSize() {
		return byteArray.length;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setNativeImage(java.awt.image.BufferedImage image) {
		nativeImage= image;
	}
	//
	@Override
	public java.awt.image.BufferedImage getNativeImage() {
		return nativeImage;
	}
	//
	@Override
	public byte[] getByteArray() {
		return byteArray;
	}
	//
	@Override
	public int getWidth() {
		return width;
	}
	//
	@Override
	public int getHeight() {
		return height;
	}
}
