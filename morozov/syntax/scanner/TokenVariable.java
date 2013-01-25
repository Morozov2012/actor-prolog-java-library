// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class TokenVariable extends PrologToken {
	protected String name;
	public TokenVariable(String s, int position) {
		super(position);
		name= s;
	}
	public PrologTokenType getType() {
		return PrologTokenType.VARIABLE;
	}
	public String getVariableName() {
		return name;
	}
	public String toString() {
		return name;
	}
}
