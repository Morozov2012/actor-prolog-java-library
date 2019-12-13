// (c) 2016 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.tools;

import java.awt.image.BufferedImage;

import java.util.Arrays;

public class FrameDrawingBasics {
	//
	protected static int maxColorValue= 255;
	//
	protected static int spotRadiusX= 3;
	protected static int spotRadiusY= 2;
	//
	///////////////////////////////////////////////////////////////
	//
	public static int byte2int(byte b) {
		if (b >= 0) {
			return (int)b;
		} else {
			return (int)(b+256);
		}
	}
	//
/////////////////////////////////////////////////////////////////////
// Short Array to Blank Gray Buffered Image                        //
/////////////////////////////////////////////////////////////////////
	public static BufferedImage shortArrayToBlankGrayBufferedImage(short[] array) {
		FrameSize depthFrameSize= FrameSize.computeDepthFrameSize(array);
		int depthFrameWidth= depthFrameSize.width;
		int depthFrameHeight= depthFrameSize.height;
		if (depthFrameWidth <= 0 || depthFrameHeight <= 0) {
			return null;
		};
		BufferedImage image= new BufferedImage(depthFrameWidth,depthFrameHeight,BufferedImage.TYPE_BYTE_GRAY);
		return image;
	}
/////////////////////////////////////////////////////////////////////
// Short Array to Blank Coloured Buffered Image                    //
/////////////////////////////////////////////////////////////////////
	public static BufferedImage shortArrayToBlankColouredBufferedImage(short[] array) {
		FrameSize depthFrameSize= FrameSize.computeDepthFrameSize(array);
		int depthFrameWidth= depthFrameSize.width;
		int depthFrameHeight= depthFrameSize.height;
		if (depthFrameWidth <= 0 || depthFrameHeight <= 0) {
			return null;
		};
		BufferedImage image= new BufferedImage(depthFrameWidth,depthFrameHeight,BufferedImage.TYPE_3BYTE_BGR);
		return image;
	}
/////////////////////////////////////////////////////////////////////
// Point Cloud to Blank Gray Buffered Image                        //
/////////////////////////////////////////////////////////////////////
	public static BufferedImage pointCloudToBlankGrayBufferedImage(float[] array) {
		FrameSize xyzFrameSize= FrameSize.computeXYZFrameSize(array);
		int xyzFrameWidth= xyzFrameSize.width;
		int xyzFrameHeight= xyzFrameSize.height;
		if (xyzFrameWidth <= 0 || xyzFrameHeight <= 0) {
			return null;
		};
		BufferedImage image= new BufferedImage(xyzFrameWidth,xyzFrameHeight,BufferedImage.TYPE_BYTE_GRAY);
		return image;
	}
/////////////////////////////////////////////////////////////////////
// Point Cloud to Blank Coloured Buffered Image                    //
/////////////////////////////////////////////////////////////////////
	public static BufferedImage pointCloudToBlankColouredBufferedImage(float[] array) {
		FrameSize xyzFrameSize= FrameSize.computeXYZFrameSize(array);
		int xyzFrameWidth= xyzFrameSize.width;
		int xyzFrameHeight= xyzFrameSize.height;
		if (xyzFrameWidth <= 0 || xyzFrameHeight <= 0) {
			return null;
		};
		BufferedImage image= new BufferedImage(xyzFrameWidth,xyzFrameHeight,BufferedImage.TYPE_3BYTE_BGR);
		return image;
	}
/////////////////////////////////////////////////////////////////////
// Rank Filtering                                                  //
/////////////////////////////////////////////////////////////////////
	public static byte[] rankFilter2D(byte noDifferenceMarker, byte[] pixels, int width, int height, int userDefinedThreshold, boolean contourForeground) {
		int counterThreshold= 8 - userDefinedThreshold;
		byte[] result= Arrays.copyOf(pixels,pixels.length);
		for (int w=0; w < width; w++) {
			int h1= 0;
			int point1= width * h1 + w;
			result[point1]= noDifferenceMarker;
			int h2= height-1;
			int point2= width * h2 + w;
			result[point2]= noDifferenceMarker;
		};
		for (int h=0; h < height; h++) {
			int w1= 0;
			int point1= width * h + w1;
			result[point1]= noDifferenceMarker;
			int w2= width-1;
			int point2= width * h + w2;
			result[point2]= noDifferenceMarker;
		};
		for (int w=1; w <= width-2; w++) {
			for (int h=1; h <= height-2; h++) {
				int point1= width * h + w;
				byte value= pixels[point1];
				if (value <= noDifferenceMarker) {
					continue;
				} else {
					int spaceCounter= 0;
					for (int x=-1; x <= 1; x++) {
						for (int y=-1; y <= 1; y++) {
							int point2= width * (h+y) + (w+x);
							if (pixels[point2] != value) {
								spaceCounter++;
							}
						}
					};
					if ((spaceCounter > counterThreshold) || (contourForeground && spaceCounter <= 0)) {
						result[point1]= noDifferenceMarker;
					}
				}
			}
		};
		return result;
	}

}
