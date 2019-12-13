// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.frames.converters.*;
import morozov.terms.*;
import morozov.worlds.*;

public abstract class ZoomDataAcquisitionBuffer extends DataAcquisitionBuffer {
	//
	protected YesNo zoomImage;
	protected NumericalValue zoomingCoefficient;
	protected YesNo useRecordedZoomingCommands;
	//
	///////////////////////////////////////////////////////////////
	//
	public ZoomDataAcquisitionBuffer() {
	}
	public ZoomDataAcquisitionBuffer(
			DataFrameReadingTask givenFrameReadingTask,
			DataFrameRecordingTask givenFrameRecordingTask) {
		super(givenFrameReadingTask,givenFrameRecordingTask);
	}
	public ZoomDataAcquisitionBuffer(GlobalWorldIdentifier id) {
		super(id);
	}
	public ZoomDataAcquisitionBuffer(
			GlobalWorldIdentifier id,
			DataFrameReadingTask givenFrameReadingTask,
			DataFrameRecordingTask givenFrameRecordingTask) {
		super(id,givenFrameReadingTask,givenFrameRecordingTask);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_zoom_image();
	abstract public Term getBuiltInSlot_E_zooming_coefficient();
	abstract public Term getBuiltInSlot_E_use_recorded_zooming_commands();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set zoom_image
	//
	public void setZoomImage1s(ChoisePoint iX, Term a1) {
		setZoomImage(YesNoConverters.argument2YesNo(a1,iX));
		updateAttributes(iX);
	}
	public void setZoomImage(YesNo value) {
		zoomImage= value;
	}
	public void getZoomImage0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getZoomImage(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getZoomImage0fs(ChoisePoint iX) {
	}
	@Override
	public YesNo getZoomImage(ChoisePoint iX) {
		if (zoomImage != null) {
			return zoomImage;
		} else {
			Term value= getBuiltInSlot_E_zoom_image();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set zooming_coefficient
	//
	public void setZoomingCoefficient1s(ChoisePoint iX, Term a1) {
		setZoomingCoefficient(NumericalValueConverters.argumentToNumericalValue(a1,iX));
		updateAttributes(iX);
	}
	public void setZoomingCoefficient(NumericalValue value) {
		zoomingCoefficient= value;
	}
	public void getZoomingCoefficient0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(NumericalValueConverters.toTerm(getZoomingCoefficient(iX)));
	}
	public void getZoomingCoefficient0fs(ChoisePoint iX) {
	}
	@Override
	public NumericalValue getZoomingCoefficient(ChoisePoint iX) {
		if (zoomingCoefficient != null) {
			return zoomingCoefficient;
		} else {
			Term value= getBuiltInSlot_E_zooming_coefficient();
			return NumericalValueConverters.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set use_recorded_zooming_commands
	//
	public void setUseRecordedZoomingCommands1s(ChoisePoint iX, Term a1) {
		setUseRecordedZoomingCommands(YesNoConverters.argument2YesNo(a1,iX));
	}
	public void setUseRecordedZoomingCommands(YesNo value) {
		useRecordedZoomingCommands= value;
	}
	public void getUseRecordedZoomingCommands0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getUseRecordedZoomingCommands(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getUseRecordedZoomingCommands0fs(ChoisePoint iX) {
	}
	public YesNo getUseRecordedZoomingCommands(ChoisePoint iX) {
		if (useRecordedZoomingCommands != null) {
			return useRecordedZoomingCommands;
		} else {
			Term value= getBuiltInSlot_E_use_recorded_zooming_commands();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
}
