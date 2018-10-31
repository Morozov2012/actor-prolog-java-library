// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.text.*;
import morozov.system.kinect.modes.*;

import java.io.Serializable;

public abstract class KinectFrame implements KinectFrameInterface, Serializable {
	//
	protected KinectDataArrayType dataArrayType;
	protected long receivedFrameNumber= -1;
	protected long serialNumber= -1;
	protected long targetFrameNumber= -1;
	protected long colorFrameNumber= -1;
	protected long skeletonsFrameNumber= -1;
	protected transient boolean isProcessed= false;
	protected long targetFrameTime= -1;
	protected long colorFrameTime= -1;
	protected long skeletonsFrameTime= -1;
	protected GeneralSkeletonInterface[] skeletons;
	protected DimensionsInterface dimensions;
	protected byte[] playerIndex;
	protected byte[][] mappedRed;
	protected byte[][] mappedGreen;
	protected byte[][] mappedBlue;
	protected KinectFrameBaseAttributesInterface baseAttributes;
	//
	private static final long serialVersionUID= 0xE28D06C37FC2DDE9L; // -2122032412689768983L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames","KinectFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectFrame(
			KinectDataArrayType givenDataArrayType,
			long givenSerialNumber,
			long givenTargetFrameNumber,
			long givenColorFrameNumber,
			long givenSkeletonsFrameNumber,
			long givenTargetFrameTime,
			long givenColorFrameTime,
			long givenSkeletonsFrameTime,
			GeneralSkeletonInterface[] givenSkeletons,
			DimensionsInterface givenDimensions,
			byte[] givenPlayerIndex,
			byte[][] givenMappedRed,
			byte[][] givenMappedGreen,
			byte[][] givenMappedBlue,
			KinectFrameBaseAttributesInterface givenAttributes) {
		dataArrayType= givenDataArrayType;
		serialNumber= givenSerialNumber;
		targetFrameNumber= givenTargetFrameNumber;
		colorFrameNumber= givenColorFrameNumber;
		skeletonsFrameNumber= givenSkeletonsFrameNumber;
		targetFrameTime= givenTargetFrameTime;
		colorFrameTime= givenColorFrameTime;
		skeletonsFrameTime= givenSkeletonsFrameTime;
		skeletons= givenSkeletons;
		dimensions= givenDimensions;
		playerIndex= givenPlayerIndex;
		mappedRed= givenMappedRed;
		mappedGreen= givenMappedGreen;
		mappedBlue= givenMappedBlue;
		baseAttributes= givenAttributes;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectDataArrayType getDataArrayType() {
		return dataArrayType;
	}
	public long getSerialNumber() {
		return serialNumber;
	}
	public long getTargetFrameNumber() {
		return targetFrameNumber;
	}
	public long getColorFrameNumber() {
		return colorFrameNumber;
	}
	public long getSkeletonsFrameNumber() {
		return skeletonsFrameNumber;
	}
	public long getActingFrameNumber() {
		return targetFrameNumber;
	}
	public boolean isProcessed() {
		return isProcessed;
	}
	public void setIsProcessed(boolean value) {
		isProcessed= value;
	}
	public long getTargetFrameTime() {
		return targetFrameTime;
	}
	public long getColorFrameTime() {
		return colorFrameTime;
	}
	public long getSkeletonsFrameTime() {
		return skeletonsFrameTime;
	}
	public long getActingFrameTime() {
		return targetFrameTime;
	}
	public GeneralSkeletonInterface[] getSkeletons() {
		// if (skeletons != null) {
		return skeletons;
		// } else {
		//	return emptySkeletonArray;
		// }
	}
	public boolean hasTrackedSkeletons() {
		return KinectSkeletonsFrameText.hasTrackedSkeletons(skeletons);
	}
	public DimensionsInterface getDimensions() {
		return dimensions;
	}
	public byte[] getPlayerIndex() {
		return playerIndex;
	}
	public byte[][] getMappedRed() {
		return mappedRed;
	}
	public byte[][] getMappedGreen() {
		return mappedGreen;
	}
	public byte[][] getMappedBlue() {
		return mappedBlue;
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
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getReceivedFrameNumber() {
		return receivedFrameNumber;
	}
	public void putReceivedFrameNumber(long number) {
		receivedFrameNumber= number;
	}
	//
	public KinectSkeletonsFrame extractSkeletonsFrame(long receivedFrameNumber) {
		return new KinectSkeletonsFrame(
			receivedFrameNumber,
			getSkeletonsFrameNumber(),
			getSkeletonsFrameTime(),
			getSkeletons(),
			getDimensions(),
			getBaseAttributes());
	}
}
