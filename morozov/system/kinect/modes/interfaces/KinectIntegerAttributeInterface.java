// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.interfaces;

import morozov.system.kinect.modes.interfaces.signals.*;

public interface KinectIntegerAttributeInterface {
	public boolean getUseDefaultValue();
	public long getValue() throws UseDefaultValue;
	public long getValue(long defaultValue);
}
