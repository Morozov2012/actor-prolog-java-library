// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data;

import morozov.system.kinect.frames.data.interfaces.*;

import java.io.Serializable;

public class SkeletonDimensionsChange implements SkeletonDimensionsChangeInterface, Serializable {
	//
	protected PlayerDimensionsChangeInterface changeOfTotalDepthDimensions= new PlayerDimensionsChange();
	protected PlayerDimensionsChangeInterface changeOfSkeletonsDepthDimensions= new PlayerDimensionsChange();
	protected PlayerDimensionsChangeInterface changeOfTotalColorDimensions= new PlayerDimensionsChange();
	protected PlayerDimensionsChangeInterface changeOfSkeletonsColorDimensions= new PlayerDimensionsChange();
	//
	///////////////////////////////////////////////////////////////
	//
	public SkeletonDimensionsChange() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void updateChangeOfTotalDepthDimensions(PlayerDimensionsInterface left, PlayerDimensionsInterface right) {
		changeOfTotalDepthDimensions.subtract(left,right);
	}
	public void updateChangeOfSkeletonDepthDimensions(PlayerDimensionsInterface left, PlayerDimensionsInterface right) {
		changeOfSkeletonsDepthDimensions.subtract(left,right);
	}
	public void updateChangeOfTotalColorDimensions(PlayerDimensionsInterface left, PlayerDimensionsInterface right) {
		changeOfTotalColorDimensions.subtract(left,right);
	}
	public void updateChangeOfSkeletonColorDimensions(PlayerDimensionsInterface left, PlayerDimensionsInterface right) {
		changeOfSkeletonsColorDimensions.subtract(left,right);
	}
	//
	public void updateChangeOfTotalDepthDimensions(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right) {
		changeOfTotalDepthDimensions.subtract(left,right);
	}
	public void updateChangeOfSkeletonDepthDimensions(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right) {
		changeOfSkeletonsDepthDimensions.subtract(left,right);
	}
	public void updateChangeOfTotalColorDimensions(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right) {
		changeOfTotalColorDimensions.subtract(left,right);
	}
	public void updateChangeOfSkeletonColorDimensions(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right) {
		changeOfSkeletonsColorDimensions.subtract(left,right);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public PlayerDimensionsChangeInterface getChangeOfTotalDepthDimensions() {
		return changeOfTotalDepthDimensions;
	}
	public PlayerDimensionsChangeInterface getChangeOfSkeletonDepthDimensions() {
		return changeOfSkeletonsDepthDimensions;
	}
	public PlayerDimensionsChangeInterface getChangeOfTotalColorDimensions() {
		return changeOfTotalColorDimensions;
	}
	public PlayerDimensionsChangeInterface getChangeOfSkeletonColorDimensions() {
		return changeOfSkeletonsColorDimensions;
	}
}
