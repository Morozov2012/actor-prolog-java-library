// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public abstract class CoordinateInterpolator2D extends Interpolator2D {
	//
	protected int meshWidth= 1;
	protected int meshHeight= 1;
	//
	protected double targetMeshEpsilon= 4;
	protected double physicalCoordinateEpsilon= 0.001;
	protected int maxPixelDistanceIterations= 100;
	//
	///////////////////////////////////////////////////////////////
	//
	public CoordinateInterpolator2D() {
		meshEpsilon= targetMeshEpsilon;
	}
	public CoordinateInterpolator2D(double epsilon, int iterations) {
		super();
		physicalCoordinateEpsilon= epsilon;
		maxPixelDistanceIterations= iterations;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double computeDefaultValue() {
		return Double.NaN;
	}
}
