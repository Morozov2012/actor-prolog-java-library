// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class OceanColorMap extends ColorMapRGBA {
	//
	public OceanColorMap() {
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
			cm[1][0]= maximalIndex;
			cm[2][0]= maximalIndex;
			cm[3][0]= alpha;
			return cm;
		};
		if (size==2) {
			cm[0][0]= 0;
			cm[1][0]= 0;
			cm[2][0]= 0;
			cm[3][0]= alpha;
			cm[0][1]= maximalIndex;
			cm[1][1]= maximalIndex;
			cm[2][1]= maximalIndex;
			cm[3][1]= alpha;
			return cm;
		};
		float k= (float)size / 3;
		int p2= (int)k;
		int p3= (int)(2*k);
		for (int n=0; n<=p2; n++) {
			int blue= (int)(maximalIndex * n / p1);
			if (blue > maximalIndex) {
				blue= maximalIndex;
			};
			cm[0][n]= 0;
			cm[1][n]= 0;
			cm[2][n]= blue;
			cm[3][n]= alpha;
		};
		for (int n=p2+1; n<=p3; n++) {
			int green= (int)(maximalIndex*(n-p2)/(p1-p2));
			if (green > maximalIndex) {
				green= maximalIndex;
			};
			int blue= (int)(maximalIndex * n / p1);
			if (blue > maximalIndex) {
				blue= maximalIndex;
			};
			cm[0][n]= 0;
			cm[1][n]= green;
			cm[2][n]= blue;
			cm[3][n]= alpha;
		};
		for (int n=p3+1; n<=p1; n++) {
			int red= (int)(maximalIndex*(n-p3)/(p1-p3));
			if (red > maximalIndex) {
				red= maximalIndex;
			};
			int green= (int)(maximalIndex*(n-p2)/(p1-p2));
			if (green > maximalIndex) {
				green= maximalIndex;
			};
			int blue= (int)(maximalIndex * n / p1);
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
