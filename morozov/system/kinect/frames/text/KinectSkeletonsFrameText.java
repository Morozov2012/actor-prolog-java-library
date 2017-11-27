// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.text;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.data.tools.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class KinectSkeletonsFrameText {
	//
	public static boolean hasTrackedSkeletons(GeneralSkeletonInterface[] skeletons) {
		if (skeletons != null) {
			for (int n=0; n < skeletons.length; n++) {
				if (skeletons[n].isTracked()) {
					return true;
				}
			};
			return false;
		} else {
			return false;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeText(KinectFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		if (exportMode==ExportMode.SKELETONS) {
			writeSkeletonsText(frame,writer,exportMode,locale);
		} else {
			writeSkeletonsFrameText(frame,writer,exportMode,locale);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeSkeletonsText(KinectSkeletonsFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
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
	public static void writeSkeletonsFrameText(KinectSkeletonsFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; SKELETONS FRAME:\n");
		writer.write(String.format(locale,"; skeletonsFrameNumber: %s\n",frame.getSkeletonsFrameNumber()));
		writer.write(String.format(locale,"; skeletonsFrameTime: %s\n",frame.getSkeletonsFrameTime()));
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
		writer.write(String.format(locale,"; deviceType: %s\n",frame.getDeviceType()));
		writer.write(String.format(locale,"; focalLengthX: %s\n",frame.getFocalLengthX()));
		writer.write(String.format(locale,"; focalLengthY: %s\n",frame.getFocalLengthY()));
		writer.write(String.format(locale,"; depthFrameWidth: %s\n",frame.getDepthFrameWidth()));
		writer.write(String.format(locale,"; depthFrameHeight: %s\n",frame.getDepthFrameHeight()));
		writer.write(String.format(locale,"; colorFrameWidth: %s\n",frame.getColorFrameWidth()));
		writer.write(String.format(locale,"; colorFrameHeight: %s\n",frame.getColorFrameHeight()));
		writer.write("; End of skeletons frame.\n");
	}
}
