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
