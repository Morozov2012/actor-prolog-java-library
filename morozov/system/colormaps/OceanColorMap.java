// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class OceanColorMap extends ColorMapRGBA {
	//
	public OceanColorMap() {
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
		if (bandWidth==2) {
			if (shift+0 >= totalSize) {
				return;
			};
			cm[0][shift+0]= 0;
			cm[1][shift+0]= 0;
			cm[2][shift+0]= 0;
			cm[3][shift+0]= alpha;
			if (shift+1 >= totalSize) {
				return;
			};
			cm[0][shift+1]= maximalIndex;
			cm[1][shift+1]= maximalIndex;
			cm[2][shift+1]= maximalIndex;
			cm[3][shift+1]= alpha;
			return;
		};
		float k= (float)bandWidth / 3;
		int p2= (int)k;
		int p3= (int)(2*k);
		for (int n=0; n<=p2; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int blue= (int)(maximalIndex * n / p1);
			if (blue > maximalIndex) {
				blue= maximalIndex;
			};
			cm[0][shift+n]= 0;
			cm[1][shift+n]= 0;
			cm[2][shift+n]= blue;
			cm[3][shift+n]= alpha;
		};
		for (int n=p2+1; n<=p3; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int green= (int)(maximalIndex*(n-p2)/(p1-p2));
			if (green > maximalIndex) {
				green= maximalIndex;
			};
			int blue= (int)(maximalIndex * n / p1);
			if (blue > maximalIndex) {
				blue= maximalIndex;
			};
			cm[0][shift+n]= 0;
			cm[1][shift+n]= green;
			cm[2][shift+n]= blue;
			cm[3][shift+n]= alpha;
		};
		for (int n=p3+1; n<=p1; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int red= (int)(maximalIndex*(n-p3)/(p1-p3));
			if (red > maximalIndex) {
				red= maximalIndex;
			};
			int green= (int)(maximalIndex*(n-p2)/(p1-p2));
			if (green > maximalIndex) {
				green= maximalIndex;
			};
			int blue= (int)(maximalIndex * n / p1);
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
