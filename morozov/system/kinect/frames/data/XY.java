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
	///////////////////////////////////////////////////////////////
	//
	public XY(int w, int h) {
		x= w;
		y= h;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getX() {
		return x;
	}
	public int getX(int shift, float ratio) {
		return shift + (int)(x * ratio);
	}
	//
	public int getY() {
		return y;
	}
	public int getY(int shift, float ratio) {
		return shift + (int)(y * ratio);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString() {
		return XY_Tools.toString(this);
	}
}
