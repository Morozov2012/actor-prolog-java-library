// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.pxl;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMpxlSmoothImage extends VPM_FrameCommand {
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
	protected long[][] layers;
	protected int numberOfDirectives= 0;
	protected int[] layerNumberSequence;
	protected int[] widthSequence;
	protected int[] halfwidthSequence;
	//
	protected int[] resultMatrix;
	//
	public VPMpxlSmoothImage(int h) {
		if (h < 0) {
			h= 0;
		};
		halfwidth= h;
	}
	//
	@Override
	public void execute(VPM vpm) {
		imageWidth= vpm.getOperationalImageWidth();
		imageHeight= vpm.getOperationalImageHeight();
		int[] operationalMatrix= vpm.getOperationalMatrix();
		if (halfwidth <= 3 || halfwidth==6 || halfwidth==9) {
			initiateResultMatrixIfNecessary();
			for (int y=0; y < imageHeight; y++) {
				for (int x=0; x < imageWidth; x++) {
//=====================================================================
long s= 0;
int n= 0;
for (int h=-halfwidth; h <= halfwidth; h++) {
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
		s+= operationalMatrix[index1];
		n++;
	}
};
int index0= imageWidth * y + x;
resultMatrix[index0]= (int)(s / n);
//=====================================================================
				}
			};
			vpm.setOperationalMatrix(resultMatrix);
			resultMatrix= operationalMatrix;
		} else {
			if (layers==null) {
				initializeClassInstance();
			};
			fillLayers(operationalMatrix);
			averageMatrix();
			vpm.setOperationalMatrix(resultMatrix);
			resultMatrix= operationalMatrix;
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
		layers= new long[numberOfLayers][extendedImageLength];
		initiateResultMatrixIfNecessary();
	}
	//
	protected void initiateResultMatrixIfNecessary() {
		if (resultMatrix==null) {
			int operationalMatrixLength= imageWidth * imageHeight;
			resultMatrix= new int[operationalMatrixLength];
		}
	}
	//
	protected void fillLayers(int[] operationalMatrix) {
		for (int y=0; y < imageHeight; y++) {
			for (int x=0; x < imageWidth; x++) {
				int index1= imageWidth * y + x;
				int index2= extendedImageWidth * (y+borderEffect) + (x+borderEffect);
				layers[0][index2]= operationalMatrix[index1];
			}
		};
		int layerCoefficient= 1;
		for (int layerNumber=1; layerNumber < numberOfLayers; layerNumber++) {
			for (int y=-borderEffect; y < imageHeight+borderEffect; y++) {
				for (int x=-borderEffect; x < imageWidth+borderEffect; x++) {
//---------------------------------------------------------------------
long s= 0;
for (int h1=-1; h1 <= 1; h1++) {
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
		s+= layers[layerNumber-1][index1];
	}
};
int index0= extendedImageWidth * (y+borderEffect) + (x+borderEffect);
layers[layerNumber][index0]= s;
//---------------------------------------------------------------------
				}
			};
			layerCoefficient*= 3;
		}
	}
	//
	protected void averageMatrix() {
		int imageWidthMinusOne= imageWidth - 1;
		int imageHeightMinusOne= imageHeight - 1;
		for (int y=0; y < imageHeight; y++) {
			for (int x=0; x < imageWidth; x++) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
long s= 0;
int n= 0;
if (	y > borderEffect-1 && y < imageHeight-borderEffect-1 &&
	x > borderEffect-1 && x < imageWidth-borderEffect-1) {
//---------------------------------------------------------------------
int radius= 0;
for (int directiveNumber= 0; directiveNumber < numberOfDirectives; directiveNumber++) {
	int layerNumber= layerNumberSequence[directiveNumber];
	int windowWidth= widthSequence[directiveNumber];
	int windowHalfwidth= halfwidthSequence[directiveNumber];
	int area= windowWidth * windowWidth;
	if (directiveNumber==0) {
		int index0= extendedImageWidth * (y+borderEffect) + (x+borderEffect);
		s+= layers[layerNumber][index0];
		n+= area;
		radius+= windowHalfwidth;
	} else {
		radius+= windowHalfwidth + 1;
		int yy= y + radius;
		if (-windowHalfwidth <= yy && yy < imageHeight+windowHalfwidth) {
			for (int w=-radius; w <= radius; w+= windowWidth) {
				int xx= x + w;
				if (-windowHalfwidth <= xx && xx < imageWidth+windowHalfwidth) {
					int index1= extendedImageWidth * (yy+borderEffect) + (xx+borderEffect);
					s+= layers[layerNumber][index1];
					n+= area;
				}
			}
		};
		yy= y - radius;
		if (-windowHalfwidth <= yy && yy < imageHeight+windowHalfwidth) {
			for (int w=-radius; w <= radius; w+= windowWidth) {
				int xx= x + w;
				if (-windowHalfwidth <= xx && xx < imageWidth+windowHalfwidth) {
					int index1= extendedImageWidth * (yy+borderEffect) + (xx+borderEffect);
					s+= layers[layerNumber][index1];
					n+= area;
				}
			}
		};
		int xx= x + radius;
		if (-windowHalfwidth <= xx && xx < imageWidth+windowHalfwidth) {
			for (int h=windowWidth-radius; h <= radius-windowWidth; h+= windowWidth) {
				yy= y + h;
				if (-windowHalfwidth <= yy && yy < imageHeight+windowHalfwidth) {
					int index1= extendedImageWidth * (yy+borderEffect) + (xx+borderEffect);
					s+= layers[layerNumber][index1];
					n+= area;
				}
			}
		};
		xx= x - radius;
		if (-windowHalfwidth <= xx && xx < imageWidth+windowHalfwidth) {
			for (int h=windowWidth-radius; h <= radius-windowWidth; h+= windowWidth) {
				yy= y + h;
				if (-windowHalfwidth <= yy && yy < imageHeight+windowHalfwidth) {
					int index1= extendedImageWidth * (yy+borderEffect) + (xx+borderEffect);
					s+= layers[layerNumber][index1];
					n+= area;
				}
			}
		};
		radius+= windowHalfwidth;
	}
}
//---------------------------------------------------------------------
} else {
//---------------------------------------------------------------------
int radius= 0;
for (int directiveNumber= 0; directiveNumber < numberOfDirectives; directiveNumber++) {
	int layerNumber= layerNumberSequence[directiveNumber];
	int windowWidth= widthSequence[directiveNumber];
	int windowHalfwidth= halfwidthSequence[directiveNumber];
	if (directiveNumber==0) {
		int xLeft= x - windowHalfwidth;
		int xRight= x + windowHalfwidth;
		int yBottom= y - windowHalfwidth;
		int yTop= y + windowHalfwidth;
		if (xLeft < 0) {
			xLeft= 0;
		} else if (xLeft > imageWidthMinusOne) {
			xLeft= imageWidthMinusOne;
		};
		if (xRight < 0) {
			xRight= 0;
		} else if (xRight > imageWidthMinusOne) {
			xRight= imageWidthMinusOne;
		};
		if (yBottom < 0) {
			yBottom= 0;
		} else if (yBottom > imageHeightMinusOne) {
			yBottom= imageHeightMinusOne;
		};
		if (yTop < 0) {
			yTop= 0;
		} else if (yTop > imageHeightMinusOne) {
			yTop= imageHeightMinusOne;
		};
		int area= (xRight-xLeft+1) * (yTop-yBottom+1);
		int index0= extendedImageWidth * (y+borderEffect) + (x+borderEffect);
		s+= layers[layerNumber][index0];
		n+= area;
		radius+= windowHalfwidth;
	} else {
		radius+= windowHalfwidth + 1;
		int yy= y + radius;
		if (-windowHalfwidth <= yy && yy < imageHeight+windowHalfwidth) {
			int yBottom= yy - windowHalfwidth;
			int yTop= yy + windowHalfwidth;
			if (yBottom < 0) {
				yBottom= 0;
			} else if (yBottom > imageHeightMinusOne) {
				yBottom= imageHeightMinusOne;
			};
			if (yTop < 0) {
				yTop= 0;
			} else if (yTop > imageHeightMinusOne) {
				yTop= imageHeightMinusOne;
			};
			for (int w=-radius; w <= radius; w+= windowWidth) {
				int xx= x + w;
				if (-windowHalfwidth <= xx && xx < imageWidth+windowHalfwidth) {
					int xLeft= xx - windowHalfwidth;
					int xRight= xx + windowHalfwidth;
					if (xLeft < 0) {
						xLeft= 0;
					} else if (xLeft > imageWidthMinusOne) {
						xLeft= imageWidthMinusOne;
					};
					if (xRight < 0) {
						xRight= 0;
					} else if (xRight > imageWidthMinusOne) {
						xRight= imageWidthMinusOne;
					};
					int area= (xRight-xLeft+1) * (yTop-yBottom+1);
					int index1= extendedImageWidth * (yy+borderEffect) + (xx+borderEffect);
					s+= layers[layerNumber][index1];
					n+= area;
				}
			}
		};
		yy= y - radius;
		if (-windowHalfwidth <= yy && yy < imageHeight+windowHalfwidth) {
			int yBottom= yy - windowHalfwidth;
			int yTop= yy + windowHalfwidth;
			if (yBottom < 0) {
				yBottom= 0;
			} else if (yBottom > imageHeightMinusOne) {
				yBottom= imageHeightMinusOne;
			};
			if (yTop < 0) {
				yTop= 0;
			} else if (yTop > imageHeightMinusOne) {
				yTop= imageHeightMinusOne;
			};
			for (int w=-radius; w <= radius; w+= windowWidth) {
				int xx= x + w;
				if (-windowHalfwidth <= xx && xx < imageWidth+windowHalfwidth) {
					int xLeft= xx - windowHalfwidth;
					int xRight= xx + windowHalfwidth;
					if (xLeft < 0) {
						xLeft= 0;
					} else if (xLeft > imageWidthMinusOne) {
						xLeft= imageWidthMinusOne;
					};
					if (xRight < 0) {
						xRight= 0;
					} else if (xRight > imageWidthMinusOne) {
						xRight= imageWidthMinusOne;
					};
					int area= (xRight-xLeft+1) * (yTop-yBottom+1);
					int index1= extendedImageWidth * (yy+borderEffect) + (xx+borderEffect);
					s+= layers[layerNumber][index1];
					n+= area;
				}
			}
		};
		int xx= x + radius;
		if (-windowHalfwidth <= xx && xx < imageWidth+windowHalfwidth) {
			int xLeft= xx - windowHalfwidth;
			int xRight= xx + windowHalfwidth;
			if (xLeft < 0) {
				xLeft= 0;
			} else if (xLeft > imageWidthMinusOne) {
				xLeft= imageWidthMinusOne;
			};
			if (xRight < 0) {
				xRight= 0;
			} else if (xRight > imageWidthMinusOne) {
				xRight= imageWidthMinusOne;
			};
			for (int h=windowWidth-radius; h <= radius-windowWidth; h+= windowWidth) {
				yy= y + h;
				if (-windowHalfwidth <= yy && yy < imageHeight+windowHalfwidth) {
					int yBottom= yy - windowHalfwidth;
					int yTop= yy + windowHalfwidth;
					if (yBottom < 0) {
						yBottom= 0;
					} else if (yBottom > imageHeightMinusOne) {
						yBottom= imageHeightMinusOne;
					};
					if (yTop < 0) {
						yTop= 0;
					} else if (yTop > imageHeightMinusOne) {
						yTop= imageHeightMinusOne;
					};
					int area= (xRight-xLeft+1) * (yTop-yBottom+1);
					int index1= extendedImageWidth * (yy+borderEffect) + (xx+borderEffect);
					s+= layers[layerNumber][index1];
					n+= area;
				}
			}
		};
		xx= x - radius;
		if (-windowHalfwidth <= xx && xx < imageWidth+windowHalfwidth) {
			int xLeft= xx - windowHalfwidth;
			int xRight= xx + windowHalfwidth;
			if (xLeft < 0) {
				xLeft= 0;
			} else if (xLeft > imageWidthMinusOne) {
				xLeft= imageWidthMinusOne;
			};
			if (xRight < 0) {
				xRight= 0;
			} else if (xRight > imageWidthMinusOne) {
				xRight= imageWidthMinusOne;
			};
			for (int h=windowWidth-radius; h <= radius-windowWidth; h+= windowWidth) {
				yy= y + h;
				if (-windowHalfwidth <= yy && yy < imageHeight+windowHalfwidth) {
					int yBottom= yy - windowHalfwidth;
					int yTop= yy + windowHalfwidth;
					if (yBottom < 0) {
						yBottom= 0;
					} else if (yBottom > imageHeightMinusOne) {
						yBottom= imageHeightMinusOne;
					};
					if (yTop < 0) {
						yTop= 0;
					} else if (yTop > imageHeightMinusOne) {
						yTop= imageHeightMinusOne;
					};
					int area= (xRight-xLeft+1) * (yTop-yBottom+1);
					int index1= extendedImageWidth * (yy+borderEffect) + (xx+borderEffect);
					s+= layers[layerNumber][index1];
					n+= area;
				}
			}
		};
		radius+= windowHalfwidth;
	}
}
//---------------------------------------------------------------------
};
int index2= imageWidth * y + x;
resultMatrix[index2]= (int)(s / n);
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			}
		}
	}
}
