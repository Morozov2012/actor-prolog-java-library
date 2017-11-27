// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.interfaces;

public interface KinectFrameWritableBaseAttributesInterface extends KinectFrameBaseAttributesInterface {
	public void setDeviceType(byte type);
	public void setFocalLengthX(float x);
	public void setFocalLengthY(float y);
	public void setFocalLength(float x, float y);
	public void setDepthFrameWidth(int width);
	public void setDepthFrameHeight(int height);
	public void setDepthFrameSize(int width, int height);
	public void setColorFrameWidth(int width);
	public void setColorFrameHeight(int height);
	public void setColorFrameSize(int width, int height);
	public void setNumberOfSkeletons(int number);
	public void setCorrectionX(int x);
	public void setCorrectionY(int y);
	public void setCorrection(int x, int y);
}
