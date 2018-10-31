// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import target.*;

import morozov.system.*;
import morozov.system.vision.vpm.converters.*;
import morozov.terms.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigInteger;
import java.io.PrintStream;

public class StableTrack {
	//
	protected BigInteger identifier;
	protected BlobType blobType;
	protected long beginningTime;
	protected long endTime;
	protected TrackSegment[] segments;
	protected int minimalTrackDuration;
	protected double samplingRate;
	protected TransformationMatrices transformationMatrices;
	protected boolean isStrong= false;
	protected boolean isInsensible= false;
	protected boolean isCompleted= false;
	protected boolean isDeposed= false;
	//
	protected HashMap<Long,Term> mapOfBlobTerms= new HashMap<>();
	//
	protected Integer numberOfFrames;
	protected Double meanBlobArea;
	protected Double meanForegroundArea;
	protected Double meanContourLength;
	protected Double totalDistance;
	protected Double totalShift;
	protected Double meanVelocity;
	//
	protected Long frame1;
	protected Long time1;
	protected Long x1;
	protected Long y1;
	protected Long centroidX1;
	protected Long centroidY1;
	protected Long frame2;
	protected Long time2;
	protected Long x2;
	protected Long y2;
	protected Long centroidX2;
	protected Long centroidY2;
	//
	protected Term prologStableTrack;
	protected Term prologTrackSegmentList;
	//
	///////////////////////////////////////////////////////////////
	//
	public StableTrack(
			BigInteger id,
			BlobType type,
			long bTime,
			long eTime,
			TrackSegment[] segmentArray,
			int minimalDuration,
			double rate,
			TransformationMatrices tMatrices,
			boolean flagIsStrong,
			boolean flagIsInsensible,
			boolean flagIsCompleted,
			boolean flagIsDeposed) {
		//
		identifier= id;
		blobType= type;
		beginningTime= bTime;
		endTime= eTime;
		segments= segmentArray;
		minimalTrackDuration= minimalDuration;
		samplingRate= rate;
		transformationMatrices= tMatrices;
		isStrong= flagIsStrong;
		isInsensible= flagIsInsensible;
		isCompleted= flagIsCompleted;
		isDeposed= flagIsDeposed;
		//
		for (int k=0; k < segments.length; k++) {
			segments[k].collectMapOfBlobTerms(mapOfBlobTerms);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BigInteger getIdentifier() {
		return identifier;
	}
	public BlobType getBlobType() {
		return blobType;
	}
	public TrackSegment[] getTrackSegments() {
		return segments;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isStrong() {
		return isStrong;
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
	public void assembleConnectedGraph(ArrayList<ConnectedGraph> graphs, HashMap<BigInteger,StableTrack> tracks) {
		if (!isStrong) {
			return;
		};
		ConnectedGraph graph= new ConnectedGraph();
		assembleConnectedGraph(graph,tracks);
		graphs.add(graph);
	}
	public void assembleConnectedGraph(ConnectedGraph graph, HashMap<BigInteger,StableTrack> tracks) {
		if (!isStrong) {
			return;
		};
		if (graph.containsTrack(identifier)) {
			return;
		};
		graph.addTrack(identifier,this);
		int numberOfSegments= segments.length;
		boolean hasRecentSegment= false;
		if (numberOfSegments > 0) {
			for (int k=numberOfSegments-1; k >= 0; k--) {
				TrackSegment s= segments[k];
				boolean isFirst= (k==0);
				boolean isLast= (!hasRecentSegment && k==numberOfSegments-1);
				s.assembleConnectedGraph(isFirst,isLast,graph,tracks);
			}
		}
	}
	//
	public TrackSegment getLinkedSegment(long time) {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return null;
		};
		for (int k=0; k < numberOfSegments; k++) {
			TrackSegment segment= segments[k];
			if (time == segment.getSegmentBreakPoint()) {
				return segment;
			}
		};
		return null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int getNumberOfFrames() {
		if (numberOfFrames==null) {
			numberOfFrames= computeNumberOfFrames();
		};
		return numberOfFrames;
	}
	public double getMeanBlobArea() {
		if (meanBlobArea==null) {
			meanBlobArea= computeMeanBlobArea();
		};
		return meanBlobArea;
	}
	public double getMeanForegroundArea() {
		if (meanForegroundArea==null) {
			meanForegroundArea= computeMeanForegroundArea();
		};
		return meanForegroundArea;
	}
	public double getMeanContourLength() {
		if (meanContourLength==null) {
			meanContourLength= computeMeanContourLength();
		};
		return meanContourLength;
	}
	public double getTotalDistance() {
		if (totalDistance==null) {
			totalDistance= computeTotalDistance();
		};
		return totalDistance;
	}
	public double getTotalShift() {
		if (totalShift==null) {
			totalShift= computeTotalShiftInMeters();
		};
		return totalShift;
	}
	public double getMeanVelocity() {
		if (meanVelocity==null) {
			meanVelocity= computeMeanVelocity();
		};
		return meanVelocity;
	}
	//
	public long getFrameNumber1() {
		if (frame1==null) {
			frame1= computeFrame1();
		};
		return frame1;
	}
	public long getFrameNumber2() {
		if (frame2==null) {
			frame2= computeFrame2();
		};
		return frame2;
	}
	public long getTime1() {
		if (time1==null) {
			time1= computeTime1();
		};
		return time1;
	}
	public long getTime2() {
		if (time2==null) {
			time2= computeTime2();
		};
		return time2;
	}
	public long getX1() {
		if (x1==null) {
			x1= computeX1();
		};
		return x1;
	}
	public long getX2() {
		if (x2==null) {
			x2= computeX2();
		};
		return x2;
	}
	public long getY1() {
		if (y1==null) {
			y1= computeY1();
		};
		return y1;
	}
	public long getY2() {
		if (y2==null) {
			y2= computeY2();
		};
		return y2;
	}
	public long getCentroidX1() {
		if (centroidX1==null) {
			centroidX1= computeCentroidX1();
		};
		return centroidX1;
	}
	public long getCentroidX2() {
		if (centroidX2==null) {
			centroidX2= computeCentroidX2();
		};
		return centroidX2;
	}
	public long getCentroidY1() {
		if (centroidY1==null) {
			centroidY1= computeCentroidY1();
		};
		return centroidY1;
	}
	public long getCentroidY2() {
		if (centroidY2==null) {
			centroidY2= computeCentroidY2();
		};
		return centroidY2;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected int computeNumberOfFrames() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return 0;
		};
		int counter= 0;
		for (int k=0; k < numberOfSegments; k++) {
			TrackSegment segment= segments[k];
			counter+= segment.getLength();
		};
		return counter;
	}
	protected double computeMeanBlobArea() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return 0;
		};
		UniversalAverager averager= new UniversalAverager();
		for (int k=0; k < numberOfSegments; k++) {
			TrackSegment s= segments[k];
			s.computeMeanBlobArea(averager);
		};
		return averager.getMeanValue();
	}
	protected double computeMeanForegroundArea() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return 0;
		};
		UniversalAverager averager= new UniversalAverager();
		for (int k=0; k < numberOfSegments; k++) {
			TrackSegment s= segments[k];
			s.computeMeanForegroundArea(averager);
		};
		return averager.getMeanValue();
	}
	protected double computeMeanContourLength() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return 0;
		};
		UniversalAverager averager= new UniversalAverager();
		for (int k=0; k < numberOfSegments; k++) {
			TrackSegment s= segments[k];
			s.computeMeanContourLength(averager);
		};
		return averager.getMeanValue();
	}
	protected double computeTotalDistance() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return 0;
		};
		UniversalAverager averager= new UniversalAverager();
		for (int k=0; k < numberOfSegments; k++) {
			TrackSegment s= segments[k];
			s.computeTotalDistance(averager);
		};
		return averager.getCumulativeValue();
	}
	protected double computeTotalShiftInPixels() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return 0;
		};
		EvaluatingAverager averagerX= new EvaluatingAverager();
		EvaluatingAverager averagerY= new EvaluatingAverager();
		for (int k=0; k < numberOfSegments; k++) {
			TrackSegment s= segments[k];
			s.computeTotalShift(averagerX,averagerY);
		};
		double x1= averagerX.getMinimalValue();
		double y1= averagerY.getMinimalValue();
		double x2= averagerX.getMaximalValue();
		double y2= averagerY.getMaximalValue();
		double absDeltaX= x2 - x1;
		double absDeltaY= y2 - y1;
		if (absDeltaX < 0) {
			absDeltaX= - absDeltaX;
		};
		if (absDeltaY < 0) {
			absDeltaY= - absDeltaY;
		};
		return StrictMath.hypot(absDeltaX,absDeltaY);
	}
	protected double computeTotalShiftInMeters() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return 0;
		};
		EvaluatingAverager averagerX= new EvaluatingAverager();
		EvaluatingAverager averagerY= new EvaluatingAverager();
		for (int k=0; k < numberOfSegments; k++) {
			TrackSegment s= segments[k];
			s.computeTotalShift(averagerX,averagerY);
		};
		int x1= (int)averagerX.getMinimalValue();
		int y1= (int)averagerY.getMinimalValue();
		int x2= (int)averagerX.getMaximalValue();
		int y2= (int)averagerY.getMaximalValue();
		double[][] physicalMatrixX= transformationMatrices.getPhysicalMatrixX();
		double[][] physicalMatrixY= transformationMatrices.getPhysicalMatrixY();
		double rX1= transformationMatrices.getPhysicalX(x1,y1);
		double rY1= transformationMatrices.getPhysicalY(x1,y1);
		double rX2= transformationMatrices.getPhysicalX(x2,y2);
		double rY2= transformationMatrices.getPhysicalY(x2,y2);
		double absDeltaX= rX2 - rX1;
		double absDeltaY= rY2 - rY1;
		if (absDeltaX < 0) {
			absDeltaX= - absDeltaX;
		};
		if (absDeltaY < 0) {
			absDeltaY= - absDeltaY;
		};
		return StrictMath.hypot(absDeltaX,absDeltaY);
	}
	public double computeMeanVelocity() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return 0;
		};
		UniversalAverager averager= new UniversalAverager();
		for (int k=numberOfSegments-1; k >= 0; k--) {
			TrackSegment s= segments[k];
			s.computeMeanVelocity(averager);
		};
		return averager.getMeanValue();
	}
	//
	public long computeFrame1() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return -1;
		};
		TrackSegment s= segments[0];
		return s.getFirstFrameNumber();
	}
	public long computeFrame2() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return -1;
		};
		TrackSegment s= segments[numberOfSegments-1];
		return s.getLastFrameNumber();
	}
	public long computeTime1() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return -1;
		};
		TrackSegment s= segments[0];
		return s.getFirstTimeInMilliseconds();
	}
	public long computeTime2() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return -1;
		};
		TrackSegment s= segments[numberOfSegments-1];
		return s.getLastTimeInMilliseconds();
	}
	public long computeX1() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return -1;
		};
		TrackSegment s= segments[0];
		return s.getFirstX();
	}
	public long computeX2() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return -1;
		};
		TrackSegment s= segments[numberOfSegments-1];
		return s.getLastX();
	}
	public long computeY1() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return -1;
		};
		TrackSegment s= segments[0];
		return s.getFirstY();
	}
	public long computeY2() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return -1;
		};
		TrackSegment s= segments[numberOfSegments-1];
		return s.getLastY();
	}
	public long computeCentroidX1() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return -1;
		};
		TrackSegment s= segments[0];
		return s.getFirstCentroidX();
	}
	public long computeCentroidX2() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return -1;
		};
		TrackSegment s= segments[numberOfSegments-1];
		return s.getLastCentroidX();
	}
	public long computeCentroidY1() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return -1;
		};
		TrackSegment s= segments[0];
		return s.getFirstCentroidY();
	}
	public long computeCentroidY2() {
		int numberOfSegments= segments.length;
		if (numberOfSegments <= 0) {
			return -1;
		};
		TrackSegment s= segments[numberOfSegments-1];
		return s.getLastCentroidY();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public int[] getCurrentRectangle(long time) {
		if (beginningTime > time || endTime < time) {
			return null;
		};
		int numberOfSegments= segments.length;
		for (int k=numberOfSegments-1; k >= 0; k--) {
			TrackSegment s= segments[k];
			long segmentBeginning= s.getSegmentBeginning();
			long segmentEnd= s.getSegmentEnd();
			if (segmentBeginning > time || segmentEnd < time) {
				continue;
			};
			BlobAttributes[] blobAttributeArray= s.getAttributeArray();
			for (int n=0; n < blobAttributeArray.length; n++) {
				BlobAttributes blobAttributes= blobAttributeArray[n];
				long frameNumber= blobAttributes.getFrameNumber();
				if (frameNumber==time) {
					int[] rectangle= new int[4];
					rectangle[0]= blobAttributes.getX1();
					rectangle[1]= blobAttributes.getX2();
					rectangle[2]= blobAttributes.getY1();
					rectangle[3]= blobAttributes.getY2();
					return rectangle;
				}
			}
		};
		return null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm() {
		if (prologStableTrack != null) {
			return prologStableTrack;
		};
		Term prologIdentifier= new PrologInteger(identifier);
		Term prologType= blobType.toTerm();
		Term prologIsMature= YesNo.boolean2TermYesNo(isStrong);
		Term prologFrame1= new PrologInteger(getFrameNumber1());
		Term prologTime1= new PrologInteger(getTime1());
		Term prologX1= new PrologInteger(getX1());
		Term prologY1= new PrologInteger(getY1());
		Term prologCentroidX1= new PrologInteger(getCentroidX1());
		Term prologCentroidY1= new PrologInteger(getCentroidY1());
		Term prologFrame2= new PrologInteger(getFrameNumber2());
		Term prologTime2= new PrologInteger(getTime2());
		Term prologX2= new PrologInteger(getX2());
		Term prologY2= new PrologInteger(getY2());
		Term prologCentroidX2= new PrologInteger(getCentroidX2());
		Term prologCentroidY2= new PrologInteger(getCentroidY2());
		Term trackSegmentList= trackSegmentListToTerm();
		Term prologNumberOfFrames= new PrologInteger(getNumberOfFrames());
		Term prologMeanBlobArea= new PrologReal(getMeanBlobArea());
		Term prologMeanForegroundArea= new PrologReal(getMeanForegroundArea());
		Term prologMeanContourLength= new PrologReal(getMeanContourLength());
		Term prologTotalDistance= new PrologReal(getTotalDistance());
		Term prologTotalShift= new PrologReal(getTotalShift());
		Term prologMeanVelocity= new PrologReal(getMeanVelocity());
		prologStableTrack= new PrologSet(
			- SymbolCodes.symbolCode_E_identifier,
			prologIdentifier,
			new PrologSet(
			- SymbolCodes.symbolCode_E_type,
			prologType,
			new PrologSet(
			- SymbolCodes.symbolCode_E_is_mature,
			prologIsMature,
			new PrologSet(
			- SymbolCodes.symbolCode_E_frame1,
			prologFrame1,
			new PrologSet(
			- SymbolCodes.symbolCode_E_time1,
			prologTime1,
			new PrologSet(
			- SymbolCodes.symbolCode_E_x1,
			prologX1,
			new PrologSet(
			- SymbolCodes.symbolCode_E_y1,
			prologY1,
			new PrologSet(
			- SymbolCodes.symbolCode_E_centroid_x1,
			prologCentroidX1,
			new PrologSet(
			- SymbolCodes.symbolCode_E_centroid_y1,
			prologCentroidY1,
			new PrologSet(
			- SymbolCodes.symbolCode_E_frame2,
			prologFrame2,
			new PrologSet(
			- SymbolCodes.symbolCode_E_time2,
			prologTime2,
			new PrologSet(
			- SymbolCodes.symbolCode_E_x2,
			prologX2,
			new PrologSet(
			- SymbolCodes.symbolCode_E_y2,
			prologY2,
			new PrologSet(
			- SymbolCodes.symbolCode_E_centroid_x2,
			prologCentroidX2,
			new PrologSet(
			- SymbolCodes.symbolCode_E_centroid_y2,
			prologCentroidY2,
			new PrologSet(
			- SymbolCodes.symbolCode_E_segments,
			trackSegmentList,
			new PrologSet(
			- SymbolCodes.symbolCode_E_number_of_frames,
			prologNumberOfFrames,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_blob_area,
			prologMeanBlobArea,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_foreground_area,
			prologMeanForegroundArea,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_contour_length,
			prologMeanContourLength,
			new PrologSet(
			- SymbolCodes.symbolCode_E_total_distance,
			prologTotalDistance,
			new PrologSet(
			- SymbolCodes.symbolCode_E_total_shift,
			prologTotalShift,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_velocity,
			prologMeanVelocity,
			PrologEmptySet.instance)))))))))))))))))))))));
		return prologStableTrack;
	}
	//
	public Term trackSegmentListToTerm() {
		if (prologTrackSegmentList != null) {
			return prologTrackSegmentList;
		};
		prologTrackSegmentList= PrologEmptyList.instance;
		for (int k=segments.length-1; k >= 0; k--) {
			TrackSegment segment= segments[k];
			if (segment.isEmpty()) {
				continue;
			};
			prologTrackSegmentList= new PrologList(segment.toTerm(),prologTrackSegmentList);
		};
		return prologTrackSegmentList;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term getBlobTerm(long frameNumber) {
		return mapOfBlobTerms.get(frameNumber);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void dump(PrintStream stream) {
		stream.printf("identifier:                 %s\n",identifier);
		stream.printf("beginningTime:              %s\n",beginningTime);
		stream.printf("endTime:                    %s\n",endTime);
		stream.printf("endTime-beginningTime+1:    %s\n",endTime-beginningTime+1);
		stream.printf("segments:                   %s items\n",segments.length);
		stream.printf("computeTotalDistance():     %s\n",computeTotalDistance());
		stream.printf("computeMeanVelocity():      %s\n",computeMeanVelocity());
		stream.printf("minimalTrackDuration:       %s\n",minimalTrackDuration);
		stream.printf("isStrong:                   %s\n",isStrong);
		stream.printf("isInsensible:               %s\n",isInsensible);
		stream.printf("isCompleted:                %s\n",isCompleted);
		stream.printf("isDeposed:                  %s\n",isDeposed);
		stream.printf("\nSEGMENTS:\n");
		for (int k=0; k < segments.length; k++) {
			stream.printf("\n");
			stream.printf("Segment #%s\n",k);
			segments[k].dump(stream);
		}
	}
	public void createMatlab(PrintStream stream) {
		stream.printf("%% identifier:                 %s\n",identifier);
		stream.printf("%% beginningTime:              %s\n",beginningTime);
		stream.printf("%% endTime:                    %s\n",endTime);
		stream.printf("%% endTime-beginningTime+1:    %s\n",endTime-beginningTime+1);
		stream.printf("%% segments:                   %s items\n",segments.length);
		stream.printf("%% computeTotalDistance():     %s\n",computeTotalDistance());
		stream.printf("%% computeMeanVelocity():      %s\n",computeMeanVelocity());
		stream.printf("%% minimalTrackDuration:       %s\n",minimalTrackDuration);
		stream.printf("%% isStrong:                   %s\n",isStrong);
		stream.printf("%% isInsensible:               %s\n",isInsensible);
		stream.printf("%% isCompleted:                %s\n",isCompleted);
		stream.printf("%% isDeposed:                  %s\n",isDeposed);
		stream.printf("%% SEGMENTS:\n");
		for (int k=0; k < segments.length; k++) {
			stream.printf("%% Segment #%s\n",k);
			segments[k].createMatlab(stream,identifier,k);
		}
	}
}
