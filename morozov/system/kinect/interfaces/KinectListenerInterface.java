// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.interfaces;

import morozov.system.kinect.modes.interfaces.*;

public interface KinectListenerInterface {
	//
	public void setHorizontalCorrection(ExtendedCorrectionInterface x);
	public void setVerticalCorrection(ExtendedCorrectionInterface y);
	public void setCorrection(ExtendedCorrectionInterface x, ExtendedCorrectionInterface y);
	public void setCorrection(int x, int y);
	//
	public boolean initializeDevice(ConsolidatedDataAcquisitionModeInterface acquisitionMode);
	//
	public void stop();
	public boolean isConnected();
	public boolean frameMappingIsInitialized();
}
