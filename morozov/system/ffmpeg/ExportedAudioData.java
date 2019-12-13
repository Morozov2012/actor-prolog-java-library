// (c) 2019 Alexei A. Morozov

package morozov.system.ffmpeg;

public class ExportedAudioData extends ExportedData {
	//
	protected byte[] audioData;
	//
	public ExportedAudioData(byte[] array, long time) {
		super(time);
		audioData= array;
	}
	//
	public byte[] getAudioData() {
		return audioData;
	}
	//
	@Override
	public boolean isAudioData() {
		return true;
	}
}
