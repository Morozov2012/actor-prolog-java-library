// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.commands.msk;

public class VPMmskFlipPolygon extends VPMmskApplyPolygon {
	//
	public VPMmskFlipPolygon(double[] aX, double[] aY, boolean useStandardizedCoordinates) {
		super(aX,aY,useStandardizedCoordinates);
	}
	//
	@Override
	protected void applyPolygon(boolean[] foregroundMask, int[] imagePixels) {
		int vectorLength= foregroundMask.length;
		for (int k=0; k < vectorLength; k++) {
			int value= imagePixels[k];
			if (value > 0) {
				foregroundMask[k]= !foregroundMask[k];
			}
		}
	}
}
