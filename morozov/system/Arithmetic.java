// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.classes.*;
import morozov.run.*;
import morozov.system.errors.*;
import morozov.terms.*;

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
	public static void compare_two_numbers(ChoisePoint iX, Term n1, Term n2, ComparisonOperation operation) throws Backtracking {
		n1.compareWithTerm(n2,iX,operation);
	}
	//
	public static boolean realsAreEqual(double v, double value) {
		if (value==v) {
			return true;
		} else {
			long n= DefaultOptions.significantDigitsNumber;
			if (n > 0) {
				String fString= String.format("%%1.%de",n-1);
				String s1= String.format(fString,value);
				String s2= String.format(fString,v);
				if (s1.equals(s2)) {
					return true;
				}
			};
			return false;
		}
	}
	// Arithmetic operations
	public static void calculate_nullary_arithmetic_function(ChoisePoint iX, PrologVariable nV, NullaryArithmeticOperation operation) {
		nV.value= operation.eval();
		// iX.pushTrail(nV);
	}
	//
	public static void calculate_unary_function(ChoisePoint iX, PrologVariable nV, Term n1, UnaryOperation operation) {
		nV.value= n1.evaluate(iX,operation);
		// iX.pushTrail(nV);
	}
	//
	public static void calculate_binary_arithmetic_function(ChoisePoint iX, PrologVariable nV, Term n1, Term n2, BinaryOperation operation) {
		nV.value= n1.reactWithTerm(n2,iX,operation);
		// iX.pushTrail(nV);
	}
	//
	public static void calculate_binary_bitwise_function(ChoisePoint iX, PrologVariable nV, Term n1, Term n2, BinaryOperation operation) {
		nV.value= n1.blitWithTerm(n2,iX,operation);
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
