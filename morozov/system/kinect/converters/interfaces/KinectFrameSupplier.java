// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.converters.interfaces;

import morozov.run.*;

public interface KinectFrameSupplier {
	public void registerListener(KinectDeviceInterface device, boolean requireExclusiveAccess, ChoisePoint iX);
	public void suspendListener(KinectDeviceInterface device, ChoisePoint iX);
	public void cancelListener(KinectDeviceInterface device, ChoisePoint iX);
	public void activate(ChoisePoint iX);
	public void stop();
}
