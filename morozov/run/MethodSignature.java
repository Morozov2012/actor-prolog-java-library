// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.domains.*;

import java.io.Serializable;
import java.util.HashMap;

public class MethodSignature implements Serializable {
	//
	protected long domainSignature;
	protected String methodName;
	protected boolean isFunctionCall;
	protected boolean hasAsterisk;
	protected MethodArgument[] arguments;
	//
	private static final long serialVersionUID= 0x814194FF0C4B61AAL; // -9132854746186817110L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.run","MethodSignature");
	// }
	//
	public MethodSignature(long s, String n, boolean iFC, boolean hA, MethodArgument[] list) {
		domainSignature= s;
		methodName= n;
		isFunctionCall= iFC;
		hasAsterisk= hA;
		arguments= list;
	}
	//
	public long getDomainSignature() {
		return domainSignature;
	}
	public String getMethodName() {
		return methodName;
	}
	public boolean isFunctionCall() {
		return isFunctionCall;
	}
	public boolean hasAsterisk() {
		return hasAsterisk;
	}
	public MethodArgument[] getArguments() {
		return arguments;
	}
	//
	public void collectLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
		if (arguments.length > 0) {
			for (int n=0; n < arguments.length; n++) {
				arguments[n].collectLocalDomainTable(localDomainTable);
			}
		}
	}
	//
	public void acceptLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
		if (arguments.length > 0) {
			for (int n=0; n < arguments.length; n++) {
				arguments[n].acceptLocalDomainTable(localDomainTable);
			}
		}
	}
	//
	public boolean match(MethodSignature foreignSignature) {
		if (!methodName.equals(foreignSignature.methodName)) {
			return false;
		};
		if (isFunctionCall != foreignSignature.isFunctionCall) {
			return false;
		};
		if (arguments.length != foreignSignature.arguments.length) {
			return false;
		};
		if (hasAsterisk != foreignSignature.hasAsterisk) {
			return false;
		};
		for (int n=0; n < arguments.length; n++) {
			if (!arguments[n].match(foreignSignature.arguments[n])) {
				return false;
			}
		};
		return true;
	}
}
