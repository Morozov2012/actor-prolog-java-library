// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

import morozov.syntax.scanner.errors.*;

public class ActorPrologParserError extends RuntimeException {
	//
	protected SyntaxError error;
	//
	public ActorPrologParserError(SyntaxError e) {
		error= e;
	}
	//
	public int getPosition() {
		return error.getPosition();
	}
	//
	public String toString() {
		return error.getClass().toString() + "(position:" + Integer.toString(getPosition()) + ")";
	}
}
