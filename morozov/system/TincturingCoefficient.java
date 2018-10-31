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
	private static final long serialVersionUID= 1;
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
