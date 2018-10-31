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
	private static final long serialVersionUID= 1;
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
	public PlayerDimensionsInterface getTotalDepthDimensions() {
		return totalDepthDimensions;
	}
	public PlayerDimensionsInterface getSkeletonDepthDimensions() {
		return skeletonsDepthDimensions;
	}
	public PlayerDimensionsInterface getTotalColorDimensions() {
		return totalColorDimensions;
	}
	public PlayerDimensionsInterface getSkeletonColorDimensions() {
		return skeletonsColorDimensions;
	}
}
