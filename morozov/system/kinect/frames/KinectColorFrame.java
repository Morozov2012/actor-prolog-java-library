// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.*;

public class KinectColorFrame extends KinectFrame implements KinectColorFrameInterface {
	//
	protected byte[] color;
	protected float[][] u;
	protected float[][] v;
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectColorFrame(
			long givenSerialNumber,
			long givenTargetFrameNumber,
			long givenColorFrameNumber,
			long givenSkeletonsFrameNumber,
			long givenTargetFrameTime,
			long givenColorFrameTime,
			long givenSkeletonsFrameTime,
			byte[] givenColor,
			GeneralSkeletonInterface[] givenSkeletons,
			DimensionsInterface givenDimensions,
			byte[] givenPlayerIndex,
			float[][] givenU,
			float[][] givenV,
			KinectFrameBaseAttributesInterface givenAttributes) {
		super(	KinectDataArrayType.COLOR_FRAME,
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
			null,
			null,
			null,
			givenAttributes);
		color= givenColor;
		u= givenU;
		v= givenV;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getActingFrameNumber() {
		return colorFrameNumber;
	}
	public long getActingFrameTime() {
		return colorFrameTime;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public byte[] getColor() {
		return color;
	}
	public float[][] getU() {
		return u;
	}
	public float[][] getV() {
		return v;
	}
}
