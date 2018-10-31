// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.system.*;

import java.nio.charset.CharsetEncoder;

public class ClusterDomainName extends PrologDomainName {
	//
	protected long cluster;
	protected String name;
	//
	public ClusterDomainName(long c, String n) {
		cluster= c;
		name= n;
	}
	//
	public String toString(CharsetEncoder encoder) {
		return tagDomainName_Cluster + "(" + Long.toString(cluster) + ",\"" + FormatOutput.encodeString(name,false,encoder) + "\")";
	}
}
