// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class ActorPrologLexicalScannerError extends RuntimeException {
	//
	protected LexicalScannerError error;
	//
	public ActorPrologLexicalScannerError(LexicalScannerError e) {
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
