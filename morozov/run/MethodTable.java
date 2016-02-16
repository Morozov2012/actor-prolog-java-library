// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.run;

import morozov.run.errors.*;

import java.util.HashMap;

public abstract class MethodTable {
	//
	public static MethodSignature[] initialContent;
	//
	protected static HashMap<Long,MethodSignature> table= new HashMap<>();
	//
	public static void initializeMethodTable() {
		int size= initialContent.length;
		for (int n=0; n < size; n++) {
			MethodSignature item= initialContent[n];
			long domainSignature= item.domainSignature;
			table.put(domainSignature,item);
		}
	}
	//
	public static MethodSignature getSignature(long signatureNumber) {
		MethodSignature signature= table.get(signatureNumber);
		if (signature==null) {
			throw new UnknownSignatureNumber();
		} else {
			return signature;
		}
	}
}
