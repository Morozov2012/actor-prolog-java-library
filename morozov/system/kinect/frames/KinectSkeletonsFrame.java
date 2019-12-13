// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.text.*;

import java.io.Serializable;

public class KinectSkeletonsFrame implements KinectSkeletonsFrameInterface, Serializable {
	//
	protected long receivedFrameNumber= -1;
	protected long skeletonsFrameNumber= -1;
	protected long skeletonsFrameTime= -1;
	protected GeneralSkeletonInterface[] skeletons;
	protected DimensionsInterface dimensions;
	protected KinectFrameBaseAttributesInterface baseAttributes;
	//
	// private static final long serialVersionUID= 1;
	private static final long serialVersionUID= 0xB55CA02725E99E1FL; // -5378247764986978785L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames","KinectSkeletonsFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectSkeletonsFrame(
			long givenReceivedFrameNumber,
			long givenSkeletonsFrameNumber,
			long givenSkeletonsFrameTime,
			GeneralSkeletonInterface[] givenSkeletons,
			DimensionsInterface givenDimensions,
			KinectFrameBaseAttributesInterface givenAttributes) {
		receivedFrameNumber= givenReceivedFrameNumber;
		skeletonsFrameNumber= givenSkeletonsFrameNumber;
		skeletonsFrameTime= givenSkeletonsFrameTime;
		skeletons= givenSkeletons;
		dimensions= givenDimensions;
		baseAttributes= givenAttributes;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public long getReceivedFrameNumber() {
		return receivedFrameNumber;
	}
	@Override
	public long getSkeletonsFrameNumber() {
		return skeletonsFrameNumber;
	}
	@Override
	public long getSkeletonsFrameTime() {
		return skeletonsFrameTime;
	}
	@Override
	public GeneralSkeletonInterface[] getSkeletons() {
		return skeletons;
	}
	@Override
	public boolean hasTrackedSkeletons() {
		return KinectSkeletonsFrameText.hasTrackedSkeletons(skeletons);
	}
	@Override
	public DimensionsInterface getDimensions() {
		return dimensions;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public KinectFrameBaseAttributesInterface getBaseAttributes() {
		return baseAttributes;
	}
	@Override
	public byte getDeviceType() {
		return baseAttributes.getDeviceType();
	}
	@Override
	public float getFocalLengthX() {
		return baseAttributes.getFocalLengthX();
	}
	@Override
	public float getFocalLengthY() {
		return baseAttributes.getFocalLengthY();
	}
	@Override
	public int getDepthFrameWidth() {
		return baseAttributes.getDepthFrameWidth();
	}
	@Override
	public int getDepthFrameHeight() {
		return baseAttributes.getDepthFrameHeight();
	}
	@Override
	public int getColorFrameWidth() {
		return baseAttributes.getColorFrameWidth();
	}
	@Override
	public int getColorFrameHeight() {
		return baseAttributes.getColorFrameHeight();
	}
	@Override
	public int getNumberOfSkeletons() {
		return baseAttributes.getNumberOfSkeletons();
	}
	@Override
	public int getCorrectionX() {
		return baseAttributes.getCorrectionX();
	}
	@Override
	public int getCorrectionY() {
		return baseAttributes.getCorrectionY();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean isLightweightFrame() {
		return true;
	}
}
