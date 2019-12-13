// (c) 2018 Alexei A. Morozov

package morozov.system.frames.tools;

import morozov.system.frames.interfaces.*;

public class EnumeratedCompoundFrame extends EnumeratedFrame {
	//
	protected CompoundFrameInterface frame;
	protected long numberOfFrame;
	//
	private static final long serialVersionUID= 0xC5548B965E96D1BBL; // -4227600672245755461L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.frames.tools","EnumeratedCompoundFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public EnumeratedCompoundFrame(
			CompoundFrameInterface givenFrame,
			long givenNumberOfFrame) {
		frame= givenFrame;
		numberOfFrame= givenNumberOfFrame;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public CompoundFrameInterface getFrame() {
		return frame;
	}
	//
	public long getNumberOfFrame() {
		return numberOfFrame;
	}
	//
	@Override
	public long getTime() {
		return frame.getTime();
	}
}
