// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;
import morozov.system.vision.vpm.converters.*;

abstract public class VPMmskAnalyzeForeground extends VPM_FrameCommand {
	//
	protected ImageChannelName channelName;
	protected int lowerBound;
	protected int upperBound;
	//
	public VPMmskAnalyzeForeground(ImageChannelName name, int left, int right) {
		channelName= name;
		lowerBound= left;
		upperBound= right;
	}
	//
	public void execute(VPM vpm) {
		int[] operationalMatrix;
		if (channelName==null) {
			operationalMatrix= vpm.getOperationalMatrix();
		} else {
			switch (channelName) {
			case GRAYSCALE:
				operationalMatrix= vpm.getMatrixGrayscale();
				break;
			case HUE:
				operationalMatrix= vpm.getMatrixHue();
				break;
			case SATURATION:
				operationalMatrix= vpm.getMatrixSaturation();
				break;
			case BRIGHTNESS:
				operationalMatrix= vpm.getMatrixBrightness();
				break;
			case RED:
				operationalMatrix= vpm.getMatrixRed();
				break;
			case GREEN:
				operationalMatrix= vpm.getMatrixGreen();
				break;
			case BLUE:
				operationalMatrix= vpm.getMatrixBlue();
				break;
			case ALL:
				operationalMatrix= vpm.getMatrixGrayscale();
				break;
			default:
				System.err.printf("Unknown channel name: %s\n",channelName);
				return;
			}
		};
		boolean[] foregroundMask= vpm.getForegroundMask();
		analyzeOperationalMatrix(foregroundMask,operationalMatrix);
	}
	//
	abstract protected void analyzeOperationalMatrix(boolean[] foregroundMask, int[] operationalMatrix);
}
