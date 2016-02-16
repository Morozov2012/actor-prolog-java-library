// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.domains.*;
import morozov.domains.signals.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class MethodArgument implements Serializable {
	//
	public MethodArgumentDirection direction;
	public String domainName;
	public transient PrologDomain domain;
	//
	public MethodArgument(MethodArgumentDirection f, String n, PrologDomain d) {
		direction= f;
		domainName= n;
		domain= d;
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
