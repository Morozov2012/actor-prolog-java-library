// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.tools;

import morozov.system.*;

public class Quantiles {
	//
	protected RealAttribute lowerBoundRatio;
	protected RealAttribute upperBoundRatio;
	protected YesNoDefault lowerBoundIsZero;
	protected YesNoDefault upperBoundIsZero;
	protected boolean colorMapIsIterative;
	//
	protected int numberOfBins= 256;
	//
	protected double defaultLowerBoundRatio= 0.10;
	protected double defaultUpperBoundRatio= 0.90;
	//
	///////////////////////////////////////////////////////////////
	//
	public Quantiles(
			RealAttribute givenLowerBoundRatio,
			RealAttribute givenUpperBoundRatio,
			YesNoDefault givenLowerBoundIsZero,
			YesNoDefault givenUpperBoundIsZero,
			boolean givenColorMapIsIterative) {
		lowerBoundRatio= givenLowerBoundRatio;
		upperBoundRatio= givenUpperBoundRatio;
		lowerBoundIsZero= givenLowerBoundIsZero;
		upperBoundIsZero= givenUpperBoundIsZero;
		colorMapIsIterative= givenColorMapIsIterative;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setLowerBoundRatio(RealAttribute r) {
		lowerBoundRatio= r;
	}
	public void setUpperBoundRatio(RealAttribute r) {
		upperBoundRatio= r;
	}
	public void setLowerBoundIsZero(YesNoDefault mode) {
		lowerBoundIsZero= mode;
	}
	public void setUpperBoundIsZero(YesNoDefault mode) {
		upperBoundIsZero= mode;
	}
	public void setColorMapIsIterative(boolean mode) {
		colorMapIsIterative= mode;
	}
	//
	public RealAttribute getLowerBoundRatio() {
		return lowerBoundRatio;
	}
	public float getLowerBoundRatioValue() {
		return (float)lowerBoundRatio.getValue(getDefaultLowerBoundRatio());
	}
	public RealAttribute getUpperBoundRatio() {
		return upperBoundRatio;
	}
	public float getUpperBoundRatioValue() {
		return (float)upperBoundRatio.getValue(getDefaultUpperBoundRatio());
	}
	public YesNoDefault getLowerBoundIsZero() {
		return lowerBoundIsZero;
	}
	public boolean lowerBoundIsZero() {
		return lowerBoundIsZero.toBoolean(false);
	}
	public YesNoDefault getUpperBoundIsZero() {
		return upperBoundIsZero;
	}
	public boolean upperBoundIsZero() {
		return upperBoundIsZero.toBoolean(false);
	}
	public boolean getColorMapIsIterative() {
		return colorMapIsIterative;
	}
	//
	public void setNumberOfBins(int n) {
		numberOfBins= n;
	}
	public void setDefaultLowerBoundRatio(double r) {
		defaultLowerBoundRatio= r;
	}
	public void setDefaultUpperBoundRatio(double r) {
		defaultUpperBoundRatio= r;
	}
	//
	public int getNumberOfBins() {
		return numberOfBins;
	}
	public double getDefaultLowerBoundRatio() {
		return defaultLowerBoundRatio;
	}
	public double getDefaultUpperBoundRatio() {
		return defaultUpperBoundRatio;
	}
}
