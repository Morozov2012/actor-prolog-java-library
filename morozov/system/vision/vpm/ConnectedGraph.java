// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import morozov.system.vision.vpm.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.math.BigInteger;
import java.io.PrintStream;

public class ConnectedGraph {
	//
	protected HashSet<BigInteger> referredTracks= new HashSet<>();
	protected HashSet<BigInteger> refusedTracks= new HashSet<>();
	protected ArrayList<TrackSegment> segments= new ArrayList<>();
	protected ArrayList<ConnectedSegment> connectedSegments= new ArrayList<>();
	protected HashMap<BigInteger,ConnectedSegment> inputEntries= new HashMap<>();
	protected HashMap<BigInteger,ConnectedSegment> outputEntries= new HashMap<>();
	//
	protected double commonMetricsThreshold= 0.75;
	//
	///////////////////////////////////////////////////////////////
	//
	public ConnectedGraph() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
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
			double velocity= track.computeMeanVelocity();
			// double distance= track.computeTotalDistance();
			double distance= track.computeTotalShiftInPixels();
			double velosityHalfwidth= velocityThreshold * fuzzyThresholdBorder;
			double distanceHalfwidth= distanceThreshold * fuzzyThresholdBorder;
			double metrics1= VisionUtils.metrics(velocity,velocityThreshold,velosityHalfwidth);
			double metrics2= VisionUtils.metrics(distance,distanceThreshold,distanceHalfwidth);
			double commonMetrics= 1 - (1-metrics1) * (1-metrics2);
			// if (commonMetrics < 0.75) {
			if (commonMetrics < commonMetricsThreshold) {
				refusedTracks.add(identifier);
			}
		}
	}
	public void refuseSlowTracksAndReport(double velocityThreshold, double distanceThreshold, double fuzzyThresholdBorder, HashMap<BigInteger,StableTrack> tracks) {
		Iterator<BigInteger> iterator1= referredTracks.iterator();
		while (iterator1.hasNext()) {
			BigInteger identifier= iterator1.next();
			StableTrack track= tracks.get(identifier);
			double velocity= track.computeMeanVelocity();
			// double distance= track.computeTotalDistance();
			double distance= track.computeTotalShiftInPixels();
			double velosityHalfwidth= velocityThreshold * fuzzyThresholdBorder;
			double distanceHalfwidth= distanceThreshold * fuzzyThresholdBorder;
			double metrics1= VisionUtils.metrics(velocity,velocityThreshold,velosityHalfwidth);
			double metrics2= VisionUtils.metrics(distance,distanceThreshold,distanceHalfwidth);
			double commonMetrics= metrics1 * metrics2;
			// if (commonMetrics < 0.75) {
			if (commonMetrics < commonMetricsThreshold) {
				refusedTracks.add(identifier);
			}
		}
	}
	public void alloy(HashMap<BigInteger,StableTrack> tracks) {
		int numberOfSegments= connectedSegments.size();
		HashMap<BigInteger,HashMap<Integer,ConnectedSegment>> hash1= new HashMap<>();
		for (int n=numberOfSegments-1; n >= 0; n--) {
			ConnectedSegment c= connectedSegments.get(n);
			TrackSegment s= c.getTrackSegment();
			BigInteger owner= s.getOwner();
			// if (!refusedTracks.contains(owner)) {
			HashMap<Integer,ConnectedSegment> hash2= hash1.get(owner);
			if (hash2==null) {
				hash2= new HashMap<Integer,ConnectedSegment>();
			};
			int number= s.getNumber();
			hash2.put(number,c);
			hash1.put(owner,hash2);
			// } else {
			//	connectedSegments.remove(n);
			// }
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
			if (c.isEmpty()) {
				c.excludeSegment();
				connectedSegments.remove(n);
			}
		}
	}
	//public void deleteSlowSegments() {
	//}
	public void deleteSlowSegments() {
		int numberOfSegments= connectedSegments.size();
		for (int n=numberOfSegments-1; n >= 0; n--) {
			ConnectedSegment c= connectedSegments.get(n);
			BigInteger owner= c.getOwner();
			if (refusedTracks.contains(owner)) {
				c.excludeSegment();
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
	//
	///////////////////////////////////////////////////////////////
	//
	public HashSet<BigInteger> getReferredTracks() {
		return referredTracks;
	}
	public HashSet<BigInteger> getRefusedTracks() {
		return refusedTracks;
	}
	public ArrayList<TrackSegment> getSegments() {
		return segments;
	}
	public ArrayList<ConnectedSegment> getConnectedSegments() {
		return connectedSegments;
	}
	public HashMap<BigInteger,ConnectedSegment> getInputEntries() {
		return inputEntries;
	}
	public HashMap<BigInteger,ConnectedSegment> getOutputEntries() {
		return outputEntries;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void dump(PrintStream stream) {
		stream.printf("referredTracks:    %s items\n",referredTracks.size());
		stream.printf("refusedTracks:     %s items\n",refusedTracks.size());
		stream.printf("segments:          %s items\n",segments.size());
		stream.printf("connectedSegments: %s items\n",connectedSegments.size());
		stream.printf("\nREFERRED TRACKS:\n");
		Iterator<BigInteger> iterator1= referredTracks.iterator();
		while (iterator1.hasNext()) {
			BigInteger identifier= iterator1.next();
			stream.printf(" %s",identifier);
		};
		stream.printf("\n");
		stream.printf("\nREFUSED TRACKS:\n");
		Iterator<BigInteger> iterator2= refusedTracks.iterator();
		while (iterator2.hasNext()) {
			BigInteger identifier= iterator2.next();
			stream.printf(" %s",identifier);
		};
		stream.printf("\n");
		stream.printf("\nCONNECTED SEGMENTS:\n");
		for (int n=0; n < connectedSegments.size(); n++) {
			ConnectedSegment s= connectedSegments.get(n);
			stream.printf("\n");
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
