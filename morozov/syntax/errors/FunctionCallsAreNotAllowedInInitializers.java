// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class FunctionCallsAreNotAllowedInInitializers extends ParserError {
	public FunctionCallsAreNotAllowedInInitializers(int p) {
		super(p);
	}
}
