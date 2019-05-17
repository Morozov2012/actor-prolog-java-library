// (c) 2018 Alexei A. Morozov

package morozov.system.i3v1.converters;

import morozov.system.frames.converters.*;
import morozov.system.kinect.modes.*;
import morozov.system.modes.*;

public class I3DataReadingTask extends DataFrameReadingTask {
	//
	protected boolean isTargetArrayType(DataArrayType dataArrayType, CompoundArrayType compoundArrayType, KinectDataArrayType kinectArrayType) {
		boolean answer= false;
		if (dataArrayType != null) {
			if (dataArrayType==DataArrayType.DOUBLE_FRAME) {
				answer= true;
			}
		};
		return answer;
	}
}
