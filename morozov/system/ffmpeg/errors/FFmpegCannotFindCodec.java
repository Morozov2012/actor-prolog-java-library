// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

public class FFmpegCannotFindCodec extends FFmpegInputOutputError {
	public FFmpegCannotFindCodec(String name) {
		super(name);
	}
}
