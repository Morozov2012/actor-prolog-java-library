// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.domains.*;
import morozov.domains.signals.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class MethodArgument implements Serializable {
	//
	protected MethodArgumentDirection direction;
	protected String domainName;
	protected transient PrologDomain domain;
	//
	private static final long serialVersionUID= 0x49B573A1B3B32849L; // 5311278473895159881L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","MethodArgument");
	// }
	//
	public MethodArgument(MethodArgumentDirection f, String n, PrologDomain d) {
		direction= f;
		domainName= n;
		domain= d;
	}
	//
	public MethodArgumentDirection getDirection() {
		return direction;
	}
	public String getDomainName() {
		return domainName;
	}
	public PrologDomain getDomain() {
		return domain;
	}
	//
	public void collectLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
		if (!localDomainTable.containsKey(domainName)) {
			localDomainTable.put(domainName,domain);
		};
		domain.collectLocalDomainTable(localDomainTable);
	}
	//
	public void acceptLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
		if (domain == null) {
			domain= localDomainTable.get(domainName);
		};
		domain.acceptLocalDomainTable(localDomainTable);
	}
	//
	public boolean match(MethodArgument foreignArgument) {
		if (!direction.equals(foreignArgument.direction)) {
			return false;
		};
		try {
			domain.isEqualTo(foreignArgument.domain,new HashSet<PrologDomainPair>());
		} catch (PrologDomainsAreNotEqual e) {
			return false;
		};
		return true;
	}
}
