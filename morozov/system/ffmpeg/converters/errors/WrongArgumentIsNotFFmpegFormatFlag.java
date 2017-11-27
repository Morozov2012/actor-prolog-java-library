// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotFFmpegFormatFlag extends WrongArgument {
	public WrongArgumentIsNotFFmpegFormatFlag(Term value) {
		super(value);
	}
}
