// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

public class ClusterDomainName extends PrologDomainName {
	protected long cluster;
	protected String name;
	public ClusterDomainName(long c, String n) {
		cluster= c;
		name= n;
	}
}
