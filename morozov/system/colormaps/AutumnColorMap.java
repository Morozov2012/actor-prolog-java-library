// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class AutumnColorMap extends ColorMapRGBA {
	//
	public AutumnColorMap() {
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
			cm[2][shift+0]= 0;
			cm[3][shift+0]= alpha;
			return;
		};
		for (int n=0; n<=p1; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int green= maximalIndex * n / p1;
			if (green > maximalIndex) {
				green= maximalIndex;
			};
			cm[0][shift+n]= maximalIndex;
			cm[1][shift+n]= green;
			cm[2][shift+n]= 0;
			cm[3][shift+n]= alpha;
		}
	}
}
