// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class FunctionCallsAreNotAllowedInExternalCalls extends ParserError {
	public FunctionCallsAreNotAllowedInExternalCalls(int p) {
		super(p);
	}
}
