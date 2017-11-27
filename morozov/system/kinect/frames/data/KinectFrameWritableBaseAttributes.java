// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.data.tools.*;

import java.io.Serializable;

public class KinectFrameWritableBaseAttributes extends KinectFrameBaseAttributes implements KinectFrameWritableBaseAttributesInterface {
	//
	public KinectFrameWritableBaseAttributes(
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
		super(	device,
			focX,
			focY,
			givenDepthFrameWidth,
			givenDepthFrameHeight,
			givenColorFrameWidth,
			givenColorFrameHeight,
			givenNumberOfSkeletons,
			givenCorrectionX,
			givenCorrectionY);
	}
	//
	public void setDeviceType(byte type) {
		deviceType= type;
	}
	public void setFocalLengthX(float x) {
		focalLengthX= x;
	}
	public void setFocalLengthY(float y) {
		focalLengthY= y;
	}
	public void setFocalLength(float x, float y) {
		focalLengthX= x;
		focalLengthY= y;
	}
	public void setDepthFrameWidth(int width) {
		depthFrameWidth= width;
	}
	public void setDepthFrameHeight(int height) {
		depthFrameHeight= height;
	}
	public void setDepthFrameSize(int width, int height) {
		depthFrameWidth= width;
		depthFrameHeight= height;
	}
	public void setColorFrameWidth(int width) {
		colorFrameWidth= width;
	}
	public void setColorFrameHeight(int height) {
		colorFrameHeight= height;
	}
	public void setColorFrameSize(int width, int height) {
		colorFrameWidth= width;
		colorFrameHeight= height;
	}
	public void setNumberOfSkeletons(int number) {
		numberOfSkeletons= number;
	}
	public void setCorrectionX(int x) {
		correctionX= x;
	}
	public void setCorrectionY(int y) {
		correctionY= y;
	}
	public void setCorrection(int x, int y) {
		correctionX= x;
		correctionY= y;
	}
}
