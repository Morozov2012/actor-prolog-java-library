// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

//import morozov.classes.*;
import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
//import morozov.system.signals.*;
import morozov.system.vision.*;
import morozov.system.vision.errors.*;
import morozov.terms.*;

import java.util.concurrent.atomic.AtomicReference;
//import java.math.BigDecimal;
//import java.math.MathContext;
//import java.util.Calendar;
//import java.util.TimerTask;

public abstract class ImageSubtractor extends Alpha {
	//
	protected AtomicReference<PlainImageSubtractor> imageSubtractor= new AtomicReference<>(null);
	//
	abstract public Term getBuiltInSlot_E_extract_blobs();
	abstract public Term getBuiltInSlot_E_track_blobs();
	abstract public Term getBuiltInSlot_E_minimal_training_interval();
	abstract public Term getBuiltInSlot_E_use_grayscale_colors();
	abstract public Term getBuiltInSlot_E_apply_gaussian_filtering_to_background();
	abstract public Term getBuiltInSlot_E_background_gaussian_filter_radius();
	abstract public Term getBuiltInSlot_E_apply_median_filtering_to_background();
	abstract public Term getBuiltInSlot_E_background_median_filter_threshold();
	abstract public Term getBuiltInSlot_E_background_dispersion_factor();
	abstract public Term getBuiltInSlot_E_horizontal_blob_border();
	abstract public Term getBuiltInSlot_E_vertical_blob_border();
	abstract public Term getBuiltInSlot_E_minimal_blob_intersection_area();
	abstract public Term getBuiltInSlot_E_minimal_blob_size();
	abstract public Term getBuiltInSlot_E_minimal_track_duration();
	abstract public Term getBuiltInSlot_E_maximal_blob_invisibility_interval();
	abstract public Term getBuiltInSlot_E_maximal_track_retention_interval();
	abstract public Term getBuiltInSlot_E_inverse_transformation_matrix();
	abstract public Term getBuiltInSlot_E_sampling_rate();
	abstract public Term getBuiltInSlot_E_apply_median_filtering_to_velocity();
	abstract public Term getBuiltInSlot_E_velocity_median_filter_halfwidth();
	abstract public Term getBuiltInSlot_E_refuse_slow_tracks();
	abstract public Term getBuiltInSlot_E_fuzzy_velocity_threshold();
	abstract public Term getBuiltInSlot_E_fuzzy_distance_threshold();
	abstract public Term getBuiltInSlot_E_fuzzy_threshold_border();
	abstract public Term getBuiltInSlot_E_synthesized_image_transparency();
	abstract public Term getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image();
	//
	public void subtract2s(ChoisePoint iX, Term a1, Term a2) {
		subtract(a1,a2,true,iX);
	}
	public void subtract3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		boolean takeFrameIntoAccount= Converters.term2YesNo(a3,iX);
		subtract(a1,a2,takeFrameIntoAccount,iX);
	}
	protected void subtract(Term frame, Term image, boolean takeFrameIntoAccount, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		long frameNumber= PrologInteger.toLong(Converters.argumentToRoundInteger(frame,iX));
		image= image.dereferenceValue(iX);
		if (image instanceof morozov.built_in.BufferedImage) {
			java.awt.image.BufferedImage bufferedImage= ((morozov.built_in.BufferedImage)image).getImage(iX);
			imageSubtractor.get().subtractImageAndAnalyseBlobs(frameNumber,bufferedImage,takeFrameIntoAccount);
		} else {
			throw new WrongArgumentIsNotBufferedImage(image);
		}
	}
	synchronized protected boolean createImageSubtractorIfNecessary(ChoisePoint iX) {
		if (imageSubtractor.get()==null) {
			boolean extractBlobs= Converters.term2YesNo(getBuiltInSlot_E_extract_blobs(),iX);
			boolean trackBlobs= Converters.term2YesNo(getBuiltInSlot_E_track_blobs(),iX);
			int trainingInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_training_interval(),iX);
			boolean useGrayscaleColors= Converters.term2YesNo(getBuiltInSlot_E_use_grayscale_colors(),iX);
			boolean applyGaussian_2D_Filtering= Converters.term2YesNo(getBuiltInSlot_E_apply_gaussian_filtering_to_background(),iX);
			int gaussian_2D_FilterRadius= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_background_gaussian_filter_radius(),iX);
			boolean applyMedian_2D_Filtering= Converters.term2YesNo(getBuiltInSlot_E_apply_median_filtering_to_background(),iX);
			int median_2D_FilterThreshold= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_background_median_filter_threshold(),iX);
			double backgroundDispersionFactor= Converters.argumentToReal(getBuiltInSlot_E_background_dispersion_factor(),iX);
			int horizontalBlobBorder= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_horizontal_blob_border(),iX);
			int verticalBlobBorder= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_vertical_blob_border(),iX);
			int minimalBlobIntersectionArea= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_intersection_area(),iX);
			int minimalBlobSize= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_size(),iX);
			int minimalTrackDuration= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_track_duration(),iX);
			int maximalBlobInvisibilityInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_blob_invisibility_interval(),iX);
			int maximalTrackRetentionInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_track_retention_interval(),iX);
			Term matrixValue= getBuiltInSlot_E_inverse_transformation_matrix();
			double[][] inverseMatrix= Converters.argumentToMatrix(matrixValue,iX);
			checkMatrix(inverseMatrix,matrixValue);
			double samplingRate= Converters.argumentToReal(getBuiltInSlot_E_sampling_rate(),iX);
			boolean applyMedianFiltering= Converters.term2YesNo(getBuiltInSlot_E_apply_median_filtering_to_velocity(),iX);
			int medianFilterHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_velocity_median_filter_halfwidth(),iX);
			boolean refuseSlowTracks= Converters.term2YesNo(getBuiltInSlot_E_refuse_slow_tracks(),iX);
			double velocityThreshold= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_velocity_threshold(),iX);
			double distanceThreshold= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_distance_threshold(),iX);
			double fuzzyThresholdBorder= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_threshold_border(),iX);
			int synthesizedImageTransparency= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_synthesized_image_transparency(),iX);
			boolean makeSquareBlobsInSynthesizedImage= Converters.term2YesNo(getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image(),iX);
			imageSubtractor.set(
				new PlainImageSubtractor(
					extractBlobs,
					trackBlobs,
					trainingInterval,
					useGrayscaleColors,
					applyGaussian_2D_Filtering,
					gaussian_2D_FilterRadius,
					applyMedian_2D_Filtering,
					median_2D_FilterThreshold,
					backgroundDispersionFactor,
					horizontalBlobBorder,
					verticalBlobBorder,
					minimalBlobIntersectionArea,
					minimalBlobSize,
					minimalTrackDuration,
					maximalBlobInvisibilityInterval,
					maximalTrackRetentionInterval,
					inverseMatrix,
					samplingRate,
					applyMedianFiltering,
					medianFilterHalfwidth,
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
	public void commit0s(ChoisePoint iX) {
		commit(iX);
	}
	protected void commit(ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().commit();
	}
	// public void reset1s(ChoisePoint iX, Term a1) {
	//	boolean forgetBackgroundStatistics= Converters.term2YesNo(a1,iX);
	//	reset(forgetBackgroundStatistics,iX);
	// }
	public void resetSettings0s(ChoisePoint iX) {
		reset(true,false,false,iX);
	}
	public void resetStatistics0s(ChoisePoint iX) {
		reset(false,true,false,iX);
	}
	public void resetResults0s(ChoisePoint iX) {
		reset(false,false,true,iX);
	}
	public void resetAll0s(ChoisePoint iX) {
		reset(true,true,true,iX);
	}
	protected void reset(boolean forgetSettings, boolean forgetStatistics, boolean forgetResults, ChoisePoint iX) {
		if (!createImageSubtractorIfNecessary(iX)) {
			boolean extractBlobs= Converters.term2YesNo(getBuiltInSlot_E_extract_blobs(),iX);
			boolean trackBlobs= Converters.term2YesNo(getBuiltInSlot_E_track_blobs(),iX);
			int trainingInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_training_interval(),iX);
			boolean useGrayscaleColors= Converters.term2YesNo(getBuiltInSlot_E_use_grayscale_colors(),iX);
			boolean applyGaussian_2D_Filtering= Converters.term2YesNo(getBuiltInSlot_E_apply_gaussian_filtering_to_background(),iX);
			int gaussian_2D_FilterRadius= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_background_gaussian_filter_radius(),iX);
			boolean applyMedian_2D_Filtering= Converters.term2YesNo(getBuiltInSlot_E_apply_median_filtering_to_background(),iX);
			int median_2D_FilterThreshold= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_background_median_filter_threshold(),iX);
			double backgroundDispersionFactor= Converters.argumentToReal(getBuiltInSlot_E_background_dispersion_factor(),iX);
			int horizontalBlobBorder= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_horizontal_blob_border(),iX);
			int verticalBlobBorder= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_vertical_blob_border(),iX);
			int minimalBlobIntersectionArea= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_intersection_area(),iX);
			int minimalBlobSize= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_size(),iX);
			int minimalTrackDuration= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_track_duration(),iX);
			int maximalBlobInvisibilityInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_blob_invisibility_interval(),iX);
			int maximalTrackRetentionInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_track_retention_interval(),iX);
			Term matrixValue= getBuiltInSlot_E_inverse_transformation_matrix();
			double[][] inverseMatrix= Converters.argumentToMatrix(matrixValue,iX);
			checkMatrix(inverseMatrix,matrixValue);
			double samplingRate= Converters.argumentToReal(getBuiltInSlot_E_sampling_rate(),iX);
			boolean applyMedianFiltering= Converters.term2YesNo(getBuiltInSlot_E_apply_median_filtering_to_velocity(),iX);
			int medianFilterHalfwidth= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_velocity_median_filter_halfwidth(),iX);
			boolean refuseSlowTracks= Converters.term2YesNo(getBuiltInSlot_E_refuse_slow_tracks(),iX);
			double velocityThreshold= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_velocity_threshold(),iX);
			double distanceThreshold= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_distance_threshold(),iX);
			double fuzzyThresholdBorder= Converters.argumentToReal(getBuiltInSlot_E_fuzzy_threshold_border(),iX);
			int synthesizedImageTransparency= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_synthesized_image_transparency(),iX);
			boolean makeSquareBlobsInSynthesizedImage= Converters.term2YesNo(getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image(),iX);
			imageSubtractor.get().reset(
				forgetSettings,
				forgetStatistics,
				forgetResults,
				extractBlobs,
				trackBlobs,
				trainingInterval,
				useGrayscaleColors,
				applyGaussian_2D_Filtering,
				gaussian_2D_FilterRadius,
				applyMedian_2D_Filtering,
				median_2D_FilterThreshold,
				backgroundDispersionFactor,
				horizontalBlobBorder,
				verticalBlobBorder,
				minimalBlobIntersectionArea,
				minimalBlobSize,
				minimalTrackDuration,
				maximalBlobInvisibilityInterval,
				maximalTrackRetentionInterval,
				inverseMatrix,
				samplingRate,
				applyMedianFiltering,
				medianFilterHalfwidth,
				refuseSlowTracks,
				velocityThreshold,
				distanceThreshold,
				fuzzyThresholdBorder,
				synthesizedImageTransparency,
				makeSquareBlobsInSynthesizedImage);
		}
	}
	//
	public void getFrameNumber1s(ChoisePoint iX, PrologVariable frameNumber) {
		PlainImageSubtractor subtractor= imageSubtractor.get();
		if (subtractor != null) {
			frameNumber.value= new PrologInteger(subtractor.getFrameNumber());
		} else {
			frameNumber.value= new PrologInteger(-1);
		}
	}
	//
	public void getRecentImage1s(ChoisePoint iX, Term image) {
		createImageSubtractorIfNecessary(iX);
		if (image instanceof morozov.built_in.BufferedImage) {
			morozov.built_in.BufferedImage bufferedImage= (morozov.built_in.BufferedImage)image;
			bufferedImage.setImage(imageSubtractor.get().get_recent_image());
		} else {
			throw new WrongArgumentIsNotBufferedImage(image);
		}
	}
	public void getBackgroundImage1s(ChoisePoint iX, Term image) {
		createImageSubtractorIfNecessary(iX);
		if (image instanceof morozov.built_in.BufferedImage) {
			morozov.built_in.BufferedImage bufferedImage= (morozov.built_in.BufferedImage)image;
			bufferedImage.setImage(imageSubtractor.get().get_background_image());
		} else {
			throw new WrongArgumentIsNotBufferedImage(image);
		}
	}
	public void getSigmaImage1s(ChoisePoint iX, Term image) {
		createImageSubtractorIfNecessary(iX);
		if (image instanceof morozov.built_in.BufferedImage) {
			morozov.built_in.BufferedImage bufferedImage= (morozov.built_in.BufferedImage)image;
			bufferedImage.setImage(imageSubtractor.get().get_sigma_image());
		} else {
			throw new WrongArgumentIsNotBufferedImage(image);
		}
	}
	public void getForegroundImage1s(ChoisePoint iX, Term image) {
		createImageSubtractorIfNecessary(iX);
		if (image instanceof morozov.built_in.BufferedImage) {
			morozov.built_in.BufferedImage bufferedImage= (morozov.built_in.BufferedImage)image;
			bufferedImage.setImage(imageSubtractor.get().get_foreground_image());
		} else {
			throw new WrongArgumentIsNotBufferedImage(image);
		}
	}
	public void getSynthesizedImage1s(ChoisePoint iX, Term image) {
		createImageSubtractorIfNecessary(iX);
		if (image instanceof morozov.built_in.BufferedImage) {
			morozov.built_in.BufferedImage bufferedImage= (morozov.built_in.BufferedImage)image;
			bufferedImage.setImage(imageSubtractor.get().get_synthesized_image());
		} else {
			throw new WrongArgumentIsNotBufferedImage(image);
		}
	}
	//
	public void getBlobs1s(ChoisePoint iX, PrologVariable blobs) {
		createImageSubtractorIfNecessary(iX);
		Term blobList= imageSubtractor.get().get_blobs();
		blobs.value= blobList;
	}
	public void getTracks1s(ChoisePoint iX, PrologVariable tracks) {
		createImageSubtractorIfNecessary(iX);
		Term trackList= imageSubtractor.get().get_tracks();
		tracks.value= trackList;
	}
	public void getConnectedGraphs1s(ChoisePoint iX, PrologVariable graphs) {
		createImageSubtractorIfNecessary(iX);
		Term graphList= imageSubtractor.get().get_connected_graphs();
		graphs.value= graphList;
	}
	// BlobExtractionMode
	public void setBlobExtractionMode1s(ChoisePoint iX, Term value) {
		boolean mode= Converters.term2YesNo(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setBlobExtractionMode(mode);
	}
	//
	public void getBlobExtractionMode0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= Converters.boolean2YesNoTerm(imageSubtractor.get().getBlobExtractionMode());
	}
	public void getBlobExtractionMode0fs(ChoisePoint iX) {
	}
	// BlobTracingMode
	public void setBlobTracingMode1s(ChoisePoint iX, Term value) {
		boolean mode= Converters.term2YesNo(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setBlobTracingMode(mode);
	}
	//
	public void getBlobTracingMode0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= Converters.boolean2YesNoTerm(imageSubtractor.get().getBlobExtractionMode());
	}
	public void getBlobTracingMode0fs(ChoisePoint iX) {
	}
	// MinimalTrainingInterval
	public void setMinimalTrainingInterval1s(ChoisePoint iX, Term value) {
		int interval= Converters.argumentToSmallRoundInteger(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setMinimalTrainingInterval(interval);
	}
	//
	public void getMinimalTrainingInterval0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologInteger(imageSubtractor.get().getMinimalTrainingInterval());
	}
	public void getMinimalTrainingInterval0fs(ChoisePoint iX) {
	}
	// GrayscaleMode
	public void setGrayscaleMode1s(ChoisePoint iX, Term value) {
		boolean mode= Converters.term2YesNo(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setGrayscaleMode(mode);
	}
	//
	public void getGrayscaleMode0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= Converters.boolean2YesNoTerm(imageSubtractor.get().getGrayscaleMode());
	}
	public void getGrayscaleMode0fs(ChoisePoint iX) {
	}
	// BackgroundGaussianFilteringMode
	public void setBackgroundGaussianFilteringMode1s(ChoisePoint iX, Term value) {
		boolean mode= Converters.term2YesNo(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setBackgroundGaussianFilteringMode(mode);
	}
	//
	public void getBackgroundGaussianFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= Converters.boolean2YesNoTerm(imageSubtractor.get().getBackgroundGaussianFilteringMode());
	}
	public void getBackgroundGaussianFilteringMode0fs(ChoisePoint iX) {
	}
	// BackgroundGaussianFilterRadius
	public void setBackgroundGaussianFilterRadius1s(ChoisePoint iX, Term value) {
		int radius= Converters.argumentToSmallRoundInteger(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setBackgroundGaussianFilterRadius(radius);
	}
	//
	public void getBackgroundGaussianFilterRadius0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologInteger(imageSubtractor.get().getBackgroundGaussianFilterRadius());
	}
	public void getBackgroundGaussianFilterRadius0fs(ChoisePoint iX) {
	}
	// BackgroundMedianFilteringMode
	public void setBackgroundMedianFilteringMode1s(ChoisePoint iX, Term value) {
		boolean mode= Converters.term2YesNo(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setBackgroundMedianFilteringMode(mode);
	}
	//
	public void getBackgroundMedianFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= Converters.boolean2YesNoTerm(imageSubtractor.get().getBackgroundMedianFilteringMode());
	}
	public void getBackgroundMedianFilteringMode0fs(ChoisePoint iX) {
	}
	// BackgroundMedianFilterThreshold
	public void setBackgroundMedianFilterThreshold1s(ChoisePoint iX, Term value) {
		int threshold= Converters.argumentToSmallRoundInteger(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setBackgroundMedianFilterThreshold(threshold);
	}
	//
	public void getBackgroundMedianFilterThreshold0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologInteger(imageSubtractor.get().getBackgroundMedianFilterThreshold());
	}
	public void getBackgroundMedianFilterThreshold0fs(ChoisePoint iX) {
	}
	// BackgroundDispersionFactor
	public void setBackgroundDispersionFactor1s(ChoisePoint iX, Term value) {
		double factor= Converters.argumentToReal(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setBackgroundDispersionFactor(factor);
	}
	//
	public void getBackgroundDispersionFactor0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologReal(imageSubtractor.get().getBackgroundDispersionFactor());
	}
	public void getBackgroundDispersionFactor0fs(ChoisePoint iX) {
	}
	// HorizontalBlobBorder
	public void setHorizontalBlobBorder1s(ChoisePoint iX, Term value) {
		int interval= Converters.argumentToSmallRoundInteger(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setHorizontalBlobBorder(interval);
	}
	//
	public void getHorizontalBlobBorder0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologInteger(imageSubtractor.get().getHorizontalBlobBorder());
	}
	public void getHorizontalBlobBorder0fs(ChoisePoint iX) {
	}
	// VerticalBlobBorder
	public void setVerticalBlobBorder1s(ChoisePoint iX, Term value) {
		int interval= Converters.argumentToSmallRoundInteger(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setVerticalBlobBorder(interval);
	}
	//
	public void getVerticalBlobBorder0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologInteger(imageSubtractor.get().getVerticalBlobBorder());
	}
	public void getVerticalBlobBorder0fs(ChoisePoint iX) {
	}
	// MinimalBlobIntersectionArea
	public void setMinimalBlobIntersectionArea1s(ChoisePoint iX, Term value) {
		int interval= Converters.argumentToSmallRoundInteger(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setMinimalBlobIntersectionArea(interval);
	}
	//
	public void getMinimalBlobIntersectionArea0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologInteger(imageSubtractor.get().getMinimalBlobIntersectionArea());
	}
	public void getMinimalBlobIntersectionArea0fs(ChoisePoint iX) {
	}
	// MinimalBlobSize
	public void setMinimalBlobSize1s(ChoisePoint iX, Term value) {
		int interval= Converters.argumentToSmallRoundInteger(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setMinimalBlobSize(interval);
	}
	//
	public void getMinimalBlobSize0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologInteger(imageSubtractor.get().getMinimalBlobSize());
	}
	public void getMinimalBlobSize0fs(ChoisePoint iX) {
	}
	// MinimalTrackDuration
	public void setMinimalTrackDuration1s(ChoisePoint iX, Term value) {
		int interval= Converters.argumentToSmallRoundInteger(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setMinimalTrackDuration(interval);
	}
	//
	public void getMinimalTrackDuration0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologInteger(imageSubtractor.get().getMinimalTrackDuration());
	}
	public void getMinimalTrackDuration0fs(ChoisePoint iX) {
	}
	// MaximalBlobInvisibilityInterval
	public void setMaximalBlobInvisibilityInterval1s(ChoisePoint iX, Term value) {
		int interval= Converters.argumentToSmallRoundInteger(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setMaximalBlobInvisibilityInterval(interval);
	}
	//
	public void getMaximalBlobInvisibilityInterval0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologInteger(imageSubtractor.get().getMaximalBlobInvisibilityInterval());
	}
	public void getMaximalBlobInvisibilityInterval0fs(ChoisePoint iX) {
	}
	// MaximalTrackRetentionInterval
	public void setMaximalTrackRetentionInterval1s(ChoisePoint iX, Term value) {
		int interval= Converters.argumentToSmallRoundInteger(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setMaximalTrackRetentionInterval(interval);
	}
	//
	public void getMaximalTrackRetentionInterval0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologInteger(imageSubtractor.get().getMaximalTrackRetentionInterval());
	}
	public void getMaximalTrackRetentionInterval0fs(ChoisePoint iX) {
	}
	// InverseTransformationMatrix
	public void setInverseTransformationMatrix1s(ChoisePoint iX, Term value) {
		double[][] inverseMatrix= Converters.argumentToMatrix(value,iX);
		checkMatrix(inverseMatrix,value);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setInverseTransformationMatrix(inverseMatrix);
	}
	//
	public void getInverseTransformationMatrix0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		double[][] inverseMatrix= imageSubtractor.get().getInverseTransformationMatrix();
		result.value= Converters.doubleMatrixToListOfList(inverseMatrix);
	}
	public void getInverseTransformationMatrix0fs(ChoisePoint iX) {
	}
	// SamplingRate
	public void setSamplingRate1s(ChoisePoint iX, Term value) {
		double threshold= Converters.argumentToReal(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setSamplingRate(threshold);
	}
	//
	public void getSamplingRate0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologReal(imageSubtractor.get().getSamplingRate());
	}
	public void getSamplingRate0fs(ChoisePoint iX) {
	}
	// VelocityMedianFilteringMode
	public void setVelocityMedianFilteringMode1s(ChoisePoint iX, Term value) {
		boolean mode= Converters.term2YesNo(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setVelocityMedianFilteringMode(mode);
	}
	//
	public void getVelocityMedianFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= Converters.boolean2YesNoTerm(imageSubtractor.get().getVelocityMedianFilteringMode());
	}
	public void getVelocityMedianFilteringMode0fs(ChoisePoint iX) {
	}
	// VelocityMedianFilterHalfwidth
	public void setVelocityMedianFilterHalfwidth1s(ChoisePoint iX, Term value) {
		int halfwidth= Converters.argumentToSmallRoundInteger(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setVelocityMedianFilterHalfwidth(halfwidth);
	}
	//
	public void getVelocityMedianFilterHalfwidth0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologInteger(imageSubtractor.get().getVelocityMedianFilterHalfwidth());
	}
	public void getVelocityMedianFilterHalfwidth0fs(ChoisePoint iX) {
	}
	// SlowTracksDeletionMode
	public void setSlowTracksDeletionMode1s(ChoisePoint iX, Term value) {
		boolean mode= Converters.term2YesNo(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setSlowTracksDeletionMode(mode);
	}
	//
	public void getSlowTracksDeletionMode0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= Converters.boolean2YesNoTerm(imageSubtractor.get().getSlowTracksDeletionMode());
	}
	public void getSlowTracksDeletionMode0fs(ChoisePoint iX) {
	}
	// FuzzyVelocityThreshold
	public void setFuzzyVelocityThreshold1s(ChoisePoint iX, Term value) {
		double threshold= Converters.argumentToReal(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setFuzzyVelocityThreshold(threshold);
	}
	//
	public void getFuzzyVelocityThreshold0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologReal(imageSubtractor.get().getFuzzyVelocityThreshold());
	}
	public void getFuzzyVelocityThreshold0fs(ChoisePoint iX) {
	}
	// FuzzyDistanceThreshold
	public void setFuzzyDistanceThreshold1s(ChoisePoint iX, Term value) {
		double threshold= Converters.argumentToReal(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setFuzzyDistanceThreshold(threshold);
	}
	//
	public void getFuzzyDistanceThreshold0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologReal(imageSubtractor.get().getFuzzyDistanceThreshold());
	}
	public void getFuzzyDistanceThreshold0fs(ChoisePoint iX) {
	}
	// FuzzyThresholdBorder
	public void setFuzzyThresholdBorder1s(ChoisePoint iX, Term value) {
		double threshold= Converters.argumentToReal(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setFuzzyThresholdBorder(threshold);
	}
	//
	public void getFuzzyThresholdBorder0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologReal(imageSubtractor.get().getFuzzyThresholdBorder());
	}
	public void getFuzzyThresholdBorder0fs(ChoisePoint iX) {
	}
	// SynthesizedImageTransparency
	public void setSynthesizedImageTransparency1s(ChoisePoint iX, Term value) {
		int interval= Converters.argumentToSmallRoundInteger(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setSynthesizedImageTransparency(interval);
	}
	//
	public void getSynthesizedImageTransparency0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= new PrologInteger(imageSubtractor.get().getSynthesizedImageTransparency());
	}
	public void getSynthesizedImageTransparency0fs(ChoisePoint iX) {
	}
	// SynthesizedImageRectangularBlobsMode
	public void setSynthesizedImageRectangularBlobsMode1s(ChoisePoint iX, Term value) {
		boolean mode= Converters.term2YesNo(value,iX);
		createImageSubtractorIfNecessary(iX);
		imageSubtractor.get().setSynthesizedImageRectangularBlobsMode(mode);
	}
	//
	public void getSynthesizedImageRectangularBlobsMode0ff(ChoisePoint iX, PrologVariable result) {
		createImageSubtractorIfNecessary(iX);
		result.value= Converters.boolean2YesNoTerm(imageSubtractor.get().getSynthesizedImageRectangularBlobsMode());
	}
	public void getSynthesizedImageRectangularBlobsMode0fs(ChoisePoint iX) {
	}
}
