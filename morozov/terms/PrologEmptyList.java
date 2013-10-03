// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;

public final class PrologEmptyList extends Term {
	public static final PrologEmptyList instance= new PrologEmptyList();
	//
	private PrologEmptyList() {
	}
	//
	public void isEmptyList(ChoisePoint cp) throws Backtracking {
	}
	public boolean thisIsEmptyList() {
		return true;
	}
	public void isOutputEmptyList(ChoisePoint cp) {
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isEmptyList(cp);
	}
	public Term getNextListHead(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw EndOfList.instance;
	}
	public Term getNextListTail(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw EndOfList.instance;
	}
	public Term getNextListHeadSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw EndOfList.instance;
	}
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw EndOfList.instance;
	}
	// Comparison operations
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	public void compareWithString(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	public void compareStringWith(String a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
	}
	// Arithmetic operations
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	// Bitwise operations
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return instance;
	}
	// Unary operations
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		return instance;
	}
	// Converting Term to String
	// public String toString() {
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return "[]";
	}
}
