// (c) 2014 IRE RAS Alexei A. Morozov

package morozov.system.datum.signals;

import morozov.run.*;

public class SectionNumberDoesNotExist extends LightweightException {
	//
	public static final SectionNumberDoesNotExist instance= new SectionNumberDoesNotExist();
	//
	private SectionNumberDoesNotExist() {
	}
}
