// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.tools;

import morozov.system.*;

public class FloatQuantiles extends Quantiles {
	//
	protected float minimum;
	protected float maximum;
	protected float range;
	protected boolean useExtremes;
	protected float lowerQuantile;
	protected float upperQuantile;
	protected float quantileRange;
	protected boolean useQuantiles;
	//
	protected double defaultLowerBoundRatio= 0.01;
	protected double defaultUpperBoundRatio= 0.99;
	//
	///////////////////////////////////////////////////////////////
	//
	public FloatQuantiles(
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
	protected void setQuantiles(float min, float max, float q1, float q2) {
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
	public void setMinimum(float value) {
		minimum= value;
	}
	public void setMaximum(float value) {
		maximum= value;
	}
	public void setRange(float value) {
		range= value;
	}
	public void setUseExtremes(boolean mode) {
		useExtremes= mode;
	}
	public void setLowerQuantile(float value) {
		lowerQuantile= value;
	}
	public void setUpperQuantile(float value) {
		upperQuantile= value;
	}
	public void setQuantileRange(float value) {
		quantileRange= value;
	}
	public void setUseQuantiles(boolean mode) {
		useQuantiles= mode;
	}
	//
	public float getMinimum() {
		return minimum;
	}
	public float getMaximum() {
		return maximum;
	}
	public float getRange() {
		return range;
	}
	public boolean useExtremes() {
		return useExtremes;
	}
	public float getLowerQuantile() {
		return lowerQuantile;
	}
	public float getUpperQuantile() {
		return upperQuantile;
	}
	public float getQuantileRange() {
		return quantileRange;
	}
	public boolean useQuantiles() {
		return useQuantiles;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setDefaultLowerBoundRatio(double r) {
		defaultLowerBoundRatio= r;
	}
	@Override
	public void setDefaultUpperBoundRatio(double r) {
		defaultUpperBoundRatio= r;
	}
	//
	@Override
	public double getDefaultLowerBoundRatio() {
		return defaultLowerBoundRatio;
	}
	@Override
	public double getDefaultUpperBoundRatio() {
		return defaultUpperBoundRatio;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public float standardizeByQuantilesAndExtremes(float value) {
		if (useQuantiles) {
			while (value < lowerQuantile) {
				value= value + quantileRange;
			};
			while (value > upperQuantile) {
				value= value - quantileRange;
			};
			return (value - lowerQuantile) / quantileRange;
		} else {
			return value;
		}
	}
	public float standardizeByQuantiles(float value) {
		if (useQuantiles) {
			return (value - lowerQuantile) / quantileRange;
		} else {
			return value;
		}
	}
	public float standardizeByExtremes(float value) {
		if (useExtremes) {
			return (value - minimum) / range;
		} else {
			return value;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void computeQuantiles(float[] cloud, boolean extractPlayers, byte[] playerIndex, int frameWidth, int frameHeight) {
		int arrayLength= cloud.length;
		if (arrayLength <= 0) {
			setQuantiles(0.0f,1.0f,0.0f,1.0f);
			return;
		};
		float min= -1;
		float max= -1;
		int totalNumberOfValues= 0;
		boolean checkValues= false;
		for (int w1=0; w1 < frameWidth; w1++) {
			for (int h1=0; h1 < frameHeight; h1++) {
				int index1= h1*frameWidth + w1;
				if (extractPlayers && playerIndex[index1] < 0) {
					continue;
				};
				float value= cloud[index1*3+2];
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
		float lowerQuantile= min;
		float upperQuantile= max;
		float step= (max - min) / numberOfBins;
		if (step <= 0) {
			step= 1.0f;
		};
		if (lowerBoundIsZero()) {
			lowerQuantile= 0.0f;
		} else {
			float lBR= getLowerBoundRatioValue();
			float lowerBound= (int)(totalNumberOfValues * lBR);
			while (true) {
				lowerQuantile= lowerQuantile + step;
				if (lowerQuantile >= max) {
					break;
				};
				int quantity= 0;
				for (int w1=0; w1 < frameWidth; w1++) {
					for (int h1=0; h1 < frameHeight; h1++) {
						int index1= h1*frameWidth + w1;
						if (extractPlayers && playerIndex[index1] < 0) {
							continue;
						};
						float value= cloud[index1*3+2];
						if (value <= lowerQuantile) {
							quantity++;
						}
					}
				};
				if (quantity >= lowerBound) {
					break;
				}
			}
		};
		if (upperBoundIsZero()) {
			upperQuantile= 0.0f;
		} else {
			float uBR= getUpperBoundRatioValue();
			float upperBound= (int)(totalNumberOfValues * uBR);
			while (true) {
				upperQuantile= upperQuantile - step;
				if (upperQuantile <= 0) {
					break;
				};
				int quantity= 0;
				for (int w1=0; w1 < frameWidth; w1++) {
					for (int h1=0; h1 < frameHeight; h1++) {
						int index1= h1*frameWidth + w1;
						if (extractPlayers && playerIndex[index1] < 0) {
							continue;
						};
						float value= cloud[index1*3+2];
						if (value <= upperQuantile) {
							quantity++;
						}
					}
				};
				if (quantity <= upperBound) {
					break;
				}
			}
		};
		setQuantiles(min,max,lowerQuantile,upperQuantile);
	}
}
