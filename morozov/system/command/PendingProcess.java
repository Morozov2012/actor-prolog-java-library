// (c) 2010-2015 IRE RAS Alexei A. Morozov

package morozov.system.command;

import target.*;

import morozov.built_in.*;
import morozov.run.*;
import morozov.terms.*;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class PendingProcess extends Thread {
	//
	protected Application targetWorld;
	protected Process observedProcess;
	protected long domainSignature;
	protected HashSet<PendingProcess> supervisors;
	protected AtomicBoolean isActual= new AtomicBoolean(true);
	//
	public PendingProcess(Application target, Process process, long signature, HashSet<PendingProcess> supervisorList) {
		targetWorld= target;
		observedProcess= process;
		domainSignature= signature;
		supervisors= supervisorList;
	}
	//
	@Override
	public void run() {
		Term[] arguments= new Term[1];
		try {
			int exitValue= observedProcess.waitFor();
			arguments[0]= new PrologInteger(exitValue);
		} catch (InterruptedException e) {
			arguments[0]= new PrologSymbol(SymbolCodes.symbolCode_E_interrupted_exception);
		};
		supervisors.remove(this);
		if (isActual.get()) {
			AsyncCall item= new AsyncCall(domainSignature,targetWorld,true,true,arguments,true);
			targetWorld.transmitAsyncCall(item,null);
		}
	}
	//
	public void cancel() {
		isActual.set(false);
	}
}
