// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public class UniversalAverager {
	//
	protected double sum;
	protected int quantity= 0;
	//
	///////////////////////////////////////////////////////////////
	//
	public UniversalAverager() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void add(long value) {
		sum+= value;
		quantity++;
	}
	public void add(double value) {
		sum+= value;
		quantity++;
	}
	public void add(int n, long value) {
		sum+= value;
		quantity+= n;
	}
	public void add(int n, double value) {
		sum+= value;
		quantity+= n;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double getCumulativeValue() {
		return sum;
	}
	public double getMeanValue() {
		if (quantity > 0) {
			return sum / quantity;
		} else {
			return 0;
		}
	}
}
