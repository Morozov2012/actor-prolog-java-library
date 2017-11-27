// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.signals;

import morozov.run.*;

public final class TermIsNotFFmpegSubtitleTextFormatName extends LightweightException {
	public static final TermIsNotFFmpegSubtitleTextFormatName instance= new TermIsNotFFmpegSubtitleTextFormatName();
	//
	private TermIsNotFFmpegSubtitleTextFormatName() {
	}
}
