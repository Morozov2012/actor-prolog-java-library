// (c) 2018 Alexei A. Morozov

package morozov.system.astrohn;

import morozov.system.astrohn.frames.data.*;

import java.io.Serializable;

public class TerahertzDataBuffer implements Serializable {
	//
	double[] data;
	//
	int matrixWidth;
	int matrixHeight;
	int imageWidth;
	int imageHeight;
	int horizontalOffset;
	int verticalOffset;
	//
	private static final long serialVersionUID= 0xD9B8C62D6167EE6AL; // -2758236873554399638L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.astrohn","TerahertzDataBuffer");
	// }
	//
	public TerahertzDataBuffer(double[] d, TVFilterImageHeader terahertzPacketBody) {
		data= d;
		matrixWidth= terahertzPacketBody.getColumns();
		matrixHeight= terahertzPacketBody.getRows();
		TVImageHeader hdr= terahertzPacketBody.getHDR();
		imageWidth= hdr.getRectWidth();
		imageHeight= hdr.getRectHeight();
		horizontalOffset= hdr.getRectLeft();
		verticalOffset= hdr.getRectTop();
	}
	//
	public double[] getData() {
		return data;
	}
	public int getMatrixWidth() {
		return matrixWidth;
	}
	public int getMatrixHeight() {
		return matrixHeight;
	}
	public int getImageWidth() {
		return imageWidth;
	}
	public int getImageHeight() {
		return imageHeight;
	}
	public int getHorizontalOffset() {
		return horizontalOffset;
	}
	public int getVerticalOffset() {
		return verticalOffset;
	}
}
