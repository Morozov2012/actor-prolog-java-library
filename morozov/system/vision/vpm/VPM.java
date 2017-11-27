// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import morozov.system.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.commands.blb.*;
import morozov.system.vision.vpm.commands.msk.*;
import morozov.system.vision.vpm.converters.*;
import morozov.system.vision.vpm.errors.*;
import morozov.terms.*;

import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.math.BigInteger;

public class VPM extends GenericVideoProcessingMachine {
	//
	protected VPM_FrameCommand[] actualFrameCommands;
	protected VPM_SnapshotCommand[] actualSnapshotCommands;
	//
	protected ArrayList<BlobGroup> blobGroups= new ArrayList<>();
	protected BlobGroup recentBlobGroup;
	//
	protected int[] operationalMatrix;
	protected ArrayDeque<boolean[]> foregroundStack= new ArrayDeque<>();
	//
	protected int[] matrixGrayscale;
	protected int[][] matrixRGB;
	protected int[][] matrixHSB;
	//
	protected ImageChannelName outputChannelName= ImageChannelName.ALL;
	//
	///////////////////////////////////////////////////////////////
	//
	public VPM(	List<VPM_FrameCommand> frameCommands,
			List<VPM_SnapshotCommand> snapshotCommands,
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
		super(	minimalTrainingIntervalValue,
			maximalTrainingIntervalValue,
			horizontalBlobBorderValue,
			verticalBlobBorderValue,
			horizontalExtraBorderCoefficientValue,
			verticalExtraBorderCoefficientValue,
			minimalBlobIntersectionAreaValue,
			minimalBlobSizeValue,
			minimalTrackDurationValue,
			maximalTrackDurationValue,
			maximalChronicleLengthValue,
			maximalBlobInvisibilityIntervalValue,
			maximalTrackRetentionIntervalValue,
			iMatrix,
			rate,
			r2WindowHalfwidthValue,
			applyFilterToCharacteristicLength,
			characteristicLengthFilterHalfwidth,
			applyFilterToVelocity,
			velocityFilterHalfwidth,
			synthesizedImageTransparencyValue,
			makeRectangularBlobsInSynthesizedImageValue);
		actualFrameCommands= frameCommands.toArray(new VPM_FrameCommand[0]);
		actualSnapshotCommands= snapshotCommands.toArray(new VPM_SnapshotCommand[0]);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public void updateActualFrameCommands(List<VPM_FrameCommand> commands) {
		actualFrameCommands= commands.toArray(new VPM_FrameCommand[0]);
	}
	synchronized public void updateActualSnapshotCommands(List<VPM_SnapshotCommand> commands) {
		actualSnapshotCommands= commands.toArray(new VPM_SnapshotCommand[0]);
	}
	synchronized public void updateActualCommands(List<VPM_FrameCommand> frameCommands, List<VPM_SnapshotCommand> snapshotCommands) {
		actualFrameCommands= frameCommands.toArray(new VPM_FrameCommand[0]);
		actualSnapshotCommands= snapshotCommands.toArray(new VPM_SnapshotCommand[0]);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void blbSetBlobBorders(int h, int v) {
		activeHorizontalBlobBorder= h;
		activeVerticalBlobBorder= v;
	}
	public void blbSetExtraBorderCoefficients(double h, double v) {
		activeHorizontalExtraBorderCoefficient= h;
		activeVerticalExtraBorderCoefficient= v;
	}
	public void blbSetMinimalBlobIntersectionArea(int area) {
		activeMinimalBlobIntersectionArea= area;
	}
	public void blbSetMinimalBlobSize(int size) {
		activeMinimalBlobSize= size;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int[] getOperationalMatrix() {
		createOperationalMatrixIfNecessary();
		return operationalMatrix;
	}
	public void setOperationalMatrix(int[] matrix) {
		operationalMatrix= matrix;
		if (outputChannelName==null) {
			outputChannelName= ImageChannelName.GRAYSCALE;
			matrixGrayscale= operationalMatrix;
		} else {
			switch (outputChannelName) {
			case GRAYSCALE:
				matrixGrayscale= operationalMatrix;
				break;
			case HUE:
				matrixHSB[0]= operationalMatrix;
				break;
			case SATURATION:
				matrixHSB[1]= operationalMatrix;
				break;
			case BRIGHTNESS:
				matrixHSB[2]= operationalMatrix;
				break;
			case RED:
				matrixRGB[0]= operationalMatrix;
				break;
			case GREEN:
				matrixRGB[1]= operationalMatrix;
				break;
			case BLUE:
				matrixRGB[2]= operationalMatrix;
				break;
			case ALL:
				outputChannelName= ImageChannelName.GRAYSCALE;
				matrixGrayscale= operationalMatrix;
				break;
			default:
				System.err.printf("Unknown channel name: %s\n",outputChannelName);
			}
		}
	}
	//
	protected void createOperationalMatrixIfNecessary() {
		if (operationalMatrix==null) {
			if (outputChannelName==null) {
				outputChannelName= ImageChannelName.GRAYSCALE;
				convertImageToGrayscaleIfNecessary();
				operationalMatrix= matrixGrayscale;
			} else {
				switch (outputChannelName) {
				case GRAYSCALE:
					convertImageToGrayscaleIfNecessary();
					operationalMatrix= matrixGrayscale;
					break;
				case HUE:
					convertImageToHSBifNecessary();
					operationalMatrix= matrixHSB[0];
					break;
				case SATURATION:
					convertImageToHSBifNecessary();
					operationalMatrix= matrixHSB[1];
					break;
				case BRIGHTNESS:
					convertImageToHSBifNecessary();
					operationalMatrix= matrixHSB[2];
					break;
				case RED:
					convertImageToRGBifNecessary();
					operationalMatrix= matrixRGB[0];
					break;
				case GREEN:
					convertImageToRGBifNecessary();
					operationalMatrix= matrixRGB[1];
					break;
				case BLUE:
					convertImageToRGBifNecessary();
					operationalMatrix= matrixRGB[2];
					break;
				case ALL:
					convertImageToGrayscaleIfNecessary();
					operationalMatrix= matrixGrayscale;
					break;
				default:
					System.err.printf("Unknown channel name: %s\n",outputChannelName);
				}
			}
		}
	}
	//
	public void clearOperationalMatrix() {
		operationalMatrix= null;
		matrixGrayscale= null;
		matrixRGB= null;
		matrixHSB= null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean[] pushForegroundMask() {
		createForegroundMaskIfNecessary();
		foregroundStack.push(foregroundMask);
		return foregroundMask;
	}
	public boolean[] popForegroundMask() {
		try {
			return foregroundStack.pop();
		} catch (NoSuchElementException e) {
			throw new ForegroundStackIsEmpty();
		}
	}
	//
	protected void clearForegroundMask() {
		super.clearForegroundMask();
		foregroundStack.clear();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int[] getMatrixGrayscale() {
		convertImageToGrayscaleIfNecessary();
		return matrixGrayscale;
	}
	//
	public int[] getMatrixHue() {
		convertImageToHSBifNecessary();
		return matrixHSB[0];
	}
	public int[] getMatrixSaturation() {
		convertImageToHSBifNecessary();
		return matrixHSB[1];
	}
	public int[] getMatrixBrightness() {
		convertImageToHSBifNecessary();
		return matrixHSB[2];
	}
	//
	public int[] getMatrixRed() {
		convertImageToRGBifNecessary();
		return matrixRGB[0];
	}
	public int[] getMatrixGreen() {
		convertImageToRGBifNecessary();
		return matrixRGB[1];
	}
	public int[] getMatrixBlue() {
		convertImageToRGBifNecessary();
		return matrixRGB[2];
	}
	//
	public ImageChannelName getOutputChannelName() {
		return outputChannelName;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void selectImageChannel(ImageChannelName name) {
		if (name==null) {
			name= ImageChannelName.ALL;
		};
		outputChannelName= name;
		if (outputChannelName != null) {
			switch (outputChannelName) {
			case GRAYSCALE:
				if (operationalMatrix != null) {
					convertImageToGrayscaleIfNecessary();
				};
				operationalMatrix= matrixGrayscale;
				break;
			case HUE:
				if (operationalMatrix != null) {
					convertImageToHSBifNecessary();
				};
				if (matrixHSB != null) {
					operationalMatrix= matrixHSB[0];
				} else {
					operationalMatrix= null;
				};
				break;
			case SATURATION:
				if (operationalMatrix != null) {
					convertImageToHSBifNecessary();
				};
				if (matrixHSB != null) {
					operationalMatrix= matrixHSB[1];
				} else {
					operationalMatrix= null;
				};
				break;
			case BRIGHTNESS:
				if (operationalMatrix != null) {
					convertImageToHSBifNecessary();
				};
				if (matrixHSB != null) {
					operationalMatrix= matrixHSB[2];
				} else {
					operationalMatrix= null;
				};
				break;
			case RED:
				if (operationalMatrix != null) {
					convertImageToRGBifNecessary();
				};
				if (matrixRGB != null) {
					operationalMatrix= matrixRGB[0];
				} else {
					operationalMatrix= null;
				};
				break;
			case GREEN:
				if (operationalMatrix != null) {
					convertImageToRGBifNecessary();
				};
				if (matrixRGB != null) {
					operationalMatrix= matrixRGB[1];
				} else {
					operationalMatrix= null;
				};
				break;
			case BLUE:
				if (operationalMatrix != null) {
					convertImageToRGBifNecessary();
				};
				if (matrixRGB != null) {
					operationalMatrix= matrixRGB[2];
				} else {
					operationalMatrix= null;
				};
				break;
			case ALL:
				if (operationalMatrix != null) {
					convertImageToGrayscaleIfNecessary();
				};
				operationalMatrix= matrixGrayscale;
				break;
			default:
				System.err.printf("Unknown channel name: %s\n",outputChannelName);
			}
		} else {
			if (operationalMatrix != null) {
				convertImageToGrayscaleIfNecessary();
			};
			operationalMatrix= matrixGrayscale;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void convertImageToGrayscaleIfNecessary() {
		if (matrixGrayscale==null) {
			checkForegroundMaskSize();
			matrixGrayscale= VisionUtils.convertImageToGrayscale(
				operationalImageWidth,
				operationalImageHeight,
				preprocessedImage,
				recentImage);
		}
	}
	//
	protected void convertImageToHSBifNecessary() {
		if (matrixHSB==null) {
			checkForegroundMaskSize();
			convertImageToRGBifNecessary();
			matrixHSB= VisionUtils.convertRGBtoHSB(matrixRGB);
		}
	}
	//
	protected void convertImageToRGBifNecessary() {
		if (matrixRGB==null) {
			checkForegroundMaskSize();
			matrixRGB= VisionUtils.convertImageToRGB(
				operationalImageWidth,
				operationalImageHeight,
				preprocessedImage,
				recentImage);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public void processImage(java.awt.image.BufferedImage image, long frameNumber, long timeInMilliseconds, boolean takeImageIntoAccount) {
		recentFrameNumber= frameNumber;
		recentTimeInMilliseconds= timeInMilliseconds;
		takeRecentImageIntoAccount= takeImageIntoAccount;
		recentImage= image;
		outputChannelName= ImageChannelName.ALL;
		setActiveValues();
		preprocessedImage= null;
		clearOperationalMatrix();
		clearForegroundMask();
		clearBlobStore();
		operationalImageWidth= image.getWidth();
		operationalImageHeight= image.getHeight();
		operationalVectorLength= operationalImageWidth * operationalImageHeight;
		shortenArraysIfNecessary(timeInMilliseconds,getMaximalChronicleLength());
		arrayOfTime.add(new VPM_FrameNumberAndTime(frameNumber,timeInMilliseconds));
		for (int k=0; k < actualFrameCommands.length; k++) {
			actualFrameCommands[k].execute(this);
		};
		getOperationalMatrix();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public void commit() {
		if (operationalMatrix==null) {
			return;
		};
		if (frameNumberIsIllegal(recentFrameNumber)){
			return;
		};
		VPM_Snapshot snapshot= new VPM_Snapshot(
			actualSnapshotCommands,
			recentFrameNumber,
			recentTimeInMilliseconds,
			operationalImageWidth,
			operationalImageHeight,
			recentImage,
			preprocessedImage,
			matrixGrayscale,
			matrixRGB,
			matrixHSB,
			foregroundMask,
			outputChannelName,
			arrayOfTime,
			blobGroups,
			tracks,
			getSynthesizedImageTransparency(),
			getSynthesizedImageRectangularBlobsMode()
			);
		recentSnapshot.set(snapshot);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void forgetStatistics() {
		super.forgetStatistics();
		clearOperationalMatrix();
		clearForegroundMask();
		clearBlobStore();
		for (int k=0; k < actualFrameCommands.length; k++) {
			actualFrameCommands[k].forgetStatistics();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void storeBlobGroup(BlobGroup group) {
		blobGroups.add(group);
		recentBlobGroup= group;
		currentBlobTypes= null;
		currentBlobAttributes= null;
	}
	public BlobGroup getRecentBlobGroup() {
		return recentBlobGroup;
	}
	public ArrayList<BlobGroup> getBlobGroups() {
		return blobGroups;
	}
	public void setBlobGroups(ArrayList<BlobGroup> groups) {
		blobGroups= groups;
	}
	//
	protected void createBlobAttributesIfNecessary() {
		if (currentBlobTypes==null) {
			ArrayList<BlobType> arrayOfBlobTypes= new ArrayList<>();
			ArrayList<BlobAttributes> arrayOfBlobAttributes= new ArrayList<>();
			Iterator<BlobGroup> blobGroupIterator= blobGroups.iterator();
			while (blobGroupIterator.hasNext()) {
				BlobGroup group= blobGroupIterator.next();
				BlobType type= group.getType();
				BlobAttributes[] attributeArray= group.getAttributeArray();
				int numberOfBlobs= attributeArray.length;
				for (int k=0; k < numberOfBlobs; k++) {
					arrayOfBlobTypes.add(type);
					arrayOfBlobAttributes.add(attributeArray[k]);
				}
			};
			currentBlobTypes= arrayOfBlobTypes.toArray(new BlobType[0]);
			currentBlobAttributes= arrayOfBlobAttributes.toArray(new BlobAttributes[0]);
		}
	}
	//
	protected void clearBlobStore() {
		super.clearBlobStore();
		blobGroups.clear();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public java.awt.image.BufferedImage getBackgroundImage(int layerNumber) {
		if (operationalImageWidth < 0 || operationalImageHeight < 0) {
			return null;
		};
		int counter= VideoProcessingMachineOperations.firstLayerNumber;
		for (int k=0; k < actualFrameCommands.length; k++) {
			VPM_FrameCommand command= actualFrameCommands[k];
			if (command.isBackgroundSubtractionCommand()) {
				if (layerNumber==counter) {
					int[] pixels= ((VPMmskBackgroundSubtractionCommand)command).getBackgroundImage(this);
					java.awt.image.BufferedImage backgroundImage= new java.awt.image.BufferedImage(operationalImageWidth,operationalImageHeight,java.awt.image.BufferedImage.TYPE_BYTE_GRAY);
					WritableRaster raster= backgroundImage.getRaster();
					raster.setSamples(0,0,operationalImageWidth,operationalImageHeight,0,pixels);
					return backgroundImage;
				};
				counter++;
			}
		};
		return null;
	}
	synchronized public java.awt.image.BufferedImage getSigmaImage(int layerNumber) {
		if (operationalImageWidth < 0 || operationalImageHeight < 0) {
			return null;
		};
		int counter= VideoProcessingMachineOperations.firstLayerNumber;
		for (int k=0; k < actualFrameCommands.length; k++) {
			VPM_FrameCommand command= actualFrameCommands[k];
			if (command.isBackgroundSubtractionCommand()) {
				if (layerNumber==counter) {
					int[] pixels= ((VPMmskBackgroundSubtractionCommand)command).getSigmaImage(this);
					java.awt.image.BufferedImage sigmaImage= new java.awt.image.BufferedImage(operationalImageWidth,operationalImageHeight,java.awt.image.BufferedImage.TYPE_BYTE_GRAY);
					WritableRaster raster= sigmaImage.getRaster();
					raster.setSamples(0,0,operationalImageWidth,operationalImageHeight,0,pixels);
					return sigmaImage;
				};
				counter++;
			}
		};
		return null;
	}
}
