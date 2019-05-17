// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class MinusIsNotAllowedBeforeExtendedNumbers extends ParserError {
	public MinusIsNotAllowedBeforeExtendedNumbers(int p) {
		super(p);
	}
}
