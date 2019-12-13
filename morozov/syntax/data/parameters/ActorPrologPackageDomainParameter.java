// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.parameters;

import target.*;

import morozov.terms.*;

public class ActorPrologPackageDomainParameter extends ActorPrologPackageParameter {
	//
	protected String name1;
	protected String name2;
	//
	public ActorPrologPackageDomainParameter(String n1, String n2, int p) {
		super(p);
		name1= n1;
		name2= n2;
	}
	public ActorPrologPackageDomainParameter(String n1, int p) {
		super(p);
		name1= n1;
		name2= n1;
	}
	//
	public String getName1() {
		return name1;
	}
	public String getName2() {
		return name2;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		if (name1.equals(name2)) {
			Term[] internalArray= new Term[2];
			internalArray[0]= new PrologString(name1);
			internalArray[1]= new PrologInteger(position);
			return new PrologStructure(SymbolCodes.symbolCode_E_domain_parameter,internalArray);
		} else {
			Term[] internalArray= new Term[3];
			internalArray[0]= new PrologString(name1);
			internalArray[1]= new PrologString(name2);
			internalArray[2]= new PrologInteger(position);
			return new PrologStructure(SymbolCodes.symbolCode_E_default_domain_argument,internalArray);
		}
	}
}
