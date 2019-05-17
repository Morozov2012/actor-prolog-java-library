// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.converters;

import java.io.Serializable;

public class KinectFrameNumberAndTime implements Serializable {
	//
	protected long frameNumber;
	protected long time;
	//
	private static final long serialVersionUID= 0x86A9D6108C060082L; // -8743221835006082942L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.data.converters","KinectFrameNumberAndTime");
	// }
	//
	public KinectFrameNumberAndTime(long fN, long t) {
		frameNumber= fN;
		time= t;
	}
	//
	public long getFrameNumber() {
		return frameNumber;
	}
	public long getTime() {
		return time;
	}
}
