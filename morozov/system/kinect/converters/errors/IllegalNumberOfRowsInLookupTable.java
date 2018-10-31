// (c) 2016 IRE RAS Alexei A. Morozov                                                       

package morozov.system.kinect.converters.errors;

public class IllegalNumberOfRowsInLookupTable extends RuntimeException {
	//
	protected int row;
	protected int expectedNumber;
	//
	public IllegalNumberOfRowsInLookupTable(int r, int n) {
		row= r;
		expectedNumber= n;
	}
	//
	public String toString() {
		return this.getClass().toString() + "(" + Integer.toString(row) + ";" + Integer.toString(expectedNumber) + ")";
	}
}
