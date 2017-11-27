// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
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

// import java.util.Calendar;
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
	// protected long recentFrameNumber= 0;
	//
	protected List<VPM_FrameCommand> actualFrameCommands= Collections.synchronizedList(new ArrayList<VPM_FrameCommand>());
	protected List<VPM_SnapshotCommand> actualSnapshotCommands= Collections.synchronizedList(new ArrayList<VPM_SnapshotCommand>());
	//
	protected ArrayList<VPM_FrameCommand> temporaryFrameCommands= new ArrayList<VPM_FrameCommand>();
	protected ArrayList<VPM_SnapshotCommand> temporarySnapshotCommands= new ArrayList<VPM_SnapshotCommand>();
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
	// MinimalTrainingInterval
	//
	public void setMinimalTrainingInterval(int frames, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMinimalTrainingInterval(frames);
	}
	public int getMinimalTrainingInterval(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMinimalTrainingInterval();
	}
	//
	// MaximalTrainingInterval
	//
	public void setMaximalTrainingInterval(int frames, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMaximalTrainingInterval(frames);
	}
	public int getMaximalTrainingInterval(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMaximalTrainingInterval();
	}
	//
	// HorizontalBlobBorder
	//
	public void setHorizontalBlobBorder(int size, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setHorizontalBlobBorder(size);
	}
	public int getHorizontalBlobBorder(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getHorizontalBlobBorder();
	}
	//
	// VerticalBlobBorder
	//
	public void setVerticalBlobBorder(int size, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setVerticalBlobBorder(size);
	}
	public int getVerticalBlobBorder(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getVerticalBlobBorder();
	}
	//
	// HorizontalExtraBorderCoefficient
	//
	public void setHorizontalExtraBorderCoefficient(double coefficient, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setHorizontalExtraBorderCoefficient(coefficient);
	}
	public double getHorizontalExtraBorderCoefficient(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getHorizontalExtraBorderCoefficient();
	}
	//
	// VerticalExtraBorderCoefficient
	//
	public void setVerticalExtraBorderCoefficient(double coefficient, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setVerticalExtraBorderCoefficient(coefficient);
	}
	public double getVerticalExtraBorderCoefficient(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getVerticalExtraBorderCoefficient();
	}
	//
	// MinimalBlobIntersectionArea
	//
	public void setMinimalBlobIntersectionArea(int size, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMinimalBlobIntersectionArea(size);
	}
	public int getMinimalBlobIntersectionArea(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMinimalBlobIntersectionArea();
	}
	//
	// MinimalBlobSize
	//
	public void setMinimalBlobSize(int size, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMinimalBlobSize(size);
	}
	public int getMinimalBlobSize(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMinimalBlobSize();
	}
	//
	// MinimalTrackDuration
	//
	public void setMinimalTrackDuration(int frames, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMinimalTrackDuration(frames);
	}
	public int getMinimalTrackDuration(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMinimalTrackDuration();
	}
	//
	// MaximalTrackDuration
	//
	public void setMaximalTrackDuration(int frames, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMaximalTrackDuration(frames);
	}
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
	public void setMaximalBlobInvisibilityInterval(int frames, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMaximalBlobInvisibilityInterval(frames);
	}
	public int getMaximalBlobInvisibilityInterval(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMaximalBlobInvisibilityInterval();
	}
	//
	// MaximalTrackRetentionInterval
	//
	public void setMaximalTrackRetentionInterval(int frames, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setMaximalTrackRetentionInterval(frames);
	}
	public int getMaximalTrackRetentionInterval(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getMaximalTrackRetentionInterval();
	}
	//
	// InverseTransformationMatrix
	//
	public void setInverseTransformationMatrix(double[][] matrix, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setInverseTransformationMatrix(matrix);
	}
	public double[][] getInverseTransformationMatrix(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getInverseTransformationMatrix();
	}
	// public byte[] getSerializedInverseTransformationMatrix(ChoisePoint iX) {
	//	createVideoProcessingMachineIfNecessary(iX);
	//	VPM machine= vpm.get();
	//	double[][] matrix= machine.getInverseTransformationMatrix();
	//	return Converters.serializeMatrix(matrix);
	// }
	//
	// SamplingRate
	//
	public void setSamplingRate(double rate, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setSamplingRate(rate);
	}
	public double getSamplingRate(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getSamplingRate();
	}
	//
	// R2WindowHalfwidth
	//
	public void setR2WindowHalfwidth(int halfwidth, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setR2WindowHalfwidth(halfwidth);
	}
	public int getR2WindowHalfwidth(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getR2WindowHalfwidth();
	}
	//
	// CharacteristicLengthMedianFilteringMode
	//
	public void setCharacteristicLengthMedianFilteringMode(boolean mode, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setCharacteristicLengthMedianFilteringMode(mode);
	}
	public boolean getCharacteristicLengthMedianFilteringMode(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getCharacteristicLengthMedianFilteringMode();
	}
	//
	// CharacteristicLengthMedianFilterHalfwidth
	//
	public void setCharacteristicLengthMedianFilterHalfwidth(int halfwidth, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setCharacteristicLengthMedianFilterHalfwidth(halfwidth);
	}
	public int getCharacteristicLengthMedianFilterHalfwidth(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getCharacteristicLengthMedianFilterHalfwidth();
	}
	//
	// VelocityMedianFilteringMode
	//
	public void setVelocityMedianFilteringMode(boolean mode, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setVelocityMedianFilteringMode(mode);
	}
	public boolean getVelocityMedianFilteringMode(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getVelocityMedianFilteringMode();
	}
	//
	// VelocityMedianFilterHalfwidth
	//
	public void setVelocityMedianFilterHalfwidth(int halfwidth, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setVelocityMedianFilterHalfwidth(halfwidth);
	}
	public int getVelocityMedianFilterHalfwidth(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getVelocityMedianFilterHalfwidth();
	}
	//
	// SynthesizedImageTransparency
	//
	public void setSynthesizedImageTransparency(int transparency, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setSynthesizedImageTransparency(transparency);
	}
	public int getSynthesizedImageTransparency(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getSynthesizedImageTransparency();
	}
	//
	// SynthesizedImageRectangularBlobsMode
	//
	public void setSynthesizedImageRectangularBlobsMode(boolean mode, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		machine.setSynthesizedImageRectangularBlobsMode(mode);
	}
	public boolean getSynthesizedImageRectangularBlobsMode(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		VPM machine= vpm.get();
		return machine.getSynthesizedImageRectangularBlobsMode();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	/*
	public void processNumberedFrame1s(ChoisePoint iX, Term a1) {
		processNumberedFrame(a1,null,true,iX);
	}
	public void processNumberedFrame2s(ChoisePoint iX, Term a1, Term a2) {
		processNumberedFrame(a1,a2,true,iX);
	}
	public void processNumberedFrame3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		boolean takeFrameIntoAccount= YesNo.termYesNo2Boolean(a3,iX);
		processNumberedFrame(a1,a2,takeFrameIntoAccount,iX);
	}
	//
	public void processRealtimeFrame1s(ChoisePoint iX, Term a1) {
		processRealtimeFrame(a1,null,true,iX);
	}
	public void processRealtimeFrame2s(ChoisePoint iX, Term a1, Term a2) {
		processRealtimeFrame(a1,a2,true,iX);
	}
	public void processRealtimeFrame3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		boolean takeFrameIntoAccount= YesNo.termYesNo2Boolean(a3,iX);
		processRealtimeFrame(a1,a2,takeFrameIntoAccount,iX);
	}
	*/
	//
	///////////////////////////////////////////////////////////////
	//
	/*
	protected void processNumberedFrame(Term a1, Term a2, boolean takeFrameIntoAccount, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		long frameNumber;
		if (a2 != null) {
			frameNumber= PrologInteger.toLong(Converters.argumentToRoundInteger(a2,iX));
		} else {
			recentFrameNumber++;
			frameNumber= recentFrameNumber;
		};
		GenericImageEncodingAttributes attributes= getCurrentImageEncodingAttributes();
		process(nativeImage,frameNumber,-1,takeFrameIntoAccount,iX,attributes);
	}
	protected void processRealtimeFrame(Term a1, Term a2, boolean takeFrameIntoAccount, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		long timeInMilliseconds;
		if (a2 != null) {
			timeInMilliseconds= PrologInteger.toLong(Converters.argumentToRoundInteger(a2,iX));
		} else {
			Calendar calendar= Calendar.getInstance();
			timeInMilliseconds= calendar.getTimeInMillis();
		};
		recentFrameNumber++;
		GenericImageEncodingAttributes attributes= getCurrentImageEncodingAttributes();
		process(nativeImage,recentFrameNumber,timeInMilliseconds,takeFrameIntoAccount,iX,attributes);
	}
	*/
	//
	public void process(java.awt.image.BufferedImage nativeImage, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		createVideoProcessingMachineIfNecessary(iX);
		if (nativeImage != null) {
			VPM machine= vpm.get();
			machine.processImage(nativeImage,frameNumber,timeInMilliseconds,takeFrameIntoAccount);
		}
	}
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
			int minimalTrainingInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_training_interval(),iX);
			int maximalTrainingInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_training_interval(),iX);
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
			int synthesizedImageTransparency= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_synthesized_image_transparency(),iX);
			boolean makeRectangularBlobsInSynthesizedImage= YesNo.termYesNo2Boolean(getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image(),iX);
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
		if (!createVideoProcessingMachineIfNecessary(iX)) {
			GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
			setCurrentImageEncodingAttributes(attributes);
			int minimalTrainingInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_minimal_training_interval(),iX);
			int maximalTrainingInterval= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_maximal_training_interval(),iX);
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
			int synthesizedImageTransparency= Converters.argumentToSmallRoundInteger(getBuiltInSlot_E_synthesized_image_transparency(),iX);
			boolean makeRectangularBlobsInSynthesizedImage= YesNo.termYesNo2Boolean(getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image(),iX);
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
	public Term getFrameNumberOrSpacer(ChoisePoint iX) {
		return new PrologInteger(getFrameNumber(iX));
	}
	public long getFrameNumber(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			return machine.getCommittedRecentFrameNumber();
		} else {
			return -1;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentImage(Term image, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= vpm.get().getCommittedRecentImage();
		modifyImage(image,nativeImage,iX);
	}
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
	public void getPreprocessedImage(Term image, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= vpm.get().getCommittedPreprocessedImage();
		modifyImage(image,nativeImage,iX);
	}
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
	public void getForegroundImage(Term image, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= vpm.get().getCommittedForegroundImage();
		modifyImage(image,nativeImage,iX);
	}
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
	public void getSynthesizedImage(Term image, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= vpm.get().getCommittedSynthesizedImage();
		modifyImage(image,nativeImage,iX);
	}
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
		int layerNumber= Converters.argumentToSmallRoundInteger(a2,iX);
		getBackgroundImage(a1,layerNumber,iX);
	}
	public void getBackgroundImage(Term image, int layerNumber, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= vpm.get().getBackgroundImage(layerNumber);
		modifyImage(image,nativeImage,iX);
	}
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
		int layerNumber= Converters.argumentToSmallRoundInteger(a2,iX);
		getSigmaImage(a1,layerNumber,iX);
	}
	public void getSigmaImage(Term image, int layerNumber, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		java.awt.image.BufferedImage nativeImage= vpm.get().getSigmaImage(layerNumber);
		modifyImage(image,nativeImage,iX);
	}
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
	public Term getBlobs(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		return vpm.get().getCommittedBlobs();
	}
	public byte[] getSerializedBlobs(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			Term blobs= machine.getCommittedBlobs();
			return Converters.serializeArgument(blobs);
		} else {
			return null;
		}
	}
	//
	public Term getTracks(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		return vpm.get().getCommittedTracks();
	}
	public byte[] getSerializedTracks(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			Term tracks= machine.getCommittedTracks();
			return Converters.serializeArgument(tracks);
		} else {
			return null;
		}
	}
	//
	public Term getChronicle(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		return vpm.get().getCommittedChronicle();
	}
	public byte[] getSerializedChronicle(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			Term tracks= machine.getCommittedChronicle();
			return Converters.serializeArgument(tracks);
		} else {
			return null;
		}
	}
	//
	public Term getConnectedGraphs(ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		return vpm.get().getCommittedConnectedGraphs();
	}
	public byte[] getSerializedConnectedGraphs(ChoisePoint iX) {
		VPM machine= vpm.get();
		if (machine != null) {
			Term tracks= machine.getCommittedConnectedGraphs();
			return Converters.serializeArgument(tracks);
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double[] physicalCoordinates(int pixelX, int pixelY, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		return vpm.get().physicalCoordinates(pixelX,pixelY);
	}
	//
	public double characteristicLength(int x, int y, ChoisePoint iX) {
		createVideoProcessingMachineIfNecessary(iX);
		return vpm.get().characteristicLength(x,y);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void imgGetSubimage4s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4) {
		int x= Converters.argumentToSmallInteger(a1,iX);
		int y= Converters.argumentToSmallInteger(a2,iX);
		int width= Converters.argumentToSmallInteger(a3,iX);
		int height= Converters.argumentToSmallInteger(a4,iX);
		appendFrameCommand(new VPMimgGetSubimage(x,y,width,height));
	}
	//
	public void imgResizeImage2s(ChoisePoint iX, Term a1, Term a2) {
		int width= Converters.argumentToSmallInteger(a1,iX);
		int height= Converters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMimgResizeImage(width,height));
	}
	//
	public void imgApplyGaussianFilter1s(ChoisePoint iX, Term a1) {
		int radius= Converters.argumentToSmallInteger(a1,iX);
		appendFrameCommand(new VPMimgApplyGaussianFilter(radius));
	}
	//
	public void imgWithdrawImagePreprocessing0s(ChoisePoint iX) {
		appendFrameCommand(new VPMimgWithdrawImagePreprocessing());
	}
	//
	///////////////////////////////////////////////////////////////
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
		int halfwidth= Converters.argumentToSmallInteger(a1,iX);
		appendFrameCommand(new VPMpxlSmoothImage(halfwidth));
	}
	//
	public void pxlNormalizePixels2s(ChoisePoint iX, Term a1, Term a2) {
		int minimalValue= Converters.argumentToSmallInteger(a1,iX);
		int maximalValue= Converters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMpxlNormalizePixels(minimalValue,maximalValue));
	}
	//
	public void imgWithdrawPixelPreprocessing0s(ChoisePoint iX) {
		appendFrameCommand(new VPMimgWithdrawPixelPreprocessing());
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
		int lowerBound= Converters.argumentToSmallInteger(a1,iX);
		int upperBound= Converters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMmskSelectForeground(null,lowerBound,upperBound));
	}
	public void mskSelectForeground3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		ImageChannelName channelName= ImageChannelName.argumentToImageChannelName(a1,iX);
		int lowerBound= Converters.argumentToSmallInteger(a2,iX);
		int upperBound= Converters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskSelectForeground(channelName,lowerBound,upperBound));
	}
	//
	public void mskAddForeground2s(ChoisePoint iX, Term a1, Term a2) {
		int lowerBound= Converters.argumentToSmallInteger(a1,iX);
		int upperBound= Converters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMmskAddForeground(null,lowerBound,upperBound));
	}
	public void mskAddForeground3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		ImageChannelName channelName= ImageChannelName.argumentToImageChannelName(a1,iX);
		int lowerBound= Converters.argumentToSmallInteger(a2,iX);
		int upperBound= Converters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskAddForeground(channelName,lowerBound,upperBound));
	}
	//
	public void mskExcludeForeground3s(ChoisePoint iX, Term a1, Term a2) {
		ImageChannelName channelName= ImageChannelName.argumentToImageChannelName(a1,iX);
		int lowerBound= Converters.argumentToSmallInteger(a1,iX);
		int upperBound= Converters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMmskExcludeForeground(null,lowerBound,upperBound));
	}
	public void mskExcludeForeground3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		ImageChannelName channelName= ImageChannelName.argumentToImageChannelName(a1,iX);
		int lowerBound= Converters.argumentToSmallInteger(a2,iX);
		int upperBound= Converters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskExcludeForeground(channelName,lowerBound,upperBound));
	}
	//
	public void mskFlipForeground2s(ChoisePoint iX, Term a1, Term a2) {
		int lowerBound= Converters.argumentToSmallInteger(a1,iX);
		int upperBound= Converters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMmskFlipForeground(null,lowerBound,upperBound));
	}
	public void mskFlipForeground3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		ImageChannelName channelName= ImageChannelName.argumentToImageChannelName(a1,iX);
		int lowerBound= Converters.argumentToSmallInteger(a2,iX);
		int upperBound= Converters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskFlipForeground(channelName,lowerBound,upperBound));
	}
	//
	public void mskSelectPolygon1s(ChoisePoint iX, Term a1) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(a1,iX);
		appendFrameCommand(new VPMmskSelectPolygon(arrays.xPoints,arrays.yPoints));
	}
	public void mskAddPolygon1s(ChoisePoint iX, Term a1) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(a1,iX);
		appendFrameCommand(new VPMmskAddPolygon(arrays.xPoints,arrays.yPoints));
	}
	public void mskExcludePolygon1s(ChoisePoint iX, Term a1) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(a1,iX);
		appendFrameCommand(new VPMmskExcludePolygon(arrays.xPoints,arrays.yPoints));
	}
	public void mskFlipPolygon1s(ChoisePoint iX, Term a1) {
		Point2DArrays arrays= Point2DArrays.argumentToPoint2DArrays(a1,iX);
		appendFrameCommand(new VPMmskFlipPolygon(arrays.xPoints,arrays.yPoints));
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
		double varianceFactor= Converters.argumentToReal(a1,iX);
		appendFrameCommand(new VPMmskSubtractGaussianBackground(varianceFactor,-1,-1));
	}
	public void mskSubtractGaussianBackground2s(ChoisePoint iX, Term a1, Term a2) {
		double varianceFactor= Converters.argumentToReal(a1,iX);
		int stabilityInterval= Converters.argumentToSmallInteger(a2,iX);
		appendFrameCommand(new VPMmskSubtractGaussianBackground(varianceFactor,stabilityInterval,-1));
	}
	public void mskSubtractGaussianBackground3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		double varianceFactor= Converters.argumentToReal(a1,iX);
		int stabilityInterval= Converters.argumentToSmallInteger(a2,iX);
		int reductionCoefficient= Converters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskSubtractGaussianBackground(varianceFactor,stabilityInterval,reductionCoefficient));
	}
	//
	public void mskSubtractNonparametricBackground2s(ChoisePoint iX, Term a1, Term a2) {
		int numberOfBins= Converters.argumentToSmallInteger(a1,iX);
		double alphaLevel= Converters.argumentToReal(a2,iX);
		appendFrameCommand(new VPMmskSubtractNonparametricBackground(numberOfBins,alphaLevel,-1,-1));
	}
	public void mskSubtractNonparametricBackground3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		int numberOfBins= Converters.argumentToSmallInteger(a1,iX);
		double alphaLevel= Converters.argumentToReal(a2,iX);
		int stabilityInterval= Converters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskSubtractNonparametricBackground(numberOfBins,alphaLevel,stabilityInterval,-1));
	}
	public void mskSubtractNonparametricBackground4s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4) {
		int numberOfBins= Converters.argumentToSmallInteger(a1,iX);
		double alphaLevel= Converters.argumentToReal(a2,iX);
		int stabilityInterval= Converters.argumentToSmallInteger(a3,iX);
		int reductionCoefficient= Converters.argumentToSmallInteger(a3,iX);
		appendFrameCommand(new VPMmskSubtractNonparametricBackground(numberOfBins,alphaLevel,stabilityInterval,reductionCoefficient));
	}
	//
	public void mskApplyRankFilter1s(ChoisePoint iX, Term a1) {
		int threshold= Converters.argumentToSmallInteger(a1,iX);
		appendFrameCommand(new VPMmskApplyRankFilter(threshold));
	}
	//
	public void mskDilateForeground0s(ChoisePoint iX) {
		appendFrameCommand(new VPMmskDilateForeground(1,DilationAlgorithm.PLAIN_DILATION));
	}
	public void mskDilateForeground1s(ChoisePoint iX, Term a1) {
		int halfwidth= Converters.argumentToSmallInteger(a1,iX);
		appendFrameCommand(new VPMmskDilateForeground(halfwidth,DilationAlgorithm.PLAIN_DILATION));
	}
	public void mskDilateForeground2s(ChoisePoint iX, Term a1, Term a2) {
		int halfwidth= Converters.argumentToSmallInteger(a1,iX);
		DilationAlgorithm algorithm= DilationAlgorithm.argumentToDilationAlgorithm(a2,iX);
		appendFrameCommand(new VPMmskDilateForeground(halfwidth,algorithm));
	}
	//
	public void mskErodeForeground0s(ChoisePoint iX) {
		appendFrameCommand(new VPMmskErodeForeground(1,ErodingAlgorithm.PLAIN_ERODING));
	}
	public void mskErodeForeground1s(ChoisePoint iX, Term a1) {
		int halfwidth= Converters.argumentToSmallInteger(a1,iX);
		appendFrameCommand(new VPMmskErodeForeground(halfwidth,ErodingAlgorithm.PLAIN_ERODING));
	}
	public void mskErodeForeground2s(ChoisePoint iX, Term a1, Term a2) {
		int halfwidth= Converters.argumentToSmallInteger(a1,iX);
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
		int numberOfBlobs= Converters.argumentToSmallRoundInteger(a1,iX);
		appendFrameCommand(new VPMblbSelectFrontBlobs(numberOfBlobs));
	}
	public void blbSelectFrontBlobs2s(ChoisePoint iX, Term a1, Term a2) {
		int numberOfBlobs= Converters.argumentToSmallRoundInteger(a1,iX);
		BlobSortingCriterion sortingCriterion= BlobSortingCriterion.argumentToBlobSortingCriterion(a2,iX);
		appendFrameCommand(new VPMblbSelectFrontBlobs(numberOfBlobs,sortingCriterion,SortingMode.DEFAULT));
	}
	public void blbSelectFrontBlobs3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		int numberOfBlobs= Converters.argumentToSmallRoundInteger(a1,iX);
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
		int numberOfBins= Converters.argumentToSmallRoundInteger(a1,iX);
		appendFrameCommand(new VPMblbComputeColorHistograms(numberOfBins));
	}
	//
	public void blbTrackBlobs0s(ChoisePoint iX) {
		appendFrameCommand(new VPMblbTrackBlobs());
	}
	//
	public void blbSetBlobBorders2s(ChoisePoint iX, Term a1, Term a2) {
		int borderX= Converters.argumentToSmallRoundInteger(a1,iX);
		int borderY= Converters.argumentToSmallRoundInteger(a2,iX);
		appendFrameCommand(new VPMblbSetBlobBorders(borderX,borderY));
	}
	public void blbSetExtraBorderCoefficients2s(ChoisePoint iX, Term a1, Term a2) {
		double coefX= Converters.argumentToReal(a1,iX);
		double coefY= Converters.argumentToReal(a2,iX);
		appendFrameCommand(new VPMblbSetExtraBorderCoefficients(coefX,coefY));
	}
	public void blbSetMinimalBlobIntersectionArea1s(ChoisePoint iX, Term a1) {
		int area= Converters.argumentToSmallRoundInteger(a1,iX);
		appendFrameCommand(new VPMblbSetMinimalBlobIntersectionArea(area));
	}
	public void blbSetMinimalBlobSize1s(ChoisePoint iX, Term a1) {
		int size= Converters.argumentToSmallRoundInteger(a1,iX);
		appendFrameCommand(new VPMblbSetMinimalBlobSize(size));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void trkSelectFrontTracks2s(ChoisePoint iX, Term a1, Term a2) {
		int numberOfTracks= Converters.argumentToSmallRoundInteger(a1,iX);
		TrackSortingCriterion sortingCriterion= TrackSortingCriterion.argumentToTrackSortingCriterion(a2,iX);
		appendSnapshotCommand(new VPMtrkSelectFrontTracks(numberOfTracks,sortingCriterion,SortingMode.DEFAULT));
	}
	public void trkSelectFrontTracks3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		int numberOfTracks= Converters.argumentToSmallRoundInteger(a1,iX);
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
		double velocityThreshold= Converters.argumentToReal(a1,iX);
		double distanceThreshold= Converters.argumentToReal(a2,iX);
		double fuzzyThresholdBorder= Converters.argumentToReal(a3,iX);
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
