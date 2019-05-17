// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.worlds.*;

import java.io.Serializable;

public class PredefinedClassRecord implements Serializable {
	//
	public AbstractInternalWorld target;
	//
	private static final long serialVersionUID= 0xA33BD2E435797595L; // -6684517344309840491L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","PredefinedClassRecord");
	// }
	//
	public PredefinedClassRecord(AbstractInternalWorld world) {
		target= world;
	}
}
