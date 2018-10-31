// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class PinkColorMap extends HotColorMap {
	//
	public PinkColorMap() {
	}
	//
	public int[][] createColorMap(int size, int alpha) {
		int[][] cm= super.createColorMap(size,alpha);
		if (size <= 0) {
			return cm;
		};
		int p1= size-1;
		if (p1 <= 0) {
			cm[0][0]= maximalIndex;
			cm[1][0]= maximalIndex;
			cm[2][0]= maximalIndex;
			return cm;
		};
		for (int n=0; n<=p1; n++) {
			float red1= (float)cm[0][n] / maximalIndex;
			float green1= (float)cm[1][n] / maximalIndex;
			float blue1= (float)cm[2][n] / maximalIndex;
			float v= (float)n / p1;
			int red2= (int)(maximalIndex*StrictMath.sqrt((red1 + v * 2) / 3));
			int green2= (int)(maximalIndex*StrictMath.sqrt((green1 + v * 2) / 3));
			int blue2= (int)(maximalIndex*StrictMath.sqrt((blue1 + v * 2) / 3));
			if (red2 > maximalIndex) {
				red2= maximalIndex;
			};
			if (green2 > maximalIndex) {
				green2= maximalIndex;
			};
			if (blue2 > maximalIndex) {
				blue2= maximalIndex;
			};
			cm[0][n]= red2;
			cm[1][n]= green2;
			cm[2][n]= blue2;
		};
		return cm;
	}
}
