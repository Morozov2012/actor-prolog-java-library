// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.plain;

import morozov.system.vision.vpm.*;
import morozov.terms.*;

import java.awt.image.WritableRaster;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.math.BigInteger;

public class ImageSubtractorSnapshot extends GenericVideoProcessingMachineSnapshot {
	//
	protected int numberOfBands;
	protected int[][] backgroundSum;
	protected int[][] backgroundSumX2;
	protected int backgroundN;
	//
	protected BlobAttributes[] blobAttributeArray;
	//
	protected boolean refuseSlowTracks= false;
	protected double velocityThreshold;
	protected double distanceThreshold;
	protected double fuzzyThresholdBorder;
	//
	protected java.awt.image.BufferedImage backgroundImage;
	protected java.awt.image.BufferedImage sigmaImage;
	//
	///////////////////////////////////////////////////////////////
	//
	public ImageSubtractorSnapshot(
			long frameNumber,
			long timeInMilliseconds,
			int imageWidth,
			int imageHeight,
			java.awt.image.BufferedImage rImage,
			java.awt.image.BufferedImage pImage,
			int bandNumber,
			int[][] sum,
			int[][] sumX2,
			int number,
			boolean[] mask,
			ArrayList<VPM_FrameNumberAndTime> timeArray,
			BlobAttributes[] blobAttributes,
			HashMap<BigInteger,GrowingTrack> currentTracks,
			boolean refuseTracks,
			double minimalVelocity,
			double minimalDistance,
			double borderOfFuzzyThreshold,
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
		numberOfBands= bandNumber;
		backgroundSum= new int[numberOfBands][0];
		for (int n=0; n < numberOfBands; n++) {
			backgroundSum[n]= Arrays.copyOf(sum[n],operationalVectorLength);
		};
		backgroundSumX2= new int[numberOfBands][0];
		for (int n=0; n < numberOfBands; n++) {
			backgroundSumX2[n]= Arrays.copyOf(sumX2[n],operationalVectorLength);
		};
		backgroundN= number;
		if (blobAttributes != null) {
			blobAttributeArray= Arrays.copyOf(blobAttributes,blobAttributes.length);
		} else {
			blobAttributeArray= null;
		};
		refuseSlowTracks= refuseTracks;
		velocityThreshold= minimalVelocity;
		distanceThreshold= minimalDistance;
		fuzzyThresholdBorder= borderOfFuzzyThreshold;
		synthesizedImageTransparency= transparency;
		makeRectangularBlobsInSynthesizedImage= makeRectangularBlobs;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected Term createPrologBlobs() {
		if (blobAttributeArray==null) {
			return PrologEmptyList.instance;
		};
		BigInteger recentIdentifier= BigInteger.ZERO;
		Term list= PrologEmptyList.instance;
		for (int n=blobAttributeArray.length-1; n >= 0; n--) {
			recentIdentifier= recentIdentifier.add(BigInteger.ONE);
			Term prologBlob= blobAttributeArray[n].toTerm(recentIdentifier);
			list= new PrologList(prologBlob,list);
		};
		return list;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public void setSlowTracksDeletionMode(boolean mode) {
		if (refuseSlowTracks != mode) {
			refuseSlowTracks= mode;
			graphs= null;
			synthesizedImage= null;
		}
	}
	synchronized protected boolean getRefuseSlowTracks() {
		return refuseSlowTracks;
	}
	//
	synchronized public void setFuzzyVelocityThreshold(double value) {
		if (velocityThreshold != value) {
			velocityThreshold= value;
			graphs= null;
			synthesizedImage= null;
		}
	}
	synchronized public double getVelocityThreshold() {
		return velocityThreshold;
	}
	//
	synchronized public void setFuzzyDistanceThreshold(double value) {
		if (distanceThreshold != value) {
			distanceThreshold= value;
			graphs= null;
			synthesizedImage= null;
		}
	}
	synchronized public double getDistanceThreshold() {
		return distanceThreshold;
	}
	//
	synchronized public void setFuzzyThresholdBorder(double value) {
		if (fuzzyThresholdBorder != value) {
			fuzzyThresholdBorder= value;
			graphs= null;
			synthesizedImage= null;
		}
	}
	synchronized public double getFuzzyThresholdBorder() {
		return fuzzyThresholdBorder;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public java.awt.image.BufferedImage getBackgroundImage() {
		if (backgroundImage != null) {
			return backgroundImage;
		};
		if (backgroundN < 0) {
			return null;
		};
		int[][] pixels= new int[numberOfBands][operationalVectorLength];
		for (int k=0; k < numberOfBands; k++) {
			for (int n=0; n < operationalVectorLength; n++) {
				int sum= backgroundSum[k][n];
				int mean= 0;
				if (backgroundN > 0) {
					mean= sum / backgroundN;
				};
				pixels[k][n]= mean;
			}
		};
		java.awt.image.BufferedImage resultImage;
		if (numberOfBands==1) {
			resultImage= new java.awt.image.BufferedImage(operationalImageWidth,operationalImageHeight,java.awt.image.BufferedImage.TYPE_BYTE_GRAY);
		} else {
			resultImage= new java.awt.image.BufferedImage(operationalImageWidth,operationalImageHeight,java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
		};
		WritableRaster resultRaster= resultImage.getRaster();
		for (int k=0; k < numberOfBands; k++) {
			resultRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,k,pixels[k]);
		};
		backgroundImage= resultImage;
		return resultImage;
	}
	//
	synchronized public java.awt.image.BufferedImage getSigmaImage() {
		if (sigmaImage != null) {
			return sigmaImage;
		};
		if (backgroundN < 0) {
			return null;
		};
		int[][] pixels= new int[numberOfBands][operationalVectorLength];
		for (int k=0; k < numberOfBands; k++) {
			for (int n=0; n < operationalVectorLength; n++) {
				int sum= backgroundSum[k][n];
				int mean= 0;
				if (backgroundN > 0) {
					mean= sum / backgroundN;
					double dispersion= (backgroundSumX2[k][n] / backgroundN) - mean*mean;
					pixels[k][n]= (int)Math.sqrt(dispersion);
				}
			}
		};
		java.awt.image.BufferedImage resultImage;
		if (numberOfBands==1) {
			resultImage= new java.awt.image.BufferedImage(operationalImageWidth,operationalImageHeight,java.awt.image.BufferedImage.TYPE_BYTE_GRAY);
		} else {
			resultImage= new java.awt.image.BufferedImage(operationalImageWidth,operationalImageHeight,java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
		};
		WritableRaster resultRaster= resultImage.getRaster();
		for (int k=0; k < numberOfBands; k++) {
			resultRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,k,pixels[k]);
		};
		sigmaImage= resultImage;
		return resultImage;
	}
	//
	///////////////////////////////////////////////////////////////
	//
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
			createForegroundImageWithAllChannels(alphaPixels);
		}
	}
	protected void createForegroundImageWithAllChannels(int[] alphaPixels) {
		Graphics2D g2= foregroundImage.createGraphics();
		try {
			if (preprocessedImage != null) {
				g2.drawImage(preprocessedImage,0,0,null);
			} else {
				g2.drawImage(recentImage,0,0,null);
			}
		} finally {
			g2.dispose();
		};
		WritableRaster imageRaster= foregroundImage.getRaster();
		imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,3,alphaPixels);
	}
}
