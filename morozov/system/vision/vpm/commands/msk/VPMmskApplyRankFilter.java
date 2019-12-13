// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMmskApplyRankFilter extends VPM_FrameCommand {
	//
	protected int threshold;
	//
	public VPMmskApplyRankFilter(int t) {
		threshold= t;
	}
	//
	@Override
	public void execute(VPM vpm) {
		int imageWidth= vpm.getOperationalImageWidth();
		int imageHeight= vpm.getOperationalImageHeight();
		boolean[] foregroundMask= vpm.getForegroundMask();
		VisionUtils.rankFilter2D(foregroundMask,imageWidth,imageHeight,threshold,false);
	}
}
