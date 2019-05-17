// (c) 2018 Alexei A. Morozov

package morozov.system.frames.tools;

import java.awt.image.WritableRaster;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;

public class DataFrameTools {
	//
	protected static int defaultColorImageWidth= 704;
	protected static int defaultColorImageHeight= 576;
	protected static Color transparentBackground= new Color(0,0,0,0);
	//
	public static AttachedImage temperaturesToImage(
			double[] targetTemperatures,
			int totalWidth,
			int totalHeight,
			int[][] mainColorMap,
			int[][] auxiliaryColorMap,
			boolean temperatureAutoranging,
			boolean useDoubleColorMap,
			double lowerTemperatureBound,
			double upperTemperatureBound,
			double lowerTemperatureQuantile1,
			double upperTemperatureQuantile1,
			double lowerTemperatureQuantile2,
			double upperTemperatureQuantile2,
			boolean zoomFrame,
			double zoomingCoefficient) {
		if (targetTemperatures==null) {
			return null;
		};
		if (temperatureAutoranging) {
			useDoubleColorMap= false;
			double minimalTemperature= targetTemperatures[0];
			double maximalTemperature= targetTemperatures[0];
			for (int m=0; m < targetTemperatures.length; m++) {
				double t= targetTemperatures[m];
				if (t < minimalTemperature) {
					minimalTemperature= t;
				};
				if (t > maximalTemperature) {
					maximalTemperature= t;
				}
			};
			if (lowerTemperatureBound < upperTemperatureBound) {
				if (minimalTemperature < lowerTemperatureBound) {
					minimalTemperature= lowerTemperatureBound;
				};
				if (maximalTemperature > upperTemperatureBound) {
					maximalTemperature= upperTemperatureBound;
				}
			};
			lowerTemperatureQuantile1= minimalTemperature;
			upperTemperatureQuantile1= maximalTemperature;
		};
		int imageX0= 0;
		int imageY0= 0;
		int imageWidth= totalWidth;
		int imageHeight= totalHeight;
		if (zoomFrame) {
			int initialWidth= imageWidth;
			int initialHeight= imageHeight;
			imageWidth= (int)(imageWidth / zoomingCoefficient);
			imageHeight= (int)(imageHeight / zoomingCoefficient);
			if (imageWidth > initialWidth) {
				imageWidth= initialWidth;
			};
			if (imageHeight > initialHeight) {
				imageHeight= initialHeight;
			};
			imageX0= (int)((initialWidth - imageWidth) / 2);
			imageY0= (int)((initialHeight - imageHeight) / 2);
			if (imageX0 < 0) {
				imageX0= 0;
			};
			if (imageY0 < 0) {
				imageY0= 0;
			}
		};
		int imageSize= imageWidth*imageHeight;
		int[] red= new int[imageSize];
		int[] green= new int[imageSize];
		int[] blue= new int[imageSize];
		int[] alpha= new int[imageSize];
		int mainColorMapSize= 256;
		int auxiliaryColorMapSize= 256;
		if (mainColorMap != null && mainColorMap.length > 0) {
			mainColorMapSize= mainColorMap[0].length;
		};
		if (auxiliaryColorMap != null && auxiliaryColorMap.length > 0) {
			auxiliaryColorMapSize= auxiliaryColorMap[0].length;
		};
		if (useDoubleColorMap) {
			for (int w=0; w < imageWidth; w++) {
				for (int h=0; h < imageHeight; h++) {
					int pos0= (h + imageY0) * totalWidth + w + imageX0;
					int pos1= h * imageWidth + w;
					double t= targetTemperatures[pos0];
					if (lowerTemperatureQuantile2 <= t && t <= upperTemperatureQuantile2) {
						int mainIndex= (int)(mainColorMapSize * (t-lowerTemperatureQuantile2) / (upperTemperatureQuantile2-lowerTemperatureQuantile2));
						if (mainIndex < 0) {
							mainIndex= 0;
						} else if (mainIndex > mainColorMapSize-1) {
							mainIndex= mainColorMapSize-1;
						};
						if (mainColorMap != null) {
							red[pos1]= mainColorMap[0][mainIndex];
							green[pos1]= mainColorMap[1][mainIndex];
							blue[pos1]= mainColorMap[2][mainIndex];
							alpha[pos1]= mainColorMap[3][mainIndex];
						} else {
							red[pos1]= 255 - (int)(255.0 * (t - lowerTemperatureQuantile2) / (upperTemperatureQuantile2 - lowerTemperatureQuantile2));
							green[pos1]= (int)(255.0 * (t - lowerTemperatureQuantile2) / (upperTemperatureQuantile2 - lowerTemperatureQuantile2));
							blue[pos1]= 255 - (int)(255.0 * (t - lowerTemperatureQuantile2) / (upperTemperatureQuantile2 - lowerTemperatureQuantile2));
							alpha[pos1]= 255;
						}
					} else {
						if (t > upperTemperatureQuantile1) {
							t= upperTemperatureQuantile1;
						} else if (t < lowerTemperatureQuantile1) {
							t= lowerTemperatureQuantile1;
						};
						int auxiliaryIndex= (int)(auxiliaryColorMapSize * (t-lowerTemperatureQuantile1) / (upperTemperatureQuantile1-lowerTemperatureQuantile1));
						if (auxiliaryIndex < 0) {
							auxiliaryIndex= 0;
						} else if (auxiliaryIndex > auxiliaryColorMapSize-1) {
							auxiliaryIndex= auxiliaryColorMapSize-1;
						};
						if (auxiliaryColorMap != null) {
							red[pos1]= auxiliaryColorMap[0][auxiliaryIndex];
							green[pos1]= auxiliaryColorMap[1][auxiliaryIndex];
							blue[pos1]= auxiliaryColorMap[2][auxiliaryIndex];
							alpha[pos1]= auxiliaryColorMap[3][auxiliaryIndex];
						} else {
							red[pos1]= 255 - (int)(255.0 * (t - lowerTemperatureQuantile1) / (upperTemperatureQuantile1 - lowerTemperatureQuantile1));
							green[pos1]= (int)(255.0 * (t - lowerTemperatureQuantile1) / (upperTemperatureQuantile1 - lowerTemperatureQuantile1));
							blue[pos1]= 255 - (int)(255.0 * (t - lowerTemperatureQuantile1) / (upperTemperatureQuantile1 - lowerTemperatureQuantile1));
							alpha[pos1]= 255;
						}
						/*
						int grey= (int)(255.0 * (t - lowerTemperatureQuantile1) / (upperTemperatureQuantile1 - lowerTemperatureQuantile1));
						red[pos1]= grey;
						green[pos1]= grey;
						blue[pos1]= grey;
						alpha[pos1]= 255;
						*/
					}
				}
			}
		} else {
			for (int w=0; w < imageWidth; w++) {
				for (int h=0; h < imageHeight; h++) {
					int pos0= (h + imageY0) * totalWidth + w + imageX0;
					int pos1= h * imageWidth + w;
					double t= targetTemperatures[pos0];
					if (t > upperTemperatureQuantile1) {
						t= upperTemperatureQuantile1;
					} else if (t < lowerTemperatureQuantile1) {
						t= lowerTemperatureQuantile1;
					};
					int index= (int)(mainColorMapSize * (t-lowerTemperatureQuantile1) / (upperTemperatureQuantile1-lowerTemperatureQuantile1));
					if (index < 0) {
						index= 0;
					} else if (index > mainColorMapSize-1) {
						index= mainColorMapSize-1;
					};
					if (mainColorMap != null) {
						red[pos1]= mainColorMap[0][index];
						green[pos1]= mainColorMap[1][index];
						blue[pos1]= mainColorMap[2][index];
						alpha[pos1]= mainColorMap[3][index];
					} else {
						red[pos1]= 255 - (int)(255.0 * (t - lowerTemperatureQuantile2) / (upperTemperatureQuantile2 - lowerTemperatureQuantile2));
						green[pos1]= (int)(255.0 * (t - lowerTemperatureQuantile2) / (upperTemperatureQuantile2 - lowerTemperatureQuantile2));
						blue[pos1]= 255 - (int)(255.0 * (t - lowerTemperatureQuantile2) / (upperTemperatureQuantile2 - lowerTemperatureQuantile2));
						alpha[pos1]= 255;
					}
				}
			}
		};
		java.awt.image.BufferedImage image= new java.awt.image.BufferedImage(imageWidth,imageHeight,java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster imageRaster= image.getRaster();
		imageRaster.setSamples(0,0,imageWidth,imageHeight,0,red);
		imageRaster.setSamples(0,0,imageWidth,imageHeight,1,green);
		imageRaster.setSamples(0,0,imageWidth,imageHeight,2,blue);
		// imageRaster.setSamples(0,0,imageWidth,imageHeight,3,alpha);
		return new AttachedImage(image,imageX0,imageY0);
	}
	//
	public static java.awt.image.BufferedImage zoomImage(
			java.awt.image.BufferedImage sourceImage,
			boolean zoomFrame,
			double zoomingCoefficient) {
		if (zoomFrame) {
			int imageWidth= sourceImage.getWidth();
			int imageHeight= sourceImage.getHeight();
			int imageType= sourceImage.getType();
			if (imageType==java.awt.image.BufferedImage.TYPE_CUSTOM) {
				imageType= java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
			};
			java.awt.image.BufferedImage zoomedImage= new java.awt.image.BufferedImage(
				imageWidth,
				imageHeight,
				imageType);
			int x2= (int)(imageWidth * (zoomingCoefficient-1) / zoomingCoefficient / 2);
			int y2= (int)(imageHeight * (zoomingCoefficient-1) / zoomingCoefficient / 2);
			if (x2 > 0 && y2 > 0) {
				Graphics2D g22= (Graphics2D)zoomedImage.getGraphics();
				try {
					g22.drawImage(
						sourceImage,
						0,
						0,
						imageWidth,
						imageHeight,
						x2,
						y2,
						imageWidth - x2,
						imageHeight - y2,
						null);
				} finally {
					g22.dispose();
				};
				sourceImage= zoomedImage;
			}
		} else {
			sourceImage= correctImage(sourceImage);
		};
		return sourceImage;
	}
	//
	public static java.awt.image.BufferedImage correctImage(
			java.awt.image.BufferedImage sourceImage) {
		int imageType= sourceImage.getType();
		if (imageType==java.awt.image.BufferedImage.TYPE_CUSTOM) {
			int imageWidth= sourceImage.getWidth();
			int imageHeight= sourceImage.getHeight();
			java.awt.image.BufferedImage correctedImage= new java.awt.image.BufferedImage(
				imageWidth,
				imageHeight,
				java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g22= (Graphics2D)correctedImage.getGraphics();
			try {
				g22.drawImage(sourceImage,0,0,null);
			} finally {
				g22.dispose();
			};
			return correctedImage;
		} else {
			return sourceImage;
		}
	}
	//
	public static java.awt.image.BufferedImage rotateAndZoomImage(
			java.awt.image.BufferedImage sourceImage,
			boolean zoomFrame,
			double zoomingCoefficient) {
		int imageWidth= sourceImage.getWidth();
		int imageHeight= sourceImage.getHeight();
		int imageType= sourceImage.getType();
		if (imageType==java.awt.image.BufferedImage.TYPE_CUSTOM) {
			imageType= java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
		};
		AffineTransform xform= AffineTransform.getQuadrantRotateInstance(-1,imageHeight/2.0,imageWidth/2.0);
		double shiftX= - (imageWidth/2.0 - imageHeight/2.0);
		double shiftY= imageWidth/2.0 - imageHeight/2.0;
		xform.translate(shiftX,shiftY);
		java.awt.image.BufferedImage rotatedImage= new java.awt.image.BufferedImage(
			imageHeight,
			imageWidth,
			imageType);
		Graphics2D g2= (Graphics2D)rotatedImage.getGraphics();
		try {
			g2.drawImage(sourceImage,xform,null);
		} finally {
			g2.dispose();
		};
		if (zoomFrame) {
			java.awt.image.BufferedImage zoomedImage= new java.awt.image.BufferedImage(
				imageHeight,
				imageWidth,
				imageType);
			int x2= (int)(imageHeight * (zoomingCoefficient-1) / zoomingCoefficient / 2);
			int y2= (int)(imageWidth * (zoomingCoefficient-1) / zoomingCoefficient / 2);
			if (x2 > 0 && y2 > 0) {
				Graphics2D g22= (Graphics2D)zoomedImage.getGraphics();
				try {
					g22.drawImage(
						rotatedImage,
						0,
						0,
						imageHeight,
						imageWidth,
						x2,
						y2,
						imageHeight - x2,
						imageWidth - y2,
						null);
				} finally {
					g22.dispose();
				};
				rotatedImage= zoomedImage;
			}
		};
		return rotatedImage;
	}
	//
	public static java.awt.image.BufferedImage rotateAugmentAndZoomImage(
			java.awt.image.BufferedImage sourceImage,
			double[] targetTemperatures,
			int matrixWidth,
			int matrixHeight,
			int subimageWidth,
			int subimageHeight,
			int offsetX,
			int offsetY,
			int[][] mainColorMap,
			int[][] auxiliaryColorMap,
			boolean temperatureAutoranging,
			boolean useDoubleColorMap,
			double lowerTemperatureBound,
			double upperTemperatureBound,
			double lowerTemperatureQuantile1,
			double upperTemperatureQuantile1,
			double lowerTemperatureQuantile2,
			double upperTemperatureQuantile2,
			boolean zoomFrame,
			double zoomingCoefficient) {
		int colorImageWidth= sourceImage.getWidth();
		int colorImageHeight= sourceImage.getHeight();
		int imageType= sourceImage.getType();
		if (imageType==java.awt.image.BufferedImage.TYPE_CUSTOM) {
			imageType= java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
		};
		AffineTransform xform= AffineTransform.getQuadrantRotateInstance(-1,colorImageHeight/2.0,colorImageWidth/2.0);
		double shiftX= - (colorImageWidth/2.0 - colorImageHeight/2.0);
		double shiftY= colorImageWidth/2.0 - colorImageHeight/2.0;
		xform.translate(shiftX,shiftY);
		AttachedImage attachedImage= temperaturesToImage(
			targetTemperatures,
			matrixWidth,
			matrixHeight,
			mainColorMap,
			auxiliaryColorMap,
			temperatureAutoranging,
			useDoubleColorMap,
			lowerTemperatureBound,
			upperTemperatureBound,
			lowerTemperatureQuantile1,
			upperTemperatureQuantile1,
			lowerTemperatureQuantile2,
			upperTemperatureQuantile2,
			false, // zoomFrame
			zoomingCoefficient);
		java.awt.image.BufferedImage terahertzImage= attachedImage.getImage();
		java.awt.image.BufferedImage rotatedImage= new java.awt.image.BufferedImage(
			colorImageHeight,
			colorImageWidth,
			imageType);
		Graphics2D g21= (Graphics2D)rotatedImage.getGraphics();
		try {
			g21.drawImage(sourceImage,xform,null);
			g21.drawImage(terahertzImage,offsetX,offsetY,subimageWidth,subimageHeight,null);
		} finally {
			g21.dispose();
		};
		if (zoomFrame) {
			java.awt.image.BufferedImage zoomedImage= new java.awt.image.BufferedImage(
				colorImageHeight,
				colorImageWidth,
				imageType);
			int roiWidth= (int)(colorImageHeight / zoomingCoefficient);
			int roiHeight= (int)(colorImageWidth / zoomingCoefficient);
			int x2= (colorImageHeight - roiWidth) / 2;
			int y2= (colorImageWidth - roiHeight) / 2;
			Graphics2D g22= (Graphics2D)zoomedImage.getGraphics();
			try {
				g22.drawImage(
					rotatedImage,
					0,
					0,
					colorImageHeight,
					colorImageWidth,
					x2,
					y2,
					colorImageHeight - x2,
					colorImageWidth - y2,
					null);
			} finally {
				g22.dispose();
			};
			rotatedImage= zoomedImage;
		};
		return rotatedImage;
	}
	//
	public static java.awt.image.BufferedImage borderAndZoomImage(
			double[] targetTemperatures,
			int matrixWidth,
			int matrixHeight,
			int subimageWidth,
			int subimageHeight,
			int offsetX,
			int offsetY,
			int[][] mainColorMap,
			int[][] auxiliaryColorMap,
			boolean temperatureAutoranging,
			boolean useDoubleColorMap,
			double lowerTemperatureBound,
			double upperTemperatureBound,
			double lowerTemperatureQuantile1,
			double upperTemperatureQuantile1,
			double lowerTemperatureQuantile2,
			double upperTemperatureQuantile2,
			boolean zoomFrame,
			double zoomingCoefficient,
			Color backgroundColor) {
		int colorImageWidth= defaultColorImageWidth;
		int colorImageHeight= defaultColorImageHeight;
		double shiftX= - (colorImageWidth/2.0 - colorImageHeight/2.0);
		double shiftY= colorImageWidth/2.0 - colorImageHeight/2.0;
		AttachedImage attachedImage= temperaturesToImage(
			targetTemperatures,
			matrixWidth,
			matrixHeight,
			mainColorMap,
			auxiliaryColorMap,
			temperatureAutoranging,
			useDoubleColorMap,
			lowerTemperatureBound,
			upperTemperatureBound,
			lowerTemperatureQuantile1,
			upperTemperatureQuantile1,
			lowerTemperatureQuantile2,
			upperTemperatureQuantile2,
			false, // zoomFrame
			zoomingCoefficient);
		java.awt.image.BufferedImage terahertzImage= attachedImage.getImage();
		java.awt.image.BufferedImage rotatedImage;
		if (backgroundColor==null) {
			rotatedImage= new java.awt.image.BufferedImage(
				colorImageHeight,
				colorImageWidth,
				java.awt.image.BufferedImage.TYPE_4BYTE_ABGR);
		} else {
			rotatedImage= new java.awt.image.BufferedImage(
				colorImageHeight,
				colorImageWidth,
				java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
		};
		Graphics2D g21= (Graphics2D)rotatedImage.getGraphics();
		try {
			if (backgroundColor==null) {
				g21.setBackground(transparentBackground);
			} else {
				g21.setBackground(backgroundColor);
			};
			g21.clearRect(0,0,colorImageHeight,colorImageWidth);
			g21.drawImage(terahertzImage,offsetX,offsetY,subimageWidth,subimageHeight,null);
		} finally {
			g21.dispose();
		};
		if (zoomFrame) {
			java.awt.image.BufferedImage zoomedImage;
			if (backgroundColor==null) {
				zoomedImage= new java.awt.image.BufferedImage(
					colorImageHeight,
					colorImageWidth,
					java.awt.image.BufferedImage.TYPE_4BYTE_ABGR);
			} else {
				zoomedImage= new java.awt.image.BufferedImage(
					colorImageHeight,
					colorImageWidth,
					java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
			};
			int roiWidth= (int)(colorImageHeight / zoomingCoefficient);
			int roiHeight= (int)(colorImageWidth / zoomingCoefficient);
			int x2= (colorImageHeight - roiWidth) / 2;
			int y2= (colorImageWidth - roiHeight) / 2;
			Graphics2D g22= (Graphics2D)zoomedImage.getGraphics();
			try {
				g22.drawImage(
					rotatedImage,
					0,
					0,
					colorImageHeight,
					colorImageWidth,
					x2,
					y2,
					colorImageHeight - x2,
					colorImageWidth - y2,
					null);
			} finally {
				g22.dispose();
			};
			rotatedImage= zoomedImage;
		};
		return rotatedImage;
	}
}
