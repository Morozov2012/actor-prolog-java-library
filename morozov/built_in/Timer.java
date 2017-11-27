// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.built_in;

import target.*;

import morozov.run.*;
import morozov.system.*;
import morozov.system.errors.*;
import morozov.system.signals.*;
import morozov.terms.*;
import morozov.worlds.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;

public abstract class Timer extends Alpha {
	//
	public TimeInterval period= null;
	public TimeInterval initialDelay= null;
	//
	private java.util.Timer scheduler;
	private LocalTimerTask currentTask;
	private boolean isRepeatedAction= false;
	private long currentPeriod= 1000;
	private long currentFirstDelay= 1000;
	//
	///////////////////////////////////////////////////////////////
	//
	abstract protected Term getBuiltInSlot_E_period();
	abstract protected Term getBuiltInSlot_E_initial_delay();
	//
	abstract public long entry_s_Tick_0();
	//
	///////////////////////////////////////////////////////////////
	//
	public Timer() {
	}
	public Timer(GlobalWorldIdentifier id) {
		super(id);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// get/set period
	//
	public void setPeriod1s(ChoisePoint iX, Term a1) {
		setPeriod(TimeInterval.argumentSecondsToTimeInterval(a1,iX));
		changePeriod(iX);
	}
	public void setPeriod2s(ChoisePoint iX, Term a1, Term a2) {
		TimeInterval p= TimeInterval.argumentSecondsToTimeInterval(a1,iX);
		TimeInterval d= TimeInterval.argumentSecondsToTimeInterval(a2,iX);
		setPeriod(p);
		setInitialDelay(d);
		changePeriod(iX);
	}
	public void setPeriod(TimeInterval value) {
		period= value;
	}
	public void getPeriod0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getPeriod(iX).toTerm());
		// iX.pushTrail(a1);
	}
	public void getPeriod0fs(ChoisePoint iX) {
	}
	public TimeInterval getPeriod(ChoisePoint iX) {
		if (period != null) {
			return period;
		} else {
			Term value= getBuiltInSlot_E_period();
			return TimeInterval.argumentSecondsToTimeInterval(value,iX);
		}
	}
	//
	// get/set initialDelay
	//
	public void setInitialDelay1s(ChoisePoint iX, Term a1) {
		setInitialDelay(TimeInterval.argumentSecondsToTimeInterval(a1,iX));
		changePeriod(iX);
	}
	public void setInitialDelay(TimeInterval value) {
		initialDelay= value;
	}
	public void getInitialDelay0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(getInitialDelay(iX).toTerm());
		// iX.pushTrail(a1);
	}
	public void getInitialDelay0fs(ChoisePoint iX) {
	}
	public TimeInterval getInitialDelay(ChoisePoint iX) {
		if (initialDelay != null) {
			return initialDelay;
		} else {
			Term value= getBuiltInSlot_E_initial_delay();
			return TimeInterval.argumentSecondsToTimeInterval(value,iX);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected void changePeriod(ChoisePoint iX) {
		long p= getPeriod(iX).toMillisecondsLong();
		long d= getInitialDelay(iX).toMillisecondsLong();
		if (currentPeriod != p || currentFirstDelay != d) {
			currentPeriod= p;
			currentFirstDelay= d;
			if (scheduler != null) {
				if (currentTask != null) {
					currentTask.cancel();
					scheduler.purge();
					currentTask= new LocalTimerTask(currentProcess,this);
					long correctedPeriod= currentPeriod;
					if (correctedPeriod <= 1) {
						correctedPeriod= 1;
					};
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
	///////////////////////////////////////////////////////////////
	//
	public void closeFiles() {
		closeTimer();
		super.closeFiles();
	}
	protected void closeTimer() {
		if (scheduler != null) {
			if (currentTask != null) {
				currentTask.cancel();
				scheduler.purge();
				currentProcess.cancelTimerMessage(this);
				currentTask= null;
			}
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void activate0s(ChoisePoint iX) {
		if (currentTask==null) {
			if (scheduler==null) {
				scheduler= new java.util.Timer(false);
			};
			currentTask= new LocalTimerTask(currentProcess,this);
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
		long delay= TimeInterval.argumentSecondsToTimeInterval(n1,iX).toMillisecondsLong();
		if (scheduler==null) {
			scheduler= new java.util.Timer(false);
		};
		if (currentTask==null) {
			isRepeatedAction= false;
			scheduler.schedule(new LocalTimerTask(currentProcess,this),delay);
		} else {
			currentTask.cancel();
			scheduler.purge();
			currentProcess.cancelTimerMessage(this);
			currentTask= new LocalTimerTask(currentProcess,this);
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
	public void suspend0s(ChoisePoint iX) {
		closeTimer();
	}
	//
	public void isActive0s(ChoisePoint iX) throws Backtracking {
		if (scheduler==null || currentTask==null) {
			throw Backtracking.instance;
		}
	}
	//
	///////////////////////////////////////////////////////////////
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
	///////////////////////////////////////////////////////////////
	//
	public static void time3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) {
		// Calendar calendar= new GregorianCalendar();
		Calendar calendar= Calendar.getInstance();
		int hours= calendar.get(Calendar.HOUR);
		int minutes= calendar.get(Calendar.MINUTE);
		int seconds= calendar.get(Calendar.SECOND);
		a1.setBacktrackableValue(new PrologInteger(hours),iX);
		a2.setBacktrackableValue(new PrologInteger(minutes),iX);
		a3.setBacktrackableValue(new PrologInteger(seconds),iX);
		//iX.pushTrail(a1);
		//iX.pushTrail(a2);
		//iX.pushTrail(a3);
	}
	public static void time4s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4) {
		// Calendar calendar= new GregorianCalendar();
		Calendar calendar= Calendar.getInstance();
		int hours= calendar.get(Calendar.HOUR);
		int minutes= calendar.get(Calendar.MINUTE);
		int seconds= calendar.get(Calendar.SECOND);
		int milliseconds= calendar.get(Calendar.MILLISECOND);
		a1.setBacktrackableValue(new PrologInteger(hours),iX);
		a2.setBacktrackableValue(new PrologInteger(minutes),iX);
		a3.setBacktrackableValue(new PrologInteger(seconds),iX);
		a4.setBacktrackableValue(new PrologInteger(milliseconds),iX);
		//iX.pushTrail(a1);
		//iX.pushTrail(a2);
		//iX.pushTrail(a3);
		//iX.pushTrail(a4);
	}
	public static void time0ff(ChoisePoint iX, PrologVariable result) {
		// Calendar calendar= new GregorianCalendar();
		Calendar calendar= Calendar.getInstance();
		int hours= calendar.get(Calendar.HOUR);
		int minutes= calendar.get(Calendar.MINUTE);
		int seconds= calendar.get(Calendar.SECOND);
		long milliseconds= calendar.get(Calendar.MILLISECOND);
		result.setNonBacktrackableValue(Converters.formTime(calendar));
		// iX.pushTrail(argument);
	}
	public static void time0fs(ChoisePoint iX) {
	}
	//
	public static void milliseconds0ff(ChoisePoint iX, PrologVariable result) {
		Calendar calendar= Calendar.getInstance();
		long milliseconds= calendar.getTimeInMillis();
		result.setNonBacktrackableValue(new PrologInteger(milliseconds));
		// iX.pushTrail(argument);
	}
	public static void milliseconds0fs(ChoisePoint iX) {
	}
	//
	public static void convertToTime1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		BigInteger milliseconds= Converters.argumentToRoundInteger(a1,iX);
		result.setNonBacktrackableValue(Converters.millisecondsToTime(milliseconds));
		// iX.pushTrail(argument);
	}
	public static void convertToTime1fs(ChoisePoint iX, Term a1) {
	}
	//
	public static void convertToDate1ff(ChoisePoint iX, PrologVariable result, Term a1) {
		BigInteger milliseconds= Converters.argumentToRoundInteger(a1,iX);
		result.setNonBacktrackableValue(Converters.millisecondsToDate(milliseconds));
		// iX.pushTrail(argument);
	}
	public static void convertToDate1fs(ChoisePoint iX, Term a1) {
	}
	//
	public static void convertToMilliseconds2ff(ChoisePoint iX, PrologVariable result, Term a1, Term a2) {
		Calendar calendar= Calendar.getInstance();
		Converters.argumentToTimeInMilliseconds(a1,calendar,iX);
		Converters.argumentToDateInMilliseconds(a2,calendar,iX);
		long milliseconds= calendar.getTimeInMillis();
		result.setNonBacktrackableValue(new PrologInteger(milliseconds));
		// iX.pushTrail(argument);
	}
	public static void convertToMilliseconds2fs(ChoisePoint iX, Term a1, Term a2) {
	}
	//
	public static void date4s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3, PrologVariable a4) {
		Calendar calendar= Calendar.getInstance();
		int year= calendar.get(Calendar.YEAR);
		int month= calendar.get(Calendar.MONTH) + 1;
		int dayOfMonth= calendar.get(Calendar.DAY_OF_MONTH);
		int dayOfWeek= calendar.get(Calendar.DAY_OF_WEEK);
		dayOfWeek--;
		if (dayOfWeek==0) {
			dayOfWeek= 7;
		};
		a1.setBacktrackableValue(new PrologInteger(year),iX);
		a2.setBacktrackableValue(new PrologInteger(month),iX);
		a3.setBacktrackableValue(new PrologInteger(dayOfMonth),iX);
		a4.setBacktrackableValue(new PrologInteger(dayOfWeek),iX);
		//iX.pushTrail(a1);
		//iX.pushTrail(a2);
		//iX.pushTrail(a3);
		//iX.pushTrail(a4);
	}
	public static void date3s(ChoisePoint iX, PrologVariable a1, PrologVariable a2, PrologVariable a3) {
		Calendar calendar= Calendar.getInstance();
		int year= calendar.get(Calendar.YEAR);
		int month= calendar.get(Calendar.MONTH) + 1;
		int day= calendar.get(Calendar.DAY_OF_MONTH);
		a1.setBacktrackableValue(new PrologInteger(year),iX);
		a2.setBacktrackableValue(new PrologInteger(month),iX);
		a3.setBacktrackableValue(new PrologInteger(day),iX);
		//iX.pushTrail(a1);
		//iX.pushTrail(a2);
		//iX.pushTrail(a3);
	}
	public static void date0ff(ChoisePoint iX, PrologVariable result) {
		Calendar calendar= Calendar.getInstance();
		result.setNonBacktrackableValue(Converters.formDate(calendar));
		// iX.pushTrail(argument);
	}
	public static void date0fs(ChoisePoint iX) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void sleep1s(ChoisePoint iX, Term n1) {
		BigDecimal nanos= TimeInterval.argumentSecondsToTimeInterval(n1,iX).toNanosecondsBigDecimal();
		SystemUtils.sleep(nanos,currentProcess);
	}
	//
	public void setPriority1s(ChoisePoint iX, Term n1) {
		try {
			int priority= Converters.termToProcessPriority(n1,iX);
			currentProcess.thread.setPriority(priority);
		} catch (TermIsNotProcessPriority e) {
			throw new WrongArgumentIsNotProcessPriority(n1);
		}
	}
	//
	public void getPriority0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(Converters.ProcessPriorityToTerm(currentProcess.thread.getPriority()));
	}
	public void getPriority0fs(ChoisePoint iX) {
	}
	//
	public void getPriorityNumber0ff(ChoisePoint iX, PrologVariable result) {
		result.setNonBacktrackableValue(new PrologInteger(currentProcess.thread.getPriority()));
	}
	public void getPriorityNumber0fs(ChoisePoint iX) {
	}
}
