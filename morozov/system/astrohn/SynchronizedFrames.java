// (c) 2018 Alexei A. Morozov

package morozov.system.astrohn;

import morozov.system.astrohn.frames.interfaces.*;
import morozov.system.ip_camera.frames.interfaces.*;

import java.io.Serializable;

public class SynchronizedFrames implements Serializable {
	//
	protected THzDataFrameInterface terahertzFrame;
	protected IPCameraFrameInterface colorFrame;
	//
	protected long numberOfTerahertzFrame;
	protected long numberOfColorFrame;
	//
	public SynchronizedFrames(
			THzDataFrameInterface givenTerahertzFrame,
			long givenNumberOfTerahertzFrame,
			IPCameraFrameInterface givenColorFrame,
			long givenNumberOfColorFrame) {
		terahertzFrame= givenTerahertzFrame;
		numberOfTerahertzFrame= givenNumberOfTerahertzFrame;
		colorFrame= givenColorFrame;
		numberOfColorFrame= givenNumberOfColorFrame;
	}
	//
	public THzDataFrameInterface getTerahertzFrame() {
		return terahertzFrame;
	}
	public IPCameraFrameInterface getColorFrame() {
		return colorFrame;
	}
	//
	public long getNumberOfTerahertzFrame() {
		return numberOfTerahertzFrame;
	}
	public long getNumberOfColorFrame() {
		return numberOfColorFrame;
	}
	//
	public long getTime() {
		return terahertzFrame.getTime();
	}
}
