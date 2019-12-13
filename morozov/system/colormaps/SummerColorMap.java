// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class SummerColorMap extends ColorMapRGBA {
	//
	public SummerColorMap() {
	}
	//
	@Override
	public void createColorMap(int[][] cm, int shift, int bandWidth, int totalSize, int alpha) {
		int p1= bandWidth-1;
		if (p1 <= 0) {
			if (shift+0 >= totalSize) {
				return;
			};
			cm[0][shift+0]= maximalIndex;
			cm[1][shift+0]= maximalIndex;
			cm[2][shift+0]= (int)(maximalIndex * 0.4);
			cm[3][shift+0]= alpha;
			return;
		};
		for (int n=0; n<=p1; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int red= maximalIndex * n / p1;
			if (red > maximalIndex) {
				red= maximalIndex;
			};
			int green= (int)(maximalIndex * (0.5 + ((float)n / p1) / 2));
			if (green > maximalIndex) {
				green= maximalIndex;
			};
			cm[0][shift+n]= red;
			cm[1][shift+n]= green;
			cm[2][shift+n]= (int)(maximalIndex * 0.4);
			cm[3][shift+n]= alpha;
		}
	}
}
