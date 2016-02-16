// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ExternalResidentInterface extends Remote {
	public void returnResultList(ExternalWorldInterface target, byte[] list) throws RemoteException;
	public void cancelResultList(ExternalWorldInterface target) throws RemoteException;
}
