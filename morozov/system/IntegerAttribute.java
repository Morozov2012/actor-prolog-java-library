// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.signals.*;

import java.io.Serializable;

public class IntegerAttribute implements Serializable {
	//
	protected boolean useDefaultValue;
	protected long value;
	//
	public static IntegerAttribute instanceDefault= new IntegerAttribute();
	//
	// private static final long serialVersionUID= 0xC2AABA3498F8547L; // 876701795279144263
	private static final long serialVersionUID= 0x2964B349906196E2L; // 2982705981762410210L
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system","IntegerAttribute");
	//	SerialVersionChecker.report("morozov.system","IntegerAttribute");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public IntegerAttribute(long n) {
		useDefaultValue= false;
		value= n;
	}
	public IntegerAttribute() {
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
	public void setValue(long v) {
		value= v;
		useDefaultValue= false;
	}
	//
	public long getValue() throws UseDefaultValue {
		if (useDefaultValue) {
			throw UseDefaultValue.instance;
		} else {
			return value;
		}
	}
	public long getValue(long defaultValue) {
		if (useDefaultValue) {
			return defaultValue;
		} else {
			return value;
		}
	}
	public IntegerAttribute getValue(IntegerAttribute defaultValue) {
		if (useDefaultValue) {
			return defaultValue;
		} else {
			return this;
		}
	}
}
