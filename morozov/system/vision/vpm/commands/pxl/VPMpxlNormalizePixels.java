// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.pxl;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMpxlNormalizePixels extends VPM_FrameCommand {
	//
	protected int minimalValue;
	protected int maximalValue;
	//
	public VPMpxlNormalizePixels(int min, int max) {
		minimalValue= min;
		maximalValue= max;
	}
	//
	@Override
	public void execute(VPM vpm) {
		int imageWidth= vpm.getOperationalImageWidth();
		int imageHeight= vpm.getOperationalImageHeight();
		int[] operationalMatrix= vpm.getOperationalMatrix();
		int divisor= maximalValue - minimalValue;
		for (int index=0; index < operationalMatrix.length; index++) {
			int value= operationalMatrix[index];
			value= (value-minimalValue) * maximalColor / divisor;
			if (value < 0) {
				value= 0;
			} else if (value > maximalColor) {
				value= maximalColor;
			};
			operationalMatrix[index]= value;
		}
	}
}
