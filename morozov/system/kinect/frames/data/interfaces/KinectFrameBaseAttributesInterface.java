// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.interfaces;

public interface KinectFrameBaseAttributesInterface {
	public byte getDeviceType();
	public float getFocalLengthX();
	public float getFocalLengthY();
	public int getDepthFrameWidth();
	public int getDepthFrameHeight();
	public int getColorFrameWidth();
	public int getColorFrameHeight();
	public int getNumberOfSkeletons();
	public int getCorrectionX();
	public int getCorrectionY();
}
