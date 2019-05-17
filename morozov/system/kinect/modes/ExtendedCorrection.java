// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes;

import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.tools.*;

import java.io.Serializable;

public class ExtendedCorrection implements ExtendedCorrectionInterface, Serializable {
	//
	protected boolean useDefaultCorrection= false;
	//
	protected int value;
	//
	// private static final long serialVersionUID= 1;
	private static final long serialVersionUID= 0x3C45241E5A86013DL; // 4342917128455061821L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.modes","ExtendedCorrection");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ExtendedCorrection() {
		useDefaultCorrection= true;
	}
	public ExtendedCorrection(int v) {
		useDefaultCorrection= false;
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean getUseDefaultCorrection() {
		return useDefaultCorrection;
	}
	//
	public int getValue() {
		return value;
	}
	//
	public int getHorizontalCorrection() {
		if (useDefaultCorrection) {
			return ExtendedCorrectionTools.getDefaultHorizontalCorrection();
		} else {
			return value;
		}
	}
	public int getVerticalCorrection() {
		if (useDefaultCorrection) {
			return ExtendedCorrectionTools.getDefaultVerticalCorrection();
		} else {
			return value;
		}
	}
}
