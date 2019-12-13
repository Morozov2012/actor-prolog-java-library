// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.pxl;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMpxlInvertColors extends VPM_FrameCommand {
	//
	public VPMpxlInvertColors() {
	}
	//
	@Override
	public void execute(VPM vpm) {
		int[] red= vpm.getMatrixRed();
		int[] green= vpm.getMatrixGreen();
		int[] blue= vpm.getMatrixBlue();
		int length= red.length;
		for (int k=0; k < length; k++) {
			int r1= red[k];
			int g1= green[k];
			int b1= blue[k];
			int r2= maximalColor - r1;
			int g2= maximalColor - g1;
			int b2= maximalColor - b1;
			if (r2 < 0) {
				r2= 0;
			} else if (r2 > maximalColor) {
				r2= maximalColor;
			};
			if (g2 < 0) {
				g2= 0;
			} else if (g2 > maximalColor) {
				g2= maximalColor;
			};
			if (b2 < 0) {
				b2= 0;
			} else if (b2 > maximalColor) {
				b2= maximalColor;
			};
			red[k]= r2;
			green[k]= g2;
			blue[k]= b2;
		};
		vpm.acceptMatrixRGB();
	}
}
