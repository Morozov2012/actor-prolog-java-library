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
import morozov.system.i3v1.frames.data.*;
import morozov.system.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.modes.*;
import morozov.system.modes.converters.*;
import morozov.system.sound.*;
import morozov.system.sound.frames.*;
import morozov.system.sound.frames.data.interfaces.*;
import morozov.system.sound.frames.interfaces.*;
import morozov.system.sound.interfaces.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public abstract class DataAcquisitionBuffer
		extends ReadWriteBuffer
		implements AudioDataConsumerInterface {
	//
	///////////////////////////////////////////////////////////////
	// Data Acquisition Attributes
	///////////////////////////////////////////////////////////////
	//
	protected DataAcquisitionBufferOperatingMode operatingMode;
	protected YesNo attachAudioData;
	protected IterativeDetailedColorMapInterface mainColorMap;
	protected IterativeDetailedColorMapInterface auxilairyColorMap;
	protected YesNo useRecordedColorMapCommands;
	protected ActionPeriod connectionAttemptPeriod;
	protected Integer maximalErrorsQuantity;
	//
	protected AtomicReference<DataAcquisitionBufferOperatingMode> actingDataAcquisitionBufferOperatingMode= new AtomicReference<>();
	protected AtomicBoolean actingAttachAudioData= new AtomicBoolean(false);
	//
	protected static int defaultDeviceConnectionAttemptPeriod= 100;
	protected static final Term term100= new PrologInteger(100);
	//
	///////////////////////////////////////////////////////////////
	// Audio Data Acquisition Processes & Modes
	///////////////////////////////////////////////////////////////
	//
	protected SoundFramingTask soundFramingTask= new SoundFramingTask(this);
	//
	protected AtomicReference<DataAcquisitionError> dataAcquisitionError= new AtomicReference<>();
	//
	protected AtomicBoolean waitFirstVideoFrame= new AtomicBoolean(false);
	protected AtomicInteger firstVideoFrameIsReceived= new AtomicInteger(0);
	protected int numberOfInitialFramesToBeReceived= 2;
	//
	///////////////////////////////////////////////////////////////
	// Data Frame Reading/Writing Processes & Modes
	///////////////////////////////////////////////////////////////
	//
	protected DataFrameInterface recentFrame;
	protected DataFrameInterface committedFrame;
	//
	protected boolean recentFrameIsRepeated= false;
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
	abstract public Term getBuiltInSlot_E_operating_mode();
	abstract public Term getBuiltInSlot_E_attach_audio_data();
	public Term getBuiltInSlot_E_use_recorded_color_map_commands() {
		return termNo;
	}
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
	// get/set attach_audio_data
	//
	public void setAttachAudioData1s(ChoisePoint iX, Term a1) {
		setAttachAudioData(YesNoConverters.argument2YesNo(a1,iX));
	}
	public void setAttachAudioData(YesNo value) {
		attachAudioData= value;
		actingAttachAudioData.set(attachAudioData.toBoolean());
	}
	public void getAttachAudioData0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(YesNoConverters.toTerm(getAttachAudioData(iX)));
	}
	public void getAttachAudioData0fs(ChoisePoint iX) {
	}
	public YesNo getAttachAudioData(ChoisePoint iX) {
		if (attachAudioData != null) {
			return attachAudioData;
		} else {
			Term value= getBuiltInSlot_E_attach_audio_data();
			return YesNoConverters.argument2YesNo(value,iX);
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
		setConnectionAttemptPeriod(ActionPeriodConverters.argumentToActionPeriod(a1,iX));
	}
	public void setConnectionAttemptPeriod(ActionPeriod value) {
		connectionAttemptPeriod= value;
	}
	public void getConnectionAttemptPeriod0ff(ChoisePoint iX, PrologVariable result) {
		ActionPeriod value= getConnectionAttemptPeriod(iX);
		result.setNonBacktrackableValue(ActionPeriodConverters.toTerm(value));
	}
	public void getConnectionAttemptPeriod0fs(ChoisePoint iX) {
	}
	public ActionPeriod getConnectionAttemptPeriod(ChoisePoint iX) {
		if (connectionAttemptPeriod != null) {
			return connectionAttemptPeriod;
		} else {
			Term value= getBuiltInSlot_E_connection_attempt_period();
			return ActionPeriodConverters.argumentToActionPeriod(value,iX);
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
	public void flushMicrophoneBuffer0s(ChoisePoint iX) {
		flushMicrophoneBuffer();
	}
	//
	public void flushMicrophoneBuffer() {
		soundFramingTask.flush(false,true);
	}
	//
	public void microphoneIsAvailable0s(ChoisePoint iX) throws Backtracking {
		if (!soundFramingTask.microphoneIsAvailable()) {
			throw Backtracking.instance;
		}
	}
	//
	public void microphoneIsActive0s(ChoisePoint iX) throws Backtracking {
		if (!soundFramingTask.microphoneIsActive()) {
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
	@Override
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
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
			dataAcquisitionError.set(null);
			deliveredMainColorMap.set(null);
			deliveredAuxiliaryColorMap.set(null);
			deliveredMainColorMapTerm= null;
			deliveredAuxiliaryColorMapTerm= null;
		}
	}
	//
	@Override
	protected void resetAudioDataCounters() {
		synchronized (numberOfRecentReceivedAudioData) {
			super.resetAudioDataCounters();
			counterOfAcquiredAudioFrames.set(-1);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void start0s(ChoisePoint iX) {
		start(false,iX);
	}
	//
	public void start(boolean requireExclusiveAccess, ChoisePoint iX) {
		DataAcquisitionBufferOperatingMode actingOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		boolean currentOutputAudioData= actingOutputAudioData.get();
		if (actingOperatingMode != null) {
			switch (actingOperatingMode) {
			case RECORDING:
				activateDataAcquisition(false,iX);
				break;
			case PLAYING:
				frameReadingTask.setOutputAudioData(currentOutputAudioData);
				frameReadingTask.setStopAfterSingleReading(false);
				frameReadingTask.setApplyAudioDataTiming(!synchronizeAudioStreamWithFrontVideoFrame());
				frameReadingTask.activateReading();
				break;
			case READING:
				frameReadingTask.setOutputAudioData(currentOutputAudioData);
				frameReadingTask.setStopAfterSingleReading(true);
				frameReadingTask.activateReading();
				break;
			case SPECULATIVE_READING:
				frameReadingTask.setOutputAudioData(currentOutputAudioData);
				frameReadingTask.setStopAfterSingleReading(true);
				break;
			case LISTENING:
				activateDataAcquisition(false,iX);
				break;
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
				frameReadingTask.setApplyAudioDataTiming(!synchronizeAudioStreamWithFrontVideoFrame());
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
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void activateDataAcquisition(boolean flushBuffers, ChoisePoint iX) {
		boolean currentAttachAudioData= getAttachAudioData(iX).toBoolean();
		boolean currentOutputAudioData= getOutputAudioData(iX).toBoolean();
		int currentOutputDebugInformation= Arithmetic.toInteger(getOutputDebugInformation(iX));
		activateAudioSystemIfNecessary(flushBuffers,currentAttachAudioData,currentOutputAudioData,currentOutputDebugInformation);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void startRecording(DataAcquisitionBufferOperatingMode currentOperatingMode, boolean requireExclusiveAccess, ChoisePoint iX) {
		int currentWriteBufferSize= getWriteBufferSize(iX);
		ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
		int currentOutputDebugInformation= Arithmetic.toInteger(getOutputDebugInformation(iX));
		frameRecordingTask.setWriteBufferSize(currentWriteBufferSize);
		frameRecordingTask.setOutputDebugInformation(currentOutputDebugInformation);
		frameRecordingTask.reset(currentFileName);
		setActingMetadata(iX);
		actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
		try {
			activateDataAcquisition(true,iX);
		} catch (Throwable e) {
			resetActingMode();
			throw e;
		}
	}
	//
	protected void startReadingOrPlaying(boolean doActivateReading, DataAcquisitionBufferOperatingMode currentOperatingMode, ChoisePoint iX) {
		ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
		boolean currentAttachAudioData= getAttachAudioData(iX).toBoolean();
		boolean currentOutputAudioData= getOutputAudioData(iX).toBoolean();
		int currentTimeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		int currentReadBufferSize= getReadBufferSize(iX);
		NumericalValue currentSlowMotionCoefficient= getSlowMotionCoefficient(iX);
		IntegerAttribute currentMaximalFrameDelay= getMaximalFrameDelay(iX);
		int currentOutputDebugInformation= Arithmetic.toInteger(getOutputDebugInformation(iX));
		actingReadBufferSize.set(currentReadBufferSize);
		actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
		activateAudioSystemIfNecessary(true,currentAttachAudioData,currentOutputAudioData,currentOutputDebugInformation);
		try {
			frameReadingTask.setSlowMotionCoefficient(currentSlowMotionCoefficient);
			frameReadingTask.setMaximalFrameDelay(currentMaximalFrameDelay);
			frameReadingTask.setOutputAudioData(currentOutputAudioData);
			frameReadingTask.setDisplayingMode(null);
			frameReadingTask.setOutputDebugInformation(currentOutputDebugInformation);
			frameReadingTask.startReading(doActivateReading,currentFileName,currentTimeout,currentCharacterSet,staticContext);
		} catch (Throwable e) {
			resetActingMode();
			throw e;
		}
	}
	//
	protected void startListening(DataAcquisitionBufferOperatingMode currentOperatingMode, boolean requireExclusiveAccess, ChoisePoint iX) {
		int currentReadBufferSize= getReadBufferSize(iX);
		actingReadBufferSize.set(currentReadBufferSize);
		actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
		try {
			activateDataAcquisition(true,iX);
		} catch (Throwable e) {
			resetActingMode();
			throw e;
		}
	}
	//
	@Override
	protected void activateAudioSystemIfNecessary(boolean flushBuffers, boolean currentAttachAudioData, boolean currentOutputAudioData, int currentOutputDebugInformation) {
		super.activateAudioSystemIfNecessary(flushBuffers,currentAttachAudioData,currentOutputAudioData,currentOutputDebugInformation);
		actingAttachAudioData.set(currentAttachAudioData);
		if (currentAttachAudioData) {
			soundFramingTask.setOutputDebugInformation(currentOutputDebugInformation);
			AudioFormatBaseAttributesInterface format= soundFramingTask.getAudioFormat();
			recentAudioFormat.set(format);
			if (flushBuffers) {
				soundFramingTask.flush(true,true);
			};
			soundFramingTask.startDataTransfer();
		}
	}
	//
	@Override
	public void implementAudioSystemReset(boolean forgetAudioFormat) {
		resetAudioDataCounters();
		if (forgetAudioFormat) {
			recentAudioFormat.set(null);
		};
		if (synchronizeAudioStreamWithFrontVideoFrame()) {
			waitFirstVideoFrame.set(true);
			firstVideoFrameIsReceived.set(0);
		}
	}
	//
	@Override
	public void implementAudioFormatReset() {
		recentAudioFormat.set(null);
	}
	//
	@Override
	protected void resetActingMode() {
		super.resetActingMode();
		actingDataAcquisitionBufferOperatingMode.set(null);
		actingAttachAudioData.set(false);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
		}
	}
	//
	protected void suspendRecording(ChoisePoint iX) {
		stopAudioSystemIfNecessary(iX);
	}
	protected void suspendReading(ChoisePoint iX) {
		if (frameReadingTask != null) {
			frameReadingTask.suspendReading();
		}
	}
	protected void suspendListening(ChoisePoint iX) {
		stopAudioSystemIfNecessary(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
		}
	}
	//
	protected void doStop() {
		DataAcquisitionBufferOperatingMode currentOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			doStopRecording();
			break;
		case PLAYING:
		case READING:
		case SPECULATIVE_READING:
			doStopReading();
			break;
		case LISTENING:
			doStopListening();
			break;
		}
	}
	//
	protected void stopRecording(ChoisePoint iX) {
		doStopRecording();
	}
	protected void doStopRecording() {
		resetActingMode();
		frameRecordingTask.close();
		doStopAudioSystemIfNecessary();
	}
	//
	protected void stopReading(ChoisePoint iX) {
		doStopReading();
	}
	protected void doStopReading() {
		resetActingMode();
		if (frameReadingTask != null) {
			frameReadingTask.closeReading();
		}
	}
	//
	protected void stopListening(ChoisePoint iX) {
		doStopListening();
	}
	protected void doStopListening() {
		resetActingMode();
		doStopAudioSystemIfNecessary();
	}
	//
	@Override
	protected void stopAudioSystemIfNecessary(ChoisePoint iX) {
		super.stopAudioSystemIfNecessary(iX);
		actingAttachAudioData.set(false);
		soundFramingTask.stopDataTransfer();
	}
	@Override
	protected void doStopAudioSystemIfNecessary() {
		super.doStopAudioSystemIfNecessary();
		actingAttachAudioData.set(false);
		soundFramingTask.stopDataTransfer();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
		}
	}
	//
	protected boolean dataAcquisitionIsActive() {
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
		}
	}
	//
	protected boolean dataAcquisitionIsSuspended() {
		return true;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
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
	@Override
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
	public void setDataAcquisitionError(DataAcquisitionError error) {
		dataAcquisitionError.set(error);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean sendDataFrame(DataFrameInterface frame) {
		DataAcquisitionBufferOperatingMode currentOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (currentOperatingMode != null && currentOperatingMode==DataAcquisitionBufferOperatingMode.RECORDING) {
			long currentFrameNumber;
			if (frame instanceof AudioDataFrameInterface) {
				AudioDataFrameInterface audioDataFrame= (AudioDataFrameInterface)frame;
				currentFrameNumber= updateRecentAudioData(audioDataFrame);
			} else {
				currentFrameNumber= updateRecentFrame(frame);
			};
			if (currentFrameNumber == 0) {
				ModeFrame modeFrame= createModeFrame(frame.getBaseAttributes());
				frameRecordingTask.store(modeFrame);
			};
			frameRecordingTask.store(frame);
			if (frame instanceof AudioDataFrameInterface) {
				sendAudioDataObtained();
			} else {
				sendFrameObtainedIfNecessary(frame);
			};
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
			} else if (frame instanceof AudioDataFrameInterface) {
				AudioDataFrameInterface audioDataFrame= (AudioDataFrameInterface)frame;
				byte[] audioDataArray= audioDataFrame.getAudioData();
				if (actingOutputAudioData.get()) {
					soundPlayingTask.generateSound(audioDataArray);
				};
				updateRecentAudioData(audioDataFrame);
				sendAudioDataObtained();
				return true;
			} else {
				DataFrameBaseAttributesInterface attributes= frame.getBaseAttributes();
				if (attributes instanceof ThermalDataFrameColorfulAttributes) {
					deliveredMainColorMap.set(((ThermalDataFrameColorfulAttributes)attributes).getDetailedMainColorMap());
					deliveredAuxiliaryColorMap.set(((ThermalDataFrameColorfulAttributes)attributes).getDetailedAuxiliaryColorMap());
				} else if (attributes instanceof DataFrameColorfulAttributes) {
					deliveredMainColorMap.set(((DataFrameColorfulAttributes)attributes).getDetailedMainColorMap());
					deliveredAuxiliaryColorMap.set(((DataFrameColorfulAttributes)attributes).getDetailedAuxiliaryColorMap());
				};
				updateRecentFrame(frame);
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
	@Override
	public boolean sendCompoundFrame(CompoundFrameInterface frame) {
		return false;
	}
	@Override
	public boolean sendKinectFrame(KinectFrameInterface frame) {
		return false;
	}
	//
	protected long updateRecentFrame(DataFrameInterface frame) {
		if (frame.isLightweightFrame()) {
			return -1;
		};
		synchronized (numberOfRecentReceivedFrame) {
			long currentFrameNumber= acceptFrame(frame,-1);
			if (currentFrameNumber >= 0) {
				numberOfRecentReceivedFrame.notifyAll();
			};
			return currentFrameNumber;
		}
	}
	//
	protected long acceptFrame(DataFrameInterface frame, long currentFrameNumber) {
		if (frame instanceof AudioDataFrameInterface) {
			AudioDataFrameInterface audioFrame= (AudioDataFrameInterface)frame;
			currentFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			updateHistory(audioFrame);
		};
		if (currentFrameNumber >= 0) {
			recentFrame= frame;
			committedFrameWasAssignedDirectly.set(false);
			recentFrameIsRepeated= false;
		};
		return currentFrameNumber;
	}
	//
	protected void updateHistory(DataFrameInterface recentDataFrame) {
		if (recentDataFrame==null) {
			return;
		};
		updateHistory(new EnumeratedDataFrame(recentDataFrame,numberOfRecentReceivedFrame.get()));
	}
	//
	protected long updateRecentAudioData(AudioDataFrameInterface frame) {
		synchronized (numberOfRecentReceivedAudioData) {
			long currentFrameNumber= numberOfRecentReceivedAudioData.incrementAndGet();
			recentAudioData= frame;
			committedAudioDataWasAssignedDirectly.set(false);
			recentAudioDataIsRepeated= false;
			return currentFrameNumber;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setAudioData(byte[] buffer, long time) {
		if (buffer==null) {
			return;
		};
		boolean currentAttachAudioData= actingAttachAudioData.get();
		if (currentAttachAudioData) {
			if (synchronizeAudioStreamWithFrontVideoFrame()) {
				if (waitFirstVideoFrame.get() && (firstVideoFrameIsReceived.get() < numberOfInitialFramesToBeReceived)) {
					return;
				}
			}
		} else {
			return;
		};
		DataAcquisitionBufferOperatingMode actingOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (actingOperatingMode==null) {
			return;
		};
		if (	actingOperatingMode==DataAcquisitionBufferOperatingMode.LISTENING ||
			actingOperatingMode==DataAcquisitionBufferOperatingMode.RECORDING) {
			AudioFormatBaseAttributesInterface format= recentAudioFormat.get();
			if (format==null) {
				format= soundFramingTask.getAudioFormat();
				recentAudioFormat.set(format);
			};
			if (format != null) {
				AudioDataFrame frame= new EncodedAudioDataFrame(
					counterOfAcquiredAudioFrames.incrementAndGet(),
					time,
					buffer,
					format,
					recentAttributes.get());
				sendDataFrame(frame);
			}
		}
	}
	//
	@Override
	public void reportMicrophoneAvailability(boolean state) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void flushAudioSystemIfNecessary() {
		boolean givenAttachAudioData= actingAttachAudioData.get();
		if (givenAttachAudioData) {
			if (synchronizeAudioStreamWithFrontVideoFrame()) {
				if (waitFirstVideoFrame.get()) {
					if (firstVideoFrameIsReceived.getAndIncrement() < numberOfInitialFramesToBeReceived) {
						flushMicrophoneBuffer();
						flushAudioBuffer();
						resetFrameRateAndAudioDataRate();
						waitFirstVideoFrame.set(false);
					}
				}
			} else {
				flushMicrophoneBuffer();
				flushAudioBuffer();
				resetFrameRateAndAudioDataRate();
				waitFirstVideoFrame.set(false);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void completeDataReading(long numberOfAcquiredFrames) {
		resetActingMode();
		super.completeDataReading(numberOfAcquiredFrames);
	}
	@Override
	public void completeDataReading(long numberOfFrameToBeAcquired, Throwable e) {
		super.completeDataReading(numberOfFrameToBeAcquired,e);
	}
	//
	@Override
	public void completeDataWriting(long numberOfFrame, Throwable e) {
		super.completeDataWriting(numberOfFrame,e);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected boolean isSpeculativeReadingMode() {
		DataAcquisitionBufferOperatingMode currentOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		return (currentOperatingMode==DataAcquisitionBufferOperatingMode.SPECULATIVE_READING);
	}
}
