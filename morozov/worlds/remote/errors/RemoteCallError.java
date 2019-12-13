// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds.remote.errors;

import java.rmi.RemoteException;

public class RemoteCallError extends RuntimeException {
	//
	protected RemoteException exception;
	//
	public RemoteCallError(RemoteException e) {
		super(e);
		exception= e;
	}
	@Override
	public String toString() {
		return exception.toString();
	}
}
