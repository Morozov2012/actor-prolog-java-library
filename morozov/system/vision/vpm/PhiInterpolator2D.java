// (c) 2014-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public class PhiInterpolator2D extends Interpolator2D {
	//
	protected double targetMeshEpsilon= 0.1;
	protected double physicalDistanceEpsilon= 0.01;
	protected int maxPhysicalDistanceIterations= 30;
	//
	///////////////////////////////////////////////////////////////
	//
	public PhiInterpolator2D() {
		meshEpsilon= targetMeshEpsilon;
	}
	public PhiInterpolator2D(double epsilon, int iterations) {
		super();
		physicalDistanceEpsilon= epsilon;
		int maxPhysicalDistanceIterations= iterations;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double computeMatrixItem(int pX, int pY, double[][] inverseMatrix, double[][] dummyArgument) {
		return computePhi(pX,pY,inverseMatrix);
	}
	public double computeDefaultValue() {
		return StrictMath.PI / 2;
	}
	public double computePhi(int pX, int pY, double[][] inverseMatrix) {
		double outsidePhi= 0.0;
		boolean outsidePhiIsDefined= false;
		double leftBound= 0.0;
		double rightBound= 2*StrictMath.PI;
		int n_Sections= 8;
		double leftDistance= estimateSquaredDistance(pX,pY,leftBound,inverseMatrix,1);
		double rightDistance= estimateSquaredDistance(pX,pY,rightBound,inverseMatrix,1);
		int length= n_Sections + 1;
		double[] phiList= new double[length];
		double[] distanceList= new double[length];
		for (int k=1; k <= maxPhysicalDistanceIterations; k++) {
			double step= (rightBound - leftBound) / n_Sections;
			double phiJ= leftBound;
			phiList[0]= leftBound;
			distanceList[0]= leftDistance;
			phiList[length-1]= rightBound;
			distanceList[length-1]= rightDistance;
			for (int s=1; s <= n_Sections-1; s++) {
				phiJ= phiJ + step;
				double distanceJ= estimateSquaredDistance(pX,pY,phiJ,inverseMatrix,1);
				phiList[s]= phiJ;
				distanceList[s]= distanceJ;
			};
			int maxN= 0;
			double maxPhi= 0.0;
			double maxDistance= 0.0;
			boolean maxDistanceIsInitiated= false;
			for (int n=0; n < length; n++) {
				double phi= phiList[n];
				double distance= distanceList[n];
				if (maxDistanceIsInitiated) {
					if (distance >= maxDistance) {
						maxN= n;
						maxPhi= phi;
						maxDistance= distance;
					}
				} else {
					maxN= n;
					maxPhi= phi;
					maxDistance= distance;
					maxDistanceIsInitiated= true;
				}
			};
			leftBound= maxPhi - step;
			rightBound= maxPhi + step;
			if (maxN > 0) {
				leftDistance= distanceList[maxN-1];
			} else {
				leftDistance= distanceList[length-1];
			};
			if (maxN < length-1) {
				rightDistance= distanceList[maxN+1];
			} else {
				rightDistance= distanceList[1];
			};
			boolean previousPhiIsDefined= outsidePhiIsDefined;
			outsidePhi= maxPhi;
			outsidePhiIsDefined= true;
			if (previousPhiIsDefined && rightBound-leftBound <= physicalDistanceEpsilon) {
				break;
			}
		};
		return outsidePhi;
	}
	protected double estimateSquaredDistance(int pX, int pY, double phi, double[][] inverseMatrix, double amplitude) {
		double a= inverseMatrix[0][0];
		double b= inverseMatrix[1][0];
		double c= inverseMatrix[2][0];
		double d= inverseMatrix[0][1];
		double e= inverseMatrix[1][1];
		double f= inverseMatrix[2][1];
		double g= inverseMatrix[0][2];
		double h= inverseMatrix[1][2];
		double i= inverseMatrix[2][2];
		double tX= (a*pX + b*pY + c) / (g*pX + h*pY + i);
		double tY= (d*pX + e*pY + f) / (g*pX + h*pY + i);
		double deltaX= amplitude * StrictMath.cos(phi);
		double deltaY= - amplitude * StrictMath.sin(phi);
		double qX= pX + deltaX;
		double qY= pY + deltaY;
		double uX= (a*qX + b*qY + c) / (g*qX + h*qY + i);
		double uY= (d*qX + e*qY + f) / (g*qX + h*qY + i);
		double dX= tX-uX;
		double dY= tY-uY;
		double distance= dX*dX + dY*dY;
		return distance;
	}
}
