// (c) 2018 Alexei A. Morozov

package morozov.system.astrohn.converters;

import morozov.system.frames.converters.*;
import morozov.system.kinect.modes.*;
import morozov.system.modes.*;

public class AstrohnDataReadingTask extends DataFrameReadingTask {
	//
	protected boolean isTargetArrayType(DataArrayType dataArrayType, CompoundArrayType compoundArrayType, KinectDataArrayType kinectArrayType) {
		boolean answer= false;
		if (dataArrayType != null) {
			if (dataArrayType==DataArrayType.THZ_FRAME) {
				answer= true;
			}
		};
		return answer;
	}
}
