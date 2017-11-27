// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.tools;

import morozov.system.kinect.modes.interfaces.*;

public class ExtendedCorrectionTools {
	//
	protected static int defaultCorrectionX= 5;
	protected static int defaultCorrectionY= 12;
	//
	protected static String stringDefault= "default";
	//
	///////////////////////////////////////////////////////////////
	//
	public static int getDefaultHorizontalCorrection() {
		return defaultCorrectionX;
	}
	public static int getDefaultVerticalCorrection() {
		return defaultCorrectionY;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ExtendedCorrectionInterface correction) {
		if (correction.getUseDefaultCorrection()) {
			return stringDefault;
		} else {
			return Integer.toString(correction.getValue());
		}
	}
}
