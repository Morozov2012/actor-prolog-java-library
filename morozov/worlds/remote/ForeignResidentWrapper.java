// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.rmi.RemoteException;

class AnAttemptToExtractInternalWorldFromForeignResidentWrapper extends RuntimeException {}

public class ForeignResidentWrapper extends Resident {
	//
	public ExternalResidentInterface stub;
	//
	public ForeignResidentWrapper(ExternalResidentInterface s) {
		stub= s;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void returnResultList(AbstractWorld world, Term list) {
                try {
			ExternalWorldInterface target= OwnWorldWrapper.registerWorld(world);
			byte[] byteArray= GeneralConverters.serializeArgument(list);
			stub.returnResultList(target,byteArray);
		} catch (RemoteException e) {
			world.withdrawRequest(this);
		}
	}
	//
	public void cancelResultList(AbstractWorld world) {
                try {
			ExternalWorldInterface target= OwnWorldWrapper.registerWorld(world);
			stub.cancelResultList(target);
		} catch (RemoteException e) {
			world.withdrawRequest(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void sendResidentRequest(Resident resident, long domainSignatureNumber, Term[] arguments, boolean sortAndReduceResultList) {
	}
	//
	public void withdrawRequest(Resident resident) {
	}
	//
	public void receiveAsyncCall(AsyncCall item) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void startProcesses() {
	}
	public void releaseSystemResources() {
	}
	public void stopProcesses() {
	}
	//
	public MethodSignature[] getMethodSignatures() {
		return emptySignatureList;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isInternalWorldOf(AbstractProcess currentProcess) {
		return false;
	}
	//
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw Backtracking.instance;
	}
	//
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	//
	public AbstractInternalWorld internalWorld(ChoisePoint iX) {
		throw new AnAttemptToExtractInternalWorldFromForeignResidentWrapper();
	}
	//
	public AbstractInternalWorld internalWorld() {
		throw new AnAttemptToExtractInternalWorldFromForeignResidentWrapper();
	}
	//
	public boolean isNumberOfTemporaryActor() {
		return false;
	}
}
