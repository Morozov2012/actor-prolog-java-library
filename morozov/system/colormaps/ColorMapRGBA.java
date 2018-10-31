// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public abstract class ColorMapRGBA {
	//
	protected int[][] recentMap= null;
	protected int recentAlpha;
	//
	protected static int maximalIndex= 255;
	//
	public ColorMapRGBA() {
	}
	//
	public int[][] getColorMap(int size, int alpha) {
		synchronized (this) {
			if (recentMap != null && recentMap.length==size && recentAlpha==alpha) {
				return recentMap;
			};
			recentMap= createColorMap(size,alpha);
			recentAlpha= alpha;
			return recentMap;
		}
	}
	public abstract int[][] createColorMap(int size, int alpha);
}
