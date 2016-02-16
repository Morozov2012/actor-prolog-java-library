// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

public class FileIsNotFound extends FileAccessError {
	public FileIsNotFound(String name) {
		super(name);
	}
}
