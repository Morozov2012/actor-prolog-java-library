// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.files.*;
import morozov.system.frames.converters.*;
import morozov.system.frames.interfaces.*;
import morozov.system.frames.tools.*;
import morozov.system.gui.space2d.*;
import morozov.system.gui.space2d.errors.*;
import morozov.system.gui.space3d.*;
import morozov.system.gui.space3d.errors.*;
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
import morozov.system.modes.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.media.j3d.Appearance;

public abstract class KinectBuffer extends DataAcquisitionBuffer implements KinectBufferInterface {
	//
	protected KinectDisplayingModeInterface displayingMode;
	protected KinectDeviceInterface inputDevice;
	protected KinectPerformanceOptimization dataAcquisitionOptimization;
	protected KinectColorMap colorMap;
	protected KinectColorMap peopleColors;
	//
	protected NumericalValue maximalChronicleLength;
	//
	protected int computedHorizontalCorrection= ExtendedCorrectionTools.getDefaultHorizontalCorrection();
	protected int computedVerticalCorrection= ExtendedCorrectionTools.getDefaultVerticalCorrection();
	//
	protected AtomicReference<KinectDeviceInterface> actingDevice= new AtomicReference<>();
	protected AtomicBoolean actingExclusiveAccessFlag= new AtomicBoolean(false);
	//
	protected AtomicReference<KinectDisplayingModeInterface> actingDisplayingMode= new AtomicReference<>();
	protected AtomicReference<KinectFrameType> actingFrameType= new AtomicReference<>();
	protected AtomicReference<KinectFrameType[]> actingDataAcquisitionMode= new AtomicReference<>();
	protected AtomicReference<KinectPeopleIndexMode> actingPeopleIndexMode= new AtomicReference<>();
	protected AtomicReference<KinectCircumscriptionMode[]> actingCircumscriptionModes= new AtomicReference<>();
	protected AtomicReference<KinectSkeletonsMode> actingSkeletonsMode= new AtomicReference<>();
	//
	protected AtomicReference<NumericalValue> actingMaximalChronicleLength= new AtomicReference<>();
	//
	protected ArrayList<KinectSkeletonsFrameInterface> rawSkeletonsHistory= new ArrayList<>();
	protected long recentRawSkeletonTime= -1;
	protected boolean recentRawSkeletonsAreTracked= false;
	//
	protected CommittedSkeletons committedSkeletons= new CommittedSkeletons();
	protected long recentCommittedSkeletonTime= -1;
	//
	protected KinectFrameInterface recentKinectFrame;
	//
	// protected AtomicLong numberOfRecentKinectFrame= new AtomicLong(-1);
	protected long numberOfRepeatedFrame= -1;
	//
	protected KinectFrameInterface committedKinectFrame;
	protected long committedFrameNumber= -1;
	protected long committedFrameTime= -1;
	protected long firstCommittedFrameNumber= -1;
	protected long firstCommittedFrameTime= -1;
	protected Term committedSkeletonsTerm;
	protected Term committedTracksTerm;
	protected Term committedChronicleTerm;
	//
	protected KinectLookupTable kinectLookupTable= new KinectLookupTable();
	protected AtomicReference<Appearance> sceneAppearance= new AtomicReference<>(null);
	protected NumericalValue sceneDepthThreshold;
	protected KinectSurfaceType sceneSurfaceType;
	//
	protected AtomicReference<KinectFrameType> deliveredFrameType= new AtomicReference<>();
	protected AtomicReference<KinectFrameType[]> deliveredDataAcquisitionMode= new AtomicReference<>();
	protected AtomicReference<KinectPeopleIndexMode> deliveredPeopleIndexMode= new AtomicReference<>();
	protected AtomicReference<KinectCircumscriptionMode[]> deliveredCircumscriptionModes= new AtomicReference<>();
	protected AtomicReference<KinectSkeletonsMode> deliveredSkeletonsMode= new AtomicReference<>();
	protected AtomicInteger deliveredHorizontalCorrection= new AtomicInteger(0);
	protected AtomicInteger deliveredVerticalCorrection= new AtomicInteger(0);
	//
	protected Term deliveredFrameTypeTerm;
	protected Term deliveredDataAcquisitionModeTerm;
	protected Term deliveredPeopleIndexModeTerm;
	protected Term deliveredCircumscriptionModesTerm;
	protected Term deliveredSkeletonsModeTerm;
	protected Term deliveredHorizontalCorrectionTerm;
	protected Term deliveredVerticalCorrectionTerm;
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectBuffer() {
		super(	new DataFrameReadingTask(),
			new DataFrameRecordingTask());
	}
	public KinectBuffer(GlobalWorldIdentifier id) {
		super(	id,
			new DataFrameReadingTask(),
			new DataFrameRecordingTask());
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_displaying_mode();
	abstract public Term getBuiltInSlot_E_input_device();
	abstract public Term getBuiltInSlot_E_data_acquisition_optimization();
	abstract public Term getBuiltInSlot_E_color_map();
	abstract public Term getBuiltInSlot_E_people_colors();
	abstract public Term getBuiltInSlot_E_maximal_chronicle_length();
	abstract public Term getBuiltInSlot_E_scene_depth_threshold();
	abstract public Term getBuiltInSlot_E_scene_surface_type();
	//
	///////////////////////////////////////////////////////////////
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
	// get/set color_map
	//
	public void setColorMap1s(ChoisePoint iX, Term a1) {
		setColorMap(KinectColorMapConverters.argumentToKinectColorMap(a1,iX));
	}
	public void setColorMap(KinectColorMap value) {
		colorMap= value;
	}
	public void getColorMap0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(KinectColorMapConverters.toTerm(getColorMap(iX)));
	}
	public void getColorMap0fs(ChoisePoint iX) {
	}
	public KinectColorMap getColorMap(ChoisePoint iX) {
		if (colorMap != null) {
			return colorMap;
		} else {
			Term value= getBuiltInSlot_E_color_map();
			return KinectColorMapConverters.argumentToKinectColorMap(value,iX);
		}
	}
	//
	// get/set people_colors
	//
	public void setPeopleColors1s(ChoisePoint iX, Term a1) {
		setPeopleColors(KinectColorMapConverters.argumentToKinectColorMap(a1,iX));
	}
	public void setPeopleColors(KinectColorMap value) {
		peopleColors= value;
	}
	public void getPeopleColors0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(KinectColorMapConverters.toTerm(getPeopleColors(iX)));
	}
	public void getPeopleColors0fs(ChoisePoint iX) {
	}
	public KinectColorMap getPeopleColors(ChoisePoint iX) {
		if (peopleColors != null) {
			return peopleColors;
		} else {
			Term value= getBuiltInSlot_E_people_colors();
			return KinectColorMapConverters.argumentToKinectColorMap(value,iX);
		}
	}
	//
	// get/set maximal_chronicle_length
	//
	public void setMaximalChronicleLength1s(ChoisePoint iX, Term a1) {
		setMaximalChronicleLength(NumericalValueConverters.argumentToNumericalValue(a1,iX));
	}
	public void setMaximalChronicleLength(NumericalValue value) {
		maximalChronicleLength= value;
		actingMaximalChronicleLength.set(maximalChronicleLength);
	}
	public void getMaximalChronicleLength0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(NumericalValueConverters.toTerm(getMaximalChronicleLength(iX)));
	}
	public void getMaximalChronicleLength0fs(ChoisePoint iX) {
	}
	public NumericalValue getMaximalChronicleLength(ChoisePoint iX) {
		if (maximalChronicleLength != null) {
			return maximalChronicleLength;
		} else {
			Term value= getBuiltInSlot_E_maximal_chronicle_length();
			return NumericalValueConverters.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set scene_depth_threshold
	//
	public void setSceneDepthThreshold1s(ChoisePoint iX, Term a1) {
		setSceneDepthThreshold(NumericalValueConverters.argumentToNumericalValue(a1,iX));
	}
	public void setSceneDepthThreshold(NumericalValue value) {
		sceneDepthThreshold= value;
	}
	public void getSceneDepthThreshold0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(NumericalValueConverters.toTerm(getSceneDepthThreshold(iX)));
	}
	public void getSceneDepthThreshold0fs(ChoisePoint iX) {
	}
	public NumericalValue getSceneDepthThreshold(ChoisePoint iX) {
		if (sceneDepthThreshold != null) {
			return sceneDepthThreshold;
		} else {
			Term value= getBuiltInSlot_E_scene_depth_threshold();
			return NumericalValueConverters.argumentToNumericalValue(value,iX);
		}
	}
	//
	// get/set scene_surface_type
	//
	public void setSceneSurfaceType1s(ChoisePoint iX, Term a1) {
		setSceneSurfaceType(KinectSurfaceTypeConverters.argumentToKinectSurfaceType(a1,iX));
	}
	public void setSceneSurfaceType(KinectSurfaceType value) {
		sceneSurfaceType= value;
	}
	public void getSceneSurfaceType0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(KinectSurfaceTypeConverters.toTerm(getSceneSurfaceType(iX)));
	}
	public void getSceneSurfaceType0fs(ChoisePoint iX) {
	}
	public KinectSurfaceType getSceneSurfaceType(ChoisePoint iX) {
		if (sceneSurfaceType != null) {
			return sceneSurfaceType;
		} else {
			Term value= getBuiltInSlot_E_scene_surface_type();
			return KinectSurfaceTypeConverters.argumentToKinectSurfaceType(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateAttributes(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getComputedCorrection2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		a1.setBacktrackableValue(new PrologInteger(computedHorizontalCorrection),iX);
		a2.setBacktrackableValue(new PrologInteger(computedVerticalCorrection),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void connect0s(ChoisePoint iX) {
		connect(false,iX);
	}
	public void connect0s(ChoisePoint iX, Term a1) {
		boolean requireExclusiveAccess= YesNoConverters.termYesNo2Boolean(a1,iX);
		connect(requireExclusiveAccess,iX);
	}
	//
	protected void connect(boolean requireExclusiveAccess, ChoisePoint iX) {
		KinectDeviceInterface currentDevice= getInputDevice(iX);
		if (currentDevice != null) {
			KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
			KinectPerformanceOptimization currentPerformanceOptimization= getDataAcquisitionOptimization(iX);
			currentDevice.registerListener(currentDisplayingMode,currentPerformanceOptimization,requireExclusiveAccess,iX);
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	//
	public void disconnect0s(ChoisePoint iX) {
		KinectDeviceInterface currentDevice= getInputDevice(iX);
		if (currentDevice != null) {
			currentDevice.cancelListener(iX);
		}
	}
	//
	//
	public void isConnected0s(ChoisePoint iX) throws Backtracking {
		KinectDeviceInterface currentDevice= getInputDevice(iX);
		if (currentDevice != null) {
			if (!currentDevice.isRegisteredListener(iX)) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	public void hasExclusiveAccess0s(ChoisePoint iX) throws Backtracking {
		KinectDeviceInterface currentDevice= getInputDevice(iX);
		if (currentDevice != null) {
			if (!currentDevice.hasExclusiveAccess(iX)) {
				throw Backtracking.instance;
			}
		} else {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void resetCounters() {
		synchronized (numberOfRecentReceivedFrame) {
			super.resetCounters();
			clearSkeletonHistory();
			recentKinectFrame= null;
			numberOfRepeatedFrame= -1;
			committedKinectFrame= null;
			resetCommittedTerms();
			deliveredFrameType.set(null);
			deliveredDataAcquisitionMode.set(null);
			deliveredPeopleIndexMode.set(null);
			deliveredCircumscriptionModes.set(null);
			deliveredSkeletonsMode.set(null);
			deliveredHorizontalCorrection.set(0);
			deliveredVerticalCorrection.set(0);
			deliveredFrameTypeTerm= null;
			deliveredDataAcquisitionModeTerm= null;
			deliveredPeopleIndexModeTerm= null;
			deliveredCircumscriptionModesTerm= null;
			deliveredSkeletonsModeTerm= null;
			deliveredHorizontalCorrectionTerm= null;
			deliveredVerticalCorrectionTerm= null;
		}
	}
	//
	protected void resetFrameRate() {
		committedFrameNumber= -1;
		committedFrameTime= -1;
		firstCommittedFrameNumber= -1;
		firstCommittedFrameTime= -1;
	}
	//
	protected void clearSkeletonHistory() {
		synchronized (numberOfRecentReceivedFrame) {
			rawSkeletonsHistory.clear();
			recentRawSkeletonTime= -1;
			recentRawSkeletonsAreTracked= false;
			committedSkeletons.clear();
			recentCommittedSkeletonTime= -1;
		}
	}
	protected void resetCommittedTerms() {
		committedSkeletonsTerm= null;
		committedTracksTerm= null;
		committedChronicleTerm= null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void start0s(ChoisePoint iX, Term a1) {
		boolean requireExclusiveAccess= YesNoConverters.termYesNo2Boolean(a1,iX);
		start(requireExclusiveAccess,iX);
	}
	//
	protected void activateDataAcquisition(ChoisePoint iX) {
		KinectDeviceInterface device= actingDevice.get();
		if (device != null) {
			device.activate(iX);
		}
	}
	//
	protected void startRecording(DataAcquisitionBufferOperatingMode currentOperatingMode, boolean requireExclusiveAccess, ChoisePoint iX) {
		KinectDeviceInterface currentDevice= getInputDevice(iX);
		if (currentDevice != null) {
			KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
			int currentWriteBufferSize= getWriteBufferSize(iX);
			KinectPerformanceOptimization currentPerformanceOptimization= getDataAcquisitionOptimization(iX);
			ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
			int currentOutputDebugInformation= PrologInteger.toInteger(getOutputDebugInformation(iX));
			frameRecordingTask.setWriteBufferSize(currentWriteBufferSize);
			frameRecordingTask.setOutputDebugInformation(currentOutputDebugInformation);
			frameRecordingTask.reset(currentFileName);
			actingDevice.set(currentDevice);
			setActingMetadata(iX);
			currentDevice.registerListener(currentDisplayingMode,currentPerformanceOptimization,requireExclusiveAccess,iX);
			actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
			try {
				updateActingDisplayingMode(currentDisplayingMode,iX);
				updateActingDataAcquisitionMode(currentDevice.getDataAcquisitionMode());
				actingExclusiveAccessFlag.set(requireExclusiveAccess);
				currentDevice.activate(iX);
			} catch (Throwable e) {
				actingDataAcquisitionBufferOperatingMode.set(null);
				throw e;
			}
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	//
	protected void startReadingOrPlaying(boolean doActivateReading, DataAcquisitionBufferOperatingMode currentOperatingMode, ChoisePoint iX) {
		ExtendedFileName currentFileName= retrieveRealLocalFileName(iX);
		int currentTimeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet currentCharacterSet= getCharacterSet(iX);
		KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
		int currentReadBufferSize= getReadBufferSize(iX);
		NumericalValue currentMaximalChronicleLength= getMaximalChronicleLength(iX);
		NumericalValue currentSlowMotionCoefficient= getSlowMotionCoefficient(iX);
		actingReadBufferSize.set(currentReadBufferSize);
		actingDevice.set(null);
		actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
		try {
			updateActingDisplayingMode(currentDisplayingMode,iX);
			actingMaximalChronicleLength.set(currentMaximalChronicleLength);
			frameReadingTask.setSlowMotionCoefficient(currentSlowMotionCoefficient);
			frameReadingTask.setMaximalFrameDelay(getMaximalFrameDelay(iX));
			frameReadingTask.setDisplayingMode(currentDisplayingMode);
			frameReadingTask.startReading(doActivateReading,currentFileName,currentTimeout,currentCharacterSet,staticContext);
		} catch (Throwable e) {
			actingDataAcquisitionBufferOperatingMode.set(null);
			throw e;
		}
	}
	//
	protected void startListening(DataAcquisitionBufferOperatingMode currentOperatingMode, boolean requireExclusiveAccess, ChoisePoint iX) {
		KinectDeviceInterface currentDevice= getInputDevice(iX);
		if (currentDevice != null) {
			KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
			KinectPerformanceOptimization currentPerformanceOptimization= getDataAcquisitionOptimization(iX);
			NumericalValue currentMaximalChronicleLength= getMaximalChronicleLength(iX);
			int currentReadBufferSize= getReadBufferSize(iX);
			actingDevice.set(currentDevice);
			currentDevice.registerListener(currentDisplayingMode,currentPerformanceOptimization,requireExclusiveAccess,iX);
			actingDataAcquisitionBufferOperatingMode.set(currentOperatingMode);
			try {
				updateActingDisplayingMode(currentDisplayingMode,iX);
				actingMaximalChronicleLength.set(currentMaximalChronicleLength);
				actingReadBufferSize.set(currentReadBufferSize);
				actingExclusiveAccessFlag.set(requireExclusiveAccess);
				currentDevice.activate(iX);
			} catch (Throwable e) {
				actingDataAcquisitionBufferOperatingMode.set(null);
				throw e;
			}
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void updateActingDisplayingModeIfNecessary(ChoisePoint iX) {
		if (actingDisplayingMode.get()==null) {
			// int currentTimeout= getMaximalWaitingTimeInMilliseconds(iX);
			// CharacterSet currentCharacterSet= getCharacterSet(iX);
			KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
			int currentReadBufferSize= getReadBufferSize(iX);
			NumericalValue currentMaximalChronicleLength= getMaximalChronicleLength(iX);
			// NumericalValue currentSlowMotionCoefficient= getSlowMotionCoefficient(iX);
			actingReadBufferSize.set(currentReadBufferSize);
			actingDevice.set(null);
			actingDataAcquisitionBufferOperatingMode.set(null);
			updateActingDisplayingMode(currentDisplayingMode,iX);
			actingMaximalChronicleLength.set(currentMaximalChronicleLength);
			// frameReadingTask.setSlowMotionCoefficient(currentSlowMotionCoefficient);
			// frameReadingTask.setMaximalFrameDelay(getMaximalFrameDelay(iX));
			// frameReadingTask.setDisplayingMode(currentDisplayingMode);
			// frameReadingTask.startReading(doActivateReading,currentFileName,currentTimeout,currentCharacterSet,staticContext);
		}
	}
	//
	protected void updateActingDisplayingMode(KinectDisplayingModeInterface currentDisplayingMode, ChoisePoint iX) {
		KinectFrameType currentFrameType= currentDisplayingMode.getActingFrameType();
		KinectPeopleIndexMode currentPeopleIndexMode= currentDisplayingMode.getActingPeopleIndexMode();
		KinectCircumscriptionMode[] currentCircumscriptionModes= currentDisplayingMode.getActingCircumscriptionModes();
		KinectSkeletonsMode currentSkeletonsMode= currentDisplayingMode.getActingSkeletonsMode();
		actingDisplayingMode.set(currentDisplayingMode);
		actingFrameType.set(currentFrameType);
		actingPeopleIndexMode.set(currentPeopleIndexMode);
		actingCircumscriptionModes.set(currentCircumscriptionModes);
		updateActingSkeletonsMode(currentSkeletonsMode);
		frameReadingTask.setDisplayingMode(currentDisplayingMode);
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
	protected void reactivateDeviceIfNecessary(KinectDisplayingModeInterface currentDisplayingMode, ChoisePoint iX) {
		DataAcquisitionBufferOperatingMode actingOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
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
	///////////////////////////////////////////////////////////////
	//
	protected void suspendRecording(ChoisePoint iX) {
		KinectDeviceInterface device= actingDevice.get();
		if (device != null) {
			device.suspendListener(iX);
		};
		super.suspendRecording(iX);
	}
	protected void suspendListening(ChoisePoint iX) {
		KinectDeviceInterface device= actingDevice.get();
		if (device != null) {
			device.suspendListener(iX);
		};
		super.suspendListening(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void stopRecording(ChoisePoint iX) {
		KinectDeviceInterface device= actingDevice.get();
		if (device != null) {
			device.cancelListener(iX);
			frameRecordingTask.close();
			actingDevice.set(null);
			actingDataAcquisitionBufferOperatingMode.set(null);
		}
	}
	protected void stopListening(ChoisePoint iX) {
		KinectDeviceInterface device= actingDevice.get();
		if (device != null) {
			device.cancelListener(iX);
			actingDevice.set(null);
			actingDataAcquisitionBufferOperatingMode.set(null);
		}
	}
	//
	protected boolean committedFrameIsNull() {
		return (committedKinectFrame==null);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected boolean dataAcquisitionIsActive() {
		KinectDeviceInterface device= actingDevice.get();
		if (device != null) {
			if (device.isNotSuspended()) {
				return true;
			}
		};
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected boolean dataAcquisitionIsSuspended() {
		KinectDeviceInterface device= actingDevice.get();
		if (device != null) {
			if (device.isSuspended()) {
				return true;
			}
		};
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit0s(ChoisePoint iX) throws Backtracking {
		if (committedFrameWasAssignedDirectly.get()) {
			if (committedKinectFrame==null) {
				throw Backtracking.instance;
			}
		} else {
			KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
			synchronized (numberOfRecentReceivedFrame) {
				if (recentKinectFrame==null) {
					throw Backtracking.instance;
				};
				commit(currentDisplayingMode);
			}
		}
	}
	//
	protected void commit(KinectDisplayingModeInterface currentDisplayingMode) {
		super.commit();
		boolean updateSkeletonHistory;
		synchronized (numberOfRecentReceivedFrame) {
			committedKinectFrame= recentKinectFrame;
			if (!recentFrameIsRepeated) {
				// committedFrameNumber= numberOfRecentKinectFrame.get();
				committedFrameNumber= numberOfRecentReceivedFrame.get();
				updateSkeletonHistory= true;
			} else {
				committedFrameNumber= numberOfRepeatedFrame;
				updateSkeletonHistory= false;
			}
		};
		updateCommittedFrameTime(updateSkeletonHistory,currentDisplayingMode);
	}
	//
	protected void updateCommittedFrameTime(boolean updateSkeletonHistory, KinectDisplayingModeInterface currentDisplayingMode) {
		if (committedKinectFrame != null) {
			committedFrameTime= committedKinectFrame.getActingFrameTime();
			if (updateSkeletonHistory) {
				if (firstCommittedFrameTime < 0) {
					firstCommittedFrameNumber= committedFrameNumber;
					firstCommittedFrameTime= committedFrameTime;
				};
				if (currentDisplayingMode != null) {
///////////////////////////////////////////////////////////////////////
KinectSkeletonsMode skeletonsMode= currentDisplayingMode.getActingSkeletonsMode();
switch (skeletonsMode) {
case DETECT_SKELETONS:
	resetCommittedTerms();
	clearSkeletonHistory();
	GeneralSkeletonInterface[] recentSkeletons= committedKinectFrame.getSkeletons();
	DimensionsInterface recentDimensions= committedKinectFrame.getDimensions();
	committedSkeletonsTerm= DimensionsConverters.skeletonsAndDimensionsToTerm(recentSkeletons,recentDimensions,currentDisplayingMode);
	break;
case DETECT_AND_TRACK_SKELETONS:
	KinectSkeletonsFrameInterface[] copyOfRawSkeletonsHistory;
	synchronized (numberOfRecentReceivedFrame) {
		int historySize= rawSkeletonsHistory.size();
		int lastElementToBeCopied= 0;
		if (historySize > 0) {
			KinectSkeletonsFrameInterface firstSkeletonsFrame= rawSkeletonsHistory.get(0);
			KinectSkeletonsFrameInterface lastSkeletonsFrame= rawSkeletonsHistory.get(historySize-1);
			long minimalTime= firstSkeletonsFrame.getSkeletonsFrameTime();
			long maximalTime= lastSkeletonsFrame.getSkeletonsFrameTime();
			if (committedFrameTime >= minimalTime) {
				// if (committedFrameTime <= maximalTime) {
///////////////////////////////////////////////////////////////////////
KinectSkeletonsFrameInterface selectedSkeletonsFrame= firstSkeletonsFrame;
long delay1= committedFrameTime - minimalTime;
if (delay1 < 0) {
	delay1= -delay1;
};
// long DELAY= delay1;
for (int k=0; k < historySize; k++) {
	KinectSkeletonsFrameInterface currentSkeletonsFrame= rawSkeletonsHistory.get(k);
	long time2= currentSkeletonsFrame.getSkeletonsFrameTime();
	long delay2= committedFrameTime - time2;
	if (delay2 < 0) {
		delay2= -delay2;
	};
	if (time2 >= committedFrameTime) {
		if (delay2 < delay1) {
			selectedSkeletonsFrame= currentSkeletonsFrame;
			lastElementToBeCopied= k;
// DELAY= delay2;
		};
		break;
	} else {
		selectedSkeletonsFrame= currentSkeletonsFrame;
		lastElementToBeCopied= k;
		delay1= delay2;
// DELAY= delay1;
	}
}
///////////////////////////////////////////////////////////////////////
				// }
			};
			List<KinectSkeletonsFrameInterface> subList= rawSkeletonsHistory.subList(0,lastElementToBeCopied+1);
			copyOfRawSkeletonsHistory= subList.toArray(new KinectSkeletonsFrameInterface[0]);
			// rawSkeletonsHistory.clear();
			subList.clear();
		} else {
			copyOfRawSkeletonsHistory= rawSkeletonsHistory.toArray(new KinectSkeletonsFrameInterface[0]);
			rawSkeletonsHistory.clear();
		}
	};
	for (int n=0; n < copyOfRawSkeletonsHistory.length; n++) {
		KinectSkeletonsFrameInterface skeletonFrame= copyOfRawSkeletonsHistory[n];
		boolean isLastElement= (n >= copyOfRawSkeletonsHistory.length-1);
		GeneralSkeletonInterface[] skeletons= skeletonFrame.getSkeletons();
		DimensionsInterface dimensions= skeletonFrame.getDimensions();
		recentCommittedSkeletonTime= committedKinectFrame.getSkeletonsFrameTime();
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
			committedSkeletonsTerm= GeneralConverters.arrayListToTerm(recentSkeletonTermArray);
		}
	};
	committedChronicleTerm= committedSkeletons.chronicleToTerm();
	break;
default:
	clearSkeletonHistory();
	resetCommittedTerms();
	break;
}
///////////////////////////////////////////////////////////////////////
				}
			} else { // !updateSkeletonHistory
				if (currentDisplayingMode != null) {
///////////////////////////////////////////////////////////////////////
KinectSkeletonsMode skeletonsMode= currentDisplayingMode.getActingSkeletonsMode();
switch (skeletonsMode) {
case DETECT_SKELETONS:
	resetCommittedTerms();
	clearSkeletonHistory();
	GeneralSkeletonInterface[] recentSkeletons= committedKinectFrame.getSkeletons();
	DimensionsInterface recentDimensions= committedKinectFrame.getDimensions();
	committedSkeletonsTerm= DimensionsConverters.skeletonsAndDimensionsToTerm(recentSkeletons,recentDimensions,currentDisplayingMode);
	break;
case DETECT_AND_TRACK_SKELETONS:
	ArrayList<Term> recentSkeletonTermArray= new ArrayList<>();
	committedTracksTerm= committedSkeletons.analyseFrame(
		committedFrameNumber,
		committedKinectFrame.getSkeletonsFrameTime(),
		recentSkeletonTermArray);
	committedSkeletonsTerm= GeneralConverters.arrayListToTerm(recentSkeletonTermArray);
	committedChronicleTerm= committedSkeletons.chronicleToTerm(numberOfRepeatedFrame);
	break;
default:
	clearSkeletonHistory();
	resetCommittedTerms();
	break;
}
///////////////////////////////////////////////////////////////////////
				}
			}
		} else {
			committedFrameTime= -1;
			clearSkeletonHistory();
			resetCommittedTerms();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentFrameNumber1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(new PrologInteger(committedFrameNumber),iX);
	}
	//
	public void getRecentFrameTime1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(new PrologInteger(committedFrameTime),iX);
	}
	//
	public void getRecentFrameRelativeTime1s(ChoisePoint iX, PrologVariable a1) {
		long deltaTime= committedFrameTime - firstCommittedFrameTime;
		a1.setBacktrackableValue(new PrologInteger(deltaTime),iX);
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
		/*
		long deltaN= committedFrameNumber - firstCommittedFrameNumber;
		long deltaTime= committedFrameTime - firstCommittedFrameTime;
		double rate;
		if (deltaTime > 0) {
			rate= deltaN * 1000.0 / deltaTime;
		} else {
			rate= -1.0;
		};
		a1.setBacktrackableValue(new PrologReal(rate),iX);
		*/
	}
	//
	public void getRecentImage1s(ChoisePoint iX, Term value) {
		updateAttributesIfNecessary(iX);
		KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
		KinectColorMap currentColorMap= getColorMap(iX);
		KinectColorMap currentPeopleColors= getPeopleColors(iX);
		if (committedKinectFrame != null) {
			java.awt.image.BufferedImage nativeImage;
			if (committedKinectFrame instanceof KinectPointCloudsFrameInterface && currentDisplayingMode.getActingFrameType() == KinectFrameType.DEVICE_TUNING) {
				KinectPointCloudsFrameInterface pointCloudsFrame= (KinectPointCloudsFrameInterface)committedKinectFrame;
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
				nativeImage= FrameDrawingUtils.getImage(committedKinectFrame,currentDisplayingMode,currentColorMap,currentPeopleColors);
			};
			modifyImage(value,nativeImage,iX);
		} else {
			throw new KinectFrameIsNotCommitted();
		}
	}
	//
	public void getRecentScene1s(ChoisePoint iX, Term a1) {
		updateAttributesIfNecessary(iX);
		KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
		KinectColorMap currentColorMap= getColorMap(iX);
		KinectColorMap currentPeopleColors= getPeopleColors(iX);
		a1= a1.dereferenceValue(iX);
		if (a1 instanceof BufferedScene) {
			BufferedScene scene= (BufferedScene)a1;
			if (committedKinectFrame != null) {
				Kinect2Java3D.setBufferedScene(scene,committedKinectFrame,currentDisplayingMode,currentColorMap,currentPeopleColors,null,kinectLookupTable,getSceneDepthThreshold(iX),getSceneSurfaceType(iX),sceneAppearance.get());
			} else {
				throw new KinectFrameIsNotCommitted();
			}
		} else {
			throw new WrongArgumentIsNotBufferedScene(a1);
		}
	}
	public void getRecentScene2s(ChoisePoint iX, Term a1, Term a2) {
		updateAttributesIfNecessary(iX);
		KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
		KinectColorMap currentColorMap= getColorMap(iX);
		KinectColorMap currentPeopleColors= getPeopleColors(iX);
		a1= a1.dereferenceValue(iX);
		a2= a2.dereferenceValue(iX);
		if (a1 instanceof BufferedScene) {
			BufferedScene scene= (BufferedScene)a1;
			if (a2 instanceof BufferedImage) {
				BufferedImage bufferedImage= (BufferedImage)a2;
				java.awt.image.BufferedImage nativeImage= bufferedImage.getImage();
				if (committedKinectFrame != null) {
					Kinect2Java3D.setBufferedScene(scene,committedKinectFrame,currentDisplayingMode,currentColorMap,currentPeopleColors,nativeImage,kinectLookupTable,getSceneDepthThreshold(iX),getSceneSurfaceType(iX),sceneAppearance.get());
				} else {
					throw new KinectFrameIsNotCommitted();
				}
			} else {
				throw new WrongArgumentIsNotBufferedImage(a2);
			}
		} else {
			throw new WrongArgumentIsNotBufferedScene(a1);
		}
	}
	//
	public void getRecentMapping1s(ChoisePoint iX, Term a1) {
		updateAttributesIfNecessary(iX);
		KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
		KinectColorMap currentPeopleColors= getPeopleColors(iX);
		a1= a1.dereferenceValue(iX);
		if (a1 instanceof BufferedImage) {
			BufferedImage mappedImage= (BufferedImage)a1;
			if (committedKinectFrame != null) {
				java.awt.image.BufferedImage nativeMappedImage= Kinect2Java3D.computeMappedImage(committedKinectFrame,currentDisplayingMode,/*currentColorMap,*/currentPeopleColors,null,kinectLookupTable);
				GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
				mappedImage.setImage(nativeMappedImage,attributes);
			} else {
				throw new KinectFrameIsNotCommitted();
			}
		} else {
			throw new WrongArgumentIsNotBufferedImage(a1);
		}
	}
	public void getRecentMapping2s(ChoisePoint iX, Term a1, Term a2) {
		updateAttributesIfNecessary(iX);
		KinectDisplayingModeInterface currentDisplayingMode= getDisplayingMode(iX);
		KinectColorMap currentPeopleColors= getPeopleColors(iX);
		a1= a1.dereferenceValue(iX);
		a2= a2.dereferenceValue(iX);
		if (a1 instanceof BufferedImage) {
			BufferedImage mappedImage= (BufferedImage)a1;
			if (a2 instanceof BufferedImage) {
				BufferedImage textureImage= (BufferedImage)a2;
				java.awt.image.BufferedImage nativeTextureImage= textureImage.getImage();
				if (committedKinectFrame != null) {
					java.awt.image.BufferedImage nativeMappedImage= Kinect2Java3D.computeMappedImage(committedKinectFrame,currentDisplayingMode,/*currentColorMap,*/currentPeopleColors,nativeTextureImage,kinectLookupTable);
					GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
					mappedImage.setImage(nativeMappedImage,attributes);
				} else {
					throw new KinectFrameIsNotCommitted();
				}
			} else {
				throw new WrongArgumentIsNotBufferedImage(a2);
			}
		} else {
			throw new WrongArgumentIsNotBufferedImage(a1);
		}
	}
	//
	public void setLookupTable1s(ChoisePoint iX, Term a1) {
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet requestedCharacterSet= getCharacterSet(iX);
		ExtendedFileName fileName= retrieveRealGlobalFileNameWithoutExtension(a1,iX);
		kinectLookupTable.loadLookupTable(fileName,timeout,requestedCharacterSet,0,0,staticContext,iX);
	}
	public void setLookupTable3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		int correctionX= PrologInteger.toInteger(GeneralConverters.argumentToStrictInteger(a2,iX));
		int correctionY= PrologInteger.toInteger(GeneralConverters.argumentToStrictInteger(a3,iX));
		int timeout= getMaximalWaitingTimeInMilliseconds(iX);
		CharacterSet requestedCharacterSet= getCharacterSet(iX);
		ExtendedFileName fileName= retrieveRealGlobalFileNameWithoutExtension(a1,iX);
		kinectLookupTable.loadLookupTable(fileName,timeout,requestedCharacterSet,correctionX,correctionY,staticContext,iX);
	}
	//
	public void resetLookupTable0s(ChoisePoint iX) {
		kinectLookupTable.resetLookupTable();
	}
	//
	public void setSceneAppearance1s(ChoisePoint iX, Term a1) {
		Appearance appearance= AuxiliaryNode3D.argumentToAppearance(a1,this,iX);
		sceneAppearance.set(appearance);
	}
	public void resetSceneAppearance0s(ChoisePoint iX) {
		sceneAppearance.set(null);
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
		if (committedKinectFrame != null) {
			a1.setBacktrackableValue(new PrologInteger(committedKinectFrame.getDepthFrameWidth()),iX);
			a2.setBacktrackableValue(new PrologInteger(committedKinectFrame.getDepthFrameHeight()),iX);
		} else {
			throw new KinectFrameIsNotCommitted();
		}
	}
	//
	public void getColorImageSizeInPixels2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		if (committedKinectFrame != null) {
			a1.setBacktrackableValue(new PrologInteger(committedKinectFrame.getColorFrameWidth()),iX);
			a2.setBacktrackableValue(new PrologInteger(committedKinectFrame.getColorFrameHeight()),iX);
		} else {
			throw new KinectFrameIsNotCommitted();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getDeliveredFrameType1s(ChoisePoint iX, PrologVariable a1) {
		if (deliveredFrameTypeTerm==null) {
			KinectFrameType frameType= deliveredFrameType.get();
			if (frameType != null) {
				deliveredFrameTypeTerm= KinectFrameTypeConverters.toTerm(frameType);
			}
		};
		if (deliveredFrameTypeTerm != null) {
			a1.setBacktrackableValue(deliveredFrameTypeTerm,iX);
		} else {
			a1.setBacktrackableValue(termNone,iX);
		}
	}
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
	public void getDeliveredCorrection2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		if (deliveredHorizontalCorrectionTerm==null) {
			deliveredHorizontalCorrectionTerm= new PrologInteger(deliveredHorizontalCorrection.get());
		};
		if (deliveredVerticalCorrectionTerm==null) {
			deliveredVerticalCorrectionTerm= new PrologInteger(deliveredVerticalCorrection.get());
		};
		a1.setBacktrackableValue(deliveredHorizontalCorrectionTerm,iX);
		a2.setBacktrackableValue(deliveredVerticalCorrectionTerm,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean sendDataFrame(DataFrameInterface frame) {
		return false;
	}
	//
	public boolean sendKinectFrame(KinectFrameInterface frame) {
		DataAcquisitionBufferOperatingMode currentOperatingMode= actingDataAcquisitionBufferOperatingMode.get();
		if (currentOperatingMode != null && currentOperatingMode==DataAcquisitionBufferOperatingMode.RECORDING) {
			long currentFrameNumber= updateRecentFrame(frame);
			if (currentFrameNumber == 0) {
				KinectModeFrame modeFrame= createKinectModeFrame(frame.getBaseAttributes());
				frameRecordingTask.store(modeFrame);
			};
			frameRecordingTask.store(frame);
			sendFrameObtained();
			return true;
		} else {
			KinectFrameType currentActingFrameType= actingFrameType.get();
			KinectDataArrayType dataArrayType= frame.getDataArrayType();
			if (dataArrayType==KinectDataArrayType.MODE_FRAME) {
				acceptSettings(frame);
				return false;
			} else if (currentActingFrameType.requiresFrameType(dataArrayType)) {
				long recentFrameNumber= updateRecentFrame(frame);
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
	protected KinectModeFrame createKinectModeFrame(KinectFrameBaseAttributesInterface givenAttributes) {
		MetadataDescription metadataDescription= createMetadataDescription();
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
			metadataDescription.getDescription(),
			metadataDescription.getCopyright(),
			metadataDescription.getRegistrationDate(),
			metadataDescription.getRegistrationTime(),
			givenAttributes);
		return modeFrame;
	}
	//
	protected void acceptSettings(KinectFrameInterface frame) {
		KinectModeFrame modeFrame= (KinectModeFrame)frame;
		deliveredFrameType.set(modeFrame.getKinectFrameType());
		deliveredDataAcquisitionMode.set(modeFrame.getDataAcquisitionMode());
		deliveredPeopleIndexMode.set(modeFrame.getPeopleIndexMode());
		deliveredCircumscriptionModes.set(modeFrame.getCircumscriptionModes());
		deliveredSkeletonsMode.set(modeFrame.getSkeletonsMode());
		deliveredHorizontalCorrection.set(modeFrame.getCorrectionX());
		deliveredVerticalCorrection.set(modeFrame.getCorrectionY());
		deliveredFrameTypeTerm= null;
		deliveredDataAcquisitionModeTerm= null;
		deliveredPeopleIndexModeTerm= null;
		deliveredCircumscriptionModesTerm= null;
		deliveredSkeletonsModeTerm= null;
		deliveredHorizontalCorrectionTerm= null;
		deliveredVerticalCorrectionTerm= null;
		deliveredDescription.set(modeFrame.getDescription());
		deliveredCopyright.set(modeFrame.getCopyright());
		deliveredRegistrationDate.set(modeFrame.getRegistrationDate());
		deliveredRegistrationTime.set(modeFrame.getRegistrationTime());
	}
	//
	protected long updateRecentFrame(KinectFrameInterface frame) {
		synchronized (numberOfRecentReceivedFrame) {
			recentKinectFrame= frame;
			committedFrameWasAssignedDirectly.set(false);
			// numberOfRecentKinectFrame.set(recentKinectFrame.getSerialNumber());
			long currentNumber= numberOfRecentReceivedFrame.incrementAndGet();
			// numberOfRecentKinectFrame.set(currentNumber);
			updateHistory(frame);
			recentFrameIsRepeated= false;
			numberOfRepeatedFrame= -1;
			numberOfRecentReceivedFrame.notifyAll();
			return currentNumber;
		}
	}
	//
	protected void updateHistory(KinectFrameInterface recentDataFrame) {
		if (recentDataFrame==null) {
			return;
		};
		// updateHistory(new EnumeratedKinectFrame(recentDataFrame,numberOfRecentKinectFrame.get()));
		updateHistory(new EnumeratedKinectFrame(recentDataFrame,numberOfRecentReceivedFrame.get()));
	}
	//
	protected void updateRawSkeletonsHistory(KinectFrameInterface frame, long recentFrameNumber) {
		boolean thereAreTrackedSkeletons= frame.hasTrackedSkeletons();
		if (thereAreTrackedSkeletons || recentRawSkeletonsAreTracked) {
			long currentTime= frame.getSkeletonsFrameTime();
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
			long longMaximalChronicleLength= NumericalValueConverters.toLong(currentMaximalChronicleLength,1000);
			if (longMaximalChronicleLength < 0) {
				return;
			};
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
	///////////////////////////////////////////////////////////////
	//
	public void completeDataReading(long numberOfAcquiredFrames) {
		actingDevice.set(null);
		super.completeDataReading(numberOfAcquiredFrames);
	}
	//
	public void completeDataReading(long numberOfFrameToBeAcquired, Throwable e) {
		actingDevice.set(null);
		super.completeDataReading(numberOfFrameToBeAcquired,e);
	}
	//
	public void completeDataWriting(long numberOfFrame, Throwable e) {
		actingDevice.set(null);
		super.completeDataWriting(numberOfFrame,e);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void acceptRequestedFrame(EnumeratedFrame enumeratedFrame) {
		EnumeratedKinectFrame selectedFrame= (EnumeratedKinectFrame)enumeratedFrame;
		recentKinectFrame= selectedFrame.getFrame();
		committedFrameWasAssignedDirectly.set(false);
		recentFrameIsRepeated= true;
		numberOfRepeatedFrame= selectedFrame.getNumberOfFrame();
	}
	//
	protected void acceptRetrievedFrame(EnumeratedFrame enumeratedFrame) {
		EnumeratedKinectFrame selectedFrame= (EnumeratedKinectFrame)enumeratedFrame;
		KinectFrameInterface kinectFrame= selectedFrame.getFrame();
		committedKinectFrame= kinectFrame;
		committedFrameWasAssignedDirectly.set(true);
		committedFrameNumber= selectedFrame.getNumberOfFrame();
		committedFrameTime= committedKinectFrame.getActingFrameTime();
		if (firstCommittedFrameTime < 0) {
			firstCommittedFrameNumber= committedFrameNumber;
			firstCommittedFrameTime= committedFrameTime;
		};
		KinectDisplayingModeInterface currentDisplayingMode= actingDisplayingMode.get();
		updateCommittedFrameTime(true,currentDisplayingMode);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void extractFrame(String key, CompoundFrameInterface container) {
		if (committedKinectFrame != null) {
			EnumeratedKinectFrame enumeratedFrame= new EnumeratedKinectFrame(
				committedKinectFrame,
				committedFrameNumber);
			container.insertComponent(key,enumeratedFrame);
		} else {
			throw new KinectFrameIsNotCommitted();
		}
	}
	//
	public void extractSettings(String key, CompoundFrameInterface container, ChoisePoint iX) {
		// if (committedFrame != null) {
		KinectDeviceInterface device= actingDevice.get();
		if (device != null) {
			KinectFrameWritableBaseAttributes attributes= device.createKinectFrameWritableBaseAttributes();
			setActingMetadata(iX);
			KinectModeFrame modeFrame= createKinectModeFrame(attributes);
			EnumeratedKinectFrame enumeratedFrame= new EnumeratedKinectFrame(
				modeFrame,
				-1);
			container.insertComponent(key,enumeratedFrame);
		}
		// } else {
		//	throw new CompoundFrameIsNotCommitted();
		// }
	}
	//
	public void assignFrame(String key, CompoundFrameInterface container, ChoisePoint iX) {
		updateActingDisplayingModeIfNecessary(iX);
		EnumeratedKinectFrame enumeratedFrame= (EnumeratedKinectFrame)container.getComponent(key);
		synchronized (numberOfRecentReceivedFrame) {
			KinectFrameInterface frame= enumeratedFrame.getFrame();
			KinectDataArrayType dataArrayType= frame.getDataArrayType();
			if (dataArrayType==KinectDataArrayType.MODE_FRAME) {
				acceptSettings(frame);
			} else {
				committedKinectFrame= frame;
				committedFrameWasAssignedDirectly.set(true);
				// committedFrameNumber= enumeratedFrame.getNumberOfFrame();
				// committedFrameNumber= numberOfRecentKinectFrame.incrementAndGet();
				committedFrameNumber= numberOfRecentReceivedFrame.incrementAndGet();
				updateCommittedFrameTime(true,actingDisplayingMode.get());
			}
		}
	}
}
