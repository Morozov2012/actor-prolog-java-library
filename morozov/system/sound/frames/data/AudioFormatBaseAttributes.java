// (c) 2019 Alexei A. Morozov

package morozov.system.sound.frames.data;

import morozov.system.sound.frames.data.interfaces.*;

import java.io.Serializable;
import java.lang.Comparable;

public class AudioFormatBaseAttributes
		implements
			AudioFormatBaseAttributesInterface,
			Comparable<AudioFormatBaseAttributes>,
			Serializable {
	//
	protected int channels;
	protected String encoding;
	protected float frameRate;
	protected int frameSize;
	protected float sampleRate;
	protected int sampleSizeInBits;
	protected boolean isBigEndian;
	//
	private static final long serialVersionUID= 0x56985C3A20164960L; // 6239838688438012256
	//
	// static {
	//	SerialVersionChecker.report("morozov.system.sound.frames.data","AudioFormatBaseAttributes");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public AudioFormatBaseAttributes(
			int givenChannels,
			String givenEncoding,
			float givenFrameRate,
			int givenFrameSize,
			float givenSampleRate,
			int givenSampleSizeInBits,
			boolean givenIsBigEndian
			) {
		channels= givenChannels;
		encoding= givenEncoding;
		frameRate= givenFrameRate;
		frameSize= givenFrameSize;
		sampleRate= givenSampleRate;
		sampleSizeInBits= givenSampleSizeInBits;
		isBigEndian= givenIsBigEndian;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setChannels(int n) {
		channels= n;
	}
	@Override
	public void setEncoding(String name) {
		encoding= name;
	}
	@Override
	public void setFrameRate(float value) {
		frameRate= value;
	}
	@Override
	public void setFrameSize(int value) {
		frameSize= value;
	}
	@Override
	public void setSampleRate(float value) {
		sampleRate= value;
	}
	@Override
	public void setSampleSizeInBits(int size) {
		sampleSizeInBits= size;
	}
	@Override
	public void setIsBigEndian(boolean mode) {
		isBigEndian= mode;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int getChannels() {
		return channels;
	}
	@Override
	public String getEncoding() {
		return encoding;
	}
	@Override
	public float getFrameRate() {
		return frameRate;
	}
	@Override
	public int getFrameSize() {
		return frameSize;
	}
	@Override
	public float getSampleRate() {
		return sampleRate;
	}
	@Override
	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}
	@Override
	public boolean getIsBigEndian() {
		return isBigEndian;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int compareTo(AudioFormatBaseAttributes o) {
		AudioFormatBaseAttributes object= (AudioFormatBaseAttributes)o;
		int result= Integer.compare(object.getChannels(),getChannels());
		if (result==0) {
			result= Integer.compare(object.getSampleSizeInBits(),getSampleSizeInBits());
		};
		if (result==0) {
			result= Integer.compare(object.getFrameSize(),getFrameSize());
		};
		if (result==0) {
			result= Boolean.compare(getIsBigEndian(),object.getIsBigEndian());
		};
		if (result==0) {
			boolean isUnsigned1= getEncoding().toUpperCase().contains("UNSIGNED");
			boolean isUnsigned2= object.getEncoding().toUpperCase().contains("UNSIGNED");
			result= Boolean.compare(isUnsigned1,isUnsigned2);
		};
		if (result==0) {
			float frameRate1= object.getFrameRate();
			float frameRate2= getFrameRate();
			if (frameRate1 > 0 && frameRate2 > 0) {
				result= Float.compare(frameRate1,frameRate2);
			}
		};
		if (result==0) {
			float sampleRate1= object.getSampleRate();
			float sampleRate2= getSampleRate();
			if (sampleRate1 > 0 && sampleRate2 > 0) {
				result= Float.compare(sampleRate1,sampleRate2);
			}
		};
		return result;
	}
}
