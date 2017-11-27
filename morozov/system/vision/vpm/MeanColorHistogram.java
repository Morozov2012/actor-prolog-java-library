// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import morozov.system.vision.vpm.errors.*;
import morozov.terms.*;

public class MeanColorHistogram {
	//
	protected int numberOfBins;
	protected int histogramN;
	protected double[] histogramSumX;
	protected double[] histogramSumX2;
	//
	protected double[] histogramMean;
	protected double[] histogramSigma;
	//
	protected Term prologHistogramMean;
	protected Term prologHistogramSigma;
	//
	public MeanColorHistogram(int n) {
		numberOfBins= n;
		histogramSumX= new double[numberOfBins];
		histogramSumX2= new double[numberOfBins];
	}
	//
	public void add(double[] histogram) {
		if (numberOfBins != histogram.length) {
			throw new IncorrectNumberOfBinsInTheHistogram(numberOfBins,histogram.length);
		};
		histogramN++;
		for (int k=0; k < numberOfBins; k++) {
			double value= histogram[k];
			histogramSumX[k]= histogramSumX[k] + value;
			histogramSumX2[k]= histogramSumX2[k] + value * value;
		}
	}
	//
	public int getNumberOfBins() {
		return numberOfBins;
	}
	public int getCardinality() {
		return histogramN;
	}
	//
	public Term histogramMeanToTerm() {
		if (prologHistogramMean != null) {
			return prologHistogramMean;
		};
		computeMeanAndSigmaIfNecessary();
		Term prologHistogram= PrologEmptyList.instance;
		for (int n=numberOfBins-1; n >= 0; n--) {
			double value= histogramMean[n];
			prologHistogram= new PrologList(new PrologReal(value),prologHistogram);
		};
		prologHistogramMean= prologHistogram;
		return prologHistogramMean;
	}
	public Term histogramSigmaToTerm() {
		if (prologHistogramSigma != null) {
			return prologHistogramSigma;
		};
		computeMeanAndSigmaIfNecessary();
		Term prologHistogram= PrologEmptyList.instance;
		for (int n=numberOfBins-1; n >= 0; n--) {
			double value= histogramSigma[n];
			prologHistogram= new PrologList(new PrologReal(value),prologHistogram);
		};
		prologHistogramSigma= prologHistogram;
		return prologHistogramSigma;
	}
	//
	protected void computeMeanAndSigmaIfNecessary() {
		if (histogramMean==null) {
			computeMeanAndSigma();
		}
	}
	protected void computeMeanAndSigma() {
		histogramMean= new double[numberOfBins];
		histogramSigma= new double[numberOfBins];
		double minValue= 0;
		double maxValue= 0;
		boolean isFirstValue= true;
		for (int k=0; k < numberOfBins; k++) {
			if (histogramN > 0) {
				double mean= histogramSumX[k] / histogramN;
				histogramMean[k]= mean;
				double dispersion= (histogramSumX2[k] / histogramN) - mean*mean;
				if (dispersion < 0) {
					dispersion= 0;
				};
				histogramSigma[k]= StrictMath.sqrt(dispersion);
				if (!isFirstValue) {
					if (minValue > mean) {
						minValue= mean;
					};
					if (maxValue < mean) {
						maxValue= mean;
					}
				} else {
					minValue= mean;
					maxValue= mean;
					isFirstValue= false;
				}
			} else {
				histogramMean[k]= -1;
				histogramSigma[k]= -1;
			}
		};
		double delta= maxValue - minValue;
		if (!isFirstValue && delta > 0) {
			for (int k=0; k < numberOfBins; k++) {
				histogramMean[k]= (histogramMean[k] - minValue) / delta;
				histogramSigma[k]= histogramSigma[k] / delta;
			}
		} else {
			for (int k=0; k < numberOfBins; k++) {
				histogramMean[k]= -1;
				histogramSigma[k]= -1;
			}
		}
	}
}
