// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.gui.space2d.errors;

public class ImageConversionError extends RuntimeException {
	//
	protected Throwable exception;
	//
	public ImageConversionError(Throwable e) {
		super(e);
		exception= e;
	}
	//
	@Override
	public String toString() {
		return exception.toString();
	}
}
