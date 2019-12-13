// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class JetColorMap extends ColorMapRGBA {
	//
	public JetColorMap() {
	}
	//
	@Override
	public void createColorMap(int[][] cm, int shift, int bandWidth, int totalSize, int alpha) {
		int p1= bandWidth-1;
		if (p1 <= 0) {
			if (shift+0 >= totalSize) {
				return;
			};
			cm[0][shift+0]= maximalIndex / 2;
			cm[1][shift+0]= 0;
			cm[2][shift+0]= 0;
			cm[3][shift+0]= alpha;
			return;
		};
		if (bandWidth==2) {
			if (shift+0 >= totalSize) {
				return;
			};
			cm[0][shift+0]= 0;
			cm[1][shift+0]= 0;
			cm[2][shift+0]= maximalIndex / 2;
			cm[3][shift+0]= alpha;
			if (shift+1 >= totalSize) {
				return;
			};
			cm[0][shift+1]= maximalIndex / 2;
			cm[1][shift+1]= 0;
			cm[2][shift+1]= 0;
			cm[3][shift+1]= alpha;
			return;
		};
		float k= (float)bandWidth / 8;
		int p3= (int)k;
		int p2= (int)(p3+k);
		int p4= (int)(3*k);
		int p5= (int)(5*k);
		int p6= (int)(7*k);
		int p7= (int)(9*k);
		for (int n=0; n<=p3; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int blue;
			if (p2 > 0) {
				blue= (int)(maximalIndex*(n+k)/p2);
				if (blue > maximalIndex) {
					blue= maximalIndex;
				}
			} else {
				blue= maximalIndex;
			};
			cm[0][shift+n]= 0;
			cm[1][shift+n]= 0;
			cm[2][shift+n]= blue;
			cm[3][shift+n]= alpha;
		};
		for (int n=p3+1; n<=p4; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int green= maximalIndex*(n-p3)/(p4-p3);
			if (green > maximalIndex) {
				green= maximalIndex;
			};
			cm[0][shift+n]= 0;
			cm[1][shift+n]= green;
			cm[2][shift+n]= maximalIndex;
			cm[3][shift+n]= alpha;
		};
		for (int n=p4+1; n<=p5; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int red= maximalIndex*(n-p4)/(p5-p4);
			if (red > maximalIndex) {
				red= maximalIndex;
			};
			int blue= maximalIndex*(p5-n)/(p5-p4);
			if (blue < 0) {
				blue= 0;
			};
			cm[0][shift+n]= red;
			cm[1][shift+n]= maximalIndex;
			cm[2][shift+n]= blue;
			cm[3][shift+n]= alpha;
		};
		for (int n=p5+1; n<=p6; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int green= maximalIndex*(p6-n)/(p6-p5);
			if (green < 0) {
				green= 0;
			};
			cm[0][shift+n]= maximalIndex;
			cm[1][shift+n]= green;
			cm[2][shift+n]= 0;
			cm[3][shift+n]= alpha;
		};
		for (int n=p6+1; n<=p1; n++) {
			if (shift+n >= totalSize) {
				break;
			};
			int red= maximalIndex*(p7-n)/(p7-p6);
			if (red < 0) {
				red= 0;
			};
			cm[0][shift+n]= red;
			cm[1][shift+n]= 0;
			cm[2][shift+n]= 0;
			cm[3][shift+n]= alpha;
		}
	}
}
