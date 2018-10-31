// (c) 2018 Alexei A. Morozov

package morozov.system.frames.tools;

import morozov.system.frames.interfaces.*;

import java.io.Serializable;

public class EnumeratedFrame implements Serializable {
	//
	protected DataFrameInterface frame;
	protected long numberOfFrame;
	//
	public EnumeratedFrame(
			DataFrameInterface givenFrame,
			long givenNumberOfFrame) {
		frame= givenFrame;
		numberOfFrame= givenNumberOfFrame;
	}
	//
	public DataFrameInterface getFrame() {
		return frame;
	}
	//
	public long getNumberOfFrame() {
		return numberOfFrame;
	}
	//
	public long getTime() {
		return frame.getTime();
	}
}
