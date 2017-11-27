// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

public class VPMmskFlipPolygon extends VPMmskApplyPolygon {
	//
	public VPMmskFlipPolygon(double[] aX, double[] aY) {
		super(aX,aY);
	}
	//
	protected void applyPolygon(boolean[] foregroundMask, int[] imagePixels) {
		int vectorLength= foregroundMask.length;
		for (int k=0; k < vectorLength; k++) {
			int value= imagePixels[k];
			if (value > 0) {
				foregroundMask[k]= !foregroundMask[k];
			}
		}
	}
}
