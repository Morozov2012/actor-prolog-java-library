// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotFFmpegCodecOption extends WrongArgument {
	public WrongArgumentIsNotFFmpegCodecOption(Term value) {
		super(value);
	}
}
