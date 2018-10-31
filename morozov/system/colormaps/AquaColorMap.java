// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class AquaColorMap extends CubicSplineColorMapRGBA {
	//
	protected static double[][] redChannelDefinition= new double[][]{{0,18},{14,25},{35,40},{60,50},{88,70},{149,130},{189,180},{233,230},{255,247}};
	protected static double[][] greenChannelDefinition= new double[][]{{0,255},{6,240},{10,225},{15,205},{25,160},{35,125},{57,90},{67,80},{81,60},{102,30},{111,20},{124,10},{133,4},{155,4},{165,10},{178,20},{201,55},{215,95},{221,120},{239,200},{244,220},{255,253}};
	protected static double[][] blueChannelDefinition= new double[][]{{0,253},{18,250},{38,240},{63,205},{76,190},{95,175},{124,150},{144,130},{165,100},{202,35},{216,20},{232,10},{242,5},{255,0}};
	//
	protected static int[][] redChannelConstraints= new int[][]{};
	protected static int[][] greenChannelConstraints= new int[][]{{133,155,4}};
	protected static int[][] blueChannelConstraints= new int[][]{};
	//
	public AquaColorMap() {
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
