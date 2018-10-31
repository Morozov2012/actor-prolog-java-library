// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import morozov.system.*;
import morozov.system.vision.vpm.commands.blb.*;
import morozov.system.vision.vpm.converters.*;
import morozov.system.vision.vpm.errors.*;
import morozov.terms.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.math.BigInteger;

abstract public class GenericVideoProcessingMachine implements BlobHolder {
	//
	protected int initialMinimalTrainingInterval= -1;
	protected int initialMaximalTrainingInterval= -1;
	protected int activeMinimalTrainingInterval= initialMinimalTrainingInterval;
	protected int activeMaximalTrainingInterval= initialMaximalTrainingInterval;
	//
	protected int initialHorizontalBlobBorder= 3;
	protected int initialVerticalBlobBorder= 3;
	protected int activeHorizontalBlobBorder= initialHorizontalBlobBorder;
	protected int activeVerticalBlobBorder= initialVerticalBlobBorder;
	//
	protected double initialHorizontalExtraBorderCoefficient= 0.10;
	protected double initialVerticalExtraBorderCoefficient= 0.10;
	protected double activeHorizontalExtraBorderCoefficient= initialHorizontalExtraBorderCoefficient;
	protected double activeVerticalExtraBorderCoefficient= initialVerticalExtraBorderCoefficient;
	//
	protected int initialMinimalBlobIntersectionArea= 1;
	protected int activeMinimalBlobIntersectionArea= initialMinimalBlobIntersectionArea;
	//
	protected int initialMinimalBlobSize= 10;
	protected int activeMinimalBlobSize= initialMinimalBlobSize;
	//
	protected int initialMinimalTrackDuration= 30;
	protected int initialMaximalTrackDuration= -1;
	protected int activeMinimalTrackDuration= initialMinimalTrackDuration;
	protected int activeMaximalTrackDuration= initialMaximalTrackDuration;
	//
	protected NumericalValue initialMaximalChronicleLength= null;
	protected NumericalValue activeMaximalChronicleLength= initialMaximalChronicleLength;
	//
	protected int initialMaximalBlobInvisibilityInterval= 3;
	protected int initialMaximalTrackRetentionInterval= 10000;
	protected int activeMaximalBlobInvisibilityInterval= initialMaximalBlobInvisibilityInterval;
	protected int activeMaximalTrackRetentionInterval= initialMaximalTrackRetentionInterval;
	//
	protected double[][] initialInverseTransformationMatrix= null;
	protected double initialSamplingRate= -1;
	protected double[][] activeInverseTransformationMatrix= initialInverseTransformationMatrix;
	protected double activeSamplingRate= initialSamplingRate;
	//
	protected TransformationMatrices transformationMatrices= null;
	//
	protected int initialR2WindowHalfwidth= 5;
	protected int activeR2WindowHalfwidth= initialR2WindowHalfwidth;
	//
	protected boolean initialApplyCharacteristicLengthMedianFiltering= true;
	protected int initialCharacteristicLengthMedianFilterHalfwidth= 5;
	protected boolean activeApplyCharacteristicLengthMedianFiltering= initialApplyCharacteristicLengthMedianFiltering;
	protected int activeCharacteristicLengthMedianFilterHalfwidth= initialCharacteristicLengthMedianFilterHalfwidth;
	//
	protected boolean initialApplyVelocityMedianFiltering= true;
	protected int initialVelocityMedianFilterHalfwidth= 5;
	protected boolean activeApplyVelocityMedianFiltering= initialApplyVelocityMedianFiltering;
	protected int activeVelocityMedianFilterHalfwidth= initialVelocityMedianFilterHalfwidth;
	//
	protected int initialSynthesizedImageTransparency= 64;
	protected boolean initialMakeRectangularBlobsInSynthesizedImage= true;
	protected int activeSynthesizedImageTransparency= initialSynthesizedImageTransparency;
	protected boolean activeMakeRectangularBlobsInSynthesizedImage= initialMakeRectangularBlobsInSynthesizedImage;
	//
	///////////////////////////////////////////////////////////////
	//
	protected long recentFrameNumber;
	protected long recentTimeInMilliseconds;
	protected boolean takeRecentImageIntoAccount;
	//
	protected ArrayList<VPM_FrameNumberAndTime> arrayOfTime= new ArrayList<>();
	//
	protected BlobType[] currentBlobTypes;
	protected BlobAttributes[] currentBlobAttributes;
	//
	protected java.awt.image.BufferedImage recentImage;
	protected java.awt.image.BufferedImage preprocessedImage;
	protected int operationalImageWidth= -1;
	protected int operationalImageHeight= -1;
	protected int operationalVectorLength= -1;
	//
	protected boolean[] foregroundMask;
	protected int foregroundMaskWidth;
	protected int foregroundMaskHeight;
	//
	protected AtomicReference<GenericVideoProcessingMachineSnapshot> recentSnapshot= new AtomicReference<>();
	protected HashMap<BigInteger,GrowingTrack> tracks= new HashMap<>();
	//
	///////////////////////////////////////////////////////////////
	//
	public GenericVideoProcessingMachine(
			int minimalTrainingIntervalValue,
			int maximalTrainingIntervalValue,
			int horizontalBlobBorderValue,
			int verticalBlobBorderValue,
			double horizontalExtraBorderCoefficientValue,
			double verticalExtraBorderCoefficientValue,
			int minimalBlobIntersectionAreaValue,
			int minimalBlobSizeValue,
			int minimalTrackDurationValue,
			int maximalTrackDurationValue,
			NumericalValue maximalChronicleLengthValue,
			int maximalBlobInvisibilityIntervalValue,
			int maximalTrackRetentionIntervalValue,
			double[][] iMatrix,
			double rate,
			int r2WindowHalfwidthValue,
			boolean applyFilterToCharacteristicLength,
			int characteristicLengthFilterHalfwidth,
			boolean applyFilterToVelocity,
			int velocityFilterHalfwidth,
			int synthesizedImageTransparencyValue,
			boolean makeRectangularBlobsInSynthesizedImageValue) {
		initialMinimalTrainingInterval= minimalTrainingIntervalValue;
		initialMaximalTrainingInterval= maximalTrainingIntervalValue;
		initialHorizontalBlobBorder= horizontalBlobBorderValue;
		initialVerticalBlobBorder= verticalBlobBorderValue;
		initialHorizontalExtraBorderCoefficient= horizontalExtraBorderCoefficientValue;
		initialVerticalExtraBorderCoefficient= verticalExtraBorderCoefficientValue;
		initialMinimalBlobIntersectionArea= minimalBlobIntersectionAreaValue;
		initialMinimalBlobSize= minimalBlobSizeValue;
		initialMinimalTrackDuration= minimalTrackDurationValue;
		initialMaximalTrackDuration= maximalTrackDurationValue;
		initialMaximalChronicleLength= maximalChronicleLengthValue;
		initialMaximalBlobInvisibilityInterval= maximalBlobInvisibilityIntervalValue;
		initialMaximalTrackRetentionInterval= maximalTrackRetentionIntervalValue;
		initialInverseTransformationMatrix= iMatrix;
		initialSamplingRate= rate;
		initialR2WindowHalfwidth= r2WindowHalfwidthValue;
		initialApplyCharacteristicLengthMedianFiltering= applyFilterToCharacteristicLength;
		initialCharacteristicLengthMedianFilterHalfwidth= characteristicLengthFilterHalfwidth;
		initialApplyVelocityMedianFiltering= applyFilterToVelocity;
		initialVelocityMedianFilterHalfwidth= velocityFilterHalfwidth;
		initialSynthesizedImageTransparency= synthesizedImageTransparencyValue;
		initialMakeRectangularBlobsInSynthesizedImage= makeRectangularBlobsInSynthesizedImageValue;
		setActiveValues();
	}
	//
	protected void setActiveValues() {
		activeMinimalTrainingInterval= initialMinimalTrainingInterval;
		activeMaximalTrainingInterval= initialMaximalTrainingInterval;
		activeHorizontalBlobBorder= initialHorizontalBlobBorder;
		activeVerticalBlobBorder= initialVerticalBlobBorder;
		activeHorizontalExtraBorderCoefficient= initialHorizontalExtraBorderCoefficient;
		activeVerticalExtraBorderCoefficient= initialVerticalExtraBorderCoefficient;
		activeMinimalBlobIntersectionArea= initialMinimalBlobIntersectionArea;
		activeMinimalBlobSize= initialMinimalBlobSize;
		activeMinimalTrackDuration= initialMinimalTrackDuration;
		activeMaximalTrackDuration= initialMaximalTrackDuration;
		activeMaximalChronicleLength= initialMaximalChronicleLength;
		activeMaximalBlobInvisibilityInterval= initialMaximalBlobInvisibilityInterval;
		activeMaximalTrackRetentionInterval= initialMaximalTrackRetentionInterval;
		activeInverseTransformationMatrix= initialInverseTransformationMatrix;
		activeSamplingRate= initialSamplingRate;
		activeR2WindowHalfwidth= initialR2WindowHalfwidth;
		activeApplyCharacteristicLengthMedianFiltering= initialApplyCharacteristicLengthMedianFiltering;
		activeCharacteristicLengthMedianFilterHalfwidth= initialCharacteristicLengthMedianFilterHalfwidth;
		activeApplyVelocityMedianFiltering= initialApplyVelocityMedianFiltering;
		activeVelocityMedianFilterHalfwidth= initialVelocityMedianFilterHalfwidth;
		activeSynthesizedImageTransparency= initialSynthesizedImageTransparency;
		activeMakeRectangularBlobsInSynthesizedImage= initialMakeRectangularBlobsInSynthesizedImage;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public void setMinimalTrainingInterval(int value) {
		initialMinimalTrainingInterval= value;
	}
	public int getMinimalTrainingInterval() {
		return activeMinimalTrainingInterval;
	}
	//
	synchronized public void setMaximalTrainingInterval(int value) {
		initialMaximalTrainingInterval= value;
	}
	public int getMaximalTrainingInterval() {
		return activeMaximalTrainingInterval;
	}
	//
	synchronized public void setHorizontalBlobBorder(int value) {
		initialHorizontalBlobBorder= value;
	}
	public int getHorizontalBlobBorder() {
		return activeHorizontalBlobBorder;
	}
	//
	synchronized public void setVerticalBlobBorder(int value) {
		initialVerticalBlobBorder= value;
	}
	public int getVerticalBlobBorder() {
		return activeVerticalBlobBorder;
	}
	//
	synchronized public void setHorizontalExtraBorderCoefficient(double value) {
		initialHorizontalExtraBorderCoefficient= value;
	}
	public double getHorizontalExtraBorderCoefficient() {
		return activeHorizontalExtraBorderCoefficient;
	}
	//
	synchronized public void setVerticalExtraBorderCoefficient(double value) {
		initialVerticalExtraBorderCoefficient= value;
	}
	public double getVerticalExtraBorderCoefficient() {
		return activeVerticalExtraBorderCoefficient;
	}
	//
	synchronized public void setMinimalBlobIntersectionArea(int value) {
		initialMinimalBlobIntersectionArea= value;
	}
	public int getMinimalBlobIntersectionArea() {
		return activeMinimalBlobIntersectionArea;
	}
	//
	synchronized public void setMinimalBlobSize(int value) {
		initialMinimalBlobSize= value;
	}
	public int getMinimalBlobSize() {
		return activeMinimalBlobSize;
	}
	//
	synchronized public void setMinimalTrackDuration(int value) {
		initialMinimalTrackDuration= value;
	}
	public int getMinimalTrackDuration() {
		return activeMinimalTrackDuration;
	}
	//
	synchronized public void setMaximalTrackDuration(int value) {
		initialMaximalTrackDuration= value;
	}
	public int getMaximalTrackDuration() {
		return activeMaximalTrackDuration;
	}
	//
	synchronized public void setMaximalChronicleLength(NumericalValue value) {
		initialMaximalChronicleLength= value;
	}
	public NumericalValue getMaximalChronicleLength() {
		return activeMaximalChronicleLength;
	}
	//
	synchronized public void setMaximalBlobInvisibilityInterval(int value) {
		initialMaximalBlobInvisibilityInterval= value;
	}
	public int getMaximalBlobInvisibilityInterval() {
		return activeMaximalBlobInvisibilityInterval;
	}
	//
	synchronized public void setMaximalTrackRetentionInterval(int value) {
		initialMaximalTrackRetentionInterval= value;
	}
	public int getMaximalTrackRetentionInterval() {
		return activeMaximalTrackRetentionInterval;
	}
	//
	synchronized public void setInverseTransformationMatrix(double[][] value) {
		initialInverseTransformationMatrix= value;
		transformationMatrices= null;
	}
	public double[][] getInverseTransformationMatrix() {
		return activeInverseTransformationMatrix;
	}
	//
	synchronized public void setSamplingRate(double value) {
		initialSamplingRate= value;
	}
	public double getSamplingRate() {
		return activeSamplingRate;
	}
	//
	synchronized public void setR2WindowHalfwidth(int value) {
		initialR2WindowHalfwidth= value;
	}
	public int getR2WindowHalfwidth() {
		return activeR2WindowHalfwidth;
	}
	//
	synchronized public void setCharacteristicLengthMedianFilteringMode(boolean mode) {
		initialApplyCharacteristicLengthMedianFiltering= mode;
	}
	public boolean getCharacteristicLengthMedianFilteringMode() {
		return activeApplyCharacteristicLengthMedianFiltering;
	}
	//
	synchronized public void setCharacteristicLengthMedianFilterHalfwidth(int value) {
		initialCharacteristicLengthMedianFilterHalfwidth= value;
	}
	public int getCharacteristicLengthMedianFilterHalfwidth() {
		return activeCharacteristicLengthMedianFilterHalfwidth;
	}
	//
	synchronized public void setVelocityMedianFilteringMode(boolean mode) {
		initialApplyVelocityMedianFiltering= mode;
	}
	public boolean getVelocityMedianFilteringMode() {
		return activeApplyVelocityMedianFiltering;
	}
	//
	synchronized public void setVelocityMedianFilterHalfwidth(int value) {
		initialVelocityMedianFilterHalfwidth= value;
	}
	public int getVelocityMedianFilterHalfwidth() {
		return activeVelocityMedianFilterHalfwidth;
	}
	//
	synchronized public void setSynthesizedImageTransparency(int value) {
		initialSynthesizedImageTransparency= value;
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			snapshot.setSynthesizedImageTransparency(value);
		}
	}
	public int getSynthesizedImageTransparency() {
		return activeSynthesizedImageTransparency;
	}
	//
	synchronized public void setSynthesizedImageRectangularBlobsMode(boolean value) {
		initialMakeRectangularBlobsInSynthesizedImage= value;
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			snapshot.setSynthesizedImageRectangularBlobsMode(value);
		}
	}
	public boolean getSynthesizedImageRectangularBlobsMode() {
		return activeMakeRectangularBlobsInSynthesizedImage;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public java.awt.image.BufferedImage getRecentImage() {
		return recentImage;
	}
	public java.awt.image.BufferedImage getPreprocessedImage() {
		return preprocessedImage;
	}
	public void setPreprocessedImage(java.awt.image.BufferedImage image) {
		preprocessedImage= image;
		operationalImageWidth= image.getWidth(null);
		operationalImageHeight= image.getHeight(null);
		operationalVectorLength= operationalImageWidth * operationalImageHeight;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public TransformationMatrices getTransformationMatrices() {
		createTransformationMatricesIfNecessary();
		return transformationMatrices;
	}
	//
	protected void createTransformationMatricesIfNecessary() {
		if (transformationMatrices==null) {
			int imageWidth= getOperationalImageWidth();
			int imageHeight= getOperationalImageHeight();
			transformationMatrices= new TransformationMatrices(getInverseTransformationMatrix(),imageWidth,imageHeight);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getOperationalImageWidth() {
		return operationalImageWidth;
	}
	//
	public int getOperationalImageHeight() {
		return operationalImageHeight;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean[] getForegroundMask() {
		createForegroundMaskIfNecessary();
		return foregroundMask;
	}
	public void setForegroundMask(boolean[] mask) {
		foregroundMask= mask;
	}
	//
	public int getForegroundMaskWidth() {
		createForegroundMaskIfNecessary();
		return foregroundMaskWidth;
	}
	public int getForegroundMaskHeight() {
		createForegroundMaskIfNecessary();
		return foregroundMaskHeight;
	}
	//
	protected void createForegroundMaskIfNecessary() {
		if (foregroundMask==null) {
			foregroundMask= new boolean[operationalVectorLength];
			Arrays.fill(foregroundMask,true);
			foregroundMaskWidth= operationalImageWidth;
			foregroundMaskHeight= operationalImageHeight;
		}
	}
	//
	protected void checkForegroundMaskSize() {
		if (foregroundMask != null) {
			if (	foregroundMaskWidth != operationalImageWidth ||
				foregroundMaskHeight != operationalImageHeight) {
				throw new IncorrectForegroundMaskSize(foregroundMaskWidth,foregroundMaskHeight,operationalImageWidth,operationalImageHeight);
			}
		}
	}
	//
	protected void clearForegroundMask() {
		foregroundMask= null;
		foregroundMaskWidth= -1;
		foregroundMaskHeight= -1;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void shortenArraysIfNecessary(long frameTime, NumericalValue maximalChronicleLength) {
		if (frameTime < 0 || tracks.isEmpty() || arrayOfTime.isEmpty() || maximalChronicleLength==null) {
			return;
		};
		long longMaximalChronicleLength= maximalChronicleLength.toLong(1000);
		if (longMaximalChronicleLength < 0) {
			return;
		};
		long minimalTime= frameTime - longMaximalChronicleLength;
		ListIterator<VPM_FrameNumberAndTime> iteratorOfArrayOfTime= arrayOfTime.listIterator();
		boolean thereWereObsoletePoints= false;
		while (iteratorOfArrayOfTime.hasNext()) {
			VPM_FrameNumberAndTime frameNumberAndTime= iteratorOfArrayOfTime.next();
			if (frameNumberAndTime.getTime() < minimalTime) {
				iteratorOfArrayOfTime.remove();
				thereWereObsoletePoints= true;
			} else {
				break;
			}
		};
		if (thereWereObsoletePoints) {
			shortenArrayOfTracks(minimalTime);
		}
	}
	//
	protected void shortenArrayOfTracks(long minimalTime) {
		Set<BigInteger> trackKeySet= tracks.keySet();
		Iterator<BigInteger> iteratorOfTrackKeySet= trackKeySet.iterator();
		while (iteratorOfTrackKeySet.hasNext()) {
			BigInteger key= iteratorOfTrackKeySet.next();
			GrowingTrack track= tracks.get(key);
			long beginningTime= track.getBeginningTimeInMilliseconds();
			long endTime= track.getEndTimeInMilliseconds();
			if (endTime < minimalTime) {
				track.depose();
				iteratorOfTrackKeySet.remove();
			} else if (beginningTime <= minimalTime) {
				track.reduceNumberOfPoints(minimalTime);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public void reset(
			boolean forgetSettings,
			boolean forgetStatistics,
			boolean forgetResults,
			int minimalTrainingIntervalValue,
			int maximalTrainingIntervalValue,
			int horizontalBlobBorderValue,
			int verticalBlobBorderValue,
			double horizontalExtraBorderCoefficientValue,
			double verticalExtraBorderCoefficientValue,
			int minimalBlobIntersectionAreaValue,
			int minimalBlobSizeValue,
			int minimalTrackDurationValue,
			int maximalTrackDurationValue,
			NumericalValue maximalChronicleLengthValue,
			int maximalBlobInvisibilityIntervalValue,
			int maximalTrackRetentionIntervalValue,
			double[][] iMatrix,
			double rate,
			int r2WindowHalfwidthValue,
			boolean applyFilterToCharacteristicLength,
			int characteristicLengthFilterHalfwidth,
			boolean applyFilterToVelocity,
			int velocityFilterHalfwidth,
			int synthesizedImageTransparencyValue,
			boolean makeRectangularBlobsInSynthesizedImageValue) {
		if (forgetSettings) {
			forgetStatistics= true;
		};
		if (forgetSettings) {
			initialMinimalTrainingInterval= minimalTrainingIntervalValue;
			initialMaximalTrainingInterval= maximalTrainingIntervalValue;
			initialHorizontalBlobBorder= horizontalBlobBorderValue;
			initialVerticalBlobBorder= verticalBlobBorderValue;
			initialHorizontalExtraBorderCoefficient= horizontalExtraBorderCoefficientValue;
			initialVerticalExtraBorderCoefficient= verticalExtraBorderCoefficientValue;
			initialMinimalBlobIntersectionArea= minimalBlobIntersectionAreaValue;
			initialMinimalBlobSize= minimalBlobSizeValue;
			initialMinimalTrackDuration= minimalTrackDurationValue;
			initialMaximalTrackDuration= maximalTrackDurationValue;
			initialMaximalChronicleLength= maximalChronicleLengthValue;
			initialMaximalBlobInvisibilityInterval= maximalBlobInvisibilityIntervalValue;
			initialMaximalTrackRetentionInterval= maximalTrackRetentionIntervalValue;
			initialInverseTransformationMatrix= iMatrix;
			initialSamplingRate= rate;
			initialR2WindowHalfwidth= r2WindowHalfwidthValue;
			initialApplyCharacteristicLengthMedianFiltering= applyFilterToCharacteristicLength;
			initialCharacteristicLengthMedianFilterHalfwidth= characteristicLengthFilterHalfwidth;
			initialApplyVelocityMedianFiltering= applyFilterToVelocity;
			initialVelocityMedianFilterHalfwidth= velocityFilterHalfwidth;
			initialSynthesizedImageTransparency= synthesizedImageTransparencyValue;
			initialMakeRectangularBlobsInSynthesizedImage= makeRectangularBlobsInSynthesizedImageValue;
			setActiveValues();
			transformationMatrices= null;
		};
		if (forgetStatistics) {
			forgetStatistics();
		};
		if (forgetResults) {
			forgetResults();
		}
	}
	protected void forgetStatistics() {
		recentImage= null;
		preprocessedImage= null;
		foregroundMask= null;
		recentFrameNumber= 0;
		recentTimeInMilliseconds= 0;
	}
	protected void forgetResults() {
		recentSnapshot.set(null);
		tracks.clear();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getRecentFrameNumber() {
		return recentFrameNumber;
	}
	//
	public long getRecentTimeInMilliseconds() {
		return recentTimeInMilliseconds;
	}
	//
	public boolean takeRecentImageIntoAccount() {
		return takeRecentImageIntoAccount;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BlobType[] getBlobTypes() {
		createBlobAttributesIfNecessary();
		return currentBlobTypes;
	}
	public BlobAttributes[] getBlobAttributes() {
		createBlobAttributesIfNecessary();
		return currentBlobAttributes;
	}
	//
	protected abstract void createBlobAttributesIfNecessary();
	//
	protected void clearBlobStore() {
		currentBlobTypes= null;
		currentBlobAttributes= null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setTracks(HashMap<BigInteger,GrowingTrack> t) {
		tracks= t;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getCommittedRecentFrameNumber() {
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			return snapshot.getRecentFrameNumber();
		} else {
			return -1;
		}
	}
	public boolean frameNumberIsIllegal(long frameNumber) {
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			if (snapshot.getRecentFrameNumber() >= recentFrameNumber) {
				return true;
			}
		};
		return false;
	}
	//
	public long getCommittedRecentFrameTime() {
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			return snapshot.getRecentTimeInMilliseconds();
		} else {
			return -1;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public java.awt.image.BufferedImage getCommittedRecentImage() {
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			return snapshot.getRecentImage();
		} else {
			return null;
		}
	}
	public java.awt.image.BufferedImage getCommittedPreprocessedImage() {
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			return snapshot.getPreprocessedImage();
		} else {
			return null;
		}
	}
	public java.awt.image.BufferedImage getCommittedForegroundImage() {
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			return snapshot.getForegroundImage();
		} else {
			return null;
		}
	}
	public java.awt.image.BufferedImage getCommittedSynthesizedImage() {
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			return snapshot.getSynthesizedImage();
		} else {
			return null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getCommittedBlobs() {
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			return snapshot.getPrologBlobs();
		} else {
			return PrologEmptyList.instance;
		}
	}
	//
	public Term getCommittedTracks() {
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			return snapshot.getPrologTracks();
		} else {
			return PrologEmptyList.instance;
		}
	}
	//
	public Term getCommittedConnectedGraphs() {
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			return snapshot.getPrologConnectedGraphs();
		} else {
			return PrologEmptyList.instance;
		}
	}
	//
	public Term getCommittedChronicle() {
		GenericVideoProcessingMachineSnapshot snapshot= recentSnapshot.get();
		if (snapshot != null) {
			return snapshot.getPrologChronicle();
		} else {
			return PrologEmptyList.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double[] physicalCoordinates(int pixelX, int pixelY) {
		createTransformationMatricesIfNecessary();
		double[] physicalCoordinates= new double[2];
		physicalCoordinates[0]= transformationMatrices.getPhysicalX(pixelX,pixelY);
		physicalCoordinates[1]= transformationMatrices.getPhysicalY(pixelX,pixelY);
		return physicalCoordinates;
	}
	//
	public double characteristicLength(int x, int y) {
		createTransformationMatricesIfNecessary();
		int imageWidth= getOperationalImageWidth();
		int imageHeight= getOperationalImageHeight();
		double[][] characteristicMatrix= transformationMatrices.getCharacteristicMatrix();
		if (x < 0 || x >= imageWidth) {
			return -1.0;
		};
		if (y < 0 || y >= imageHeight) {
			return -1.0;
		};
		return characteristicMatrix[y][x];
	}
}
