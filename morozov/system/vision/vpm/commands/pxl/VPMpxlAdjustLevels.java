// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.pxl;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMpxlAdjustLevels extends VPM_FrameCommand {
	//
	protected double histogramTailCoefficient;
	//
	public VPMpxlAdjustLevels(double c) {
		histogramTailCoefficient= c;
	}
	//
	public void execute(VPM vpm) {
		int[] red= vpm.getMatrixRed();
		int[] green= vpm.getMatrixGreen();
		int[] blue= vpm.getMatrixBlue();
		boolean[] foregroundMask= vpm.getForegroundMask();
		adjustChannel(red,foregroundMask);
		adjustChannel(green,foregroundMask);
		adjustChannel(blue,foregroundMask);
		vpm.acceptMatrixRGB();
	}
	//
	protected void adjustChannel(int[] colorChannel, boolean[] foregroundMask) {
		int length= colorChannel.length;
		int[] histogram= new int[length];
		for (int k=0; k < length; k++) {
			if (foregroundMask[k]) {
				int value= colorChannel[k];
				histogram[value]++;
			}
		};
		double sum= 0.0;
		for (int k=0; k < length; k++) {
			sum+= histogram[k];
		};
		double leftThreshold= 0.0;
		double rightThreshold= sum;
		if (histogramTailCoefficient > 0) {
			leftThreshold= sum * histogramTailCoefficient;
			rightThreshold= sum * (1-histogramTailCoefficient);
		};
		double counter= 0.0;
		int leftBound= 0;
		for (int k=0; k < length; k++) {
			counter+= histogram[k];
			if (counter >= leftThreshold) {
				leftBound= k;
				break;
			}
		};
		counter= sum;
		int rightBound= maximalColor;
		for (int k=length-1; k >= 0; k--) {
			counter-= histogram[k];
			if (counter <= rightThreshold) {
				rightBound= k;
				break;
			}
		};
		double coefficient= (double)maximalColor / (rightBound-leftBound);
		for (int k=0; k < length; k++) {
			int value1= colorChannel[k];
			int value2= (int)((value1-leftBound) * coefficient);
			if (value2 < 0) {
				value2= 0;
			} else if (value2 > maximalColor) {
				value2= maximalColor;
			};
			colorChannel[k]= value2;
		}
	}
}
