// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.run;

public class LightweightException extends Exception {
	public LightweightException() {
		super(null,null,false,false);
	}
	public LightweightException(String message) {
		super(message,null,false,false);
	}
	public LightweightException(String message, Throwable cause) {
		super(message,cause,false,false);
	}
	public LightweightException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message,cause,enableSuppression,writableStackTrace);
	}
	public LightweightException(Throwable cause) {
		super((cause==null ? null : cause.toString()),cause,false,false);
	}
	public Throwable fillInStackTrace() {
		return this;
	}
	public void setStackTrace(StackTraceElement[] stackTrace) {
	}
}
