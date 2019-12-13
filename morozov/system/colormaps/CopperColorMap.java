// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class CopperColorMap extends ColorMapRGBA {
	//
	public CopperColorMap() {
	}
	//
	@Override
	public void createColorMap(int[][] cm, int shift, int bandWidth, int totalSize, int alpha) {
		double k1= 1.2500 * maximalIndex;
		double k2= 0.7812 * maximalIndex;
		double k3= 0.4975 * maximalIndex;
		int p1= bandWidth-1;
		if (p1 <= 0) {
			if (shift+0 >= totalSize) {
				return;
			};
			cm[0][shift+0]= maximalIndex;
			cm[1][shift+0]= (int)k2;
			cm[2][shift+0]= (int)k3;
			cm[3][shift+0]= alpha;
			return;
		};
		for (int n=0; n<=p1; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int red= (int)(k1 * n / p1);
			if (red > maximalIndex) {
				red= maximalIndex;
			};
			int green= (int)(k2 * n / p1);
			if (green > maximalIndex) {
				green= maximalIndex;
			};
			int blue= (int)(k3 * n / p1);
			if (blue > maximalIndex) {
				blue= maximalIndex;
			};
			cm[0][shift+n]= red;
			cm[1][shift+n]= green;
			cm[2][shift+n]= blue;
			cm[3][shift+n]= alpha;
		}
	}
}
