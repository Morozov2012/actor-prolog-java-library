// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.text;

import morozov.system.kinect.frames.interfaces.*;
import morozov.system.kinect.frames.tools.*;
import morozov.system.kinect.modes.*;
import morozov.system.kinect.modes.tools.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class KinectModeFrameText {
	//
	public static String toString(KinectModeFrameInterface frame) {
		KinectFrameType kinectFrameType= frame.getKinectFrameType();
		KinectSkeletonsMode skeletonsMode= frame.getSkeletonsMode();
		KinectPeopleIndexMode peopleIndexMode= frame.getPeopleIndexMode();
		KinectCircumscriptionMode[] circumscriptionModes= frame.getCircumscriptionModes();
		KinectFrameType[] dataAcquisitionMode= frame.getDataAcquisitionMode();
		boolean depthIsSelected= frame.depthIsSelected();
		boolean peopleIndexIsSelected= frame.peopleIndexIsSelected();
		boolean xyzIsSelected= frame.xyzIsSelected();
		boolean uvIsSelected= frame.uvIsSelected();
		boolean infraredIsSelected= frame.infraredIsSelected();
		boolean longExposureInfraredIsSelected= frame.longExposureInfraredIsSelected();
		boolean colorIsSelected= frame.colorIsSelected();
		boolean skeletonsAreSelected= frame.skeletonsAreSelected();
		String description= frame.getDescription();
		String copyright= frame.getCopyright();
		String registrationDate= frame.getRegistrationDate();
		String registrationTime= frame.getRegistrationTime();
		return	"{" +
			String.format("%s",kinectFrameType) + ";" +
			String.format("%s",skeletonsMode) + ";" +
			String.format("%s",peopleIndexMode) + ";" +
			KinectCircumscriptionModesTools.toString(circumscriptionModes) + ";" +
			KinectDataAcquisitionModeTools.toString(dataAcquisitionMode) + ";" +
			String.format("%s",depthIsSelected) + ";" +
			String.format("%s",peopleIndexIsSelected) + ";" +
			String.format("%s",xyzIsSelected) + ";" +
			String.format("%s",uvIsSelected) + ";" +
			String.format("%s",infraredIsSelected) + ";" +
			String.format("%s",longExposureInfraredIsSelected) + ";" +
			String.format("%s",colorIsSelected) + ";" +
			String.format("%s",skeletonsAreSelected) + ";" +
			description + ";" +
			copyright + ";" +
			registrationDate + ";" +
			registrationTime + "}";
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeText(KinectModeFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		if (exportMode==ExportMode.SKELETONS) {
			KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		} else {
			writeModeFrameText(frame,writer,exportMode,locale);
		}
	}
	//
	public static void writeModeFrameText(KinectModeFrameInterface frame, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		writer.write("; MODE FRAME:\n");
		KinectFrameText.writeRootFrameText(frame,writer,exportMode,locale);
		KinectFrameType kinectFrameType= frame.getKinectFrameType();
		KinectSkeletonsMode skeletonsMode= frame.getSkeletonsMode();
		KinectPeopleIndexMode peopleIndexMode= frame.getPeopleIndexMode();
		KinectCircumscriptionMode[] circumscriptionModes= frame.getCircumscriptionModes();
		KinectFrameType[] dataAcquisitionMode= frame.getDataAcquisitionMode();
		boolean depthIsSelected= frame.depthIsSelected();
		boolean peopleIndexIsSelected= frame.peopleIndexIsSelected();
		boolean xyzIsSelected= frame.xyzIsSelected();
		boolean uvIsSelected= frame.uvIsSelected();
		boolean infraredIsSelected= frame.infraredIsSelected();
		boolean longExposureInfraredIsSelected= frame.longExposureInfraredIsSelected();
		boolean colorIsSelected= frame.colorIsSelected();
		boolean skeletonsAreSelected= frame.skeletonsAreSelected();
		String description= frame.getDescription();
		String copyright= frame.getCopyright();
		String registrationDate= frame.getRegistrationDate();
		String registrationTime= frame.getRegistrationTime();
		writer.write(String.format(locale,"; kinectFrameType: %s\n",kinectFrameType));
		writer.write(String.format(locale,"; skeletonsMode: %s\n",skeletonsMode));
		writer.write(String.format(locale,"; peopleIndexMode: %s\n",peopleIndexMode));
		KinectCircumscriptionModesTools.writeText(circumscriptionModes,writer,exportMode,locale);
		KinectDataAcquisitionModeTools.writeText(dataAcquisitionMode,writer,exportMode,locale);
		writer.write(String.format(locale,"; depthIsSelected= %s\n",depthIsSelected));
		writer.write(String.format(locale,"; peopleIndexIsSelected= %s\n",peopleIndexIsSelected));
		writer.write(String.format(locale,"; xyzIsSelected= %s\n",xyzIsSelected));
		writer.write(String.format(locale,"; uvIsSelected= %s\n",uvIsSelected));
		writer.write(String.format(locale,"; infraredIsSelected= %s\n",infraredIsSelected));
		writer.write(String.format(locale,"; longExposureInfraredIsSelected= %s\n",longExposureInfraredIsSelected));
		writer.write(String.format(locale,"; colorIsSelected= %s\n",colorIsSelected));
		writer.write(String.format(locale,"; skeletonsAreSelected= %s\n",skeletonsAreSelected));
		writer.write(String.format(locale,"; description: %s\n",description));
		writer.write(String.format(locale,"; copyright: %s\n",copyright));
		writer.write(String.format(locale,"; registrationDate: %s\n",registrationDate));
		writer.write(String.format(locale,"; registrationTime: %s\n",registrationTime));
		writer.write("; End of mode frame.\n");
	}
}
