// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.pxl;

import morozov.system.vision.vpm.*;
import morozov.system.vision.vpm.commands.*;

public class VPMpxlAdjustContrast extends VPM_FrameCommand {
	//
	protected double histogramTailCoefficient;
	protected int leftBound;
	protected int rightBound;
	protected boolean computeThresholds;
	//
	public VPMpxlAdjustContrast(double c) {
		histogramTailCoefficient= c;
		computeThresholds= true;
	}
	public VPMpxlAdjustContrast(int minimalValue, int maximalValue) {
		leftBound= minimalValue;
		rightBound= maximalValue;
		computeThresholds= false;
	}
	//
	@Override
	public void execute(VPM vpm) {
		int[] red= vpm.getMatrixRed();
		int[] green= vpm.getMatrixGreen();
		int[] blue= vpm.getMatrixBlue();
		boolean[] foregroundMask= vpm.getForegroundMask();
		int length= red.length;
		if (computeThresholds) {
			int[] histogram= new int[length];
			for (int k=0; k < length; k++) {
				if (foregroundMask[k]) {
					int value= (red[k] + green[k] + blue[k]) / 3;
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
			leftBound= 0;
			for (int k=0; k < length; k++) {
				counter+= histogram[k];
				if (counter >= leftThreshold) {
					leftBound= k;
					break;
				}
			};
			counter= sum;
			rightBound= maximalColor;
			for (int k=length-1; k >= 0; k--) {
				counter-= histogram[k];
				if (counter <= rightThreshold) {
					rightBound= k;
					break;
				}
			}
		};
		double coefficient= (double)maximalColor / (rightBound-leftBound);
		for (int k=0; k < length; k++) {
			int r1= red[k];
			int g1= green[k];
			int b1= blue[k];
			int r2= (int)((r1-leftBound) * coefficient);
			int g2= (int)((g1-leftBound) * coefficient);
			int b2= (int)((b1-leftBound) * coefficient);
			if (r2 < 0) {
				r2= 0;
			} else if (r2 > maximalColor) {
				r2= maximalColor;
			};
			if (g2 < 0) {
				g2= 0;
			} else if (g2 > maximalColor) {
				g2= maximalColor;
			};
			if (b2 < 0) {
				b2= 0;
			} else if (b2 > maximalColor) {
				b2= maximalColor;
			};
			red[k]= r2;
			green[k]= g2;
			blue[k]= b2;
		};
		vpm.acceptMatrixRGB();
	}
}
