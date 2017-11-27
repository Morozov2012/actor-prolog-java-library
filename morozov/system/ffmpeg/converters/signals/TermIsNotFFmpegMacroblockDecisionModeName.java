// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.signals;

import morozov.run.*;

public final class TermIsNotFFmpegMacroblockDecisionModeName extends LightweightException {
	public static final TermIsNotFFmpegMacroblockDecisionModeName instance= new TermIsNotFFmpegMacroblockDecisionModeName();
	//
	private TermIsNotFFmpegMacroblockDecisionModeName() {
	}
}
