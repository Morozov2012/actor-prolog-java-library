// (c) 2018 Alexei A. Morozov

package morozov.system.sound.frames;

import morozov.system.frames.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.sound.frames.interfaces.*;
import morozov.system.modes.*;

import java.util.Arrays;

public class AudioDataFrame extends DataFrame implements AudioDataFrameInterface {
	//
	protected byte[] data;
	//
	private static final long serialVersionUID= 0x590FCEBE39C1A011L; // 6417575310434344977L;
	//
	// static {
	//	System.out.printf("AudioDataFrame: serialVersionUID: %x\n",serialVersionUID);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public AudioDataFrame(long n, long t, byte[] d, DataFrameBaseAttributesInterface givenAttributes) {
		super(n,t,givenAttributes);
		data= Arrays.copyOf(d,d.length);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public DataArrayType getDataArrayType() {
		return DataArrayType.AUDIO_FRAME;
	}
	@Override
	public byte[] getAudioData() {
		return data;
	}
	@Override
	public int getDataSize() {
		return data.length;
	}
	@Override
	public boolean isLightweightFrame() {
		return true;
	}
}
