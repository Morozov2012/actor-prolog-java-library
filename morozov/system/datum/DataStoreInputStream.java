// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import morozov.domains.*;
import morozov.worlds.*;
import morozov.worlds.remote.*;

import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.IOException;

public class DataStoreInputStream extends ObjectInputStream {
	protected boolean someWorldIsDetected= false;
	protected boolean analyseDataTables= false;
	protected DatabaseTable currentDatabaseTable= null;
	protected boolean aWorldIsDetectedInTable= false;
	//
	///////////////////////////////////////////////////////////////
	//
	public DataStoreInputStream(InputStream out, boolean mode) throws IOException {
		super(out);
		analyseDataTables= mode;
		enableResolveObject(true);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	protected final Object resolveObject(Object obj) throws IOException {
		if (obj instanceof ExternalWorldInterface) {
			ExternalWorldInterface stub= (ExternalWorldInterface)obj;
			AbstractWorld world= OwnWorldWrapper.registerWrapper(stub,this);
			return world;
		} else if (obj instanceof ExternalDomainInterface) {
			ExternalDomainInterface stub= (ExternalDomainInterface)obj;
			DomainAlternative domain= OwnDomainWrapper.registerWrapper(stub);
			return domain;
		} else if (obj instanceof ExternalResidentInterface) {
			ExternalResidentInterface stub= (ExternalResidentInterface)obj;
			Resident resident= OwnResidentWrapper.registerWrapper(stub);
			return resident;
		};
		return obj;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public void beginDatabaseTable(DatabaseTable table) {
		currentDatabaseTable= table;
		aWorldIsDetectedInTable= false;
	}
	//
	public void endDatabaseTable(DatabaseTable table) {
		currentDatabaseTable.declareWhetherAWorldIsDetected(aWorldIsDetectedInTable);
		currentDatabaseTable= null;
	}
	//
	public void declareWorld() {
		someWorldIsDetected= true;
		aWorldIsDetectedInTable= true;
	}
	//
	public boolean worldsAreDetected() {
		return someWorldIsDetected;
	}
}
