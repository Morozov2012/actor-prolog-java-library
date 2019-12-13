// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.initializers;

import target.*;

import morozov.run.*;
import morozov.syntax.data.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.system.converters.*;
import morozov.terms.*;

public class ActorPrologResident extends ActorPrologInitializer {
	//
	protected ActorPrologInitializer target;
	protected ActorPrologAtom atom;
	//
	public ActorPrologResident(ActorPrologInitializer t, ActorPrologAtom a) {
		super(t.getPosition());
		target= t;
		atom= a;
	}
	//
	public ActorPrologInitializer getTarget() {
		return target;
	}
	public ActorPrologAtom getAtom() {
		return atom;
	}
	//
	@Override
	public void checkWhetherThereAreNoArrays(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		target.checkWhetherThereAreNoArrays(master,iX);
	}
	@Override
	public void checkWhetherThereAreNoSimpleConstructors(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		target.checkWhetherThereAreNoSimpleConstructors(master,iX);
	}
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] arguments= atom.getArguments();
		Term[] internalArray= new Term[3];
		internalArray[0]= target.toActorPrologTerm();
		internalArray[1]= new PrologSymbol(atom.getFunctorName());
		internalArray[2]= GeneralConverters.arrayToList(arguments);
		return new PrologStructure(SymbolCodes.symbolCode_E_resident,internalArray);
	}
}
