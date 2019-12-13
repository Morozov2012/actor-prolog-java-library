// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import target.*;

import morozov.system.converters.*;
import morozov.terms.*;

import java.awt.image.WritableRaster;
import java.awt.Graphics2D;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.math.BigInteger;

public abstract class GenericVideoProcessingMachineSnapshot {
	//
	protected int synthesizedImageTransparency;
	protected boolean makeRectangularBlobsInSynthesizedImage;
	//
	protected long recentFrameNumber;
	protected long recentTimeInMilliseconds;
	protected int operationalImageWidth;
	protected int operationalImageHeight;
	protected int operationalVectorLength;
	protected java.awt.image.BufferedImage recentImage;
	protected java.awt.image.BufferedImage preprocessedImage;
	protected boolean[] foregroundMask;
	//
	protected VPM_FrameNumberAndTime[] arrayOfTime;
	protected HashMap<BigInteger,StableTrack> tracks= new HashMap<>();
	protected ConnectedGraph[] graphs;
	//
	protected java.awt.image.BufferedImage foregroundImage;
	protected java.awt.image.BufferedImage synthesizedImage;
	//
	protected Term prologBlobs;
	protected Term prologTracks;
	protected Term prologChronicle;
	protected Term prologConnectedGraphs;
	//
	///////////////////////////////////////////////////////////////
	//
	public static final int noTransparency= 255;
	public static final int totalTransparency= 0;
	//
	///////////////////////////////////////////////////////////////
	//
	public GenericVideoProcessingMachineSnapshot(
			long frameNumber,
			long timeInMilliseconds,
			int imageWidth,
			int imageHeight,
			java.awt.image.BufferedImage rImage,
			java.awt.image.BufferedImage pImage,
			boolean[] mask,
			ArrayList<VPM_FrameNumberAndTime> timeArray,
			HashMap<BigInteger,GrowingTrack> currentTracks,
			int transparency,
			boolean makeRectangularBlobs
			) {
		recentFrameNumber= frameNumber;
		recentTimeInMilliseconds= timeInMilliseconds;
		operationalImageWidth= imageWidth;
		operationalImageHeight= imageHeight;
		operationalVectorLength= operationalImageWidth * operationalImageHeight;
		recentImage= rImage;
		preprocessedImage= pImage;
		foregroundMask= mask;
		arrayOfTime= timeArray.toArray(new VPM_FrameNumberAndTime[timeArray.size()]);
		Set<BigInteger> trackIdentifiers= currentTracks.keySet();
		Iterator<BigInteger> trackIdentifiersIterator= trackIdentifiers.iterator();
		while (trackIdentifiersIterator.hasNext()) {
			BigInteger trackIdentifier= trackIdentifiersIterator.next();
			GrowingTrack track= currentTracks.get(trackIdentifier);
			tracks.put(trackIdentifier,track.createStableTrack());
		};
		synthesizedImageTransparency= transparency;
		makeRectangularBlobsInSynthesizedImage= makeRectangularBlobs;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public HashMap<BigInteger,StableTrack> getTracks() {
		return tracks;
	}
	public void setTracks(HashMap<BigInteger,StableTrack> value) {
		tracks= value;
		prologTracks= null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public void setSynthesizedImageTransparency(int value) {
		if (synthesizedImageTransparency != value) {
			synthesizedImageTransparency= value;
			synthesizedImage= null;
		}
	}
	synchronized public void setSynthesizedImageRectangularBlobsMode(boolean value) {
		if (makeRectangularBlobsInSynthesizedImage != value) {
			makeRectangularBlobsInSynthesizedImage= value;
			synthesizedImage= null;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getRecentFrameNumber() {
		return recentFrameNumber;
	}
	public long getRecentTimeInMilliseconds() {
		return recentTimeInMilliseconds;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public java.awt.image.BufferedImage getRecentImage() {
		return recentImage;
	}
	//
	public java.awt.image.BufferedImage getPreprocessedImage() {
		if (preprocessedImage==null) {
			return getRecentImage();
		} else {
			return preprocessedImage;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public java.awt.image.BufferedImage getForegroundImage() {
		createForegroundImageIfNecessary();
		return foregroundImage;
	}
	//
	abstract protected void createForegroundImageIfNecessary();
	//
	protected void createForegroundMaskIfNecessary() {
		if (foregroundMask==null) {
			foregroundMask= new boolean[operationalVectorLength];
			Arrays.fill(foregroundMask,true);
		}
	}
	//
	protected void fillUpForegroundImageWithAllChannels(int[] alphaPixels) {
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
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public java.awt.image.BufferedImage getSynthesizedImage() {
		createSynthesizedImageIfNecessary();
		return synthesizedImage;
	}
	//
	protected void createSynthesizedImageIfNecessary() {
		if (synthesizedImage==null) {
			createForegroundMaskIfNecessary();
			int[] alphaPixels= new int[operationalVectorLength];
			if (makeRectangularBlobsInSynthesizedImage) {
				formAndGetConnectedGraphs();
				boolean[] bitMask= new boolean[operationalVectorLength];
				for (int n=0; n < graphs.length; n++) {
					graphs[n].enablePixels(recentFrameNumber,bitMask,operationalImageWidth,operationalImageHeight,tracks);
				};
				for (int k=0; k < operationalVectorLength; k++) {
					if (bitMask[k]) {
						alphaPixels[k]= noTransparency;
					} else {
						alphaPixels[k]= synthesizedImageTransparency;
					}
				}
			} else {
				for (int k=0; k < operationalVectorLength; k++) {
					if (foregroundMask[k]) {
						alphaPixels[k]= noTransparency;
					} else {
						alphaPixels[k]= synthesizedImageTransparency;
					}
				}
			};
			synthesizedImage= new java.awt.image.BufferedImage(operationalImageWidth,operationalImageHeight,java.awt.image.BufferedImage.TYPE_4BYTE_ABGR);
			fillUpSynthesizedImage(alphaPixels);
		}
	}
	//
	protected void fillUpSynthesizedImage(int[] alphaPixels) {
		Graphics2D g2= synthesizedImage.createGraphics();
		try {
			if (preprocessedImage != null) {
				g2.drawImage(preprocessedImage,0,0,null);
			} else {
				g2.drawImage(recentImage,0,0,null);
			}
		} finally {
			g2.dispose();
		};
		WritableRaster imageRaster= synthesizedImage.getRaster();
		imageRaster.setSamples(0,0,operationalImageWidth,operationalImageHeight,3,alphaPixels);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getPrologBlobs() {
		if (prologBlobs != null) {
			return prologBlobs;
		};
		prologBlobs= createPrologBlobs();
		return prologBlobs;
	}
	abstract protected Term createPrologBlobs();
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getPrologTracks() {
		if (prologTracks != null) {
			return prologTracks;
		};
		if (tracks==null) {
			prologTracks= PrologEmptyList.instance;
			return prologTracks;
		};
		Set<BigInteger> trackIdentifiers= tracks.keySet();
		Iterator<BigInteger> trackIdentifiersIterator= trackIdentifiers.iterator();
		prologTracks= PrologEmptyList.instance;
		while (trackIdentifiersIterator.hasNext()) {
			BigInteger trackIdentifier= trackIdentifiersIterator.next();
			StableTrack track= tracks.get(trackIdentifier);
			Term prologTrack= track.toTerm();
			prologTracks= new PrologList(prologTrack,prologTracks);
		};
		return prologTracks;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getPrologChronicle() {
		if (prologChronicle != null) {
			return prologChronicle;
		};
		if (tracks==null) {
			prologChronicle= PrologEmptyList.instance;
			return prologChronicle;
		};
		prologChronicle= PrologEmptyList.instance;
		for (int k=0; k < arrayOfTime.length; k++) {
			VPM_FrameNumberAndTime frameNumberAndTime= arrayOfTime[k];
			Term frameTerm= chronicleFrameToTerm(frameNumberAndTime);
			prologChronicle= new PrologList(frameTerm,prologChronicle);
		};
		return prologChronicle;
	}
	//
	public Term chronicleFrameToTerm(VPM_FrameNumberAndTime frameNumberAndTime) {
		long frameNumber= frameNumberAndTime.getFrameNumber();
		long frameTime= frameNumberAndTime.getTime();
		Term termFrameNumber= new PrologInteger(frameNumber);
		Term termTime= new PrologInteger(frameTime);
		ArrayList<Term> arrayOfBlobs= new ArrayList<>();
		Set<BigInteger> trackKeySet= tracks.keySet();
		Iterator<BigInteger> trackKeySetIterator= trackKeySet.iterator();
		while (trackKeySetIterator.hasNext()) {
			BigInteger identifier= trackKeySetIterator.next();
			StableTrack track= tracks.get(identifier);
			Term blobTerm= track.getBlobTerm(frameNumber);
			if (blobTerm != null) {
				arrayOfBlobs.add(blobTerm);
			}
		};
		Term termBlobs= GeneralConverters.arrayListToTerm(arrayOfBlobs);
		Term result= PrologEmptySet.instance;
		result= new PrologSet(-SymbolCodes.symbolCode_E_blobs,termBlobs,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_time,termTime,result);
		result= new PrologSet(-SymbolCodes.symbolCode_E_frame,termFrameNumber,result);
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	synchronized public Term getPrologConnectedGraphs() {
		if (prologConnectedGraphs != null) {
			return prologConnectedGraphs;
		};
		ConnectedGraph[] currentGraphs= formAndGetConnectedGraphs();
		prologConnectedGraphs= PrologEmptyList.instance;
		for (int n=currentGraphs.length-1; n >= 0; n--) {
			ConnectedGraph graph= currentGraphs[n];
			ArrayList<ConnectedSegment> connectedSegments= graph.getConnectedSegments();
			int numberOfSegments= connectedSegments.size();
			if (numberOfSegments <= 0) {
				continue;
			};
			Term list2= PrologEmptyList.instance;
			for (int k=numberOfSegments-1; k >= 0; k--) {
				ConnectedSegment connectedSegment= connectedSegments.get(k);
				Term prologEdge= connectedSegment.toTerm(connectedSegments);
				list2= new PrologList(prologEdge,list2);
			};
			prologConnectedGraphs= new PrologList(list2,prologConnectedGraphs);
		};
		return prologConnectedGraphs;
	}
	//
	protected ConnectedGraph[] formAndGetConnectedGraphs() {
		if (graphs != null) {
			return graphs;
		} else {
			graphs= formConnectedGraphs();
			if (getRefuseSlowTracks()) {
				refuseSlowTracks(getVelocityThreshold(),getDistanceThreshold(),getFuzzyThresholdBorder(),graphs);
			};
			alloyConnectedGraphs(graphs);
			return graphs;
		}
	}
	//
	abstract protected boolean getRefuseSlowTracks();
	abstract protected double getVelocityThreshold();
	abstract protected double getDistanceThreshold();
	abstract protected double getFuzzyThresholdBorder();
	//
	protected ConnectedGraph[] formConnectedGraphs() {
		ArrayList<ConnectedGraph> currentGraphs= new ArrayList<>();
		Set<BigInteger> trackIdentifiers= tracks.keySet();
		Iterator<BigInteger> trackIdentifiersIterator= trackIdentifiers.iterator();
		Loop1: while (trackIdentifiersIterator.hasNext()) {
			BigInteger trackIdentifier= trackIdentifiersIterator.next();
			for (int k=0; k < currentGraphs.size(); k++) {
				if (currentGraphs.get(k).containsTrack(trackIdentifier)) {
					continue Loop1;
				}
			};
			StableTrack track= tracks.get(trackIdentifier);
			if (track != null) {
				track.assembleConnectedGraph(currentGraphs,tracks);
			}
		};
		ConnectedGraph[] array= new ConnectedGraph[currentGraphs.size()];
		currentGraphs.toArray(array);
		return array;
	}
	//
	protected void refuseSlowTracks(double velocityThreshold, double distanceThreshold, double fuzzyThresholdBorder, ConnectedGraph[] array) {
		for (int n=0; n < array.length; n++) {
			array[n].refuseSlowTracks(velocityThreshold,distanceThreshold,fuzzyThresholdBorder,tracks);
		}
	}
	//
	protected void refuseSlowTracksAndReport(double velocityThreshold, double distanceThreshold, double fuzzyThresholdBorder, ConnectedGraph[] array) {
		for (int n=0; n < array.length; n++) {
			array[n].refuseSlowTracksAndReport(velocityThreshold,distanceThreshold,fuzzyThresholdBorder,tracks);
		}
	}
	//
	protected void alloyConnectedGraphs(ConnectedGraph[] array) {
		for (int n=0; n < array.length; n++) {
			array[n].alloy(tracks);
			array[n].collectOriginsAndBranches();
			array[n].deleteEmptySegments();
			array[n].deleteSlowSegments();
		}
	}
}
