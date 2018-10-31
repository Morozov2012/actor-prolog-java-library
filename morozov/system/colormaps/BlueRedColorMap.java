// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class BlueRedColorMap extends CubicSplineColorMapRGBA {
	//
	protected static double[][] redChannelDefinition= new double[][]{{0,0},{107,0},{109,3},{114,18},{141,177},{145,224},{149,255},{255,255}};
	protected static double[][] greenChannelDefinition= new double[][]{{0,0},{4,25},{7,52},{11,93},{15,138},{21,190},{33,254},{34,255},{40,255},{48,230},{60,170},{66,140},{73,115},{80,90},{88,60},{97,27},{103,10},{107,1},{108,0},{149,0},{175,50},{188,100},{199,165},{207,210},{211,230},{215,245},{218,254},{219,255},{253,255},{255,254}};
	protected static double[][] blueChannelDefinition= new double[][]{{0,0},{2,10},{6,40},{14,120},{25,210},{32,250},{34,255},{149,255},{152,240},{160,180},{173,60},{180,10},{182,1},{183,0},{218,0},{219,1},{229,30},{242,80},{255,121}};
	//
	protected static int[][] redChannelConstraints= new int[][]{{0,107,0},{149,255,255}};
	protected static int[][] greenChannelConstraints= new int[][]{{34,40,255},{108,149,0},{219,253,255}};
	protected static int[][] blueChannelConstraints= new int[][]{{34,149,255},{183,218,0}};
	//
	public BlueRedColorMap() {
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
