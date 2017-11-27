// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public class EvaluatingAverager {
	//
	protected double sum;
	protected int quantity= 0;
	protected double minimum;
	protected double maximum;
	//
	///////////////////////////////////////////////////////////////
	//
	public EvaluatingAverager() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void add(long value) {
		sum+= value;
		quantity++;
		if (quantity > 0) {
			if (minimum > value) {
				minimum= value;
			};
			if (maximum < value) {
				maximum= value;
			}
		} else {
			minimum= value;
			maximum= value;
		}
	}
	public void add(double value) {
		sum+= value;
		quantity++;
		if (quantity > 0) {
			if (minimum > value) {
				minimum= value;
			};
			if (maximum < value) {
				maximum= value;
			}
		} else {
			minimum= value;
			maximum= value;
		}
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
	public double getMinimalValue() {
		return minimum;
	}
	public double getMaximalValue() {
		return maximum;
	}
}
