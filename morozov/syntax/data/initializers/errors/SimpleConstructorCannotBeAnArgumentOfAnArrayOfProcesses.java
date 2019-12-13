// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.initializers.errors;

import morozov.syntax.errors.*;

public class SimpleConstructorCannotBeAnArgumentOfAnArrayOfProcesses extends ParserError {
	public SimpleConstructorCannotBeAnArgumentOfAnArrayOfProcesses(int p) {
		super(p);
	}
}
