// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotFFmpegLevel extends WrongArgument {
	public WrongArgumentIsNotFFmpegLevel(Term value) {
		super(value);
	}
}
