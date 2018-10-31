// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class GreenColorMap extends CubicSplineColorMapRGBA {
	//
	protected static double[][] redChannelDefinition= new double[][]{{0,0},{29,0},{36,5},{51,16},{59,25},{68,40},{75,55},{85,90},{100,164},{114,225},{122,245},{129,254},{175,254},{199,250},{231,247},{255,250}};
	protected static double[][] greenChannelDefinition= new double[][]{{0,5},{13,35},{21,60},{31,90},{36,100},{44,115},{63,150},{80,175},{90,190},{108,210},{119,210},{128,200},{147,160},{159,130},{163,120},{169,100},{180,60},{187,40},{197,25},{208,40},{217,70},{222,90},{227,111},{237,150},{242,180},{246,205},{250,227},{255,248}};
	protected static double[][] blueChannelDefinition= new double[][]{{0,76},{15,145},{28,204},{31,208},{34,208},{38,203},{66,100},{73,75},{81,55},{91,35},{105,15},{118,5},{129,5},{148,20},{166,35},{186,50},{204,80},{217,115},{224,135},{229,150},{234,163},{243,205},{251,240},{255,253}};
	//
	protected static int[][] redChannelConstraints= new int[][]{{0,29,0},{129,175,254}};
	protected static int[][] greenChannelConstraints= new int[][]{{108,119,210}};
	protected static int[][] blueChannelConstraints= new int[][]{{31,34,208},{118,129,5}};
	//
	public GreenColorMap() {
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
