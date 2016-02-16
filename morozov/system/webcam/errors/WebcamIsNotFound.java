// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.webcam.errors;

public class WebcamIsNotFound extends RuntimeException {
	public String name;
	//
	public WebcamIsNotFound(String text) {
		name= text;
	}
	public String toString() {
		if (name != null) {
			return this.getClass().toString() + "(" + name.toString() + ")";
		} else {
			return this.getClass().toString();
		}
	}
}
