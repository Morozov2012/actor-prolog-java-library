// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.errors;

import morozov.syntax.errors.*;

public class MetaPredicateIsNotAllowedInFunctionDeclaration extends ParserError {
	public MetaPredicateIsNotAllowedInFunctionDeclaration(int p) {
		super(p);
	}
}
