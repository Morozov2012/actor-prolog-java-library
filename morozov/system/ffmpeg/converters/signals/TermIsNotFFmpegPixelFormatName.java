// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.signals;

import morozov.run.*;

public final class TermIsNotFFmpegPixelFormatName extends LightweightException {
	//
	public static final TermIsNotFFmpegPixelFormatName instance= new TermIsNotFFmpegPixelFormatName();
	//
	private TermIsNotFFmpegPixelFormatName() {
	}
}
