// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class SlotWithThisNameIsAlreadyDefined extends ParserError {
	public SlotWithThisNameIsAlreadyDefined(int p) {
		super(p);
	}
}
