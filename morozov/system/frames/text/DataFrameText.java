// (c) 2018 Alexei A. Morozov

package morozov.system.frames.text;

import morozov.system.frames.data.interfaces.*;
import morozov.system.frames.data.text.*;
import morozov.system.frames.interfaces.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class DataFrameText {
	//
	public static void writeText(DataFrameInterface frame, BufferedWriter writer, Locale locale) throws IOException {
		writeRootFrameText(frame,writer,locale);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeRootFrameText(DataFrameInterface frame, BufferedWriter writer, Locale locale) throws IOException {
		writer.write("; ROOT FRAME:\n");
		DataFrameBaseAttributesInterface attributes= frame.getBaseAttributes();
		writer.write(String.format(locale,"; serialNumber: %s\n",frame.getSerialNumber()));
		writer.write(String.format(locale,"; time: %s\n",frame.getTime()));
		writer.write(String.format(locale,"; isLightweight= %s\n",frame.isLightweightFrame()));
		DataFrameBaseAttributesText.writeText(attributes,writer,locale);
		writer.write("; End of root frame.\n");
	}
}
