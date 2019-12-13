// (c) 2018 Alexei A. Morozov

package morozov.system.ip_camera.converters;

import morozov.system.frames.converters.*;
import morozov.system.kinect.modes.*;
import morozov.system.modes.*;

public class IPCameraFrameReadingTask extends DataFrameReadingTask {
	//
	@Override
	protected boolean isTargetArrayType(DataArrayType dataArrayType, CompoundArrayType compoundArrayType, KinectDataArrayType kinectArrayType) {
		boolean answer= false;
		if (dataArrayType != null) {
			if (dataArrayType==DataArrayType.IP_CAMERA_FRAME) {
				answer= true;
			}
		};
		return answer;
	}
}
