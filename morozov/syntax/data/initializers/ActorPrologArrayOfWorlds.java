// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.initializers;

import target.*;

import morozov.run.*;
import morozov.syntax.data.initializers.errors.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.terms.*;

public class ActorPrologArrayOfWorlds extends ActorPrologInitializer {
	//
	protected long nameCode;
	protected Term range;
	protected ActorPrologInitializer initializer;
	//
	public ActorPrologArrayOfWorlds(long c, Term r, ActorPrologInitializer i, int p) {
		super(p);
		nameCode= c;
		range= r;
		initializer= i;
	}
	//
	public long getNameCode() {
		return nameCode;
	}
	public Term getRange() {
		return range;
	}
	//
	@Override
	public void checkWhetherThereAreNoArrays(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		master.handleError(new TheArrayCannotBeAnArgumentOfConstructor(getPosition()),iX);
		initializer.checkWhetherThereAreNoArrays(master,iX);
	}
	@Override
	public void checkWhetherThereAreNoSimpleConstructors(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		initializer.checkWhetherThereAreNoSimpleConstructors(master,iX);
	}
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[3];
		internalArray[0]= new PrologSymbol(nameCode);
		internalArray[1]= range;
		internalArray[2]= initializer.toActorPrologTerm();
		return new PrologStructure(SymbolCodes.symbolCode_E_array_of_worlds,internalArray);
	}
}
