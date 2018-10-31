// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;

public class VPMmskSubtractGaussianBackground extends VPMmskBackgroundSubtractionCommand {
	//
	protected long numberOfProcessedFrames= 0;
	//
	protected double varianceFactor;
	protected long stabilityLevel;
	protected int reductionCoefficient;
	//
	protected int imageWidth;
	protected int imageHeight;
	protected int vectorLength;
	//
	protected int[] counters;
	protected int[] backgroundSum;
	protected int[] backgroundSumX2;
	//
	// protected static final int maximalColor= 255;
	// protected static final int maximalSum= Integer.MAX_VALUE - 2;
	//
	protected static int maximalStabilityLevel= Integer.MAX_VALUE - maximalColor*maximalColor;
	protected static int defaultReductionCoefficient= 3;
	//
	///////////////////////////////////////////////////////////////
	//
	public VPMmskSubtractGaussianBackground(double factor, int s, int r) {
		varianceFactor= factor;
		stabilityLevel= s;
		reductionCoefficient= r;
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
			int numberOfIterations= counters[k];
			int sum= backgroundSum[k];
			int mean= 0;
			int dispersion= 0;
			if (numberOfIterations > 0) {
				mean= sum / numberOfIterations;
				dispersion= (backgroundSumX2[k] / numberOfIterations) - mean*mean;
				if (dispersion < 0) {
					dispersion= 0;
				}
			};
			int delta1= value - mean;
			int delta2= delta1 * delta1;
			if (delta2 <= dispersion * varianceFactor) {
				foregroundMask[k]= false;
			};
			if (!takeImageIntoAccount) {
				continue;
			};
			if (stabilityLevel > 0) {
				if (numberOfIterations < stabilityLevel) {
					counters[k]= counters[k] + 1;
					backgroundSum[k]= backgroundSum[k] + value;
					backgroundSumX2[k]= backgroundSumX2[k] + value*value;
				} else {
					int previousQuantity= counters[k];
					int decreasedQuantity= previousQuantity / reductionCoefficient;
					counters[k]= decreasedQuantity + 1;
					long correctedSum= (long)backgroundSum[k] * decreasedQuantity / previousQuantity;
					long correctedSumX2= (long)backgroundSumX2[k] * decreasedQuantity / previousQuantity;
					backgroundSum[k]= (int)correctedSum + value;
					backgroundSumX2[k]= (int)correctedSumX2 + value*value;
				}
			} else {
				counters[k]= counters[k] + 1;
				backgroundSum[k]= backgroundSum[k] + value;
				backgroundSumX2[k]= backgroundSumX2[k] + value*value;
			};
			if (	backgroundSumX2[k] >= maximalStabilityLevel ||
				backgroundSum[k] >= maximalStabilityLevel ||
				counters[k] >= maximalStabilityLevel) {
				int previousQuantity= counters[k];
				int decreasedQuantity= previousQuantity / reductionCoefficient;
				counters[k]= decreasedQuantity;
				long correctedSum= (long)backgroundSum[k] * decreasedQuantity / previousQuantity;
				long correctedSumX2= (long)backgroundSumX2[k] * decreasedQuantity / previousQuantity;
				backgroundSum[k]= (int)correctedSum;
				backgroundSumX2[k]= (int)correctedSumX2;
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
			backgroundSum= new int[vectorLength];
			backgroundSumX2= new int[vectorLength];
		}
	}
	//
	public void forgetStatistics() {
		counters= null;
		backgroundSum= null;
		backgroundSumX2= null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int[] getBackgroundImage(VPM vpm) {
		initializeInternalMatricesIfNecessary(vpm);
		int[] mean= new int[vectorLength];
		for (int k=0; k < vectorLength; k++) {
			int numberOfIterations= counters[k];
			int sum= backgroundSum[k];
			if (numberOfIterations > 0) {
				mean[k]= sum / numberOfIterations;
			}
		};
		return mean;
	}
	//
	public int[] getSigmaImage(VPM vpm) {
		initializeInternalMatricesIfNecessary(vpm);
		int[] sigma= new int[vectorLength];
		for (int k=0; k < vectorLength; k++) {
			int numberOfIterations= counters[k];
			int sum= backgroundSum[k];
			int sumX2= backgroundSumX2[k];
			if (numberOfIterations > 0) {
				double mean= sum / numberOfIterations;
				double dispersion= (sumX2 / numberOfIterations) - mean*mean;
				if (dispersion < 0) {
					dispersion= 0;
				};
				sigma[k]= (int)StrictMath.sqrt(dispersion);
			}
		};
		return sigma;
	}
}
