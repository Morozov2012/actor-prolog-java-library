// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class TheVariableIsNotUnique extends ParserError {
	//
	protected int secondPosition;
	//
	public TheVariableIsNotUnique(int p1, int p2) {
		super(p1);
		secondPosition= p2;
	}
	//
	public String toString() {
		return this.getClass().toString() + "(" + Integer.toString(position) + "," + Integer.toString(secondPosition) + ")";
	}
}
