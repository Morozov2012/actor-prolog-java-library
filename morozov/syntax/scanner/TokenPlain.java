// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.syntax.scanner;

public class TokenPlain extends PrologToken {
	protected PrologTokenType type;
	public TokenPlain(PrologTokenType t, int p) {
		super(p);
		type= t;
	}
	public PrologTokenType getType() {
		return type;
	}
	public String toString() {
		try {
			return type.toText();
		} catch (TokenIsCompound e) {
			return type.toString();
		}
	}
}
