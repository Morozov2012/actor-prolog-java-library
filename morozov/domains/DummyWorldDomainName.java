// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import java.nio.charset.CharsetEncoder;

public class DummyWorldDomainName extends PrologDomainName {
	//
	public DummyWorldDomainName() {
	}
	//
	public String toString(CharsetEncoder encoder) {
		return tagDomainName_DummyWorld;
	}
}
