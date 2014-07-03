// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import target.*;

import morozov.terms.*;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.math.BigInteger;

import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.Collection;

class BlobSet {
	//
//
private boolean recentFrameNumberIsPrinted= false;
//
	private int minimalTrackDuration;
	//
	private boolean refuseSlowTracks;
	private double velocityThreshold;
	private double distanceThreshold;
	private double fuzzyThresholdBorder;
	private boolean makeSquareBlobsInSynthesizedImage;
	private int synthesizedImageTransparency;
	//
	private long recentFrameNumber;
	private String title= "";
	private int imageWidth;
	private int imageHeight;
	private int numberOfBands;
	private int numberOfExtraBands;
	private int[][] backgroundSum;
	private int[][] backgroundSumX2;
	private int backgroundN;
	private int[] contourPixels;
	private BufferedImage recentImage;
	private BufferedImage backgroundImage;
	private BufferedImage sigmaImage;
	private BufferedImage foregroundImage;
	private BufferedImage synthesizedImage;
	private ConnectedGraph[] graphs;
	private int differenceMarker;
	private int noDifferenceMarker;
	private int[] blobSize;
	private int[][] blobRectangles;
	private BigInteger[] blobIdentifiers;
	private int[] trackDurations;
	private HashMap<BigInteger,StableTrack> tracks;
	private static final int spaceColor= 255;
	//
	public BlobSet(	long t,
			int width,
			int height,
			BufferedImage sourceImage,
			BufferedImage extractedImage,
			int bandNumber,
			int extraBands,
			int[][] sum,
			int[][] sumX2,
			int number,
			int[] contour,
			int marker1,
			int marker2,
			int[] currentBlobSize,
			int[][] currentBlobRectangles,
			BigInteger[] currentBlobIdentifiers,
			int[] currentTrackDurations,
			HashMap<BigInteger,GrowingTrack> currentTracks,
			int minimalDuration,
			boolean refuseTracks,
			double minimalVelocity,
			double minimalDistance,
			double borderOfFuzzyThreshold,
			boolean makeRectangleBlobs,
			int transparency
			) {
		recentFrameNumber= t;
		imageWidth= width;
		imageHeight= height;
		recentImage= sourceImage;
		foregroundImage= extractedImage;
		numberOfBands= bandNumber;
		numberOfExtraBands= extraBands;
		int vectorLength= imageWidth * imageHeight;
		backgroundSum= new int[numberOfBands][0];
		for (int n=0; n < numberOfBands; n++) {
			backgroundSum[n]= Arrays.copyOf(sum[n],vectorLength);
		};
		backgroundSumX2= new int[numberOfBands][0];
		for (int n=0; n < numberOfBands; n++) {
			backgroundSumX2[n]= Arrays.copyOf(sumX2[n],vectorLength);
		};
		backgroundN= number;
		contourPixels= Arrays.copyOf(contour,vectorLength);
		differenceMarker= marker1;
		noDifferenceMarker= marker2;
		blobSize= Arrays.copyOf(currentBlobSize,currentBlobSize.length);
		blobRectangles= new int[currentBlobRectangles.length][4];
		for (int n=0; n < currentBlobRectangles.length; n++) {
			for (int k=0; k < 4; k++) {
				blobRectangles[n][k]= currentBlobRectangles[n][k];
			}
		};
		blobIdentifiers= Arrays.copyOf(currentBlobIdentifiers,currentBlobIdentifiers.length);
		trackDurations= Arrays.copyOf(currentTrackDurations,currentTrackDurations.length);
		tracks= new HashMap<BigInteger,StableTrack>();
		// tracks.putAll(currentTracks);
		Set<BigInteger> trackIdentifiers= currentTracks.keySet();
		Iterator<BigInteger> trackIdentifiersIterator= trackIdentifiers.iterator();
		while (trackIdentifiersIterator.hasNext()) {
			BigInteger blobIdentifier= trackIdentifiersIterator.next();
			GrowingTrack track= currentTracks.get(blobIdentifier);
			tracks.put(blobIdentifier,track.createStableTrack());
		};
		minimalTrackDuration= minimalDuration;
		refuseSlowTracks= refuseTracks;
		velocityThreshold= minimalVelocity;
		distanceThreshold= minimalDistance;
		fuzzyThresholdBorder= borderOfFuzzyThreshold;
		makeSquareBlobsInSynthesizedImage= makeRectangleBlobs;
		synthesizedImageTransparency= transparency;
	}
	synchronized public long getRecentFrameNumber() {
		return recentFrameNumber;
	}
	synchronized public void setTitle(String text) {
		title= text;
	}
	synchronized public String getTitle() {
		return title;
	}
	synchronized public void setSlowTracksDeletionMode(boolean mode) {
		if (refuseSlowTracks != mode) {
			refuseSlowTracks= mode;
			graphs= null;
			synthesizedImage= null;
		}
	}
	synchronized public void setFuzzyVelocityThreshold(double value) {
		if (velocityThreshold != value) {
			velocityThreshold= value;
			graphs= null;
			synthesizedImage= null;
		}
	}
	synchronized public void setFuzzyDistanceThreshold(double value) {
		if (distanceThreshold != value) {
			distanceThreshold= value;
			graphs= null;
			synthesizedImage= null;
		}
	}
	synchronized public void setFuzzyThresholdBorder(double value) {
		if (fuzzyThresholdBorder != value) {
			fuzzyThresholdBorder= value;
			graphs= null;
			synthesizedImage= null;
		}
	}
	synchronized public void setSynthesizedImageTransparency(int value) {
		if (synthesizedImageTransparency != value) {
			synthesizedImageTransparency= value;
			synthesizedImage= null;
		}
	}
	synchronized public void setSynthesizedImageRectangularBlobsMode(boolean value) {
		if (makeSquareBlobsInSynthesizedImage != value) {
			makeSquareBlobsInSynthesizedImage= value;
			synthesizedImage= null;
		}
	}
	synchronized public Term getBlobs() {
/*
System.out.printf("TIME=%d\n",recentFrameNumber);
// if (recentFrameNumber==400) {
// if (recentFrameNumber>=408) {
// if (recentFrameNumber>=251) {
if (recentFrameNumber>=500) {
recentFrameNumberIsPrinted= false;
};
if (recentFrameNumber>=232 && recentFrameNumber < 500 && !recentFrameNumberIsPrinted) {
recentFrameNumberIsPrinted= true;
ConnectedGraph[] graphs= formAndGetConnectedGraphs();
// if (recentFrameNumber==450) {
//System.out.printf("!!! TIME=551\n\n\n\n\n");
Collection<StableTrack> trackValues= tracks.values();
Iterator<StableTrack> iterator1= trackValues.iterator();
while (iterator1.hasNext()) {
	StableTrack track= iterator1.next();
	if (track != null) {
		//if (track.duration > 70) {
			try {
				String fileName= String.format("log\\track_%05d_(%05d).txt",track.identifier,recentFrameNumber);
				PrintStream stream= new PrintStream(fileName);
				try {
					track.dump(stream);
				} finally {
					stream.close();
				};
				fileName= String.format("log\\track_%05d_(%05d).m",track.identifier,recentFrameNumber);
				stream= new PrintStream(fileName);
				try {
					track.createMatlab(stream);
				} finally {
					stream.close();
				}
			} catch (FileNotFoundException e) {
				System.out.printf("FileNotFoundException: %s",e);
			}
		//}
	}
}
for (int n=0; n < graphs.length; n++) {
	try {
		String fileName= String.format("log\\graph_%05d_(%05d).txt",n,recentFrameNumber);
		PrintStream stream= new PrintStream(fileName);
		try {
			graphs[n].dump(stream);
		} finally {
			stream.close();
		};
		fileName= String.format("log\\graph_%05d_(%05d).m",n,recentFrameNumber);
		stream= new PrintStream(fileName);
		try {
			graphs[n].createMatlab(stream);
		} finally {
			stream.close();
		}
	} catch (FileNotFoundException e) {
		System.out.printf("FileNotFoundException: %s",e);
	}
}
}
*/
		Term result= PrologEmptyList.instance;
		for (int n=blobRectangles.length-1; n >= 0; n--) {
			Term prologIdentifier= new PrologInteger(blobIdentifiers[n]);
			int[] rectangle= blobRectangles[n];
			Term prologX= new PrologInteger(StrictMath.round((rectangle[0]+rectangle[1])/2));
			Term prologY= new PrologInteger(StrictMath.round((rectangle[2]+rectangle[3])/2));
			Term prologWidth= new PrologInteger(rectangle[1]-rectangle[0]);
			Term prologHeight= new PrologInteger(rectangle[3]-rectangle[2]);
			Term prologBlob= new PrologSet(
				- SymbolCodes.symbolCode_E_identifier,
				prologIdentifier,
				new PrologSet(
				- SymbolCodes.symbolCode_E_x,
				prologX,
				new PrologSet(
				- SymbolCodes.symbolCode_E_y,
				prologY,
				new PrologSet(
				- SymbolCodes.symbolCode_E_width,
				prologWidth,
				new PrologSet(
				- SymbolCodes.symbolCode_E_height,
				prologHeight,
				PrologEmptySet.instance)))));
			result= new PrologList(prologBlob,result);
		};
		return result;
	}
	synchronized public Term getTracks() {
		Set<BigInteger> trackIdentifiers= tracks.keySet();
		Iterator<BigInteger> trackIdentifiersIterator= trackIdentifiers.iterator();
		Term list1= PrologEmptyList.instance;
		while (trackIdentifiersIterator.hasNext()) {
			BigInteger blobIdentifier= trackIdentifiersIterator.next();
			StableTrack track= tracks.get(blobIdentifier);
			Term prologIdentifier= new PrologInteger(blobIdentifier);
			ArrayList<TrackSegment> segmentList= track.getTrackSegments();
			ListIterator<TrackSegment> segmentListIterator= segmentList.listIterator(segmentList.size());
			Term list2= PrologEmptyList.instance;
			while (segmentListIterator.hasPrevious()) {
				TrackSegment segment= segmentListIterator.previous();
				int[][] rectangles= segment.rectangles;
				if (rectangles.length <= 0) {
					continue;
				};
				long beginningTime= segment.beginningTime;
				int[] firstRectangle= rectangles[0];
				long frameNumber1= beginningTime+firstRectangle[0];
				long x1= StrictMath.round((firstRectangle[1]+firstRectangle[2])/2.0);
				long y1= StrictMath.round((firstRectangle[3]+firstRectangle[4])/2.0);
				int[] lastRectangle= rectangles[rectangles.length-1];
				long frameNumber2= beginningTime+lastRectangle[0];
				long x2= StrictMath.round((lastRectangle[1]+lastRectangle[2])/2.0);
				long y2= StrictMath.round((lastRectangle[3]+lastRectangle[4])/2.0);
				Term prologSegment= assembleTrackSegment(frameNumber1,x1,y1,frameNumber2,x2,y2,segment);
				list2= new PrologList(prologSegment,list2);
			};
			Term prologTrack= new PrologSet(
				- SymbolCodes.symbolCode_E_identifier,
				prologIdentifier,
				new PrologSet(
				- SymbolCodes.symbolCode_E_segments,
				list2,
				PrologEmptySet.instance));
			list1= new PrologList(prologTrack,list1);
		};
		return list1;
	}
	protected Term formTrackOfBlob(TrackSegment segment) {
		long beginningTime= segment.beginningTime;
		int[][] rectangles= segment.rectangles;
		double[] windowedR2ReferentValues= segment.getWindowedR2ReferentValues();
		Term list3= PrologEmptyList.instance;
		for (int n=rectangles.length-1; n >= 0; n--) {
			int[] currentRectangle= rectangles[n];
			long currentForegroundArea= segment.foregroundAreaValues[n];
			double currentCharacteristicLength= segment.characteristicLengthValues[n];
			long currentContourLength= segment.contourLengthValues[n];
			double currentR2= windowedR2ReferentValues[n];
			double currentVelocity= segment.velocityValues[n];
			Term prologFrameN= new PrologInteger(beginningTime+currentRectangle[0]);
			Term prologXn= new PrologInteger(StrictMath.round((currentRectangle[1]+currentRectangle[2])/2.0));
			Term prologYn= new PrologInteger(StrictMath.round((currentRectangle[3]+currentRectangle[4])/2.0));
			Term prologWidthN= new PrologInteger(currentRectangle[2]-currentRectangle[1]);
			Term prologHeightN= new PrologInteger(currentRectangle[4]-currentRectangle[3]);
			Term prologForegroundAreaN= new PrologInteger(currentForegroundArea);
			Term prologCharacteristicLengthN= new PrologReal(currentCharacteristicLength);
			Term prologContourLengthN= new PrologInteger(currentContourLength);
			Term prologR2N= new PrologReal(currentR2);
			Term prologVelocityN= new PrologReal(currentVelocity);
			Term prologRectangle= new PrologSet(
				- SymbolCodes.symbolCode_E_frame,
				prologFrameN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_x,
				prologXn,
				new PrologSet(
				- SymbolCodes.symbolCode_E_y,
				prologYn,
				new PrologSet(
				- SymbolCodes.symbolCode_E_width,
				prologWidthN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_height,
				prologHeightN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_foreground_area,
				prologForegroundAreaN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_characteristic_length,
				prologCharacteristicLengthN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_contour_length,
				prologContourLengthN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_r2,
				prologR2N,
				new PrologSet(
				- SymbolCodes.symbolCode_E_velocity,
				prologVelocityN,
				PrologEmptySet.instance))))))))));
			list3= new PrologList(prologRectangle,list3);
		};
		return list3;
	}
	synchronized public java.awt.image.BufferedImage getRecentImage() {
		if (recentImage != null) {
			return recentImage;
		} else {
			return null;
		}
	}
	synchronized public java.awt.image.BufferedImage getBackgroundImage() {
		if (backgroundImage != null) {
			return backgroundImage;
		};
		if (backgroundN < 0) {
			return null;
		};
		int vectorLength= imageWidth * imageHeight;
		int[][] pixels= new int[numberOfBands][vectorLength];
		for (int k=0; k < numberOfBands; k++) {
			for (int n=0; n < vectorLength; n++) {
				int sum= backgroundSum[k][n];
				int mean= 0;
				if (backgroundN > 0) {
					mean= sum / backgroundN;
				};
				pixels[k][n]= mean;
			}
		};
		BufferedImage resultImage;
		if (numberOfBands==1) {
			resultImage= new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_BYTE_GRAY);
		} else {
			resultImage= new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_3BYTE_BGR);
		};
		WritableRaster resultRaster= resultImage.getRaster();
		for (int k=0; k < numberOfBands; k++) {
			resultRaster.setSamples(0,0,imageWidth,imageHeight,k,pixels[k]);
		};
		backgroundImage= resultImage;
		// bufferedImage.setData(backgroundImage.getData());
		return resultImage;
	}
	synchronized public java.awt.image.BufferedImage getSigmaImage() {
		if (sigmaImage != null) {
			return sigmaImage;
		};
		if (backgroundN < 0) {
			return null;
		};
		int vectorLength= imageWidth * imageHeight;
		int[][] pixels= new int[numberOfBands][vectorLength];
		for (int k=0; k < numberOfBands; k++) {
			for (int n=0; n < vectorLength; n++) {
				int sum= backgroundSum[k][n];
				int mean= 0;
				if (backgroundN > 0) {
					mean= sum / backgroundN;
					double dispersion= (backgroundSumX2[k][n] / backgroundN) - mean*mean;
					pixels[k][n]= (int)Math.sqrt(dispersion);
				}
			}
		};
		BufferedImage resultImage;
		if (numberOfBands==1) {
			resultImage= new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_BYTE_GRAY);
		} else {
			resultImage= new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_3BYTE_BGR);
		};
		WritableRaster resultRaster= resultImage.getRaster();
		for (int k=0; k < numberOfBands; k++) {
			resultRaster.setSamples(0,0,imageWidth,imageHeight,k,pixels[k]);
		};
		sigmaImage= resultImage;
		// bufferedImage.setData(sigmaImage.getData());
		return resultImage;
	}
	synchronized public java.awt.image.BufferedImage getForegroundImage() {
		return foregroundImage;
	}
	synchronized public java.awt.image.BufferedImage getSynthesizedImage() {
		if (synthesizedImage != null) {
			return synthesizedImage;
		};
		if (foregroundImage != null) {
			formAndGetConnectedGraphs();
			int vectorLength= imageWidth * imageHeight;
			boolean[] bitMask= new boolean[vectorLength];
			for (int n=0; n < graphs.length; n++) {
				graphs[n].enablePixels(recentFrameNumber,bitMask,imageWidth,imageHeight,tracks);
			};
			BufferedImage extractedImage= new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_4BYTE_ABGR);
			WritableRaster raster= extractedImage.getRaster();
			raster.setDataElements(0,0,foregroundImage.getRaster());
			BufferedImage resultImage= applyMaskAndCreateImage(extractedImage,bitMask);
			synthesizedImage= resultImage;
			// bufferedImage.setData(synthesizedImage.getData());
			return synthesizedImage;
		} else {
			return null;
		}
	}
	synchronized public Term getConnectedGraphs() {
/*
System.out.printf("TIME=%d\n",recentFrameNumber);
// if (recentFrameNumber==400) {
// if (recentFrameNumber>=408) {
// if (recentFrameNumber>=251) {
if (recentFrameNumber>=500) {
recentFrameNumberIsPrinted= false;
};
// if (recentFrameNumber>=232 && recentFrameNumber < 500 && !recentFrameNumberIsPrinted) {
// if (recentFrameNumber>=400 && recentFrameNumber < 500 && !recentFrameNumberIsPrinted) {
// if (false && recentFrameNumber>=275 && recentFrameNumber < 500 && !recentFrameNumberIsPrinted) {
if (recentFrameNumber>=499 && recentFrameNumber < 500 && !recentFrameNumberIsPrinted) {
recentFrameNumberIsPrinted= true;
ConnectedGraph[] graphs= formAndGetConnectedGraphs();
// if (recentFrameNumber==450) {
System.out.printf("!!! TIME=%s\n\n\n\n\n",recentFrameNumber);
Collection<StableTrack> trackValues= tracks.values();
Iterator<StableTrack> iterator1= trackValues.iterator();
while (iterator1.hasNext()) {
	StableTrack track= iterator1.next();
	if (track != null) {
		//if (track.duration > 70) {
			try {
				String fileName= String.format("log\\track_%05d_(%05d).txt",track.identifier,recentFrameNumber);
				PrintStream stream= new PrintStream(fileName);
				try {
					track.dump(stream);
				} finally {
					stream.close();
				};
				fileName= String.format("log\\track_%05d_(%05d).m",track.identifier,recentFrameNumber);
				stream= new PrintStream(fileName);
				try {
					track.createMatlab(stream);
				} finally {
					stream.close();
				}
			} catch (FileNotFoundException e) {
				System.out.printf("FileNotFoundException: %s",e);
			}
		//}
	}
}
for (int n=0; n < graphs.length; n++) {
	try {
		String fileName= String.format("log\\graph_%05d_(%05d).txt",n,recentFrameNumber);
		PrintStream stream= new PrintStream(fileName);
		try {
			graphs[n].dump(stream);
		} finally {
			stream.close();
		};
		fileName= String.format("log\\graph_%05d_(%05d).m",n,recentFrameNumber);
		stream= new PrintStream(fileName);
		try {
			graphs[n].createMatlab(stream);
		} finally {
			stream.close();
		}
	} catch (FileNotFoundException e) {
		System.out.printf("FileNotFoundException: %s",e);
	}
}
}
*/
		ConnectedGraph[] graphs= formAndGetConnectedGraphs();
		Term list1= PrologEmptyList.instance;
		for (int n=graphs.length-1; n >= 0; n--) {
			ConnectedGraph graph= graphs[n];
			ArrayList<ConnectedSegment> connectedSegments= graph.connectedSegments;
			int numberOfSegments= connectedSegments.size();
			if (numberOfSegments <= 0) {
				continue;
			};
			Term list2= PrologEmptyList.instance;
			for (int k=numberOfSegments-1; k >= 0; k--) {
				ConnectedSegment connectedSegment= connectedSegments.get(k);
				int connectedSegmentNumber= connectedSegments.indexOf(connectedSegment);
				TrackSegment trackSegment= connectedSegment.trackSegment;
				Term prologOriginEdges= collectOriginEdges(connectedSegment,connectedSegmentNumber,connectedSegments);
				Term prologBrachEdges= collectBranchEdges(connectedSegment,connectedSegmentNumber,connectedSegments);
				Term prologIdentifier= new PrologInteger(trackSegment.owner);
				long beginningTime= trackSegment.beginningTime;
				int[][] rectangles= trackSegment.rectangles;
				int[] firstRectangle= rectangles[0];
				long frameNumber1= beginningTime+firstRectangle[0];
				long x1= StrictMath.round((firstRectangle[1]+firstRectangle[2])/2.0);
				long y1= StrictMath.round((firstRectangle[3]+firstRectangle[4])/2.0);
				int[] lastRectangle= rectangles[rectangles.length-1];
				long frameNumber2= beginningTime+lastRectangle[0];
				long x2= StrictMath.round((lastRectangle[1]+lastRectangle[2])/2.0);
				long y2= StrictMath.round((lastRectangle[3]+lastRectangle[4])/2.0);
				Term prologSegment= assembleTrackSegment(frameNumber1,x1,y1,frameNumber2,x2,y2,trackSegment);
				Term prologEdge= new PrologSet(
					- SymbolCodes.symbolCode_E_inputs,
					prologOriginEdges,
					new PrologSet(
					- SymbolCodes.symbolCode_E_outputs,
					prologBrachEdges,
					new PrologSet(
					- SymbolCodes.symbolCode_E_identifier,
					prologIdentifier,
					prologSegment)));
				list2= new PrologList(prologEdge,list2);
			};
			list1= new PrologList(list2,list1);
		};
		return list1;
	}
	protected Term assembleTrackSegment(
			long frameNumber1,
			long x1,
			long y1,
			long frameNumber2,
			long x2,
			long y2,
			TrackSegment trackSegment
			) {
		Term prologFrame1= new PrologInteger(frameNumber1);
		Term prologX1= new PrologInteger(x1);
		Term prologY1= new PrologInteger(y1);
		Term prologFrame2= new PrologInteger(frameNumber2);
		Term prologX2= new PrologInteger(x2);
		Term prologY2= new PrologInteger(y2);
		Term list3= formTrackOfBlob(trackSegment);
		Term prologMeanBlobArea= new PrologReal(trackSegment.meanBlobArea);
		Term prologMeanForegroundArea= new PrologReal(trackSegment.meanForegroundArea);
		Term prologMeanCharacteristicLength= new PrologReal(trackSegment.meanCharacteristicLength);
		Term prologMeanSquaredCharacteristicLength= new PrologReal(trackSegment.meanSquaredCharacteristicLength);
		Term prologMeanContourLength= new PrologReal(trackSegment.meanContourLength);
		Term prologWR2Mean= new PrologReal(trackSegment.getWindowedR2Mean());
		Term prologWR2StandardDeviation= new PrologReal(trackSegment.getWindowedR2StandardDeviation());
		Term prologWR2Skewness= new PrologReal(trackSegment.getWindowedR2Skewness());
		Term prologWR2Kurtosis= new PrologReal(trackSegment.getWindowedR2Kurtosis());
		Term prologWR2Cardinality= new PrologInteger(trackSegment.getWindowedR2Cardinality());
		Term prologMeanVelocity= new PrologReal(trackSegment.meanVelocity);
		Term prologTrackSegment= new PrologSet(
			- SymbolCodes.symbolCode_E_frame1,
			prologFrame1,
			new PrologSet(
			- SymbolCodes.symbolCode_E_x1,
			prologX1,
			new PrologSet(
			- SymbolCodes.symbolCode_E_y1,
			prologY1,
			new PrologSet(
			- SymbolCodes.symbolCode_E_frame2,
			prologFrame2,
			new PrologSet(
			- SymbolCodes.symbolCode_E_x2,
			prologX2,
			new PrologSet(
			- SymbolCodes.symbolCode_E_y2,
			prologY2,
			new PrologSet(
			- SymbolCodes.symbolCode_E_coordinates,
			list3,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_blob_area,
			prologMeanBlobArea,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_foreground_area,
			prologMeanForegroundArea,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_characteristic_length,
			prologMeanCharacteristicLength,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_squared_characteristic_length,
			prologMeanSquaredCharacteristicLength,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_contour_length,
			prologMeanContourLength,
			new PrologSet(
			- SymbolCodes.symbolCode_E_wr2_mean,
			prologWR2Mean,
			new PrologSet(
			- SymbolCodes.symbolCode_E_wr2_standard_deviation,
			prologWR2StandardDeviation,
			new PrologSet(
			- SymbolCodes.symbolCode_E_wr2_skewness,
			prologWR2Skewness,
			new PrologSet(
			- SymbolCodes.symbolCode_E_wr2_kurtosis,
			prologWR2Kurtosis,
			new PrologSet(
			- SymbolCodes.symbolCode_E_wr2_cardinality,
			prologWR2Cardinality,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_velocity,
			prologMeanVelocity,
			PrologEmptySet.instance))))))))))))))))));
		return prologTrackSegment;
	}
	protected Term collectOriginEdges(ConnectedSegment connectedSegment, int connectedSegmentNumber, ArrayList<ConnectedSegment> connectedSegments) {
		HashSet<ConnectedSegment> origins= connectedSegment.origins;
		Term result= PrologEmptyList.instance;
		Iterator<ConnectedSegment> iterator= origins.iterator();
		while (iterator.hasNext()) {
			ConnectedSegment entry= iterator.next();
			BigInteger owner= entry.getOwner();
			int entryNumber= connectedSegments.indexOf(entry);
			if (entryNumber < 0) {
				continue;
			} else if (entryNumber==connectedSegmentNumber) {
				continue;
			};
			Term prologEdgeNumber= new PrologInteger(entryNumber+1);
			result= new PrologList(prologEdgeNumber,result);
		};
		return result;
	}
	protected Term collectBranchEdges(ConnectedSegment connectedSegment, int connectedSegmentNumber, ArrayList<ConnectedSegment> connectedSegments) {
		HashSet<ConnectedSegment> branches= connectedSegment.branches;
		Term result= PrologEmptyList.instance;
		Iterator<ConnectedSegment> iterator= branches.iterator();
		while (iterator.hasNext()) {
			ConnectedSegment entry= iterator.next();
			BigInteger owner= entry.getOwner();
			int entryNumber= connectedSegments.indexOf(entry);
			if (entryNumber < 0) {
				continue;
			} else if (entryNumber==connectedSegmentNumber) {
				continue;
			};
			Term prologEdgeNumber= new PrologInteger(entryNumber+1);
			result= new PrologList(prologEdgeNumber,result);
		};
		return result;
	}
	protected ConnectedGraph[] formAndGetConnectedGraphs() {
		if (graphs != null) {
			return graphs;
		} else {
			graphs= formConnectedGraphs();
			if (refuseSlowTracks) {
				refuseSlowTracks(velocityThreshold,distanceThreshold,fuzzyThresholdBorder,graphs);
			};
			alloyConnectedGraphs(graphs);
			return graphs;
		}
	}
	protected ConnectedGraph[] formConnectedGraphs() {
		ArrayList<ConnectedGraph> graphs= new ArrayList<>();
		int numberOfActiveBlobs= blobIdentifiers.length;
		Loop1: for (int n=0; n < numberOfActiveBlobs; n++) {
			BigInteger identifier= blobIdentifiers[n];
			for (int k=0; k < graphs.size(); k++) {
				if (graphs.get(k).containsTrack(identifier)) {
					continue Loop1;
				}
			};
			StableTrack track= tracks.get(identifier);
			if (track != null) {
				track.assembleConnectedGraph(graphs,tracks);
			}
		};
		ConnectedGraph[] array= new ConnectedGraph[graphs.size()];
		graphs.toArray(array);
		return array;
	}
	protected void refuseSlowTracks(double velocityThreshold, double distanceThreshold, double fuzzyThresholdBorder, ConnectedGraph[] array) {
		for (int n=0; n < array.length; n++) {
			array[n].refuseSlowTracks(velocityThreshold,distanceThreshold,fuzzyThresholdBorder,tracks);
		}
	}
	protected void refuseSlowTracksAndReport(double velocityThreshold, double distanceThreshold, double fuzzyThresholdBorder, ConnectedGraph[] array) {
		for (int n=0; n < array.length; n++) {
			array[n].refuseSlowTracksAndReport(velocityThreshold,distanceThreshold,fuzzyThresholdBorder,tracks);
		}
	}
	protected void alloyConnectedGraphs(ConnectedGraph[] array) {
		for (int n=0; n < array.length; n++) {
			array[n].alloy(tracks);
			array[n].collectOriginsAndBranches();
			array[n].deleteEmptySegments();
		}
	}
	protected BufferedImage applyMaskAndCreateImage(BufferedImage image, boolean[] bitMask) {
		int[] deltaMatrix= Arrays.copyOf(contourPixels,contourPixels.length);
		if (makeSquareBlobsInSynthesizedImage) {
			for (int n=0; n < deltaMatrix.length; n++) {
				deltaMatrix[n]= differenceMarker; // Прямоугольники вместо блобов
			}
		} else {
			WritableRaster imageRaster= image.getRaster();
			int vectorLength= imageWidth * imageHeight;
			int[] imagePixels= new int[vectorLength];
			int bandNumber= imageRaster.getNumBands();
			for (int k=0; k < bandNumber; k++) {
				imagePixels= imageRaster.getSamples(0,0,imageWidth,imageHeight,k,imagePixels);
				for (int n=0; n < vectorLength; n++) {
					if (imagePixels[n] == noDifferenceMarker) {
						deltaMatrix[n]= synthesizedImageTransparency;
					}
				};
				imageRaster.setSamples(0,0,imageWidth,imageHeight,k,imagePixels);
			}
		};
		for (int n=0; n < deltaMatrix.length; n++) {
			if (!bitMask[n]) {
				deltaMatrix[n]= synthesizedImageTransparency;
			}
		};
		return extractSelectedPixels(image,deltaMatrix,noDifferenceMarker);
	}
	protected BufferedImage extractSelectedPixels(BufferedImage image, int[] deltaMatrix, int noDifferenceMarker) {
		int type= image.getType();
		WritableRaster imageRaster= image.getRaster();
		switch (type) {
			case BufferedImage.TYPE_INT_ARGB:
			case BufferedImage.TYPE_INT_ARGB_PRE:
			case BufferedImage.TYPE_4BYTE_ABGR:
			case BufferedImage.TYPE_4BYTE_ABGR_PRE:
				imageRaster.setSamples(0,0,imageWidth,imageHeight,3,deltaMatrix);
				break;
			default:
				int vectorLength= imageWidth * imageHeight;
				int[] imagePixels= new int[vectorLength];
				int bandNumber= imageRaster.getNumBands();
				for (int k=0; k < bandNumber; k++) {
					imagePixels= imageRaster.getSamples(0,0,imageWidth,imageHeight,k,imagePixels);
					for (int n=0; n < vectorLength; n++) {
						if (deltaMatrix[n] == noDifferenceMarker) {
							imagePixels[n]= spaceColor;
						}
					};
					imageRaster.setSamples(0,0,imageWidth,imageHeight,k,imagePixels);
				}
		};
		return image;
	}
}
