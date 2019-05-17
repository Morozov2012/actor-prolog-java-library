// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.checker.signals.*;
import morozov.system.i3v1.*;
import morozov.system.i3v1.converters.*;
import morozov.system.i3v1.errors.*;
import morozov.system.i3v1.frames.*;
import morozov.system.i3v1.frames.data.*;
import morozov.system.i3v1.frames.data.interfaces.*;
import morozov.system.i3v1.frames.interfaces.*;
import morozov.system.i3v1.interfaces.*;
import morozov.system.converters.*;
import morozov.system.files.*;
import morozov.system.files.errors.*;
import morozov.system.frames.converters.*;
import morozov.system.frames.data.interfaces.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.system.modes.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Arrays;
import java.math.BigInteger;

public abstract class TEV1 extends ThermalDataAcquisitionBuffer implements I3DataConsumerInterface {
	//
	protected DeviceIdentifier defaultIdentifier;
	protected TemperatureScale temperatureScale;
	protected YesNo useRecordedCalibrationCommands;
	protected YesNo eliminateAnomalousPixels;
	protected YesNo applyAnomalousVoltagePixelDetector;
	protected NumericalValue anomalousVoltageThreshold;
	protected YesNo applyAnomalousTemperaturePixelDetector;
	protected NumericalValue anomalousTemperatureThreshold;
	protected YesNo doNotSuspendUSBDataTransfer;
	protected BigInteger readTimeOut;
	protected BigInteger writeTimeOut;
	//
	protected I3Camera camera= new I3Camera();
	protected I3DataAcquisition thermalDataAcquisition= new I3DataAcquisition(camera);
	protected AtomicLong counterOfAcquiredFrames= new AtomicLong(-1);
	// protected AtomicLong numberOfRecentAcceptedFrame= new AtomicLong(-1);
	protected long numberOfRepeatedFrame= -1;
	//
	protected long committedFrameNumber= -1;
	protected long committedFrameTime= -1;
	protected long firstCommittedFrameNumber= -1;
	protected long firstCommittedFrameTime= -1;
	//
	protected long validFrameNumber= 0;
	protected long validFrameTime= -1;
	protected long firstValidFrameNumber= -1;
	protected long firstValidFrameTime= -1;
	//
	protected long corruptedFrameNumber= 0;
	protected long corruptedFrameTime= -1;
	protected long firstCorruptedFrameNumber= -1;
	protected long firstCorruptedFrameTime= -1;
	//
	///////////////////////////////////////////////////////////////
	//
	public TEV1() {
		super(	new I3DataReadingTask(),
			new DataFrameRecordingTask());
		connectTEV1ClassInstance();
	}
	public TEV1(GlobalWorldIdentifier id) {
		super(	id,
			new I3DataReadingTask(),
			new DataFrameRecordingTask());
		connectTEV1ClassInstance();
	}
	//
	protected void connectTEV1ClassInstance() {
		thermalDataAcquisition.setDataConsumer(this);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_default_identifier();
	abstract public Term getBuiltInSlot_E_temperature_scale();
	abstract public Term getBuiltInSlot_E_use_recorded_calibration_commands();
	abstract public Term getBuiltInSlot_E_eliminate_anomalous_pixels();
	abstract public Term getBuiltInSlot_E_apply_anomalous_voltage_pixel_detector();
	abstract public Term getBuiltInSlot_E_anomalous_voltage_threshold();
	abstract public Term getBuiltInSlot_E_apply_anomalous_temperature_pixel_detector();
	abstract public Term getBuiltInSlot_E_anomalous_temperature_threshold();
	abstract public Term getBuiltInSlot_E_do_not_suspend_USB_data_transfer();
	abstract public Term getBuiltInSlot_E_read_time_out();
	abstract public Term getBuiltInSlot_E_write_time_out();
	//
	abstract public long entry_s_CompleteCalibration_0();
	abstract public long entry_s_MissedFrame_1_i();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set default_identifier
	//
	public void setDefaultIdentifier1s(ChoisePoint iX, Term a1) {
		DeviceIdentifier value= DeviceIdentifierConverters.argumentToDeviceIdentifier(a1,iX);
		setDefaultIdentifier(value);
	}
	public void setDefaultIdentifier(DeviceIdentifier value) {
		defaultIdentifier= value;
	}
	public void getDefaultIdentifier0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(DeviceIdentifierConverters.toTerm(getDefaultIdentifier(iX)));
	}
	public void getDefaultIdentifier0fs(ChoisePoint iX) {
	}
	public DeviceIdentifier getDefaultIdentifier(ChoisePoint iX) {
		if (defaultIdentifier != null) {
			return defaultIdentifier;
		} else {
			Term value= getBuiltInSlot_E_default_identifier();
			return DeviceIdentifierConverters.argumentToDeviceIdentifier(value,iX);
		}
	}
	//
	// get/set temperature_scale
	//
	public void setTemperatureScale1s(ChoisePoint iX, Term a1) {
		TemperatureScale value= TemperatureScale.argumentToTemperatureScale(a1,iX);
		setTemperatureScale(value);
		updateAttributes(iX);
	}
	public void setTemperatureScale(TemperatureScale value) {
		temperatureScale= value;
		camera.setIsCelsius(temperatureScale.isCelsius());
	}
	public void getTemperatureScale0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getTemperatureScale(iX).toTerm());
	}
	public void getTemperatureScale0fs(ChoisePoint iX) {
	}
	public TemperatureScale getTemperatureScale(ChoisePoint iX) {
		if (temperatureScale != null) {
			return temperatureScale;
		} else {
			Term value= getBuiltInSlot_E_temperature_scale();
			return TemperatureScale.argumentToTemperatureScale(value,iX);
		}
	}
	//
	// get/set use_recorded_calibration_commands
	//
	public void setUseRecordedCalibrationCommands1s(ChoisePoint iX, Term a1) {
		setUseRecordedCalibrationCommands(YesNoConverters.argument2YesNo(a1,iX));
	}
	public void setUseRecordedCalibrationCommands(YesNo value) {
		useRecordedCalibrationCommands= value;
	}
	public void getUseRecordedCalibrationCommands0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getUseRecordedCalibrationCommands(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getUseRecordedCalibrationCommands0fs(ChoisePoint iX) {
	}
	public YesNo getUseRecordedCalibrationCommands(ChoisePoint iX) {
		if (useRecordedCalibrationCommands != null) {
			return useRecordedCalibrationCommands;
		} else {
			Term value= getBuiltInSlot_E_use_recorded_calibration_commands();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set eliminate_anomalous_pixels
	//
	public void setEliminateAnomalousPixels1s(ChoisePoint iX, Term a1) {
		setEliminateAnomalousPixels(YesNoConverters.argument2YesNo(a1,iX));
		updateAttributes(iX);
	}
	public void setEliminateAnomalousPixels(YesNo value) {
		eliminateAnomalousPixels= value;
		camera.setEliminateAnomalousPixels(value.toBoolean());
	}
	public void getEliminateAnomalousPixels0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getEliminateAnomalousPixels(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getEliminateAnomalousPixels0fs(ChoisePoint iX) {
	}
	public YesNo getEliminateAnomalousPixels(ChoisePoint iX) {
		if (eliminateAnomalousPixels != null) {
			return eliminateAnomalousPixels;
		} else {
			Term value= getBuiltInSlot_E_eliminate_anomalous_pixels();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set apply_anomalous_voltage_pixel_detector
	//
	public void setApplyAnomalousVoltagePixelDetector1s(ChoisePoint iX, Term a1) {
		setApplyAnomalousVoltagePixelDetector(YesNoConverters.argument2YesNo(a1,iX));
		updateAttributes(iX);
	}
	public void setApplyAnomalousVoltagePixelDetector(YesNo value) {
		applyAnomalousVoltagePixelDetector= value;
		camera.setApplyVoltageBasedAnomalousPixelDetector(value.toBoolean());
	}
	public void getApplyAnomalousVoltagePixelDetector0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getApplyAnomalousVoltagePixelDetector(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getApplyAnomalousVoltagePixelDetector0fs(ChoisePoint iX) {
	}
	public YesNo getApplyAnomalousVoltagePixelDetector(ChoisePoint iX) {
		if (applyAnomalousVoltagePixelDetector != null) {
			return applyAnomalousVoltagePixelDetector;
		} else {
			Term value= getBuiltInSlot_E_apply_anomalous_voltage_pixel_detector();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set anomalous_voltage_threshold
	//
	public void setAnomalousVoltageThreshold1s(ChoisePoint iX, Term a1) {
		setAnomalousVoltageThreshold(NumericalValueConverters.argumentToNumericalValue(a1,iX));
		updateAttributes(iX);
	}
	public void setAnomalousVoltageThreshold(NumericalValue value) {
		anomalousVoltageThreshold= value;
		camera.setVoltageBasedAnomalousPixelDetectorThreshold(NumericalValueConverters.toDouble(value));
	}
	public void getAnomalousVoltageThreshold0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(NumericalValueConverters.toTerm(getAnomalousVoltageThreshold(iX)));
	}
	public void getAnomalousVoltageThreshold0fs(ChoisePoint iX) {
	}
	public NumericalValue getAnomalousVoltageThreshold(ChoisePoint iX) {
		if (anomalousVoltageThreshold != null) {
			return anomalousVoltageThreshold;
		} else {
			Term value= getBuiltInSlot_E_anomalous_voltage_threshold();
			return NumericalValueConverters.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set apply_anomalous_temperature_pixel_detector
	//
	public void setApplyAnomalousTemperaturePixelDetector1s(ChoisePoint iX, Term a1) {
		setApplyAnomalousTemperaturePixelDetector(YesNoConverters.argument2YesNo(a1,iX));
		updateAttributes(iX);
	}
	public void setApplyAnomalousTemperaturePixelDetector(YesNo value) {
		applyAnomalousTemperaturePixelDetector= value;
		camera.setApplyTemperatureBasedAnomalousPixelDetector(value.toBoolean());
	}
	public void getApplyAnomalousTemperaturePixelDetector0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getApplyAnomalousTemperaturePixelDetector(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getApplyAnomalousTemperaturePixelDetector0fs(ChoisePoint iX) {
	}
	public YesNo getApplyAnomalousTemperaturePixelDetector(ChoisePoint iX) {
		if (applyAnomalousTemperaturePixelDetector != null) {
			return applyAnomalousTemperaturePixelDetector;
		} else {
			Term value= getBuiltInSlot_E_apply_anomalous_temperature_pixel_detector();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set anomalous_temperature_threshold
	//
	public void setAnomalousTemperatureThreshold1s(ChoisePoint iX, Term a1) {
		setAnomalousTemperatureThreshold(NumericalValueConverters.argumentToNumericalValue(a1,iX));
		updateAttributes(iX);
	}
	public void setAnomalousTemperatureThreshold(NumericalValue value) {
		anomalousTemperatureThreshold= value;
		camera.setTemperatureBasedAnomalousPixelDetectorThreshold(NumericalValueConverters.toDouble(value));
	}
	public void getAnomalousTemperatureThreshold0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(NumericalValueConverters.toTerm(getAnomalousTemperatureThreshold(iX)));
	}
	public void getAnomalousTemperatureThreshold0fs(ChoisePoint iX) {
	}
	public NumericalValue getAnomalousTemperatureThreshold(ChoisePoint iX) {
		if (anomalousTemperatureThreshold != null) {
			return anomalousTemperatureThreshold;
		} else {
			Term value= getBuiltInSlot_E_anomalous_temperature_threshold();
			return NumericalValueConverters.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set do_not_suspend_USB_data_transfer
	//
	public void setDoNotSuspendUSBDataTransfer1s(ChoisePoint iX, Term a1) {
		setDoNotSuspendUSBDataTransfer(YesNoConverters.argument2YesNo(a1,iX));
	}
	public void setDoNotSuspendUSBDataTransfer(YesNo value) {
		doNotSuspendUSBDataTransfer= value;
		thermalDataAcquisition.setDoNotSuspendUSBDataTransfer(value.toBoolean());
	}
	public void getDoNotSuspendUSBDataTransfer0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getDoNotSuspendUSBDataTransfer(iX);
		result.setNonBacktrackableValue(YesNoConverters.toTerm(value));
	}
	public void getDoNotSuspendUSBDataTransfer0fs(ChoisePoint iX) {
	}
	public YesNo getDoNotSuspendUSBDataTransfer(ChoisePoint iX) {
		if (doNotSuspendUSBDataTransfer != null) {
			return doNotSuspendUSBDataTransfer;
		} else {
			Term value= getBuiltInSlot_E_do_not_suspend_USB_data_transfer();
			return YesNoConverters.argument2YesNo(value,iX);
		}
	}
	//
	// get/set read_time_out
	//
	public void setReadTimeOut1s(ChoisePoint iX, Term a1) {
		setReadTimeOut(GeneralConverters.argumentToStrictInteger(a1,iX));
		updateAttributes(iX);
	}
	public void setReadTimeOut(BigInteger value) {
		readTimeOut= value;
		thermalDataAcquisition.setReadTimeOut(PrologInteger.toInteger(value));
	}
	public void getReadTimeOut0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getReadTimeOut(iX)));
	}
	public void getReadTimeOut0fs(ChoisePoint iX) {
	}
	public BigInteger getReadTimeOut(ChoisePoint iX) {
		if (readTimeOut != null) {
			return readTimeOut;
		} else {
			Term value= getBuiltInSlot_E_read_time_out();
			return GeneralConverters.argumentToStrictInteger(value,iX);
		}
	}
	//
	// get/set write_time_out
	//
	public void setWriteTimeOut1s(ChoisePoint iX, Term a1) {
		setWriteTimeOut(GeneralConverters.argumentToStrictInteger(a1,iX));
		updateAttributes(iX);
	}
	public void setWriteTimeOut(BigInteger value) {
		writeTimeOut= value;
		thermalDataAcquisition.setWriteTimeOut(PrologInteger.toInteger(value));
	}
	public void getWriteTimeOut0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getWriteTimeOut(iX)));
	}
	public void getWriteTimeOut0fs(ChoisePoint iX) {
	}
	public BigInteger getWriteTimeOut(ChoisePoint iX) {
		if (writeTimeOut != null) {
			return writeTimeOut;
		} else {
			Term value= getBuiltInSlot_E_write_time_out();
			return GeneralConverters.argumentToStrictInteger(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setOutputDebugInformation(BigInteger value) {
		super.setOutputDebugInformation(value);
		thermalDataAcquisition.setOutputDebugInformation(PrologInteger.toInteger(value));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void releaseSystemResources() {
		camera.disconnectDevice();
		camera.closeUSB();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateAttributes(ChoisePoint iX) {
		boolean currentDoNotControlTemperatureRange= getUseRecordedTemperatureRangeCommands(iX).toBoolean();
		boolean currentAveragingMode= getAveragingMode(iX).toBoolean();
		ThermalDataFrameColorfulAttributes attributes= new ThermalDataFrameColorfulAttributes(
			counterOfRecentAttributes.incrementAndGet(),
			getEliminateAnomalousPixels(iX).toBoolean(),
			getApplyAnomalousVoltagePixelDetector(iX).toBoolean(),
			NumericalValueConverters.toDouble(getAnomalousVoltageThreshold(iX)),
			getApplyAnomalousTemperaturePixelDetector(iX).toBoolean(),
			NumericalValueConverters.toDouble(getAnomalousTemperatureThreshold(iX)),
			camera.getNumberOfDeadPixels(),
			camera.getNumberOfVoltageAnomalousPixels(),
			camera.getNumberOfTemperatureAnomalousPixels(),
			getTemperatureScale(iX).isCelsius(),
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
			currentAveragingMode,
			getZoomImage(iX).toBoolean(),
			getZoomingCoefficient(iX),
			PrologInteger.toInteger(getReadTimeOut(iX)),
			PrologInteger.toInteger(getWriteTimeOut(iX)));
		actingDoNotControlTemperatureRange.set(currentDoNotControlTemperatureRange);
		actingAveragingMode.set(currentAveragingMode);
		recentAttributes.set(attributes);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
			counterOfAcquiredFrames.set(-1);
			// numberOfRecentAcceptedFrame.set(-1);
			numberOfRepeatedFrame= -1;
		}
	}
	//
	protected void resetFrameRate() {
		committedFrameNumber= -1;
		committedFrameTime= -1;
		firstCommittedFrameNumber= -1;
		firstCommittedFrameTime= -1;
		validFrameNumber= 0;
		validFrameTime= -1;
		firstValidFrameNumber= -1;
		firstValidFrameTime= -1;
		corruptedFrameNumber= 0;
		corruptedFrameTime= -1;
		firstCorruptedFrameNumber= -1;
		firstCorruptedFrameTime= -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getDeviceList0ff(ChoisePoint iX, PrologVariable result) {
		String[] identifiers= camera.findDevices();
		result.setNonBacktrackableValue(GeneralConverters.stringArrayToList(identifiers));
	}
	public void getDeviceList0fs(ChoisePoint iX) {
	}
	//
	public void connect0s(ChoisePoint iX) throws Backtracking {
		DeviceIdentifier identifier= getDefaultIdentifier(iX);
		ActionPeriod period= getConnectionAttemptPeriod(iX);
		int currentMaximalErrorsQuantity= getMaximalErrorsQuantity(iX);
		if (!thermalDataAcquisition.connectDevice(identifier,period.toMillisecondsOrDefault(defaultDeviceConnectionAttemptPeriod),currentMaximalErrorsQuantity)) {
			throw Backtracking.instance;
		}
	}
	public void connect1s(ChoisePoint iX, Term a1) throws Backtracking {
		DeviceIdentifier identifier= DeviceIdentifierConverters.argumentToDeviceIdentifier(a1,iX);
		ActionPeriod period= getConnectionAttemptPeriod(iX);
		int currentMaximalErrorsQuantity= getMaximalErrorsQuantity(iX);
		if (!thermalDataAcquisition.connectDevice(identifier,period.toMillisecondsOrDefault(defaultDeviceConnectionAttemptPeriod),currentMaximalErrorsQuantity)) {
			throw Backtracking.instance;
		}
	}
	//
	public void getActualIdentifier0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologString(thermalDataAcquisition.getCurrentDeviceIdentifier()));
	}
	public void getActualIdentifier0fs(ChoisePoint iX) {
	}
	//
	public void disconnect0s(ChoisePoint iX) {
		thermalDataAcquisition.stopDataTransfer();
		frameRecordingTask.close();
		frameReadingTask.closeReading();
		actingDataAcquisitionBufferOperatingMode.set(null);
		thermalDataAcquisition.disconnectDevice();
	}
	//
	public void isConnected0s(ChoisePoint iX) throws Backtracking {
		if (!thermalDataAcquisition.isConnected()) {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void activateDataAcquisition(ChoisePoint iX) {
		DeviceIdentifier identifier= getDefaultIdentifier(iX);
		ActionPeriod period= getConnectionAttemptPeriod(iX);
		int currentMaximalErrorsQuantity= getMaximalErrorsQuantity(iX);
		int currentReadTimeOut= PrologInteger.toInteger(getReadTimeOut(iX));
		int currentWriteTimeOut= PrologInteger.toInteger(getWriteTimeOut(iX));
		int currentOutputDebugInformation= PrologInteger.toInteger(getOutputDebugInformation(iX));
		thermalDataAcquisition.activateDataTransfer(
			identifier,
			period.toMillisecondsOrDefault(defaultDeviceConnectionAttemptPeriod),
			currentMaximalErrorsQuantity,
			currentReadTimeOut,
			currentWriteTimeOut,
			currentOutputDebugInformation);
	}
	//
	protected void suspendRecording(ChoisePoint iX) {
		thermalDataAcquisition.suspendDataTransfer();
		super.suspendRecording(iX);
	}
	protected void suspendListening(ChoisePoint iX) {
		thermalDataAcquisition.suspendDataTransfer();
		super.suspendListening(iX);
	}
	//
	protected void stopRecording(ChoisePoint iX) {
		thermalDataAcquisition.stopDataTransfer();
		super.stopRecording(iX);
	}
	protected void stopListening(ChoisePoint iX) {
		thermalDataAcquisition.stopDataTransfer();
		super.stopListening(iX);
	}
	//
	protected boolean dataAcquisitionIsActive() {
		return thermalDataAcquisition.isNotSuspended();
	}
	//
	protected boolean dataAcquisitionIsSuspended() {
		return thermalDataAcquisition.isSuspended();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void calibrate0s(ChoisePoint iX) {
		camera.calibrateDevice();
	}
	//
	public void completeCalibration() {
		completeCalibration(camera.collectFlashData());
	}
	public void completeCalibration(I3CameraFlashAttributesInterface cameraFlashAttributes) {
		frameRecordingTask.store(new I3CameraFlashFrame(
			counterOfAcquiredFrames.incrementAndGet(),
			System.currentTimeMillis(),
			cameraFlashAttributes,
			recentAttributes.get()));
		reportCalibrationCompletion();
	}
	//
	public void reportCalibrationCompletion() {
		long domainSignature= entry_s_CompleteCalibration_0();
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,noArguments,true);
		transmitAsyncCall(call,null);
	}
	//
	public void completeCalibration0s(ChoisePoint iX) {
	}
	//
	public void annulCalibration0s(ChoisePoint iX) {
		camera.annulDeviceCalibration();
	}
	//
	public void getNumberOfDeadPixels0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(camera.getNumberOfDeadPixels()));
	}
	public void getNumberOfDeadPixels0fs(ChoisePoint iX) {
	}
	//
	public void getNumberOfAnomalousVoltagePixels0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(camera.getNumberOfVoltageAnomalousPixels()));
	}
	public void getNumberOfAnomalousVoltagePixels0fs(ChoisePoint iX) {
	}
	//
	public void getNumberOfAnomalousTemperaturePixels0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(camera.getNumberOfTemperatureAnomalousPixels()));
	}
	public void getNumberOfAnomalousTemperaturePixels0fs(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void saveSensorAttributes1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		DataOutputStream dataStream= fileName.getDataOutputStream();
		try {
			try {
				camera.saveSensorAttributes(dataStream);
			} finally {
				dataStream.close();
			}
		} catch (IOException e) {
			// e.printStackTrace();
			throw new FileInputOutputError(fileName.toString(),e);
		}
	}
	//
	public void loadSensorAttributes1s(ChoisePoint iX, Term a1) {
		ExtendedFileName fileName= retrieveRealLocalFileName(a1,iX);
		int timeOut= getMaximalWaitingTimeInMilliseconds(iX);
		try {
			InputStream inputStream= fileName.getInputStreamOfUniversalResource(timeOut,staticContext);
			BufferedInputStream bufferedStream= new BufferedInputStream(inputStream);
			DataInputStream dataStream= new DataInputStream(bufferedStream);
			try {
				try {
					camera.loadSensorAttributes(dataStream);
				} finally {
					dataStream.close();
				}
			} catch (IOException e) {
				// e.printStackTrace();
				throw new FileInputOutputError(fileName.toString(),e);
			}
		} catch (CannotRetrieveContent e) {
			throw new FileInputOutputError(fileName.toString(),e);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void commit() {
		synchronized (numberOfRecentReceivedFrame) {
			super.commit();
			committedFrame= recentFrame;
			if (!recentFrameIsRepeated) {
				// committedFrameNumber= numberOfRecentAcceptedFrame.get();
				committedFrameNumber= numberOfRecentReceivedFrame.get();
			} else {
				committedFrameNumber= numberOfRepeatedFrame;
			};
			updateCommittedFrameTime();
		}
	}
	//
	protected void updateCommittedFrameTime() {
		if (committedFrame != null) {
			committedFrameTime= committedFrame.getTime();
		} else {
			committedFrameTime= -1;
		};
		if (firstCommittedFrameTime < 0) {
			firstCommittedFrameNumber= committedFrameNumber;
			firstCommittedFrameTime= committedFrameTime;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentFrameNumber1s(ChoisePoint iX, PrologVariable a1) {
		long frameNumber;
		synchronized (numberOfRecentReceivedFrame) {
			frameNumber= committedFrameNumber;
		};
		a1.setBacktrackableValue(new PrologInteger(frameNumber),iX);
	}
	public void getRecentFrameNumber3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) {
		long committedFN;
		long validFN;
		long corruptedFN;
		synchronized (numberOfRecentReceivedFrame) {
			committedFN= committedFrameNumber;
			validFN= validFrameNumber;
			corruptedFN= corruptedFrameNumber;
		};
		a1.setBacktrackableValue(new PrologInteger(committedFN),iX);
		a2.setBacktrackableValue(new PrologInteger(validFN),iX);
		a3.setBacktrackableValue(new PrologInteger(corruptedFN),iX);
	}
	//
	public void getRecentFrameTime1s(ChoisePoint iX, PrologVariable a1) {
		long frameTime;
		synchronized (numberOfRecentReceivedFrame) {
			frameTime= committedFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(frameTime),iX);
	}
	public void getRecentFrameTime3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) {
		long committedFT;
		long validFT;
		long corruptedFT;
		synchronized (numberOfRecentReceivedFrame) {
			committedFT= committedFrameTime;
			validFT= validFrameTime;
			corruptedFT= corruptedFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(committedFT),iX);
		a2.setBacktrackableValue(new PrologInteger(validFT),iX);
		a3.setBacktrackableValue(new PrologInteger(corruptedFT),iX);
	}
	//
	public void getRecentFrameRelativeTime1s(ChoisePoint iX, PrologVariable a1) {
		long deltaTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaTime= committedFrameTime - firstCommittedFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(deltaTime),iX);
	}
	public void getRecentFrameRelativeTime3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) {
		long deltaCommittedFrameTime;
		long deltaValidFrameTime;
		long deltaCorruptedFrameTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaCommittedFrameTime= committedFrameTime - firstCommittedFrameTime;
			deltaValidFrameTime= validFrameTime - firstValidFrameTime;
			deltaCorruptedFrameTime= corruptedFrameTime - firstCorruptedFrameTime;
		};
		a1.setBacktrackableValue(new PrologInteger(deltaCommittedFrameTime),iX);
		a2.setBacktrackableValue(new PrologInteger(deltaValidFrameTime),iX);
		a3.setBacktrackableValue(new PrologInteger(deltaCorruptedFrameTime),iX);
	}
	//
	public void getRecentFrameRate1s(ChoisePoint iX, PrologVariable a1) {
		long deltaN;
		long deltaTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaN= committedFrameNumber - firstCommittedFrameNumber;
			deltaTime= committedFrameTime - firstCommittedFrameTime;
		};
		double rate= computeFrameRate(deltaN,deltaTime);
		a1.setBacktrackableValue(new PrologReal(rate),iX);
	}
	public void getRecentFrameRate3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) {
		long deltaCommittedFrameNumber;
		long deltaValidFrameNumber;
		long deltaCorruptedFrameNumber;
		long deltaCommittedFrameTime;
		long deltaValidFrameTime;
		long deltaCorruptedFrameTime;
		synchronized (numberOfRecentReceivedFrame) {
			deltaCommittedFrameNumber= committedFrameNumber - firstCommittedFrameNumber;
			deltaValidFrameNumber= validFrameNumber - firstValidFrameNumber;
			deltaCorruptedFrameNumber= corruptedFrameNumber - firstCorruptedFrameNumber;
			deltaCommittedFrameTime= committedFrameTime - firstCommittedFrameTime;
			deltaValidFrameTime= validFrameTime - firstValidFrameTime;
			deltaCorruptedFrameTime= corruptedFrameTime - firstCorruptedFrameTime;
		};
		double committedFrameRate= computeFrameRate(deltaCommittedFrameNumber,deltaCommittedFrameTime);
		double validFrameRate= computeFrameRate(deltaValidFrameNumber,deltaValidFrameTime);
		double corruptedFrameRate= computeFrameRate(deltaCorruptedFrameNumber,deltaCorruptedFrameTime);
		a1.setBacktrackableValue(new PrologReal(committedFrameRate),iX);
		a2.setBacktrackableValue(new PrologReal(validFrameRate),iX);
		a3.setBacktrackableValue(new PrologReal(corruptedFrameRate),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentImage1s(ChoisePoint iX, Term value) {
		updateAttributesIfNecessary(iX);
		double[] thermalDataBuffer;
		DataFrameBaseAttributesInterface attributes;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				attributes= committedFrame.getBaseAttributes();
				thermalDataBuffer= getThermalDataBuffer(attributes);
/*
double min= thermalDataBuffer[0];
double max= thermalDataBuffer[0];
for (int k=0; k < thermalDataBuffer.length; k++) {
	if (min > thermalDataBuffer[k]) {
		min= thermalDataBuffer[k];
	};
	if (max < thermalDataBuffer[k]) {
		max= thermalDataBuffer[k];
	}
}
*/
			} else {
				throw new I3FrameIsNotCommitted();
			}
		};
		YesNo doNotControlColorMaps= getUseRecordedColorMapCommands(iX);
		int[][] mainColorMap= prepareMainColorMap(doNotControlColorMaps.toBoolean(),attributes);
		int[][] auxiliaryColorMap= prepareAuxiliaryColorMap(doNotControlColorMaps.toBoolean(),attributes);
		boolean isAutorangingMode;
		boolean isDoubleColorMapMode;
		double lowerDataBound;
		double upperDataBound;
		double lowerDataQuantile1;
		double upperDataQuantile1;
		double lowerDataQuantile2;
		double upperDataQuantile2;
		if (actingDoNotControlTemperatureRange.get()) {
			isAutorangingMode= attributes.isAutorangingMode();
			isDoubleColorMapMode= attributes.isDoubleColorMapMode();
			lowerDataBound= attributes.getLowerDataBound();
			upperDataBound= attributes.getUpperDataBound();
			lowerDataQuantile1= attributes.getLowerDataQuantile1();
			upperDataQuantile1= attributes.getUpperDataQuantile1();
			lowerDataQuantile2= attributes.getLowerDataQuantile2();
			upperDataQuantile2= attributes.getUpperDataQuantile2();
		} else {
			isAutorangingMode= getAutorangingMode(iX).toBoolean();
			isDoubleColorMapMode= getDoubleColorMapMode(iX).toBoolean();
			lowerDataBound= NumericalValueConverters.toDouble(getLowerTemperatureBound(iX));
			upperDataBound= NumericalValueConverters.toDouble(getUpperTemperatureBound(iX));
			lowerDataQuantile1= NumericalValueConverters.toDouble(getLowerMainTemperatureQuantile(iX));
			upperDataQuantile1= NumericalValueConverters.toDouble(getUpperMainTemperatureQuantile(iX));
			lowerDataQuantile2= NumericalValueConverters.toDouble(getLowerAuxiliaryTemperatureQuantile(iX));
			upperDataQuantile2= NumericalValueConverters.toDouble(getUpperAuxiliaryTemperatureQuantile(iX));
		};
		YesNo doNotControlZooming= getUseRecordedZoomingCommands(iX);
		boolean zoomIt;
		double zCoefficient;
		if (doNotControlZooming.toBoolean()) {
			zoomIt= attributes.isZoomingMode();
			zCoefficient= attributes.getZoomingCoefficient();
		} else {
			zoomIt= getZoomImage(iX).toBoolean();
			zCoefficient= NumericalValueConverters.toDouble(getZoomingCoefficient(iX));
		};
		AttachedImage attachedImage= DataFrameTools.temperaturesToImage(
			thermalDataBuffer,
			camera.getWindowingWidth(),
			camera.getWindowingHeight(),
			mainColorMap,
			auxiliaryColorMap,
			isAutorangingMode,
			isDoubleColorMapMode,
			lowerDataBound,
			upperDataBound,
			lowerDataQuantile1,
			upperDataQuantile1,
			lowerDataQuantile2,
			upperDataQuantile2,
			zoomIt,
			zCoefficient);
		java.awt.image.BufferedImage nativeImage= attachedImage.getImage();
		modifyImage(value,nativeImage,iX);
	}
	//
	protected double[] getThermalDataBuffer(DataFrameBaseAttributesInterface attributes) {
		boolean isAveragingMode;
		if (actingDoNotControlTemperatureRange.get()) {
			isAveragingMode= attributes.isAverageMode();
		} else {
			isAveragingMode= actingAveragingMode.get();
		};
		if (isAveragingMode && committedCumulativeTemperatures!=null) {
			return committedCumulativeTemperatures;
		} else {
			return ((DoubleDataFrameInterface)committedFrame).getDoubleData();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getTemperature2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		int x= GeneralConverters.argumentToSmallInteger(a1,iX);
		int y= GeneralConverters.argumentToSmallInteger(a2,iX);
		double[] thermalDataBuffer;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				DataFrameBaseAttributesInterface attributes= committedFrame.getBaseAttributes();
				thermalDataBuffer= getThermalDataBuffer(attributes);
			} else {
				throw new I3FrameIsNotCommitted();
			}
		};
		int columns= camera.getWindowingWidth();
		int pos0= y * columns + x;
		double targetTemperature= thermalDataBuffer[pos0];
		result.setNonBacktrackableValue(new PrologReal(targetTemperature));
	}
	public void getTemperature2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void getMinimalTemperature0ff(ChoisePoint iX, PrologVariable result) {
		double[] thermalDataBuffer;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				DataFrameBaseAttributesInterface attributes= committedFrame.getBaseAttributes();
				thermalDataBuffer= getThermalDataBuffer(attributes);
			} else {
				throw new I3FrameIsNotCommitted();
			}
		};
		double minimum= thermalDataBuffer[0];
		for (int k=1; k < thermalDataBuffer.length; k++) {
			double value= thermalDataBuffer[k];
			if (minimum > value) {
				minimum= value;
			}
		};
		result.setNonBacktrackableValue(new PrologReal(minimum));
	}
	public void getMinimalTemperature0fs(ChoisePoint iX) {
	}
	//
	public void getMaximalTemperature0ff(ChoisePoint iX, PrologVariable result) {
		double[] thermalDataBuffer;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				DataFrameBaseAttributesInterface attributes= committedFrame.getBaseAttributes();
				thermalDataBuffer= getThermalDataBuffer(attributes);
			} else {
				throw new I3FrameIsNotCommitted();
			}
		};
		double maximum= thermalDataBuffer[0];
		for (int k=1; k < thermalDataBuffer.length; k++) {
			double value= thermalDataBuffer[k];
			if (maximum < value) {
				maximum= value;
			}
		};
		result.setNonBacktrackableValue(new PrologReal(maximum));
	}
	public void getMaximalTemperature0fs(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getImageSizeInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		int columns= camera.getWindowingWidth();
		int rows= camera.getWindowingHeight();
		a1.setBacktrackableValue(new PrologInteger(columns),iX);
		a2.setBacktrackableValue(new PrologInteger(rows),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setByteThermalData(byte[] buffer, long time) {
		byte[] byteThermalDataBuffer= camera.createBuffer();
		for (int k=0; k < byteThermalDataBuffer.length; k++) {
			byteThermalDataBuffer[k]= buffer[k];
		};
		camera.setRecvData(byteThermalDataBuffer);
		camera.compute_FPA_Temperature();
		camera.computeTemperatureMap();
		camera.computeCorrectedTargetTemperatures();
		double[] targetTemperatures= camera.getTargetTemperatures();
		dataAcquisitionError.set(null);
		DoubleDataFrame frame= new DoubleDataFrame(
			counterOfAcquiredFrames.incrementAndGet(),
			time,
			targetTemperatures,
			recentAttributes.get());
		sendDataFrame(frame);
	}
	//
	// public void setAudioData(byte[] buffer, long time) {
	// }
	//
	public void reportMissedFrame(long currentTime) {
		synchronized (numberOfRecentReceivedFrame) {
			corruptedFrameNumber++;
			corruptedFrameTime= currentTime;
			if (firstCorruptedFrameNumber < 0) {
				firstCorruptedFrameNumber= corruptedFrameNumber;
				firstCorruptedFrameTime= corruptedFrameTime;
			}
		};
		long domainSignature= entry_s_MissedFrame_1_i();
		Term[] arguments= new Term[1];
		arguments[0]= new PrologInteger(currentTime);
		// 2018.10.30: AsyncCall call= new AsyncCall(domainSignature,this,true,false,arguments,true);
		AsyncCall call= new AsyncCall(domainSignature,this,true,true,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	public void missedFrame1s(ChoisePoint iX, Term value) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected long updateRecentFrame(DataFrameInterface frame) {
		if (frame.isLightweightFrame()) {
			return -1;
		};
		synchronized (numberOfRecentReceivedFrame) {
			long currentFrameNumber= -1;
			if (frame instanceof DoubleDataFrameInterface) {
				recentFrame= frame;
				committedFrameWasAssignedDirectly.set(false);
				// numberOfRecentAcceptedFrame.set(recentFrame.getSerialNumber());
				// long currentNumber=
				currentFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
				// numberOfRecentAcceptedFrame.set(currentNumber);
				DoubleDataFrameInterface doubleDataFrame= (DoubleDataFrameInterface)frame;
				updateHistory(doubleDataFrame);
				updateCumulativeTemperatures(doubleDataFrame);
				validFrameNumber++;
				validFrameTime= frame.getTime();
				if (firstValidFrameNumber < 0) {
					firstValidFrameNumber= validFrameNumber;
					firstValidFrameTime= validFrameTime;
				}
			} else {
				return currentFrameNumber;
			};
			// long currentFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			recentFrameIsRepeated= false;
			numberOfRepeatedFrame= -1;
			numberOfRecentReceivedFrame.notifyAll();
			return currentFrameNumber;
		}
	}
	//
	protected void updateHistory(DoubleDataFrameInterface recentDataFrame) {
		if (recentDataFrame==null) {
			return;
		};
		// updateHistory(new EnumeratedDataFrame(recentDataFrame,numberOfRecentAcceptedFrame.get()));
		updateHistory(new EnumeratedDataFrame(recentDataFrame,numberOfRecentReceivedFrame.get()));
	}
	//
	protected void updateCumulativeTemperatures(DoubleDataFrameInterface frame) {
		boolean isAveragingMode;
		if (actingDoNotControlTemperatureRange.get()) {
			DataFrameBaseAttributesInterface attributes= frame.getBaseAttributes();
			isAveragingMode= attributes.isAverageMode();
		} else {
			isAveragingMode= actingAveragingMode.get();
		};
		if (isAveragingMode) {
			numberOfAveragedFrames++;
			double[] targetTemperatures= frame.getDoubleData();
			if (cumulativeTemperatures==null) {
				cumulativeTemperatures= Arrays.copyOf(targetTemperatures,targetTemperatures.length);
			} else {
				for (int k=0; k < cumulativeTemperatures.length; k++) {
					cumulativeTemperatures[k]+= targetTemperatures[k];
				}
			}
		} else {
			resetCumulativeTemperatures();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void acceptRequestedFrame(EnumeratedFrame enumeratedFrame) {
		EnumeratedDataFrame selectedFrame= (EnumeratedDataFrame)enumeratedFrame;
		recentFrame= selectedFrame.getFrame();
		committedFrameWasAssignedDirectly.set(false);
		recentFrameIsRepeated= true;
		numberOfRepeatedFrame= selectedFrame.getNumberOfFrame();
	}
	//
	protected void acceptRetrievedFrame(EnumeratedFrame enumeratedFrame) {
		EnumeratedDataFrame selectedFrame= (EnumeratedDataFrame)enumeratedFrame;
		DoubleDataFrameInterface thermalFrame= (DoubleDataFrameInterface)selectedFrame.getFrame();
		committedFrame= thermalFrame;
		committedFrameWasAssignedDirectly.set(true);
		committedFrameNumber= selectedFrame.getNumberOfFrame();
		updateCommittedFrameTime();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void extractFrame(String key, CompoundFrameInterface container) {
		if (committedFrame != null) {
			EnumeratedDataFrame enumeratedFrame= new EnumeratedDataFrame(
				committedFrame,
				committedFrameNumber);
			container.insertComponent(key,enumeratedFrame);
		} else {
			throw new I3FrameIsNotCommitted();
		}
	}
	//
	public void assignFrame(String key, CompoundFrameInterface container, ChoisePoint iX) {
		EnumeratedDataFrame enumeratedFrame= (EnumeratedDataFrame)container.getComponent(key);
		synchronized (numberOfRecentReceivedFrame) {
			committedFrame= enumeratedFrame.getFrame();
			committedFrameWasAssignedDirectly.set(true);
			// committedFrameNumber= enumeratedFrame.getNumberOfFrame();
			// committedFrameNumber= numberOfRecentAcceptedFrame.incrementAndGet();
			committedFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			DoubleDataFrameInterface doubleDataFrame= (DoubleDataFrameInterface)committedFrame;
			updateCumulativeTemperatures(doubleDataFrame);
			updateCommittedFrameTime();
		}
	}
}
