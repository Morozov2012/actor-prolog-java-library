// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.frames.text;

import morozov.system.frames.interfaces.*;
import morozov.system.modes.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class ModeFrameText {
	//
	public static String toString(ModeFrameInterface frame) {
		DataArrayType frameType= frame.getDataArrayType();
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
	public static void writeText(ModeFrameInterface frame, BufferedWriter writer, Locale locale) throws IOException {
		writeModeFrameText(frame,writer,locale);
	}
	//
	public static void writeModeFrameText(ModeFrameInterface frame, BufferedWriter writer, Locale locale) throws IOException {
		writer.write("; MODE FRAME:\n");
		DataFrameText.writeRootFrameText(frame,writer,locale);
		DataArrayType frameType= frame.getDataArrayType();
		String description= frame.getDescription();
		String copyright= frame.getCopyright();
		String registrationDate= frame.getRegistrationDate();
		String registrationTime= frame.getRegistrationTime();
		writer.write(String.format(locale,"; frameType: %s\n",frameType));
		writer.write(String.format(locale,"; description: %s\n",description));
		writer.write(String.format(locale,"; copyright: %s\n",copyright));
		writer.write(String.format(locale,"; registrationDate: %s\n",registrationDate));
		writer.write(String.format(locale,"; registrationTime: %s\n",registrationTime));
		writer.write("; End of mode frame.\n");
	}
}
