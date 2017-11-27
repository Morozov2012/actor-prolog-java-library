// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

public class FileInputOutputError extends RuntimeException {
	//
	public String fileName1;
	public String fileName2;
	//
	public FileInputOutputError(String name, Throwable e) {
		super(e);
		fileName1= name;
		fileName2= null;
	}
	public FileInputOutputError(String name1, String name2, Throwable e) {
		super(e);
		fileName1= name1;
		fileName2= name2;
	}
	//
	public String toString() {
		if (fileName1 != null) {
			if (fileName2 != null) {
				return this.getClass().toString() + "(" + fileName1.toString() + "," + fileName2.toString() + ")";
			} else {
				return this.getClass().toString() + "(" + fileName1.toString() + ")";
			}
		} else {
			return this.getClass().toString();
		}
	}
}
