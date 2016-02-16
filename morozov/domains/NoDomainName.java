// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import java.nio.charset.CharsetEncoder;

public class NoDomainName extends PrologDomainName {
	public NoDomainName() {
	}
	//
	public String toString(CharsetEncoder encoder) {
		return tagDomainName_None;
	}
}
