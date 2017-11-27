// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

public class FFmpegCannotSeekFrame extends RuntimeException {
	protected String fileName;
	protected double targetTime;
	public FFmpegCannotSeekFrame(String name, double t) {
		fileName= name;
		targetTime= t;
	}
	public String toString() {
		return this.getClass().toString() + "(" + fileName + ";" + Double.toString(targetTime) + ")";
	}
}
