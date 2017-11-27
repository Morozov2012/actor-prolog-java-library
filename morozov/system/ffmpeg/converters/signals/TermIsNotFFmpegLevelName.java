// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.signals;

import morozov.run.*;

public final class TermIsNotFFmpegLevelName extends LightweightException {
	public static final TermIsNotFFmpegLevelName instance= new TermIsNotFFmpegLevelName();
	//
	private TermIsNotFFmpegLevelName() {
	}
}
