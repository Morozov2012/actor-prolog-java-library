// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.signals.*;

import java.io.Serializable;

public class RealAttribute implements Serializable {
	//
	protected boolean useDefaultValue;
	protected double value;
	//
	public static RealAttribute instanceDefault= new RealAttribute();
	//
	private static final long serialVersionUID= 0x637DA8D5DE4F9DE9L; // 7169071818354957801
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system","RealAttribute");
	//	SerialVersionChecker.report("morozov.system","RealAttribute");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public RealAttribute(double n) {
		useDefaultValue= false;
		value= n;
	}
	public RealAttribute() {
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
	public double getValue() throws UseDefaultValue {
		if (useDefaultValue) {
			throw UseDefaultValue.instance;
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
	public RealAttribute getValue(RealAttribute defaultValue) {
		if (useDefaultValue) {
			return defaultValue;
		} else {
			return this;
		}
	}
}
