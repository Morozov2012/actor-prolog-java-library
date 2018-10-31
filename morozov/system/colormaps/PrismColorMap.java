// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class PrismColorMap extends ColorMapRGBA {
	//
	public PrismColorMap() {
	}
	//
	public int[][] createColorMap(int size, int alpha) {
		int[][] cm= new int[4][size];
		if (size <= 0) {
			return cm;
		};
		int p1= size-1;
		if (p1 <= 0) {
			cm[0][0]= maximalIndex * 2 / 3;
			cm[1][0]= 0;
			cm[2][0]= maximalIndex;
			cm[3][0]= alpha;
			return cm;
		};
		int bandWidth= size / 10 / 6;
		int k= 0;
		int m= 0;
		for (int n=0; n<=p1; n++) {
			if (k >= 6) {
				k= 0;
			};
			int red;
			int green;
			int blue;
			switch (k) {
			case 0:
				red= maximalIndex;
				green= 0;
				blue= 0;
				break;
			case 1:
				red= maximalIndex;
				green= maximalIndex/2;
				blue= 0;
				break;
			case 2:
				red= maximalIndex;
				green= maximalIndex;
				blue= 0;
				break;
			case 3:
				red= 0;
				green= maximalIndex;
				blue= 0;
				break;
			case 4:
				red= 0;
				green= 0;
				blue= maximalIndex;
				break;
			default:
				red= maximalIndex * 2 / 3;
				green= 0;
				blue= maximalIndex;
				break;
			};
			cm[0][n]= red;
			cm[1][n]= green;
			cm[2][n]= blue;
			cm[3][n]= alpha;
			m= m + 1;
			if (m >= bandWidth) {
				k= k + 1;
				m= 0;
			}
		};
		return cm;
	}
}
