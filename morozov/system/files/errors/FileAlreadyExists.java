// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

public class FileAlreadyExists extends FileAccessError {
	public FileAlreadyExists(String name) {
		super(name);
	}
}
