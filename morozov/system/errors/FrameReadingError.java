// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.errors;

public class FrameReadingError extends RuntimeException {
	//
	protected long numberOfFrameToBeAcquired;
	protected Throwable exception;
	//
	public FrameReadingError(long n, Throwable e) {
		super(e);
		numberOfFrameToBeAcquired= n;
		exception= e;
	}
	//
	public long getNumberOfFrameToBeAcquired() {
		return numberOfFrameToBeAcquired;
	}
	public Throwable getException() {
		return exception;
	}
	//
	public String toString() {
		return exception.toString();
	}
}
