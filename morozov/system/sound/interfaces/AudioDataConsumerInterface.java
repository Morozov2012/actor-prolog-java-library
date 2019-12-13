// (c) 2018 Alexei A. Morozov

package morozov.system.sound.interfaces;

public interface AudioDataConsumerInterface {
	public void setAudioData(byte[] buffer, long time);
	public void implementAudioSystemReset(boolean forgetAudioFormat);
	public void implementAudioFormatReset();
	public void reportMicrophoneAvailability(boolean state);
}
