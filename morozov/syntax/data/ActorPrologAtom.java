// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.run.*;
import morozov.syntax.data.errors.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.system.converters.*;
import morozov.terms.*;

public class ActorPrologAtom {
	//
	protected long functorName;
	protected int metavariableNumber;
	protected Term[] arguments;
	protected boolean lastTermHasAsterisk= false;
	protected boolean metavariableIsMetaFunctor= false;
	protected boolean metavariableIsMetaPredicate= false;
	protected int position;
	protected boolean atomIsAValidTerm= false;
	protected boolean atomIsAnEnvelope= false;
	protected boolean isSimple= false;
	//
	protected static int anonymousVariableNumber= 0;
	protected static int noVariableNumber= -1;
	//
	protected static Term termPlain= new PrologSymbol(SymbolCodes.symbolCode_E_plain);
	protected static Term termMetaatom= new PrologSymbol(SymbolCodes.symbolCode_E_metaatom);
	//
	public ActorPrologAtom(long f, int v, Term[] a, boolean hasAsterisk, boolean isMetaFunctor, boolean isMetaPredicate, int p, boolean isValidTerm, boolean isEnvelope, boolean atomIsSimple) {
		functorName= f;
		metavariableNumber= v;
		arguments= a;
		lastTermHasAsterisk= hasAsterisk;
		metavariableIsMetaFunctor= isMetaFunctor;
		metavariableIsMetaPredicate= isMetaPredicate;
		position= p;
		atomIsAValidTerm= isValidTerm;
		atomIsAnEnvelope= isEnvelope;
		isSimple= atomIsSimple;
	}
	public ActorPrologAtom(long f, Term[] a, boolean hasAsterisk, int p, boolean isValidTerm, boolean isEnvelope, boolean atomIsSimple) {
		functorName= f;
		arguments= a;
		lastTermHasAsterisk= hasAsterisk;
		metavariableIsMetaFunctor= false;
		metavariableIsMetaPredicate= false;
		position= p;
		atomIsAValidTerm= isValidTerm;
		atomIsAnEnvelope= isEnvelope;
		isSimple= atomIsSimple;
	}
	public ActorPrologAtom(boolean metaFunctor, int number, Term[] a, boolean hasAsterisk, int p, boolean isValidTerm, boolean isEnvelope, boolean atomIsSimple) {
		if (metaFunctor) {
			metavariableNumber= number;
		} else {
			functorName= number;
		};
		arguments= a;
		lastTermHasAsterisk= hasAsterisk;
		metavariableIsMetaFunctor= metaFunctor;
		metavariableIsMetaPredicate= false;
		position= p;
		atomIsAValidTerm= isValidTerm;
		atomIsAnEnvelope= isEnvelope;
		isSimple= atomIsSimple;
	}
	public ActorPrologAtom(int var, int p) {
		metavariableNumber= var;
		metavariableIsMetaFunctor= false;
		metavariableIsMetaPredicate= true;
		position= p;
		atomIsAValidTerm= true;
		atomIsAnEnvelope= false;
		isSimple= true;
	}
	//
	public void insertArgument(Term argument, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (metavariableIsMetaPredicate) {
			master.handleError(new MetaPredicateIsNotAllowedInFunctionDeclaration(position),iX);
		};
		if (arguments != null) {
			Term[] newArguments= new Term[arguments.length+1];
			newArguments[0]= argument;
			for (int k=0; k < arguments.length; k++) {
				newArguments[k+1]= arguments[k];
			};
			arguments= newArguments;
		} else {
			master.handleError(new MetaPredicateIsNotAllowedInFunctionDeclaration(position),iX);
		}
	}
	//
	public long getFunctorName() {
		return functorName;
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
	public Term toPlainTerm(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (lastTermHasAsterisk) {
			master.handleError(new TermCannotContainTheAsterisk(position),iX);
		};
		if (!atomIsAValidTerm) {
			master.handleError(new TermIsExpected(position),iX);
		};
		if (metavariableIsMetaFunctor) {
			master.handleError(new TermCannotContainAMetafunctor(position),iX);
			return PrologUnknownValue.instance;
		} else if (metavariableIsMetaPredicate) {
			Term[] internalArray= new Term[2];
			internalArray[0]= new PrologInteger(metavariableNumber);
			internalArray[1]= termPlain;
			return new PrologStructure(SymbolCodes.symbolCode_E_var,internalArray);
		} else {
			Term result;
			if (atomIsAnEnvelope) {
				result= arguments[0];
			} else {
				result= new PrologStructure(functorName,arguments);
			};
			return result;
		}
	}
	//
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[5];
		if (metavariableIsMetaFunctor) {
			internalArray[0]= new PrologInteger(metavariableNumber);
			internalArray[1]= GeneralConverters.arrayToList(arguments);
		} else if (metavariableIsMetaPredicate) {
			internalArray[0]= new PrologInteger(metavariableNumber);
			internalArray[1]= termMetaatom;
		} else {
			internalArray[0]= new PrologSymbol(functorName);
			internalArray[1]= GeneralConverters.arrayToList(arguments);
		};
		internalArray[2]= YesNoConverters.boolean2TermYesNo(lastTermHasAsterisk);
		internalArray[3]= YesNoConverters.boolean2TermYesNo(isSimple);
		internalArray[4]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_atom,internalArray);
	}
}
