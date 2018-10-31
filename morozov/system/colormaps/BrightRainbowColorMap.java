// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class BrightRainbowColorMap extends ColorMapRGBA {
	//
	public BrightRainbowColorMap() {
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
		if (size==2) {
			cm[0][0]= maximalIndex;
			cm[1][0]= 0;
			cm[2][0]= 0;
			cm[3][0]= alpha;
			cm[0][1]= maximalIndex;
			cm[1][1]= 0;
			cm[2][1]= maximalIndex;
			cm[3][1]= alpha;
			return cm;
		};
		float k= (float)size / 5;
		int p2= (int)k;
		int p3= (int)(2*k);
		int p4= (int)(3*k);
		int p5= (int)(4*k);
		int p6= (int)(5*k);
		int p7= (int)(6*k);
		for (int n=0; n<=p2; n++) {
			int green;
			if (p2 > 0) {
				green= maximalIndex * n / p2;
				if (green > maximalIndex) {
					green= maximalIndex;
				}
			} else {
				green= maximalIndex;
			};
			cm[0][n]= maximalIndex;
			cm[1][n]= green;
			cm[2][n]= 0;
			cm[3][n]= alpha;
		};
		for (int n=p2+1; n<=p3; n++) {
			int red= maximalIndex*(p3-n)/(p3-p2);
			if (red < 0) {
				red= 0;
			};
			cm[0][n]= red;
			cm[1][n]= maximalIndex;
			cm[2][n]= 0;
			cm[3][n]= alpha;
		};
		for (int n=p3+1; n<=p4; n++) {
			int blue= maximalIndex*(n-p3)/(p4-p3);
			if (blue > maximalIndex) {
				blue= maximalIndex;
			};
			cm[0][n]= 0;
			cm[1][n]= maximalIndex;
			cm[2][n]= blue;
			cm[3][n]= alpha;
		};
		for (int n=p4+1; n<=p5; n++) {
			int green= maximalIndex*(p5-n)/(p5-p4);
			if (green < 0) {
				green= 0;
			};
			cm[0][n]= 0;
			cm[1][n]= green;
			cm[2][n]= maximalIndex;
			cm[3][n]= alpha;
		};
		for (int n=p5+1; n<=p1; n++) {
			int red= maximalIndex*(n-p5)/(p6-p5);
			if (red > maximalIndex) {
				red= maximalIndex;
			};
			cm[0][n]= red;
			cm[1][n]= 0;
			cm[2][n]= maximalIndex;
			cm[3][n]= alpha;
		};
		return cm;
	}
}
