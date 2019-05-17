// (c) 2018 Alexei A. Morozov

package morozov.system.frames.tools;

import morozov.system.frames.interfaces.*;

public class EnumeratedDataFrame extends EnumeratedFrame {
	//
	protected DataFrameInterface frame;
	protected long numberOfFrame;
	//
	private static final long serialVersionUID= 0x1B1694BB24A5CC09L; // 1951911020006329353L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.frames.tools","EnumeratedDataFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public EnumeratedDataFrame(
			DataFrameInterface givenFrame,
			long givenNumberOfFrame) {
		frame= givenFrame;
		numberOfFrame= givenNumberOfFrame;
	}
	//
	///////////////////////////////////////////////////////////////
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
