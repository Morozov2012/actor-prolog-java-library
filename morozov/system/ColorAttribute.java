// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.signals.*;

import java.awt.Color;

import java.io.Serializable;

public class ColorAttribute implements Serializable {
	//
	protected boolean useDefaultColor= true;
	protected Color value= Color.WHITE;
	//
	public static ColorAttribute instanceDefault= new ColorAttribute();
	//
	private static final long serialVersionUID= 0xE6C0E9E203B0E49BL; // -1819197092523875173
	//
	// static {
	//	// SerialVersionChecker.check(serialVersionUID,"morozov.system","ColorAttribute");
	//	SerialVersionChecker.report("morozov.system","ColorAttribute");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ColorAttribute() {
		useDefaultColor= true;
	}
	public ColorAttribute(Color v) {
		useDefaultColor= false;
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setUseDefaultColor(boolean mode) {
		useDefaultColor= mode;
	}
	public void setUseDefaultColor() {
		setUseDefaultColor(true);
	}
	//
	public boolean getUseDefaultValue() {
		return useDefaultColor;
	}
	//
	public void setValue(Color v) {
		value= v;
		useDefaultColor= false;
	}
	//
	public Color getValue() throws UseDefaultColor {
		if (useDefaultColor) {
			throw UseDefaultColor.instance;
		} else {
			return value;
		}
	}
	public Color getValue(Color defaultColor) {
		if (useDefaultColor) {
			return defaultColor;
		} else {
			return value;
		}
	}
	public ColorAttribute getValue(ColorAttribute defaultColor) {
		if (useDefaultColor) {
			return defaultColor;
		} else {
			return this;
		}
	}
}
