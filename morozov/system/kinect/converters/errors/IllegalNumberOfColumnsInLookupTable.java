// (c) 2016 IRE RAS Alexei A. Morozov                                                       

package morozov.system.kinect.converters.errors;

public class IllegalNumberOfColumnsInLookupTable extends RuntimeException {
	//
	protected int column;
	protected int expectedNumber;
	//
	public IllegalNumberOfColumnsInLookupTable(int c, int n) {
		column= c;
		expectedNumber= n;
	}
	//
	public String toString() {
		return this.getClass().toString() + "(" + Integer.toString(column) + ";" + Integer.toString(expectedNumber) + ")";
	}
}
