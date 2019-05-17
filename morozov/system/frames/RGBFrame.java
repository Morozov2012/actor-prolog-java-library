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
			// BufferedImageController targetClass,
			byte[] givenByteArray,
			long givenNumber,
			long givenTime,
			DataFrameBaseAttributesInterface givenAttributes) {
		super(givenNumber,givenTime,givenAttributes);
		nativeImage= givenImage;
		// byteArray= targetClass.convertImageToBytes(givenImage);
		byteArray= givenByteArray;
		width= givenImage.getWidth();
		height= givenImage.getHeight();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public DataArrayType getDataArrayType() {
		return DataArrayType.RGB_FRAME;
	}
	//
	public int getDataSize() {
		return byteArray.length;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	/*
	public java.awt.image.BufferedImage getImage() {
		if (nativeImage==null) {
			nativeImage= Space2DWriter.bytesToImage(byteArray);
		};
		return nativeImage;
	}
	*/
	//
	public void setNativeImage(java.awt.image.BufferedImage image) {
		nativeImage= image;
	}
	//
	public java.awt.image.BufferedImage getNativeImage() {
		return nativeImage;
	}
	//
	public byte[] getByteArray() {
		return byteArray;
	}
	//
	public int getWidth() {
		return width;
	}
	//
	public int getHeight() {
		return height;
	}
}
