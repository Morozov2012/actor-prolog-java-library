// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.pxl;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

public class VPMpxlSelectImageChannel extends VPM_FrameCommand {
	//
	protected ImageChannelName channelName;
	//
	public VPMpxlSelectImageChannel(ImageChannelName name) {
		channelName= name;
	}
	//
	public void execute(VPM vpm) {
		vpm.selectImageChannel(channelName);
	}
}
