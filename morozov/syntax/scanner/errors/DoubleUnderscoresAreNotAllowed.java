// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class DoubleUnderscoresAreNotAllowed extends LexicalScannerError {
	public DoubleUnderscoresAreNotAllowed(int p) {
		super(p);
	}
}
