// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.frames.text;

import morozov.system.frames.interfaces.*;
import morozov.system.modes.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class DescriptionCompoundFrameText {
	//
	public static String toString(DescriptionCompoundFrameInterface frame) {
		CompoundArrayType frameType= frame.getCompoundArrayType();
		String description= frame.getDescription();
		String copyright= frame.getCopyright();
		String registrationDate= frame.getRegistrationDate();
		String registrationTime= frame.getRegistrationTime();
		return	"{" +
			String.format("%s",frameType) + ";" +
			description + ";" +
			copyright + ";" +
			registrationDate + ";" +
			registrationTime + "}";
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeText(DescriptionCompoundFrameInterface frame, BufferedWriter writer, Locale locale) throws IOException {
		writeDescriptionCompoundFrameText(frame,writer,locale);
	}
	//
	public static void writeDescriptionCompoundFrameText(DescriptionCompoundFrameInterface frame, BufferedWriter writer, Locale locale) throws IOException {
		writer.write("; DESCRIPTION COMPOUND FRAME:\n");
		CompoundFrameText.writeRootFrameText(frame,writer,locale);
		CompoundArrayType frameType= frame.getCompoundArrayType();
		String description= frame.getDescription();
		String copyright= frame.getCopyright();
		String registrationDate= frame.getRegistrationDate();
		String registrationTime= frame.getRegistrationTime();
		writer.write(String.format(locale,"; frameType: %s\n",frameType));
		writer.write(String.format(locale,"; description: %s\n",description));
		writer.write(String.format(locale,"; copyright: %s\n",copyright));
		writer.write(String.format(locale,"; registrationDate: %s\n",registrationDate));
		writer.write(String.format(locale,"; registrationTime: %s\n",registrationTime));
		writer.write("; End of description compound frame.\n");
	}
}
