// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.blb;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMblbSetBlobBorders extends VPM_FrameCommand {
	//
	protected int horizontalBorderWidth;
	protected int verticalBorderWidth;
	//
	public VPMblbSetBlobBorders(int borderX, int borderY) {
		horizontalBorderWidth= borderX;
		verticalBorderWidth= borderY;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void execute(VPM vpm) {
		vpm.blbSetBlobBorders(horizontalBorderWidth,verticalBorderWidth);
	}
}
