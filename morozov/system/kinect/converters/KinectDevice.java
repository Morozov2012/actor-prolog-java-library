// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.converters;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.kinect.converters.errors.*;
import morozov.system.kinect.converters.interfaces.*;
import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.interfaces.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.converters.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.system.kinect.modes.tools.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class KinectDevice implements KinectDeviceInterface {
	//
	protected AtomicBoolean useNoDevice= new AtomicBoolean(false);
	protected AtomicReference<Kinect> device= new AtomicReference<>();
	protected AtomicReference<KinectFrameType[]> dataAcquisitionMode= new AtomicReference<>();
	protected AtomicReference<KinectBufferInterface> buffer= new AtomicReference<>();
	//
	protected AtomicBoolean requireExclusiveAccess= new AtomicBoolean(false);
	//
	protected AtomicReference<KinectDisplayingModeInterface> displayingMode= new AtomicReference<>();
	protected AtomicReference<KinectPerformanceOptimization> performanceOptimization= new AtomicReference<>();
	//
	protected AtomicBoolean isRegistered= new AtomicBoolean(false);
	protected AtomicBoolean isSuspended= new AtomicBoolean(true);
	//
	///////////////////////////////////////////////////////////////
	//
	public KinectDevice(Kinect d, KinectFrameType[] m, KinectBufferInterface b) {
		useNoDevice.set(false);
		device.set(d);
		dataAcquisitionMode.set(m);
		buffer.set(b);
	}
	public KinectDevice(KinectBufferInterface b) {
		useNoDevice.set(true);
		buffer.set(b);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void update(KinectDeviceInterface newAttributes, KinectDisplayingModeInterface givenDisplayingMode, KinectPerformanceOptimization givenOptimizationMode, ChoisePoint iX) {
		useNoDevice.set(newAttributes.getUseNoDevice());
		Kinect newDevice= newAttributes.getDevice();
		device.set(newDevice);
		dataAcquisitionMode.set(newAttributes.getDataAcquisitionMode());
		buffer.set(newAttributes.getBuffer());
		displayingMode.set(givenDisplayingMode);
		performanceOptimization.set(givenOptimizationMode);
		if (isRegistered.get()) {
			registerListener(givenDisplayingMode,givenOptimizationMode,requireExclusiveAccess.get(),iX);
		}
	}
	//
	@Override
	public KinectFrameWritableBaseAttributes createKinectFrameWritableBaseAttributes() {
		return device.get().createKinectFrameWritableBaseAttributes();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean getUseNoDevice() {
		return useNoDevice.get();
	}
	@Override
	public Kinect getDevice() {
		return device.get();
	}
	@Override
	public KinectFrameType[] getDataAcquisitionMode() {
		return dataAcquisitionMode.get();
	}
	@Override
	public KinectBufferInterface getBuffer() {
		return buffer.get();
	}
	@Override
	public boolean getRequireExclusiveAccess() {
		return requireExclusiveAccess.get();
	}
	@Override
	public KinectDisplayingModeInterface getDisplayingMode() {
		return displayingMode.get();
	}
	@Override
	public KinectPerformanceOptimization getPerformanceOptimization() {
		return performanceOptimization.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void setIsRegistered(boolean mode) {
		isRegistered.set(mode);
	}
	//
	@Override
	public boolean isRegistered() {
		return isRegistered.get();
	}
	@Override
	public boolean isNotRegistered() {
		return !isRegistered.get();
	}
	//
	@Override
	public void setIsSuspended(boolean mode) {
		isSuspended.set(mode);
	}
	//
	@Override
	public boolean isSuspended() {
		return isSuspended.get();
	}
	@Override
	public boolean isNotSuspended() {
		return !isSuspended.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void registerListener(KinectDisplayingModeInterface givenDisplayingMode, KinectPerformanceOptimization givenOptimizationMode, boolean accessMode, ChoisePoint iX) {
		if (!useNoDevice.get()) {
			displayingMode.set(givenDisplayingMode);
			performanceOptimization.set(givenOptimizationMode);
			requireExclusiveAccess.set(accessMode);
			isRegistered.set(true);
			try {
				device.get().registerListener(this,accessMode,iX);
			} catch (Throwable e) {
				isRegistered.set(false);
			}
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	@Override
	public void activate(ChoisePoint iX) {
		if (!useNoDevice.get()) {
			isSuspended.set(false);
			device.get().activate(iX);
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	//
	@Override
	public boolean isRegisteredListener(ChoisePoint iX) {
		if (!useNoDevice.get()) {
			if (isRegistered.get()) {
				try {
					return device.get().hasExclusiveAccess(this,iX);
				} catch (Throwable e) {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	//
	@Override
	public boolean hasExclusiveAccess(ChoisePoint iX) {
		if (!useNoDevice.get()) {
			if (isRegistered.get()) {
				try {
					return device.get().isRegisteredListener(this,iX);
				} catch (Throwable e) {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	//
	@Override
	public void cancelListener(ChoisePoint iX) {
		if (!useNoDevice.get()) {
			if (performanceOptimization.get()==KinectPerformanceOptimization.OPERATION_SPEED) {
				device.get().suspendListener(this,iX);
			} else {
				isRegistered.set(false);
				try {
					device.get().cancelListener(this,iX);
				} catch (Throwable e) {
					isRegistered.set(true);
				}
			}
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	//
	@Override
	public void suspendListener(ChoisePoint iX) {
		if (!useNoDevice.get()) {
			device.get().suspendListener(this,iX);
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
		if (dataAcquisitionMode.get() != null) {
			KinectDataAcquisitionModeTools.refineDataAcquisitionMode(dataAcquisitionMode.get(),consolidatedMode,displayingMode.get());
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	@Override
	public boolean requiresFrameType(KinectDataArrayType proposedFrameType) {
		if (dataAcquisitionMode.get() != null) {
			KinectFrameType actingFrameType= displayingMode.get().getActingFrameType();
			return KinectDataAcquisitionModeTools.requiresFrameType(dataAcquisitionMode.get(),proposedFrameType,actingFrameType);
		} else {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean requiresDepthFrames() {
		return requiresFrameType(KinectDataArrayType.DEPTH_FRAME);
	}
	@Override
	public boolean requiresInfraredFrames() {
		return requiresFrameType(KinectDataArrayType.INFRARED_FRAME);
	}
	@Override
	public boolean requiresLongExposureInfraredFrames() {
		return requiresFrameType(KinectDataArrayType.LONG_EXPOSURE_INFRARED_FRAME);
	}
	@Override
	public boolean requiresMappedColorFrames() {
		return requiresFrameType(KinectDataArrayType.MAPPED_COLOR_FRAME);
	}
	@Override
	public boolean requiresEntirePointClouds() {
		if (dataAcquisitionMode.get() != null) {
			KinectDisplayingModeInterface mode= displayingMode.get();
			KinectPeopleIndexMode actignPeopleIndexMode= mode.getActingPeopleIndexMode();
			if (!actignPeopleIndexMode.peopleAreToBeExtracted()) {
				KinectFrameType actingFrameType= mode.getActingFrameType();
				return KinectDataAcquisitionModeTools.requiresFrameType(dataAcquisitionMode.get(),KinectDataArrayType.POINT_CLOUDS_FRAME,actingFrameType);
			}
		};
		return false;
	}
	@Override
	public boolean requiresForegroundPointClouds() {
		if (dataAcquisitionMode.get() != null) {
			KinectDisplayingModeInterface mode= displayingMode.get();
			KinectPeopleIndexMode actignPeopleIndexMode= mode.getActingPeopleIndexMode();
			if (actignPeopleIndexMode.peopleAreToBeExtracted()) {
				KinectFrameType actingFrameType= mode.getActingFrameType();
				return KinectDataAcquisitionModeTools.requiresFrameType(dataAcquisitionMode.get(),KinectDataArrayType.POINT_CLOUDS_FRAME,actingFrameType);
			}
		};
		return false;
	}
	@Override
	public boolean requiresColorFrames() {
		return requiresFrameType(KinectDataArrayType.COLOR_FRAME);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void sendKinectFrame(KinectFrameInterface frame) {
		if (!useNoDevice.get()) {
			if (!isSuspended.get()) {
				buffer.get().sendKinectFrame(frame);
			}
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static KinectDevice argumentToKinectDevice(Term value, KinectBufferInterface buffer, ChoisePoint iX) {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_none) {
				return new KinectDevice(buffer);
			} else {
				throw new WrongArgumentIsNotKinectDevice(value);
			}
		} catch (TermIsNotASymbol e1) {
			try {
				long functor= value.getStructureFunctor(iX);
				if (functor==SymbolCodes.symbolCode_E_device) {
					Term[] arguments= value.getStructureArguments(iX);
					if (arguments.length == 2) {
						Term device= arguments[0];
						device= device.dereferenceValue(iX);
						if (device instanceof Kinect) {
							Kinect kinect= (Kinect)device;
							Term argumentMode= arguments[1];
							KinectFrameType[] mode= KinectDataAcquisitionModeConverters.argumentToKinectDataAcquisitionMode(argumentMode,iX);
							return new KinectDevice(kinect,mode,buffer);
						} else {
							throw new WrongArgumentIsNotKinectDevice(value);
						}
					} else {
						throw new WrongArgumentIsNotKinectDevice(value);
					}
				} else if (functor==SymbolCodes.symbolCode_E_auto) {
					Term[] arguments= value.getStructureArguments(iX);
					if (arguments.length == 1) {
						Term device= arguments[0];
						device= device.dereferenceValue(iX);
						if (device instanceof Kinect) {
							Kinect kinect= (Kinect)device;
							return new KinectDevice(kinect,new KinectFrameType[0],buffer);
						} else {
							throw new WrongArgumentIsNotKinectDevice(value);
						}
					} else {
						throw new WrongArgumentIsNotKinectDevice(value);
					}
				} else {
					throw new WrongArgumentIsNotKinectDevice(value);
				}
			} catch (TermIsNotAStructure e2) {
				throw new WrongArgumentIsNotKinectDevice(value);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected static Term termNone= new PrologSymbol(SymbolCodes.symbolCode_E_none);
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term toTerm() {
		if (useNoDevice.get()) {
			return termNone;
		} else {
			return new PrologStructure(SymbolCodes.symbolCode_E_device,new Term[]{device.get(),KinectDataAcquisitionModeConverters.toTerm(dataAcquisitionMode.get())});
		}
	}
}
