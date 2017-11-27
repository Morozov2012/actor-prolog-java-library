// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.interfaces;

public interface DimensionsInterface {
	public int getSize();
	public PlayerDimensionsInterface[] getTotalDepthDimensions();
	public PlayerDimensionsInterface getTotalDepthDimensions(int n);
	public PlayerDimensionsInterface[] getSkeletonsDepthDimensions();
	public PlayerDimensionsInterface getSkeletonDepthDimensions(int n);
	public PlayerDimensionsInterface[] getTotalColorDimensions();
	public PlayerDimensionsInterface getTotalColorDimensions(int n);
	public PlayerDimensionsInterface[] getSkeletonsColorDimensions();
	public PlayerDimensionsInterface getSkeletonColorDimensions(int n);
	public SkeletonDimensionsInterface getSkeletonDimensions(int n);
}
