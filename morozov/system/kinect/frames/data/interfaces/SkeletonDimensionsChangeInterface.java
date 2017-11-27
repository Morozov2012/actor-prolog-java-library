// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.interfaces;

public interface SkeletonDimensionsChangeInterface {
	//
	public void updateChangeOfTotalDepthDimensions(PlayerDimensionsInterface left, PlayerDimensionsInterface right);
	public void updateChangeOfSkeletonDepthDimensions(PlayerDimensionsInterface left, PlayerDimensionsInterface right);
	public void updateChangeOfTotalColorDimensions(PlayerDimensionsInterface left, PlayerDimensionsInterface right);
	public void updateChangeOfSkeletonColorDimensions(PlayerDimensionsInterface left, PlayerDimensionsInterface right);
	//
	public void updateChangeOfTotalDepthDimensions(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right);
	public void updateChangeOfSkeletonDepthDimensions(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right);
	public void updateChangeOfTotalColorDimensions(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right);
	public void updateChangeOfSkeletonColorDimensions(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right);
	//
	public PlayerDimensionsChangeInterface getChangeOfTotalDepthDimensions();
	public PlayerDimensionsChangeInterface getChangeOfSkeletonDepthDimensions();
	public PlayerDimensionsChangeInterface getChangeOfTotalColorDimensions();
	public PlayerDimensionsChangeInterface getChangeOfSkeletonColorDimensions();
}
