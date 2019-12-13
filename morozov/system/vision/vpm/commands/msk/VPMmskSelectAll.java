// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

import java.util.Arrays;

public class VPMmskSelectAll extends VPM_FrameCommand {
	//
	public VPMmskSelectAll() {
	}
	//
	@Override
	public void execute(VPM vpm) {
		boolean[] foregroundMask= vpm.getForegroundMask();
		Arrays.fill(foregroundMask,true);
	}
}
