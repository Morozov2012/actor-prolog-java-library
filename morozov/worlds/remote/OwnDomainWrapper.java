// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import morozov.domains.*;

import java.nio.charset.CharsetEncoder;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;

public class OwnDomainWrapper
		extends DomainAbstractWorld
		implements ExternalDomainInterface {
	//
	protected DomainAlternative ownDomain;
	//
	protected static HashMap<DomainAlternative,ExternalDomainInterface> ownDomainRegister= new HashMap<>();
	protected static HashMap<ExternalDomainInterface,OwnDomainWrapper> invertedOwnDomainRegister= new HashMap<>();
	//
	protected static HashMap<ExternalDomainInterface,ForeignDomainWrapper> foreignDomainRegister= new HashMap<>();
	//
	private static final long serialVersionUID= 0x66889E2B6928F6EBL; // 7388329097986373355L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.worlds.remote","OwnDomainWrapper");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public OwnDomainWrapper(DomainAlternative d) {
		ownDomain= d;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static ExternalDomainInterface registerDomain(DomainAbstractWorld domain) throws RemoteException {
		if (domain instanceof ForeignDomainWrapper) {
			ForeignDomainWrapper wrapper= (ForeignDomainWrapper)domain;
			return wrapper.stub;
		} else {
			ExternalDomainInterface stub= ownDomainRegister.get(domain);
			if (stub == null) {
				OwnDomainWrapper wrapper= new OwnDomainWrapper(domain);
				stub= (ExternalDomainInterface) UnicastRemoteObject.exportObject(wrapper,0);
				ownDomainRegister.put(domain,stub);
				invertedOwnDomainRegister.put(stub,wrapper);
			};
			return stub;
		}
	}
	//
	public static DomainAlternative registerWrapper(ExternalDomainInterface stub) {
		OwnDomainWrapper wrapper1= invertedOwnDomainRegister.get(stub);
		if (wrapper1 != null) {
			return wrapper1.ownDomain;
		} else {
			ForeignDomainWrapper wrapper2= foreignDomainRegister.get(stub);
			if (wrapper2 == null) {
				wrapper2= new ForeignDomainWrapper(stub);
				foreignDomainRegister.put(stub,wrapper2);
			};
			return wrapper2;
		}
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack) {
		return ownDomain.isEqualTo(a,stack);
	}
	//
	@Override
	public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) {
		return ownDomain.coversAlternative(a,ownerDomain,stack);
	}
	//
	@Override
	public String toString(CharsetEncoder encoder) {
		return ownDomain.toString(encoder);
	}
}
