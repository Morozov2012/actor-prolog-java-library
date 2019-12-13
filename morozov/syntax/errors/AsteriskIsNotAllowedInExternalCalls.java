// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class AsteriskIsNotAllowedInExternalCalls extends ParserError {
	public AsteriskIsNotAllowedInExternalCalls(int p) {
		super(p);
	}
}
