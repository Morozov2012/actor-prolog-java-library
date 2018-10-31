// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.pxl;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMpxlWithdrawPixelPreprocessing extends VPM_FrameCommand {
	//
	public VPMpxlWithdrawPixelPreprocessing() {
	}
	//
	public void execute(VPM vpm) {
		vpm.clearOperationalMatrix();
	}
}
