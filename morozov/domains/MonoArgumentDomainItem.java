// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import target.*;

public abstract class MonoArgumentDomainItem extends DomainAlternative {
	protected String domainTableEntry;
	protected PrologDomain domainItem;
	public MonoArgumentDomainItem(String entry) {
		domainTableEntry= entry;
	}
	protected void initiateDomainItemIfNecessary() {
		if (domainItem==null) {
			synchronized(this) {
				if (domainItem==null) {
					domainItem= DomainTable.getDomainAlternatives(domainTableEntry);
				}
			}
		}
	}
}
