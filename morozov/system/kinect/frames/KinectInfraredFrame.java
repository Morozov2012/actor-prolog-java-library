// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.modes.*;

public class KinectInfraredFrame extends KinectFrame implements KinectInfraredFrameInterface {
	//
	protected short[] infrared;
	//
	private static final long serialVersionUID= 0xD3B5292755B74FFBL; // -3191599512003588101L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames","KinectInfraredFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectInfraredFrame(
			long givenSerialNumber,
			long givenTargetFrameNumber,
			long givenColorFrameNumber,
			long givenSkeletonsFrameNumber,
			long givenTargetFrameTime,
			long givenColorFrameTime,
			long givenSkeletonsFrameTime,
			short[] givenInfrared,
			GeneralSkeletonInterface[] givenSkeletons,
			DimensionsInterface givenDimensions,
			byte[] givenPlayerIndex,
			byte[][] givenMappedRed,
			byte[][] givenMappedGreen,
			byte[][] givenMappedBlue,
			KinectFrameBaseAttributesInterface givenAttributes) {
		super(	KinectDataArrayType.INFRARED_FRAME,
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
		infrared= givenInfrared;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public short[] getInfrared() {
		return infrared;
	}
}
