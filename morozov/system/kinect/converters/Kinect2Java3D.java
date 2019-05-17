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
	protected static float maximalColorValue= 255.0f;
	protected static BranchGroup emptyBranchGroup= new BranchGroup();
	//
	static {
		emptyBranchGroup.compile(); // 2019-04-07
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void setBufferedScene(BufferedScene scene, KinectFrameInterface committedFrame, KinectDisplayingModeInterface displayingMode, KinectColorMap colorMap, KinectColorMap peopleColors, NumericalValue sceneDepthThreshold, KinectSurfaceType sceneSurfaceType, Appearance sceneAppearance) {
		setBufferedScene(scene,committedFrame,displayingMode,colorMap,peopleColors,null,null,sceneDepthThreshold,sceneSurfaceType,sceneAppearance);
	}
	public static void setBufferedScene(BufferedScene scene, KinectFrameInterface committedFrame, KinectDisplayingModeInterface displayingMode, KinectColorMap colorMap, KinectColorMap peopleColors, java.awt.image.BufferedImage nativeTextureImage, KinectLookupTable lookupTable, NumericalValue sceneDepthThreshold, KinectSurfaceType sceneSurfaceType, Appearance sceneAppearance) {
		if (committedFrame instanceof KinectPointCloudsFrameInterface) {
			KinectPointCloudsFrameInterface pointCloudsFrame= (KinectPointCloudsFrameInterface)committedFrame;
			BranchGroup branchGroup= computeBufferedScene(pointCloudsFrame,displayingMode,colorMap,peopleColors,nativeTextureImage,lookupTable,sceneDepthThreshold,sceneSurfaceType,sceneAppearance);
			scene.setBranchGroup(branchGroup);
		} else {
			scene.setBranchGroup(emptyBranchGroup);
		}
	}
	public static BranchGroup computeBufferedScene(KinectPointCloudsFrameInterface pointCloudsFrame, KinectDisplayingModeInterface displayingMode, KinectColorMap colorMap, KinectColorMap peopleColors, java.awt.image.BufferedImage nativeTextureImage, KinectLookupTable lookupTable, NumericalValue sceneDepthThreshold, KinectSurfaceType sceneSurfaceType, Appearance sceneAppearance) {
		KinectPeopleIndexMode peopleIndexMode= displayingMode.getActingPeopleIndexMode();
		ColorMapName colorMapName= colorMap.getColorMapName();
		boolean extractPlayers= peopleIndexMode.peopleAreToBeExtracted();
		boolean mapColors= (colorMapName==ColorMapName.OPTICAL);
		float[] cloud= pointCloudsFrame.getXYZ();
		FrameSize frameSize= FrameSize.computeXYZFrameSize(cloud);
		int frameWidth= frameSize.getWidth();
		int frameHeight= frameSize.getHeight();
		byte[] playerIndex= pointCloudsFrame.getPlayerIndex();
		byte[][] red;
		byte[][] green;
		byte[][] blue;
		byte[][] alpha;
		boolean hasTransparentPixels= false;
		if (nativeTextureImage==null) {
			red= pointCloudsFrame.getMappedRed();
			green= pointCloudsFrame.getMappedGreen();
			blue= pointCloudsFrame.getMappedBlue();
			alpha= new byte[frameWidth][frameHeight];
		} else {
///////////////////////////////////////////////////////////////////////
red= new byte[frameWidth][frameHeight];
green= new byte[frameWidth][frameHeight];
blue= new byte[frameWidth][frameHeight];
alpha= new byte[frameWidth][frameHeight];
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
		int w2;
		int h2;
		if (lookupArray != null) {
			if (isVolumetricLookupTable) {
				int index11= h1*frameWidth + w1;
				if (extractPlayers && playerIndex[index11] < 0) {
					continue;
				};
				float z11= cloud[index11*3+2];
				if (z11 < minimalDepth) {
					continue;
				};
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
			red[w1][h1]= valueRed;
			green[w1][h1]= valueGreen;
			blue[w1][h1]= valueBlue;
			alpha[w1][h1]= valueAlpha;
			if (valueAlpha > 0) {
				hasTransparentPixels= false;
			}
		}
	}
}
///////////////////////////////////////////////////////////////////////
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
		if (mapColors) {
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
///////////////////////////////////////////////////////////////////////
quads[quadIndex]= new Point3f(-x12,y12,-z12);
if (mapColors) {
	float floatRed12= (float)(red[x+1][y] & 0xFF) / maximalColorValue;
	float floatGreen12= (float)(green[x+1][y] & 0xFF) / maximalColorValue;
	float floatBlue12= (float)(blue[x+1][y] & 0xFF) / maximalColorValue;
	float floatAlpha12= (float)(alpha[x+1][y] & 0xFF) / maximalColorValue;
	Color4f color12= new Color4f(floatRed12,floatGreen12,floatBlue12,floatAlpha12);
	colors[quadIndex]= color12;
};
quadIndex++;
//
quads[quadIndex]= new Point3f(-x22,y22,-z22);
if (mapColors) {
	float floatRed22= (float)(red[x+1][y+1] & 0xFF) / maximalColorValue;
	float floatGreen22= (float)(green[x+1][y+1] & 0xFF) / maximalColorValue;
	float floatBlue22= (float)(blue[x+1][y+1] & 0xFF) / maximalColorValue;
	float floatAlpha22= (float)(alpha[x+1][y+1] & 0xFF) / maximalColorValue;
	Color4f color22= new Color4f(floatRed22,floatGreen22,floatBlue22,floatAlpha22);
	colors[quadIndex]= color22;
};
quadIndex++;
//
quads[quadIndex]= new Point3f(-x21,y21,-z21);
if (mapColors) {
	float floatRed21= (float)(red[x][y+1] & 0xFF) / maximalColorValue;
	float floatGreen21= (float)(green[x][y+1] & 0xFF) / maximalColorValue;
	float floatBlue21= (float)(blue[x][y+1] & 0xFF) / maximalColorValue;
	float floatAlpha21= (float)(alpha[x][y+1] & 0xFF) / maximalColorValue;
	Color4f color21= new Color4f(floatRed21,floatGreen21,floatBlue21,floatAlpha21);
	colors[quadIndex]= color21;
};
quadIndex++;
//
quads[quadIndex]= new Point3f(-x11,y11,-z11);
if (mapColors) {
	float floatRed11= (float)(red[x][y] & 0xFF) / maximalColorValue;
	float floatGreen11= (float)(green[x][y] & 0xFF) / maximalColorValue;
	float floatBlue11= (float)(blue[x][y] & 0xFF) / maximalColorValue;
	float floatAlpha11= (float)(alpha[x][y] & 0xFF) / maximalColorValue;
	Color4f color11= new Color4f(floatRed11,floatGreen11,floatBlue11,floatAlpha11);
	colors[quadIndex]= color11;
};
quadIndex++;
///////////////////////////////////////////////////////////////////////
					//
				} else {
					//
///////////////////////////////////////////////////////////////////////
quads[quadIndex]= new Point3f(-x11,y11,-z11);
if (mapColors) {
	float floatRed11= (float)(red[x][y] & 0xFF) / maximalColorValue;
	float floatGreen11= (float)(green[x][y] & 0xFF) / maximalColorValue;
	float floatBlue11= (float)(blue[x][y] & 0xFF) / maximalColorValue;
	float floatAlpha11= (float)(alpha[x][y] & 0xFF) / maximalColorValue;
	Color4f color11= new Color4f(floatRed11,floatGreen11,floatBlue11,floatAlpha11);
	colors[quadIndex]= color11;
};
quadIndex++;
//
quads[quadIndex]= new Point3f(-x21,y21,-z21);
if (mapColors) {
	float floatRed21= (float)(red[x][y+1] & 0xFF) / maximalColorValue;
	float floatGreen21= (float)(green[x][y+1] & 0xFF) / maximalColorValue;
	float floatBlue21= (float)(blue[x][y+1] & 0xFF) / maximalColorValue;
	float floatAlpha21= (float)(alpha[x][y+1] & 0xFF) / maximalColorValue;
	Color4f color21= new Color4f(floatRed21,floatGreen21,floatBlue21,floatAlpha21);
	colors[quadIndex]= color21;
};
quadIndex++;
//
quads[quadIndex]= new Point3f(-x22,y22,-z22);
if (mapColors) {
	float floatRed22= (float)(red[x+1][y+1] & 0xFF) / maximalColorValue;
	float floatGreen22= (float)(green[x+1][y+1] & 0xFF) / maximalColorValue;
	float floatBlue22= (float)(blue[x+1][y+1] & 0xFF) / maximalColorValue;
	float floatAlpha22= (float)(alpha[x+1][y+1] & 0xFF) / maximalColorValue;
	Color4f color22= new Color4f(floatRed22,floatGreen22,floatBlue22,floatAlpha22);
	colors[quadIndex]= color22;
};
quadIndex++;
//
quads[quadIndex]= new Point3f(-x12,y12,-z12);
if (mapColors) {
	float floatRed12= (float)(red[x+1][y] & 0xFF) / maximalColorValue;
	float floatGreen12= (float)(green[x+1][y] & 0xFF) / maximalColorValue;
	float floatBlue12= (float)(blue[x+1][y] & 0xFF) / maximalColorValue;
	float floatAlpha12= (float)(alpha[x+1][y] & 0xFF) / maximalColorValue;
	Color4f color12= new Color4f(floatRed12,floatGreen12,floatBlue12,floatAlpha12);
	colors[quadIndex]= color12;
};
quadIndex++;
///////////////////////////////////////////////////////////////////////
					//
				}
			}
		};
		quads= Arrays.copyOfRange(quads,0,quadIndex,quads.getClass());
		BranchGroup branchGroup= new BranchGroup();
		if (quadIndex > 0) {
			geometryInfo.setCoordinates(quads);
			if (mapColors) {
				colors= Arrays.copyOfRange(colors,0,quadIndex,colors.getClass());
				geometryInfo.setColors(colors);
			} else {
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
				// new TransparencyAttributes(TransparencyAttributes.BLENDED,0));
				// new TransparencyAttributes(TransparencyAttributes.NICEST,0));
				// new TransparencyAttributes(TransparencyAttributes.FASTEST,0.9f));
				new TransparencyAttributes(
					TransparencyAttributes.FASTEST,
					// TransparencyAttributes.NICEST,
					0.0f));
				// new TransparencyAttributes(TransparencyAttributes.SCREEN_DOOR,0));
				// new TransparencyAttributes(TransparencyAttributes.NONE,0));
		};
		return materialAppear;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static java.awt.image.BufferedImage computeMappedImage(KinectFrameInterface committedFrame, KinectDisplayingModeInterface displayingMode, KinectColorMap peopleColors, java.awt.image.BufferedImage nativeTextureImage, KinectLookupTable lookupTable) {
		if (committedFrame instanceof KinectPointCloudsFrameInterface) {
			KinectPointCloudsFrameInterface pointCloudsFrame= (KinectPointCloudsFrameInterface)committedFrame;
			return computeMappedImage(pointCloudsFrame,displayingMode,peopleColors,nativeTextureImage,lookupTable);
		} else {
			return null;
		}
	}
	public static java.awt.image.BufferedImage computeMappedImage(KinectPointCloudsFrameInterface pointCloudsFrame, KinectDisplayingModeInterface displayingMode, KinectColorMap peopleColors, java.awt.image.BufferedImage nativeTextureImage, KinectLookupTable lookupTable) {
		KinectPeopleIndexMode peopleIndexMode= displayingMode.getActingPeopleIndexMode();
		boolean extractPlayers= peopleIndexMode.peopleAreToBeExtracted();
		float[] cloud= pointCloudsFrame.getXYZ();
		FrameSize frameSize= FrameSize.computeXYZFrameSize(cloud);
		int frameWidth= frameSize.getWidth();
		int frameHeight= frameSize.getHeight();
		byte[] playerIndex= pointCloudsFrame.getPlayerIndex();
		int frameLength= frameWidth*frameHeight;
		int[] red= new int[frameLength];
		int[] green= new int[frameLength];
		int[] blue= new int[frameLength];
		if (nativeTextureImage==null) {
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
		} else {
///////////////////////////////////////////////////////////////////////
int textureWidth= nativeTextureImage.getWidth();
int textureHeight= nativeTextureImage.getHeight();
int[] rgbArray= nativeTextureImage.getRGB(0,0,textureWidth,textureHeight,null,0,textureWidth);
int rgbArrayLength= rgbArray.length;
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
		int w2;
		int h2;
		if (lookupArray != null) {
			int index11= h1*frameWidth + w1;
			if (extractPlayers && playerIndex[index11] < 0) {
				continue;
			};
			float z11= cloud[index11*3+2];
			if (z11 < minimalDepth) {
				continue;
			};
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
			if (w2 < 0 || w2 >= textureWidth) {
				continue;
			};
			if (h2 < 0 || h2 >= textureHeight) {
				continue;
			}
		} else {
			w2= w1;
			h2= h1;
		};
		int index1= h2 * textureWidth + w2;
		if (index1 >= 0 && index1 < rgbArrayLength) {
			int rgb= rgbArray[index1];
			byte valueRed= (byte)(rgb >>> 16 & 0x000000FF);
			byte valueGreen= (byte)(rgb >>> 8 & 0x000000FF);
			byte valueBlue= (byte)(rgb & 0x000000FF);
			int index2= h1*frameWidth + frameWidth - 1 - w1;
			red[index2]= valueRed;
			green[index2]= valueGreen;
			blue[index2]= valueBlue;
		}
	}
}
///////////////////////////////////////////////////////////////////////
		};
		java.awt.image.BufferedImage resultImage= new java.awt.image.BufferedImage(frameWidth,frameHeight,java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster resultRaster= resultImage.getRaster();
		resultRaster.setSamples(0,0,frameWidth,frameHeight,0,red);
		resultRaster.setSamples(0,0,frameWidth,frameHeight,1,green);
		resultRaster.setSamples(0,0,frameWidth,frameHeight,2,blue);
		return resultImage;
	}
}
