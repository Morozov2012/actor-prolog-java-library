// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.*;
import morozov.terms.*;
import morozov.worlds.*;

public abstract class GenericImageSubtractor
		extends BufferedImageController
		implements ImageSubtractorOperations {
	//
	public GenericImageSubtractor() {
	}
	public GenericImageSubtractor(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// BlobExtractionMode
	//
	public void setBlobExtractionMode1s(ChoisePoint iX, Term value) {
		setBlobExtractionMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setBlobExtractionMode(boolean mode, ChoisePoint iX);
	//
	public void getBlobExtractionMode0ff(ChoisePoint iX, PrologVariable result) {
		result.value= YesNo.boolean2TermYesNo(getBlobExtractionMode(iX));
	}
	public void getBlobExtractionMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getBlobExtractionMode(ChoisePoint iX);
	//
	// BlobTracingMode
	//
	public void setBlobTracingMode1s(ChoisePoint iX, Term value) {
		setBlobTracingMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setBlobTracingMode(boolean mode, ChoisePoint iX);
	//
	public void getBlobTracingMode0ff(ChoisePoint iX, PrologVariable result) {
		result.value= YesNo.boolean2TermYesNo(getBlobTracingMode(iX));
	}
	public void getBlobTracingMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getBlobTracingMode(ChoisePoint iX);
	//
	// MinimalTrainingInterval
	//
	public void setMinimalTrainingInterval1s(ChoisePoint iX, Term value) {
		setMinimalTrainingInterval(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMinimalTrainingInterval(int frames, ChoisePoint iX);
	//
	public void getMinimalTrainingInterval0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getMinimalTrainingInterval(iX));
	}
	public void getMinimalTrainingInterval0fs(ChoisePoint iX) {
	}
	abstract public int getMinimalTrainingInterval(ChoisePoint iX);
	//
	// MaximalTrainingInterval
	//
	public void setMaximalTrainingInterval1s(ChoisePoint iX, Term value) {
		setMaximalTrainingInterval(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMaximalTrainingInterval(int frames, ChoisePoint iX);
	//
	public void getMaximalTrainingInterval0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getMaximalTrainingInterval(iX));
	}
	public void getMaximalTrainingInterval0fs(ChoisePoint iX) {
	}
	abstract public int getMaximalTrainingInterval(ChoisePoint iX);
	//
	// GrayscaleMode
	//
	public void setGrayscaleMode1s(ChoisePoint iX, Term value) {
		setGrayscaleMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setGrayscaleMode(boolean mode, ChoisePoint iX);
	//
	public void getGrayscaleMode0ff(ChoisePoint iX, PrologVariable result) {
		result.value= YesNo.boolean2TermYesNo(getGrayscaleMode(iX));
	}
	public void getGrayscaleMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getGrayscaleMode(ChoisePoint iX);
	//
	// BackgroundGaussianFilteringMode
	//
	public void setBackgroundGaussianFilteringMode1s(ChoisePoint iX, Term value) {
		setBackgroundGaussianFilteringMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setBackgroundGaussianFilteringMode(boolean mode, ChoisePoint iX);
	//
	public void getBackgroundGaussianFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		result.value= YesNo.boolean2TermYesNo(getBackgroundGaussianFilteringMode(iX));
	}
	public void getBackgroundGaussianFilteringMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getBackgroundGaussianFilteringMode(ChoisePoint iX);
	//
	// BackgroundGaussianFilterRadius
	//
	public void setBackgroundGaussianFilterRadius1s(ChoisePoint iX, Term value) {
		setBackgroundGaussianFilterRadius(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setBackgroundGaussianFilterRadius(int radius, ChoisePoint iX);
	//
	public void getBackgroundGaussianFilterRadius0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getBackgroundGaussianFilterRadius(iX));
	}
	public void getBackgroundGaussianFilterRadius0fs(ChoisePoint iX) {
	}
	abstract public int getBackgroundGaussianFilterRadius(ChoisePoint iX);
	//
	// BackgroundRankFilteringMode
	//
	public void setBackgroundRankFilteringMode1s(ChoisePoint iX, Term value) {
		setBackgroundRankFilteringMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setBackgroundRankFilteringMode(boolean mode, ChoisePoint iX);
	//
	public void getBackgroundRankFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		result.value= YesNo.boolean2TermYesNo(getBackgroundRankFilteringMode(iX));
	}
	public void getBackgroundRankFilteringMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getBackgroundRankFilteringMode(ChoisePoint iX);
	//
	// BackgroundRankFilterThreshold
	//
	public void setBackgroundRankFilterThreshold1s(ChoisePoint iX, Term value) {
		setBackgroundRankFilterThreshold(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setBackgroundRankFilterThreshold(int threshold, ChoisePoint iX);
	//
	public void getBackgroundRankFilterThreshold0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getBackgroundRankFilterThreshold(iX));
	}
	public void getBackgroundRankFilterThreshold0fs(ChoisePoint iX) {
	}
	abstract public int getBackgroundRankFilterThreshold(ChoisePoint iX);
	//
	// BackgroundStandardDeviationFactor
	//
	public void setBackgroundStandardDeviationFactor1s(ChoisePoint iX, Term value) {
		setBackgroundStandardDeviationFactor(Converters.argumentToReal(value,iX),iX);
	}
	abstract public void setBackgroundStandardDeviationFactor(double factor, ChoisePoint iX);
	//
	public void getBackgroundStandardDeviationFactor0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologReal(getBackgroundStandardDeviationFactor(iX));
	}
	public void getBackgroundStandardDeviationFactor0fs(ChoisePoint iX) {
	}
	abstract public double getBackgroundStandardDeviationFactor(ChoisePoint iX);
	//
	// ForegroundContouringMode
	//
	public void setForegroundContouringMode1s(ChoisePoint iX, Term value) {
		setForegroundContouringMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setForegroundContouringMode(boolean mode, ChoisePoint iX);
	//
	public void getForegroundContouringMode0ff(ChoisePoint iX, PrologVariable result) {
		result.value= YesNo.boolean2TermYesNo(getForegroundContouringMode(iX));
	}
	public void getForegroundContouringMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getForegroundContouringMode(ChoisePoint iX);
	//
	// R2WindowHalfwidth
	//
	public void setR2WindowHalfwidth1s(ChoisePoint iX, Term value) {
		setR2WindowHalfwidth(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setR2WindowHalfwidth(int halfwidth, ChoisePoint iX);
	//
	public void getR2WindowHalfwidth0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getR2WindowHalfwidth(iX));
	}
	public void getR2WindowHalfwidth0fs(ChoisePoint iX) {
	}
	abstract public int getR2WindowHalfwidth(ChoisePoint iX);
	//
	// HorizontalBlobBorder
	//
	public void setHorizontalBlobBorder1s(ChoisePoint iX, Term value) {
		setHorizontalBlobBorder(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setHorizontalBlobBorder(int size, ChoisePoint iX);
	//
	public void getHorizontalBlobBorder0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getHorizontalBlobBorder(iX));
	}
	public void getHorizontalBlobBorder0fs(ChoisePoint iX) {
	}
	abstract public int getHorizontalBlobBorder(ChoisePoint iX);
	//
	// VerticalBlobBorder
	//
	public void setVerticalBlobBorder1s(ChoisePoint iX, Term value) {
		setVerticalBlobBorder(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setVerticalBlobBorder(int size, ChoisePoint iX);
	//
	public void getVerticalBlobBorder0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getVerticalBlobBorder(iX));
	}
	public void getVerticalBlobBorder0fs(ChoisePoint iX) {
	}
	abstract public int getVerticalBlobBorder(ChoisePoint iX);
	//
	// HorizontalExtraBorderCoefficient
	//
	public void setHorizontalExtraBorderCoefficient1s(ChoisePoint iX, Term value) {
		setHorizontalExtraBorderCoefficient(Converters.argumentToReal(value,iX),iX);
	}
	abstract public void setHorizontalExtraBorderCoefficient(double coefficient, ChoisePoint iX);
	//
	public void getHorizontalExtraBorderCoefficient0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologReal(getHorizontalExtraBorderCoefficient(iX));
	}
	public void getHorizontalExtraBorderCoefficient0fs(ChoisePoint iX) {
	}
	abstract public double getHorizontalExtraBorderCoefficient(ChoisePoint iX);
	//
	// VerticalExtraBorderCoefficient
	//
	public void setVerticalExtraBorderCoefficient1s(ChoisePoint iX, Term value) {
		setVerticalExtraBorderCoefficient(Converters.argumentToReal(value,iX),iX);
	}
	abstract public void setVerticalExtraBorderCoefficient(double coefficient, ChoisePoint iX);
	//
	public void getVerticalExtraBorderCoefficient0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologReal(getVerticalExtraBorderCoefficient(iX));
	}
	public void getVerticalExtraBorderCoefficient0fs(ChoisePoint iX) {
	}
	abstract public double getVerticalExtraBorderCoefficient(ChoisePoint iX);
	//
	// MinimalBlobIntersectionArea
	//
	public void setMinimalBlobIntersectionArea1s(ChoisePoint iX, Term value) {
		setMinimalBlobIntersectionArea(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMinimalBlobIntersectionArea(int size, ChoisePoint iX);
	//
	public void getMinimalBlobIntersectionArea0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getMinimalBlobIntersectionArea(iX));
	}
	public void getMinimalBlobIntersectionArea0fs(ChoisePoint iX) {
	}
	abstract public int getMinimalBlobIntersectionArea(ChoisePoint iX);
	//
	// MinimalBlobSize
	//
	public void setMinimalBlobSize1s(ChoisePoint iX, Term value) {
		setMinimalBlobSize(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMinimalBlobSize(int size, ChoisePoint iX);
	//
	public void getMinimalBlobSize0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getMinimalBlobSize(iX));
	}
	public void getMinimalBlobSize0fs(ChoisePoint iX) {
	}
	abstract public int getMinimalBlobSize(ChoisePoint iX);
	//
	// MinimalTrackDuration
	//
	public void setMinimalTrackDuration1s(ChoisePoint iX, Term value) {
		setMinimalTrackDuration(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMinimalTrackDuration(int frames, ChoisePoint iX);
	//
	public void getMinimalTrackDuration0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getMinimalTrackDuration(iX));
	}
	public void getMinimalTrackDuration0fs(ChoisePoint iX) {
	}
	abstract public int getMinimalTrackDuration(ChoisePoint iX);
	//
	// MaximalBlobInvisibilityInterval
	//
	public void setMaximalBlobInvisibilityInterval1s(ChoisePoint iX, Term value) {
		setMaximalBlobInvisibilityInterval(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMaximalBlobInvisibilityInterval(int frames, ChoisePoint iX);
	//
	public void getMaximalBlobInvisibilityInterval0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getMaximalBlobInvisibilityInterval(iX));
	}
	public void getMaximalBlobInvisibilityInterval0fs(ChoisePoint iX) {
	}
	abstract public int getMaximalBlobInvisibilityInterval(ChoisePoint iX);
	//
	// MaximalTrackRetentionInterval
	//
	public void setMaximalTrackRetentionInterval1s(ChoisePoint iX, Term value) {
		setMaximalTrackRetentionInterval(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMaximalTrackRetentionInterval(int frames, ChoisePoint iX);
	//
	public void getMaximalTrackRetentionInterval0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getMaximalTrackRetentionInterval(iX));
	}
	public void getMaximalTrackRetentionInterval0fs(ChoisePoint iX) {
	}
	abstract public int getMaximalTrackRetentionInterval(ChoisePoint iX);
	//
	// InverseTransformationMatrix
	//
	public void setInverseTransformationMatrix1s(ChoisePoint iX, Term value) {
		setInverseTransformationMatrix(Converters.argumentToMatrix(value,iX),iX);
	}
	abstract public void setInverseTransformationMatrix(double[][] matrix, ChoisePoint iX);
	abstract public void setSerializedInverseTransformationMatrix(byte[] matrix, ChoisePoint iX);
	//
	public void getInverseTransformationMatrix0ff(ChoisePoint iX, PrologVariable result) {
		result.value= Converters.doubleMatrixToListOfList(getInverseTransformationMatrix(iX));
	}
	public void getInverseTransformationMatrix0fs(ChoisePoint iX) {
	}
	abstract public double[][] getInverseTransformationMatrix(ChoisePoint iX);
	abstract public byte[] getSerializedInverseTransformationMatrix(ChoisePoint iX);
	//
	// SamplingRate
	//
	public void setSamplingRate1s(ChoisePoint iX, Term value) {
		setSamplingRate(Converters.argumentToReal(value,iX),iX);
	}
	abstract public void setSamplingRate(double rate, ChoisePoint iX);
	//
	public void getSamplingRate0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologReal(getSamplingRate(iX));
	}
	public void getSamplingRate0fs(ChoisePoint iX) {
	}
	abstract public double getSamplingRate(ChoisePoint iX);
	//
	// CharacteristicLengthMedianFilteringMode
	//
	public void setCharacteristicLengthMedianFilteringMode1s(ChoisePoint iX, Term value) {
		setCharacteristicLengthMedianFilteringMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setCharacteristicLengthMedianFilteringMode(boolean mode, ChoisePoint iX);
	//
	public void getCharacteristicLengthMedianFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		result.value= YesNo.boolean2TermYesNo(getCharacteristicLengthMedianFilteringMode(iX));
	}
	public void getCharacteristicLengthMedianFilteringMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getCharacteristicLengthMedianFilteringMode(ChoisePoint iX);
	//
	// CharacteristicLengthMedianFilterHalfwidth
	//
	public void setCharacteristicLengthMedianFilterHalfwidth1s(ChoisePoint iX, Term value) {
		setCharacteristicLengthMedianFilterHalfwidth(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setCharacteristicLengthMedianFilterHalfwidth(int halfwidth, ChoisePoint iX);
	//
	public void getCharacteristicLengthMedianFilterHalfwidth0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getCharacteristicLengthMedianFilterHalfwidth(iX));
	}
	public void getCharacteristicLengthMedianFilterHalfwidth0fs(ChoisePoint iX) {
	}
	abstract public int getCharacteristicLengthMedianFilterHalfwidth(ChoisePoint iX);
	//
	// VelocityMedianFilteringMode
	//
	public void setVelocityMedianFilteringMode1s(ChoisePoint iX, Term value) {
		setVelocityMedianFilteringMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setVelocityMedianFilteringMode(boolean mode, ChoisePoint iX);
	//
	public void getVelocityMedianFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		result.value= YesNo.boolean2TermYesNo(getVelocityMedianFilteringMode(iX));
	}
	public void getVelocityMedianFilteringMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getVelocityMedianFilteringMode(ChoisePoint iX);
	//
	// VelocityMedianFilterHalfwidth
	//
	public void setVelocityMedianFilterHalfwidth1s(ChoisePoint iX, Term value) {
		setVelocityMedianFilterHalfwidth(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setVelocityMedianFilterHalfwidth(int threshold, ChoisePoint iX);
	//
	public void getVelocityMedianFilterHalfwidth0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getVelocityMedianFilterHalfwidth(iX));
	}
	public void getVelocityMedianFilterHalfwidth0fs(ChoisePoint iX) {
	}
	abstract public int getVelocityMedianFilterHalfwidth(ChoisePoint iX);
	//
	// SlowTracksDeletionMode
	//
	public void setSlowTracksDeletionMode1s(ChoisePoint iX, Term value) {
		setSlowTracksDeletionMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setSlowTracksDeletionMode(boolean mode, ChoisePoint iX);
	//
	public void getSlowTracksDeletionMode0ff(ChoisePoint iX, PrologVariable result) {
		result.value= YesNo.boolean2TermYesNo(getSlowTracksDeletionMode(iX));
	}
	public void getSlowTracksDeletionMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getSlowTracksDeletionMode(ChoisePoint iX);
	//
	// FuzzyVelocityThreshold
	//
	public void setFuzzyVelocityThreshold1s(ChoisePoint iX, Term value) {
		setFuzzyVelocityThreshold(Converters.argumentToReal(value,iX),iX);
	}
	abstract public void setFuzzyVelocityThreshold(double threshold, ChoisePoint iX);
	//
	public void getFuzzyVelocityThreshold0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologReal(getFuzzyVelocityThreshold(iX));
	}
	public void getFuzzyVelocityThreshold0fs(ChoisePoint iX) {
	}
	abstract public double getFuzzyVelocityThreshold(ChoisePoint iX);
	//
	// FuzzyDistanceThreshold
	//
	public void setFuzzyDistanceThreshold1s(ChoisePoint iX, Term value) {
		setFuzzyDistanceThreshold(Converters.argumentToReal(value,iX),iX);
	}
	abstract public void setFuzzyDistanceThreshold(double threshold, ChoisePoint iX);
	//
	public void getFuzzyDistanceThreshold0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologReal(getFuzzyDistanceThreshold(iX));
	}
	public void getFuzzyDistanceThreshold0fs(ChoisePoint iX) {
	}
	abstract public double getFuzzyDistanceThreshold(ChoisePoint iX);
	//
	// FuzzyThresholdBorder
	//
	public void setFuzzyThresholdBorder1s(ChoisePoint iX, Term value) {
		setFuzzyThresholdBorder(Converters.argumentToReal(value,iX),iX);
	}
	abstract public void setFuzzyThresholdBorder(double size, ChoisePoint iX);
	//
	public void getFuzzyThresholdBorder0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologReal(getFuzzyThresholdBorder(iX));
	}
	public void getFuzzyThresholdBorder0fs(ChoisePoint iX) {
	}
	abstract public double getFuzzyThresholdBorder(ChoisePoint iX);
	//
	// SynthesizedImageTransparency
	//
	public void setSynthesizedImageTransparency1s(ChoisePoint iX, Term value) {
		setSynthesizedImageTransparency(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setSynthesizedImageTransparency(int transparency, ChoisePoint iX);
	//
	public void getSynthesizedImageTransparency0ff(ChoisePoint iX, PrologVariable result) {
		result.value= new PrologInteger(getSynthesizedImageTransparency(iX));
	}
	public void getSynthesizedImageTransparency0fs(ChoisePoint iX) {
	}
	abstract public int getSynthesizedImageTransparency(ChoisePoint iX);
	//
	// SynthesizedImageRectangularBlobsMode
	//
	public void setSynthesizedImageRectangularBlobsMode1s(ChoisePoint iX, Term value) {
		setSynthesizedImageRectangularBlobsMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setSynthesizedImageRectangularBlobsMode(boolean mode, ChoisePoint iX);
	//
	public void getSynthesizedImageRectangularBlobsMode0ff(ChoisePoint iX, PrologVariable result) {
		result.value= YesNo.boolean2TermYesNo(getSynthesizedImageRectangularBlobsMode(iX));
	}
	public void getSynthesizedImageRectangularBlobsMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getSynthesizedImageRectangularBlobsMode(ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public void subtract2s(ChoisePoint iX, Term a1, Term a2) {
		subtract(a1,a2,true,iX);
	}
	public void subtract3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		boolean takeFrameIntoAccount= YesNo.termYesNo2Boolean(a3,iX);
		subtract(a1,a2,takeFrameIntoAccount,iX);
	}
	//
	protected void subtract(Term frame, Term image, boolean takeFrameIntoAccount, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		long frameNumber= PrologInteger.toLong(Converters.argumentToRoundInteger(frame,iX));
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(image,iX);
		GenericImageEncodingAttributes attributes= getCurrentImageEncodingAttributes();
		subtract(frameNumber,nativeImage,takeFrameIntoAccount,iX,attributes);
	}
	abstract public void subtract(long frameNumber, java.awt.image.BufferedImage nativeImage, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes);
	//
	public boolean createImageSubtractorIfNecessary(ChoisePoint iX) {
		GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
		setCurrentImageEncodingAttributes(attributes);
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit0s(ChoisePoint iX) {
		commit(iX);
	}
	abstract public void commit(ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public void resetSettings0s(ChoisePoint iX) {
		resetSettings(iX);
	}
	abstract public void resetSettings(ChoisePoint iX);
	//
	public void resetStatistics0s(ChoisePoint iX) {
		resetStatistics(iX);
	}
	abstract public void resetStatistics(ChoisePoint iX);
	//
	public void resetResults0s(ChoisePoint iX) {
		resetResults(iX);
	}
	abstract public void resetResults(ChoisePoint iX);
	//
	public void resetAll0s(ChoisePoint iX) {
		resetAll(iX);
	}
	abstract public void resetAll(ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentFrameNumber1s(ChoisePoint iX, PrologVariable frameNumber) {
		frameNumber.value= getFrameNumberOrSpacer(iX);
	}
	abstract public Term getFrameNumberOrSpacer(ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentImage1s(ChoisePoint iX, Term image) {
		getRecentImage(image,iX);
	}
	abstract public void getRecentImage(Term image, ChoisePoint iX);
	abstract public byte[] getSerializedRecentImage(ChoisePoint iX);
	//
	public void getBackgroundImage1s(ChoisePoint iX, Term image) {
		getBackgroundImage(image,iX);
	}
	abstract public void getBackgroundImage(Term image, ChoisePoint iX);
	abstract public byte[] getSerializedBackgroundImage(ChoisePoint iX);
	//
	public void getSigmaImage1s(ChoisePoint iX, Term image) {
		getSigmaImage(image,iX);
	}
	abstract public void getSigmaImage(Term image, ChoisePoint iX);
	abstract public byte[] getSerializedSigmaImage(ChoisePoint iX);
	//
	public void getForegroundImage1s(ChoisePoint iX, Term image) {
		getForegroundImage(image,iX);
	}
	abstract public void getForegroundImage(Term image, ChoisePoint iX);
	abstract public byte[] getSerializedForegroundImage(ChoisePoint iX);
	//
	public void getSynthesizedImage1s(ChoisePoint iX, Term image) {
		getSynthesizedImage(image,iX);
	}
	abstract public void getSynthesizedImage(Term image, ChoisePoint iX);
	abstract public byte[] getSerializedSynthesizedImage(ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public void getBlobs1s(ChoisePoint iX, PrologVariable blobs) {
		blobs.value= getBlobs(iX);
	}
	abstract public Term getBlobs(ChoisePoint iX);
	abstract public byte[] getSerializedBlobs(ChoisePoint iX);
	//
	public void getTracks1s(ChoisePoint iX, PrologVariable tracks) {
		tracks.value= getTracks(iX);
	}
	abstract public Term getTracks(ChoisePoint iX);
	abstract public byte[] getSerializedTracks(ChoisePoint iX);
	//
	public void getConnectedGraphs1s(ChoisePoint iX, PrologVariable graphs) {
		graphs.value= getConnectedGraphs(iX);
	}
	abstract public Term getConnectedGraphs(ChoisePoint iX);
	abstract public byte[] getSerializedConnectedGraphs(ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		return this;
	}
}
