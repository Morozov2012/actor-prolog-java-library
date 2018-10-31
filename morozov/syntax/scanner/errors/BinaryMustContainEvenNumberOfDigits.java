// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class BinaryMustContainEvenNumberOfDigits extends LexicalScannerError {
	public BinaryMustContainEvenNumberOfDigits(int p) {
		super(p);
	}
}
