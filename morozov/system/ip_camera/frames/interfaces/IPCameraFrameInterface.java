// (c) 2018 Alexei A. Morozov

package morozov.system.ip_camera.frames.interfaces;

import morozov.system.frames.interfaces.*;

public interface IPCameraFrameInterface extends DataFrameInterface {
	public byte[] getByteArray();
}
