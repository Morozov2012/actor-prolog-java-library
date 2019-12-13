// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.parameters;

import target.*;

import morozov.terms.*;

public class ActorPrologPackageClassParameter extends ActorPrologPackageParameter {
	//
	protected long nameCode1;
	protected long nameCode2;
	//
	public ActorPrologPackageClassParameter(long nC1, long nC2, int p) {
		super(p);
		nameCode1= nC1;
		nameCode2= nC2;
	}
	public ActorPrologPackageClassParameter(long nC1, int p) {
		super(p);
		nameCode1= nC1;
		nameCode2= nC1;
	}
	//
	public long getNameCode1() {
		return nameCode1;
	}
	public long getNameCode2() {
		return nameCode2;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		if (nameCode1==nameCode2) {
			Term[] internalArray= new Term[2];
			internalArray[0]= new PrologSymbol(nameCode1);
			internalArray[1]= new PrologInteger(position);
			return new PrologStructure(SymbolCodes.symbolCode_E_class_parameter,internalArray);
		} else {
			Term[] internalArray= new Term[3];
			internalArray[0]= new PrologSymbol(nameCode1);
			internalArray[1]= new PrologSymbol(nameCode2);
			internalArray[2]= new PrologInteger(position);
			return new PrologStructure(SymbolCodes.symbolCode_E_default_class_argument,internalArray);
		}
	}
}
