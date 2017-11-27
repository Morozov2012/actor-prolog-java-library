// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotFFmpegPixelFormat extends WrongArgument {
	public WrongArgumentIsNotFFmpegPixelFormat(Term value) {
		super(value);
	}
}
