// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class SymbolCannotBeCompletedByUnderscore extends LexicalScannerError {
	public SymbolCannotBeCompletedByUnderscore(int p) {
		super(p);
	}
}
