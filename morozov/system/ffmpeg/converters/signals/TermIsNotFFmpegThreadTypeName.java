// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.signals;

import morozov.run.*;

public final class TermIsNotFFmpegThreadTypeName extends LightweightException {
	//
	public static final TermIsNotFFmpegThreadTypeName instance= new TermIsNotFFmpegThreadTypeName();
	//
	private TermIsNotFFmpegThreadTypeName() {
	}
}
