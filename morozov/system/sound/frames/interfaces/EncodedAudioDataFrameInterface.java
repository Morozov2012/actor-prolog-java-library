// (c) 2019 Alexei A. Morozov

package morozov.system.sound.frames.interfaces;

import morozov.system.sound.frames.data.interfaces.*;

public interface EncodedAudioDataFrameInterface extends AudioDataFrameInterface {
	//
	public void setAudioFormat(AudioFormatBaseAttributesInterface format);
	//
	public AudioFormatBaseAttributesInterface getAudioFormat();
	public int getChannels();
	public String getEncoding();
	public float getFrameRate();
	public int getFrameSize();
	public float getSampleRate();
	public int getSampleSizeInBits();
	public boolean getIsBigEndian();
}
