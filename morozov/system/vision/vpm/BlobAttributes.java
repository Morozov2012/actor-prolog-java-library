// (c) 2013-2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import target.*;

import morozov.system.vision.vpm.converters.*;
import morozov.terms.*;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.math.BigInteger;

public class BlobAttributes {
	//
	protected long frameNumber;
	protected long timeInMilliseconds;
	protected BlobType type;
	protected int x1;
	protected int x2;
	protected int y1;
	protected int y2;
	protected int centroidX;
	protected int centroidY;
	protected long foregroundArea;
	protected long contourLength;
	//
	protected Term prologColorHistograms;
	protected Term prologBlobAttributes;
	//
	///////////////////////////////////////////////////////////////
	//
	protected HashMap<ImageChannelName,double[]> colorHistograms;
	//
	///////////////////////////////////////////////////////////////
	//
	public BlobAttributes(
			long frame,
			long milliseconds,
			BlobType givenType,
			int rX1,
			int rX2,
			int rY1,
			int rY2,
			int cX,
			int cY,
			long area,
			long length) {
		frameNumber= frame;
		timeInMilliseconds= milliseconds;
		type= givenType;
		x1= rX1;
		x2= rX2;
		y1= rY1;
		y2= rY2;
		centroidX= cX;
		centroidY= cY;
		foregroundArea= area;
		contourLength= length;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getFrameNumber() {
		return frameNumber;
	}
	public long getTimeInMilliseconds() {
		return timeInMilliseconds;
	}
	public BlobType getType() {
		return type;
	}
	public int getX1() {
		return x1;
	}
	public int getX2() {
		return x2;
	}
	public int getY1() {
		return y1;
	}
	public int getY2() {
		return y2;
	}
	public int getCentroidX() {
		return centroidX;
	}
	public int getCentroidY() {
		return centroidY;
	}
	public long getForegroundArea() {
		return foregroundArea;
	}
	public long getContourLength() {
		return contourLength;
	}
	public HashMap<ImageChannelName,double[]> getColorHistograms() {
		return colorHistograms;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term toTerm(BigInteger identifier) {
		Term prologIdentifier= new PrologInteger(identifier);
		Term prologBlob=
			new PrologSet(
			- SymbolCodes.symbolCode_E_identifier,
			prologIdentifier,
			blobAttributesToTerm());
		return prologBlob;
	}
	//
	public Term blobAttributesToTerm() {
		if (prologBlobAttributes != null) {
			return prologBlobAttributes;
		};
		Term prologType= type.toTerm();
		Term prologX= new PrologInteger(StrictMath.round((x1+x2)/2));
		Term prologY= new PrologInteger(StrictMath.round((y1+y2)/2));
		Term prologWidth= new PrologInteger(x2-x1);
		Term prologHeight= new PrologInteger(y2-y1);
		Term prologCentroidX= new PrologInteger(centroidX);
		Term prologCentroidY= new PrologInteger(centroidY);
		Term prologForegroundArea= new PrologInteger(foregroundArea);
		Term prologContourLength= new PrologInteger(contourLength);
		prologBlobAttributes= PrologEmptySet.instance;
		if (colorHistograms != null) {
			Term prologBlobHistogramList= colorHistogramsToTerm();
			prologBlobAttributes=
				new PrologSet(
				- SymbolCodes.symbolCode_E_histograms,
				prologBlobHistogramList,
				prologBlobAttributes);
		};
		prologBlobAttributes=
			new PrologSet(
			- SymbolCodes.symbolCode_E_type,
			prologType,
			new PrologSet(
			- SymbolCodes.symbolCode_E_x,
			prologX,
			new PrologSet(
			- SymbolCodes.symbolCode_E_y,
			prologY,
			new PrologSet(
			- SymbolCodes.symbolCode_E_width,
			prologWidth,
			new PrologSet(
			- SymbolCodes.symbolCode_E_height,
			prologHeight,
			new PrologSet(
			- SymbolCodes.symbolCode_E_centroid_x,
			prologCentroidX,
			new PrologSet(
			- SymbolCodes.symbolCode_E_centroid_y,
			prologCentroidY,
			new PrologSet(
			- SymbolCodes.symbolCode_E_foreground_area,
			prologForegroundArea,
			new PrologSet(
			- SymbolCodes.symbolCode_E_contour_length,
			prologContourLength,
			prologBlobAttributes)))))))));
		return prologBlobAttributes;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void storeHistogram(ImageChannelName outputChannelName, double[] histogram) {
		if (outputChannelName==ImageChannelName.ALL) {
			outputChannelName= ImageChannelName.GRAYSCALE;
		};
		if (colorHistograms==null) {
			colorHistograms= new HashMap<>();
		};
		colorHistograms.put(outputChannelName,histogram);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term colorHistogramsToTerm() {
		if (prologColorHistograms != null) {
			return prologColorHistograms;
		};
		prologColorHistograms= PrologEmptyList.instance;
		Set<ImageChannelName> imageChannelNames= colorHistograms.keySet();
		Iterator<ImageChannelName> imageChannelNamesIterator= imageChannelNames.iterator();
		while (imageChannelNamesIterator.hasNext()) {
			ImageChannelName imageChannelName= imageChannelNamesIterator.next();
			double[] histogram= colorHistograms.get(imageChannelName);
			int numberOfBins= histogram.length;
			if (numberOfBins <= 0) {
				continue;
			};
			double minValue= histogram[0];
			double maxValue= histogram[0];
			for (int n=0; n < numberOfBins-1; n++) {
				double value= histogram[n];
				if (minValue > value) {
					minValue= value;
				};
				if (maxValue < value) {
					maxValue= value;
				}
			};
			double[] standardizedHistogram= new double[numberOfBins];
			double delta= maxValue - minValue;
			if (delta > 0) {
				for (int n=0; n < numberOfBins-1; n++) {
					standardizedHistogram[n]= (histogram[n] - minValue) / delta;
				}
			};
			Term prologHistogram= PrologEmptyList.instance;
			for (int n=numberOfBins-1; n >= 0; n--) {
				double value= standardizedHistogram[n];
				prologHistogram= new PrologList(new PrologReal(value),prologHistogram);
			};
			Term prologColorHistogram=
				new PrologSet(
				- SymbolCodes.symbolCode_E_channel,
				imageChannelName.toTerm(),
				new PrologSet(
				- SymbolCodes.symbolCode_E_number_of_bins,
				new PrologInteger(numberOfBins),
				new PrologSet(
				- SymbolCodes.symbolCode_E_histogram_mean,
				prologHistogram,
				new PrologSet(
				- SymbolCodes.symbolCode_E_cardinality,
				PrologInteger.ONE,
				PrologEmptySet.instance))));
			prologColorHistograms= new PrologList(prologColorHistogram,prologColorHistograms);
		};
		return prologColorHistograms;
	}
}
