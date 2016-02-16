// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import java.nio.charset.CharsetEncoder;

public class AnonymousDomainName extends PrologDomainName {
	public AnonymousDomainName() {
	}
	//
	public String toString(CharsetEncoder encoder) {
		return tagDomainName_Anonymous;
	}
}
