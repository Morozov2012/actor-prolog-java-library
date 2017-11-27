// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.interfaces;

public interface KinectColorFrameInterface extends KinectFrameInterface {
	public byte[] getColor();
	public float[][] getU();
	public float[][] getV();
}
