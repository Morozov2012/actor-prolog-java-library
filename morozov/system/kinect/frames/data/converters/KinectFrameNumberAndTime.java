// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.converters;

import java.io.Serializable;

public class KinectFrameNumberAndTime implements Serializable {
	//
	protected long frameNumber;
	protected long time;
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
