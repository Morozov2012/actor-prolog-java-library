// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import java.util.Arrays;

class VisionUtils {
	public static float[] gaussianMatrix(int radius) {
		float[] window= gaussianWindow(radius);
		int length= window.length;
		int halfLength= length / 2 + 1;
		int squaredLength= length * length;
		float[] matrix= new float[squaredLength];
		for (int n=0; n < halfLength; n++) {
			for (int k=n; k < length-n; k++) {
				int point= n*length + k;
				matrix[point]= window[n];
			};
			for (int k=n; k < length-n; k++) {
				int point= k*length + (length-1-n);
				matrix[point]= window[n];
			};
			for (int k=n; k < length-n; k++) {
				int point= (length-1-n)*length + k;
				matrix[point]= window[n];
			};
			for (int k=n; k < length-n; k++) {
				int point= k*length + n;
				matrix[point]= window[n];
			}
		};
		float sum= 0;
		for (int n=0; n < squaredLength; n++) {
			sum= sum + matrix[n];
		};
		for (int n=0; n < squaredLength; n++) {
			matrix[n]= matrix[n] / sum;
		};
		return matrix;
	}
	public static float[] gaussianWindow(int radius) {
		if (radius <= 0) {
			return new float[]{1};
		};
		int nPoints= radius * 2 + 1;
		float[] y= new float[nPoints];
		y[radius]= 1.0f;
		double step= 1.0f / radius;
		double currentX= step;
		for (int n=1; n <= radius; n++) {
			int index1= radius + n;
			int index2= radius - n;
			float value= (float)StrictMath.exp(-(currentX*currentX));
			y[index1]= value;
			y[index2]= value;
			currentX= currentX + step;
		};
		return y;
	}
	public static double[] project(int x, int y, double[][] matrix) {
		if (matrix.length==0) {
			return new double[]{x,y};
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
			double v= (d*x + e*y + f) / (g*x + h*y + i);
			return new double[]{u,v};
		}
	}
	public static double median(double[] vector, int from, int to) {
		int initialLength= vector.length;
		if (from < 0) {
			from= 0;
		};
		if (to > initialLength) {
			to= initialLength;
		};
		double[] array= Arrays.copyOfRange(vector,from,to);
		int actualLength= array.length;
		Arrays.sort(array);
		double result;
		if (actualLength % 2 == 0) {
			int index= actualLength / 2;
			result= (array[index-1] + array[index]) / 2;
		} else {
			int index= actualLength / 2;
			result= array[index];
		};
		return result;
	}
	public static double medianAbs(double[] vector, int from, int to) {
		int initialLength= vector.length;
		if (initialLength * 2 < to - from) {
			return 0;
		};
		if (from < 0) {
			from= 0;
		};
		if (to > initialLength) {
			to= initialLength;
		};
		double[] array= Arrays.copyOfRange(vector,from,to);
		int actualLength= array.length;
		for (int n=0; n < actualLength; n++) {
			if (array[n] < 0) {
				array[n]= - array[n];
			}
		};
		Arrays.sort(array);
		double result;
		if (actualLength % 2 == 0) {
			int index= actualLength / 2;
			result= (array[index-1] + array[index]) / 2;
		} else {
			int index= actualLength / 2;
			result= array[index];
		};
		return result;
	}
	public static double metrics(double x, double threshold, double halfwidth) {
		if (x >= threshold + halfwidth) {
			return 1;
		} else if (x <= threshold - halfwidth) {
			return 0;
		} else {
			return (x - threshold + halfwidth) * (1 / (2*halfwidth));
		}
	}
}
