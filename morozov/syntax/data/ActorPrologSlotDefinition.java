// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.syntax.data.domains.*;
import morozov.syntax.data.initializers.*;
import morozov.terms.*;

public class ActorPrologSlotDefinition {
	//
	protected long nameCode;
	protected ActorPrologPortVariety portVariety;
	protected ActorPrologInitializer initializer;
	protected ActorPrologVisibility visibility;
	protected int position;
	//
	public ActorPrologSlotDefinition(
			long nC,
			ActorPrologPortVariety pV,
			ActorPrologInitializer i,
			ActorPrologVisibility v,
			int p) {
		nameCode= nC;
		portVariety= pV;
		initializer= i;
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
	public ActorPrologInitializer getInitializer() {
		return initializer;
	}
	public ActorPrologVisibility getVisibility() {
		return visibility;
	}
	public int getPosition() {
		return position;
	}
	//
	public ActorPrologSlotDeclaration toSlotDeclaration(ActorPrologArgumentDomain domain) {
		return new ActorPrologSlotDeclaration(
			nameCode,
			portVariety,
			domain,
			visibility,
			position);
	}
	//
	public static Term arrayToList(ActorPrologSlotDefinition[] array) {
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
		internalArray[2]= initializer.toActorPrologTerm();
		internalArray[3]= visibility.toTerm();
		internalArray[4]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_slot,internalArray);
	}
}
