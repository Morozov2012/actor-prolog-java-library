// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.tools;

import morozov.system.kinect.frames.data.tools.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.errors.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.interfaces.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.Graphics2D;

public class FrameDrawingUtils extends FrameDrawingBasics {
	//
	public static final int coincedenceThreshold= 100;
	//
/////////////////////////////////////////////////////////////////////
// Kinect Frame -> Image                                           //
/////////////////////////////////////////////////////////////////////
	public static java.awt.image.BufferedImage getImage(KinectFrameInterface frame, KinectDisplayingModeInterface displayingMode, KinectColorMap colorMap, KinectColorMap peopleColors) {
		KinectPeopleIndexMode peopleIndexMode= displayingMode.getActingPeopleIndexMode();
		int maximalNumberOfSkeletons= frame.getNumberOfSkeletons();
		if (frame instanceof KinectColorFrameInterface) {
			KinectColorFrameInterface colorFrame= (KinectColorFrameInterface)frame;
			return colorFrameToBufferedImage(colorFrame.getColor(),colorFrame.getU(),colorFrame.getV(),peopleIndexMode,colorFrame.getPlayerIndex(),true,colorMap,peopleColors,maximalNumberOfSkeletons);
		} else if (frame instanceof KinectDepthFrameInterface) {
			KinectDepthFrameInterface depthFrame= (KinectDepthFrameInterface)frame;
			if (displayingMode.getActingFrameType() == KinectFrameType.DEPTH_MAPS) {
				return shortArrayToGrayBufferedImage(depthFrame.getDepth(),true,peopleIndexMode,depthFrame.getPlayerIndex(),true,depthFrame.getMappedRed(),depthFrame.getMappedGreen(),depthFrame.getMappedBlue(),colorMap,peopleColors,maximalNumberOfSkeletons);
			} else if (displayingMode.getActingFrameType() == KinectFrameType.COLORED_DEPTH_MAPS) {
				return shortArrayToColouredBufferedImage(depthFrame.getDepth(),true,peopleIndexMode,depthFrame.getPlayerIndex(),true,depthFrame.getMappedRed(),depthFrame.getMappedGreen(),depthFrame.getMappedBlue(),colorMap,peopleColors,maximalNumberOfSkeletons);
			} else {
				return shortArrayToEmptyBufferedImage(depthFrame.getDepth(),peopleIndexMode,depthFrame.getPlayerIndex(),false,depthFrame.getMappedRed(),depthFrame.getMappedGreen(),depthFrame.getMappedBlue(),peopleColors,maximalNumberOfSkeletons);
			}
		// } else if (frame instanceof KinectForegroundPointCloudsFrameInterface) {
		// } else if (frame instanceof KinectFrameInterface) {
		} else if (frame instanceof KinectInfraredFrameInterface) {
			KinectInfraredFrameInterface infraredFrame= (KinectInfraredFrameInterface)frame;
			return shortArrayToGrayBufferedImage(infraredFrame.getInfrared(),true,peopleIndexMode,infraredFrame.getPlayerIndex(),false,infraredFrame.getMappedRed(),infraredFrame.getMappedGreen(),infraredFrame.getMappedBlue(),colorMap,peopleColors,maximalNumberOfSkeletons);
		} else if (frame instanceof KinectLongExposureInfraredFrameInterface) {
			KinectLongExposureInfraredFrameInterface longExposureInfraredFrame= (KinectLongExposureInfraredFrameInterface)frame;
			return shortArrayToGrayBufferedImage(longExposureInfraredFrame.getLongExposureInfrared(),true,peopleIndexMode,longExposureInfraredFrame.getPlayerIndex(),false,longExposureInfraredFrame.getMappedRed(),longExposureInfraredFrame.getMappedGreen(),longExposureInfraredFrame.getMappedBlue(),colorMap,peopleColors,maximalNumberOfSkeletons);
		} else if (frame instanceof KinectMappedColorFrameInterface) {
			KinectMappedColorFrameInterface mappedColorFrame= (KinectMappedColorFrameInterface)frame;
			return colorFrameToMappedImage(mappedColorFrame.getMappedRed(),mappedColorFrame.getMappedGreen(),mappedColorFrame.getMappedBlue(),peopleIndexMode,mappedColorFrame.getPlayerIndex(),false,colorMap,peopleColors,maximalNumberOfSkeletons);
		// } else if (frame instanceof KinectModeFrameInterface) {
		} else if (frame instanceof KinectPointCloudsFrameInterface) {
			KinectPointCloudsFrameInterface pointCloudsFrame= (KinectPointCloudsFrameInterface)frame;
			if (displayingMode.getActingFrameType() == KinectFrameType.DEVICE_TUNING) {
				return tuneProgramAndCreateMappedBufferedImage(pointCloudsFrame.getXYZ(),pointCloudsFrame.getMappedRed(),pointCloudsFrame.getMappedGreen(),pointCloudsFrame.getMappedBlue(),peopleIndexMode,pointCloudsFrame.getPlayerIndex(),false,pointCloudsFrame.getFocalLengthX(),pointCloudsFrame.getFocalLengthY(),pointCloudsFrame.getCorrectionX(),pointCloudsFrame.getCorrectionY()).getImage();
			} else {
				return pointCloudToMappedBufferedImage(pointCloudsFrame.getXYZ(),pointCloudsFrame.getMappedRed(),pointCloudsFrame.getMappedGreen(),pointCloudsFrame.getMappedBlue(),peopleIndexMode,pointCloudsFrame.getPlayerIndex(),false,pointCloudsFrame.getFocalLengthX(),pointCloudsFrame.getFocalLengthY(),pointCloudsFrame.getCorrectionX(),pointCloudsFrame.getCorrectionY(),colorMap,peopleColors,maximalNumberOfSkeletons);
			}
		} else {
			throw new FrameHasNoVisualRepresentation(frame);
		}
	}
/////////////////////////////////////////////////////////////////////
// Short Array to Gray Buffered Image                              //
// (tinctureRatio4)                                                //
/////////////////////////////////////////////////////////////////////
	public static BufferedImage shortArrayToGrayBufferedImage(short[] array, boolean computeQuantiles, KinectPeopleIndexMode currentPeopleIndexMode, byte[] playerIndex, boolean useWhiteBackground, byte[][] red1, byte[][] green1, byte[][] blue1, KinectColorMap colorMap, KinectColorMap peopleColors, int maximalNumberOfSkeletons) {
		if (playerIndex==null) {
			currentPeopleIndexMode= KinectPeopleIndexMode.NONE;
		};
		BufferedImage image;
		if (	currentPeopleIndexMode==KinectPeopleIndexMode.TINCTURE_PEOPLE ||
			currentPeopleIndexMode==KinectPeopleIndexMode.PAINT_PEOPLE ||
			currentPeopleIndexMode==KinectPeopleIndexMode.PROJECT_PEOPLE) {
			image= shortArrayToBlankColouredBufferedImage(array);
			if (image==null) {
				return null;
			};
			int[][] peopleColorMatrix= peopleColors.toRepeatedColors(maximalNumberOfSkeletons);
			int[] peopleIndexRed= peopleColorMatrix[0];
			int[] peopleIndexGreen= peopleColorMatrix[1];
			int[] peopleIndexBlue= peopleColorMatrix[2];
			double colormapTincturingCoefficient= colorMap.getColorMapTincturingCoefficient();
			double peopleColorsTincturingCoefficient= peopleColors.getPeopleColorsTincturingCoefficient();
			Quantiles quantiles;
			if (computeQuantiles) {
				if (currentPeopleIndexMode==KinectPeopleIndexMode.ADAPTIVELY_EXTRACT_PEOPLE) {
					quantiles= Quantiles.computeQuantiles(array,playerIndex);
				} else {
					quantiles= Quantiles.computeQuantiles(array,true);
				}
			} else {
				quantiles= Quantiles.computeBounds(array);
			};
			if (currentPeopleIndexMode==KinectPeopleIndexMode.PROJECT_PEOPLE) {
				if (red1==null || green1==null || blue1==null) {
					currentPeopleIndexMode= KinectPeopleIndexMode.PAINT_PEOPLE;
				}
			};
			int frameLength= array.length;
			int imageWidth= image.getWidth();
			int imageHeight= image.getHeight();
			int bandLength= imageWidth * imageHeight;
			int[] red2= new int[bandLength];
			int[] green2= new int[bandLength];
			int[] blue2= new int[bandLength];
			int counter1= -1;
			for (int h=0; h < imageHeight; h++) {
				for (int w=0; w < imageWidth; w++) {
					counter1++;
					int counter2= h*imageWidth + (imageWidth-w-1);
					int index= (int)playerIndex[counter1];
					if (index >= 0) {
						if (index > peopleIndexRed.length) {
							index= peopleIndexRed.length;
						};
						if (currentPeopleIndexMode==KinectPeopleIndexMode.TINCTURE_PEOPLE) {
							//
//-----------------------------------------------------------------//
// Tincture People                                                 //
//-----------------------------------------------------------------//
//
int value= (int)(quantiles.standardize(array[counter1]) * maxColorValue);
if (value > maxColorValue) {
	value= maxColorValue;
} if (value < 0) {
	value= 0;
};
int redValue= value;
int greenValue= value;
int blueValue= value;
int previousMaxValue= redValue;
if (previousMaxValue < greenValue) {
	previousMaxValue= greenValue;
};
if (previousMaxValue < blueValue) {
	previousMaxValue= blueValue;
};
redValue= (int)(redValue / colormapTincturingCoefficient + peopleIndexRed[index] / peopleColorsTincturingCoefficient);
greenValue= (int)(greenValue / colormapTincturingCoefficient + peopleIndexGreen[index] / peopleColorsTincturingCoefficient);
blueValue= (int)(blueValue / colormapTincturingCoefficient + peopleIndexBlue[index] / peopleColorsTincturingCoefficient);
int newMaxValue= redValue;
if (newMaxValue < greenValue) {
	newMaxValue= greenValue;
};
if (newMaxValue < blueValue) {
	newMaxValue= blueValue;
};
if (newMaxValue > 0) {
	redValue= redValue * previousMaxValue / newMaxValue;
	greenValue= greenValue * previousMaxValue / newMaxValue;
	blueValue= blueValue * previousMaxValue / newMaxValue;
};
if (redValue > maxColorValue) {
	redValue= maxColorValue;
};
if (greenValue > maxColorValue) {
	greenValue= maxColorValue;
};
if (blueValue > maxColorValue) {
	blueValue= maxColorValue;
};
red2[counter2]= redValue;
green2[counter2]= greenValue;
blue2[counter2]= blueValue;
//
//-----------------------------------------------------------------//
							//
						} else if (currentPeopleIndexMode==KinectPeopleIndexMode.PROJECT_PEOPLE) {
							//
//-----------------------------------------------------------------//
// Project People                                                  //
//-----------------------------------------------------------------//
//
int counter3= h*imageWidth + (imageWidth-w-1);
red2[counter3]= byte2int(red1[w][h]);
green2[counter3]= byte2int(green1[w][h]);
blue2[counter3]= byte2int(blue1[w][h]);
//
//-----------------------------------------------------------------//
							//
						} else {
							red2[counter2]= peopleIndexRed[index];
							green2[counter2]= peopleIndexGreen[index];
							blue2[counter2]= peopleIndexBlue[index];
						}
					} else {
						int value= (int)(quantiles.standardize(array[counter1])*maxColorValue);
						if (value > maxColorValue) {
							value= maxColorValue;
						} if (value < 0) {
							value= 0;
						};
						red2[counter2]= value;
						green2[counter2]= value;
						blue2[counter2]= value;
					}
				}
			};
			WritableRaster imageRaster= image.getRaster();
			imageRaster.setSamples(0,0,imageWidth,imageHeight,0,red2);
			imageRaster.setSamples(0,0,imageWidth,imageHeight,1,green2);
			imageRaster.setSamples(0,0,imageWidth,imageHeight,2,blue2);
		} else {
			image= shortArrayToBlankGrayBufferedImage(array);
			if (image==null) {
				return null;
			};
			Quantiles quantiles;
			if (computeQuantiles) {
				if (currentPeopleIndexMode==KinectPeopleIndexMode.ADAPTIVELY_EXTRACT_PEOPLE) {
					quantiles= Quantiles.computeQuantiles(array,playerIndex);
				} else {
					quantiles= Quantiles.computeQuantiles(array,true);
				}
			} else {
				quantiles= Quantiles.computeBounds(array);
			};
			int frameLength= array.length;
			int imageWidth= image.getWidth();
			int imageHeight= image.getHeight();
			double[] doubleNumberArray= new double[frameLength];
			int counter1= -1;
			for (int h=0; h < imageHeight; h++) {
				for (int w=0; w < imageWidth; w++) {
					counter1++;
					int counter2= h*imageWidth + (imageWidth-w-1);
					if (currentPeopleIndexMode==KinectPeopleIndexMode.NONE) {
						int value= (int)(quantiles.standardize(array[counter1])*maxColorValue);
						if (value > maxColorValue) {
							value= maxColorValue;
						} else if (value < 0) {
							value= 0;
						};
						doubleNumberArray[counter2]= value;
					} else {
						int index= (int)playerIndex[counter1];
						if (index >= 0) {
							int value= (int)(quantiles.standardize(array[counter1])*maxColorValue);
							if (value > maxColorValue) {
								value= maxColorValue;
							} else if (value < 0) {
								value= 0;
							};
							doubleNumberArray[counter2]= value;
						} else if (useWhiteBackground) {
							doubleNumberArray[counter2]= maxColorValue;
						}
					}
				}
			};
			WritableRaster imageRaster= image.getRaster();
			imageRaster.setSamples(0,0,imageWidth,imageHeight,0,doubleNumberArray);
		};
		return image;
	}
/////////////////////////////////////////////////////////////////////
// Short Array to Coloured Buffered Image                          //
// (tinctureRatio2)                                                //
/////////////////////////////////////////////////////////////////////
	public static BufferedImage shortArrayToColouredBufferedImage(short[] array, boolean computeQuantiles, KinectPeopleIndexMode currentPeopleIndexMode, byte[] playerIndex, boolean useWhiteBackground, byte[][] red1, byte[][] green1, byte[][] blue1, KinectColorMap colorMap, KinectColorMap peopleColors, int maximalNumberOfSkeletons) {
		if (playerIndex==null) {
			currentPeopleIndexMode= KinectPeopleIndexMode.NONE;
		};
		BufferedImage image= shortArrayToBlankColouredBufferedImage(array);
		if (image==null) {
			return null;
		};
		int[][] colors= colorMap.toColors();
		int[] jetRed= colors[0];
		int[] jetGreen= colors[1];
		int[] jetBlue= colors[2];
		int jetLength= jetRed.length;
		int[][] peopleColorMatrix= peopleColors.toRepeatedColors(maximalNumberOfSkeletons);
		int[] peopleIndexRed= peopleColorMatrix[0];
		int[] peopleIndexGreen= peopleColorMatrix[1];
		int[] peopleIndexBlue= peopleColorMatrix[2];
		double colormapTincturingCoefficient= colorMap.getColorMapTincturingCoefficient();
		double peopleColorsTincturingCoefficient= peopleColors.getPeopleColorsTincturingCoefficient();
		Quantiles quantiles;
		if (computeQuantiles) {
			if (currentPeopleIndexMode==KinectPeopleIndexMode.ADAPTIVELY_EXTRACT_PEOPLE) {
				quantiles= Quantiles.computeQuantiles(array,playerIndex);
			} else {
				quantiles= Quantiles.computeQuantiles(array,true);
			}
		} else {
			quantiles= Quantiles.computeBounds(array);
		};
		if (currentPeopleIndexMode==KinectPeopleIndexMode.PROJECT_PEOPLE) {
			if (red1==null || green1==null || blue1==null) {
				currentPeopleIndexMode= KinectPeopleIndexMode.PAINT_PEOPLE;
			}
		};
		int frameLength= array.length;
		int imageWidth= image.getWidth();
		int imageHeight= image.getHeight();
		int bandLength= imageWidth * imageHeight;
		int[] red2= new int[bandLength];
		int[] green2= new int[bandLength];
		int[] blue2= new int[bandLength];
		int counter1= -1;
		for (int h=0; h < imageHeight; h++) {
			for (int w=0; w < imageWidth; w++) {
				counter1++;
				int counter2= h*imageWidth + (imageWidth-w-1);
				int value= array[counter1];
				int i= (int)(quantiles.standardize(value)*(jetLength-1));
				// i= jetLength - 1 - i;
				if (i >= jetLength) {
					i= jetLength-1;
				} else if (i < 0) {
					i= 0;
				};
				if (currentPeopleIndexMode==KinectPeopleIndexMode.TINCTURE_PEOPLE) {
					//
//-----------------------------------------------------------------//
// Tincture People                                                 //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[counter1];
if (index >= 0) {
	if (index > peopleIndexRed.length) {
		index= peopleIndexRed.length;
	};
	int redValue= jetRed[i];
	int greenValue= jetGreen[i];
	int blueValue= jetBlue[i];
	int previousMaxValue= redValue;
	if (previousMaxValue < greenValue) {
		previousMaxValue= greenValue;
	};
	if (previousMaxValue < blueValue) {
		previousMaxValue= blueValue;
	};
	redValue= (int)(redValue / colormapTincturingCoefficient + peopleIndexRed[index] / peopleColorsTincturingCoefficient);
	greenValue= (int)(greenValue / colormapTincturingCoefficient + peopleIndexGreen[index] / peopleColorsTincturingCoefficient);
	blueValue= (int)(blueValue / colormapTincturingCoefficient + peopleIndexBlue[index] / peopleColorsTincturingCoefficient);
	int newMaxValue= redValue;
	if (newMaxValue < greenValue) {
		newMaxValue= greenValue;
	};
	if (newMaxValue < blueValue) {
		newMaxValue= blueValue;
	};
	if (newMaxValue > 0) {
		redValue= redValue * previousMaxValue / newMaxValue;
		greenValue= greenValue * previousMaxValue / newMaxValue;
		blueValue= blueValue * previousMaxValue / newMaxValue;
	};
	if (redValue > maxColorValue) {
		redValue= maxColorValue;
	};
	if (greenValue > maxColorValue) {
		greenValue= maxColorValue;
	};
	if (blueValue > maxColorValue) {
		blueValue= maxColorValue;
	};
	red2[counter2]= redValue;
	green2[counter2]= greenValue;
	blue2[counter2]= blueValue;
} else {
	red2[counter2]= jetRed[i];
	green2[counter2]= jetGreen[i];
	blue2[counter2]= jetBlue[i];
}
//
//-----------------------------------------------------------------//
					//
				} else if (currentPeopleIndexMode==KinectPeopleIndexMode.PAINT_PEOPLE) {
					//
//-----------------------------------------------------------------//
// Paint People                                                    //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[counter1];
if (index >= 0) {
	if (index > peopleIndexRed.length) {
		index= peopleIndexRed.length;
	};
	red2[counter2]= peopleIndexRed[index];
	green2[counter2]= peopleIndexGreen[index];
	blue2[counter2]= peopleIndexBlue[index];
} else {
	red2[counter2]= jetRed[i];
	green2[counter2]= jetGreen[i];
	blue2[counter2]= jetBlue[i];
}
//
//-----------------------------------------------------------------//
					//
				} else if (currentPeopleIndexMode==KinectPeopleIndexMode.EXTRACT_PEOPLE) {
					//
//-----------------------------------------------------------------//
// Extract People                                                  //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[counter1];
if (index >= 0) {
	red2[counter2]= jetRed[i];
	green2[counter2]= jetGreen[i];
	blue2[counter2]= jetBlue[i];
} else if (useWhiteBackground) {
	red2[counter2]= maxColorValue;
	green2[counter2]= maxColorValue;
	blue2[counter2]= maxColorValue;
}
//
//-----------------------------------------------------------------//
					//
				} else if (currentPeopleIndexMode==KinectPeopleIndexMode.ADAPTIVELY_EXTRACT_PEOPLE) {
					//
//-----------------------------------------------------------------//
// Adaptively Extract People                                       //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[counter1];
if (index >= 0) {
	red2[counter2]= jetRed[i];
	green2[counter2]= jetGreen[i];
	blue2[counter2]= jetBlue[i];
} else if (useWhiteBackground) {
	red2[counter2]= maxColorValue;
	green2[counter2]= maxColorValue;
	blue2[counter2]= maxColorValue;
}
//
//-----------------------------------------------------------------//
					//
				} else if (currentPeopleIndexMode==KinectPeopleIndexMode.PROJECT_PEOPLE) {
					//
//-----------------------------------------------------------------//
// Project People                                                  //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[counter1];
if (index >= 0) {
	int counter3= h*imageWidth + (imageWidth-w-1);
	red2[counter3]= byte2int(red1[w][h]);
	green2[counter3]= byte2int(green1[w][h]);
	blue2[counter3]= byte2int(blue1[w][h]);
} else {
	red2[counter2]= jetRed[i];
	green2[counter2]= jetGreen[i];
	blue2[counter2]= jetBlue[i];
}
//
//-----------------------------------------------------------------//
					//
				} else {
					red2[counter2]= jetRed[i];
					green2[counter2]= jetGreen[i];
					blue2[counter2]= jetBlue[i];
				}
			}
		};
		WritableRaster imageRaster= image.getRaster();
		imageRaster.setSamples(0,0,imageWidth,imageHeight,0,red2);
		imageRaster.setSamples(0,0,imageWidth,imageHeight,1,green2);
		imageRaster.setSamples(0,0,imageWidth,imageHeight,2,blue2);
		return image;
	}
/////////////////////////////////////////////////////////////////////
// Color Frame to Mapped Image                                     //
// (tinctureRatio2)                                                //
/////////////////////////////////////////////////////////////////////
	public static BufferedImage colorFrameToMappedImage(byte[][] red1, byte[][] green1, byte[][] blue1, KinectPeopleIndexMode currentPeopleIndexMode, byte[] playerIndex, boolean useWhiteBackground, KinectColorMap colorMap, KinectColorMap peopleColors, int maximalNumberOfSkeletons) {
		if (red1==null || green1==null || blue1==null) {
			return null;
		};
		if (playerIndex==null) {
			currentPeopleIndexMode= KinectPeopleIndexMode.NONE;
		};
		int uvWidth= red1.length;
		if (uvWidth <= 0) {
			return null;
		};
		int uvHeight= red1[0].length;
		int frameLength= uvWidth * uvHeight;
		int[] red2= new int[frameLength];
		int[] green2= new int[frameLength];
		int[] blue2= new int[frameLength];
		int[][] peopleColorMatrix= peopleColors.toRepeatedColors(maximalNumberOfSkeletons);
		int[] peopleIndexRed= peopleColorMatrix[0];
		int[] peopleIndexGreen= peopleColorMatrix[1];
		int[] peopleIndexBlue= peopleColorMatrix[2];
		double colormapTincturingCoefficient= colorMap.getColorMapTincturingCoefficient();
		double peopleColorsTincturingCoefficient= peopleColors.getPeopleColorsTincturingCoefficient();
		int counter1= -1;
		for (int h=0; h < uvHeight; h++) {
			for (int w=0; w < uvWidth; w++) {
				counter1++;
				int counter2= h*uvWidth + (uvWidth-w-1);
				if (currentPeopleIndexMode==KinectPeopleIndexMode.TINCTURE_PEOPLE) {
					//
//-----------------------------------------------------------------//
// Tincture People                                                 //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[counter1];
if (index >= 0) {
	if (index > peopleIndexRed.length) {
		index= peopleIndexRed.length;
	};
	int redValue= byte2int(red1[w][h]);
	int greenValue= byte2int(green1[w][h]);
	int blueValue= byte2int(blue1[w][h]);
	int previousMaxValue= redValue;
	if (previousMaxValue < greenValue) {
		previousMaxValue= greenValue;
	};
	if (previousMaxValue < blueValue) {
		previousMaxValue= blueValue;
	};
	redValue= (int)(redValue / colormapTincturingCoefficient + peopleIndexRed[index] / peopleColorsTincturingCoefficient);
	greenValue= (int)(greenValue / colormapTincturingCoefficient + peopleIndexGreen[index] / peopleColorsTincturingCoefficient);
	blueValue= (int)(blueValue / colormapTincturingCoefficient + peopleIndexBlue[index] / peopleColorsTincturingCoefficient);
	int newMaxValue= redValue;
	if (newMaxValue < greenValue) {
		newMaxValue= greenValue;
	};
	if (newMaxValue < blueValue) {
		newMaxValue= blueValue;
	};
	if (newMaxValue > 0) {
		redValue= redValue * previousMaxValue / newMaxValue;
		greenValue= greenValue * previousMaxValue / newMaxValue;
		blueValue= blueValue * previousMaxValue / newMaxValue;
	};
	if (redValue > maxColorValue) {
		redValue= maxColorValue;
	};
	if (greenValue > maxColorValue) {
		greenValue= maxColorValue;
	};
	if (blueValue > maxColorValue) {
		blueValue= maxColorValue;
	};
	red2[counter2]= redValue;
	green2[counter2]= greenValue;
	blue2[counter2]= blueValue;
} else {
	red2[counter2]= byte2int(red1[w][h]);
	green2[counter2]= byte2int(green1[w][h]);
	blue2[counter2]= byte2int(blue1[w][h]);
}
//
//-----------------------------------------------------------------//
					//
				} else if(currentPeopleIndexMode==KinectPeopleIndexMode.PAINT_PEOPLE) {
					//
//-----------------------------------------------------------------//
// Paint People                                                    //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[counter1];
if (index >= 0) {
	if (index > peopleIndexRed.length) {
		index= peopleIndexRed.length;
	};
	red2[counter2]= peopleIndexRed[index];
	green2[counter2]= peopleIndexGreen[index];
	blue2[counter2]= peopleIndexBlue[index];
} else {
	red2[counter2]= byte2int(red1[w][h]);
	green2[counter2]= byte2int(green1[w][h]);
	blue2[counter2]= byte2int(blue1[w][h]);
}
//
//-----------------------------------------------------------------//
					//
				} else if (currentPeopleIndexMode==KinectPeopleIndexMode.EXTRACT_PEOPLE) {
					//
//-----------------------------------------------------------------//
// Extract People                                                  //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[counter1];
if (index >= 0) {
	red2[counter2]= byte2int(red1[w][h]);
	green2[counter2]= byte2int(green1[w][h]);
	blue2[counter2]= byte2int(blue1[w][h]);
} else if (useWhiteBackground) {
	red2[counter2]= maxColorValue;
	green2[counter2]= maxColorValue;
	blue2[counter2]= maxColorValue;
}
//
//-----------------------------------------------------------------//
					//
				} else if (currentPeopleIndexMode==KinectPeopleIndexMode.ADAPTIVELY_EXTRACT_PEOPLE) {
					//
//-----------------------------------------------------------------//
// Adaptively Extract People                                       //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[counter1];
if (index >= 0) {
	red2[counter2]= byte2int(red1[w][h]);
	green2[counter2]= byte2int(green1[w][h]);
	blue2[counter2]= byte2int(blue1[w][h]);
} else if (useWhiteBackground) {
	red2[counter2]= maxColorValue;
	green2[counter2]= maxColorValue;
	blue2[counter2]= maxColorValue;
}
//
//-----------------------------------------------------------------//
					//
				} else {
					red2[counter2]= byte2int(red1[w][h]);
					green2[counter2]= byte2int(green1[w][h]);
					blue2[counter2]= byte2int(blue1[w][h]);
				}
			}
		};
		BufferedImage image= new BufferedImage(uvWidth,uvHeight,BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster imageRaster= image.getRaster();
		imageRaster.setSamples(0,0,uvWidth,uvHeight,0,red2);
		imageRaster.setSamples(0,0,uvWidth,uvHeight,1,green2);
		imageRaster.setSamples(0,0,uvWidth,uvHeight,2,blue2);
		return image;
	}
/////////////////////////////////////////////////////////////////////
// Point Cloud to Mapped Buffered Image                            //
// (tinctureRatio2)                                                //
/////////////////////////////////////////////////////////////////////
	public static BufferedImage pointCloudToMappedBufferedImage(float[] xyz, byte[][] red1, byte[][] green1, byte[][] blue1, KinectPeopleIndexMode currentPeopleIndexMode, byte[] playerIndex, boolean useWhiteBackground, float focalLengthX, float focalLengthY, int correctionX, int correctionY, KinectColorMap colorMap, KinectColorMap peopleColors, int maximalNumberOfSkeletons) {
		if (red1==null || green1==null || blue1==null) {
			return null;
		};
		if (playerIndex==null) {
			currentPeopleIndexMode= KinectPeopleIndexMode.NONE;
		};
		BufferedImage image= pointCloudToBlankColouredBufferedImage(xyz);
		if (image==null) {
			return null;
		};
		int uvWidth= red1.length;
		if (uvWidth <= 0) {
			return null;
		};
		int uvHeight= red1[0].length;
		int imageWidth= image.getWidth();
		int imageHeight= image.getHeight();
		int bandLength= imageWidth * imageHeight;
		int[] red2= new int[bandLength];
		int[] green2= new int[bandLength];
		int[] blue2= new int[bandLength];
		int[][] peopleColorMatrix= peopleColors.toRepeatedColors(maximalNumberOfSkeletons);
		int[] peopleIndexRed= peopleColorMatrix[0];
		int[] peopleIndexGreen= peopleColorMatrix[1];
		int[] peopleIndexBlue= peopleColorMatrix[2];
		double colormapTincturingCoefficient= colorMap.getColorMapTincturingCoefficient();
		double peopleColorsTincturingCoefficient= peopleColors.getPeopleColorsTincturingCoefficient();
		for (int n=0; n < bandLength; n++) {
			float x0= xyz[n*3];
			float y0= xyz[n*3+1];
			float z0= xyz[n*3+2];
			float fX= XY_Tools.xyzToX(x0,z0,focalLengthX,correctionX);
			float fY= XY_Tools.xyzToY(y0,z0,focalLengthY,correctionY);
			int x2= XY_Tools.centeredXtoIndexUV(fX,imageWidth,true);
			int y2= XY_Tools.centeredYtoIndexUV(fY,imageHeight,true);
			int counter2= y2*imageWidth + (imageWidth-x2-1);
			if (currentPeopleIndexMode==KinectPeopleIndexMode.TINCTURE_PEOPLE) {
				//
//-----------------------------------------------------------------//
// Tincture People                                                 //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[n];
if (index >= 0) {
	if (index > peopleIndexRed.length) {
		index= peopleIndexRed.length;
	};
	int redValue= byte2int(red1[x2][y2]);
	int greenValue= byte2int(green1[x2][y2]);
	int blueValue= byte2int(blue1[x2][y2]);
	int previousMaxValue= redValue;
	if (previousMaxValue < greenValue) {
		previousMaxValue= greenValue;
	};
	if (previousMaxValue < blueValue) {
		previousMaxValue= blueValue;
	};
	redValue= (int)(redValue / colormapTincturingCoefficient + peopleIndexRed[index] / peopleColorsTincturingCoefficient);
	greenValue= (int)(greenValue / colormapTincturingCoefficient + peopleIndexGreen[index] / peopleColorsTincturingCoefficient);
	blueValue= (int)(blueValue / colormapTincturingCoefficient + peopleIndexBlue[index] / peopleColorsTincturingCoefficient);
	int newMaxValue= redValue;
	if (newMaxValue < greenValue) {
		newMaxValue= greenValue;
	};
	if (newMaxValue < blueValue) {
		newMaxValue= blueValue;
	};
	if (newMaxValue > 0) {
		redValue= redValue * previousMaxValue / newMaxValue;
		greenValue= greenValue * previousMaxValue / newMaxValue;
		blueValue= blueValue * previousMaxValue / newMaxValue;
	};
	if (redValue > maxColorValue) {
		redValue= maxColorValue;
	};
	if (greenValue > maxColorValue) {
		greenValue= maxColorValue;
	};
	if (blueValue > maxColorValue) {
		blueValue= maxColorValue;
	};
	red2[counter2]= redValue;
	green2[counter2]= greenValue;
	blue2[counter2]= blueValue;
} else {
	red2[counter2]= byte2int(red1[x2][y2]);
	green2[counter2]= byte2int(green1[x2][y2]);
	blue2[counter2]= byte2int(blue1[x2][y2]);
}
//
//-----------------------------------------------------------------//
				//
			} else if (currentPeopleIndexMode==KinectPeopleIndexMode.PAINT_PEOPLE) {
				//
//-----------------------------------------------------------------//
// Paint People                                                    //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[n];
if (index >= 0) {
	if (index > peopleIndexRed.length) {
		index= peopleIndexRed.length;
	};
	red2[counter2]= peopleIndexRed[index];
	green2[counter2]= peopleIndexGreen[index];
	blue2[counter2]= peopleIndexBlue[index];
} else {
	red2[counter2]= byte2int(red1[x2][y2]);
	green2[counter2]= byte2int(green1[x2][y2]);
	blue2[counter2]= byte2int(blue1[x2][y2]);
}
//
//-----------------------------------------------------------------//
				//
			} else if (currentPeopleIndexMode==KinectPeopleIndexMode.EXTRACT_PEOPLE) {
				//
//-----------------------------------------------------------------//
// Extract People                                                  //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[n];
if (index >= 0) {
	red2[counter2]= byte2int(red1[x2][y2]);
	green2[counter2]= byte2int(green1[x2][y2]);
	blue2[counter2]= byte2int(blue1[x2][y2]);
} else if (useWhiteBackground) {
	red2[counter2]= maxColorValue;
	green2[counter2]= maxColorValue;
	blue2[counter2]= maxColorValue;
}
//
//-----------------------------------------------------------------//
				//
			} else if (currentPeopleIndexMode==KinectPeopleIndexMode.ADAPTIVELY_EXTRACT_PEOPLE) {
				//
//-----------------------------------------------------------------//
// Adaptively Extract People                                       //
//-----------------------------------------------------------------//
//
int index= (int)playerIndex[n];
if (index >= 0) {
	red2[counter2]= byte2int(red1[x2][y2]);
	green2[counter2]= byte2int(green1[x2][y2]);
	blue2[counter2]= byte2int(blue1[x2][y2]);
} else if (useWhiteBackground) {
	red2[counter2]= maxColorValue;
	green2[counter2]= maxColorValue;
	blue2[counter2]= maxColorValue;
}
//
//-----------------------------------------------------------------//
				//
			} else {
				red2[counter2]= byte2int(red1[x2][y2]);
				green2[counter2]= byte2int(green1[x2][y2]);
				blue2[counter2]= byte2int(blue1[x2][y2]);
			}
                };
		WritableRaster imageRaster= image.getRaster();
		imageRaster.setSamples(0,0,imageWidth,imageHeight,0,red2);
		imageRaster.setSamples(0,0,imageWidth,imageHeight,1,green2);
		imageRaster.setSamples(0,0,imageWidth,imageHeight,2,blue2);
		return image;
	}
/////////////////////////////////////////////////////////////////////
// Color Frame to Buffered Image                                   //
// (tinctureRatio4)                                                //
/////////////////////////////////////////////////////////////////////
	public static BufferedImage colorFrameToBufferedImage(byte[] data, float[][] u, float v[][], KinectPeopleIndexMode currentPeopleIndexMode, byte[] playerIndex, boolean useWhiteBackground, KinectColorMap colorMap, KinectColorMap peopleColors, int maximalNumberOfSkeletons) {
		FrameSize colorFrameSize= FrameSize.computeColorFrameSize(data);
		int colorFrameWidth= colorFrameSize.width;
		int colorFrameHeight= colorFrameSize.height;
		if (colorFrameWidth <= 0 || colorFrameHeight <= 0) {
			return null;
		};
		BufferedImage image= new BufferedImage(colorFrameWidth,colorFrameHeight,BufferedImage.TYPE_3BYTE_BGR);
		int bandLength= colorFrameWidth * colorFrameHeight;
		int[] red2= new int[bandLength];
		int[] green2= new int[bandLength];
		int[] blue2= new int[bandLength];
		int[][] peopleColorMatrix= peopleColors.toRepeatedColors(maximalNumberOfSkeletons);
		int[] peopleIndexRed= peopleColorMatrix[0];
		int[] peopleIndexGreen= peopleColorMatrix[1];
		int[] peopleIndexBlue= peopleColorMatrix[2];
		double colormapTincturingCoefficient= colorMap.getColorMapTincturingCoefficient();
		double peopleColorsTincturingCoefficient= peopleColors.getPeopleColorsTincturingCoefficient();
		int counter1= 0;
		for (int h=0; h < colorFrameHeight; h++) {
			for (int w=0; w < colorFrameWidth; w++) {
				int counter2= h*colorFrameWidth + (colorFrameWidth-w-1);
				blue2[counter2]= byte2int(data[counter1++]);
				green2[counter2]= byte2int(data[counter1++]);
				red2[counter2]= byte2int(data[counter1++]);
				counter1++;
			}
		};
		//
		if (playerIndex != null && u != null && v != null) {
			int uvWidth= u.length;
			if (uvWidth > 0 && currentPeopleIndexMode != KinectPeopleIndexMode.NONE) {
				int uvHeight= u[0].length;
				if (currentPeopleIndexMode==KinectPeopleIndexMode.TINCTURE_PEOPLE) {
					//
//-----------------------------------------------------------------//
// Tincture People                                                 //
//-----------------------------------------------------------------//
//
int[] previousRed= new int[bandLength];
int[] previousGreen= new int[bandLength];
int[] previousBlue= new int[bandLength];
for (int n=0; n < bandLength; n++) {
	previousRed[n]= red2[n];
	previousGreen[n]= green2[n];
	previousBlue[n]= blue2[n];
};
counter1= -1;
for (int h=0; h < uvHeight; h++) {
	for (int w=0; w < uvWidth; w++) {
		counter1++;
		int index= (int)playerIndex[counter1];
		double tinctureRed;
		double tinctureGreen;
		double tinctureBlue;
		if (index >= 0) {
			if (index > peopleIndexRed.length) {
				index= peopleIndexRed.length;
			};
			tinctureRed= peopleIndexRed[index] / peopleColorsTincturingCoefficient;
			tinctureGreen= peopleIndexGreen[index] / peopleColorsTincturingCoefficient;
			tinctureBlue= peopleIndexBlue[index] / peopleColorsTincturingCoefficient;
		} else {
			continue;
		};
		float x1= u[w][h];
		float y1= v[w][h];
		int x2= XY_Tools.uToColorIndexX(x1,colorFrameWidth,false);
		int y2= XY_Tools.vToColorIndexY(y1,colorFrameHeight,false);
		if (!XY_Tools.isLegal(x2) || !XY_Tools.isLegal(y2)) {
			continue;
		};
		for (int x3= x2-spotRadiusX; x3 <= x2+spotRadiusX; x3++) {
			for (int y3= y2-spotRadiusY; y3 <= y2+spotRadiusY; y3++) {
				if (x3 < 0) {
					continue;
				} else if (x3 >= colorFrameWidth) {
					continue;
				};
				if (y3 < 0) {
					continue;
				} else if (y3 >= colorFrameHeight) {
					continue;
				};
				int counter3= y3*colorFrameWidth + (colorFrameWidth - x3 - 1);
				int redValue= previousRed[counter3];
				int greenValue= previousGreen[counter3];
				int blueValue= previousBlue[counter3];
				int previousMaxValue= redValue;
				if (previousMaxValue < greenValue) {
					previousMaxValue= greenValue;
				};
				if (previousMaxValue < blueValue) {
					previousMaxValue= blueValue;
				};
				redValue= (int)(redValue / colormapTincturingCoefficient + tinctureRed);
				greenValue= (int)(greenValue / colormapTincturingCoefficient + tinctureGreen);
				blueValue= (int)(blueValue / colormapTincturingCoefficient + tinctureBlue);
				int newMaxValue= redValue;
				if (newMaxValue < greenValue) {
					newMaxValue= greenValue;
				};
				if (newMaxValue < blueValue) {
					newMaxValue= blueValue;
				};
				if (newMaxValue > 0) {
					redValue= redValue * previousMaxValue / newMaxValue;
					greenValue= greenValue * previousMaxValue / newMaxValue;
					blueValue= blueValue * previousMaxValue / newMaxValue;
				};
				if (redValue > maxColorValue) {
					redValue= maxColorValue;
				};
				if (greenValue > maxColorValue) {
					greenValue= maxColorValue;
				};
				if (blueValue > maxColorValue) {
					blueValue= maxColorValue;
				};
				red2[counter3]= redValue;
				green2[counter3]= greenValue;
				blue2[counter3]= blueValue;
			}
		}
	}
}
//
//-----------------------------------------------------------------//
					//
				} else {
					//
//-----------------------------------------------------------------//
// Paint or Extract People if Necessary                            //
//-----------------------------------------------------------------//
//
counter1= -1;
for (int h=0; h < uvHeight; h++) {
	for (int w=0; w < uvWidth; w++) {
		counter1++;
		int index= (int)playerIndex[counter1];
		boolean paintPeople= false;
		boolean clearBackground= false;
		if (currentPeopleIndexMode==KinectPeopleIndexMode.PAINT_PEOPLE) {
			paintPeople= true;
		} else if (currentPeopleIndexMode==KinectPeopleIndexMode.EXTRACT_PEOPLE) {
			clearBackground= true;
		} else if (currentPeopleIndexMode==KinectPeopleIndexMode.ADAPTIVELY_EXTRACT_PEOPLE) {
			clearBackground= true;
		};
		int valueRed;
		int valueGreen;
		int valueBlue;
		if (index >= 0) {
			if (paintPeople) {
				if (index > peopleIndexRed.length) {
					index= peopleIndexRed.length;
				};
				valueRed= peopleIndexRed[index];
				valueGreen= peopleIndexGreen[index];
				valueBlue= peopleIndexBlue[index];
			} else {
				continue;
			}
		} else {
			if (clearBackground) {
				if (useWhiteBackground) {
					valueRed= maxColorValue;
					valueGreen= maxColorValue;
					valueBlue= maxColorValue;
				} else {
					valueRed= 0;
					valueGreen= 0;
					valueBlue= 0;
				}
			} else {
				continue;
			}
		};
		float x1= u[w][h];
		float y1= v[w][h];
		int x2= XY_Tools.uToColorIndexX(x1,colorFrameWidth,false);
		int y2= XY_Tools.vToColorIndexY(y1,colorFrameHeight,false);
		if (!XY_Tools.isLegal(x2) || !XY_Tools.isLegal(y2)) {
			continue;
		};
		for (int x3= x2-spotRadiusX; x3 <= x2+spotRadiusX; x3++) {
			for (int y3= y2-spotRadiusY; y3 <= y2+spotRadiusY; y3++) {
				if (x3 < 0) {
					continue;
				} else if (x3 >= colorFrameWidth) {
					continue;
				};
				if (y3 < 0) {
					continue;
				} else if (y3 >= colorFrameHeight) {
					continue;
				};
				// int counter3= y3*colorFrameWidth + x3;
				int counter3= y3*colorFrameWidth + (colorFrameWidth - x3 - 1);
				red2[counter3]= valueRed;
				green2[counter3]= valueGreen;
				blue2[counter3]= valueBlue;
			}
		}
	}
}
//
//-----------------------------------------------------------------//
					//
				}
			}
		};
		//
		WritableRaster imageRaster= image.getRaster();
		imageRaster.setSamples(0,0,colorFrameWidth,colorFrameHeight,0,red2);
		imageRaster.setSamples(0,0,colorFrameWidth,colorFrameHeight,1,green2);
		imageRaster.setSamples(0,0,colorFrameWidth,colorFrameHeight,2,blue2);
		return image;
	}
/////////////////////////////////////////////////////////////////////
// Tune Program and Create Mapped Buffered Image                   //
/////////////////////////////////////////////////////////////////////
	public static TunedBufferedImage tuneProgramAndCreateMappedBufferedImage(float[] xyz, byte[][] red1, byte[][] green1, byte[][] blue1, KinectPeopleIndexMode currentPeopleIndexMode, byte[] playerIndex, boolean useWhiteBackground, float focalLengthX, float focalLengthY, int correctionX, int correctionY) {
		if (red1==null || green1==null || blue1==null) {
			return null;
		};
		if (playerIndex==null) {
			return null;
		};
		BufferedImage image= pointCloudToBlankColouredBufferedImage(xyz);
		if (image==null) {
			return null;
		};
		// int colorFrameWidth= red1.length;
		// if (colorFrameWidth <= 0) {
		//	return null;
		// };
		// int colorFrameHeight= red1[0].length;
		int uvWidth= red1.length;
		if (uvWidth <= 0) {
			return null;
		};
		int uvHeight= red1[0].length;
		int imageWidth= image.getWidth();
		int imageHeight= image.getHeight();
		int bandLength= imageWidth * imageHeight;
		int[] red2= new int[bandLength];
		int[] green2= new int[bandLength];
		int[] blue2= new int[bandLength];
		byte[][] matrix1= new byte[imageWidth][imageHeight];
		for (int n=0; n < bandLength; n++) {
			float x0= xyz[n*3];
			float y0= xyz[n*3+1];
			float z0= xyz[n*3+2];
			float fX= XY_Tools.xyzToX(x0,z0,focalLengthX,false,correctionX);
			float fY= XY_Tools.xyzToY(y0,z0,focalLengthY,false,correctionY);
			int x2= XY_Tools.centeredXtoIndexUV(fX,imageWidth,true);
			int y2= XY_Tools.centeredYtoIndexUV(fY,imageHeight,true);
			matrix1[imageWidth-x2-1][y2]= playerIndex[n];
			int counter2= y2*imageWidth + (imageWidth-x2-1);
			int index= (int)playerIndex[n];
			if (index >= 0) {
				red2[counter2]= maxColorValue / 2;
				green2[counter2]= 0;
				blue2[counter2]= 0;
			} else {
				red2[counter2]= byte2int(red1[x2][y2]);
				green2[counter2]= byte2int(green1[x2][y2]);
				blue2[counter2]= byte2int(blue1[x2][y2]);
			}
                };
		byte[][] matrix2= new byte[imageWidth][imageHeight];
		int counter1= -1;
		for (int h=0; h < imageHeight; h++) {
			for (int w=0; w < imageWidth; w++) {
				counter1++;
				matrix2[imageWidth-1-w][h]= playerIndex[counter1];
			}
		};
		int leftBoundX= -15;
		int rightBoundX= 15;
		int leftBoundY= -15;
		int rightBoundY= 15;
		int maxCoincidence= -1;
		int maxCoincidenceDX= 0;
		int maxCoincidenceDY= 0;
		for (int dX=leftBoundX; dX <= rightBoundX; dX++) {
			for (int dY=leftBoundY; dY <= rightBoundY; dY++) {
				int coincidenceCounter= 0;
				for (int h=-leftBoundY; h < imageHeight-rightBoundY; h++) {
					for (int w=-leftBoundX; w < imageWidth-rightBoundX; w++) {
						if (matrix1[w+dX][h+dY]==matrix2[w][h]) {
							if (matrix2[w][h] >= 0) {
								coincidenceCounter++;
							}
						}
					}
				};
				if (maxCoincidence < coincidenceCounter) {
					maxCoincidence= coincidenceCounter;
					maxCoincidenceDX= dX;
					maxCoincidenceDY= dY;
				}
			}
		};
		int counter3= 0;
		for (int h=0; h < imageHeight; h++) {
			for (int w=0; w < imageWidth; w++) {
				if (matrix2[w][h] >= 0) {
					red2[counter3]= 0;
					green2[counter3]= maxColorValue / 2;
					blue2[counter3]= 0;
				};
				counter3++;
			}
		};
		WritableRaster imageRaster= image.getRaster();
		imageRaster.setSamples(0,0,imageWidth,imageHeight,0,red2);
		imageRaster.setSamples(0,0,imageWidth,imageHeight,1,green2);
		imageRaster.setSamples(0,0,imageWidth,imageHeight,2,blue2);
		Font font= new Font(Font.SANS_SERIF,Font.BOLD,32);
		String results;
		if (maxCoincidence >= coincedenceThreshold) {
			results= String.format("Correction: dX = %s; dY = %s",maxCoincidenceDX,maxCoincidenceDY);
			// FrameDrawingBasics.setCorrectionX(maxCoincidenceDX);
			// FrameDrawingBasics.setCorrectionY(maxCoincidenceDY);
		} else {
			results= "No people found!";
		};
		Graphics2D g2= image.createGraphics();
		try {
			g2.setFont(font);
			g2.setColor(Color.YELLOW);
			g2.drawString(results,15,35);
		} finally {
			g2.dispose();
		};
		return new TunedBufferedImage(image,maxCoincidenceDX,maxCoincidenceDY);
	}
/////////////////////////////////////////////////////////////////////
// Short Array to Empty Buffered Image                             //
/////////////////////////////////////////////////////////////////////
	public static BufferedImage shortArrayToEmptyBufferedImage(short[] array, KinectPeopleIndexMode currentPeopleIndexMode, byte[] playerIndex, boolean useWhiteBackground, byte[][] red1, byte[][] green1, byte[][] blue1, KinectColorMap peopleColors, int maximalNumberOfSkeletons) {
		if (playerIndex==null) {
			currentPeopleIndexMode= KinectPeopleIndexMode.NONE;
		};
		BufferedImage image;
		if (	currentPeopleIndexMode==KinectPeopleIndexMode.TINCTURE_PEOPLE ||
			currentPeopleIndexMode==KinectPeopleIndexMode.PAINT_PEOPLE ||
			currentPeopleIndexMode==KinectPeopleIndexMode.PROJECT_PEOPLE) {
			image= shortArrayToBlankColouredBufferedImage(array);
			if (image==null) {
				return null;
			};
			if (currentPeopleIndexMode==KinectPeopleIndexMode.PROJECT_PEOPLE) {
				if (red1==null || green1==null || blue1==null) {
					currentPeopleIndexMode= KinectPeopleIndexMode.PAINT_PEOPLE;
				}
			};
			int frameLength= array.length;
			int imageWidth= image.getWidth();
			int imageHeight= image.getHeight();
			int bandLength= imageWidth * imageHeight;
			int[] red2= new int[bandLength];
			int[] green2= new int[bandLength];
			int[] blue2= new int[bandLength];
			int[][] peopleColorMatrix= peopleColors.toRepeatedColors(maximalNumberOfSkeletons);
			int[] peopleIndexRed= peopleColorMatrix[0];
			int[] peopleIndexGreen= peopleColorMatrix[1];
			int[] peopleIndexBlue= peopleColorMatrix[2];
			int counter1= -1;
			for (int h=0; h < imageHeight; h++) {
				for (int w=0; w < imageWidth; w++) {
					counter1++;
					int counter2= h*imageWidth + (imageWidth-w-1);
					int index= (int)playerIndex[counter1];
					if (index >= 0) {
						if (index > peopleIndexRed.length) {
							index= peopleIndexRed.length;
						};
						if (currentPeopleIndexMode==KinectPeopleIndexMode.PROJECT_PEOPLE) {
							//
//-----------------------------------------------------------------//
// Project People                                                  //
//-----------------------------------------------------------------//
//
int counter3= h*imageWidth + (imageWidth-w-1);
red2[counter3]= byte2int(red1[w][h]);
green2[counter3]= byte2int(green1[w][h]);
blue2[counter3]= byte2int(blue1[w][h]);
//
//-----------------------------------------------------------------//
							//
						} else {
							red2[counter2]= peopleIndexRed[index];
							green2[counter2]= peopleIndexGreen[index];
							blue2[counter2]= peopleIndexBlue[index];
						}
					} else if (useWhiteBackground) {
						red2[counter2]= maxColorValue;
						green2[counter2]= maxColorValue;
						blue2[counter2]= maxColorValue;
					}
				}
			};
			WritableRaster imageRaster= image.getRaster();
			imageRaster.setSamples(0,0,imageWidth,imageHeight,0,red2);
			imageRaster.setSamples(0,0,imageWidth,imageHeight,1,green2);
			imageRaster.setSamples(0,0,imageWidth,imageHeight,2,blue2);
		} else {
			image= shortArrayToBlankGrayBufferedImage(array);
			if (image==null) {
				return null;
			};
			int frameLength= array.length;
			int imageWidth= image.getWidth();
			int imageHeight= image.getHeight();
			double[] doubleNumberArray= new double[frameLength];
			for (int h=0; h < imageHeight; h++) {
				for (int w=0; w < imageWidth; w++) {
					int counter2= h*imageWidth + (imageWidth-w-1);
					if (useWhiteBackground) {
						doubleNumberArray[counter2]= maxColorValue;
					}
				}
			};
			WritableRaster imageRaster= image.getRaster();
			imageRaster.setSamples(0,0,imageWidth,imageHeight,0,doubleNumberArray);
		};
		return image;
	}
}
