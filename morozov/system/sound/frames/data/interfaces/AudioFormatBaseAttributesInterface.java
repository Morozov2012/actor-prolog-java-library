// (c) 2019 Alexei A. Morozov

package morozov.system.sound.frames.data.interfaces;

public interface AudioFormatBaseAttributesInterface {
	//
	public void setChannels(int n);
	public void setEncoding(String name);
	public void setFrameRate(float value);
	public void setFrameSize(int value);
	public void setSampleRate(float value);
	public void setSampleSizeInBits(int size);
	public void setIsBigEndian(boolean mode);
	//
	public int getChannels();
	public String getEncoding();
	public float getFrameRate();
	public int getFrameSize();
	public float getSampleRate();
	public int getSampleSizeInBits();
	public boolean getIsBigEndian();
}
