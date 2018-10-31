// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.tools;

import morozov.system.kinect.frames.data.interfaces.*;

public class SkeletonDimensionsChangeTools {
	//
	public static void subtractVelocities(SkeletonDimensionsChangeInterface left, SkeletonDimensionsChangeInterface right, SkeletonDimensionsChangeInterface delta) {
		PlayerDimensionsChangeInterface totalDepthDimensionsChangeLeft= left.getChangeOfTotalDepthDimensions();
		PlayerDimensionsChangeInterface skeletonDepthDimensionsChangeLeft= left.getChangeOfSkeletonDepthDimensions();
		PlayerDimensionsChangeInterface totalColorDimensionsChangeLeft= left.getChangeOfTotalColorDimensions();
		PlayerDimensionsChangeInterface skeletonColorDimensionsChangeLeft= left.getChangeOfSkeletonColorDimensions();
		PlayerDimensionsChangeInterface totalDepthDimensionsChangeRight= right.getChangeOfTotalDepthDimensions();
		PlayerDimensionsChangeInterface skeletonDepthDimensionsChangeRight= right.getChangeOfSkeletonDepthDimensions();
		PlayerDimensionsChangeInterface totalColorDimensionsChangeRight= right.getChangeOfTotalColorDimensions();
		PlayerDimensionsChangeInterface skeletonColorDimensionsChangeRight= right.getChangeOfSkeletonColorDimensions();
		delta.updateChangeOfTotalDepthDimensions(totalDepthDimensionsChangeLeft,totalDepthDimensionsChangeRight);
		delta.updateChangeOfSkeletonDepthDimensions(skeletonDepthDimensionsChangeLeft,skeletonDepthDimensionsChangeRight);
		delta.updateChangeOfTotalColorDimensions(totalColorDimensionsChangeLeft,totalColorDimensionsChangeRight);
		delta.updateChangeOfSkeletonColorDimensions(skeletonColorDimensionsChangeLeft,skeletonColorDimensionsChangeRight);
	}
}
