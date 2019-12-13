// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

import morozov.syntax.scanner.*;

public class ThisDeclaratorIsNotAllowedInTheImperativesSection extends ParserError {
	//
	protected PrologToken token;
	//
	public ThisDeclaratorIsNotAllowedInTheImperativesSection(PrologToken t) {
		super(t.getPosition());
		token= t;
	}
	//
	public PrologToken getToken() {
		return token;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(keyword: " + token.toActorPrologTerm().toString() + "; position:" + Integer.toString(position) + ")";
	}
}
