// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.indices.errors;

public class IllegalNumberOfIndices extends RuntimeException {
	//
	protected int givenIndicesNumber;
	protected int expectedIndicesNumber;
	//
	public IllegalNumberOfIndices(int gIN, int eIN) {
		givenIndicesNumber= gIN;
		expectedIndicesNumber= eIN;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(found:" + String.format("%d",givenIndicesNumber) + ",expected:" + String.format("%d",expectedIndicesNumber) + ")";
	}
}
