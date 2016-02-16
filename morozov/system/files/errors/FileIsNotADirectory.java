// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

public class FileIsNotADirectory extends FileAccessError {
	public FileIsNotADirectory(String name) {
		super(name);
	}
}
