// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.concurrent.atomic.AtomicReference;

public abstract class ImageSubtractor
		extends GenericImageSubtractor
		implements ImageSubtractorOperations {
	//
	protected AtomicReference<PlainImageSubtractor> imageSubtractor= new AtomicReference<>(null);
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_extract_blobs();
	abstract public Term getBuiltInSlot_E_track_blobs();
	abstract public Term getBuiltInSlot_E_minimal_training_interval();
	abstract public Term getBuiltInSlot_E_maximal_training_interval();
	abstract public Term getBuiltInSlot_E_use_grayscale_colors();
	abstract public Term getBuiltInSlot_E_apply_gaussian_filtering_to_background();
	abstract public Term getBuiltInSlot_E_background_gaussian_filter_radius();
	abstract public Term getBuiltInSlot_E_apply_rank_filtering_to_background();
	abstract public Term getBuiltInSlot_E_background_rank_filter_threshold();
	abstract public Term getBuiltInSlot_E_background_standard_deviation_factor();
	abstract public Term getBuiltInSlot_E_contour_foreground();
	abstract public Term getBuiltInSlot_E_r2_window_halfwidth();
	abstract public Term getBuiltInSlot_E_horizontal_blob_border();
	abstract public Term getBuiltInSlot_E_vertical_blob_border();
	abstract public Term getBuiltInSlot_E_horizontal_extra_border_coefficient();
	abstract public Term getBuiltInSlot_E_vertical_extra_border_coefficient();
	abstract public Term getBuiltInSlot_E_minimal_blob_intersection_area();
	abstract public Term getBuiltInSlot_E_minimal_blob_size();
	abstract public Term getBuiltInSlot_E_minimal_track_duration();
	abstract public Term getBuiltInSlot_E_maximal_blob_invisibility_interval();
	abstract public Term getBuiltInSlot_E_maximal_track_retention_interval();
	abstract public Term getBuiltInSlot_E_inverse_transformation_matrix();
	abstract public Term getBuiltInSlot_E_sampling_rate();
	abstract public Term getBuiltInSlot_E_apply_median_filtering_to_characteristic_length();
	abstract public Term getBuiltInSlot_E_characteristic_length_median_filter_halfwidth();
	abstract public Term getBuiltInSlot_E_apply_median_filtering_to_velocity();
	abstract public Term getBuiltInSlot_E_velocity_median_filter_halfwidth();
	abstract public Term getBuiltInSlot_E_refuse_slow_tracks();
	abstract public Term getBuiltInSlot_E_fuzzy_velocity_threshold();
	abstract public Term getBuiltInSlot_E_fuzzy_distance_threshold();
	abstract public Term getBuiltInSlot_E_fuzzy_threshold_border();
	abstract public Term getBuiltInSlot_E_synthesized_image_transparency();
	abstract public Term getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image();
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
	public void setBlobExtractionMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBlobExtractionMode(mode);
	}
	public boolean getBlobExtractionMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBlobExtractionMode();
	}
	//
	// BlobTracingMode
	//
	public void setBlobTracingMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBlobTracingMode(mode);
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
	public void setGrayscaleMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setGrayscaleMode(mode);
	}
	public boolean getGrayscaleMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getGrayscaleMode();
	}
	//
	// BackgroundGaussianFilteringMode
	//
	public void setBackgroundGaussianFilteringMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBackgroundGaussianFilteringMode(mode);
	}
	public boolean getBackgroundGaussianFilteringMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBackgroundGaussianFilteringMode();
	}
	//
	// BackgroundGaussianFilterRadius
	//
	public void setBackgroundGaussianFilterRadius(int radius, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBackgroundGaussianFilterRadius(radius);
	}
	public int getBackgroundGaussianFilterRadius(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBackgroundGaussianFilterRadius();
	}
	//
	// BackgroundRankFilteringMode
	//
	public void setBackgroundRankFilteringMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBackgroundRankFilteringMode(mode);
	}
	public boolean getBackgroundRankFilteringMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBackgroundRankFilteringMode();
	}
	//
	// BackgroundRankFilterThreshold
	//
	public void setBackgroundRankFilterThreshold(int threshold, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBackgroundRankFilterThreshold(threshold);
	}
	public int getBackgroundRankFilterThreshold(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBackgroundRankFilterThreshold();
	}
	//
	// BackgroundStandardDeviationFactor
	//
	public void setBackgroundStandardDeviationFactor(double factor, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setBackgroundStandardDeviationFactor(factor);
	}
	public double getBackgroundStandardDeviationFactor(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getBackgroundStandardDeviationFactor();
	}
	//
	// ForegroundContouringMode
	//
	public void setForegroundContouringMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setForegroundContouringMode(mode);
	}
	public boolean getForegroundContouringMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getForegroundContouringMode();
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
	public void setSerializedInverseTransformationMatrix(byte[] bytes, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		double[][] matrix= Converters.deserializeMatrix(bytes);
		subtractor.setInverseTransformationMatrix(matrix);
	}
	public double[][] getInverseTransformationMatrix(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getInverseTransformationMatrix();
	}
	public byte[] getSerializedInverseTransformationMatrix(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		double[][] matrix= subtractor.getInverseTransformationMatrix();
		return Converters.serializeMatrix(matrix);
	}
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
	public void setSlowTracksDeletionMode(boolean mode, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setSlowTracksDeletionMode(mode);
	}
	public boolean getSlowTracksDeletionMode(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getSlowTracksDeletionMode();
	}
	//
	// FuzzyVelocityThreshold
	//
	public void setFuzzyVelocityThreshold(double threshold, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setFuzzyVelocityThreshold(threshold);
	}
	public double getFuzzyVelocityThreshold(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getFuzzyVelocityThreshold();
	}
	//
	// FuzzyDistanceThreshold
	//
	public void setFuzzyDistanceThreshold(double threshold, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setFuzzyDistanceThreshold(threshold);
	}
	public double getFuzzyDistanceThreshold(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.getFuzzyDistanceThreshold();
	}
	//
	// FuzzyThresholdBorder
	//
	public void setFuzzyThresholdBorder(double size, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		subtractor.setFuzzyThresholdBorder(size);
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
	public void subtract(long frameNumber, java.awt.image.BufferedImage nativeImage, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		createImageSubtractorIfNecessary(iX);
		if (nativeImage != null) {
			PlainImageSubtractor subtractor= imageSubtractor.get();
			subtractor.subtractImageAndAnalyseBlobs(frameNumber,nativeImage,takeFrameIntoAccount);
		}
	}
	public void subtract(long frameNumber, byte[] bytes, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= Space2DWriter.bytesToImage(bytes);
		if (nativeImage != null) {
			PlainImageSubtractor subtractor= imageSubtractor.get();
			subtractor.subtractImageAndAnalyseBlobs(frameNumber,nativeImage,takeFrameIntoAccount);
		}
	}
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
			int r2WindowHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_r2_window_halfwidth(),iX);
			int horizontalBlobBorder= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_horizontal_blob_border(),iX);
			int verticalBlobBorder= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_vertical_blob_border(),iX);
			double horizontalExtraBorderCoefficient= Converters.argumentToReal(getBuiltInSlot_E_horizontal_extra_border_coefficient(),iX);
			double verticalExtraBorderCoefficient= Converters.argumentToReal(getBuiltInSlot_E_vertical_extra_border_coefficient(),iX);
			int minimalBlobIntersectionArea= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_intersection_area(),iX);
			int minimalBlobSize= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_size(),iX);
			int minimalTrackDuration= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_track_duration(),iX);
			int maximalBlobInvisibilityInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_blob_invisibility_interval(),iX);
			int maximalTrackRetentionInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_track_retention_interval(),iX);
			Term matrixValue= getBuiltInSlot_E_inverse_transformation_matrix();
			double[][] inverseMatrix= Converters.argumentToMatrix(matrixValue,iX);
			checkMatrix(inverseMatrix,matrixValue);
			double samplingRate= Converters.argumentToReal(getBuiltInSlot_E_sampling_rate(),iX);
			boolean applyCharacteristicLengthMedianFiltering= YesNo.termYesNo2Boolean(getBuiltInSlot_E_apply_median_filtering_to_characteristic_length(),iX);
			int characteristicLengthMedianFilterHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_characteristic_length_median_filter_halfwidth(),iX);
			boolean applyVelocityMedianFiltering= YesNo.termYesNo2Boolean(getBuiltInSlot_E_apply_median_filtering_to_velocity(),iX);
			int velocityMedianFilterHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_velocity_median_filter_halfwidth(),iX);
			boolean refuseSlowTracks= YesNo.termYesNo2Boolean(getBuiltInSlot_E_refuse_slow_tracks(),iX);
			double velocityThreshold= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_velocity_threshold(),iX);
			double distanceThreshold= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_distance_threshold(),iX);
			double fuzzyThresholdBorder= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_threshold_border(),iX);
			int synthesizedImageTransparency= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_synthesized_image_transparency(),iX);
			boolean makeSquareBlobsInSynthesizedImage= YesNo.termYesNo2Boolean(getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image(),iX);
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
					r2WindowHalfwidth,
					horizontalBlobBorder,
					verticalBlobBorder,
					horizontalExtraBorderCoefficient,
					verticalExtraBorderCoefficient,
					minimalBlobIntersectionArea,
					minimalBlobSize,
					minimalTrackDuration,
					maximalBlobInvisibilityInterval,
					maximalTrackRetentionInterval,
					inverseMatrix,
					samplingRate,
					applyCharacteristicLengthMedianFiltering,
					characteristicLengthMedianFilterHalfwidth,
					applyVelocityMedianFiltering,
					velocityMedianFilterHalfwidth,
					refuseSlowTracks,
					velocityThreshold,
					distanceThreshold,
					fuzzyThresholdBorder,
					synthesizedImageTransparency,
					makeSquareBlobsInSynthesizedImage));
			return true;
		} else {
			return false;
		}
	}
	protected void checkMatrix(double[][] matrix, Term matrixValue) {
		int numberOfRows= matrix.length;
		if (numberOfRows==0) {
		} else if (numberOfRows==3) {
			int numberOfColumns= matrix[0].length;
			if (numberOfColumns != 3) {
				throw new WrongArgumentIsNotAMatrix(matrixValue);
			}
		} else {
			throw new WrongArgumentIsNotAMatrix(matrixValue);
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
			int r2WindowHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_r2_window_halfwidth(),iX);
			int horizontalBlobBorder= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_horizontal_blob_border(),iX);
			int verticalBlobBorder= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_vertical_blob_border(),iX);
			double horizontalExtraBorderCoefficient= Converters.argumentToReal(getBuiltInSlot_E_horizontal_extra_border_coefficient(),iX);
			double verticalExtraBorderCoefficient= Converters.argumentToReal(getBuiltInSlot_E_vertical_extra_border_coefficient(),iX);
			int minimalBlobIntersectionArea= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_intersection_area(),iX);
			int minimalBlobSize= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_size(),iX);
			int minimalTrackDuration= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_track_duration(),iX);
			int maximalBlobInvisibilityInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_blob_invisibility_interval(),iX);
			int maximalTrackRetentionInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_track_retention_interval(),iX);
			Term matrixValue= getBuiltInSlot_E_inverse_transformation_matrix();
			double[][] inverseMatrix= Converters.argumentToMatrix(matrixValue,iX);
			checkMatrix(inverseMatrix,matrixValue);
			double samplingRate= Converters.argumentToReal(getBuiltInSlot_E_sampling_rate(),iX);
			boolean applyCharacteristicLengthMedianFiltering= YesNo.termYesNo2Boolean(getBuiltInSlot_E_apply_median_filtering_to_characteristic_length(),iX);
			int characteristicLengthMedianFilterHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_characteristic_length_median_filter_halfwidth(),iX);
			boolean applyVelocityMedianFiltering= YesNo.termYesNo2Boolean(getBuiltInSlot_E_apply_median_filtering_to_velocity(),iX);
			int velocityMedianFilterHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_velocity_median_filter_halfwidth(),iX);
			boolean refuseSlowTracks= YesNo.termYesNo2Boolean(getBuiltInSlot_E_refuse_slow_tracks(),iX);
			double velocityThreshold= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_velocity_threshold(),iX);
			double distanceThreshold= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_distance_threshold(),iX);
			double fuzzyThresholdBorder= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_threshold_border(),iX);
			int synthesizedImageTransparency= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_synthesized_image_transparency(),iX);
			boolean makeSquareBlobsInSynthesizedImage= YesNo.termYesNo2Boolean(getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image(),iX);
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
				r2WindowHalfwidth,
				horizontalBlobBorder,
				verticalBlobBorder,
				horizontalExtraBorderCoefficient,
				verticalExtraBorderCoefficient,
				minimalBlobIntersectionArea,
				minimalBlobSize,
				minimalTrackDuration,
				maximalBlobInvisibilityInterval,
				maximalTrackRetentionInterval,
				inverseMatrix,
				samplingRate,
				applyCharacteristicLengthMedianFiltering,
				characteristicLengthMedianFilterHalfwidth,
				applyVelocityMedianFiltering,
				velocityMedianFilterHalfwidth,
				refuseSlowTracks,
				velocityThreshold,
				distanceThreshold,
				fuzzyThresholdBorder,
				synthesizedImageTransparency,
				makeSquareBlobsInSynthesizedImage);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getFrameNumberOrSpacer(ChoisePoint iX) {
		return new PrologInteger(getFrameNumber(iX));
	}
	public long getFrameNumber(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			return subtractor.getFrameNumber();
		} else {
			return -1;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentImage(Term image, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= imageSubtractor.get().get_recent_image();
		modifyImage(image,nativeImage,iX);
	}
	public byte[] getSerializedRecentImage(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			java.awt.image.BufferedImage nativeImage= subtractor.get_recent_image();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	public void getBackgroundImage(Term image, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= imageSubtractor.get().get_background_image();
		modifyImage(image,nativeImage,iX);
	}
	public byte[] getSerializedBackgroundImage(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			java.awt.image.BufferedImage nativeImage= subtractor.get_background_image();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	public void getSigmaImage(Term image, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= imageSubtractor.get().get_sigma_image();
		modifyImage(image,nativeImage,iX);
	}
	public byte[] getSerializedSigmaImage(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			java.awt.image.BufferedImage nativeImage= subtractor.get_sigma_image();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	public void getForegroundImage(Term image, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= imageSubtractor.get().get_foreground_image();
		modifyImage(image,nativeImage,iX);
	}
	public byte[] getSerializedForegroundImage(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			java.awt.image.BufferedImage nativeImage= subtractor.get_foreground_image();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	public void getSynthesizedImage(Term image, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= imageSubtractor.get().get_synthesized_image();
		modifyImage(image,nativeImage,iX);
	}
	public byte[] getSerializedSynthesizedImage(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			java.awt.image.BufferedImage nativeImage= subtractor.get_synthesized_image();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	protected byte[] convertImageToBytes(java.awt.image.BufferedImage nativeImage) {
		GenericImageEncodingAttributes attributes= getCurrentImageEncodingAttributes();
		if (nativeImage != null && attributes != null) {
			Space2DWriter writer= Space2DWriter.createSpace2DWriter(nativeImage,attributes);
			try {
				return writer.imageToBytes(nativeImage);
			} finally {
				writer.dispose();
			}
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
		return subtractor.get_blobs();
	}
	public byte[] getSerializedBlobs(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			Term blobs= subtractor.get_blobs();
			return Converters.serializeArgument(blobs);
		} else {
			return null;
		}
	}
	//
	public Term getTracks(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.get_tracks();
	}
	public byte[] getSerializedTracks(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			Term tracks= subtractor.get_tracks();
			return Converters.serializeArgument(tracks);
		} else {
			return null;
		}
	}
	//
	public Term getConnectedGraphs(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		PlainImageSubtractor subtractor= imageSubtractor.get();
		return subtractor.get_connected_graphs();
	}
	public byte[] getSerializedConnectedGraphs(ChoisePoint iX) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			Term graphs= subtractor.get_connected_graphs();
			return Converters.serializeArgument(graphs);
		} else {
			return null;
		}
	}
}
