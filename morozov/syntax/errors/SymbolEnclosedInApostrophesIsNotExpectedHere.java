// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class SymbolEnclosedInApostrophesIsNotExpectedHere extends ParserError {
	public SymbolEnclosedInApostrophesIsNotExpectedHere(int p) {
		super(p);
	}
}
