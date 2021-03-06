// (c) 2014-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public class SizeInterpolator2D extends PhiInterpolator2D {
	//
	protected double targetSizeInterpolatorMeshEpsilon= 4;
	protected double physicalSizeEpsilon= 0.001;
	protected int maxPixelDistanceIterations= 100;
	//
	///////////////////////////////////////////////////////////////
	//
	public SizeInterpolator2D() {
		meshEpsilon= targetSizeInterpolatorMeshEpsilon;
	}
	public SizeInterpolator2D(double epsilon, int iterations) {
		super();
		physicalSizeEpsilon= epsilon;
		maxPixelDistanceIterations= iterations;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public double computeMatrixItem(int pX, int pY, double[][] inverseMatrix, double[][] phiMatrix) {
		double phi0;
		int phiMatrixHeight= phiMatrix.length;
		if (pY < phiMatrixHeight) {
			int phiMatrixWidth= phiMatrix[0].length;
			if (pX < phiMatrixWidth) {
				phi0= phiMatrix[pY][pX];
			} else {
				phi0= computePhi(pX,pY,inverseMatrix);
			}
		} else {
			phi0= computePhi(pX,pY,inverseMatrix);
		};
		double phi1= phi0 + StrictMath.PI/2;
		double r1= estimateSize(pX+1,pY+1,phi1,inverseMatrix,0.5);
		double phi2= phi0 - StrictMath.PI/2;
		double r2= estimateSize(pX+1,pY+1,phi2,inverseMatrix,0.5);
		double value= r1 + r2;
		return value;
	}
	@Override
	public double computeDefaultValue() {
		return 1.0;
	}
	public double estimateSize(int pX, int pY, double phi, double[][] inverseMatrix, double targetDistance) {
		double squaredTargetDistance= targetDistance*targetDistance;
		double leftBound= 0;
		double rightBound= 0;
		double currentDistance= 1;
		boolean rightBoundIsKnown= false;
		for (int k=1; k <= maxPixelDistanceIterations; k++) {
			if (	rightBoundIsKnown &&
				rightBound - leftBound < physicalSizeEpsilon ) {
				break;
			};
			double squaredCurrentSize= estimateSquaredDistance(pX,pY,phi,inverseMatrix,currentDistance);
			if (squaredCurrentSize < squaredTargetDistance) {
				leftBound= currentDistance;
				if (rightBoundIsKnown) {
					currentDistance= (leftBound+rightBound)/2;
				} else {
					currentDistance= currentDistance * 2;
				};
			} else if (squaredCurrentSize > squaredTargetDistance) {
				rightBound= currentDistance;
				rightBoundIsKnown= true;
				currentDistance= (leftBound+rightBound)/2;
			} else if (squaredCurrentSize == squaredTargetDistance) {
				break;
			}
		};
		return currentDistance;
	}
}
