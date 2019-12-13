// (c) 2018 Alexei A. Morozov

package morozov.system.i3v1.frames;

import morozov.system.i3v1.frames.data.interfaces.*;
import morozov.system.i3v1.frames.interfaces.*;
import morozov.system.frames.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.modes.*;

public class I3CameraFlashFrame extends DataFrame implements I3CameraFlashFrameInterface {
	//
	protected I3CameraFlashAttributesInterface attributes;
	//
	private static final long serialVersionUID= 0x8BCFEA89D4F4ACCDL; // -8372215304554238771L;
	//
	// static {
	//	System.out.printf("CameraFlashFrame: serialVersionUID: %x\n",serialVersionUID);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public I3CameraFlashFrame(long n, long t, I3CameraFlashAttributesInterface a, DataFrameBaseAttributesInterface givenAttributes) {
		super(n,t,givenAttributes);
		attributes= a;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public DataArrayType getDataArrayType() {
		return DataArrayType.CAMERA_FLASH_FRAME;
	}
	@Override
	public I3CameraFlashAttributesInterface getCameraFlashAttributes() {
		return attributes;
	}
	@Override
	public int getDataSize() {
		return 0;
	}
}
