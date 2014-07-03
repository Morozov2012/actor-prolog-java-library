// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Iterator;
import java.math.BigInteger;
import java.io.PrintStream;

class GrowingTrack {
	//
	public BigInteger identifier;
	public long beginningTime;
	public long endTime;
	public long recentSegmentBeginning;
	public ArrayList<BlobAttributes> recentPoints= new ArrayList<>();
	public TreeMap<Long,BreakPoint> requestedBreakPoints= new TreeMap<>();
	public long firstRequestedBreakPoint;
	public ArrayList<TrackSegment> segments= new ArrayList<>();
	public int minimalTrackDuration;
	public int maximalRarefactionOfObject;
	public double[][] inverseMatrix;
	public double samplingRate;
	public int r2WindowHalfwidth;
	public double[][] characteristicMatrix;
	public boolean applyCharacteristicLengthMedianFiltering;
	public int characteristicLengthMedianFilterHalfwidth;
	public boolean applyVelocityMedianFiltering;
	public int velocityMedianFilterHalfwidth;
	public boolean isStrong= false;
	public int numberOfPoints= 0;
	public int numberOfRarefiedHistograms= 0;
	public boolean isRarefied= false;
	public boolean isInsensible= false;
	public boolean isCompleted= false;
	public boolean isDeposed= false;
	public ArrayList<DeferredCollision> deferredCollisions= new ArrayList<>();
	public static final int nBins= 256;
	//
	public GrowingTrack(BigInteger id, long time, int duration, int maximalRarefaction, double[][] iMatrix, double rate, int r2Halfwidth, double[][] cMatrix, boolean applyFilterToCharacteristicLength, int characteristicLengthFilterHalfwidth, boolean applyFilterToVelocity, int velocityFilterHalfwidth) {
		identifier= id;
		beginningTime= time;
		endTime= time;
		recentSegmentBeginning= time;
		minimalTrackDuration= duration;
		maximalRarefactionOfObject= maximalRarefaction;
		inverseMatrix= iMatrix;
		samplingRate= rate;
		r2WindowHalfwidth= r2Halfwidth;
		characteristicMatrix= cMatrix;
		applyCharacteristicLengthMedianFiltering= applyFilterToCharacteristicLength;
		characteristicLengthMedianFilterHalfwidth= characteristicLengthFilterHalfwidth;
		applyVelocityMedianFiltering= applyFilterToVelocity;
		velocityMedianFilterHalfwidth= velocityFilterHalfwidth;
	}
	//
	public long getCriticalTime() {
		return beginningTime + minimalTrackDuration;
	}
	public void appendPoint(BlobAttributes attributes) {
		if (!isInsensible && !isCompleted && !isDeposed) {
			long time= attributes.time;
			if (recentPoints.isEmpty()) {
				recentSegmentBeginning= time;
			};
			recentPoints.add(attributes);
			endTime= attributes.time;
			numberOfPoints++;
			boolean pointIsRarefied= false;
			double[][] spectrum= attributes.spectrum;
			int bandNumber= spectrum.length;
			if (bandNumber >= 1) {
				if (maximalRarefactionOfObject >= 0) {
					pointIsRarefied= true;
					for (int band=0; band < bandNumber; band++) {
						if (maximalRarefactionOfObject >= spectrum[band][nBins+2]) {
							pointIsRarefied= false;
							break;
						}
					}
				}
			};
			if (pointIsRarefied) {
				numberOfRarefiedHistograms++;
			};
			isRarefied= pointIsRarefied;
			prolong(time,false);
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
	public void registerCollision(GrowingTrack receiver, long time, TrackSegmentEntryType entryType) {
		if (!isDeposed) {
			if (isStrong && receiver.isStrong) {
				if (!isRarefied && !receiver.isRarefied) {
					requestBreakPoint(time,receiver.identifier,entryType);
					receiver.requestBreakPoint(time,identifier,entryType.computeComplementaryType());
				}
			} else {
				long criticalTime1= getCriticalTime();
				long criticalTime2= receiver.getCriticalTime();
				if (criticalTime2 > criticalTime1) {
					criticalTime1= criticalTime2;
				};
				deferredCollisions.add(new DeferredCollision(criticalTime1,receiver,time,this,entryType));
			};
		}
	}
	protected void implementDeferredOperations(long time) {
		if (!deferredCollisions.isEmpty()) {
			for (int n=deferredCollisions.size()-1; n >= 0; n--) {
				DeferredCollision c= deferredCollisions.get(n);
				if (c.implementationTime < time) {
					if (c.implement()) {
						deferredCollisions.remove(n);
					}
				}
			}
		}
	}
	public void requestBreakPoint(long time, BigInteger neighbour, TrackSegmentEntryType entryType) {
		if (!isDeposed) {
			if (requestedBreakPoints.isEmpty()) {
				firstRequestedBreakPoint= time;
			} else if (time < firstRequestedBreakPoint) {
				firstRequestedBreakPoint= time;
			};
			BreakPoint point= requestedBreakPoints.get(time);
			if (point==null) {
				point= new BreakPoint(time,neighbour,entryType);
			} else {
				point.addBranch(neighbour,entryType);
			};
			requestedBreakPoints.put(time,point);
		}
	}
	protected void createSegments(long time, boolean emptyBuffer) {
		int numberOfRecentPoints= recentPoints.size();
		if (time < recentSegmentBeginning + minimalTrackDuration && !emptyBuffer) {
			return;
		};
		if (emptyBuffer) {
			// Returns the least key greater than or equal to
			// the given key, or null if there is no such key.
			Long ceilingKey= requestedBreakPoints.ceilingKey(endTime);
			if (ceilingKey==null) {
				BreakPoint point= new BreakPoint(endTime);
				requestedBreakPoints.put(endTime,point);
			}
		};
		long criticalTime= time - minimalTrackDuration;
		Collection<BreakPoint> breakPointSet= requestedBreakPoints.values();
		Iterator<BreakPoint> iterator= breakPointSet.iterator();
		int firstIndex= 0;
		int lastIndex= -1;
		while (iterator.hasNext()) {
			BreakPoint breakPoint= iterator.next();
			long breakTime= breakPoint.time;
			firstRequestedBreakPoint= breakTime;
			if (breakTime > criticalTime && !emptyBuffer) {
				break;
			};
			lastIndex= -1;
			for (int n=0; n < numberOfRecentPoints; n++) {
				BlobAttributes p= recentPoints.get(n);
				long t= p.time;
				if (t <= breakTime) {
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
			int[][] rectangles= new int[length][5];
			long segmentBeginning;
			int bandNumber;
			if (length > 0) {
				BlobAttributes p0= recentPoints.get(firstIndex);
				segmentBeginning= p0.time;
				bandNumber= p0.spectrum.length;
			} else {
				segmentBeginning= breakTime;
				bandNumber= 0;
			};
			long segmentEnd= segmentBeginning;
			double[][] spectrumSumX= new double[bandNumber][nBins+3];
			double[][] spectrumSumX2= new double[bandNumber][nBins];
			long[] frameNumbers= new long[length];
			long[] foregroundPixelNumber= new long[length];
			long[] contourPixelNumber= new long[length];
			int spectrumN= 0;
			int index= 0;
			for (int n=firstIndex; n <= lastIndex; n++) {
				BlobAttributes p1= recentPoints.get(n);
				segmentEnd= p1.time;
				int shift= (int)(segmentEnd - segmentBeginning);
				rectangles[index][0]= shift;
				rectangles[index][1]= p1.x1;
				rectangles[index][2]= p1.x2;
				rectangles[index][3]= p1.y1;
				rectangles[index][4]= p1.y2;
				frameNumbers[index]= p1.time;
				foregroundPixelNumber[index]= p1.foregroundArea;
				contourPixelNumber[index]= p1.contourLength;
				for (int band=0; band < bandNumber; band++) {
					for (int k=0; k < nBins; k++) {
						double value= p1.spectrum[band][k];
						spectrumSumX[band][k]= spectrumSumX[band][k] + value;
						spectrumSumX2[band][k]= spectrumSumX2[band][k] + value*value;
					};
					spectrumSumX[band][nBins]= spectrumSumX[band][nBins] + p1.spectrum[band][nBins];
					spectrumSumX[band][nBins+1]= spectrumSumX[band][nBins+1] + p1.spectrum[band][nBins+1];
					spectrumSumX[band][nBins+2]= spectrumSumX[band][nBins+2] + p1.spectrum[band][nBins+2];
				};
				spectrumN++;
				index++;
			};
			double[][] spectrumMean= new double[bandNumber][nBins+3];
			double[][] spectrumDispersion= new double[bandNumber][nBins];
			for (int band=0; band < bandNumber; band++) {
				for (int k=0; k < nBins; k++) {
					if (spectrumN > 0) {
						double mean= spectrumSumX[band][k] / spectrumN;
						spectrumMean[band][k]= mean;
						spectrumDispersion[band][k]= (spectrumSumX2[band][k] / spectrumN) - mean*mean;
					} else {
						spectrumMean[band][k]= -1;
						spectrumDispersion[band][k]= -1;
					}
				};
				if (spectrumN > 0) {
					spectrumMean[band][nBins]= spectrumSumX[band][nBins] / spectrumN;
					spectrumMean[band][nBins+1]= spectrumSumX[band][nBins+1] / spectrumN;
					spectrumMean[band][nBins+2]= spectrumSumX[band][nBins+2] / spectrumN;
				} else {
					spectrumMean[band][nBins]= -1;
					spectrumMean[band][nBins+1]= -1;
					spectrumMean[band][nBins+2]= -1;
				}
			};
			TrackSegment newSegment= new TrackSegment(
				identifier,
				segments.size(),
				segmentBeginning,
				segmentEnd,
				breakTime,
				rectangles,
				frameNumbers,
				foregroundPixelNumber,
				contourPixelNumber,
				spectrumMean,
				spectrumDispersion,
				spectrumN,
				breakPoint.entries,
				maximalRarefactionOfObject,
				inverseMatrix,
				samplingRate,
				r2WindowHalfwidth,
				characteristicMatrix,
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
		if (firstIndex > 0) {
			if (firstIndex < recentPoints.size()) {
				recentPoints.subList(0,firstIndex).clear();
			} else {
				recentPoints.clear();
			}
		};
		if (!recentPoints.isEmpty()) {
			recentSegmentBeginning= recentPoints.get(0).time;
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
		int[][] rectangles= new int[numberOfPoints][5];
		BlobAttributes p0= recentPoints.get(0);
		long segmentBeginning= p0.time;
		int bandNumber= p0.spectrum.length;
		long segmentEnd= segmentBeginning;
		double[][] spectrumSumX= new double[bandNumber][nBins+3];
		double[][] spectrumSumX2= new double[bandNumber][nBins];
		long[] frameNumbers= new long[numberOfPoints];
		long[] foregroundPixelNumber= new long[numberOfPoints];
		long[] contourPixelNumber= new long[numberOfPoints];
		for (int n=0; n < numberOfPoints; n++) {
			BlobAttributes p1= recentPoints.get(n);
			segmentEnd= p1.time;
			int shift= (int)(segmentEnd - segmentBeginning);
			rectangles[n][0]= shift;
			rectangles[n][1]= p1.x1;
			rectangles[n][2]= p1.x2;
			rectangles[n][3]= p1.y1;
			rectangles[n][4]= p1.y2;
			frameNumbers[n]= p1.time;
			foregroundPixelNumber[n]= p1.foregroundArea;
			contourPixelNumber[n]= p1.contourLength;
			for (int band=0; band < bandNumber; band++) {
				for (int k=0; k < nBins; k++) {
					double value= p1.spectrum[band][k];
					spectrumSumX[band][k]= spectrumSumX[band][k] + value;
					spectrumSumX2[band][k]= spectrumSumX2[band][k] + value*value;
				};
				spectrumSumX[band][nBins]= spectrumSumX[band][nBins] + p1.spectrum[band][nBins];
				spectrumSumX[band][nBins+1]= spectrumSumX[band][nBins+1] + p1.spectrum[band][nBins+1];
				spectrumSumX[band][nBins+2]= spectrumSumX[band][nBins+2] + p1.spectrum[band][nBins+2];
			}
		};
		double[][] spectrumMean= new double[bandNumber][nBins+3];
		double[][] spectrumDispersion= new double[bandNumber][nBins];
		for (int band=0; band < bandNumber; band++) {
			for (int k=0; k < nBins; k++) {
				double mean= spectrumSumX[band][k] / numberOfPoints;
				spectrumMean[band][k]= mean;
				spectrumDispersion[band][k]= (spectrumSumX2[band][k] / numberOfPoints) + mean*mean;
			};
			spectrumMean[band][nBins]= spectrumSumX[band][nBins] / numberOfPoints;
			spectrumMean[band][nBins+1]= spectrumSumX[band][nBins+1] / numberOfPoints;
			spectrumMean[band][nBins+2]= spectrumSumX[band][nBins+2] / numberOfPoints;
		};
		TrackSegment newSegment= new TrackSegment(
				identifier,
				segments.size(),
				segmentBeginning,
				segmentEnd,
				segmentEnd,
				rectangles,
				frameNumbers,
				foregroundPixelNumber,
				contourPixelNumber,
				spectrumMean,
				spectrumDispersion,
				numberOfPoints,
				null,
				maximalRarefactionOfObject,
				inverseMatrix,
				samplingRate,
				r2WindowHalfwidth,
				characteristicMatrix,
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
				// if (!isStrong) {
				//	isStrong= true;
				// };
				implementDeferredOperations(time);
				if (deferredCollisions.isEmpty()) {
					// deferredCollisions.clear();
					isCompleted= true;
				}
			}
		}
	}
	public boolean isMature(long time) {
		return time >= beginningTime + minimalTrackDuration;
	}
	public void depose() {
		isDeposed= true;
		recentPoints.clear();
		requestedBreakPoints.clear();
		segments.clear();
		deferredCollisions.clear();
	}
	public boolean isToBeCompleted(long time) {
		if (!isCompleted && isInsensible && endTime < time - minimalTrackDuration) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isOutOfDate(long time, long maximalTrackRetentionPeriod) {
		if (!isDeposed && isInsensible && isCompleted && endTime < time - maximalTrackRetentionPeriod) {
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
			beginningTime,
			endTime,
			list,
			minimalTrackDuration,
			maximalRarefactionOfObject,
			inverseMatrix,
			samplingRate,
			isStrong,
			isRarefied,
			isInsensible,
			isCompleted,
			isDeposed);
	}
}
