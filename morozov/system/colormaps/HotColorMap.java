// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class HotColorMap extends ColorMapRGBA {
	//
	public HotColorMap() {
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
		double k= 3.0 / 8.0 * bandWidth;
		int p2= (int)k;
		int p3= (int)(k-1);
		int p4= (int)(2*k-1);
		int p5= (int)(bandWidth-1-2*k+1);
		for (int n=0; n<=p1; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int red;
			int green;
			int blue;
			if (n <= p3) {
				if (p3 > 0) {
					red= maximalIndex * n / p3;
					if (red > maximalIndex) {
						red= maximalIndex;
					}
				} else {
					red= maximalIndex;
				};
				green= 0;
				blue= 0;
			} else {
				red= maximalIndex;
				if (n <= p4) {
					if (p2 > 0) {
						green= maximalIndex * (n-p3) / p2;
						if (green > maximalIndex) {
							green= maximalIndex;
						}
					} else {
						green= maximalIndex;
					};
					blue= 0;
				} else {
					green= maximalIndex;
					if (p5 > 0) {
						blue= maximalIndex * (n-p4) / p5;
						if (blue > maximalIndex) {
							blue= maximalIndex;
						}
					} else {
						blue= maximalIndex;
					}
				}
			};
			cm[0][shift+n]= red;
			cm[1][shift+n]= green;
			cm[2][shift+n]= blue;
			cm[3][shift+n]= alpha;
		}
	}
}
