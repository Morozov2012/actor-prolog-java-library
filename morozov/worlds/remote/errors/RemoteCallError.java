// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.worlds.remote.errors;

import java.rmi.RemoteException;

public class RemoteCallError extends RuntimeException {
	public RemoteException exception;
	public RemoteCallError(RemoteException e) {
		super(e);
		exception= e;
	}
	public String toString() {
		return exception.toString();
	}
}
