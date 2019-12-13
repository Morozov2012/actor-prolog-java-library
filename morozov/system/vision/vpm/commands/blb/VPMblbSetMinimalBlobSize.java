// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.blb;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMblbSetMinimalBlobSize extends VPM_FrameCommand {
	//
	protected int size;
	//
	public VPMblbSetMinimalBlobSize(int s) {
		size= s;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void execute(VPM vpm) {
		vpm.blbSetMinimalBlobSize(size);
	}
}
