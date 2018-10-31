// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class BlackhotColorMap extends ColorMapRGBA {
	//
	public BlackhotColorMap() {
	}
	//
	public int[][] createColorMap(int size, int alpha) {
		int[][] cm= new int[4][size];
		if (size <= 0) {
			return cm;
		};
		int p1= size-1;
		if (p1 <= 0) {
			cm[0][0]= 0;
			cm[1][0]= 0;
			cm[2][0]= 0;
			cm[3][0]= alpha;
			return cm;
		};
		for (int n=0; n<=p1; n++) {
			int gray= maximalIndex * (p1-n) / p1;
			if (gray > maximalIndex) {
				gray= maximalIndex;
			};
			cm[0][n]= gray;
			cm[1][n]= gray;
			cm[2][n]= gray;
			cm[3][n]= alpha;
		};
		return cm;
	}
}
