// (c) 2019 Alexei A. Morozov

package morozov.system.ffmpeg;

public class ExportedData {
	//
	protected long time;
	//
	public ExportedData(long t) {
		time= t;
	}
	//
	public long getTime() {
		return time;
	}
	//
	public boolean isVideoFrame() {
		return false;
	}
	public boolean isAudioData() {
		return false;
	}
}
