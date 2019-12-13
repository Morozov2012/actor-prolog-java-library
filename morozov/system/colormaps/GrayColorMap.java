// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class GrayColorMap extends ColorMapRGBA {
	//
	public GrayColorMap() {
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
			cm[2][shift+0]= maximalIndex;
			cm[3][shift+0]= alpha;
			return;
		};
		for (int n=0; n<=p1; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int gray= maximalIndex * n / p1;
			if (gray > maximalIndex) {
				gray= maximalIndex;
			};
			cm[0][shift+n]= gray;
			cm[1][shift+n]= gray;
			cm[2][shift+n]= gray;
			cm[3][shift+n]= alpha;
		}
	}
}
