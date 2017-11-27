// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.interfaces;

public interface ExtendedCorrectionInterface {
	//
	public boolean getUseDefaultCorrection();
	public int getValue();
	//
	public int getHorizontalCorrection();
	public int getVerticalCorrection();
}
