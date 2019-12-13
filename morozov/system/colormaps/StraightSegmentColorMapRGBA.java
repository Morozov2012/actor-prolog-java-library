// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public abstract class StraightSegmentColorMapRGBA extends ColorMapRGBA {
	//
	public StraightSegmentColorMapRGBA() {
	}
	//
	public abstract double[][] getRedChannelStraightSegments();
	public abstract double[][] getGreenChannelStraightSegments();
	public abstract double[][] getBlueChannelStraightSegments();
	//
	@Override
	public void createColorMap(int[][] map, int shift, int bandWidth, int totalSize, int alpha) {
		double[][] red= getRedChannelStraightSegments();
		double[][] green= getGreenChannelStraightSegments();
		double[][] blue= getBlueChannelStraightSegments();
		computeStraightSegmentColorMapChannel(map,0,shift,bandWidth,totalSize,red);
		computeStraightSegmentColorMapChannel(map,1,shift,bandWidth,totalSize,green);
		computeStraightSegmentColorMapChannel(map,2,shift,bandWidth,totalSize,blue);
		for (int k=0; k < bandWidth; k++) {
			if (shift+k >= totalSize) {
				break;
			};
			map[3][shift+k]= alpha;
		};
		return;
	}
	//
	public void computeStraightSegmentColorMapChannel(int[][] map, int channelNumber, int shift, int bandWidth, int totalSize, double[][] channelStraightSegments) {
		int channelStraightSegmentsNumber= channelStraightSegments.length;
		for (int k=0; k < bandWidth; k++) {
			boolean pointIsFound= false;
			double firstX= 0;
			double secondX= maximalIndex;
			double firstY= 0;
			double secondY= maximalIndex;
			for (int m=0; m < channelStraightSegmentsNumber; m++) {
				double[] pair= channelStraightSegments[m];
				double x= pair[0];
				x= x * (bandWidth-1) / maximalIndex;
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
				if (shift+k >= totalSize) {
					return;
				};
				int value= (int)StrictMath.round(firstY + (k - firstX) * (secondY - firstY) / (secondX - firstX));
				if (value < 0) {
					value= 0;
				} else if (value > maximalIndex) {
					value= maximalIndex;
				};
				map[channelNumber][shift+k]= value;
			}
		}
	}
}
