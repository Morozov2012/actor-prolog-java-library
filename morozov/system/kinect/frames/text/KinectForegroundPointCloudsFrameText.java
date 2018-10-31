// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.text;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.data.tools.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class KinectForegroundPointCloudsFrameText {
	//
	public static void writeText(KinectForegroundPointCloudsFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		if (exportMode==ExportMode.SKELETONS) {
			// super.writeText(writer,exportMode,locale);
			KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		} else {
			writeForegroundPointCloudsFrameText(frame,writer,exportMode,locale);
		}
	}
	//
	public static void writeForegroundPointCloudsFrameText(KinectForegroundPointCloudsFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; FOREGROUND POINT CLOUDS FRAME:\n");
		// super.writeText(writer,exportMode,locale);
		KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		int expectedNumberOfSkeletons= frame.getNumberOfSkeletons();
		ForegroundPointCloudInterface[] pointClouds= frame.getPointClouds();
		writer.write(String.format(locale,"; frameWidth= %s\n",frame.getDepthFrameWidth()));
		writer.write(String.format(locale,"; frameHeight= %s\n",frame.getDepthFrameHeight()));
		writer.write(String.format(locale,"; numberOfSkeletons= %s\n",expectedNumberOfSkeletons));
		writer.write(String.format(locale,"; Point clouds: %s\n",expectedNumberOfSkeletons));
		for (int n=0; n < expectedNumberOfSkeletons; n++) {
			writer.write(String.format(locale,"; cloud %s\n",n));
			ForegroundPointCloudTools.writeText(pointClouds[n],writer,exportMode,locale);
		};
		writer.write("; End of foreground point clouds frame.\n");
	}
}
