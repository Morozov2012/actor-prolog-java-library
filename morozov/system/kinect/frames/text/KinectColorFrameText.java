// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.text;

import morozov.system.kinect.frames.data.*;
import morozov.system.kinect.frames.data.tools.*;
import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class KinectColorFrameText {
	//
	public static void writeText(KinectColorFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		if (exportMode==ExportMode.SKELETONS) {
			// super.writeText(frame,writer,exportMode,locale);
			KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		} else {
			writeColorFrameText(frame,writer,exportMode,locale);
		}
	}
	//
	public static void writeColorFrameText(KinectColorFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; COLOR FRAME:\n");
		// super.writeText(writer,exportMode,locale);
		KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		byte[] color= frame.getColor();
		writer.write(String.format(locale,"; Color matrix: %d\n",color.length));
		for (int n=0; n < color.length; n++) {
			if (n > 0) {
				writer.write(String.format(locale,"\t%s",color[n]));
			} else {
				writer.write(String.format(locale,"%s",color[n]));
			}
		};
		writer.write("\n");
		float[][] u= frame.getU();
		float[][] v= frame.getV();
		if (u != null && u.length > 0) {
			int width= u.length;
			int height= u[0].length;
			writer.write(String.format(locale,"; U matrix: %s %s\n",width,height));
			for (int h=0; h < height; h++) {
				for (int w=0; w < width; w++) {
					if (w > 0) {
						writer.write(String.format(locale,"\t%s",u[w][h]));
					} else {
						writer.write(String.format(locale,"%s",u[w][h]));
					}
				};
				writer.write("\n");
			}
		} else {
			writer.write("; U matrix is dummy.\n");
		};
		if (v != null && v.length > 0) {
			int width= v.length;
			int height= v[0].length;
			writer.write(String.format(locale,"; V matrix: %s %s\n",width,height));
			for (int h=0; h < height; h++) {
				for (int w=0; w < width; w++) {
					if (w > 0) {
						writer.write(String.format(locale,"\t%s",v[w][h]));
					} else {
						writer.write(String.format(locale,"%s",v[w][h]));
					}
				};
				writer.write("\n");
			}
		} else {
			writer.write("; V matrix is dummy.\n");
		};
		writer.write("; End of color frame.\n");
	}
}
