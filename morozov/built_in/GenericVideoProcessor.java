// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.gui.space2d.*;
import morozov.system.vision.vpm.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.util.Calendar;

public abstract class GenericVideoProcessor extends BufferedImageController {
	//
	protected long recentFrameNumber= 0;
	//
	///////////////////////////////////////////////////////////////
	//
	public GenericVideoProcessor() {
	}
	public GenericVideoProcessor(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void processNumberedFrame1s(ChoisePoint iX, Term a1) {
		processNumberedFrame(a1,null,true,iX);
	}
	public void processNumberedFrame2s(ChoisePoint iX, Term a1, Term a2) {
		processNumberedFrame(a1,a2,true,iX);
	}
	public void processNumberedFrame3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		boolean takeFrameIntoAccount= YesNo.termYesNo2Boolean(a3,iX);
		processNumberedFrame(a1,a2,takeFrameIntoAccount,iX);
	}
	//
	public void processRealtimeFrame1s(ChoisePoint iX, Term a1) {
		processRealtimeFrame(a1,null,true,iX);
	}
	public void processRealtimeFrame2s(ChoisePoint iX, Term a1, Term a2) {
		processRealtimeFrame(a1,a2,true,iX);
	}
	public void processRealtimeFrame3s(ChoisePoint iX, Term a1, Term a2, Term a3) {
		boolean takeFrameIntoAccount= YesNo.termYesNo2Boolean(a3,iX);
		processRealtimeFrame(a1,a2,takeFrameIntoAccount,iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void processNumberedFrame(Term a1, Term a2, boolean takeFrameIntoAccount, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		long frameNumber;
		if (a2 != null) {
			frameNumber= PrologInteger.toLong(GeneralConverters.argumentToRoundInteger(a2,iX));
		} else {
			recentFrameNumber++;
			frameNumber= recentFrameNumber;
		};
		GenericImageEncodingAttributes attributes= getCurrentImageEncodingAttributes();
		process(nativeImage,frameNumber,-1,takeFrameIntoAccount,iX,attributes);
	}
	protected void processRealtimeFrame(Term a1, Term a2, boolean takeFrameIntoAccount, ChoisePoint iX) {
		java.awt.image.BufferedImage nativeImage= acquireNativeImage(a1,iX);
		long timeInMilliseconds;
		if (a2 != null) {
			timeInMilliseconds= PrologInteger.toLong(GeneralConverters.argumentToRoundInteger(a2,iX));
		} else {
			Calendar calendar= Calendar.getInstance();
			timeInMilliseconds= calendar.getTimeInMillis();
		};
		recentFrameNumber++;
		GenericImageEncodingAttributes attributes= getCurrentImageEncodingAttributes();
		process(nativeImage,recentFrameNumber,timeInMilliseconds,takeFrameIntoAccount,iX,attributes);
	}
	abstract public void process(java.awt.image.BufferedImage nativeImage, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes);
	abstract public void process(byte[] bytes, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes);
	//
	///////////////////////////////////////////////////////////////
	//
	public void commit0s(ChoisePoint iX) {
		commit(iX);
	}
	abstract public void commit(ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public void resetSettings0s(ChoisePoint iX) {
		resetSettings(iX);
	}
	abstract public void resetSettings(ChoisePoint iX);
	//
	public void resetStatistics0s(ChoisePoint iX) {
		recentFrameNumber= 0;
		resetStatistics(iX);
	}
	abstract public void resetStatistics(ChoisePoint iX);
	//
	public void resetResults0s(ChoisePoint iX) {
		resetResults(iX);
	}
	abstract public void resetResults(ChoisePoint iX);
	//
	public void resetAll0s(ChoisePoint iX) {
		recentFrameNumber= 0;
		resetAll(iX);
	}
	abstract public void resetAll(ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentFrameNumber1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(getFrameNumberOrSpacer(iX),iX);
	}
	abstract public Term getFrameNumberOrSpacer(ChoisePoint iX);
	//
	public void getRecentFrameTime1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(getFrameTimeOrSpacer(iX),iX);
	}
	abstract public Term getFrameTimeOrSpacer(ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public void getRecentImage1s(ChoisePoint iX, Term image) {
		getRecentImage(image,iX);
	}
	abstract public void getRecentImage(Term image, ChoisePoint iX);
	abstract public byte[] getSerializedRecentImage(ChoisePoint iX);
	//
	public void getPreprocessedImage1s(ChoisePoint iX, Term image) {
		getPreprocessedImage(image,iX);
	}
	abstract public void getPreprocessedImage(Term image, ChoisePoint iX);
	abstract public byte[] getSerializedPreprocessedImage(ChoisePoint iX);
	//
	public void getForegroundImage1s(ChoisePoint iX, Term image) {
		getForegroundImage(image,iX);
	}
	abstract public void getForegroundImage(Term image, ChoisePoint iX);
	abstract public byte[] getSerializedForegroundImage(ChoisePoint iX);
	//
	public void getSynthesizedImage1s(ChoisePoint iX, Term image) {
		getSynthesizedImage(image,iX);
	}
	abstract public void getSynthesizedImage(Term image, ChoisePoint iX);
	abstract public byte[] getSerializedSynthesizedImage(ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public void getBackgroundImage1s(ChoisePoint iX, Term image) {
		getBackgroundImage(image,VideoProcessingMachineOperations.firstLayerNumber,iX);
	}
	abstract public void getBackgroundImage(Term image, int layerNumber, ChoisePoint iX);
	abstract public byte[] getSerializedBackgroundImage(int layerNumber, ChoisePoint iX);
	//
	public void getSigmaImage1s(ChoisePoint iX, Term image) {
		getSigmaImage(image,VideoProcessingMachineOperations.firstLayerNumber,iX);
	}
	abstract public void getSigmaImage(Term image, int layerNumber, ChoisePoint iX);
	abstract public byte[] getSerializedSigmaImage(int layerNumber, ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public void getBlobs1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(getBlobs(iX),iX);
	}
	abstract public Term getBlobs(ChoisePoint iX);
	abstract public byte[] getSerializedBlobs(ChoisePoint iX);
	//
	public void getTracks1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(getTracks(iX),iX);
	}
	abstract public Term getTracks(ChoisePoint iX);
	abstract public byte[] getSerializedTracks(ChoisePoint iX);
	//
	public void getChronicle1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(getChronicle(iX),iX);
	}
	abstract public Term getChronicle(ChoisePoint iX);
	abstract public byte[] getSerializedChronicle(ChoisePoint iX);
	//
	public void getConnectedGraphs1s(ChoisePoint iX, PrologVariable a1) {
		a1.setBacktrackableValue(getConnectedGraphs(iX),iX);
	}
	abstract public Term getConnectedGraphs(ChoisePoint iX);
	abstract public byte[] getSerializedConnectedGraphs(ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public void physicalCoordinates4s(ChoisePoint iX, Term a1, Term a2, Term a3, Term a4) {
		int pixelX= GeneralConverters.argumentToSmallRoundInteger(a1,iX);
		int pixelY= GeneralConverters.argumentToSmallRoundInteger(a2,iX);
		double[] coordinates= physicalCoordinates(pixelX,pixelY,iX);
		a3.setBacktrackableValue(new PrologReal(coordinates[0]),iX);
		a4.setBacktrackableValue(new PrologReal(coordinates[1]),iX);
	}
	abstract public double[] physicalCoordinates(int pixelX, int pixelY, ChoisePoint iX);
	//
	public void characteristicLength2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		int x= GeneralConverters.argumentToSmallRoundInteger(a1,iX);
		int y= GeneralConverters.argumentToSmallRoundInteger(a2,iX);
		result.setNonBacktrackableValue(new PrologReal(characteristicLength(x,y,iX)));
	}
	public void characteristicLength2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	abstract public double characteristicLength(int x, int y, ChoisePoint iX);
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		return this;
	}
}
