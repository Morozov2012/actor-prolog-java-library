// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import morozov.run.*;
import morozov.system.gui.space2d.*;
import morozov.terms.*;

public interface VideoProcessingMachineOperations {
	//
	public static int firstLayerNumber= 1;
	//
	public void process(java.awt.image.BufferedImage nativeImage, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes);
	public void process(byte[] bytes, long frameNumber, long timeInMilliseconds, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes);
	//
	public void commit(ChoisePoint iX);
	//
	public void resetSettings(ChoisePoint iX);
	public void resetStatistics(ChoisePoint iX);
	public void resetResults(ChoisePoint iX);
	public void resetAll(ChoisePoint iX);
	//
	public long getFrameNumber(ChoisePoint iX);
	public Term getFrameNumberOrSpacer(ChoisePoint iX);
	//
	public void getRecentImage(Term image, ChoisePoint iX);
	public byte[] getSerializedRecentImage(ChoisePoint iX);
	public void getPreprocessedImage(Term image, ChoisePoint iX);
	public byte[] getSerializedPreprocessedImage(ChoisePoint iX);
	public void getForegroundImage(Term image, ChoisePoint iX);
	public byte[] getSerializedForegroundImage(ChoisePoint iX);
	public void getSynthesizedImage(Term image, ChoisePoint iX);
	public byte[] getSerializedSynthesizedImage(ChoisePoint iX);
	//
	public void getBackgroundImage(Term image, int layerNumber, ChoisePoint iX);
	public byte[] getSerializedBackgroundImage(int layerNumber, ChoisePoint iX);
	//
	public void getSigmaImage(Term image, int layerNumber, ChoisePoint iX);
	public byte[] getSerializedSigmaImage(int layerNumber, ChoisePoint iX);
	//
	public Term getBlobs(ChoisePoint iX);
	public byte[] getSerializedBlobs(ChoisePoint iX);
	//
	public Term getTracks(ChoisePoint iX);
	public byte[] getSerializedTracks(ChoisePoint iX);
	//
	public Term getConnectedGraphs(ChoisePoint iX);
	public byte[] getSerializedConnectedGraphs(ChoisePoint iX);
	//
	public Term getChronicle(ChoisePoint iX);
	public byte[] getSerializedChronicle(ChoisePoint iX);
	//
	public double[] physicalCoordinates(int pixelX, int pixelY, ChoisePoint iX);
	public double characteristicLength(int x, int y, ChoisePoint iX);
	//
	public GenericImageEncodingAttributes getCurrentImageEncodingAttributes();
	//
	public Term toTerm();
}
