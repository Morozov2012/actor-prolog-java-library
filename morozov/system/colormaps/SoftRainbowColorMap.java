// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class SoftRainbowColorMap extends ColorMapRGBA {
	//
	public SoftRainbowColorMap() {
	}
	//
	public int[][] createColorMap(int size, int alpha) {
		int[][] cm= new int[4][size];
		if (size <= 0) {
			return cm;
		};
		int p1= size-1;
		if (p1 <= 0) {
			cm[0][0]= maximalIndex;
			cm[1][0]= 0;
			cm[2][0]= maximalIndex;
			cm[3][0]= alpha;
			return cm;
		};
		double f= 2 * StrictMath.PI;
		double shift1= 2 * StrictMath.PI / 3;
		double shift0= - (shift1 + StrictMath.PI/2);
		for (int n=0; n<=p1; n++) {
			double x= (1.0 - (double)n/p1) * (1.0-1.0/6);
			int red= (int)StrictMath.round(maximalIndex*(StrictMath.sin(x*f + shift0) + 1) / 2);
			int green= (int)StrictMath.round(maximalIndex*(StrictMath.sin(x*f + shift1 + shift0) + 1) / 2);
			int blue= (int)StrictMath.round(maximalIndex*(StrictMath.sin(x*f + 2*shift1 + shift0) + 1) / 2);
			cm[0][n]= red;
			cm[1][n]= green;
			cm[2][n]= blue;
			cm[3][n]= alpha;
		};
		return cm;
	}
}
