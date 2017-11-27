// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.interfaces;

public interface PlayerDimensionsChangeInterface {
	public void subtract(PlayerDimensionsInterface left, PlayerDimensionsInterface right);
	public void subtract(PlayerDimensionsChangeInterface left, PlayerDimensionsChangeInterface right);
	public boolean areInitialized();
	public float getChangeOfMinimalX();
	public float getChangeOfMaximalX();
	public float getChangeOfMinimalY();
	public float getChangeOfMaximalY();
	public float getChangeOfMinimalZ();
	public float getChangeOfMaximalZ();
}
