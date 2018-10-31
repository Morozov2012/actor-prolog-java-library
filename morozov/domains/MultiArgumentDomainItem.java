// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import target.*;

import morozov.system.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;

public abstract class MultiArgumentDomainItem extends DomainAlternative {
	//
	protected String[] domainTableEntries;
	protected transient PrologDomain[] domainItems;
	//
	public MultiArgumentDomainItem(String[] entries) {
		domainTableEntries= entries;
	}
	//
	public void initiate() {
		if (null == domainItems) {
			domainItems= new PrologDomain[domainTableEntries.length];
			for (int n=0; n < domainTableEntries.length; n++) {
				domainItems[n]= DomainTable.getDomainAlternatives(domainTableEntries[n]);
			}
		}
	}
	//
	public void collectLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
		for (int n=0; n < domainItems.length; n++) {
			if (!localDomainTable.containsKey(domainTableEntries[n])) {
				localDomainTable.put(domainTableEntries[n],domainItems[n]);
				domainItems[n].collectLocalDomainTable(localDomainTable);
			}
		}
	}
	public void acceptLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
		if (domainItems == null) {
			domainItems= new PrologDomain[domainTableEntries.length];
			for (int n=0; n < domainItems.length; n++) {
				domainItems[n]= localDomainTable.get(domainTableEntries[n]);
			}
		}
	}
	//
	abstract protected String getMultiArgumentDomainTag();
	//
	public String toString(CharsetEncoder encoder) {
		StringBuffer buffer= new StringBuffer();
		buffer.append(getMultiArgumentDomainTag());
		buffer.append("([");
		if (domainTableEntries.length > 0) {
			buffer.append("\"");
			buffer.append(FormatOutput.encodeString(domainTableEntries[0],false,encoder));
			for (int n=1; n < domainTableEntries.length; n++) {
				buffer.append("\",\"");
				buffer.append(FormatOutput.encodeString(domainTableEntries[n],false,encoder));
			};
			buffer.append("\"");
		};
		buffer.append("])");
		return buffer.toString();
	}
}
