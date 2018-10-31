// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data;

import morozov.system.kinect.frames.data.interfaces.*;

import java.io.Serializable;

public class KinectFrameBaseAttributes implements KinectFrameBaseAttributesInterface, Serializable {
	//
	protected byte deviceType;
	protected float focalLengthX;
	protected float focalLengthY;
	protected int depthFrameWidth;
	protected int depthFrameHeight;
	protected int colorFrameWidth;
	protected int colorFrameHeight;
	protected int numberOfSkeletons;
	protected int correctionX;
	protected int correctionY;
	//
	private static final long serialVersionUID= 0x39291EC3E6ABC8BCL; // 4118857160944502972L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.data","KinectFrameBaseAttributes");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectFrameBaseAttributes(
			byte device,
			float focX,
			float focY,
			int givenDepthFrameWidth,
			int givenDepthFrameHeight,
			int givenColorFrameWidth,
			int givenColorFrameHeight,
			int givenNumberOfSkeletons,
			int givenCorrectionX,
			int givenCorrectionY) {
		deviceType= device;
		focalLengthX= focX;
		focalLengthY= focY;
		depthFrameWidth= givenDepthFrameWidth;
		depthFrameHeight= givenDepthFrameHeight;
		colorFrameWidth= givenColorFrameWidth;
		colorFrameHeight= givenColorFrameHeight;
		numberOfSkeletons= givenNumberOfSkeletons;
		correctionX= givenCorrectionX;
		correctionY= givenCorrectionY;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public byte getDeviceType() {
		return deviceType;
	}
	public float getFocalLengthX() {
		return focalLengthX;
	}
	public float getFocalLengthY() {
		return focalLengthY;
	}
	public int getDepthFrameWidth() {
		return depthFrameWidth;
	}
	public int getDepthFrameHeight() {
		return depthFrameHeight;
	}
	public int getColorFrameWidth() {
		return colorFrameWidth;
	}
	public int getColorFrameHeight() {
		return colorFrameHeight;
	}
	public int getNumberOfSkeletons() {
		return numberOfSkeletons;
	}
	public int getCorrectionX() {
		return correctionX;
	}
	public int getCorrectionY() {
		return correctionY;
	}
}
