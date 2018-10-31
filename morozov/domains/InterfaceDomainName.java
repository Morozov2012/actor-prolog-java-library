// (c) 2011 IRE RAS Alexei A. Morozov

package morozov.domains;

import target.*;

import morozov.run.*;
import morozov.system.*;

import java.nio.charset.CharsetEncoder;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class InterfaceDomainName extends PrologDomainName {
	//
	protected long unit;
	protected String name;
	//
	public InterfaceDomainName(long u, String n) {
		unit= u;
		name= n;
	}
	// Converting Term to String
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
	public String toString(CharsetEncoder encoder) {
		String uN= SymbolNames.retrieveSymbolName(unit).toRawString(encoder);
		return tagDomainName_Interface + "(\'" + uN + "\',\"" + FormatOutput.encodeString(name,false,encoder) + "\")";
	}
}
