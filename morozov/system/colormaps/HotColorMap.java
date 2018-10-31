// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class HotColorMap extends ColorMapRGBA {
	//
	public HotColorMap() {
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
		double k= 3.0 / 8.0 * size;
		int p2= (int)k;
		int p3= (int)(k-1);
		int p4= (int)(2*k-1);
		int p5= (int)(size-1-2*k+1);
		for (int n=0; n<=p1; n++) {
			int red;
			int green;
			int blue;
			if (n <= p3) {
				if (p3 > 0) {
					red= maximalIndex * n / p3;
					if (red > maximalIndex) {
						red= maximalIndex;
					}
				} else {
					red= maximalIndex;
				};
				green= 0;
				blue= 0;
			} else {
				red= maximalIndex;
				if (n <= p4) {
					if (p2 > 0) {
						green= maximalIndex * (n-p3) / p2;
						if (green > maximalIndex) {
							green= maximalIndex;
						}
					} else {
						green= maximalIndex;
					};
					blue= 0;
				} else {
					green= maximalIndex;
					if (p5 > 0) {
						blue= maximalIndex * (n-p4) / p5;
						if (blue > maximalIndex) {
							blue= maximalIndex;
						}
					} else {
						blue= maximalIndex;
					}
				}
			};
			cm[0][n]= red;
			cm[1][n]= green;
			cm[2][n]= blue;
			cm[3][n]= alpha;
		};
		return cm;
	}
}
