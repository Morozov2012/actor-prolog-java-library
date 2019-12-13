// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run.errors;

public class UndefinedSlotTableEntry extends RuntimeException {
	//
	protected String slotName;
	//
	public UndefinedSlotTableEntry(String name) {
		slotName= name;
	}
}
