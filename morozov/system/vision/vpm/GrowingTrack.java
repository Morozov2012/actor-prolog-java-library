// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import morozov.system.vision.vpm.converters.*;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Iterator;
import java.math.BigInteger;

public class GrowingTrack {
	//
	protected BigInteger identifier;
	protected BlobType blobType;
	protected long trackBeginning;
	protected long trackEnd;
	protected long recentSegmentBeginning;
	protected long beginningTimeInMilliseconds;
	protected long endTimeInMilliseconds;
	//
	protected ArrayList<BlobAttributes> recentPoints= new ArrayList<>();
	protected TreeMap<Long,BreakPoint> requestedBreakPoints= new TreeMap<>();
	protected long firstRequestedBreakPoint;
	protected ArrayList<TrackSegment> segments= new ArrayList<>();
	//
	protected int minimalTrackDuration;
	protected int maximalTrackDuration;
	protected double samplingRate;
	protected int r2WindowHalfwidth;
	protected TransformationMatrices transformationMatrices;
	protected boolean applyCharacteristicLengthMedianFiltering;
	protected int characteristicLengthMedianFilterHalfwidth;
	protected boolean applyVelocityMedianFiltering;
	protected int velocityMedianFilterHalfwidth;
	//
	protected boolean isStrong= false;
	protected int numberOfPoints= 0;
	protected boolean isInsensible= false;
	protected boolean isCompleted= false;
	protected boolean isDeposed= false;
	protected ArrayList<DeferredCollision> deferredCollisions= new ArrayList<>();
	//
	///////////////////////////////////////////////////////////////
	//
	public GrowingTrack(
			BigInteger id,
			BlobType type,
			long frameNumber,
			long timeInMilliseconds,
			int minimalDuration,
			int maximalDuration,
			double rate,
			int r2Halfwidth,
			boolean applyFilterToCharacteristicLength,
			int characteristicLengthFilterHalfwidth,
			boolean applyFilterToVelocity,
			int velocityFilterHalfwidth,
			TransformationMatrices tMatrices) {
		//
		identifier= id;
		blobType= type;
		trackBeginning= frameNumber;
		trackEnd= frameNumber;
		recentSegmentBeginning= frameNumber;
		beginningTimeInMilliseconds= timeInMilliseconds;
		endTimeInMilliseconds= timeInMilliseconds;
		minimalTrackDuration= minimalDuration;
		maximalTrackDuration= maximalDuration;
		samplingRate= rate;
		r2WindowHalfwidth= r2Halfwidth;
		applyCharacteristicLengthMedianFiltering= applyFilterToCharacteristicLength;
		characteristicLengthMedianFilterHalfwidth= characteristicLengthFilterHalfwidth;
		applyVelocityMedianFiltering= applyFilterToVelocity;
		velocityMedianFilterHalfwidth= velocityFilterHalfwidth;
		transformationMatrices= tMatrices;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BigInteger getIdentifier() {
		return identifier;
	}
	public long getTrackBeginning() {
		return trackBeginning;
	}
	public long getTrackEnd() {
		return trackEnd;
	}
	public long getBeginningTimeInMilliseconds() {
		return beginningTimeInMilliseconds;
	}
	public long getEndTimeInMilliseconds() {
		return endTimeInMilliseconds;
	}
	//
	public long getCriticalFrameNumber() {
		return trackBeginning + minimalTrackDuration;
	}
	//
	public boolean isStrong() {
		return isStrong;
	}
	public int getNumberOfPoints() {
		return numberOfPoints;
	}
	public boolean isInsensible() {
		return isInsensible;
	}
	public boolean isCompleted() {
		return isCompleted;
	}
	public boolean isDeposed() {
		return isDeposed;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void appendPoint(BlobAttributes attributes) {
		if (!isInsensible && !isCompleted && !isDeposed) {
			long frameNumber= attributes.getFrameNumber();
			long timeInMilliseconds= attributes.getTimeInMilliseconds();
			if (recentPoints.isEmpty()) {
				recentSegmentBeginning= frameNumber;
			};
			recentPoints.add(attributes);
			trackEnd= frameNumber;
			endTimeInMilliseconds= timeInMilliseconds;
			numberOfPoints++;
			prolong(frameNumber,false);
			if (maximalTrackDuration >= 1) {
				if (numberOfPoints > maximalTrackDuration) {
					reduceNumberOfPoints();
				}
			}
		}
	}
	public void prolong(long time, boolean emptyBuffer) {
		if (!isCompleted && !isDeposed) {
			if (isMature(time)) {
				if (!isStrong) {
					isStrong= true;
				};
				implementDeferredOperations(time);
				if (time > recentSegmentBeginning + minimalTrackDuration) {
					createSegments(time,emptyBuffer);
				}
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void reduceNumberOfPoints() {
		if (maximalTrackDuration < 1) {
			return;
		};
		if (reduceNumberOfSegments()) {
			int numberOfElementsToBeRemoved= numberOfPoints - maximalTrackDuration;
			if (numberOfElementsToBeRemoved > 0) {
				if (recentPoints.size() >= numberOfElementsToBeRemoved) {
					recentPoints.subList(0,numberOfElementsToBeRemoved).clear();
					BlobAttributes attributes= recentPoints.get(0);
					long frameNumber= attributes.getFrameNumber();
					trackBeginning= frameNumber;
					beginningTimeInMilliseconds= attributes.getTimeInMilliseconds();
					recentSegmentBeginning= frameNumber;
					numberOfPoints= recentPoints.size();
				}
			}
		}
	}
	public void reduceNumberOfPoints(long minimalTime) {
		if (minimalTime < 0) {
			return;
		};
		if (reduceNumberOfSegments(minimalTime)) {
			int numberOfElementsToBeRemoved= 0;
			for (int k=0; k < recentPoints.size()-1; k++) {
				BlobAttributes attributes= recentPoints.get(k);
				if (attributes.getTimeInMilliseconds() < minimalTime) {
					numberOfElementsToBeRemoved++;
				} else {
					break;
				}
			};
			if (numberOfElementsToBeRemoved > 0) {
				if (recentPoints.size() >= numberOfElementsToBeRemoved) {
					recentPoints.subList(0,numberOfElementsToBeRemoved).clear();
					BlobAttributes attributes= recentPoints.get(0);
					long frameNumber= attributes.getFrameNumber();
					trackBeginning= frameNumber;
					beginningTimeInMilliseconds= attributes.getTimeInMilliseconds();
					recentSegmentBeginning= frameNumber;
					numberOfPoints= recentPoints.size();
				}
			}
		}
	}
	protected boolean reduceNumberOfSegments() {
		if (maximalTrackDuration < 1) {
			return false;
		};
		int numberOfElementsToBeRemoved= numberOfPoints - maximalTrackDuration;
		if (numberOfElementsToBeRemoved > 0) {
			int numberOfSegments= segments.size();
			for (int n=0; n < numberOfSegments; n++) {
				TrackSegment firstSegment= segments.get(0);
				int length= firstSegment.getLength();
				if (length <= numberOfElementsToBeRemoved) {
					boolean nextPointDoesExist= false;
					if (n < numberOfSegments-1) {
						TrackSegment nextSegment= segments.get(1);
						trackBeginning= nextSegment.getSegmentBeginning();
						beginningTimeInMilliseconds= nextSegment.getBeginningTimeInMilliseconds();
						nextPointDoesExist= true;
					} else if (recentPoints.size() > 0) {
						BlobAttributes attributes1= recentPoints.get(0);
						trackBeginning= attributes1.getFrameNumber();
						beginningTimeInMilliseconds= attributes1.getTimeInMilliseconds();
						nextPointDoesExist= true;
					};
					if (nextPointDoesExist) {
						segments.remove(0);
						numberOfPoints= numberOfPoints - length;
						continue;
					} else {
						return false;
					}
				} else {
					return false;
				}
			};
			return true;
		} else {
			return false;
		}
	}
	protected boolean reduceNumberOfSegments(long minimalTime) {
		if (minimalTime < 0) {
			return false;
		};
		// int numberOfElementsToBeRemoved= numberOfPoints - maximalTrackDuration;
		// if (numberOfElementsToBeRemoved > 0) {
		int numberOfSegments= segments.size();
		for (int n=0; n < numberOfSegments; n++) {
			TrackSegment firstSegment= segments.get(0);
			long segmentEndTime= firstSegment.getEndTimeInMilliseconds();
			int length= firstSegment.getLength();
			if (segmentEndTime < minimalTime) {
				boolean nextPointDoesExist= false;
				if (n < numberOfSegments-1) {
					TrackSegment nextSegment= segments.get(1);
					trackBeginning= nextSegment.getSegmentBeginning();
					beginningTimeInMilliseconds= nextSegment.getBeginningTimeInMilliseconds();
					nextPointDoesExist= true;
				} else if (recentPoints.size() > 0) {
					BlobAttributes attributes1= recentPoints.get(0);
					trackBeginning= attributes1.getFrameNumber();
					beginningTimeInMilliseconds= attributes1.getTimeInMilliseconds();
					nextPointDoesExist= true;
				};
				if (nextPointDoesExist) {
					segments.remove(0);
					numberOfPoints= numberOfPoints - length;
					continue;
				} else {
					return false;
				}
			} else {
				return false;
			}
		};
		return true;
	}
	/*
	protected boolean checkNumberOfPoints(int label) {
		int sum= 0;
		int numberOfSegments= segments.size();
		for (int n=0; n < numberOfSegments; n++) {
			TrackSegment segment= segments.get(n);
			int length= segment.getLength();
			sum= sum + length;
		};
		sum= sum + recentPoints.size();
		if (sum != numberOfPoints) {
			System.err.printf("%s!!! sum: %s, numberOfPoints: %s\n",label,sum,numberOfPoints);
			for (int n=0; n < numberOfSegments; n++) {
				TrackSegment segment= segments.get(n);
				int length= segment.getLength();
				System.err.printf("%s!!! segmrnt: %s: length: %s\n",label,n,length);
			};
			System.err.printf("%s!!! recentPoints.size(): %s\n",label,recentPoints.size());
			// return true;
			//
			throw new RuntimeException();
		} else {
			// System.err.printf("sum: %s, numberOfPoints: %s\n",sum,numberOfPoints);
		};
		return false;
	}
	*/
	//
	///////////////////////////////////////////////////////////////
	//
	public void registerCollision(GrowingTrack receiver, long frameNumber, long timeInMilliseconds, TrackSegmentEntryType entryType) {
		if (!isDeposed) {
			if (isStrong && receiver.isStrong) {
				requestBreakPoint(frameNumber,timeInMilliseconds,receiver.identifier,entryType);
				receiver.requestBreakPoint(frameNumber,timeInMilliseconds,identifier,entryType.computeComplementaryType());
			} else {
				long criticalPoint1= getCriticalFrameNumber();
				long criticalPoint2= receiver.getCriticalFrameNumber();
				if (criticalPoint2 > criticalPoint1) {
					criticalPoint1= criticalPoint2;
				};
				deferredCollisions.add(new DeferredCollision(criticalPoint1,receiver,frameNumber,timeInMilliseconds,this,entryType));
			};
		}
	}
	protected void implementDeferredOperations(long frameNumber) {
		if (!deferredCollisions.isEmpty()) {
			for (int n=deferredCollisions.size()-1; n >= 0; n--) {
				DeferredCollision c= deferredCollisions.get(n);
				if (c.getImplementationPoint() < frameNumber) {
					if (c.implement()) {
						deferredCollisions.remove(n);
					}
				}
			}
		}
	}
	public void requestBreakPoint(long frameNumber, long timeInMilliseconds, BigInteger neighbour, TrackSegmentEntryType entryType) {
		if (!isDeposed) {
			if (requestedBreakPoints.isEmpty()) {
				firstRequestedBreakPoint= frameNumber;
			} else if (frameNumber < firstRequestedBreakPoint) {
				firstRequestedBreakPoint= frameNumber;
			};
			BreakPoint point= requestedBreakPoints.get(frameNumber);
			if (point==null) {
				point= new BreakPoint(frameNumber,timeInMilliseconds,neighbour,entryType);
			} else {
				point.addBranch(neighbour,entryType);
			};
			requestedBreakPoints.put(frameNumber,point);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void createSegments(long time, boolean emptyBuffer) {
		int numberOfRecentPoints= recentPoints.size();
		if (time < recentSegmentBeginning + minimalTrackDuration && !emptyBuffer) {
			return;
		};
		if (emptyBuffer) {
			// Returns the least key greater than or equal to
			// the given key, or null if there is no such key.
			Long ceilingKey= requestedBreakPoints.ceilingKey(trackEnd);
			if (ceilingKey==null) {
				BreakPoint point= new BreakPoint(trackEnd,endTimeInMilliseconds);
				requestedBreakPoints.put(trackEnd,point);
			}
		};
		long criticalTime= time - minimalTrackDuration;
		Collection<BreakPoint> breakPointSet= requestedBreakPoints.values();
		Iterator<BreakPoint> iterator= breakPointSet.iterator();
		int firstIndex= 0;
		int lastIndex= -1;
		int numberOfPointsToBeDeleted= 0;
		while (iterator.hasNext()) {
			BreakPoint breakPoint= iterator.next();
			long breakFrameNumber= breakPoint.getFrameNumber();
			long breakTimeInMilliseconds= breakPoint.getTimeInMilliseconds();
			firstRequestedBreakPoint= breakFrameNumber;
			if (breakFrameNumber > criticalTime && !emptyBuffer) {
				break;
			};
			lastIndex= -1;
			for (int n=0; n < numberOfRecentPoints; n++) {
				BlobAttributes p= recentPoints.get(n);
				long t= p.getFrameNumber();
				if (t <= breakFrameNumber) {
					lastIndex= n;
				}
			};
			int length= lastIndex - firstIndex + 1;
			if (length < 0) {
				length= 0;
			};
			if (breakPoint.isPureOrigin()) {
				length= 0;
				firstIndex= firstIndex - 1;
				lastIndex= firstIndex - 1;
			};
			long segmentBeginningPoint;
			long segmentBeginningTime;
			if (length > 0) {
				BlobAttributes p0= recentPoints.get(firstIndex);
				segmentBeginningPoint= p0.getFrameNumber();
				segmentBeginningTime= p0.getTimeInMilliseconds();
			} else {
				segmentBeginningPoint= breakFrameNumber;
				segmentBeginningTime= breakTimeInMilliseconds;
			};
			long segmentEndPoint= segmentBeginningPoint;
			long segmentEndTime= segmentBeginningTime;
			long[] frameNumbers= new long[length];
			BlobAttributes[] blobAttributeArray= new BlobAttributes[length];
			int index= 0;
			for (int n=firstIndex; n <= lastIndex; n++) {
				BlobAttributes p1= recentPoints.get(n);
				blobAttributeArray[index]= p1;
				segmentEndPoint= p1.getFrameNumber();
				segmentEndTime= p1.getTimeInMilliseconds();
				frameNumbers[index]= segmentEndPoint;
				index++;
			};
			numberOfPointsToBeDeleted= numberOfPointsToBeDeleted + length;
			TrackSegment newSegment= new TrackSegment(
				identifier,
				segments.size(),
				segmentBeginningPoint,
				segmentEndPoint,
				breakFrameNumber,
				segmentBeginningTime,
				segmentEndTime,
				blobAttributeArray,
				frameNumbers,
				breakPoint.getEntries(),
				samplingRate,
				r2WindowHalfwidth,
				transformationMatrices,
				applyCharacteristicLengthMedianFiltering,
				characteristicLengthMedianFilterHalfwidth,
				applyVelocityMedianFiltering,
				velocityMedianFilterHalfwidth);
			segments.add(newSegment);
			if (firstIndex <= lastIndex) {
				firstIndex= lastIndex + 1;
			} else {
				firstIndex++;
			};
			iterator.remove();
		};
		recentPoints.subList(0,numberOfPointsToBeDeleted).clear();
		if (!recentPoints.isEmpty()) {
			recentSegmentBeginning= recentPoints.get(0).getFrameNumber();
		} else {
			recentSegmentBeginning= time;
		}
	}
	public boolean isToBeInspected() {
		int pointNumber= recentPoints.size();
		if (pointNumber > 0 && minimalTrackDuration <= pointNumber) {
			return true;
		} else {
			return false;
		}
	}
	protected TrackSegment assembleRecentSegment() {
		int numberOfPoints= recentPoints.size();
		BlobAttributes p0= recentPoints.get(0);
		long segmentBeginningPoint= p0.getFrameNumber();
		long segmentBeginningTime= p0.getTimeInMilliseconds();
		long segmentEndPoint= segmentBeginningPoint;
		long segmentEndTime= segmentBeginningTime;
		long[] frameNumbers= new long[numberOfPoints];
		BlobAttributes[] blobAttributeArray= new BlobAttributes[numberOfPoints];
		for (int n=0; n < numberOfPoints; n++) {
			BlobAttributes p1= recentPoints.get(n);
			blobAttributeArray[n]= p1;
			segmentEndPoint= p1.getFrameNumber();
			segmentEndTime= p1.getTimeInMilliseconds();
			frameNumbers[n]= segmentEndPoint;
		};
		TrackSegment newSegment= new TrackSegment(
				identifier,
				segments.size(),
				segmentBeginningPoint,
				segmentEndPoint,
				segmentEndPoint,
				segmentBeginningTime,
				segmentEndTime,
				blobAttributeArray,
				frameNumbers,
				null,
				samplingRate,
				r2WindowHalfwidth,
				transformationMatrices,
				applyCharacteristicLengthMedianFiltering,
				characteristicLengthMedianFilterHalfwidth,
				applyVelocityMedianFiltering,
				velocityMedianFilterHalfwidth);
		return newSegment;
	}
	//
	public void makeInsensible(long time) {
		prolong(time,true);
		isInsensible= true;
	}
	public void complete(long time) {
		if (!isDeposed && isInsensible && !isCompleted) {
			if (isMature(time)) {
				implementDeferredOperations(time);
				if (deferredCollisions.isEmpty()) {
					isCompleted= true;
				}
			}
		}
	}
	public boolean isMature(long time) {
		return time >= trackBeginning + minimalTrackDuration;
	}
	public void depose() {
		isDeposed= true;
		recentPoints.clear();
		requestedBreakPoints.clear();
		segments.clear();
		deferredCollisions.clear();
	}
	public boolean isToBeCompleted(long time) {
		if (!isCompleted && isInsensible && trackEnd < time - minimalTrackDuration) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isOutOfDate(long time, long maximalTrackRetentionPeriod) {
		if (!isDeposed && isInsensible && isCompleted && trackEnd < time - maximalTrackRetentionPeriod) {
			return true;
		} else {
			return false;
		}
	}
	public StableTrack createStableTrack() {
		ArrayList<TrackSegment> list= new ArrayList<>(segments);
		int numberOfPoints= recentPoints.size();
		if (numberOfPoints > 0) {
			TrackSegment recentSegment= assembleRecentSegment();
			list.add(recentSegment);
		};
		return new StableTrack(
			identifier,
			blobType,
			trackBeginning,
			trackEnd,
			list.toArray(new TrackSegment[0]),
			minimalTrackDuration,
			samplingRate,
			transformationMatrices,
			isStrong,
			isInsensible,
			isCompleted,
			isDeposed);
	}
}
