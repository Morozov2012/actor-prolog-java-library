// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.modes.tools;

import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.interfaces.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class KinectDataAcquisitionModeTools {
	//
	public static void refineDataAcquisitionMode(KinectFrameType[] frameTypes, ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectDisplayingModeInterface displayingMode) {
		KinectCircumscriptionMode[] circumscriptionModes= displayingMode.getActingCircumscriptionModes();
		KinectSkeletonsMode skeletonsMode= displayingMode.getActingSkeletonsMode();
		KinectPeopleIndexMode peopleIndexMode= displayingMode.getActingPeopleIndexMode();
		if (frameTypes.length > 0) {
			for (int n=0; n < frameTypes.length; n++) {
				KinectFrameType frameType= frameTypes[n];
				frameType.refineDataAcquisitionMode(consolidatedMode);
				KinectCircumscriptionModesTools.refineDataAcquisitionMode(circumscriptionModes,consolidatedMode,frameType);
				skeletonsMode.refineDataAcquisitionMode(consolidatedMode,frameType);
			}
		} else {
			KinectFrameType defaultFrameType= displayingMode.getActingFrameType();
			defaultFrameType.refineDataAcquisitionMode(consolidatedMode);
			KinectCircumscriptionModesTools.refineDataAcquisitionMode(circumscriptionModes,consolidatedMode,defaultFrameType);
			skeletonsMode.refineDataAcquisitionMode(consolidatedMode,defaultFrameType);
		};
		peopleIndexMode.refineDataAcquisitionMode(consolidatedMode);
	}
	//
	public static void refineDataAcquisitionMode(KinectFrameType[] frameTypes, ConsolidatedDataAcquisitionModeInterface consolidatedMode, KinectFrameType defaultFrameType) {
		if (frameTypes.length <= 0) {
			defaultFrameType.refineDataAcquisitionMode(consolidatedMode);
		} else {
			for (int n=0; n < frameTypes.length; n++) {
				frameTypes[n].refineDataAcquisitionMode(consolidatedMode);
			}
		}
	}
	//
	public static boolean requiresFrameType(KinectFrameType[] frameTypes, KinectDataArrayType proposedFrameType, KinectFrameType defaultFrameType) {
		if (frameTypes.length <= 0) {
			return defaultFrameType.requiresFrameType(proposedFrameType);
		} else {
			for (int n=0; n < frameTypes.length; n++) {
				if (frameTypes[n].requiresFrameType(proposedFrameType)) {
					return true;
				}
			};
			return false;
		}
	}
	//
	public static KinectFrameType getFirstItem(KinectFrameType[] items) {
		if (items.length >= 1) {
			return items[0];
		} else {
			return KinectFrameType.NONE;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String toString(KinectFrameType[] frameTypes) {
		if (frameTypes.length <= 0) {
			return "AUTO";
		} else {
			StringBuffer buffer= new StringBuffer();
			buffer.append("[");
			for (int n=0; n < frameTypes.length; n++) {
				if (n > 0) {
					buffer.append(",");
				};
				buffer.append(frameTypes[n].toString());
			};
			buffer.append("]");
			return buffer.toString();
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeText(KinectFrameType[] frameTypes, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; dataAcquisitionMode: ");
		if (frameTypes.length <= 0) {
			writer.write("AUTO");
		} else {
			for (int n=0; n < frameTypes.length; n++) {
				if (n > 0) {
					writer.write(", ");
				};
				writer.write(String.format(locale,"%s",frameTypes[n].toString()));
			}
		};
		writer.write("\n");
	}
}
