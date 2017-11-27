// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.data.tools;

import morozov.system.kinect.frames.data.interfaces.*;
import morozov.system.kinect.frames.tools.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.Arrays;

public class ForegroundPointCloudTools {
	public static void writeText(ForegroundPointCloudInterface cloud, BufferedWriter writer, ExportMode exportMode, Locale locale) throws IOException {
		int minimalX= cloud.getMinimalX();
		int maximalX= cloud.getMaximalX();
		int minimalY= cloud.getMinimalY();
		int maximalY= cloud.getMaximalY();
		int width= maximalX - minimalX + 1;
		int height= maximalY - minimalY + 1;
		writer.write("; FOREGROUND POINT CLOUD:\n");
		writer.write(String.format(locale,"; minimalX= %s\n",minimalX));
		writer.write(String.format(locale,"; maximalX= %s\n",maximalX));
		writer.write(String.format(locale,"; minimalY= %s\n",minimalY));
		writer.write(String.format(locale,"; maximalY= %s\n",maximalY));
		writer.write(String.format(locale,"; frameWidth= %s\n",cloud.getFrameWidth()));
		writer.write(String.format(locale,"; frameHeight= %s\n",cloud.getFrameHeight()));
		if (minimalX < 0 || maximalX < 0 || minimalY < 0 || maximalY < 0) {
			writer.write("; Point cloud is dummy.\n");
			return;
		};
		float[][] matrixX= cloud.getMatrixX();
		float[][] matrixY= cloud.getMatrixY();
		float[][] matrixZ= cloud.getMatrixZ();
		writer.write(String.format(locale,"; X: %s %s\n",width,height));
		for (int h=0; h < height; h++) {
			for (int w=0; w < width; w++) {
				if (w > 0) {
					writer.write(String.format(locale,"\t%s",matrixX[w][h]));
				} else {
					writer.write(String.format(locale,"%s",matrixX[w][h]));
				}
			};
			writer.write("\n");
		};
		writer.write(String.format(locale,"; Y: %s %s\n",width,height));
		for (int h=0; h < height; h++) {
			for (int w=0; w < width; w++) {
				if (w > 0) {
					writer.write(String.format(locale,"\t%s",matrixY[w][h]));
				} else {
					writer.write(String.format(locale,"%s",matrixY[w][h]));
				}
			};
			writer.write("\n");
		};
		writer.write(String.format(locale,"; Z: %s %s\n",width,height));
		for (int h=0; h < height; h++) {
			for (int w=0; w < width; w++) {
				if (w > 0) {
					writer.write(String.format(locale,"\t%s",matrixZ[w][h]));
				} else {
					writer.write(String.format(locale,"%s",matrixZ[w][h]));
				}
			};
			writer.write("\n");
		};
		writer.write("; End of foreground point cloud.\n");
	}
}
