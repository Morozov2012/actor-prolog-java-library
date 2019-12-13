// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.syntax.data.domains.*;
import morozov.terms.*;

public class ActorPrologSlotDeclaration {
	//
	protected long nameCode;
	protected ActorPrologPortVariety portVariety;
	protected ActorPrologArgumentDomain domain;
	protected ActorPrologVisibility visibility;
	protected int position;
	//
	public ActorPrologSlotDeclaration(
			long nC,
			ActorPrologPortVariety pV,
			ActorPrologArgumentDomain d,
			ActorPrologVisibility v,
			int p) {
		nameCode= nC;
		portVariety= pV;
		domain= d;
		visibility= v;
		position= p;
	}
	//
	public long getNameCode() {
		return nameCode;
	}
	public ActorPrologPortVariety getPortVariety() {
		return portVariety;
	}
	public ActorPrologArgumentDomain getDomain() {
		return domain;
	}
	public ActorPrologVisibility getVisibility() {
		return visibility;
	}
	public int getPosition() {
		return position;
	}
	//
	public static Term arrayToList(ActorPrologSlotDeclaration[] array) {
		Term result= PrologEmptyList.instance;
		for (int k=array.length-1; k >= 0; k--) {
			result= new PrologList(array[k].toActorPrologTerm(),result);
		};
		return result;
	}
	//
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[5];
		internalArray[0]= new PrologSymbol(nameCode);
		internalArray[1]= portVariety.toTerm();
		internalArray[2]= domain.toActorPrologTerm();
		internalArray[3]= visibility.toTerm();
		internalArray[4]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_slot,internalArray);
	}
}
