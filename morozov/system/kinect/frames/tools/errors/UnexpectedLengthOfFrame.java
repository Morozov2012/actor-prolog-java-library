// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.kinect.frames.tools.errors;

public class UnexpectedLengthOfFrame extends RuntimeException {
	//
	protected int length;
	//
	public UnexpectedLengthOfFrame(int n) {
		length= n;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + Integer.toString(length) + ")";
	}
}
