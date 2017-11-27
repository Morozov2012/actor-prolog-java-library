// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.text;

import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class KinectLongExposureInfraredFrameText {
	//
	public static void writeText(KinectLongExposureInfraredFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		if (exportMode==ExportMode.SKELETONS) {
			// super.writeText(writer,exportMode,locale);
			KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		} else {
			writeLongExposureInfraredFrameText(frame,writer,exportMode,locale);
		}
	}
	//
	public static void writeLongExposureInfraredFrameText(KinectLongExposureInfraredFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; LONG EXPOSURE INFRARED FRAME:\n");
		// super.writeText(writer,exportMode,locale);
		KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		short[] longExposureInfrared= frame.getLongExposureInfrared();
		writer.write(String.format(locale,"; Long exposure infrared matrix: %d\n",longExposureInfrared.length));
		for (int n=0; n < longExposureInfrared.length; n++) {
			if (n > 0) {
				writer.write(String.format(locale,"\t%s",longExposureInfrared[n]));
			} else {
				writer.write(String.format(locale,"%s",longExposureInfrared[n]));
			}
		};
		writer.write("\n");
		writer.write("; End of long exposure infrared frame.\n");
	}
}
