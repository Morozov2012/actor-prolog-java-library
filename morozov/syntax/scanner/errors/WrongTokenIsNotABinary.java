// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class WrongTokenIsNotABinary extends LexicalScannerError {
	public WrongTokenIsNotABinary(int p) {
		super(p);
	}
}
