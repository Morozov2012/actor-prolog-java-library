// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import morozov.run.*;
import morozov.system.*;
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
	public static HashMap<Resident,ExternalResidentInterface> ownResidentRegister= new HashMap<>();
	public static HashMap<ExternalResidentInterface,OwnResidentWrapper> invertedOwnResidentRegister= new HashMap<>();
	//
	public static HashMap<ExternalResidentInterface,ForeignResidentWrapper> foreignResidentRegister= new HashMap<>();
	//
	// protected transient MethodSignature[] methodSignatures= null;
	// protected transient HashMap<String,MethodSignature[]> methodHash= new HashMap<>();
	//
	///////////////////////////////////////////////////////////////
	//
	public OwnResidentWrapper(Resident w) {
		// super(w.getGlobalWorldIdentifier());
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
	public void returnResultList(AbstractWorld target, Term list) {
		ownResident.returnResultList(target,list);
	}
	//
	public void returnResultList(ExternalWorldInterface stub, byte[] argumentByteArray) {
		AbstractWorld world= OwnWorldWrapper.registerWrapper(stub);
		Term list= Converters.deserializeArgument(argumentByteArray,ownResident.domainSignature);
		ownResident.returnResultList(world,list);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void cancelResultList(AbstractWorld target) {
		ownResident.cancelResultList(target);
	}
	public void cancelResultList(ExternalWorldInterface stub) {
		AbstractWorld world= OwnWorldWrapper.registerWrapper(stub);
		ownResident.cancelResultList(world);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void sendResidentRequest(Resident resident, long domainSignature, Term[] arguments, boolean sortAndReduceResultList) {
		ownResident.sendResidentRequest(resident,domainSignature,arguments,sortAndReduceResultList);
	}
	public void withdrawRequest(Resident resident) {
		ownResident.withdrawRequest(resident);
	}
	//
	// public void transmitAsyncCall(AsyncCall item, ChoisePoint iX) {
	//	ownResident.transmitAsyncCall(item,iX);
	// }
	//
	public void receiveAsyncCall(AsyncCall item) {
		ownResident.receiveAsyncCall(item);
	}
	//
	//public void receiveAsyncCall(long domainSignatureNumber, boolean isControlCall, boolean useBuffer, byte[] argumentByteArray) {
	//	ownResident.receiveAsyncCall(domainSignatureNumber,isControlCall,useBuffer,argumentByteArray);
	//}
	//
	///////////////////////////////////////////////////////////////
	//
	public void startProcesses() {
		ownResident.startProcesses();
	}
	public void closeFiles() {
		ownResident.closeFiles();
	}
	public void stopProcesses() {
		ownResident.stopProcesses();
	}
	public MethodSignature[] getMethodSignatures() {
		return ownResident.getMethodSignatures();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public boolean isInternalWorldOf(AbstractProcess currentProcess) {
		return ownResident.isInternalWorldOf(currentProcess);
	}
	//
	public AbstractInternalWorld getInternalWorld(ChoisePoint cp) throws Backtracking, TermIsNotAWorld, TermIsDummyWorld, TermIsUnboundVariable {
		return ownResident.getInternalWorld(cp);
	}
	//
	public AbstractInternalWorld internalWorld(AbstractProcess process, ChoisePoint cp) throws Backtracking {
		return ownResident.internalWorld(process,cp);
	}
	//
	public AbstractInternalWorld internalWorld(ChoisePoint iX) {
		return ownResident.internalWorld(iX);
	}
	//
	public AbstractInternalWorld internalWorld() {
		return ownResident.internalWorld();
	}
	//
	public boolean isNumberOfTemporaryActor() {
		return ownResident.isNumberOfTemporaryActor();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	// private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
	//	stream.defaultReadObject();
	// }
}
