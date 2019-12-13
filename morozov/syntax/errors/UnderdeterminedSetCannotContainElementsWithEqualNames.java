// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class UnderdeterminedSetCannotContainElementsWithEqualNames extends ParserError {
	public UnderdeterminedSetCannotContainElementsWithEqualNames(int p) {
		super(p);
	}
}
