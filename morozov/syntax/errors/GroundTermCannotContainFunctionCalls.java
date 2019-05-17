// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class GroundTermCannotContainFunctionCalls extends ParserError {
	public GroundTermCannotContainFunctionCalls(int p) {
		super(p);
	}
}
