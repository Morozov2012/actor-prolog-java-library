// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.classes.*;
import morozov.syntax.errors.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.*;
import morozov.system.errors.*;
import morozov.system.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

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
		throw Backtracking.instance;
	}
	//
	public class Alarm1s extends Continuation {
		public Alarm1s(Continuation aC, Term exceptionName) {
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			throw Backtracking.instance;
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
						throw Backtracking.instance;
					}
				};
				return;
			}
		}
	}
	//
	public void free1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.FREE);
	}
	//
	public void bound1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.BOUND);
	}
	//
	public void symbol1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.SYMBOL);
	}
	//
	public void string1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.STRING);
	}
	//
	public void integer1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.INTEGER);
	}
	//
	public void real1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.REAL);
	}
	//
	public void numerical1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.NUMERICAL);
	}
	//
	public void classInstance1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.CLASS_INSTANCE);
	}
	//
	public void internalWorld1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.INTERNAL_WORLD,currentProcess);
	}
	//
	public void externalWorld1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.EXTERNAL_WORLD,currentProcess);
	}
	//
	public void even1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.EVEN);
	}
	//
	public void odd1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= Converters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.ODD);
	}
	//
	public void lt2s(ChoisePoint iX, Term a1, Term a2) throws Backtracking {
		Arithmetic.compare_two_numbers(iX,a1,a2,ComparisonOperation.LT);
	}
	//
	public void gt2s(ChoisePoint iX, Term a1, Term a2) throws Backtracking {
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
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryOperation.PLUS);
	}
	public void add2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void sub2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryOperation.MINUS);
	}
	public void sub2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void sub1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.MINUS);
	}
	public void sub1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void inc1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.INC);
	}
	public void inc1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void dec1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.DEC);
	}
	public void dec1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void mult2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryOperation.MULT);
	}
	public void mult2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void slash2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryOperation.SLASH);
	}
	public void slash2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void div2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryOperation.DIV);
	}
	public void div2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void mod2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryOperation.MOD);
	}
	public void mod2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void random1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.RANDOM);
	}
	public void random1fs(ChoisePoint iX, Term a2) {
		Arithmetic.calculate_unary_function(iX,new PrologVariable(),a2,UnaryOperation.RANDOM);
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
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.ABS);
	}
	public void abs1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void round1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.ROUND);
	}
	public void round1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void trunc1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.TRUNC);
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
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.SQRT);
	}
	public void sqrt1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void power2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_arithmetic_function(iX,a1,a2,a3,BinaryOperation.POWER);
	}
	public void power2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void ln1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.LN);
	}
	public void ln1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void log_10_1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.LOG10);
	}
	public void log_10_1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void exp1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.EXP);
	}
	public void exp1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void sin1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.SIN);
	}
	public void sin1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void cos1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.COS);
	}
	public void cos1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void tan1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.TAN);
	}
	public void tan1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void arctan1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.ARCTAN);
	}
	public void arctan1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void signum1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.SIGNUM);
	}
	public void signum1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void max1mff(ChoisePoint iX, PrologVariable a1, Term... args) {
		Term result= Arithmetic.calculate_multi_argument_function(iX,MultiArgumentArithmeticOperation.MAX,(Term[])args);
		a1.value= result;
	}
	public void max1mfs(ChoisePoint iX, Term... args) {
	}
	//
	public void min1mff(ChoisePoint iX, PrologVariable a1, Term... args) {
		Term result= Arithmetic.calculate_multi_argument_function(iX,MultiArgumentArithmeticOperation.MIN,(Term[])args);
		a1.value= result;
	}
	public void min1mfs(ChoisePoint iX, Term... args) {
	}
	//
	public void bitnot1ff(ChoisePoint iX, PrologVariable a1, Term a2) {
		Arithmetic.calculate_unary_function(iX,a1,a2,UnaryOperation.BITNOT);
	}
	public void bitnot1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void bitand2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_bitwise_function(iX,a1,a2,a3,BinaryOperation.BITAND);
	}
	public void bitand2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void bitor2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_bitwise_function(iX,a1,a2,a3,BinaryOperation.BITOR);
	}
	public void bitor2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void bitxor2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_bitwise_function(iX,a1,a2,a3,BinaryOperation.BITXOR);
	}
	public void bitxor2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void bitright2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_bitwise_function(iX,a1,a2,a3,BinaryOperation.BITRIGHT);
	}
	public void bitright2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void bitleft2ff(ChoisePoint iX, PrologVariable a1, Term a2, Term a3) {
		Arithmetic.calculate_binary_bitwise_function(iX,a1,a2,a3,BinaryOperation.BITLEFT);
	}
	public void bitleft2fs(ChoisePoint iX, Term a1, Term a2) {
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
			throw Backtracking.instance;
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
			throw Backtracking.instance;
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
			throw Backtracking.instance;
		}
	}
	//
	public void convertToInteger1ff(ChoisePoint iX, PrologVariable a1, Term a2) throws Backtracking {
		// a2= a2.dereferenceValue(iX);
		try {
			a1.value= new PrologInteger(Converters.termToStrictInteger(a2,iX,true));
			// iX.pushTrail(a1);
		} catch (TermIsNotAnInteger e) {
			throw Backtracking.instance;
		}
	}
	public void convertToInteger1fs(ChoisePoint iX, Term a1) throws Backtracking {
		// a1= a1.dereferenceValue(iX);
		try {
			// Converters.termToRoundInteger(a1,iX,true);
			Converters.termToStrictInteger(a1,iX,true);
		} catch (TermIsNotAnInteger e) {
			throw Backtracking.instance;
		}
	}
	//
	public void convertToReal1ff(ChoisePoint iX, PrologVariable a1, Term a2) throws Backtracking {
		try {
			a1.value= new PrologReal(Converters.termToReal(a2,iX));
			// iX.pushTrail(a1);
		} catch (TermIsNotAReal e) {
			throw Backtracking.instance;
		}
	}
	public void convertToReal1fs(ChoisePoint iX, Term a1) throws Backtracking {
		try {
			Converters.termToReal(a1,iX);
		} catch (TermIsNotAReal e) {
			throw Backtracking.instance;
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
					throw Backtracking.instance;
				}
			} catch (LexicalScannerError e) {
				throw Backtracking.instance;
			} catch (ParserError e) {
				throw Backtracking.instance;
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
				throw Backtracking.instance;
			} catch (ParserError e) {
				throw Backtracking.instance;
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
		// iX.pushTrail(result);
	}
	public void stringsToText1fs(ChoisePoint iX, Term list) {
	}
	public void stringsToText2ff(ChoisePoint iX, PrologVariable result, Term list, Term separator) {
		try {
			String infix= separator.getStringValue(iX);
			String text= Converters.concatenateStringList(list,infix,iX);
			result.value= new PrologString(text);
			// iX.pushTrail(result);
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
	//
	protected void callInternalProcedure(long domainSignature, boolean dialogIsModal, ChoisePoint modalChoisePoint) {
		Term[] arguments= new Term[0];
		if (dialogIsModal) {
			ChoisePoint newIx= new ChoisePoint(modalChoisePoint);
			Continuation c1= new DomainSwitch(new SuccessTermination(),domainSignature,this,this,arguments);
			try {
				c1.execute(newIx);
			} catch (Backtracking b) {
			};
			newIx.freeTrail();
		} else {
			AsyncCall call= new AsyncCall(domainSignature,this,true,false/*true*/,arguments,true);
			receiveAsyncCall(call);
		}
	}
}
