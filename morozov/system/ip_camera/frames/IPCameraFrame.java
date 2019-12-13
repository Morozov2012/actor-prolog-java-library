// (c) 2018 Alexei A. Morozov

package morozov.system.ip_camera.frames;

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
	@Override
	public DataArrayType getDataArrayType() {
		return DataArrayType.IP_CAMERA_FRAME;
	}
	//
	@Override
	public byte[] getByteArray() {
		return byteArray;
	}
	//
	@Override
	public int getDataSize() {
		return byteArray.length;
	}
}
