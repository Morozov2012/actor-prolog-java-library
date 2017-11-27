// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.signals;

import morozov.run.*;

public final class TermIsNotFFmpegProfileName extends LightweightException {
	public static final TermIsNotFFmpegProfileName instance= new TermIsNotFFmpegProfileName();
	//
	private TermIsNotFFmpegProfileName() {
	}
}
