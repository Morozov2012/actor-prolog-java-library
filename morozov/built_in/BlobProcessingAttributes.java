// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.terms.*;
import morozov.worlds.*;

public abstract class BlobProcessingAttributes extends GenericVideoProcessor {
	//
	abstract public Term getBuiltInSlot_E_minimal_training_interval();
	abstract public Term getBuiltInSlot_E_maximal_training_interval();
	abstract public Term getBuiltInSlot_E_horizontal_blob_border();
	abstract public Term getBuiltInSlot_E_vertical_blob_border();
	abstract public Term getBuiltInSlot_E_horizontal_extra_border_coefficient();
	abstract public Term getBuiltInSlot_E_vertical_extra_border_coefficient();
	abstract public Term getBuiltInSlot_E_minimal_blob_intersection_area();
	abstract public Term getBuiltInSlot_E_minimal_blob_size();
	abstract public Term getBuiltInSlot_E_minimal_track_duration();
	abstract public Term getBuiltInSlot_E_maximal_track_duration();
	abstract public Term getBuiltInSlot_E_maximal_chronicle_length();
	abstract public Term getBuiltInSlot_E_maximal_blob_invisibility_interval();
	abstract public Term getBuiltInSlot_E_maximal_track_retention_interval();
	abstract public Term getBuiltInSlot_E_inverse_transformation_matrix();
	abstract public Term getBuiltInSlot_E_sampling_rate();
	abstract public Term getBuiltInSlot_E_r2_window_halfwidth();
	abstract public Term getBuiltInSlot_E_apply_median_filtering_to_characteristic_length();
	abstract public Term getBuiltInSlot_E_characteristic_length_median_filter_halfwidth();
	abstract public Term getBuiltInSlot_E_apply_median_filtering_to_velocity();
	abstract public Term getBuiltInSlot_E_velocity_median_filter_halfwidth();
	abstract public Term getBuiltInSlot_E_synthesized_image_transparency();
	abstract public Term getBuiltInSlot_E_make_rectangular_blobs_in_synthesized_image();
	//
	///////////////////////////////////////////////////////////////
	//
	public BlobProcessingAttributes() {
	}
	public BlobProcessingAttributes(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// MinimalTrainingInterval
	//
	public void setMinimalTrainingInterval1s(ChoisePoint iX, Term value) {
		setMinimalTrainingInterval(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMinimalTrainingInterval(int frames, ChoisePoint iX);
	//
	public void getMinimalTrainingInterval0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getMinimalTrainingInterval(iX)));
	}
	public void getMinimalTrainingInterval0fs(ChoisePoint iX) {
	}
	abstract public int getMinimalTrainingInterval(ChoisePoint iX);
	//
	// MaximalTrainingInterval
	//
	public void setMaximalTrainingInterval1s(ChoisePoint iX, Term value) {
		setMaximalTrainingInterval(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMaximalTrainingInterval(int frames, ChoisePoint iX);
	//
	public void getMaximalTrainingInterval0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getMaximalTrainingInterval(iX)));
	}
	public void getMaximalTrainingInterval0fs(ChoisePoint iX) {
	}
	abstract public int getMaximalTrainingInterval(ChoisePoint iX);
	//
	// HorizontalBlobBorder
	//
	public void setHorizontalBlobBorder1s(ChoisePoint iX, Term value) {
		setHorizontalBlobBorder(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setHorizontalBlobBorder(int size, ChoisePoint iX);
	//
	public void getHorizontalBlobBorder0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getHorizontalBlobBorder(iX)));
	}
	public void getHorizontalBlobBorder0fs(ChoisePoint iX) {
	}
	abstract public int getHorizontalBlobBorder(ChoisePoint iX);
	//
	// VerticalBlobBorder
	//
	public void setVerticalBlobBorder1s(ChoisePoint iX, Term value) {
		setVerticalBlobBorder(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setVerticalBlobBorder(int size, ChoisePoint iX);
	//
	public void getVerticalBlobBorder0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getVerticalBlobBorder(iX)));
	}
	public void getVerticalBlobBorder0fs(ChoisePoint iX) {
	}
	abstract public int getVerticalBlobBorder(ChoisePoint iX);
	//
	// HorizontalExtraBorderCoefficient
	//
	public void setHorizontalExtraBorderCoefficient1s(ChoisePoint iX, Term value) {
		setHorizontalExtraBorderCoefficient(GeneralConverters.argumentToReal(value,iX),iX);
	}
	abstract public void setHorizontalExtraBorderCoefficient(double coefficient, ChoisePoint iX);
	//
	public void getHorizontalExtraBorderCoefficient0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologReal(getHorizontalExtraBorderCoefficient(iX)));
	}
	public void getHorizontalExtraBorderCoefficient0fs(ChoisePoint iX) {
	}
	abstract public double getHorizontalExtraBorderCoefficient(ChoisePoint iX);
	//
	// VerticalExtraBorderCoefficient
	//
	public void setVerticalExtraBorderCoefficient1s(ChoisePoint iX, Term value) {
		setVerticalExtraBorderCoefficient(GeneralConverters.argumentToReal(value,iX),iX);
	}
	abstract public void setVerticalExtraBorderCoefficient(double coefficient, ChoisePoint iX);
	//
	public void getVerticalExtraBorderCoefficient0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologReal(getVerticalExtraBorderCoefficient(iX)));
	}
	public void getVerticalExtraBorderCoefficient0fs(ChoisePoint iX) {
	}
	abstract public double getVerticalExtraBorderCoefficient(ChoisePoint iX);
	//
	// MinimalBlobIntersectionArea
	//
	public void setMinimalBlobIntersectionArea1s(ChoisePoint iX, Term value) {
		setMinimalBlobIntersectionArea(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMinimalBlobIntersectionArea(int size, ChoisePoint iX);
	//
	public void getMinimalBlobIntersectionArea0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getMinimalBlobIntersectionArea(iX)));
	}
	public void getMinimalBlobIntersectionArea0fs(ChoisePoint iX) {
	}
	abstract public int getMinimalBlobIntersectionArea(ChoisePoint iX);
	//
	// MinimalBlobSize
	//
	public void setMinimalBlobSize1s(ChoisePoint iX, Term value) {
		setMinimalBlobSize(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMinimalBlobSize(int size, ChoisePoint iX);
	//
	public void getMinimalBlobSize0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getMinimalBlobSize(iX)));
	}
	public void getMinimalBlobSize0fs(ChoisePoint iX) {
	}
	abstract public int getMinimalBlobSize(ChoisePoint iX);
	//
	// MinimalTrackDuration
	//
	public void setMinimalTrackDuration1s(ChoisePoint iX, Term value) {
		setMinimalTrackDuration(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMinimalTrackDuration(int frames, ChoisePoint iX);
	//
	public void getMinimalTrackDuration0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getMinimalTrackDuration(iX)));
	}
	public void getMinimalTrackDuration0fs(ChoisePoint iX) {
	}
	abstract public int getMinimalTrackDuration(ChoisePoint iX);
	//
	// MaximalTrackDuration
	//
	public void setMaximalTrackDuration1s(ChoisePoint iX, Term value) {
		setMaximalTrackDuration(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMaximalTrackDuration(int frames, ChoisePoint iX);
	//
	public void getMaximalTrackDuration0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getMaximalTrackDuration(iX)));
	}
	public void getMaximalTrackDuration0fs(ChoisePoint iX) {
	}
	abstract public int getMaximalTrackDuration(ChoisePoint iX);
	//
	// MaximalBlobInvisibilityInterval
	//
	public void setMaximalBlobInvisibilityInterval1s(ChoisePoint iX, Term value) {
		setMaximalBlobInvisibilityInterval(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMaximalBlobInvisibilityInterval(int frames, ChoisePoint iX);
	//
	public void getMaximalBlobInvisibilityInterval0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getMaximalBlobInvisibilityInterval(iX)));
	}
	public void getMaximalBlobInvisibilityInterval0fs(ChoisePoint iX) {
	}
	abstract public int getMaximalBlobInvisibilityInterval(ChoisePoint iX);
	//
	// MaximalTrackRetentionInterval
	//
	public void setMaximalTrackRetentionInterval1s(ChoisePoint iX, Term value) {
		setMaximalTrackRetentionInterval(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setMaximalTrackRetentionInterval(int frames, ChoisePoint iX);
	//
	public void getMaximalTrackRetentionInterval0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getMaximalTrackRetentionInterval(iX)));
	}
	public void getMaximalTrackRetentionInterval0fs(ChoisePoint iX) {
	}
	abstract public int getMaximalTrackRetentionInterval(ChoisePoint iX);
	//
	// InverseTransformationMatrix
	//
	public void setInverseTransformationMatrix1s(ChoisePoint iX, Term value) {
		setInverseTransformationMatrix(GeneralConverters.argumentToMatrix(value,iX),iX);
	}
	abstract public void setInverseTransformationMatrix(double[][] matrix, ChoisePoint iX);
	//
	public void getInverseTransformationMatrix0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(GeneralConverters.doubleMatrixToListOfList(getInverseTransformationMatrix(iX)));
	}
	public void getInverseTransformationMatrix0fs(ChoisePoint iX) {
	}
	abstract public double[][] getInverseTransformationMatrix(ChoisePoint iX);
	//
	// SamplingRate
	//
	public void setSamplingRate1s(ChoisePoint iX, Term value) {
		setSamplingRate(GeneralConverters.argumentToReal(value,iX),iX);
	}
	abstract public void setSamplingRate(double rate, ChoisePoint iX);
	//
	public void getSamplingRate0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologReal(getSamplingRate(iX)));
	}
	public void getSamplingRate0fs(ChoisePoint iX) {
	}
	abstract public double getSamplingRate(ChoisePoint iX);
	//
	// R2WindowHalfwidth
	//
	public void setR2WindowHalfwidth1s(ChoisePoint iX, Term value) {
		setR2WindowHalfwidth(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setR2WindowHalfwidth(int halfwidth, ChoisePoint iX);
	//
	public void getR2WindowHalfwidth0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getR2WindowHalfwidth(iX)));
	}
	public void getR2WindowHalfwidth0fs(ChoisePoint iX) {
	}
	abstract public int getR2WindowHalfwidth(ChoisePoint iX);
	//
	// CharacteristicLengthMedianFilteringMode
	//
	public void setCharacteristicLengthMedianFilteringMode1s(ChoisePoint iX, Term value) {
		setCharacteristicLengthMedianFilteringMode(YesNoConverters.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setCharacteristicLengthMedianFilteringMode(boolean mode, ChoisePoint iX);
	//
	public void getCharacteristicLengthMedianFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(getCharacteristicLengthMedianFilteringMode(iX)));
	}
	public void getCharacteristicLengthMedianFilteringMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getCharacteristicLengthMedianFilteringMode(ChoisePoint iX);
	//
	// CharacteristicLengthMedianFilterHalfwidth
	//
	public void setCharacteristicLengthMedianFilterHalfwidth1s(ChoisePoint iX, Term value) {
		setCharacteristicLengthMedianFilterHalfwidth(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setCharacteristicLengthMedianFilterHalfwidth(int halfwidth, ChoisePoint iX);
	//
	public void getCharacteristicLengthMedianFilterHalfwidth0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getCharacteristicLengthMedianFilterHalfwidth(iX)));
	}
	public void getCharacteristicLengthMedianFilterHalfwidth0fs(ChoisePoint iX) {
	}
	abstract public int getCharacteristicLengthMedianFilterHalfwidth(ChoisePoint iX);
	//
	// VelocityMedianFilteringMode
	//
	public void setVelocityMedianFilteringMode1s(ChoisePoint iX, Term value) {
		setVelocityMedianFilteringMode(YesNoConverters.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setVelocityMedianFilteringMode(boolean mode, ChoisePoint iX);
	//
	public void getVelocityMedianFilteringMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(getVelocityMedianFilteringMode(iX)));
	}
	public void getVelocityMedianFilteringMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getVelocityMedianFilteringMode(ChoisePoint iX);
	//
	// VelocityMedianFilterHalfwidth
	//
	public void setVelocityMedianFilterHalfwidth1s(ChoisePoint iX, Term value) {
		setVelocityMedianFilterHalfwidth(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setVelocityMedianFilterHalfwidth(int halfwidth, ChoisePoint iX);
	//
	public void getVelocityMedianFilterHalfwidth0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getVelocityMedianFilterHalfwidth(iX)));
	}
	public void getVelocityMedianFilterHalfwidth0fs(ChoisePoint iX) {
	}
	abstract public int getVelocityMedianFilterHalfwidth(ChoisePoint iX);
	//
	// SynthesizedImageTransparency
	//
	public void setSynthesizedImageTransparency1s(ChoisePoint iX, Term value) {
		setSynthesizedImageTransparency(GeneralConverters.argumentToSmallRoundInteger(value,iX),iX);
	}
	abstract public void setSynthesizedImageTransparency(int transparency, ChoisePoint iX);
	//
	public void getSynthesizedImageTransparency0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getSynthesizedImageTransparency(iX)));
	}
	public void getSynthesizedImageTransparency0fs(ChoisePoint iX) {
	}
	abstract public int getSynthesizedImageTransparency(ChoisePoint iX);
	//
	// SynthesizedImageRectangularBlobsMode
	//
	public void setSynthesizedImageRectangularBlobsMode1s(ChoisePoint iX, Term value) {
		setSynthesizedImageRectangularBlobsMode(YesNoConverters.termYesNo2Boolean(value,iX),iX);
	}
	abstract public void setSynthesizedImageRectangularBlobsMode(boolean mode, ChoisePoint iX);
	//
	public void getSynthesizedImageRectangularBlobsMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNoConverters.boolean2TermYesNo(getSynthesizedImageRectangularBlobsMode(iX)));
	}
	public void getSynthesizedImageRectangularBlobsMode0fs(ChoisePoint iX) {
	}
	abstract public boolean getSynthesizedImageRectangularBlobsMode(ChoisePoint iX);
}
