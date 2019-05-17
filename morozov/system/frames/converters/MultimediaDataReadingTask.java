// (c) 2018 Alexei A. Morozov

package morozov.system.frames.converters;

import morozov.system.kinect.modes.*;
import morozov.system.modes.*;

public class MultimediaDataReadingTask extends DataFrameReadingTask {
	//
	protected boolean isTargetArrayType(DataArrayType dataArrayType, CompoundArrayType compoundArrayType, KinectDataArrayType kinectArrayType) {
		boolean answer= false;
		if (compoundArrayType != null) {
			if (compoundArrayType==CompoundArrayType.DATA_FRAME) {
				answer= true;
			} else if (compoundArrayType==CompoundArrayType.CONTROL_FRAME) {
				answer= true;
			}
		};
		return answer;
	}
}
