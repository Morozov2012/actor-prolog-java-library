// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

class MeanAcceleration {
	public double totalAccelerationX= 0;
	public double totalAccelerationY= 0;
	public double totalAccelerationXY= 0;
	public int totalQuantity= 0;
	public MeanAcceleration() {
	}
	public void add(double[] value, int number) {
		if (number > 2) {
			totalAccelerationX+= value[0] * number;
			totalAccelerationY+= value[1] * number;
			totalAccelerationXY+= value[2] * number;
			totalQuantity+= number;
		}
	}
	public double getAccelerationX() {
		if (totalQuantity > 0) {
			return totalAccelerationX / totalQuantity;
		} else {
			return 0;
		}
	}
	public double getAccelerationY() {
		if (totalQuantity > 0) {
			return totalAccelerationY / totalQuantity;
		} else {
			return 0;
		}
	}
	public double getAccelerationXY() {
		if (totalQuantity > 0) {
			return totalAccelerationXY / totalQuantity;
		} else {
			return 0;
		}
	}
}
