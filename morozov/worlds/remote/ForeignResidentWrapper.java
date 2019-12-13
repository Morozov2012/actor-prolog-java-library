// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;
import morozov.worlds.remote.errors.*;

import java.rmi.RemoteException;

public class ForeignResidentWrapper extends Resident {
	//
	protected ExternalResidentInterface stub;
	//
	private static final long serialVersionUID= 0x2E0FA9FBBF37F4D6L; // 3319058349105345750L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.worlds.remote","ForeignResidentWrapper");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public ForeignResidentWrapper(ExternalResidentInterface s) {
		stub= s;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void returnResultList(AbstractWorld world, Term list) {
                try {
			ExternalWorldInterface currentTarget= OwnWorldWrapper.registerWorld(world);
			byte[] byteArray= GeneralConverters.serializeArgument(list);
			stub.returnResultList(currentTarget,byteArray);
		} catch (RemoteException e) {
			world.withdrawRequest(this);
		}
	}
	//
	@Override
	public void cancelResultList(AbstractWorld world) {
                try {
			ExternalWorldInterface currentTarget= OwnWorldWrapper.registerWorld(world);
			stub.cancelResultList(currentTarget);
		} catch (RemoteException e) {
			world.withdrawRequest(this);
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void sendResidentRequest(Resident resident, long domainSignatureNumber, Term[] arguments, boolean sortAndReduceResultList) {
	}
	//
	@Override
	public void withdrawRequest(Resident resident) {
	}
	//
	@Override
	public void receiveAsyncCall(AsyncCall item) {
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void startProcesses() {
	}
	@Override
	public void releaseSystemResources() {
	}
	@Override
	public void stopProcesses() {
	}
	//
	@Override
	public MethodSignature[] getMethodSignatures() {
		return emptySignatureList;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean isInternalWorldOf(AbstractProcess currentProcess) {
		return false;
	}
	//
	@Override
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		throw Backtracking.instance;
	}
	//
	@Override
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		throw Backtracking.instance;
	}
	//
	@Override
	public AbstractInternalWorld internalWorld(ChoisePoint iX) {
		throw new AnAttemptToExtractInternalWorldFromForeignResidentWrapper();
	}
	//
	@Override
	public AbstractInternalWorld internalWorld() {
		throw new AnAttemptToExtractInternalWorldFromForeignResidentWrapper();
	}
	//
	@Override
	public boolean isNumberOfTemporaryActor() {
		return false;
	}
}
