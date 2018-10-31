// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class LightJetColorMap extends ColorMapRGBA {
	//
	public LightJetColorMap() {
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
			cm[2][0]= 0;
			cm[3][0]= alpha;
			return cm;
		};
		if (size==2) {
			cm[0][0]= 0;
			cm[1][0]= 0;
			cm[2][0]= maximalIndex / 2;
			cm[3][0]= alpha;
			cm[0][1]= maximalIndex;
			cm[1][1]= 0;
			cm[2][1]= 0;
			cm[3][1]= alpha;
			return cm;
		};
		float k= (float)size / 7;
		int p3= (int)k;
		int p2= (int)(p3+k);
		int p4= (int)(3*k);
		int p5= (int)(5*k);
		int p6= (int)(7*k);
		// int p7= (int)(9*k);
		for (int n=0; n<=p3; n++) {
			int blue;
			if (p2 > 0) {
				blue= (int)(maximalIndex*(n+k)/p2);
				if (blue > maximalIndex) {
					blue= maximalIndex;
				}
			} else {
				blue= maximalIndex;
			};
			cm[0][n]= 0;
			cm[1][n]= 0;
			cm[2][n]= blue;
			cm[3][n]= alpha;
		};
		for (int n=p3+1; n<=p4; n++) {
			int green= maximalIndex*(n-p3)/(p4-p3);
		if (green > maximalIndex) {
			green= maximalIndex;
			};
			cm[0][n]= 0;
			cm[1][n]= green;
			cm[2][n]= maximalIndex;
			cm[3][n]= alpha;
		};
		for (int n=p4+1; n<=p5; n++) {
			int red= maximalIndex*(n-p4)/(p5-p4);
			if (red > maximalIndex) {
				red= maximalIndex;
			};
			int blue= maximalIndex*(p5-n)/(p5-p4);
			if (blue < 0) {
				blue= 0;
			};
			cm[0][n]= red;
			cm[1][n]= maximalIndex;
			cm[2][n]= blue;
			cm[3][n]= alpha;
		};
		for (int n=p5+1; n<=p1; n++) {
			int green= maximalIndex*(p6-n)/(p6-p5);
			if (green < 0) {
				green= 0;
			};
			cm[0][n]= maximalIndex;
			cm[1][n]= green;
			cm[2][n]= 0;
			cm[3][n]= alpha;
		};
		/*
		for (int n=p6+1; n<=p1; n++) {
			int red= maximalIndex*(p7-n)/(p7-p6);
			if (red < 0) {
				red= 0;
			};
			cm[0][n]= red;
			cm[1][n]= 0;
			cm[2][n]= 0;
			cm[3][n]= alpha;
		};
		*/
		return cm;
	}
}
