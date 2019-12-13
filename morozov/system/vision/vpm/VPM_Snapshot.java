// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;
import morozov.terms.*;

import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigInteger;

public class VPM_Snapshot extends GenericVideoProcessingMachineSnapshot {
	//
	protected VPM_SnapshotCommand[] actualSnapshotCommands;
	//
	protected int[] matrixGrayscale;
	protected int[][] matrixRGB;
	protected int[][] matrixHSB;
	protected boolean imageWasAdjusted= false;
	//
	protected java.awt.image.BufferedImage adjustedImage;
	//
	protected ImageChannelName outputChannelName;
	//
	protected BlobGroup[] blobGroups;
	//
	protected boolean refuseSlowTracks= false;
	protected double velocityThreshold;
	protected double distanceThreshold;
	protected double fuzzyThresholdBorder;
	//
	///////////////////////////////////////////////////////////////
	//
	public VPM_Snapshot(
			VPM_SnapshotCommand[] actualCommands,
			long frameNumber,
			long timeInMilliseconds,
			int imageWidth,
			int imageHeight,
			java.awt.image.BufferedImage rImage,
			java.awt.image.BufferedImage pImage,
			int[] mGrayscale,
			int[][] mRGB,
			int[][] mHSB,
			boolean isAdjusted,
			boolean[] mask,
			ImageChannelName channelName,
			ArrayList<VPM_FrameNumberAndTime> timeArray,
			ArrayList<BlobGroup> blobGroupArray,
			HashMap<BigInteger,GrowingTrack> currentTracks,
			int transparency,
			boolean makeRectangularBlobs
			) {
		super(	frameNumber,
			timeInMilliseconds,
			imageWidth,
			imageHeight,
			rImage,
			pImage,
			mask,
			timeArray,
			currentTracks,
			transparency,
			makeRectangularBlobs);
		actualSnapshotCommands= actualCommands;
		matrixGrayscale= mGrayscale;
		matrixRGB= mRGB;
		matrixHSB= mHSB;
		imageWasAdjusted= isAdjusted;
		outputChannelName= channelName;
		blobGroups= blobGroupArray.toArray(new BlobGroup[blobGroupArray.size()]);
		for (int k=0; k < actualSnapshotCommands.length; k++) {
			actualSnapshotCommands[k].execute(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void setSlowTrackDeletionAttributes(double givenVelocityThreshold, double givenDistanceThreshold, double givenFuzzyThresholdBorder) {
		velocityThreshold= givenVelocityThreshold;
		distanceThreshold= givenDistanceThreshold;
		fuzzyThresholdBorder= givenFuzzyThresholdBorder;
		refuseSlowTracks= true;
	}
	//
	@Override
	protected boolean getRefuseSlowTracks() {
		return refuseSlowTracks;
	}
	@Override
	protected double getVelocityThreshold() {
		return velocityThreshold;
	}
	@Override
	protected double getDistanceThreshold() {
		return distanceThreshold;
	}
	@Override
	protected double getFuzzyThresholdBorder() {
		return fuzzyThresholdBorder;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected Term createPrologBlobs() {
		if (blobGroups==null) {
			return PrologEmptyList.instance;
		};
		BigInteger recentIdentifier= BigInteger.ZERO;
		Term list= PrologEmptyList.instance;
		for (int g=blobGroups.length-1; g >= 0; g--) {
			BlobGroup group= blobGroups[g];
			BlobAttributes[] blobAttributeArray= group.getAttributeArray();
			for (int n=blobAttributeArray.length-1; n >= 0; n--) {
				recentIdentifier= recentIdentifier.add(BigInteger.ONE);
				Term prologBlob= blobAttributeArray[n].toTerm(recentIdentifier);
				list= new PrologList(prologBlob,list);
			}
		};
		return list;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	protected void createForegroundImageIfNecessary() {
		if (foregroundImage==null) {
			createForegroundMaskIfNecessary();
			int[] alphaPixels= new int[operationalVectorLength];
			for (int k=0; k < operationalVectorLength; k++) {
				if (foregroundMask[k]) {
					alphaPixels[k]= noTransparency;
				} else {
					alphaPixels[k]= totalTransparency;
				}
			};
			foregroundImage= new java.awt.image.BufferedImage(operationalImageWidth,operationalImageHeight,java.awt.image.BufferedImage.TYPE_4BYTE_ABGR);
			if (outputChannelName==null) {
				fillUpForegroundImageWithAllChannels(alphaPixels);
			} else {
				WritableRaster imageRaster= foregroundImage.getRaster();
				switch (outputChannelName) {
				case GRAYSCALE:
					convertImageToGrayscaleIfNecessary();
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,0,matrixGrayscale);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,1,matrixGrayscale);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,2,matrixGrayscale);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,3,alphaPixels);
					break;
				case HUE:
					convertImageToHSBifNecessary();
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,0,matrixHSB[0]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,1,matrixHSB[0]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,2,matrixHSB[0]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,3,alphaPixels);
					break;
				case SATURATION:
					convertImageToHSBifNecessary();
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,0,matrixHSB[1]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,1,matrixHSB[1]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,2,matrixHSB[1]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,3,alphaPixels);
					break;
				case BRIGHTNESS:
					convertImageToHSBifNecessary();
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,0,matrixHSB[2]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,1,matrixHSB[2]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,2,matrixHSB[2]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,3,alphaPixels);
					break;
				case RED:
					convertImageToRGBifNecessary();
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,0,matrixRGB[0]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,1,matrixRGB[0]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,2,matrixRGB[0]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,3,alphaPixels);
					break;
				case GREEN:
					convertImageToRGBifNecessary();
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,0,matrixRGB[1]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,1,matrixRGB[1]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,2,matrixRGB[1]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,3,alphaPixels);
					break;
				case BLUE:
					convertImageToRGBifNecessary();
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,0,matrixRGB[2]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,1,matrixRGB[2]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,2,matrixRGB[2]);
					imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,3,alphaPixels);
					break;
				case ALL:
					fillUpForegroundImageWithAllChannels(alphaPixels);
					break;
				default:
					System.err.printf("Unknown channel name: %s\n",outputChannelName);
				}
			}
		}
	}
	//
	@Override
	protected void fillUpForegroundImageWithAllChannels(int[] alphaPixels) {
		if (imageWasAdjusted) {
			WritableRaster imageRaster= foregroundImage.getRaster();
			imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,0,matrixRGB[0]);
			imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,1,matrixRGB[1]);
			imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,2,matrixRGB[2]);
			imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,3,alphaPixels);
		} else {
			super.fillUpForegroundImageWithAllChannels(alphaPixels);
		}
	}
	//
	@Override
	protected void fillUpSynthesizedImage(int[] alphaPixels) {
		if (imageWasAdjusted) {
			WritableRaster imageRaster= synthesizedImage.getRaster();
			imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,0,matrixRGB[0]);
			imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,1,matrixRGB[1]);
			imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,2,matrixRGB[2]);
			imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,3,alphaPixels);
		} else {
			super.fillUpSynthesizedImage(alphaPixels);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void convertImageToGrayscaleIfNecessary() {
		if (matrixGrayscale==null) {
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
			convertImageToRGBifNecessary();
			matrixHSB= VisionUtils.convertRGBtoHSB(matrixRGB);
		}
	}
	//
	protected void convertImageToRGBifNecessary() {
		if (matrixRGB==null) {
			matrixRGB= VisionUtils.convertImageToRGB(
				operationalImageWidth,
				operationalImageHeight,
				preprocessedImage,
				recentImage);
		}
	}
}
