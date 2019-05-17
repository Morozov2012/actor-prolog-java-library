// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;
import java.util.Arrays;

public class PrologBinary extends Term {
	//
	private byte[] value;
	//
	private static final long serialVersionUID= 0x8F588CA49DAD4B27L; // -8117583689687413977L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","PrologBinary");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologBinary(byte[] v) {
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public byte[] getByteArray() {
		return value;
	}
	//
	public int hashCode() {
		return Arrays.hashCode(value);
	}
	//
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToBinary(value);
		} else {
			return false;
		}
	}
	public boolean isEqualToBinary(byte[] v2) {
		return twoBinariesAreEqual(value,v2);
	}
	//
	public static boolean twoBinariesAreEqual(byte[] v1, byte[] v2) {
		if (v1.length != v2.length) {
			return false;
		} else {
			for (int k=0; k < v1.length; k++) {
				if (v1[k] != v2[k]) {
					return false;
				}
			};
			return true;
		}
	}
	//
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithBinary(value);
		} else {
			return 1;
		}
	}
	public int compareWithBinary(byte[] v2) {
		return compareTwoBinaries(value,v2);
	}
	//
	public static int compareTwoBinaries(byte[] v1, byte[] v2) {
		for (int k=0; k < v1.length; k++) {
			if (v1[k] != v2[k]) {
				return (int)v1[k] - (int)v2[k];
			}
		};
		return v1.length - v2.length;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isBinary(PrologBinary v, ChoisePoint cp) throws Backtracking {
		if ( !twoBinariesAreEqual(value,v.getByteArray()) ) {
			throw Backtracking.instance;
		}
	}
	public void isBinary(byte[] v, ChoisePoint cp) throws Backtracking {
		if ( !twoBinariesAreEqual(value,v) ) {
			throw Backtracking.instance;
		}
	}
	//
	public byte[] getBinaryValue(ChoisePoint cp) throws TermIsNotABinary {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isBinary(value,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		a.compareBinaryWith(value,iX,op);
	}
	public void compareWithBinary(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if ( !op.eval(value,a) ) {
			throw Backtracking.instance;
		}
	}
	public void compareBinaryWith(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if ( !op.eval(a,value) ) {
			throw Backtracking.instance;
		}
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		aHead.compareWithBinary(value,iX,op);
		aTail.compareWithBinary(value,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return a.reactBinaryWith(value,iX,op);
	}
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a);
	}
	public Term reactWithBinary(byte[] a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a);
	}
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value);
	}
	public Term reactBinaryWith(byte[] a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value);
	}
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return new PrologList(
			aHead.reactWithBinary(value,iX,op),
			aTail.reactWithBinary(value,iX,op));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static String binaryToString(byte[] array) {
		StringBuilder buffer= new StringBuilder();
		for (int k=0; k < array.length; k++) {
			buffer.append(String.format("%02X",array[k]));
		};
		return buffer.toString();
	}
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		String text= binaryToString(value);
		if (isInner || provideStrictSyntax) {
			return "~" + text;
		} else {
			return text;
		}
	}
}
