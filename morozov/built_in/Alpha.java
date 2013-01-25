// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import morozov.classes.*;
import morozov.syntax.scanner.*;
import morozov.syntax.*;
import morozov.system.*;
import morozov.run.*;
import morozov.terms.*;

import java.math.BigInteger;
import java.util.Arrays;

public abstract class Alpha extends AbstractWorld {
	//
	public void goal0s(ChoisePoint iX) {
	}
	//
	public class Goal0s extends Continuation {
		//
		public Goal0s(Continuation aC) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public void alarm1s(ChoisePoint iX, Term exceptionName) throws Backtracking {
		throw new Backtracking();
	}
	//
	public class Alarm1s extends Continuation {
		public Alarm1s(Continuation aC, Term exceptionName) {
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			throw new Backtracking();
		}
	}
	//
	public class Repeat0s extends Continuation {
		//
		public Repeat0s(Continuation aC) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			ChoisePoint newIx= new ChoisePoint(iX);
			while(true) {
				try {
					c0.execute(newIx);
				} catch (Backtracking b) {
					if (newIx.isEnabled()) {
						newIx.freeTrail();
						continue;
					} else {
						throw new Backtracking();
					}
				};
				return;
			}
		}
	}
	//
	public void free1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,CheckTermOperation.FREE);
	}
	//
	public void bound1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,CheckTermOperation.BOUND);
	}
	//
	public void symbol1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,CheckTermOperation.SYMBOL);
	}
	//
	public void string1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,CheckTermOperation.STRING);
	}
	//
	public void integer1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,CheckTermOperation.INTEGER);
	}
	//
	public void real1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,CheckTermOperation.REAL);
	}
	//
	public void numerical1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,CheckTermOperation.NUMERICAL);
	}
	//
	public void classInstance1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,CheckTermOperation.CLASS_INSTANCE);
	}
	//
	public void internalWorld1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,CheckTermOperation.INTERNAL_WORLD,currentProcess);
	}
	//
	public void externalWorld1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,CheckTermOperation.EXTERNAL_WORLD,currentProcess);
	}
	//
	public void even1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,CheckTermOperation.EVEN);
	}
	//
	public void odd1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,CheckTermOperation.ODD);
	}
	//
	public void lt2s(ChoisePoint iX, Term a1, Term a2) throws Backtracking {
		Arithmetic.compare_two_numbers(iX,a1,a2,ComparisonOperation.LT);
	}
	//
	public void gt2s(ChoisePoint iX, Term a1, Term a2) throws Backtracking {
		// System.out.printf("gt2s; a1=%s, a2=%s\n",a1,a2);
		Arithmetic.compare_two_numbers(iX,a1,a2,ComparisonOperation.GT);
	}
	//
	public void ne2s(ChoisePoint iX, Term a1, Term a2) throws Backtracking {
		Arithmetic.compare_two_numbers(iX,a1,a2,ComparisonOperation.NE);
	}
	//
	public void le2s(ChoisePoint iX, Term a1, Term a2) throws Backtracking {
		Arithmetic.compare_two_numbers(iX,a1,a2,ComparisonOperation.LE);
	}
	//
	public void ge2s(ChoisePoint iX, Term a1, Term a2) throws Backtracking {
		Arithmetic.compare_two_numbers(iX,a1,a2,ComparisonOperation.GE);
	}
	//
	public void add2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryArithmeticOperation.PLUS);
	}
	public void add2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void sub2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryArithmeticOperation.MINUS);
	}
	public void sub2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void sub1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.MINUS);
	}
	public void sub1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void inc1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.INC);
	}
	public void inc1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void dec1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.DEC);
	}
	public void dec1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void mult2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryArithmeticOperation.MULT);
	}
	public void mult2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void slash2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryArithmeticOperation.SLASH);
	}
	public void slash2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void div2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryArithmeticOperation.DIV);
	}
	public void div2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void mod2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryArithmeticOperation.MOD);
	}
	public void mod2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void random1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.RANDOM);
	}
	public void random1fs(ChoisePoint iX, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,new PrologVariable(),a2,UnaryArithmeticOperation.RANDOM);
	}
	//
	public void random0ff(ChoisePoint iX, PrologVariable a1) {
		Arithmetic.calculate_nullary_arithmetic_function(iX,a1,NullaryArithmeticOperation.RANDOM);
	}
	public void random0fs(ChoisePoint iX) {
		Arithmetic.calculate_nullary_arithmetic_function(iX,new PrologVariable(),NullaryArithmeticOperation.RANDOM);
	}
	//
	public void abs1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.ABS);
	}
	public void abs1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void round1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.ROUND);
	}
	public void round1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void trunc1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.TRUNC);
	}
	public void trunc1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void pi0ff(ChoisePoint iX, PrologVariable a1) {
		Arithmetic.calculate_nullary_arithmetic_function(iX,a1,NullaryArithmeticOperation.PI);
	}
	public void pi0fs(ChoisePoint iX) {
	}
	//
	public void sqrt1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.SQRT);
	}
	public void sqrt1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void ln1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.LN);
	}
	public void ln1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void log_10_1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.LOG);
	}
	public void log_10_1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void exp1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.EXP);
	}
	public void exp1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void sin1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.SIN);
	}
	public void sin1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void cos1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.COS);
	}
	public void cos1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void tan1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.TAN);
	}
	public void tan1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void arctan1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_arithmetic_function(iX,a1,a2,UnaryArithmeticOperation.ARCTAN);
	}
	public void arctan1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void concat3s(ChoisePoint iX, Term a1, Term a2, PrologVariable a3) {
		String s1;
		String s2;
		try {
			s1= a1.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		};
		try {
			s2= a2.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		};
		a3.value= new PrologString(s1.concat(s2));
		iX.pushTrail(a3);
	}
	public void concat3s(ChoisePoint iX, Term a1, PrologVariable a2, Term a3) throws Backtracking {
		String s1;
		String s3;
		try {
			s1= a1.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		};
		try {
			s3= a3.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a3);
		};
		if (!s3.startsWith(s1)) {
			throw new Backtracking();
		};
		a2.value= new PrologString(s3.substring(s1.length()));
		iX.pushTrail(a2);
	}
	public void concat3s(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) throws Backtracking {
		String s2;
		String s3;
		try {
			s2= a2.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		};
		try {
			s3= a3.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a3);
		};
		if (!s3.endsWith(s2)) {
			throw new Backtracking();
		};
		a1.value= new PrologString(s3.substring(0,s3.length()-s2.length()));
		iX.pushTrail(a1);
	}
	public void concat3s(ChoisePoint iX, Term a1, Term a2, Term a3) throws Backtracking {
		String s1;
		String s2;
		String s3;
		try {
			s1= a1.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		};
		try {
			s2= a2.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		};
		try {
			s3= a3.getStringValue(iX);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a3);
		};
		if (!s3.equals(s1.concat(s2))) {
			throw new Backtracking();
		}
	}
	//
	public void convertToInteger1ff(ChoisePoint iX, PrologVariable a1, Term a2) throws Backtracking {
		// a2= a2.dereferenceValue(iX);
		try {
			// a1.value= new PrologInteger(Converters.termToRoundInteger(a2,iX,true));
			a1.value= new PrologInteger(Converters.termToStrictInteger(a2,iX,true));
			// iX.pushTrail(a1);
		} catch (TermIsNotAnInteger e) {
			throw new Backtracking();
		}
	}
	public void convertToInteger1fs(ChoisePoint iX, Term a1) throws Backtracking {
		// a1= a1.dereferenceValue(iX);
		try {
			// Converters.termToRoundInteger(a1,iX,true);
			Converters.termToStrictInteger(a1,iX,true);
		} catch (TermIsNotAnInteger e) {
			throw new Backtracking();
		}
	}
	//
	public void convertToReal1ff(ChoisePoint iX, PrologVariable a1, Term a2) throws Backtracking {
		try {
			a1.value= new PrologReal(Converters.termToReal(a2,iX));
			// iX.pushTrail(a1);
		} catch (TermIsNotAReal e) {
			throw new Backtracking();
		}
	}
	public void convertToReal1fs(ChoisePoint iX, Term a1) throws Backtracking {
		try {
			Converters.termToReal(a1,iX);
		} catch (TermIsNotAReal e) {
			throw new Backtracking();
		}
	}
	//
	public void convertToNumerical1ff(ChoisePoint iX, PrologVariable a1, Term a2) throws Backtracking {
		a1.value= Converters.termToNumerical(a2,iX,true);
		// iX.pushTrail(a1);
	}
	public void convertToNumerical1fs(ChoisePoint iX, Term a1) throws Backtracking {
		Converters.termToNumerical(a1,iX,true);
	}
	//
	public void stringToTerm1ff(ChoisePoint iX, PrologVariable a1, Term a2) throws Backtracking {
		try {
			String text= a2.getStringValue(iX);
			Parser parser= new Parser();
			try {
				Term[] terms= parser.stringToTerms(text);
				if (terms.length==1) {
					if (a1 != null) {
						a1.value= terms[0];
					}
				} else {
					throw new Backtracking();
				}
			} catch (LexicalScannerError e) {
				throw new Backtracking();
			} catch (ParserError e) {
				throw new Backtracking();
			}
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		}
	}
	public void stringToTerm1fs(ChoisePoint iX, Term a1) throws Backtracking {
		stringToTerm1ff(iX,null,a1);
	}
	//
	public void stringToTerms1ff(ChoisePoint iX, PrologVariable a1, Term a2) throws Backtracking {
		try {
			String text= a2.getStringValue(iX);
			Parser parser= new Parser();
			try {
				Term[] terms= parser.stringToTerms(text);
				if (a1 != null) {
					a1.value= Converters.arrayToList(terms);
				}
			} catch (LexicalScannerError e) {
				throw new Backtracking();
			} catch (ParserError e) {
				throw new Backtracking();
			}
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		}
	}
	public void stringToTerms1fs(ChoisePoint iX, Term a1) throws Backtracking {
		stringToTerms1ff(iX,null,a1);
	}
	//
	public void stringsToText1ff(ChoisePoint iX, PrologVariable result, Term list) {
		String text= Converters.concatenateStringList(list,"",iX);
		result.value= new PrologString(text);
		iX.pushTrail(result);
	}
	public void stringsToText1fs(ChoisePoint iX, Term list) {
	}
	public void stringsToText2ff(ChoisePoint iX, PrologVariable result, Term list, Term separator) {
		try {
			String infix= separator.getStringValue(iX);
			String text= Converters.concatenateStringList(list,infix,iX);
			result.value= new PrologString(text);
			iX.pushTrail(result);
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(separator);
		}
	}
	public void stringsToText2fs(ChoisePoint iX, Term list, Term separator) {
	}
	//
	public void convertToString1mff(ChoisePoint iX, PrologVariable a1, Term... args) {
		StringBuilder textBuffer= FormatOutput.termsToString(iX,(Term[])args);
		a1.value= new PrologString(textBuffer.toString());
	}
	public void convertToString1mfs(ChoisePoint iX, Term... args) {
	}
	//
	public void codesToString1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		BigInteger[] codes= Converters.termToIntegers(a2,iX);
		a1.value= new PrologString(Converters.codesToString(codes));
	}
	public void codesToString1fs(ChoisePoint iX, PrologVariable a1, Term a2) {
	}
	//
	public void sortList1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Term[] array= Converters.listToArray(a2,iX);
		Arrays.sort(array,new TermComparator(true));
		a1.value= Converters.arrayToList(array);
	}
	public void sortList1fs(ChoisePoint iX, Term a2) {
	}
}
