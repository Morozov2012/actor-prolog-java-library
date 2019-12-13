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
	@Override
	public KinectDataArrayType getDataArrayType() {
		return dataArrayType;
	}
	@Override
	public long getSerialNumber() {
		return serialNumber;
	}
	@Override
	public long getTargetFrameNumber() {
		return targetFrameNumber;
	}
	@Override
	public long getColorFrameNumber() {
		return colorFrameNumber;
	}
	@Override
	public long getSkeletonsFrameNumber() {
		return skeletonsFrameNumber;
	}
	@Override
	public long getActingFrameNumber() {
		return targetFrameNumber;
	}
	@Override
	public boolean isProcessed() {
		return isProcessed;
	}
	@Override
	public void setIsProcessed(boolean value) {
		isProcessed= value;
	}
	@Override
	public long getTargetFrameTime() {
		return targetFrameTime;
	}
	@Override
	public long getColorFrameTime() {
		return colorFrameTime;
	}
	@Override
	public long getSkeletonsFrameTime() {
		return skeletonsFrameTime;
	}
	@Override
	public long getActingFrameTime() {
		return targetFrameTime;
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
	@Override
	public byte[] getPlayerIndex() {
		return playerIndex;
	}
	@Override
	public byte[][] getMappedRed() {
		return mappedRed;
	}
	@Override
	public byte[][] getMappedGreen() {
		return mappedGreen;
	}
	@Override
	public byte[][] getMappedBlue() {
		return mappedBlue;
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
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public long getReceivedFrameNumber() {
		return receivedFrameNumber;
	}
	@Override
	public void putReceivedFrameNumber(long number) {
		receivedFrameNumber= number;
	}
	//
	@Override
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
