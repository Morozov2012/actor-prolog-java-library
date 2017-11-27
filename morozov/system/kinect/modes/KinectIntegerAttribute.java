// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.interfaces.signals.*;
import morozov.system.kinect.modes.tools.*;

import java.io.Serializable;

public class KinectIntegerAttribute implements KinectIntegerAttributeInterface, Serializable {
	//
	protected boolean useDefaultValue;
	protected long value;
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectIntegerAttribute(long n) {
		useDefaultValue= false;
		value= n;
	}
	public KinectIntegerAttribute() {
		useDefaultValue= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean getUseDefaultValue() {
		return useDefaultValue;
	}
	public long getValue() throws UseDefaultValue {
		return value;
	}
	public long getValue(long defaultValue) {
		if (useDefaultValue) {
			return defaultValue;
		} else {
			return value;
		}
	}
}
