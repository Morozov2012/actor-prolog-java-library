// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.math.BigInteger;
import java.io.PrintStream;

class StableTrack {
	//
	public BigInteger identifier;
	public long beginningTime;
	public long endTime;
	public ArrayList<TrackSegment> segments= new ArrayList<>();
	public int minimalTrackDuration;
	public int maximalRarefactionOfObject;
	public double[][] inverseMatrix;
	public double samplingRate;
	public boolean isStrong= false;
	public boolean isRarefied= false;
	public boolean isInsensible= false;
	public boolean isCompleted= false;
	public boolean isDeposed= false;
	//
	public StableTrack(BigInteger id, long bTime, long eTime, ArrayList<TrackSegment> segmentArray, int minimalDuration, int maximalRarefaction, double[][] iMatrix, double rate, boolean flagIsStrong, boolean flagIsRarefied, boolean flagIsInsensible, boolean flagIsCompleted, boolean flagIsDeposed) {
		identifier= id;
		beginningTime= bTime;
		endTime= eTime;
		segments= segmentArray;
		minimalTrackDuration= minimalDuration;
		maximalRarefactionOfObject= maximalRarefaction;
		inverseMatrix= iMatrix;
		samplingRate= rate;
		isStrong= flagIsStrong;
		isRarefied= flagIsRarefied;
		isInsensible= flagIsInsensible;
		isCompleted= flagIsCompleted;
		isDeposed= flagIsDeposed;
	}
	//
	public void assembleConnectedGraph(ArrayList<ConnectedGraph> graphs, HashMap<BigInteger,StableTrack> tracks) {
		if (!isStrong) {
			return;
		};
		ConnectedGraph graph= new ConnectedGraph();
		assembleConnectedGraph(graph,tracks);
		graphs.add(graph);
	}
	public ArrayList<TrackSegment> getTrackSegments() {
		return segments;
	}
	public void assembleConnectedGraph(ConnectedGraph graph, HashMap<BigInteger,StableTrack> tracks) {
		if (!isStrong) {
			return;
		};
		if (graph.containsTrack(identifier)) {
			return;
		};
		graph.addTrack(identifier,this);
		int numberOfSegments= segments.size();
		boolean hasRecentSegment= false;
		if (numberOfSegments > 0) {
			for (int n=numberOfSegments-1; n >= 0; n--) {
				TrackSegment s= segments.get(n);
				boolean isFirst= (n==0);
				boolean isLast= (!hasRecentSegment && n==numberOfSegments-1);
				s.assembleConnectedGraph(isFirst,isLast,graph,tracks);
			}
		}
	}
	public TrackSegment getLinkedSegment(long time) {
		int numberOfSegments= segments.size();
		if (numberOfSegments <= 0) {
			return null;
		};
		for (int n=0; n < segments.size(); n++) {
			TrackSegment segment= segments.get(n);
			if (time == segment.breakPointTime) {
				return segment;
			}
		};
		return null;
	}
	public double computeTotalDistance() {
		MeanArea mean= new MeanArea();
		int numberOfSegments= segments.size();
		if (numberOfSegments > 0) {
			for (int n=0; n < numberOfSegments; n++) {
				TrackSegment s= segments.get(n);
				s.computeMeanArea(mean);
			}
		};
		double deltaX= mean.getMaximalCenterX() - mean.getMinimalCenterX();
		double deltaY= mean.getMaximalCenterY() - mean.getMinimalCenterY();
		return StrictMath.hypot(deltaX,deltaY);
	}
	public double computeTotalVelocity() {
		double distance= computeTotalDistance();
		long deltaTime= endTime - beginningTime + 1;
		if (samplingRate > 0) {
			return (double)distance * samplingRate / deltaTime;
		} else {
			return (double)distance / deltaTime;
		}
	}
	public double computeMeanVelocity() {
		MeanVelocity mean= new MeanVelocity();
		int numberOfSegments= segments.size();
		if (numberOfSegments > 0) {
			for (int n=numberOfSegments-1; n >= 0; n--) {
				TrackSegment s= segments.get(n);
				s.computeMeanVelocity(mean);
			}
		};
		return mean.getMeanVelocity();
	}
	//
	public int[] getCurrentRectangle(long time) {
		if (beginningTime > time || endTime < time) {
			return null;
		};
		int numberOfSegments= segments.size();
		for (int n=numberOfSegments-1; n >= 0; n--) {
			TrackSegment s= segments.get(n);
			long segmentBeginning= s.beginningTime;
			if (segmentBeginning > time || s.endTime < time) {
				continue;
			};
			int[][] rectangles= s.rectangles;
			for (int k=0; k < rectangles.length; k++) {
				int shift= rectangles[k][0];
				long t= segmentBeginning + shift;
				if (t==time) {
					int[] rectangle= new int[4];
					rectangle[0]= rectangles[k][1];
					rectangle[1]= rectangles[k][2];
					rectangle[2]= rectangles[k][3];
					rectangle[3]= rectangles[k][4];
					return rectangle;
				}
			}
		};
		return null;
	}
	//
	public void dump(PrintStream stream) {
		stream.printf("identifier:                 %s\n",identifier);
		stream.printf("beginningTime:              %s\n",beginningTime);
		stream.printf("endTime:                    %s\n",endTime);
		stream.printf("endTime-beginningTime+1:    %s\n",endTime-beginningTime+1);
		//stream.printf("recentSegmentBeginning:     %s\n",recentSegmentBeginning);
		//stream.printf("recentPoints:               %s items\n",recentPoints.size());
		//stream.printf("requestedBreakPoints:       %s items\n",requestedBreakPoints.size());
		//stream.printf("firstRequestedBreakPoint:   %s\n",firstRequestedBreakPoint);
		stream.printf("segments:                   %s items\n",segments.size());
		stream.printf("computeTotalDistance():     %s\n",computeTotalDistance());
		stream.printf("totalDistance / Time:       %s\n",computeTotalVelocity());
		stream.printf("computeMeanVelocity():      %s\n",computeMeanVelocity());
		stream.printf("minimalTrackDuration:       %s\n",minimalTrackDuration);
		stream.printf("isStrong:                   %s\n",isStrong);
		//stream.printf("numberOfPoints:             %s\n",numberOfPoints);
		//stream.printf("numberOfRarefiedHistograms: %s\n",numberOfRarefiedHistograms);
		stream.printf("isRarefied:                 %s\n",isRarefied);
		stream.printf("isInsensible:               %s\n",isInsensible);
		stream.printf("isCompleted:                %s\n",isCompleted);
		stream.printf("isDeposed:                  %s\n",isDeposed);
		//stream.printf("deferredCollisions:         %s items\n",deferredCollisions.size());
		//stream.printf("nBins:                      %s\n",nBins);
		//stream.printf("drawCompletedTracks:        %s\n",drawCompletedTracks);
		stream.printf("\nSEGMENTS:\n");
		for (int n=0; n < segments.size(); n++) {
			stream.printf("\n");
			stream.printf("Segment #%s\n",n);
			segments.get(n).dump(stream);
		}
	}
	public void createMatlab(PrintStream stream) {
		stream.printf("%% identifier:                 %s\n",identifier);
		stream.printf("%% beginningTime:              %s\n",beginningTime);
		stream.printf("%% endTime:                    %s\n",endTime);
		stream.printf("%% endTime-beginningTime+1:    %s\n",endTime-beginningTime+1);
		//stream.printf("%% recentSegmentBeginning:     %s\n",recentSegmentBeginning);
		//stream.printf("%% recentPoints:               %s items\n",recentPoints.size());
		//stream.printf("%% requestedBreakPoints:       %s items\n",requestedBreakPoints.size());
		//stream.printf("%% firstRequestedBreakPoint:   %s\n",firstRequestedBreakPoint);
		stream.printf("%% segments:                   %s items\n",segments.size());
		stream.printf("%% computeTotalDistance():     %s\n",computeTotalDistance());
		stream.printf("%% totalDistance / Time:       %s\n",computeTotalVelocity());
		stream.printf("%% computeMeanVelocity():      %s\n",computeMeanVelocity());
		//stream.printf("N_%s_%s_%s= %s;\n",identifier,beginningTime,endTime,spectrumN);
		//stream.printf("T%s{%s}.owner= %s;\n",identifier,index+1,owner);
		//for (int w=1; w < 50; w++) {
		//	double c= computeVelocityCoefficient(w);
		//	stream.printf("VelocityCoefficient(%s)= %s;\n",w,c);
		//};
		stream.printf("%% minimalTrackDuration:       %s\n",minimalTrackDuration);
		stream.printf("%% isStrong:                   %s\n",isStrong);
		//stream.printf("%% numberOfPoints:             %s\n",numberOfPoints);
		//stream.printf("%% numberOfRarefiedHistograms: %s\n",numberOfRarefiedHistograms);
		stream.printf("%% isRarefied:                 %s\n",isRarefied);
		stream.printf("%% isInsensible:               %s\n",isInsensible);
		stream.printf("%% isCompleted:                %s\n",isCompleted);
		stream.printf("%% isDeposed:                  %s\n",isDeposed);
		//stream.printf("%% deferredCollisions:         %s items\n",deferredCollisions.size());
		//stream.printf("%% nBins:                      %s\n",nBins);
		//stream.printf("%% drawCompletedTracks:        %s\n",drawCompletedTracks);
		stream.printf("%% SEGMENTS:\n");
		for (int n=0; n < segments.size(); n++) {
			// stream.printf("\n");
			stream.printf("%% Segment #%s\n",n);
			segments.get(n).createMatlab(stream,identifier,n);
		}
	}
}
