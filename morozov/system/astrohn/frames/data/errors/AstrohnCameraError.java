// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov

package morozov.system.astrohn.frames.data.errors;

public class AstrohnCameraError extends RuntimeException {
	//
	protected int errorCode;
	//
	public AstrohnCameraError(int code) {
		errorCode= code;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + Integer.toString(errorCode) + ")";
	}
}
