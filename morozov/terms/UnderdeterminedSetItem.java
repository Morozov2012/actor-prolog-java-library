// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.terms;

import target.*;

import morozov.run.*;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public abstract class UnderdeterminedSetItem extends UnderdeterminedSetWithTail {
	//
	protected long name;
	//
	private static final long serialVersionUID= 0x7602FF0B9F24BE4FL; // 8503639471808626255L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","UnderdeterminedSetItem");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		if (name < 0) {
			stream.writeObject(SymbolNames.retrieveSymbolName(-name));
		};
	}
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		if (name < 0) {
			SymbolName symbolName= (SymbolName)stream.readObject();
			name= - SymbolNames.insertSymbolName(symbolName.identifier);
		}
	}
}
