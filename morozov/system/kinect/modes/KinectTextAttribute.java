// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.interfaces.signals.*;
import morozov.system.kinect.modes.tools.*;

import java.io.Serializable;

public class KinectTextAttribute implements KinectTextAttributeInterface, Serializable {
	//
	protected boolean useDefaultValue;
	protected String value;
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectTextAttribute(String s) {
		useDefaultValue= false;
		value= s;
	}
	public KinectTextAttribute() {
		useDefaultValue= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean getUseDefaultValue() {
		return useDefaultValue;
	}
	public String getValue() throws UseDefaultValue {
		return value;
	}
	public String getValue(String defaultValue) {
		if (useDefaultValue) {
			return defaultValue;
		} else {
			return value;
		}
	}
}
