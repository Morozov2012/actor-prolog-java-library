// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.interfaces;

import morozov.system.kinect.modes.interfaces.signals.*;

public interface ColorMapSizeInterface {
	public boolean getUseDefaultSize();
	public int getValue() throws UseDefaultSize;
	public int getValue(int defaultSize);
}
