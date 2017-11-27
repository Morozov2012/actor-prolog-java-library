// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.text.*;
import morozov.system.kinect.frames.tools.*;

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
	public long getReceivedFrameNumber() {
		return receivedFrameNumber;
	}
	public long getSkeletonsFrameNumber() {
		return skeletonsFrameNumber;
	}
	public long getSkeletonsFrameTime() {
		return skeletonsFrameTime;
	}
	public GeneralSkeletonInterface[] getSkeletons() {
		return skeletons;
	}
	public boolean hasTrackedSkeletons() {
		return KinectSkeletonsFrameText.hasTrackedSkeletons(skeletons);
	}
	public DimensionsInterface getDimensions() {
		return dimensions;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectFrameBaseAttributesInterface getBaseAttributes() {
		return baseAttributes;
	}
	public byte getDeviceType() {
		return baseAttributes.getDeviceType();
	}
	public float getFocalLengthX() {
		return baseAttributes.getFocalLengthX();
	}
	public float getFocalLengthY() {
		return baseAttributes.getFocalLengthY();
	}
	public int getDepthFrameWidth() {
		return baseAttributes.getDepthFrameWidth();
	}
	public int getDepthFrameHeight() {
		return baseAttributes.getDepthFrameHeight();
	}
	public int getColorFrameWidth() {
		return baseAttributes.getColorFrameWidth();
	}
	public int getColorFrameHeight() {
		return baseAttributes.getColorFrameHeight();
	}
	public int getNumberOfSkeletons() {
		return baseAttributes.getNumberOfSkeletons();
	}
	public int getCorrectionX() {
		return baseAttributes.getCorrectionX();
	}
	public int getCorrectionY() {
		return baseAttributes.getCorrectionY();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isLightweightFrame() {
		return true;
	}
}
