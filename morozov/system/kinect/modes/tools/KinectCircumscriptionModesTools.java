// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.tools;

import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.interfaces.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class KinectCircumscriptionModesTools {
	//
	public static void refineDataAcquisitionMode(KinectCircumscriptionMode[] items, ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectFrameType frameType) {
		for (int n=0; n < items.length; n++) {
			items[n].refineDataAcquisitionMode(consolidatedMode,frameType);
		}
	}
	//
	public static KinectCircumscriptionMode getFirstItem(KinectCircumscriptionMode[] items) {
		if (items.length >= 1) {
			return items[0];
		} else {
			return KinectCircumscriptionMode.NONE;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String toString(KinectCircumscriptionMode[] items) {
		if (items.length == 1) {
			return items[0].toString();
		} else {
			StringBuffer buffer= new StringBuffer();
			buffer.append("[");
			for (int n=0; n < items.length; n++) {
				if (n > 0) {
					buffer.append(",");
				};
				buffer.append(items[n].toString());
			};
			buffer.append("]");
			return buffer.toString();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeText(KinectCircumscriptionMode[] items, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; circumscriptionModes: ");
		for (int n=0; n < items.length; n++) {
			if (n > 0) {
				writer.write(", ");
			};
			writer.write(String.format(locale,"%s",items[n].toString()));
		};
		writer.write("\n");
	}
}
