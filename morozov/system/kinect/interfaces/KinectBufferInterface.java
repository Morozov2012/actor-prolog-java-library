// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.interfaces;

import morozov.system.kinect.frames.interfaces.*;

public interface KinectBufferInterface {
	//
	public boolean sendFrame(KinectFrameInterface frame);
	public void transferBufferedFrame(KinectFrameInterface frame, int number);
	//
	public void completeDataTransfer(long numberOfAcquiredFrames);
	public void completeDataTransfer(long numberOfFrameToBeAcquired, Throwable e);
	//
	public void reportBufferOverflow();
	public void annulBufferOverflow();
}
