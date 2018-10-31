// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.signals.*;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.nio.charset.CharsetEncoder;

public class PrologSymbol extends Term {
	//
	private long value;
	//
	public PrologSymbol(long v) {
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public long getSymbolFunctor() {
		return value;
	}
	public int hashCode() {
		return calculateHashCode(value);
	}
	public static int calculateHashCode(long functor) {
		return (int)( functor ^ (functor >>> 32) );
	}
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToSymbol(value);
		} else {
			return false;
		}
	}
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithSymbol(value);
		} else {
			return 1;
		}
	}
	public boolean isEqualToSymbol(long v2) {
		return value==v2;
	}
	public int compareWithSymbol(long v2) {
		return Long.compare(value,v2);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isSymbol(PrologSymbol v, ChoisePoint cp) throws Backtracking {
		if ( value != v.getSymbolFunctor() )
			throw Backtracking.instance;
	}
	public void isSymbol(long v, ChoisePoint cp) throws Backtracking {
		if ( value != v )
			throw Backtracking.instance;
	}
	public long getSymbolValue(ChoisePoint cp) throws TermIsNotASymbol {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isSymbol(value,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		SymbolName name= SymbolNames.retrieveSymbolName(value);
		a.compareStringWith(name.identifier,iX,op);
	}
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		SymbolName name= SymbolNames.retrieveSymbolName(value);
		if ( !op.eval(name.identifier,a) ) {
			throw Backtracking.instance;
		}
	}
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		SymbolName name= SymbolNames.retrieveSymbolName(value);
		if ( !op.eval(a,name.identifier) ) {
			throw Backtracking.instance;
		}
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		SymbolName name= SymbolNames.retrieveSymbolName(value);
		aHead.compareWithString(name.identifier,iX,op);
		aTail.compareWithString(name.identifier,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		stream.writeObject(SymbolNames.retrieveSymbolName(value));
	}
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		SymbolName symbolName= (SymbolName)stream.readObject();
		value= SymbolNames.insertSymbolName(symbolName.identifier);
	}
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		SymbolName name= SymbolNames.retrieveSymbolName(value);
		if (isInner) {
			// return "'" + FormatOutput.encodeString(name.identifier,true,encoder) + "'";
			return "'" + name.toRawString(encoder) + "'";
		} else if (provideStrictSyntax) {
			// return name.toString(encoder);
			return "'" + name.toRawString(encoder) + "'";
		} else {
			return name.identifier;
		}
	}
}
