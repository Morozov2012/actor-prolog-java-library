// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.text;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.data.tools.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class KinectFrameText {
	//
	public static void writeText(KinectFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		if (exportMode==ExportMode.SKELETONS) {
			writeSkeletonsText(frame,writer,exportMode,locale);
		} else {
			if (frame instanceof KinectModeFrameInterface) {
				KinectModeFrameText.writeText((KinectModeFrameInterface)frame,writer,exportMode,locale);
			} else if (frame instanceof KinectDepthFrameInterface) {
				KinectDepthFrameText.writeText((KinectDepthFrameInterface)frame,writer,exportMode,locale);
			} else if (frame instanceof KinectInfraredFrameInterface) {
				KinectInfraredFrameText.writeText((KinectInfraredFrameInterface)frame,writer,exportMode,locale);
			} else if (frame instanceof KinectLongExposureInfraredFrameInterface) {
				KinectLongExposureInfraredFrameText.writeText((KinectLongExposureInfraredFrameInterface)frame,writer,exportMode,locale);
			} else if (frame instanceof KinectMappedColorFrameInterface) {
				KinectMappedColorFrameText.writeText((KinectMappedColorFrameInterface)frame,writer,exportMode,locale);
			} else if (frame instanceof KinectPointCloudsFrameInterface) {
				KinectPointCloudsFrameText.writeText((KinectPointCloudsFrameInterface)frame,writer,exportMode,locale);
			} else if (frame instanceof KinectColorFrameInterface) {
				KinectColorFrameText.writeText((KinectColorFrameInterface)frame,writer,exportMode,locale);
			} else {
				writeRootFrameText(frame,writer,exportMode,locale);
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeSkeletonsText(KinectFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		GeneralSkeletonInterface[] skeletons= frame.getSkeletons();
		writer.write("[");
		if (skeletons != null) {
			boolean isFrontElement= true;
			for (int n=0; n < skeletons.length; n++) {
				GeneralSkeletonInterface skeleton= skeletons[n];
				if (skeleton.isTracked()) {
					if (!isFrontElement) {
						writer.write(",");
					};
					GeneralSkeletonTools.writeText(skeleton,writer,exportMode,locale);
					isFrontElement= false;
				}
			}
		};
		writer.write("]");
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeRootFrameText(KinectFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; ROOT FRAME:\n");
		writer.write(String.format(locale,"; dataArrayType: %s\n",frame.getDataArrayType()));
		writer.write(String.format(locale,"; serialNumber: %s\n",frame.getSerialNumber()));
		writer.write(String.format(locale,"; targetFrameNumber: %s\n",frame.getTargetFrameNumber()));
		writer.write(String.format(locale,"; colorFrameNumber: %s\n",frame.getColorFrameNumber()));
		writer.write(String.format(locale,"; skeletonsFrameNumber: %s\n",frame.getSkeletonsFrameNumber()));
		writer.write(String.format(locale,"; actingFrameNumber: %s\n",frame.getActingFrameNumber()));
		writer.write(String.format(locale,"; isProcessed= %s\n",frame.isProcessed()));
		writer.write(String.format(locale,"; targetFrameTime: %s\n",frame.getTargetFrameTime()));
		writer.write(String.format(locale,"; colorFrameTime: %s\n",frame.getColorFrameTime()));
		writer.write(String.format(locale,"; skeletonsFrameTime: %s\n",frame.getSkeletonsFrameTime()));
		writer.write(String.format(locale,"; actingFrameTime: %s\n",frame.getActingFrameTime()));
		GeneralSkeletonInterface[] skeletons= frame.getSkeletons();
		if (skeletons != null) {
			writer.write(String.format(locale,"; skeletons: %d\n",skeletons.length));
			for (int n=0; n < skeletons.length; n++) {
				GeneralSkeletonTools.writeText(skeletons[n],writer,exportMode,locale);
			}
		} else {
			writer.write("; No skeletons.\n");
		};
		DimensionsInterface dimensions= frame.getDimensions();
		if (dimensions != null) {
			writer.write("; dimensions:\n");
			DimensionsTools.writeText(dimensions,writer,exportMode,locale);
		} else {
			writer.write("; No dimensions.\n");
		};
		byte[] playerIndex= frame.getPlayerIndex();
		if (playerIndex != null) {
			writer.write(String.format(locale,"; playerIndex: %d\n",playerIndex.length));
			for (int n=0; n < playerIndex.length; n++) {
				if (n > 0) {
					writer.write(String.format(locale,"\t%d",(int)playerIndex[n]));
				} else {
					writer.write(String.format(locale,"%d",(int)playerIndex[n]));
				}
			};
			writer.write("\n");
		} else {
			writer.write("; No player index.\n");
		};
		byte[][] mappedRed= frame.getMappedRed();
		byte[][] mappedGreen= frame.getMappedGreen();
		byte[][] mappedBlue= frame.getMappedBlue();
		writer.write("; Mapped colors:\n");
		if (mappedRed != null && mappedRed.length > 0) {
			int width= mappedRed.length;
			int height= mappedRed[0].length;
			writer.write(String.format(locale,"; Mapped Red color: %s %s\n",width,height));
			for (int h=0; h < height; h++) {
				for (int w=0; w < width; w++) {
					if (w > 0) {
						writer.write(String.format(locale,"\t%s",FrameDrawingBasics.byte2int(mappedRed[w][h])));
					} else {
						writer.write(String.format(locale,"%s",FrameDrawingBasics.byte2int(mappedRed[w][h])));
					}
				};
				writer.write("\n");
			}
		} else {
			writer.write("; No mapped Red color.\n");
		};
		if (mappedGreen != null && mappedGreen.length > 0) {
			int width= mappedGreen.length;
			int height= mappedGreen[0].length;
			writer.write(String.format(locale,"; Mapped Green color: %s %s\n",width,height));
			for (int h=0; h < height; h++) {
				for (int w=0; w < width; w++) {
					if (w > 0) {
						writer.write(String.format(locale,"\t%s",FrameDrawingBasics.byte2int(mappedGreen[w][h])));
					} else {
						writer.write(String.format(locale,"%s",FrameDrawingBasics.byte2int(mappedGreen[w][h])));
					}
				};
				writer.write("\n");
			}
		} else {
			writer.write("; No mapped Green color.\n");
		};
		if (mappedBlue != null && mappedBlue.length > 0) {
			int width= mappedBlue.length;
			int height= mappedBlue[0].length;
			writer.write(String.format(locale,"; Mapped Blue color: %s %s\n",width,height));
			for (int h=0; h < height; h++) {
				for (int w=0; w < width; w++) {
					if (w > 0) {
						writer.write(String.format(locale,"\t%s",FrameDrawingBasics.byte2int(mappedBlue[w][h])));
					} else {
						writer.write(String.format(locale,"%s",FrameDrawingBasics.byte2int(mappedBlue[w][h])));
					}
				};
				writer.write("\n");
			}
		} else {
			writer.write("; No mapped Blue color.\n");
		};
		writer.write(String.format(locale,"; deviceType: %s\n",frame.getDeviceType()));
		writer.write(String.format(locale,"; focalLengthX: %s\n",frame.getFocalLengthX()));
		writer.write(String.format(locale,"; focalLengthY: %s\n",frame.getFocalLengthY()));
		writer.write(String.format(locale,"; depthFrameWidth: %s\n",frame.getDepthFrameWidth()));
		writer.write(String.format(locale,"; depthFrameHeight: %s\n",frame.getDepthFrameHeight()));
		writer.write(String.format(locale,"; colorFrameWidth: %s\n",frame.getColorFrameWidth()));
		writer.write(String.format(locale,"; colorFrameHeight: %s\n",frame.getColorFrameHeight()));
		writer.write("; End of root frame.\n");
	}
}
