// (c) 2017 IRE RAS Alexei A. Morozov

package morozov.system.ffmpeg.converters.signals;

import morozov.run.*;

public final class TermIsNotFFmpegStandardComplianceName extends LightweightException {
	//
	public static final TermIsNotFFmpegStandardComplianceName instance= new TermIsNotFFmpegStandardComplianceName();
	//
	private TermIsNotFFmpegStandardComplianceName() {
	}
}
