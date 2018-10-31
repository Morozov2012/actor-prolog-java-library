// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.frames.converters.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ThermalVideoBuffer extends VideoBuffer {
	//
	protected OnOff autorangingMode;
	protected OnOff averagingMode;
	protected OnOff doubleColorMapMode;
	protected DetailedColorMap mainColorMap;
	protected DetailedColorMap auxiliaryColorMap;
	protected NumericalValue lowerTemperatureBound;
	protected NumericalValue upperTemperatureBound;
	protected NumericalValue lowerMainTemperatureQuantile;
	protected NumericalValue upperMainTemperatureQuantile;
	protected NumericalValue lowerAuxiliaryTemperatureQuantile;
	protected NumericalValue upperAuxiliaryTemperatureQuantile;
	protected YesNo useRecordedTemperatureRangeCommands;
	//
	protected AtomicBoolean actingDoNotControlTemperatureRange= new AtomicBoolean(false);
	protected AtomicBoolean actingAveragingMode= new AtomicBoolean(false);
	protected double[] cumulativeTemperatures;
	protected long numberOfAveragedFrames= 0;
	//
	protected double[] committedCumulativeTemperatures;
	//
	///////////////////////////////////////////////////////////////
	//
	public ThermalVideoBuffer(
			GlobalWorldIdentifier id,
			DataFrameReadingTask givenFrameReadingTask,
			DataFrameRecordingTask givenFrameRecordingTask) {
		super(id,givenFrameReadingTask,givenFrameRecordingTask);
	}
	public ThermalVideoBuffer(
			DataFrameReadingTask givenFrameReadingTask,
			DataFrameRecordingTask givenFrameRecordingTask) {
		super(givenFrameReadingTask,givenFrameRecordingTask);
	}
	public ThermalVideoBuffer(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_autoranging_mode();
	abstract public Term getBuiltInSlot_E_averaging_mode();
	abstract public Term getBuiltInSlot_E_double_color_map_mode();
	abstract public Term getBuiltInSlot_E_main_color_map();
	abstract public Term getBuiltInSlot_E_auxiliary_color_map();
	abstract public Term getBuiltInSlot_E_lower_temperature_bound();
	abstract public Term getBuiltInSlot_E_upper_temperature_bound();
	abstract public Term getBuiltInSlot_E_lower_main_temperature_quantile();
	abstract public Term getBuiltInSlot_E_upper_main_temperature_quantile();
	abstract public Term getBuiltInSlot_E_lower_auxiliary_temperature_quantile();
	abstract public Term getBuiltInSlot_E_upper_auxiliary_temperature_quantile();
	abstract public Term getBuiltInSlot_E_use_recorded_temperature_range_commands();
	abstract public Term getBuiltInSlot_E_use_recorded_color_map_commands();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set autoranging_mode
	//
	public void setAutorangingMode1s(ChoisePoint iX, Term a1) {
		OnOff value= OnOff.argument2OnOff(a1,iX);
		setAutorangingMode(value);
		updateAttributes(iX);
	}
	public void setAutorangingMode(OnOff value) {
		autorangingMode= value;
	}
	public void getAutorangingMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getAutorangingMode(iX).toTerm());
	}
	public void getAutorangingMode0fs(ChoisePoint iX) {
	}
	public OnOff getAutorangingMode(ChoisePoint iX) {
		if (autorangingMode != null) {
			return autorangingMode;
		} else {
			Term value= getBuiltInSlot_E_autoranging_mode();
			return OnOff.argument2OnOff(value,iX);
		}
	}
	//
	// get/set averaging_mode
	//
	public void setAveragingMode1s(ChoisePoint iX, Term a1) {
		OnOff value= OnOff.argument2OnOff(a1,iX);
		setAveragingMode(value);
		updateAttributes(iX);
	}
	public void setAveragingMode(OnOff value) {
		averagingMode= value;
	}
	public void getAveragingMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getAveragingMode(iX).toTerm());
	}
	public void getAveragingMode0fs(ChoisePoint iX) {
	}
	public OnOff getAveragingMode(ChoisePoint iX) {
		if (averagingMode != null) {
			return averagingMode;
		} else {
			Term value= getBuiltInSlot_E_averaging_mode();
			return OnOff.argument2OnOff(value,iX);
		}
	}
	//
	// get/set double_color_map_mode
	//
	public void setDoubleColorMapMode1s(ChoisePoint iX, Term a1) {
		OnOff value= OnOff.argument2OnOff(a1,iX);
		setDoubleColorMapMode(value);
		updateAttributes(iX);
	}
	public void setDoubleColorMapMode(OnOff value) {
		doubleColorMapMode= value;
	}
	public void getDoubleColorMapMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getDoubleColorMapMode(iX).toTerm());
	}
	public void getDoubleColorMapMode0fs(ChoisePoint iX) {
	}
	public OnOff getDoubleColorMapMode(ChoisePoint iX) {
		if (doubleColorMapMode != null) {
			return doubleColorMapMode;
		} else {
			Term value= getBuiltInSlot_E_double_color_map_mode();
			return OnOff.argument2OnOff(value,iX);
		}
	}
	//
	// get/set main_color_map
	//
	public void setMainColorMap1s(ChoisePoint iX, Term a1) {
		setMainColorMap(ColorMapConverters.argumentToColorMap(a1,iX));
		updateAttributes(iX);
	}
	public void setMainColorMap(DetailedColorMap value) {
		mainColorMap= value;
	}
	public void getMainColorMap0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(ColorMapConverters.toTerm(getMainColorMap(iX)));
	}
	public void getMainColorMap0fs(ChoisePoint iX) {
	}
	public DetailedColorMap getMainColorMap(ChoisePoint iX) {
		if (mainColorMap != null) {
			return mainColorMap;
		} else {
			Term value= getBuiltInSlot_E_main_color_map();
			return ColorMapConverters.argumentToColorMap(value,iX);
		}
	}
	//
	// get/set auxiliary_color_map
	//
	public void setAuxiliaryColorMap1s(ChoisePoint iX, Term a1) {
		setAuxiliaryColorMap(ColorMapConverters.argumentToColorMap(a1,iX));
		updateAttributes(iX);
	}
	public void setAuxiliaryColorMap(DetailedColorMap value) {
		auxiliaryColorMap= value;
	}
	public void getAuxiliaryColorMap0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(ColorMapConverters.toTerm(getAuxiliaryColorMap(iX)));
	}
	public void getAuxiliaryColorMap0fs(ChoisePoint iX) {
	}
	public DetailedColorMap getAuxiliaryColorMap(ChoisePoint iX) {
		if (auxiliaryColorMap != null) {
			return auxiliaryColorMap;
		} else {
			Term value= getBuiltInSlot_E_auxiliary_color_map();
			return ColorMapConverters.argumentToColorMap(value,iX);
		}
	}
	//
	// get/set lower_temperature_bound
	//
	public void setLowerTemperatureBound1s(ChoisePoint iX, Term a1) {
		setLowerTemperatureBound(NumericalValue.argumentToNumericalValue(a1,iX));
		updateAttributes(iX);
	}
	public void setLowerTemperatureBound(NumericalValue value) {
		lowerTemperatureBound= value;
		// camera.setLowerTemperatureBound(value.toDouble());
	}
	public void getLowerTemperatureBound0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getLowerTemperatureBound(iX).toTerm());
	}
	public void getLowerTemperatureBound0fs(ChoisePoint iX) {
	}
	public NumericalValue getLowerTemperatureBound(ChoisePoint iX) {
		if (lowerTemperatureBound != null) {
			return lowerTemperatureBound;
		} else {
			Term value= getBuiltInSlot_E_lower_temperature_bound();
			return NumericalValue.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set upper_temperature_bound
	//
	public void setUpperTemperatureBound1s(ChoisePoint iX, Term a1) {
		setUpperTemperatureBound(NumericalValue.argumentToNumericalValue(a1,iX));
		updateAttributes(iX);
	}
	public void setUpperTemperatureBound(NumericalValue value) {
		upperTemperatureBound= value;
		// camera.setUpperTemperatureBound(value.toDouble());
	}
	public void getUpperTemperatureBound0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getUpperTemperatureBound(iX).toTerm());
	}
	public void getUpperTemperatureBound0fs(ChoisePoint iX) {
	}
	public NumericalValue getUpperTemperatureBound(ChoisePoint iX) {
		if (upperTemperatureBound != null) {
			return upperTemperatureBound;
		} else {
			Term value= getBuiltInSlot_E_upper_temperature_bound();
			return NumericalValue.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set lower_main_temperature_quantile
	//
	public void setLowerMainTemperatureQuantile1s(ChoisePoint iX, Term a1) {
		setLowerMainTemperatureQuantile(NumericalValue.argumentToNumericalValue(a1,iX));
		updateAttributes(iX);
	}
	public void setLowerMainTemperatureQuantile(NumericalValue value) {
		lowerMainTemperatureQuantile= value;
	}
	public void getLowerMainTemperatureQuantile0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getLowerMainTemperatureQuantile(iX).toTerm());
	}
	public void getLowerMainTemperatureQuantile0fs(ChoisePoint iX) {
	}
	public NumericalValue getLowerMainTemperatureQuantile(ChoisePoint iX) {
		if (lowerMainTemperatureQuantile != null) {
			return lowerMainTemperatureQuantile;
		} else {
			Term value= getBuiltInSlot_E_lower_main_temperature_quantile();
			return NumericalValue.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set upper_main_temperature_quantile
	//
	public void setUpperMainTemperatureQuantile1s(ChoisePoint iX, Term a1) {
		setUpperMainTemperatureQuantile(NumericalValue.argumentToNumericalValue(a1,iX));
		updateAttributes(iX);
	}
	public void setUpperMainTemperatureQuantile(NumericalValue value) {
		upperMainTemperatureQuantile= value;
	}
	public void getUpperMainTemperatureQuantile0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getUpperMainTemperatureQuantile(iX).toTerm());
	}
	public void getUpperMainTemperatureQuantile0fs(ChoisePoint iX) {
	}
	public NumericalValue getUpperMainTemperatureQuantile(ChoisePoint iX) {
		if (upperMainTemperatureQuantile != null) {
			return upperMainTemperatureQuantile;
		} else {
			Term value= getBuiltInSlot_E_upper_main_temperature_quantile();
			return NumericalValue.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set lower_auxiliary_temperature_quantile
	//
	public void setLowerAuxiliaryTemperatureQuantile1s(ChoisePoint iX, Term a1) {
		setLowerAuxiliaryTemperatureQuantile(NumericalValue.argumentToNumericalValue(a1,iX));
		updateAttributes(iX);
	}
	public void setLowerAuxiliaryTemperatureQuantile(NumericalValue value) {
		lowerAuxiliaryTemperatureQuantile= value;
	}
	public void getLowerAuxiliaryTemperatureQuantile0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getLowerAuxiliaryTemperatureQuantile(iX).toTerm());
	}
	public void getLowerAuxiliaryTemperatureQuantile0fs(ChoisePoint iX) {
	}
	public NumericalValue getLowerAuxiliaryTemperatureQuantile(ChoisePoint iX) {
		if (lowerAuxiliaryTemperatureQuantile != null) {
			return lowerAuxiliaryTemperatureQuantile;
		} else {
			Term value= getBuiltInSlot_E_lower_auxiliary_temperature_quantile();
			return NumericalValue.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set upper_auxiliary_temperature_quantile
	//
	public void setUpperAuxiliaryTemperatureQuantile1s(ChoisePoint iX, Term a1) {
		setUpperAuxiliaryTemperatureQuantile(NumericalValue.argumentToNumericalValue(a1,iX));
		updateAttributes(iX);
	}
	public void setUpperAuxiliaryTemperatureQuantile(NumericalValue value) {
		upperAuxiliaryTemperatureQuantile= value;
	}
	public void getUpperAuxiliaryTemperatureQuantile0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getUpperAuxiliaryTemperatureQuantile(iX).toTerm());
	}
	public void getUpperAuxiliaryTemperatureQuantile0fs(ChoisePoint iX) {
	}
	public NumericalValue getUpperAuxiliaryTemperatureQuantile(ChoisePoint iX) {
		if (upperAuxiliaryTemperatureQuantile != null) {
			return upperAuxiliaryTemperatureQuantile;
		} else {
			Term value= getBuiltInSlot_E_upper_auxiliary_temperature_quantile();
			return NumericalValue.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set use_recorded_temperature_range_commands
	//
	public void setUseRecordedTemperatureRangeCommands1s(ChoisePoint iX, Term a1) {
		setUseRecordedTemperatureRangeCommands(YesNo.argument2YesNo(a1,iX));
		updateAttributes(iX);
	}
	public void setUseRecordedTemperatureRangeCommands(YesNo value) {
		useRecordedTemperatureRangeCommands= value;
	}
	public void getUseRecordedTemperatureRangeCommands0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getUseRecordedTemperatureRangeCommands(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getUseRecordedTemperatureRangeCommands0fs(ChoisePoint iX) {
	}
	public YesNo getUseRecordedTemperatureRangeCommands(ChoisePoint iX) {
		if (useRecordedTemperatureRangeCommands != null) {
			return useRecordedTemperatureRangeCommands;
		} else {
			Term value= getBuiltInSlot_E_use_recorded_temperature_range_commands();
			return YesNo.argument2YesNo(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
			committedCumulativeTemperatures= null;
			resetFrameRate();
			resetCumulativeTemperatures();
		}
	}
	//
	public void resetFrameRate0s(ChoisePoint iX) {
		synchronized (numberOfRecentReceivedFrame) {
			resetFrameRate();
		}
	}
	//
	protected void resetFrameRate() {
	}
	//
	public void resetCumulativeTemperatures() {
		cumulativeTemperatures= null;
		numberOfAveragedFrames= 0;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit0s(ChoisePoint iX) throws Backtracking {
		synchronized (numberOfRecentReceivedFrame) {
			if (recentFrame==null) {
				throw Backtracking.instance;
			};
			commit();
		}
	}
	//
	protected void commit() {
		synchronized (numberOfRecentReceivedFrame) {
			super.commit();
			if (cumulativeTemperatures != null && numberOfAveragedFrames > 0) {
				if (	committedCumulativeTemperatures==null ||
					committedCumulativeTemperatures.length != cumulativeTemperatures.length) {
					committedCumulativeTemperatures= new double[cumulativeTemperatures.length];
				};
				if (committedCumulativeTemperatures != null) {
					for (int k=0; k < committedCumulativeTemperatures.length; k++) {
						committedCumulativeTemperatures[k]= cumulativeTemperatures[k] / numberOfAveragedFrames;
					}
				}
			} else {
				committedCumulativeTemperatures= null;
			}
		}
	}
}
