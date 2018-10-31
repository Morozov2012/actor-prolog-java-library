// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.text;

import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class KinectMappedColorFrameText {
	//
	public static void writeText(KinectMappedColorFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		if (exportMode==ExportMode.SKELETONS) {
			// super.writeText(writer,exportMode,locale);
			KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		} else {
			writeMappedColorFrameText(frame,writer,exportMode,locale);
		}
	}
	//
	public static void writeMappedColorFrameText(KinectMappedColorFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; MAPPED COLOR FRAME.\n");
		// super.writeText(writer,exportMode,locale);
		KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		writer.write("; End of mapped color frame.\n");
	}
}
