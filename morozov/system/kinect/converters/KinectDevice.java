// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.converters;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.kinect.converters.errors.*;
import morozov.system.kinect.converters.interfaces.*;
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
	///////////////////////////////////////////////////////////////
	//
	public boolean getUseNoDevice() {
		return useNoDevice.get();
	}
	public Kinect getDevice() {
		return device.get();
	}
	public KinectFrameType[] getDataAcquisitionMode() {
		return dataAcquisitionMode.get();
	}
	public KinectBufferInterface getBuffer() {
		return buffer.get();
	}
	public boolean getRequireExclusiveAccess() {
		return requireExclusiveAccess.get();
	}
	public KinectDisplayingModeInterface getDisplayingMode() {
		return displayingMode.get();
	}
	public KinectPerformanceOptimization getPerformanceOptimization() {
		return performanceOptimization.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setIsRegistered(boolean mode) {
		isRegistered.set(mode);
	}
	//
	public boolean isRegistered() {
		return isRegistered.get();
	}
	public boolean isNotRegistered() {
		return !isRegistered.get();
	}
	//
	public void setIsSuspended(boolean mode) {
		isSuspended.set(mode);
	}
	//
	public boolean isSuspended() {
		return isSuspended.get();
	}
	public boolean isNotSuspended() {
		return !isSuspended.get();
	}
	//
	///////////////////////////////////////////////////////////////
	//
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
	public void activate(ChoisePoint iX) {
		if (!useNoDevice.get()) {
			isSuspended.set(false);
			device.get().activate(iX);
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
	//
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
	public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode) {
		if (dataAcquisitionMode.get() != null) {
			KinectDataAcquisitionModeTools.refineDataAcquisitionMode(dataAcquisitionMode.get(),consolidatedMode,displayingMode.get());
		} else {
			throw new InputDeviceIsNotDefined();
		}
	}
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
	public void sendFrame(KinectFrameInterface frame) {
		if (!useNoDevice.get()) {
			if (!isSuspended.get()) {
				buffer.get().sendFrame(frame);
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
	public Term toTerm() {
		if (useNoDevice.get()) {
			return termNone;
		} else {
			return new PrologStructure(SymbolCodes.symbolCode_E_device,new Term[]{device.get(),KinectDataAcquisitionModeConverters.toTerm(dataAcquisitionMode.get())});
		}
	}
}