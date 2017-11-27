// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.interfaces;

public interface XY_Interface {
	public int getX();
	public int getX(int shift, float ratio);
	public int getY();
	public int getY(int shift, float ratio);
}
