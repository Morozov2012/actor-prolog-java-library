// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import morozov.system.*;

import java.nio.charset.CharsetEncoder;

public class ClusterDomainName extends PrologDomainName {
	//
	protected long cluster;
	protected String name;
	//
	private static final long serialVersionUID= 0x48BB75329E836F00L; // 5240911451643932416L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","ClusterDomainName");
	// }
	//
	public ClusterDomainName(long c, String n) {
		cluster= c;
		name= n;
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return tagDomainName_Cluster + "(" + Long.toString(cluster) + ",\"" + FormatOutput.encodeString(name,false,encoder) + "\")";
	}
}
