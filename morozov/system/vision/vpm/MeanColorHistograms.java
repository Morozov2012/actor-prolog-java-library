// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm;

import target.*;

import morozov.system.vision.vpm.converters.*;
import morozov.terms.*;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class MeanColorHistograms {
	//
	protected HashMap<ImageChannelName,MeanColorHistogram> colorHistograms;
	//
	protected Term prologColorHistograms;
	//
	public MeanColorHistograms() {
	}
	//
	public void addHistograms(HashMap<ImageChannelName,double[]> blobHistograms) {
		if (colorHistograms==null) {
			colorHistograms= new HashMap<>();
		};
		Set<ImageChannelName> imageChannelNames= blobHistograms.keySet();
		Iterator<ImageChannelName> imageChannelNamesIterator= imageChannelNames.iterator();
		while (imageChannelNamesIterator.hasNext()) {
			ImageChannelName imageChannelName= imageChannelNamesIterator.next();
			double[] histogram= blobHistograms.get(imageChannelName);
			int numberOfBins= histogram.length;
			MeanColorHistogram meanColorHistogram= colorHistograms.get(imageChannelName);
			if (meanColorHistogram==null) {
				meanColorHistogram= new MeanColorHistogram(numberOfBins);
				colorHistograms.put(imageChannelName,meanColorHistogram);
			};
			meanColorHistogram.add(histogram);
		}
	}
	//
	public boolean isEmpty() {
		if (colorHistograms==null) {
			return true;
		} else {
			return colorHistograms.isEmpty();
		}
	}
	//
	public Term toTerm() {
		if (colorHistograms==null) {
			prologColorHistograms= PrologEmptyList.instance;
			return prologColorHistograms;
		};
		if (prologColorHistograms != null) {
			return prologColorHistograms;
		};
		prologColorHistograms= PrologEmptyList.instance;
		Set<ImageChannelName> imageChannelNames= colorHistograms.keySet();
		Iterator<ImageChannelName> imageChannelNamesIterator= imageChannelNames.iterator();
		while (imageChannelNamesIterator.hasNext()) {
			ImageChannelName imageChannelName= imageChannelNamesIterator.next();
			MeanColorHistogram histogram= colorHistograms.get(imageChannelName);
			if (histogram==null) {
				continue;
			};
			Term prologHistogramMean= histogram.histogramMeanToTerm();
			Term prologHistogramSigma= histogram.histogramSigmaToTerm();
			Term prologColorHistogram=
				new PrologSet(
				- SymbolCodes.symbolCode_E_channel,
				imageChannelName.toTerm(),
				new PrologSet(
				- SymbolCodes.symbolCode_E_number_of_bins,
				new PrologInteger(histogram.getNumberOfBins()),
				new PrologSet(
				- SymbolCodes.symbolCode_E_histogram_mean,
				prologHistogramMean,
				new PrologSet(
				- SymbolCodes.symbolCode_E_histogram_sigma,
				prologHistogramSigma,
				new PrologSet(
				- SymbolCodes.symbolCode_E_cardinality,
				new PrologInteger(histogram.getCardinality()),
				PrologEmptySet.instance)))));
			prologColorHistograms= new PrologList(prologColorHistogram,prologColorHistograms);
		};
		return prologColorHistograms;
	}
}
