// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d.errors;

public class SceneConversionError extends RuntimeException {
	public Throwable exception;
	public SceneConversionError(Throwable e) {
		super(e);
		exception= e;
	}
	public String toString() {
		return exception.toString();
	}
}
