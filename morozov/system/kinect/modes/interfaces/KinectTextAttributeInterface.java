// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.interfaces;

import morozov.system.kinect.modes.interfaces.signals.*;

public interface KinectTextAttributeInterface {
	public boolean getUseDefaultValue();
	public String getValue() throws UseDefaultValue;
	public String getValue(String defaultValue);
}
