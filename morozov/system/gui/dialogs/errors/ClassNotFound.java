// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs.errors;

public class ClassNotFound extends RuntimeException {
	//
	protected String className;
	//
	public ClassNotFound(String name) {
		className= name;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + className + ")";
	}
}
