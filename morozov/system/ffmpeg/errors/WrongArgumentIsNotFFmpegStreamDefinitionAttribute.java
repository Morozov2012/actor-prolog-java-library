// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

import morozov.terms.errors.*;

public class WrongArgumentIsNotFFmpegStreamDefinitionAttribute extends WrongArgumentIsUnknownAttribute {
	public WrongArgumentIsNotFFmpegStreamDefinitionAttribute(long name) {
		super(name);
	}
}
