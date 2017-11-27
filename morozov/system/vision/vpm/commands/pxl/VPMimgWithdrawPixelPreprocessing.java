// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.pxl;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMimgWithdrawPixelPreprocessing extends VPM_FrameCommand {
	//
	public VPMimgWithdrawPixelPreprocessing() {
	}
	//
	public void execute(VPM vpm) {
		vpm.clearOperationalMatrix();
	}
}
