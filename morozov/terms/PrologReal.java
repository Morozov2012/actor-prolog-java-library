// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.system.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.math.BigInteger;

public class PrologReal extends Term {
	private double value;
	public PrologReal(double v) {
		value= v;
	}
	public int hashCode() {
		return (int)StrictMath.round(value);
	}
	public void isReal(double v, ChoisePoint cp) throws Backtracking {
		if (Arithmetic.realsAreEqual(v,value)) {
			return;
		} else {
			throw Backtracking.instance;
		}
	}
	public double getRealValue(ChoisePoint cp) throws TermIsNotAReal {
		return value;
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isReal(value,cp);
	}
	// Comparison operations
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
		if (!op.eval(Converters.doubleArgumentToBigInteger(value,this).multiply(Converters.oneDayLengthBigInteger),BigInteger.valueOf(a))) {
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
		if (!op.eval(BigInteger.valueOf(a),Converters.doubleArgumentToBigInteger(value,this).multiply(Converters.oneDayLengthBigInteger))) {
			throw Backtracking.instance;
		}
	}
	public void compareListWith(Term aHead, Term aTail, ChoisePoint iX, ComparisonOperation op) throws Backtracking {
		aHead.compareWithDouble(value,iX,op);
		aTail.compareWithDouble(value,iX,op);
	}
	// Arithmetic operations
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
		return op.eval(Converters.doubleArgumentToBigInteger(value,this),a);
	}
	public Term reactWithDate(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalDate(Converters.doubleArgumentToBigInteger(value,this).multiply(Converters.oneDayLengthBigInteger),BigInteger.valueOf(a));
	}
	public Term reactWithTime(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalTime(Converters.doubleArgumentToBigInteger(value,this),BigInteger.valueOf(a));
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
		return op.eval(a,Converters.doubleArgumentToBigInteger(value,this));
	}
	public Term reactDateWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalDate(BigInteger.valueOf(a),Converters.doubleArgumentToBigInteger(value,this).multiply(Converters.oneDayLengthBigInteger));
	}
	public Term reactTimeWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.evalTime(BigInteger.valueOf(a),Converters.doubleArgumentToBigInteger(value,this));
	}
	public Term reactListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return new PrologList(
			aHead.reactWithDouble(value,iX,op),
			aTail.reactWithDouble(value,iX,op));
	}
	// Bitwise operations
	public Term blitWithTerm(Term a, ChoisePoint iX, BinaryOperation op) {
		return a.blitDoubleWith(value,iX,op);
	}
	public Term blitWithBigInteger(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(Converters.doubleArgumentToBigInteger(value,this),a);
	}
	public Term blitWithLong(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(Converters.doubleArgumentToBigInteger(value,this),BigInteger.valueOf(a));
	}
	public Term blitWithDouble(double a, ChoisePoint iX, BinaryOperation op) {
		BigInteger integerValue= Converters.doubleArgumentToBigInteger(value,this);
		return op.eval(integerValue,Converters.doubleValueToBigInteger(a));
	}
	public Term blitBigIntegerWith(BigInteger a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(a,Converters.doubleArgumentToBigInteger(value,this));
	}
	public Term blitLongWith(long a, ChoisePoint iX, BinaryOperation op) {
		return op.eval(BigInteger.valueOf(a),Converters.doubleArgumentToBigInteger(value,this));
	}
	public Term blitDoubleWith(double a, ChoisePoint iX, BinaryOperation op) {
		BigInteger integerValue= Converters.doubleArgumentToBigInteger(value,this);
		return op.eval(Converters.doubleValueToBigInteger(a),integerValue);
	}
	public Term blitListWith(Term aHead, Term aTail, ChoisePoint iX, BinaryOperation op) {
		return new PrologList(
			aHead.blitWithDouble(value,iX,op),
			aTail.blitWithDouble(value,iX,op));
	}
	// Unary operations
	public Term evaluate(ChoisePoint iX, UnaryOperation op) {
		return op.eval(value);
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return FormatOutput.realToString(value);
	}
	// public String toString() {
	//	return FormatOutput.realToString(value);
	// }
}
