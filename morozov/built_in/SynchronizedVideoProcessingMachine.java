// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.errors.*;
import morozov.terms.*;
import morozov.terms.errors.*;
import morozov.worlds.*;
import morozov.worlds.remote.*;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

public abstract class SynchronizedVideoProcessingMachine
		extends GenericVideoProcessor
		implements VideoProcessingMachineOperations {
	//
	public AtomicReference<VideoProcessingMachineOperations> videoProcessingMachine= new AtomicReference<>(null);
	//
	///////////////////////////////////////////////////////////////
	//
	public SynchronizedVideoProcessingMachine() {
	}
	public SynchronizedVideoProcessingMachine(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_video_processing_machine();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set videoProcessingMachine
	//
	public void setVideoProcessingMachine1s(ChoisePoint iX, Term a1) {
		setVideoProcessingMachine(argumentToVideoProcessingMachine(a1,iX));
	}
	public void setVideoProcessingMachine(VideoProcessingMachineOperations value) {
		videoProcessingMachine.set(value);
	}
	public void getVideoProcessingMachine0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getVideoProcessingMachine(iX).toTerm());
	}
	public void getVideoProcessingMachine0fs(ChoisePoint iX) {
	}
	public VideoProcessingMachineOperations getVideoProcessingMachine(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= videoProcessingMachine.get();
		if (vpm != null) {
			return vpm;
		} else {
			Term value= getBuiltInSlot_E_video_processing_machine();
			return argumentToVideoProcessingMachine(value,iX);
		}
	}
	public VideoProcessingMachineOperations argumentToVideoProcessingMachine(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable()) {
			throw new WrongArgumentIsNotBoundVariable(value);
		} else if (value instanceof VideoProcessingMachine) {
			VideoProcessingMachineOperations vpm= (VideoProcessingMachineOperations)value;
			return vpm;
		} else if (value instanceof ForeignWorldWrapper) {
			ForeignWorldWrapper wrapper= (ForeignWorldWrapper)value;
			ForeignVideoProcessingMachine vpm= new ForeignVideoProcessingMachine(wrapper);
			return vpm;
		} else {
			throw new WrongArgumentIsNotVideoProcessingMachine(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void processNumberedFrame1s(ChoisePoint iX, Term a1) {
		processNumberedFrame(a1,null,true,iX);
	}
	@Override
	public void processNumberedFrame2s(ChoisePoint iX, Term a1, Term a2) {
		processNumberedFrame(a1,a2,true,iX);
	}
	@Override
	public void processNumberedFrame3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		boolean takeFrameIntoAccount= YesNoConverters.termYesNo2Boolean(a3,iX);
		processNumberedFrame(a1,a2,takeFrameIntoAccount,iX);
	}
	//
	@Override
	public void processRealtimeFrame1s(ChoisePoint iX, Term a1) {
		processRealtimeFrame(a1,null,true,iX);
	}
	@Override
	public void processRealtimeFrame2s(ChoisePoint iX, Term a1, Term a2) {
		processRealtimeFrame(a1,a2,true,iX);
	}
	@Override
	public void processRealtimeFrame3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		boolean takeFrameIntoAccount= YesNoConverters.termYesNo2Boolean(a3,iX);
		processRealtimeFrame(a1,a2,takeFrameIntoAccount,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void processNumberedFrame(Term a1, Term a2, boolean takeFrameIntoAccount, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		long frameNumber;
		if (a2 != null) {
			frameNumber= Arithmetic.toLong(GeneralConverters.argumentToRoundInteger(a2,iX));
		} else {
			recentFrameNumber++;
			frameNumber= recentFrameNumber;
		};
		GenericImageEncodingAttributes attributes= getCurrentImageEncodingAttributes();
		process(nativeImage,frameNumber,-1,takeFrameIntoAccount,iX,attributes);
	}
	@Override
	protected void processRealtimeFrame(Term a1, Term a2, boolean takeFrameIntoAccount, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		long timeInMilliseconds;
		if (a2 != null) {
			timeInMilliseconds= Arithmetic.toLong(GeneralConverters.argumentToRoundInteger(a2,iX));
		} else {
			Calendar calendar= Calendar.getInstance();
			timeInMilliseconds= calendar.getTimeInMillis();
		};
		recentFrameNumber++;
		GenericImageEncodingAttributes attributes= getCurrentImageEncodingAttributes();
		process(nativeImage,recentFrameNumber,timeInMilliseconds,takeFrameIntoAccount,iX,attributes);
	}
	//
	@Override
	public void process(java.awt.image.BufferedImage nativeImage, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.process(nativeImage,frameNumber,timeInMilliseconds,takeFrameIntoAccount,iX,attributes);
	}
	@Override
	public void process(byte[] image, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.process(image,frameNumber,timeInMilliseconds,takeFrameIntoAccount,iX,attributes);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void commit(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.commit(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void resetSettings(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.resetSettings(iX);
	}
	//
	@Override
	public void resetStatistics(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.resetStatistics(iX);
	}
	//
	@Override
	public void resetResults(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.resetResults(iX);
	}
	//
	@Override
	public void resetAll(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.resetAll(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term getFrameNumberOrSpacer(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getFrameNumberOrSpacer(iX);
	}
	@Override
	public long getFrameNumber(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getFrameNumber(iX);
	}
	//
	@Override
	public Term getFrameTimeOrSpacer(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getFrameTimeOrSpacer(iX);
	}
	@Override
	public long getFrameTime(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getFrameTime(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void getRecentImage(Term image, ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.getRecentImage(image,iX);
	}
	@Override
	public byte[] getSerializedRecentImage(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getSerializedRecentImage(iX);
	}
	//
	@Override
	public void getPreprocessedImage(Term image, ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.getPreprocessedImage(image,iX);
	}
	@Override
	public byte[] getSerializedPreprocessedImage(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getSerializedPreprocessedImage(iX);
	}
	//
	@Override
	public void getForegroundImage(Term image, ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.getForegroundImage(image,iX);
	}
	@Override
	public byte[] getSerializedForegroundImage(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getSerializedForegroundImage(iX);
	}
	//
	@Override
	public void getSynthesizedImage(Term image, ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.getSynthesizedImage(image,iX);
	}
	@Override
	public byte[] getSerializedSynthesizedImage(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getSerializedSynthesizedImage(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getBackgroundImage2s(ChoisePoint iX, Term a1, Term a2) {
		int layerNumber= GeneralConverters.argumentToSmallRoundInteger(a2,iX);
		getBackgroundImage(a1,layerNumber,iX);
	}
	@Override
	public void getBackgroundImage(Term image, int layerNumber, ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.getBackgroundImage(image,layerNumber,iX);
	}
	@Override
	public byte[] getSerializedBackgroundImage(int layerNumber, ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getSerializedBackgroundImage(layerNumber,iX);
	}
	//
	public void getSigmaImage2s(ChoisePoint iX, Term a1, Term a2) {
		int layerNumber= GeneralConverters.argumentToSmallRoundInteger(a2,iX);
		getSigmaImage(a1,layerNumber,iX);
	}
	@Override
	public void getSigmaImage(Term image, int layerNumber, ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		vpm.getSigmaImage(image,layerNumber,iX);
	}
	@Override
	public byte[] getSerializedSigmaImage(int layerNumber, ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getSerializedSigmaImage(layerNumber,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term getBlobs(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getBlobs(iX);
	}
	@Override
	public byte[] getSerializedBlobs(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getSerializedBlobs(iX);
	}
	//
	@Override
	public Term getTracks(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getTracks(iX);
	}
	@Override
	public byte[] getSerializedTracks(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getSerializedTracks(iX);
	}
	//
	@Override
	public Term getChronicle(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getChronicle(iX);
	}
	@Override
	public byte[] getSerializedChronicle(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getSerializedChronicle(iX);
	}
	//
	@Override
	public Term getConnectedGraphs(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getConnectedGraphs(iX);
	}
	@Override
	public byte[] getSerializedConnectedGraphs(ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.getSerializedConnectedGraphs(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public double[] physicalCoordinates(int pixelX, int pixelY, ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.physicalCoordinates(pixelX,pixelY,iX);
	}
	//
	@Override
	public double characteristicLength(int x, int y, ChoisePoint iX) {
		VideoProcessingMachineOperations vpm= getVideoProcessingMachine(iX);
		return vpm.characteristicLength(x,y,iX);
	}
}
