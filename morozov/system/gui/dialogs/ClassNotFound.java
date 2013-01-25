// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.gui.dialogs;

public class ClassNotFound extends RuntimeException {
	public String className;
	public ClassNotFound(String name) {
		className= name;
	}
}
