// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.converters.interfaces;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.interfaces.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.interfaces.*;
import morozov.terms.*;

public interface KinectDeviceInterface {
	//
	public void update(KinectDeviceInterface newAttributes, KinectDisplayingModeInterface displayingMode, KinectPerformanceOptimization optimizationMode, ChoisePoint iX);
	public KinectFrameWritableBaseAttributes createKinectFrameWritableBaseAttributes();
	//
	public boolean getUseNoDevice();
	public Kinect getDevice();
	public KinectFrameType[] getDataAcquisitionMode();
	public KinectBufferInterface getBuffer();
	public boolean getRequireExclusiveAccess();
	//
	public KinectDisplayingModeInterface getDisplayingMode();
	public KinectPerformanceOptimization getPerformanceOptimization();
	//
	public void setIsRegistered(boolean mode);
	public boolean isRegistered();
	public boolean isNotRegistered();
	//
	public void setIsSuspended(boolean mode);
	public boolean isSuspended();
	public boolean isNotSuspended();
	//
	public void registerListener(KinectDisplayingModeInterface mode, KinectPerformanceOptimization optimizationMode, boolean requireExclusiveAccess, ChoisePoint iX);
	public void activate(ChoisePoint iX);
	public boolean isRegisteredListener(ChoisePoint iX);
	public boolean hasExclusiveAccess(ChoisePoint iX);
	public void cancelListener(ChoisePoint iX);
	public void suspendListener(ChoisePoint iX);
	//
	public void refineDataAcquisitionMode(ConsolidatedDataAcquisitionModeInterface consolidatedMode);
	public boolean requiresFrameType(KinectDataArrayType proposedFrameType);
	//
	public void sendKinectFrame(KinectFrameInterface frame);
	//
	public Term toTerm();
}
