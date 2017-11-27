// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class KinectLongExposureInfraredFrame extends KinectFrame implements KinectLongExposureInfraredFrameInterface {
	//
	protected short[] longExposureInfrared;
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectLongExposureInfraredFrame(
			long givenSerialNumber,
			long givenTargetFrameNumber,
			long givenColorFrameNumber,
			long givenSkeletonsFrameNumber,
			long givenTargetFrameTime,
			long givenColorFrameTime,
			long givenSkeletonsFrameTime,
			short[] givenLongExposureInfrared,
			GeneralSkeletonInterface[] givenSkeletons,
			DimensionsInterface givenDimensions,
			byte[] givenPlayerIndex,
			byte[][] givenMappedRed,
			byte[][] givenMappedGreen,
			byte[][] givenMappedBlue,
			KinectFrameBaseAttributesInterface givenAttributes) {
		super(	KinectDataArrayType.LONG_EXPOSURE_INFRARED_FRAME,
			givenSerialNumber,
			givenTargetFrameNumber,
			givenColorFrameNumber,
			givenSkeletonsFrameNumber,
			givenTargetFrameTime,
			givenColorFrameTime,
			givenSkeletonsFrameTime,
			givenSkeletons,
			givenDimensions,
			givenPlayerIndex,
			givenMappedRed,
			givenMappedGreen,
			givenMappedBlue,
			givenAttributes);
		longExposureInfrared= givenLongExposureInfrared;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public short[] getLongExposureInfrared() {
		return longExposureInfrared;
	}
}
