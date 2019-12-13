// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.interfaces;

import morozov.system.kinect.frames.interfaces.*;

public interface KinectInterface {
	public void sendDepthFrame(KinectDepthFrameInterface frame);
	public void sendInfraredFrame(KinectInfraredFrameInterface frame);
	public void sendLongExposureInfraredFrame(KinectLongExposureInfraredFrameInterface frame);
	public void sendMappedColorFrame(KinectMappedColorFrameInterface frame);
	public void sendEntirePointCloudsFrame(KinectPointCloudsFrameInterface frame);
	public void sendForegroundPointCloudsFrame(KinectForegroundPointCloudsFrameInterface frame);
	public void sendColorFrame(KinectColorFrameInterface frame);
}
