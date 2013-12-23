// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

class MeanVelocity {
	public double totalVelocityX= 0;
	public double totalVelocityY= 0;
	public double totalVelocityXY= 0;
	public int totalQuantity= 0;
	// public int maximalLocalQuantity= 12;
	public double accumulatedVelocityX= 0;
	public double accumulatedVelocityY= 0;
	public int accumulatedQuantity;
	public MeanVelocity() {
	}
	// public MeanVelocity(int windowWidth) {
	//	maximalLocalQuantity= windowWidth;
	// }
	public void add(double[][] velocity) {
		int length= velocity.length;
		int number= length - 1;
		double[] averagedValue= velocity[length-1];
		if (number > 1) {
			totalVelocityX+= averagedValue[0] * number;
			totalVelocityY+= averagedValue[1] * number;
			totalVelocityXY+= averagedValue[2] * number;
			totalQuantity+= number;
		}
	}
	public double getVelocityX() {
		if (totalQuantity > 0) {
			return totalVelocityX / totalQuantity;
		} else {
			return 0;
		}
	}
	public double getVelocityY() {
		if (totalQuantity > 0) {
			return totalVelocityY / totalQuantity;
		} else {
			return 0;
		}
	}
	public double getVelocityXY() {
		if (totalQuantity > 0) {
			return totalVelocityXY / totalQuantity;
		} else {
			return 0;
		}
	}
	public double getAccumulatedVelocityX() {
		if (accumulatedQuantity > 0) {
			return accumulatedVelocityX / accumulatedQuantity;
		} else {
			return 0;
		}
	}
	public double getAccumulatedVelocityY() {
		if (accumulatedQuantity > 0) {
			return accumulatedVelocityY / accumulatedQuantity;
		} else {
			return 0;
		}
	}
}
