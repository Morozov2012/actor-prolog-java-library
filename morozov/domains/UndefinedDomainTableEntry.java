package morozov.domains;

public class UndefinedDomainTableEntry extends RuntimeException {
	public String domainName;
	public UndefinedDomainTableEntry(String name) {
		domainName= name;
	}
}
