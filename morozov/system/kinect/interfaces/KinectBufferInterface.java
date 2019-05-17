// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.interfaces;

import morozov.system.kinect.frames.interfaces.*;

public interface KinectBufferInterface {
	//
	public boolean sendKinectFrame(KinectFrameInterface frame);
}
