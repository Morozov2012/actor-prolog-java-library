// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.checker.errors;

public class WrongArgumentIsMalformedURL extends RuntimeException {
	public String fileName;
	//
	public WrongArgumentIsMalformedURL(String name, Throwable e) {
		super(e);
		fileName= name;
	}
	public String toString() {
		if (fileName != null) {
			return this.getClass().toString() + "(" + fileName.toString() + ")";
		} else {
			return this.getClass().toString();
		}
	}
}
