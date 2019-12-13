// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.blb;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMblbSetExtraBorderCoefficients extends VPM_FrameCommand {
	//
	protected double horizontalExtraBorderCoefficient;
	protected double verticalExtraBorderCoefficient;
	//
	public VPMblbSetExtraBorderCoefficients(double coefX, double coefY) {
		horizontalExtraBorderCoefficient= coefX;
		verticalExtraBorderCoefficient= coefY;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void execute(VPM vpm) {
		vpm.blbSetExtraBorderCoefficients(horizontalExtraBorderCoefficient,verticalExtraBorderCoefficient);
	}
}
