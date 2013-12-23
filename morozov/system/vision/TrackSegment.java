// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.FontMetrics;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.math.BigInteger;
import java.io.PrintStream;

class TrackSegment {
	public BigInteger owner;
	public int number;
	public long beginningTime;
	public long endTime;
	public long breakPointTime;
	public int maximalRarefactionOfObject;
	public double[][] inverseMatrix;
	public double samplingRate;
	public boolean applyMedianFiltering;
	public int medianFilterHalfwidth;
	public int[][] rectangles;
	public double[][] velocity;
	public double[][] acceleration;
	public BigInteger[] entries;
	public double[][] spectrumMean;
	public double[][] spectrumDispersion;
	public int spectrumN;
	public boolean hasNoHistogram= true;
	public boolean isRarefied= false;
	public static final int nBins= 256;
	public static final int radius= 5;
	//
	public TrackSegment(BigInteger ownerIdentifier, int serialNumber, long t1, long t2, long t3, int[][] r, double[][] mean, double[][] dispersion, int numberOfItems, HashSet<BigInteger> list1, int maximalRarefaction, double[][] iMatrix, double rate, boolean applyFilterToVelocity, int filterHalfwidth) {
		owner= ownerIdentifier;
		number= serialNumber;
		beginningTime= t1;
		endTime= t2;
		breakPointTime= t3;
		maximalRarefactionOfObject= maximalRarefaction;
		inverseMatrix= iMatrix;
		samplingRate= rate;
		applyMedianFiltering= applyFilterToVelocity;
		medianFilterHalfwidth= filterHalfwidth;
		rectangles= r;
		int length= rectangles.length;
		velocity= new double[length+1][3];
		acceleration= new double[length+1][3];
		double[] velocityVectorX= new double[length];
		double[] velocityVectorY= new double[length];
		if (length > 0) {
			int time1= rectangles[0][0];
			double[] xy;
			int x11= rectangles[0][1];
			int x12= rectangles[0][2];
			int y11= rectangles[0][3];
			int y12= rectangles[0][4];
			xy= VisionUtils.project(x11,y11,inverseMatrix);
			double rX111= xy[0];
			double rY111= xy[1];
			xy= VisionUtils.project(x11,y12,inverseMatrix);
			double rX112= xy[0];
			double rY112= xy[1];
			xy= VisionUtils.project(x12,y11,inverseMatrix);
			double rX121= xy[0];
			double rY121= xy[1];
			xy= VisionUtils.project(x12,y12,inverseMatrix);
			double rX122= xy[0];
			double rY122= xy[1];
			for (int n=1; n < length; n++) {
				int time2= rectangles[n][0];
				int x21= rectangles[n][1];
				int x22= rectangles[n][2];
				int y21= rectangles[n][3];
				int y22= rectangles[n][4];
				xy= VisionUtils.project(x21,y21,inverseMatrix);
				double rX211= xy[0];
				double rY211= xy[1];
				xy= VisionUtils.project(x21,y22,inverseMatrix);
				double rX212= xy[0];
				double rY212= xy[1];
				xy= VisionUtils.project(x22,y21,inverseMatrix);
				double rX221= xy[0];
				double rY221= xy[1];
				xy= VisionUtils.project(x22,y22,inverseMatrix);
				double rX222= xy[0];
				double rY222= xy[1];
				int deltaTime= time2 - time1;
				double deltaX11= rX211 - rX111;
				double deltaX12= rX212 - rX112;
				double deltaX21= rX221 - rX121;
				double deltaX22= rX222 - rX122;
				double deltaY11= rY211 - rY111;
				double deltaY12= rY212 - rY112;
				double deltaY21= rY221 - rY121;
				double deltaY22= rY222 - rY122;
				double absDeltaX11= deltaX11;
				if (absDeltaX11 < 0) {
					absDeltaX11= - absDeltaX11;
				};
				double absDeltaX12= deltaX12;
				if (absDeltaX12 < 0) {
					absDeltaX12= - absDeltaX12;
				};
				double absDeltaX21= deltaX21;
				if (absDeltaX21 < 0) {
					absDeltaX21= - absDeltaX21;
				};
				double absDeltaX22= deltaX22;
				if (absDeltaX22 < 0) {
					absDeltaX22= - absDeltaX22;
				};
				double absDeltaY11= deltaY11;
				if (absDeltaY11 < 0) {
					absDeltaY11= - absDeltaY11;
				};
				double absDeltaY12= deltaY12;
				if (absDeltaY12 < 0) {
					absDeltaY12= - absDeltaY12;
				};
				double absDeltaY21= deltaY21;
				if (absDeltaY21 < 0) {
					absDeltaY21= - absDeltaY21;
				};
				double absDeltaY22= deltaY22;
				if (absDeltaY22 < 0) {
					absDeltaY22= - absDeltaY22;
				};
				double dX= deltaX11;
				if (dX > absDeltaX12) {
					dX= deltaX12;
				};
				if (dX > absDeltaX21) {
					dX= deltaX21;
				};
				if (dX > absDeltaX22) {
					dX= deltaX22;
				};
				double dY= deltaY11;
				if (dY > absDeltaY12) {
					dY= deltaY12;
				};
				if (dY > absDeltaY21) {
					dY= deltaY21;
				};
				if (dY > absDeltaY22) {
					dY= deltaY22;
				};
				double velocityX;
				double velocityY;
				if (samplingRate > 0.0) {
					velocityX= dX * samplingRate / deltaTime;
					velocityY= dY * samplingRate / deltaTime;
				} else {
					velocityX= dX / deltaTime;
					velocityY= dY / deltaTime;
				};
				velocityVectorX[n]= velocityX;
				velocityVectorY[n]= velocityY;
				time1= time2;
				rX111= rX211;
				rX112= rX212;
				rX121= rX221;
				rX122= rX222;
				rY111= rY211;
				rY112= rY212;
				rY121= rY221;
				rY122= rY222;
			};
			if (length > 1) {
				// velocityVectorX[0]= velocityVectorX[1];
				// velocityVectorY[0]= velocityVectorY[1];
				velocityVectorX[0]= 0;
				velocityVectorY[0]= 0;
			};
			// int halfWindow= 12;
			// int halfWindow= 1;
			// int halfWindow= 5;
			// int halfWindow= 3;
			// int halfWindow= 0;
			// int halfWindow= 1;
			// int halfWindow= 2;
			// int halfWindow= 3;
			// int halfWindow= 4;
			// int halfWindow= 5;
			if (applyMedianFiltering) {
				for (int n=0; n < length; n++) {
					velocityVectorX[n]= VisionUtils.medianAbs(velocityVectorX,n-medianFilterHalfwidth,n+medianFilterHalfwidth+1);
					velocityVectorY[n]= VisionUtils.medianAbs(velocityVectorY,n-medianFilterHalfwidth,n+medianFilterHalfwidth+1);
				}
			} else {
				for (int n=0; n < length; n++) {
					if (velocityVectorX[n] < 0) {
						velocityVectorX[n]= -velocityVectorX[n];
					};
					if (velocityVectorY[n] < 0) {
						velocityVectorY[n]= -velocityVectorY[n];
					}
				}
			};
			double meanVelocityX= 0;
			double meanVelocityY= 0;
			double meanVelocityXY= 0;
			for (int n=0; n < length; n++) {
				double velocityX= velocityVectorX[n];
				double velocityY= velocityVectorY[n];
				double velocityXY= StrictMath.hypot(velocityX,velocityY);
				velocity[n][0]= velocityX;
				velocity[n][1]= velocityY;
				velocity[n][2]= velocityXY;
				meanVelocityX= meanVelocityX + velocityX;
				meanVelocityY= meanVelocityY + velocityY;
				meanVelocityXY= meanVelocityXY + velocityXY;
			};
			if (length > 1) {
				velocity[0][0]= velocity[1][0];
				velocity[0][1]= velocity[1][1];
				velocity[0][2]= velocity[1][2];
				meanVelocityX= meanVelocityX / length;
				meanVelocityY= meanVelocityY / length;
				meanVelocityXY= meanVelocityXY / length;
			};
			velocity[length][0]= meanVelocityX;
			velocity[length][1]= meanVelocityY;
			velocity[length][2]= meanVelocityXY;
			double meanAccelerationX= 0;
			double meanAccelerationY= 0;
			double meanAccelerationXY= 0;
			time1= rectangles[0][0];
			double velocityX1= velocity[0][0];
			double velocityY1= velocity[0][1];
			for (int n=1; n < length; n++) {
				int time2= rectangles[n][0];
				double velocityX2= velocity[n][0];
				double velocityY2= velocity[n][1];
				int deltaTime= time2 - time1;
				double accelerationX;
				double accelerationY;
				if (samplingRate > 0) {
					accelerationX= (velocityX2 - velocityX1) * samplingRate / deltaTime;
					accelerationY= (velocityY2 - velocityY1) * samplingRate / deltaTime;
				} else {
					accelerationX= (velocityX2 - velocityX1) / deltaTime;
					accelerationY= (velocityY2 - velocityY1) / deltaTime;
				};
				double accelerationXY= StrictMath.hypot(accelerationX,accelerationY);
				acceleration[n][0]= accelerationX;
				acceleration[n][1]= accelerationY;
				acceleration[n][2]= accelerationXY;
				meanAccelerationX= meanAccelerationX + accelerationX;
				meanAccelerationY= meanAccelerationY + accelerationY;
				meanAccelerationXY= meanAccelerationXY + accelerationXY;
				time1= time2;
				velocityX1= velocityX2;
				velocityY1= velocityY2;
			};
			if (length > 1) {
				if (length > 2) {
					acceleration[1][0]= acceleration[2][0];
					acceleration[1][1]= acceleration[2][1];
					acceleration[1][2]= acceleration[2][2];
				};
				acceleration[0][0]= acceleration[1][0];
				acceleration[0][1]= acceleration[1][1];
				acceleration[0][2]= acceleration[1][2];
				meanAccelerationX= meanAccelerationX / length;
				meanAccelerationY= meanAccelerationY / length;
				meanAccelerationXY= meanAccelerationXY / length;
			};
			acceleration[length][0]= meanAccelerationX;
			acceleration[length][1]= meanAccelerationY;
			acceleration[length][2]= meanAccelerationXY;
		};
		if (list1 != null) {
			int length1= list1.size();
			entries= new BigInteger[length1];
			Iterator<BigInteger> iterator= list1.iterator();
			int index= 0;
			while (iterator.hasNext()) {
				entries[index]= iterator.next();
				index++;
			}
		} else {
			entries= new BigInteger[0];
		};
		spectrumMean= mean;
		spectrumDispersion= dispersion;
		spectrumN= numberOfItems;
		int bandNumber= spectrumMean.length;
		if (bandNumber >= 1) {
			hasNoHistogram= false;
			if (maximalRarefaction >= 0) {
				isRarefied= true;
				for (int band=0; band < bandNumber; band++) {
					if (maximalRarefactionOfObject >= spectrumMean[band][nBins+2]) {
						isRarefied= false;
						break;
					}
				}
			} else {
				isRarefied= false;
			}
		} else {
			hasNoHistogram= true;
			isRarefied= false;
		}
	}
	public void assembleConnectedGraph(boolean isFirst, boolean isLast, ConnectedGraph graph, HashMap<BigInteger,StableTrack> tracks) {
		if (!graph.containsSegment(this)) {
			graph.addSegment(this,isFirst,isLast);
			for (int n=0; n < entries.length; n++) {
				BigInteger neighbour= entries[n];
				StableTrack track= tracks.get(neighbour);
				if (track != null) {
					track.assembleConnectedGraph(graph,tracks);
				}
			}
		}
	}
	public void computeMeanArea(MeanArea mean) {
		int length= rectangles.length;
		for (int n=0; n < length; n++) {
			int x1= rectangles[n][1];
			int x2= rectangles[n][2];
			int y1= rectangles[n][3];
			int y2= rectangles[n][4];
			mean.add(x1,x2,y1,y2,inverseMatrix);
		}
	}
	public double computeTotalDistance() {
		MeanArea mean= new MeanArea();
		computeMeanArea(mean);
		double deltaX= mean.getMaximalCenterX() - mean.getMinimalCenterX();
		double deltaY= mean.getMaximalCenterY() - mean.getMinimalCenterY();
		return StrictMath.hypot(deltaX,deltaY);
	}
	public double computeTotalVelocity() {
		double distance= computeTotalDistance();
		long deltaTime= endTime - beginningTime + 1;
		if (samplingRate > 0) {
			return distance * samplingRate / deltaTime;
		} else {
			return distance / deltaTime;
		}
	}
	public double computeMeanVelocity(boolean averageAbsoluteValues) {
		MeanVelocity mean= new MeanVelocity();
		computeMeanVelocity(mean);
		if (averageAbsoluteValues) {
			return mean.getVelocityXY();
		} else {
			return StrictMath.hypot(mean.getAccumulatedVelocityX(),mean.getAccumulatedVelocityY());
		}
	}
	public void computeMeanVelocity(MeanVelocity mean) {
		mean.add(velocity);
	}
	public double computeMeanAcceleration(boolean averageAbsoluteValues) {
		MeanAcceleration mean= new MeanAcceleration();
		computeMeanAcceleration(mean);
		if (averageAbsoluteValues) {
			return mean.getAccelerationXY();
		} else {
			return StrictMath.hypot(mean.getAccelerationX(),mean.getAccelerationY());
		}
	}
	public void computeMeanAcceleration(MeanAcceleration mean) {
		int length= acceleration.length;
		mean.add(acceleration[length-1],length-1);
	}
	//
	public void dump(PrintStream stream) {
		stream.printf("owner:                      %s\n",owner);
		stream.printf("number:                     %s\n",number);
		stream.printf("beginningTime:              %s\n",beginningTime);
		stream.printf("endTime:                    %s\n",endTime);
		stream.printf("endTime-beginningTime+1:    %s\n",endTime-beginningTime+1);
		stream.printf("breakPointTime:             %s\n",breakPointTime);
		// stream.printf("label:                      %s\n",label);
		stream.printf("rectangles:                 %s items\n",rectangles.length);
		stream.printf("computeTotalDistance():     %s\n",computeTotalDistance());
		stream.printf("totalDistance / Time:       %s\n",computeTotalVelocity());
		stream.printf("computeMeanVelocity(T):     %s\n",computeMeanVelocity(true));
		stream.printf("computeMeanVelocity(F):     %s\n",computeMeanVelocity(false));
		stream.printf("computeMeanAcceleration(T): %s\n",computeMeanAcceleration(true));
		stream.printf("computeMeanAcceleration(F): %s\n",computeMeanAcceleration(false));
		stream.printf("entries:           ");
		for (int n=0; n < entries.length; n++) {
			stream.printf(" %s",entries[n]);
		};
		stream.printf("\n");
		stream.printf("spectrumMean:              %s bands\n",spectrumMean.length);
		stream.printf("spectrumDispersion:        %s bands\n",spectrumDispersion.length);
		stream.printf("spectrumN:                 %s\n",spectrumN);
		stream.printf("hasNoHistogram:            %s\n",hasNoHistogram);
		stream.printf("isRarefied:                %s\n",isRarefied);
	}
	public void createMatlab(PrintStream stream, BigInteger identifier, int index) {
		stream.printf("%% owner:                      %s\n",owner);
		stream.printf("%% number:                     %s\n",number);
		stream.printf("%% beginningTime:              %s\n",beginningTime);
		stream.printf("%% endTime:                    %s\n",endTime);
		stream.printf("%% endTime-beginningTime+1:    %s\n",endTime-beginningTime+1);
		stream.printf("%% breakPointTime:             %s\n",breakPointTime);
		stream.printf("%% rectangles:                 %s items\n",rectangles.length);
		stream.printf("%% computeTotalDistance():     %s\n",computeTotalDistance());
		stream.printf("%% totalDistance / Time:       %s\n",computeTotalVelocity());
		stream.printf("%% computeMeanVelocity(T):     %s\n",computeMeanVelocity(true));
		stream.printf("%% computeMeanVelocity(F):     %s\n",computeMeanVelocity(false));
		stream.printf("%% computeMeanAcceleration(T): %s\n",computeMeanAcceleration(true));
		stream.printf("%% computeMeanAcceleration(F): %s\n",computeMeanAcceleration(false));
		stream.printf("R_%s_%s_%s= [...\n",identifier,beginningTime,endTime);
		for (int n=0; n < rectangles.length; n++) {
			int shift= rectangles[n][0];
			int x1= rectangles[n][1];
			int x2= rectangles[n][2];
			int y1= rectangles[n][3];
			int y2= rectangles[n][4];
			stream.printf(" [ %s %s %s %s %s ]",beginningTime+shift,x1,x2,y1,y2);
			if (n < rectangles.length - 1) {
				stream.printf(";");
			};
			stream.printf("...\n");
		};
		stream.printf("];\n");
		stream.printf("V_%s_%s_%s= [...\n",identifier,beginningTime,endTime);
		for (int n=0; n < velocity.length-1; n++) {
			// int shift= rectangles[n][0];
			double vX= velocity[n][0];
			double vY= velocity[n][1];
			double vXY= velocity[n][2];
			stream.printf(" [ %s %s %s ]",vX,vY,vXY);
			stream.printf(";");
			stream.printf("...\n");
		};
		double vX= velocity[velocity.length-1][0];
		double vY= velocity[velocity.length-1][1];
		double vXY= velocity[velocity.length-1][2];
		stream.printf(" [ %s %s %s ]",vX,vY,vXY);
		stream.printf("];\n");
		stream.printf("%% entries:      ");
		for (int n=0; n < entries.length; n++) {
			stream.printf(" %s",entries[n]);
		};
		stream.printf("\n");
		stream.printf("%% spectrumMean:\n");
		int bandNumber= spectrumMean.length;
		stream.printf("M_%s_%s_%s= [...\n",identifier,beginningTime,endTime);
		for (int band=0; band < bandNumber; band++) {
			stream.printf("[");
			for (int n=0; n < nBins+3; n++) {
				stream.printf(" %s",spectrumMean[band][n]);
			};
			stream.printf(" ];...\n");
		};
		stream.printf("];\n");
		stream.printf("%% spectrumDispersion:\n");
		bandNumber= spectrumDispersion.length;
		stream.printf("D_%s_%s_%s= [...\n",identifier,beginningTime,endTime);
		for (int band=0; band < bandNumber; band++) {
			stream.printf("[");
			for (int n=0; n < nBins; n++) {
				stream.printf(" %s",spectrumDispersion[band][n]);
			};
			stream.printf(" ];...\n");
		};
		stream.printf("];\n");
		stream.printf("%% spectrumN:                 %s\n",spectrumN);
		stream.printf("%% hasNoHistogram:            %s\n",hasNoHistogram);
		stream.printf("%% isRarefied:                %s\n",isRarefied);
		stream.printf("N_%s_%s_%s= %s;\n",identifier,beginningTime,endTime,spectrumN);
		stream.printf("T%s{%s}.owner= %s;\n",identifier,index+1,owner);
		stream.printf("T%s{%s}.number= %s;\n",identifier,index+1,number);
		stream.printf("T%s{%s}.beginningTime= %s;\n",identifier,index+1,beginningTime);
		stream.printf("T%s{%s}.endTime= %s;\n",identifier,index+1,endTime);
		stream.printf("T%s{%s}.breakPointTime= %s;\n",identifier,index+1,breakPointTime);
		stream.printf("T%s{%s}.rectangles= R_%s_%s_%s;\n",identifier,index+1,identifier,beginningTime,endTime);
		stream.printf("T%s{%s}.velocity= V_%s_%s_%s;\n",identifier,index+1,identifier,beginningTime,endTime);
		stream.printf("T%s{%s}.mean= M_%s_%s_%s;\n",identifier,index+1,identifier,beginningTime,endTime);
		stream.printf("T%s{%s}.dispersion= D_%s_%s_%s;\n",identifier,index+1,identifier,beginningTime,endTime);
		stream.printf("T%s{%s}.N= N_%s_%s_%s;\n",identifier,index+1,identifier,beginningTime,endTime);
	}
}
