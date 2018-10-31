// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.text;

import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class KinectInfraredFrameText {
	//
	public static void writeText(KinectInfraredFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		if (exportMode==ExportMode.SKELETONS) {
			// super.writeText(writer,exportMode,locale);
			KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		} else {
			writeInfraredFrameText(frame,writer,exportMode,locale);
		}
	}
	//
	public static void writeInfraredFrameText(KinectInfraredFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; INFRARED FRAME:\n");
		// super.writeText(writer,exportMode,locale);
		KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		short[] infrared= frame.getInfrared();
		writer.write(String.format(locale,"; Infrared matrix: %d\n",infrared.length));
		for (int n=0; n < infrared.length; n++) {
			if (n > 0) {
				writer.write(String.format(locale,"\t%s",infrared[n]));
			} else {
				writer.write(String.format(locale,"%s",infrared[n]));
			}
		};
		writer.write("\n");
		writer.write("; End of infrared frame.\n");
	}
}
