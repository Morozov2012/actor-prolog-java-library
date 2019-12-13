// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

public class CoordinateXInterpolator2D extends CoordinateInterpolator2D {
	//
	public CoordinateXInterpolator2D() {
	}
	public CoordinateXInterpolator2D(double epsilon, int iterations) {
		super(epsilon,iterations);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public double computeMatrixItem(int x, int y, double[][] matrix, double[][] extraArguments2) {
		if (matrix.length==0) {
			return x;
		} else {
			// M= [	[ A, D, G ];
			//	[ B, E, H ];
			//	[ C, F, I ]	]
			double a= matrix[0][0];
			double b= matrix[1][0];
			double c= matrix[2][0];
			double d= matrix[0][1];
			double e= matrix[1][1];
			double f= matrix[2][1];
			double g= matrix[0][2];
			double h= matrix[1][2];
			double i= matrix[2][2];
			double u= (a*x + b*y + c) / (g*x + h*y + i);
			return u;
		}
	}
}
