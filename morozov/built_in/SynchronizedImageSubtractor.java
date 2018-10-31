// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.plain.errors.*;
import morozov.system.vision.vpm.*;
import morozov.terms.*;
import morozov.terms.errors.*;
import morozov.worlds.*;
import morozov.worlds.remote.*;

import java.util.concurrent.atomic.AtomicReference;

public abstract class SynchronizedImageSubtractor
		extends GenericVideoProcessor
		implements VideoProcessingMachineOperations {
	//
	public AtomicReference<VideoProcessingMachineOperations> imageSubtractor= new AtomicReference<>(null);
	//
	public SynchronizedImageSubtractor() {
	}
	public SynchronizedImageSubtractor(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	abstract public Term getBuiltInSlot_E_image_subtractor();
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set imageSubtractor
	//
	public void setImageSubtractor1s(ChoisePoint iX, Term a1) {
		setImageSubtractor(argumentToImageSubtractor(a1,iX));
	}
	public void setImageSubtractor(VideoProcessingMachineOperations value) {
		imageSubtractor.set(value);
	}
	public void getImageSubtractor0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getImageSubtractor(iX).toTerm());
	}
	public void getImageSubtractor0fs(ChoisePoint iX) {
	}
	public VideoProcessingMachineOperations getImageSubtractor(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= imageSubtractor.get();
		if (subtractor != null) {
			return subtractor;
		} else {
			Term value= getBuiltInSlot_E_image_subtractor();
			return argumentToImageSubtractor(value,iX);
		}
	}
	public VideoProcessingMachineOperations argumentToImageSubtractor(Term value, ChoisePoint iX) {
		value= value.dereferenceValue(iX);
		if (value.thisIsFreeVariable()) {
			throw new WrongArgumentIsNotBoundVariable(value);
		} else if (value instanceof ImageSubtractor) {
			VideoProcessingMachineOperations subtractor= (VideoProcessingMachineOperations)value;
			return subtractor;
		} else if (value instanceof ForeignWorldWrapper) {
			ForeignWorldWrapper wrapper= (ForeignWorldWrapper)value;
			ForeignVideoProcessingMachine subtractor= new ForeignVideoProcessingMachine(wrapper);
			return subtractor;
		} else {
			throw new WrongArgumentIsNotImageSubtractor(value);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void subtract2s(ChoisePoint iX, Term a1, Term a2) {
		subtract(a1,a2,true,iX);
	}
	public void subtract3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		boolean takeFrameIntoAccount= YesNo.termYesNo2Boolean(a3,iX);
		subtract(a1,a2,takeFrameIntoAccount,iX);
	}
	//
	protected void subtract(Term frame, Term image, boolean takeFrameIntoAccount, ChoisePoint iX) {
		createImageSubtractorIfNecessary(iX);
		long frameNumber= PrologInteger.toLong(GeneralConverters.argumentToRoundInteger(frame,iX));
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(image,iX);
		GenericImageEncodingAttributes attributes= getCurrentImageEncodingAttributes();
		process(nativeImage,frameNumber,-1,takeFrameIntoAccount,iX,attributes);
	}
	// abstract public void subtract(long frameNumber, java.awt.image.BufferedImage nativeImage, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes);
	//
	public void process(java.awt.image.BufferedImage nativeImage, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.process(nativeImage,frameNumber,timeInMilliseconds,takeFrameIntoAccount,iX,attributes);
	}
	public void process(byte[] image, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.process(image,frameNumber,timeInMilliseconds,takeFrameIntoAccount,iX,attributes);
	}
	//
	public boolean createImageSubtractorIfNecessary(ChoisePoint iX) {
		GenericImageEncodingAttributes attributes= getImageEncodingAttributes(iX);
		setCurrentImageEncodingAttributes(attributes);
		return false;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.commit(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void resetSettings(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.resetSettings(iX);
	}
	//
	public void resetStatistics(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.resetStatistics(iX);
	}
	//
	public void resetResults(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.resetResults(iX);
	}
	//
	public void resetAll(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.resetAll(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getFrameNumber(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getFrameNumber(iX);
	}
	public Term getFrameNumberOrSpacer(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getFrameNumberOrSpacer(iX);
	}
	//
	public long getFrameTime(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getFrameTime(iX);
	}
	public Term getFrameTimeOrSpacer(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getFrameTimeOrSpacer(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentImage(Term image, ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.getRecentImage(image,iX);
	}
	public byte[] getSerializedRecentImage(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedRecentImage(iX);
	}
	//
	public void getPreprocessedImage(Term image, ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.getPreprocessedImage(image,iX);
	}
	public byte[] getSerializedPreprocessedImage(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedPreprocessedImage(iX);
	}
	//
	public void getForegroundImage(Term image, ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.getForegroundImage(image,iX);
	}
	public byte[] getSerializedForegroundImage(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedForegroundImage(iX);
	}
	//
	public void getSynthesizedImage(Term image, ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.getSynthesizedImage(image,iX);
	}
	public byte[] getSerializedSynthesizedImage(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedSynthesizedImage(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void getBackgroundImage(Term image, int layerNumber, ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.getBackgroundImage(image,layerNumber,iX);
	}
	public byte[] getSerializedBackgroundImage(int layerNumber, ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedBackgroundImage(layerNumber,iX);
	}
	//
	public void getSigmaImage(Term image, int layerNumber, ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		subtractor.getSigmaImage(image,layerNumber,iX);
	}
	public byte[] getSerializedSigmaImage(int layerNumber, ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedSigmaImage(layerNumber,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getBlobs(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getBlobs(iX);
	}
	public byte[] getSerializedBlobs(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedBlobs(iX);
	}
	//
	public Term getTracks(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getTracks(iX);
	}
	public byte[] getSerializedTracks(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedTracks(iX);
	}
	//
	public Term getChronicle(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getChronicle(iX);
	}
	public byte[] getSerializedChronicle(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedChronicle(iX);
	}
	//
	public Term getConnectedGraphs(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getConnectedGraphs(iX);
	}
	public byte[] getSerializedConnectedGraphs(ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.getSerializedConnectedGraphs(iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double[] physicalCoordinates(int pixelX, int pixelY, ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.physicalCoordinates(pixelX,pixelY,iX);
	}
	//
	public double characteristicLength(int x, int y, ChoisePoint iX) {
		VideoProcessingMachineOperations subtractor= getImageSubtractor(iX);
		return subtractor.characteristicLength(x,y,iX);
	}
}
