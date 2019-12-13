// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.sound.errors;

public class MicrophoneIsNotFound extends RuntimeException {
	//
	protected String name;
	//
	public MicrophoneIsNotFound(String text) {
		name= text;
	}
	@Override
	public String toString() {
		if (name != null) {
			return this.getClass().toString() + "(" + name.toString() + ")";
		} else {
			return this.getClass().toString();
		}
	}
}
