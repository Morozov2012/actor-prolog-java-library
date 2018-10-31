// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class BlazeColorMap extends CubicSplineColorMapRGBA {
	//
	protected static double[][] redChannelDefinition= new double[][]{{0,57},{48,90},{80,120},{111,150},{136,170},{170,200},{215,235},{232,245},{244,250},{255,250}};
	protected static double[][] greenChannelDefinition= new double[][]{{0,0},{31,20},{63,50},{110,100},{146,140},{190,200},{244,255},{253,255},{255,252}};
	protected static double[][] blueChannelDefinition= new double[][]{{0,128},{32,120},{82,100},{105,90},{120,82},{133,75},{177,30},{198,8},{203,4},{207,1},{209,0},{244,0},{245,9},{247,44},{252,175},{255,241}};
	//
	protected static int[][] redChannelConstraints= new int[][]{{244,255,250}};
	protected static int[][] greenChannelConstraints= new int[][]{{244,253,255}};
	protected static int[][] blueChannelConstraints= new int[][]{{209,244,0}};
	//
	public BlazeColorMap() {
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
}
