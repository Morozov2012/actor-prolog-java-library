// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.system.*;
import morozov.classes.*;
import morozov.terms.*;
import morozov.run.*;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public abstract class Timer extends Alpha {
	//
	private java.util.Timer scheduler;
	private TimerTask currentTask;
	private boolean isRepeatedAction= false;
	private long currentPeriod= 1000;
	private long currentFirstDelay= 1000;
	// private static final BigDecimal oneMillionBig= BigDecimal.valueOf(1000000);
	// private static final BigInteger constant_MaxLong= BigInteger.valueOf(Long.MAX_VALUE);
	// private static final BigInteger constant_MinLong= BigInteger.valueOf(Long.MIN_VALUE);
	//
	public abstract long entry_s_Tick_0();
	//
	public void activate0s(ChoisePoint iX) {
		if (currentTask==null) {
			if (scheduler==null) {
				scheduler= new java.util.Timer(false);
			};
			currentTask= new LocalTask(currentProcess,this);
			isRepeatedAction= true;
			long correctedPeriod= currentPeriod;
			long correctedFirstDelay= currentFirstDelay;
			if (correctedPeriod <= 1) {
				correctedPeriod= 1;
			};
			if (correctedFirstDelay < 0) {
				correctedFirstDelay= 0;
			};
			scheduler.scheduleAtFixedRate(currentTask,correctedFirstDelay,correctedPeriod);
		}
	}
	//
	public void delay1s(ChoisePoint iX, Term n1) {
		long delay= Converters.termSecondsToMilliseconds(n1,iX);
		if (scheduler==null) {
			scheduler= new java.util.Timer(false);
		};
		if (currentTask==null) {
			isRepeatedAction= false;
			scheduler.schedule(new LocalTask(currentProcess,this),delay);
		} else {
			currentTask.cancel();
			currentProcess.cancelTimerMessage(this);
			currentTask= new LocalTask(currentProcess,this);
			if (isRepeatedAction) {
				long correctedPeriod= currentPeriod;
				if (correctedPeriod <= 1) {
					correctedPeriod= 1;
				};
				scheduler.scheduleAtFixedRate(currentTask,delay,correctedPeriod);
			} else {
				scheduler.schedule(currentTask,delay);
			}
		}
	}
	//
	public void setPeriod1s(ChoisePoint iX, Term n1) {
		long period= Converters.termSecondsToMilliseconds(n1,iX);
		setPeriod(period,period);
	}
	public void setPeriod2s(ChoisePoint iX, Term n1, Term n2) {
		long period= Converters.termSecondsToMilliseconds(n1,iX);
		long firstDelay= Converters.termSecondsToMilliseconds(n2,iX);
		setPeriod(period,firstDelay);
	}
	protected void setPeriod(long period, long firstDelay) {
		if (currentPeriod != period || currentFirstDelay != firstDelay) {
			currentPeriod= period;
			currentFirstDelay= firstDelay;
			if (scheduler != null) {
				if (currentTask != null) {
					currentTask.cancel();
					currentTask= new LocalTask(currentProcess,this);
					long correctedPeriod= currentPeriod;
					// long correctedFirstDelay= currentFirstDelay;
					if (correctedPeriod <= 1) {
						correctedPeriod= 1;
					};
					// if (correctedFirstDelay < 0) {
					//	correctedFirstDelay= 0;
					// };
					if (isRepeatedAction) {
						scheduler.scheduleAtFixedRate(currentTask,correctedPeriod,correctedPeriod);
					} else {
						scheduler.schedule(currentTask,correctedPeriod);
					}
				}
			}
		}
	}
	//
	public void suspend0s(ChoisePoint iX) {
		if (scheduler != null) {
			if (currentTask != null) {
				currentTask.cancel();
				currentProcess.cancelTimerMessage(this);
				currentTask= null;
			}
		}
	}
	//
	public void tick0s(ChoisePoint iX) {
        }
	//
	public class Tick0s extends Continuation {
		// private Continuation c0;
		//
		public Tick0s(Continuation aC) {
			c0= aC;
		}
		//
		public void execute(ChoisePoint iX) throws Backtracking {
			c0.execute(iX);
		}
	}
	//
	public static void time3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) {
		// Calendar calendar= new GregorianCalendar();
		Calendar calendar= Calendar.getInstance();
		int hours= calendar.get(Calendar.HOUR);
		int minutes= calendar.get(Calendar.MINUTE);
		int seconds= calendar.get(Calendar.SECOND);
		a1.value= new PrologInteger(hours);
		a2.value= new PrologInteger(minutes);
		a3.value= new PrologInteger(seconds);
		iX.pushTrail(a1);
		iX.pushTrail(a2);
		iX.pushTrail(a3);
	}
	public static void time4s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4) {
		// Calendar calendar= new GregorianCalendar();
		Calendar calendar= Calendar.getInstance();
		int hours= calendar.get(Calendar.HOUR);
		int minutes= calendar.get(Calendar.MINUTE);
		int seconds= calendar.get(Calendar.SECOND);
		int milliseconds= calendar.get(Calendar.MILLISECOND);
		a1.value= new PrologInteger(hours);
		a2.value= new PrologInteger(minutes);
		a3.value= new PrologInteger(seconds);
		a4.value= new PrologInteger(milliseconds);
		iX.pushTrail(a1);
		iX.pushTrail(a2);
		iX.pushTrail(a3);
		iX.pushTrail(a4);
	}
	public static void time0ff(ChoisePoint iX, PrologVariable argument) {
		// Calendar calendar= new GregorianCalendar();
		Calendar calendar= Calendar.getInstance();
		int hours= calendar.get(Calendar.HOUR);
		int minutes= calendar.get(Calendar.MINUTE);
		int seconds= calendar.get(Calendar.SECOND);
		long milliseconds= calendar.get(Calendar.MILLISECOND);
		argument.value=
			new PrologStructure(
				SymbolCodes.symbolCode_E_time,
				new Term[]{
					new PrologInteger(hours),
					new PrologInteger(minutes),
					new PrologInteger(seconds),
					new PrologInteger(milliseconds)}
				);
		// iX.pushTrail(argument);
	}
	public static void time0fs(ChoisePoint iX) {
	}
	//
	public static void date4s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4) {
		// Calendar calendar= new GregorianCalendar();
		Calendar calendar= Calendar.getInstance();
		int year= calendar.get(Calendar.YEAR);
		int month= calendar.get(Calendar.MONTH) + 1;
		int dayOfMonth= calendar.get(Calendar.DAY_OF_MONTH);
		int dayOfWeek= calendar.get(Calendar.DAY_OF_WEEK);
		dayOfWeek--;
		if (dayOfWeek==0) {
			dayOfWeek= 7;
		};
		a1.value= new PrologInteger(year);
		a2.value= new PrologInteger(month);
		a3.value= new PrologInteger(dayOfMonth);
		a4.value= new PrologInteger(dayOfWeek);
		iX.pushTrail(a1);
		iX.pushTrail(a2);
		iX.pushTrail(a3);
		iX.pushTrail(a4);
	}
	public static void date3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) {
		// Calendar calendar= new GregorianCalendar();
		Calendar calendar= Calendar.getInstance();
		int year= calendar.get(Calendar.YEAR);
		int month= calendar.get(Calendar.MONTH) + 1;
		int day= calendar.get(Calendar.DAY_OF_MONTH);
		a1.value= new PrologInteger(year);
		a2.value= new PrologInteger(month);
		a3.value= new PrologInteger(day);
		iX.pushTrail(a1);
		iX.pushTrail(a2);
		iX.pushTrail(a3);
	}
	public static void date0ff(ChoisePoint iX, PrologVariable argument) {
		// Calendar calendar= new GregorianCalendar();
		Calendar calendar= Calendar.getInstance();
		int year= calendar.get(Calendar.YEAR);
		int month= calendar.get(Calendar.MONTH) + 1;
		int day= calendar.get(Calendar.DAY_OF_MONTH);
		argument.value=
			new PrologStructure(
				SymbolCodes.symbolCode_E_date,
				new Term[]{
					new PrologInteger(year),
					new PrologInteger(month),
					new PrologInteger(day)}
				);
		// iX.pushTrail(argument);
	}
	public static void date0fs(ChoisePoint iX) {
	}
	//
	public void sleep1s(ChoisePoint iX, Term n1) {
		try {
			BigDecimal nanos= Converters.termToTimeInterval(n1,iX);
			BigDecimal milliseconds= nanos.divideToIntegralValue(Converters.oneMillionBig,MathContext.DECIMAL128);
			BigDecimal remainder= nanos.subtract(milliseconds.multiply(Converters.oneMillionBig));
			int delayInMilliseconds= PrologInteger.toInteger(milliseconds);
			int delayInNanos= PrologInteger.toInteger(remainder);
			if (delayInMilliseconds >= 0) {
				if (delayInNanos > 0) {
					if (delayInNanos <= 999999) {
						currentProcess.thread.sleep(delayInMilliseconds,delayInNanos);
					} else {
						delayInMilliseconds++;
						currentProcess.thread.sleep(delayInMilliseconds);
					}
				} else {
					currentProcess.thread.sleep(delayInMilliseconds);
				}
			}
		} catch (TermIsNotTimeInterval e1) {
			throw new WrongArgumentIsNotTimeInterval(n1);
		} catch (InterruptedException e2) {
		}
	}
}

class LocalTask extends TimerTask {
	private ActiveWorld currentProcess;
	private AbstractWorld targetWorld;
	//
	public LocalTask(ActiveWorld process, AbstractWorld target) {
		currentProcess= process;
		targetWorld= target;
	}
	//
	public void run() {
		currentProcess.receiveTimerMessage(targetWorld);
	}
}
