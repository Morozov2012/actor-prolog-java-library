// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

public class FFmpegDataTransferError extends RuntimeException {
	//
	protected long numberOfFrame;
	protected Throwable exception;
	//
	public FFmpegDataTransferError(long n, Throwable e) {
		super(e);
		numberOfFrame= n;
		exception= e;
	}
	//
	public long getNumberOfFrame() {
		return numberOfFrame;
	}
	public Throwable getException() {
		return exception;
	}
	//
	public String toString() {
		return exception.toString();
	}
}
