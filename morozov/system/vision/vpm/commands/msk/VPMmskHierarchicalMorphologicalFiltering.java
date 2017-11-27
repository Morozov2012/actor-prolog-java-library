// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

abstract public class VPMmskHierarchicalMorphologicalFiltering extends VPM_FrameCommand {
	//
	protected int halfwidth;
	//
	protected int imageWidth= -1;
	protected int imageHeight= -1;
	protected int extendedImageWidth= -1;
	protected int extendedImageHeight= -1;
	//
	protected int fullWidth= -1;
	protected int numberOfLayers= -1;
	protected int maximalDivisor= -1;
	protected int borderEffect= -1;
	//
	protected boolean[][] layers;
	protected int numberOfDirectives= 0;
	protected int[] layerNumberSequence;
	protected int[] widthSequence;
	protected int[] halfwidthSequence;
	//
	protected boolean[] resultMatrix;
	//
	public VPMmskHierarchicalMorphologicalFiltering(int h) {
		halfwidth= h;
	}
	//
	protected void initializeClassInstance() {
		fullWidth= halfwidth * 2 + 1;
		numberOfLayers= 1;
		maximalDivisor= 1;
		int a= fullWidth;
		int c= 1;
		for (int k=1; k <= fullWidth; k++) {
			c*= 3;
			int b= a / c;
			if (b <= 0) {
				numberOfLayers= k;
				break;
			};
			maximalDivisor*= 3;
		};
		borderEffect= maximalDivisor / 2 + 1;
		int d= maximalDivisor;
		for (int k=1; k < fullWidth; k++) {
			d/= 3;
			if (d <= 1) {
				break;
			} else {
				borderEffect+= d + d;
			}
		};
		borderEffect--;
		int halfMaximalDivisor= maximalDivisor / 2;
		layerNumberSequence= new int[halfwidth+1];
		widthSequence= new int[halfwidth+1];
		halfwidthSequence= new int[halfwidth+1];
		layerNumberSequence[0]= numberOfLayers - 1;
		widthSequence[0]= maximalDivisor;
		halfwidthSequence[0]= halfMaximalDivisor;
		numberOfDirectives= 1;
		int windowWidth= maximalDivisor;
		int currentLayerNumber= numberOfLayers-1;
		int distance= halfwidth - halfMaximalDivisor;
		for (int k=1; k <= halfwidth+1; k++) {
			if (distance <= 0) {
				break;
			};
			int delta= distance - windowWidth;
			if (delta >= 0) {
				for (int m=1; m <= halfwidth+1; m++) {
					if (delta >= 0) {
						distance= delta;
						layerNumberSequence[numberOfDirectives]= currentLayerNumber;
						widthSequence[numberOfDirectives]= windowWidth;
						halfwidthSequence[numberOfDirectives]= windowWidth / 2;
						numberOfDirectives++;
						if (distance <= 0) {
							break;
						}
					} else {
						break;
					};
					delta= distance - windowWidth;
				}
			} else {
				windowWidth/= 3;
				currentLayerNumber--;
			}
		};
		extendedImageWidth= imageWidth + borderEffect*2;
		extendedImageHeight= imageHeight + borderEffect*2;
		int extendedImageLength= extendedImageWidth * extendedImageHeight;
		layers= new boolean[numberOfLayers][extendedImageLength];
		initiateResultMatrixIfNecessary();
	}
	//
	protected void initiateResultMatrixIfNecessary() {
		if (resultMatrix==null) {
			int foregroundMaskLength= imageWidth * imageHeight;
			resultMatrix= new boolean[foregroundMaskLength];
		}
	}
}
