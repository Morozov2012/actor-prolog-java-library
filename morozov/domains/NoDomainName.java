// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import java.nio.charset.CharsetEncoder;

public class NoDomainName extends PrologDomainName {
	//
	private static final long serialVersionUID= 0x4ACAB70325C8EA32L; // 5389321128248928818L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","NoDomainName");
	// }
	//
	public NoDomainName() {
	}
	//
	public String toString(CharsetEncoder encoder) {
		return tagDomainName_None;
	}
}
