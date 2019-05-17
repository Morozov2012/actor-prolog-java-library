// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov

package morozov.system.astrohn.frames.data.errors;

public class MagicCodeIsExpected extends RuntimeException {
	public int illegalCode;
	public MagicCodeIsExpected(int code) {
		illegalCode= code;
	}
	public String toString() {
		return this.getClass().toString() + "(" + String.format("%x",illegalCode) + ")";
	}
}
