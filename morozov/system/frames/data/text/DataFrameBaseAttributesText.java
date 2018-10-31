// (c) 2018 Alexei A. Morozov

package morozov.system.frames.data.text;

import morozov.system.frames.data.interfaces.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;

public class DataFrameBaseAttributesText {
	//
	public static void writeText(DataFrameBaseAttributesInterface attributes, BufferedWriter writer, Locale locale) throws IOException {
		writer.write("; BASE ATTRIBUTES:\n");
		writer.write(String.format(locale,"; serialNumber: %s\n",attributes.getSerialNumber()));
		writer.write(String.format(locale,"; isAutorangingMode: %s\n",attributes.isAutorangingMode()));
		writer.write(String.format(locale,"; isDoubleColorMapMode: %s\n",attributes.isDoubleColorMapMode()));
		writer.write(String.format(locale,"; selectedDataRange: %s\n",attributes.getSelectedDataRange()));
		writer.write(String.format(locale,"; lowerDataBound: %s\n",attributes.getLowerDataBound()));
		writer.write(String.format(locale,"; upperDataBound: %s\n",attributes.getUpperDataBound()));
		writer.write(String.format(locale,"; lowerDataQuantile1: %s\n",attributes.getLowerDataQuantile1()));
		writer.write(String.format(locale,"; upperDataQuantile1: %s\n",attributes.getUpperDataQuantile1()));
		writer.write(String.format(locale,"; lowerDataQuantile2: %s\n",attributes.getLowerDataQuantile2()));
		writer.write(String.format(locale,"; upperDataQuantile2: %s\n",attributes.getUpperDataQuantile2()));
		writer.write(String.format(locale,"; mainColorMap: %s\n",attributes.getMainColorMap()));
		writer.write(String.format(locale,"; auxiliaryColorMap: %s\n",attributes.getAuxiliaryColorMap()));
		writer.write(String.format(locale,"; isAverageMode: %s\n",attributes.isAverageMode()));
		writer.write(String.format(locale,"; isZoomingMode: %s\n",attributes.isZoomingMode()));
		writer.write(String.format(locale,"; zoomingCoefficient: %s\n",attributes.getZoomingCoefficient()));
		writer.write("; End of base attributes.\n");
	}
}
