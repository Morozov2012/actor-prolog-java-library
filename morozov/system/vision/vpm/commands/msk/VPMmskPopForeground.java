// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

public class VPMmskPopForeground extends VPM_FrameCommand {
	//
	PopOperationMode mode;
	//
	public VPMmskPopForeground(PopOperationMode m) {
		mode= m;
	}
	//
	public void execute(VPM vpm) {
		boolean[] currentMask= vpm.getForegroundMask();
		boolean[] previousMask= vpm.popForegroundMask();
		int length= currentMask.length;
		boolean[] newMask;
		switch (mode) {
		case AND:
			newMask= new boolean[length];
			for (int k=0; k < length; k++) {
				newMask[k]= currentMask[k] && previousMask[k];
			};
			vpm.setForegroundMask(newMask);
			break;
		case OR:
			newMask= new boolean[length];
			for (int k=0; k < length; k++) {
				newMask[k]= currentMask[k] || previousMask[k];
			};
			vpm.setForegroundMask(newMask);
			break;
		case XOR:
			newMask= new boolean[length];
			for (int k=0; k < length; k++) {
				newMask[k]= currentMask[k] ^ previousMask[k];
			};
			vpm.setForegroundMask(newMask);
			break;
		case ASSIGN_FOREGROUND:
			break;
		case FORGET_FOREGROUND:
			vpm.setForegroundMask(previousMask);
			break;
		default:
			System.err.printf("Unknown pop operation mode: %s\n",mode);
		}
	}
}
