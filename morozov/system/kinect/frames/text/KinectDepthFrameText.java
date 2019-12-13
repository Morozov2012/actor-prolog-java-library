// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.text;

import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class KinectDepthFrameText {
	//
	public static void writeText(KinectDepthFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		if (exportMode==ExportMode.SKELETONS) {
			KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		} else {
			writeDepthFrameText(frame,writer,exportMode,locale);
		}
	}
	//
	public static void writeDepthFrameText(KinectDepthFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; DEPTH FRAME:\n");
		KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		short[] depth= frame.getDepth();
		writer.write(String.format(locale,"; Depth matrix: %d\n",depth.length));
		for (int n=0; n < depth.length; n++) {
			if (n > 0) {
				writer.write(String.format(locale,"\t%s",depth[n]));
			} else {
				writer.write(String.format(locale,"%s",depth[n]));
			}
		};
		writer.write("\n");
		writer.write("; End of depth frame.\n");
	}
}
