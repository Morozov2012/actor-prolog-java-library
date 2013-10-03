/*
 * @(#)ScalableTableResident.java 1.0 2012/08/31
 *
 * Copyright 2012 IRE RAS Alexei A. Morozov
*/

package morozov.system.gui.dialogs.scalable.common;

import morozov.classes.*;
import morozov.run.*;
import morozov.system.gui.dialogs.*;
import morozov.terms.*;

import javax.swing.SwingUtilities;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * ScalableTableResident implementation for the Actor Prolog language
 * @version 1.0 2012/08/31
 * @author IRE RAS Alexei A. Morozov
*/

public class ScalableTableResident extends Resident {
	//
	protected ATable table;
	protected ScalableTableModel tableModel;
	protected AtomicBoolean requestIsToBeSend= new AtomicBoolean(false);
	protected Term previousResult= null;
	//
	public ScalableTableResident(AbstractDialog dialog, ATable control) {
		target= dialog.targetWorld;
		table= control;
		// tableModel= model;
	}
	//
	public void initiate(Term aTarget, long signature, Term... args) {
		// staticContext= context;
		// output= aOutput;
		synchronized(this) {
			previousResult= null;
			target= aTarget;
			domainSignature= signature;
			// functor= aFunctor;
			arguments= (Term[])args;
			// System.out.printf("Resident::initiate/3(%s,%s,%s)\n",target,this,domainSignature,arguments[0]);
			requestIsToBeSend.set(true);
			wakeUp();
		}
	}
	//
	protected Term get_previous_result() {
		synchronized(this) {
			return previousResult;
		}
	}
	protected void set_previous_result(Term value) {
		synchronized(this) {
			previousResult= value;
		}
	}
	protected AbstractWorld get_target() {
		synchronized(this) {
			return (AbstractWorld)target;
		}
	}
	protected long get_domain_signature() {
		synchronized(this) {
			return domainSignature;
		}
	}
	protected Term[] get_arguments() {
		synchronized(this) {
			return arguments;
		}
	}
	//
	public void acceptFlowMessages() {
		// System.out.printf("Resident::[1]acceptFlowMessages\n");
		if (!requestIsToBeSend.compareAndSet(true,false)) {
			return;
		};
		// System.out.printf("Resident::[2]acceptFlowMessages\n");
		// phaseInitiation();
		// boolean hasUpdatedPorts= acceptPortValues();
		//
		// prepareTargetWorlds(target,iX);
		// Term[] argumentList= prepareArguments(iX);
		//
		// fixSlotVariables(true);
		// storeSlotVariables();
		//
		// Iterator<AbstractWorld> targetWorldsIterator= targetWorlds.iterator();
		// while (targetWorldsIterator.hasNext()) {
			// AbstractWorld world= targetWorldsIterator.next();
			// System.out.printf("Resident::%s.sendResidentRequest(%s,%s,%s)\n",world,this,get_domain_signature(),get_arguments());
		get_target().sendResidentRequest(this,get_domain_signature(),get_arguments(),false,rootCP);
		// };
		// hasNewResults.set(true);
		// System.out.printf("Resident::[3]acceptFlowMessages:wakeUp()\n");
		// wakeUp();
	}
	//
	// public void returnResultList(AbstractWorld target, Term list) {
	//	System.out.printf("%s\n",list);
	//	tableModel.setTableValues(list,null);
	// }
	//
	public void acceptDirectMessage() {
		// System.out.printf("Resident::acceptDirectMessage()\n");
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
		// System.out.printf("Resident::%s\n",resultValue);
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
