// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public abstract class VPMmskBackgroundSubtractionCommand extends VPM_FrameCommand {
	//
	public VPMmskBackgroundSubtractionCommand() {
	}
	//
	@Override
	public boolean isBackgroundSubtractionCommand() {
		return true;
	}
	//
	abstract public int[] getBackgroundImage(VPM vpm);
	abstract public int[] getSigmaImage(VPM vpm);
}
