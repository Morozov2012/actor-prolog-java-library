// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class WrongTokenIsNotANumber extends LexicalScannerError {
	public WrongTokenIsNotANumber(int p) {
		super(p);
	}
}
