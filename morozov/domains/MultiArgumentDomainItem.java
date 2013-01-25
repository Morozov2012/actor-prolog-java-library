// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import target.*;

public abstract class MultiArgumentDomainItem extends DomainAlternative {
	protected String[] domainTableEntries;
	protected PrologDomain[] domainItems;
	public MultiArgumentDomainItem(String[] entries) {
		domainTableEntries= entries;
	}
	protected void initiateDomainItemsIfNecessary() {
		if (domainItems==null) {
			synchronized(this) {
				if (domainItems==null) {
					domainItems= new PrologDomain[domainTableEntries.length];
					for (int n=0; n < domainTableEntries.length; n++) {
						domainItems[n]= DomainTable.getDomainAlternatives(domainTableEntries[n]);
					}
				}
			}
		}
	}
}
