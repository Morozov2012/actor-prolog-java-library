// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class IronColorMap extends CubicSplineColorMapRGBA {
	//
	protected static double[][] redChannelDefinition= new double[][]{{0,0},{8,0},{26,36},{62,144},{92,204},{123,235},{163,255},{255,255}};
	protected static double[][] greenChannelDefinition= new double[][]{{0,0},{81,0},{107,26},{151,114},{162,125},{190,180},{242,250},{252,255},{255,255}};
	protected static double[][] blueChannelDefinition= new double[][]{{0,36},{16,116},{32,148},{54,166},{55,166},{90,148},{98,138},{112,96},{128,19},{147,0},{204,0},{223,49},{248,215},{255,250}};
	//
	protected static int[][] redChannelConstraints= new int[][]{{0,8,0},{163,255,255}};
	protected static int[][] greenChannelConstraints= new int[][]{{0,81,0},{252,255,255}};
	protected static int[][] blueChannelConstraints= new int[][]{{147,204,0}};
	//
	public IronColorMap() {
	}
	//
	@Override
	public double[][] getRedChannelDefinition() {
		return redChannelDefinition;
	}
	@Override
	public double[][] getGreenChannelDefinition() {
		return greenChannelDefinition;
	}
	@Override
	public double[][] getBlueChannelDefinition() {
		return blueChannelDefinition;
	}
	//
	@Override
	public int[][] getRedChannelConstraints() {
		return redChannelConstraints;
	}
	@Override
	public int[][] getGreenChannelConstraints() {
		return greenChannelConstraints;
	}
	@Override
	public int[][] getBlueChannelConstraints() {
		return blueChannelConstraints;
	}
}
