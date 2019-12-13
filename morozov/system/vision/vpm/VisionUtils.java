// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import java.awt.image.SampleModel;
import java.awt.image.Raster;
import java.awt.Graphics2D;
import java.util.Arrays;

public class VisionUtils {
	//
	public static final int maximalColor= 255;
	//
	///////////////////////////////////////////////////////////////
	//
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
	//
	///////////////////////////////////////////////////////////////
	//
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
	//
	///////////////////////////////////////////////////////////////
	//
	public static double medianWnd(double[] vector, int from, int to) {
		int initialLength= vector.length;
		int windowLength= to - from + 1;
		if (initialLength <= 0 || windowLength <= 0) {
			return 0;
		} else {
			if (initialLength >= windowLength) {
				if (from < 0) {
					from= 0;
					to= windowLength - 1;
				} else if (to > initialLength - 1) {
					to= initialLength - 1;
					from= initialLength - windowLength;
				}
			} else {
				from= 0;
				to= vector.length-1;
			};
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
	//
	public static double medianAbs(double[] vector, int from, int to) {
		int initialLength= vector.length;
		int windowLength= to - from + 1;
		if (initialLength <= 0 || windowLength <= 0) {
			return 0;
		} else {
			if (initialLength >= windowLength) {
				if (from < 0) {
					from= 0;
					to= windowLength - 1;
				} else if (to > initialLength - 1) {
					to= initialLength - 1;
					from= initialLength - windowLength;
				}
			} else {
				from= 0;
				to= vector.length-1;
			};
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
	// Order-statistic filtering:
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
	//
	public static void rankFilter2D(boolean[] foregroundMask, int width, int height, int userDefinedThreshold, boolean contourForeground) {
		int counterThreshold= 8 - userDefinedThreshold;
		boolean[] previousMask= Arrays.copyOf(foregroundMask,foregroundMask.length);
		for (int w=0; w < width; w++) {
			int h1= 0;
			int point1= width * h1 + w;
			foregroundMask[point1]= false;
			int h2= height-1;
			int point2= width * h2 + w;
			foregroundMask[point2]= false;
		};
		for (int h=0; h < height; h++) {
			int w1= 0;
			int point1= width * h + w1;
			foregroundMask[point1]= false;
			int w2= width-1;
			int point2= width * h + w2;
			foregroundMask[point2]= false;
		};
		for (int w=1; w < width-1; w++) {
			for (int h=1; h < height-1; h++) {
				int point1= width * h + w;
				boolean value= previousMask[point1];
				if (!value) {
					continue;
				} else {
					int spaceCounter= 0;
					for (int x=-1; x <= 1; x++) {
						for (int y=-1; y <= 1; y++) {
							int point2= width * (h+y) + (w+x);
							if (!previousMask[point2]) {
								spaceCounter++;
							}
						}
					};
					if ((spaceCounter > counterThreshold) || (contourForeground && spaceCounter <= 0)) {
						foregroundMask[point1]= false;
					}
				}
			}
		}
	}
	//
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
	//
	public static boolean[] contourForeground(boolean[] foregroundMask, int width, int height) {
		boolean[] result= Arrays.copyOf(foregroundMask,foregroundMask.length);
		for (int w=1; w < width-1; w++) {
			for (int h=1; h < height-1; h++) {
				int point1= width * h + w;
				if (foregroundMask[point1]) {
					int counter= 0;
					for (int x=-1; x <= 1; x++) {
						for (int y=-1; y <= 1; y++) {
							int point2= width * (h+y) + (w+x);
							if (!foregroundMask[point2]) {
								counter++;
							}
						}
					};
					if (counter <= 0) {
						result[point1]= false;
					}
				}
			}
		};
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static double metrics(double x, double threshold, double halfwidth) {
		if (x >= threshold + halfwidth) {
			return 1;
		} else if (x <= threshold - halfwidth) {
			return 0;
		} else {
			return (x - threshold + halfwidth) * (1 / (2*halfwidth));
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int[] convertImageToGrayscale(
			int operationalImageWidth,
			int operationalImageHeight,
			java.awt.image.BufferedImage preprocessedImage,
			java.awt.image.BufferedImage recentImage) {
		int operationalVectorLength= operationalImageWidth * operationalImageHeight;
		int[] matrixGrayscale= new int[operationalVectorLength];
		java.awt.image.BufferedImage sourceImage= preprocessedImage;
		if (sourceImage==null) {
			sourceImage= recentImage;
		};
		SampleModel sampleModel= sourceImage.getSampleModel();
		int recentNumberOfBands= sampleModel.getNumBands();
		if (recentNumberOfBands != 1) {
			java.awt.image.BufferedImage temporaryImage= new java.awt.image.BufferedImage(operationalImageWidth,operationalImageHeight,java.awt.image.BufferedImage.TYPE_BYTE_GRAY);
			Graphics2D g2= temporaryImage.createGraphics();
			try {
				g2.drawImage(sourceImage,0,0,null);
				sourceImage= temporaryImage;
			} finally {
				g2.dispose();
			}
		};
		Raster raster= sourceImage.getData();
		raster.getSamples(0,0,operationalImageWidth,operationalImageHeight,0,matrixGrayscale);
		return matrixGrayscale;
	}
	//
	public static int[][] convertImageToRGB(
			int operationalImageWidth,
			int operationalImageHeight,
			java.awt.image.BufferedImage preprocessedImage,
			java.awt.image.BufferedImage recentImage) {
		java.awt.image.BufferedImage sourceImage= preprocessedImage;
		if (sourceImage==null) {
			sourceImage= recentImage;
		};
		Raster raster= sourceImage.getData();
		int operationalVectorLength= operationalImageWidth * operationalImageHeight;
		int[][] matrixRGB= new int[3][operationalVectorLength];
		for (int k=0; k < 3; k++) {
			raster.getSamples(0,0,operationalImageWidth,operationalImageHeight,k,matrixRGB[k]);
		};
		return matrixRGB;
	}
	//
	public static int[] convertRGBtoGrayscale(int[][] pixelsRGB) {
		int numberOfBands= pixelsRGB.length;
		if (numberOfBands < 3) {
			return pixelsRGB[0];
		};
		int vectorLength= pixelsRGB[0].length;
		int[] pixelsGray= new int[vectorLength];
		for (int k=0; k < vectorLength; k++) {
			int r= pixelsRGB[0][k];
			int g= pixelsRGB[1][k];
			int b= pixelsRGB[2][k];
			int gray= (r + g + b) / 3;
			pixelsGray[k]= gray;
		};
		return pixelsGray;
	}
	//
	public static int[][] convertRGBtoHSB(int[][] pixelsRGB) {
		int numberOfBands= pixelsRGB.length;
		if (numberOfBands < 3) {
			return pixelsRGB;
		};
		int vectorLength= pixelsRGB[0].length;
		int[][] pixelsHSB= new int[numberOfBands][vectorLength];
		if (numberOfBands > 3) {
			for (int b=0; b < numberOfBands; b++) {
				System.arraycopy(pixelsRGB[b],0,pixelsHSB[b],0,vectorLength);
			}
		};
		for (int k=0; k < vectorLength; k++) {
			int r= pixelsRGB[0][k];
			int g= pixelsRGB[1][k];
			int b= pixelsRGB[2][k];
			int hue;
			int saturation;
			int brightness;
			int cmax= (r > g) ? r : g;
			if (b > cmax) {
				cmax= b;
			};
			int cmin= (r < g) ? r : g;
			if (b < cmin) {
				cmin= b;
			};
			brightness= cmax;
			if (cmax != 0) {
				saturation= (cmax - cmin) * maximalColor / cmax;
			} else {
				saturation= 0;
			};
			if (saturation == 0) {
				hue= 0;
			} else {
				int redc= (cmax - r) * maximalColor / (cmax - cmin);
				int greenc= (cmax - g) * maximalColor / (cmax - cmin);
				int bluec= (cmax - b) * maximalColor / (cmax - cmin);
				if (r == cmax) {
					hue= bluec - greenc;
				} else if (g == cmax) {
					hue= maximalColor * 2 + redc - bluec;
				} else {
					hue= maximalColor * 4 + greenc - redc;
				};
				hue= hue / 6;
				if (hue < 0) {
					hue= hue + maximalColor;
				}
			};
			pixelsHSB[0][k]= hue;
			pixelsHSB[1][k]= saturation;
			pixelsHSB[2][k]= brightness;
		};
		return pixelsHSB;
	}
	//
	public static int[] hsb2rgb(int hue256, int saturation256, int brightness256) {
		float hue= (float)hue256 / maximalColor;
		float saturation= (float)saturation256 / maximalColor;
		float brightness= (float)brightness256 / maximalColor;
		hue= 6 * hue;
		int k= (int)hue;
		float p= hue - k;
		float t= 1 - saturation;
		float n= 1 - saturation * p;
		p= 1 - (saturation * (1 - p));
		float r= 0;
		float g= 0;
		float b= 0;
		switch (k) {
		case 0:
		case 6:
			r= 1;
			g= p;
			b= t;
			break;
		case 1:
			r= r + n;
			g= g + 1;
			b= b + t;
			break;
		case 2:
			r= r + t;
			g= g + 1;
			b= b + p;
			break;
		case 3:
			r= r + t;
			g= g + n;
			b= b + 1;
			break;
		case 4:
			r= r + p;
			g= g + t;
			b= b + 1;
			break;
		case 5:
			r= r + 1;
			g= g + t;
			b= b + n;
			break;
		};
		float max= r;
		if (max < g) {
			max= g;
		};
		if (max < b) {
			max= b;
		};
		float f= brightness / max;
		float rout= f * r;
		g= f * g;
		b= f * b;
		int[] rgb= new int[3];
		rgb[0]= (int)(rout*maximalColor);
		rgb[1]= (int)(g*maximalColor);
		rgb[2]= (int)(b*maximalColor);
		return rgb;
	}
}
