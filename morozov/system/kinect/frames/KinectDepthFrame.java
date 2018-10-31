// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.modes.*;

public class KinectDepthFrame extends KinectFrame implements KinectDepthFrameInterface {
	//
	protected short[] depth;
	//
	private static final long serialVersionUID= 0x9AED83F3E9867307L; // -7283019938770029817L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames","KinectDepthFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectDepthFrame(
			long givenSerialNumber,
			long givenTargetFrameNumber,
			long givenColorFrameNumber,
			long givenSkeletonsFrameNumber,
			long givenTargetFrameTime,
			long givenColorFrameTime,
			long givenSkeletonsFrameTime,
			short[] givenDepth,
			byte[] givenPlayerIndex,
			GeneralSkeletonInterface[] givenSkeletons,
			DimensionsInterface givenDimensions,
			byte[][] givenMappedRed,
			byte[][] givenMappedGreen,
			byte[][] givenMappedBlue,
			KinectFrameBaseAttributesInterface givenAttributes) {
		super(	KinectDataArrayType.DEPTH_FRAME,
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
		depth= givenDepth;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public short[] getDepth() {
		return depth;
	}
}
