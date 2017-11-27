// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.plain.*;
import morozov.system.vision.vpm.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.concurrent.atomic.AtomicReference;

public abstract class ImageSubtractor
		extends BlobProcessingAttributes
		implements VideoProcessingMachineOperations {
	//
	protected AtomicReference<PlainImageSubtractor> imageSubtractor= new AtomicReference<>(null);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_extract_blobs();
	abstract public Term getBuiltInSlot_E_track_blobs();
	abstract public Term getBuiltInSlot_E_use_grayscale_colors();
	abstract public Term getBuiltInSlot_E_apply_gaussian_filtering_to_background();
	abstract public Term getBuiltInSlot_E_background_gaussian_filter_radius();
	abstract public Term getBuiltInSlot_E_apply_rank_filtering_to_background();
	abstract public Term getBuiltInSlot_E_background_rank_filter_threshold();
	abstract public Term getBuiltInSlot_E_background_standard_deviation_factor();
	abstract public Term getBuiltInSlot_E_contour_foreground();
	abstract public Term getBuiltInSlot_E_refuse_slow_tracks();
	abstract public Term getBuiltInSlot_E_fuzzy_velocity_threshold();
	abstract public Term getBuiltInSlot_E_fuzzy_distance_threshold();
	abstract public Term getBuiltInSlot_E_fuzzy_threshold_border();
	//
	///////////////////////////////////////////////////////////////
	//
	public ImageSubtractor() {
	}
	public ImageSubtractor(GlobalWorldIdentifier id) {
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
	public void setBlobExtractionMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBlobExtractionMode(mode);
	}
	//
	public void getBlobExtractionMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(getBlobExtractionMode(iX)));
	}
	public void getBlobExtractionMode0fs(ChoisePoint iX) {
	}
	public boolean getBlobExtractionMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBlobExtractionMode();
	}
	//
	// BlobTracingMode
	//
	public void setBlobTracingMode1s(ChoisePoint iX, Term value) {
		setBlobTracingMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	public void setBlobTracingMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBlobTracingMode(mode);
	}
	//
	public void getBlobTracingMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(getBlobTracingMode(iX)));
	}
	public void getBlobTracingMode0fs(ChoisePoint iX) {
	}
	public boolean getBlobTracingMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBlobTracingMode();
	}
	//
	// MinimalTrainingInterval
	//
	public void setMinimalTrainingInterval(int frames, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setMinimalTrainingInterval(frames);
	}
	public int getMinimalTrainingInterval(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getMinimalTrainingInterval();
	}
	//
	// MaximalTrainingInterval
	//
	public void setMaximalTrainingInterval(int frames, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setMaximalTrainingInterval(frames);
	}
	public int getMaximalTrainingInterval(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getMaximalTrainingInterval();
	}
	//
	// GrayscaleMode
	//
	public void setGrayscaleMode1s(ChoisePoint iX, Term value) {
		setGrayscaleMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	public void setGrayscaleMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setGrayscaleMode(mode);
	}
	//
	public void getGrayscaleMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(getGrayscaleMode(iX)));
	}
	public void getGrayscaleMode0fs(ChoisePoint iX) {
	}
	public boolean getGrayscaleMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getGrayscaleMode();
	}
	//
	// BackgroundGaussianFilteringMode
	//
	public void setBackgroundGaussianFilteringMode1s(ChoisePoint iX, Term value) {
		setBackgroundGaussianFilteringMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	public void setBackgroundGaussianFilteringMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBackgroundGaussianFilteringMode(mode);
	}
	//
	public void getBackgroundGaussianFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(getBackgroundGaussianFilteringMode(iX)));
	}
	public void getBackgroundGaussianFilteringMode0fs(ChoisePoint iX) {
	}
	public boolean getBackgroundGaussianFilteringMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBackgroundGaussianFilteringMode();
	}
	//
	// BackgroundGaussianFilterRadius
	//
	public void setBackgroundGaussianFilterRadius1s(ChoisePoint iX, Term value) {
		setBackgroundGaussianFilterRadius(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	public void setBackgroundGaussianFilterRadius(int radius, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBackgroundGaussianFilterRadius(radius);
	}
	//
	public void getBackgroundGaussianFilterRadius0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getBackgroundGaussianFilterRadius(iX)));
	}
	public void getBackgroundGaussianFilterRadius0fs(ChoisePoint iX) {
	}
	public int getBackgroundGaussianFilterRadius(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBackgroundGaussianFilterRadius();
	}
	//
	// BackgroundRankFilteringMode
	//
	public void setBackgroundRankFilteringMode1s(ChoisePoint iX, Term value) {
		setBackgroundRankFilteringMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	public void setBackgroundRankFilteringMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBackgroundRankFilteringMode(mode);
	}
	//
	public void getBackgroundRankFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(getBackgroundRankFilteringMode(iX)));
	}
	public void getBackgroundRankFilteringMode0fs(ChoisePoint iX) {
	}
	public boolean getBackgroundRankFilteringMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBackgroundRankFilteringMode();
	}
	//
	// BackgroundRankFilterThreshold
	//
	public void setBackgroundRankFilterThreshold1s(ChoisePoint iX, Term value) {
		setBackgroundRankFilterThreshold(Converters.argumentToSmallRoundInteger(value,iX),iX);
	}
	public void setBackgroundRankFilterThreshold(int threshold, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBackgroundRankFilterThreshold(threshold);
	}
	//
	public void getBackgroundRankFilterThreshold0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getBackgroundRankFilterThreshold(iX)));
	}
	public void getBackgroundRankFilterThreshold0fs(ChoisePoint iX) {
	}
	public int getBackgroundRankFilterThreshold(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBackgroundRankFilterThreshold();
	}
	//
	// BackgroundStandardDeviationFactor
	//
	public void setBackgroundStandardDeviationFactor1s(ChoisePoint iX, Term value) {
		setBackgroundStandardDeviationFactor(Converters.argumentToReal(value,iX),iX);
	}
	public void setBackgroundStandardDeviationFactor(double factor, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBackgroundStandardDeviationFactor(factor);
	}
	//
	public void getBackgroundStandardDeviationFactor0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologReal(getBackgroundStandardDeviationFactor(iX)));
	}
	public void getBackgroundStandardDeviationFactor0fs(ChoisePoint iX) {
	}
	public double getBackgroundStandardDeviationFactor(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBackgroundStandardDeviationFactor();
	}
	//
	// ForegroundContouringMode
	//
	public void setForegroundContouringMode1s(ChoisePoint iX, Term value) {
		setForegroundContouringMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	public void setForegroundContouringMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setForegroundContouringMode(mode);
	}
	//
	public void getForegroundContouringMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(getForegroundContouringMode(iX)));
	}
	public void getForegroundContouringMode0fs(ChoisePoint iX) {
	}
	public boolean getForegroundContouringMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getForegroundContouringMode();
	}
	//
	// HorizontalBlobBorder
	//
	public void setHorizontalBlobBorder(int size, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setHorizontalBlobBorder(size);
	}
	public int getHorizontalBlobBorder(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getHorizontalBlobBorder();
	}
	//
	// VerticalBlobBorder
	//
	public void setVerticalBlobBorder(int size, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setVerticalBlobBorder(size);
	}
	public int getVerticalBlobBorder(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getVerticalBlobBorder();
	}
	//
	// HorizontalExtraBorderCoefficient
	//
	public void setHorizontalExtraBorderCoefficient(double coefficient, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setHorizontalExtraBorderCoefficient(coefficient);
	}
	public double getHorizontalExtraBorderCoefficient(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getHorizontalExtraBorderCoefficient();
	}
	//
	// VerticalExtraBorderCoefficient
	//
	public void setVerticalExtraBorderCoefficient(double coefficient, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setVerticalExtraBorderCoefficient(coefficient);
	}
	public double getVerticalExtraBorderCoefficient(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getVerticalExtraBorderCoefficient();
	}
	//
	// MinimalBlobIntersectionArea
	//
	public void setMinimalBlobIntersectionArea(int size, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setMinimalBlobIntersectionArea(size);
	}
	public int getMinimalBlobIntersectionArea(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getMinimalBlobIntersectionArea();
	}
	//
	// MinimalBlobSize
	//
	public void setMinimalBlobSize(int size, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setMinimalBlobSize(size);
	}
	public int getMinimalBlobSize(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getMinimalBlobSize();
	}
	//
	// MinimalTrackDuration
	//
	public void setMinimalTrackDuration(int frames, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setMinimalTrackDuration(frames);
	}
	public int getMinimalTrackDuration(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getMinimalTrackDuration();
	}
	//
	// MaximalTrackDuration
	//
	public void setMaximalTrackDuration(int frames, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setMaximalTrackDuration(frames);
	}
	public int getMaximalTrackDuration(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getMaximalTrackDuration();
	}
	//
	// MaximalChronicleLength
	//
	public void setMaximalChronicleLength(NumericalValue value, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setMaximalChronicleLength(value);
	}
	public NumericalValue getMaximalChronicleLength(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getMaximalChronicleLength();
	}
	//
	// MaximalBlobInvisibilityInterval
	//
	public void setMaximalBlobInvisibilityInterval(int frames, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setMaximalBlobInvisibilityInterval(frames);
	}
	public int getMaximalBlobInvisibilityInterval(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getMaximalBlobInvisibilityInterval();
	}
	//
	// MaximalTrackRetentionInterval
	//
	public void setMaximalTrackRetentionInterval(int frames, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setMaximalTrackRetentionInterval(frames);
	}
	public int getMaximalTrackRetentionInterval(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getMaximalTrackRetentionInterval();
	}
	//
	// InverseTransformationMatrix
	//
	public void setInverseTransformationMatrix(double[][] matrix, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setInverseTransformationMatrix(matrix);
	}
	// public void setSerializedInverseTransformationMatrix(byte[] bytes, ChoisePoint iX) {
	//	createImageSubtractorIfNecessary(iX);
	//	PlainImageSubtractor subtractor= imageSubtractor.get();
	//	double[][] matrix= Converters.deserializeMatrix(bytes);
	//	subtractor.setInverseTransformationMatrix(matrix);
	// }
	//
	public double[][] getInverseTransformationMatrix(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getInverseTransformationMatrix();
	}
	// public byte[] getSerializedInverseTransformationMatrix(ChoisePoint iX) {
	//	createImageSubtractorIfNecessary(iX);
	//	PlainImageSubtractor subtractor= imageSubtractor.get();
	//	double[][] matrix= getInverseTransformationMatrix();
	//	return Converters.serializeMatrix(matrix);
	// }
	//
	// SamplingRate
	//
	public void setSamplingRate(double rate, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setSamplingRate(rate);
	}
	public double getSamplingRate(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getSamplingRate();
	}
	//
	// R2WindowHalfwidth
	//
	public void setR2WindowHalfwidth(int halfwidth, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setR2WindowHalfwidth(halfwidth);
	}
	public int getR2WindowHalfwidth(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getR2WindowHalfwidth();
	}
	//
	// CharacteristicLengthMedianFilteringMode
	//
	public void setCharacteristicLengthMedianFilteringMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setCharacteristicLengthMedianFilteringMode(mode);
	}
	public boolean getCharacteristicLengthMedianFilteringMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getCharacteristicLengthMedianFilteringMode();
	}
	//
	// CharacteristicLengthMedianFilterHalfwidth
	//
	public void setCharacteristicLengthMedianFilterHalfwidth(int halfwidth, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setCharacteristicLengthMedianFilterHalfwidth(halfwidth);
	}
	public int getCharacteristicLengthMedianFilterHalfwidth(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getCharacteristicLengthMedianFilterHalfwidth();
	}
	//
	// VelocityMedianFilteringMode
	//
	public void setVelocityMedianFilteringMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setVelocityMedianFilteringMode(mode);
	}
	public boolean getVelocityMedianFilteringMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getVelocityMedianFilteringMode();
	}
	//
	// VelocityMedianFilterHalfwidth
	//
	public void setVelocityMedianFilterHalfwidth(int halfwidth, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setVelocityMedianFilterHalfwidth(halfwidth);
	}
	public int getVelocityMedianFilterHalfwidth(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getVelocityMedianFilterHalfwidth();
	}
	//
	// SlowTracksDeletionMode
	//
	public void setSlowTracksDeletionMode1s(ChoisePoint iX, Term value) {
		setSlowTracksDeletionMode(YesNo.termYesNo2Boolean(value,iX),iX);
	}
	public void setSlowTracksDeletionMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setSlowTracksDeletionMode(mode);
	}
	//
	public void getSlowTracksDeletionMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(getSlowTracksDeletionMode(iX)));
	}
	public void getSlowTracksDeletionMode0fs(ChoisePoint iX) {
	}
	public boolean getSlowTracksDeletionMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getSlowTracksDeletionMode();
	}
	//
	// FuzzyVelocityThreshold
	//
	public void setFuzzyVelocityThreshold1s(ChoisePoint iX, Term value) {
		setFuzzyVelocityThreshold(Converters.argumentToReal(value,iX),iX);
	}
	public void setFuzzyVelocityThreshold(double threshold, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setFuzzyVelocityThreshold(threshold);
	}
	//
	public void getFuzzyVelocityThreshold0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologReal(getFuzzyVelocityThreshold(iX)));
	}
	public void getFuzzyVelocityThreshold0fs(ChoisePoint iX) {
	}
	public double getFuzzyVelocityThreshold(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getFuzzyVelocityThreshold();
	}
	//
	// FuzzyDistanceThreshold
	//
	public void setFuzzyDistanceThreshold1s(ChoisePoint iX, Term value) {
		setFuzzyDistanceThreshold(Converters.argumentToReal(value,iX),iX);
	}
	public void setFuzzyDistanceThreshold(double threshold, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setFuzzyDistanceThreshold(threshold);
	}
	//
	public void getFuzzyDistanceThreshold0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologReal(getFuzzyDistanceThreshold(iX)));
	}
	public void getFuzzyDistanceThreshold0fs(ChoisePoint iX) {
	}
	public double getFuzzyDistanceThreshold(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getFuzzyDistanceThreshold();
	}
	//
	// FuzzyThresholdBorder
	//
	public void setFuzzyThresholdBorder1s(ChoisePoint iX, Term value) {
		setFuzzyThresholdBorder(Converters.argumentToReal(value,iX),iX);
	}
	public void setFuzzyThresholdBorder(double size, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setFuzzyThresholdBorder(size);
	}
	//
	public void getFuzzyThresholdBorder0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologReal(getFuzzyThresholdBorder(iX)));
	}
	public void getFuzzyThresholdBorder0fs(ChoisePoint iX) {
	}
	public double getFuzzyThresholdBorder(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getFuzzyThresholdBorder();
	}
	//
	// SynthesizedImageTransparency
	//
	public void setSynthesizedImageTransparency(int transparency, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setSynthesizedImageTransparency(transparency);
	}
	public int getSynthesizedImageTransparency(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getSynthesizedImageTransparency();
	}
	//
	// SynthesizedImageRectangularBlobsMode
	//
	public void setSynthesizedImageRectangularBlobsMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setSynthesizedImageRectangularBlobsMode(mode);
	}
	public boolean getSynthesizedImageRectangularBlobsMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getSynthesizedImageRectangularBlobsMode();
	}
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
	// abstract public void subtract(long frameNumber, java.awt.image.BufferedImage nativeImage, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes);
	//
	public void subtract(long frameNumber, java.awt.image.BufferedImage nativeImage, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		createImageSubtractorIfNecessary(iX);
		if (nativeImage != null) {
			PlainImageSubtractor subtractor= imageSubtractor.get();
			// subtractor.subtractImageAndAnalyseBlobs(frameNumber,nativeImage,takeFrameIntoAccount);
			subtractor.processImage(nativeImage,frameNumber,-1,takeFrameIntoAccount);
		}
	}
	public void subtract(long frameNumber, byte[] bytes, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= Space2DWriter.bytesToImage(bytes);
		if (nativeImage != null) {
			PlainImageSubtractor subtractor= imageSubtractor.get();
			// subtractor.subtractImageAndAnalyseBlobs(frameNumber,nativeImage,takeFrameIntoAccount);
			subtractor.processImage(nativeImage,frameNumber,-1,takeFrameIntoAccount);
		}
	}
	//
	public void process(java.awt.image.BufferedImage nativeImage, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		createImageSubtractorIfNecessary(iX);
		if (nativeImage != null) {
			PlainImageSubtractor subtractor= imageSubtractor.get();
			subtractor.processImage(nativeImage,frameNumber,timeInMilliseconds,takeFrameIntoAccount);
		}
	}
	public void process(byte[] bytes, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= Space2DWriter.bytesToImage(bytes);
		if (nativeImage != null) {
			PlainImageSubtractor subtractor= imageSubtractor.get();
			subtractor.processImage(nativeImage,frameNumber,timeInMilliseconds,takeFrameIntoAccount);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean createImageSubtractorIfNecessary(ChoisePoint iX) {
		if (imageSubtractor.get()==null) {
			synchronized (this) {
				return createImageSubtractor(iX);
			}
		} else {
			return false;
		}
	}
	protected boolean createImageSubtractor(ChoisePoint iX) {
		if (imageSubtractor.get()==null) {
			GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
			setCurrentImageEncodingAttributes(attributes);
			boolean extractBlobs= YesNo.termYesNo2Boolean(getBuiltInSlot_E_extract_blobs(),iX);
			boolean trackBlobs= YesNo.termYesNo2Boolean(getBuiltInSlot_E_track_blobs(),iX);
			int minimalTrainingInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_training_interval(),iX);
			int maximalTrainingInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_training_interval(),iX);
			boolean useGrayscaleColors= YesNo.termYesNo2Boolean(getBuiltInSlot_E_use_grayscale_colors(),iX);
			boolean applyGaussian_2D_Filtering= YesNo.termYesNo2Boolean(getBuiltInSlot_E_apply_gaussian_filtering_to_background(),iX);
			int gaussian_2D_FilterRadius= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_background_gaussian_filter_radius(),iX);
			boolean applyRank_2D_Filtering= YesNo.termYesNo2Boolean(getBuiltInSlot_E_apply_rank_filtering_to_background(),iX);
			int rank_2D_FilterThreshold= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_background_rank_filter_threshold(),iX);
			double backgroundStandardDeviationFactor= Converters.argumentToReal(getBuiltInSlot_E_background_standard_deviation_factor(),iX);
			boolean contourForeground= YesNo.termYesNo2Boolean(getBuiltInSlot_E_contour_foreground(),iX);
			int horizontalBlobBorder= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_horizontal_blob_border(),iX);
			int verticalBlobBorder= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_vertical_blob_border(),iX);
			double horizontalExtraBorderCoefficient= Converters.argumentToReal(getBuiltInSlot_E_horizontal_extra_border_coefficient(),iX);
			double verticalExtraBorderCoefficient= Converters.argumentToReal(getBuiltInSlot_E_vertical_extra_border_coefficient(),iX);
			int minimalBlobIntersectionArea= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_intersection_area(),iX);
			int minimalBlobSize= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_size(),iX);
			int minimalTrackDuration= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_track_duration(),iX);
			int maximalTrackDuration= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_track_duration(),iX);
			NumericalValue maximalChronicleLength= NumericalValue.argumentToNumericalValue(getBuiltInSlot_E_maximal_chronicle_length(),iX);
			int maximalBlobInvisibilityInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_blob_invisibility_interval(),iX);
			int maximalTrackRetentionInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_track_retention_interval(),iX);
			Term matrixValue= getBuiltInSlot_E_inverse_transformation_matrix();
			double[][] inverseMatrix= Converters.argumentToMatrix(matrixValue,iX);
			checkMatrix(inverseMatrix,matrixValue);
			double samplingRate= Converters.argumentToReal(getBuiltInSlot_E_sampling_rate(),iX);
			int r2WindowHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_r2_window_halfwidth(),iX);
			boolean applyCharacteristicLengthMedianFiltering= YesNo.termYesNo2Boolean(getBuiltInSlot_E_apply_median_filtering_to_characteristic_length(),iX);
			int characteristicLengthMedianFilterHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_characteristic_length_median_filter_halfwidth(),iX);
			boolean applyVelocityMedianFiltering= YesNo.termYesNo2Boolean(getBuiltInSlot_E_apply_median_filtering_to_velocity(),iX);
			int velocityMedianFilterHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_velocity_median_filter_halfwidth(),iX);
			boolean refuseSlowTracks= YesNo.termYesNo2Boolean(getBuiltInSlot_E_refuse_slow_tracks(),iX);
			double velocityThreshold= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_velocity_threshold(),iX);
			double distanceThreshold= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_distance_threshold(),iX);
			double fuzzyThresholdBorder= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_threshold_border(),iX);
			int synthesizedImageTransparency= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_synthesized_image_transparency(),iX);
			boolean makeRectangularBlobsInSynthesizedImage= YesNo.termYesNo2Boolean(getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image(),iX);
			imageSubtractor.set(
				new PlainImageSubtractor(
					extractBlobs,
					trackBlobs,
					minimalTrainingInterval,
					maximalTrainingInterval,
					useGrayscaleColors,
					applyGaussian_2D_Filtering,
					gaussian_2D_FilterRadius,
					applyRank_2D_Filtering,
					rank_2D_FilterThreshold,
					backgroundStandardDeviationFactor,
					contourForeground,
					horizontalBlobBorder,
					verticalBlobBorder,
					horizontalExtraBorderCoefficient,
					verticalExtraBorderCoefficient,
					minimalBlobIntersectionArea,
					minimalBlobSize,
					minimalTrackDuration,
					maximalTrackDuration,
					maximalChronicleLength,
					maximalBlobInvisibilityInterval,
					maximalTrackRetentionInterval,
					inverseMatrix,
					samplingRate,
					r2WindowHalfwidth,
					applyCharacteristicLengthMedianFiltering,
					characteristicLengthMedianFilterHalfwidth,
					applyVelocityMedianFiltering,
					velocityMedianFilterHalfwidth,
					refuseSlowTracks,
					velocityThreshold,
					distanceThreshold,
					fuzzyThresholdBorder,
					synthesizedImageTransparency,
					makeRectangularBlobsInSynthesizedImage));
			return true;
		} else {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			subtractor.commit();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void resetSettings(ChoisePoint iX) {
		reset(true,false,false,iX);
	}
	//
	public void resetStatistics(ChoisePoint iX) {
		reset(false,true,false,iX);
	}
	public void resetResults(ChoisePoint iX) {
		reset(false,false,true,iX);
	}
	//
	public void resetAll(ChoisePoint iX) {
		reset(true,true,true,iX);
	}
	//
	protected void reset(boolean forgetSettings, boolean forgetStatistics, boolean forgetResults, ChoisePoint iX) {
		if (!createImageSubtractorIfNecessary(iX)) {
			boolean extractBlobs= YesNo.termYesNo2Boolean(getBuiltInSlot_E_extract_blobs(),iX);
			boolean trackBlobs= YesNo.termYesNo2Boolean(getBuiltInSlot_E_track_blobs(),iX);
			int minimalTrainingInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_training_interval(),iX);
			int maximalTrainingInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_training_interval(),iX);
			boolean useGrayscaleColors= YesNo.termYesNo2Boolean(getBuiltInSlot_E_use_grayscale_colors(),iX);
			boolean applyGaussian_2D_Filtering= YesNo.termYesNo2Boolean(getBuiltInSlot_E_apply_gaussian_filtering_to_background(),iX);
			int gaussian_2D_FilterRadius= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_background_gaussian_filter_radius(),iX);
			boolean applyRank_2D_Filtering= YesNo.termYesNo2Boolean(getBuiltInSlot_E_apply_rank_filtering_to_background(),iX);
			int rank_2D_FilterThreshold= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_background_rank_filter_threshold(),iX);
			double backgroundStandardDeviationFactor= Converters.argumentToReal(getBuiltInSlot_E_background_standard_deviation_factor(),iX);
			boolean contourForeground= YesNo.termYesNo2Boolean(getBuiltInSlot_E_contour_foreground(),iX);
			int horizontalBlobBorder= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_horizontal_blob_border(),iX);
			int verticalBlobBorder= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_vertical_blob_border(),iX);
			double horizontalExtraBorderCoefficient= Converters.argumentToReal(getBuiltInSlot_E_horizontal_extra_border_coefficient(),iX);
			double verticalExtraBorderCoefficient= Converters.argumentToReal(getBuiltInSlot_E_vertical_extra_border_coefficient(),iX);
			int minimalBlobIntersectionArea= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_intersection_area(),iX);
			int minimalBlobSize= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_size(),iX);
			int minimalTrackDuration= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_track_duration(),iX);
			int maximalTrackDuration= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_track_duration(),iX);
			NumericalValue maximalChronicleLength= NumericalValue.argumentToNumericalValue(getBuiltInSlot_E_maximal_chronicle_length(),iX);
			int maximalBlobInvisibilityInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_blob_invisibility_interval(),iX);
			int maximalTrackRetentionInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_track_retention_interval(),iX);
			Term matrixValue= getBuiltInSlot_E_inverse_transformation_matrix();
			double[][] inverseMatrix= Converters.argumentToMatrix(matrixValue,iX);
			checkMatrix(inverseMatrix,matrixValue);
			double samplingRate= Converters.argumentToReal(getBuiltInSlot_E_sampling_rate(),iX);
			int r2WindowHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_r2_window_halfwidth(),iX);
			boolean applyCharacteristicLengthMedianFiltering= YesNo.termYesNo2Boolean(getBuiltInSlot_E_apply_median_filtering_to_characteristic_length(),iX);
			int characteristicLengthMedianFilterHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_characteristic_length_median_filter_halfwidth(),iX);
			boolean applyVelocityMedianFiltering= YesNo.termYesNo2Boolean(getBuiltInSlot_E_apply_median_filtering_to_velocity(),iX);
			int velocityMedianFilterHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_velocity_median_filter_halfwidth(),iX);
			boolean refuseSlowTracks= YesNo.termYesNo2Boolean(getBuiltInSlot_E_refuse_slow_tracks(),iX);
			double velocityThreshold= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_velocity_threshold(),iX);
			double distanceThreshold= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_distance_threshold(),iX);
			double fuzzyThresholdBorder= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_threshold_border(),iX);
			int synthesizedImageTransparency= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_synthesized_image_transparency(),iX);
			boolean makeRectangularBlobsInSynthesizedImage= YesNo.termYesNo2Boolean(getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image(),iX);
			PlainImageSubtractor subtractor= imageSubtractor.get();
			subtractor.reset(
				forgetSettings,
				forgetStatistics,
				forgetResults,
				extractBlobs,
				trackBlobs,
				minimalTrainingInterval,
				maximalTrainingInterval,
				useGrayscaleColors,
				applyGaussian_2D_Filtering,
				gaussian_2D_FilterRadius,
				applyRank_2D_Filtering,
				rank_2D_FilterThreshold,
				backgroundStandardDeviationFactor,
				contourForeground,
				horizontalBlobBorder,
				verticalBlobBorder,
				horizontalExtraBorderCoefficient,
				verticalExtraBorderCoefficient,
				minimalBlobIntersectionArea,
				minimalBlobSize,
				minimalTrackDuration,
				maximalTrackDuration,
				maximalChronicleLength,
				maximalBlobInvisibilityInterval,
				maximalTrackRetentionInterval,
				inverseMatrix,
				samplingRate,
				r2WindowHalfwidth,
				applyCharacteristicLengthMedianFiltering,
				characteristicLengthMedianFilterHalfwidth,
				applyVelocityMedianFiltering,
				velocityMedianFilterHalfwidth,
				refuseSlowTracks,
				velocityThreshold,
				distanceThreshold,
				fuzzyThresholdBorder,
				synthesizedImageTransparency,
				makeRectangularBlobsInSynthesizedImage);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getFrameNumber(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			return subtractor.getCommittedRecentFrameNumber();
		} else {
			return -1;
		}
	}
	public Term getFrameNumberOrSpacer(ChoisePoint iX) {
		return new PrologInteger(getFrameNumber(iX));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentImage(Term image, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= imageSubtractor.get().getCommittedRecentImage();
		modifyImage(image,nativeImage,iX);
	}
	public byte[] getSerializedRecentImage(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			java.awt.image.BufferedImage nativeImage= subtractor.getCommittedRecentImage();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	public void getPreprocessedImage(Term image, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= imageSubtractor.get().getCommittedPreprocessedImage();
		modifyImage(image,nativeImage,iX);
	}
	public byte[] getSerializedPreprocessedImage(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			java.awt.image.BufferedImage nativeImage= subtractor.getCommittedPreprocessedImage();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	public void getForegroundImage(Term image, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= imageSubtractor.get().getCommittedForegroundImage();
		modifyImage(image,nativeImage,iX);
	}
	public byte[] getSerializedForegroundImage(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			java.awt.image.BufferedImage nativeImage= subtractor.getCommittedForegroundImage();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	public void getSynthesizedImage(Term image, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= imageSubtractor.get().getCommittedSynthesizedImage();
		modifyImage(image,nativeImage,iX);
	}
	public byte[] getSerializedSynthesizedImage(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			java.awt.image.BufferedImage nativeImage= subtractor.getCommittedSynthesizedImage();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getBackgroundImage(Term image, int layerNumber, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= imageSubtractor.get().getCommittedBackgroundImage();
		modifyImage(image,nativeImage,iX);
	}
	public byte[] getSerializedBackgroundImage(int layerNumber, ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			java.awt.image.BufferedImage nativeImage= subtractor.getCommittedBackgroundImage();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	public void getSigmaImage(Term image, int layerNumber, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= imageSubtractor.get().getCommittedSigmaImage();
		modifyImage(image,nativeImage,iX);
	}
	public byte[] getSerializedSigmaImage(int layerNumber, ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			java.awt.image.BufferedImage nativeImage= subtractor.getCommittedSigmaImage();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getBlobs(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getCommittedBlobs();
	}
	public byte[] getSerializedBlobs(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			Term blobs= subtractor.getCommittedBlobs();
			return Converters.serializeArgument(blobs);
		} else {
			return null;
		}
	}
	//
	public Term getTracks(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getCommittedTracks();
	}
	public byte[] getSerializedTracks(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			Term tracks= subtractor.getCommittedTracks();
			return Converters.serializeArgument(tracks);
		} else {
			return null;
		}
	}
	//
	public Term getChronicle(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getCommittedChronicle();
	}
	public byte[] getSerializedChronicle(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			Term tracks= subtractor.getCommittedChronicle();
			return Converters.serializeArgument(tracks);
		} else {
			return null;
		}
	}
	//
	public Term getConnectedGraphs(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getCommittedConnectedGraphs();
	}
	public byte[] getSerializedConnectedGraphs(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			Term graphs= subtractor.getCommittedConnectedGraphs();
			return Converters.serializeArgument(graphs);
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double[] physicalCoordinates(int pixelX, int pixelY, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.physicalCoordinates(pixelX,pixelY);
	}
	//
	public double characteristicLength(int x, int y, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.characteristicLength(x,y);
	}
}
