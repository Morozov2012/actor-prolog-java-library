// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.text;

import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class KinectPointCloudsFrameText {
	//
	public static void writeText(KinectPointCloudsFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		if (exportMode==ExportMode.SKELETONS) {
			// super.writeText(writer,exportMode,locale);
			KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		} else {
			writePointCloudsFrameText(frame,writer,exportMode,locale);
		}
	}
	//
	public static void writePointCloudsFrameText(KinectPointCloudsFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; POINT CLOUDS FRAME:\n");
		// super.writeText(writer,exportMode,locale);
		KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		float[] xyz= frame.getXYZ();
		if (xyz != null) {
			writer.write(String.format(locale,"; XYZ matrix: %d\n",xyz.length));
			for (int n=0; n < xyz.length; n++) {
				if (n > 0) {
					writer.write(String.format(locale,"\t%s",xyz[n]));
				} else {
					writer.write(String.format(locale,"%s",xyz[n]));
				}
			};
			writer.write("\n");
		} else {
			writer.write("; No XYZ matrix.\n");
		};
		writer.write("; End of point clouds frame.\n");
	}
}
