// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

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
	public int r2WindowHalfwidth;
	public boolean applyCharacteristicLengthMedianFiltering;
	public int characteristicLengthMedianFilterHalfwidth;
	public boolean applyVelocityMedianFiltering;
	public int velocityMedianFilterHalfwidth;
	public int[][] rectangles;
	public long[] foregroundAreaValues;
	public double[] characteristicLengthValues;
	public double meanCharacteristicLength= 0.0;
	public double meanSquaredCharacteristicLength= 0.0;
	public double meanStandardizedArea= 0.0;
	public long[] contourLengthValues;
	public double meanBlobArea= 0.0;
	public double meanForegroundArea= 0.0;
	public double meanContourLength= 0.0;
	public WindowedR2 windowedR2= null;
	public double[] velocityValues;
	protected double velocitySum= 0.0;
	public double meanVelocity= 0.0;
	public BigInteger[] entries;
	public long[] frameNumbers;
	public double[][] spectrumMean;
	public double[][] spectrumDispersion;
	public int spectrumN;
	public boolean hasNoHistogram= true;
	public boolean isRarefied= false;
	public static final int nBins= 256;
	public static final int radius= 5;
	//
	public TrackSegment(BigInteger ownerIdentifier, int serialNumber, long t1, long t2, long t3, int[][] r, long[] fN, long[] fPN, long[] cPN, double[][] mean, double[][] dispersion, int numberOfItems, HashSet<BigInteger> list1, int maximalRarefaction, double[][] iMatrix, double rate, int r2Halfwidth, double[][] characteristicMatrix, boolean applyFilterToCharacteristicLength, int characteristicLengthFilterHalfwidth, boolean applyFilterToVelocity, int velocityFilterHalfwidth) {
		owner= ownerIdentifier;
		number= serialNumber;
		beginningTime= t1;
		endTime= t2;
		breakPointTime= t3;
		maximalRarefactionOfObject= maximalRarefaction;
		inverseMatrix= iMatrix;
		samplingRate= rate;
		r2WindowHalfwidth= r2Halfwidth;
		applyCharacteristicLengthMedianFiltering= applyFilterToCharacteristicLength;
		characteristicLengthMedianFilterHalfwidth= characteristicLengthFilterHalfwidth;
		applyVelocityMedianFiltering= applyFilterToVelocity;
		velocityMedianFilterHalfwidth= velocityFilterHalfwidth;
		rectangles= r;
		frameNumbers= fN;
		foregroundAreaValues= fPN;
		contourLengthValues= cPN;
		int length= rectangles.length;
		characteristicLengthValues= new double[length];
		velocityValues= new double[length];
		double[] velocityVectorX= new double[length];
		double[] velocityVectorY= new double[length];
		if (length > 0) {
			double blobArea= 0.0;
			double foregroundArea= 0.0;
			double contourLength= 0.0;
			for (int n=0; n < length; n++) {
				int x1= rectangles[n][1];
				int x2= rectangles[n][2];
				int y1= rectangles[n][3];
				int y2= rectangles[n][4];
				int width= x2 - x1 + 1;
				int height= y2 - y1 + 1;
				blobArea= blobArea + width*height;
				foregroundArea= foregroundArea + foregroundAreaValues[n];
				contourLength= contourLength + contourLengthValues[n];
				int x3= (x1 + x2) / 2;
				int y3= (y1 + y2) / 2;
				double d1= characteristicMatrix[y1][x1];
				double d2= characteristicMatrix[y1][x2];
				double d3= characteristicMatrix[y1][x3];
				double d4= characteristicMatrix[y2][x1];
				double d5= characteristicMatrix[y2][x2];
				double d6= characteristicMatrix[y2][x3];
				double d7= characteristicMatrix[y3][x1];
				double d8= characteristicMatrix[y3][x2];
				double d0= d1;
				if (d2 > d0) {
					d0= d2;
				};
				if (d3 > d0) {
					d0= d3;
				};
				if (d4 > d0) {
					d0= d4;
				};
				if (d5 > d0) {
					d0= d5;
				};
				if (d6 > d0) {
					d0= d6;
				};
				if (d7 > d0) {
					d0= d7;
				};
				if (d8 > d0) {
					d0= d8;
				};
				characteristicLengthValues[n]= d0;
			};
			meanBlobArea= blobArea / length;
			meanForegroundArea= foregroundArea / length;
			meanContourLength= contourLength / length;
			//
			if (applyCharacteristicLengthMedianFiltering) {
				double[] temporaryVector= new double[length];
				for (int n=0; n < length; n++) {
					temporaryVector[n]= VisionUtils.medianAbs(characteristicLengthValues,n-characteristicLengthMedianFilterHalfwidth,n+characteristicLengthMedianFilterHalfwidth);
				};
				characteristicLengthValues= temporaryVector;
			} else {
				for (int n=0; n < length; n++) {
					if (characteristicLengthValues[n] < 0) {
						characteristicLengthValues[n]= -characteristicLengthValues[n];
					}
				}
			};
			//
			double characteristicLength= 0.0;
			double squaredCharacteristicLength= 0.0;
			double standardizedArea= 0.0;
			for (int n=0; n < length; n++) {
				double value= characteristicLengthValues[n];
				characteristicLength= characteristicLength + value;
				double squaredValue= value*value;
				squaredCharacteristicLength= squaredCharacteristicLength + squaredValue;
				standardizedArea= standardizedArea + (foregroundAreaValues[n] / squaredValue);
			};
			//
			meanCharacteristicLength= characteristicLength / length;
			meanSquaredCharacteristicLength= squaredCharacteristicLength / length;
			meanStandardizedArea= standardizedArea / length;
			//
			windowedR2= WindowedR2.fastWindowedR2(frameNumbers,contourLengthValues,r2WindowHalfwidth);
			//
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
			double[] deltaX11= new double[length];
			double[] deltaX12= new double[length];
			double[] deltaX21= new double[length];
			double[] deltaX22= new double[length];
			double[] deltaY11= new double[length];
			double[] deltaY12= new double[length];
			double[] deltaY21= new double[length];
			double[] deltaY22= new double[length];
			deltaX11[0]= 0;
			deltaX12[0]= 0;
			deltaX21[0]= 0;
			deltaX22[0]= 0;
			deltaY11[0]= 0;
			deltaY12[0]= 0;
			deltaY21[0]= 0;
			deltaY22[0]= 0;
			for (int n=1; n < length; n++) {
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
				deltaX11[n]= rX211 - rX111;
				deltaX12[n]= rX212 - rX112;
				deltaX21[n]= rX221 - rX121;
				deltaX22[n]= rX222 - rX122;
				deltaY11[n]= rY211 - rY111;
				deltaY12[n]= rY212 - rY112;
				deltaY21[n]= rY221 - rY121;
				deltaY22[n]= rY222 - rY122;
				rX111= rX211;
				rX112= rX212;
				rX121= rX221;
				rX122= rX222;
				rY111= rY211;
				rY112= rY212;
				rY121= rY221;
				rY122= rY222;
			};
			if (applyVelocityMedianFiltering) {
				double[] temporaryVectorX11= new double[length];
				double[] temporaryVectorX12= new double[length];
				double[] temporaryVectorX21= new double[length];
				double[] temporaryVectorX22= new double[length];
				double[] temporaryVectorY11= new double[length];
				double[] temporaryVectorY12= new double[length];
				double[] temporaryVectorY21= new double[length];
				double[] temporaryVectorY22= new double[length];
				for (int n=0; n < length; n++) {
					temporaryVectorX11[n]= VisionUtils.medianWnd(deltaX11,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
					temporaryVectorX12[n]= VisionUtils.medianWnd(deltaX12,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
					temporaryVectorX21[n]= VisionUtils.medianWnd(deltaX21,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
					temporaryVectorX22[n]= VisionUtils.medianWnd(deltaX22,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
					temporaryVectorY11[n]= VisionUtils.medianWnd(deltaY11,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
					temporaryVectorY12[n]= VisionUtils.medianWnd(deltaY12,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
					temporaryVectorY21[n]= VisionUtils.medianWnd(deltaY21,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
					temporaryVectorY22[n]= VisionUtils.medianWnd(deltaY22,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
				};
				deltaX11= temporaryVectorX11;
				deltaX12= temporaryVectorX12;
				deltaX21= temporaryVectorX21;
				deltaX22= temporaryVectorX22;
				deltaY11= temporaryVectorY11;
				deltaY12= temporaryVectorY12;
				deltaY21= temporaryVectorY21;
				deltaY22= temporaryVectorY22;
			};
			for (int n=0; n < length; n++) {
				if (deltaX11[n] < 0) {
					deltaX11[n]= -deltaX11[n];
				};
				if (deltaX12[n] < 0) {
					deltaX12[n]= -deltaX12[n];
				};
				if (deltaX21[n] < 0) {
					deltaX21[n]= -deltaX21[n];
				};
				if (deltaX22[n] < 0) {
					deltaX22[n]= -deltaX22[n];
				};
				if (deltaY11[n] < 0) {
					deltaY11[n]= -deltaY11[n];
				};
				if (deltaY12[n] < 0) {
					deltaY12[n]= -deltaY12[n];
				};
				if (deltaY21[n] < 0) {
					deltaY21[n]= -deltaY21[n];
				};
				if (deltaY22[n] < 0) {
					deltaY22[n]= -deltaY22[n];
				}
			};
			int time1= rectangles[0][0];
			for (int n=1; n < length; n++) {
				int time2= rectangles[n][0];
				double absDeltaX11= deltaX11[n];
				double absDeltaX12= deltaX12[n];
				double absDeltaX21= deltaX21[n];
				double absDeltaX22= deltaX22[n];
				double absDeltaY11= deltaY11[n];
				double absDeltaY12= deltaY12[n];
				double absDeltaY21= deltaY21[n];
				double absDeltaY22= deltaY22[n];
				double dX= absDeltaX11;
				if (dX > absDeltaX12) {
					dX= absDeltaX12;
				};
				if (dX > absDeltaX21) {
					dX= absDeltaX21;
				};
				if (dX > absDeltaX22) {
					dX= absDeltaX22;
				};
				double dY= absDeltaY11;
				if (dY > absDeltaY12) {
					dY= absDeltaY12;
				};
				if (dY > absDeltaY21) {
					dY= absDeltaY21;
				};
				if (dY > absDeltaY22) {
					dY= absDeltaY22;
				};
				int deltaTime= time2 - time1;
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
			};
			velocityVectorX[0]= 0;
			velocityVectorY[0]= 0;
			if (applyVelocityMedianFiltering) {
				double[] temporaryVectorX= new double[length];
				double[] temporaryVectorY= new double[length];
				for (int n=0; n < length; n++) {
					// Fixed 2014-05-13:
					// velocityVectorX[n]= VisionUtils.medianAbs(velocityVectorX,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth+1);
					// velocityVectorY[n]= VisionUtils.medianAbs(velocityVectorY,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth+1);
					temporaryVectorX[n]= VisionUtils.medianAbs(velocityVectorX,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
					temporaryVectorY[n]= VisionUtils.medianAbs(velocityVectorY,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
				};
				velocityVectorX= temporaryVectorX;
				velocityVectorY= temporaryVectorY;
			};
			for (int n=0; n < length; n++) {
				double velocityX= velocityVectorX[n];
				double velocityY= velocityVectorY[n];
				double velocityXY= StrictMath.hypot(velocityX,velocityY);
				velocityValues[n]= velocityXY;
			};
			if (length > 1) {
				velocityValues[0]= velocityValues[1];
			};
			if (applyVelocityMedianFiltering) {
				double[] temporaryVector= new double[length];
				for (int n=0; n < length; n++) {
					temporaryVector[n]= VisionUtils.medianAbs(velocityValues,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
				};
				velocityValues= temporaryVector;
			};
			velocitySum= 0.0;
			for (int n=0; n < length; n++) {
				velocitySum+= velocityValues[n];
			};
			meanVelocity= velocitySum / length;
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
	public long getWindowedR2Cardinality() {
		if (windowedR2 != null) {
			return windowedR2.cardinality;
		} else {
			return 0;
		}
	}
	public double getWindowedR2Mean() {
		if (windowedR2 != null) {
			return windowedR2.mean;
		} else {
			return WindowedR2.noValue;
		}
	}
	public double getWindowedR2StandardDeviation() {
		if (windowedR2 != null) {
			return windowedR2.standardDeviation;
		} else {
			return 0;
		}
	}
	public double getWindowedR2Skewness() {
		if (windowedR2 != null) {
			return windowedR2.skewness;
		} else {
			return 0;
		}
	}
	public double getWindowedR2Kurtosis() {
		if (windowedR2 != null) {
			return windowedR2.kurtosis;
		} else {
			return 0;
		}
	}
	public double[] getWindowedR2ReferentValues() {
		if (windowedR2 != null) {
			return windowedR2.referentValues;
		} else {
			return new double[0];
		}
	}
	public void computeMeanArea(MeanArea mean) {
		int length= rectangles.length;
		for (int n=0; n < length; n++) {
			int x1= rectangles[n][1];
			int x2= rectangles[n][2];
			int y1= rectangles[n][3];
			int y2= rectangles[n][4];
			mean.add(x1,x2,y1,y2);
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
	public double computeMeanVelocity() {
		MeanVelocity mean= new MeanVelocity();
		computeMeanVelocity(mean);
		return mean.getMeanVelocity();
	}
	public void computeMeanVelocity(MeanVelocity mean) {
		mean.add(velocityValues.length,velocitySum);
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
		stream.printf("computeMeanVelocity():      %s\n",computeMeanVelocity());
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
		stream.printf("%% computeMeanVelocity():      %s\n",computeMeanVelocity());
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
		stream.printf("R2_mean_%s_%s_%s= %s;\n",identifier,beginningTime,endTime,getWindowedR2Mean());
		stream.printf("R2_standard_deviation_%s_%s_%s= %s;\n",identifier,beginningTime,endTime,getWindowedR2StandardDeviation());
		stream.printf("R2_skewness_%s_%s_%s= %s;\n",identifier,beginningTime,endTime,getWindowedR2Skewness());
		stream.printf("R2_Kurtosis_%s_%s_%s= %s;\n",identifier,beginningTime,endTime,getWindowedR2Kurtosis());
		stream.printf("V_%s_%s_%s= [",identifier,beginningTime,endTime);
		for (int n=0; n < velocityValues.length; n++) {
			double vXY= velocityValues[n];
			stream.printf(" %s",vXY);
			// stream.printf(";");
			// stream.printf("...\n");
		};
		stream.printf(" ];\n");
		stream.printf("V_mean_%s_%s_%s= %s;\n",identifier,beginningTime,endTime,meanVelocity);
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
