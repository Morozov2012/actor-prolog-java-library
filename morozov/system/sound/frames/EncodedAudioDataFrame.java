// (c) 2019 Alexei A. Morozov

package morozov.system.sound.frames;

import morozov.system.frames.data.interfaces.*;
import morozov.system.sound.frames.data.interfaces.*;
import morozov.system.sound.frames.interfaces.*;

public class EncodedAudioDataFrame
		extends AudioDataFrame
		implements EncodedAudioDataFrameInterface {
	//
	protected AudioFormatBaseAttributesInterface audioFormat;
	//
	private static final long serialVersionUID= 0x25FDE3473CD56D0BL; // 2737594043613932811
	//
	// static {
	//	SerialVersionChecker.report("morozov.system.sound.frames","EncodedAudioDataFrame");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public EncodedAudioDataFrame(
			long n,
			long t,
			byte[] d,
			AudioFormatBaseAttributesInterface givenAudioFormat,
			DataFrameBaseAttributesInterface givenAttributes) {
		super(n,t,d,givenAttributes);
		audioFormat= givenAudioFormat;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setAudioFormat(AudioFormatBaseAttributesInterface format) {
		audioFormat= format;
	}
	//
	@Override
	public AudioFormatBaseAttributesInterface getAudioFormat() {
		return audioFormat;
	}
	//
	@Override
	public int getChannels() {
		return audioFormat.getChannels();
	}
	@Override
	public String getEncoding() {
		return audioFormat.getEncoding();
	}
	@Override
	public float getFrameRate() {
		return audioFormat.getFrameRate();
	}
	@Override
	public int getFrameSize() {
		return audioFormat.getFrameSize();
	}
	@Override
	public float getSampleRate() {
		return audioFormat.getSampleRate();
	}
	@Override
	public int getSampleSizeInBits() {
		return audioFormat.getSampleSizeInBits();
	}
	@Override
	public boolean getIsBigEndian() {
		return audioFormat.getIsBigEndian();
	}
}
