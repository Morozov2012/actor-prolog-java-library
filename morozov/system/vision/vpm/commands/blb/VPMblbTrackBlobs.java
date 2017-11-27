// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.blb;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.math.BigInteger;

public class VPMblbTrackBlobs extends VPM_FrameCommand {
	//
	protected int minimalTrackDuration= 30;
	protected int maximalTrackDuration= -1;
	protected int maximalBlobInvisibilityInterval= 3;
	protected long maximalTrackRetentionInterval;
	protected double samplingRate= -1;
	protected int r2WindowHalfwidth= 5;
	protected boolean applyCharacteristicLengthMedianFiltering= true;
	protected int characteristicLengthMedianFilterHalfwidth= 5;
	protected boolean applyVelocityMedianFiltering= true;
	protected int velocityMedianFilterHalfwidth= 5;
	//
	///////////////////////////////////////////////////////////////
	//
	protected int imageWidth;
	protected int imageHeight;
	//
	protected long frameNumber;
	protected long timeInMilliseconds;
	//
	///////////////////////////////////////////////////////////////
	//
	protected BlobType[] currentBlobTypes;
	protected int[][] currentBlobRectangles;
	protected BlobAttributes[] currentBlobAttributes;
	protected BigInteger[] currentBlobIdentifiers;
	//
	protected int[] currentTrackDurations;
	//
	protected BlobType[] previousBlobTypes;
	protected int[][] previousBlobRectangles;
	protected BigInteger[] previousBlobIdentifiers;
	//
	protected int[] previousTrackDurations;
	//
	protected BlobType[] invisibleBlobTypes;
	protected int[][] invisibleBlobRectangles;
	protected BigInteger[] invisibleBlobIdentifiers;
	protected int[] invisibleBlobDelays;
	//
	protected int[] invisibleTrackDurations;
	//
	///////////////////////////////////////////////////////////////
	//
	protected BigInteger recentBlobIdentifier= BigInteger.ZERO;
	protected HashMap<BigInteger,GrowingTrack> tracks= new HashMap<>();
	//
	///////////////////////////////////////////////////////////////
	//
	protected TransformationMatrices transformationMatrices= null;
	//
	protected static BlobIntersectionsComparator comparator= new BlobIntersectionsComparator();
	//
	///////////////////////////////////////////////////////////////
	//
	public VPMblbTrackBlobs() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void execute(VPM vpm) {
		trackBlobs(vpm);
	}
	public void trackBlobs(BlobHolder vpm) {
		//
		previousBlobTypes= currentBlobTypes;
		previousBlobRectangles= currentBlobRectangles;
		previousBlobIdentifiers= currentBlobIdentifiers;
		previousTrackDurations= currentTrackDurations;
		//
		imageWidth= vpm.getOperationalImageWidth();
		imageHeight= vpm.getOperationalImageHeight();
		frameNumber= vpm.getRecentFrameNumber();
		timeInMilliseconds= vpm.getRecentTimeInMilliseconds();
		transformationMatrices= vpm.getTransformationMatrices();
		currentBlobTypes= vpm.getBlobTypes();
		// currentBlobRectangles= vpm.getBlobRectangles();
		currentBlobAttributes= vpm.getBlobAttributes();
		//
		minimalTrackDuration= vpm.getMinimalTrackDuration();
		maximalTrackDuration= vpm.getMaximalTrackDuration();
		maximalBlobInvisibilityInterval= vpm.getMaximalBlobInvisibilityInterval();
		maximalTrackRetentionInterval= vpm.getMaximalTrackRetentionInterval();
		// inverseTransformationMatrix= vpm.getInverseTransformationMatrix();
		samplingRate= vpm.getSamplingRate();
		r2WindowHalfwidth= vpm.getR2WindowHalfwidth();
		applyCharacteristicLengthMedianFiltering= vpm.getCharacteristicLengthMedianFilteringMode();
		characteristicLengthMedianFilterHalfwidth= vpm.getCharacteristicLengthMedianFilterHalfwidth();
		applyVelocityMedianFiltering= vpm.getVelocityMedianFilteringMode();
		velocityMedianFilterHalfwidth= vpm.getVelocityMedianFilterHalfwidth();
		trackBlobs();
		vpm.setTracks(tracks);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// IDENTIFY BLOBS
	public void trackBlobs() {
		if (currentBlobTypes==null) {
			return;
		};
		// CHECK PREVIOUS BLOBS
		if (previousBlobRectangles==null) {
			previousBlobRectangles= new int[0][0];
		};
		int numberOfActiveBlobs= currentBlobTypes.length;
		currentBlobRectangles= new int[numberOfActiveBlobs][4];
		for (int k=0; k < numberOfActiveBlobs; k++) {
			BlobAttributes attributes= currentBlobAttributes[k];
			currentBlobRectangles[k][0]= attributes.getX1();
			currentBlobRectangles[k][1]= attributes.getX2();
			currentBlobRectangles[k][2]= attributes.getY1();
			currentBlobRectangles[k][3]= attributes.getY2();
		};
		currentBlobIdentifiers= new BigInteger[numberOfActiveBlobs];
		for (int k=0; k < numberOfActiveBlobs; k++) {
			currentBlobIdentifiers[k]= BigInteger.ZERO;
		};
		currentTrackDurations= new int[numberOfActiveBlobs];
		int numberOfPreviousBlobs= previousBlobRectangles.length;
		BlobIntersection[] blobIntersections1= new BlobIntersection[numberOfActiveBlobs*numberOfPreviousBlobs];
		int numberOfIntersections1= 0;
		int[] collisionCounters1= new int[numberOfActiveBlobs];
		int[] collisionCounters2= new int[numberOfPreviousBlobs];
		for (int n=0; n < numberOfActiveBlobs; n++) {
			BlobType type1= currentBlobTypes[n];
			int x11= currentBlobRectangles[n][0];
			int x12= currentBlobRectangles[n][1];
			int y11= currentBlobRectangles[n][2];
			int y12= currentBlobRectangles[n][3];
			for (int k=0; k < numberOfPreviousBlobs; k++) {
				BlobType type2= previousBlobTypes[k];
				if (!type1.equals(type2)) {
					continue;
				};
				int x21= previousBlobRectangles[k][0];
				int x22= previousBlobRectangles[k][1];
				int x31= x11;
				int x32= x12;
				if (x31 < x21) {
					x31= x21;
				};
				if (x32 > x22) {
					x32= x22;
				};
				int widthMinusOne3= x32 - x31;
				if (widthMinusOne3 < 0) {
					widthMinusOne3= -1;
				};
				int y21= previousBlobRectangles[k][2];
				int y22= previousBlobRectangles[k][3];
				int y31= y11;
				int y32= y12;
				if (y31 < y21) {
					y31= y21;
				};
				if (y32 > y22) {
					y32= y22;
				};
				int heightMinusOne3= y32 - y31;
				if (heightMinusOne3 < 0) {
					heightMinusOne3= -1;
				};
				int commonAreaSize= (widthMinusOne3 + 1) * (heightMinusOne3 + 1);
				if (commonAreaSize > 0) {
					collisionCounters1[n]++;
					collisionCounters2[k]++;
					BigInteger identifier2= previousBlobIdentifiers[k];
					GrowingTrack track= tracks.get(identifier2);
					if (track != null) {
						boolean isStrongIntersection= track.isStrong();
						blobIntersections1[numberOfIntersections1]= new BlobIntersection(commonAreaSize,n,k,isStrongIntersection);
						numberOfIntersections1++;
					}
				}
			}
		};
		Arrays.sort(blobIntersections1,0,numberOfIntersections1,comparator);
		BigInteger[] usedIdentifiers1= new BigInteger[numberOfActiveBlobs];
		int numberOfUsedIdentifiers1= 0;
		boolean[] usedPreviousBlobs= new boolean[numberOfPreviousBlobs];
		int numberOfUsedPreviousBlobs= 0;
		Loop2: for (int n=0; n < numberOfIntersections1; n++) {
			int index1= blobIntersections1[n].getIndex1();
			if (currentBlobIdentifiers[index1].compareTo(BigInteger.ZERO) > 0) {
				continue;
			};
			int index2= blobIntersections1[n].getIndex2();
			if (usedPreviousBlobs[index2]) {
				continue;
			};
			BigInteger identifier= previousBlobIdentifiers[index2];
			if (identifier.compareTo(BigInteger.ZERO) > 0) {
				for (int k=0; k < numberOfUsedIdentifiers1; k++) {
					if (usedIdentifiers1[k].equals(identifier)) {
						continue Loop2;
					}
				};
				currentBlobIdentifiers[index1]= identifier;
				int previousDuration= previousTrackDurations[index2];
				if (previousDuration < Integer.MAX_VALUE) {
					previousDuration++;
				};
				currentTrackDurations[index1]= previousDuration;
				usedIdentifiers1[numberOfUsedIdentifiers1]= identifier;
				numberOfUsedIdentifiers1++;
				usedPreviousBlobs[index2]= true;
				numberOfUsedPreviousBlobs++;
			}
		};
		// CHECK INVISIBLE BLOBS
		if (invisibleBlobRectangles==null) {
			invisibleBlobRectangles= new int[0][4];
		};
		int numberOfInvisibleBlobs= invisibleBlobRectangles.length;
		int numberOfRemainderBlobs= numberOfActiveBlobs - numberOfUsedIdentifiers1;
		BlobIntersection[] blobIntersections2= new BlobIntersection[numberOfRemainderBlobs*numberOfInvisibleBlobs];
		int numberOfIntersections2= 0;
		boolean[] usedInvisibleBlobs= new boolean[numberOfInvisibleBlobs];
		boolean[] deletedBlobs= new boolean[numberOfInvisibleBlobs];
		for (int n=0; n < numberOfActiveBlobs; n++) {
			BlobType type1= currentBlobTypes[n];
			int x11= currentBlobRectangles[n][0];
			int x12= currentBlobRectangles[n][1];
			int y11= currentBlobRectangles[n][2];
			int y12= currentBlobRectangles[n][3];
			for (int k=0; k < numberOfInvisibleBlobs; k++) {
				BlobType type2= invisibleBlobTypes[k];
				if (!type1.equals(type2)) {
					continue;
				};
				int x21= invisibleBlobRectangles[k][0];
				int x22= invisibleBlobRectangles[k][1];
				int x31= x11;
				int x32= x12;
				if (x31 < x21) {
					x31= x21;
				};
				if (x32 > x22) {
					x32= x22;
				};
				int widthMinusOne3= x32 - x31;
				if (widthMinusOne3 < 0) {
					widthMinusOne3= -1;
				};
				int y21= invisibleBlobRectangles[k][2];
				int y22= invisibleBlobRectangles[k][3];
				int y31= y11;
				int y32= y12;
				if (y31 < y21) {
					y31= y21;
				};
				if (y32 > y22) {
					y32= y22;
				};
				int heightMinusOne3= y32 - y31;
				if (heightMinusOne3 < 0) {
					heightMinusOne3= -1;
				};
				int commonAreaSize= (widthMinusOne3 + 1) * (heightMinusOne3 + 1);
				if (commonAreaSize > 0) {
					if (currentBlobIdentifiers[n].compareTo(BigInteger.ZERO) > 0) {
						usedInvisibleBlobs[k]= true;
						deletedBlobs[k]= true;
					} else {
						blobIntersections2[numberOfIntersections2]= new BlobIntersection(commonAreaSize,n,k,true);
						numberOfIntersections2++;
					}
				}
			}
		};
		Arrays.sort(blobIntersections2,0,numberOfIntersections2,comparator);
		Loop3: for (int n=0; n < numberOfIntersections2; n++) {
			int index1= blobIntersections2[n].getIndex1();
			if (currentBlobIdentifiers[index1].compareTo(BigInteger.ZERO) > 0) {
				continue;
			};
			int index2= blobIntersections2[n].getIndex2();
			if (usedInvisibleBlobs[index2]) {
				continue;
			};
			BigInteger identifier= invisibleBlobIdentifiers[index2];
			if (identifier.compareTo(BigInteger.ZERO) > 0) {
				for (int k=0; k < numberOfUsedIdentifiers1; k++) {
					if (usedIdentifiers1[k].equals(identifier)) {
						continue Loop3;
					}
				};
				currentBlobIdentifiers[index1]= identifier;
				int invisibleDuration= invisibleTrackDurations[index2];
				if (invisibleDuration < Integer.MAX_VALUE) {
					invisibleDuration++;
				};
				currentTrackDurations[index1]= invisibleDuration;
				usedIdentifiers1[numberOfUsedIdentifiers1]= identifier;
				numberOfUsedIdentifiers1++;
				usedInvisibleBlobs[index2]= true;
			}
		};
		// CREATE NEW TRACKS
		for (int k=0; k < numberOfActiveBlobs; k++) {
			if (currentBlobIdentifiers[k].compareTo(BigInteger.ZERO) <= 0) {
				recentBlobIdentifier= recentBlobIdentifier.add(BigInteger.ONE);
				currentBlobIdentifiers[k]= recentBlobIdentifier;
				BlobType blobType= currentBlobTypes[k];
				GrowingTrack track= new GrowingTrack(
					recentBlobIdentifier,
					blobType,
					frameNumber,
					timeInMilliseconds,
					minimalTrackDuration,
					maximalTrackDuration,
					samplingRate,
					r2WindowHalfwidth,
					applyCharacteristicLengthMedianFiltering,
					characteristicLengthMedianFilterHalfwidth,
					applyVelocityMedianFiltering,
					velocityMedianFilterHalfwidth,
					transformationMatrices);
				tracks.put(recentBlobIdentifier,track);
			}
		};
		// PREPARE DEFERRED OPERATIONS
		// Prepare creation of inputs
		for (int n=0; n < numberOfIntersections1; n++) {
			int index1a= blobIntersections1[n].getIndex1();
			if (collisionCounters1[index1a] > 1) {
				int index2a= blobIntersections1[n].getIndex2();
				BigInteger identifier2a= previousBlobIdentifiers[index2a];
				GrowingTrack track2a= tracks.get(identifier2a);
				if (track2a != null) {
					for (int k=0; k < numberOfIntersections1; k++) {
						int index1b= blobIntersections1[k].getIndex1();
						if (index1a != index1b) {
							continue;
						};
						int index2b= blobIntersections1[k].getIndex2();
						if (index2a==index2b) {
							continue;
						};
						BigInteger identifier2b= previousBlobIdentifiers[index2b];
						GrowingTrack track2b= tracks.get(identifier2b);
						if (track2b != null) {
							track2b.registerCollision(track2a,frameNumber,timeInMilliseconds,TrackSegmentEntryType.INPUT);
						}
					}
				}
			}
		};
		// Prepare creation of outputs
		for (int n=0; n < numberOfIntersections1; n++) {
			int index2= blobIntersections1[n].getIndex2();
			if (collisionCounters2[index2] > 1) {
				BigInteger identifier2= previousBlobIdentifiers[index2];
				int index1= blobIntersections1[n].getIndex1();
				BigInteger identifier1= currentBlobIdentifiers[index1];
				if (!identifier1.equals(identifier2)) {
					GrowingTrack track1= tracks.get(identifier1);
					if (track1 != null) {
						GrowingTrack track2= tracks.get(identifier2);
						if (track2 != null) {
							track2.registerCollision(track1,frameNumber,timeInMilliseconds,TrackSegmentEntryType.OUTPUT);
						}
					}
				}
			}
		};
		// PROLONG & DELETE INVISIBLE BLOBS
		int numberOfProlongedBlobs= 0;
		for (int k=0; k < numberOfInvisibleBlobs; k++) {
			if (usedInvisibleBlobs[k]) {
				continue;
			};
			if (invisibleBlobDelays[k] > maximalBlobInvisibilityInterval) {
				deletedBlobs[k]= true;
				continue;
			};
			numberOfProlongedBlobs++;
		};
		int numberOfUnusedPreviousBlobs= numberOfPreviousBlobs - numberOfUsedPreviousBlobs;
		numberOfProlongedBlobs= numberOfProlongedBlobs + numberOfUnusedPreviousBlobs;
		BlobType[] prolongedBlobTypes= new BlobType[numberOfProlongedBlobs];
		int[][] prolongedBlobRectangles= new int[numberOfProlongedBlobs][4];
		BigInteger[] prolongedBlobIdentifiers= new BigInteger[numberOfProlongedBlobs];
		int[] prolongedBlobDelays= new int[numberOfProlongedBlobs];
		int[] prolongedTrackDurations= new int[numberOfProlongedBlobs];
		numberOfProlongedBlobs= 0;
		for (int k=0; k < numberOfPreviousBlobs; k++) {
			if (usedPreviousBlobs[k]) {
				continue;
			};
			prolongedBlobTypes[numberOfProlongedBlobs]= previousBlobTypes[k];
			for (int m=0; m < 4; m++) {
				prolongedBlobRectangles[numberOfProlongedBlobs][m]= previousBlobRectangles[k][m];
			};
			prolongedBlobIdentifiers[numberOfProlongedBlobs]= previousBlobIdentifiers[k];
			prolongedBlobDelays[numberOfProlongedBlobs]= 1;
			prolongedTrackDurations[numberOfProlongedBlobs]= previousTrackDurations[k];
			numberOfProlongedBlobs++;
		};
		for (int k=0; k < numberOfInvisibleBlobs; k++) {
			if (usedInvisibleBlobs[k]) {
				continue;
			};
			if (invisibleBlobDelays[k] > maximalBlobInvisibilityInterval) {
				deletedBlobs[k]= true;
				continue;
			};
			prolongedBlobTypes[numberOfProlongedBlobs]= invisibleBlobTypes[k];
			for (int m=0; m < 4; m++) {
				prolongedBlobRectangles[numberOfProlongedBlobs][m]= invisibleBlobRectangles[k][m];
			};
			prolongedBlobIdentifiers[numberOfProlongedBlobs]= invisibleBlobIdentifiers[k];
			prolongedBlobDelays[numberOfProlongedBlobs]= invisibleBlobDelays[k] + 1;
			prolongedTrackDurations[numberOfProlongedBlobs]= invisibleTrackDurations[k];
			numberOfProlongedBlobs++;
		};
		// STOP TRACKS
		for (int k=0; k < numberOfInvisibleBlobs; k++) {
			BigInteger identifier= invisibleBlobIdentifiers[k];
			GrowingTrack track= tracks.get(identifier);
			if (track != null) {
				if (deletedBlobs[k]) {
					if (track.isStrong()) {
						track.makeInsensible(frameNumber);
					} else {
						track.depose();
						tracks.remove(identifier);
					}
				} else if (!usedInvisibleBlobs[k]) {
					track.prolong(frameNumber,false);
				}
			}
		};
		// STORE PROLONGED BLOBS
		invisibleBlobTypes= prolongedBlobTypes;
		invisibleBlobRectangles= prolongedBlobRectangles;
		invisibleBlobIdentifiers= prolongedBlobIdentifiers;
		invisibleBlobDelays= prolongedBlobDelays;
		invisibleTrackDurations= prolongedTrackDurations;
		// IDENTIFY OBJECTS
		HashSet<BigInteger> tracksToBeInspected= new HashSet<>();
		// ERASE OLD TRACKS
		Set<BigInteger> trackIdentifiers= tracks.keySet();
		Iterator<BigInteger> trackIterator= trackIdentifiers.iterator();
		while (trackIterator.hasNext()) {
			BigInteger identifier= trackIterator.next();
			GrowingTrack track= tracks.get(identifier);
			if (track != null) {
				if (track.isToBeCompleted(frameNumber)) {
					tracksToBeInspected.add(identifier);
					track.complete(frameNumber);
				} else if (track.isOutOfDate(frameNumber,maximalTrackRetentionInterval)) {
					track.depose();
					trackIterator.remove();
				}
			}
		};
		// UPDATE TRACKS
		for (int k=0; k < numberOfActiveBlobs; k++) {
			BigInteger identifier= currentBlobIdentifiers[k];
			BlobAttributes blobAttributes= currentBlobAttributes[k];
			GrowingTrack track= tracks.get(identifier);
			if (track != null) {
				track.appendPoint(blobAttributes);
				tracks.put(identifier,track);
			}
		}
	}
	//
	public void forgetStatistics() {
		recentBlobIdentifier= BigInteger.ZERO;
		currentBlobTypes= null;
		currentBlobRectangles= null;
		currentBlobAttributes= null;
		currentBlobIdentifiers= null;
		currentTrackDurations= null;
		previousBlobTypes= null;
		previousBlobRectangles= null;
		previousBlobIdentifiers= null;
		previousTrackDurations= null;
		invisibleBlobTypes= null;
		invisibleBlobRectangles= null;
		invisibleBlobIdentifiers= null;
		invisibleBlobDelays= null;
		invisibleTrackDurations= null;
	}
}
