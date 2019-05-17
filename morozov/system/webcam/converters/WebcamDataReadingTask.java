// (c) 2019 Alexei A. Morozov

package morozov.system.webcam.converters;

import morozov.system.frames.converters.*;
import morozov.system.kinect.modes.*;
import morozov.system.modes.*;

public class WebcamDataReadingTask extends DataFrameReadingTask {
	//
	protected boolean isTargetArrayType(DataArrayType dataArrayType, CompoundArrayType compoundArrayType, KinectDataArrayType kinectArrayType) {
		boolean answer= false;
		if (dataArrayType != null) {
			if (dataArrayType==DataArrayType.RGB_FRAME) {
				answer= true;
			}
		};
		return answer;
	}
}
