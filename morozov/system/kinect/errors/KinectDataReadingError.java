// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.errors;

public class KinectDataReadingError extends RuntimeException {
	//
	protected long numberOfFrameToBeAcquired;
	protected Throwable exception;
	//
	public KinectDataReadingError(long n, Throwable e) {
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
