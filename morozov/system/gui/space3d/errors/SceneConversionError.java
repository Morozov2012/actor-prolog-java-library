// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.gui.space3d.errors;

public class SceneConversionError extends RuntimeException {
	//
	protected Throwable exception;
	//
	public SceneConversionError(Throwable e) {
		super(e);
		exception= e;
	}
	//
	@Override
	public String toString() {
		return exception.toString();
	}
}
