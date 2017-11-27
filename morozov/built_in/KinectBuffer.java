// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.files.*;
import morozov.system.kinect.*;
import morozov.system.kinect.converters.*;
import morozov.system.kinect.converters.errors.*;
import morozov.system.kinect.converters.interfaces.*;
import morozov.system.kinect.errors.*;
import morozov.system.kinect.frames.*;
import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.frames.data.converters.*;
import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.interfaces.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.tools.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public abstract class KinectBuffer extends BufferedImageController implements KinectBufferInterface {
	//
	protected KinectBufferOperatingMode operatingMode;
	protected KinectDisplayingModeInterface displayingMode;
	protected KinectDeviceInterface inputDevice;
	protected KinectPerformanceOptimization dataAcquisitionOptimization;
	protected KinectTextAttributeInterface description;
	protected KinectTextAttributeInterface copyright;
	protected KinectTextAttributeInterface registrationDate;
	protected KinectTextAttributeInterface registrationTime;
	protected ColorMapInterface colorMap;
	protected ColorMapInterface peopleColors;
	protected KinectIntegerAttributeInterface maximalFrameDelay;
	//
	protected Integer writeBufferSize;
	protected Integer readBufferSize;
	protected NumericalValue maximalChronicleLength;
	protected NumericalValue slowMotionCoefficient;
	//
	protected int computedHorizontalCorrection= ExtendedCorrectionTools.getDefaultHorizontalCorrection();
	protected int computedVerticalCorrection= ExtendedCorrectionTools.getDefaultVerticalCorrection();
	//
	protected AtomicReference<KinectBufferOperatingMode> actingKinectBufferOperatingMode= new AtomicReference<>();
	protected AtomicReference<KinectDeviceInterface> actingDevice= new AtomicReference<>();
	protected AtomicBoolean actingExclusiveAccessFlag= new AtomicBoolean(false);
	//
	protected AtomicReference<KinectFrameType> actingFrameType= new AtomicReference<>();
	protected AtomicReference<KinectFrameType[]> actingDataAcquisitionMode= new AtomicReference<>();
	protected AtomicReference<KinectPeopleIndexMode> actingPeopleIndexMode= new AtomicReference<>();
	protected AtomicReference<KinectCircumscriptionMode[]> actingCircumscriptionModes= new AtomicReference<>();
	protected AtomicReference<KinectSkeletonsMode> actingSkeletonsMode= new AtomicReference<>();
	protected AtomicReference<KinectTextAttributeInterface> actingDescription= new AtomicReference<>();
	protected AtomicReference<KinectTextAttributeInterface> actingCopyright= new AtomicReference<>();
	protected AtomicReference<KinectTextAttributeInterface> actingRegistrationDate= new AtomicReference<>();
	protected AtomicReference<KinectTextAttributeInterface> actingRegistrationTime= new AtomicReference<>();
	//
	protected AtomicReference<NumericalValue> actingMaximalChronicleLength= new AtomicReference<>();
	protected AtomicReference<KinectDataReadingError> dataTransferError= new AtomicReference<>();
	//
	protected ArrayList<KinectSkeletonsFrameInterface> rawSkeletonsHistory= new ArrayList<>();
	// protected long beginningRawSkeletonTime= -1;
	protected long recentRawSkeletonTime= -1;
	protected boolean recentRawSkeletonsAreTracked= false;
	//
	protected CommittedSkeletons committedSkeletons= new CommittedSkeletons();
	protected long recentCommittedSkeletonTime= -1;
	//
	protected KinectFrameReadingTask frameReadingTask= new KinectFrameReadingTask(this);
	protected KinectFrameRecordingTask frameRecordingTask= new KinectFrameRecordingTask(this);
	//
	protected KinectFrameInterface recentFrame;
	protected AtomicLong numberOfRecentReceivedFrame= new AtomicLong(-1);
	protected boolean recentFrameIsRepeated= false;
	protected long numberOfRepeatedFrame= -1;
	//
	protected KinectFrameInterface committedFrame;
	protected long committedFrameNumber= -1;
	protected long committedFrameTime= -1;
	protected long firstCommittedFrameNumber= -1;
	protected long firstCommittedFrameTime= -1;
	protected Term committedSkeletonsTerm;
	protected Term committedTracksTerm;
	protected Term committedChronicleTerm;
	//
	protected AtomicReference<KinectFrameType[]> deliveredDataAcquisitionMode= new AtomicReference<>();
	protected AtomicReference<KinectPeopleIndexMode> deliveredPeopleIndexMode= new AtomicReference<>();
	protected AtomicReference<KinectCircumscriptionMode[]> deliveredCircumscriptionModes= new AtomicReference<>();
	protected AtomicReference<KinectSkeletonsMode> deliveredSkeletonsMode= new AtomicReference<>();
	protected AtomicReference<String> deliveredDescription= new AtomicReference<>();
	protected AtomicReference<String> deliveredCopyright= new AtomicReference<>();
	protected AtomicReference<String> deliveredRegistrationDate= new AtomicReference<>();
	protected AtomicReference<String> deliveredRegistrationTime= new AtomicReference<>();
	//
	protected Term deliveredDataAcquisitionModeTerm;
	protected Term deliveredPeopleIndexModeTerm;
	protected Term deliveredCircumscriptionModesTerm;
	protected Term deliveredSkeletonsModeTerm;
	protected Term deliveredDescriptionTerm;
	protected Term deliveredCopyrightTerm;
	protected Term deliveredRegistrationDateTerm;
	protected Term deliveredRegistrationTimeTerm;
	//
	protected static String defaultDescription= "Actor Prolog";
	protected static String defaultCopyright= "(c) www.fullvision.ru";
	protected static Term termZero= new PrologInteger(0);
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectBuffer() {
	}
	public KinectBuffer(GlobalWorldIdentifier id) {
		super(id);
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
	abstract protected Term getBuiltInSlot_E_operating_mode();
	abstract protected Term getBuiltInSlot_E_displaying_mode();
	abstract protected Term getBuiltInSlot_E_input_device();
	abstract protected Term getBuiltInSlot_E_data_acquisition_optimization();
	abstract protected Term getBuiltInSlot_E_description();
	abstract protected Term getBuiltInSlot_E_copyright();
	abstract protected Term getBuiltInSlot_E_registration_date();
	abstract protected Term getBuiltInSlot_E_registration_time();
	abstract protected Term getBuiltInSlot_E_color_map();
	abstract protected Term getBuiltInSlot_E_people_colors();
	abstract protected Term getBuiltInSlot_E_write_buffer_size();
	abstract protected Term getBuiltInSlot_E_read_buffer_size();
	abstract protected Term getBuiltInSlot_E_maximal_chronicle_length();
	abstract protected Term getBuiltInSlot_E_slow_motion_coefficient();
	abstract protected Term getBuiltInSlot_E_maximal_frame_delay();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set operating_mode
	//
	public void setOperatingMode1s(ChoisePoint iX, Term a1) {
		setOperatingMode(KinectBufferOperatingModeConverters.argumentToKinectBufferOperatingMode(a1,iX));
	}
	public void setOperatingMode(KinectBufferOperatingMode value) {
		operatingMode= value;
	}
	public void getOperatingMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(KinectBufferOperatingModeConverters.toTerm(getOperatingMode(iX)));
	}
	public void getOperatingMode0fs(ChoisePoint iX) {
	}
	public KinectBufferOperatingMode getOperatingMode(ChoisePoint iX) {
		if (operatingMode != null) {
			return operatingMode;
		} else {
			Term value= getBuiltInSlot_E_operating_mode();
			return KinectBufferOperatingModeConverters.argumentToKinectBufferOperatingMode(value,iX);
		}
	}
	//
	// get/set displaying_mode
	//
	public void setDisplayingMode1s(ChoisePoint iX, Term a1) {
		setDisplayingMode(KinectDisplayingModeConverters.argumentToKinectDisplayingMode(a1,iX),iX);
	}
	public void setDisplayingMode(KinectDisplayingModeInterface value, ChoisePoint iX) {
		displayingMode= value;
		updateActingDisplayingMode(value,iX);
		reactivateDeviceIfNecessary(value,iX);
	}
	public void getDisplayingMode0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(KinectDisplayingModeConverters.toTerm(getDisplayingMode(iX)));
	}
	public void getDisplayingMode0fs(ChoisePoint iX) {
	}
	public KinectDisplayingModeInterface getDisplayingMode(ChoisePoint iX) {
		if (displayingMode != null) {
			return displayingMode;
		} else {
			Term value= getBuiltInSlot_E_displaying_mode();
			return KinectDisplayingModeConverters.argumentToKinectDisplayingMode(value,iX);
		}
	}
	//
	// get/set input_device
	//
	public void setInputDevice1s(ChoisePoint iX, Term a1) {
		setInputDevice(KinectDevice.argumentToKinectDevice(a1,this,iX),iX);
	}
	public void setInputDevice(KinectDeviceInterface value, ChoisePoint iX) {
		if (inputDevice != null) {
			inputDevice.update(value,getDisplayingMode(iX),getDataAcquisitionOptimization(iX),iX);
		} else {
			inputDevice= value;
		}
	}
	public void getInputDevice0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getInputDevice(iX).toTerm());
	}
	public void getInputDevice0fs(ChoisePoint iX) {
	}
	public KinectDeviceInterface getInputDevice(ChoisePoint iX) {
		if (inputDevice != null) {
			return inputDevice;
		} else {
			Term value= getBuiltInSlot_E_input_device();
			return KinectDevice.argumentToKinectDevice(value,this,iX);
		}
	}
	//
	// get/set data_acquisition_optimization
	//
	public void setDataAcquisitionOptimization1s(ChoisePoint iX, Term a1) {
		setDataAcquisitionOptimization(KinectPerformanceOptimizationConverters.argumentToKinectPerformanceOptimization(a1,iX));
	}
	public void setDataAcquisitionOptimization(KinectPerformanceOptimization value) {
		dataAcquisitionOptimization= value;
	}
	public void getDataAcquisitionOptimization0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(KinectPerformanceOptimizationConverters.toTerm(getDataAcquisitionOptimization(iX)));
	}
	public void getDataAcquisitionOptimization0fs(ChoisePoint iX) {
	}
	public KinectPerformanceOptimization getDataAcquisitionOptimization(ChoisePoint iX) {
		if (dataAcquisitionOptimization != null) {
			return dataAcquisitionOptimization;
		} else {
			Term value= getBuiltInSlot_E_data_acquisition_optimization();
			return KinectPerformanceOptimizationConverters.argumentToKinectPerformanceOptimization(value,iX);
		}
	}
	//
	// get/set description
	//
	public void setDescription1s(ChoisePoint iX, Term a1) {
		setDescription(KinectTextAttributeConverters.argumentToKinectTextAttribute(a1,iX));
	}
	public void setDescription(KinectTextAttributeInterface value) {
		description= value;
	}
	public void getDescription0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(KinectTextAttributeConverters.toTerm(getDescription(iX)));
	}
	public void getDescription0fs(ChoisePoint iX) {
	}
	public KinectTextAttributeInterface getDescription(ChoisePoint iX) {
		if (description != null) {
			return description;
		} else {
			Term value= getBuiltInSlot_E_description();
			return KinectTextAttributeConverters.argumentToKinectTextAttribute(value,iX);
		}
	}
	//
	// get/set copyright
	//
	public void setCopyright1s(ChoisePoint iX, Term a1) {
		setCopyright(KinectTextAttributeConverters.argumentToKinectTextAttribute(a1,iX));
	}
	public void setCopyright(KinectTextAttributeInterface value) {
		copyright= value;
	}
	public void getCopyright0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(KinectTextAttributeConverters.toTerm(getCopyright(iX)));
	}
	public void getCopyright0fs(ChoisePoint iX) {
	}
	public KinectTextAttributeInterface getCopyright(ChoisePoint iX) {
		if (copyright != null) {
			return copyright;
		} else {
			Term value= getBuiltInSlot_E_copyright();
			return KinectTextAttributeConverters.argumentToKinectTextAttribute(value,iX);
		}
	}
	//
	// get/set registration_date
	//
	public void setRegistrationDate1s(ChoisePoint iX, Term a1) {
		setRegistrationDate(KinectTextAttributeConverters.argumentToKinectTextAttribute(a1,iX));
	}
	public void setRegistrationDate(KinectTextAttributeInterface value) {
		registrationDate= value;
	}
	public void getRegistrationDate0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(KinectTextAttributeConverters.toTerm(getRegistrationDate(iX)));
	}
	public void getRegistrationDate0fs(ChoisePoint iX) {
	}
	public KinectTextAttributeInterface getRegistrationDate(ChoisePoint iX) {
		if (registrationDate != null) {
			return registrationDate;
		} else {
			Term value= getBuiltInSlot_E_registration_date();
			return KinectTextAttributeConverters.argumentToKinectTextAttribute(value,iX);
		}
	}
	//
	// get/set registration_time
	//
	public void setRegistrationTime1s(ChoisePoint iX, Term a1) {
		setRegistrationTime(KinectTextAttributeConverters.argumentToKinectTextAttribute(a1,iX));
	}
	public void setRegistrationTime(KinectTextAttributeInterface value) {
		registrationTime= value;
	}
	public void getRegistrationTime0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(KinectTextAttributeConverters.toTerm(getRegistrationTime(iX)));
	}
	public void getRegistrationTime0fs(ChoisePoint iX) {
	}
	public KinectTextAttributeInterface getRegistrationTime(ChoisePoint iX) {
		if (registrationTime != null) {
			return registrationTime;
		} else {
			Term value= getBuiltInSlot_E_registration_time();
			return KinectTextAttributeConverters.argumentToKinectTextAttribute(value,iX);
		}
	}
	//
	// get/set color_map
	//
	public void setColorMap1s(ChoisePoint iX, Term a1) {
		setColorMap(ColorMapConverters.argumentToColorMap(a1,iX));
	}
	public void setColorMap(ColorMapInterface value) {
		colorMap= value;
	}
	public void getColorMap0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(ColorMapConverters.toTerm(getColorMap(iX)));
	}
	public void getColorMap0fs(ChoisePoint iX) {
	}
	public ColorMapInterface getColorMap(ChoisePoint iX) {
		if (colorMap != null) {
			return colorMap;
		} else {
			Term value= getBuiltInSlot_E_color_map();
			return ColorMapConverters.argumentToColorMap(value,iX);
		}
	}
	//
	// get/set people_colors
	//
	public void setPeopleColors1s(ChoisePoint iX, Term a1) {
		setPeopleColors(ColorMapConverters.argumentToColorMap(a1,iX));
	}
	public void setPeopleColors(ColorMap value) {
		peopleColors= value;
	}
	public void getPeopleColors0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(ColorMapConverters.toTerm(getPeopleColors(iX)));
	}
	public void getPeopleColors0fs(ChoisePoint iX) {
	}
	public ColorMapInterface getPeopleColors(ChoisePoint iX) {
		if (peopleColors != null) {
			return peopleColors;
		} else {
			Term value= getBuiltInSlot_E_people_colors();
			return ColorMapConverters.argumentToColorMap(value,iX);
		}
	}
	//
	// get/set write_buffer_size
	//
	public void setWriteBufferSize1s(ChoisePoint iX, Term a1) {
		setWriteBufferSize(Converters.argumentToSmallInteger(a1,iX));
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
			return Converters.argumentToSmallInteger(value,iX);
		}
	}
	//
	// get/set read_buffer_size
	//
	public void setReadBufferSize1s(ChoisePoint iX, Term a1) {
		setReadBufferSize(Converters.argumentToSmallInteger(a1,iX));
	}
	public void setReadBufferSize(int value) {
		readBufferSize= value;
		frameReadingTask.setReadBufferSize(readBufferSize);
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
			return Converters.argumentToSmallInteger(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set maximal_chronicle_length
	//
	public void setMaximalChronicleLength1s(ChoisePoint iX, Term a1) {
		setMaximalChronicleLength(NumericalValue.argumentToNumericalValue(a1,iX));
	}
	public void setMaximalChronicleLength(NumericalValue value) {
		maximalChronicleLength= value;
		actingMaximalChronicleLength.set(maximalChronicleLength);
	}
	public void getMaximalChronicleLength0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getMaximalChronicleLength(iX).toTerm());
	}
	public void getMaximalChronicleLength0fs(ChoisePoint iX) {
	}
	public NumericalValue getMaximalChronicleLength(ChoisePoint iX) {
		if (maximalChronicleLength != null) {
			return maximalChronicleLength;
		} else {
			Term value= getBuiltInSlot_E_maximal_chronicle_length();
			return NumericalValue.argumentToNumericalValue(value,iX);
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
		setMaximalFrameDelay(KinectIntegerAttributeConverters.argumentToKinectIntegerAttribute(a1,iX));
	}
	public void setMaximalFrameDelay(KinectIntegerAttributeInterface value) {
		maximalFrameDelay= value;
		frameReadingTask.setMaximalWaitingPeriod(value);
	}
	public void getMaximalFrameDelay0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(KinectIntegerAttributeConverters.toTerm(getMaximalFrameDelay(iX)));
	}
	public void getMaximalFrameDelay0fs(ChoisePoint iX) {
	}
	public KinectIntegerAttributeInterface getMaximalFrameDelay(ChoisePoint iX) {
		if (maximalFrameDelay != null) {
			return maximalFrameDelay;
		} else {
			Term value= getBuiltInSlot_E_maximal_frame_delay();
			return KinectIntegerAttributeConverters.argumentToKinectIntegerAttribute(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getComputedCorrection2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		a1.setBacktrackableValue(new PrologInteger(computedHorizontalCorrection),iX);
		a2.setBacktrackableValue(new PrologInteger(computedVerticalCorrection),iX);
		//iX.pushTrail(a1);
		//iX.pushTrail(a2);
	}
	//
	public void recentReadingError2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) throws Backtracking {
		KinectDataReadingError error= dataTransferError.get();
		if (error != null) {
			a1.setBacktrackableValue(new PrologInteger(error.getNumberOfFrameToBeAcquired()),iX);
			a2.setBacktrackableValue(new PrologString(error.getException().toString()),iX);
		} else {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void start0s(ChoisePoint iX) {
		start(true,false,iX);
	}
	public void start0s(ChoisePoint iX, Term a1) {
		boolean requireExclusiveAccess= YesNo.termYesNo2Boolean(a1,iX);
		start(true,requireExclusiveAccess,iX);
	}
	//
	protected void start(boolean activateBuffer, boolean requireExclusiveAccess, ChoisePoint iX) {
		KinectBufferOperatingMode actingOperatingMode= actingKinectBufferOperatingMode.get();
		if (actingOperatingMode != null) {
			if (activateBuffer) {
				switch (actingOperatingMode) {
				case RECORDING:
					{
						KinectDeviceInterface device= actingDevice.get();
						if (device != null) {
							device.activate(iX);
						}
					};
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
				case LISTENING:
					{
						KinectDeviceInterface device= actingDevice.get();
						if (device != null) {
							device.activate(iX);
						}
					};
					break;
				}
			}
		} else {
			resetBuffer();
			KinectBufferOperatingMode currentOperatingMode= getOperatingMode(iX);
			switch (currentOperatingMode) {
			case RECORDING:
				startRecording(currentOperatingMode,activateBuffer,requireExclusiveAccess,iX);
				break;
			case PLAYING:
				frameReadingTask.setStopAfterSingleReading(false);
				startReadingOrPlaying(currentOperatingMode,activateBuffer,iX);
				break;
			case READING:
				frameReadingTask.setStopAfterSingleReading(true);
				startReadingOrPlaying(currentOperatingMode,activateBuffer,iX);
				break;
			case LISTENING:
				startListening(currentOperatingMode,activateBuffer,requireExclusiveAccess,iX);
				break;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void connect0s(ChoisePoint iX) {
		connect(false,false,iX);
	}
	public void connect0s(ChoisePoint iX, Term a1) {
		boolean requireExclusiveAccess= YesNo.termYesNo2Boolean(a1,iX);
		connect(false,requireExclusiveAccess,iX);
	}
	//
	protected void connect(boolean activateBuffer, boolean requireExclusiveAccess, ChoisePoint iX) {
		KinectDeviceInterface currentDevice= getInputDevice(iX);
		if (currentDevice != null) {
			KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
			// int currentWriteBufferSize= getWriteBufferSize(iX);
			KinectPerformanceOptimization currentPerformanceOptimization= getDataAcquisitionOptimization(iX);
			// ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
			// frameRecordingTask.setWriteBufferSize(currentWriteBufferSize);
			// frameRecordingTask.reset(currentFileName);
			// actingDevice.set(currentDevice);
			// actingDescription.set(getDescription(iX));
			// actingCopyright.set(getCopyright(iX));
			// actingRegistrationDate.set(getRegistrationDate(iX));
			// actingRegistrationTime.set(getRegistrationTime(iX));
			// updateActingDisplayingMode(currentDisplayingMode,iX);
			// updateActingDataAcquisitionMode(currentDevice.getDataAcquisitionMode());
			currentDevice.registerListener(currentDisplayingMode,currentPerformanceOptimization,requireExclusiveAccess,iX);
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void startRecording(KinectBufferOperatingMode currentOperatingMode, boolean activateBuffer, boolean requireExclusiveAccess, ChoisePoint iX) {
		KinectDeviceInterface currentDevice= getInputDevice(iX);
		if (currentDevice != null) {
			KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
			int currentWriteBufferSize= getWriteBufferSize(iX);
			KinectPerformanceOptimization currentPerformanceOptimization= getDataAcquisitionOptimization(iX);
			ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
			frameRecordingTask.setWriteBufferSize(currentWriteBufferSize);
			frameRecordingTask.reset(currentFileName);
			actingDevice.set(currentDevice);
			actingDescription.set(getDescription(iX));
			actingCopyright.set(getCopyright(iX));
			actingRegistrationDate.set(getRegistrationDate(iX));
			actingRegistrationTime.set(getRegistrationTime(iX));
			currentDevice.registerListener(currentDisplayingMode,currentPerformanceOptimization,requireExclusiveAccess,iX);
			actingKinectBufferOperatingMode.set(currentOperatingMode);
			try {
				updateActingDisplayingMode(currentDisplayingMode,iX);
				updateActingDataAcquisitionMode(currentDevice.getDataAcquisitionMode());
				actingExclusiveAccessFlag.set(requireExclusiveAccess);
				if (activateBuffer) {
					currentDevice.activate(iX);
				}
			} catch (Throwable e) {
				actingKinectBufferOperatingMode.set(null);
				throw e;
			}
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	//
	protected void startReadingOrPlaying(KinectBufferOperatingMode currentOperatingMode, boolean activateBuffer, ChoisePoint iX) {
		ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
		int currentTimeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
		// int currentSkeletonReleaseTime= getSkeletonReleaseTime(iX);
		int currentReadBufferSize= getReadBufferSize(iX);
		NumericalValue currentMaximalChronicleLength= getMaximalChronicleLength(iX);
		NumericalValue currentSlowMotionCoefficient= getSlowMotionCoefficient(iX);
		frameReadingTask.setReadBufferSize(currentReadBufferSize);
		actingDevice.set(null);
		actingKinectBufferOperatingMode.set(currentOperatingMode);
		try {
			updateActingDisplayingMode(currentDisplayingMode,iX);
			actingMaximalChronicleLength.set(currentMaximalChronicleLength);
			frameReadingTask.setSlowMotionCoefficient(currentSlowMotionCoefficient);
			frameReadingTask.setMaximalWaitingPeriod(getMaximalFrameDelay(iX));
			if (activateBuffer) {
				frameReadingTask.startReading(currentFileName,currentTimeout,currentCharacterSet,currentDisplayingMode,staticContext);
			}
		} catch (Throwable e) {
			actingKinectBufferOperatingMode.set(null);
			throw e;
		}
	}
	//
	protected void startListening(KinectBufferOperatingMode currentOperatingMode, boolean activateBuffer, boolean requireExclusiveAccess, ChoisePoint iX) {
		KinectDeviceInterface currentDevice= getInputDevice(iX);
		if (currentDevice != null) {
			KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
			KinectPerformanceOptimization currentPerformanceOptimization= getDataAcquisitionOptimization(iX);
			// int currentSkeletonReleaseTime= getSkeletonReleaseTime(iX);
			// int currentReadBufferSize= getReadBufferSize(iX);
			NumericalValue currentMaximalChronicleLength= getMaximalChronicleLength(iX);
			actingDevice.set(currentDevice);
			currentDevice.registerListener(currentDisplayingMode,currentPerformanceOptimization,requireExclusiveAccess,iX);
			actingKinectBufferOperatingMode.set(currentOperatingMode);
			try {
				updateActingDisplayingMode(currentDisplayingMode,iX);
				actingMaximalChronicleLength.set(currentMaximalChronicleLength);
				actingExclusiveAccessFlag.set(requireExclusiveAccess);
				if (activateBuffer) {
					currentDevice.activate(iX);
				}
			} catch (Throwable e) {
				actingKinectBufferOperatingMode.set(null);
				throw e;
			}
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	//
	protected void updateActingDisplayingMode(KinectDisplayingModeInterface currentDisplayingMode, ChoisePoint iX) {
		KinectFrameType currentFrameType= currentDisplayingMode.getActingFrameType();
		KinectPeopleIndexMode currentPeopleIndexMode= currentDisplayingMode.getActingPeopleIndexMode();
		KinectCircumscriptionMode[] currentCircumscriptionModes= currentDisplayingMode.getActingCircumscriptionModes();
		KinectSkeletonsMode currentSkeletonsMode= currentDisplayingMode.getActingSkeletonsMode();
		actingFrameType.set(currentFrameType);
		actingPeopleIndexMode.set(currentPeopleIndexMode);
		actingCircumscriptionModes.set(currentCircumscriptionModes);
		updateActingSkeletonsMode(currentSkeletonsMode);
	}
	protected void updateActingSkeletonsMode(KinectSkeletonsMode currentSkeletonsMode) {
		KinectSkeletonsMode previousSkeletonsMode= actingSkeletonsMode.get();
		if (currentSkeletonsMode != previousSkeletonsMode) {
			if (previousSkeletonsMode==KinectSkeletonsMode.NONE) {
				clearSkeletonHistory();
			};
			actingSkeletonsMode.set(currentSkeletonsMode);
		}
	}
	//
	protected void updateActingDataAcquisitionMode(KinectFrameType[] dataAcquisitionMode) {
		actingDataAcquisitionMode.set(dataAcquisitionMode);
	}
	//
	protected void resetBuffer() {
		clearSkeletonHistory();
		// synchronized(rawSkeletonsHistory) {
		synchronized (numberOfRecentReceivedFrame) {
			recentFrame= null;
			numberOfRecentReceivedFrame.set(-1);
			recentFrameIsRepeated= false;
			numberOfRepeatedFrame= -1;
			committedFrame= null;
			committedFrameNumber= -1;
			committedFrameTime= -1;
			firstCommittedFrameNumber= -1;
			firstCommittedFrameTime= -1;
			resetCommittedTerms();
			dataTransferError.set(null);
			deliveredDataAcquisitionMode.set(null);
			deliveredPeopleIndexMode.set(null);
			deliveredCircumscriptionModes.set(null);
			deliveredSkeletonsMode.set(null);
			deliveredDescription.set(null);
			deliveredCopyright.set(null);
			deliveredRegistrationDate.set(null);
			deliveredRegistrationTime.set(null);
			deliveredDataAcquisitionModeTerm= null;
			deliveredPeopleIndexModeTerm= null;
			deliveredCircumscriptionModesTerm= null;
			deliveredSkeletonsModeTerm= null;
			deliveredDescriptionTerm= null;
			deliveredCopyrightTerm= null;
			deliveredRegistrationDateTerm= null;
			deliveredRegistrationTimeTerm= null;
		}
	}
	protected void resetCommittedTerms() {
		committedSkeletonsTerm= null;
		committedTracksTerm= null;
		committedChronicleTerm= null;
	}
	//
	protected void clearSkeletonHistory() {
		// synchronized(rawSkeletonsHistory) {
		synchronized (numberOfRecentReceivedFrame) {
			rawSkeletonsHistory.clear();
			// beginningRawSkeletonTime= -1;
			recentRawSkeletonTime= -1;
			recentRawSkeletonsAreTracked= false;
			committedSkeletons.clear();
			recentCommittedSkeletonTime= -1;
		}
	}
	//
	protected void reactivateDeviceIfNecessary(KinectDisplayingModeInterface currentDisplayingMode, ChoisePoint iX) {
		KinectBufferOperatingMode actingOperatingMode= actingKinectBufferOperatingMode.get();
		if (actingOperatingMode != null) {
			switch (actingOperatingMode) {
			case RECORDING:
			case LISTENING:
				KinectDeviceInterface device= actingDevice.get();
				if (device != null) {
					KinectPerformanceOptimization currentPerformanceOptimization= getDataAcquisitionOptimization(iX);
					boolean requireExclusiveAccess= actingExclusiveAccessFlag.get();
					device.registerListener(currentDisplayingMode,currentPerformanceOptimization,requireExclusiveAccess,iX);
				}
			}
		}
	}
	//
	public void pause0s(ChoisePoint iX) {
		KinectBufferOperatingMode currentOperatingMode= actingKinectBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			{
				KinectDeviceInterface device= actingDevice.get();
				if (device != null) {
					device.suspendListener(iX);
				}
			};
			break;
		case PLAYING:
		case READING:
			frameReadingTask.pauseReading();
			break;
		case LISTENING:
			{
				KinectDeviceInterface device= actingDevice.get();
				if (device != null) {
					device.suspendListener(iX);
				}
			};
			break;
		}
	}
	//
	public void stop0s(ChoisePoint iX) {
		KinectBufferOperatingMode currentOperatingMode= actingKinectBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			{
				KinectDeviceInterface device= actingDevice.get();
				if (device != null) {
					device.cancelListener(iX);
					frameRecordingTask.close();
					actingDevice.set(null);
					actingKinectBufferOperatingMode.set(null);
				}
			}
			break;
		case PLAYING:
		case READING:
			frameReadingTask.closeReading();
			actingDevice.set(null);
			actingKinectBufferOperatingMode.set(null);
			break;
		case LISTENING:
			{
				KinectDeviceInterface device= actingDevice.get();
				if (device != null) {
					device.cancelListener(iX);
					actingDevice.set(null);
					actingKinectBufferOperatingMode.set(null);
				}
			};
			break;
		}
	}
	//
	public void isActive0s(ChoisePoint iX) throws Backtracking {
		KinectBufferOperatingMode currentOperatingMode= actingKinectBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			throw Backtracking.instance;
		}
	}
	public void isSuspended0s(ChoisePoint iX) throws Backtracking {
		KinectBufferOperatingMode currentOperatingMode= actingKinectBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			throw Backtracking.instance;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			{
				KinectDeviceInterface device= actingDevice.get();
				if (device != null) {
					if (device.isSuspended()) {
						return;
					}
				}
			};
			break;
		case PLAYING:
		case READING:
			if (frameReadingTask.isSuspended()) {
				return;
			};
			break;
		case LISTENING:
			{
				KinectDeviceInterface device= actingDevice.get();
				if (device != null) {
					if (device.isSuspended()) {
						return;
					}
				}
			};
			break;
		};
		throw Backtracking.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void retrieveBufferedFrame1s(ChoisePoint iX, Term a1) throws Backtracking {
		int number= Converters.argumentToSmallInteger(a1,iX);
		if (!frameReadingTask.retrieveBufferedFrame(number)) {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void eof0s(ChoisePoint iX) throws Backtracking {
		KinectBufferOperatingMode currentOperatingMode= actingKinectBufferOperatingMode.get();
		if (currentOperatingMode==null) {
			return;
		};
		switch (currentOperatingMode) {
		case RECORDING:
			throw Backtracking.instance;
		case PLAYING:
		case READING:
			// if (actingDevice.get() != null) {
			if (!frameReadingTask.eof()) {
				throw Backtracking.instance;
			};
			// };
			break;
		case LISTENING:
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit0s(ChoisePoint iX) throws Backtracking {
		KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
		boolean updateSkeletonHistory;
		synchronized (numberOfRecentReceivedFrame) {
			if (recentFrame==null) {
				throw Backtracking.instance;
			};
			committedFrame= recentFrame;
			if (!recentFrameIsRepeated) {
				committedFrameNumber= numberOfRecentReceivedFrame.get();
				updateSkeletonHistory= true;
			} else {
				committedFrameNumber= numberOfRepeatedFrame;
				updateSkeletonHistory= false;
			}
		};
		if (committedFrame != null) {
			committedFrameTime= committedFrame.getActingFrameTime();
			if (updateSkeletonHistory) {
				if (firstCommittedFrameTime < 0) {
					firstCommittedFrameNumber= committedFrameNumber;
					firstCommittedFrameTime= committedFrameTime;
				};
				KinectSkeletonsMode skeletonsMode= currentDisplayingMode.getActingSkeletonsMode();
				switch (skeletonsMode) {
				case DETECT_SKELETONS:
					resetCommittedTerms();
					clearSkeletonHistory();
					GeneralSkeletonInterface[] recentSkeletons= committedFrame.getSkeletons();
					DimensionsInterface recentDimensions= committedFrame.getDimensions();
					committedSkeletonsTerm= DimensionsConverters.skeletonsAndDimensionsToTerm(recentSkeletons,recentDimensions,currentDisplayingMode);
					break;
				case DETECT_AND_TRACK_SKELETONS:
					KinectSkeletonsFrameInterface[] copyOfRawSkeletonsHistory;
					// synchronized (rawSkeletonsHistory) {
					synchronized (numberOfRecentReceivedFrame) {
						copyOfRawSkeletonsHistory= rawSkeletonsHistory.toArray(new KinectSkeletonsFrameInterface[0]);
						rawSkeletonsHistory.clear();
						// beginningRawSkeletonTime= -1;
					};
					for (int n=0; n < copyOfRawSkeletonsHistory.length; n++) {
						KinectSkeletonsFrameInterface skeletonFrame= copyOfRawSkeletonsHistory[n];
						boolean isLastElement= (n >= copyOfRawSkeletonsHistory.length-1);
						GeneralSkeletonInterface[] skeletons= skeletonFrame.getSkeletons();
						DimensionsInterface dimensions= skeletonFrame.getDimensions();
						recentCommittedSkeletonTime= committedFrame.getSkeletonsFrameTime();
						ArrayList<Term> recentSkeletonTermArray= new ArrayList<>();
						committedTracksTerm= committedSkeletons.appendFrame(
							skeletonFrame.getReceivedFrameNumber(),
							skeletonFrame.getSkeletonsFrameTime(),
							skeletons,
							dimensions,
							recentSkeletonTermArray,
							currentDisplayingMode,
							actingMaximalChronicleLength.get());
						if (isLastElement) {
							committedSkeletonsTerm= Converters.arrayListToTerm(recentSkeletonTermArray);
						}
					};
					committedChronicleTerm= committedSkeletons.chronicleToTerm();
					break;
				default:
					clearSkeletonHistory();
					resetCommittedTerms();
					break;
				}
			} else {
				KinectSkeletonsMode skeletonsMode= currentDisplayingMode.getActingSkeletonsMode();
				switch (skeletonsMode) {
				case DETECT_SKELETONS:
					resetCommittedTerms();
					clearSkeletonHistory();
					GeneralSkeletonInterface[] recentSkeletons= committedFrame.getSkeletons();
					DimensionsInterface recentDimensions= committedFrame.getDimensions();
					committedSkeletonsTerm= DimensionsConverters.skeletonsAndDimensionsToTerm(recentSkeletons,recentDimensions,currentDisplayingMode);
					break;
				case DETECT_AND_TRACK_SKELETONS:
					ArrayList<Term> recentSkeletonTermArray= new ArrayList<>();
					committedTracksTerm= committedSkeletons.analyseFrame(
						// committedFrame.getReceivedFrameNumber(),
						committedFrameNumber,
						committedFrame.getSkeletonsFrameTime(),
						recentSkeletonTermArray);
					committedSkeletonsTerm= Converters.arrayListToTerm(recentSkeletonTermArray);
					committedChronicleTerm= committedSkeletons.chronicleToTerm(numberOfRepeatedFrame);
					break;
				default:
					clearSkeletonHistory();
					resetCommittedTerms();
					break;
				}
			}
		} else {
			committedFrameTime= -1;
			clearSkeletonHistory();
			resetCommittedTerms();
		}
	}
	//
	// protected void throwRecentError() {
	//	KinectDataReadingError recentError= dataTransferError.get();
	//	if (recentError != null) {
	//		throw recentError;
	//	}
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentFrameNumber1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(new PrologInteger(committedFrameNumber),iX);
		//iX.pushTrail(a1);
	}
	//
	public void getRecentFrameTime1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(new PrologInteger(committedFrameTime),iX);
		//iX.pushTrail(a1);
	}
	//
	public void getRecentFrameRate1s(ChoisePoint iX, PrologVariable a1) {
		long deltaN= committedFrameNumber - firstCommittedFrameNumber;
		long deltaTime= committedFrameTime - firstCommittedFrameTime;
		double rate;
		if (deltaTime > 0) {
			rate= deltaN * 1000.0 / deltaTime;
		} else {
			rate= -1.0;
		};
		a1.setBacktrackableValue(new PrologReal(rate),iX);
		//iX.pushTrail(a1);
	}
	//
	public void getRecentImage1s(ChoisePoint iX, Term value) {
		KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
		ColorMapInterface currentColorMap= getColorMap(iX);
		ColorMapInterface currentPeopleColors= getPeopleColors(iX);
		if (committedFrame != null) {
			java.awt.image.BufferedImage nativeImage;
			if (committedFrame instanceof KinectPointCloudsFrameInterface && currentDisplayingMode.getActingFrameType() == KinectFrameType.DEVICE_TUNING) {
				KinectPointCloudsFrameInterface pointCloudsFrame= (KinectPointCloudsFrameInterface)committedFrame;
				KinectPeopleIndexMode currentPeopleIndexMode= currentDisplayingMode.getActingPeopleIndexMode();
				TunedBufferedImage tunedImage= FrameDrawingUtils.tuneProgramAndCreateMappedBufferedImage(pointCloudsFrame.getXYZ(),pointCloudsFrame.getMappedRed(),pointCloudsFrame.getMappedGreen(),pointCloudsFrame.getMappedBlue(),currentPeopleIndexMode,pointCloudsFrame.getPlayerIndex(),false,pointCloudsFrame.getFocalLengthX(),pointCloudsFrame.getFocalLengthY(),pointCloudsFrame.getCorrectionX(),pointCloudsFrame.getCorrectionY());
				if (tunedImage != null) {
					nativeImage= tunedImage.getImage();
					computedHorizontalCorrection= tunedImage.getHorizontalCorrection();
					computedVerticalCorrection= tunedImage.getVerticalCorrection();
				} else {
					nativeImage= null;
				}
			} else {
				nativeImage= FrameDrawingUtils.getImage(committedFrame,currentDisplayingMode,currentColorMap,currentPeopleColors);
			};
			modifyImage(value,nativeImage,iX);
		} else {
			throw new KinectFrameIsNotCommitted();
		}
	}
	//
	public void getSkeletons1s(ChoisePoint iX, PrologVariable a1) {
		if (committedSkeletonsTerm != null) {
			a1.setBacktrackableValue(committedSkeletonsTerm,iX);
		} else {
			a1.setBacktrackableValue(PrologEmptyList.instance,iX);
		}
	}
	//
	public void getTracks1s(ChoisePoint iX, PrologVariable a1) {
		if (committedTracksTerm != null) {
			a1.setBacktrackableValue(committedTracksTerm,iX);
		} else {
			a1.setBacktrackableValue(PrologEmptyList.instance,iX);
		}
	}
	//
	public void getChronicle1s(ChoisePoint iX, PrologVariable a1) {
		if (committedChronicleTerm != null) {
			a1.setBacktrackableValue(committedChronicleTerm,iX);
		} else {
			a1.setBacktrackableValue(PrologEmptyList.instance,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getDepthImageSizeInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		// synchronized (committedFramesSemaphore) {
		if (committedFrame != null) {
			a1.setBacktrackableValue(new PrologInteger(committedFrame.getDepthFrameWidth()),iX);
			a2.setBacktrackableValue(new PrologInteger(committedFrame.getDepthFrameHeight()),iX);
		} else {
			throw new KinectFrameIsNotCommitted();
		};
		// };
		//iX.pushTrail(a1);
		//iX.pushTrail(a2);
	}
	//
	public void getColorImageSizeInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		// synchronized (committedFramesSemaphore) {
		if (committedFrame != null) {
			a1.setBacktrackableValue(new PrologInteger(committedFrame.getColorFrameWidth()),iX);
			a2.setBacktrackableValue(new PrologInteger(committedFrame.getColorFrameHeight()),iX);
		} else {
			throw new KinectFrameIsNotCommitted();
		};
		// };
		//iX.pushTrail(a1);
		//iX.pushTrail(a2);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getDeliveredDataAcquisitionMode1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredDataAcquisitionModeTerm==null) {
			KinectFrameType[] dataAcquisitionMode= deliveredDataAcquisitionMode.get();
			if (dataAcquisitionMode != null) {
				deliveredDataAcquisitionModeTerm= KinectDataAcquisitionModeConverters.toTerm(dataAcquisitionMode);
			}
		};
		if (deliveredDataAcquisitionModeTerm != null) {
			a1.setBacktrackableValue(deliveredDataAcquisitionModeTerm,iX);
		} else {
			a1.setBacktrackableValue(PrologEmptyList.instance,iX);
		}
	}
	//
	public void getDeliveredPeopleIndexMode1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredPeopleIndexModeTerm==null) {
			KinectPeopleIndexMode peopleIndexMode= deliveredPeopleIndexMode.get();
			if (peopleIndexMode != null) {
				deliveredPeopleIndexModeTerm= KinectPeopleIndexModeConverters.toTerm(peopleIndexMode);
			}
		};
		if (deliveredPeopleIndexModeTerm != null) {
			a1.setBacktrackableValue(deliveredPeopleIndexModeTerm,iX);
		} else {
			a1.setBacktrackableValue(termNone,iX);
		}
	}
	//
	public void getDeliveredCircumscriptionModes1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredCircumscriptionModesTerm==null) {
			KinectCircumscriptionMode[] circumscriptionModes= deliveredCircumscriptionModes.get();
			if (circumscriptionModes != null) {
				deliveredCircumscriptionModesTerm= KinectCircumscriptionModesConverters.toTerm(circumscriptionModes);
			}
		};
		if (deliveredCircumscriptionModesTerm != null) {
			a1.setBacktrackableValue(deliveredCircumscriptionModesTerm,iX);
		} else {
			a1.setBacktrackableValue(PrologEmptyList.instance,iX);
		}
	}
	//
	public void getDeliveredSkeletonsMode1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredSkeletonsModeTerm==null) {
			KinectSkeletonsMode skeletonsMode= deliveredSkeletonsMode.get();
			if (skeletonsMode != null) {
				deliveredSkeletonsModeTerm= KinectSkeletonsModeConverters.toTerm(skeletonsMode);
			}
		};
		if (deliveredSkeletonsModeTerm != null) {
			a1.setBacktrackableValue(deliveredSkeletonsModeTerm,iX);
		} else {
			a1.setBacktrackableValue(termNone,iX);
		}
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
	public boolean sendFrame(KinectFrameInterface frame) {
		KinectBufferOperatingMode currentOperatingMode= actingKinectBufferOperatingMode.get();
		if (currentOperatingMode != null && currentOperatingMode==KinectBufferOperatingMode.RECORDING) {
			long currentFrameNumber;
			synchronized (numberOfRecentReceivedFrame) {
				recentFrame= frame;
				currentFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
				recentFrameIsRepeated= false;
				numberOfRepeatedFrame= -1;
			};
			if (currentFrameNumber <= 0) {
				String fileDescription= actingDescription.get().getValue(defaultDescription);
				String fileCopyright= actingCopyright.get().getValue(defaultCopyright);
				Calendar timer= new GregorianCalendar();
				Date date= timer.getTime();
				DateFormat dateFormat= DateFormat.getDateInstance(DateFormat.LONG,Locale.ENGLISH);
				DateFormat timeFormat= DateFormat.getTimeInstance(DateFormat.FULL,Locale.ENGLISH);
				String textDate= dateFormat.format(date);
				String textTime= timeFormat.format(date);
				String fileRegistrationDate= actingRegistrationDate.get().getValue(textDate);
				String fileRegistrationTime= actingRegistrationTime.get().getValue(textTime);
				KinectModeFrame modeFrame= new KinectModeFrame(
					-1,
					-1,
					-1,
					-1,
					-1,
					-1,
					-1,
					actingFrameType.get(),
					actingSkeletonsMode.get(),
					actingPeopleIndexMode.get(),
					actingCircumscriptionModes.get(),
					actingDataAcquisitionMode.get(),
					false,
					false,
					false,
					false,
					false,
					false,
					false,
					false,
					fileDescription,
					fileCopyright,
					fileRegistrationDate,
					fileRegistrationTime,
					frame.getBaseAttributes());
				frameRecordingTask.store(modeFrame);
			};
			frameRecordingTask.store(frame);
			return true;
		} else {
			KinectFrameType currentActingFrameType= actingFrameType.get();
			KinectDataArrayType dataArrayType= frame.getDataArrayType();
			if (dataArrayType==KinectDataArrayType.MODE_FRAME) {
				KinectModeFrame modeFrame= (KinectModeFrame)frame;
				deliveredDataAcquisitionMode.set(modeFrame.getDataAcquisitionMode());
				deliveredPeopleIndexMode.set(modeFrame.getPeopleIndexMode());
				deliveredCircumscriptionModes.set(modeFrame.getCircumscriptionModes());
				deliveredSkeletonsMode.set(modeFrame.getSkeletonsMode());
				deliveredDescription.set(modeFrame.getDescription());
				deliveredCopyright.set(modeFrame.getCopyright());
				deliveredRegistrationDate.set(modeFrame.getRegistrationDate());
				deliveredRegistrationTime.set(modeFrame.getRegistrationTime());
				return false;
			} else if (currentActingFrameType.requiresFrameType(dataArrayType)) {
				long recentFrameNumber;
				synchronized (numberOfRecentReceivedFrame) {
					recentFrame= frame;
					recentFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
					recentFrameIsRepeated= false;
					numberOfRepeatedFrame= -1;
				};
				KinectSkeletonsMode currentSkeletonsMode= actingSkeletonsMode.get();
				if (currentSkeletonsMode==KinectSkeletonsMode.DETECT_AND_TRACK_SKELETONS) {
					updateRawSkeletonsHistory(frame,recentFrameNumber);
				};
				sendFrameObtained();
				return true;
			} else {
				return false;
			}
		}
	}
	//
	public void transferBufferedFrame(KinectFrameInterface frame, int relativeNumber) {
		KinectFrameType currentActingFrameType= actingFrameType.get();
		if (currentActingFrameType.requiresFrameType(frame.getDataArrayType())) {
			long recentFrameNumber;
			synchronized (numberOfRecentReceivedFrame) {
				recentFrame= frame;
				// recentFrameNumber
				recentFrameIsRepeated= true;
				numberOfRepeatedFrame= numberOfRecentReceivedFrame.get() - relativeNumber;;
			};
			sendFrameObtained();
		}
	}
	//
	protected void updateRawSkeletonsHistory(KinectFrameInterface frame, long recentFrameNumber) {
		boolean thereAreTrackedSkeletons= frame.hasTrackedSkeletons();
		if (thereAreTrackedSkeletons || recentRawSkeletonsAreTracked) {
			long currentTime= frame.getSkeletonsFrameTime();
			// synchronized (rawSkeletonsHistory) {
			synchronized (numberOfRecentReceivedFrame) {
				if (recentRawSkeletonTime < currentTime || currentTime < 0) {
					if (currentTime >= 0) {
						if (!rawSkeletonsHistory.isEmpty()) {
							shortenRawSkeletonsHistoryIfNecessary(currentTime);
						};
						recentRawSkeletonTime= currentTime;
					};
					KinectSkeletonsFrameInterface skeletonFrame= frame.extractSkeletonsFrame(recentFrameNumber);
					rawSkeletonsHistory.add(skeletonFrame);
					recentRawSkeletonsAreTracked= thereAreTrackedSkeletons;
				}
			}
		}
	}
	//
	protected void shortenRawSkeletonsHistoryIfNecessary(long currentTime) {
		NumericalValue currentMaximalChronicleLength= actingMaximalChronicleLength.get();
		if (currentMaximalChronicleLength != null) {
			long longMaximalChronicleLength= currentMaximalChronicleLength.toLong(1000);
			long minimalTime= currentTime - longMaximalChronicleLength;
			ListIterator<KinectSkeletonsFrameInterface> iteratorOfRawSkeletonsHistory= rawSkeletonsHistory.listIterator();
			while (iteratorOfRawSkeletonsHistory.hasNext()) {
				KinectSkeletonsFrameInterface frame= iteratorOfRawSkeletonsHistory.next();
				if (frame.getSkeletonsFrameTime() < minimalTime) {
					iteratorOfRawSkeletonsHistory.remove();
				} else {
					break;
				}
			}
		}
	}
	//
	protected void sendFrameObtained() {
		long domainSignature= entry_s_FrameObtained_0();
		Term[] arguments= new Term[0];
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void completeDataTransfer(long numberOfAcquiredFrames) {
		// frameReadingTask.closeReading();
		actingDevice.set(null);
		actingKinectBufferOperatingMode.set(null);
		dataTransferError.set(null);
		long domainSignature= entry_s_DataTransferCompletion_0();
		Term[] arguments= new Term[0];
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,arguments,true);
		transmitAsyncCall(call,null);
	}
	public void completeDataTransfer(long numberOfFrameToBeAcquired, Throwable e) {
		// frameReadingTask.closeReading();
		actingDevice.set(null);
		actingKinectBufferOperatingMode.set(null);
		dataTransferError.set(new KinectDataReadingError(numberOfFrameToBeAcquired,e));
		long domainSignature= entry_s_DataTransferError_1_i();
		Term[] arguments= new Term[]{new PrologString(e.toString())};
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	public void reportBufferOverflow() {
		long domainSignature= entry_s_BufferOverflow_0();
		Term[] arguments= new Term[0];
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,arguments,true);
		transmitAsyncCall(call,null);
	}
	//
	public void annulBufferOverflow() {
		long domainSignature= entry_s_BufferDeallocation_0();
		Term[] arguments= new Term[0];
		AsyncCall call= new AsyncCall(domainSignature,this,true,false,arguments,true);
		transmitAsyncCall(call,null);
	}
}
