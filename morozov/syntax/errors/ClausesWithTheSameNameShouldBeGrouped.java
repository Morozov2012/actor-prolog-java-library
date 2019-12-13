// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class ClausesWithTheSameNameShouldBeGrouped extends ParserError {
	public ClausesWithTheSameNameShouldBeGrouped(int p) {
		super(p);
	}
}
