// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.signals.*;

import java.io.Serializable;

public class TincturingCoefficient implements Serializable {
	//
	protected boolean useDefaultValue;
	protected double value;
	//
	public static TincturingCoefficient instanceDefault= new TincturingCoefficient();
	//
	protected static double defaultColorMapTincturingCoefficient= 0.5;
	//
	private static final long serialVersionUID= 0xDE84F743D0CC3DC7L; // -2412531629725827641L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system","TincturingCoefficient");
	// }
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
	public void setUseDefaultValue(boolean mode) {
		useDefaultValue= mode;
	}
	public void setUseDefaultValue() {
		setUseDefaultValue(true);
	}
	//
	public boolean getUseDefaultValue() {
		return useDefaultValue;
	}
	//
	public void setValue(double v) {
		value= v;
		useDefaultValue= false;
	}
	//
	public double getValue() throws UseDefaultCoefficient {
		if (useDefaultValue) {
			throw UseDefaultCoefficient.instance;
		} else {
			return value;
		}
	}
	public double getValue(double defaultValue) {
		if (useDefaultValue) {
			return defaultValue;
		} else {
			return value;
		}
	}
	public TincturingCoefficient getValue(TincturingCoefficient defaultValue) {
		if (useDefaultValue) {
			return defaultValue;
		} else {
			return this;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static double getDefaultColorMapTincturingCoefficient() {
		return defaultColorMapTincturingCoefficient;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double getColorMapTincturingCoefficient() {
		return getValue(getDefaultColorMapTincturingCoefficient());
	}
}
