// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

public class FFmpegCannotSetDictionaryEntry extends RuntimeException {
	//
	protected String key;
	protected String value;
	//
	public FFmpegCannotSetDictionaryEntry(String k, String v) {
		key= k;
		value= v;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + key + ';' + value + ")";
	}
}
