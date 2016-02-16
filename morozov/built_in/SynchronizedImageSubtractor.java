// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.*;
import morozov.system.vision.errors.*;
import morozov.terms.*;
import morozov.terms.errors.*;
import morozov.worlds.*;
import morozov.worlds.remote.*;

import java.util.concurrent.atomic.AtomicReference;

public abstract class SynchronizedImageSubtractor
		extends GenericImageSubtractor
		implements ImageSubtractorOperations {
	//
	public AtomicReference<ImageSubtractorOperations> imageSubtractor= new AtomicReference<>(null);
	//
	public SynchronizedImageSubtractor() {
	}
	public SynchronizedImageSubtractor(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_image_subtractor();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set imageSubtractor
	//
	public void setImageSubtractor1s(ChoisePoint iX, Term a1) {
		setImageSubtractor(termToImageSubtractor(a1,iX));
	}
	public void setImageSubtractor(ImageSubtractorOperations value) {
		imageSubtractor.set(value);
	}
	public void getImageSubtractor0ff(ChoisePoint iX, PrologVariable a1) {
		a1.value= getImageSubtractor(iX).toTerm();
	}
	public void getImageSubtractor0fs(ChoisePoint iX) {
	}
	public ImageSubtractorOperations getImageSubtractor(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= imageSubtractor.get();
		if (subtractor != null) {
			return subtractor;
		} else {
			Term value= getBuiltInSlot_E_image_subtractor();
			return termToImageSubtractor(value,iX);
		}
	}
	public ImageSubtractorOperations termToImageSubtractor(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable()) {
			throw new WrongArgumentIsNotBoundVariable(value);
		} else if (value instanceof ImageSubtractor) {
			ImageSubtractorOperations subtractor= (ImageSubtractorOperations)value;
			return subtractor;
		} else if (value instanceof ForeignWorldWrapper) {
			ForeignWorldWrapper wrapper= (ForeignWorldWrapper)value;
			ForeignImageSubtractor subtractor= new ForeignImageSubtractor(wrapper);
			return subtractor;
		} else {
			throw new WrongArgumentIsNotImageSubtractor(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// BlobExtractionMode
	//
	public void setBlobExtractionMode(boolean mode, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setBlobExtractionMode(mode,iX);
	}
	public boolean getBlobExtractionMode(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getBlobExtractionMode(iX);
	}
	//
	// BlobTracingMode
	//
	public void setBlobTracingMode(boolean mode, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setBlobTracingMode(mode,iX);
	}
	public boolean getBlobTracingMode(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getBlobTracingMode(iX);
	}
	//
	// MinimalTrainingInterval
	//
	public void setMinimalTrainingInterval(int frames, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setMinimalTrainingInterval(frames,iX);
	}
	public int getMinimalTrainingInterval(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getMinimalTrainingInterval(iX);
	}
	//
	// MaximalTrainingInterval
	//
	public void setMaximalTrainingInterval(int frames, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setMaximalTrainingInterval(frames,iX);
	}
	public int getMaximalTrainingInterval(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getMaximalTrainingInterval(iX);
	}
	//
	// GrayscaleMode
	//
	public void setGrayscaleMode(boolean mode, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setGrayscaleMode(mode,iX);
	}
	public boolean getGrayscaleMode(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getGrayscaleMode(iX);
	}
	//
	// BackgroundGaussianFilteringMode
	//
	public void setBackgroundGaussianFilteringMode(boolean mode, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setBackgroundGaussianFilteringMode(mode,iX);
	}
	public boolean getBackgroundGaussianFilteringMode(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getBackgroundGaussianFilteringMode(iX);
	}
	//
	// BackgroundGaussianFilterRadius
	//
	public void setBackgroundGaussianFilterRadius(int radius, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setBackgroundGaussianFilterRadius(radius,iX);
	}
	public int getBackgroundGaussianFilterRadius(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getBackgroundGaussianFilterRadius(iX);
	}
	//
	// BackgroundRankFilteringMode
	//
	public void setBackgroundRankFilteringMode(boolean mode, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setBackgroundRankFilteringMode(mode,iX);
	}
	public boolean getBackgroundRankFilteringMode(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getBackgroundRankFilteringMode(iX);
	}
	//
	// BackgroundRankFilterThreshold
	//
	public void setBackgroundRankFilterThreshold(int threshold, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setBackgroundRankFilterThreshold(threshold,iX);
	}
	public int getBackgroundRankFilterThreshold(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getBackgroundRankFilterThreshold(iX);
	}
	//
	// BackgroundStandardDeviationFactor
	//
	public void setBackgroundStandardDeviationFactor(double factor, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setBackgroundStandardDeviationFactor(factor,iX);
	}
	public double getBackgroundStandardDeviationFactor(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getBackgroundStandardDeviationFactor(iX);
	}
	//
	// ForegroundContouringMode
	//
	public void setForegroundContouringMode(boolean mode, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setForegroundContouringMode(mode,iX);
	}
	public boolean getForegroundContouringMode(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getForegroundContouringMode(iX);
	}
	//
	// R2WindowHalfwidth
	//
	public void setR2WindowHalfwidth(int halfwidth, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setR2WindowHalfwidth(halfwidth,iX);
	}
	public int getR2WindowHalfwidth(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getR2WindowHalfwidth(iX);
	}
	//
	// HorizontalBlobBorder
	//
	public void setHorizontalBlobBorder(int size, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setHorizontalBlobBorder(size,iX);
	}
	public int getHorizontalBlobBorder(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getHorizontalBlobBorder(iX);
	}
	//
	// VerticalBlobBorder
	//
	public void setVerticalBlobBorder(int size, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setVerticalBlobBorder(size,iX);
	}
	public int getVerticalBlobBorder(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getVerticalBlobBorder(iX);
	}
	//
	// HorizontalExtraBorderCoefficient
	//
	public void setHorizontalExtraBorderCoefficient(double coefficient, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setHorizontalExtraBorderCoefficient(coefficient,iX);
	}
	public double getHorizontalExtraBorderCoefficient(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getHorizontalExtraBorderCoefficient(iX);
	}
	//
	// VerticalExtraBorderCoefficient
	//
	public void setVerticalExtraBorderCoefficient(double coefficient, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setVerticalExtraBorderCoefficient(coefficient,iX);
	}
	public double getVerticalExtraBorderCoefficient(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getVerticalExtraBorderCoefficient(iX);
	}
	//
	// MinimalBlobIntersectionArea
	//
	public void setMinimalBlobIntersectionArea(int size, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setMinimalBlobIntersectionArea(size,iX);
	}
	public int getMinimalBlobIntersectionArea(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getMinimalBlobIntersectionArea(iX);
	}
	//
	// MinimalBlobSize
	//
	public void setMinimalBlobSize(int size, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setMinimalBlobSize(size,iX);
	}
	public int getMinimalBlobSize(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getMinimalBlobSize(iX);
	}
	//
	// MinimalTrackDuration
	//
	public void setMinimalTrackDuration(int frames, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setMinimalTrackDuration(frames,iX);
	}
	public int getMinimalTrackDuration(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getMinimalTrackDuration(iX);
	}
	//
	// MaximalBlobInvisibilityInterval
	//
	public void setMaximalBlobInvisibilityInterval(int frames, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setMaximalBlobInvisibilityInterval(frames,iX);
	}
	public int getMaximalBlobInvisibilityInterval(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getMaximalBlobInvisibilityInterval(iX);
	}
	//
	// MaximalTrackRetentionInterval
	//
	public void setMaximalTrackRetentionInterval(int frames, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setMaximalTrackRetentionInterval(frames,iX);
	}
	public int getMaximalTrackRetentionInterval(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getMaximalTrackRetentionInterval(iX);
	}
	//
	// InverseTransformationMatrix
	//
	public void setInverseTransformationMatrix(double[][] matrix, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setInverseTransformationMatrix(matrix,iX);
	}
	public void setSerializedInverseTransformationMatrix(byte[] matrix, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setSerializedInverseTransformationMatrix(matrix,iX);
	}
	public double[][] getInverseTransformationMatrix(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getInverseTransformationMatrix(iX);
	}
	public byte[] getSerializedInverseTransformationMatrix(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedInverseTransformationMatrix(iX);
	}
	//
	// SamplingRate
	//
	public void setSamplingRate(double rate, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setSamplingRate(rate,iX);
	}
	public double getSamplingRate(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSamplingRate(iX);
	}
	//
	// CharacteristicLengthMedianFilteringMode
	//
	public void setCharacteristicLengthMedianFilteringMode(boolean mode, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setCharacteristicLengthMedianFilteringMode(mode,iX);
	}
	public boolean getCharacteristicLengthMedianFilteringMode(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getCharacteristicLengthMedianFilteringMode(iX);
	}
	//
	// CharacteristicLengthMedianFilterHalfwidth
	//
	public void setCharacteristicLengthMedianFilterHalfwidth(int halfwidth, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setCharacteristicLengthMedianFilterHalfwidth(halfwidth,iX);
	}
	public int getCharacteristicLengthMedianFilterHalfwidth(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getCharacteristicLengthMedianFilterHalfwidth(iX);
	}
	//
	// VelocityMedianFilteringMode
	//
	public void setVelocityMedianFilteringMode(boolean mode, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setVelocityMedianFilteringMode(mode,iX);
	}
	public boolean getVelocityMedianFilteringMode(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getVelocityMedianFilteringMode(iX);
	}
	//
	// VelocityMedianFilterHalfwidth
	//
	public void setVelocityMedianFilterHalfwidth(int threshold, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setVelocityMedianFilterHalfwidth(threshold,iX);
	}
	public int getVelocityMedianFilterHalfwidth(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getVelocityMedianFilterHalfwidth(iX);
	}
	//
	// SlowTracksDeletionMode
	//
	public void setSlowTracksDeletionMode(boolean mode, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setSlowTracksDeletionMode(mode,iX);
	}
	public boolean getSlowTracksDeletionMode(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSlowTracksDeletionMode(iX);
	}
	//
	// FuzzyVelocityThreshold
	//
	public void setFuzzyVelocityThreshold(double threshold, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setFuzzyVelocityThreshold(threshold,iX);
	}
	public double getFuzzyVelocityThreshold(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getFuzzyVelocityThreshold(iX);
	}
	//
	// FuzzyDistanceThreshold
	//
	public void setFuzzyDistanceThreshold(double threshold, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setFuzzyDistanceThreshold(threshold,iX);
	}
	public double getFuzzyDistanceThreshold(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getFuzzyDistanceThreshold(iX);
	}
	//
	// FuzzyThresholdBorder
	//
	public void setFuzzyThresholdBorder(double size, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setFuzzyThresholdBorder(size,iX);
	}
	public double getFuzzyThresholdBorder(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getFuzzyThresholdBorder(iX);
	}
	//
	// SynthesizedImageTransparency
	//
	public void setSynthesizedImageTransparency(int transparency, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setSynthesizedImageTransparency(transparency,iX);
	}
	public int getSynthesizedImageTransparency(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSynthesizedImageTransparency(iX);
	}
	//
	// SynthesizedImageRectangularBlobsMode
	//
	public void setSynthesizedImageRectangularBlobsMode(boolean mode, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.setSynthesizedImageRectangularBlobsMode(mode,iX);
	}
	public boolean getSynthesizedImageRectangularBlobsMode(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSynthesizedImageRectangularBlobsMode(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void subtract(long frameNumber, java.awt.image.BufferedImage nativeImage, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.subtract(frameNumber,nativeImage,takeFrameIntoAccount,iX,attributes);
	}
	public void subtract(long frameNumber, byte[] image, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.subtract(frameNumber,image,takeFrameIntoAccount,iX,attributes);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.commit(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void resetSettings(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.resetSettings(iX);
	}
	//
	public void resetStatistics(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.resetStatistics(iX);
	}
	//
	public void resetResults(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.resetResults(iX);
	}
	//
	public void resetAll(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.resetAll(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getFrameNumberOrSpacer(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getFrameNumberOrSpacer(iX);
	}
	public long getFrameNumber(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getFrameNumber(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentImage(Term image, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.getRecentImage(image,iX);
	}
	public byte[] getSerializedRecentImage(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedRecentImage(iX);
	}
	//
	public void getBackgroundImage(Term image, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.getBackgroundImage(image,iX);
	}
	public byte[] getSerializedBackgroundImage(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedBackgroundImage(iX);
	}
	//
	public void getSigmaImage(Term image, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.getSigmaImage(image,iX);
	}
	public byte[] getSerializedSigmaImage(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedSigmaImage(iX);
	}
	//
	public void getForegroundImage(Term image, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.getForegroundImage(image,iX);
	}
	public byte[] getSerializedForegroundImage(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedForegroundImage(iX);
	}
	//
	public void getSynthesizedImage(Term image, ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		subtractor.getSynthesizedImage(image,iX);
	}
	public byte[] getSerializedSynthesizedImage(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedSynthesizedImage(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getBlobs(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getBlobs(iX);
	}
	public byte[] getSerializedBlobs(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedBlobs(iX);
	}
	//
	public Term getTracks(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getTracks(iX);
	}
	public byte[] getSerializedTracks(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedTracks(iX);
	}
	//
	public Term getConnectedGraphs(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getConnectedGraphs(iX);
	}
	public byte[] getSerializedConnectedGraphs(ChoisePoint iX) {
		ImageSubtractorOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedConnectedGraphs(iX);
	}
}
