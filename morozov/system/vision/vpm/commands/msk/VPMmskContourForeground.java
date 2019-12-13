// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMmskContourForeground extends VPM_FrameCommand {
	//
	public VPMmskContourForeground() {
	}
	//
	@Override
	public void execute(VPM vpm) {
		int imageWidth= vpm.getOperationalImageWidth();
		int imageHeight= vpm.getOperationalImageHeight();
		boolean[] foregroundMask= vpm.getForegroundMask();
		foregroundMask= VisionUtils.contourForeground(foregroundMask,imageWidth,imageHeight);
		vpm.setForegroundMask(foregroundMask);
	}
}
