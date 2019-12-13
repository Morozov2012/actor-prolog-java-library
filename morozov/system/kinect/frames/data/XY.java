// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.data.tools.*;

import java.io.Serializable;

public class XY implements XY_Interface, Serializable {
	//
	protected int x;
	protected int y;
	//
	private static final long serialVersionUID= 0x35878389751695F4L; // 3857196232264291828L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.data","XY");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public XY(int w, int h) {
		x= w;
		y= h;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int getX() {
		return x;
	}
	@Override
	public int getX(int shift, float ratio) {
		return shift + (int)(x * ratio);
	}
	//
	@Override
	public int getY() {
		return y;
	}
	@Override
	public int getY(int shift, float ratio) {
		return shift + (int)(y * ratio);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString() {
		return XY_Tools.toString(this);
	}
}
