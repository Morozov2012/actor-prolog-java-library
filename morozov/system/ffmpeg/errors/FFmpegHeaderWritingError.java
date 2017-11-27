// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

public class FFmpegHeaderWritingError extends FFmpegInputOutputError {
	public FFmpegHeaderWritingError(String fileName, String message) {
		super(fileName,message);
	}
}
