// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class TokenString extends PrologToken {
	protected String value;
	public TokenString(String s, int position) {
		super(position);
		value= s;
	}
	public PrologTokenType getType() {
		return PrologTokenType.STRING;
	}
	public String getStringValue() {
		return value;
	}
	public String toString() {
		return value;
	}
}
