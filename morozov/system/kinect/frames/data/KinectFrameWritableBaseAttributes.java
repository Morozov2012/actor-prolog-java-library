// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data;

import morozov.system.kinect.frames.data.interfaces.*;

public class KinectFrameWritableBaseAttributes extends KinectFrameBaseAttributes implements KinectFrameWritableBaseAttributesInterface {
	//
	private static final long serialVersionUID= 0x54D02B242B716927L; // 6111432128689432871L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.data","KinectFrameWritableBaseAttributes");
	// }
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
	@Override
	public void setDeviceType(byte type) {
		deviceType= type;
	}
	@Override
	public void setFocalLengthX(float x) {
		focalLengthX= x;
	}
	@Override
	public void setFocalLengthY(float y) {
		focalLengthY= y;
	}
	@Override
	public void setFocalLength(float x, float y) {
		focalLengthX= x;
		focalLengthY= y;
	}
	@Override
	public void setDepthFrameWidth(int width) {
		depthFrameWidth= width;
	}
	@Override
	public void setDepthFrameHeight(int height) {
		depthFrameHeight= height;
	}
	@Override
	public void setDepthFrameSize(int width, int height) {
		depthFrameWidth= width;
		depthFrameHeight= height;
	}
	@Override
	public void setColorFrameWidth(int width) {
		colorFrameWidth= width;
	}
	@Override
	public void setColorFrameHeight(int height) {
		colorFrameHeight= height;
	}
	@Override
	public void setColorFrameSize(int width, int height) {
		colorFrameWidth= width;
		colorFrameHeight= height;
	}
	@Override
	public void setNumberOfSkeletons(int number) {
		numberOfSkeletons= number;
	}
	@Override
	public void setCorrectionX(int x) {
		correctionX= x;
	}
	@Override
	public void setCorrectionY(int y) {
		correctionY= y;
	}
	@Override
	public void setCorrection(int x, int y) {
		correctionX= x;
		correctionY= y;
	}
}
