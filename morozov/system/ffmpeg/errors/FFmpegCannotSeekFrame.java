// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

public class FFmpegCannotSeekFrame extends RuntimeException {
	//
	protected String fileName;
	protected long targetFrameNumber;
	protected double targetTime;
	//
	public FFmpegCannotSeekFrame(String name, long n, double t) {
		fileName= name;
		targetFrameNumber= n;
		targetTime= t;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + fileName + ";" + Long.toString(targetFrameNumber) + ";" + Double.toString(targetTime) + ")";
	}
}
