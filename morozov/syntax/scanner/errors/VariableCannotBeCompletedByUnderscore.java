// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner.errors;

public class VariableCannotBeCompletedByUnderscore extends LexicalScannerError {
	public VariableCannotBeCompletedByUnderscore(int p) {
		super(p);
	}
}
