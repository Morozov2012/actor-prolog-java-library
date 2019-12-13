// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import target.*;

import morozov.run.*;
import morozov.terms.*;

import java.nio.charset.CharsetEncoder;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.HashSet;

public class DomainWorld extends DomainAbstractWorld {
	//
	protected long constantCode;
	//
	private static final long serialVersionUID= 0xA89BCB00BE85E810L; // -6297216449890621424L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainWorld");
	// }
	//
	public DomainWorld(long code) {
		constantCode= code;
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			if (t.thisIsOwnWorld()) {
				long[] classes= t.getClassHierarchy();
				for (int i= 0; i < classes.length; i++) {
					if (constantCode == classes[i]) {
						return true;
					}
				};
				classes= t.getInterfaceHierarchy();
				for (int i= 0; i < classes.length; i++) {
					if (constantCode == classes[i]) {
						return true;
					}
				};
				return false;
			} else if (t.thisIsForeignWorld()) {
				return true; // Never compare worlds with foreign worlds.
			} else {
				return false;
			}
		}
	}
	//
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToWorld(constantCode);
	}
	@Override
	public boolean isEqualToWorld(long value) {
		return constantCode == value;
	}
	// Converting Term to String:
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		stream.writeObject(SymbolNames.retrieveSymbolName(constantCode));
	}
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		SymbolName symbolName= (SymbolName)stream.readObject();
		constantCode= SymbolNames.insertSymbolName(symbolName.identifier);
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		String text= SymbolNames.retrieveSymbolName(constantCode).toRawString(encoder);
		return PrologDomainName.tagDomainAlternative_World + "(\'" + text + "\')";
	}
}
