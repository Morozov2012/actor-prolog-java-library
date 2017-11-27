// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.signals;

import morozov.run.*;

public final class TermIsNotFFmpegAudioSampleFormatName extends LightweightException {
	public static final TermIsNotFFmpegAudioSampleFormatName instance= new TermIsNotFFmpegAudioSampleFormatName();
	//
	private TermIsNotFFmpegAudioSampleFormatName() {
	}
}
