// (c) 2019 Alexei A. Morozov

package morozov.system.frames.text;

import morozov.system.frames.interfaces.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class CompoundFrameText {
	//
	public static void writeText(CompoundFrameInterface frame, BufferedWriter writer, Locale locale) throws IOException {
		writeRootFrameText(frame,writer,locale);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static void writeRootFrameText(CompoundFrameInterface frame, BufferedWriter writer, Locale locale) throws IOException {
		writer.write("; COMPOUND FRAME:\n");
		writer.write(String.format(locale,"; serialNumber: %s\n",frame.getSerialNumber()));
		writer.write(String.format(locale,"; time: %s\n",frame.getTime()));
		writer.write(String.format(locale,"; isLightweight= %s\n",frame.isLightweightFrame()));
		writer.write("; End of compound frame.\n");
	}
}
