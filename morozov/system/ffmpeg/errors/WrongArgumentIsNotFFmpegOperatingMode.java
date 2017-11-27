// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class WrongArgumentIsNotFFmpegOperatingMode extends WrongArgument {
	public WrongArgumentIsNotFFmpegOperatingMode(Term value) {
		super(value);
	}
}
