// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.interfaces;

public interface KinectDepthFrameInterface extends KinectFrameInterface {
	public short[] getDepth();
}
