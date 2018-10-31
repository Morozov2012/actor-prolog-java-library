// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.errors.*;
import morozov.system.files.*;
import morozov.system.frames.*;
import morozov.system.frames.converters.*;
import morozov.system.frames.converters.interfaces.*;
import morozov.system.frames.data.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.frames.interfaces.*;
import morozov.system.modes.*;
import morozov.system.modes.converters.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public abstract class VideoBuffer extends BufferedImageController implements DataFrameConsumerInterface, DataFrameProviderInterface {
	//
	protected VideoBufferOperatingMode operatingMode;
	protected DetailedColorMap mainColorMap;
	protected DetailedColorMap auxilairyColorMap;
	protected YesNo useRecordedColorMapCommands;
	protected YesNo zoomImage;
	protected NumericalValue zoomingCoefficient;
	protected YesNo useRecordedZoomingCommands;
	protected ActionPeriod openingAttemptPeriod;
	protected Integer writeBufferSize;
	protected Integer readBufferSize;
	protected NumericalValue slowMotionCoefficient;
	protected IntegerAttribute maximalFrameDelay;
	//
	protected DataFrameReadingTask frameReadingTask;
	protected DataFrameRecordingTask frameRecordingTask;
	//
	protected AtomicReference<VideoBufferOperatingMode> actingVideoBufferOperatingMode= new AtomicReference<>();
	protected static int defaultReadBufferSize= 0;
	protected AtomicInteger actingReadBufferSize= new AtomicInteger(defaultReadBufferSize);
	//
	protected AtomicReference<TextAttribute> actingDescription= new AtomicReference<>();
	protected AtomicReference<TextAttribute> actingCopyright= new AtomicReference<>();
	protected AtomicReference<TextAttribute> actingRegistrationDate= new AtomicReference<>();
	protected AtomicReference<TextAttribute> actingRegistrationTime= new AtomicReference<>();
	//
	protected AtomicReference<FrameReadingError> dataTransferError= new AtomicReference<>();
	protected AtomicReference<DataAcquisitionError> dataAcquisitionError= new AtomicReference<>();
	//
	protected AtomicLong counterOfRecentAttributes= new AtomicLong(-1);
	protected AtomicLong numberOfRecentReceivedFrame= new AtomicLong(-1);
	//
	protected AtomicReference<DataFrameColorfulAttributesInterface> recentAttributes= new AtomicReference<>();
	protected boolean recentFrameIsRepeated= false;
	//
	protected AtomicReference<DetailedColorMap> ownMainColorMap= new AtomicReference<>();
	protected AtomicReference<DetailedColorMap> ownAuxiliaryColorMap= new AtomicReference<>();
	protected AtomicReference<DetailedColorMap> recordedMainColorMap= new AtomicReference<>();
	protected AtomicReference<DetailedColorMap> recordedAuxiliaryColorMap= new AtomicReference<>();
	protected AtomicLong ownMainColorMapSerialNumber= new AtomicLong(-1);
	protected AtomicLong ownAuxiliaryColorMapSerialNumber= new AtomicLong(-1);
	protected AtomicLong recordedMainColorMapSerialNumber= new AtomicLong(-1);
	protected AtomicLong recordedAuxiliaryColorMapSerialNumber= new AtomicLong(-1);
	//
	protected DataFrameInterface recentFrame;
	protected DataFrameInterface committedFrame;
	//
	protected AtomicBoolean containsNewFrame= new AtomicBoolean(false);
	//
	protected AtomicReference<DetailedColorMap> deliveredMainColorMap= new AtomicReference<>();
	protected AtomicReference<DetailedColorMap> deliveredAuxiliaryColorMap= new AtomicReference<>();
	protected AtomicReference<String> deliveredDescription= new AtomicReference<>();
	protected AtomicReference<String> deliveredCopyright= new AtomicReference<>();
	protected AtomicReference<String> deliveredRegistrationDate= new AtomicReference<>();
	protected AtomicReference<String> deliveredRegistrationTime= new AtomicReference<>();
	//
	protected Term deliveredMainColorMapTerm;
	protected Term deliveredAuxiliaryColorMapTerm;
	protected Term deliveredDescriptionTerm;
	protected Term deliveredCopyrightTerm;
	protected Term deliveredRegistrationDateTerm;
	protected Term deliveredRegistrationTimeTerm;
	//
	protected static String defaultDescription= "Actor Prolog";
	protected static String defaultCopyright= "(c) www.fullvision.ru";
	//
	protected static OnOff defaultAutorangingMode= OnOff.OFF;
	protected static OnOff defaultDoubleColorMapMode= OnOff.OFF;
	protected static OnOff defaultAveragingMode= OnOff.OFF;
	protected static DetailedColorMap defaultMainColorMap= DetailedColorMap.getDefaultColorMap();
	protected static DetailedColorMap defaultAuxiliaryColorMap= DetailedColorMap.getDefaultGrayColorMap();
	//
	protected static NumericalValue defaultLowerTemperatureBound= new NumericalValue(0.0d);
	protected static NumericalValue defaultUpperTemperatureBound= new NumericalValue(65_536.0d);
	protected static NumericalValue defaultLowerTemperatureQuantile1= new NumericalValue(0.0d);
	protected static NumericalValue defaultUpperTemperatureQuantile1= new NumericalValue(65_536.0d);
	protected static NumericalValue defaultLowerTemperatureQuantile2= new NumericalValue(0.0d);
	protected static NumericalValue defaultUpperTemperatureQuantile2= new NumericalValue(65_536.0d);
	//
	protected static int defaultDeviceOpeningAttemptDelay= 100;
	//
	protected static Term termNo= new PrologSymbol(SymbolCodes.symbolCode_E_no);
	//
	///////////////////////////////////////////////////////////////
	//
	public VideoBuffer() {
		frameReadingTask= new DataFrameReadingTask();
		frameRecordingTask= new DataFrameRecordingTask();
		connectVideoBufferClassInstance();
	}
	public VideoBuffer(
			DataFrameReadingTask givenFrameReadingTask,
			DataFrameRecordingTask givenFrameRecordingTask) {
		frameReadingTask= givenFrameReadingTask;
		frameRecordingTask= givenFrameRecordingTask;
		connectVideoBufferClassInstance();
	}
	public VideoBuffer(GlobalWorldIdentifier id) {
		super(id);
		frameReadingTask= new DataFrameReadingTask();
		frameRecordingTask= new DataFrameRecordingTask();
		connectVideoBufferClassInstance();
	}
	public VideoBuffer(
			GlobalWorldIdentifier id,
			DataFrameReadingTask givenFrameReadingTask,
			DataFrameRecordingTask givenFrameRecordingTask) {
		super(id);
		frameReadingTask= givenFrameReadingTask;
		frameRecordingTask= givenFrameRecordingTask;
		connectVideoBufferClassInstance();
	}
	//
	protected void connectVideoBufferClassInstance() {
		frameReadingTask.setDataConsumer(this);
		frameRecordingTask.setDataProvider(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public long entry_s_FrameObtained_0();
	abstract public long entry_s_DataTransferCompletion_0();
	abstract public long entry_s_BufferOverflow_0();
	abstract public long entry_s_BufferDeallocation_0();
	abstract public long entry_s_DataTransferError_1_i();
	//
	abstract public Term getBuiltInSlot_E_operating_mode();
	abstract public Term getBuiltInSlot_E_description();
	abstract public Term getBuiltInSlot_E_copyright();
	abstract public Term getBuiltInSlot_E_registration_date();
	abstract public Term getBuiltInSlot_E_registration_time();
	public Term getBuiltInSlot_E_use_recorded_color_map_commands() {
		return termNo;
	}
	abstract public Term getBuiltInSlot_E_zoom_image();
	abstract public Term getBuiltInSlot_E_zooming_coefficient();
	abstract public Term getBuiltInSlot_E_use_recorded_zooming_commands();
	abstract public Term getBuiltInSlot_E_opening_attempt_period();
	abstract public Term getBuiltInSlot_E_write_buffer_size();
	abstract public Term getBuiltInSlot_E_read_buffer_size();
	abstract public Term getBuiltInSlot_E_slow_motion_coefficient();
	abstract public Term getBuiltInSlot_E_maximal_frame_delay();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set operating_mode
	//
	public void setOperatingMode1s(ChoisePoint iX, Term a1) {
		setOperatingMode(VideoBufferOperatingModeConverters.argumentToVideoBufferOperatingMode(a1,iX));
	}
	public void setOperatingMode(VideoBufferOperatingMode value) {
		operatingMode= value;
	}
	public void getOperatingMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(VideoBufferOperatingModeConverters.toTerm(getOperatingMode(iX)));
	}
	public void getOperatingMode0fs(ChoisePoint iX) {
	}
	public VideoBufferOperatingMode getOperatingMode(ChoisePoint iX) {
		if (operatingMode != null) {
			return operatingMode;
		} else {
			Term value= getBuiltInSlot_E_operating_mode();
			return VideoBufferOperatingModeConverters.argumentToVideoBufferOperatingMode(value,iX);
		}
	}
	//
	// get/set use_recorded_color_map_commands
	//
	public void setUseRecordedColorMapCommands1s(ChoisePoint iX, Term a1) {
		setUseRecordedColorMapCommands(YesNo.argument2YesNo(a1,iX));
	}
	public void setUseRecordedColorMapCommands(YesNo value) {
		useRecordedColorMapCommands= value;
	}
	public void getUseRecordedColorMapCommands0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getUseRecordedColorMapCommands(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getUseRecordedColorMapCommands0fs(ChoisePoint iX) {
	}
	public YesNo getUseRecordedColorMapCommands(ChoisePoint iX) {
		if (useRecordedColorMapCommands != null) {
			return useRecordedColorMapCommands;
		} else {
			Term value= getBuiltInSlot_E_use_recorded_color_map_commands();
			return YesNo.argument2YesNo(value,iX);
		}
	}
	//
	// get/set zoom_image
	//
	public void setZoomImage1s(ChoisePoint iX, Term a1) {
		setZoomImage(YesNo.argument2YesNo(a1,iX));
		updateAttributes(iX);
	}
	public void setZoomImage(YesNo value) {
		zoomImage= value;
	}
	public void getZoomImage0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getZoomImage(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getZoomImage0fs(ChoisePoint iX) {
	}
	public YesNo getZoomImage(ChoisePoint iX) {
		if (zoomImage != null) {
			return zoomImage;
		} else {
			Term value= getBuiltInSlot_E_zoom_image();
			return YesNo.argument2YesNo(value,iX);
		}
	}
	//
	// get/set zooming_coefficient
	//
	public void setZoomingCoefficient1s(ChoisePoint iX, Term a1) {
		setZoomingCoefficient(NumericalValue.argumentToNumericalValue(a1,iX));
		updateAttributes(iX);
	}
	public void setZoomingCoefficient(NumericalValue value) {
		zoomingCoefficient= value;
	}
	public void getZoomingCoefficient0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getZoomingCoefficient(iX).toTerm());
	}
	public void getZoomingCoefficient0fs(ChoisePoint iX) {
	}
	public NumericalValue getZoomingCoefficient(ChoisePoint iX) {
		if (zoomingCoefficient != null) {
			return zoomingCoefficient;
		} else {
			Term value= getBuiltInSlot_E_zooming_coefficient();
			return NumericalValue.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set use_recorded_zooming_commands
	//
	public void setUseRecordedZoomingCommands1s(ChoisePoint iX, Term a1) {
		setUseRecordedZoomingCommands(YesNo.argument2YesNo(a1,iX));
	}
	public void setUseRecordedZoomingCommands(YesNo value) {
		useRecordedZoomingCommands= value;
	}
	public void getUseRecordedZoomingCommands0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getUseRecordedZoomingCommands(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getUseRecordedZoomingCommands0fs(ChoisePoint iX) {
	}
	public YesNo getUseRecordedZoomingCommands(ChoisePoint iX) {
		if (useRecordedZoomingCommands != null) {
			return useRecordedZoomingCommands;
		} else {
			Term value= getBuiltInSlot_E_use_recorded_zooming_commands();
			return YesNo.argument2YesNo(value,iX);
		}
	}
	//
	// get/set opening_attempt_period
	//
	public void setOpeningAttemptPeriod1s(ChoisePoint iX, Term a1) {
		setOpeningAttemptPeriod(ActionPeriod.argumentToActionPeriod(a1,iX));
	}
	public void setOpeningAttemptPeriod(ActionPeriod value) {
		openingAttemptPeriod= value;
	}
	public void getOpeningAttemptPeriod0ff(ChoisePoint iX, PrologVariable result) {
		ActionPeriod value= getOpeningAttemptPeriod(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getOpeningAttemptPeriod0fs(ChoisePoint iX) {
	}
	public ActionPeriod getOpeningAttemptPeriod(ChoisePoint iX) {
		if (openingAttemptPeriod != null) {
			return openingAttemptPeriod;
		} else {
			Term value= getBuiltInSlot_E_opening_attempt_period();
			return ActionPeriod.argumentToActionPeriod(value,iX);
		}
	}
	//
	// get/set write_buffer_size
	//
	public void setWriteBufferSize1s(ChoisePoint iX, Term a1) {
		setWriteBufferSize(GeneralConverters.argumentToSmallInteger(a1,iX));
	}
	public void setWriteBufferSize(int value) {
		writeBufferSize= value;
		frameRecordingTask.setWriteBufferSize(writeBufferSize);
	}
	public void getWriteBufferSize0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getWriteBufferSize(iX)));
	}
	public void getWriteBufferSize0fs(ChoisePoint iX) {
	}
	public int getWriteBufferSize(ChoisePoint iX) {
		if (writeBufferSize != null) {
			return writeBufferSize;
		} else {
			Term value= getBuiltInSlot_E_write_buffer_size();
			return GeneralConverters.argumentToSmallInteger(value,iX);
		}
	}
	//
	// get/set read_buffer_size
	//
	public void setReadBufferSize1s(ChoisePoint iX, Term a1) {
		setReadBufferSize(GeneralConverters.argumentToSmallInteger(a1,iX));
	}
	public void setReadBufferSize(int value) {
		readBufferSize= value;
		actingReadBufferSize.set(value);
	}
	public void getReadBufferSize0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getReadBufferSize(iX)));
	}
	public void getReadBufferSize0fs(ChoisePoint iX) {
	}
	public int getReadBufferSize(ChoisePoint iX) {
		if (readBufferSize != null) {
			return readBufferSize;
		} else {
			Term value= getBuiltInSlot_E_read_buffer_size();
			return GeneralConverters.argumentToSmallInteger(value,iX);
		}
	}
	//
	// get/set slow_motion_coefficient
	//
	public void setSlowMotionCoefficient1s(ChoisePoint iX, Term a1) {
		setSlowMotionCoefficient(NumericalValue.argumentToNumericalValue(a1,iX));
	}
	public void setSlowMotionCoefficient(NumericalValue value) {
		slowMotionCoefficient= value;
		frameReadingTask.setSlowMotionCoefficient(slowMotionCoefficient);
	}
	public void getSlowMotionCoefficient0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getSlowMotionCoefficient(iX).toTerm());
	}
	public void getSlowMotionCoefficient0fs(ChoisePoint iX) {
	}
	public NumericalValue getSlowMotionCoefficient(ChoisePoint iX) {
		if (slowMotionCoefficient != null) {
			return slowMotionCoefficient;
		} else {
			Term value= getBuiltInSlot_E_slow_motion_coefficient();
			return NumericalValue.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set maximal_frame_delay
	//
	public void setMaximalFrameDelay1s(ChoisePoint iX, Term a1) {
		setMaximalFrameDelay(IntegerAttribute.argumentToIntegerAttribute(a1,iX));
	}
	public void setMaximalFrameDelay(IntegerAttribute value) {
		maximalFrameDelay= value;
		frameReadingTask.setMaximalWaitingPeriod(value);
	}
	public void getMaximalFrameDelay0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getMaximalFrameDelay(iX).toTerm());
	}
	public void getMaximalFrameDelay0fs(ChoisePoint iX) {
	}
	public IntegerAttribute getMaximalFrameDelay(ChoisePoint iX) {
		if (maximalFrameDelay != null) {
			return maximalFrameDelay;
		} else {
			Term value= getBuiltInSlot_E_maximal_frame_delay();
			return IntegerAttribute.argumentToIntegerAttribute(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateAttributes(ChoisePoint iX) {
		DataFrameColorfulAttributes attributes= new DataFrameColorfulAttributes(
			counterOfRecentAttributes.incrementAndGet(),
			getAutorangingMode(iX).toBoolean(),
			getDoubleColorMapMode(iX).toBoolean(),
			DataRange.BOUNDS, // getSelectedDataRange(),
			getLowerTemperatureBound(iX).toDouble(),
			getUpperTemperatureBound(iX).toDouble(),
			getLowerMainTemperatureQuantile(iX).toDouble(),
			getUpperMainTemperatureQuantile(iX).toDouble(),
			getLowerAuxiliaryTemperatureQuantile(iX).toDouble(),
			getUpperAuxiliaryTemperatureQuantile(iX).toDouble(),
			getMainColorMap(iX),
			getAuxiliaryColorMap(iX),
			getAveragingMode(iX).toBoolean(),
			getZoomImage(iX).toBoolean(),
			getZoomingCoefficient(iX));
		recentAttributes.set(attributes);
	}
	//
	public OnOff getAutorangingMode(ChoisePoint iX) {
		return defaultAutorangingMode;
	}
	public OnOff getAveragingMode(ChoisePoint iX) {
		return defaultAveragingMode;
	}
	public OnOff getDoubleColorMapMode(ChoisePoint iX) {
		return defaultDoubleColorMapMode;
	}
	//
	public DetailedColorMap getMainColorMap(ChoisePoint iX) {
		return defaultMainColorMap;
	}
	public DetailedColorMap getAuxiliaryColorMap(ChoisePoint iX) {
		return defaultAuxiliaryColorMap;
	}
	//
	public NumericalValue getLowerTemperatureBound(ChoisePoint iX) {
		return defaultLowerTemperatureBound;
	}
	public NumericalValue getUpperTemperatureBound(ChoisePoint iX) {
		return defaultUpperTemperatureBound;
	}
	public NumericalValue getLowerMainTemperatureQuantile(ChoisePoint iX) {
		return defaultLowerTemperatureQuantile1;
	}
	public NumericalValue getUpperMainTemperatureQuantile(ChoisePoint iX) {
		return defaultUpperTemperatureQuantile1;
	}
	public NumericalValue getLowerAuxiliaryTemperatureQuantile(ChoisePoint iX) {
		return defaultLowerTemperatureQuantile2;
	}
	public NumericalValue getUpperAuxiliaryTemperatureQuantile(ChoisePoint iX) {
		return defaultUpperTemperatureQuantile2;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void recentReadingError2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) throws Backtracking {
		FrameReadingError error= dataTransferError.get();
		if (error != null) {
			a1.setBacktrackableValue(new PrologInteger(error.getNumberOfFrameToBeAcquired()),iX);
			a2.setBacktrackableValue(new PrologString(error.getException().toString()),iX);
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void recentDataAcquisitionError1s(ChoisePoint iX, PrologVariable a1) throws Backtracking {
		DataAcquisitionError error= dataAcquisitionError.get();
		if (error != null) {
			a1.setBacktrackableValue(new PrologString(error.getException().toString()),iX);
		} else {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateOwnMainColorMap(DataFrameBaseAttributesInterface attributes) {
		boolean doUpdateColorMap= false;
		if (ownMainColorMap.get()==null) {
			doUpdateColorMap= true;
		} else {
			long serialNumber= attributes.getSerialNumber();
			if (ownMainColorMapSerialNumber.get() != serialNumber) {
				doUpdateColorMap= true;
			}
		};
		if (doUpdateColorMap) {
			if (attributes instanceof DataFrameColorfulAttributes) {
				ownMainColorMap.set(((DataFrameColorfulAttributes)attributes).getDetailedMainColorMap());
			} else {
				DataColorMap dataMainColorMap= attributes.getMainColorMap();
				ownMainColorMap.set(new DetailedColorMap(dataMainColorMap));
			}
		}
	}
	protected void updateOwnAuxiliaryColorMap(DataFrameBaseAttributesInterface attributes) {
		boolean doUpdateColorMap= false;
		if (ownAuxiliaryColorMap.get()==null) {
			doUpdateColorMap= true;
		} else {
			long serialNumber= attributes.getSerialNumber();
			if (ownAuxiliaryColorMapSerialNumber.get() != serialNumber) {
				doUpdateColorMap= true;
			}
		};
		if (doUpdateColorMap) {
			if (attributes instanceof DataFrameColorfulAttributes) {
				ownAuxiliaryColorMap.set(((DataFrameColorfulAttributes)attributes).getDetailedAuxiliaryColorMap());
			} else {
				DataColorMap dataAuxiliaryColorMap= attributes.getAuxiliaryColorMap();
				ownAuxiliaryColorMap.set(new DetailedColorMap(dataAuxiliaryColorMap));
			}
		}
	}
	//
	protected void updateRecordedMainColorMap(DataFrameBaseAttributesInterface attributes) {
		boolean doUpdateColorMap= false;
		if (recordedMainColorMap.get()==null) {
			doUpdateColorMap= true;
		} else {
			long serialNumber= attributes.getSerialNumber();
			if (recordedMainColorMapSerialNumber.get() != serialNumber) {
				doUpdateColorMap= true;
			}
		};
		if (doUpdateColorMap) {
			if (attributes instanceof DataFrameColorfulAttributes) {
				recordedMainColorMap.set(((DataFrameColorfulAttributes)attributes).getDetailedMainColorMap());
			} else {
				DataColorMap dataMainColorMap= attributes.getMainColorMap();
				recordedMainColorMap.set(new DetailedColorMap(dataMainColorMap));
			}
		}
	}
	protected void updateRecordedAuxiliaryColorMap(DataFrameBaseAttributesInterface attributes) {
		boolean doUpdateColorMap= false;
		if (recordedAuxiliaryColorMap.get()==null) {
			doUpdateColorMap= true;
		} else {
			long serialNumber= attributes.getSerialNumber();
			if (recordedAuxiliaryColorMapSerialNumber.get() != serialNumber) {
				doUpdateColorMap= true;
			}
		};
		if (doUpdateColorMap) {
			if (attributes instanceof DataFrameColorfulAttributes) {
				recordedAuxiliaryColorMap.set(((DataFrameColorfulAttributes)attributes).getDetailedAuxiliaryColorMap());
			} else {
				DataColorMap dataAuxiliaryColorMap= attributes.getAuxiliaryColorMap();
				recordedAuxiliaryColorMap.set(new DetailedColorMap(dataAuxiliaryColorMap));
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void start0s(ChoisePoint iX) {
		VideoBufferOperatingMode actingOperatingMode= actingVideoBufferOperatingMode.get();
		if (actingOperatingMode != null) {
			switch (actingOperatingMode) {
			case RECORDING:
				activateDataAcquisition(iX);
				break;
			case PLAYING:
				frameReadingTask.setMaximalWaitingPeriod(getMaximalFrameDelay(iX));
				frameReadingTask.setStopAfterSingleReading(false);
				frameReadingTask.activateReading();
				break;
			case READING:
				frameReadingTask.setMaximalWaitingPeriod(getMaximalFrameDelay(iX));
				frameReadingTask.setStopAfterSingleReading(true);
				frameReadingTask.activateReading();
				break;
			case SPECULATIVE_READING:
				frameReadingTask.setMaximalWaitingPeriod(getMaximalFrameDelay(iX));
				frameReadingTask.setStopAfterSingleReading(true);
				// frameReadingTask.activateReading();
				break;
			case LISTENING:
				activateDataAcquisition(iX);
				break;
			}
		} else {
			resetCounters();
			updateAttributes(iX);
			VideoBufferOperatingMode currentOperatingMode= getOperatingMode(iX);
			switch (currentOperatingMode) {
			case RECORDING:
				startRecording(currentOperatingMode,iX);
				break;
			case PLAYING:
				frameReadingTask.setStopAfterSingleReading(false);
				startReadingOrPlaying(true,currentOperatingMode,iX);
				break;
			case READING:
				frameReadingTask.setStopAfterSingleReading(true);
				startReadingOrPlaying(true,currentOperatingMode,iX);
				break;
			case SPECULATIVE_READING:
				frameReadingTask.setStopAfterSingleReading(true);
				startReadingOrPlaying(false,currentOperatingMode,iX);
				readGivenNumberOfTargetFrames(actingReadBufferSize.get());
				// ((AstrohnDataReadingTask)frameReadingTask).readGivenNumberOfTerahertzFrames(actingReadBufferSize.get());
				break;
			case LISTENING:
				startListening(currentOperatingMode,iX);
				break;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void startRecording(VideoBufferOperatingMode currentOperatingMode, ChoisePoint iX) {
		int currentWriteBufferSize= getWriteBufferSize(iX);
		ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
		frameRecordingTask.setWriteBufferSize(currentWriteBufferSize);
		frameRecordingTask.reset(currentFileName);
		actingDescription.set(getDescription(iX));
		actingCopyright.set(getCopyright(iX));
		actingRegistrationDate.set(getRegistrationDate(iX));
		actingRegistrationTime.set(getRegistrationTime(iX));
		actingVideoBufferOperatingMode.set(currentOperatingMode);
		try {
			activateDataAcquisition(iX);
		} catch (Throwable e) {
			actingVideoBufferOperatingMode.set(null);
			throw e;
		}
	}
	//
	protected void startReadingOrPlaying(boolean doActivateReading, VideoBufferOperatingMode currentOperatingMode, ChoisePoint iX) {
		ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
		int currentTimeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		int currentReadBufferSize= getReadBufferSize(iX);
		NumericalValue currentSlowMotionCoefficient= getSlowMotionCoefficient(iX);
		actingVideoBufferOperatingMode.set(currentOperatingMode);
		actingReadBufferSize.set(currentReadBufferSize);
		try {
			frameReadingTask.setSlowMotionCoefficient(currentSlowMotionCoefficient);
			frameReadingTask.setMaximalWaitingPeriod(getMaximalFrameDelay(iX));
			frameReadingTask.startReading(doActivateReading,currentFileName,currentTimeout,currentCharacterSet,staticContext);
		} catch (Throwable e) {
			actingVideoBufferOperatingMode.set(null);
			throw e;
		}
	}
	//
	protected void startListening(VideoBufferOperatingMode currentOperatingMode, ChoisePoint iX) {
		actingVideoBufferOperatingMode.set(currentOperatingMode);
		try {
			activateDataAcquisition(iX);
		} catch (Throwable e) {
			actingVideoBufferOperatingMode.set(null);
			throw e;
		}
	}
	//
	protected void activateDataAcquisition(ChoisePoint iX) {
	}
	protected void readGivenNumberOfTargetFrames(int number) {
	}
	//
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			counterOfRecentAttributes.set(-1);
			numberOfRecentReceivedFrame.set(-1);
			ownMainColorMap.set(null);
			ownAuxiliaryColorMap.set(null);
			recordedMainColorMap.set(null);
			recordedAuxiliaryColorMap.set(null);
			ownMainColorMapSerialNumber.set(-1);
			ownAuxiliaryColorMapSerialNumber.set(-1);
			recordedMainColorMapSerialNumber.set(-1);
			recordedAuxiliaryColorMapSerialNumber.set(-1);
			recentFrame= null;
			recentFrameIsRepeated= false;
			committedFrame= null;
			dataTransferError.set(null);
			dataAcquisitionError.set(null);
			deliveredMainColorMap.set(null);
			deliveredAuxiliaryColorMap.set(null);
			deliveredDescription.set(null);
			deliveredCopyright.set(null);
			deliveredRegistrationDate.set(null);
			deliveredRegistrationTime.set(null);
			deliveredMainColorMapTerm= null;
			deliveredAuxiliaryColorMapTerm= null;
			deliveredDescriptionTerm= null;
			deliveredCopyrightTerm= null;
			deliveredRegistrationDateTerm= null;
			deliveredRegistrationTimeTerm= null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void pause0s(ChoisePoint iX) {
		VideoBufferOperatingMode currentOperatingMode= actingVideoBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			suspendDataAcquisition();
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			frameReadingTask.suspendReading();
			break;
		case LISTENING:
			suspendDataAcquisition();
			break;
		}
	}
	//
	protected void suspendDataAcquisition() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void stop0s(ChoisePoint iX) {
		VideoBufferOperatingMode currentOperatingMode= actingVideoBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			stopDataAcquisition();
			frameRecordingTask.close();
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			stopDataAcquisition();
			frameReadingTask.closeReading();
			break;
		case LISTENING:
			stopDataAcquisition();
			break;
		}
	}
	//
	protected void stopDataAcquisition() {
		actingVideoBufferOperatingMode.set(null);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isCommitted0s(ChoisePoint iX) throws Backtracking {
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame==null) {
				throw Backtracking.instance;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isActive0s(ChoisePoint iX) throws Backtracking {
		VideoBufferOperatingMode currentOperatingMode= actingVideoBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			throw Backtracking.instance;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			if (dataAcquisitionIsActive()) {
				return;
			};
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			if (frameReadingTask.isNotSuspended()) {
				return;
			};
			break;
		case LISTENING:
			if (dataAcquisitionIsActive()) {
				return;
			};
			break;
		};
		throw Backtracking.instance;
	}
	//
	protected boolean dataAcquisitionIsActive() {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isSuspended0s(ChoisePoint iX) throws Backtracking {
		VideoBufferOperatingMode currentOperatingMode= actingVideoBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			throw Backtracking.instance;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			if (dataAcquisitionIsSuspended()) {
				return;
			};
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			if (frameReadingTask.isSuspended()) {
				return;
			};
			break;
		case LISTENING:
			if (dataAcquisitionIsSuspended()) {
				return;
			};
			break;
		};
		throw Backtracking.instance;
	}
	//
	protected boolean dataAcquisitionIsSuspended() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void eof0s(ChoisePoint iX) throws Backtracking {
		VideoBufferOperatingMode currentOperatingMode= actingVideoBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			throw Backtracking.instance;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			if (!frameReadingTask.eof()) {
				throw Backtracking.instance;
			};
			break;
		case LISTENING:
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit0s(ChoisePoint iX) throws Backtracking {
	}
	//
	protected void commit() {
		containsNewFrame.set(false);
	}
	//
	public void containsNewFrame0s(ChoisePoint iX) throws Backtracking {
		if (!containsNewFrame.get()) {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static double computeFrameRate(long deltaN, long deltaTime) {
		double rate;
		if (deltaTime > 0) {
			rate= deltaN * 1000.0 / deltaTime;
		} else {
			rate= -1.0;
		};
		return rate;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected int[][] prepareMainColorMap(boolean doNotControlColorMaps, DataFrameBaseAttributesInterface attributes) {
		int[][] colorMap;
		if (doNotControlColorMaps) {
			updateRecordedMainColorMap(attributes);
			colorMap= recordedMainColorMap.get().toColors();
		} else {
			updateOwnMainColorMap(recentAttributes.get());
			colorMap= ownMainColorMap.get().toColors();
		};
		return colorMap;
	}
	//
	protected int[][] prepareAuxiliaryColorMap(boolean doNotControlColorMaps, DataFrameBaseAttributesInterface attributes) {
		int[][] colorMap;
		if (doNotControlColorMaps) {
			updateRecordedAuxiliaryColorMap(attributes);
			colorMap= recordedAuxiliaryColorMap.get().toColors();
		} else {
			updateOwnAuxiliaryColorMap(recentAttributes.get());
			colorMap= ownAuxiliaryColorMap.get().toColors();
		};
		return colorMap;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getDeliveredColorMap2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		if (deliveredMainColorMapTerm==null) {
			DetailedColorMap colorMap= deliveredMainColorMap.get();
			if (colorMap != null) {
				deliveredMainColorMapTerm= ColorMapConverters.toTerm(colorMap);
			}
		};
		if (deliveredAuxiliaryColorMapTerm==null) {
			DetailedColorMap colorMap= deliveredAuxiliaryColorMap.get();
			if (colorMap != null) {
				deliveredAuxiliaryColorMapTerm= ColorMapConverters.toTerm(colorMap);
			}
		};
		a1.setBacktrackableValue(deliveredMainColorMapTerm,iX);
		a2.setBacktrackableValue(deliveredAuxiliaryColorMapTerm,iX);
	}
	//
	public void getDeliveredDescription1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredDescriptionTerm==null) {
			String description= deliveredDescription.get();
			if (description != null) {
				deliveredDescriptionTerm= new PrologString(description);
			}
		};
		if (deliveredDescriptionTerm != null) {
			a1.setBacktrackableValue(deliveredDescriptionTerm,iX);
		} else {
			a1.setBacktrackableValue(termEmptyString,iX);
		}
	}
	//
	public void getDeliveredCopyright1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredCopyrightTerm==null) {
			String description= deliveredCopyright.get();
			if (description != null) {
				deliveredCopyrightTerm= new PrologString(description);
			}
		};
		if (deliveredCopyrightTerm != null) {
			a1.setBacktrackableValue(deliveredCopyrightTerm,iX);
		} else {
			a1.setBacktrackableValue(termEmptyString,iX);
		}
	}
	//
	public void getDeliveredRegistrationDate1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredRegistrationDateTerm==null) {
			String description= deliveredRegistrationDate.get();
			if (description != null) {
				deliveredRegistrationDateTerm= new PrologString(description);
			}
		};
		if (deliveredRegistrationDateTerm != null) {
			a1.setBacktrackableValue(deliveredRegistrationDateTerm,iX);
		} else {
			a1.setBacktrackableValue(termEmptyString,iX);
		}
	}
	//
	public void getDeliveredRegistrationTime1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredRegistrationTimeTerm==null) {
			String description= deliveredRegistrationTime.get();
			if (description != null) {
				deliveredRegistrationTimeTerm= new PrologString(description);
			}
		};
		if (deliveredRegistrationTimeTerm != null) {
			a1.setBacktrackableValue(deliveredRegistrationTimeTerm,iX);
		} else {
			a1.setBacktrackableValue(termEmptyString,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void frameObtained0s(ChoisePoint iX) {
	}
	public void dataTransferCompletion0s(ChoisePoint iX) {
	}
	public void bufferOverflow0s(ChoisePoint iX) {
	}
	public void bufferDeallocation0s(ChoisePoint iX) {
	}
	public void dataTransferError1s(ChoisePoint iX, Term a1) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setDataAcquisitionError(DataAcquisitionError error) {
		dataAcquisitionError.set(error);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean sendFrame(DataFrameInterface frame) {
		VideoBufferOperatingMode currentOperatingMode= actingVideoBufferOperatingMode.get();
		if (currentOperatingMode != null && currentOperatingMode==VideoBufferOperatingMode.RECORDING) {
			long currentFrameNumber= updateRecentFrame(frame);
			if (currentFrameNumber==0) {
				String fileDescription= actingDescription.get().getValue(defaultDescription);
				String fileCopyright= actingCopyright.get().getValue(defaultCopyright);
				Calendar timer= new GregorianCalendar();
				Date date= timer.getTime();
				DateFormat dateFormat= DateFormat.getDateInstance(DateFormat.LONG,Locale.ENGLISH);
				// DateFormat timeFormat= DateFormat.getTimeInstance(DateFormat.FULL,Locale.ENGLISH);
				SimpleDateFormat timeFormat= new SimpleDateFormat("HH:mm:ss z",Locale.ENGLISH);
				String textDate= dateFormat.format(date);
				String textTime= timeFormat.format(date);
				String fileRegistrationDate= actingRegistrationDate.get().getValue(textDate);
				String fileRegistrationTime= actingRegistrationTime.get().getValue(textTime);
				ModeFrame modeFrame= new ModeFrame(
					frame.getSerialNumber(),
					frame.getTime(),
					fileDescription,
					fileCopyright,
					fileRegistrationDate,
					fileRegistrationTime,
					frame.getBaseAttributes());
				frameRecordingTask.store(modeFrame);
			};
			frameRecordingTask.store(frame);
			sendFrameObtained();
			return true;
		} else {
			DataArrayType dataArrayType= frame.getDataArrayType();
			if (dataArrayType==DataArrayType.MODE_FRAME) {
				ModeFrame modeFrame= (ModeFrame)frame;
				deliveredDescription.set(modeFrame.getDescription());
				deliveredCopyright.set(modeFrame.getCopyright());
				deliveredRegistrationDate.set(modeFrame.getRegistrationDate());
				deliveredRegistrationTime.set(modeFrame.getRegistrationTime());
				return false;
			} else {
				DataFrameBaseAttributesInterface attributes= frame.getBaseAttributes();
				if (attributes instanceof DataFrameColorfulAttributes) {
					deliveredMainColorMap.set(((DataFrameColorfulAttributes)attributes).getDetailedMainColorMap());
					deliveredAuxiliaryColorMap.set(((DataFrameColorfulAttributes)attributes).getDetailedAuxiliaryColorMap());
				};
				updateRecentFrame(frame);
				sendFrameObtained();
				return true;
			}
		}
	}
	//
	protected long updateRecentFrame(DataFrameInterface frame) {
		return -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void sendFrameObtained() {
		long domainSignature= entry_s_FrameObtained_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void completeDataTransfer(long numberOfAcquiredFrames) {
		// frameReadingTask.closeReading();
		actingVideoBufferOperatingMode.set(null);
		dataTransferError.set(null);
		long domainSignature= entry_s_DataTransferCompletion_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
	public void completeDataTransfer(long numberOfFrameToBeAcquired, Throwable e) {
		// frameReadingTask.closeReading();
		actingVideoBufferOperatingMode.set(null);
		dataTransferError.set(new FrameReadingError(numberOfFrameToBeAcquired,e));
		long domainSignature= entry_s_DataTransferError_1_i();
		Term[] arguments= new Term[]{new PrologString(e.toString())};
		// 2018.10.30: AsyncCall call= new AsyncCall(domainSignature,this,true,false,arguments,true);
		AsyncCall call= new AsyncCall(domainSignature,this,true,true,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	public void reportBufferOverflow() {
		long domainSignature= entry_s_BufferOverflow_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
	//
	public void annulBufferOverflow() {
		long domainSignature= entry_s_BufferDeallocation_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
}
