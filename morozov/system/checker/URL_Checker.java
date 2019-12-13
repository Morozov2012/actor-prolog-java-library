// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import morozov.run.*;
import morozov.system.*;
import morozov.system.files.*;
import morozov.worlds.*;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.net.URI;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import java.util.Calendar;
import java.util.NoSuchElementException;

public class URL_Checker extends ThreadHolder {
	//
	protected SortedMap<BigInteger,FutureEvent> eventTable= Collections.synchronizedSortedMap(new TreeMap<BigInteger,FutureEvent>());
	protected static final BigInteger oneMillion= BigInteger.valueOf(1000000);
	//
	public URL_Checker(ActiveWorld process) {
		super(process);
	}
	//
	@Override
	public void run() {
		while (!stopThisThread.get()) {
			BigInteger firstKey= null;
			try {
				firstKey= eventTable.firstKey();
			} catch (NoSuchElementException e) {
			};
			Calendar calendar= Calendar.getInstance();
			BigInteger currentTime= BigInteger.valueOf(calendar.getTimeInMillis()).multiply(oneMillion);
			if (firstKey==null) {
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
					}
				}
			} else if (firstKey.compareTo(currentTime) > 0) {
				BigInteger delayInNanoseconds= firstKey.subtract(currentTime);
				BigInteger delayInMilliseconds= delayInNanoseconds.divide(oneMillion); //,MathContext.DECIMAL128);
				BigInteger nanoDelay= delayInNanoseconds.remainder(oneMillion); //,MathContext.DECIMAL128);
				long timeout= Arithmetic.toLong(delayInMilliseconds);
				int nanos= nanoDelay.intValue();
				if (timeout > 0 || nanos > 0) {
					synchronized (this) {
						try {
							wait(timeout,nanos);
						} catch (InterruptedException e) {
						}
					}
				}
			} else {
				FutureEvent firstEvent= (FutureEvent)eventTable.remove(firstKey);
				if (firstEvent != null) {
					URL_Attributes newAttributes= performCheck(firstEvent.recentAttributes,currentTime);
					BigDecimal checkUpPeriod;
					if (newAttributes.connectionWasSuccessful()) {
						checkUpPeriod= firstEvent.revisionPeriod;
					} else {
						checkUpPeriod= firstEvent.attemptPeriod;
					};
					BigDecimal eT= (new BigDecimal(currentTime)).add(checkUpPeriod);
					BigInteger eventTime= eT.toBigInteger();
					addFutureEvent(eventTime,firstEvent.actorNumber,newAttributes,firstEvent.revisionPeriod,firstEvent.attemptPeriod);
				}
			}
		}
	}
	//
	public URL_Attributes performCheck(URL_Attributes recentAttributes, BigInteger currentTime) {
		try {
			URL_Attributes newAttributes= ExtendedFileName.renewUniversalResourceAttributes(recentAttributes);
			if (newAttributes.wereChanged(recentAttributes)) {
				((ActiveResource)currentProcess).publishResults(currentTime);
			};
			return newAttributes;
		} catch (Throwable e) {
			if (!recentAttributes.connectionThrowsException(e)) {
				((ActiveResource)currentProcess).publishResults(currentTime);
				recentAttributes.setException(e);
			};
			return recentAttributes;
		}
	}
	//
	public void addFutureEvent(BigInteger eT, ActorNumber actor, URL_Attributes attributes, BigDecimal rP, BigDecimal aP) {
		synchronized (eventTable) {
			addFutureEventUnsafely(eT,actor,attributes,rP,aP);
		};
		wakeUp();
	}
	public void addFutureEventUnsafely(BigInteger eT, ActorNumber actor, URL_Attributes attributes, BigDecimal rP, BigDecimal aP) {
		BigInteger eventTime= eT;
		while (true) {
			FutureEvent selectedItem= eventTable.get(eventTime);
			if (selectedItem==null) {
				eventTable.put(eventTime,new FutureEvent(eT,actor,attributes,rP,aP));
				break;
			} else {
				eventTime= eventTime.add(BigInteger.ONE);
				continue;
			}
		}
	}
	//
	public void addFutureEvents(HashMap<ActorAndURI,WebReceptorRecord> actualRecords, BigInteger currentTime) {
		Collection<WebReceptorRecord> actualRecordValues= actualRecords.values();
		Iterator<WebReceptorRecord> actualRecordsIterator= actualRecordValues.iterator();
		synchronized (eventTable) {
			while(actualRecordsIterator.hasNext()) {
				WebReceptorRecord currentRecord= actualRecordsIterator.next();
				BigDecimal eT= (new BigDecimal(currentTime)).add(currentRecord.getCheckUpPeriod());
				BigInteger eventTime= eT.toBigInteger();
				addFutureEventUnsafely(eventTime,currentRecord.actorNumber,currentRecord.attributes,currentRecord.revisionPeriod,currentRecord.attemptPeriod);
			}
		};
		wakeUp();
	}
	//
	public void checkAndAddFutureEvents(HashMap<ActorAndURI,WebReceptorRecord> actualRecords, BigInteger currentTime) {
		Collection<WebReceptorRecord> actualRecordValues= actualRecords.values();
		Iterator<WebReceptorRecord> actualRecordsIterator= actualRecordValues.iterator();
		synchronized (eventTable) {
			Set<Map.Entry<BigInteger,FutureEvent>> set= eventTable.entrySet();
			Iterator<Map.Entry<BigInteger,FutureEvent>> iterator= set.iterator();
			while(iterator.hasNext()) {
				Map.Entry<BigInteger,FutureEvent> item= iterator.next();
				FutureEvent event= item.getValue();
				ActorNumber actorNumber= event.actorNumber;
				URI uri= event.recentAttributes.uri;
				ActorAndURI key= new ActorAndURI(actorNumber,uri);
				WebReceptorRecord newRecord= actualRecords.get(key);
				if (newRecord != null) {
					iterator.remove();
				}
			};
			while(actualRecordsIterator.hasNext()) {
				WebReceptorRecord currentRecord= actualRecordsIterator.next();
				BigDecimal cT= (new BigDecimal(currentTime)).add(currentRecord.getCheckUpPeriod());
				BigInteger eventTime= cT.toBigInteger();
				addFutureEventUnsafely(eventTime,currentRecord.actorNumber,currentRecord.attributes,currentRecord.revisionPeriod,currentRecord.attemptPeriod);
			}
		};
		wakeUp();
	}
	//
	public void forgetEvents(ActorNumber actor) {
		synchronized (eventTable) {
			Set<Map.Entry<BigInteger,FutureEvent>> set= eventTable.entrySet();
			Iterator<Map.Entry<BigInteger,FutureEvent>> iterator= set.iterator();
			while(iterator.hasNext()) {
				Map.Entry<BigInteger,FutureEvent> item= iterator.next();
				if (item.getValue().actorNumber==actor) {
					iterator.remove();
				}
			}
		}
	}
}
