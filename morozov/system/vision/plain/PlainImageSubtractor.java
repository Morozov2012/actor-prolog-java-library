// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.plain;

import target.*;

import morozov.system.*;
import morozov.system.gui.*;
import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.blb.*;
import morozov.system.vision.vpm.converters.*;
import morozov.terms.*;

import java.awt.image.Raster;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.Graphics2D;

public class PlainImageSubtractor extends GenericVideoProcessingMachine implements BlobHolder {
	//
	protected boolean extractBlobs= false;
	protected boolean trackBlobs= false;
	protected boolean useGrayscaleColors= true;
	protected boolean useGaussianFiltering= true;
	protected int gaussianFilterRadius= 1;
	protected boolean useRank_2D_Filtering= true;
	protected int rankFilterThreshold= 3;
	protected double backgroundVarianceFactor= 4.0;
	protected boolean contourForeground= false;
	protected boolean refuseSlowTracks= false;
	protected double velocityThreshold= 0.4;
	protected double distanceThreshold= 40.0;
	protected double fuzzyThresholdBorder= 0.20;
	//
	protected VPMblbTrackBlobs blobTracker= new VPMblbTrackBlobs();
	//
	protected int numberOfBands;
	protected int backgroundN= -1;
	protected int maximalN= 30000;
	protected int[][] backgroundSum;
	protected int[][] backgroundSumX2;
	protected boolean[][] extraForegroundMasks;
	//
	protected static final BlobType universalBlobType= new BlobType(SymbolCodes.symbolCode_E_,false);
	//
	protected static final int overcrowdedMatrixReductionCoefficient= 3;
	//
	protected static float[] gaussianMatrix;
	//
	///////////////////////////////////////////////////////////////
	//
	public PlainImageSubtractor(
			boolean extractBlobsFlag,
			boolean trackBlobsFlag,
			int minimalTrainingIntervalValue,
			int maximalTrainingIntervalValue,
			boolean useGrayscaleColorsFlag,
			boolean useGaussianFilter,
			int radiusOfGaussianFilter,
			boolean useRank_2D_FilteringFlag,
			int thresholdOfRankFilter,
			double backgroundStandardDeviationFactorValue,
			boolean contourForegroundFlag,
			int horizontalBlobBorderValue,
			int verticalBlobBorderValue,
			double horizontalExtraBorderCoefficientValue,
			double verticalExtraBorderCoefficientValue,
			int minimalBlobIntersectionAreaValue,
			int minimalBlobSizeValue,
			int minimalTrackDurationValue,
			int maximalTrackDurationValue,
			NumericalValue maximalChronicleLengthValue,
			int maximalBlobInvisibilityIntervalValue,
			int maximalTrackRetentionIntervalValue,
			double[][] iMatrix,
			double rate,
			int r2WindowHalfwidthValue,
			boolean applyFilterToCharacteristicLength,
			int characteristicLengthFilterHalfwidth,
			boolean applyFilterToVelocity,
			int velocityFilterHalfwidth,
			boolean refuseTracks,
			double velocityThresholdValue,
			double distanceThresholdValue,
			double borderOfFuzzyThreshold,
			int synthesizedImageTransparencyValue,
			boolean makeRectangularBlobsInSynthesizedImageValue) {
		super(	minimalTrainingIntervalValue,
			maximalTrainingIntervalValue,
			horizontalBlobBorderValue,
			verticalBlobBorderValue,
			horizontalExtraBorderCoefficientValue,
			verticalExtraBorderCoefficientValue,
			minimalBlobIntersectionAreaValue,
			minimalBlobSizeValue,
			minimalTrackDurationValue,
			maximalTrackDurationValue,
			maximalChronicleLengthValue,
			maximalBlobInvisibilityIntervalValue,
			maximalTrackRetentionIntervalValue,
			iMatrix,
			rate,
			r2WindowHalfwidthValue,
			applyFilterToCharacteristicLength,
			characteristicLengthFilterHalfwidth,
			applyFilterToVelocity,
			velocityFilterHalfwidth,
			synthesizedImageTransparencyValue,
			makeRectangularBlobsInSynthesizedImageValue);
		extractBlobs= extractBlobsFlag;
		trackBlobs= trackBlobsFlag;
		useGrayscaleColors= useGrayscaleColorsFlag;
		useGaussianFiltering= useGaussianFilter;
		gaussianFilterRadius= radiusOfGaussianFilter;
		useRank_2D_Filtering= useRank_2D_FilteringFlag;
		rankFilterThreshold= thresholdOfRankFilter;
		backgroundVarianceFactor= backgroundStandardDeviationFactorValue*backgroundStandardDeviationFactorValue;
		contourForeground= contourForegroundFlag;
		refuseSlowTracks= refuseTracks;
		velocityThreshold= velocityThresholdValue;
		distanceThreshold= distanceThresholdValue;
		fuzzyThresholdBorder= borderOfFuzzyThreshold;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public void processImage(java.awt.image.BufferedImage image, long frameNumber, long timeInMilliseconds, boolean takeImageIntoAccount) {
		if (frameNumber <= recentFrameNumber) {
			forgetResults();
		};
		recentFrameNumber= frameNumber;
		recentTimeInMilliseconds= timeInMilliseconds;
		recentImage= image;
		int minimalTrainingInterval= getMinimalTrainingInterval();
		int maximalTrainingInterval= getMaximalTrainingInterval();
		if (maximalTrainingInterval >= 0) {
			if (maximalTrainingInterval >= minimalTrainingInterval) {
				if (backgroundN > maximalTrainingInterval) {
					takeImageIntoAccount= false;
				}
			} else {
				if (backgroundN > minimalTrainingInterval) {
					takeImageIntoAccount= false;
				}
			}
		};
		subtractImage(image,takeImageIntoAccount);
		if (backgroundN >= minimalTrainingInterval) {
			if (extractBlobs) {
				int[][] blobRectangles= VPMblbExtractBlobs.multipassBlobExtraction(
					foregroundMask,
					operationalImageWidth,
					operationalImageHeight,
					getHorizontalBlobBorder(),
					getVerticalBlobBorder(),
					getHorizontalExtraBorderCoefficient(),
					getVerticalExtraBorderCoefficient(),
					getMinimalBlobIntersectionArea(),
					getMinimalBlobSize());
				currentBlobAttributes= VPMblbExtractBlobs.createBlobAttributeArray(
					universalBlobType,
					recentFrameNumber,
					recentTimeInMilliseconds,
					operationalImageWidth,
					operationalImageHeight,
					foregroundMask,
					blobRectangles);
				currentBlobTypes= null;
				if (trackBlobs) {
					blobTracker.trackBlobs(this);
				}
			}
		};
		shortenArraysIfNecessary(timeInMilliseconds,getMaximalChronicleLength());
		arrayOfTime.add(new VPM_FrameNumberAndTime(frameNumber,timeInMilliseconds));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public void commit() {
		if (backgroundN <= 0) {
			return;
		};
		if (frameNumberIsIllegal(recentFrameNumber)){
			return;
		};
		ImageSubtractorSnapshot blobs= new ImageSubtractorSnapshot(
			recentFrameNumber,
			recentTimeInMilliseconds,
			operationalImageWidth,
			operationalImageHeight,
			recentImage,
			preprocessedImage,
			numberOfBands,
			backgroundSum,
			backgroundSumX2,
			backgroundN,
			foregroundMask,
			arrayOfTime,
			currentBlobAttributes,
			tracks,
			refuseSlowTracks,
			velocityThreshold,
			distanceThreshold,
			fuzzyThresholdBorder,
			getSynthesizedImageTransparency(),
			getSynthesizedImageRectangularBlobsMode()
			);
		recentSnapshot.set(blobs);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public void reset(
			boolean forgetSettings,
			boolean forgetStatistics,
			boolean forgetResults,
			boolean extractBlobsFlag,
			boolean trackBlobsFlag,
			int minimalTrainingIntervalValue,
			int maximalTrainingIntervalValue,
			boolean useGrayscaleColorsFlag,
			boolean useGaussianFilter,
			int radiusOfGaussianFilter,
			boolean useRank_2D_FilteringFlag,
			int thresholdOfRankFilter,
			double backgroundStandardDeviationFactorValue,
			boolean contourForegroundFlag,
			int horizontalBlobBorderValue,
			int verticalBlobBorderValue,
			double horizontalExtraBorderCoefficientValue,
			double verticalExtraBorderCoefficientValue,
			int minimalBlobIntersectionAreaValue,
			int minimalBlobSizeValue,
			int minimalTrackDurationValue,
			int maximalTrackDurationValue,
			NumericalValue maximalChronicleLengthValue,
			int maximalBlobInvisibilityIntervalValue,
			int maximalTrackRetentionIntervalValue,
			double[][] iMatrix,
			double rate,
			int r2WindowHalfwidthValue,
			boolean applyFilterToCharacteristicLength,
			int characteristicLengthFilterHalfwidth,
			boolean applyFilterToVelocity,
			int velocityFilterHalfwidth,
			boolean refuseTracks,
			double velocityThresholdValue,
			double distanceThresholdValue,
			double borderOfFuzzyThreshold,
			int synthesizedImageTransparencyValue,
			boolean makeRectangularBlobsInSynthesizedImageValue) {
		super.reset(
			forgetSettings,
			forgetStatistics,
			forgetResults,
			minimalTrainingIntervalValue,
			maximalTrainingIntervalValue,
			horizontalBlobBorderValue,
			verticalBlobBorderValue,
			horizontalExtraBorderCoefficientValue,
			verticalExtraBorderCoefficientValue,
			minimalBlobIntersectionAreaValue,
			minimalBlobSizeValue,
			minimalTrackDurationValue,
			maximalTrackDurationValue,
			maximalChronicleLengthValue,
			maximalBlobInvisibilityIntervalValue,
			maximalTrackRetentionIntervalValue,
			iMatrix,
			rate,
			r2WindowHalfwidthValue,
			applyFilterToCharacteristicLength,
			characteristicLengthFilterHalfwidth,
			applyFilterToVelocity,
			velocityFilterHalfwidth,
			synthesizedImageTransparencyValue,
			makeRectangularBlobsInSynthesizedImageValue);
		if (forgetSettings) {
			forgetStatistics= true;
		};
		if (forgetSettings) {
			extractBlobs= extractBlobsFlag;
			trackBlobs= trackBlobsFlag;
			useGrayscaleColors= useGrayscaleColorsFlag;
			useGaussianFiltering= useGaussianFilter;
			gaussianFilterRadius= radiusOfGaussianFilter;
			useRank_2D_Filtering= useRank_2D_FilteringFlag;
			rankFilterThreshold= thresholdOfRankFilter;
			backgroundVarianceFactor= backgroundStandardDeviationFactorValue*backgroundStandardDeviationFactorValue;
			contourForeground= contourForegroundFlag;
			refuseSlowTracks= refuseTracks;
			velocityThreshold= velocityThresholdValue;
			distanceThreshold= distanceThresholdValue;
			fuzzyThresholdBorder= borderOfFuzzyThreshold;
			gaussianMatrix= null;
		}
	}
	protected void forgetStatistics() {
		blobTracker.forgetStatistics();
		recentImage= null;
		preprocessedImage= null;
		backgroundN= -1;
		backgroundSum= null;
		backgroundSumX2= null;
		foregroundMask= null;
		extraForegroundMasks= null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public void setBlobExtractionMode(boolean mode) {
		extractBlobs= mode;
	}
	public boolean getBlobExtractionMode() {
		return extractBlobs;
	}
	//
	synchronized public void setBlobTracingMode(boolean mode) {
		trackBlobs= mode;
	}
	public boolean getBlobTracingMode() {
		return trackBlobs;
	}
	//
	synchronized public void setGrayscaleMode(boolean mode) {
		if (useGrayscaleColors != mode) {
			useGrayscaleColors= mode;
			forgetStatistics();
		}
	}
	public boolean getGrayscaleMode() {
		return useGrayscaleColors;
	}
	//
	synchronized public void setBackgroundGaussianFilteringMode(boolean mode) {
		if (useGaussianFiltering != mode) {
			useGaussianFiltering= mode;
			forgetStatistics();
		}
	}
	public boolean getBackgroundGaussianFilteringMode() {
		return useGaussianFiltering;
	}
	//
	synchronized public void setBackgroundGaussianFilterRadius(int value) {
		if (gaussianFilterRadius != value) {
			gaussianFilterRadius= value;
			gaussianMatrix= null;
		}
	}
	public int getBackgroundGaussianFilterRadius() {
		return gaussianFilterRadius;
	}
	//
	synchronized public void setBackgroundRankFilteringMode(boolean mode) {
		if (useRank_2D_Filtering != mode) {
			useRank_2D_Filtering= mode;
			forgetStatistics();
		}
	}
	public boolean getBackgroundRankFilteringMode() {
		return useRank_2D_Filtering;
	}
	//
	synchronized public void setBackgroundRankFilterThreshold(int value) {
		rankFilterThreshold= value;
	}
	public int getBackgroundRankFilterThreshold() {
		return rankFilterThreshold;
	}
	//
	synchronized public void setBackgroundStandardDeviationFactor(double value) {
		backgroundVarianceFactor= value*value;
	}
	public double getBackgroundStandardDeviationFactor() {
		return StrictMath.sqrt(backgroundVarianceFactor);
	}
	//
	synchronized public void setForegroundContouringMode(boolean mode) {
		if (contourForeground != mode) {
			contourForeground= mode;
		}
	}
	public boolean getForegroundContouringMode() {
		return contourForeground;
	}
	//
	synchronized public void setSlowTracksDeletionMode(boolean mode) {
		refuseSlowTracks= mode;
		ImageSubtractorSnapshot blobs= (ImageSubtractorSnapshot)recentSnapshot.get();
		if (blobs != null) {
			blobs.setSlowTracksDeletionMode(mode);
		}
	}
	public boolean getSlowTracksDeletionMode() {
		return refuseSlowTracks;
	}
	//
	synchronized public void setFuzzyVelocityThreshold(double value) {
		velocityThreshold= value;
		ImageSubtractorSnapshot blobs= (ImageSubtractorSnapshot)recentSnapshot.get();
		if (blobs != null) {
			blobs.setFuzzyVelocityThreshold(value);
		}
	}
	public double getFuzzyVelocityThreshold() {
		return velocityThreshold;
	}
	//
	synchronized public void setFuzzyDistanceThreshold(double value) {
		distanceThreshold= value;
		ImageSubtractorSnapshot blobs= (ImageSubtractorSnapshot)recentSnapshot.get();
		if (blobs != null) {
			blobs.setFuzzyDistanceThreshold(value);
		}
	}
	public double getFuzzyDistanceThreshold() {
		return distanceThreshold;
	}
	//
	synchronized public void setFuzzyThresholdBorder(double value) {
		fuzzyThresholdBorder= value;
		ImageSubtractorSnapshot blobs= (ImageSubtractorSnapshot)recentSnapshot.get();
		if (blobs != null) {
			blobs.setFuzzyThresholdBorder(value);
		}
	}
	public double getFuzzyThresholdBorder() {
		return fuzzyThresholdBorder;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public java.awt.image.BufferedImage getCommittedBackgroundImage() {
		ImageSubtractorSnapshot snapshot= (ImageSubtractorSnapshot)recentSnapshot.get();
		if (snapshot != null) {
			return snapshot.getBackgroundImage();
		} else {
			return null;
		}
	}
	public java.awt.image.BufferedImage getCommittedSigmaImage() {
		ImageSubtractorSnapshot snapshot= (ImageSubtractorSnapshot)recentSnapshot.get();
		if (snapshot != null) {
			return snapshot.getSigmaImage();
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void createBlobAttributesIfNecessary() {
		if (currentBlobTypes==null) {
			if (currentBlobAttributes!=null) {
				int numberOfBlobs= currentBlobAttributes.length;
				currentBlobTypes= new BlobType[numberOfBlobs];
				for (int k=0; k < numberOfBlobs; k++) {
					currentBlobTypes[k]= currentBlobAttributes[k].getType();
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	// SUBTRACT IMAGE                                            //
	///////////////////////////////////////////////////////////////
	//
	public void subtractImage(java.awt.image.BufferedImage image, boolean takeImageIntoAccount) {
		if (useGrayscaleColors) {
			preprocessedImage= filterAndConvertToGray(image);
		} else {
			preprocessedImage= filterAndConvertToBGR(image);
		};
		if (backgroundN < 0) {
			operationalImageWidth= preprocessedImage.getWidth();
			operationalImageHeight= preprocessedImage.getHeight();
			operationalVectorLength= operationalImageWidth * operationalImageHeight;
			if (useGrayscaleColors) {
				numberOfBands= 1;
			} else {
				numberOfBands= 3;
			};
			backgroundN= 0;
			backgroundSum= new int[numberOfBands][operationalVectorLength];
			backgroundSumX2= new int[numberOfBands][operationalVectorLength];
			foregroundMask= new boolean[operationalVectorLength];
			if (numberOfBands > 1) {
				extraForegroundMasks= new boolean[numberOfBands][operationalVectorLength];
			} else {
				extraForegroundMasks= null;
			}
		};
		Raster raster= preprocessedImage.getData();
		int[][] imagePixels= new int[numberOfBands][operationalVectorLength];
		for (int k=0; k < numberOfBands; k++) {
			raster.getSamples(0,0,operationalImageWidth,operationalImageHeight,k,imagePixels[k]);
		};
		for (int n=0; n < operationalVectorLength; n++) {
			foregroundMask[n]= false;
			if (numberOfBands > 1) {
				for (int k=0; k < numberOfBands; k++) {
					extraForegroundMasks[k][n]= false;
				}
			}
		};
		for (int k=0; k < numberOfBands; k++) {
			for (int n=0; n < operationalVectorLength; n++) {
				int sum= backgroundSum[k][n];
				int mean= 0;
				int dispersion= 0;
				if (backgroundN > 0) {
					mean= sum / backgroundN;
					dispersion= (backgroundSumX2[k][n] / backgroundN) - mean*mean;
				};
				int value= imagePixels[k][n];
				int delta1= value - mean;
				int delta2= delta1 * delta1;
				// if (delta2 < dispersion) {	// BUG#2
				if (delta2 <= dispersion * backgroundVarianceFactor) {
					if (numberOfBands > 1) {
						extraForegroundMasks[k][n]= false;
					} else {
						foregroundMask[n]= false;
					}
				} else {
					if (numberOfBands > 1) {
						extraForegroundMasks[k][n]= true;
					} else {
						foregroundMask[n]= true;
					}
				}
			}
		};
		if (useRank_2D_Filtering) {
			if (numberOfBands > 1) {
				for (int k=0; k < numberOfBands; k++) {
					VisionUtils.rankFilter2D(extraForegroundMasks[k],operationalImageWidth,operationalImageHeight,rankFilterThreshold,false);
				}
			} else {
				VisionUtils.rankFilter2D(foregroundMask,operationalImageWidth,operationalImageHeight,rankFilterThreshold,false);
			}
		};
		if (numberOfBands > 1) {
			for (int n=0; n < operationalVectorLength; n++) {
				boolean value= false;
				for (int k=0; k < numberOfBands; k++) {
					if (extraForegroundMasks[k][n]) {
						value= true;
						break;
					}
				};
				foregroundMask[n]= value;
			};
			if (useRank_2D_Filtering) {
				VisionUtils.rankFilter2D(foregroundMask,operationalImageWidth,operationalImageHeight,rankFilterThreshold,false);
			}
		};
		if (getForegroundContouringMode()) {
			foregroundMask= VisionUtils.contourForeground(foregroundMask,operationalImageWidth,operationalImageHeight);
		};
		if (takeImageIntoAccount) {
			if (backgroundN >= maximalN) {
				backgroundN= backgroundN / overcrowdedMatrixReductionCoefficient;
				for (int k=0; k < numberOfBands; k++) {
					for (int n=0; n < operationalVectorLength; n++) {
						backgroundSum[k][n]= backgroundSum[k][n] / overcrowdedMatrixReductionCoefficient;
						backgroundSumX2[k][n]= backgroundSumX2[k][n] / overcrowdedMatrixReductionCoefficient;
					}
				}
			};
			backgroundN= backgroundN + 1;
			for (int k=0; k < numberOfBands; k++) {
				for (int n=0; n < operationalVectorLength; n++) {
					int value= imagePixels[k][n];
					backgroundSum[k][n]= backgroundSum[k][n] + value;
					backgroundSumX2[k][n]= backgroundSumX2[k][n] + value*value;
				}
			}
		}
	}
	public java.awt.image.BufferedImage filterAndConvertToGray(java.awt.image.BufferedImage a) {
		java.awt.image.BufferedImage b= new java.awt.image.BufferedImage(a.getWidth(),a.getHeight(),java.awt.image.BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2= DesktopUtils.safelyGetGraphics2D(b);
		try {
			if (useGaussianFiltering) {
				if (gaussianMatrix==null) {
					gaussianMatrix= VisionUtils.gaussianMatrix(gaussianFilterRadius);
				};
				int length= PrologInteger.toInteger(StrictMath.sqrt(gaussianMatrix.length));
				ConvolveOp cop= new ConvolveOp(
					new Kernel(length,length,gaussianMatrix),
					ConvolveOp.EDGE_ZERO_FILL,
					null);
				g2.drawImage(a,cop,0,0);
			} else {
				g2.drawImage(a,0,0,null);
			}
		} finally {
			g2.dispose();
		};
		return b;
	}
	public java.awt.image.BufferedImage filterAndConvertToBGR(java.awt.image.BufferedImage a) {
		java.awt.image.BufferedImage b= new java.awt.image.BufferedImage(a.getWidth(),a.getHeight(),java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2= DesktopUtils.safelyGetGraphics2D(b);
		try {
			if (useGaussianFiltering) {
				if (gaussianMatrix==null) {
					gaussianMatrix= VisionUtils.gaussianMatrix(gaussianFilterRadius);
				};
				int length= PrologInteger.toInteger(StrictMath.sqrt(gaussianMatrix.length));
				ConvolveOp cop= new ConvolveOp(
					new Kernel(length,length,gaussianMatrix),
					ConvolveOp.EDGE_ZERO_FILL,
					null);
				g2.drawImage(a,cop,0,0);
			} else {
				g2.drawImage(a,0,0,null);
			}
		} finally {
			g2.dispose();
		};
		return b;
	}
}
