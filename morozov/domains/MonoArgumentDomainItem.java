// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import target.*;

import morozov.system.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;

public abstract class MonoArgumentDomainItem extends DomainAlternative {
	//
	protected String domainTableEntry;
	protected transient PrologDomain domainItem;
	//
	private static final long serialVersionUID= 0xFC5963000F620330L; // -263070251315231952L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","MonoArgumentDomainItem");
	// }
	//
	public MonoArgumentDomainItem(String entry) {
		domainTableEntry= entry;
	}
	//
	public void initiate() {
		// synchronized (this) {
		if (domainItem==null) {
			domainItem= DomainTable.getDomainAlternatives(domainTableEntry);
		}
		// }
	}
	//
	public void collectLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
		// initiateDomainItemIfNecessary();
		if (!localDomainTable.containsKey(domainTableEntry)) {
			localDomainTable.put(domainTableEntry,domainItem);
			domainItem.collectLocalDomainTable(localDomainTable);
		}
	}
	public void acceptLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
		// initiateDomainItemIfNecessary();
		if (domainItem == null) {
			domainItem= localDomainTable.get(domainTableEntry);
		}
	}
	//
	abstract protected String getMonoArgumentDomainTag();
	//
	public String toString(CharsetEncoder encoder) {
		// initiateDomainItemIfNecessary();
		StringBuffer buffer= new StringBuffer();
		buffer.append(getMonoArgumentDomainTag());
		buffer.append("(\"");
		buffer.append(FormatOutput.encodeString(domainTableEntry,false,encoder));
		buffer.append("\")");
		return buffer.toString();
	}
}
