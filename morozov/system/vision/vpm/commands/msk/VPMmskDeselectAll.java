// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

import java.util.Arrays;

public class VPMmskDeselectAll extends VPM_FrameCommand {
	//
	public VPMmskDeselectAll() {
	}
	//
	public void execute(VPM vpm) {
		boolean[] foregroundMask= vpm.getForegroundMask();
		Arrays.fill(foregroundMask,false);
	}
}
