// (c) 2018 Alexei A. Morozov

package morozov.system.kinect.frames.tools;

import morozov.system.frames.tools.*;
import morozov.system.kinect.frames.interfaces.*;

public class EnumeratedKinectFrame extends EnumeratedFrame {
	//
	protected KinectFrameInterface frame;
	protected long numberOfFrame;
	//
	// private static final long serialVersionUID= 0xFB34B21E58C97F83L; // -345455427992322173L
	private static final long serialVersionUID= 0x14003077E73A6EA3L; // 1441205172297166499L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.tools","EnumeratedKinectFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public EnumeratedKinectFrame(
			KinectFrameInterface givenFrame,
			long givenNumberOfFrame) {
		frame= givenFrame;
		numberOfFrame= givenNumberOfFrame;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectFrameInterface getFrame() {
		return frame;
	}
	//
	public long getNumberOfFrame() {
		return numberOfFrame;
	}
	//
	@Override
	public long getTime() {
		return frame.getActingFrameTime();
	}
}
