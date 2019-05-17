// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import java.nio.charset.CharsetEncoder;

public class AnonymousDomainName extends PrologDomainName {
	//
	private static final long serialVersionUID= 0xAA62A048A2639627L; // -6169192305721633241L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","AnonymousDomainName");
	// }
	//
	public AnonymousDomainName() {
	}
	//
	public String toString(CharsetEncoder encoder) {
		return tagDomainName_Anonymous;
	}
}
