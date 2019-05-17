// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.errors.*;
import morozov.system.files.*;
import morozov.system.frames.*;
import morozov.system.frames.converters.*;
import morozov.system.frames.data.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.modes.*;
import morozov.system.modes.converters.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public abstract class DataAcquisitionBuffer extends ReadWriteBuffer {
	//
	protected DataAcquisitionBufferOperatingMode operatingMode;
	protected DetailedColorMap mainColorMap;
	protected DetailedColorMap auxilairyColorMap;
	protected YesNo useRecordedColorMapCommands;
	//
	protected ActionPeriod connectionAttemptPeriod;
	protected Integer maximalErrorsQuantity;
	//
	protected AtomicReference<DataAcquisitionBufferOperatingMode> actingDataAcquisitionBufferOperatingMode= new AtomicReference<>();
	//
	protected AtomicReference<DataAcquisitionError> dataAcquisitionError= new AtomicReference<>();
	//
	protected AtomicLong counterOfRecentAttributes= new AtomicLong(-1);
	// protected AtomicLong numberOfRecentReceivedFrame= new AtomicLong(-1);
	//
	protected AtomicReference<DataFrameColorfulAttributesInterface> recentAttributes= new AtomicReference<>();
	protected boolean recentFrameIsRepeated= false;
	//
	protected AtomicReference<DetailedColorMap> ownMainColorMap= new AtomicReference<>();
	protected AtomicReference<DetailedColorMap> ownAuxiliaryColorMap= new AtomicReference<>();
	protected AtomicReference<DetailedColorMap> recordedMainColorMap= new AtomicReference<>();
	protected AtomicReference<DetailedColorMap> recordedAuxiliaryColorMap= new AtomicReference<>();
	//
	protected AtomicLong ownMainColorMapSerialNumber= new AtomicLong(-1);
	protected AtomicLong ownAuxiliaryColorMapSerialNumber= new AtomicLong(-1);
	protected AtomicLong recordedMainColorMapSerialNumber= new AtomicLong(-1);
	protected AtomicLong recordedAuxiliaryColorMapSerialNumber= new AtomicLong(-1);
	//
	protected DataFrameInterface recentFrame;
	protected DataFrameInterface committedFrame;
	//
	// protected AtomicBoolean containsNewFrame= new AtomicBoolean(false);
	//
	protected AtomicReference<DetailedColorMap> deliveredMainColorMap= new AtomicReference<>();
	protected AtomicReference<DetailedColorMap> deliveredAuxiliaryColorMap= new AtomicReference<>();
	//
	protected Term deliveredMainColorMapTerm;
	protected Term deliveredAuxiliaryColorMapTerm;
	//
	protected static YesNo defaultZoomMode= YesNo.NO;
	protected static NumericalValue defaultZoomingCoefficient= new NumericalValue(5.0);
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
	protected static int defaultDeviceConnectionAttemptPeriod= 100;
	//
	// protected static Term termNo= new PrologSymbol(SymbolCodes.symbolCode_E_no);
	protected static final Term term100= new PrologInteger(100);
	//
	///////////////////////////////////////////////////////////////
	//
	public DataAcquisitionBuffer() {
	}
	public DataAcquisitionBuffer(
			DataFrameReadingTask givenFrameReadingTask,
			DataFrameRecordingTask givenFrameRecordingTask) {
		super(givenFrameReadingTask,givenFrameRecordingTask);
	}
	public DataAcquisitionBuffer(GlobalWorldIdentifier id) {
		super(id);
	}
	public DataAcquisitionBuffer(
			GlobalWorldIdentifier id,
			DataFrameReadingTask givenFrameReadingTask,
			DataFrameRecordingTask givenFrameRecordingTask) {
		super(id,givenFrameReadingTask,givenFrameRecordingTask);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// abstract public long entry_s_FrameObtained_0();
	// abstract public long entry_s_DataTransferCompletion_0();
	// abstract public long entry_s_BufferOverflow_0();
	// abstract public long entry_s_BufferDeallocation_0();
	// abstract public long entry_s_DataTransferError_1_i();
	//
	abstract public Term getBuiltInSlot_E_operating_mode();
	// abstract public Term getBuiltInSlot_E_description();
	// abstract public Term getBuiltInSlot_E_copyright();
	// abstract public Term getBuiltInSlot_E_registration_date();
	// abstract public Term getBuiltInSlot_E_registration_time();
	public Term getBuiltInSlot_E_use_recorded_color_map_commands() {
		return termNo;
	}
	// abstract public Term getBuiltInSlot_E_write_buffer_size();
	// abstract public Term getBuiltInSlot_E_read_buffer_size();
	// abstract public Term getBuiltInSlot_E_slow_motion_coefficient();
	// abstract public Term getBuiltInSlot_E_maximal_frame_delay();
	//
	public Term getBuiltInSlot_E_connection_attempt_period() {
		return termDefault;
	}
	public Term getBuiltInSlot_E_maximal_errors_quantity() {
		return term100;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set operating_mode
	//
	public void setOperatingMode1s(ChoisePoint iX, Term a1) {
		setOperatingMode(DataAcquisitionBufferOperatingModeConverters.argumentToDataAcquisitionBufferOperatingMode(a1,iX));
	}
	public void setOperatingMode(DataAcquisitionBufferOperatingMode value) {
		operatingMode= value;
	}
	public void getOperatingMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(DataAcquisitionBufferOperatingModeConverters.toTerm(getOperatingMode(iX)));
	}
	public void getOperatingMode0fs(ChoisePoint iX) {
	}
	public DataAcquisitionBufferOperatingMode getOperatingMode(ChoisePoint iX) {
		if (operatingMode != null) {
			return operatingMode;
		} else {
			Term value= getBuiltInSlot_E_operating_mode();
			return DataAcquisitionBufferOperatingModeConverters.argumentToDataAcquisitionBufferOperatingMode(value,iX);
		}
	}
	//
	// get/set use_recorded_color_map_commands
	//
	public void setUseRecordedColorMapCommands1s(ChoisePoint iX, Term a1) {
		setUseRecordedColorMapCommands(YesNoConverters.argument2YesNo(a1,iX));
	}
	public void setUseRecordedColorMapCommands(YesNo value) {
		useRecordedColorMapCommands= value;
	}
	public void getUseRecordedColorMapCommands0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getUseRecordedColorMapCommands(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getUseRecordedColorMapCommands0fs(ChoisePoint iX) {
	}
	public YesNo getUseRecordedColorMapCommands(ChoisePoint iX) {
		if (useRecordedColorMapCommands != null) {
			return useRecordedColorMapCommands;
		} else {
			Term value= getBuiltInSlot_E_use_recorded_color_map_commands();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set connection_attempt_period
	//
	public void setConnectionAttemptPeriod1s(ChoisePoint iX, Term a1) {
		setConnectionAttemptPeriod(ActionPeriod.argumentToActionPeriod(a1,iX));
	}
	public void setConnectionAttemptPeriod(ActionPeriod value) {
		connectionAttemptPeriod= value;
	}
	public void getConnectionAttemptPeriod0ff(ChoisePoint iX, PrologVariable result) {
		ActionPeriod value= getConnectionAttemptPeriod(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getConnectionAttemptPeriod0fs(ChoisePoint iX) {
	}
	public ActionPeriod getConnectionAttemptPeriod(ChoisePoint iX) {
		if (connectionAttemptPeriod != null) {
			return connectionAttemptPeriod;
		} else {
			Term value= getBuiltInSlot_E_connection_attempt_period();
			return ActionPeriod.argumentToActionPeriod(value,iX);
		}
	}
	//
	// get/set maximal_errors_quantity
	//
	public void setMaximalErrorsQuantity1s(ChoisePoint iX, Term a1) {
		setMaximalErrorsQuantity(GeneralConverters.argumentToSmallInteger(a1,iX));
	}
	public void setMaximalErrorsQuantity(int value) {
		maximalErrorsQuantity= value;
	}
	public void getMaximalErrorsQuantity0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getMaximalErrorsQuantity(iX)));
	}
	public void getMaximalErrorsQuantity0fs(ChoisePoint iX) {
	}
	public int getMaximalErrorsQuantity(ChoisePoint iX) {
		if (maximalErrorsQuantity != null) {
			return maximalErrorsQuantity;
		} else {
			Term value= getBuiltInSlot_E_maximal_errors_quantity();
			return GeneralConverters.argumentToSmallInteger(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateAttributesIfNecessary(ChoisePoint iX) {
		if (recentAttributes.get()==null) {
			updateAttributes(iX);
		}
	}
	//
	protected void updateAttributes(ChoisePoint iX) {
		DataFrameColorfulAttributes attributes= new DataFrameColorfulAttributes(
			counterOfRecentAttributes.incrementAndGet(),
			getAutorangingMode(iX).toBoolean(),
			getDoubleColorMapMode(iX).toBoolean(),
			DataRange.BOUNDS, // getSelectedDataRange(),
			NumericalValueConverters.toDouble(getLowerTemperatureBound(iX)),
			NumericalValueConverters.toDouble(getUpperTemperatureBound(iX)),
			NumericalValueConverters.toDouble(getLowerMainTemperatureQuantile(iX)),
			NumericalValueConverters.toDouble(getUpperMainTemperatureQuantile(iX)),
			NumericalValueConverters.toDouble(getLowerAuxiliaryTemperatureQuantile(iX)),
			NumericalValueConverters.toDouble(getUpperAuxiliaryTemperatureQuantile(iX)),
			getMainColorMap(iX),
			getAuxiliaryColorMap(iX),
			getAveragingMode(iX).toBoolean(),
			getZoomImage(iX).toBoolean(),
			getZoomingCoefficient(iX));
		recentAttributes.set(attributes);
	}
	//
	public YesNo getZoomImage(ChoisePoint iX) {
		return defaultZoomMode;
	}
	public NumericalValue getZoomingCoefficient(ChoisePoint iX) {
		return defaultZoomingCoefficient;
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
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
			counterOfRecentAttributes.set(-1);
			// numberOfRecentReceivedFrame.set(-1);
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
			// containsNewFrame.set(false);
			dataAcquisitionError.set(null);
			// dataReadingError.set(null);
			// dataWritingError.set(null);
			deliveredMainColorMap.set(null);
			deliveredAuxiliaryColorMap.set(null);
			// deliveredDescription.set(null);
			// deliveredCopyright.set(null);
			// deliveredRegistrationDate.set(null);
			// deliveredRegistrationTime.set(null);
			deliveredMainColorMapTerm= null;
			deliveredAuxiliaryColorMapTerm= null;
			// deliveredDescriptionTerm= null;
			// deliveredCopyrightTerm= null;
			// deliveredRegistrationDateTerm= null;
			// deliveredRegistrationTimeTerm= null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void start0s(ChoisePoint iX) {
		start(false,iX);
	}
	//
	public void start(boolean requireExclusiveAccess, ChoisePoint iX) {
		DataAcquisitionBufferOperatingMode actingOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (actingOperatingMode != null) {
			switch (actingOperatingMode) {
			case RECORDING:
				activateDataAcquisition(iX);
				break;
			case PLAYING:
				frameReadingTask.setStopAfterSingleReading(false);
				frameReadingTask.activateReading();
				break;
			case READING:
				frameReadingTask.setStopAfterSingleReading(true);
				frameReadingTask.activateReading();
				break;
			case SPECULATIVE_READING:
				frameReadingTask.setStopAfterSingleReading(true);
				// frameReadingTask.activateReading();
				break;
			case LISTENING:
				activateDataAcquisition(iX);
				break;
			// case DISPLAYING:
			//	break;
			}
		} else {
			resetCounters();
			updateAttributes(iX);
			DataAcquisitionBufferOperatingMode currentOperatingMode= getOperatingMode(iX);
			switch (currentOperatingMode) {
			case RECORDING:
				startRecording(currentOperatingMode,requireExclusiveAccess,iX);
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
				frameReadingTask.readGivenNumberOfFrames(actingReadBufferSize.get());
				break;
			case LISTENING:
				startListening(currentOperatingMode,requireExclusiveAccess,iX);
				break;
			// case DISPLAYING:
			//	startDisplaying(currentOperatingMode,iX);
			//	break;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void activateDataAcquisition(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void startRecording(DataAcquisitionBufferOperatingMode currentOperatingMode, boolean requireExclusiveAccess, ChoisePoint iX) {
		int currentWriteBufferSize= getWriteBufferSize(iX);
		ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
		int currentOutputDebugInformation= PrologInteger.toInteger(getOutputDebugInformation(iX));
		frameRecordingTask.setWriteBufferSize(currentWriteBufferSize);
		frameRecordingTask.setOutputDebugInformation(currentOutputDebugInformation);
		frameRecordingTask.reset(currentFileName);
		setActingMetadata(iX);
		actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
		try {
			activateDataAcquisition(iX);
		} catch (Throwable e) {
			actingDataAcquisitionBufferOperatingMode.set(null);
			throw e;
		}
	}
	//
	protected void startReadingOrPlaying(boolean doActivateReading, DataAcquisitionBufferOperatingMode currentOperatingMode, ChoisePoint iX) {
		ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
		int currentTimeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		int currentReadBufferSize= getReadBufferSize(iX);
		NumericalValue currentSlowMotionCoefficient= getSlowMotionCoefficient(iX);
		IntegerAttribute currentMaximalFrameDelay= getMaximalFrameDelay(iX);
		actingReadBufferSize.set(currentReadBufferSize);
		actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
		try {
			frameReadingTask.setSlowMotionCoefficient(currentSlowMotionCoefficient);
			frameReadingTask.setMaximalFrameDelay(currentMaximalFrameDelay);
			frameReadingTask.setDisplayingMode(null);
			frameReadingTask.startReading(doActivateReading,currentFileName,currentTimeout,currentCharacterSet,staticContext);
		} catch (Throwable e) {
			actingDataAcquisitionBufferOperatingMode.set(null);
			throw e;
		}
	}
	//
	protected void startListening(DataAcquisitionBufferOperatingMode currentOperatingMode, boolean requireExclusiveAccess, ChoisePoint iX) {
		int currentReadBufferSize= getReadBufferSize(iX);
		actingReadBufferSize.set(currentReadBufferSize);
		actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
		try {
			activateDataAcquisition(iX);
		} catch (Throwable e) {
			actingDataAcquisitionBufferOperatingMode.set(null);
			throw e;
		}
	}
	//
	// protected void startDisplaying(DataAcquisitionBufferOperatingMode currentOperatingMode, ChoisePoint iX) {
	//	int currentReadBufferSize= getReadBufferSize(iX);
	//	actingReadBufferSize.set(currentReadBufferSize);
	//	actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public void pause0s(ChoisePoint iX) {
		DataAcquisitionBufferOperatingMode currentOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			suspendRecording(iX);
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			suspendReading(iX);
			break;
		case LISTENING:
			suspendListening(iX);
			break;
		// case DISPLAYING:
		//	break;
		}
	}
	//
	protected void suspendRecording(ChoisePoint iX) {
	}
	protected void suspendReading(ChoisePoint iX) {
		if (frameReadingTask != null) {
			frameReadingTask.suspendReading();
		}
	}
	protected void suspendListening(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void stop0s(ChoisePoint iX) {
		stop(iX);
	}
	//
	protected void stop(ChoisePoint iX) {
		DataAcquisitionBufferOperatingMode currentOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			stopRecording(iX);
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			stopReading(iX);
			break;
		case LISTENING:
			stopListening(iX);
			break;
		// case DISPLAYING:
		//	stopDisplaying(iX);
		//	break;
		}
	}
	//
	protected void stopRecording(ChoisePoint iX) {
		actingDataAcquisitionBufferOperatingMode.set(null);
		frameRecordingTask.close();
	}
	protected void stopReading(ChoisePoint iX) {
		actingDataAcquisitionBufferOperatingMode.set(null);
		if (frameReadingTask != null) {
			frameReadingTask.closeReading();
		}
	}
	protected void stopListening(ChoisePoint iX) {
		actingDataAcquisitionBufferOperatingMode.set(null);
	}
	// protected void stopDisplaying(ChoisePoint iX) {
	//	actingDataAcquisitionBufferOperatingMode.set(null);
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public void isOpen0s(ChoisePoint iX) throws Backtracking {
		DataAcquisitionBufferOperatingMode currentOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			throw Backtracking.instance;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			if (!frameRecordingTask.outputIsOpen()) {
				throw Backtracking.instance;
			};
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			if (!frameReadingTask.inputIsOpen()) {
				throw Backtracking.instance;
			};
			break;
		case LISTENING:
			if (!dataAcquisitionIsActive()) {
				throw Backtracking.instance;
			};
			break;
		// case DISPLAYING:
		//	break;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isActive0s(ChoisePoint iX) throws Backtracking {
		DataAcquisitionBufferOperatingMode currentOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			throw Backtracking.instance;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			if (!dataAcquisitionIsActive()) {
				throw Backtracking.instance;
			};
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			if (!frameReadingTask.inputIsOpen()) {
				throw Backtracking.instance;
			} else if (frameReadingTask.stopThisThread()) {
				throw Backtracking.instance;
			};
			break;
		case LISTENING:
			if (!dataAcquisitionIsActive()) {
				throw Backtracking.instance;
			};
			break;
		// case DISPLAYING:
		//	break;
		}
	}
	//
	protected boolean dataAcquisitionIsActive() {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isSuspended0s(ChoisePoint iX) throws Backtracking {
		DataAcquisitionBufferOperatingMode currentOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			throw Backtracking.instance;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			if (!dataAcquisitionIsSuspended()) {
				throw Backtracking.instance;
			};
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			if (!frameReadingTask.isSuspended()) {
				throw Backtracking.instance;
			};
			break;
		case LISTENING:
			if (!dataAcquisitionIsSuspended()) {
				throw Backtracking.instance;
			};
			break;
		// case DISPLAYING:
		//	throw Backtracking.instance;
		}
	}
	//
	protected boolean dataAcquisitionIsSuspended() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void eof0s(ChoisePoint iX) throws Backtracking {
		DataAcquisitionBufferOperatingMode currentOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
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
		// case DISPLAYING:
		//	throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit0s(ChoisePoint iX) throws Backtracking {
		if (committedFrameWasAssignedDirectly.get()) {
			if (committedFrame==null) {
				throw Backtracking.instance;
			}
		} else {
			synchronized (numberOfRecentReceivedFrame) {
				if (recentFrame==null) {
					throw Backtracking.instance;
				};
				commit();
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isCommitted0s(ChoisePoint iX) throws Backtracking {
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrameIsNull()) {
				throw Backtracking.instance;
			}
		}
	}
	//
	protected boolean committedFrameIsNull() {
		return (committedFrame==null);
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
	public void getDeliveredColorMaps2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
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
	///////////////////////////////////////////////////////////////
	//
	public void setDataAcquisitionError(DataAcquisitionError error) {
		dataAcquisitionError.set(error);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean sendDataFrame(DataFrameInterface frame) {
		DataAcquisitionBufferOperatingMode currentOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (currentOperatingMode != null && currentOperatingMode==DataAcquisitionBufferOperatingMode.RECORDING) {
			long currentFrameNumber= updateRecentFrame(frame);
			if (currentFrameNumber == 0) {
				ModeFrame modeFrame= createModeFrame(frame.getBaseAttributes());
				frameRecordingTask.store(modeFrame);
			};
			frameRecordingTask.store(frame);
			// sendFrameObtained();
			sendFrameObtainedIfNecessary(frame);
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
				// sendFrameObtained();
				sendFrameObtainedIfNecessary(frame);
				return true;
			}
		}
	}
	//
	public void sendFrameObtainedIfNecessary(DataFrameInterface frame) {
		sendFrameObtained();
	}
	//
	protected ModeFrame createModeFrame(DataFrameBaseAttributesInterface givenAttributes) {
		MetadataDescription metadataDescription= createMetadataDescription();
		ModeFrame modeFrame= new ModeFrame(
			-1,
			-1,
			metadataDescription.getDescription(),
			metadataDescription.getCopyright(),
			metadataDescription.getRegistrationDate(),
			metadataDescription.getRegistrationTime(),
			givenAttributes);
		return modeFrame;
	}
	//
	public boolean sendCompoundFrame(CompoundFrameInterface frame) {
		return false;
	}
	public boolean sendKinectFrame(KinectFrameInterface frame) {
		return false;
	}
	//
	protected long updateRecentFrame(DataFrameInterface frame) {
		synchronized (numberOfRecentReceivedFrame) {
			numberOfRecentReceivedFrame.notifyAll();
		};
		return -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void completeDataReading(long numberOfAcquiredFrames) {
		actingDataAcquisitionBufferOperatingMode.set(null);
		super.completeDataReading(numberOfAcquiredFrames);
	}
	public void completeDataReading(long numberOfFrameToBeAcquired, Throwable e) {
		super.completeDataReading(numberOfFrameToBeAcquired,e);
	}
	//
	public void completeDataWriting(long numberOfFrame, Throwable e) {
		super.completeDataWriting(numberOfFrame,e);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected boolean isSpeculativeReadingMode() {
		DataAcquisitionBufferOperatingMode currentOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		return (currentOperatingMode==DataAcquisitionBufferOperatingMode.SPECULATIVE_READING);
	}
}
