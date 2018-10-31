// (c) 2018 Alexei A. Morozov

package morozov.system.frames.converters.interfaces;

import morozov.system.frames.interfaces.*;

public interface DataFrameConsumerInterface {
	//
	public boolean sendFrame(DataFrameInterface frame);
	//
	public void completeDataTransfer(long numberOfAcquiredFrames);
	public void completeDataTransfer(long numberOfFrameToBeAcquired, Throwable e);
}
