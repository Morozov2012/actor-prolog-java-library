// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.errors;

import morozov.terms.*;
import morozov.terms.errors.*;

public class CodecTypeIsNotDefined extends WrongArgument {
	public CodecTypeIsNotDefined(Term value) {
		super(value);
	}
}
