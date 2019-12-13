// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data;

import morozov.system.kinect.frames.data.interfaces.*;

import java.io.Serializable;

public class SkeletonDimensions implements SkeletonDimensionsInterface, Serializable {
	//
	protected PlayerDimensionsInterface totalDepthDimensions;
	protected PlayerDimensionsInterface skeletonsDepthDimensions;
	protected PlayerDimensionsInterface totalColorDimensions;
	protected PlayerDimensionsInterface skeletonsColorDimensions;
	//
	// private static final long serialVersionUID= 1;
	private static final long serialVersionUID= 0xB213842520E02CD9L; // -5614999015400657703L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.data","SkeletonDimensions");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public SkeletonDimensions(
			PlayerDimensionsInterface givenTotalDepthDimensions,
			PlayerDimensionsInterface givenSkeletonsDepthDimensions,
			PlayerDimensionsInterface givenTotalColorDimensions,
			PlayerDimensionsInterface givenSkeletonsColorDimensions) {
		totalDepthDimensions= givenTotalDepthDimensions;
		skeletonsDepthDimensions= givenSkeletonsDepthDimensions;
		totalColorDimensions= givenTotalColorDimensions;
		skeletonsColorDimensions= givenSkeletonsColorDimensions;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public PlayerDimensionsInterface getTotalDepthDimensions() {
		return totalDepthDimensions;
	}
	@Override
	public PlayerDimensionsInterface getSkeletonDepthDimensions() {
		return skeletonsDepthDimensions;
	}
	@Override
	public PlayerDimensionsInterface getTotalColorDimensions() {
		return totalColorDimensions;
	}
	@Override
	public PlayerDimensionsInterface getSkeletonColorDimensions() {
		return skeletonsColorDimensions;
	}
}
