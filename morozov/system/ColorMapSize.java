// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system;

import morozov.system.signals.*;

import java.io.Serializable;

public class ColorMapSize implements Serializable {
	//
	protected boolean useDefaultSize;
	protected int value;
	//
	public static ColorMapSize instanceDefault= new ColorMapSize();
	//
	public static int defaultColorMapSize= 2400;
	//
	// private static final long serialVersionUID= 1;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system","ColorMapSize");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ColorMapSize(int size) {
		useDefaultSize= false;
		value= size;
	}
	public ColorMapSize() {
		useDefaultSize= true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean getUseDefaultSize() {
		return useDefaultSize;
	}
	public int getValue() throws UseDefaultSize {
		return value;
	}
	public int getValue(int defaultSize) {
		if (useDefaultSize || value <= 0) {
			return defaultSize;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int getDefaultColorMapSize() {
		return defaultColorMapSize;
	}
}
