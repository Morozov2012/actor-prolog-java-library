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
	// private static final long serialVersionUID= 1;
	private static final long serialVersionUID= 0xEE3A756E2C7244A7L; // -1280582027980946265L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.system.kinect.frames.data","SkeletonDimensionsChange");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public SkeletonDimensionsChange() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void updateChangeOfTotalDepthDimensions(PlayerDimensionsInterface left, PlayerDimensionsInterface right) {
		changeOfTotalDepthDimensions.subtract(left,right);
	}
	@Override
	public void updateChangeOfSkeletonDepthDimensions(PlayerDimensionsInterface left, PlayerDimensionsInterface right) {
		changeOfSkeletonsDepthDimensions.subtract(left,right);
	}
	@Override
	public void updateChangeOfTotalColorDimensions(PlayerDimensionsInterface left, PlayerDimensionsInterface right) {
		changeOfTotalColorDimensions.subtract(left,right);
	}
	@Override
	public void updateChangeOfSkeletonColorDimensions(PlayerDimensionsInterface left, PlayerDimensionsInterface right) {
		changeOfSkeletonsColorDimensions.subtract(left,right);
	}
	//
	@Override
	public void updateChangeOfTotalDepthDimensions(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right) {
		changeOfTotalDepthDimensions.subtract(left,right);
	}
	@Override
	public void updateChangeOfSkeletonDepthDimensions(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right) {
		changeOfSkeletonsDepthDimensions.subtract(left,right);
	}
	@Override
	public void updateChangeOfTotalColorDimensions(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right) {
		changeOfTotalColorDimensions.subtract(left,right);
	}
	@Override
	public void updateChangeOfSkeletonColorDimensions(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right) {
		changeOfSkeletonsColorDimensions.subtract(left,right);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public PlayerDimensionsChangeInterface getChangeOfTotalDepthDimensions() {
		return changeOfTotalDepthDimensions;
	}
	@Override
	public PlayerDimensionsChangeInterface getChangeOfSkeletonDepthDimensions() {
		return changeOfSkeletonsDepthDimensions;
	}
	@Override
	public PlayerDimensionsChangeInterface getChangeOfTotalColorDimensions() {
		return changeOfTotalColorDimensions;
	}
	@Override
	public PlayerDimensionsChangeInterface getChangeOfSkeletonColorDimensions() {
		return changeOfSkeletonsColorDimensions;
	}
}
