// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class DomainCannotBeAnAlternativeOfItself extends ParserError {
	public DomainCannotBeAnAlternativeOfItself(int p) {
		super(p);
	}
}
