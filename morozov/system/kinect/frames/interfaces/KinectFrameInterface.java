// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.interfaces;

import morozov.system.kinect.modes.*;

public interface KinectFrameInterface extends KinectSkeletonsFrameInterface {
	public KinectDataArrayType getDataArrayType();
	public long getSerialNumber();
	public long getTargetFrameNumber();
	public long getColorFrameNumber();
	// public long getSkeletonsFrameNumber();
	public long getActingFrameNumber();
	public boolean isProcessed();
	public void setIsProcessed(boolean value);
	public long getTargetFrameTime();
	public long getColorFrameTime();
	// public long getSkeletonsFrameTime();
	public long getActingFrameTime();
	// public GeneralSkeletonInterface[] getSkeletons();
	// public boolean hasTrackedSkeletons();
	// public DimensionsInterface getDimensions();
	public byte[] getPlayerIndex();
	public byte[][] getMappedRed();
	public byte[][] getMappedGreen();
	public byte[][] getMappedBlue();
	// public KinectFrameBaseAttributesInterface getBaseAttributes();
	// public byte getDeviceType();
	// public float getFocalLengthX();
	// public float getFocalLengthY();
	// public int getDepthFrameWidth();
	// public int getDepthFrameHeight();
	// public int getColorFrameWidth();
	// public int getColorFrameHeight();
	// public int getNumberOfSkeletons();
	// public int getCorrectionX();
	// public int getCorrectionY();
	// public boolean isLightweightFrame();
	// public long getReceivedFrameNumber();
	public void putReceivedFrameNumber(long number);
	public KinectSkeletonsFrameInterface extractSkeletonsFrame(long receivedFrameNumber);
}
