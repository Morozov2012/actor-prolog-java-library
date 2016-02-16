// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.system.*;

import java.nio.charset.CharsetEncoder;

public class SimpleDomainName extends PrologDomainName {
	protected String name;
	//
	public SimpleDomainName(String n) {
		name= n;
	}
	//
	public String toString(CharsetEncoder encoder) {
		return tagDomainName_Simple + "(\"" + FormatOutput.encodeString(name,false,encoder) + "\")";
	}
}
