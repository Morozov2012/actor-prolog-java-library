// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.img;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMimgWithdrawImagePreprocessing extends VPM_FrameCommand {
	//
	public VPMimgWithdrawImagePreprocessing() {
	}
	//
	@Override
	public void execute(VPM vpm) {
		java.awt.image.BufferedImage recentImage= vpm.getRecentImage();
		vpm.setPreprocessedImage(recentImage);
	}
}
