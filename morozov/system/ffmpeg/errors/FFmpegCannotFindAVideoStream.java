// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

public class FFmpegCannotFindAVideoStream extends FFmpegInputOutputError {
	public FFmpegCannotFindAVideoStream(String name) {
		super(name);
	}
}
