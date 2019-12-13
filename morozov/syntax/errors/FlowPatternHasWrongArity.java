// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.errors;

public class FlowPatternHasWrongArity extends ParserError {
	//
	protected int actualArity;
	protected int expectedArity;
	//
	public FlowPatternHasWrongArity(int aA, int eA, int position) {
		super(position);
		actualArity= aA;
		expectedArity= eA;
	}
	//
	public int getActualArity() {
		return actualArity;
	}
	public int getExpectedArity() {
		return expectedArity;
	}
	//
	@Override
	public String toString() {
		return	this.getClass().toString() +
			"(actual: " + Integer.toString(actualArity) +
			"; expected: " + Integer.toString(expectedArity) +
			"; position:" + Integer.toString(position) + ")";
	}
}
