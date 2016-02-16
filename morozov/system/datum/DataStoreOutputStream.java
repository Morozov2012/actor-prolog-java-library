// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import morozov.domains.*;
import morozov.worlds.*;
import morozov.worlds.remote.*;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.lang.SecurityException;

public class DataStoreOutputStream extends ObjectOutputStream {
	public DataStoreOutputStream() throws IOException, SecurityException {
		enableReplaceObject(true);
	}
	public DataStoreOutputStream(OutputStream out) throws IOException {
		super(out);
		enableReplaceObject(true);
	}
	//
	protected final Object replaceObject(Object obj) throws IOException {
		if (obj instanceof AbstractWorld) {
			AbstractWorld world= (AbstractWorld)obj;
			ExternalWorldInterface stub= OwnWorldWrapper.registerWorld(world);
			return stub;
		} else if (obj instanceof DomainAbstractWorld) {
			DomainAbstractWorld domain= (DomainAbstractWorld)obj;
			ExternalDomainInterface stub= OwnDomainWrapper.registerDomain(domain);
			return stub;
		} else if (obj instanceof Resident) {
			Resident resident= (Resident)obj;
			ExternalResidentInterface stub= OwnResidentWrapper.registerResident(resident);
			return stub;
		};
		return obj;
	}
}
