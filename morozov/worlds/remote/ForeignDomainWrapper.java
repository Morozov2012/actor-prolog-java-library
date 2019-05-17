// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import morozov.domains.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashSet;

public class ForeignDomainWrapper
		extends DomainAbstractWorld {
	//
	public ExternalDomainInterface stub;
	//
	private static final long serialVersionUID= 0x62B9475553FB9EC5L; // 7113795518215200453L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.worlds.remote","ForeignDomainWrapper");
	// }
	public ForeignDomainWrapper(ExternalDomainInterface s) {
		stub= s;
	}
	//
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToForeignDomain(stub);
	}
	public boolean isEqualToWorld(long value) {
		return true; // Never compare world domains with foreign world domains.
	}
	//
	public String toString(CharsetEncoder encoder) {
		return PrologDomainName.tagDomainAlternative_ForeignDomain;
	}
}
