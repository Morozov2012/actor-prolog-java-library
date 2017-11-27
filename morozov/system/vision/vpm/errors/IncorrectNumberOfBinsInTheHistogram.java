// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.errors;

public class IncorrectNumberOfBinsInTheHistogram extends RuntimeException {
	//
	protected int expectedNumberOfBins;
	protected int providedNumberOfBins;
	//
	public IncorrectNumberOfBinsInTheHistogram(int n1, int n2) {
		expectedNumberOfBins= n1;
		providedNumberOfBins= n2;
	}
	//
	public String toString() {
		return this.getClass().toString() + "(" +
			Integer.toString(expectedNumberOfBins) + "<>" +
			Integer.toString(providedNumberOfBins) + ")";
	}
}
