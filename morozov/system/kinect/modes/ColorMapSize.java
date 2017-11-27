// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.interfaces.signals.*;

import java.io.Serializable;

public class ColorMapSize implements ColorMapSizeInterface, Serializable {
	//
	protected boolean useDefaultSize;
	protected int value;
	//
	///////////////////////////////////////////////////////////////
	//
	public ColorMapSize(int size) {
		useDefaultSize= false;
		value= size;
	}
	public ColorMapSize() {
		useDefaultSize= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean getUseDefaultSize() {
		return useDefaultSize;
	}
	public int getValue() throws UseDefaultSize {
		return value;
	}
	public int getValue(int defaultSize) {
		if (useDefaultSize || value <= 0) {
			return defaultSize;
		} else {
			return value;
		}
	}
}
