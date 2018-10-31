// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public abstract class StraightSegmentColorMapRGBA extends ColorMapRGBA {
	//
	protected int[][] recentMap= null;
	protected int recentAlpha;
	//
	protected static int maximalIndex= 255;
	//
	public StraightSegmentColorMapRGBA() {
	}
	//
	public abstract double[][] getRedChannelStraightSegments();
	public abstract double[][] getGreenChannelStraightSegments();
	public abstract double[][] getBlueChannelStraightSegments();
	//
	public int[][] createColorMap(int size, int alpha) {
		int[][] map= new int[4][size];
		double[][] red= getRedChannelStraightSegments();
		double[][] green= getGreenChannelStraightSegments();
		double[][] blue= getBlueChannelStraightSegments();
		computeStraightSegmentColorMapChannel(map,0,red);
		computeStraightSegmentColorMapChannel(map,1,green);
		computeStraightSegmentColorMapChannel(map,2,blue);
		for (int k=0; k < size; k++) {
			map[3][k]= alpha;
		};
		return map;
	}
	//
	public void computeStraightSegmentColorMapChannel(int[][] map, int channelNumber, double[][] channelStraightSegments) {
		int channelStraightSegmentsNumber= channelStraightSegments.length;
		int mapLength= 0;
		if (map.length > 0) {
			mapLength= map[0].length;
		};
		for (int k=0; k < mapLength; k++) {
			boolean pointIsFound= false;
			double firstX= 0;
			double secondX= maximalIndex;
			double firstY= 0;
			double secondY= maximalIndex;
			for (int m=0; m < channelStraightSegmentsNumber; m++) {
				double[] pair= channelStraightSegments[m];
				double x= pair[0];
				x= x * (mapLength-1) / maximalIndex;
				int integerX= (int)StrictMath.round(x);
				if (integerX==k) {
					map[channelNumber][k]= (int)StrictMath.round(pair[1]);
					pointIsFound= true;
					break;
				} else if (integerX < k) {
					firstX= x;
					firstY= pair[1];
				} else if (integerX > k) {
					secondX= x;
					secondY= pair[1];
					break;
				}
			};
			if (!pointIsFound) {
				int value= (int)StrictMath.round(firstY + (k - firstX) * (secondY - firstY) / (secondX - firstX));
				if (value < 0) {
					value= 0;
				} else if (value > maximalIndex) {
					value= maximalIndex;
				};
				map[channelNumber][k]= value;
			}
		}
	}
}
