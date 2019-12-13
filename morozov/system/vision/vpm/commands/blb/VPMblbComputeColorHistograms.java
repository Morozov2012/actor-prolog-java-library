// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.blb;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

public class VPMblbComputeColorHistograms extends VPM_FrameCommand {
	//
	protected int numberOfBins;
	//
	protected static int defaultNumberOfBins= 16;
	//
	///////////////////////////////////////////////////////////////
	//
	public VPMblbComputeColorHistograms(int n) {
		if (n > 0) {
			numberOfBins= n;
		} else {
			numberOfBins= defaultNumberOfBins;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void execute(VPM vpm) {
		BlobGroup group= vpm.getRecentBlobGroup();
		if (group==null) {
			return;
		};
		BlobAttributes[] attributeArray= group.getAttributeArray();
		int numberOfBlobs= attributeArray.length;
		if (numberOfBlobs <= 0) {
			return;
		};
		int imageWidth= vpm.getOperationalImageWidth();
		int[] operationalMatrix= vpm.getOperationalMatrix();
		boolean[] foregroundMask= vpm.getForegroundMask();
		ImageChannelName outputChannelName= vpm.getOutputChannelName();
		for (int k=0; k < numberOfBlobs; k++) {
			BlobAttributes attributes= attributeArray[k];
			int x1= attributes.getX1();
			int y1= attributes.getY1();
			int x2= attributes.getX2();
			int y2= attributes.getY2();
			double[] histogram= new double[numberOfBins];
			int counter= 0;
			for (int xx=x1; xx <= x2; xx++) {
				for (int yy=y1; yy <= y2; yy++) {
//---------------------------------------------------------------------
int index1= imageWidth * yy + xx;
if (foregroundMask[index1]) {
	counter++;
	int value= operationalMatrix[index1];
	int index2= value * numberOfBins / maximalColor;
	if (index2 >= numberOfBins) {
		index2= numberOfBins-1;
	};
	histogram[index2]++;
}
//---------------------------------------------------------------------
				}
			};
			for (int m=0; m < numberOfBins; m++) {
				histogram[m]= histogram[m] / counter;
			};
			attributes.storeHistogram(outputChannelName,histogram);
		}
	}
}
