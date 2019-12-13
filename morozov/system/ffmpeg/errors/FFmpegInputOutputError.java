// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

public class FFmpegInputOutputError extends RuntimeException {
	//
	protected String fileName;
	protected String message;
	//
	public FFmpegInputOutputError(String name) {
		fileName= name;
	}
	public FFmpegInputOutputError(String name, String m) {
		fileName= name;
		message= m;
	}
	//
	@Override
	public String toString() {
		if (fileName != null) {
			if (message != null) {
				return this.getClass().toString() + "(" + fileName + ';' + message + ")";
			} else {
				return this.getClass().toString() + "(" + fileName + ")";
			}
		} else {
			return this.getClass().toString();
		}
	}
}
