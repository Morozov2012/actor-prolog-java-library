// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public class VPM_FrameNumberAndTime {
	//
	protected long frameNumber;
	protected long time;
	//
	public VPM_FrameNumberAndTime(long fN, long t) {
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
