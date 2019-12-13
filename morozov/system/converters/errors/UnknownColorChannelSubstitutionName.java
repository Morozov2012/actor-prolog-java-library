// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.system.converters.errors;

import morozov.system.*;

public class UnknownColorChannelSubstitutionName extends RuntimeException {
	//
	protected ColorChannelSubstitutionName value;
	//
	public UnknownColorChannelSubstitutionName(ColorChannelSubstitutionName v) {
		value= v;
	}
	//
	@Override
	public String toString() {
		return this.getClass().toString() + "(" + value.toString() + ")";
	}
}
