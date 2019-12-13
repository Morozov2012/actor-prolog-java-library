// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.vision.vpm.errors;

public class IncorrectForegroundMaskSize extends RuntimeException {
	//
	protected int foregroundMaskWidth;
	protected int foregroundMaskHeight;
	protected int operationalImageWidth;
	protected int operationalImageHeight;
	//
	public IncorrectForegroundMaskSize(int fW, int fH, int oW, int oH) {
		foregroundMaskWidth= fW;
		foregroundMaskHeight= fH;
		operationalImageWidth= oW;
		operationalImageHeight= oH;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" +
			Integer.toString(foregroundMaskWidth) + "x" +
			Integer.toString(foregroundMaskHeight) + "<>" +
			Integer.toString(operationalImageWidth) + "x" +
			Integer.toString(operationalImageHeight) + ")";
	}
}
