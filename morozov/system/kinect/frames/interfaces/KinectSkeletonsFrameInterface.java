// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.interfaces;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.tools.*;

public interface KinectSkeletonsFrameInterface {
	public long getReceivedFrameNumber();
	public long getSkeletonsFrameNumber();
	public long getSkeletonsFrameTime();
	public GeneralSkeletonInterface[] getSkeletons();
	public boolean hasTrackedSkeletons();
	public DimensionsInterface getDimensions();
	public KinectFrameBaseAttributesInterface getBaseAttributes();
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
	public boolean isLightweightFrame();
}
