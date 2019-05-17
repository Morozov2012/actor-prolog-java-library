// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class AnonymousVariablesAreNotAllowedInExternalClauses extends ParserError {
	public AnonymousVariablesAreNotAllowedInExternalClauses(int p) {
		super(p);
	}
}
