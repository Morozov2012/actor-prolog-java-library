// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class UnexpectedCharacter extends LexicalScannerError {
	public UnexpectedCharacter(int p) {
		super(p);
	}
}