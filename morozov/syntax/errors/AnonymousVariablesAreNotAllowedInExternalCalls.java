// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class AnonymousVariablesAreNotAllowedInExternalCalls extends ParserError {
	public AnonymousVariablesAreNotAllowedInExternalCalls(int p) {
		super(p);
	}
}
