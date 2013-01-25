// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class StringOrSymbolicLiteralIsNotTerminated extends LexicalScannerError {
	public StringOrSymbolicLiteralIsNotTerminated(int p) {
		super(p);
	}
}
