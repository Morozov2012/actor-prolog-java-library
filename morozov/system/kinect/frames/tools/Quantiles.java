// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.tools;

public class Quantiles {
	//
	public int lowerQuantile;
	public int upperQuantile;
	//
	protected static int numberOfBins= 256;
	protected static double lowerBoundRatio= 0.10;
	protected static double upperBoundRatio= 0.90;
	//
	public Quantiles(int min, int max) {
		lowerQuantile= min;
		upperQuantile= max;
	}
	//
	public double standardize(int value) {
		return ((double)value - lowerQuantile) / (upperQuantile - lowerQuantile);
	}
	//
	public static Quantiles computeBounds(short[] array) {
		int arrayLength= array.length;
		if (arrayLength <= 0) {
			return new Quantiles(0,0);
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
		return new Quantiles(min,max);
	}
	public static Quantiles computeQuantiles(short[] array, boolean ignoreMinimum) {
		int arrayLength= array.length;
		if (arrayLength <= 0) {
			return new Quantiles(0,0);
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
		int step= (max - min) / numberOfBins;
		if (step < 1) {
			step= 1;
		};
		if (ignoreMinimum) {
			min= 0;
		} else {
			int lowerQuantile= min;
			int lowerBound= (int)(arrayLength * lowerBoundRatio);
			while (true) {
				lowerQuantile= lowerQuantile + step;
				if (lowerQuantile >= max) {
					break;
				};
				int quantity= 0;
				for (int n=0; n < arrayLength; n++) {
					short value= array[n];
					if (value >= lowerQuantile) {
						quantity++;
					}
				};
				if (quantity >= lowerBound) {
					min= lowerQuantile;
					break;
				}
			}
		};
		int upperQuantile= max;
		int upperBound= (int)(arrayLength * upperBoundRatio);
		while (true) {
			upperQuantile= upperQuantile - step;
			if (upperQuantile <= 0) {
				break;
			};
			int quantity= 0;
			for (int n=0; n < arrayLength; n++) {
				short value= array[n];
				if (value <= upperQuantile) {
					quantity++;
				}
			};
			if (quantity <= upperBound) {
				max= upperQuantile;
				break;
			}
		};
		return new Quantiles(min,max);
	}
	public static Quantiles computeQuantiles(short[] array, byte[] playerIndex) {
		int arrayLength= array.length;
		if (arrayLength <= 0) {
			return new Quantiles(0,0);
		};
		int min= -1;
		int max= -1;
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
				}
			}
		};
		int lowerQuantile= min;
		int upperQuantile= max;
		int step= (max - min) / numberOfBins;
		if (step < 1) {
			step= 1;
		};
		int lowerBound= (int)(arrayLength * lowerBoundRatio);
		int upperBound= (int)(arrayLength * upperBoundRatio);
		while (true) {
			lowerQuantile= lowerQuantile + step;
			if (lowerQuantile >= max) {
				break;
			};
			int quantity= 0;
			for (int n=0; n < arrayLength; n++) {
				int index= (int)playerIndex[n];
				if (index >= 0) {
					short value= array[n];
					if (value >= lowerQuantile) {
						quantity++;
					}
				}
			};
			if (quantity >= lowerBound) {
				min= lowerQuantile;
				break;
			}
		};
		while (true) {
			upperQuantile= upperQuantile - step;
			if (upperQuantile <= 0) {
				break;
			};
			int quantity= 0;
			for (int n=0; n < arrayLength; n++) {
				int index= (int)playerIndex[n];
				if (index >= 0) {
					short value= array[n];
					if (value <= upperQuantile) {
						quantity++;
					}
				}
			};
			if (quantity <= upperBound) {
				max= upperQuantile;
				break;
			}
		};
		return new Quantiles(min,max);
	}
}
