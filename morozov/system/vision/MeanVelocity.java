// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

class MeanVelocity {
	public int totalQuantity= 0;
	public double totalSum= 0;
	public MeanVelocity() {
	}
	public void add(int number, double velocitySum) {
		if (number > 1) {
			totalQuantity+= number;
			totalSum+= velocitySum;
		}
	}
	public double getMeanVelocity() {
		if (totalQuantity > 0) {
			return totalSum / totalQuantity;
		} else {
			return 0;
		}
	}
}
