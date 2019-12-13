// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.interfaces;

import morozov.system.kinect.frames.data.interfaces.*;

public interface KinectForegroundPointCloudsFrameInterface extends KinectPointCloudsFrameInterface {
	public ForegroundPointCloudInterface[] getPointClouds();
	@Override
	public float[] getXYZ();
}
