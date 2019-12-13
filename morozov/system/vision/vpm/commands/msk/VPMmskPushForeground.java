// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

import java.util.Arrays;

public class VPMmskPushForeground extends VPM_FrameCommand {
	//
	protected PushOperationMode mode;
	//
	public VPMmskPushForeground(PushOperationMode m) {
		mode= m;
	}
	//
	@Override
	public void execute(VPM vpm) {
		boolean[] previousMask= vpm.pushForegroundMask();
		int length= previousMask.length;
		boolean[] newMask;
		switch (mode) {
		case SELECT_ALL:
			newMask= new boolean[length];
			Arrays.fill(newMask,true);
			vpm.setForegroundMask(newMask);
			break;
		case SELECT_NOTHING:
			newMask= new boolean[length];
			Arrays.fill(newMask,false);
			vpm.setForegroundMask(newMask);
			break;
		case DUPLICATE_FOREGROUND:
			newMask= Arrays.copyOf(previousMask,length);
			vpm.setForegroundMask(newMask);
			break;
		case FLIP_FOREGROUND:
			newMask= new boolean[length];
			for (int k=0; k < length; k++) {
				if (previousMask[k]) {
					newMask[k]= false;
				} else {
					newMask[k]= true;
				}
			};
			vpm.setForegroundMask(newMask);
			break;
		default:
			System.err.printf("Unknown push operation mode: %s\n",mode);
		}
	}
}
