// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.signals;

import morozov.run.*;

public final class TermIsNotFFmpegWorkAroundBugName extends LightweightException {
	public static final TermIsNotFFmpegWorkAroundBugName instance= new TermIsNotFFmpegWorkAroundBugName();
	//
	private TermIsNotFFmpegWorkAroundBugName() {
	}
}
