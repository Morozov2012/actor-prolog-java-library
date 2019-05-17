// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class FunctionCallsAreNotAllowedInExternalClauses extends ParserError {
	public FunctionCallsAreNotAllowedInExternalClauses(int p) {
		super(p);
	}
}
