// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.signals;

import morozov.run.*;

public final class TermIsNotFFmpegDebugOptionName extends LightweightException {
	public static final TermIsNotFFmpegDebugOptionName instance= new TermIsNotFFmpegDebugOptionName();
	//
	private TermIsNotFFmpegDebugOptionName() {
	}
}
