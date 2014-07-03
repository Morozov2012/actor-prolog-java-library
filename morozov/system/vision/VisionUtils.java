// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import java.awt.image.WritableRaster;
import java.util.Arrays;

class VisionUtils {
	public static float[] gaussianMatrix(int radius) {
		float[] window= gaussianWindow(radius);
		int length= window.length;
		int halfLength= length / 2 + 1;
		int squaredLength= length * length;
		float[] matrix= new float[squaredLength];
		for (int n=0; n < halfLength; n++) {
			for (int k=n; k < length-n; k++) {
				int point= n*length + k;
				matrix[point]= window[n];
			};
			for (int k=n; k < length-n; k++) {
				int point= k*length + (length-1-n);
				matrix[point]= window[n];
			};
			for (int k=n; k < length-n; k++) {
				int point= (length-1-n)*length + k;
				matrix[point]= window[n];
			};
			for (int k=n; k < length-n; k++) {
				int point= k*length + n;
				matrix[point]= window[n];
			}
		};
		float sum= 0;
		for (int n=0; n < squaredLength; n++) {
			sum= sum + matrix[n];
		};
		for (int n=0; n < squaredLength; n++) {
			matrix[n]= matrix[n] / sum;
		};
		return matrix;
	}
	public static float[] gaussianWindow(int radius) {
		if (radius <= 0) {
			return new float[]{1};
		};
		int nPoints= radius * 2 + 1;
		float[] y= new float[nPoints];
		y[radius]= 1.0f;
		double step= 1.0f / radius;
		double currentX= step;
		for (int n=1; n <= radius; n++) {
			int index1= radius + n;
			int index2= radius - n;
			float value= (float)StrictMath.exp(-(currentX*currentX));
			y[index1]= value;
			y[index2]= value;
			currentX= currentX + step;
		};
		return y;
	}
	public static double[] project(int x, int y, double[][] matrix) {
		if (matrix.length==0) {
			return new double[]{x,y};
		} else {
			// M= [	[ A, D, G ];
			//	[ B, E, H ];
			//	[ C, F, I ]	]
			double a= matrix[0][0];
			double b= matrix[1][0];
			double c= matrix[2][0];
			double d= matrix[0][1];
			double e= matrix[1][1];
			double f= matrix[2][1];
			double g= matrix[0][2];
			double h= matrix[1][2];
			double i= matrix[2][2];
			double u= (a*x + b*y + c) / (g*x + h*y + i);
			double v= (d*x + e*y + f) / (g*x + h*y + i);
			return new double[]{u,v};
		}
	}
	public static double medianWnd(double[] vector, int from, int to) {
		int initialLength= vector.length;
		int windowLength= to - from + 1;
		if (initialLength < windowLength) {
			return 0;
		} else if (initialLength <= 0 || windowLength <= 0) {
			return 0;
		} else {
			if (from < 0) {
				from= 0;
				to= windowLength - 1;
			} else if (to > initialLength - 1) {
				to= initialLength - 1;
				from= initialLength - windowLength;
			};
			// Fixed:2014-05-13: double[] array= Arrays.copyOfRange(vector,from,to);
			double[] array= Arrays.copyOfRange(vector,from,to+1);
			int actualLength= array.length;
			Arrays.sort(array);
			double result;
			if (actualLength % 2 == 0) {
				int index= actualLength / 2;
				result= (array[index-1] + array[index]) / 2;
			} else {
				int index= actualLength / 2;
				result= array[index];
			};
			return result;
		}
	}
	public static double medianAbs(double[] vector, int from, int to) {
		int initialLength= vector.length;
		int windowLength= to - from + 1;
		if (initialLength < windowLength) {
			return 0;
		} else if (initialLength <= 0 || windowLength <= 0) {
			return 0;
		} else {
			if (from < 0) {
				from= 0;
				to= windowLength - 1;
			} else if (to > initialLength - 1) {
				to= initialLength - 1;
				from= initialLength - windowLength;
			};
			// Fixed:2014-05-13: double[] array= Arrays.copyOfRange(vector,from,to);
			double[] array= Arrays.copyOfRange(vector,from,to+1);
			int actualLength= array.length;
			for (int n=0; n < actualLength; n++) {
				if (array[n] < 0) {
					array[n]= - array[n];
				}
			};
			Arrays.sort(array);
			double result;
			if (actualLength % 2 == 0) {
				int index= actualLength / 2;
				result= (array[index-1] + array[index]) / 2;
			} else {
				int index= actualLength / 2;
				result= array[index];
			};
			return result;
		}
	}
	// Order-statistic filtering
	public static int[] rankFilter2D(int noDifferenceMarker, int[] pixels, int width, int height, int userDefinedThreshold, boolean contourForeground) {
		int counterThreshold= 8 - userDefinedThreshold;
		int[] result= Arrays.copyOf(pixels,pixels.length);
		for (int w=0; w < width; w++) {
			int h1= 0;
			int point1= width * h1 + w;
			result[point1]= noDifferenceMarker;
			int h2= height-1;
			int point2= width * h2 + w;
			result[point2]= noDifferenceMarker;
		};
		for (int h=0; h < height; h++) {
			int w1= 0;
			int point1= width * h + w1;
			result[point1]= noDifferenceMarker;
			int w2= width-1;
			int point2= width * h + w2;
			result[point2]= noDifferenceMarker;
		};
		for (int w=1; w < width-1; w++) {
			for (int h=1; h < height-1; h++) {
				int point1= width * h + w;
				int value= pixels[point1];
				if (value == noDifferenceMarker) {
					continue;
				} else {
					int spaceCounter= 0;
					for (int x=-1; x <= 1; x++) {
						for (int y=-1; y <= 1; y++) {
							int point2= width * (h+y) + (w+x);
							if (pixels[point2] == noDifferenceMarker) {
								spaceCounter++;
							}
						}
					};
					if ((spaceCounter > counterThreshold) || (contourForeground && spaceCounter <= 0)) {
						result[point1]= noDifferenceMarker;
					}
				}
			}
		};
		return result;
	}
	public static int[] contourForeground(int noDifferenceMarker, int[] pixels, int width, int height) {
		int[] result= Arrays.copyOf(pixels,pixels.length);
		for (int w=1; w < width-1; w++) {
			for (int h=1; h < height-1; h++) {
				int point1= width * h + w;
				int value= pixels[point1];
				if (value == noDifferenceMarker) {
					continue;
				} else {
					int counter= 0;
					for (int x=-1; x <= 1; x++) {
						for (int y=-1; y <= 1; y++) {
							int point2= width * (h+y) + (w+x);
							if (pixels[point2] == noDifferenceMarker) {
								counter++;
							}
						}
					};
					if (counter <= 0) {
						result[point1]= noDifferenceMarker;
					}
				}
			}
		};
		return result;
	}
	protected static BlobAttributes computeHistogram(java.awt.image.BufferedImage image, int[] deltaPixels, int[] contourPixels, long time, int x1, int x2, int y1, int y2, int imageWidth, int noDifferenceMarker) {
		int nBins= 256;
		WritableRaster raster= image.getRaster();
		int bandNumber= raster.getNumBands();
		int maximalWidth= raster.getWidth();
		int maximalHeight= raster.getHeight();
		if (x1 < 0) {
			x1= 0;
		} else if (x1 >= maximalWidth) {
			x1= maximalWidth - 1;
		};
		if (x2 < 0) {
			x2= 0;
		} else if (x2 >= maximalWidth) {
			x2= maximalWidth - 1;
		};
		if (y1 < 0) {
			y1= 0;
		} else if (y1 >= maximalHeight) {
			y1= maximalHeight - 1;
		};
		if (y2 < 0) {
			y2= 0;
		} else if (y2 >= maximalHeight) {
			y2= maximalHeight - 1;
		};
		int width= x2 - x1 + 1;
		int height= y2 - y1 + 1;
		int[] pixels= new int[width*height];
		double[][] frequencyCounts= new double[bandNumber][nBins+3];
		int deltaCounter= 0;
		int contourCounter= 0;
		for (int k=0; k < bandNumber; k++) {
			pixels= raster.getSamples(x1,y1,width,height,k,pixels);
			deltaCounter= 0;
			contourCounter= 0;
			for (int xx=x1; xx <= x2; xx++) {
				for (int yy=y1; yy <= y2; yy++) {
					int index1= imageWidth * yy + xx;
					if (deltaPixels[index1] != noDifferenceMarker) {
						deltaCounter++;
						int index2= width * (yy-y1) + (xx-x1);
						int pixel= pixels[index2];
						frequencyCounts[k][pixel]++;
					};
					if (contourPixels[index1] != noDifferenceMarker) {
						contourCounter++;
					}
				}
			};
			frequencyCounts[k][nBins]= deltaCounter;
			frequencyCounts[k][nBins+1]= pixels.length;
			if (deltaCounter > 0) {
				frequencyCounts[k][nBins+2]= pixels.length / deltaCounter;
			} else {
				frequencyCounts[k][nBins+2]= -1;
			}
		};
		return new BlobAttributes(time,x1,x2,y1,y2,frequencyCounts,deltaCounter,contourCounter);
	}
	public static WindowedR2 fastWindowedR2(long[] vectorT, long[] vectorF, int windowHalfwidth) {
		int realLength= vectorT.length;
		if (realLength < 2) {
			return new WindowedR2(realLength);
		};
		int fullLength= (int)(vectorT[realLength-1] - vectorT[0] + 1);
		double[] x1= new double[fullLength];
		double[] y1= new double[fullLength];
		double[] xy= new double[fullLength];
		double[] x2= new double[fullLength];
		double[] y2= new double[fullLength];
		// double[] allWindowedR2Values= new double[fullLength];
		boolean[] isSelectedWindowedR2Value= new boolean[fullLength];
		double[] selectedWindowedR2Values= new double[realLength];
		x1[0]= vectorT[0];
		y1[0]= vectorF[0];
		xy[0]= vectorT[0] * vectorF[0];
		x2[0]= vectorT[0] * vectorT[0];
		y2[0]= vectorF[0] * vectorF[0];
		// allWindowedR2Values[0]= -1.0;
		isSelectedWindowedR2Value[0]= true;
		selectedWindowedR2Values[0]= -1.0;
		long time1= (long)vectorT[0];
		double value1= vectorF[0];
		int counter1= 0;
                for (int n=1; n < realLength; n++) {
			long time2= (long)vectorT[n];
			double value2= vectorF[n];
			long deltaTime= time2 - time1;
			if (deltaTime <= 1) {
				counter1= counter1 + 1;
				x1[counter1]= time2;
				y1[counter1]= value2;
				xy[counter1]= time2 * value2;
				x2[counter1]= time2 * time2;
				y2[counter1]= value2 * value2;
				// allWindowedR2Values[counter1]= -1.0;
			} else {
				double k= (value2-value1)/(time2-time1);
				for (int t=1; t <= deltaTime; t++) {
					long time3= time1 + t;
					double value3= k*t + value1;
					counter1= counter1 + 1;
					x1[counter1]= time3;
					y1[counter1]= value3;
					xy[counter1]= time3 * value3;
					x2[counter1]= time3 * time3;
					y2[counter1]= value3 * value3;
					// allWindowedR2Values[counter1]= -1.0;
				}
			};
			isSelectedWindowedR2Value[counter1]= true;
			selectedWindowedR2Values[n]= -1.0;
			time1= time2;
			value1= value2;
		};
		double windowedR2Sum1= 0.0;
		double windowedR2Sum2= 0.0;
		double windowedR2Sum3= 0.0;
		double windowedR2Sum4= 0.0;
		int counter2= 0;
		for (int center=1; center < windowHalfwidth; center++) {
			if (center > fullLength-1) {
				break;
			};
			if (isSelectedWindowedR2Value[center]) {
				counter2= counter2 + 1;
			}
		};
		int counter3= 0;
		for (int center=windowHalfwidth; center < fullLength - windowHalfwidth; center++) {
			int from= center - windowHalfwidth;
			int to= center + windowHalfwidth;
			double sumX= 0.0;	// Сумма X-ов.
			double sumY= 0.0;	// Сумма Y-ов.
			double sumXY= 0.0;	// Нескорректированная сумма парных произведений.
			double sumX2= 0.0;	// Нескорректированная сумма квадратов X-в.
			double sumY2= 0.0;	// Нескорректированная сумма квадратов Y-в.
			int nPoints= to - from + 1;	// Количество точек.
			for (int n=from; n <= to; n++) {
				sumX= sumX + x1[n];
				sumY= sumY + y1[n];
				sumXY= sumXY + xy[n];
				sumX2= sumX2 + x2[n];
				sumY2= sumY2 + y2[n];
			};
			double xyC= sumX*sumY / nPoints;	// Коррекция на среднее.
			double cXY= sumXY - xyC;		// Скорректированная сумма произведений X и Y.
			double x2C= sumX*sumX / nPoints;	// Коррекция на среднее значение X-в.
			double cSX= sumX2 - x2C;		// Скорректированная сумма квадратов X-в.
			double a1= cXY / cSX;
			double b1= (sumY - a1*sumX) / nPoints;
			// Полная (скорректированная) сумма квадратов:
			double sumYY2= sumY2 - sumY*sumY / nPoints;
			// Сумма квадратов, обусловленная регерессией:
			double ss= cXY*cXY / cSX;
			double r2= ss / sumYY2;
			// allWindowedR2Values[center]= r2;
			windowedR2Sum1= windowedR2Sum1 + r2;
			double XYk= r2 * r2;
			windowedR2Sum2= windowedR2Sum2 + XYk;
			XYk= XYk * r2;
			windowedR2Sum3= windowedR2Sum3 + XYk;
			XYk= XYk * r2;
			windowedR2Sum4= windowedR2Sum4 + XYk;
			if (isSelectedWindowedR2Value[center]) {
				counter2= counter2 + 1;
				counter3= counter3 + 1;
				selectedWindowedR2Values[counter2]= r2;
			}
		};
		int windowedR2Cardinality= counter3;
		// int from= windowHalfwidth;
		// int to= fullLength-windowHalfwidth-1;
		// int nPoints= to - from + 1;	// Количество точек.
		int nPoints= fullLength - 2*windowHalfwidth;	// Количество точек.
		if (nPoints < 0) {
			nPoints= 0;
		};
		double moment1= windowedR2Sum1 / nPoints;
		double moment2= windowedR2Sum2 / nPoints;
		double moment3= windowedR2Sum3 / nPoints;
		double moment4= windowedR2Sum4 / nPoints;
		double squaredMoment1= moment1 * moment1;
		double centralMoment2= moment2 - squaredMoment1;
		double centralMoment3= moment3 - 3*moment2*moment1 + 2*moment1*squaredMoment1;
		double centralMoment4= moment4 - 4*moment3*moment1 + 6*moment2*squaredMoment1 - 3*squaredMoment1*squaredMoment1;
		double windowedR2Mean= moment1;
		double sigma= StrictMath.sqrt(centralMoment2);
		double windowedR2StandardDeviation= sigma;
		double windowedR2Skewness= centralMoment3 / (centralMoment2*sigma);
		double windowedR2Kurtosis= centralMoment4 / (centralMoment2*centralMoment2);
		if (nPoints > 1) {
			double windowedR2BiasCorrectedVariance= centralMoment2 * nPoints/(nPoints-1.0);
			windowedR2StandardDeviation= StrictMath.sqrt(windowedR2BiasCorrectedVariance);
			if (nPoints >= 3) {
				windowedR2Skewness= windowedR2Skewness * StrictMath.sqrt((nPoints-1.0)/nPoints) * nPoints/(nPoints-2.0);
				if (nPoints >= 4) {
					windowedR2Kurtosis= ((nPoints+1.0)*windowedR2Kurtosis - 3.0*(nPoints-1.0)) * (nPoints-1.0)/((nPoints-2.0)*(nPoints-3.0)) + 3.0;
				}
			}
		} else {
			double windowedR2Variance= centralMoment2;
			windowedR2StandardDeviation= StrictMath.sqrt(windowedR2Variance);
		};
		windowedR2Kurtosis= windowedR2Kurtosis - 3.0;
		return new WindowedR2(
				windowedR2Cardinality,
				windowedR2Mean,
				windowedR2StandardDeviation,
				windowedR2Skewness,
				windowedR2Kurtosis,
				selectedWindowedR2Values);
	}
	public static double metrics(double x, double threshold, double halfwidth) {
		if (x >= threshold + halfwidth) {
			return 1;
		} else if (x <= threshold - halfwidth) {
			return 0;
		} else {
			return (x - threshold + halfwidth) * (1 / (2*halfwidth));
		}
	}
}
