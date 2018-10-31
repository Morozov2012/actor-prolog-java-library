// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class NoneColorMap extends ColorMapRGBA {
	//
	public NoneColorMap() {
	}
	//
	public int[][] createColorMap(int size, int alpha) {
		int[][] cm= new int[4][size];
		return cm;
	}
}
