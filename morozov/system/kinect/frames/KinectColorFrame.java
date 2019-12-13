// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.modes.*;

public class KinectColorFrame extends KinectFrame implements KinectColorFrameInterface {
	//
	protected byte[] color;
	protected float[][] u;
	protected float[][] v;
	//
	private static final long serialVersionUID= 0x1AA1BBB236392CDDL; // 1919021290324831453L;
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames","KinectColorFrame");
	// }
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
	@Override
	public long getActingFrameNumber() {
		return colorFrameNumber;
	}
	@Override
	public long getActingFrameTime() {
		return colorFrameTime;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public byte[] getColor() {
		return color;
	}
	@Override
	public float[][] getU() {
		return u;
	}
	@Override
	public float[][] getV() {
		return v;
	}
}
