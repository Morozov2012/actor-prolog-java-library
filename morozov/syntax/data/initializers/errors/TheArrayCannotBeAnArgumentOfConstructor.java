// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.initializers.errors;

import morozov.syntax.errors.*;

public class TheArrayCannotBeAnArgumentOfConstructor extends ParserError {
	public TheArrayCannotBeAnArgumentOfConstructor(int p) {
		super(p);
	}
}
