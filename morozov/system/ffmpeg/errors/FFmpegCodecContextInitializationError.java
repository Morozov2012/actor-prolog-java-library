// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

public class FFmpegCodecContextInitializationError extends FFmpegInputOutputError {
	public FFmpegCodecContextInitializationError(String name) {
		super(name);
	}
}
