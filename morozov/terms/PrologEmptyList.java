// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;

public final class PrologEmptyList extends Term {
	//
	public static final PrologEmptyList instance= new PrologEmptyList();
	//
	private static final long serialVersionUID= 0x107661AF6767A43CL; // 1186242957840720956L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","PrologEmptyList");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	private PrologEmptyList() {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToEmptyList();
		} else {
			return false;
		}
	}
	@Override
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithEmptyList();
		} else {
			return 1;
		}
	}
	@Override
	public boolean isEqualToEmptyList() {
		return true;
	}
	@Override
	public int compareWithEmptyList() {
		return 0;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isEmptyList(ChoisePoint cp) throws Backtracking {
	}
	@Override
	public boolean thisIsEmptyList() {
		return true;
	}
	@Override
	public void isOutputEmptyList(ChoisePoint cp) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isEmptyList(cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term getNextListHead(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw EndOfList.instance;
	}
	@Override
	public Term getNextListTail(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw EndOfList.instance;
	}
	@Override
	public Term getNextListHeadSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw EndOfList.instance;
	}
	@Override
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw EndOfList.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareWithBinary(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareBinaryWith(byte[] a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	@Override
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactWithBinary(byte[] a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactBinaryWith(byte[] a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	@Override
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		return instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return "[]";
	}
}
