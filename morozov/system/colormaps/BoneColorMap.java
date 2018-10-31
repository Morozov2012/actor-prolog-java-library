// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class BoneColorMap extends HotColorMap {
	//
	public BoneColorMap() {
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
			int red1= cm[0][n];
			int green1= cm[1][n];
			int blue1= cm[2][n];
			float v= (float)maximalIndex * n / p1;
			int red2= (int)((blue1 + v * 7) / 8);
			int green2= (int)((green1 + v * 7) / 8);
			int blue2= (int)((red1 + v * 7) / 8);
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
