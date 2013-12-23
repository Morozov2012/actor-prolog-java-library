// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.math.BigInteger;
import java.io.PrintStream;

class ConnectedGraph {
	public HashSet<BigInteger> referredTracks= new HashSet<>();
	public HashSet<BigInteger> refusedTracks= new HashSet<>();
	public ArrayList<TrackSegment> segments= new ArrayList<>();
	public ArrayList<ConnectedSegment> connectedSegments= new ArrayList<>();
	public HashMap<BigInteger,ConnectedSegment> inputEntries= new HashMap<>();
	public HashMap<BigInteger,ConnectedSegment> outputEntries= new HashMap<>();
	// public int halfwidthPercentage= 20;
	// HashSet<SegmentChain> hypotheticPathes= new HashSet<>();
	public ConnectedGraph() {
	}
	public void addTrack(BigInteger identifier, StableTrack track) {
		referredTracks.add(identifier);
	}
	public void addSegment(TrackSegment segment, boolean isFirst, boolean isLast) {
		if (!segments.contains(segment)) {
			segments.add(segment);
			connectedSegments.add(new ConnectedSegment(segment,isFirst,isLast));
		}
	}
	public boolean containsTrack(BigInteger identifier) {
		return referredTracks.contains(identifier);
	}
	public boolean containsSegment(TrackSegment segment) {
		return segments.contains(segment);
	}
	public void refuseSlowTracks(double velocityThreshold, double distanceThreshold, double fuzzyThresholdBorder, HashMap<BigInteger,StableTrack> tracks) {
		Iterator<BigInteger> iterator1= referredTracks.iterator();
		while (iterator1.hasNext()) {
			BigInteger identifier= iterator1.next();
			StableTrack track= tracks.get(identifier);
			boolean averageAbsoluteValues= true;
			double velocity= track.computeMeanVelocity(averageAbsoluteValues);
			// double velocity= track.computeTotalVelocity();
			double distance= track.computeTotalDistance();
			double velosityHalfwidth= velocityThreshold * fuzzyThresholdBorder;
			double distanceHalfwidth= distanceThreshold * fuzzyThresholdBorder;
			double metrics1= VisionUtils.metrics(velocity,velocityThreshold,velosityHalfwidth);
			double metrics2= VisionUtils.metrics(distance,distanceThreshold,distanceHalfwidth);
			// double commonMetrics= 1 - (1-metrics1) * (1-metrics2);
			double commonMetrics= metrics1 * metrics2;
			if (commonMetrics < 0.5) {
				refusedTracks.add(identifier);
			}
		}
	}
	public void refuseSlowTracksAndReport(double velocityThreshold, double distanceThreshold, double fuzzyThresholdBorder, HashMap<BigInteger,StableTrack> tracks) {
		Iterator<BigInteger> iterator1= referredTracks.iterator();
		while (iterator1.hasNext()) {
			BigInteger identifier= iterator1.next();
			StableTrack track= tracks.get(identifier);
			boolean averageAbsoluteValues= true;
			double velocity= track.computeMeanVelocity(averageAbsoluteValues);
			double distance= track.computeTotalDistance();
			double velosityHalfwidth= velocityThreshold * fuzzyThresholdBorder;
			double distanceHalfwidth= distanceThreshold * fuzzyThresholdBorder;
			double metrics1= VisionUtils.metrics(velocity,velocityThreshold,velosityHalfwidth);
			double metrics2= VisionUtils.metrics(distance,distanceThreshold,distanceHalfwidth);
System.out.printf("\n? %s [beginningTime=%s, endTime=%s]\n",this,track.beginningTime,track.endTime);
System.out.printf("[1] velocity=%s (velocityThreshold=%s, metrics1=%s)\n",velocity,velocityThreshold,metrics1);
System.out.printf("[2] distance=%s (distanceThreshold=%s, metrics2=%s)\n",distance,distanceThreshold,metrics2);
			// double commonMetrics= 1 - (1-metrics1) * (1-metrics2);
			double commonMetrics= metrics1 * metrics2;
			if (commonMetrics < 0.5) {
				refusedTracks.add(identifier);
System.out.printf("refuseSlowTracks: YES, commonMetrics=%s\n",commonMetrics);
			} else {
System.out.printf("refuseSlowTracks: NO, commonMetrics=%s\n",commonMetrics);
			}
		}
	}
	public void alloy(HashMap<BigInteger,StableTrack> tracks) {
		int numberOfSegments= connectedSegments.size();
		HashMap<BigInteger,HashMap<Integer,ConnectedSegment>> hash1= new HashMap<>();
		for (int n=numberOfSegments-1; n >= 0; n--) {
			ConnectedSegment c= connectedSegments.get(n);
			TrackSegment s= c.trackSegment;
			BigInteger owner= s.owner;
			if (!refusedTracks.contains(owner)) {
				HashMap<Integer,ConnectedSegment> hash2= hash1.get(owner);
				if (hash2==null) {
					hash2= new HashMap<Integer,ConnectedSegment>();
				};
				int number= s.number;
				hash2.put(number,c);
				hash1.put(owner,hash2);
			} else {
				connectedSegments.remove(n);
			}
		};
		numberOfSegments= connectedSegments.size();
		for (int n=0; n < numberOfSegments; n++) {
			ConnectedSegment c= connectedSegments.get(n);
			c.resolveLinks(tracks,refusedTracks,hash1);
			if (c.isInputEntry()) {
				inputEntries.put(c.getOwner(),c);
			};
			if (c.isOutputEntry()) {
				outputEntries.put(c.getOwner(),c);
			}
		}
	}
	public void collectOriginsAndBranches() {
		int numberOfSegments= connectedSegments.size();
		for (int n=0; n < numberOfSegments; n++) {
			ConnectedSegment c= connectedSegments.get(n);
			c.collectOriginsAndBranches();
		}
	}
	public void deleteEmptySegments() {
		int numberOfSegments= connectedSegments.size();
		for (int n=numberOfSegments-1; n >= 0; n--) {
			ConnectedSegment c= connectedSegments.get(n);
			if (c.trackSegment.rectangles.length <= 0) {
				connectedSegments.remove(n);
			}
		}
	}
	public void enablePixels(long time, boolean[] bitMask, int imageWidth, int imageHeight, HashMap<BigInteger,StableTrack> tracks) {
		Iterator<BigInteger> iterator1= referredTracks.iterator();
		while (iterator1.hasNext()) {
			BigInteger identifier= iterator1.next();
			if (!refusedTracks.contains(identifier)) {
				StableTrack track= tracks.get(identifier);
				int[] rectangle= track.getCurrentRectangle(time);
				if (rectangle != null) {
					int x1= rectangle[0];
					int x2= rectangle[1];
					int y1= rectangle[2];
					int y2= rectangle[3];
					if (x1 < 0) {
						x1= 0;
					} else if (x1 >= imageWidth) {
						x1= imageWidth - 1;
					};
					if (x2 < 0) {
						x2= 0;
					} else if (x2 >= imageWidth) {
						x2= imageWidth - 1;
					};
					if (y1 < 0) {
						y1= 0;
					} else if (y1 >= imageHeight) {
						y1= imageHeight - 1;
					};
					if (y2 < 0) {
						y2= 0;
					} else if (y2 >= imageHeight) {
						y2= imageHeight - 1;
					};
					for (int row=y1; row <=y2; row++) {
						for (int column=x1; column <= x2; column++) {
							int point= imageWidth*row + column;
							bitMask[point]= true;
						}
					}
				}
			}
		}
	}
	public void dump(PrintStream stream) {
		stream.printf("referredTracks:    %s items\n",referredTracks.size());
		stream.printf("refusedTracks:     %s items\n",refusedTracks.size());
		stream.printf("segments:          %s items\n",segments.size());
		stream.printf("connectedSegments: %s items\n",connectedSegments.size());
		stream.printf("\nREFERRED TRACKS:\n");
		Iterator<BigInteger> iterator1= referredTracks.iterator();
		while (iterator1.hasNext()) {
			// long identifier= iterator1.next();
			BigInteger identifier= iterator1.next();
			stream.printf(" %s",identifier);
		};
		stream.printf("\n");
		stream.printf("\nREFUSED TRACKS:\n");
		Iterator<BigInteger> iterator2= refusedTracks.iterator();
		while (iterator2.hasNext()) {
			// long identifier= iterator2.next();
			BigInteger identifier= iterator2.next();
			stream.printf(" %s",identifier);
		};
		stream.printf("\n");
		stream.printf("\nCONNECTED SEGMENTS:\n");
		for (int n=0; n < connectedSegments.size(); n++) {
			ConnectedSegment s= connectedSegments.get(n);
			stream.printf("\n");
			// stream.printf("%%%%%%%%%%\n");
			stream.printf("Segment #%s\n",n);
			s.dump(stream);
		}
	}
	public void createMatlab(PrintStream stream) {
		stream.printf("%% referredTracks:    %s items\n",referredTracks.size());
		stream.printf("%% refusedTracks:     %s items\n",refusedTracks.size());
		stream.printf("%% segments:          %s items\n",segments.size());
		stream.printf("%% connectedSegments: %s items\n",connectedSegments.size());
		stream.printf("%% REFERRED TRACKS:\n%%");
		Iterator<BigInteger> iterator1= referredTracks.iterator();
		while (iterator1.hasNext()) {
			BigInteger identifier= iterator1.next();
			stream.printf(" %s",identifier);
		};
		stream.printf("\n");
		stream.printf("%% REFUSED TRACKS:\n%%");
		Iterator<BigInteger> iterator2= refusedTracks.iterator();
		while (iterator2.hasNext()) {
			BigInteger identifier= iterator2.next();
			stream.printf(" %s",identifier);
		};
		stream.printf("\n");
		stream.printf("%% CONNECTED SEGMENTS:\n");
		for (int n=0; n < connectedSegments.size(); n++) {
			ConnectedSegment s= connectedSegments.get(n);
			stream.printf("%%%%%%%%%%\n");
			stream.printf("%% Segment #%s\n",n);
			s.createMatlab(stream,BigInteger.ZERO,n);
		}
	}
}
