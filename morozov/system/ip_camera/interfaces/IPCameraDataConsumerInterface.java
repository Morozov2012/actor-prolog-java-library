// (c) 2018 Alexei A. Morozov

package morozov.system.ip_camera.interfaces;

import morozov.system.errors.*;

public interface IPCameraDataConsumerInterface {
	public void setIPCameraData(byte[] array, long currentTime);
	public void setDataAcquisitionError(DataAcquisitionError error);
}
