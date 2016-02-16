// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

public class ThisIsNotALocalResource extends FileAccessError {
	public ThisIsNotALocalResource(String name) {
		super(name);
	}
}
