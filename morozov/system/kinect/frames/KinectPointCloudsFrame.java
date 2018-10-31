// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.modes.*;

public class KinectPointCloudsFrame extends KinectFrame implements KinectPointCloudsFrameInterface {
	//
	protected float[] xyz;
	//
	private static final long serialVersionUID= 0xEBEB4978A2744B01L; // -1446982072799048959L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames","KinectPointCloudsFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectPointCloudsFrame(
			long givenSerialNumber,
			long givenTargetFrameNumber,
			long givenColorFrameNumber,
			long givenSkeletonsFrameNumber,
			long givenTargetFrameTime,
			long givenColorFrameTime,
			long givenSkeletonsFrameTime,
			float[] givenXYZ,
			byte[][] givenMappedRed,
			byte[][] givenMappedGreen,
			byte[][] givenMappedBlue,
			GeneralSkeletonInterface[] givenSkeletons,
			DimensionsInterface givenDimensions,
			byte[] givenPlayerIndex,
			KinectFrameBaseAttributesInterface givenAttributes) {
		super(	KinectDataArrayType.POINT_CLOUDS_FRAME,
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
		xyz= givenXYZ;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public float[] getXYZ() {
		return xyz;
	}
}
