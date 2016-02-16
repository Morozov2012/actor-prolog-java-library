// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

public class DirectoryDoesNotExist extends FileAccessError {
	public DirectoryDoesNotExist(String name) {
		super(name);
	}
}
