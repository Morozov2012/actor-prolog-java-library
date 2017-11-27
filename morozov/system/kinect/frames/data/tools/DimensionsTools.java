// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.tools;

import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.tools.*;

import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class DimensionsTools {
	//
	public static Dimensions computeDimensions(int expectedNumberOfSkeletons, byte[] playerIndex, float[] xyz, float[][] u, float[][] v, GeneralSkeletonInterface[] skeletons, KinectFrameBaseAttributesInterface attributes) {
		Dimensions dimensions= new Dimensions(expectedNumberOfSkeletons);
		dimensions.computeDimensions(playerIndex,xyz,u,v,skeletons,attributes);
		return dimensions;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void draw(DimensionsInterface dimensions, Graphics2D g2, boolean useColorFramesMode, KinectCircumscriptionMode[] dimensionsModes, int x0, int y0, int imageWidth, int imageHeight, int scaledImageWidth, int scaledImageHeight) {
		KinectCircumscriptionMode firstItem= KinectCircumscriptionModesTools.getFirstItem(dimensionsModes);
		switch (firstItem) {
		case TOTAL_RECTANGLES:
		case TOTAL_PARALLELEPIPEDS:
			PlayerDimensionsInterface[] totalDepthDimensions= dimensions.getTotalDepthDimensions();
			PlayerDimensionsInterface[] totalColorDimensions= dimensions.getTotalColorDimensions();
			if (useColorFramesMode) {
				for (int n=0; n < totalColorDimensions.length; n++) {
					PlayerDimensionsInterface playerDimensions= totalColorDimensions[n];
					PlayerDimensionsTools.draw(playerDimensions,g2,dimensionsModes,x0,y0,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight);
				}
			} else {
				for (int n=0; n < totalDepthDimensions.length; n++) {
					PlayerDimensionsInterface playerDimensions= totalDepthDimensions[n];
					PlayerDimensionsTools.draw(playerDimensions,g2,dimensionsModes,x0,y0,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight);
				}
			};
			break;
		case SKELETON_RECTANGLES:
		case SKELETON_PARALLELEPIPEDS:
			PlayerDimensionsInterface[] skeletonsDepthDimensions= dimensions.getSkeletonsDepthDimensions();
			PlayerDimensionsInterface[] skeletonsColorDimensions= dimensions.getSkeletonsColorDimensions();
			if (useColorFramesMode) {
				for (int n=0; n < skeletonsColorDimensions.length; n++) {
					PlayerDimensionsInterface playerDimensions= skeletonsColorDimensions[n];
					PlayerDimensionsTools.draw(playerDimensions,g2,dimensionsModes,x0,y0,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight);
				}
			} else {
				for (int n=0; n < skeletonsDepthDimensions.length; n++) {
					PlayerDimensionsInterface playerDimensions= skeletonsDepthDimensions[n];
					PlayerDimensionsTools.draw(playerDimensions,g2,dimensionsModes,x0,y0,imageWidth,imageHeight,scaledImageWidth,scaledImageHeight);
				}
			};
			break;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeText(DimensionsInterface dimensions, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		//
		PlayerDimensionsInterface[] totalDepthDimensions= dimensions.getTotalDepthDimensions();
		PlayerDimensionsInterface[] skeletonsDepthDimensions= dimensions.getSkeletonsDepthDimensions();
		PlayerDimensionsInterface[] totalColorDimensions= dimensions.getTotalColorDimensions();
		PlayerDimensionsInterface[] skeletonsColorDimensions= dimensions.getSkeletonsColorDimensions();
		//
		writer.write("; DIMENSIONS:\n");
		//
		writer.write(String.format(locale,"; totalDepthDimensions: %d\n",totalDepthDimensions.length));
		for (int n=0; n < totalDepthDimensions.length; n++) {
			PlayerDimensionsTools.writeText(totalDepthDimensions[n],writer,exportMode,locale);
		};
		//
		writer.write(String.format(locale,"; skeletonsDepthDimensions: %d\n",skeletonsDepthDimensions.length));
		for (int n=0; n < skeletonsDepthDimensions.length; n++) {
			PlayerDimensionsTools.writeText(skeletonsDepthDimensions[n],writer,exportMode,locale);
		};
		//
		writer.write(String.format(locale,"; totalColorDimensions: %d\n",totalColorDimensions.length));
		for (int n=0; n < totalColorDimensions.length; n++) {
			PlayerDimensionsTools.writeText(totalColorDimensions[n],writer,exportMode,locale);
		};
		//
		writer.write(String.format(locale,"; skeletonsColorDimensions: %d\n",skeletonsColorDimensions.length));
		for (int n=0; n < skeletonsColorDimensions.length; n++) {
			PlayerDimensionsTools.writeText(skeletonsColorDimensions[n],writer,exportMode,locale);
		};
		//
		writer.write("; End of dimensions.\n");
	}
}
