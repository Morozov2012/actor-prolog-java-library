// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.checker.errors;

public class WrongArgumentIsMalformedURL extends RuntimeException {
	//
	protected String fileName;
	//
	public WrongArgumentIsMalformedURL(String name, Throwable e) {
		super(e);
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
