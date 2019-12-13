// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class TheMainClassIsDefinedExplicitlyInTheProgram extends ThisClassIsAlreadyDefined {
	public TheMainClassIsDefinedExplicitlyInTheProgram(long name, int position) {
		super(name,position);
	}
}
