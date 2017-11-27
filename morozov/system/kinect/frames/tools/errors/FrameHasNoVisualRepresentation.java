// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.tools.errors;

import morozov.system.kinect.frames.*;
import morozov.system.kinect.frames.interfaces.*;

public class FrameHasNoVisualRepresentation extends RuntimeException {
	protected KinectFrameInterface frame;
	public FrameHasNoVisualRepresentation(KinectFrameInterface f) {
		frame= f;
	}
	public String toString() {
		return this.getClass().toString() + "(" + frame.toString() + ")";
	}
}
