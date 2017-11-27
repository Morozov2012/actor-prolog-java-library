// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.interfaces;

public interface GeneralSkeletonInterface {
	//
	public void computeDepthFXY(int correctionX, int correctionY);
	public void computeColorFXY(float[][] u, float[][] v, int correctionX, int correctionY);
	//
	public int getIdentifier();
	public void setIdentifier(int id);
	public boolean isTracked();
	public void setIsTracked(boolean value);
	public long getFrameNumber();
	public void setFrameNumber(long n);
	public long getTime();
	public void setTime(long t);
	//
	public float[] getJointPositions();
	public void setJointPositions(float[] array);
	public float[] getJointOrientations();
	public void setJointOrientations(float[] array);
	public byte[] getJointTrackingStates();
	public void setJointTrackingStates(byte[] array);
	//
	public float get3DJointX(int jointId);
	public float get3DJointY(int jointId);
	public float get3DJointZ(int jointId);
	//
	public float getJointOrientationX(int jointId);
	public float getJointOrientationY(int jointId);
	public float getJointOrientationZ(int jointId);
	public float getJointOrientationW(int jointId);
	//
	public byte getJointTrackingState(int jointId);
	public int getNumberOfJoints();
	//
	public boolean isInitialized_Depth();
	public void setIsInitialized_Depth(boolean value);
	public int[] getDepthX();
	public int[] getDepthY();
	public boolean isInitialized_Color();
	public void setIsInitialized_Color(boolean value);
	public int[] getColorX();
	public int[] getColorY();
	//
	public KinectFrameBaseAttributesInterface getBaseAttributes();
	public void setBaseAttributes(KinectFrameBaseAttributesInterface attributes);
	public byte getDeviceType();
	public float getFocalLengthX();
	public float getFocalLengthY();
	public int getDepthFrameWidth();
	public int getDepthFrameHeight();
	public int getColorFrameWidth();
	public int getColorFrameHeight();
}
