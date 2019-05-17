// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

import morozov.syntax.scanner.*;

public class UnexpectedToken extends ParserError {
	//
	protected PrologToken token;
	//
	public UnexpectedToken(PrologToken t) {
		super(t.getPosition());
		token= t;
	}
	//
	public PrologToken getToken() {
		return token;
	}
	//
	public String toString() {
		return this.getClass().toString() + "(token: " + token.toActorPrologTerm().toString() + "; position:" + Integer.toString(position) + ")";
	}
}
