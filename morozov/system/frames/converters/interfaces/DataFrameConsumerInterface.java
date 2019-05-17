// (c) 2018 Alexei A. Morozov

package morozov.system.frames.converters.interfaces;

import morozov.system.frames.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;

public interface DataFrameConsumerInterface {
	//
	public boolean sendDataFrame(DataFrameInterface frame);
	public boolean sendCompoundFrame(CompoundFrameInterface frame);
	public boolean sendKinectFrame(KinectFrameInterface frame);
	//
	public void completeDataReading(long numberOfAcquiredFrames);
	public void completeDataReading(long numberOfFrameToBeAcquired, Throwable e);
}
