// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

public abstract class CubicSplineColorMapRGBA extends ColorMapRGBA {
	//
	protected static int[][] emptyChannelStraightSegments= new int[][]{};
	//
	public CubicSplineColorMapRGBA() {
	}
	//
	public abstract double[][] getRedChannelDefinition();
	public abstract double[][] getGreenChannelDefinition();
	public abstract double[][] getBlueChannelDefinition();
	//
	public abstract int[][] getRedChannelConstraints();
	public abstract int[][] getGreenChannelConstraints();
	public abstract int[][] getBlueChannelConstraints();
	//
	public int[][] getRedChannelStraightSegments() {
		return emptyChannelStraightSegments;
	}
	public int[][] getGreenChannelStraightSegments() {
		return emptyChannelStraightSegments;
	}
	public int[][] getBlueChannelStraightSegments() {
		return emptyChannelStraightSegments;
	}
	//
	@Override
	public void createColorMap(int[][] map, int shift, int bandWidth, int totalSize, int alpha) {
		double[][] dRed= getRedChannelDefinition();
		double[][] dGreen= getGreenChannelDefinition();
		double[][] dBlue= getBlueChannelDefinition();
		int[][] cRed= getRedChannelConstraints();
		int[][] cGreen= getGreenChannelConstraints();
		int[][] cBlue= getBlueChannelConstraints();
		int[][] sRed= getRedChannelStraightSegments();
		int[][] sGreen= getGreenChannelStraightSegments();
		int[][] sBlue= getBlueChannelStraightSegments();
		computeCubicSplineColorMapChannel(map,0,shift,bandWidth,totalSize,dRed,cRed,sRed);
		computeCubicSplineColorMapChannel(map,1,shift,bandWidth,totalSize,dGreen,cGreen,sGreen);
		computeCubicSplineColorMapChannel(map,2,shift,bandWidth,totalSize,dBlue,cBlue,sBlue);
		for (int k=0; k < bandWidth; k++) {
			if (shift+k >= totalSize) {
				break;
			};
			map[3][shift+k]= alpha;
		};
		return;
	}
	//
	public void computeCubicSplineColorMapChannel(int[][] map, int channelNumber, int shift, int bandWidth, int totalSize, double[][] channelDefinition, int[][] channelConstraints, int[][] channelStraightSegments) {
		int channelDefinitionLength= channelDefinition.length;
		int channelConstraintsNumber= channelConstraints.length;
		int channelStraightSegmentsNumber= channelStraightSegments.length;
		double[] b= new double[channelDefinitionLength];
		double[] c= new double[channelDefinitionLength];
		double[] d= new double[channelDefinitionLength];
		computeCubicSpline(channelDefinition,b,c,d);
		for (int k=0; k < bandWidth; k++) {
			if (shift+k >= totalSize) {
				break;
			};
			double u= (double)k * maximalIndex / (bandWidth-1);
			int value= 0;
			boolean isConstrainedSegment= false;
			int integerU= (int)StrictMath.round(u);
			for (int m=0; m < channelStraightSegmentsNumber; m++) {
				int[] item= channelStraightSegments[m];
				int leftBound= item[0];
				int rightBound= item[2];
				if (leftBound <= integerU && integerU <= rightBound) {
					isConstrainedSegment= true;
					double firstY= item[1];
					double secondY= item[3];
					value= (int)StrictMath.round(firstY + (u - leftBound) * (secondY - firstY) / (rightBound - leftBound));
					break;
				}
			};
			if (!isConstrainedSegment) {
				for (int m=0; m < channelConstraintsNumber; m++) {
					int[] item= channelConstraints[m];
					int leftBound= item[0];
					int rightBound= item[1];
					if (leftBound <= integerU && integerU <= rightBound) {
						isConstrainedSegment= true;
						value= item[2];
						break;
					}
				}
			};
			if (!isConstrainedSegment) {
				value= (int)StrictMath.round(seval(u,channelDefinition,b,c,d));
			};
			if (value < 0) {
				value= 0;
			} else if (value > maximalIndex) {
				value= maximalIndex;
			};
			map[channelNumber][shift+k]= value;
		}
	}
	//
	public void computeCubicSpline(double[][] xy, double[] b, double[] c, double[] d) {
		int n= xy.length-1;
		int nm1= n-1;
		if (n < 1) {
			return;
		} else if (n < 2) {
			b[0]= (xy[1][1]-xy[0][1])/(xy[1][0]-xy[0][0]);
			c[0]= 0;
			d[0]= 0;
			b[1]= b[0];
			c[1]= 0;
			d[1]= 0;
			return;
		} else {
			d[0]= xy[1][0]-xy[0][0];
			c[1]= (xy[1][1]-xy[0][1])/d[0];
			for (int i=1; i <= nm1; i++) {
				d[i]= xy[i+1][0]-xy[i][0];
				b[i]= 2*(d[i-1]+d[i]);
				c[i+1]= (xy[i+1][1]-xy[i][1])/d[i];
				c[i]= c[i+1]-c[i];
			};
			b[0]= -d[0];
			b[n]= -d[n-1];
			c[0]= 0;
			c[n]= 0;
			if (n != 2) {
				c[0]= c[2]/(xy[3][0]-xy[1][0]) - c[1]/(xy[2][0]-xy[0][0]);
				c[n]= c[n-1]/(xy[n][0]-xy[n-2][0]) - c[n-2]/(xy[n-1][0]-xy[n-3][0]);
				c[0]= c[0]*(d[0]*d[0])/(xy[3][0]-xy[0][0]);
				c[n]=-c[n]*(d[n-1]*d[n-1])/(xy[n][0]-xy[n-3][0]);
			};
			for (int i=1; i <= n; i++) {
				double t= d[i-1]/b[i-1];
				b[i]= b[i]-t*d[i-1];
				c[i]= c[i]-t*c[i-1];
			};
			c[n]= c[n]/b[n];
			for (int ib=1; ib <= nm1; ib++) {
				int i= n-ib;
				c[i]= (c[i]-d[i]*c[i+1])/b[i];
			}
		};
		b[n]= (xy[n][1]-xy[nm1][1])/d[nm1] + d[nm1]*(c[nm1]+2*c[n]);
		for (int i=0; i <= nm1; i++) {
			b[i]= (xy[i+1][1]-xy[i][1])/d[i] - d[i]*(c[i+1]+2*c[i]);
			d[i]= (c[i+1]-c[i])/d[i];
			c[i]= 3*c[i];
		};
		c[n]= 3*c[n];
		d[n]= d[n-1];
	}
	//
	public double seval(double u, double[][] xy, double[] b, double[] c, double[] d) {
		int n= xy.length-1;
		if (n < 0) {
			return 0;
		} else if (n < 1) {
			return xy[1][0];
		};
		int i= 0;
		if (u < xy[0][0] || u > xy[1][0]) {
			int j= n+1;
			while (true) {
				int k= (i+j)/2;
				if (u < xy[k][0]) {
					j= k;
				} else { // if u >= xy[k][0],
					i= k;
				};
				if (j <= i+1) {
					break;
				}
			}
		};
		double dX= u-xy[i][0];
		return xy[i][1]+dX*(b[i]+dX*(c[i]+dX*d[i]));
	}
}
