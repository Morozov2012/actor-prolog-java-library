// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

import morozov.system.vision.vpm.converters.*;

public class VPMmskSelectForeground extends VPMmskAnalyzeForeground {
	//
	public VPMmskSelectForeground(ImageChannelName name, int left, int right) {
		super(name,left,right);
	}
	//
	protected void analyzeOperationalMatrix(boolean[] foregroundMask, int[] operationalMatrix) {
		int vectorLength= foregroundMask.length;
		for (int k=0; k < vectorLength; k++) {
			if (!foregroundMask[k]) {
				continue;
			};
			int value= operationalMatrix[k];
			if (value < lowerBound || value > upperBound) {
				foregroundMask[k]= false;
			}
		}
	}
}
