// (c) 2018 Alexei A. Morozov

package morozov.system.colormaps;

import morozov.system.*;

public abstract class ColorMapRGBA {
	//
	protected int[][] recentMap= null;
	protected int recentAlpha;
	protected int recentNumberOfBands= 1;
	//
	protected static int maximalIndex= 255;
	//
	///////////////////////////////////////////////////////////////
	//
	public ColorMapRGBA() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int[][] getColorMap(int size, int alpha) {
		return getColorMap(size,getDefaultNumberOfBands(),alpha);
	}
	public int[][] getColorMap(int size, IntegerAttribute numberOfBands, int alpha) {
		return getColorMap(size,computeNumberOfBands(numberOfBands),alpha);
	}
	public int[][] getColorMap(int size, int numberOfBands, int alpha) {
		synchronized (this) {
			if (recentMap != null && recentMap.length==size && recentNumberOfBands==numberOfBands && recentAlpha==alpha) {
				return recentMap;
			};
			recentMap= createColorMap(size,numberOfBands,alpha);
			recentAlpha= alpha;
			recentNumberOfBands= numberOfBands;
			return recentMap;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int[][] getRecentMap() {
		return recentMap;
	}
	public int getRecentAlpha() {
		return recentAlpha;
	}
	public int getRecentNumberOfBands() {
		return recentNumberOfBands;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int computeNumberOfBands(IntegerAttribute numberOfBands) {
		long longValue= numberOfBands.getValue(getDefaultNumberOfBands());
		int integerValue;
		if (longValue > Integer.MAX_VALUE) {
			integerValue= Integer.MAX_VALUE;
		} else if (longValue < Integer.MIN_VALUE) {
			integerValue= Integer.MIN_VALUE;
		} else {
			integerValue= (int)longValue;
		};
		return integerValue;
	}
	//
	public int[][] createColorMap(int totalSize, int numberOfBands, int alpha) {
		int[][] cm= new int[4][totalSize];
		if (totalSize <= 0) {
			return cm;
		};
		int bandWidth= totalSize / numberOfBands;
		for (int k=0; k <= numberOfBands; k++) {
			createColorMap(cm,k*bandWidth,bandWidth,totalSize,alpha);
		};
		return cm;
	}
	//
	public void createColorMap(int[][] cm, int shift, int bandWidth, int totalSize, int alpha) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getDefaultNumberOfBands() {
		return 1;
	}
}
