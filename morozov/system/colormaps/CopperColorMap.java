// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class CopperColorMap extends ColorMapRGBA {
	//
	public CopperColorMap() {
	}
	//
	public int[][] createColorMap(int size, int alpha) {
		int[][] cm= new int[4][size];
		if (size <= 0) {
			return cm;
		};
		double k1= 1.2500 * maximalIndex;
		double k2= 0.7812 * maximalIndex;
		double k3= 0.4975 * maximalIndex;
		int p1= size-1;
		if (p1 <= 0) {
			cm[0][0]= maximalIndex;
			cm[1][0]= (int)k2;
			cm[2][0]= (int)k3;
			cm[3][0]= alpha;
			return cm;
		};
		for (int n=0; n<=p1; n++) {
			int red= (int)(k1 * n / p1);
			if (red > maximalIndex) {
				red= maximalIndex;
			};
			int green= (int)(k2 * n / p1);
			if (green > maximalIndex) {
				green= maximalIndex;
			};
			int blue= (int)(k3 * n / p1);
			if (blue > maximalIndex) {
				blue= maximalIndex;
			};
			cm[0][n]= red;
			cm[1][n]= green;
			cm[2][n]= blue;
			cm[3][n]= alpha;
		};
		return cm;
	}
}
