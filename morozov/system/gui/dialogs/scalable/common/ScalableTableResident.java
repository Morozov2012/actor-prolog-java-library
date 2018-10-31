/*
 * @(#)ScalableTableResident.java 1.0 2012/08/31
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

/*
 * ScalableTableResident implementation for the Actor Prolog language
 * @version 1.0 2012/08/31
 * @author IRE RAS Alexei A. Morozov
*/

import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.terms.*;
import morozov.worlds.*;

import javax.swing.SwingUtilities;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScalableTableResident extends Resident {
	//
	protected ATable table;
	protected ScalableTableModel tableModel;
	protected AtomicBoolean requestIsToBeSend= new AtomicBoolean(false);
	protected Term previousResult= null;
	//
	public ScalableTableResident(AbstractDialog dialog, ATable control) {
		target= dialog.getTargetWorld();
		table= control;
		// tableModel= model;
	}
	//
	public void initiate(Term aTarget, long signature, Term... args) {
		// staticContext= context;
		// output= aOutput;
		synchronized (this) {
			previousResult= null;
			target= aTarget;
			domainSignature= signature;
			// functor= aFunctor;
			arguments= (Term[])args;
			requestIsToBeSend.set(true);
			wakeUp();
		}
	}
	//
	protected Term get_previous_result() {
		synchronized (this) {
			return previousResult;
		}
	}
	protected void set_previous_result(Term value) {
		synchronized (this) {
			previousResult= value;
		}
	}
	protected AbstractWorld get_target() {
		synchronized (this) {
			return (AbstractWorld)target;
		}
	}
	protected long get_domain_signature() {
		synchronized (this) {
			return domainSignature;
		}
	}
	protected Term[] get_arguments() {
		synchronized (this) {
			return arguments;
		}
	}
	//
	public void acceptFlowMessages() {
		if (!requestIsToBeSend.compareAndSet(true,false)) {
			return;
		};
		get_target().sendResidentRequest(this,get_domain_signature(),get_arguments(),false);
	}
	//
	public void acceptDirectMessage() {
		if (!hasNewResults.compareAndSet(true,false)) {
			return;
		};
		// boolean processWasSuspended= isSuspended;
		// if (isToBeSuspended()) {
		//	if (!processWasSuspended) {
		//		finishPhaseBySuspension();
		//	};
		//	return;
		// };
		phaseInitiation();
		// boolean hasUpdatedPorts= acceptPortValues();
		// Term resultValue= target.substituteWorlds(resultLists,rootCP);
		Term resultValue= resultLists.get(get_target());
		Term previousValue= get_previous_result();
		if (previousValue!=null) {
			try {
				previousValue.unifyWith(resultValue,rootCP);
			} catch (Backtracking b) {
				set_previous_result(resultValue);
				sendResults(resultValue,rootCP);
				requestIsToBeSend.set(true);
				wakeUp();
			} catch (Throwable e) {
			}
		} else {
			set_previous_result(resultValue);
			sendResults(resultValue,rootCP);
			requestIsToBeSend.set(true);
			wakeUp();
		};
		rootCP.freeTrail();
	}
	public void sendResults(final Term resultValue, final ChoisePoint iX) {
		if (SwingUtilities.isEventDispatchThread()) {
			table.putRange(resultValue,true,iX);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						table.putRange(resultValue,true,iX);
					}
				});
			} catch (InterruptedException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
	// public void wakeUp() {
	//	requestIsToBeSend.set(true);
	//	super.wakeUp();
	// }
}
