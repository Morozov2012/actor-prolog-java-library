// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.checker.signals.*;
import morozov.system.i3v1.*;
import morozov.system.i3v1.converters.*;
import morozov.system.i3v1.converters.interfaces.*;
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
import morozov.system.frames.converters.interfaces.*;
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
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Arrays;
import java.math.BigInteger;

public abstract class TEV1 extends ThermalVideoBuffer implements I3DataConsumerInterface, DataFrameConsumerInterface, DataFrameProviderInterface {
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
	protected BigInteger outputDebugInformation;
	//
	protected I3Camera camera= new I3Camera();
	protected I3DataAcquisition thermalDataAcquisition= new I3DataAcquisition(camera);
	protected AtomicLong counterOfAcquiredFrames= new AtomicLong(-1);
	protected AtomicLong numberOfRecentFrame= new AtomicLong(-1);
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
	protected LinkedList<EnumeratedFrame> history= new LinkedList<>();
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
	abstract public Term getBuiltInSlot_E_output_debug_information();
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
		setUseRecordedCalibrationCommands(YesNo.argument2YesNo(a1,iX));
	}
	public void setUseRecordedCalibrationCommands(YesNo value) {
		useRecordedCalibrationCommands= value;
	}
	public void getUseRecordedCalibrationCommands0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getUseRecordedCalibrationCommands(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getUseRecordedCalibrationCommands0fs(ChoisePoint iX) {
	}
	public YesNo getUseRecordedCalibrationCommands(ChoisePoint iX) {
		if (useRecordedCalibrationCommands != null) {
			return useRecordedCalibrationCommands;
		} else {
			Term value= getBuiltInSlot_E_use_recorded_calibration_commands();
			return YesNo.argument2YesNo(value,iX);
		}
	}
	//
	// get/set eliminate_anomalous_pixels
	//
	public void setEliminateAnomalousPixels1s(ChoisePoint iX, Term a1) {
		setEliminateAnomalousPixels(YesNo.argument2YesNo(a1,iX));
		updateAttributes(iX);
	}
	public void setEliminateAnomalousPixels(YesNo value) {
		eliminateAnomalousPixels= value;
		camera.setEliminateAnomalousPixels(value.toBoolean());
	}
	public void getEliminateAnomalousPixels0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getEliminateAnomalousPixels(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getEliminateAnomalousPixels0fs(ChoisePoint iX) {
	}
	public YesNo getEliminateAnomalousPixels(ChoisePoint iX) {
		if (eliminateAnomalousPixels != null) {
			return eliminateAnomalousPixels;
		} else {
			Term value= getBuiltInSlot_E_eliminate_anomalous_pixels();
			return YesNo.argument2YesNo(value,iX);
		}
	}
	//
	// get/set apply_anomalous_voltage_pixel_detector
	//
	public void setApplyAnomalousVoltagePixelDetector1s(ChoisePoint iX, Term a1) {
		setApplyAnomalousVoltagePixelDetector(YesNo.argument2YesNo(a1,iX));
		updateAttributes(iX);
	}
	public void setApplyAnomalousVoltagePixelDetector(YesNo value) {
		applyAnomalousVoltagePixelDetector= value;
		camera.setApplyVoltageBasedAnomalousPixelDetector(value.toBoolean());
	}
	public void getApplyAnomalousVoltagePixelDetector0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getApplyAnomalousVoltagePixelDetector(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getApplyAnomalousVoltagePixelDetector0fs(ChoisePoint iX) {
	}
	public YesNo getApplyAnomalousVoltagePixelDetector(ChoisePoint iX) {
		if (applyAnomalousVoltagePixelDetector != null) {
			return applyAnomalousVoltagePixelDetector;
		} else {
			Term value= getBuiltInSlot_E_apply_anomalous_voltage_pixel_detector();
			return YesNo.argument2YesNo(value,iX);
		}
	}
	//
	// get/set anomalous_voltage_threshold
	//
	public void setAnomalousVoltageThreshold1s(ChoisePoint iX, Term a1) {
		setAnomalousVoltageThreshold(NumericalValue.argumentToNumericalValue(a1,iX));
		updateAttributes(iX);
	}
	public void setAnomalousVoltageThreshold(NumericalValue value) {
		anomalousVoltageThreshold= value;
		camera.setVoltageBasedAnomalousPixelDetectorThreshold(value.toDouble());
	}
	public void getAnomalousVoltageThreshold0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getAnomalousVoltageThreshold(iX).toTerm());
	}
	public void getAnomalousVoltageThreshold0fs(ChoisePoint iX) {
	}
	public NumericalValue getAnomalousVoltageThreshold(ChoisePoint iX) {
		if (anomalousVoltageThreshold != null) {
			return anomalousVoltageThreshold;
		} else {
			Term value= getBuiltInSlot_E_anomalous_voltage_threshold();
			return NumericalValue.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set apply_anomalous_temperature_pixel_detector
	//
	public void setApplyAnomalousTemperaturePixelDetector1s(ChoisePoint iX, Term a1) {
		setApplyAnomalousTemperaturePixelDetector(YesNo.argument2YesNo(a1,iX));
		updateAttributes(iX);
	}
	public void setApplyAnomalousTemperaturePixelDetector(YesNo value) {
		applyAnomalousTemperaturePixelDetector= value;
		camera.setApplyTemperatureBasedAnomalousPixelDetector(value.toBoolean());
	}
	public void getApplyAnomalousTemperaturePixelDetector0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getApplyAnomalousTemperaturePixelDetector(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getApplyAnomalousTemperaturePixelDetector0fs(ChoisePoint iX) {
	}
	public YesNo getApplyAnomalousTemperaturePixelDetector(ChoisePoint iX) {
		if (applyAnomalousTemperaturePixelDetector != null) {
			return applyAnomalousTemperaturePixelDetector;
		} else {
			Term value= getBuiltInSlot_E_apply_anomalous_temperature_pixel_detector();
			return YesNo.argument2YesNo(value,iX);
		}
	}
	//
	// get/set anomalous_temperature_threshold
	//
	public void setAnomalousTemperatureThreshold1s(ChoisePoint iX, Term a1) {
		setAnomalousTemperatureThreshold(NumericalValue.argumentToNumericalValue(a1,iX));
		updateAttributes(iX);
	}
	public void setAnomalousTemperatureThreshold(NumericalValue value) {
		anomalousTemperatureThreshold= value;
		camera.setTemperatureBasedAnomalousPixelDetectorThreshold(value.toDouble());
	}
	public void getAnomalousTemperatureThreshold0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getAnomalousTemperatureThreshold(iX).toTerm());
	}
	public void getAnomalousTemperatureThreshold0fs(ChoisePoint iX) {
	}
	public NumericalValue getAnomalousTemperatureThreshold(ChoisePoint iX) {
		if (anomalousTemperatureThreshold != null) {
			return anomalousTemperatureThreshold;
		} else {
			Term value= getBuiltInSlot_E_anomalous_temperature_threshold();
			return NumericalValue.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set do_not_suspend_USB_data_transfer
	//
	public void setDoNotSuspendUSBDataTransfer1s(ChoisePoint iX, Term a1) {
		setDoNotSuspendUSBDataTransfer(YesNo.argument2YesNo(a1,iX));
	}
	public void setDoNotSuspendUSBDataTransfer(YesNo value) {
		doNotSuspendUSBDataTransfer= value;
		thermalDataAcquisition.setDoNotSuspendUSBDataTransfer(value.toBoolean());
	}
	public void getDoNotSuspendUSBDataTransfer0ff(ChoisePoint iX, PrologVariable result) {
		YesNo value= getDoNotSuspendUSBDataTransfer(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getDoNotSuspendUSBDataTransfer0fs(ChoisePoint iX) {
	}
	public YesNo getDoNotSuspendUSBDataTransfer(ChoisePoint iX) {
		if (doNotSuspendUSBDataTransfer != null) {
			return doNotSuspendUSBDataTransfer;
		} else {
			Term value= getBuiltInSlot_E_do_not_suspend_USB_data_transfer();
			return YesNo.argument2YesNo(value,iX);
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
	// get/set output_debug_information
	//
	public void setOutputDebugInformation1s(ChoisePoint iX, Term a1) {
		setOutputDebugInformation(GeneralConverters.argumentToStrictInteger(a1,iX));
	}
	public void setOutputDebugInformation(BigInteger value) {
		outputDebugInformation= value;
		thermalDataAcquisition.setOutputDebugInformation(PrologInteger.toInteger(value));
	}
	public void getOutputDebugInformation0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(getOutputDebugInformation(iX)));
	}
	public void getOutputDebugInformation0fs(ChoisePoint iX) {
	}
	public BigInteger getOutputDebugInformation(ChoisePoint iX) {
		if (outputDebugInformation != null) {
			return outputDebugInformation;
		} else {
			Term value= getBuiltInSlot_E_output_debug_information();
			return GeneralConverters.argumentToStrictInteger(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void releaseSystemResources() {
		camera.closeDevice();
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
			getAnomalousVoltageThreshold(iX).toDouble(),
			getApplyAnomalousTemperaturePixelDetector(iX).toBoolean(),
			getAnomalousTemperatureThreshold(iX).toDouble(),
			camera.getNumberOfDeadPixels(),
			camera.getNumberOfVoltageAnomalousPixels(),
			camera.getNumberOfTemperatureAnomalousPixels(),
			getTemperatureScale(iX).isCelsius(),
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
	public void getDeviceList0ff(ChoisePoint iX, PrologVariable result) {
		String[] identifiers= camera.findDevices();
		result.setNonBacktrackableValue(GeneralConverters.stringArrayToList(identifiers));
	}
	public void getDeviceList0fs(ChoisePoint iX) {
	}
	//
	public void open0s(ChoisePoint iX) throws Backtracking {
		DeviceIdentifier identifier= getDefaultIdentifier(iX);
		ActionPeriod period= getOpeningAttemptPeriod(iX);
		if (!thermalDataAcquisition.openDevice(identifier,period.toMillisecondsOrDefault(defaultDeviceOpeningAttemptDelay))) {
			throw Backtracking.instance;
		}
	}
	public void open1s(ChoisePoint iX, Term a1) throws Backtracking {
		DeviceIdentifier identifier= DeviceIdentifierConverters.argumentToDeviceIdentifier(a1,iX);
		ActionPeriod period= getOpeningAttemptPeriod(iX);
		if (!thermalDataAcquisition.openDevice(identifier,period.toMillisecondsOrDefault(defaultDeviceOpeningAttemptDelay))) {
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
	public void close0s(ChoisePoint iX) {
		stopDataAcquisition();
		thermalDataAcquisition.closeDevice();
	}
	//
	public void isOpen0s(ChoisePoint iX) throws Backtracking {
		if (!thermalDataAcquisition.isOpen()) {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
			counterOfAcquiredFrames.set(-1);
			numberOfRecentFrame.set(-1);
			numberOfRepeatedFrame= -1;
			// committedCumulativeTemperatures= null;
			// resetFrameRate();
			synchronized (history) {
				history.clear();
			}
			// resetCumulativeTemperatures();
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
	protected void activateDataAcquisition(ChoisePoint iX) {
		DeviceIdentifier identifier= getDefaultIdentifier(iX);
		ActionPeriod period= getOpeningAttemptPeriod(iX);
		int currentReadTimeOut= PrologInteger.toInteger(getReadTimeOut(iX));
		int currentWriteTimeOut= PrologInteger.toInteger(getWriteTimeOut(iX));
		int currentOutputDebugInformation= PrologInteger.toInteger(getOutputDebugInformation(iX));
		thermalDataAcquisition.activateDataTransfer(identifier,period.toMillisecondsOrDefault(defaultDeviceOpeningAttemptDelay),currentReadTimeOut,currentWriteTimeOut,currentOutputDebugInformation);
	}
	//
	protected void readGivenNumberOfTargetFrames(int number) {
		((I3DataReadingTaskInterface)frameReadingTask).readGivenNumberOfFrames(number);
	}
	//
	protected void suspendDataAcquisition() {
		thermalDataAcquisition.suspendDataTransfer();
	}
	//
	protected void stopDataAcquisition() {
		super.stopDataAcquisition();
		thermalDataAcquisition.stopDataTransfer();
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
			committedFrame= recentFrame;
			if (!recentFrameIsRepeated) {
				committedFrameNumber= numberOfRecentFrame.get();
			} else {
				committedFrameNumber= numberOfRepeatedFrame;
			};
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
		double[] thermalDataBuffer;
		DataFrameBaseAttributesInterface attributes;
		synchronized (numberOfRecentReceivedFrame) {
			if (committedFrame != null) {
				attributes= committedFrame.getBaseAttributes();
				thermalDataBuffer= getThermalDataBuffer(attributes);
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
			lowerDataBound= getLowerTemperatureBound(iX).toDouble();
			upperDataBound= getUpperTemperatureBound(iX).toDouble();
			lowerDataQuantile1= getLowerMainTemperatureQuantile(iX).toDouble();
			upperDataQuantile1= getUpperMainTemperatureQuantile(iX).toDouble();
			lowerDataQuantile2= getLowerAuxiliaryTemperatureQuantile(iX).toDouble();
			upperDataQuantile2= getUpperAuxiliaryTemperatureQuantile(iX).toDouble();
		};
		YesNo doNotControlZooming= getUseRecordedZoomingCommands(iX);
		boolean zoomIt;
		double zCoefficient;
		if (doNotControlZooming.toBoolean()) {
			zoomIt= attributes.isZoomingMode();
			zCoefficient= attributes.getZoomingCoefficient();
		} else {
			zoomIt= getZoomImage(iX).toBoolean();
			zCoefficient= getZoomingCoefficient(iX).toDouble();
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
		sendFrame(frame);
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
			if (frame instanceof DoubleDataFrameInterface) {
				recentFrame= frame;
				numberOfRecentFrame.set(recentFrame.getSerialNumber());
				DoubleDataFrameInterface doubleDataFrame= (DoubleDataFrameInterface)frame;
				updateHistory(doubleDataFrame);
				//
				boolean isAveragingMode;
				if (actingDoNotControlTemperatureRange.get()) {
					DataFrameBaseAttributesInterface attributes= frame.getBaseAttributes();
					isAveragingMode= attributes.isAverageMode();
				} else {
					isAveragingMode= actingAveragingMode.get();
				};
				if (isAveragingMode) {
					numberOfAveragedFrames++;
					double[] targetTemperatures= doubleDataFrame.getDoubleData();
					if (cumulativeTemperatures==null) {
						cumulativeTemperatures= Arrays.copyOf(targetTemperatures,targetTemperatures.length);
					} else {
						for (int k=0; k < cumulativeTemperatures.length; k++) {
							cumulativeTemperatures[k]+= targetTemperatures[k];
						}
					}
				} else {
					resetCumulativeTemperatures();
				};
				validFrameNumber++;
				validFrameTime= frame.getTime();
				if (firstValidFrameNumber < 0) {
					firstValidFrameNumber= validFrameNumber;
					firstValidFrameTime= validFrameTime;
				}
			};
			long currentFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
			recentFrameIsRepeated= false;
			numberOfRepeatedFrame= -1;
			return currentFrameNumber;
		}
	}
	//
	protected void updateHistory(DoubleDataFrameInterface recentDataFrame) {
		if (recentDataFrame==null) {
			return;
		};
		synchronized (history) {
			history.addLast(new EnumeratedFrame(
				recentDataFrame,
				numberOfRecentFrame.get()));
			if (history.size() > actingReadBufferSize.get()) {
				history.removeFirst();
			}
		};
		containsNewFrame.set(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void retrieveBufferedFrame1s(ChoisePoint iX, Term a1) {
		int number= GeneralConverters.argumentToSmallInteger(a1,iX);
		int relativeNumber= number - 1;
		if (relativeNumber < 0) {
			relativeNumber= 0;
		};
		synchronized (numberOfRecentReceivedFrame) {
			EnumeratedFrame enumeratedFrame;
			synchronized (history) {
				int bufferSize= actingReadBufferSize.get();
				int historySize= history.size();
				int maximalIndex= bufferSize - 1;
				if (historySize < bufferSize) {
					maximalIndex= historySize - 1;
					relativeNumber= relativeNumber * historySize / bufferSize;
				};
				if (relativeNumber > maximalIndex) {
					relativeNumber= maximalIndex;
				};
				if (relativeNumber < 0 || relativeNumber >= historySize) {
					return;
				};
				enumeratedFrame= history.get(relativeNumber);
			};
			recentFrame= enumeratedFrame.getFrame();;
			recentFrameIsRepeated= true;
			numberOfRepeatedFrame= enumeratedFrame.getNumberOfFrame();
		};
		sendFrameObtained();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void retrieveTimedFrame1s(ChoisePoint iX, Term a1) throws Backtracking {
		TimeInterval timeInterval= TimeInterval.argumentMillisecondsToTimeInterval(a1,iX);
		if (!retrieveTimedFrame(timeInterval.toMillisecondsLong())) {
			throw Backtracking.instance;
		}
	}
	//
	protected boolean retrieveTimedFrame(long targetTime) {
		while (true) {
			VideoBufferOperatingMode currentOperatingMode= actingVideoBufferOperatingMode.get();
			int numberOfFramesToBeRead= 0;
			synchronized (numberOfRecentReceivedFrame) {
				synchronized (history) {
					int bufferSize= actingReadBufferSize.get();
					int historySize= history.size();
					if (historySize <= 1) {
						if (currentOperatingMode==VideoBufferOperatingMode.SPECULATIVE_READING) {
							((I3DataReadingTaskInterface)frameReadingTask).readGivenNumberOfFrames(bufferSize);
						};
						continue;
					};
					EnumeratedFrame firstEnumeratedFrame= history.getFirst();
					EnumeratedFrame lastEnumeratedFrame= history.getLast();
					long minimalTime= firstEnumeratedFrame.getTime();
					long maximalTime= lastEnumeratedFrame.getTime();
					if (targetTime >= minimalTime) {
						if(targetTime <= maximalTime) {
///////////////////////////////////////////////////////////////////////
ListIterator<EnumeratedFrame> iterator= history.listIterator(0);
int relativeNumber= 0;
EnumeratedFrame selectedFrame= firstEnumeratedFrame;
long delay1= targetTime - minimalTime;
if (delay1 < 0) {
	delay1= -delay1;
};
while (iterator.hasNext()) {
	EnumeratedFrame currentFrame= iterator.next();
	long time2= currentFrame.getTime();
	long delay2= targetTime - time2;
	if (delay2 < 0) {
		delay2= -delay2;
	};
	if (time2 >= targetTime) {
		if (delay2 < delay1) {
			selectedFrame= currentFrame;
		};
		break;
	} else {
		selectedFrame= currentFrame;
		delay1= delay2;
	}
};
DoubleDataFrameInterface thermalFrame= (DoubleDataFrameInterface)selectedFrame.getFrame();
committedFrame= thermalFrame;
committedFrameNumber= numberOfRepeatedFrame;
committedFrameTime= committedFrame.getTime();
if (firstCommittedFrameTime < 0) {
	firstCommittedFrameNumber= committedFrameNumber;
	firstCommittedFrameTime= committedFrameTime;
};
if (currentOperatingMode==VideoBufferOperatingMode.SPECULATIVE_READING) {
	((I3DataReadingTaskInterface)frameReadingTask).readGivenNumberOfFrames(numberOfFramesToBeRead);
};
return true;
///////////////////////////////////////////////////////////////////////
						} else { // Read several frames
							if (currentOperatingMode==VideoBufferOperatingMode.SPECULATIVE_READING) {
								((I3DataReadingTaskInterface)frameReadingTask).readFramesUntilGivenTime(targetTime,bufferSize);
							}
						}
					} else { // Suspend reading of the frames
						if (currentOperatingMode==VideoBufferOperatingMode.SPECULATIVE_READING) {
							frameReadingTask.suspendReading();
						};
						return false;
					}
				}
			};
			if (currentOperatingMode != VideoBufferOperatingMode.SPECULATIVE_READING) {
				break;
			}
		};
		return false;
	}
}
