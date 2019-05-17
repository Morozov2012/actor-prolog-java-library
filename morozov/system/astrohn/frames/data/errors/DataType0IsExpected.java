// The Actor Prolog project, (c) 2018 IRE RAS, Alexei A. Morozov

package morozov.system.astrohn.frames.data.errors;

public class DataType0IsExpected extends RuntimeException {
	public int illegalCode;
	public DataType0IsExpected(int code) {
		illegalCode= code;
	}
	public String toString() {
		return this.getClass().toString() + "(" + Integer.toString(illegalCode) + ")";
	}
}
