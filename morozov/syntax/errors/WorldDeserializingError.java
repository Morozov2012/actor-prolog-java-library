// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class WorldDeserializingError extends ParserError {
	public Exception exception;
	public WorldDeserializingError(int p, Exception e) {
		super(p,e);
		exception= e;
	}
}
