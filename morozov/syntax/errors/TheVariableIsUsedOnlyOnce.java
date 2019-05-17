// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class TheVariableIsUsedOnlyOnce extends ParserError {
	public TheVariableIsUsedOnlyOnce(int p) {
		super(p);
	}
}
