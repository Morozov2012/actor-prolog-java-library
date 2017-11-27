// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.signals;

import morozov.run.*;

public final class TermIsNotFFmpegMediaTypeName extends LightweightException {
	public static final TermIsNotFFmpegMediaTypeName instance= new TermIsNotFFmpegMediaTypeName();
	//
	private TermIsNotFFmpegMediaTypeName() {
	}
}
