// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.interfaces;

public interface SkeletonDimensionsInterface {
	public PlayerDimensionsInterface getTotalDepthDimensions();
	public PlayerDimensionsInterface getSkeletonDepthDimensions();
	public PlayerDimensionsInterface getTotalColorDimensions();
	public PlayerDimensionsInterface getSkeletonColorDimensions();
}
