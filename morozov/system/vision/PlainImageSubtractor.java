// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import morozov.system.vision.errors.*;
import morozov.terms.*;

import java.awt.image.WritableRaster;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.Graphics2D;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import java.math.BigInteger;

public class PlainImageSubtractor {
	//
	protected boolean extractBlobs= false;
	protected boolean trackBlobs= false;
	protected int minimalTrainingInterval= 7;
	protected boolean useGrayscaleColors= true;
	protected boolean useGaussianFiltering= true;
	protected int gaussianFilterRadius= 1;
	protected boolean useMedianFiltering= true;
	protected int medianFilterThreshold= 3;
	protected double backgroundDispersionFactor= 2.0;
	protected int horizontalBlobBorder= 3;
	protected int verticalBlobBorder= 3;
	protected int minimalBlobIntersectionArea= 1;
	protected int minimalBlobSize= 10;
	protected int minimalTrackDuration= 30;
	protected int maximalBlobInvisibilityInterval= 3;
	protected int maximalTrackRetentionInterval= 10000;
	protected double[][] inverseMatrix= null;
	protected double samplingRate= -1;
	protected boolean applyMedianFiltering= true;
	protected int medianFilterHalfwidth= 5;
	protected boolean refuseSlowTracks= false;
	protected double velocityThreshold= 0.4;
	protected double distanceThreshold= 40.0;
	protected double fuzzyThresholdBorder= 0.20;
	protected int synthesizedImageTransparency= 64;
	//
	protected int maximalRarefactionOfObject= -1; // 9;
	protected boolean makeSquareBlobsInSynthesizedImage= true;
	//
	protected long time;
	protected String title;
	//
	protected int[] currentBlobSize;
	protected int[] currentTrackDurations;
	protected int[][] currentBlobRectangles;
	protected BigInteger[] currentBlobIdentifiers;
	//
	protected int[] previousBlobSize;
	protected int[] previousTrackDurations;
	protected int[][] previousBlobRectangles;
	protected BigInteger[] previousBlobIdentifiers;
	//
	protected int[] invisibleBlobSize;
	protected int[] invisibleTrackDurations;
	protected int[][] invisibleBlobRectangles;
	protected BigInteger[] invisibleBlobIdentifiers;
	protected int[] invisibleBlobDelays;
	//
	protected java.awt.image.BufferedImage recentImage;
	protected int imageWidth;
	protected int imageHeight;
	protected int vectorLength;
	protected int numberOfBands;
	protected int numberOfExtraBands;
	protected int backgroundN= -1;
	protected int maximalN= 30000;
	protected int[][] backgroundSum;
	protected int[][] backgroundSumX2;
	protected int[][] deltaPixels;
	//
	protected BigInteger recentBlobIdentifier= BigInteger.ZERO;
	public HashMap<BigInteger,GrowingTrack> tracks= new HashMap<>();
	protected AtomicReference<BlobSet> recentBlobSet= new AtomicReference<BlobSet>();
	//
	protected static final int noDifferenceMarker= 0; // ��� ������ ���� �஧���
	protected static final int differenceMarker= 255;
	//
	protected static final int overcrowdedMatrixReductionCoefficient= 3;
	//
	// protected static final float[] gaussWindow= {0.367879f, 0.444858f, 0.527292f, 0.612626f, 0.697676f, 0.778801f, 0.852144f, 0.913931f, 0.960789f, 0.990050f, 1.000000f, 0.990050f, 0.960789f, 0.913931f, 0.852144f, 0.778801f, 0.697676f, 0.612626f, 0.527292f, 0.444858f, 0.367879f};
	// protected static final float[] gaussWindow11= {0.367879f, 0.527292f, 0.697676f, 0.852144f, 0.960789f, 1.000000f, 0.960789f, 0.852144f, 0.697676f, 0.527292f, 0.367879f};
	// protected static final float[] gaussWindow7= {0.367879f, 0.641180f, 0.894839f, 1.000000f, 0.894839f, 0.641180f, 0.367879f};
	// protected static final float[] gaussWindow5= {0.367879f, 0.778801f, 1.000000f, 0.778801f, 0.367879f};
	// protected static final float[] gaussWindow3= {0.367879f, 1.000000f, 0.367879f};
	protected static float[] gaussianMatrix;
	//
	public PlainImageSubtractor(
			boolean extractBlobsFlag,
			boolean trackBlobsFlag,
			int trainingIntervalValue,
			boolean useGrayscaleColorsFlag,
			boolean useGaussianFilter,
			int radiusOfGaussianFilter,
			boolean useMedianFilter,
			int thresholdOfMedianFilter,
			double backgroundDispersionFactorValue,
			int horizontalBlobBorderValue,
			int verticalBlobBorderValue,
			int minimalBlobIntersectionAreaValue,
			int minimalBlobSizeValue,
			int minimalTrackDurationValue,
			int maximalBlobInvisibilityIntervalValue,
			int maximalTrackRetentionIntervalValue,
			double[][] iMatrix,
			double rate,
			boolean applyFilterToVelocity,
			int filterHalfwidth,
			boolean refuseTracks,
			double velocityThresholdValue,
			double distanceThresholdValue,
			double borderOfFuzzyThreshold,
			int synthesizedImageTransparencyValue,
			boolean makeSquareBlobsInSynthesizedImageValue) {
		extractBlobs= extractBlobsFlag;
		trackBlobs= trackBlobsFlag;
		minimalTrainingInterval= trainingIntervalValue;
		useGrayscaleColors= useGrayscaleColorsFlag;
		useGaussianFiltering= useGaussianFilter;
		gaussianFilterRadius= radiusOfGaussianFilter;
		useMedianFiltering= useMedianFilter;
		medianFilterThreshold= thresholdOfMedianFilter;
		backgroundDispersionFactor= backgroundDispersionFactorValue;
		horizontalBlobBorder= horizontalBlobBorderValue;
		verticalBlobBorder= verticalBlobBorderValue;
		minimalBlobIntersectionArea= minimalBlobIntersectionAreaValue;
		minimalBlobSize= minimalBlobSizeValue;
		minimalTrackDuration= minimalTrackDurationValue;
		maximalBlobInvisibilityInterval= maximalBlobInvisibilityIntervalValue;
		maximalTrackRetentionInterval= maximalTrackRetentionIntervalValue;
		inverseMatrix= iMatrix;
		samplingRate= rate;
		applyMedianFiltering= applyFilterToVelocity;
		medianFilterHalfwidth= filterHalfwidth;
		refuseSlowTracks= refuseTracks;
		velocityThreshold= velocityThresholdValue;
		distanceThreshold= distanceThresholdValue;
		fuzzyThresholdBorder= borderOfFuzzyThreshold;
		synthesizedImageTransparency= synthesizedImageTransparencyValue;
		makeSquareBlobsInSynthesizedImage= makeSquareBlobsInSynthesizedImageValue;
	}
	//
	synchronized public void subtractImageAndAnalyseBlobs(long t, java.awt.image.BufferedImage image, boolean takeImageIntoAccount) {
		time= t;
		if (trackBlobs) {
			previousBlobSize= currentBlobSize;
			previousTrackDurations= currentTrackDurations;
			previousBlobRectangles= currentBlobRectangles;
			previousBlobIdentifiers= currentBlobIdentifiers;
		} else {
			previousBlobSize= null;
			previousTrackDurations= null;
			previousBlobRectangles= null;
			previousBlobIdentifiers= null;
			invisibleBlobSize= new int[0];
			invisibleTrackDurations= new int[0];
			invisibleBlobRectangles= new int[0][4];
			invisibleBlobIdentifiers= new BigInteger[0];
			invisibleBlobDelays= new int[0];
		};
		recentImage= image;
		subtractImage(image,takeImageIntoAccount);
		if (backgroundN >= minimalTrainingInterval) {
			if (extractBlobs) {
				extractBlobs();
			};
			if (trackBlobs) {
				identifyBlobs();
			}
		}
	}
	//
	synchronized public void commit() {
		if (backgroundN <= 0 || currentBlobSize==null) {
			return;
		};
		java.awt.image.BufferedImage extractedImage= new java.awt.image.BufferedImage(imageWidth,imageHeight,java.awt.image.BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2= (Graphics2D)extractedImage.getGraphics();
		g2.drawImage(recentImage,0,0,null);
		WritableRaster imageRaster= extractedImage.getRaster();
		imageRaster.setSamples(0,0,imageWidth,imageHeight,3,deltaPixels[0]);
		BlobSet blobs= new BlobSet(
			time,
			imageWidth,
			imageHeight,
			recentImage,
			extractedImage,
			numberOfBands,
			numberOfExtraBands,
			backgroundSum,
			backgroundSumX2,
			backgroundN,
			deltaPixels,
			differenceMarker,
			noDifferenceMarker,
			currentBlobSize,
			currentBlobRectangles,
			currentBlobIdentifiers,
			currentTrackDurations,
			tracks,
			minimalTrackDuration,
			// inverseMatrix,
			// samplingRate,
			// applyMedianFiltering,
			// medianFilterHalfwidth,
			refuseSlowTracks,
			velocityThreshold,
			distanceThreshold,
			fuzzyThresholdBorder,
			makeSquareBlobsInSynthesizedImage,
			synthesizedImageTransparency
			);
		blobs.setTitle(title);
		recentBlobSet.set(blobs);
	}
	synchronized public void reset(
			boolean forgetSettings,
			boolean forgetStatistics,
			boolean forgetResults,
			boolean extractBlobsFlag,
			boolean trackBlobsFlag,
			int trainingIntervalValue,
			boolean useGrayscaleColorsFlag,
			boolean useGaussianFilter,
			int radiusOfGaussianFilter,
			boolean useMedianFilter,
			int thresholdOfMedianFilter,
			double backgroundDispersionFactorValue,
			int horizontalBlobBorderValue,
			int verticalBlobBorderValue,
			int minimalBlobIntersectionAreaValue,
			int minimalBlobSizeValue,
			int minimalTrackDurationValue,
			int maximalBlobInvisibilityIntervalValue,
			int maximalTrackRetentionIntervalValue,
			double[][] iMatrix,
			double rate,
			boolean applyFilterToVelocity,
			int filterHalfwidth,
			boolean refuseTracks,
			double velocityThresholdValue,
			double distanceThresholdValue,
			double borderOfFuzzyThreshold,
			int synthesizedImageTransparencyValue,
			boolean makeSquareBlobsInSynthesizedImageValue) {
		//
		if (forgetSettings) {
			forgetStatistics= true;
		};
		//
		if (forgetSettings) {
			extractBlobs= extractBlobsFlag;
			trackBlobs= trackBlobsFlag;
			minimalTrainingInterval= trainingIntervalValue;
			useGrayscaleColors= useGrayscaleColorsFlag;
			useGaussianFiltering= useGaussianFilter;
			gaussianFilterRadius= radiusOfGaussianFilter;
			useMedianFiltering= useMedianFilter;
			medianFilterThreshold= thresholdOfMedianFilter;
			backgroundDispersionFactor= backgroundDispersionFactorValue;
			horizontalBlobBorder= horizontalBlobBorderValue;
			verticalBlobBorder= verticalBlobBorderValue;
			minimalBlobIntersectionArea= minimalBlobIntersectionAreaValue;
			minimalBlobSize= minimalBlobSizeValue;
			minimalTrackDuration= minimalTrackDurationValue;
			maximalBlobInvisibilityInterval= maximalBlobInvisibilityIntervalValue;
			maximalTrackRetentionInterval= maximalTrackRetentionIntervalValue;
			inverseMatrix= iMatrix;
			samplingRate= rate;
			applyMedianFiltering= applyFilterToVelocity;
			medianFilterHalfwidth= filterHalfwidth;
			refuseSlowTracks= refuseTracks;
			velocityThreshold= velocityThresholdValue;
			distanceThreshold= distanceThresholdValue;
			fuzzyThresholdBorder= borderOfFuzzyThreshold;
			synthesizedImageTransparency= synthesizedImageTransparencyValue;
			makeSquareBlobsInSynthesizedImage= makeSquareBlobsInSynthesizedImageValue;
			title= "";
			gaussianMatrix= null;
		};
		//
		if (forgetStatistics) {
			forgetStatistics();
		};
		//
		if (forgetResults) {
			currentBlobSize= null;
			currentTrackDurations= null;
			currentBlobRectangles= null;
			currentBlobIdentifiers= null;
			previousBlobSize= null;
			previousTrackDurations= null;
			previousBlobRectangles= null;
			previousBlobIdentifiers= null;
			invisibleBlobSize= new int[0];
			invisibleTrackDurations= new int[0];
			invisibleBlobRectangles= new int[0][4];
			invisibleBlobIdentifiers= new BigInteger[0];
			invisibleBlobDelays= new int[0];
			recentBlobIdentifier= BigInteger.ZERO;
			tracks.clear();
			time= 0;
		}
	}
	protected void forgetStatistics() {
		recentImage= null;
		backgroundN= -1;
		backgroundSum= null;
		backgroundSumX2= null;
		deltaPixels= null;
	}
	//
	public long getFrameNumber() {
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			return blobs.getRecentFrameNumber();
		} else {
			return -1;
		}
	}
	//
	public java.awt.image.BufferedImage get_recent_image() {
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			return blobs.getRecentImage();
		} else {
			return null;
		}
	}
	public java.awt.image.BufferedImage get_background_image() {
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			return blobs.getBackgroundImage();
		} else {
			return null;
		}
	}
	public java.awt.image.BufferedImage get_sigma_image() {
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			return blobs.getSigmaImage();
		} else {
			return null;
		}
	}
	public java.awt.image.BufferedImage get_foreground_image() {
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			return blobs.getForegroundImage();
		} else {
			return null;
		}
	}
	public java.awt.image.BufferedImage get_synthesized_image() {
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			return blobs.getSynthesizedImage();
		} else {
			return null;
		}
	}
	//
	public Term get_blobs() {
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			return blobs.getBlobs();
		} else {
			return PrologEmptyList.instance;
		}
	}
	public Term get_tracks() {
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			return blobs.getTracks();
		} else {
			return PrologEmptyList.instance;
		}
	}
	public Term get_connected_graphs() {
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			return blobs.getConnectedGraphs();
		} else {
			return PrologEmptyList.instance;
		}
	}
	//
	synchronized public void setBlobExtractionMode(boolean mode) {
		extractBlobs= mode;
	}
	synchronized public boolean getBlobExtractionMode() {
		return extractBlobs;
	}
	//
	synchronized public void setBlobTracingMode(boolean mode) {
		trackBlobs= mode;
	}
	synchronized public boolean getBlobTracingMode() {
		return trackBlobs;
	}
	//
	synchronized public void setMinimalTrainingInterval(int value) {
		minimalTrainingInterval= value;
	}
	synchronized public int getMinimalTrainingInterval() {
		return minimalTrainingInterval;
	}
	//
	synchronized public void setGrayscaleMode(boolean mode) {
		if (useGrayscaleColors != mode) {
			useGrayscaleColors= mode;
			forgetStatistics();
		}
	}
	synchronized public boolean getGrayscaleMode() {
		return useGrayscaleColors;
	}
	//
	synchronized public void setBackgroundGaussianFilteringMode(boolean mode) {
		if (useGaussianFiltering != mode) {
			useGaussianFiltering= mode;
			forgetStatistics();
		}
	}
	synchronized public boolean getBackgroundGaussianFilteringMode() {
		return useGaussianFiltering;
	}
	//
	synchronized public void setBackgroundGaussianFilterRadius(int value) {
		if (gaussianFilterRadius != value) {
			gaussianFilterRadius= value;
			gaussianMatrix= null;
		}
	}
	synchronized public int getBackgroundGaussianFilterRadius() {
		return gaussianFilterRadius;
	}
	//
	synchronized public void setBackgroundMedianFilteringMode(boolean mode) {
		if (useMedianFiltering != mode) {
			useMedianFiltering= mode;
			forgetStatistics();
		}
	}
	synchronized public boolean getBackgroundMedianFilteringMode() {
		return useMedianFiltering;
	}
	//
	synchronized public void setBackgroundMedianFilterThreshold(int value) {
		medianFilterThreshold= value;
	}
	synchronized public int getBackgroundMedianFilterThreshold() {
		return medianFilterThreshold;
	}
	//
	synchronized public void setBackgroundDispersionFactor(double value) {
		backgroundDispersionFactor= value;
	}
	synchronized public double getBackgroundDispersionFactor() {
		return backgroundDispersionFactor;
	}
	//
	synchronized public void setHorizontalBlobBorder(int value) {
		horizontalBlobBorder= value;
	}
	synchronized public int getHorizontalBlobBorder() {
		return horizontalBlobBorder;
	}
	//
	synchronized public void setVerticalBlobBorder(int value) {
		verticalBlobBorder= value;
	}
	synchronized public int getVerticalBlobBorder() {
		return verticalBlobBorder;
	}
	//
	synchronized public void setMinimalBlobIntersectionArea(int value) {
		minimalBlobIntersectionArea= value;
	}
	synchronized public int getMinimalBlobIntersectionArea() {
		return minimalBlobIntersectionArea;
	}
	//
	synchronized public void setMinimalBlobSize(int value) {
		minimalBlobSize= value;
	}
	synchronized public int getMinimalBlobSize() {
		return minimalBlobSize;
	}
	//
	synchronized public void setMinimalTrackDuration(int value) {
		minimalTrackDuration= value;
	}
	synchronized public int getMinimalTrackDuration() {
		return minimalTrackDuration;
	}
	//
	synchronized public void setMaximalBlobInvisibilityInterval(int value) {
		maximalBlobInvisibilityInterval= value;
	}
	synchronized public int getMaximalBlobInvisibilityInterval() {
		return maximalBlobInvisibilityInterval;
	}
	//
	synchronized public void setMaximalTrackRetentionInterval(int value) {
		maximalTrackRetentionInterval= value;
	}
	synchronized public int getMaximalTrackRetentionInterval() {
		return maximalTrackRetentionInterval;
	}
	//
	synchronized public void setInverseTransformationMatrix(double[][] value) {
		inverseMatrix= value;
	}
	synchronized public double[][] getInverseTransformationMatrix() {
		return inverseMatrix;
	}
	//
	synchronized public void setSamplingRate(double value) {
		samplingRate= value;
	}
	synchronized public double getSamplingRate() {
		return samplingRate;
	}
	//
	synchronized public void setVelocityMedianFilteringMode(boolean mode) {
		applyMedianFiltering= mode;
	}
	synchronized public boolean getVelocityMedianFilteringMode() {
		return applyMedianFiltering;
	}
	//
	synchronized public void setVelocityMedianFilterHalfwidth(int value) {
		medianFilterHalfwidth= value;
	}
	synchronized public int getVelocityMedianFilterHalfwidth() {
		return medianFilterHalfwidth;
	}
	//
	synchronized public void setSlowTracksDeletionMode(boolean mode) {
		refuseSlowTracks= mode;
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			blobs.setSlowTracksDeletionMode(mode);
		}
	}
	synchronized public boolean getSlowTracksDeletionMode() {
		return refuseSlowTracks;
	}
	//
	synchronized public void setFuzzyVelocityThreshold(double value) {
		velocityThreshold= value;
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			blobs.setFuzzyVelocityThreshold(value);
		}
	}
	synchronized public double getFuzzyVelocityThreshold() {
		return velocityThreshold;
	}
	//
	synchronized public void setFuzzyDistanceThreshold(double value) {
		distanceThreshold= value;
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			blobs.setFuzzyDistanceThreshold(value);
		}
	}
	synchronized public double getFuzzyDistanceThreshold() {
		return distanceThreshold;
	}
	//
	synchronized public void setFuzzyThresholdBorder(double value) {
		fuzzyThresholdBorder= value;
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			blobs.setFuzzyThresholdBorder(value);
		}
	}
	synchronized public double getFuzzyThresholdBorder() {
		return fuzzyThresholdBorder;
	}
	//
	synchronized public void setSynthesizedImageTransparency(int value) {
		synthesizedImageTransparency= value;
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			blobs.setSynthesizedImageTransparency(value);
		}
	}
	synchronized public int getSynthesizedImageTransparency() {
		return synthesizedImageTransparency;
	}
	//
	synchronized public void setSynthesizedImageRectangularBlobsMode(boolean value) {
		makeSquareBlobsInSynthesizedImage= value;
		BlobSet blobs= recentBlobSet.get();
		if (blobs != null) {
			blobs.setSynthesizedImageRectangularBlobsMode(value);
		}
	}
	synchronized public boolean getSynthesizedImageRectangularBlobsMode() {
		return makeSquareBlobsInSynthesizedImage;
	}
	///////////////////////////////////////////////////////////////
	// SUBTRACT IMAGE                                            //
	///////////////////////////////////////////////////////////////
	public void subtractImage(java.awt.image.BufferedImage image, boolean takeImageIntoAccount) {
		if (backgroundN < 0) {
			imageWidth= image.getWidth();
			imageHeight= image.getHeight();
			vectorLength= imageWidth * imageHeight;
			if (useGrayscaleColors) {
				numberOfBands= 1;
				numberOfExtraBands= 0;
			} else {
				numberOfBands= 3;
				numberOfExtraBands= 1;
			};
			backgroundN= 0;
			backgroundSum= new int[numberOfBands][vectorLength];
			backgroundSumX2= new int[numberOfBands][vectorLength];
			deltaPixels= new int[numberOfBands+numberOfExtraBands][vectorLength];
		};
		if (useGrayscaleColors) {
			image= filterAndConvertToGray(image);
		} else {
			image= filterAndConvertToBGR(image);
		};
		WritableRaster raster= image.getRaster();
		int[][] imagePixels= new int[numberOfBands][vectorLength];
		for (int k=0; k < numberOfBands; k++) {
			raster.getSamples(0,0,imageWidth,imageHeight,k,imagePixels[k]);
		};
		for (int k=0; k < numberOfBands+numberOfExtraBands; k++) {
			for (int n=0; n < vectorLength; n++) {
				deltaPixels[k][n]= 0;
			}
		};
		for (int k=0; k < numberOfBands; k++) {
			for (int n=0; n < vectorLength; n++) {
				int sum= backgroundSum[k][n];
				int mean= 0;
				int dispersion= 0;
				if (backgroundN > 0) {
					mean= sum / backgroundN;
					dispersion= (backgroundSumX2[k][n] / backgroundN) - mean*mean;
				};
				int value= imagePixels[k][n];
				int delta1= value - mean;
				// int delta1= (value - mean) / 2;
				int delta2= delta1 * delta1;
				// if (delta2 < dispersion) {	// BUG#2
				if (delta2 <= dispersion * backgroundDispersionFactor) {
					deltaPixels[k+numberOfExtraBands][n]= noDifferenceMarker;
				} else {
					if (delta1 < 0) {
						delta1= - delta1;
					};
					deltaPixels[k+numberOfExtraBands][n]= 255 - delta1;
				}
			}
		};
		if (useMedianFiltering) {
			for (int k=0; k < numberOfBands; k++) {
				deltaPixels[k+numberOfExtraBands]= median2D(noDifferenceMarker,deltaPixels[k+numberOfExtraBands],imageWidth,imageHeight,medianFilterThreshold);
			}
		};
		if (numberOfExtraBands > 0) {
			for (int n=0; n < vectorLength; n++) {
				int value= noDifferenceMarker;
				for (int k=0; k < numberOfBands; k++) {
					if (deltaPixels[k+numberOfExtraBands][n] != noDifferenceMarker) {
						value= differenceMarker;
						break;
					}
				};
				deltaPixels[0][n]= value;
			};
			if (useMedianFiltering) {
				deltaPixels[0]= median2D(noDifferenceMarker,deltaPixels[0],imageWidth,imageHeight,medianFilterThreshold);
			}
		};
		if (takeImageIntoAccount) {
			if (backgroundN >= maximalN) {
				backgroundN= backgroundN / overcrowdedMatrixReductionCoefficient;
				for (int k=0; k < numberOfBands; k++) {
					for (int n=0; n < vectorLength; n++) {
						backgroundSum[k][n]= backgroundSum[k][n] / overcrowdedMatrixReductionCoefficient;
						backgroundSumX2[k][n]= backgroundSumX2[k][n] / overcrowdedMatrixReductionCoefficient;
					}
				}
			};
			backgroundN= backgroundN + 1;
			for (int k=0; k < numberOfBands; k++) {
				for (int n=0; n < vectorLength; n++) {
					int value= imagePixels[k][n];
					backgroundSum[k][n]= backgroundSum[k][n] + value;
					backgroundSumX2[k][n]= backgroundSumX2[k][n] + value*value;
				}
			}
		}
	}
	public java.awt.image.BufferedImage filterAndConvertToGray(java.awt.image.BufferedImage a) {
		java.awt.image.BufferedImage b= new java.awt.image.BufferedImage(a.getWidth(),a.getHeight(),java.awt.image.BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2= (Graphics2D)b.getGraphics();
		if (useGaussianFiltering) {
			if (gaussianMatrix==null) {
				gaussianMatrix= VisionUtils.gaussianMatrix(gaussianFilterRadius);
			};
			int length= (int)StrictMath.round(StrictMath.sqrt(gaussianMatrix.length));
			ConvolveOp cop= new ConvolveOp(
				new Kernel(length,length,gaussianMatrix),
				// ConvolveOp.EDGE_NO_OP,
				ConvolveOp.EDGE_ZERO_FILL,
				null);
			g2.drawImage(a,cop,0,0);
		} else {
			g2.drawImage(a,0,0,null);
		};
		return b;
	}
	public java.awt.image.BufferedImage filterAndConvertToBGR(java.awt.image.BufferedImage a) {
		java.awt.image.BufferedImage b= new java.awt.image.BufferedImage(a.getWidth(),a.getHeight(),java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2= (Graphics2D)b.getGraphics();
		if (useGaussianFiltering) {
			if (gaussianMatrix==null) {
				gaussianMatrix= VisionUtils.gaussianMatrix(gaussianFilterRadius);
			};
			int length= (int)StrictMath.round(StrictMath.sqrt(gaussianMatrix.length));
			ConvolveOp cop= new ConvolveOp(
				new Kernel(length,length,gaussianMatrix),
				// ConvolveOp.EDGE_NO_OP,
				ConvolveOp.EDGE_ZERO_FILL,
				null);
			g2.drawImage(a,cop,0,0);
		} else {
			g2.drawImage(a,0,0,null);
		};
		return b;
	}
	public static int[] median2D(int noDifferenceMarker, int[] pixels, int width, int height, int threshold) {
		int[] result= Arrays.copyOf(pixels,pixels.length);
		for (int w=0; w < width; w++) {
			int h1= 0;
			int point1= width * h1 + w;
			result[point1]= noDifferenceMarker;
			int h2= height-1;
			int point2= width * h2 + w;
			result[point2]= noDifferenceMarker;
		};
		for (int h=0; h < height; h++) {
			int w1= 0;
			int point1= width * h + w1;
			result[point1]= noDifferenceMarker;
			int w2= width-1;
			int point2= width * h + w2;
			result[point2]= noDifferenceMarker;
		};
		for (int w=1; w < width-1; w++) {
			for (int h=1; h < height-1; h++) {
				int point= width * h + w;
				int value= pixels[point];
				if (value == noDifferenceMarker) {
					continue;
				} else {
					int counter= 0;
					int north= width * (h - 1) + w;
					if (pixels[north] == noDifferenceMarker) {
						counter++;
					};
					int northEast= width * (h - 1) + (w + 1);
					if (pixels[northEast] == noDifferenceMarker) {
						counter++;
					};
					int east= width * h + (w + 1);
					if (pixels[east] == noDifferenceMarker) {
						counter++;
					};
					int southEast= width * (h + 1) + (w + 1);
					if (pixels[southEast] == noDifferenceMarker) {
						counter++;
					};
					int south= width * (h + 1) + w;
					if (pixels[south] == noDifferenceMarker) {
						counter++;
					};
					int southWest= width * (h + 1) + (w - 1);
					if (pixels[southWest] == noDifferenceMarker) {
						counter++;
					};
					int west= width * h + (w - 1);
					if (pixels[west] == noDifferenceMarker) {
						counter++;
					};
					int northWest= width * (h - 1) + (w - 1);
					if (pixels[northWest] == noDifferenceMarker) {
						counter++;
					};
					if (counter > threshold) {
						result[point]= noDifferenceMarker;
					}
				}
			}
		};
		return result;
	}
	// CLUSTER IMAGE
	protected void extractBlobs() {
		int maxRadius= imageWidth;
		if (maxRadius < imageHeight) {
			maxRadius= imageHeight;
		};
		int[] inputPixels= deltaPixels[0];
		int blobCounter= 0;
		boolean[] blobFlag= new boolean[vectorLength];
		int[][] blobRectangles= new int[vectorLength][4];
//---------------------------------------------------------------------
int rows= imageHeight - 2;
int columns= imageWidth - 2;
int totalDistance= rows * columns;
int c0;
if (columns % 2 == 0) {
	c0= columns / 2 + 1;
} else {
	c0= columns / 2 + 1;
};
int r0;
if (rows % 2 == 0) {
	r0= rows / 2;
} else {
	r0= rows / 2 + 1;
};
int globalCounter1= 0;
int localCounter1= 0;
int radius1= 1;
boolean verticalMovement1= true;
boolean goUp1= true;
boolean goRight1= false;
int c1= c0;
int r1= r0;
while (globalCounter1 < totalDistance) {
	if (r1 <= rows && r1 > 0 && c1 <= columns && c1 > 0) {
		globalCounter1++;
		int point= imageWidth*(r1+1) + (c1+1);
		int currentValue= inputPixels[point];
		if (currentValue != noDifferenceMarker) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://
int x11= c1 - horizontalBlobBorder;
int x12= c1 + horizontalBlobBorder;
int y11= r1 - verticalBlobBorder;
int y12= r1 + verticalBlobBorder;
boolean accepted= false;
int actualBlobIndex= -1;
boolean repeatSearch= true;
InnerLoop: while (repeatSearch) {
	repeatSearch= false;
	for (int k=0; k < blobCounter; k++) {
		if (!blobFlag[k]) {
			continue;
		};
		if (actualBlobIndex==k) {
			if (repeatSearch) {
				continue;
			} else {
				break InnerLoop;
			}
		};
		int x21= blobRectangles[k][0];
		int x22= blobRectangles[k][1];
		int x31= x11;
		int x32= x12;
		if (x31 < x21) {
			x31= x21;
		};
		if (x32 > x22) {
			x32= x22;
		};
		int width3= x32 - x31;
		if (width3 < 0) {
			continue;
		};
		int y21= blobRectangles[k][2];
		int y22= blobRectangles[k][3];
		int y31= y11;
		int y32= y12;
		if (y31 < y21) {
			y31= y21;
		};
		if (y32 > y22) {
			y32= y22;
		};
		int height3= y32 - y31;
		if (height3 < 0) {
			continue;
		};
		int commonAreaSize= (width3 + 1) * (height3 + 1);
		if (commonAreaSize >= minimalBlobIntersectionArea) {
			if (blobRectangles[k][0] > x11) {
				blobRectangles[k][0]= x11;
			};
			if (blobRectangles[k][1] < x12) {
				blobRectangles[k][1]= x12;
			};
			if (blobRectangles[k][2] > y11) {
				blobRectangles[k][2]= y11;
			};
			if (blobRectangles[k][3] < y12) {
				blobRectangles[k][3]= y12;
			};
			accepted= true;
			repeatSearch= true;
			if (actualBlobIndex >= 0) {
				blobFlag[actualBlobIndex]= false;
			};
			actualBlobIndex= k;
			x11= blobRectangles[k][0];
			x12= blobRectangles[k][1];
			y11= blobRectangles[k][2];
			y12= blobRectangles[k][3];
		}
	};
	if (!accepted) {
		accepted= true;
		repeatSearch= true;
		blobCounter++;
		actualBlobIndex= blobCounter - 1;
		blobFlag[actualBlobIndex]= true;
		blobRectangles[actualBlobIndex][0]= x11;
		blobRectangles[actualBlobIndex][1]= x12;
		blobRectangles[actualBlobIndex][2]= y11;
		blobRectangles[actualBlobIndex][3]= y12;
	}
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://
		}
	};
	if (verticalMovement1) {
		if (goUp1) {
			r1++;
		} else {
			r1--;
		};
		if (localCounter1 <= 0) {
			verticalMovement1= !verticalMovement1;
			goUp1= !goUp1;
			localCounter1= radius1;
		}
	} else {
		if (goRight1) {
			c1++;
		} else {
			c1--;
		};
		if (localCounter1 <= 0) {
			verticalMovement1= !verticalMovement1;
			goRight1= !goRight1;
			radius1++;
			localCounter1= radius1;
		}
	};
	localCounter1--;
};
//---------------------------------------------------------------------
		int index= 0;
		int imageSize= (imageWidth - 2) * (imageHeight - 2);
		for (int k=0; k < blobCounter; k++) {
			if (blobFlag[k]) {
				int x1= blobRectangles[k][0];
				int x2= blobRectangles[k][1];
				int y1= blobRectangles[k][2];
				int y2= blobRectangles[k][3];
				if (x1 < 0) {
					x1= 0;
				} else if (x1 >= imageWidth) {
					x1= imageWidth - 1;
				};
				if (x2 < 0) {
					x2= 0;
				} else if (x2 >= imageWidth) {
					x2= imageWidth - 1;
				};
				if (y1 < 0) {
					y1= 0;
				} else if (y1 >= imageHeight) {
					y1= imageHeight - 1;
				};
				if (y2 < 0) {
					y2= 0;
				} else if (y2 >= imageHeight) {
					y2= imageHeight - 1;
				};
				int size= (x2 - x1 + 1) * (y2 - y1 + 1);
				int numberOfModifiedPixels= 0;
				for (int xx=x1; xx <= x2; xx++) {
					for (int yy=y1; yy <= y2; yy++) {
						int index1= imageWidth * yy + xx;
						if (inputPixels[index1] != noDifferenceMarker) {
							numberOfModifiedPixels++;
						}
					}
				};
				// double density= 1.0*numberOfModifiedPixels/size;
				// System.out.printf("density=%s\n",density);
				// double ratio= (x2 - x1 + 1.0) / (y2 - y1 + 1.0);
				// if (ratio > 1) {
				//	ratio= 1 / ratio;
				// };
				// System.out.printf("ratio=%s\n",ratio);
				if (size < minimalBlobSize || size >= imageSize) {
					blobFlag[k]= false;
				// } else if (density < 0.1) {
				//	blobFlag[k]= false;
				// } else if (ratio < 0.5) {
				//	blobFlag[k]= false;
				} else {
					index++;
				}
			}
		};
		int numberOfActiveBlobs= index;
		int[][] activeBlobRectangles= new int[numberOfActiveBlobs][4];
		index= 0;
		for (int k=0; k < blobCounter; k++) {
			if (blobFlag[k]) {
				for (int m=0; m < 4; m++) {
					activeBlobRectangles[index][m]= blobRectangles[k][m];
				};
				index++;
			}
		};
		int[] blobSize= new int[numberOfActiveBlobs];
		int[] trackDurations= new int[numberOfActiveBlobs];
		BigInteger[] blobIdentifiers= new BigInteger[numberOfActiveBlobs];
		for (int k=0; k < numberOfActiveBlobs; k++) {
			int x1= activeBlobRectangles[k][0];
			int x2= activeBlobRectangles[k][1];
			int y1= activeBlobRectangles[k][2];
			int y2= activeBlobRectangles[k][3];
			blobSize[k]= (x2 - x1 + 1) * (y2 - y1 + 1);
		};
		currentBlobSize= blobSize;
		currentTrackDurations= trackDurations;
		currentBlobRectangles= activeBlobRectangles;
		currentBlobIdentifiers= blobIdentifiers;
	}
	// IDENTIFY BLOBS
	public void identifyBlobs() {
		// CHECK PREVIOUS BLOBS
		if (previousBlobRectangles==null) {
			previousBlobRectangles= new int[0][4];
		};
		int numberOfActiveBlobs= currentBlobRectangles.length;
		int numberOfPreviousBlobs= previousBlobRectangles.length;
		BlobIntersection[] blobIntersections1= new BlobIntersection[numberOfActiveBlobs*numberOfPreviousBlobs];
		int numberOfIntersections1= 0;
		int[] collisionCounters1= new int[numberOfActiveBlobs];
		int[] collisionCounters2= new int[numberOfPreviousBlobs];
		for (int n=0; n < numberOfActiveBlobs; n++) {
			int x11= currentBlobRectangles[n][0];
			int x12= currentBlobRectangles[n][1];
			int y11= currentBlobRectangles[n][2];
			int y12= currentBlobRectangles[n][3];
			for (int k=0; k < numberOfPreviousBlobs; k++) {
				int x21= previousBlobRectangles[k][0];
				int x22= previousBlobRectangles[k][1];
				int x31= x11;
				int x32= x12;
				if (x31 < x21) {
					x31= x21;
				};
				if (x32 > x22) {
					x32= x22;
				};
				int widthMinusOne3= x32 - x31;
				if (widthMinusOne3 < 0) {
					widthMinusOne3= -1;
				};
				int y21= previousBlobRectangles[k][2];
				int y22= previousBlobRectangles[k][3];
				int y31= y11;
				int y32= y12;
				if (y31 < y21) {
					y31= y21;
				};
				if (y32 > y22) {
					y32= y22;
				};
				int heightMinusOne3= y32 - y31;
				if (heightMinusOne3 < 0) {
					heightMinusOne3= -1;
				};
				int commonAreaSize= (widthMinusOne3 + 1) * (heightMinusOne3 + 1);
				if (commonAreaSize > 0) {
					collisionCounters1[n]++;
					collisionCounters2[k]++;
					BigInteger identifier2= previousBlobIdentifiers[k];
					GrowingTrack track= tracks.get(identifier2);
					boolean isStrongIntersection= track.isStrong;
					blobIntersections1[numberOfIntersections1]= new BlobIntersection(commonAreaSize,n,k,isStrongIntersection);
					numberOfIntersections1++;
				}
			}
		};
		BlobIntersectionsComparator comparator= new BlobIntersectionsComparator();
		Arrays.sort(blobIntersections1,0,numberOfIntersections1,comparator);
		for (int k=0; k < numberOfActiveBlobs; k++) {
			currentBlobIdentifiers[k]= BigInteger.ZERO;
		};
		BigInteger[] usedIdentifiers1= new BigInteger[numberOfActiveBlobs];
		int numberOfUsedIdentifiers1= 0;
		boolean[] usedPreviousBlobs= new boolean[numberOfPreviousBlobs];
		int numberOfUsedPreviousBlobs= 0;
		Loop2: for (int n=0; n < numberOfIntersections1; n++) {
			int index1= blobIntersections1[n].index1;
			// if (currentBlobIdentifiers[index1] > 0) {
			if (currentBlobIdentifiers[index1].compareTo(BigInteger.ZERO) > 0) {
				continue;
			};
			int index2= blobIntersections1[n].index2;
			if (usedPreviousBlobs[index2]) {
				continue;
			};
			BigInteger identifier= previousBlobIdentifiers[index2];
			if (identifier.compareTo(BigInteger.ZERO) > 0) {
				for (int k=0; k < numberOfUsedIdentifiers1; k++) {
					if (usedIdentifiers1[k].equals(identifier)) {
						continue Loop2;
					}
				};
				currentBlobIdentifiers[index1]= identifier;
				int currentSize= currentBlobSize[index1];
				int previousSize= previousBlobSize[index2];
				if (previousSize > currentSize) {
					currentBlobSize[index1]= previousSize;
				};
				int previousDuration= previousTrackDurations[index2];
				if (previousDuration < Integer.MAX_VALUE) {
					previousDuration++;
				};
				currentTrackDurations[index1]= previousDuration;
				usedIdentifiers1[numberOfUsedIdentifiers1]= identifier;
				numberOfUsedIdentifiers1++;
				usedPreviousBlobs[index2]= true;
				numberOfUsedPreviousBlobs++;
			}
		};
		// CHECK INVISIBLE BLOBS
		if (invisibleBlobRectangles==null) {
			invisibleBlobRectangles= new int[0][4];
		};
		int numberOfInvisibleBlobs= invisibleBlobRectangles.length;
		int numberOfRemainderBlobs= numberOfActiveBlobs - numberOfUsedIdentifiers1;
		BlobIntersection[] blobIntersections2= new BlobIntersection[numberOfRemainderBlobs*numberOfInvisibleBlobs];
		int numberOfIntersections2= 0;
		boolean[] usedInvisibleBlobs= new boolean[numberOfInvisibleBlobs];
		boolean[] deletedBlobs= new boolean[numberOfInvisibleBlobs];
		for (int n=0; n < numberOfActiveBlobs; n++) {
			int x11= currentBlobRectangles[n][0];
			int x12= currentBlobRectangles[n][1];
			int y11= currentBlobRectangles[n][2];
			int y12= currentBlobRectangles[n][3];
			for (int k=0; k < numberOfInvisibleBlobs; k++) {
				int x21= invisibleBlobRectangles[k][0];
				int x22= invisibleBlobRectangles[k][1];
				int x31= x11;
				int x32= x12;
				if (x31 < x21) {
					x31= x21;
				};
				if (x32 > x22) {
					x32= x22;
				};
				int widthMinusOne3= x32 - x31;
				if (widthMinusOne3 < 0) {
					widthMinusOne3= -1;
				};
				int y21= invisibleBlobRectangles[k][2];
				int y22= invisibleBlobRectangles[k][3];
				int y31= y11;
				int y32= y12;
				if (y31 < y21) {
					y31= y21;
				};
				if (y32 > y22) {
					y32= y22;
				};
				int heightMinusOne3= y32 - y31;
				if (heightMinusOne3 < 0) {
					heightMinusOne3= -1;
				};
				int commonAreaSize= (widthMinusOne3 + 1) * (heightMinusOne3 + 1);
				if (commonAreaSize > 0) {
					if (currentBlobIdentifiers[n].compareTo(BigInteger.ZERO) > 0) {
						usedInvisibleBlobs[k]= true;
						deletedBlobs[k]= true;
					} else {
						blobIntersections2[numberOfIntersections2]= new BlobIntersection(commonAreaSize,n,k,true);
						numberOfIntersections2++;
					}
				}
			}
		};
		Arrays.sort(blobIntersections2,0,numberOfIntersections2,comparator);
		Loop3: for (int n=0; n < numberOfIntersections2; n++) {
			int index1= blobIntersections2[n].index1;
			if (currentBlobIdentifiers[index1].compareTo(BigInteger.ZERO) > 0) {
				continue;
			};
			int index2= blobIntersections2[n].index2;
			if (usedInvisibleBlobs[index2]) {
				continue;
			};
			BigInteger identifier= invisibleBlobIdentifiers[index2];
			if (identifier.compareTo(BigInteger.ZERO) > 0) {
				for (int k=0; k < numberOfUsedIdentifiers1; k++) {
					if (usedIdentifiers1[k].equals(identifier)) {
						continue Loop3;
					}
				};
				currentBlobIdentifiers[index1]= identifier;
				int currentSize= currentBlobSize[index1];
				int invisibleSize= invisibleBlobSize[index2];
				if (invisibleSize > currentSize) {
					currentBlobSize[index1]= invisibleSize;
				};
				int invisibleDuration= invisibleTrackDurations[index2];
				if (invisibleDuration < Integer.MAX_VALUE) {
					invisibleDuration++;
				};
				currentTrackDurations[index1]= invisibleDuration;
				usedIdentifiers1[numberOfUsedIdentifiers1]= identifier;
				numberOfUsedIdentifiers1++;
				usedInvisibleBlobs[index2]= true;
			}
		};
		// CREATE NEW TRACKS
		for (int k=0; k < numberOfActiveBlobs; k++) {
			if (currentBlobIdentifiers[k].compareTo(BigInteger.ZERO) <= 0) {
				recentBlobIdentifier= recentBlobIdentifier.add(BigInteger.ONE);
				currentBlobIdentifiers[k]= recentBlobIdentifier;
				GrowingTrack track= new GrowingTrack(recentBlobIdentifier,time,minimalTrackDuration,maximalRarefactionOfObject,inverseMatrix,samplingRate,applyMedianFiltering,medianFilterHalfwidth);
				tracks.put(recentBlobIdentifier,track);
			}
		};
		// PREPARE DEFERRED OPERATIONS
		// Prepare creation of inputs
		for (int n=0; n < numberOfIntersections1; n++) {
			int index1a= blobIntersections1[n].index1;
			if (collisionCounters1[index1a] > 1) {
				int index2a= blobIntersections1[n].index2;
				BigInteger identifier2a= previousBlobIdentifiers[index2a];
				GrowingTrack track2a= tracks.get(identifier2a);
				for (int k=0; k < numberOfIntersections1; k++) {
					int index1b= blobIntersections1[k].index1;
					if (index1a != index1b) {
						continue;
					};
					int index2b= blobIntersections1[k].index2;
					if (index2a==index2b) {
						continue;
					};
					BigInteger identifier2b= previousBlobIdentifiers[index2b];
					GrowingTrack track2b= tracks.get(identifier2b);
					track2b.registerCollision(track2a,time,TrackSegmentEntryType.INPUT);
				}
			}
		};
		// Prepare creation of outputs
		for (int n=0; n < numberOfIntersections1; n++) {
			int index2= blobIntersections1[n].index2;
			if (collisionCounters2[index2] > 1) {
				BigInteger identifier2= previousBlobIdentifiers[index2];
				int index1= blobIntersections1[n].index1;
				BigInteger identifier1= currentBlobIdentifiers[index1];
				if (!identifier1.equals(identifier2)) {
					GrowingTrack track1= tracks.get(identifier1);
					GrowingTrack track2= tracks.get(identifier2);
					track2.registerCollision(track1,time,TrackSegmentEntryType.OUTPUT);
				}
			}
		};
		// PROLONG & DELETE INVISIBLE BLOBS
		int numberOfProlongedBlobs= 0;
		for (int k=0; k < numberOfInvisibleBlobs; k++) {
			if (usedInvisibleBlobs[k]) {
				continue;
			};
			if (invisibleBlobDelays[k] > maximalBlobInvisibilityInterval) {
				deletedBlobs[k]= true;
				continue;
			};
			numberOfProlongedBlobs++;
		};
		int numberOfUnusedPreviousBlobs= numberOfPreviousBlobs - numberOfUsedPreviousBlobs;
		numberOfProlongedBlobs= numberOfProlongedBlobs + numberOfUnusedPreviousBlobs;
		int[] prolongedBlobSize= new int[numberOfProlongedBlobs];
		int[] prolongedTrackDurations= new int[numberOfProlongedBlobs];
		int[][] prolongedBlobRectangles= new int[numberOfProlongedBlobs][4];
		BigInteger[] prolongedBlobIdentifiers= new BigInteger[numberOfProlongedBlobs];
		int[] prolongedBlobDelays= new int[numberOfProlongedBlobs];
		numberOfProlongedBlobs= 0;
		for (int k=0; k < numberOfPreviousBlobs; k++) {
			if (usedPreviousBlobs[k]) {
				continue;
			};
			prolongedBlobSize[numberOfProlongedBlobs]= previousBlobSize[k];
			prolongedTrackDurations[numberOfProlongedBlobs]= previousTrackDurations[k];
			for (int m=0; m < 4; m++) {
				prolongedBlobRectangles[numberOfProlongedBlobs][m]= previousBlobRectangles[k][m];
			};
			prolongedBlobIdentifiers[numberOfProlongedBlobs]= previousBlobIdentifiers[k];
			prolongedBlobDelays[numberOfProlongedBlobs]= 1;
			numberOfProlongedBlobs++;
		};
		for (int k=0; k < numberOfInvisibleBlobs; k++) {
			if (usedInvisibleBlobs[k]) {
				continue;
			};
			if (invisibleBlobDelays[k] > maximalBlobInvisibilityInterval) {
				deletedBlobs[k]= true;
				continue;
			};
			prolongedBlobSize[numberOfProlongedBlobs]= invisibleBlobSize[k];
			prolongedTrackDurations[numberOfProlongedBlobs]= invisibleTrackDurations[k];
			for (int m=0; m < 4; m++) {
				prolongedBlobRectangles[numberOfProlongedBlobs][m]= invisibleBlobRectangles[k][m];
			};
			prolongedBlobIdentifiers[numberOfProlongedBlobs]= invisibleBlobIdentifiers[k];
			prolongedBlobDelays[numberOfProlongedBlobs]= invisibleBlobDelays[k] + 1;
			numberOfProlongedBlobs++;
		};
		// STOP TRACKS
		for (int k=0; k < numberOfInvisibleBlobs; k++) {
			BigInteger identifier= invisibleBlobIdentifiers[k];
			GrowingTrack track= tracks.get(identifier);
			if (deletedBlobs[k]) {
				if (track.isStrong) {
					track.makeInsensible(time);
				} else {
					track.depose();
					tracks.remove(identifier);
				}
			} else if (!usedInvisibleBlobs[k]) {
				track.prolong(time,false);
			}
		};
		// STORE PROLONGED BLOBS
		invisibleBlobSize= prolongedBlobSize;
		invisibleTrackDurations= prolongedTrackDurations;
		invisibleBlobRectangles= prolongedBlobRectangles;
		invisibleBlobIdentifiers= prolongedBlobIdentifiers;
		invisibleBlobDelays= prolongedBlobDelays;
		// IDENTIFY OBJECTS
		HashSet<BigInteger> tracksToBeInspected= new HashSet<>();
		// ERASE OLD TRACKS
		Set<BigInteger> trackIdentifiers= tracks.keySet();
		Iterator<BigInteger> trackIterator= trackIdentifiers.iterator();
		while (trackIterator.hasNext()) {
			BigInteger identifier= trackIterator.next();
			GrowingTrack track= tracks.get(identifier);
			if (track.isToBeCompleted(time)) {
				tracksToBeInspected.add(identifier);
				track.complete(time);
			} else if (track.isOutOfDate(time,maximalTrackRetentionInterval)) {
				track.depose();
				trackIterator.remove();
			}
		};
		// UPDATE TRACKS
		for (int k=0; k < numberOfActiveBlobs; k++) {
			BigInteger identifier= currentBlobIdentifiers[k];
			int x1= currentBlobRectangles[k][0];
			int x2= currentBlobRectangles[k][1];
			int y1= currentBlobRectangles[k][2];
			int y2= currentBlobRectangles[k][3];
			GrowingTrack track= tracks.get(identifier);
			int[] inputPixels= deltaPixels[0];
			double[][] histograms= computeHistogram(recentImage,inputPixels,x1,x2,y1,y2);
			track.appendPoint(new BlobAttributes(time,x1,x2,y1,y2,histograms));
			tracks.put(identifier,track);
		}
	}
	protected double[][] computeHistogram(java.awt.image.BufferedImage image, int[] inputPixels, int x1, int x2, int y1, int y2) {
		int nBins= 256;
		WritableRaster raster= image.getRaster();
		int bandNumber= raster.getNumBands();
		int maximalWidth= raster.getWidth();
		int maximalHeight= raster.getHeight();
		if (x1 < 0) {
			x1= 0;
		} else if (x1 >= maximalWidth) {
			x1= maximalWidth - 1;
		};
		if (x2 < 0) {
			x2= 0;
		} else if (x2 >= maximalWidth) {
			x2= maximalWidth - 1;
		};
		if (y1 < 0) {
			y1= 0;
		} else if (y1 >= maximalHeight) {
			y1= maximalHeight - 1;
		};
		if (y2 < 0) {
			y2= 0;
		} else if (y2 >= maximalHeight) {
			y2= maximalHeight - 1;
		};
		int width= x2 - x1 + 1;
		int height= y2 - y1 + 1;
		int[] pixels= new int[width*height];
		double[][] frequencyCounts= new double[bandNumber][nBins+3];
		for (int k=0; k < bandNumber; k++) {
			pixels= raster.getSamples(x1,y1,width,height,k,pixels);
			int counter= 0;
			for (int xx=x1; xx <= x2; xx++) {
				for (int yy=y1; yy <= y2; yy++) {
					int index1= imageWidth * yy + xx;
					if (inputPixels[index1]==noDifferenceMarker) {
						continue;
					};
					int index2= width * (yy-y1) + (xx-x1);
					int pixel= pixels[index2];
					if (pixel < nBins-1) {
						counter++;
					};
					frequencyCounts[k][pixel]++;
				}
			};
			// for (int n=0; n < nBins; n++) {
			//	frequencyCounts[k][n]= frequencyCounts[k][n] / counter;
			// };
			frequencyCounts[k][nBins]= counter;
			frequencyCounts[k][nBins+1]= pixels.length;
			if (counter > 0) {
				frequencyCounts[k][nBins+2]= pixels.length / counter;
			} else {
				frequencyCounts[k][nBins+2]= -1;
			}
		};
		return frequencyCounts;
	}
}