// (c) 2018 Alexei A. Morozov

package morozov.system.ip_camera.frames;

// import morozov.system.ip_camera.frames.data.*;
import morozov.system.ip_camera.frames.interfaces.*;
import morozov.system.frames.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.modes.*;

public class IPCameraFrame extends DataFrame implements IPCameraFrameInterface {
	//
	protected byte[] byteArray;
	//
	private static final long serialVersionUID= 0x4D2BC0C464F97162L;
	//
	///////////////////////////////////////////////////////////////
	//
	public IPCameraFrame(long n, long t, byte[] array, DataFrameBaseAttributesInterface attributes) {
		super(n,t,attributes);
		byteArray= array;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public DataArrayType getDataArrayType() {
		return DataArrayType.IP_CAMERA_FRAME;
	}
	//
	public byte[] getByteArray() {
		return byteArray;
	}
	//
	public int getDataSize() {
		return byteArray.length;
	}
}
