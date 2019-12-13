// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.kinect.converters;

import morozov.built_in.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.interfaces.*;

import java.awt.image.WritableRaster;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.GeometryArray;
import com.sun.j3d.utils.geometry.Stripifier;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.GeometryInfo;
import javax.media.j3d.Material;
import javax.vecmath.Color4f;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Appearance;
import javax.vecmath.*;
import javax.media.j3d.TransparencyAttributes;

import java.util.Arrays;

public class Kinect2Java3D {
	//
	protected static float minimalDepth= 0.0f;
	protected static BranchGroup emptyBranchGroup= new BranchGroup();
	//
	static {
		emptyBranchGroup.compile();
	}
	//
	///////////////////////////////////////////////////////////////
	// COMPUTE BUFFERED SCENE                                    //
	///////////////////////////////////////////////////////////////
	//
	public static void setBufferedScene(BufferedScene scene, KinectFrameInterface committedFrame, KinectDisplayingModeInterface displayingMode, KinectColorMap colorMap, NumericalValue sceneDepthThreshold, KinectSurfaceType sceneSurfaceType, ColorSubstitutionMode colorSubstitutionMode, Appearance sceneAppearance) {
		setBufferedScene(scene,committedFrame,displayingMode,colorMap,null,null,sceneDepthThreshold,sceneSurfaceType,colorSubstitutionMode,sceneAppearance);
	}
	public static void setBufferedScene(BufferedScene scene, KinectFrameInterface committedFrame, KinectDisplayingModeInterface displayingMode, KinectColorMap colorMap, java.awt.image.BufferedImage nativeTextureImage, KinectLookupTable lookupTable, NumericalValue sceneDepthThreshold, KinectSurfaceType sceneSurfaceType, ColorSubstitutionMode colorSubstitutionMode, Appearance sceneAppearance) {
		if (committedFrame instanceof KinectPointCloudsFrameInterface) {
			KinectPointCloudsFrameInterface pointCloudsFrame= (KinectPointCloudsFrameInterface)committedFrame;
			BranchGroup branchGroup= computeBufferedScene(pointCloudsFrame,displayingMode,colorMap,nativeTextureImage,lookupTable,sceneDepthThreshold,sceneSurfaceType,colorSubstitutionMode,sceneAppearance);
			scene.setBranchGroup(branchGroup);
		} else {
			scene.setBranchGroup(emptyBranchGroup);
		}
	}
	public static BranchGroup computeBufferedScene(KinectPointCloudsFrameInterface pointCloudsFrame, KinectDisplayingModeInterface displayingMode, KinectColorMap colorMap, java.awt.image.BufferedImage nativeTextureImage, KinectLookupTable lookupTable, NumericalValue sceneDepthThreshold, KinectSurfaceType sceneSurfaceType, ColorSubstitutionMode colorSubstitutionMode, Appearance sceneAppearance) {
		KinectPeopleIndexMode peopleIndexMode= displayingMode.getActingPeopleIndexMode();
		ColorMapName colorMapName= colorMap.getName();
		boolean extractPlayers= peopleIndexMode.peopleAreToBeExtracted();
		boolean doUseColors=
			(nativeTextureImage != null) ||
			(colorMapName != ColorMapName.NONE);
		boolean generateNormals= (colorMapName==ColorMapName.NONE);
		float[] cloud= pointCloudsFrame.getXYZ();
		FrameSize frameSize= FrameSize.computeXYZFrameSize(cloud);
		int frameWidth= frameSize.getWidth();
		int frameHeight= frameSize.getHeight();
		byte[] playerIndex= pointCloudsFrame.getPlayerIndex();
		float maximalColorValueFloat= 255.0f;
		//
		int[] red= null;
		int[] green= null;
		int[] blue= null;
		int[] alpha= null;
		boolean hasTransparentPixels= false;
		//
		if (doUseColors) {
			int frameLength= frameWidth * frameHeight;
			red= new int[frameLength];
			green= new int[frameLength];
			blue= new int[frameLength];
			alpha= new int[frameLength];
			hasTransparentPixels= fuseTwoImages(
				red,
				green,
				blue,
				alpha,
				pointCloudsFrame,
				displayingMode,
				colorMap,
				nativeTextureImage,
				lookupTable,
				colorSubstitutionMode);
		};
		float maximalDepthDifference= NumericalValueConverters.toFloat(sceneDepthThreshold);
		boolean createConvexSurface= true;
		if (sceneSurfaceType==KinectSurfaceType.CONCAVE) {
			createConvexSurface= false;
		};
		int numberOfQuads= (frameWidth-1) * (frameHeight-1);
		int arrayLength= numberOfQuads*4;
		GeometryInfo geometryInfo= new GeometryInfo(GeometryInfo.QUAD_ARRAY);
		Point3f[] quads= new Point3f[arrayLength];
		Color4f[] colors= null;
		if (doUseColors) {
			colors= new Color4f[arrayLength];
		};
		int quadIndex= 0;
		for (int y=0; y < frameHeight-1; y++) {
			for (int x=0; x < frameWidth-1; x++) {
				int index11= y*frameWidth + x;
				if (extractPlayers && playerIndex[index11] < 0) {
					continue;
				};
				int index12= y*frameWidth + x+1;
				if (extractPlayers && playerIndex[index12] < 0) {
					continue;
				};
				int index21= (y+1)*frameWidth + x;
				if (extractPlayers && playerIndex[index21] < 0) {
					continue;
				};
				int index22= (y+1)*frameWidth + x+1;
				if (extractPlayers && playerIndex[index22] < 0) {
					continue;
				};
				index11= index11 * 3;
				index12= index12 * 3;
				index21= index21 * 3;
				index22= index22 * 3;
				float x11= cloud[index11];
				if (Float.isInfinite(x11)) {
					continue;
				};
				float y11= cloud[index11+1];
				if (Float.isInfinite(y11)) {
					continue;
				};
				float z11= cloud[index11+2];
				if (z11 < minimalDepth) {
					continue;
				};
				float x12= cloud[index12];
				if (Float.isInfinite(x12)) {
					continue;
				};
				float y12= cloud[index12+1];
				if (Float.isInfinite(y12)) {
					continue;
				};
				float z12= cloud[index12+2];
				if (z12 < minimalDepth) {
					continue;
				};
				float delta= z11 - z12;
				if (delta < 0) {
					delta= -delta;
				};
				if (delta > maximalDepthDifference) {
					continue;
				};
				float x21= cloud[index21];
				if (Float.isInfinite(x21)) {
					continue;
				};
				float y21= cloud[index21+1];
				if (Float.isInfinite(y21)) {
					continue;
				};
				float z21= cloud[index21+2];
				if (z21 < minimalDepth) {
					continue;
				};
				delta= z11 - z21;
				if (delta < 0) {
					delta= -delta;
				};
				if (delta > maximalDepthDifference) {
					continue;
				};
				float x22= cloud[index22];
				if (Float.isInfinite(x22)) {
					continue;
				};
				float y22= cloud[index22+1];
				if (Float.isInfinite(y22)) {
					continue;
				};
				float z22= cloud[index22+2];
				if (z22 < minimalDepth) {
					continue;
				};
				delta= z12 - z22;
				if (delta < 0) {
					delta= -delta;
				};
				if (delta > maximalDepthDifference) {
					continue;
				};
				delta= z21 - z22;
				if (delta < 0) {
					delta= -delta;
				};
				if (delta > maximalDepthDifference) {
					continue;
				};
				//
				if (createConvexSurface) {
					//
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
quads[quadIndex]= new Point3f(-x12,y12,-z12);
if (doUseColors) {
	int i12= y*frameWidth + x+1;
	float floatRed12= (float)(red[i12] & 0xFF) / maximalColorValueFloat;
	float floatGreen12= (float)(green[i12] & 0xFF) / maximalColorValueFloat;
	float floatBlue12= (float)(blue[i12] & 0xFF) / maximalColorValueFloat;
	float floatAlpha12= (float)(alpha[i12] & 0xFF) / maximalColorValueFloat;
	Color4f color12= new Color4f(floatRed12,floatGreen12,floatBlue12,floatAlpha12);
	colors[quadIndex]= color12;
};
quadIndex++;
//
quads[quadIndex]= new Point3f(-x22,y22,-z22);
if (doUseColors) {
	int i22= (y+1)*frameWidth + x+1;
	float floatRed22= (float)(red[i22] & 0xFF) / maximalColorValueFloat;
	float floatGreen22= (float)(green[i22] & 0xFF) / maximalColorValueFloat;
	float floatBlue22= (float)(blue[i22] & 0xFF) / maximalColorValueFloat;
	float floatAlpha22= (float)(alpha[i22] & 0xFF) / maximalColorValueFloat;
	Color4f color22= new Color4f(floatRed22,floatGreen22,floatBlue22,floatAlpha22);
	colors[quadIndex]= color22;
};
quadIndex++;
//
quads[quadIndex]= new Point3f(-x21,y21,-z21);
if (doUseColors) {
	int i21= (y+1)*frameWidth + x;
	float floatRed21= (float)(red[i21] & 0xFF) / maximalColorValueFloat;
	float floatGreen21= (float)(green[i21] & 0xFF) / maximalColorValueFloat;
	float floatBlue21= (float)(blue[i21] & 0xFF) / maximalColorValueFloat;
	float floatAlpha21= (float)(alpha[i21] & 0xFF) / maximalColorValueFloat;
	Color4f color21= new Color4f(floatRed21,floatGreen21,floatBlue21,floatAlpha21);
	colors[quadIndex]= color21;
};
quadIndex++;
//
quads[quadIndex]= new Point3f(-x11,y11,-z11);
if (doUseColors) {
	int i11= y*frameWidth + x;
	float floatRed11= (float)(red[i11] & 0xFF) / maximalColorValueFloat;
	float floatGreen11= (float)(green[i11] & 0xFF) / maximalColorValueFloat;
	float floatBlue11= (float)(blue[i11] & 0xFF) / maximalColorValueFloat;
	float floatAlpha11= (float)(alpha[i11] & 0xFF) / maximalColorValueFloat;
	Color4f color11= new Color4f(floatRed11,floatGreen11,floatBlue11,floatAlpha11);
	colors[quadIndex]= color11;
};
quadIndex++;
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
					//
				} else {
					//
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
quads[quadIndex]= new Point3f(-x11,y11,-z11);
if (doUseColors) {
	int i11= y*frameWidth + x;
	float floatRed11= (float)(red[i11] & 0xFF) / maximalColorValueFloat;
	float floatGreen11= (float)(green[i11] & 0xFF) / maximalColorValueFloat;
	float floatBlue11= (float)(blue[i11] & 0xFF) / maximalColorValueFloat;
	float floatAlpha11= (float)(alpha[i11] & 0xFF) / maximalColorValueFloat;
	Color4f color11= new Color4f(floatRed11,floatGreen11,floatBlue11,floatAlpha11);
	colors[quadIndex]= color11;
};
quadIndex++;
//
quads[quadIndex]= new Point3f(-x21,y21,-z21);
if (doUseColors) {
	int i21= (y+1)*frameWidth + x;
	float floatRed21= (float)(red[i21] & 0xFF) / maximalColorValueFloat;
	float floatGreen21= (float)(green[i21] & 0xFF) / maximalColorValueFloat;
	float floatBlue21= (float)(blue[i21] & 0xFF) / maximalColorValueFloat;
	float floatAlpha21= (float)(alpha[i21] & 0xFF) / maximalColorValueFloat;
	Color4f color21= new Color4f(floatRed21,floatGreen21,floatBlue21,floatAlpha21);
	colors[quadIndex]= color21;
};
quadIndex++;
//
quads[quadIndex]= new Point3f(-x22,y22,-z22);
if (doUseColors) {
	int i22= (y+1)*frameWidth + x+1;
	float floatRed22= (float)(red[i22] & 0xFF) / maximalColorValueFloat;
	float floatGreen22= (float)(green[i22] & 0xFF) / maximalColorValueFloat;
	float floatBlue22= (float)(blue[i22] & 0xFF) / maximalColorValueFloat;
	float floatAlpha22= (float)(alpha[i22] & 0xFF) / maximalColorValueFloat;
	Color4f color22= new Color4f(floatRed22,floatGreen22,floatBlue22,floatAlpha22);
	colors[quadIndex]= color22;
};
quadIndex++;
//
quads[quadIndex]= new Point3f(-x12,y12,-z12);
if (doUseColors) {
	int i12= y*frameWidth + x+1;
	float floatRed12= (float)(red[i12] & 0xFF) / maximalColorValueFloat;
	float floatGreen12= (float)(green[i12] & 0xFF) / maximalColorValueFloat;
	float floatBlue12= (float)(blue[i12] & 0xFF) / maximalColorValueFloat;
	float floatAlpha12= (float)(alpha[i12] & 0xFF) / maximalColorValueFloat;
	Color4f color12= new Color4f(floatRed12,floatGreen12,floatBlue12,floatAlpha12);
	colors[quadIndex]= color12;
};
quadIndex++;
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
					//
				}
			}
		};
		quads= Arrays.copyOfRange(quads,0,quadIndex,quads.getClass());
		BranchGroup branchGroup= new BranchGroup();
		if (quadIndex > 0) {
			geometryInfo.setCoordinates(quads);
			if (doUseColors) {
				colors= Arrays.copyOfRange(colors,0,quadIndex,colors.getClass());
				geometryInfo.setColors(colors);
			};
			if (generateNormals) {
				NormalGenerator normalGenerator= new NormalGenerator();
				normalGenerator.generateNormals(geometryInfo);
			};
			Stripifier stripifier= new Stripifier();
			stripifier.stripify(geometryInfo);
			GeometryArray geometryArray= geometryInfo.getGeometryArray();
			Shape3D shape3D= new Shape3D(geometryArray);
			if (sceneAppearance==null) {
				sceneAppearance= createMaterialAppearance(hasTransparentPixels);
			};
			shape3D.setAppearance(sceneAppearance);
			branchGroup.addChild(shape3D);
		};
		branchGroup.compile();
		return branchGroup;
	}
	protected static Appearance createMaterialAppearance(boolean hasTransparentPixels) {
		Appearance materialAppear= new Appearance();
		PolygonAttributes polygonAttributes= new PolygonAttributes();
		polygonAttributes.setCullFace(PolygonAttributes.CULL_NONE);
		materialAppear.setPolygonAttributes(polygonAttributes);
		Material material= new Material();
		materialAppear.setMaterial(material);
		if (hasTransparentPixels) {
			materialAppear.setTransparencyAttributes(
				new TransparencyAttributes(
					TransparencyAttributes.FASTEST,
					// TransparencyAttributes.NICEST,
					0.0f));
		};
		return materialAppear;
	}
	//
	///////////////////////////////////////////////////////////////
	// COMPUTE MAPPED IMAGE                                      //
	///////////////////////////////////////////////////////////////
	//
	public static java.awt.image.BufferedImage computeMappedImage(KinectFrameInterface committedFrame, KinectDisplayingModeInterface displayingMode, KinectColorMap colorMap, java.awt.image.BufferedImage nativeTextureImage, KinectLookupTable lookupTable, ColorSubstitutionMode colorSubstitutionMode) {
		if (committedFrame instanceof KinectPointCloudsFrameInterface) {
			KinectPointCloudsFrameInterface pointCloudsFrame= (KinectPointCloudsFrameInterface)committedFrame;
			return computeMappedImage(pointCloudsFrame,displayingMode,colorMap,nativeTextureImage,lookupTable,colorSubstitutionMode);
		} else {
			return null;
		}
	}
	public static java.awt.image.BufferedImage computeMappedImage(KinectPointCloudsFrameInterface pointCloudsFrame, KinectDisplayingModeInterface displayingMode, KinectColorMap colorMap, java.awt.image.BufferedImage nativeTextureImage, KinectLookupTable lookupTable, ColorSubstitutionMode colorSubstitutionMode) {
		ColorMapName colorMapName= colorMap.getName();
		boolean doUseColors=
			(nativeTextureImage != null) ||
			(colorMapName != ColorMapName.NONE);
		float[] cloud= pointCloudsFrame.getXYZ();
		FrameSize frameSize= FrameSize.computeXYZFrameSize(cloud);
		int frameWidth= frameSize.getWidth();
		int frameHeight= frameSize.getHeight();
		//
		int[] red= null;
		int[] green= null;
		int[] blue= null;
		int[] alpha= null;
		boolean hasTransparentPixels= false;
		//
		if (doUseColors) {
			int frameLength= frameWidth * frameHeight;
			red= new int[frameLength];
			green= new int[frameLength];
			blue= new int[frameLength];
			alpha= new int[frameLength];
			hasTransparentPixels= fuseTwoImages(
				red,
				green,
				blue,
				alpha,
				pointCloudsFrame,
				displayingMode,
				colorMap,
				nativeTextureImage,
				lookupTable,
				colorSubstitutionMode);
		};
		java.awt.image.BufferedImage resultImage= new java.awt.image.BufferedImage(frameWidth,frameHeight,java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
		if (doUseColors) {
			WritableRaster resultRaster= resultImage.getRaster();
			resultRaster.setSamples(0,0,frameWidth,frameHeight,0,red);
			resultRaster.setSamples(0,0,frameWidth,frameHeight,1,green);
			resultRaster.setSamples(0,0,frameWidth,frameHeight,2,blue);
		};
		return resultImage;
	}
	//
	///////////////////////////////////////////////////////////////
	// IMAGE FUSION                                              //
	///////////////////////////////////////////////////////////////
	//
	public static boolean fuseTwoImages(
			int[] red,
			int[] green,
			int[] blue,
			int[] alpha,
			KinectPointCloudsFrameInterface pointCloudsFrame,
			KinectDisplayingModeInterface displayingMode,
			KinectColorMap colorMap,
			java.awt.image.BufferedImage nativeTextureImage,
			KinectLookupTable lookupTable,
			ColorSubstitutionMode colorSubstitutionMode) {
		KinectPeopleIndexMode peopleIndexMode= displayingMode.getActingPeopleIndexMode();
		ColorMapName colorMapName= colorMap.getName();
		boolean extractPlayers= peopleIndexMode.peopleAreToBeExtracted();
		boolean doUsePseudocolors=
			(colorMapName != ColorMapName.OPTICAL) &&
			(colorMapName != ColorMapName.NONE);
		boolean doUseColors=
			(nativeTextureImage != null) ||
			(colorMapName != ColorMapName.NONE);
		boolean generateNormals= (colorMapName==ColorMapName.NONE);
		float[] cloud= pointCloudsFrame.getXYZ();
		FrameSize frameSize= FrameSize.computeXYZFrameSize(cloud);
		int frameWidth= frameSize.getWidth();
		int frameHeight= frameSize.getHeight();
		byte[] playerIndex= pointCloudsFrame.getPlayerIndex();
		int maximalColorValueInteger= 255;
		int halfColorValueInteger= 127;
		//byte[][] red;
		//byte[][] green;
		//byte[][] blue;
		//byte[][] alpha;
		boolean hasTransparentPixels= false;
		if (nativeTextureImage==null) {
///////////////////////////////////////////////////////////////////////
if (doUsePseudocolors) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//red= new byte[frameWidth][frameHeight];
//green= new byte[frameWidth][frameHeight];
//blue= new byte[frameWidth][frameHeight];
//alpha= new byte[frameWidth][frameHeight];
int[][] colorMapColors= colorMap.toColors();
long numberOfColorMapBands= colorMap.toNumberOfBands();
boolean colorMapIsIterative= (numberOfColorMapBands > 1);
FloatQuantiles quantiles= new FloatQuantiles(
	colorMap.getLowerQuantile(),
	colorMap.getUpperQuantile(),
	colorMap.getLowerBoundIsZero(),
	colorMap.getUpperBoundIsZero(),
	colorMapIsIterative);
quantiles.computeQuantiles(cloud,extractPlayers,playerIndex,frameWidth,frameHeight);
int colorMapSize= 256;
if (colorMapColors != null && colorMapColors.length > 0) {
	colorMapSize= colorMapColors[0].length;
};
for (int w1=0; w1 < frameWidth; w1++) {
	for (int h1=0; h1 < frameHeight; h1++) {
		int index11= h1*frameWidth + w1;
		if (extractPlayers && playerIndex[index11] < 0) {
			continue;
		};
		float z11= cloud[index11*3+2];
		if (z11 < minimalDepth) {
			continue;
		};
		int colorIndex= (int)(colorMapSize * quantiles.standardizeByQuantiles(z11));
		if (colorIndex < 0) {
			colorIndex= 0;
		} else if (colorIndex > colorMapSize-1) {
			colorIndex= colorMapSize-1;
		};
		byte valueRed= (byte)colorMapColors[0][colorIndex];
		byte valueGreen= (byte)colorMapColors[1][colorIndex];
		byte valueBlue= (byte)colorMapColors[2][colorIndex];
		byte valueAlpha= (byte)colorMapColors[3][colorIndex];
		//red[w1][h1]= valueRed;
		//green[w1][h1]= valueGreen;
		//blue[w1][h1]= valueBlue;
		//alpha[w1][h1]= valueAlpha;
		red[index11]= valueRed;
		green[index11]= valueGreen;
		blue[index11]= valueBlue;
		alpha[index11]= valueAlpha;
		if (valueAlpha == 0) {
			hasTransparentPixels= true; // 2019-07-06: false;
		}
	}
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
} else { // !doUsePseudocolors
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
if (doUseColors) {
	byte[][] byteRed= pointCloudsFrame.getMappedRed();
	byte[][] byteGreen= pointCloudsFrame.getMappedGreen();
	byte[][] byteBlue= pointCloudsFrame.getMappedBlue();
	for (int w1=0; w1 < frameWidth; w1++) {
		for (int h1=0; h1 < frameHeight; h1++) {
			int index2= h1*frameWidth + w1;
			red[index2]= byteRed[w1][h1];
			green[index2]= byteGreen[w1][h1];
			blue[index2]= byteBlue[w1][h1];
		}
	}
}
//if (doUseColors) {
//	red= pointCloudsFrame.getMappedRed();
//	green= pointCloudsFrame.getMappedGreen();
//	blue= pointCloudsFrame.getMappedBlue();
//	alpha= new byte[frameWidth][frameHeight];
//} else {
//	red= null;
//	green= null;
//	blue= null;
//	alpha= null;
//}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
}
///////////////////////////////////////////////////////////////////////
		} else { // nativeTextureImage != null
///////////////////////////////////////////////////////////////////////
if (doUsePseudocolors) {
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//red= new byte[frameWidth][frameHeight];
//green= new byte[frameWidth][frameHeight];
//blue= new byte[frameWidth][frameHeight];
//alpha= new byte[frameWidth][frameHeight];
//
int textureWidth= nativeTextureImage.getWidth();
int textureHeight= nativeTextureImage.getHeight();
int[] rgbArray= nativeTextureImage.getRGB(0,0,textureWidth,textureHeight,null,0,textureWidth);
int rgbArrayLength= rgbArray.length;
boolean isVolumetricLookupTable= lookupTable.isVolumetricLookupTable();
float[][][] lookupArray= lookupTable.getLookupTable();
int correctionX= lookupTable.getCorrectionX();
int correctionY= lookupTable.getCorrectionY();
int lookupArrayWidth= 0;
int lookupArrayHeight= 0;
if (lookupArray != null) {
	lookupArrayWidth= lookupArray.length;
	if (lookupArrayWidth > 0) {
		lookupArrayHeight= lookupArray[0].length;
	}
};
//
int[][] colorMapColors= colorMap.toColors();
long numberOfColorMapBands= colorMap.toNumberOfBands();
boolean colorMapIsIterative= (numberOfColorMapBands > 1);
FloatQuantiles quantiles= new FloatQuantiles(
	colorMap.getLowerQuantile(),
	colorMap.getUpperQuantile(),
	colorMap.getLowerBoundIsZero(),
	colorMap.getUpperBoundIsZero(),
	colorMapIsIterative);
quantiles.computeQuantiles(cloud,extractPlayers,playerIndex,frameWidth,frameHeight);
int colorMapSize= 256;
if (colorMapColors != null && colorMapColors.length > 0) {
	colorMapSize= colorMapColors[0].length;
};
//
boolean use_RGB_Mode= colorSubstitutionMode.useRGBMode();
//
ColorChannelSubstitution firstChannelSubstitution= colorSubstitutionMode.getFirstChannelSubstitution();
ColorChannelSubstitution secondChannelSubstitution= colorSubstitutionMode.getSecondChannelSubstitution();
ColorChannelSubstitution thirdChannelSubstitution= colorSubstitutionMode.getThirdChannelSubstitution();
//
boolean firstChannelSubstitutionIsNamed= firstChannelSubstitution.isNamed();
boolean secondChannelSubstitutionIsNamed= secondChannelSubstitution.isNamed();
boolean thirdChannelSubstitutionIsNamed= thirdChannelSubstitution.isNamed();
ColorChannelSubstitutionName firstChannelSubstitutionName= firstChannelSubstitution.getName();
ColorChannelSubstitutionName secondChannelSubstitutionName= secondChannelSubstitution.getName();
ColorChannelSubstitutionName thirdChannelSubstitutionName= thirdChannelSubstitution.getName();
int firstChannelSubstitutionValue= firstChannelSubstitution.getValue();
int secondChannelSubstitutionValue= secondChannelSubstitution.getValue();
int thirdChannelSubstitutionValue= thirdChannelSubstitution.getValue();
//
boolean require_HSB_Channels_1= (colorSubstitutionMode.requires_HSB_Channels(1));
boolean require_HSB_Channels_2= (colorSubstitutionMode.requires_HSB_Channels(2));
boolean require_HS_Channels_1= (colorSubstitutionMode.requires_HS_Channels(1));
boolean require_HS_Channels_2= (colorSubstitutionMode.requires_HS_Channels(2));
//
for (int w1=0; w1 < frameWidth; w1++) {
	if (lookupArray != null && w1 >= lookupArrayWidth) {
		continue;
	};
	for (int h1=0; h1 < frameHeight; h1++) {
		if (lookupArray != null && h1 >= lookupArrayHeight) {
			continue;
		};
		int index11= h1*frameWidth + w1;
		if (extractPlayers && playerIndex[index11] < 0) {
			continue;
		};
		float z11= cloud[index11*3+2];
		if (z11 < minimalDepth) {
			continue;
		};
		int w2;
		int h2;
		if (lookupArray != null) {
			if (isVolumetricLookupTable) {
				float depth= 1 / z11;
				float depth2= depth*depth;
				float xp1= lookupArray[w1][h1][0];
				float xp2= lookupArray[w1][h1][1];
				float xp3= lookupArray[w1][h1][2];
				float yp1= lookupArray[w1][h1][3];
				float yp2= lookupArray[w1][h1][4];
				float yp3= lookupArray[w1][h1][5];
				float w2f= xp1*depth2 + xp2*depth + xp3;
				float h2f= yp1*depth2 + yp2*depth + yp3;
				w2= (int)StrictMath.round(w2f) + correctionX;
				h2= (int)StrictMath.round(h2f) + correctionY;
			} else {
				w2= (int)StrictMath.round(lookupArray[w1][h1][0]) + correctionX;
				h2= (int)StrictMath.round(lookupArray[w1][h1][1]) + correctionY;
			}
		} else {
			w2= w1;
			h2= h1;
		};
		if (w2 < 0 || w2 >= textureWidth) {
			continue;
		};
		if (h2 < 0 || h2 >= textureHeight) {
			continue;
		};
		int index= h2 * textureWidth + w2;
		if (index >= 0 && index < rgbArrayLength) {
//=====================================================================
int colorIndex= (int)(colorMapSize * quantiles.standardizeByExtremes(z11));
if (colorIndex < 0) {
	colorIndex= 0;
} else if (colorIndex > colorMapSize-1) {
	colorIndex= colorMapSize-1;
};
int r= colorMapColors[0][colorIndex];
int g= colorMapColors[1][colorIndex];
int b= colorMapColors[2][colorIndex];
//
int hue256= 0;
int saturation256= 0;
int brightness256= 0;
//
if (require_HSB_Channels_1) {
// System.err.printf("[1]:require_HSB_Channels_1\n");
	int cmax= (r > g) ? r : g;
	if (b > cmax) {
		cmax= b;
	};
	int cmin= (r < g) ? r : g;
	if (b < cmin) {
		cmin= b;
	};
	brightness256= cmax;
	if (require_HS_Channels_1) {
// System.err.printf("[2]:require_HS_Channels_1\n");
		if (cmax != 0) {
			saturation256= (cmax - cmin) * maximalColorValueInteger / cmax;
		} else {
			saturation256= 0;
		};
		if (saturation256 == 0) {
			hue256= 0;
		} else {
			int redc= (cmax - r) * maximalColorValueInteger / (cmax - cmin);
			int greenc= (cmax - g) * maximalColorValueInteger / (cmax - cmin);
			int bluec= (cmax - b) * maximalColorValueInteger / (cmax - cmin);
			if (r == cmax) {
				hue256= bluec - greenc;
			} else if (g == cmax) {
				hue256= maximalColorValueInteger * 2 + redc - bluec;
			} else {
				hue256= maximalColorValueInteger * 4 + greenc - redc;
			};
			hue256= hue256 / 6;
			if (hue256 < 0) {
				hue256= hue256 + maximalColorValueInteger;
			}
		}
	}
}
//
int rgb= rgbArray[index];
int textureValueAlpha= rgb >>> 24 & 0x000000FF;
int textureValueRed= rgb >>> 16 & 0x000000FF;
int textureValueGreen= rgb >>> 8 & 0x000000FF;
int textureValueBlue= rgb & 0x000000FF;
//
int hueT= 0;
int saturationT= 0;
int brightnessT= 0;
//
if (require_HSB_Channels_2) {
// System.err.printf("[3]:require_HSB_Channels_2\n");
	int cmax= (textureValueRed > textureValueGreen) ? textureValueRed : textureValueGreen;
	if (textureValueBlue > cmax) {
		cmax= textureValueBlue;
	};
	int cmin= (textureValueRed < textureValueGreen) ? textureValueRed : textureValueGreen;
	if (textureValueBlue < cmin) {
		cmin= textureValueBlue;
	};
	brightnessT= cmax;
	if (require_HS_Channels_2) {
// System.err.printf("[4]:require_HS_Channels_2\n");
		if (cmax != 0) {
			saturationT= (cmax - cmin) * maximalColorValueInteger / cmax;
		} else {
			saturationT= 0;
		};
		if (saturationT == 0) {
			hueT= 0;
		} else {
			int redc= (cmax - textureValueRed) * maximalColorValueInteger / (cmax - cmin);
			int greenc= (cmax - textureValueGreen) * maximalColorValueInteger / (cmax - cmin);
			int bluec= (cmax - textureValueBlue) * maximalColorValueInteger / (cmax - cmin);
			if (textureValueRed == cmax) {
				hueT= bluec - greenc;
			} else if (textureValueGreen == cmax) {
				hueT= maximalColorValueInteger * 2 + redc - bluec;
			} else {
				hueT= maximalColorValueInteger * 4 + greenc - redc;
			};
			hueT= hueT / 6;
			if (hueT < 0) {
				hueT= hueT + maximalColorValueInteger;
			}
		}
	}
}
//
int outputChannelOne= 0;
int outputChannelTwo= 0;
int outputChannelThree= 0;
//
// System.err.printf("[5]:firstChannelSubstitutionIsNamed=%s\n",firstChannelSubstitutionIsNamed);
if (firstChannelSubstitutionIsNamed) {
	switch (firstChannelSubstitutionName) {
	case RED1:
		outputChannelOne= r;
		break;
	case GREEN1:
		outputChannelOne= g;
		break;
	case BLUE1:
		outputChannelOne= b;
		break;
	case RED2:
		outputChannelOne= textureValueRed;
		break;
	case GREEN2:
		outputChannelOne= textureValueGreen;
		break;
	case BLUE2:
		outputChannelOne= textureValueBlue;
		break;
	case HUE1:
		outputChannelOne= hue256;
		break;
	case SATURATION1:
		outputChannelOne= saturation256;
		break;
	case BRIGHTNESS1:
		outputChannelOne= brightness256;
		break;
	case HUE2:
		outputChannelOne= hueT;
		break;
	case SATURATION2:
		outputChannelOne= saturationT;
		break;
	case BRIGHTNESS2:
		outputChannelOne= brightnessT;
		break;
	case ZERO:
		outputChannelOne= 0;
		break;
	case FULL:
		outputChannelOne= maximalColorValueInteger;
		break;
	case HALF:
		outputChannelOne= halfColorValueInteger;
		break;
	default:
		outputChannelOne= r;
		break;
	}
} else {
	outputChannelOne= firstChannelSubstitutionValue;
};
// System.err.printf("[6]:secondChannelSubstitutionIsNamed=%s\n",secondChannelSubstitutionIsNamed);
if (secondChannelSubstitutionIsNamed) {
	switch (secondChannelSubstitutionName) {
	case RED1:
		outputChannelTwo= r;
		break;
	case GREEN1:
		outputChannelTwo= g;
		break;
	case BLUE1:
		outputChannelTwo= b;
		break;
	case RED2:
		outputChannelTwo= textureValueRed;
		break;
	case GREEN2:
		outputChannelTwo= textureValueGreen;
		break;
	case BLUE2:
		outputChannelTwo= textureValueBlue;
		break;
	case HUE1:
		outputChannelTwo= hue256;
		break;
	case SATURATION1:
		outputChannelTwo= saturation256;
		break;
	case BRIGHTNESS1:
		outputChannelTwo= brightness256;
		break;
	case HUE2:
		outputChannelTwo= hueT;
		break;
	case SATURATION2:
		outputChannelTwo= saturationT;
		break;
	case BRIGHTNESS2:
		outputChannelTwo= brightnessT;
		break;
	case ZERO:
		outputChannelTwo= 0;
		break;
	case FULL:
		outputChannelTwo= maximalColorValueInteger;
		break;
	case HALF:
		outputChannelTwo= halfColorValueInteger;
		break;
	default:
		outputChannelTwo= g;
		break;
	}
} else {
	outputChannelTwo= secondChannelSubstitutionValue;
};
// System.err.printf("[7]:thirdChannelSubstitutionIsNamed=%s\n",thirdChannelSubstitutionIsNamed);
if (thirdChannelSubstitutionIsNamed) {
	switch (thirdChannelSubstitutionName) {
	case RED1:
		outputChannelThree= r;
		break;
	case GREEN1:
		outputChannelThree= g;
		break;
	case BLUE1:
		outputChannelThree= b;
		break;
	case RED2:
		outputChannelThree= textureValueRed;
		break;
	case GREEN2:
		outputChannelThree= textureValueGreen;
		break;
	case BLUE2:
		outputChannelThree= textureValueBlue;
		break;
	case HUE1:
		outputChannelThree= hue256;
		break;
	case SATURATION1:
		outputChannelThree= saturation256;
		break;
	case BRIGHTNESS1:
		outputChannelThree= brightness256;
		break;
	case HUE2:
		outputChannelThree= hueT;
		break;
	case SATURATION2:
		outputChannelThree= saturationT;
		break;
	case BRIGHTNESS2:
		outputChannelThree= brightnessT;
		break;
	case ZERO:
		outputChannelThree= 0;
		break;
	case FULL:
		outputChannelThree= maximalColorValueInteger;
		break;
	case HALF:
		outputChannelThree= halfColorValueInteger;
		break;
	default:
		outputChannelThree= b;
		break;
	}
} else {
	outputChannelThree= thirdChannelSubstitutionValue;
};
//
if (!use_RGB_Mode) {
// System.err.printf("[8]:!use_RGB_Mode\n");
	float hue= (float)outputChannelOne / maximalColorValueInteger;
	float saturation= (float)outputChannelTwo / maximalColorValueInteger;
	float brightness= (float)outputChannelThree / maximalColorValueInteger;
	hue= 6 * hue;
	int k= (int)hue;
	float p= hue - k;
	float t= 1 - saturation;
	float n= 1 - saturation * p;
	p= 1 - (saturation * (1 - p));
	float floatRed= 0;
	float floatGreen= 0;
	float floatBlue= 0;
	switch (k) {
	case 0:
	case 6:
		floatRed= 1;
		floatGreen= p;
		floatBlue= t;
		break;
	case 1:
		floatRed= floatRed + n;
		floatGreen= floatGreen + 1;
		floatBlue= floatBlue + t;
		break;
	case 2:
		floatRed= floatRed + t;
		floatGreen= floatGreen + 1;
		floatBlue= floatBlue + p;
		break;
	case 3:
		floatRed= floatRed + t;
		floatGreen= floatGreen + n;
		floatBlue= floatBlue + 1;
		break;
	case 4:
		floatRed= floatRed + p;
		floatGreen= floatGreen + t;
		floatBlue= floatBlue + 1;
		break;
	case 5:
		floatRed= floatRed + 1;
		floatGreen= floatGreen + t;
		floatBlue= floatBlue + n;
		break;
	};
	float max= floatRed;
	if (max < floatGreen) {
		max= floatGreen;
	};
	if (max < floatBlue) {
		max= floatBlue;
	};
	float f= brightness / max;
	float rout= f * floatRed;
	floatGreen= f * floatGreen;
	floatBlue= f * floatBlue;
	outputChannelOne= (int)(rout*maximalColorValueInteger);
	outputChannelTwo= (int)(floatGreen*maximalColorValueInteger);
	outputChannelThree= (int)(floatBlue*maximalColorValueInteger);
};
red[index11]= (byte)outputChannelOne;
green[index11]= (byte)outputChannelTwo;
blue[index11]= (byte)outputChannelThree;
alpha[index11]= (byte)textureValueAlpha;
if (textureValueAlpha == 0) {
	hasTransparentPixels= true;
}
//=====================================================================
		}
	}
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
} else { // !doUsePseudocolors
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//red= new byte[frameWidth][frameHeight];
//green= new byte[frameWidth][frameHeight];
//blue= new byte[frameWidth][frameHeight];
//alpha= new byte[frameWidth][frameHeight];
int textureWidth= nativeTextureImage.getWidth();
int textureHeight= nativeTextureImage.getHeight();
int[] rgbArray= nativeTextureImage.getRGB(0,0,textureWidth,textureHeight,null,0,textureWidth);
int rgbArrayLength= rgbArray.length;
boolean isVolumetricLookupTable= lookupTable.isVolumetricLookupTable();
float[][][] lookupArray= lookupTable.getLookupTable();
int correctionX= lookupTable.getCorrectionX();
int correctionY= lookupTable.getCorrectionY();
int lookupArrayWidth= 0;
int lookupArrayHeight= 0;
if (lookupArray != null) {
	lookupArrayWidth= lookupArray.length;
	if (lookupArrayWidth > 0) {
		lookupArrayHeight= lookupArray[0].length;
	}
};
for (int w1=0; w1 < frameWidth; w1++) {
	if (lookupArray != null && w1 >= lookupArrayWidth) {
		continue;
	};
	for (int h1=0; h1 < frameHeight; h1++) {
		if (lookupArray != null && h1 >= lookupArrayHeight) {
			continue;
		};
		int index11= h1*frameWidth + w1;
		if (extractPlayers && playerIndex[index11] < 0) {
			continue;
		};
		float z11= cloud[index11*3+2];
		if (z11 < minimalDepth) {
			continue;
		};
		int w2;
		int h2;
		if (lookupArray != null) {
			if (isVolumetricLookupTable) {
				float depth= 1 / z11;
				float depth2= depth*depth;
				float xp1= lookupArray[w1][h1][0];
				float xp2= lookupArray[w1][h1][1];
				float xp3= lookupArray[w1][h1][2];
				float yp1= lookupArray[w1][h1][3];
				float yp2= lookupArray[w1][h1][4];
				float yp3= lookupArray[w1][h1][5];
				float w2f= xp1*depth2 + xp2*depth + xp3;
				float h2f= yp1*depth2 + yp2*depth + yp3;
				w2= (int)StrictMath.round(w2f) + correctionX;
				h2= (int)StrictMath.round(h2f) + correctionY;
			} else {
				w2= (int)StrictMath.round(lookupArray[w1][h1][0]) + correctionX;
				h2= (int)StrictMath.round(lookupArray[w1][h1][1]) + correctionY;
			}
		} else {
			w2= w1;
			h2= h1;
		};
		if (w2 < 0 || w2 >= textureWidth) {
			continue;
		};
		if (h2 < 0 || h2 >= textureHeight) {
			continue;
		};
		int index= h2 * textureWidth + w2;
		if (index >= 0 && index < rgbArrayLength) {
			int rgb= rgbArray[index];
			byte valueAlpha= (byte)(rgb >>> 24 & 0x000000FF);
			byte valueRed= (byte)(rgb >>> 16 & 0x000000FF);
			byte valueGreen= (byte)(rgb >>> 8 & 0x000000FF);
			byte valueBlue= (byte)(rgb & 0x000000FF);
			//red[w1][h1]= valueRed;
			//green[w1][h1]= valueGreen;
			//blue[w1][h1]= valueBlue;
			//alpha[w1][h1]= valueAlpha;
			red[index11]= valueRed;
			green[index11]= valueGreen;
			blue[index11]= valueBlue;
			alpha[index11]= valueAlpha;
			if (valueAlpha == 0) {
				hasTransparentPixels= true; // 2019-07-06: false;
			}
		}
	}
}
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
}
///////////////////////////////////////////////////////////////////////
		};
		return hasTransparentPixels;
	}
}
