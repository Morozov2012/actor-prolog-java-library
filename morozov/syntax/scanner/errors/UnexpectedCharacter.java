// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class UnexpectedCharacter extends LexicalScannerError {
	//
	int code;
	//
	public UnexpectedCharacter(int c, int p) {
		super(p);
		code= c;
	}
	//
	public String toString() {
		return this.getClass().toString() + "(character: " + String.format("%c",code) + "; position:" + Integer.toString(position) + ")";
	}
}
