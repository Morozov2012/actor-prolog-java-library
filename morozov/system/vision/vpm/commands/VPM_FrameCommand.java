// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands;

import morozov.system.vision.vpm.*;

public abstract class VPM_FrameCommand extends VPM_Command {
	abstract public void execute(VPM vpm);
	public void forgetStatistics() {
	}
	public boolean isBackgroundSubtractionCommand() {
		return false;
	}
}
