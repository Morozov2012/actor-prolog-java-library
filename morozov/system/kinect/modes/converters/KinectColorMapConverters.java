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
		DetailedColorMap colorMap= ColorMapConverters.argumentToColorMap(attributes,iX);
		if (colorMap.getUseCustomMap()) {
			return new KinectColorMap(colorMap.getColors(),colorMap.getSize(),colorMap.getReverseScale(),colorMap.getReverseMinimalValue(),colorMap.getReverseMaximalValue(),colorMap.getTincturingCoefficient());
		} else {
			return new KinectColorMap(colorMap.getColorMapName(),colorMap.getSize(),colorMap.getReverseScale(),colorMap.getReverseMinimalValue(),colorMap.getReverseMaximalValue(),colorMap.getTincturingCoefficient());
		}
	}
	public static Term toTerm(DetailedColorMap colorMap) {
		return ColorMapConverters.toTerm(colorMap);
	}
}
