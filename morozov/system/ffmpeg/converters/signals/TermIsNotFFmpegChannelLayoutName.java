// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.signals;

import morozov.run.*;

public final class TermIsNotFFmpegChannelLayoutName extends LightweightException {
	//
	public static final TermIsNotFFmpegChannelLayoutName instance= new TermIsNotFFmpegChannelLayoutName();
	//
	private TermIsNotFFmpegChannelLayoutName() {
	}
}
