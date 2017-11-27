// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

import morozov.system.ffmpeg.*;

public class UnknownFFmpegOperatingMode extends RuntimeException {
	protected FFmpegOperatingMode mode;
	public UnknownFFmpegOperatingMode(FFmpegOperatingMode m) {
		mode= m;
	}
	public String toString() {
		return this.getClass().toString() + "(" + mode.toString() + ")";
	}
}
