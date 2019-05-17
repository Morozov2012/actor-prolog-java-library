// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

public class VPMmskSelectPolygon extends VPMmskApplyPolygon {
	//
	public VPMmskSelectPolygon(double[] aX, double[] aY, boolean useStandardizedCoordinates) {
		super(aX,aY,useStandardizedCoordinates);
	}
	//
	protected void applyPolygon(boolean[] foregroundMask, int[] imagePixels) {
		int vectorLength= foregroundMask.length;
		for (int k=0; k < vectorLength; k++) {
			if (!foregroundMask[k]) {
				continue;
			};
			int value= imagePixels[k];
			if (value <= 0) {
				foregroundMask[k]= false;
			}
		}
	}
}
