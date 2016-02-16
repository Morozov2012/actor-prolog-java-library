// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.files.errors;

public class WrongArgumentIsUnsupportedAudioFile extends FileInputOutputError {
	public WrongArgumentIsUnsupportedAudioFile(String name, Throwable e) {
		super(name,e);
	}
}
