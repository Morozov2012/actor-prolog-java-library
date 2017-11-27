// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data;

import morozov.system.kinect.frames.data.interfaces.*;

import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

public class SkeletonDimensions implements SkeletonDimensionsInterface, Serializable {
	//
	protected PlayerDimensionsInterface totalDepthDimensions;
	protected PlayerDimensionsInterface skeletonsDepthDimensions;
	protected PlayerDimensionsInterface totalColorDimensions;
	protected PlayerDimensionsInterface skeletonsColorDimensions;
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
