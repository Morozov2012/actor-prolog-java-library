// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.errors;

public class FrameTransferError extends RuntimeException {
	//
	protected long numberOfFrame;
	protected Throwable exception;
	//
	public FrameTransferError(long n, Throwable e) {
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
	@Override
	public String toString() {
		return exception.toString();
	}
}
