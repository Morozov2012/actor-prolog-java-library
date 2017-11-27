// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.blb;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMblbSetMinimalBlobIntersectionArea extends VPM_FrameCommand {
	//
	protected int area;
	//
	public VPMblbSetMinimalBlobIntersectionArea(int a) {
		area= a;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void execute(VPM vpm) {
		vpm.blbSetMinimalBlobIntersectionArea(area);
	}
}
