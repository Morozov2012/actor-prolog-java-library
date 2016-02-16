// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.system.vision;

import java.awt.image.WritableRaster;

class BlobAttributes {
	//
	public long time;
	public int x1;
	public int x2;
	public int y1;
	public int y2;
	public double[][] spectrum;
	public int foregroundArea;
	public int contourLength;
	public BlobAttributes(long t, int x11, int x12, int y11, int y12, double[][] histograms, int deltaCounter, int contourCounter) {
		time= t;
		x1= x11;
		x2= x12;
		y1= y11;
		y2= y12;
		spectrum= histograms;
		foregroundArea= deltaCounter;
		contourLength= contourCounter;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static BlobAttributes computeHistogram(java.awt.image.BufferedImage image, int[] deltaPixels, int[] contourPixels, long time, int x1, int x2, int y1, int y2, int imageWidth, int noDifferenceMarker) {
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
}
