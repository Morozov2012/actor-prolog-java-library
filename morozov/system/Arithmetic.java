// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.run.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class Arithmetic {
	// "Check all" operations
	public static void check_all_arguments(ChoisePoint cp, Term[] array, TermCheckOperation operation) throws Backtracking {
		for(int i= 0; i < array.length; i++) {
			if (!operation.eval(cp,array[i].dereferenceValue(cp))) {
				throw Backtracking.instance;
			}
		}
	}
	public static void check_all_arguments(ChoisePoint cp, Term[] array, TermCheckOperation operation, ActiveWorld currentProcess) throws Backtracking {
		for(int i= 0; i < array.length; i++) {
			if (!operation.eval(cp,array[i].dereferenceValue(cp),currentProcess)) {
				throw Backtracking.instance;
			}
		}
	}
	// Comparison operations
	public static void compare_two_numbers(ChoisePoint iX, Term a1, Term a2, ComparisonOperation operation) throws Backtracking {
		a1.compareWithTerm(a2,iX,operation);
	}
	//
	public static boolean realsAreEqual(double left, double right) {
		// If d1 represents +0.0 while d2 represents -0.0, or vice versa,
		// the equal test has the value false, even though +0.0==-0.0
		// has the value true.
		if (right==left) {
			return true;
		} else {
			int precision= DefaultOptions.significantDigitsNumber;
			if (precision > 0) {
				MathContext realNumberComparisonContext= DefaultOptions.realNumberComparisonContext;
				// Format String Syntax: The following conversions may be applied to float, Float, double
				// and Double ('e'): If the precision is less than the number of digits which would appear
				// after the decimal point in the string returned by Float.toString(float) or
				// Double.toString(double) respectively, then the value will be rounded using
				// the round half up algorithm.
				// ROUND_HALF_UP: Rounding mode to round towards "nearest neighbor" unless both neighbors are
				// equidistant, in which case round up. Behaves as for ROUND_UP if the discarded fraction is
				// >= 0.5; otherwise, behaves as for ROUND_DOWN. Note that this is the rounding mode that most
				// of us were taught in grade school. (?!-A.M.)
				// HALF_DOWN: Rounding mode to round towards "nearest neighbor" unless both neighbors
				// are equidistant, in which case round down. Behaves as for RoundingMode.UP if the discarded
				// fraction is > 0.5; otherwise, behaves as for RoundingMode.DOWN.
				// MathContext realNumberComparisonContext= new MathContext(precision,RoundingMode.HALF_DOWN);
				// HALF_EVEN: Rounding mode to round towards the "nearest neighbor" unless both neighbors are
				// equidistant, in which case, round towards the even neighbor. Behaves as for RoundingMode.HALF_UP
				// if the digit to the left of the discarded fraction is odd; behaves as for RoundingMode.HALF_DOWN
				// if it's even. Note that this is the rounding mode that statistically minimizes cumulative error
				// when applied repeatedly over a sequence of calculations. It is sometimes known as "Banker's
				// rounding," and is chiefly used in the USA. This rounding mode is analogous to the rounding
				// policy used for float and double arithmetic in Java.
				// MathContext realNumberComparisonContext= new MathContext(precision,RoundingMode.HALF_EVEN);
				BigDecimal decimal1= BigDecimal.valueOf(left).round(realNumberComparisonContext);
				BigDecimal decimal2= BigDecimal.valueOf(right).round(realNumberComparisonContext);
				// Compares this BigDecimal with the specified BigDecimal. Two BigDecimal
				// objects that are equal in value but have a different scale (like 2.0
				// and 2.00) are considered equal by this method.
				if (decimal1.compareTo(decimal2)==0) {
					return true;
				}
			};
			return false;
		}
	}
	// public static boolean realsAreEqual(double v, double value) {
	//	if (value==v) {
	//		return true;
	//	} else {
	//		long n= DefaultOptions.significantDigitsNumber;
	//		if (n > 0) {
	//			String fString= String.format("%%1.%de",n-1);
	//			String s1= String.format(fString,value);
	//			String s2= String.format(fString,v);
	//			if (s1.equals(s2)) {
	//				return true;
	//			}
	//		};
	//		return false;
	//	}
	// }
	//
	// Arithmetic operations
	public static void calculate_nullary_arithmetic_function(ChoisePoint iX, PrologVariable result, NullaryArithmeticOperation operation) {
		result.setNonBacktrackableValue(operation.eval());
		// iX.pushTrail(nV);
	}
	//
	public static void calculate_unary_function(ChoisePoint iX, PrologVariable result, Term a1, UnaryOperation operation) {
		result.setNonBacktrackableValue(a1.evaluate(iX,operation));
		// iX.pushTrail(nV);
	}
	//
	public static void calculate_binary_arithmetic_function(ChoisePoint iX, PrologVariable result, Term a1, Term a2, BinaryOperation operation) {
		result.setNonBacktrackableValue(a1.reactWithTerm(a2,iX,operation));
		// iX.pushTrail(nV);
	}
	//
	public static void calculate_binary_bitwise_function(ChoisePoint iX, PrologVariable result, Term a1, Term a2, BinaryOperation operation) {
		result.setNonBacktrackableValue(a1.blitWithTerm(a2,iX,operation));
		// iX.pushTrail(nV);
	}
	//
	public static Term calculate_multi_argument_function(ChoisePoint iX, MultiArgumentArithmeticOperation operation, Term... args) {
		ArrayList<Term> argumentTable= new ArrayList<Term>();
		for(int i= 0; i < args.length; i++) {
			Term item= args[i];
			if (item.thisIsArgumentNumber()) {
				Term extraItems= args[i+1];
				for (int j= 0; j < item.getNumber(); j++) {
					if (j > 0) {
						extraItems= extraItems.getExistentTail();
					};
					Term extraItem= extraItems.getExistentHead();
					argumentTable.add(extraItem);
				};
				break;
			} else {
				argumentTable.add(item);
			};
		};
		if (argumentTable.size() > 0) {
			ExtremumValue currentExtremum= new ExtremumValue();
			for(int i= 0; i < argumentTable.size(); i++) {
				currentExtremum.refine(iX,operation,argumentTable.get(i));
			};
			return currentExtremum.value();
		} else {
			throw new NoArgumentsAreProvided();
		}
	}
}
