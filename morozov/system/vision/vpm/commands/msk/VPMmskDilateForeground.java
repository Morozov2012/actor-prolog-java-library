// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

public class VPMmskDilateForeground extends VPMmskHierarchicalMorphologicalFiltering {
	//
	protected DilationAlgorithm algorithm;
	//
	public VPMmskDilateForeground(int h, DilationAlgorithm a) {
		super(h);
		algorithm= a;
	}
	//
	public void execute(VPM vpm) {
		imageWidth= vpm.getOperationalImageWidth();
		imageHeight= vpm.getOperationalImageHeight();
		boolean[] foregroundMask= vpm.getForegroundMask();
		if (algorithm==DilationAlgorithm.PLAIN_DILATION) {
			initiateResultMatrixIfNecessary();
			for (int y=0; y < imageHeight; y++) {
				for (int x=0; x < imageWidth; x++) {
//=====================================================================
int index0= imageWidth * y + x;
boolean value= foregroundMask[index0];
if (!value) {
	LoopXY: for (int h=-halfwidth; h <= halfwidth; h++) {
		int yy= y + h;
		if (yy < 0 || yy >= imageHeight) {
			continue;
		};
		for (int w=-halfwidth; w <= halfwidth; w++) {
			int xx= x + w;
			if (xx < 0 || xx >= imageWidth) {
				continue;
			};
			int index1= imageWidth * yy + xx;
			if (foregroundMask[index1]) {
				value= true;
				break LoopXY;
			}
		}
	}
};
resultMatrix[index0]= value;
//=====================================================================
				}
			};
			vpm.setForegroundMask(resultMatrix);
			resultMatrix= foregroundMask;
		} else {
			if (layers==null) {
				initializeClassInstance();
			};
			fillLayers(foregroundMask);
			dilateMatrix();
			vpm.setForegroundMask(resultMatrix);
			resultMatrix= foregroundMask;
		}
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
	protected void fillLayers(boolean[] foregroundMask) {
		for (int y=0; y < imageHeight; y++) {
			for (int x=0; x < imageWidth; x++) {
				int index1= imageWidth * y + x;
				int index2= extendedImageWidth * (y+borderEffect) + (x+borderEffect);
				layers[0][index2]= foregroundMask[index1];
			}
		};
		int layerCoefficient= 1;
		for (int layerNumber=1; layerNumber < numberOfLayers; layerNumber++) {
			for (int y=-borderEffect; y < imageHeight+borderEffect; y++) {
				for (int x=-borderEffect; x < imageWidth+borderEffect; x++) {
//---------------------------------------------------------------------
boolean value= false;
LoopXY: for (int h1=-1; h1 <= 1; h1++) {
	int h2= h1 * layerCoefficient;
	int yy= y + h2 + borderEffect;
	if (yy < 0 || yy >= extendedImageHeight) {
		continue;
	};
	for (int w1=-1; w1 <= 1; w1++) {
		int w2= w1 * layerCoefficient;
		int xx= x + w2 + borderEffect;
		if (xx < 0 || xx >= extendedImageWidth) {
			continue;
		};
		int index1= extendedImageWidth * yy + xx;
		if (layers[layerNumber-1][index1]) {
			value= true;
			break LoopXY;
		}
	}
};
int index0= extendedImageWidth * (y+borderEffect) + (x+borderEffect);
layers[layerNumber][index0]= value;
//---------------------------------------------------------------------
				}
			};
			layerCoefficient*= 3;
		}
	}
	//
	protected void dilateMatrix() {
		int imageWidthMinusOne= imageWidth - 1;
		int imageHeightMinusOne= imageHeight - 1;
		for (int y=0; y < imageHeight; y++) {
			for (int x=0; x < imageWidth; x++) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
boolean value= false;
int radius= 0;
LoopXY: for (int directiveNumber= 0; directiveNumber < numberOfDirectives; directiveNumber++) {
	int layerNumber= layerNumberSequence[directiveNumber];
	int windowWidth= widthSequence[directiveNumber];
	int windowHalfwidth= halfwidthSequence[directiveNumber];
	if (directiveNumber==0) {
		int index0= extendedImageWidth * (y+borderEffect) + (x+borderEffect);
		if (layers[layerNumber][index0]) {
			value= true;
			break LoopXY;
		};
		radius+= windowHalfwidth;
	} else {
		radius+= windowHalfwidth + 1;
		int yy= y + radius;
		if (-windowHalfwidth <= yy && yy < imageHeight+windowHalfwidth) {
			for (int w=-radius; w <= radius; w+= windowWidth) {
				int xx= x + w;
				if (-windowHalfwidth <= xx && xx < imageWidth+windowHalfwidth) {
					int index1= extendedImageWidth * (yy+borderEffect) + (xx+borderEffect);
					if (layers[layerNumber][index1]) {
						value= true;
						break LoopXY;
					}
				}
			}
		};
		yy= y - radius;
		if (-windowHalfwidth <= yy && yy < imageHeight+windowHalfwidth) {
			for (int w=-radius; w <= radius; w+= windowWidth) {
				int xx= x + w;
				if (-windowHalfwidth <= xx && xx < imageWidth+windowHalfwidth) {
					int index1= extendedImageWidth * (yy+borderEffect) + (xx+borderEffect);
					if (layers[layerNumber][index1]) {
						value= true;
						break LoopXY;
					}
				}
			}
		};
		int xx= x + radius;
		if (-windowHalfwidth <= xx && xx < imageWidth+windowHalfwidth) {
			for (int h=windowWidth-radius; h <= radius-windowWidth; h+= windowWidth) {
				yy= y + h;
				if (-windowHalfwidth <= yy && yy < imageHeight+windowHalfwidth) {
					int index1= extendedImageWidth * (yy+borderEffect) + (xx+borderEffect);
					if (layers[layerNumber][index1]) {
						value= true;
						break LoopXY;
					}
				}
			}
		};
		xx= x - radius;
		if (-windowHalfwidth <= xx && xx < imageWidth+windowHalfwidth) {
			for (int h=windowWidth-radius; h <= radius-windowWidth; h+= windowWidth) {
				yy= y + h;
				if (-windowHalfwidth <= yy && yy < imageHeight+windowHalfwidth) {
					int index1= extendedImageWidth * (yy+borderEffect) + (xx+borderEffect);
					if (layers[layerNumber][index1]) {
						value= true;
						break LoopXY;
					}
				}
			}
		};
		radius+= windowHalfwidth;
	}
};
int index2= imageWidth * y + x;
resultMatrix[index2]= value;
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			}
		}
	}
}
