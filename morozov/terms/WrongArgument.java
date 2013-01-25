// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.terms;

public class WrongArgument extends RuntimeException {
	protected Term argument;
	public WrongArgument(Term value) {
		argument= value;
	}
	public String toString() {
		if (argument != null) {
			return this.getClass().toString() + "(" + argument.toString() + ")";
		} else {
			return this.getClass().toString();
		}
	}
}
