// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

public class InterfaceDomainName extends PrologDomainName {
	protected long unit;
	protected String name;
	public InterfaceDomainName(long u, String n) {
		unit= u;
		name= n;
	}
}
