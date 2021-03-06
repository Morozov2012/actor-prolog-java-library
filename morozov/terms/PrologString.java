// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;

public class PrologString extends Term {
	//
	protected String value;
	//
	private static final long serialVersionUID= 0xB667B410A08D5CBFL; // -5303072052699374401L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","PrologString");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologString(String v) {
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String getStringValue() {
		return value;
	}
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	//
	@Override
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToString(value);
		} else {
			return false;
		}
	}
	@Override
	public boolean isEqualToString(String v2) {
		return value.equals(v2);
	}
	//
	@Override
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithString(value);
		} else {
			return 1;
		}
	}
	@Override
	public int compareWithString(String v2) {
		return value.compareTo(v2);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isString(PrologString v, ChoisePoint cp) throws Backtracking {
		if ( !value.equals(v.getStringValue()) ) {
			throw Backtracking.instance;
		}
	}
	@Override
	public void isString(String v, ChoisePoint cp) throws Backtracking {
		if ( !value.equals(v) ) {
			throw Backtracking.instance;
		}
	}
	@Override
	public String getStringValue(ChoisePoint cp) throws TermIsNotAString {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isString(value,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		a.compareStringWith(value,iX,op);
	}
	@Override
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if ( !op.eval(value,a) ) {
			throw Backtracking.instance;
		}
	}
	@Override
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if ( !op.eval(a,value) ) {
			throw Backtracking.instance;
		}
	}
	@Override
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		aHead.compareWithString(value,iX,op);
		aTail.compareWithString(value,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return a.reactStringWith(value,iX,op);
	}
	@Override
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a);
	}
	@Override
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a);
	}
	@Override
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value);
	}
	@Override
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value);
	}
	@Override
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return new PrologList(
			aHead.reactWithString(value,iX,op),
			aTail.reactWithString(value,iX,op));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		if (isInner || provideStrictSyntax) {
			return "\"" + FormatOutput.encodeString(value,false,encoder) + "\"";
		} else {
			return value;
		}
	}
}
