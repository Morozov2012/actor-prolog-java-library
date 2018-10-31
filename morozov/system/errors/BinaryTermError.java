// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.errors;

public class BinaryTermError extends RuntimeException {
	public String text;
	public BinaryTermError(String t) {
		text= t;
	}
	public String toString() {
		return this.getClass().toString() + "(" + text + ")";
	}
}
