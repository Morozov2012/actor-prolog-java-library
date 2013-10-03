// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.indices.errors;

public class IllegalNumberOfIndices extends RuntimeException {
	public int givenIndicesNumber;
	public int expectedIndicesNumber;
	//
	public IllegalNumberOfIndices(int gIN, int eIN) {
		givenIndicesNumber= gIN;
		expectedIndicesNumber= eIN;
	}
	//
	public String toString() {
		return this.getClass().toString() + "(found:" + String.format("%d",givenIndicesNumber) + ",expected:" + String.format("%d",expectedIndicesNumber) + ")";
	}
}
