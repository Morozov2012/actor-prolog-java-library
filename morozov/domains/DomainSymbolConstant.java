// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import target.*;

import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.HashSet;

public class DomainSymbolConstant extends DomainAlternative {
	//
	protected long constantCode;
	//
	private static final long serialVersionUID= 0x7B4C68F4D321630CL; // 8884591565618635532L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainSymbolConstant");
	// }
	//
	public DomainSymbolConstant(long code) {
		constantCode= code;
	}
	//
	@Override
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		t= t.dereferenceValue(cp);
		if (ignoreFreeVariables && t.thisIsFreeVariable()) {
			return true;
		} else {
			try {
				if (constantCode == t.getSymbolValue(cp)) {
					return true;
				} else {
					return false;
				}
			} catch (TermIsNotASymbol e) {
				return false;
			}
		}
	}
	//
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return a.isEqualToSymbolConstant(constantCode);
	}
	@Override
	public boolean isEqualToSymbolConstant(long value) {
		return constantCode == value;
	}
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return false;
	}
	@Override
	public boolean isCoveredBySymbol() {
		return true;
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
		return PrologDomainName.tagDomainAlternative_SymbolConstant + "(\'" + text + "\')";
	}
}
