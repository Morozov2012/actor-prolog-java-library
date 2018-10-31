// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.blb;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

public class VPMblbExtractBlobs extends VPM_FrameCommand {
	//
	protected long numberOfProcessedFrames= 0;
	//
	protected BlobType blobType;
	protected BlobExtractionAlgorithm algorithm;
	//
	///////////////////////////////////////////////////////////////
	//
	public VPMblbExtractBlobs(BlobType type, BlobExtractionAlgorithm a) {
		blobType= type;
		algorithm= a;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void execute(VPM vpm) {
		extractBlobs(vpm);
		if (numberOfProcessedFrames < Long.MAX_VALUE) {
			numberOfProcessedFrames= numberOfProcessedFrames + 1;
		}
	}
	protected void extractBlobs(VPM vpm) {
		int minimalTrainingInterval= vpm.getMinimalTrainingInterval();
		if (numberOfProcessedFrames <= minimalTrainingInterval) {
			return;
		};
		long frameNumber= vpm.getRecentFrameNumber();
		long timeInMilliseconds= vpm.getRecentTimeInMilliseconds();
		boolean[] foregroundMask= vpm.getForegroundMask();
		int width= vpm.getForegroundMaskWidth();
		int height= vpm.getForegroundMaskHeight();
		int horizontalBlobBorder= vpm.getHorizontalBlobBorder();
		int verticalBlobBorder= vpm.getVerticalBlobBorder();
		double horizontalExtraBorderCoefficient= vpm.getHorizontalExtraBorderCoefficient();
		double verticalExtraBorderCoefficient= vpm.getVerticalExtraBorderCoefficient();
		int minimalBlobIntersectionArea= vpm.getMinimalBlobIntersectionArea();
		int minimalBlobSize= vpm.getMinimalBlobSize();
		int[][] blobRectangles;
		switch (algorithm) {
		case TWO_PASS_BLOB_EXTRACTION:
			blobRectangles= twoPassBlobExtraction(
				foregroundMask,
				width,
				height,
				horizontalBlobBorder,
				verticalBlobBorder,
				horizontalExtraBorderCoefficient,
				verticalExtraBorderCoefficient,
				minimalBlobIntersectionArea,
				minimalBlobSize);
			break;
		case MULTIPASS_BLOB_EXTRACTION:
			blobRectangles= multipassBlobExtraction(
				foregroundMask,
				width,
				height,
				horizontalBlobBorder,
				verticalBlobBorder,
				horizontalExtraBorderCoefficient,
				verticalExtraBorderCoefficient,
				minimalBlobIntersectionArea,
				minimalBlobSize);
			break;
		default:
			System.err.printf("Unknown blob extraction algorithm: %d\n",algorithm);
			return;
		};
		BlobAttributes[] blobAttributeArray= createBlobAttributeArray(blobType,frameNumber,timeInMilliseconds,width,height,foregroundMask,blobRectangles);
		vpm.storeBlobGroup(new BlobGroup(blobType,blobAttributeArray));
	}
	public void forgetStatistics() {
		numberOfProcessedFrames= 0;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static BlobAttributes[] createBlobAttributeArray(BlobType blobType, long frameNumber, long timeInMilliseconds, int width, int height, boolean[] foregroundMask, int[][] blobRectangles) {
		boolean[] contourPixels= VisionUtils.contourForeground(foregroundMask,width,height);
		int numberOfBlobs= blobRectangles.length;
		BlobAttributes[] blobAttributeArray= new BlobAttributes[numberOfBlobs];
		for (int k=0; k < numberOfBlobs; k++) {
			int[] rectangle= blobRectangles[k];
			int x1= rectangle[0];
			int x2= rectangle[1];
			int y1= rectangle[2];
			int y2= rectangle[3];
			long numberOfPixels= 0;
			int centroidX= 0;
			int centroidY= 0;
			long contourLength= 0;
			for (int xx=x1; xx <= x2; xx++) {
				for (int yy=y1; yy <= y2; yy++) {
					int index1= width * yy + xx;
					if (foregroundMask[index1]) {
						numberOfPixels++;
						centroidX+= xx;
						centroidY+= yy;
					};
					if (contourPixels[index1]) {
						contourLength++;
					}
				}
			};
			centroidX= (int)(centroidX / numberOfPixels);
			centroidY= (int)(centroidY / numberOfPixels);
			BlobAttributes blobAttributes= new BlobAttributes(
				frameNumber,
				timeInMilliseconds,
				blobType,
				x1,
				x2,
				y1,
				y2,
				centroidX,
				centroidY,
				numberOfPixels,
				contourLength);
			blobAttributeArray[k]= blobAttributes;
		};
		return blobAttributeArray;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int[][] multipassBlobExtraction(
			boolean[] foregroundMask,
			int imageWidth,
			int imageHeight,
			int horizontalBlobBorder,
			int verticalBlobBorder,
			double horizontalExtraBorderCoefficient,
			double verticalExtraBorderCoefficient,
			int minimalBlobIntersectionArea,
			int minimalBlobSize) {
		int maxRadius= imageWidth;
		if (maxRadius < imageHeight) {
			maxRadius= imageHeight;
		};
		int vectorLength= imageWidth * imageHeight;
		int blobCounter= 0;
		boolean[] blobFlag= new boolean[vectorLength];
		int[][] blobRectangles= new int[vectorLength][4];
//---------------------------------------------------------------------
int rows= imageHeight - 2;
int columns= imageWidth - 2;
int totalDistance= rows * columns;
int c0;
if (columns % 2 == 0) {
	c0= columns / 2 + 1;
} else {
	c0= columns / 2 + 1;
};
int r0;
if (rows % 2 == 0) {
	r0= rows / 2;
} else {
	r0= rows / 2 + 1;
};
int globalCounter1= 0;
int localCounter1= 0;
int radius1= 1;
boolean verticalMovement1= true;
boolean goUp1= true;
boolean goRight1= false;
int c1= c0;
int r1= r0;
while (globalCounter1 < totalDistance) {
	if (r1 <= rows && r1 > 0 && c1 <= columns && c1 > 0) {
		globalCounter1++;
		int point= imageWidth*(r1+1) + (c1+1);
		if (foregroundMask[point]) {
//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://
int x11= c1 - horizontalBlobBorder;
int x12= c1 + horizontalBlobBorder;
int y11= r1 - verticalBlobBorder;
int y12= r1 + verticalBlobBorder;
boolean accepted= false;
int actualBlobIndex= -1;
boolean repeatSearch= true;
InnerLoop: while (repeatSearch) {
	repeatSearch= false;
	for (int k=0; k < blobCounter; k++) {
		if (!blobFlag[k]) {
			continue;
		};
		if (actualBlobIndex==k) {
			if (repeatSearch) {
				continue;
			} else {
				break InnerLoop;
			}
		};
		int width1= x12 - x11;
		int horizontalBorder1= 0;
		if (horizontalExtraBorderCoefficient > 0.0) {
			horizontalBorder1= (int)(width1 * horizontalExtraBorderCoefficient);
		};
		int x31= x11 - horizontalBorder1;
		int x32= x12 + horizontalBorder1;
		int x21= blobRectangles[k][0];
		int x22= blobRectangles[k][1];
		int width2= x22 - x21;
		int horizontalBorder2= 0;
		if (horizontalExtraBorderCoefficient > 0.0) {
			horizontalBorder2= (int)(width2 * horizontalExtraBorderCoefficient);
		};
		int x41= x21 - horizontalBorder2;
		int x42= x22 + horizontalBorder2;
		if (x31 < x41) {
			x31= x41;
		};
		if (x32 > x42) {
			x32= x42;
		};
		int width3= x32 - x31;
		if (width3 < 0) {
			continue;
		};
		int height1= y12 - y11;
		int verticalBorder1= 0;
		if (verticalExtraBorderCoefficient > 0.0) {
			verticalBorder1= (int)(height1 * verticalExtraBorderCoefficient);
		};
		int y31= y11 - verticalBorder1;
		int y32= y12 + verticalBorder1;
		int y21= blobRectangles[k][2];
		int y22= blobRectangles[k][3];
		int height2= y22 - y21;
		int verticalBorder2= 0;
		if (verticalExtraBorderCoefficient > 0.0) {
			verticalBorder2= (int)(height2 * verticalExtraBorderCoefficient);
		};
		int y41= y21 - verticalBorder2;
		int y42= y22 + verticalBorder2;
		if (y31 < y41) {
			y31= y41;
		};
		if (y32 > y42) {
			y32= y42;
		};
		int height3= y32 - y31;
		if (height3 < 0) {
			continue;
		};
		int commonAreaSize= (width3 + 1) * (height3 + 1);
		if (commonAreaSize >= minimalBlobIntersectionArea) {
			if (blobRectangles[k][0] > x11) {
				blobRectangles[k][0]= x11;
			};
			if (blobRectangles[k][1] < x12) {
				blobRectangles[k][1]= x12;
			};
			if (blobRectangles[k][2] > y11) {
				blobRectangles[k][2]= y11;
			};
			if (blobRectangles[k][3] < y12) {
				blobRectangles[k][3]= y12;
			};
			accepted= true;
			repeatSearch= true;
			if (actualBlobIndex >= 0) {
				blobFlag[actualBlobIndex]= false;
			};
			actualBlobIndex= k;
			x11= blobRectangles[k][0];
			x12= blobRectangles[k][1];
			y11= blobRectangles[k][2];
			y12= blobRectangles[k][3];
		}
	};
	if (!accepted) {
		accepted= true;
		repeatSearch= true;
		blobCounter++;
		actualBlobIndex= blobCounter - 1;
		blobFlag[actualBlobIndex]= true;
		blobRectangles[actualBlobIndex][0]= x11;
		blobRectangles[actualBlobIndex][1]= x12;
		blobRectangles[actualBlobIndex][2]= y11;
		blobRectangles[actualBlobIndex][3]= y12;
	}
}
//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://
		}
	};
	if (verticalMovement1) {
		if (goUp1) {
			r1++;
		} else {
			r1--;
		};
		if (localCounter1 <= 0) {
			verticalMovement1= !verticalMovement1;
			goUp1= !goUp1;
			localCounter1= radius1;
		}
	} else {
		if (goRight1) {
			c1++;
		} else {
			c1--;
		};
		if (localCounter1 <= 0) {
			verticalMovement1= !verticalMovement1;
			goRight1= !goRight1;
			radius1++;
			localCounter1= radius1;
		}
	};
	localCounter1--;
};
//---------------------------------------------------------------------
		int index= 0;
		int imageSize= (imageWidth - 2) * (imageHeight - 2);
		for (int k=0; k < blobCounter; k++) {
			if (blobFlag[k]) {
				int x1= blobRectangles[k][0];
				int x2= blobRectangles[k][1];
				int y1= blobRectangles[k][2];
				int y2= blobRectangles[k][3];
				if (x1 < 0) {
					x1= 0;
				} else if (x1 >= imageWidth) {
					x1= imageWidth - 1;
				};
				if (x2 < 0) {
					x2= 0;
				} else if (x2 >= imageWidth) {
					x2= imageWidth - 1;
				};
				if (y1 < 0) {
					y1= 0;
				} else if (y1 >= imageHeight) {
					y1= imageHeight - 1;
				};
				if (y2 < 0) {
					y2= 0;
				} else if (y2 >= imageHeight) {
					y2= imageHeight - 1;
				};
				int size= (x2 - x1 + 1) * (y2 - y1 + 1);
				if (size < minimalBlobSize) {
					blobFlag[k]= false;
				// } else if (size >= imageSize) {
				//	blobFlag[k]= false;
				} else {
					index++;
				};
				blobRectangles[k][0]= x1;
				blobRectangles[k][1]= x2;
				blobRectangles[k][2]= y1;
				blobRectangles[k][3]= y2;
			}
		};
		int numberOfActiveBlobs= index;
		int[][] activeBlobRectangles= new int[numberOfActiveBlobs][4];
		index= 0;
		for (int k=0; k < blobCounter; k++) {
			if (blobFlag[k]) {
				for (int m=0; m < 4; m++) {
					activeBlobRectangles[index][m]= blobRectangles[k][m];
				};
				index++;
			}
		};
		return activeBlobRectangles;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static int[][] twoPassBlobExtraction(
			boolean[] foregroundMask,
			int imageWidth,
			int imageHeight,
			int horizontalBlobBorder,
			int verticalBlobBorder,
			double horizontalExtraBorderCoefficient,
			double verticalExtraBorderCoefficient,
			int minimalBlobIntersectionArea,
			int minimalBlobSize) {
		int vectorLength= imageWidth * imageHeight;
		int lineCounter= -1;
		boolean[] lineFlags= new boolean[vectorLength];
		int[] lineLeftX= new int[vectorLength];
		int[] lineRightX= new int[vectorLength];
		int[] lineY= new int[vectorLength];
		for (int r1=0; r1 < imageHeight; r1++) {
			boolean lineIsDetected= false;
			for (int c1=0; c1 < imageWidth; c1++) {
				int index1= imageWidth * r1 + c1;
				if (foregroundMask[index1]) {
					if (!lineIsDetected) {
						lineCounter= lineCounter + 1;
						lineFlags[lineCounter]= true;
						lineLeftX[lineCounter]= c1;
						lineY[lineCounter]= r1;
						lineIsDetected= true;
					}
				} else {
					if (lineIsDetected) {
						lineRightX[lineCounter]= c1-1;
						lineIsDetected= false;
					}
				}
			};
			if (lineIsDetected) {
				lineRightX[lineCounter]= imageWidth;
				lineIsDetected= false;
			}
		};
		lineCounter= lineCounter + 1;
		int[] lineClusters= new int[lineCounter];
		boolean[] clusterFlags= new boolean[lineCounter];
		int[][] clusterRectangles= new int[lineCounter][4];
		for (int k=0; k < lineCounter; k++) {
			lineClusters[k]= k;
			clusterFlags[k]= true;
			int x1L= lineLeftX[k];
			int x1R= lineRightX[k];
			int y1= lineY[k];
			int x11= x1L - horizontalBlobBorder;
			int x12= x1R + horizontalBlobBorder;
			int y11= y1 - verticalBlobBorder;
			int y12= y1 + verticalBlobBorder;
			clusterRectangles[k][0]= x11;
			clusterRectangles[k][1]= x12;
			clusterRectangles[k][2]= y11;
			clusterRectangles[k][3]= y12;
		};
		for (int k=0; k < lineCounter; k++) {
			int x1L= lineLeftX[k];
			int x1R= lineRightX[k];
			int y1= lineY[k];
			int x11= x1L - horizontalBlobBorder;
			int x12= x1R + horizontalBlobBorder;
			int y11= y1 - verticalBlobBorder;
			int y12= y1 + verticalBlobBorder;
			for (int m=k+1; m < lineCounter; m++) {
				int x2L= lineLeftX[m];
				int x2R= lineRightX[m];
				int x21= x2L - horizontalBlobBorder;
				int x22= x2R + horizontalBlobBorder;
				if (x21 < x11) {
					x21= x11;
				};
				if (x22 > x12) {
					x22= x12;
				};
				int width1= x22 - x21;
				if (width1 < 0) {
					continue;
				};
				int y2= lineY[m];
				int y21= y2 - verticalBlobBorder;
				int y22= y2 + verticalBlobBorder;
				if (y21 < y11) {
					y21= y11;
				};
				if (y22 > y12) {
					y22= y12;
				};
				int height1= y22 - y21;
				if (height1 < 0) {
					continue;
				};
				int commonAreaSize= (width1 + 1) * (height1 + 1);
				if (commonAreaSize >= minimalBlobIntersectionArea) {
//---------------------------------------------------------------------
int cluster1= lineClusters[k];
int cluster2= lineClusters[m];
if (cluster1 != cluster2) {
	for (int n=0; n < lineCounter; n++) {
		if (lineClusters[n]==cluster2) {
			lineClusters[n]= cluster1;
		};
	};
	clusterFlags[cluster2]= false;
	int cX11= clusterRectangles[cluster1][0];
	int cX12= clusterRectangles[cluster1][1];
	int cY11= clusterRectangles[cluster1][2];
	int cY12= clusterRectangles[cluster1][3];
	int cX21= clusterRectangles[cluster2][0];
	int cX22= clusterRectangles[cluster2][1];
	int cY21= clusterRectangles[cluster2][2];
	int cY22= clusterRectangles[cluster2][3];
	if (cX21 < cX11) {
		cX11= cX21;
	};
	if (cX22 > cX12) {
		cX12= cX22;
	};
	if (cY21 < cY11) {
		cY11= cY21;
	};
	if (cY22 > cY12) {
		cY12= cY22;
	};
	clusterRectangles[cluster1][0]= cX11;
	clusterRectangles[cluster1][1]= cX12;
	clusterRectangles[cluster1][2]= cY11;
	clusterRectangles[cluster1][3]= cY12;
}
//---------------------------------------------------------------------
				}
			}
		};
		int index1= 0;
		for (int k=0; k < lineCounter; k++) {
			if (clusterFlags[k]) {
				int x1= clusterRectangles[k][0];
				int x2= clusterRectangles[k][1];
				int y1= clusterRectangles[k][2];
				int y2= clusterRectangles[k][3];
				if (x1 < 0) {
					x1= 0;
				} else if (x1 >= imageWidth) {
					x1= imageWidth - 1;
				};
				if (x2 < 0) {
					x2= 0;
				} else if (x2 >= imageWidth) {
					x2= imageWidth - 1;
				};
				if (y1 < 0) {
					y1= 0;
				} else if (y1 >= imageHeight) {
					y1= imageHeight - 1;
				};
				if (y2 < 0) {
					y2= 0;
				} else if (y2 >= imageHeight) {
					y2= imageHeight - 1;
				};
				int area= (x2 - x1 + 1) * (y2 - y1 + 1);
				if (area < minimalBlobSize) {
					clusterFlags[k]= false;
				} else {
					index1= index1 + 1;
				};
				clusterRectangles[k][0]= x1;
				clusterRectangles[k][1]= x2;
				clusterRectangles[k][2]= y1;
				clusterRectangles[k][3]= y2;
			}
		};
		int numberOfActiveBlobs= index1;
		int[][] activeBlobRectangles= new int[numberOfActiveBlobs][4];
		int index2= 0;
		for (int k=0; k < lineCounter; k++) {
			if (clusterFlags[k]) {
				for (int m=0; m < 4; m++) {
					activeBlobRectangles[index2][m]= clusterRectangles[k][m];
				};
				index2= index2 + 1;
			}
		};
		return activeBlobRectangles;
	}
}
