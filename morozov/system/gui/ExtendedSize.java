// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.system.gui;

import morozov.terms.*;

public class ExtendedSize {
	//
	public class UseDefaultSize extends LightweightException {}
	//
	private boolean useDefaultSize= true;
	private double value= 0;
	//
	public ExtendedSize() {
	}
	public ExtendedSize(double v) {
		useDefaultSize= false;
		value= v;
	}
	//
	public void useDefaultSize() {
		useDefaultSize= true;
	}
	public boolean isDefault() {
		return useDefaultSize;
	}
	public void setValue(double v) {
		value= v;
		useDefaultSize= false;
	}
	public double getValue() throws UseDefaultSize {
		if (useDefaultSize) {
			throw new UseDefaultSize();
		} else {
			return value;
		}
	}
	public String toString() {
		return "(" +
			String.format("%B,",useDefaultSize) +
			String.format("%f",value) + ")";
	}
}
