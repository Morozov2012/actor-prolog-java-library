// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

public class FileAccessError extends RuntimeException {
	//
	protected String fileName;
	//
	public FileAccessError(String name) {
		fileName= name;
	}
	//
	@Override
	public String toString() {
		if (fileName != null) {
			return this.getClass().toString() + "(" + fileName.toString() + ")";
		} else {
			return this.getClass().toString();
		}
	}
}
