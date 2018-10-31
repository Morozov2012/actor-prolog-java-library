// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.modes.*;

public class KinectMappedColorFrame extends KinectFrame implements KinectMappedColorFrameInterface {
	//
	private static final long serialVersionUID= 0xE00F2F240946023EL; // -2301569052742122946L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames","KinectMappedColorFrame");
	// }
	//
	public KinectMappedColorFrame(
			long givenSerialNumber,
			long givenTargetFrameNumber,
			long givenColorFrameNumber,
			long givenSkeletonsFrameNumber,
			long givenTargetFrameTime,
			long givenColorFrameTime,
			long givenSkeletonsFrameTime,
			byte[][] givenMappedRed,
			byte[][] givenMappedGreen,
			byte[][] givenMappedBlue,
			GeneralSkeletonInterface[] givenSkeletons,
			DimensionsInterface givenDimensions,
			byte[] givenPlayerIndex,
			KinectFrameBaseAttributesInterface givenAttributes) {
		super(	KinectDataArrayType.MAPPED_COLOR_FRAME,
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
	}
}
