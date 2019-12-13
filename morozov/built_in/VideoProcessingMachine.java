// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.commands.blb.*;
import morozov.system.vision.vpm.commands.img.*;
import morozov.system.vision.vpm.commands.msk.*;
import morozov.system.vision.vpm.commands.pxl.*;
import morozov.system.vision.vpm.commands.trk.*;
import morozov.system.vision.vpm.converters.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public abstract class VideoProcessingMachine
		extends BlobProcessingAttributes
		implements VideoProcessingMachineOperations {
	//
	protected AtomicReference<VPM> vpm= new AtomicReference<>(null);
	//
	protected Double brightnessHistogramTailCoefficient;
	protected Double colorHistogramTailCoefficient;
	//
	protected List<VPM_FrameCommand> actualFrameCommands= Collections.synchronizedList(new ArrayList<VPM_FrameCommand>());
	protected List<VPM_SnapshotCommand> actualSnapshotCommands= Collections.synchronizedList(new ArrayList<VPM_SnapshotCommand>());
	//
	protected ArrayList<VPM_FrameCommand> temporaryFrameCommands= new ArrayList<>();
	protected ArrayList<VPM_SnapshotCommand> temporarySnapshotCommands= new ArrayList<>();
	//
	protected boolean instructionListUpdateIsSuspended= false;
	//
	///////////////////////////////////////////////////////////////
	//
	public VideoProcessingMachine() {
	}
	public VideoProcessingMachine(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_brightness_histogram_tail_coefficient();
	abstract public Term getBuiltInSlot_E_color_histogram_tail_coefficient();
	//
	///////////////////////////////////////////////////////////////
	//
	// BrightnessHistogramTailCoefficient
	//
	public void setBrightnessHistogramTailCoefficient1s(ChoisePoint iX, Term a1) {
		setBrightnessHistogramTailCoefficient(GeneralConverters.argumentToReal(a1,iX));
	}
	public void setBrightnessHistogramTailCoefficient(double value) {
		brightnessHistogramTailCoefficient= value;
	}
	public void getBrightnessHistogramTailCoefficient0ff(ChoisePoint iX, PrologVariable result) {
		double value= getBrightnessHistogramTailCoefficient(iX);
		result.setNonBacktrackableValue(new PrologReal(value));
	}
	public void getBrightnessHistogramTailCoefficient0fs(ChoisePoint iX) {
	}
	public double getBrightnessHistogramTailCoefficient(ChoisePoint iX) {
		if (brightnessHistogramTailCoefficient != null) {
			return brightnessHistogramTailCoefficient;
		} else {
			Term value= getBuiltInSlot_E_brightness_histogram_tail_coefficient();
			return GeneralConverters.argumentToReal(value,iX);
		}
	}
	//
	// ColorHistogramTailCoefficient
	//
	public void setColorHistogramTailCoefficient1s(ChoisePoint iX, Term a1) {
		setColorHistogramTailCoefficient(GeneralConverters.argumentToReal(a1,iX));
	}
	public void setColorHistogramTailCoefficient(double value) {
		colorHistogramTailCoefficient= value;
	}
	public void getColorHistogramTailCoefficient0ff(ChoisePoint iX, PrologVariable result) {
		double value= getColorHistogramTailCoefficient(iX);
		result.setNonBacktrackableValue(new PrologReal(value));
	}
	public void getColorHistogramTailCoefficient0fs(ChoisePoint iX) {
	}
	public double getColorHistogramTailCoefficient(ChoisePoint iX) {
		if (colorHistogramTailCoefficient != null) {
			return colorHistogramTailCoefficient;
		} else {
			Term value= getBuiltInSlot_E_color_histogram_tail_coefficient();
			return GeneralConverters.argumentToReal(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// MinimalTrainingInterval
	//
	@Override
	public void setMinimalTrainingInterval(int frames, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMinimalTrainingInterval(frames);
	}
	@Override
	public int getMinimalTrainingInterval(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMinimalTrainingInterval();
	}
	//
	// MaximalTrainingInterval
	//
	@Override
	public void setMaximalTrainingInterval(int frames, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMaximalTrainingInterval(frames);
	}
	@Override
	public int getMaximalTrainingInterval(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMaximalTrainingInterval();
	}
	//
	// HorizontalBlobBorder
	//
	@Override
	public void setHorizontalBlobBorder(int size, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setHorizontalBlobBorder(size);
	}
	@Override
	public int getHorizontalBlobBorder(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getHorizontalBlobBorder();
	}
	//
	// VerticalBlobBorder
	//
	@Override
	public void setVerticalBlobBorder(int size, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setVerticalBlobBorder(size);
	}
	@Override
	public int getVerticalBlobBorder(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getVerticalBlobBorder();
	}
	//
	// HorizontalExtraBorderCoefficient
	//
	@Override
	public void setHorizontalExtraBorderCoefficient(double coefficient, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setHorizontalExtraBorderCoefficient(coefficient);
	}
	@Override
	public double getHorizontalExtraBorderCoefficient(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getHorizontalExtraBorderCoefficient();
	}
	//
	// VerticalExtraBorderCoefficient
	//
	@Override
	public void setVerticalExtraBorderCoefficient(double coefficient, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setVerticalExtraBorderCoefficient(coefficient);
	}
	@Override
	public double getVerticalExtraBorderCoefficient(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getVerticalExtraBorderCoefficient();
	}
	//
	// MinimalBlobIntersectionArea
	//
	@Override
	public void setMinimalBlobIntersectionArea(int size, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMinimalBlobIntersectionArea(size);
	}
	@Override
	public int getMinimalBlobIntersectionArea(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMinimalBlobIntersectionArea();
	}
	//
	// MinimalBlobSize
	//
	@Override
	public void setMinimalBlobSize(int size, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMinimalBlobSize(size);
	}
	@Override
	public int getMinimalBlobSize(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMinimalBlobSize();
	}
	//
	// MinimalTrackDuration
	//
	@Override
	public void setMinimalTrackDuration(int frames, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMinimalTrackDuration(frames);
	}
	@Override
	public int getMinimalTrackDuration(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMinimalTrackDuration();
	}
	//
	// MaximalTrackDuration
	//
	@Override
	public void setMaximalTrackDuration(int frames, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMaximalTrackDuration(frames);
	}
	@Override
	public int getMaximalTrackDuration(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMaximalTrackDuration();
	}
	//
	// MaximalChronicleLength
	//
	public void setMaximalChronicleLength(NumericalValue value, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMaximalChronicleLength(value);
	}
	public NumericalValue getMaximalChronicleLength(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMaximalChronicleLength();
	}
	//
	// MaximalBlobInvisibilityInterval
	//
	@Override
	public void setMaximalBlobInvisibilityInterval(int frames, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMaximalBlobInvisibilityInterval(frames);
	}
	@Override
	public int getMaximalBlobInvisibilityInterval(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMaximalBlobInvisibilityInterval();
	}
	//
	// MaximalTrackRetentionInterval
	//
	@Override
	public void setMaximalTrackRetentionInterval(int frames, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMaximalTrackRetentionInterval(frames);
	}
	@Override
	public int getMaximalTrackRetentionInterval(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMaximalTrackRetentionInterval();
	}
	//
	// InverseTransformationMatrix
	//
	@Override
	public void setInverseTransformationMatrix(double[][] matrix, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setInverseTransformationMatrix(matrix);
	}
	@Override
	public double[][] getInverseTransformationMatrix(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getInverseTransformationMatrix();
	}
	//
	// SamplingRate
	//
	@Override
	public void setSamplingRate(double rate, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setSamplingRate(rate);
	}
	@Override
	public double getSamplingRate(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getSamplingRate();
	}
	//
	// R2WindowHalfwidth
	//
	@Override
	public void setR2WindowHalfwidth(int halfwidth, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setR2WindowHalfwidth(halfwidth);
	}
	@Override
	public int getR2WindowHalfwidth(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getR2WindowHalfwidth();
	}
	//
	// CharacteristicLengthMedianFilteringMode
	//
	@Override
	public void setCharacteristicLengthMedianFilteringMode(boolean mode, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setCharacteristicLengthMedianFilteringMode(mode);
	}
	@Override
	public boolean getCharacteristicLengthMedianFilteringMode(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getCharacteristicLengthMedianFilteringMode();
	}
	//
	// CharacteristicLengthMedianFilterHalfwidth
	//
	@Override
	public void setCharacteristicLengthMedianFilterHalfwidth(int halfwidth, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setCharacteristicLengthMedianFilterHalfwidth(halfwidth);
	}
	@Override
	public int getCharacteristicLengthMedianFilterHalfwidth(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getCharacteristicLengthMedianFilterHalfwidth();
	}
	//
	// VelocityMedianFilteringMode
	//
	@Override
	public void setVelocityMedianFilteringMode(boolean mode, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setVelocityMedianFilteringMode(mode);
	}
	@Override
	public boolean getVelocityMedianFilteringMode(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getVelocityMedianFilteringMode();
	}
	//
	// VelocityMedianFilterHalfwidth
	//
	@Override
	public void setVelocityMedianFilterHalfwidth(int halfwidth, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setVelocityMedianFilterHalfwidth(halfwidth);
	}
	@Override
	public int getVelocityMedianFilterHalfwidth(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getVelocityMedianFilterHalfwidth();
	}
	//
	// SynthesizedImageTransparency
	//
	@Override
	public void setSynthesizedImageTransparency(int transparency, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setSynthesizedImageTransparency(transparency);
	}
	@Override
	public int getSynthesizedImageTransparency(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getSynthesizedImageTransparency();
	}
	//
	// SynthesizedImageRectangularBlobsMode
	//
	@Override
	public void setSynthesizedImageRectangularBlobsMode(boolean mode, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setSynthesizedImageRectangularBlobsMode(mode);
	}
	@Override
	public boolean getSynthesizedImageRectangularBlobsMode(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getSynthesizedImageRectangularBlobsMode();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void process(java.awt.image.BufferedImage nativeImage, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		createVideoProcessingMachineIfNecessary(iX);
		if (nativeImage != null) {
			VPM machine= vpm.get();
			machine.processImage(nativeImage,frameNumber,timeInMilliseconds,takeFrameIntoAccount);
		}
	}
	@Override
	public void process(byte[] bytes, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= Space2DWriter.bytesToImage(bytes);
		if (nativeImage != null) {
			VPM machine= vpm.get();
			machine.processImage(nativeImage,frameNumber,timeInMilliseconds,takeFrameIntoAccount);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean createVideoProcessingMachineIfNecessary(ChoisePoint iX) {
		if (vpm.get()==null) {
			synchronized (this) {
				return createVideoProcessingMachine(iX);
			}
		} else {
			return false;
		}
	}
	protected boolean createVideoProcessingMachine(ChoisePoint iX) {
		if (vpm.get()==null) {
			GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
			setCurrentImageEncodingAttributes(attributes);
			int minimalTrainingInterval= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_training_interval(),iX);
			int maximalTrainingInterval= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_training_interval(),iX);
			int horizontalBlobBorder= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_horizontal_blob_border(),iX);
			int verticalBlobBorder= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_vertical_blob_border(),iX);
			double horizontalExtraBorderCoefficient= GeneralConverters.argumentToReal(getBuiltInSlot_E_horizontal_extra_border_coefficient(),iX);
			double verticalExtraBorderCoefficient= GeneralConverters.argumentToReal(getBuiltInSlot_E_vertical_extra_border_coefficient(),iX);
			int minimalBlobIntersectionArea= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_intersection_area(),iX);
			int minimalBlobSize= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_size(),iX);
			int minimalTrackDuration= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_track_duration(),iX);
			int maximalTrackDuration= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_track_duration(),iX);
			NumericalValue maximalChronicleLength= NumericalValueConverters.argumentToNumericalValue(getBuiltInSlot_E_maximal_chronicle_length(),iX);
			int maximalBlobInvisibilityInterval= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_blob_invisibility_interval(),iX);
			int maximalTrackRetentionInterval= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_track_retention_interval(),iX);
			Term matrixValue= getBuiltInSlot_E_inverse_transformation_matrix();
			double[][] inverseMatrix= GeneralConverters.argumentToMatrix(matrixValue,iX);
			checkMatrix(inverseMatrix,matrixValue);
			double samplingRate= GeneralConverters.argumentToReal(getBuiltInSlot_E_sampling_rate(),iX);
			int r2WindowHalfwidth= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_r2_window_halfwidth(),iX);
			boolean applyCharacteristicLengthMedianFiltering= YesNoConverters.termYesNo2Boolean(getBuiltInSlot_E_apply_median_filtering_to_characteristic_length(),iX);
			int characteristicLengthMedianFilterHalfwidth= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_characteristic_length_median_filter_halfwidth(),iX);
			boolean applyVelocityMedianFiltering= YesNoConverters.termYesNo2Boolean(getBuiltInSlot_E_apply_median_filtering_to_velocity(),iX);
			int velocityMedianFilterHalfwidth= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_velocity_median_filter_halfwidth(),iX);
			int synthesizedImageTransparency= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_synthesized_image_transparency(),iX);
			boolean makeRectangularBlobsInSynthesizedImage= YesNoConverters.termYesNo2Boolean(getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image(),iX);
			vpm.set(new VPM(
				actualFrameCommands,
				actualSnapshotCommands,
				minimalTrainingInterval,
				maximalTrainingInterval,
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
	@Override
	public void commit(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		if (machine != null) {
			machine.commit();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void resetSettings(ChoisePoint iX) {
		reset(true,false,false,iX);
	}
	//
	@Override
	public void resetStatistics(ChoisePoint iX) {
		reset(false,true,false,iX);
	}
	@Override
	public void resetResults(ChoisePoint iX) {
		reset(false,false,true,iX);
	}
	//
	@Override
	public void resetAll(ChoisePoint iX) {
		reset(true,true,true,iX);
	}
	//
	protected void reset(boolean forgetSettings, boolean forgetStatistics, boolean forgetResults, ChoisePoint iX) {
		if (!createVideoProcessingMachineIfNecessary(iX)) {
			GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
			setCurrentImageEncodingAttributes(attributes);
			int minimalTrainingInterval= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_training_interval(),iX);
			int maximalTrainingInterval= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_training_interval(),iX);
			int horizontalBlobBorder= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_horizontal_blob_border(),iX);
			int verticalBlobBorder= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_vertical_blob_border(),iX);
			double horizontalExtraBorderCoefficient= GeneralConverters.argumentToReal(getBuiltInSlot_E_horizontal_extra_border_coefficient(),iX);
			double verticalExtraBorderCoefficient= GeneralConverters.argumentToReal(getBuiltInSlot_E_vertical_extra_border_coefficient(),iX);
			int minimalBlobIntersectionArea= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_intersection_area(),iX);
			int minimalBlobSize= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_blob_size(),iX);
			int minimalTrackDuration= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_track_duration(),iX);
			int maximalTrackDuration= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_track_duration(),iX);
			NumericalValue maximalChronicleLength= NumericalValueConverters.argumentToNumericalValue(getBuiltInSlot_E_maximal_chronicle_length(),iX);
			int maximalBlobInvisibilityInterval= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_blob_invisibility_interval(),iX);
			int maximalTrackRetentionInterval= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_track_retention_interval(),iX);
			Term matrixValue= getBuiltInSlot_E_inverse_transformation_matrix();
			double[][] inverseMatrix= GeneralConverters.argumentToMatrix(matrixValue,iX);
			checkMatrix(inverseMatrix,matrixValue);
			double samplingRate= GeneralConverters.argumentToReal(getBuiltInSlot_E_sampling_rate(),iX);
			int r2WindowHalfwidth= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_r2_window_halfwidth(),iX);
			boolean applyCharacteristicLengthMedianFiltering= YesNoConverters.termYesNo2Boolean(getBuiltInSlot_E_apply_median_filtering_to_characteristic_length(),iX);
			int characteristicLengthMedianFilterHalfwidth= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_characteristic_length_median_filter_halfwidth(),iX);
			boolean applyVelocityMedianFiltering= YesNoConverters.termYesNo2Boolean(getBuiltInSlot_E_apply_median_filtering_to_velocity(),iX);
			int velocityMedianFilterHalfwidth= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_velocity_median_filter_halfwidth(),iX);
			int synthesizedImageTransparency= GeneralConverters.argumentToSmallRoundInteger(getBuiltInSlot_E_synthesized_image_transparency(),iX);
			boolean makeRectangularBlobsInSynthesizedImage= YesNoConverters.termYesNo2Boolean(getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image(),iX);
			VPM machine= vpm.get();
			if (machine != null) {
				machine.reset(
					forgetSettings,
					forgetStatistics,
					forgetResults,
					minimalTrainingInterval,
					maximalTrainingInterval,
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
					synthesizedImageTransparency,
					makeRectangularBlobsInSynthesizedImage);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term getFrameNumberOrSpacer(ChoisePoint iX) {
		return new PrologInteger(getFrameNumber(iX));
	}
	@Override
	public long getFrameNumber(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			return machine.getCommittedRecentFrameNumber();
		} else {
			return -1;
		}
	}
	//
	@Override
	public Term getFrameTimeOrSpacer(ChoisePoint iX) {
		return new PrologInteger(getFrameTime(iX));
	}
	@Override
	public long getFrameTime(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			return machine.getCommittedRecentFrameTime();
		} else {
			return -1;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void getRecentImage(Term image, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= vpm.get().getCommittedRecentImage();
		modifyImage(image,nativeImage,iX);
	}
	@Override
	public byte[] getSerializedRecentImage(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			java.awt.image.BufferedImage nativeImage= machine.getCommittedRecentImage();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	@Override
	public void getPreprocessedImage(Term image, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= vpm.get().getCommittedPreprocessedImage();
		modifyImage(image,nativeImage,iX);
	}
	@Override
	public byte[] getSerializedPreprocessedImage(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			java.awt.image.BufferedImage nativeImage= machine.getCommittedPreprocessedImage();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	@Override
	public void getForegroundImage(Term image, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= vpm.get().getCommittedForegroundImage();
		modifyImage(image,nativeImage,iX);
	}
	@Override
	public byte[] getSerializedForegroundImage(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			java.awt.image.BufferedImage nativeImage= machine.getCommittedForegroundImage();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	@Override
	public void getSynthesizedImage(Term image, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= vpm.get().getCommittedSynthesizedImage();
		modifyImage(image,nativeImage,iX);
	}
	@Override
	public byte[] getSerializedSynthesizedImage(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		if (machine != null) {
			java.awt.image.BufferedImage nativeImage= machine.getCommittedSynthesizedImage();
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getBackgroundImage2s(ChoisePoint iX, Term a1, Term a2) {
		int layerNumber= GeneralConverters.argumentToSmallRoundInteger(a2,iX);
		getBackgroundImage(a1,layerNumber,iX);
	}
	@Override
	public void getBackgroundImage(Term image, int layerNumber, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= vpm.get().getBackgroundImage(layerNumber);
		modifyImage(image,nativeImage,iX);
	}
	@Override
	public byte[] getSerializedBackgroundImage(int layerNumber, ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			java.awt.image.BufferedImage nativeImage= machine.getBackgroundImage(layerNumber);
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	public void getSigmaImage2s(ChoisePoint iX, Term a1, Term a2) {
		int layerNumber= GeneralConverters.argumentToSmallRoundInteger(a2,iX);
		getSigmaImage(a1,layerNumber,iX);
	}
	@Override
	public void getSigmaImage(Term image, int layerNumber, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= vpm.get().getSigmaImage(layerNumber);
		modifyImage(image,nativeImage,iX);
	}
	@Override
	public byte[] getSerializedSigmaImage(int layerNumber, ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			java.awt.image.BufferedImage nativeImage= machine.getSigmaImage(layerNumber);
			return convertImageToBytes(nativeImage);
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term getBlobs(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		return vpm.get().getCommittedBlobs();
	}
	@Override
	public byte[] getSerializedBlobs(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			Term blobs= machine.getCommittedBlobs();
			return GeneralConverters.serializeArgument(blobs);
		} else {
			return null;
		}
	}
	//
	@Override
	public Term getTracks(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		return vpm.get().getCommittedTracks();
	}
	@Override
	public byte[] getSerializedTracks(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			Term tracks= machine.getCommittedTracks();
			return GeneralConverters.serializeArgument(tracks);
		} else {
			return null;
		}
	}
	//
	@Override
	public Term getChronicle(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		return vpm.get().getCommittedChronicle();
	}
	@Override
	public byte[] getSerializedChronicle(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			Term tracks= machine.getCommittedChronicle();
			return GeneralConverters.serializeArgument(tracks);
		} else {
			return null;
		}
	}
	//
	@Override
	public Term getConnectedGraphs(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		return vpm.get().getCommittedConnectedGraphs();
	}
	@Override
	public byte[] getSerializedConnectedGraphs(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			Term tracks= machine.getCommittedConnectedGraphs();
			return GeneralConverters.serializeArgument(tracks);
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public double[] physicalCoordinates(int pixelX, int pixelY, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		return vpm.get().physicalCoordinates(pixelX,pixelY);
	}
	//
	@Override
	public double characteristicLength(int x, int y, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		return vpm.get().characteristicLength(x,y);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void imgGetSubimage4s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4) {
		int currentX= GeneralConverters.argumentToSmallInteger(a1,iX);
		int currentY= GeneralConverters.argumentToSmallInteger(a2,iX);
		int currentWidth= GeneralConverters.argumentToSmallInteger(a3,iX);
		int currentHeight= GeneralConverters.argumentToSmallInteger(a4,iX);
		appendFrameCommand(new VPMimgGetSubimage(currentX,currentY,currentWidth,currentHeight));
	}
	//
	public void imgResizeImage2s(ChoisePoint iX, Term a1, Term a2) {
		int currentWidth= GeneralConverters.argumentToSmallInteger(a1,iX);
		int currentHeight= GeneralConverters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMimgResizeImage(currentWidth,currentHeight));
	}
	//
	public void imgApplyGaussianFilter1s(ChoisePoint iX, Term a1) {
		int radius= GeneralConverters.argumentToSmallInteger(a1,iX);
		appendFrameCommand(new VPMimgApplyGaussianFilter(radius));
	}
	//
	public void imgWithdrawImagePreprocessing0s(ChoisePoint iX) {
		appendFrameCommand(new VPMimgWithdrawImagePreprocessing());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void pxlAdjustContrast0s(ChoisePoint iX) {
		double coefficient= getBrightnessHistogramTailCoefficient(iX);
		appendFrameCommand(new VPMpxlAdjustContrast(coefficient));
	}
	public void pxlAdjustContrast1s(ChoisePoint iX, Term a1) {
		double quantile= GeneralConverters.argumentToReal(a1,iX);
		appendFrameCommand(new VPMpxlAdjustContrast(quantile));
	}
	public void pxlAdjustContrast2s(ChoisePoint iX, Term a1, Term a2) {
		int minimalValue= GeneralConverters.argumentToSmallInteger(a1,iX);
		int maximalValue= GeneralConverters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMpxlAdjustContrast(minimalValue,maximalValue));
	}
	//
	public void pxlAdjustLevels0s(ChoisePoint iX) {
		double coefficient= getColorHistogramTailCoefficient(iX);
		appendFrameCommand(new VPMpxlAdjustLevels(coefficient));
	}
	public void pxlAdjustLevels1s(ChoisePoint iX, Term a1) {
		double quantile= GeneralConverters.argumentToReal(a1,iX);
		appendFrameCommand(new VPMpxlAdjustLevels(quantile));
	}
	//
	public void pxlInvertColors0s(ChoisePoint iX) {
		appendFrameCommand(new VPMpxlInvertColors());
	}
	//
	public void pxlSelectImageChannel1s(ChoisePoint iX, Term a1) {
		ImageChannelName channelName= ImageChannelName.argumentToImageChannelName(a1,iX);
		appendFrameCommand(new VPMpxlSelectImageChannel(channelName));
	}
	//
	public void pxlComputeGradient1s(ChoisePoint iX, Term a1) {
		GradientComputationMode mode= GradientComputationMode.argumentToGradientComputationMode(a1,iX);
		appendFrameCommand(new VPMpxlComputeGradient(mode));
	}
	//
	public void pxlSmoothImage1s(ChoisePoint iX, Term a1) {
		int halfwidth= GeneralConverters.argumentToSmallInteger(a1,iX);
		appendFrameCommand(new VPMpxlSmoothImage(halfwidth));
	}
	//
	public void pxlNormalizePixels2s(ChoisePoint iX, Term a1, Term a2) {
		int minimalValue= GeneralConverters.argumentToSmallInteger(a1,iX);
		int maximalValue= GeneralConverters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMpxlNormalizePixels(minimalValue,maximalValue));
	}
	//
	public void pxlWithdrawPixelPreprocessing0s(ChoisePoint iX) {
		appendFrameCommand(new VPMpxlWithdrawPixelPreprocessing());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void mskPushForeground0s(ChoisePoint iX) {
		appendFrameCommand(new VPMmskPushForeground(PushOperationMode.DUPLICATE_FOREGROUND));
	}
	public void mskPushForeground1s(ChoisePoint iX, Term a1) {
		PushOperationMode mode= PushOperationMode.argumentToPushOperationMode(a1,iX);
		appendFrameCommand(new VPMmskPushForeground(mode));
	}
	//
	public void mskPopForeground0s(ChoisePoint iX) {
		appendFrameCommand(new VPMmskPopForeground(PopOperationMode.FORGET_FOREGROUND));
	}
	public void mskPopForeground1s(ChoisePoint iX, Term a1) {
		PopOperationMode mode= PopOperationMode.argumentToPopOperationMode(a1,iX);
		appendFrameCommand(new VPMmskPopForeground(mode));
	}
	//
	public void mskSelectForeground2s(ChoisePoint iX, Term a1, Term a2) {
		int lowerBound= GeneralConverters.argumentToSmallInteger(a1,iX);
		int upperBound= GeneralConverters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMmskSelectForeground(null,lowerBound,upperBound));
	}
	public void mskSelectForeground3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		ImageChannelName channelName= ImageChannelName.argumentToImageChannelName(a1,iX);
		int lowerBound= GeneralConverters.argumentToSmallInteger(a2,iX);
		int upperBound= GeneralConverters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskSelectForeground(channelName,lowerBound,upperBound));
	}
	//
	public void mskAddForeground2s(ChoisePoint iX, Term a1, Term a2) {
		int lowerBound= GeneralConverters.argumentToSmallInteger(a1,iX);
		int upperBound= GeneralConverters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMmskAddForeground(null,lowerBound,upperBound));
	}
	public void mskAddForeground3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		ImageChannelName channelName= ImageChannelName.argumentToImageChannelName(a1,iX);
		int lowerBound= GeneralConverters.argumentToSmallInteger(a2,iX);
		int upperBound= GeneralConverters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskAddForeground(channelName,lowerBound,upperBound));
	}
	//
	public void mskExcludeForeground3s(ChoisePoint iX, Term a1, Term a2) {
		ImageChannelName channelName= ImageChannelName.argumentToImageChannelName(a1,iX);
		int lowerBound= GeneralConverters.argumentToSmallInteger(a1,iX);
		int upperBound= GeneralConverters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMmskExcludeForeground(null,lowerBound,upperBound));
	}
	public void mskExcludeForeground3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		ImageChannelName channelName= ImageChannelName.argumentToImageChannelName(a1,iX);
		int lowerBound= GeneralConverters.argumentToSmallInteger(a2,iX);
		int upperBound= GeneralConverters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskExcludeForeground(channelName,lowerBound,upperBound));
	}
	//
	public void mskFlipForeground2s(ChoisePoint iX, Term a1, Term a2) {
		int lowerBound= GeneralConverters.argumentToSmallInteger(a1,iX);
		int upperBound= GeneralConverters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMmskFlipForeground(null,lowerBound,upperBound));
	}
	public void mskFlipForeground3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		ImageChannelName channelName= ImageChannelName.argumentToImageChannelName(a1,iX);
		int lowerBound= GeneralConverters.argumentToSmallInteger(a2,iX);
		int upperBound= GeneralConverters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskFlipForeground(channelName,lowerBound,upperBound));
	}
	//
	public void mskSelectPolygon1s(ChoisePoint iX, Term a1) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(a1,iX);
		appendFrameCommand(new VPMmskSelectPolygon(arrays.getXPoints(),arrays.getYPoints(),false));
	}
	public void mskSelectPolygon2s(ChoisePoint iX, Term a1, Term a2) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(a1,iX);
		YesNo useStandardizedCoordinates= YesNoConverters.argument2YesNo(a2,iX);
		appendFrameCommand(new VPMmskSelectPolygon(arrays.getXPoints(),arrays.getYPoints(),useStandardizedCoordinates.toBoolean()));
	}
	public void mskAddPolygon1s(ChoisePoint iX, Term a1) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(a1,iX);
		appendFrameCommand(new VPMmskAddPolygon(arrays.getXPoints(),arrays.getYPoints(),false));
	}
	public void mskAddPolygon2s(ChoisePoint iX, Term a1, Term a2) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(a1,iX);
		YesNo useStandardizedCoordinates= YesNoConverters.argument2YesNo(a2,iX);
		appendFrameCommand(new VPMmskAddPolygon(arrays.getXPoints(),arrays.getYPoints(),useStandardizedCoordinates.toBoolean()));
	}
	public void mskExcludePolygon1s(ChoisePoint iX, Term a1) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(a1,iX);
		appendFrameCommand(new VPMmskExcludePolygon(arrays.getXPoints(),arrays.getYPoints(),false));
	}
	public void mskExcludePolygon2s(ChoisePoint iX, Term a1, Term a2) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(a1,iX);
		YesNo useStandardizedCoordinates= YesNoConverters.argument2YesNo(a2,iX);
		appendFrameCommand(new VPMmskExcludePolygon(arrays.getXPoints(),arrays.getYPoints(),useStandardizedCoordinates.toBoolean()));
	}
	public void mskFlipPolygon1s(ChoisePoint iX, Term a1) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(a1,iX);
		appendFrameCommand(new VPMmskFlipPolygon(arrays.getXPoints(),arrays.getYPoints(),false));
	}
	public void mskFlipPolygon2s(ChoisePoint iX, Term a1, Term a2) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(a1,iX);
		YesNo useStandardizedCoordinates= YesNoConverters.argument2YesNo(a2,iX);
		appendFrameCommand(new VPMmskFlipPolygon(arrays.getXPoints(),arrays.getYPoints(),useStandardizedCoordinates.toBoolean()));
	}
	//
	public void mskSelectAll0s(ChoisePoint iX) {
		appendFrameCommand(new VPMmskSelectAll());
	}
	public void mskDeselectAll0s(ChoisePoint iX) {
		appendFrameCommand(new VPMmskDeselectAll());
	}
	//
	public void mskSubtractGaussianBackground1s(ChoisePoint iX, Term a1) {
		double varianceFactor= GeneralConverters.argumentToReal(a1,iX);
		appendFrameCommand(new VPMmskSubtractGaussianBackground(varianceFactor,-1,-1));
	}
	public void mskSubtractGaussianBackground2s(ChoisePoint iX, Term a1, Term a2) {
		double varianceFactor= GeneralConverters.argumentToReal(a1,iX);
		int stabilityInterval= GeneralConverters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMmskSubtractGaussianBackground(varianceFactor,stabilityInterval,-1));
	}
	public void mskSubtractGaussianBackground3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		double varianceFactor= GeneralConverters.argumentToReal(a1,iX);
		int stabilityInterval= GeneralConverters.argumentToSmallInteger(a2,iX);
		int reductionCoefficient= GeneralConverters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskSubtractGaussianBackground(varianceFactor,stabilityInterval,reductionCoefficient));
	}
	//
	public void mskSubtractNonparametricBackground2s(ChoisePoint iX, Term a1, Term a2) {
		int numberOfBins= GeneralConverters.argumentToSmallInteger(a1,iX);
		double alphaLevel= GeneralConverters.argumentToReal(a2,iX);
		appendFrameCommand(new VPMmskSubtractNonparametricBackground(numberOfBins,alphaLevel,-1,-1));
	}
	public void mskSubtractNonparametricBackground3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		int numberOfBins= GeneralConverters.argumentToSmallInteger(a1,iX);
		double alphaLevel= GeneralConverters.argumentToReal(a2,iX);
		int stabilityInterval= GeneralConverters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskSubtractNonparametricBackground(numberOfBins,alphaLevel,stabilityInterval,-1));
	}
	public void mskSubtractNonparametricBackground4s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4) {
		int numberOfBins= GeneralConverters.argumentToSmallInteger(a1,iX);
		double alphaLevel= GeneralConverters.argumentToReal(a2,iX);
		int stabilityInterval= GeneralConverters.argumentToSmallInteger(a3,iX);
		int reductionCoefficient= GeneralConverters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskSubtractNonparametricBackground(numberOfBins,alphaLevel,stabilityInterval,reductionCoefficient));
	}
	//
	public void mskApplyRankFilter1s(ChoisePoint iX, Term a1) {
		int threshold= GeneralConverters.argumentToSmallInteger(a1,iX);
		appendFrameCommand(new VPMmskApplyRankFilter(threshold));
	}
	//
	public void mskDilateForeground0s(ChoisePoint iX) {
		appendFrameCommand(new VPMmskDilateForeground(1,DilationAlgorithm.PLAIN_DILATION));
	}
	public void mskDilateForeground1s(ChoisePoint iX, Term a1) {
		int halfwidth= GeneralConverters.argumentToSmallInteger(a1,iX);
		appendFrameCommand(new VPMmskDilateForeground(halfwidth,DilationAlgorithm.PLAIN_DILATION));
	}
	public void mskDilateForeground2s(ChoisePoint iX, Term a1, Term a2) {
		int halfwidth= GeneralConverters.argumentToSmallInteger(a1,iX);
		DilationAlgorithm algorithm= DilationAlgorithm.argumentToDilationAlgorithm(a2,iX);
		appendFrameCommand(new VPMmskDilateForeground(halfwidth,algorithm));
	}
	//
	public void mskErodeForeground0s(ChoisePoint iX) {
		appendFrameCommand(new VPMmskErodeForeground(1,ErodingAlgorithm.PLAIN_ERODING));
	}
	public void mskErodeForeground1s(ChoisePoint iX, Term a1) {
		int halfwidth= GeneralConverters.argumentToSmallInteger(a1,iX);
		appendFrameCommand(new VPMmskErodeForeground(halfwidth,ErodingAlgorithm.PLAIN_ERODING));
	}
	public void mskErodeForeground2s(ChoisePoint iX, Term a1, Term a2) {
		int halfwidth= GeneralConverters.argumentToSmallInteger(a1,iX);
		ErodingAlgorithm algorithm= ErodingAlgorithm.argumentToErodingAlgorithm(a2,iX);
		appendFrameCommand(new VPMmskErodeForeground(halfwidth,algorithm));
	}
	//
	public void mskContourForeground0s(ChoisePoint iX) {
		appendFrameCommand(new VPMmskContourForeground());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void blbExtractBlobs2s(ChoisePoint iX, Term a1, Term a2) {
		BlobType blobType= BlobType.argumentToBlobType(a1,iX);
		BlobExtractionAlgorithm algorithm= BlobExtractionAlgorithm.argumentToBlobExtractionAlgorithm(a2,iX);
		appendFrameCommand(new VPMblbExtractBlobs(blobType,algorithm));
	}
	//
	public void blbSortBlobsBy1s(ChoisePoint iX, Term a1) {
		BlobSortingCriterion sortingCriterion= BlobSortingCriterion.argumentToBlobSortingCriterion(a1,iX);
		appendFrameCommand(new VPMblbSortBlobsBy(sortingCriterion,SortingMode.DEFAULT));
	}
	public void blbSortBlobsBy2s(ChoisePoint iX, Term a1, Term a2) {
		BlobSortingCriterion sortingCriterion= BlobSortingCriterion.argumentToBlobSortingCriterion(a1,iX);
		SortingMode sortingMode= SortingMode.argumentToSortingMode(a2,iX);
		appendFrameCommand(new VPMblbSortBlobsBy(sortingCriterion,sortingMode));
	}
	//
	public void blbSelectFrontBlobs1s(ChoisePoint iX, Term a1) {
		int numberOfBlobs= GeneralConverters.argumentToSmallRoundInteger(a1,iX);
		appendFrameCommand(new VPMblbSelectFrontBlobs(numberOfBlobs));
	}
	public void blbSelectFrontBlobs2s(ChoisePoint iX, Term a1, Term a2) {
		int numberOfBlobs= GeneralConverters.argumentToSmallRoundInteger(a1,iX);
		BlobSortingCriterion sortingCriterion= BlobSortingCriterion.argumentToBlobSortingCriterion(a2,iX);
		appendFrameCommand(new VPMblbSelectFrontBlobs(numberOfBlobs,sortingCriterion,SortingMode.DEFAULT));
	}
	public void blbSelectFrontBlobs3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		int numberOfBlobs= GeneralConverters.argumentToSmallRoundInteger(a1,iX);
		BlobSortingCriterion sortingCriterion= BlobSortingCriterion.argumentToBlobSortingCriterion(a2,iX);
		SortingMode sortingMode= SortingMode.argumentToSortingMode(a3,iX);
		appendFrameCommand(new VPMblbSelectFrontBlobs(numberOfBlobs,sortingCriterion,sortingMode));
	}
	//
	public void blbSelectSuperiorBlob1s(ChoisePoint iX, Term a1) {
		BlobSortingCriterion sortingCriterion= BlobSortingCriterion.argumentToBlobSortingCriterion(a1,iX);
		appendFrameCommand(new VPMblbSelectSuperiorBlob(sortingCriterion,SortingMode.DEFAULT));
	}
	public void blbSelectSuperiorBlob2s(ChoisePoint iX, Term a1, Term a2) {
		BlobSortingCriterion sortingCriterion= BlobSortingCriterion.argumentToBlobSortingCriterion(a1,iX);
		SortingMode sortingMode= SortingMode.argumentToSortingMode(a2,iX);
		appendFrameCommand(new VPMblbSelectSuperiorBlob(sortingCriterion,sortingMode));
	}
	//
	public void blbFillBlobs0s(ChoisePoint iX) {
		appendFrameCommand(new VPMblbFillBlobs());
	}
	//
	public void blbComputeColorHistograms0s(ChoisePoint iX) {
		appendFrameCommand(new VPMblbComputeColorHistograms(-1));
	}
	public void blbComputeColorHistograms1s(ChoisePoint iX, Term a1) {
		int numberOfBins= GeneralConverters.argumentToSmallRoundInteger(a1,iX);
		appendFrameCommand(new VPMblbComputeColorHistograms(numberOfBins));
	}
	//
	public void blbTrackBlobs0s(ChoisePoint iX) {
		appendFrameCommand(new VPMblbTrackBlobs());
	}
	//
	public void blbSetBlobBorders2s(ChoisePoint iX, Term a1, Term a2) {
		int borderX= GeneralConverters.argumentToSmallRoundInteger(a1,iX);
		int borderY= GeneralConverters.argumentToSmallRoundInteger(a2,iX);
		appendFrameCommand(new VPMblbSetBlobBorders(borderX,borderY));
	}
	public void blbSetExtraBorderCoefficients2s(ChoisePoint iX, Term a1, Term a2) {
		double coefX= GeneralConverters.argumentToReal(a1,iX);
		double coefY= GeneralConverters.argumentToReal(a2,iX);
		appendFrameCommand(new VPMblbSetExtraBorderCoefficients(coefX,coefY));
	}
	public void blbSetMinimalBlobIntersectionArea1s(ChoisePoint iX, Term a1) {
		int area= GeneralConverters.argumentToSmallRoundInteger(a1,iX);
		appendFrameCommand(new VPMblbSetMinimalBlobIntersectionArea(area));
	}
	public void blbSetMinimalBlobSize1s(ChoisePoint iX, Term a1) {
		int size= GeneralConverters.argumentToSmallRoundInteger(a1,iX);
		appendFrameCommand(new VPMblbSetMinimalBlobSize(size));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void trkSelectFrontTracks2s(ChoisePoint iX, Term a1, Term a2) {
		int numberOfTracks= GeneralConverters.argumentToSmallRoundInteger(a1,iX);
		TrackSortingCriterion sortingCriterion= TrackSortingCriterion.argumentToTrackSortingCriterion(a2,iX);
		appendSnapshotCommand(new VPMtrkSelectFrontTracks(numberOfTracks,sortingCriterion,SortingMode.DEFAULT));
	}
	public void trkSelectFrontTracks3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		int numberOfTracks= GeneralConverters.argumentToSmallRoundInteger(a1,iX);
		TrackSortingCriterion sortingCriterion= TrackSortingCriterion.argumentToTrackSortingCriterion(a2,iX);
		SortingMode sortingMode= SortingMode.argumentToSortingMode(a3,iX);
		appendSnapshotCommand(new VPMtrkSelectFrontTracks(numberOfTracks,sortingCriterion,sortingMode));
	}
	//
	public void trkSelectSuperiorTrack1s(ChoisePoint iX, Term a1) {
		TrackSortingCriterion sortingCriterion= TrackSortingCriterion.argumentToTrackSortingCriterion(a1,iX);
		appendSnapshotCommand(new VPMtrkSelectSuperiorTrack(sortingCriterion,SortingMode.DEFAULT));
	}
	public void trkSelectSuperiorTrack2s(ChoisePoint iX, Term a1, Term a2) {
		TrackSortingCriterion sortingCriterion= TrackSortingCriterion.argumentToTrackSortingCriterion(a1,iX);
		SortingMode sortingMode= SortingMode.argumentToSortingMode(a2,iX);
		appendSnapshotCommand(new VPMtrkSelectSuperiorTrack(sortingCriterion,sortingMode));
	}
	//
	public void trkRefuseSlowTracks3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		double velocityThreshold= GeneralConverters.argumentToReal(a1,iX);
		double distanceThreshold= GeneralConverters.argumentToReal(a2,iX);
		double fuzzyThresholdBorder= GeneralConverters.argumentToReal(a3,iX);
		appendSnapshotCommand(new VPMtrkRefuseSlowTracks(velocityThreshold,distanceThreshold,fuzzyThresholdBorder));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void appendFrameCommand(VPM_FrameCommand command) {
		if (!instructionListUpdateIsSuspended) {
			actualFrameCommands.add(command);
			VPM machine= vpm.get();
			if (machine != null) {
				machine.updateActualFrameCommands(actualFrameCommands);
			}
		} else {
			temporaryFrameCommands.add(command);
		}
	}
	protected void appendSnapshotCommand(VPM_SnapshotCommand command) {
		if (!instructionListUpdateIsSuspended) {
			actualSnapshotCommands.add(command);
			VPM machine= vpm.get();
			if (machine != null) {
				machine.updateActualSnapshotCommands(actualSnapshotCommands);
			}
		} else {
			temporarySnapshotCommands.add(command);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void retractAllInstructions0s(ChoisePoint iX) {
		retractAllInstructions(iX);
	}
	public void retractAllInstructions(ChoisePoint iX) {
		if (!instructionListUpdateIsSuspended) {
			actualFrameCommands.clear();
			actualSnapshotCommands.clear();
		} else {
			temporaryFrameCommands.clear();
			temporarySnapshotCommands.clear();
		}
	}
	//
	public void suspendProcessing0s(ChoisePoint iX) {
		suspendProcessing(iX);
	}
	public void suspendProcessing(ChoisePoint iX) {
		if (!instructionListUpdateIsSuspended) {
			temporaryFrameCommands.clear();
			temporarySnapshotCommands.clear();
			temporaryFrameCommands.addAll(actualFrameCommands);
			temporarySnapshotCommands.addAll(actualSnapshotCommands);
			instructionListUpdateIsSuspended= true;
		}
	}
	//
	public void processNow0s(ChoisePoint iX) {
		processNow(iX);
	}
	public void processNow(ChoisePoint iX) {
		if (instructionListUpdateIsSuspended) {
			actualFrameCommands.clear();
			actualSnapshotCommands.clear();
			actualFrameCommands.addAll(temporaryFrameCommands);
			actualSnapshotCommands.addAll(temporarySnapshotCommands);
			instructionListUpdateIsSuspended= false;
			VPM machine= vpm.get();
			if (machine != null) {
				machine.updateActualCommands(actualFrameCommands,actualSnapshotCommands);
			}
		}
	}
}
