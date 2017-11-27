// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.tools;

import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.frames.data.interfaces.*;

public class SkeletonDimensionsTools {
	//
	public static void subtractDimensions(SkeletonDimensionsInterface left, SkeletonDimensionsInterface right, SkeletonDimensionsChangeInterface delta) {
		PlayerDimensionsInterface totalDepthDimensionsLeft= left.getTotalDepthDimensions();
		PlayerDimensionsInterface skeletonDepthDimensionsLeft= left.getSkeletonDepthDimensions();
		PlayerDimensionsInterface totalColorDimensionsLeft= left.getTotalColorDimensions();
		PlayerDimensionsInterface skeletonColorDimensionsLeft= left.getSkeletonColorDimensions();
		PlayerDimensionsInterface totalDepthDimensionsRight= right.getTotalDepthDimensions();
		PlayerDimensionsInterface skeletonDepthDimensionsRight= right.getSkeletonDepthDimensions();
		PlayerDimensionsInterface totalColorDimensionsRight= right.getTotalColorDimensions();
		PlayerDimensionsInterface skeletonColorDimensionsRight= right.getSkeletonColorDimensions();
		delta.updateChangeOfTotalDepthDimensions(totalDepthDimensionsLeft,totalDepthDimensionsRight);
		delta.updateChangeOfSkeletonDepthDimensions(skeletonDepthDimensionsLeft,skeletonDepthDimensionsRight);
		delta.updateChangeOfTotalColorDimensions(totalColorDimensionsLeft,totalColorDimensionsRight);
		delta.updateChangeOfSkeletonColorDimensions(skeletonColorDimensionsLeft,skeletonColorDimensionsRight);
	}
}
