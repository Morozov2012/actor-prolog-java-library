// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;
import morozov.worlds.*;

public class ResidentRequest {
	//
	public AbstractWorld target;
	public Resident resident;
	public long domainSignature;
	public Term[] arguments;
	public boolean sortAndReduceResultList;
	//
	public ResidentRequest(AbstractWorld world, Resident sender, long signature, Term[] list, boolean mode) {
		target= world;
		resident= sender;
		domainSignature= signature;
		arguments= list;
		sortAndReduceResultList= mode;
	}
}
