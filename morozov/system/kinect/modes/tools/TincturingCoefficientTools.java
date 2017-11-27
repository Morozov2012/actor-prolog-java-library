// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.tools;

import morozov.system.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.interfaces.signals.*;

public class TincturingCoefficientTools {
	//
	public static TincturingCoefficient instanceDefault= new TincturingCoefficient();
	//
	///////////////////////////////////////////////////////////////
	//
	protected static double defaultColorMapTincturingCoefficient= 0.5;
	protected static double defaultPeopleColorsTincturingCoefficient= 1.0;
	//
	///////////////////////////////////////////////////////////////
	//
	public static double getDefaultColorMapTincturingCoefficient() {
		return defaultColorMapTincturingCoefficient;
	}
	public static double getDefaultPeopleColorsTincturingCoefficient() {
		return defaultPeopleColorsTincturingCoefficient;
	}
}
