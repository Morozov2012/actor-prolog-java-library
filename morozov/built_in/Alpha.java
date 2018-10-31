// (c) 2010-2015 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.syntax.errors.*;
import morozov.syntax.scanner.errors.*;
import morozov.syntax.*;
import morozov.system.*;
import morozov.system.converters.*;
import morozov.system.errors.*;
import morozov.system.files.*;
import morozov.system.gui.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Alpha extends AbstractInternalWorld {
	//
	public Boolean backslashAlwaysIsSeparator= null;
	public Boolean acceptOnlyUniformResourceIdentifiers= null;
	public WaitingInterval maximalWaitingTime= null;
	public CharacterSet characterSet= null;
	public AtomicReference<ExtendedSize> width= new AtomicReference<>();
	public AtomicReference<ExtendedSize> height= new AtomicReference<>();
	//
	protected static long defaultMaximalWaitingTime= 12; // [sec]
	protected static Term termDefaultMaximalWaitingTime= new PrologInteger(defaultMaximalWaitingTime);
	protected static Term termYes= new PrologSymbol(SymbolCodes.symbolCode_E_yes);
	protected static Term termNo= new PrologSymbol(SymbolCodes.symbolCode_E_no);
	protected static Term termNone= new PrologSymbol(SymbolCodes.symbolCode_E_none);
	protected static Term termDefault= new PrologSymbol(SymbolCodes.symbolCode_E_default);
	protected static Term termEmptyString= new PrologString("");
	protected static Term[] noArguments= new Term[0];
	//
	public Alpha() {
	}
	public Alpha(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	protected Term getBuiltInSlot_E_backslash_always_is_separator() {
		return termYes;
	}
	protected Term getBuiltInSlot_E_accept_only_uniform_resource_identifiers() {
		return termNo;
	}
	protected Term getBuiltInSlot_E_maximal_waiting_time() {
		return termDefaultMaximalWaitingTime;
	}
	protected Term getBuiltInSlot_E_character_set() {
		return termNone;
	}
	protected Term getBuiltInSlot_E_width() {
		return termDefault;
	}
	protected Term getBuiltInSlot_E_height() {
		return termDefault;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set backslashAlwaysIsSeparator
	//
	public void setBackslashAlwaysIsSeparator1s(ChoisePoint iX, Term a1) {
		setBackslashAlwaysIsSeparator(YesNo.termYesNo2Boolean(a1,iX));
	}
	public void setBackslashAlwaysIsSeparator(boolean value) {
		backslashAlwaysIsSeparator= value;
	}
	public void getBackslashAlwaysIsSeparator0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getBackslashAlwaysIsSeparator(iX);
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(value));
	}
	public void getBackslashAlwaysIsSeparator0fs(ChoisePoint iX) {
	}
	public boolean getBackslashAlwaysIsSeparator(ChoisePoint iX) {
		if (backslashAlwaysIsSeparator != null) {
			return backslashAlwaysIsSeparator;
		} else {
			Term value= getBuiltInSlot_E_backslash_always_is_separator();
			return YesNo.termYesNo2Boolean(value,iX);
		}
	}
	//
	// get/set acceptOnlyUniformResourceIdentifiers
	//
	public void setAcceptOnlyUniformResourceIdentifiers1s(ChoisePoint iX, Term a1) {
		setAcceptOnlyUniformResourceIdentifiers(YesNo.termYesNo2Boolean(a1,iX));
	}
	public void setAcceptOnlyUniformResourceIdentifiers(boolean value) {
		acceptOnlyUniformResourceIdentifiers= value;
	}
	public void getAcceptOnlyUniformResourceIdentifiers0ff(ChoisePoint iX, PrologVariable result) {
		boolean value= getAcceptOnlyUniformResourceIdentifiers(iX);
		result.setNonBacktrackableValue(YesNo.boolean2TermYesNo(value));
	}
	public void getAcceptOnlyUniformResourceIdentifiers0fs(ChoisePoint iX) {
	}
	public boolean getAcceptOnlyUniformResourceIdentifiers(ChoisePoint iX) {
		if (acceptOnlyUniformResourceIdentifiers != null) {
			return acceptOnlyUniformResourceIdentifiers;
		} else {
			Term value= getBuiltInSlot_E_accept_only_uniform_resource_identifiers();
			return YesNo.termYesNo2Boolean(value,iX);
		}
	}
	//
	// get/set maximalWaitingTime
	//
	public void setMaximalWaitingTime1s(ChoisePoint iX, Term a1) {
		setMaximalWaitingTime(WaitingInterval.argumentToWaitingInterval(a1,iX));
	}
	public void setMaximalWaitingTime(WaitingInterval value) {
		maximalWaitingTime= value;
	}
	public void getMaximalWaitingTime0ff(ChoisePoint iX, PrologVariable result) {
		WaitingInterval value= getMaximalWaitingTime(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getMaximalWaitingTime0fs(ChoisePoint iX) {
	}
	public WaitingInterval getMaximalWaitingTime(ChoisePoint iX) {
		if (maximalWaitingTime != null) {
			return maximalWaitingTime;
		} else {
			Term value= getBuiltInSlot_E_maximal_waiting_time();
			return WaitingInterval.argumentToWaitingInterval(value,iX);
		}
	}
	public int getMaximalWaitingTimeInMilliseconds(ChoisePoint iX) {
		WaitingInterval value= getMaximalWaitingTime(iX);
		return value.toMillisecondsIntegerOrDefault(DefaultOptions.waitingInterval,iX);
	}
	//
	// get/set characterSet
	//
	public void setCharacterSet1s(ChoisePoint iX, Term a1) {
		setCharacterSet(CharacterSet.term2CharacterSet(a1,iX));
	}
	public void setCharacterSet(CharacterSet value) {
		characterSet= value;
	}
	public void getCharacterSet0ff(ChoisePoint iX, PrologVariable result) {
		CharacterSet value= getCharacterSet(iX);
		result.setNonBacktrackableValue(value.toTerm());
	}
	public void getCharacterSet0fs(ChoisePoint iX) {
	}
	public CharacterSet getCharacterSet(ChoisePoint iX) {
		if (characterSet != null) {
			return characterSet;
		} else {
			Term value= getBuiltInSlot_E_character_set();
			return CharacterSet.term2CharacterSet(value,iX);
		}
	}
	//
	// get/set width
	//
	public void setWidth1s(ChoisePoint iX, Term a1) {
		setWidth(ExtendedSize.argumentToExtendedSize(a1,iX));
	}
	public void setWidth(ExtendedSize value) {
		width.set(value);
	}
	public void getWidth0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getWidth(iX).toTerm());
	}
	public void getWidth0fs(ChoisePoint iX) {
	}
	public ExtendedSize getWidth(ChoisePoint iX) {
		ExtendedSize size= width.get();
		if (size != null) {
			return size;
		} else {
			Term value= getBuiltInSlot_E_width();
			return ExtendedSize.argumentToExtendedSizeSafe(value,iX);
		}
	}
	//
	// get/set height
	//
	public void setHeight1s(ChoisePoint iX, Term a1) {
		setHeight(ExtendedSize.argumentToExtendedSize(a1,iX));
	}
	public void setHeight(ExtendedSize value) {
		height.set(value);
	}
	public void getHeight0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getHeight(iX).toTerm());
	}
	public void getHeight0fs(ChoisePoint iX) {
	}
	public ExtendedSize getHeight(ChoisePoint iX) {
		ExtendedSize size= height.get();
		if (size != null) {
			return size;
		} else {
			Term value= getBuiltInSlot_E_height();
			return ExtendedSize.argumentToExtendedSizeSafe(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set Default Size
	//
	public void setDefaultSize2s(ChoisePoint iX, Term a1, Term a2) {
		setWidth(ExtendedSize.argumentToExtendedSize(a1,iX));
		setHeight(ExtendedSize.argumentToExtendedSize(a2,iX));
	}
	public void getDefaultSize2s(ChoisePoint iX, PrologVariable a1, PrologVariable a2) {
		a1.setBacktrackableValue(getWidth(iX).toTerm(),iX);
		a2.setBacktrackableValue(getHeight(iX).toTerm(),iX);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void goal0s(ChoisePoint iX) throws Backtracking {
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
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.FREE);
	}
	//
	public void bound1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.BOUND);
	}
	//
	public void symbol1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.SYMBOL);
	}
	//
	public void string1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.STRING);
	}
	//
	public void binary1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.BINARY);
	}
	//
	public void integer1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.INTEGER);
	}
	//
	public void real1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.REAL);
	}
	//
	public void numerical1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.NUMERICAL);
	}
	//
	public void classInstance1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.CLASS_INSTANCE);
	}
	//
	public void internalWorld1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.INTERNAL_WORLD,currentProcess);
	}
	//
	public void externalWorld1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.EXTERNAL_WORLD,currentProcess);
	}
	//
	public void remoteWorld1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.REMOTE_WORLD,currentProcess);
	}
	//
	public void even1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.EVEN);
	}
	//
	public void odd1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.ODD);
	}
	//
	public void nan1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.NAN);
	}
	public void infinite1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.INFINITE);
	}
	public void finite1ms(ChoisePoint iX, Term... args) throws Backtracking {
		Term[] array= GeneralConverters.termsToArray(iX,(Term[])args);
		Arithmetic.check_all_arguments(iX,array,TermCheckOperation.FINITE);
	}
	//
	public void notANumber0ff(ChoisePoint iX, PrologVariable result) {
		Arithmetic.calculate_nullary_arithmetic_function(iX,result,NullaryArithmeticOperation.NOT_A_NUMBER);
	}
	public void notANumber0fs(ChoisePoint iX) {
	}
	//
	public void positiveInfinity0ff(ChoisePoint iX, PrologVariable result) {
		Arithmetic.calculate_nullary_arithmetic_function(iX,result,NullaryArithmeticOperation.POSITIVE_INFINITY);
	}
	public void positiveInfinity0fs(ChoisePoint iX) {
	}
	//
	public void negativeInfinity0ff(ChoisePoint iX, PrologVariable result) {
		Arithmetic.calculate_nullary_arithmetic_function(iX,result,NullaryArithmeticOperation.NEGATIVE_INFINITY);
	}
	public void negativeInfinity0fs(ChoisePoint iX) {
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
	public void add2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_arithmetic_function(iX,result,a1,a2,BinaryOperation.PLUS);
	}
	public void add2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void sub2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_arithmetic_function(iX,result,a1,a2,BinaryOperation.MINUS);
	}
	public void sub2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void sub1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.MINUS);
	}
	public void sub1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void inc1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.INC);
	}
	public void inc1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void dec1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.DEC);
	}
	public void dec1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void mult2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_arithmetic_function(iX,result,a1,a2,BinaryOperation.MULT);
	}
	public void mult2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void slash2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_arithmetic_function(iX,result,a1,a2,BinaryOperation.SLASH);
	}
	public void slash2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void div2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_arithmetic_function(iX,result,a1,a2,BinaryOperation.DIV);
	}
	public void div2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void mod2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_arithmetic_function(iX,result,a1,a2,BinaryOperation.MOD);
	}
	public void mod2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void random1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.RANDOM);
	}
	public void random1fs(ChoisePoint iX, Term a1) {
		Arithmetic.calculate_unary_function(iX,new PrologVariable(),a1,UnaryOperation.RANDOM);
	}
	//
	public void random0ff(ChoisePoint iX, PrologVariable result) {
		Arithmetic.calculate_nullary_arithmetic_function(iX,result,NullaryArithmeticOperation.RANDOM);
	}
	public void random0fs(ChoisePoint iX) {
		Arithmetic.calculate_nullary_arithmetic_function(iX,new PrologVariable(),NullaryArithmeticOperation.RANDOM);
	}
	//
	public void abs1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.ABS);
	}
	public void abs1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void round1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.ROUND);
	}
	public void round1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void trunc1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.TRUNC);
	}
	public void trunc1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void pi0ff(ChoisePoint iX, PrologVariable result) {
		Arithmetic.calculate_nullary_arithmetic_function(iX,result,NullaryArithmeticOperation.PI);
	}
	public void pi0fs(ChoisePoint iX) {
	}
	//
	public void sqrt1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.SQRT);
	}
	public void sqrt1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void power2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_arithmetic_function(iX,result,a1,a2,BinaryOperation.POWER);
	}
	public void power2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void hypot2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_arithmetic_function(iX,result,a1,a2,BinaryOperation.HYPOT);
	}
	public void hypot2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void ln1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.LN);
	}
	public void ln1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void log_10_1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.LOG10);
	}
	public void log_10_1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void exp1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.EXP);
	}
	public void exp1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void sin1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.SIN);
	}
	public void sin1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void cos1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.COS);
	}
	public void cos1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void tan1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.TAN);
	}
	public void tan1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void arcsin1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.ARCSIN);
	}
	public void arcsin1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void arccos1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.ARCCOS);
	}
	public void arccos1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void arctan1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.ARCTAN);
	}
	public void arctan1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void signum1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.SIGNUM);
	}
	public void signum1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void max1mff(ChoisePoint iX, PrologVariable result, Term... args) {
		Term value= Arithmetic.calculate_multi_argument_function(iX,MultiArgumentArithmeticOperation.MAX,(Term[])args);
		result.setNonBacktrackableValue(value);
	}
	public void max1mfs(ChoisePoint iX, Term... args) {
	}
	//
	public void min1mff(ChoisePoint iX, PrologVariable result, Term... args) {
		Term value= Arithmetic.calculate_multi_argument_function(iX,MultiArgumentArithmeticOperation.MIN,(Term[])args);
		result.setNonBacktrackableValue(value);
	}
	public void min1mfs(ChoisePoint iX, Term... args) {
	}
	//
	public void bitnot1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Arithmetic.calculate_unary_function(iX,result,a1,UnaryOperation.BITNOT);
	}
	public void bitnot1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void bitand2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_bitwise_function(iX,result,a1,a2,BinaryOperation.BITAND);
	}
	public void bitand2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void bitor2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_bitwise_function(iX,result,a1,a2,BinaryOperation.BITOR);
	}
	public void bitor2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void bitxor2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_bitwise_function(iX,result,a1,a2,BinaryOperation.BITXOR);
	}
	public void bitxor2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void bitright2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_bitwise_function(iX,result,a1,a2,BinaryOperation.BITRIGHT);
	}
	public void bitright2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void bitleft2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Arithmetic.calculate_binary_bitwise_function(iX,result,a1,a2,BinaryOperation.BITLEFT);
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
		a3.setBacktrackableValue(new PrologString(s1.concat(s2)),iX);
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
		a2.setBacktrackableValue(new PrologString(s3.substring(s1.length())),iX);
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
		a1.setBacktrackableValue(new PrologString(s3.substring(0,s3.length()-s2.length())),iX);
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
	public void convertToInteger1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		try {
			result.setNonBacktrackableValue(new PrologInteger(GeneralConverters.termToStrictInteger(a1,iX,true)));
		} catch (TermIsNotAnInteger e) {
			throw Backtracking.instance;
		}
	}
	public void convertToInteger1fs(ChoisePoint iX, Term a1) throws Backtracking {
		try {
			// GeneralConverters.termToRoundInteger(a1,iX,true);
			GeneralConverters.termToStrictInteger(a1,iX,true);
		} catch (TermIsNotAnInteger e) {
			throw Backtracking.instance;
		}
	}
	//
	public void convertToReal1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		try {
			result.setNonBacktrackableValue(new PrologReal(GeneralConverters.termToReal(a1,iX)));
		} catch (TermIsNotAReal e) {
			throw Backtracking.instance;
		}
	}
	public void convertToReal1fs(ChoisePoint iX, Term a1) throws Backtracking {
		try {
			GeneralConverters.termToReal(a1,iX);
		} catch (TermIsNotAReal e) {
			throw Backtracking.instance;
		}
	}
	//
	public void convertToNumerical1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		result.setNonBacktrackableValue(GeneralConverters.termToNumerical(a1,iX,true));
	}
	public void convertToNumerical1fs(ChoisePoint iX, Term a1) throws Backtracking {
		GeneralConverters.termToNumerical(a1,iX,true);
	}
	//
	public void stringToTerm1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		try {
			String text= a1.getStringValue(iX);
			Parser parser= new Parser();
			try {
				Term[] terms= parser.stringToTerms(text);
				if (terms.length==1) {
					if (a1 != null) {
						result.setNonBacktrackableValue(terms[0]);
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
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	public void stringToTerm1fs(ChoisePoint iX, Term a1) throws Backtracking {
		stringToTerm1ff(iX,null,a1);
	}
	//
	public void stringToTerms1ff(ChoisePoint iX, PrologVariable result, Term a1) throws Backtracking {
		try {
			String text= a1.getStringValue(iX);
			Parser parser= new Parser();
			try {
				Term[] terms= parser.stringToTerms(text);
				if (a1 != null) {
					result.setNonBacktrackableValue(GeneralConverters.arrayToList(terms));
				}
			} catch (LexicalScannerError e) {
				throw Backtracking.instance;
			} catch (ParserError e) {
				throw Backtracking.instance;
			}
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a1);
		}
	}
	public void stringToTerms1fs(ChoisePoint iX, Term a1) throws Backtracking {
		stringToTerms1ff(iX,null,a1);
	}
	//
	public void stringsToText1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		String text= GeneralConverters.concatenateStringList(a1,"",iX);
		result.setNonBacktrackableValue(new PrologString(text));
	}
	public void stringsToText1fs(ChoisePoint iX, Term a1) {
	}
	public void stringsToText2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		try {
			String infix= a2.getStringValue(iX);
			String text= GeneralConverters.concatenateStringList(a1,infix,iX);
			result.setNonBacktrackableValue(new PrologString(text));
		} catch (TermIsNotAString e) {
			throw new WrongArgumentIsNotAString(a2);
		}
	}
	public void stringsToText2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public void convertToString1mff(ChoisePoint iX, PrologVariable result, Term... args) {
		StringBuilder textBuffer= FormatOutput.termsToString(iX,(Term[])args);
		result.setNonBacktrackableValue(new PrologString(textBuffer.toString()));
	}
	public void convertToString1mfs(ChoisePoint iX, Term... args) {
	}
	//
	public void codesToString1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		try {
			byte[] byteArray= a1.getBinaryValue(iX);
			result.setNonBacktrackableValue(new PrologString(GeneralConverters.bytesToString(byteArray)));
		} catch (TermIsNotABinary e) {
			BigInteger[] codes= GeneralConverters.argumentToIntegers(a1,iX);
			result.setNonBacktrackableValue(new PrologString(GeneralConverters.codesToString(codes)));
		}
	}
	public void codesToString1fs(ChoisePoint iX, Term a1) {
	}
	//
	public void sortList1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		Term[] array= GeneralConverters.listToArray(a1,iX);
		Arrays.sort(array,new TermComparator(true));
		result.setNonBacktrackableValue(GeneralConverters.arrayToList(array));
	}
	public void sortList1fs(ChoisePoint iX, Term a1) {
	}
	//
	protected void callInternalProcedure(long domainSignature, boolean dialogIsModal, ChoisePoint modalChoisePoint) {
		if (dialogIsModal) {
			ChoisePoint newIx= new ChoisePoint(modalChoisePoint);
			Continuation c1= new DomainSwitch(new SuccessTermination(),domainSignature,this,this,noArguments);
			try {
				c1.execute(newIx);
			} catch (Backtracking b) {
			};
			newIx.freeTrail();
		} else {
			AsyncCall call= new AsyncCall(domainSignature,this,true,false/*true*/,noArguments,true);
			transmitAsyncCall(call,modalChoisePoint);
		}
	}
}
