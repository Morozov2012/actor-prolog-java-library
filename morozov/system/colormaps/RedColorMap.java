// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class RedColorMap extends CubicSplineColorMapRGBA {
	//
	protected static double[][] redChannelDefinition= new double[][]{{0,0},{102,0},{110,20},{114,35},{125,80},{130,100},{146,170},{156,205},{181,255},{222,255},{228,250},{233,240},{251,170},{255,159}};
	protected static double[][] greenChannelDefinition= new double[][]{{0,0},{26,0},{40,20},{56,60},{68,105},{79,150},{88,193},{96,230},{103,251},{105,254},{153,255},{158,250},{178,210},{184,190},{193,150},{204,100},{210,75},{222,25},{225,15},{230,2},{231,1},{255,1}};
	protected static double[][] blueChannelDefinition= new double[][]{{0,130},{4,135},{19,170},{32,205},{37,220},{49,250},{52,254},{101,254},{105,247},{119,195},{130,150},{137,120},{149,70},{170,13},{179,0}};
	//
	protected static int[][] redChannelConstraints= new int[][]{{0,102,0},{181,222,255}};
	protected static int[][] greenChannelConstraints= new int[][]{{0,26,0},{231,255,1}};
	protected static int[][] blueChannelConstraints= new int[][]{};
	//
	protected static int[][] greenChannelStraightSegments= new int[][]{{105,254,153,255}};
	//
	public RedColorMap() {
	}
	//
	public double[][] getRedChannelDefinition() {
		return redChannelDefinition;
	}
	public double[][] getGreenChannelDefinition() {
		return greenChannelDefinition;
	}
	public double[][] getBlueChannelDefinition() {
		return blueChannelDefinition;
	}
	//
	public int[][] getRedChannelConstraints() {
		return redChannelConstraints;
	}
	public int[][] getGreenChannelConstraints() {
		return greenChannelConstraints;
	}
	public int[][] getBlueChannelConstraints() {
		return blueChannelConstraints;
	}
	//
	public int[][] getGreenChannelStraightSegments() {
		return greenChannelStraightSegments;
	}
}
