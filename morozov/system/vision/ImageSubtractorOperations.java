// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import morozov.run.*;
import morozov.system.gui.space2d.*;
import morozov.terms.*;

public interface ImageSubtractorOperations {
	public void setBlobExtractionMode(boolean mode, ChoisePoint iX);
	public boolean getBlobExtractionMode(ChoisePoint iX);
	//
	public void setBlobTracingMode(boolean mode, ChoisePoint iX);
	public boolean getBlobTracingMode(ChoisePoint iX);
	//
	public void setMinimalTrainingInterval(int frames, ChoisePoint iX);
	public int getMinimalTrainingInterval(ChoisePoint iX);
	//
	public void setMaximalTrainingInterval(int frames, ChoisePoint iX);
	public int getMaximalTrainingInterval(ChoisePoint iX);
	//
	public void setGrayscaleMode(boolean mode, ChoisePoint iX);
	public boolean getGrayscaleMode(ChoisePoint iX);
	//
	public void setBackgroundGaussianFilteringMode(boolean mode, ChoisePoint iX);
	public boolean getBackgroundGaussianFilteringMode(ChoisePoint iX);
	//
	public void setBackgroundGaussianFilterRadius(int radius, ChoisePoint iX);
	public int getBackgroundGaussianFilterRadius(ChoisePoint iX);
	//
	public void setBackgroundRankFilteringMode(boolean mode, ChoisePoint iX);
	public boolean getBackgroundRankFilteringMode(ChoisePoint iX);
	//
	public void setBackgroundRankFilterThreshold(int threshold, ChoisePoint iX);
	public int getBackgroundRankFilterThreshold(ChoisePoint iX);
	//
	public void setBackgroundStandardDeviationFactor(double factor, ChoisePoint iX);
	public double getBackgroundStandardDeviationFactor(ChoisePoint iX);
	//
	public void setForegroundContouringMode(boolean mode, ChoisePoint iX);
	public boolean getForegroundContouringMode(ChoisePoint iX);
	//
	public void setR2WindowHalfwidth(int halfwidth, ChoisePoint iX);
	public int getR2WindowHalfwidth(ChoisePoint iX);
	//
	public void setHorizontalBlobBorder(int size, ChoisePoint iX);
	public int getHorizontalBlobBorder(ChoisePoint iX);
	//
	public void setVerticalBlobBorder(int size, ChoisePoint iX);
	public int getVerticalBlobBorder(ChoisePoint iX);
	//
	public void setHorizontalExtraBorderCoefficient(double coefficient, ChoisePoint iX);
	public double getHorizontalExtraBorderCoefficient(ChoisePoint iX);
	//
	public void setVerticalExtraBorderCoefficient(double coefficient, ChoisePoint iX);
	public double getVerticalExtraBorderCoefficient(ChoisePoint iX);
	//
	public void setMinimalBlobIntersectionArea(int size, ChoisePoint iX);
	public int getMinimalBlobIntersectionArea(ChoisePoint iX);
	//
	public void setMinimalBlobSize(int size, ChoisePoint iX);
	public int getMinimalBlobSize(ChoisePoint iX);
	//
	public void setMinimalTrackDuration(int frames, ChoisePoint iX);
	public int getMinimalTrackDuration(ChoisePoint iX);
	//
	public void setMaximalBlobInvisibilityInterval(int frames, ChoisePoint iX);
	public int getMaximalBlobInvisibilityInterval(ChoisePoint iX);
	//
	public void setMaximalTrackRetentionInterval(int frames, ChoisePoint iX);
	public int getMaximalTrackRetentionInterval(ChoisePoint iX);
	//
	public void setInverseTransformationMatrix(double[][] matrix, ChoisePoint iX);
	public void setSerializedInverseTransformationMatrix(byte[] matrix, ChoisePoint iX);
	public double[][] getInverseTransformationMatrix(ChoisePoint iX);
	public byte[] getSerializedInverseTransformationMatrix(ChoisePoint iX);
	//
	public void setSamplingRate(double rate, ChoisePoint iX);
	public double getSamplingRate(ChoisePoint iX);
	//
	public void setCharacteristicLengthMedianFilteringMode(boolean mode, ChoisePoint iX);
	public boolean getCharacteristicLengthMedianFilteringMode(ChoisePoint iX);
	//
	public void setCharacteristicLengthMedianFilterHalfwidth(int halfwidth, ChoisePoint iX);
	public int getCharacteristicLengthMedianFilterHalfwidth(ChoisePoint iX);
	//
	public void setVelocityMedianFilteringMode(boolean mode, ChoisePoint iX);
	public boolean getVelocityMedianFilteringMode(ChoisePoint iX);
	//
	public void setVelocityMedianFilterHalfwidth(int halfwidth, ChoisePoint iX);
	public int getVelocityMedianFilterHalfwidth(ChoisePoint iX);
	//
	public void setSlowTracksDeletionMode(boolean mode, ChoisePoint iX);
	public boolean getSlowTracksDeletionMode(ChoisePoint iX);
	//
	public void setFuzzyVelocityThreshold(double threshold, ChoisePoint iX);
	public double getFuzzyVelocityThreshold(ChoisePoint iX);
	//
	public void setFuzzyDistanceThreshold(double threshold, ChoisePoint iX);
	public double getFuzzyDistanceThreshold(ChoisePoint iX);
	//
	public void setFuzzyThresholdBorder(double threshold, ChoisePoint iX);
	public double getFuzzyThresholdBorder(ChoisePoint iX);
	//
	public void setSynthesizedImageTransparency(int transparency, ChoisePoint iX);
	public int getSynthesizedImageTransparency(ChoisePoint iX);
	//
	public void setSynthesizedImageRectangularBlobsMode(boolean mode, ChoisePoint iX);
	public boolean getSynthesizedImageRectangularBlobsMode(ChoisePoint iX);
	//
	public void subtract(long frame, java.awt.image.BufferedImage nativeImage, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes);
	public void subtract(long frame, byte[] image, boolean takeFrameIntoAccount, ChoisePoint iX, GenericImageEncodingAttributes attributes);
	//
	public void commit(ChoisePoint iX);
	//
	public void resetSettings(ChoisePoint iX);
	//
	public void resetStatistics(ChoisePoint iX);
	//
	public void resetResults(ChoisePoint iX);
	//
	public void resetAll(ChoisePoint iX);
	//
	public long getFrameNumber(ChoisePoint iX);
	public Term getFrameNumberOrSpacer(ChoisePoint iX);
	//
	public void getRecentImage(Term image, ChoisePoint iX);
	public byte[] getSerializedRecentImage(ChoisePoint iX);
	public void getBackgroundImage(Term image, ChoisePoint iX);
	public byte[] getSerializedBackgroundImage(ChoisePoint iX);
	public void getSigmaImage(Term image, ChoisePoint iX);
	public byte[] getSerializedSigmaImage(ChoisePoint iX);
	public void getForegroundImage(Term image, ChoisePoint iX);
	public byte[] getSerializedForegroundImage(ChoisePoint iX);
	public void getSynthesizedImage(Term image, ChoisePoint iX);
	public byte[] getSerializedSynthesizedImage(ChoisePoint iX);
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
	public GenericImageEncodingAttributes getCurrentImageEncodingAttributes();
	//
	public Term toTerm();
}
