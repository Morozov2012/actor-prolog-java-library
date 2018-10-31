// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class CoolColorMap extends ColorMapRGBA {
	//
	public CoolColorMap() {
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
		for (int n=0; n<=p1; n++) {
			int red= maximalIndex * n / p1;
			if (red > maximalIndex) {
				red= maximalIndex;
			};
			int green= maximalIndex * (p1-n) / p1;
			if (green < 0) {
				green= 0;
			};
			cm[0][n]= red;
			cm[1][n]= green;
			cm[2][n]= maximalIndex;
			cm[3][n]= alpha;
		};
		return cm;
	}
}
