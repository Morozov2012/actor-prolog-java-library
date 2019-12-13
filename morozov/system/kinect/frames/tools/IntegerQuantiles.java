// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.tools;

import morozov.system.*;

public class IntegerQuantiles extends Quantiles {
	//
	protected int minimum;
	protected int maximum;
	protected int range;
	protected boolean useExtremes;
	protected int lowerQuantile;
	protected int upperQuantile;
	protected int quantileRange;
	protected boolean useQuantiles;
	//
	///////////////////////////////////////////////////////////////
	//
	public IntegerQuantiles(
			RealAttribute lowerBound,
			RealAttribute upperBound,
			YesNoDefault lowerBoundIsZero,
			YesNoDefault upperBoundIsZero,
			boolean colorMapIsIterative) {
		super(	lowerBound,
			upperBound,
			lowerBoundIsZero,
			upperBoundIsZero,
			colorMapIsIterative);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void setQuantiles(int min, int max, int q1, int q2) {
		minimum= min;
		maximum= max;
		range= maximum - minimum;
		if (minimum < maximum) {
			useExtremes= true;
		} else {
			useExtremes= false;
		};
		lowerQuantile= q1;
		upperQuantile= q2;
		quantileRange= upperQuantile - lowerQuantile;
		if (lowerQuantile < upperQuantile) {
			useQuantiles= true;
		} else {
			useQuantiles= false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setMinimum(int value) {
		minimum= value;
	}
	public void setMaximum(int value) {
		maximum= value;
	}
	public void setRange(int value) {
		range= value;
	}
	public void setUseExtremes(boolean mode) {
		useExtremes= mode;
	}
	public void setLowerQuantile(int value) {
		lowerQuantile= value;
	}
	public void setUpperQuantile(int value) {
		upperQuantile= value;
	}
	public void setQuantileRange(int value) {
		quantileRange= value;
	}
	public void setUseQuantiles(boolean mode) {
		useQuantiles= mode;
	}
	//
	public int getMinimum() {
		return minimum;
	}
	public int getMaximum() {
		return maximum;
	}
	public int getRange() {
		return range;
	}
	public boolean useExtremes() {
		return useExtremes;
	}
	public int getLowerQuantile() {
		return lowerQuantile;
	}
	public int getUpperQuantile() {
		return upperQuantile;
	}
	public int getQuantileRange() {
		return quantileRange;
	}
	public boolean useQuantiles() {
		return useQuantiles;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public float standardize(int value) {
		if (colorMapIsIterative) {
			return standardizeByQuantilesAndExtremes(value);
		} else {
			return standardizeByQuantiles(value);
		}
	}
	//
	public float standardizeByQuantilesAndExtremes(int value) {
		if (useQuantiles) {
			while (value < lowerQuantile) {
				value= value + quantileRange;
			};
			while (value > upperQuantile) {
				value= value - quantileRange;
			};
			return ((float)value - lowerQuantile) / quantileRange;
		} else {
			return value;
		}
	}
	public float standardizeByQuantiles(int value) {
		if (useQuantiles) {
			return ((float)value - lowerQuantile) / quantileRange;
		} else {
			return value;
		}
	}
	public float standardizeByExtremes(int value) {
		if (useExtremes) {
			return ((float)value - minimum) / range;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void computeBounds(short[] array) {
		int arrayLength= array.length;
		if (arrayLength <= 0) {
			setQuantiles(0,1,0,1);
			return;
		};
		int min= array[0];
		int max= array[0];
		for (int n=1; n < arrayLength; n++) {
			short value= array[n];
			if (min > value) {
				min= value;
			};
			if (max < value) {
				max= value;
			}
		};
		setQuantiles(min,max,min,max);
	}
	//
	public void computeQuantiles(short[] array) {
		int arrayLength= array.length;
		if (arrayLength <= 0) {
			setQuantiles(0,1,0,1);
			return;
		};
		int min= array[0];
		int max= array[0];
		int totalNumberOfValues= 0;
		for (int n=1; n < arrayLength; n++) {
			short value= array[n];
			if (min > value) {
				min= value;
			};
			if (max < value) {
				max= value;
			};
			totalNumberOfValues++;
		};
		int step= (max - min) / numberOfBins;
		if (step <= 0) {
			step= 1;
		};
		int currentLowerQuantile= min;
		if (lowerBoundIsZero()) {
			currentLowerQuantile= 0;
		} else {
			float lBR= getLowerBoundRatioValue();
			int lowerBound= (int)(totalNumberOfValues * lBR);
			while (true) {
				currentLowerQuantile= currentLowerQuantile + step;
				if (currentLowerQuantile >= max) {
					break;
				};
				int quantity= 0;
				for (int n=0; n < arrayLength; n++) {
					short value= array[n];
					if (value <= currentLowerQuantile) {
						quantity++;
					}
				};
				if (quantity >= lowerBound) {
					break;
				}
			}
		};
		int currentUpperQuantile= max;
		if (upperBoundIsZero()) {
			currentUpperQuantile= 0;
		} else {
			float uBR= getUpperBoundRatioValue();
			int upperBound= (int)(totalNumberOfValues * uBR);
			while (true) {
				currentUpperQuantile= currentUpperQuantile - step;
				if (currentUpperQuantile <= 0) {
					break;
				};
				int quantity= 0;
				for (int n=0; n < arrayLength; n++) {
					short value= array[n];
					if (value <= currentUpperQuantile) {
						quantity++;
					}
				};
				if (quantity <= upperBound) {
					break;
				}
			}
		};
		setQuantiles(min,max,currentLowerQuantile,currentUpperQuantile);
	}
	public void computeQuantiles(short[] array, byte[] playerIndex) {
		int arrayLength= array.length;
		if (arrayLength <= 0) {
			setQuantiles(0,1,0,1);
			return;
		};
		int min= -1;
		int max= -1;
		int totalNumberOfValues= 0;
		boolean checkValues= false;
		for (int n=0; n < arrayLength; n++) {
			int index= (int)playerIndex[n];
			if (index >= 0) {
				short value= array[n];
				if (checkValues) {
					if (min > value) {
						min= value;
					};
					if (max < value) {
						max= value;
					}
				} else {
					min= value;
					max= value;
					checkValues= true;
				};
				totalNumberOfValues++;
			}
		};
		int currentLowerQuantile= min;
		int currentUpperQuantile= max;
		int step= (max - min) / numberOfBins;
		if (step <= 0) {
			step= 1;
		};
		float lBR= getLowerBoundRatioValue();
		float uBR= getUpperBoundRatioValue();
		int lowerBound= (int)(totalNumberOfValues * lBR);
		int upperBound= (int)(totalNumberOfValues * uBR);
		while (true) {
			currentLowerQuantile= currentLowerQuantile + step;
			if (currentLowerQuantile >= max) {
				break;
			};
			int quantity= 0;
			for (int n=0; n < arrayLength; n++) {
				int index= (int)playerIndex[n];
				if (index >= 0) {
					short value= array[n];
					if (value <= currentLowerQuantile) {
						quantity++;
					}
				}
			};
			if (quantity >= lowerBound) {
				break;
			}
		};
		while (true) {
			currentUpperQuantile= currentUpperQuantile - step;
			if (currentUpperQuantile <= 0) {
				break;
			};
			int quantity= 0;
			for (int n=0; n < arrayLength; n++) {
				int index= (int)playerIndex[n];
				if (index >= 0) {
					short value= array[n];
					if (value <= currentUpperQuantile) {
						quantity++;
					}
				}
			};
			if (quantity <= upperBound) {
				break;
			}
		};
		setQuantiles(min,max,currentLowerQuantile,currentUpperQuantile);
	}
}
