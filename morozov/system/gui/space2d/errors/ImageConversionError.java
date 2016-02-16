// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d.errors;

public class ImageConversionError extends RuntimeException {
	public Throwable exception;
	public ImageConversionError(Throwable e) {
		super(e);
		exception= e;
	}
	public String toString() {
		return exception.toString();
	}
}
