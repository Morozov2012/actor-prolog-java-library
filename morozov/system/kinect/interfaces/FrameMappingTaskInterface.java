// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.interfaces;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.modes.interfaces.*;

public interface FrameMappingTaskInterface {
	//
	public void initialize(KinectFrameBaseAttributesInterface attributes);
	public void resetCounters();
	public void setModeDataAcquisition(ConsolidatedDataAcquisitionModeInterface mode);
	//
	public int getHorizontalCorrection();
	public int getVerticalCorrection();
	public void setCorrection(int x, int y);
	//
	public int getSkeletonReleaseTime();
	public void setSkeletonReleaseTime(int length);
	public void resetSkeletonReleaseTime();
	//
	public void setDepthFrame(long time, short[] depthFrame, byte[] playerIndex, float[] xyz, float[] uv);
	public void setSkeletonFrame(long time, boolean[] skeletonTracked, float[] positions, float[] orientations, byte[] jointStatus);
	public void setColorFrame(long time, byte[] colorFrame);
	public void setInfraredFrame(long time, short[] infraredFrame);
	public void setLongExposureInfraredFrame(long time, short[] longExposureInfraredFrame);
	//
	public void run();
	public void mapFrames();
}
