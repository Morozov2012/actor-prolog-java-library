// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class NegativeNumbersAreNotAllowedHere extends ParserError {
	public NegativeNumbersAreNotAllowedHere(int p) {
		super(p);
	}
}
