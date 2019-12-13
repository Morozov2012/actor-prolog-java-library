// (c) 2019 Alexei A. Morozov

package morozov.system.ffmpeg.interfaces;

import static org.bytedeco.javacpp.avutil.*;

public interface FFmpegDataInterface {
	public long getTimeInMilliseconds();
	public long getTimeInBaseUnits();
	public AVRational getTimeBase();
	public AVRational getAverageFrameRate();
	public long getNumber();
}
