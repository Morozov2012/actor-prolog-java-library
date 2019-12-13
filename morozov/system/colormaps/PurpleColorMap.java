// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public class PurpleColorMap extends CubicSplineColorMapRGBA {
	//
	protected static double[][] redChannelDefinition= new double[][]{{0,16},{16,27},{38,50},{68,80},{86,100},{94,110},{101,120},{125,190},{140,227},{150,241},{162,250},{175,255},{255,255}};
	protected static double[][] greenChannelDefinition= new double[][]{{0,0},{21,10},{62,50},{101,96},{107,96},{135,61},{140,61},{166,100},{176,120},{194,150},{210,180},{220,198},{230,213},{233,220},{238,230},{247,245},{255,254}};
	protected static double[][] blueChannelDefinition= new double[][]{{0,2},{8,32},{19,90},{26,130},{38,190},{41,200},{45,210},{50,217},{102,217},{104,215},{107,210},{122,170},{132,140},{140,120},{145,110},{163,80},{170,70},{180,60},{190,48},{197,40},{228,40},{229,41},{230,46},{231,51},{237,95},{249,210},{255,251}};
	//
	protected static int[][] redChannelConstraints= new int[][]{{175,255,255}};
	protected static int[][] greenChannelConstraints= new int[][]{{101,107,96},{135,140,61}};
	protected static int[][] blueChannelConstraints= new int[][]{{50,102,217},{197,228,40}};
	//
	public PurpleColorMap() {
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
