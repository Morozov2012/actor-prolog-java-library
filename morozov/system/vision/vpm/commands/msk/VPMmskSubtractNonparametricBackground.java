// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;

public class VPMmskSubtractNonparametricBackground extends VPMmskBackgroundSubtractionCommand {
	//
	protected long numberOfProcessedFrames= 0;
	//
	protected int requestedNumberOfBins;
	protected int tripleNumberOfBins;
	protected double alphaLevel;
	protected long stabilityLevel;
	protected int reductionCoefficient;
	//
	protected int imageWidth;
	protected int imageHeight;
	protected int vectorLength;
	protected int[] counters;
	protected int[][] histograms;
	//
	protected static int maximalNumberOfBins= Integer.MAX_VALUE / 3;
	protected static int defaultNumberOfBins= 10;
	protected static double defaultAlphaLevel= 0.05;
	protected static int maximalStabilityLevel= Integer.MAX_VALUE - 2;
	protected static int defaultReductionCoefficient= 3;
	//
	///////////////////////////////////////////////////////////////
	//
	public VPMmskSubtractNonparametricBackground(int n, double alpha, int s, int r) {
		requestedNumberOfBins= n;
		alphaLevel= alpha;
		stabilityLevel= s;
		reductionCoefficient= r;
		if (requestedNumberOfBins <= 0) {
			requestedNumberOfBins= defaultNumberOfBins;
		};
		if (requestedNumberOfBins > maximalNumberOfBins) {
			requestedNumberOfBins= maximalNumberOfBins;
		};
		tripleNumberOfBins= requestedNumberOfBins * 3;
		if (alphaLevel <= 0) {
			alphaLevel= defaultAlphaLevel;
		};
		if (stabilityLevel > maximalStabilityLevel) {
			stabilityLevel= maximalStabilityLevel;
		};
		if (reductionCoefficient <= 1) {
			reductionCoefficient= defaultReductionCoefficient;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void execute(VPM vpm) {
		initializeInternalMatricesIfNecessary(vpm);
		int[] operationalMatrix= vpm.getOperationalMatrix();
		boolean[] foregroundMask= vpm.getForegroundMask();
		boolean takeImageIntoAccount= vpm.takeRecentImageIntoAccount();
		int minimalTrainingInterval= vpm.getMinimalTrainingInterval();
		int maximalTrainingInterval= vpm.getMaximalTrainingInterval();
		if (maximalTrainingInterval >= 0) {
			if (maximalTrainingInterval >= minimalTrainingInterval) {
				if (numberOfProcessedFrames > maximalTrainingInterval) {
					takeImageIntoAccount= false;
				}
			} else {
				if (numberOfProcessedFrames > minimalTrainingInterval) {
					takeImageIntoAccount= false;
				}
			}
		};
		for (int k=0; k < vectorLength; k++) {
			if (!foregroundMask[k]) {
				continue;
			};
			int value= operationalMatrix[k];
			int index3= value*tripleNumberOfBins/maximalColor + 1;
			if (index3 > tripleNumberOfBins) {
				index3= tripleNumberOfBins;
			};
			int index0= index3 / 3;
			int mode= index3 - index0 * 3;
			int index1= index0;
			int[] histogram= histograms[k];
			int n= histogram[index1];
			int numberOfIterations= counters[k];
			double p= (double)n / numberOfIterations;
			p= p / 3;
			if (p > alphaLevel) {
				foregroundMask[k]= false;
			};
			if (!takeImageIntoAccount) {
				continue;
			};
			if (stabilityLevel > 0) {
				if (numberOfIterations < stabilityLevel) {
					counters[k]= counters[k] + 1;
				} else {
					int previousQuantity= counters[k];
					int decreasedQuantity= previousQuantity / reductionCoefficient;
					counters[k]= decreasedQuantity + 1;
					for (int m=0; m < requestedNumberOfBins+1; m++) {
						long correctedValue= (long)histogram[m] * decreasedQuantity / previousQuantity;
						histogram[m]= (int)correctedValue;
					}
				}
			} else {
				counters[k]= counters[k] + 1;
			};
			int sum1= 0;
			int sum2= 0;
			switch (mode) {
			case 0:
				sum1= histogram[index1-1] + 1;
				sum2= histogram[index1] + 2;
				histogram[index1-1]= sum1;
				histogram[index1]= sum2;
				break;
			case 1:
				sum1= histogram[index1] + 3;
				histogram[index1]= sum1;
				break;
			case 2:
				sum1= histogram[index1] + 2;
				sum2= histogram[index1+1] + 1;
				histogram[index1]= sum1;
				histogram[index1+1]= sum2;
				break;
			default:
				System.err.printf("Illegal mode: %d\n",mode);
			};
			if (	sum1 >= maximalStabilityLevel ||
				sum2 >= maximalStabilityLevel ||
				counters[k] >= maximalStabilityLevel) {
				int previousQuantity= counters[k];
				int decreasedQuantity= previousQuantity / reductionCoefficient;
				counters[k]= decreasedQuantity;
				for (int m=0; m < requestedNumberOfBins+1; m++) {
					long correctedValue= (long)histogram[m] * decreasedQuantity / previousQuantity;
					histogram[m]= (int)correctedValue;
				}
			}
		};
		if (numberOfProcessedFrames < Long.MAX_VALUE) {
			numberOfProcessedFrames= numberOfProcessedFrames + 1;
		}
	}
	//
	protected void initializeInternalMatricesIfNecessary(VPM vpm) {
		if (counters==null) {
			imageWidth= vpm.getOperationalImageWidth();
			imageHeight= vpm.getOperationalImageHeight();
			vectorLength= imageWidth * imageHeight;
			counters= new int[vectorLength];
			histograms= new int[vectorLength][requestedNumberOfBins+1];
		}
	}
	//
	public void forgetStatistics() {
		counters= null;
		histograms= null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int[] getBackgroundImage(VPM vpm) {
		initializeInternalMatricesIfNecessary(vpm);
		int[] mean= new int[vectorLength];
		for (int k=0; k < vectorLength; k++) {
			int[] histogram= histograms[k];
			int lengthOfHistogram= histogram.length;
			double binWidth= maximalColor / lengthOfHistogram;
			double correction= 0;
			double sum= 0;
			for (int m=0; m < lengthOfHistogram; m++) {
				int value= histogram[m];
				correction+= value;
				sum+= value*(m+0.5)*binWidth;
			};
			if (correction > 0) {
				mean[k]= (int)(sum / correction);
			}
		};
		return mean;
	}
	//
	public int[] getSigmaImage(VPM vpm) {
		initializeInternalMatricesIfNecessary(vpm);
		int[] sigma= new int[vectorLength];
		for (int k=0; k < vectorLength; k++) {
			int[] histogram= histograms[k];
			int lengthOfHistogram= histogram.length;
			double binWidth= maximalColor / lengthOfHistogram;
			double correction1= 0;
			double sum1= 0;
			for (int m=0; m < lengthOfHistogram; m++) {
				int value= histogram[m];
				correction1+= value;
				sum1+= value*(m+0.5)*binWidth;
			};
			double mean= 0;
			if (correction1 > 0) {
				mean= sum1 / correction1;
			};
			double correction2= 0;
			double sum2= 0;
			for (int m=0; m < lengthOfHistogram; m++) {
				int value= histogram[m];
				correction2+= value*value;
				double delta= value*((m+0.5)*binWidth - mean);
				sum2+= delta*delta;
			};
			if (correction2 > 0) {
				sigma[k]= (int)StrictMath.sqrt(sum2 / correction2);
			}
		};
		return sigma;
	}
}
