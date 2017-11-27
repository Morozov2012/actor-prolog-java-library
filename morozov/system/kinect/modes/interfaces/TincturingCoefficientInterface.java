// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.interfaces;

import morozov.system.kinect.modes.interfaces.signals.*;

public interface TincturingCoefficientInterface {
	//
	public boolean getUseDefaultValue();
	public double getValue() throws UseDefaultCoefficient;
	public double getValue(double defaultValue);
	//
	public double getColorMapTincturingCoefficient();
	public double getPeopleColorsTincturingCoefficient();
}
