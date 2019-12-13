// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import target.*;

import morozov.run.*;

import java.nio.charset.CharsetEncoder;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class WorldDomainName extends PrologDomainName {
	//
	protected long unit;
	//
	private static final long serialVersionUID= 0xB14310DC8BF1C4DCL; // -5673672566129965860L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","WorldDomainName");
	// }
	//
	public WorldDomainName(long u) {
		unit= u;
	}
	// Converting Term to String:
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		stream.writeObject(SymbolNames.retrieveSymbolName(unit));
	}
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		SymbolName symbolName= (SymbolName)stream.readObject();
		unit= SymbolNames.insertSymbolName(symbolName.identifier);
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		String text= SymbolNames.retrieveSymbolName(unit).toRawString(encoder);
		return tagDomainName_World + "(\'" + text + "\')";
	}
}
