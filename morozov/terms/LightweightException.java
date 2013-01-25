// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

public class LightweightException extends Exception {
	public LightweightException() {
		super(null,null,true,false);
	}
	public LightweightException(String message) {
		super(message,null,true,false);
	}
	public LightweightException(String message, Throwable cause) {
		super(message,cause,true,false);
	}
	public LightweightException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message,cause,enableSuppression,writableStackTrace);
	}
	public LightweightException(Throwable cause) {
		super((cause==null ? null : cause.toString()),cause,true,false);
	}
	public Throwable fillInStackTrace() {
		return this;
	}
	public void setStackTrace(StackTraceElement[] stackTrace) {
	}
}
