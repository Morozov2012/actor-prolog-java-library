// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class SoftRainbowColorMap extends ColorMapRGBA {
	//
	public SoftRainbowColorMap() {
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
			cm[1][shift+0]= 0;
			cm[2][shift+0]= maximalIndex;
			cm[3][shift+0]= alpha;
			return;
		};
		double f= 2 * StrictMath.PI;
		double shift1= 2 * StrictMath.PI / 3;
		double shift0= - (shift1 + StrictMath.PI/2);
		for (int n=0; n<=p1; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			double x= (1.0 - (double)n/p1) * (1.0-1.0/6);
			int red= (int)StrictMath.round(maximalIndex*(StrictMath.sin(x*f + shift0) + 1) / 2);
			int green= (int)StrictMath.round(maximalIndex*(StrictMath.sin(x*f + shift1 + shift0) + 1) / 2);
			int blue= (int)StrictMath.round(maximalIndex*(StrictMath.sin(x*f + 2*shift1 + shift0) + 1) / 2);
			cm[0][shift+n]= red;
			cm[1][shift+n]= green;
			cm[2][shift+n]= blue;
			cm[3][shift+n]= alpha;
		}
	}
}
