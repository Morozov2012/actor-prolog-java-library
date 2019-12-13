// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.signals.*;

import java.io.Serializable;

public class TextAttribute implements Serializable {
	//
	protected boolean useDefaultValue;
	protected String value;
	//
	public static TextAttribute instanceDefault= new TextAttribute();
	//
	// private static final long serialVersionUID= 0x4DE6F51BBB7C1A5BL; // 5613443485022296667
	private static final long serialVersionUID= 0xCE4D35BEA760D5E1L; // -3581147035721476639L
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system","TextAttribute");
	//	SerialVersionChecker.report("morozov.system","TextAttribute");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public TextAttribute(String s) {
		useDefaultValue= false;
		value= s;
	}
	public TextAttribute() {
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
	public void setValue(String v) {
		value= v;
		useDefaultValue= false;
	}
	//
	public String getValue() throws UseDefaultValue {
		if (useDefaultValue) {
			throw UseDefaultValue.instance;
		} else {
			return value;
		}
	}
	public String getValue(String defaultValue) {
		if (useDefaultValue) {
			return defaultValue;
		} else {
			return value;
		}
	}
	public TextAttribute getValue(TextAttribute defaultValue) {
		if (useDefaultValue) {
			return defaultValue;
		} else {
			return this;
		}
	}
}
