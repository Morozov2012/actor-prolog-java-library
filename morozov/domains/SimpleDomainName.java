// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.system.*;

import java.nio.charset.CharsetEncoder;

public class SimpleDomainName extends PrologDomainName {
	//
	protected String name;
	//
	private static final long serialVersionUID= 0xA77759138EA2B4D6L; // -6379532406614739754L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","SimpleDomainName");
	// }
	//
	public SimpleDomainName(String n) {
		name= n;
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return tagDomainName_Simple + "(\"" + FormatOutput.encodeString(name,false,encoder) + "\")";
	}
}
