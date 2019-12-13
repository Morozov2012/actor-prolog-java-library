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
	@Override
	public void initiate() {
		if (domainItem==null) {
			domainItem= DomainTable.getDomainAlternatives(domainTableEntry);
		}
	}
	//
	@Override
	public void collectLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
		if (!localDomainTable.containsKey(domainTableEntry)) {
			localDomainTable.put(domainTableEntry,domainItem);
			domainItem.collectLocalDomainTable(localDomainTable);
		}
	}
	@Override
	public void acceptLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
		if (domainItem == null) {
			domainItem= localDomainTable.get(domainTableEntry);
		}
	}
	//
	abstract protected String getMonoArgumentDomainTag();
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		StringBuilder buffer= new StringBuilder();
		buffer.append(getMonoArgumentDomainTag());
		buffer.append("(\"");
		buffer.append(FormatOutput.encodeString(domainTableEntry,false,encoder));
		buffer.append("\")");
		return buffer.toString();
	}
}
