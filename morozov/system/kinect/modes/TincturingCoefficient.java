// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.interfaces.signals.*;
import morozov.system.kinect.modes.tools.*;

import java.io.Serializable;

public class TincturingCoefficient implements TincturingCoefficientInterface, Serializable {
	//
	protected boolean useDefaultValue;
	protected double value;
	//
	///////////////////////////////////////////////////////////////
	//
	public TincturingCoefficient(double c) {
		useDefaultValue= false;
		value= c;
	}
	public TincturingCoefficient() {
		useDefaultValue= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean getUseDefaultValue() {
		return useDefaultValue;
	}
	public double getValue() throws UseDefaultCoefficient {
		return value;
	}
	public double getValue(double defaultValue) {
		if (useDefaultValue) {
			return defaultValue;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double getColorMapTincturingCoefficient() {
		return getValue(TincturingCoefficientTools.getDefaultColorMapTincturingCoefficient());
	}
	public double getPeopleColorsTincturingCoefficient() {
		return getValue(TincturingCoefficientTools.getDefaultPeopleColorsTincturingCoefficient());
	}
}
