// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.tools;

import morozov.system.*;

public class TincturingCoefficientTools {
	//
	protected static double defaultPeopleColorsTincturingCoefficient= 1.0;
	//
	///////////////////////////////////////////////////////////////
	//
	public static double getDefaultPeopleColorsTincturingCoefficient() {
		return defaultPeopleColorsTincturingCoefficient;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static double getPeopleColorsTincturingCoefficient(TincturingCoefficient tincturingCoefficient) {
		return tincturingCoefficient.getValue(getDefaultPeopleColorsTincturingCoefficient());
	}
}
