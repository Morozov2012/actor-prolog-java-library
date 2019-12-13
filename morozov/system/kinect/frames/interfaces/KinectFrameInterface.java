// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.interfaces;

import morozov.system.kinect.modes.*;

public interface KinectFrameInterface extends KinectSkeletonsFrameInterface {
	public KinectDataArrayType getDataArrayType();
	public long getSerialNumber();
	public long getTargetFrameNumber();
	public long getColorFrameNumber();
	public long getActingFrameNumber();
	public boolean isProcessed();
	public void setIsProcessed(boolean value);
	public long getTargetFrameTime();
	public long getColorFrameTime();
	public long getActingFrameTime();
	public byte[] getPlayerIndex();
	public byte[][] getMappedRed();
	public byte[][] getMappedGreen();
	public byte[][] getMappedBlue();
	public void putReceivedFrameNumber(long number);
	public KinectSkeletonsFrameInterface extractSkeletonsFrame(long receivedFrameNumber);
}
