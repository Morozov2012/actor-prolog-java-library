// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;


import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;

public class PrologInteger extends Term {
	//
	protected BigInteger value;
	//
	public static PrologInteger ZERO= new PrologInteger(0);
	public static PrologInteger ONE= new PrologInteger(1);
	//
	private static final long serialVersionUID= 0xFAE707ED44C5CCBEL; // -367316128964948802L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","PrologInteger");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologInteger(long v) {
		value= BigInteger.valueOf(v);
	}
	public PrologInteger(BigInteger v) {
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public BigInteger getBigInteger() {
		return value;
	}
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	@Override
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToInteger(value);
		} else {
			return false;
		}
	}
	@Override
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithInteger(value);
		} else {
			return 1;
		}
	}
	@Override
	public boolean isEqualToInteger(BigInteger v2) {
		return value.equals(v2);
	}
	@Override
	public int compareWithInteger(BigInteger v2) {
		return value.compareTo(v2);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void isInteger(PrologInteger v, ChoisePoint cp) throws Backtracking {
		if ( !value.equals(v.getBigInteger()) )
			throw Backtracking.instance;
	}
	@Override
	public void isInteger(int v, ChoisePoint cp) throws Backtracking {
		if ( !value.equals(BigInteger.valueOf(v) ))
			throw Backtracking.instance;
	}
	@Override
	public void isInteger(BigInteger v, ChoisePoint cp) throws Backtracking {
		if ( !value.equals(v) )
			throw Backtracking.instance;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public BigInteger getIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		return value;
	}
	@Override
	public int getSmallIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		return Arithmetic.toInteger(value);
	}
	@Override
	public long getLongIntegerValue(ChoisePoint cp) throws TermIsNotAnInteger {
		return Arithmetic.toLong(value);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isInteger(value,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		a.compareBigIntegerWith(value,iX,op);
	}
	@Override
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(value,a)) {
			throw Backtracking.instance;
		}
	}
	@Override
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(value,BigInteger.valueOf(a))) {
			throw Backtracking.instance;
		}
	}
	@Override
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(value.doubleValue(),a)) {
			throw Backtracking.instance;
		}
	}
	@Override
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(value.multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger),BigInteger.valueOf(a))) {
			throw Backtracking.instance;
		}
	}
	@Override
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		a.compareWithBigInteger(value,iX,op);
	}
	@Override
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(a,value)) {
			throw Backtracking.instance;
		}
	}
	@Override
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(BigInteger.valueOf(a),value)) {
			throw Backtracking.instance;
		}
	}
	@Override
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(a,value.doubleValue())) {
			throw Backtracking.instance;
		}
	}
	@Override
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(BigInteger.valueOf(a),value.multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger))) {
			throw Backtracking.instance;
		}
	}
	@Override
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		aHead.compareWithBigInteger(value,iX,op);
		aTail.compareWithBigInteger(value,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return a.reactBigIntegerWith(value,iX,op);
	}
	@Override
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a);
	}
	@Override
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,BigInteger.valueOf(a));
	}
	@Override
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value.doubleValue(),a);
	}
	@Override
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a);
	}
	@Override
	public Term reactWithBinary(byte[] a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a);
	}
	@Override
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalDate(value.multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger),BigInteger.valueOf(a));
	}
	@Override
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalTime(value,BigInteger.valueOf(a));
	}
	@Override
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value);
	}
	@Override
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(BigInteger.valueOf(a),value);
	}
	@Override
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value.doubleValue());
	}
	@Override
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value);
	}
	@Override
	public Term reactBinaryWith(byte[] a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value);
	}
	@Override
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalDate(BigInteger.valueOf(a),value.multiply(TimeUnitsConverters.oneDayLengthInMillisecondsBigInteger));
	}
	@Override
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalTime(BigInteger.valueOf(a),value);
	}
	@Override
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return new PrologList(
			aHead.reactWithBigInteger(value,iX,op),
			aTail.reactWithBigInteger(value,iX,op));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return a.blitBigIntegerWith(value,iX,op);
	}
	@Override
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a);
	}
	@Override
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,BigInteger.valueOf(a));
	}
	@Override
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,GeneralConverters.doubleValueToBigInteger(a));
	}
	@Override
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value);
	}
	@Override
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(BigInteger.valueOf(a),value);
	}
	@Override
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(GeneralConverters.doubleValueToBigInteger(a),value);
	}
	@Override
	public Term blitListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return new PrologList(
			aHead.blitWithBigInteger(value,iX,op),
			aTail.blitWithBigInteger(value,iX,op));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		return op.eval(value);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return FormatOutput.integerToString(value);
	}
}
