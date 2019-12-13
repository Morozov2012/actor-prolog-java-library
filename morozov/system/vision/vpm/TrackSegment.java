// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import target.*;

import morozov.system.vision.vpm.converters.*;
import morozov.terms.*;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.math.BigInteger;
import java.io.PrintStream;

public class TrackSegment {
	//
	protected BigInteger owner;
	protected int number;
	protected long segmentBeginning;
	protected long segmentEnd;
	protected long segmentBreakPoint;
	protected long beginningTimeInMilliseconds;
	protected long endTimeInMilliseconds;
	//
	protected BlobAttributes[] attributeArray;
	//
	protected double samplingRate;
	protected int r2WindowHalfwidth;
	protected boolean applyCharacteristicLengthMedianFiltering;
	protected int characteristicLengthMedianFilterHalfwidth;
	protected boolean applyVelocityMedianFiltering;
	protected int velocityMedianFilterHalfwidth;
	//
	protected double[] characteristicLengthValues;
	protected TransformationMatrices transformationMatrices;
	protected double meanCharacteristicLength= 0.0;
	protected double meanSquaredCharacteristicLength= 0.0;
	protected double meanStandardizedArea= 0.0;
	protected double meanBlobArea= 0.0;
	protected double meanForegroundArea= 0.0;
	protected double meanContourLength= 0.0;
	protected WindowedR2 windowedR2= null;
	protected double[] distanceValues;
	protected double[] velocityValues;
	protected double totalDistance= 0.0;
	protected double velocitySum= 0.0;
	protected double meanVelocity= 0.0;
	protected BigInteger[] entries;
	protected long[] frameNumbers;
	//
	protected MeanColorHistograms meanColorHistograms= new MeanColorHistograms();
	//
	protected Term prologTrackSegment;
	protected Term prologTrackOfBlob;
	//
	///////////////////////////////////////////////////////////////
	//
	public TrackSegment(
			BigInteger ownerIdentifier,
			int serialNumber,
			long beginningPoint,
			long endPoint,
			long breakPoint,
			long beginningTime,
			long endTime,
			BlobAttributes[] a,
			long[] fN,
			HashSet<BigInteger> list1,
			double rate,
			int r2Halfwidth,
			TransformationMatrices tMatrices,
			boolean applyFilterToCharacteristicLength,
			int characteristicLengthFilterHalfwidth,
			boolean applyFilterToVelocity,
			int velocityFilterHalfwidth) {
		//
		owner= ownerIdentifier;
		number= serialNumber;
		segmentBeginning= beginningPoint;
		segmentEnd= endPoint;
		segmentBreakPoint= breakPoint;
		beginningTimeInMilliseconds= beginningTime;
		endTimeInMilliseconds= endTime;
		attributeArray= a;
		frameNumbers= fN;
		samplingRate= rate;
		r2WindowHalfwidth= r2Halfwidth;
		applyCharacteristicLengthMedianFiltering= applyFilterToCharacteristicLength;
		characteristicLengthMedianFilterHalfwidth= characteristicLengthFilterHalfwidth;
		applyVelocityMedianFiltering= applyFilterToVelocity;
		velocityMedianFilterHalfwidth= velocityFilterHalfwidth;
		transformationMatrices= tMatrices;
		//
		int length= attributeArray.length;
		characteristicLengthValues= new double[length];
		velocityValues= new double[length];
		distanceValues= new double[length];
		if (length > 0) {
			computeDistanceAndVelocity();
		};
		//
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
		}
	}
	protected void computeDistanceAndVelocity() {
		int length= attributeArray.length;
		double[] velocityVectorX= new double[length];
		double[] velocityVectorY= new double[length];
		long[] contourLengthArray= new long[length];
		double[][] physicalMatrixX= transformationMatrices.getPhysicalMatrixX();
		double[][] physicalMatrixY= transformationMatrices.getPhysicalMatrixY();
		double[][] characteristicMatrix= transformationMatrices.getCharacteristicMatrix();
		meanBlobArea= 0.0;
		meanForegroundArea= 0.0;
		meanContourLength= 0.0;
		for (int n=0; n < length; n++) {
			BlobAttributes attributes0= attributeArray[n];
			int x1= attributes0.getX1();
			int x2= attributes0.getX2();
			int y1= attributes0.getY1();
			int y2= attributes0.getY2();
			int width= x2 - x1 + 1;
			int height= y2 - y1 + 1;
			meanBlobArea= meanBlobArea + width*height;
			meanForegroundArea= meanForegroundArea + attributes0.getForegroundArea();
			long contourLength= attributes0.getContourLength();
			contourLengthArray[n]= contourLength;
			meanContourLength= meanContourLength + contourLength;
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
		meanBlobArea= meanBlobArea / length;
		meanForegroundArea= meanForegroundArea / length;
		meanContourLength= meanContourLength / length;
		if (applyCharacteristicLengthMedianFiltering) {
			if (length > characteristicLengthMedianFilterHalfwidth*2+1) {
				double[] temporaryVector= new double[length];
				for (int n=0; n < length; n++) {
					temporaryVector[n]= VisionUtils.medianAbs(characteristicLengthValues,n-characteristicLengthMedianFilterHalfwidth,n+characteristicLengthMedianFilterHalfwidth);
				};
				characteristicLengthValues= temporaryVector;
			} else {
				double medianValue= VisionUtils.medianAbs(characteristicLengthValues,0,characteristicLengthValues.length-1);
				for (int n=0; n < length; n++) {
					characteristicLengthValues[n]= medianValue;
				}
			}
		} else {
			for (int n=0; n < length; n++) {
				if (characteristicLengthValues[n] < 0) {
					characteristicLengthValues[n]= -characteristicLengthValues[n];
				}
			}
		};
		double characteristicLength= 0.0;
		double squaredCharacteristicLength= 0.0;
		double standardizedArea= 0.0;
		for (int n=0; n < length; n++) {
			BlobAttributes attributes0= attributeArray[n];
			double value= characteristicLengthValues[n];
			characteristicLength= characteristicLength + value;
			double squaredValue= value*value;
			squaredCharacteristicLength= squaredCharacteristicLength + squaredValue;
			standardizedArea= standardizedArea + (attributes0.getForegroundArea() / squaredValue);
		};
		meanCharacteristicLength= characteristicLength / length;
		meanSquaredCharacteristicLength= squaredCharacteristicLength / length;
		meanStandardizedArea= standardizedArea / length;
		windowedR2= WindowedR2.fastWindowedR2(frameNumbers,contourLengthArray,r2WindowHalfwidth);
		double[] xy;
		BlobAttributes attributes0= attributeArray[0];
		int x11= attributes0.getX1();
		int x12= attributes0.getX2();
		int y11= attributes0.getY1();
		int y12= attributes0.getY2();
		double rX111= physicalMatrixX[y11][x11];
		double rY111= physicalMatrixY[y11][x11];
		double rX112= physicalMatrixX[y11][x12];
		double rY112= physicalMatrixY[y11][x12];
		double rX121= physicalMatrixX[y12][x11];
		double rY121= physicalMatrixY[y12][x11];
		double rX122= physicalMatrixX[y12][x12];
		double rY122= physicalMatrixY[y12][x12];
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
			BlobAttributes attributes1= attributeArray[n];
			int x21= attributes1.getX1();
			int x22= attributes1.getX2();
			int y21= attributes1.getY1();
			int y22= attributes1.getY2();
			double rX211= physicalMatrixX[y21][x21];
			double rY211= physicalMatrixY[y21][x21];
			double rX212= physicalMatrixX[y21][x22];
			double rY212= physicalMatrixY[y21][x22];
			double rX221= physicalMatrixX[y22][x21];
			double rY221= physicalMatrixY[y22][x21];
			double rX222= physicalMatrixX[y22][x22];
			double rY222= physicalMatrixY[y22][x22];
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
			if (length > velocityMedianFilterHalfwidth*2+1) {
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
			} else {
				double medianX11= VisionUtils.medianWnd(deltaX11,0,deltaX11.length-1);
				double medianX12= VisionUtils.medianWnd(deltaX12,0,deltaX12.length-1);
				double medianX21= VisionUtils.medianWnd(deltaX21,0,deltaX21.length-1);
				double medianX22= VisionUtils.medianWnd(deltaX22,0,deltaX22.length-1);
				double medianY11= VisionUtils.medianWnd(deltaY11,0,deltaY11.length-1);
				double medianY12= VisionUtils.medianWnd(deltaY12,0,deltaY12.length-1);
				double medianY21= VisionUtils.medianWnd(deltaY21,0,deltaY21.length-1);
				double medianY22= VisionUtils.medianWnd(deltaY22,0,deltaY22.length-1);
				for (int n=0; n < length; n++) {
					deltaX11[n]= medianX11;
					deltaX12[n]= medianX12;
					deltaX21[n]= medianX21;
					deltaX22[n]= medianX22;
					deltaY11[n]= medianY11;
					deltaY12[n]= medianY12;
					deltaY21[n]= medianY21;
					deltaY22[n]= medianY22;
				}
			}
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
		BlobAttributes attributes1= attributeArray[0];
		long frameNumber1= attributes1.getFrameNumber();
		long timeInMilliseconds1= attributes1.getTimeInMilliseconds();
		for (int n=1; n < length; n++) {
			BlobAttributes attributes2= attributeArray[n];
			long frameNumber2= attributes2.getFrameNumber();
			long timeInMilliseconds2= attributes2.getTimeInMilliseconds();
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
			distanceValues[n]= StrictMath.hypot(dX,dY);
			double velocityX;
			double velocityY;
			if (timeInMilliseconds1 >= 0 && timeInMilliseconds2 >= 0) {
				long timeDifference= timeInMilliseconds2 - timeInMilliseconds1;
				velocityX= dX * 1000.0 / timeDifference;
				velocityY= dY * 1000.0 / timeDifference;
			} else {
				long frameNumberDifference= frameNumber2 - frameNumber1;
				if (samplingRate > 0.0) {
					velocityX= dX * samplingRate / frameNumberDifference;
					velocityY= dY * samplingRate / frameNumberDifference;
				} else {
					velocityX= dX / frameNumberDifference;
					velocityY= dY / frameNumberDifference;
				}
			};
			velocityVectorX[n]= velocityX;
			velocityVectorY[n]= velocityY;
			frameNumber1= frameNumber2;
			timeInMilliseconds1= timeInMilliseconds2;
		};
		velocityVectorX[0]= 0;
		velocityVectorY[0]= 0;
		if (applyVelocityMedianFiltering) {
			if (length > velocityMedianFilterHalfwidth*2+1) {
				double[] temporaryVectorX= new double[length];
				double[] temporaryVectorY= new double[length];
				for (int n=0; n < length; n++) {
					temporaryVectorX[n]= VisionUtils.medianAbs(velocityVectorX,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
					temporaryVectorY[n]= VisionUtils.medianAbs(velocityVectorY,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
				};
				velocityVectorX= temporaryVectorX;
				velocityVectorY= temporaryVectorY;
			} else {
				double medianX= VisionUtils.medianAbs(velocityVectorX,0,velocityVectorX.length-1);
				double medianY= VisionUtils.medianAbs(velocityVectorY,0,velocityVectorY.length-1);
				for (int n=0; n < length; n++) {
					velocityVectorX[n]= medianX;
					velocityVectorY[n]= medianY;
				}
			}
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
			if (length > velocityMedianFilterHalfwidth*2+1) {
				double[] temporaryVector= new double[length];
				for (int n=0; n < length; n++) {
					temporaryVector[n]= VisionUtils.medianAbs(velocityValues,n-velocityMedianFilterHalfwidth,n+velocityMedianFilterHalfwidth);
				};
				velocityValues= temporaryVector;
			} else {
				double medianVelocity= VisionUtils.medianAbs(velocityValues,0,velocityValues.length-1);
				for (int n=0; n < length; n++) {
					velocityValues[n]= medianVelocity;
				}
			}
		};
		totalDistance= 0.0;
		velocitySum= 0.0;
		for (int n=1; n < length; n++) {
			totalDistance+= distanceValues[n];
			velocitySum+= velocityValues[n];
		};
		if (length-1 > 0) {
			meanVelocity= velocitySum / (length-1);
		};
		for (int n=0; n < length; n++) {
			BlobAttributes attributes2= attributeArray[n];
			HashMap<ImageChannelName,double[]> colorHistograms= attributes2.getColorHistograms();
			if (colorHistograms != null) {
				meanColorHistograms.addHistograms(colorHistograms);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
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
	//
	///////////////////////////////////////////////////////////////
	//
	public BigInteger getOwner() {
		return owner;
	}
	public int getNumber() {
		return number;
	}
	public long getSegmentBeginning() {
		return segmentBeginning;
	}
	public long getSegmentEnd() {
		return segmentEnd;
	}
	public long getSegmentBreakPoint() {
		return segmentBreakPoint;
	}
	public long getBeginningTimeInMilliseconds() {
		return beginningTimeInMilliseconds;
	}
	public long getEndTimeInMilliseconds() {
		return endTimeInMilliseconds;
	}
	public BlobAttributes[] getAttributeArray() {
		return attributeArray;
	}
	public int getLength() {
		return attributeArray.length;
	}
	public boolean isEmpty() {
		return attributeArray.length <= 0;
	}
	public BlobType getBlobType() {
		for (int k=0; k < attributeArray.length; k++) {
			BlobType type= attributeArray[k].getType();
			if (type != null) {
				return type;
			}
		};
		return null;
	}
	//
	public long getFirstFrameNumber() {
		if (attributeArray.length > 0) {
			BlobAttributes attributes= attributeArray[0];
			return attributes.getFrameNumber();
		} else {
			return -1;
		}
	}
	public long getLastFrameNumber() {
		int length= attributeArray.length;
		if (length > 0) {
			BlobAttributes attributes= attributeArray[length-1];
			return attributes.getFrameNumber();
		} else {
			return -1;
		}
	}
	public long getFirstTimeInMilliseconds() {
		if (attributeArray.length > 0) {
			BlobAttributes attributes= attributeArray[0];
			return attributes.getTimeInMilliseconds();
		} else {
			return -1;
		}
	}
	public long getLastTimeInMilliseconds() {
		int length= attributeArray.length;
		if (length > 0) {
			BlobAttributes attributes= attributeArray[length-1];
			return attributes.getTimeInMilliseconds();
		} else {
			return -1;
		}
	}
	public long getFirstX() {
		if (attributeArray.length > 0) {
			BlobAttributes attributes= attributeArray[0];
			int x1= attributes.getX1();
			int x2= attributes.getX2();
			return StrictMath.round((x1+x2)/2.0);
		} else {
			return -1;
		}
	}
	public long getLastX() {
		int length= attributeArray.length;
		if (length > 0) {
			BlobAttributes attributes= attributeArray[length-1];
			int x1= attributes.getX1();
			int x2= attributes.getX2();
			return StrictMath.round((x1+x2)/2.0);
		} else {
			return -1;
		}
	}
	public long getFirstY() {
		if (attributeArray.length > 0) {
			BlobAttributes attributes= attributeArray[0];
			int y1= attributes.getY1();
			int y2= attributes.getY2();
			return StrictMath.round((y1+y2)/2.0);
		} else {
			return -1;
		}
	}
	public long getLastY() {
		int length= attributeArray.length;
		if (length > 0) {
			BlobAttributes attributes= attributeArray[length-1];
			int y1= attributes.getY1();
			int y2= attributes.getY2();
			return StrictMath.round((y1+y2)/2.0);
		} else {
			return -1;
		}
	}
	public long getFirstCentroidX() {
		if (attributeArray.length > 0) {
			BlobAttributes attributes= attributeArray[0];
			return attributes.getCentroidX();
		} else {
			return -1;
		}
	}
	public long getLastCentroidX() {
		int length= attributeArray.length;
		if (length > 0) {
			BlobAttributes attributes= attributeArray[length-1];
			return attributes.getCentroidX();
		} else {
			return -1;
		}
	}
	public long getFirstCentroidY() {
		if (attributeArray.length > 0) {
			BlobAttributes attributes= attributeArray[0];
			return attributes.getCentroidY();
		} else {
			return -1;
		}
	}
	public long getLastCentroidY() {
		int length= attributeArray.length;
		if (length > 0) {
			BlobAttributes attributes= attributeArray[length-1];
			return attributes.getCentroidY();
		} else {
			return -1;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public double[] getCharacteristicLengthValues() {
		return characteristicLengthValues;
	}
	public double[] getDistanceValues() {
		return distanceValues;
	}
	public double[] getVelocityValues() {
		return velocityValues;
	}
	public double getMeanBlobArea() {
		return meanBlobArea;
	}
	public double getMeanForegroundArea() {
		return meanForegroundArea;
	}
	public double getMeanCharacteristicLength() {
		return meanCharacteristicLength;
	}
	public double getMeanSquaredCharacteristicLength() {
		return meanSquaredCharacteristicLength;
	}
	public double getMeanStandardizedArea() {
		return meanStandardizedArea;
	}
	public double getMeanContourLength() {
		return meanContourLength;
	}
	public double getMeanVelocity() {
		return meanVelocity;
	}
	public long getWindowedR2Cardinality() {
		if (windowedR2 != null) {
			return windowedR2.getCardinality();
		} else {
			return 0;
		}
	}
	public double getWindowedR2Mean() {
		if (windowedR2 != null) {
			return windowedR2.getMean();
		} else {
			return WindowedR2.getNoValue();
		}
	}
	public double getWindowedR2StandardDeviation() {
		if (windowedR2 != null) {
			return windowedR2.getStandardDeviation();
		} else {
			return 0;
		}
	}
	public double getWindowedR2Skewness() {
		if (windowedR2 != null) {
			return windowedR2.getSkewness();
		} else {
			return 0;
		}
	}
	public double getWindowedR2Kurtosis() {
		if (windowedR2 != null) {
			return windowedR2.getKurtosis();
		} else {
			return 0;
		}
	}
	public double[] getWindowedR2ReferentValues() {
		if (windowedR2 != null) {
			return windowedR2.getReferentValues();
		} else {
			return new double[0];
		}
	}
	public BigInteger[] getEntries() {
		return entries;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void computeMeanBlobArea(UniversalAverager averager) {
		int length= attributeArray.length;
		for (int n=0; n < length; n++) {
			BlobAttributes attributes= attributeArray[n];
			int x1= attributes.getX1();
			int x2= attributes.getX2();
			int y1= attributes.getY1();
			int y2= attributes.getY2();
			int width= x2 - x1 + 1;
			int height= y2 - y1 + 1;
			double area= width * height;
			averager.add(area);
		}
	}
	public void computeMeanForegroundArea(UniversalAverager averager) {
		int length= attributeArray.length;
		for (int n=0; n < length; n++) {
			BlobAttributes attributes= attributeArray[n];
			long value= attributes.getForegroundArea();
			averager.add(value);
		}
	}
	public void computeMeanContourLength(UniversalAverager averager) {
		int length= attributeArray.length;
		for (int n=0; n < length; n++) {
			BlobAttributes attributes= attributeArray[n];
			long value= attributes.getContourLength();
			averager.add(value);
		}
	}
	public void computeTotalDistance(UniversalAverager averager) {
		averager.add(distanceValues.length-1,totalDistance);
	}
	public void computeTotalShift(EvaluatingAverager averagerX, EvaluatingAverager averagerY) {
		int length= attributeArray.length;
		for (int n=0; n < length; n++) {
			BlobAttributes attributes= attributeArray[n];
			int x= attributes.getCentroidX();
			int y= attributes.getCentroidY();
			averagerX.add(x);
			averagerY.add(y);
		}
	}
	public void computeMeanVelocity(UniversalAverager averager) {
		averager.add(velocityValues.length-1,velocitySum);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void collectMapOfBlobTerms(HashMap<Long,Term> mapOfBlobTerms) {
		int length= attributeArray.length;
		for (int n=0; n < length; n++) {
			BlobAttributes attributes= attributeArray[n];
			long frameNumber= attributes.getFrameNumber();
			mapOfBlobTerms.put(frameNumber,attributes.toTerm(owner));
		}
	}
	//
	public Term toTerm() {
		if (prologTrackSegment != null) {
			return prologTrackSegment;
		};
		BlobAttributes frontBlobAttributes= attributeArray[0];
		long frameNumber1= frontBlobAttributes.getFrameNumber();
		long time1= frontBlobAttributes.getTimeInMilliseconds();
		int x11= frontBlobAttributes.getX1();
		int x12= frontBlobAttributes.getX2();
		int y11= frontBlobAttributes.getY1();
		int y12= frontBlobAttributes.getY2();
		long x1= StrictMath.round((x11+x12)/2.0);
		long y1= StrictMath.round((y11+y12)/2.0);
		int centroidX1= frontBlobAttributes.getCentroidX();
		int centroidY1= frontBlobAttributes.getCentroidY();
		BlobAttributes endBlobAttributes= attributeArray[attributeArray.length-1];
		long frameNumber2= endBlobAttributes.getFrameNumber();
		long time2= endBlobAttributes.getTimeInMilliseconds();
		int x21= endBlobAttributes.getX1();
		int x22= endBlobAttributes.getX2();
		int y21= endBlobAttributes.getY1();
		int y22= endBlobAttributes.getY2();
		long x2= StrictMath.round((x21+x22)/2.0);
		long y2= StrictMath.round((y21+y22)/2.0);
		int centroidX2= endBlobAttributes.getCentroidX();
		int centroidY2= endBlobAttributes.getCentroidY();
		//
		Term prologFrame1= new PrologInteger(frameNumber1);
		Term prologTime1= new PrologInteger(time1);
		Term prologX1= new PrologInteger(x1);
		Term prologY1= new PrologInteger(y1);
		Term prologCentroidX1= new PrologInteger(centroidX1);
		Term prologCentroidY1= new PrologInteger(centroidY1);
		Term prologFrame2= new PrologInteger(frameNumber2);
		Term prologTime2= new PrologInteger(time2);
		Term prologX2= new PrologInteger(x2);
		Term prologY2= new PrologInteger(y2);
		Term prologCentroidX2= new PrologInteger(centroidX2);
		Term prologCentroidY2= new PrologInteger(centroidY2);
		Term list3= trackOfBlobToTerm();
		Term prologMeanBlobArea= new PrologReal(meanBlobArea);
		Term prologMeanForegroundArea= new PrologReal(meanForegroundArea);
		Term prologMeanCharacteristicLength= new PrologReal(meanCharacteristicLength);
		Term prologMeanSquaredCharacteristicLength= new PrologReal(meanSquaredCharacteristicLength);
		Term prologMeanStandardizedArea= new PrologReal(meanStandardizedArea);
		Term prologMeanContourLength= new PrologReal(meanContourLength);
		Term prologWR2Mean= new PrologReal(getWindowedR2Mean());
		Term prologWR2StandardDeviation= new PrologReal(getWindowedR2StandardDeviation());
		Term prologWR2Skewness= new PrologReal(getWindowedR2Skewness());
		Term prologWR2Kurtosis= new PrologReal(getWindowedR2Kurtosis());
		Term prologWR2Cardinality= new PrologInteger(getWindowedR2Cardinality());
		Term prologMeanVelocity= new PrologReal(meanVelocity);
		//
		prologTrackSegment= PrologEmptySet.instance;
		if (!meanColorHistograms.isEmpty()) {
			Term prologColorHistograms= meanColorHistograms.toTerm();
			prologTrackSegment=
				new PrologSet(
				- SymbolCodes.symbolCode_E_histograms,
				prologColorHistograms,
				prologTrackSegment);
		};
		//
		prologTrackSegment=
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
			- SymbolCodes.symbolCode_E_coordinates,
			list3,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_blob_area,
			prologMeanBlobArea,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_foreground_area,
			prologMeanForegroundArea,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_characteristic_length,
			prologMeanCharacteristicLength,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_squared_characteristic_length,
			prologMeanSquaredCharacteristicLength,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_standardized_area,
			prologMeanStandardizedArea,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_contour_length,
			prologMeanContourLength,
			new PrologSet(
			- SymbolCodes.symbolCode_E_wr2_mean,
			prologWR2Mean,
			new PrologSet(
			- SymbolCodes.symbolCode_E_wr2_standard_deviation,
			prologWR2StandardDeviation,
			new PrologSet(
			- SymbolCodes.symbolCode_E_wr2_skewness,
			prologWR2Skewness,
			new PrologSet(
			- SymbolCodes.symbolCode_E_wr2_kurtosis,
			prologWR2Kurtosis,
			new PrologSet(
			- SymbolCodes.symbolCode_E_wr2_cardinality,
			prologWR2Cardinality,
			new PrologSet(
			- SymbolCodes.symbolCode_E_mean_velocity,
			prologMeanVelocity,
			prologTrackSegment)))))))))))))))))))))))));
		return prologTrackSegment;
	}
	//
	protected Term trackOfBlobToTerm() {
		if (prologTrackOfBlob != null) {
			return prologTrackOfBlob;
		};
		double[] windowedR2ReferentValues= getWindowedR2ReferentValues();
		prologTrackOfBlob= PrologEmptyList.instance;
		for (int n=attributeArray.length-1; n >= 0; n--) {
			BlobAttributes blobAttributes= attributeArray[n];
			int centroidX= blobAttributes.getCentroidX();
			int centroidY= blobAttributes.getCentroidY();
			long foregroundArea= blobAttributes.getForegroundArea();
			long contourLength= blobAttributes.getContourLength();
			double currentCharacteristicLength= characteristicLengthValues[n];
			double currentR2= windowedR2ReferentValues[n];
			double currentVelocity= velocityValues[n];
			Term prologFrameN= new PrologInteger(blobAttributes.getFrameNumber());
			Term prologTimeN= new PrologInteger(blobAttributes.getTimeInMilliseconds());
			int x1= blobAttributes.getX1();
			int x2= blobAttributes.getX2();
			int y1= blobAttributes.getY1();
			int y2= blobAttributes.getY2();
			Term prologXn= new PrologInteger(StrictMath.round((x1+x2)/2.0));
			Term prologYn= new PrologInteger(StrictMath.round((y1+y2)/2.0));
			Term prologWidthN= new PrologInteger(x2-x1);
			Term prologHeightN= new PrologInteger(y2-y1);
			Term prologCentroidXn= new PrologInteger(centroidX);
			Term prologCentroidYn= new PrologInteger(centroidY);
			Term prologForegroundAreaN= new PrologInteger(foregroundArea);
			Term prologCharacteristicLengthN= new PrologReal(currentCharacteristicLength);
			Term prologContourLengthN= new PrologInteger(contourLength);
			Term prologR2N= new PrologReal(currentR2);
			Term prologVelocityN= new PrologReal(currentVelocity);
			Term prologRectangle=
				new PrologSet(
				- SymbolCodes.symbolCode_E_frame,
				prologFrameN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_time,
				prologTimeN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_x,
				prologXn,
				new PrologSet(
				- SymbolCodes.symbolCode_E_y,
				prologYn,
				new PrologSet(
				- SymbolCodes.symbolCode_E_width,
				prologWidthN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_height,
				prologHeightN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_centroid_x,
				prologCentroidXn,
				new PrologSet(
				- SymbolCodes.symbolCode_E_centroid_y,
				prologCentroidYn,
				new PrologSet(
				- SymbolCodes.symbolCode_E_foreground_area,
				prologForegroundAreaN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_characteristic_length,
				prologCharacteristicLengthN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_contour_length,
				prologContourLengthN,
				new PrologSet(
				- SymbolCodes.symbolCode_E_r2,
				prologR2N,
				new PrologSet(
				- SymbolCodes.symbolCode_E_velocity,
				prologVelocityN,
				PrologEmptySet.instance)))))))))))));
			prologTrackOfBlob= new PrologList(prologRectangle,prologTrackOfBlob);
		};
		return prologTrackOfBlob;
	}
	//      	
	///////////////////////////////////////////////////////////////
	//
	public void dump(PrintStream stream) {
		stream.printf("owner:                      %s\n",owner);
		stream.printf("number:                     %s\n",number);
		stream.printf("segmentBeginning:           %s\n",segmentBeginning);
		stream.printf("segmentEnd:                 %s\n",segmentEnd);
		stream.printf("segment:End-Beginning+1:    %s\n",segmentEnd-segmentBeginning+1);
		stream.printf("segmentBreakPoint:          %s\n",segmentBreakPoint);
		stream.printf("rectangles:                 %s items\n",attributeArray.length);
		stream.printf("totalDistance:              %s\n",totalDistance);
		stream.printf("meanVelocity:               %s\n",meanVelocity);
		stream.printf("entries:           ");
		for (int n=0; n < entries.length; n++) {
			stream.printf(" %s",entries[n]);
		};
		stream.printf("\n");
	}
	public void createMatlab(PrintStream stream, BigInteger identifier, int index) {
		stream.printf("%% owner:                      %s\n",owner);
		stream.printf("%% number:                     %s\n",number);
		stream.printf("%% segmentBeginning:           %s\n",segmentBeginning);
		stream.printf("%% segmentEnd:                 %s\n",segmentEnd);
		stream.printf("%% segment:End-Beginning+1:    %s\n",segmentEnd-segmentBeginning+1);
		stream.printf("%% segmentBreakPoint:          %s\n",segmentBreakPoint);
		stream.printf("%% rectangles:                 %s items\n",attributeArray.length);
		stream.printf("%% totalDistance:              %s\n",totalDistance);
		stream.printf("%% meanVelocity:               %s\n",meanVelocity);
		stream.printf("R_%s_%s_%s= [...\n",identifier,segmentBeginning,segmentEnd);
		for (int n=0; n < attributeArray.length; n++) {
			BlobAttributes attributes= attributeArray[n];
			int x1= attributes.getX1();
			int x2= attributes.getX2();
			int y1= attributes.getY1();
			int y2= attributes.getY2();
			stream.printf(" [ %s %s %s %s ]",x1,x2,y1,y2);
			if (n < attributeArray.length - 1) {
				stream.printf(";");
			};
			stream.printf("...\n");
		};
		stream.printf("];\n");
		stream.printf("T_%s_%s_%s= [...\n",identifier,segmentBeginning,segmentEnd);
		for (int n=0; n < attributeArray.length; n++) {
			BlobAttributes attributes= attributeArray[n];
			long fN= attributes.getFrameNumber();
			long t= attributes.getTimeInMilliseconds();
			stream.printf(" [ %s %s ]",fN,t);
			if (n < attributeArray.length - 1) {
				stream.printf(";");
			};
			stream.printf("...\n");
		};
		stream.printf("];\n");
		stream.printf("R2_mean_%s_%s_%s= %s;\n",identifier,segmentBeginning,segmentEnd,getWindowedR2Mean());
		stream.printf("R2_standard_deviation_%s_%s_%s= %s;\n",identifier,segmentBeginning,segmentEnd,getWindowedR2StandardDeviation());
		stream.printf("R2_skewness_%s_%s_%s= %s;\n",identifier,segmentBeginning,segmentEnd,getWindowedR2Skewness());
		stream.printf("R2_Kurtosis_%s_%s_%s= %s;\n",identifier,segmentBeginning,segmentEnd,getWindowedR2Kurtosis());
		stream.printf("V_%s_%s_%s= [",identifier,segmentBeginning,segmentEnd);
		for (int n=0; n < velocityValues.length; n++) {
			double vXY= velocityValues[n];
			stream.printf(" %s",vXY);
		};
		stream.printf(" ];\n");
		stream.printf("V_mean_%s_%s_%s= %s;\n",identifier,segmentBeginning,segmentEnd,meanVelocity);
		stream.printf("%% entries:      ");
		for (int n=0; n < entries.length; n++) {
			stream.printf(" %s",entries[n]);
		};
		stream.printf("\n");
		stream.printf("T%s{%s}.owner= %s;\n",identifier,index+1,owner);
		stream.printf("T%s{%s}.number= %s;\n",identifier,index+1,number);
		stream.printf("T%s{%s}.segmentBeginning= %s;\n",identifier,index+1,segmentBeginning);
		stream.printf("T%s{%s}.segmentEnd= %s;\n",identifier,index+1,segmentEnd);
		stream.printf("T%s{%s}.segmentBreakPoint= %s;\n",identifier,index+1,segmentBreakPoint);
		stream.printf("T%s{%s}.beginningTimeInMilliseconds= %s;\n",identifier,index+1,beginningTimeInMilliseconds);
		stream.printf("T%s{%s}.endTimeInMilliseconds= %s;\n",identifier,index+1,endTimeInMilliseconds);
		stream.printf("T%s{%s}.rectangles= R_%s_%s_%s;\n",identifier,index+1,identifier,segmentBeginning,segmentEnd);
		stream.printf("T%s{%s}.velocity= V_%s_%s_%s;\n",identifier,index+1,identifier,segmentBeginning,segmentEnd);
		stream.printf("T%s{%s}.mean= M_%s_%s_%s;\n",identifier,index+1,identifier,segmentBeginning,segmentEnd);
		stream.printf("T%s{%s}.dispersion= D_%s_%s_%s;\n",identifier,index+1,identifier,segmentBeginning,segmentEnd);
		stream.printf("T%s{%s}.N= N_%s_%s_%s;\n",identifier,index+1,identifier,segmentBeginning,segmentEnd);
	}
}
