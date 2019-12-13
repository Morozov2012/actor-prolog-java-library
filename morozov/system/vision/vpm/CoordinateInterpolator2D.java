// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public abstract class CoordinateInterpolator2D extends Interpolator2D {
	//
	protected double targetCoordinateInterpolatorMeshEpsilon= 4;
	protected double physicalCoordinateEpsilon= 0.001;
	protected int maxPixelDistanceIterations= 100;
	//
	///////////////////////////////////////////////////////////////
	//
	public CoordinateInterpolator2D() {
		meshEpsilon= targetCoordinateInterpolatorMeshEpsilon;
	}
	public CoordinateInterpolator2D(double epsilon, int iterations) {
		super();
		physicalCoordinateEpsilon= epsilon;
		maxPixelDistanceIterations= iterations;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public double computeDefaultValue() {
		return Double.NaN;
	}
}
