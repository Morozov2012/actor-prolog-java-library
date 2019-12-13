// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.converters;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.kinect.modes.*;
import morozov.terms.*;

public class KinectColorMapConverters {
	//
	public static KinectColorMap argumentToKinectColorMap(Term attributes, ChoisePoint iX) {
		IterativeDetailedColorMap colorMap= ColorMapConverters.argumentToIterativeDetailedColorMap(attributes,iX);
		return new KinectColorMap(colorMap);
	}
	public static Term toTerm(KinectColorMap colorMap) {
		return ColorMapConverters.toTerm(colorMap.getIterativeDetailedColorMap());
	}
}
