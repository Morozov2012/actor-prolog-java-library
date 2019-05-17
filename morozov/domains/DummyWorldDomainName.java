// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import java.nio.charset.CharsetEncoder;

public class DummyWorldDomainName extends PrologDomainName {
	//
	private static final long serialVersionUID= 0xDA4F1308D688CDD4L; // -2715931121601163820L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DummyWorldDomainName");
	// }
	//
	public DummyWorldDomainName() {
	}
	//
	public String toString(CharsetEncoder encoder) {
		return tagDomainName_DummyWorld;
	}
}
