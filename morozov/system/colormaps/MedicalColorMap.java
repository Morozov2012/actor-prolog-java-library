// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class MedicalColorMap extends StraightSegmentColorMapRGBA {
	//
	protected static double[][] redChannelStraightSegments= new double[][]{{0,0},{16,130},{32,100},{48,60},{80,0},{144,0},{160,130},{176,255},{240,255},{255,110}};
	protected static double[][] greenChannelStraightSegments= new double[][]{{0,0},{80,0},{112,180},{144,200},{160,230},{176,255},{192,200},{240,50},{255,50}};
	protected static double[][] blueChannelStraightSegments= new double[][]{{0,0},{16,130},{80,150},{112,180},{128,100},{160,50},{255,50}};
	//
	public MedicalColorMap() {
	}
	//
	public double[][] getRedChannelStraightSegments() {
		return redChannelStraightSegments;
	}
	public double[][] getGreenChannelStraightSegments() {
		return greenChannelStraightSegments;
	}
	public double[][] getBlueChannelStraightSegments() {
		return blueChannelStraightSegments;
	}
}
