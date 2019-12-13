// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import morozov.run.*;
import morozov.system.converters.*;
import morozov.system.datum.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class OwnResidentWrapper
		extends Resident
		implements ExternalResidentInterface {
	//
	protected Resident ownResident;
	//
	protected static HashMap<Resident,ExternalResidentInterface> ownResidentRegister= new HashMap<>();
	protected static HashMap<ExternalResidentInterface,OwnResidentWrapper> invertedOwnResidentRegister= new HashMap<>();
	//
	protected static HashMap<ExternalResidentInterface,ForeignResidentWrapper> foreignResidentRegister= new HashMap<>();
	//
	private static final long serialVersionUID= 0x14B23C67FC75932DL; // 1491320843927917357L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.worlds.remote","OwnResidentWrapper");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public OwnResidentWrapper(Resident w) {
		ownResident= w;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static ExternalResidentInterface registerResident(Resident resident) throws RemoteException {
		if (resident instanceof ForeignResidentWrapper) {
			ForeignResidentWrapper wrapper= (ForeignResidentWrapper)resident;
			return wrapper.stub;
		} else {
			ExternalResidentInterface stub= ownResidentRegister.get(resident);
			if (stub == null) {
				OwnResidentWrapper wrapper= new OwnResidentWrapper(resident);
				stub= (ExternalResidentInterface) UnicastRemoteObject.exportObject(wrapper,0);
				ownResidentRegister.put(resident,stub);
				invertedOwnResidentRegister.put(stub,wrapper);
			};
			return stub;
		}
	}
	//
	public static Resident registerWrapper(ExternalResidentInterface stub, DataStoreInputStream dataStream) {
		OwnResidentWrapper wrapper1= invertedOwnResidentRegister.get(stub);
		if (wrapper1 != null) {
			dataStream.declareWorld();
			return wrapper1.ownResident;
		} else {
			ForeignResidentWrapper wrapper2= foreignResidentRegister.get(stub);
			if (wrapper2 == null) {
				wrapper2= new ForeignResidentWrapper(stub);
				foreignResidentRegister.put(stub,wrapper2);
			};
			return wrapper2;
		}
	}
	public static Resident registerWrapper(ExternalResidentInterface stub) {
		OwnResidentWrapper wrapper1= invertedOwnResidentRegister.get(stub);
		if (wrapper1 != null) {
			return wrapper1.ownResident;
		} else {
			ForeignResidentWrapper wrapper2= foreignResidentRegister.get(stub);
			if (wrapper2 == null) {
				wrapper2= new ForeignResidentWrapper(stub);
				foreignResidentRegister.put(stub,wrapper2);
			};
			return wrapper2;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void returnResultList(AbstractWorld target, Term list) {
		ownResident.returnResultList(target,list);
	}
	//
	@Override
	public void returnResultList(ExternalWorldInterface stub, byte[] argumentByteArray) {
		AbstractWorld world= OwnWorldWrapper.registerWrapper(stub);
		Term list= GeneralConverters.deserializeArgument(argumentByteArray,ownResident.getDomainSignature());
		ownResident.returnResultList(world,list);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void cancelResultList(AbstractWorld target) {
		ownResident.cancelResultList(target);
	}
	@Override
	public void cancelResultList(ExternalWorldInterface stub) {
		AbstractWorld world= OwnWorldWrapper.registerWrapper(stub);
		ownResident.cancelResultList(world);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void sendResidentRequest(Resident resident, long domainSignature, Term[] arguments, boolean sortAndReduceResultList) {
		ownResident.sendResidentRequest(resident,domainSignature,arguments,sortAndReduceResultList);
	}
	@Override
	public void withdrawRequest(Resident resident) {
		ownResident.withdrawRequest(resident);
	}
	@Override
	public void receiveAsyncCall(AsyncCall item) {
		ownResident.receiveAsyncCall(item);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void startProcesses() {
		ownResident.startProcesses();
	}
	@Override
	public void releaseSystemResources() {
		ownResident.releaseSystemResources();
	}
	@Override
	public void stopProcesses() {
		ownResident.stopProcesses();
	}
	@Override
	public MethodSignature[] getMethodSignatures() {
		return ownResident.getMethodSignatures();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean isInternalWorldOf(AbstractProcess currentProcess) {
		return ownResident.isInternalWorldOf(currentProcess);
	}
	//
	@Override
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		return ownResident.getInternalWorld(cp);
	}
	//
	@Override
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		return ownResident.internalWorld(process,cp);
	}
	//
	@Override
	public AbstractInternalWorld internalWorld(ChoisePoint iX) {
		return ownResident.internalWorld(iX);
	}
	//
	@Override
	public AbstractInternalWorld internalWorld() {
		return ownResident.internalWorld();
	}
	//
	@Override
	public boolean isNumberOfTemporaryActor() {
		return ownResident.isNumberOfTemporaryActor();
	}
}
