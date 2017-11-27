// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.tools.errors;

public class UnexpectedLengthOfColorFrame extends RuntimeException {
	protected int length;
	public UnexpectedLengthOfColorFrame(int n) {
		length= n;
	}
	public String toString() {
		return this.getClass().toString() + "(" + Integer.toString(length) + ")";
	}
}
