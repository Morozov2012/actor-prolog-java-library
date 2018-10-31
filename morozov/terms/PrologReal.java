// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;

public class PrologReal extends Term {
	//
	private Double value;
	//
	public PrologReal(double v) {
		value= v;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Double getDoubleValue() {
		return value;
	}
	public int hashCode() {
		return value.hashCode();
	}
	public boolean equals(Object o2) {
		if (o2 instanceof Term) {
			return ((Term)o2).isEqualToReal(value);
		} else {
			return false;
		}
	}
	public int compare(Object o2) {
		if (o2 instanceof Term) {
			return -((Term)o2).compareWithReal(value);
		} else {
			return 1;
		}
	}
	public boolean isEqualToReal(Double v2) {
		return value.equals(v2);
	}
	public int compareWithReal(Double v2) {
		return value.compareTo(v2);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void isReal(PrologReal v, ChoisePoint cp) throws Backtracking {
		if ( Arithmetic.realsAreEqual(v.getDoubleValue(),value) ) {
			return;
		} else {
			throw Backtracking.instance;
		}
	}
	public void isReal(double v, ChoisePoint cp) throws Backtracking {
		if ( Arithmetic.realsAreEqual(v,value) ) {
			return;
		} else {
			throw Backtracking.instance;
		}
	}
	public double getRealValue(ChoisePoint cp) throws TermIsNotAReal {
		return value;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isReal(value,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void compareWithTerm(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		a.compareDoubleWith(value,iX,op);
	}
	public void compareWithBigInteger(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(value,a.doubleValue())) {
			throw Backtracking.instance;
		}
	}
	public void compareWithLong(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(value,(double)a)) {
			throw Backtracking.instance;
		}
	}
	public void compareWithDouble(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(value,a)) {
			throw Backtracking.instance;
		}
	}
	public void compareWithDate(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(GeneralConverters.doubleArgumentToBigInteger(value,this).multiply(TimeUnits.oneDayLengthInMillisecondsBigInteger),BigInteger.valueOf(a))) {
			throw Backtracking.instance;
		}
	}
	public void compareTermWith(Term a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		a.compareWithDouble(value,iX,op);
	}
	public void compareBigIntegerWith(BigInteger a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(a.doubleValue(),value)) {
			throw Backtracking.instance;
		}
	}
	public void compareLongWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval((double)a,value)) {
			throw Backtracking.instance;
		}
	}
	public void compareDoubleWith(double a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(a,value)) {
			throw Backtracking.instance;
		}
	}
	public void compareDateWith(long a, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		if (!op.eval(BigInteger.valueOf(a),GeneralConverters.doubleArgumentToBigInteger(value,this).multiply(TimeUnits.oneDayLengthInMillisecondsBigInteger))) {
			throw Backtracking.instance;
		}
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		aHead.compareWithDouble(value,iX,op);
		aTail.compareWithDouble(value,iX,op);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term reactWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return a.reactDoubleWith(value,iX,op);
	}
	public Term reactWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a.doubleValue());
	}
	public Term reactWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,(double)a);
	}
	public Term reactWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(value,a);
	}
	public Term reactWithString(String a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(GeneralConverters.doubleArgumentToBigInteger(value,this),a);
	}
	public Term reactWithBinary(byte[] a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(GeneralConverters.doubleArgumentToBigInteger(value,this),a);
	}
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalDate(GeneralConverters.doubleArgumentToBigInteger(value,this).multiply(TimeUnits.oneDayLengthInMillisecondsBigInteger),BigInteger.valueOf(a));
	}
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalTime(GeneralConverters.doubleArgumentToBigInteger(value,this),BigInteger.valueOf(a));
	}
	public Term reactBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a.doubleValue(),value);
	}
	public Term reactLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval((double)a,value);
	}
	public Term reactDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,value);
	}
	public Term reactStringWith(String a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,GeneralConverters.doubleArgumentToBigInteger(value,this));
	}
	public Term reactBinaryWith(byte[] a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,GeneralConverters.doubleArgumentToBigInteger(value,this));
	}
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalDate(BigInteger.valueOf(a),GeneralConverters.doubleArgumentToBigInteger(value,this).multiply(TimeUnits.oneDayLengthInMillisecondsBigInteger));
	}
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalTime(BigInteger.valueOf(a),GeneralConverters.doubleArgumentToBigInteger(value,this));
	}
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return new PrologList(
			aHead.reactWithDouble(value,iX,op),
			aTail.reactWithDouble(value,iX,op));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return a.blitDoubleWith(value,iX,op);
	}
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(GeneralConverters.doubleArgumentToBigInteger(value,this),a);
	}
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(GeneralConverters.doubleArgumentToBigInteger(value,this),BigInteger.valueOf(a));
	}
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		BigInteger integerValue= GeneralConverters.doubleArgumentToBigInteger(value,this);
		return op.eval(integerValue,GeneralConverters.doubleValueToBigInteger(a));
	}
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,GeneralConverters.doubleArgumentToBigInteger(value,this));
	}
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(BigInteger.valueOf(a),GeneralConverters.doubleArgumentToBigInteger(value,this));
	}
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		BigInteger integerValue= GeneralConverters.doubleArgumentToBigInteger(value,this);
		return op.eval(GeneralConverters.doubleValueToBigInteger(a),integerValue);
	}
	public Term blitListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return new PrologList(
			aHead.blitWithDouble(value,iX,op),
			aTail.blitWithDouble(value,iX,op));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		return op.eval(value);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return FormatOutput.realToString(value);
	}
	// public String toString() {
	//	return FormatOutput.realToString(value);
	// }
}
