// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class TheSourceSectionIsAlreadyDefinedInTheClass extends ParserError {
	public TheSourceSectionIsAlreadyDefinedInTheClass(int p) {
		super(p);
	}
}
