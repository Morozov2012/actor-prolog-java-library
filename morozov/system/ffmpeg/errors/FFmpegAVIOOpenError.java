// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

public class FFmpegAVIOOpenError extends FFmpegInputOutputError {
	public FFmpegAVIOOpenError(String fileName, String message) {
		super(fileName,message);
	}
}
