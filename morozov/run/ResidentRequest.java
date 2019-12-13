// (c) 2009 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.terms.*;
import morozov.worlds.*;

public class ResidentRequest {
	//
	protected AbstractWorld target;
	protected Resident resident;
	protected long domainSignature;
	protected Term[] arguments;
	protected boolean sortAndReduceResultList;
	//
	public ResidentRequest(AbstractWorld world, Resident sender, long signature, Term[] list, boolean mode) {
		target= world;
		resident= sender;
		domainSignature= signature;
		arguments= list;
		sortAndReduceResultList= mode;
	}
	//
	public AbstractWorld getTarget() {
		return target;
	};
	public Resident getResident() {
		return resident;
	}
	public long getDomainSignature() {
		return domainSignature;
	}
	public Term[] getArguments() {
		return arguments;
	}
	public int getArgumentsLength() {
		return arguments.length;
	}
	public boolean sortAndReduceResultList() {
		return sortAndReduceResultList;
	}
}
