// (c) 2013 IRE RAS Alexei A. Morozov

package morozov.domains.errors;

public class UndefinedDomainTableEntry extends RuntimeException {
	//
	protected String domainName;
	//
	public UndefinedDomainTableEntry(String name) {
		domainName= name;
	}
}
