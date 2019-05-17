// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.run.*;
import morozov.syntax.data.errors.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.system.converters.*;
import morozov.terms.*;

public class ActorPrologNearSubroutineCall extends ActorPrologSubgoal {
	//
	protected long functor;
	protected int metavariableNumber;
	protected Term[] arguments;
	protected boolean lastTermHasAsterisk= false;
	protected boolean metavariableIsMetaFunctor= false;
	protected boolean metavariableIsMetaPredicate= false;
	protected boolean atomIsAValidTerm= false;
	protected boolean atomIsAnEnvelope= false;
	protected boolean isSimple= false;
	//
	protected static Term termSelf= new PrologSymbol(SymbolCodes.symbolCode_E_self);
	protected static Term termSubroutine= new PrologSymbol(SymbolCodes.symbolCode_E_subroutine);
	protected static Term termPlainSubgoalSubroutine= new PrologStructure(SymbolCodes.symbolCode_E_plain_subgoal,new Term[]{termSubroutine});
	//
	public ActorPrologNearSubroutineCall(long f, Term[] array, int p, boolean isValidTerm, boolean isEnvelope, boolean isSimpleAtom) {
		super(ActorPrologSubgoalTag.SUBROUTINE_CALL,p);
		functor= f;
		arguments= array;
		lastTermHasAsterisk= false;
		atomIsAValidTerm= isValidTerm;
		atomIsAnEnvelope= isEnvelope;
		isSimple= isSimpleAtom;
	}
	public ActorPrologNearSubroutineCall(ActorPrologAtom atom) {
		super(ActorPrologSubgoalTag.SUBROUTINE_CALL,atom.getPosition());
		functor= atom.getFunctorName();
		metavariableNumber= atom.getMetavariableNumber();
		arguments= atom.getArguments();
		lastTermHasAsterisk= atom.lastTermHasAsterisk();
		metavariableIsMetaFunctor= atom.metavariableIsMetaFunctor();
		metavariableIsMetaPredicate= atom.metavariableIsMetaPredicate();
		atomIsAValidTerm= atom.isAValidTerm();
		atomIsAnEnvelope= atom.isAnEnvelope();
		isSimple= atom.isSimple();
	}
	//
	public long getFunctor() {
		return functor;
	}
	public int getMetavariableNumber() {
		return metavariableNumber;
	}
	public Term[] getArguments() {
		return arguments;
	}
	public boolean lastTermHasAsterisk() {
		return lastTermHasAsterisk;
	}
	public boolean metavariableIsMetaFunctor() {
		return metavariableIsMetaFunctor;
	}
	public boolean metavariableIsMetaPredicate() {
		return metavariableIsMetaPredicate;
	}
	public int getPosition() {
		return position;
	}
	public boolean isAValidTerm() {
		return atomIsAValidTerm;
	}
	public boolean isAnEnvelope() {
		return atomIsAnEnvelope;
	}
	public boolean isSimple() {
		return isSimple;
	}
	//
	public void assignPrimaryFunctionVariable(int variableNumber, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		master.handleError(new SubroutineCallCannotBeFunctionCall(variableNumber,position),iX);
	}
	//
	public void checkWhetherFunctorIsNotAnonymousVariable(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (metavariableIsMetaFunctor) {
			if (metavariableNumber==anonymousVariableNumber) {
				master.handleError(new AnonymousVariableCannotBeMetaFunctorInClauseBody(position),iX);
			}
		} else if (metavariableIsMetaPredicate) {
			if (metavariableNumber==anonymousVariableNumber) {
				master.handleError(new AnonymousVariableCannotBeMetaPredicateInClauseBody(position),iX);
			}
		}
	}
	//
	public ActorPrologAtom toActorPrologAtom() {
		return new ActorPrologAtom(
			functor,
			metavariableNumber,
			arguments,
			lastTermHasAsterisk,
			metavariableIsMetaFunctor,
			metavariableIsMetaPredicate,
			position,
			atomIsAValidTerm,
			atomIsAnEnvelope,
			isSimple);
	}
	//
	public Term toActorPrologTerm() {
		Term[] atomArray= new Term[5];
		if (metavariableIsMetaFunctor) {
			atomArray[0]= new PrologInteger(metavariableNumber);
			atomArray[1]= GeneralConverters.arrayToList(arguments);
		} else if (metavariableIsMetaPredicate) {
			atomArray[0]= new PrologInteger(metavariableNumber);
			atomArray[1]= new PrologSymbol(SymbolCodes.symbolCode_E_metaatom);
		} else {
			atomArray[0]= new PrologSymbol(functor);
			atomArray[1]= GeneralConverters.arrayToList(arguments);
		};
		atomArray[2]= YesNoConverters.boolean2TermYesNo(lastTermHasAsterisk);
		atomArray[3]= YesNoConverters.boolean2TermYesNo(isSimple);
		atomArray[4]= new PrologInteger(position);
		Term atom= new PrologStructure(SymbolCodes.symbolCode_E_atom,atomArray);
		Term[] internalArray= new Term[4];
		internalArray[0]= termSelf;
		internalArray[1]= termPlainSubgoalSubroutine;
		internalArray[2]= atom;
		internalArray[3]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_subgoal,internalArray);
	}
}
