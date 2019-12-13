// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.run.*;
import morozov.syntax.data.errors.*;
import morozov.syntax.errors.*;
import morozov.syntax.interfaces.*;
import morozov.system.converters.*;
import morozov.terms.*;
import morozov.terms.signals.*;

public class ActorPrologAtom {
	//
	protected long functorName;
	protected int metavariableNumber;
	protected Term[] arguments;
	protected boolean lastArgumentHasAsterisk= false;
	protected boolean metavariableIsMetaFunctor= false;
	protected boolean metavariableIsMetaPredicate= false;
	protected int atomPosition;
	protected boolean atomBeginsWithTheQuestionMark= false;
	protected int questionMarkPosition;
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
	public ActorPrologAtom(long f, int v, Term[] a, boolean hasAsterisk, boolean isMetaFunctor, boolean isMetaPredicate, int aP, boolean beginsWithQuestion, int qMP, boolean isValidTerm, boolean isEnvelope, boolean atomIsSimple) {
		functorName= f;
		metavariableNumber= v;
		arguments= a;
		lastArgumentHasAsterisk= hasAsterisk;
		metavariableIsMetaFunctor= isMetaFunctor;
		metavariableIsMetaPredicate= isMetaPredicate;
		atomPosition= aP;
		atomBeginsWithTheQuestionMark= beginsWithQuestion;
		questionMarkPosition= qMP;
		atomIsAValidTerm= isValidTerm;
		atomIsAnEnvelope= isEnvelope;
		isSimple= atomIsSimple;
	}
	public ActorPrologAtom(long f, Term[] a, boolean hasAsterisk, int aP, boolean beginsWithQuestion, int qMP, boolean isValidTerm, boolean isEnvelope, boolean atomIsSimple) {
		functorName= f;
		arguments= a;
		lastArgumentHasAsterisk= hasAsterisk;
		metavariableIsMetaFunctor= false;
		metavariableIsMetaPredicate= false;
		atomPosition= aP;
		atomBeginsWithTheQuestionMark= beginsWithQuestion;
		questionMarkPosition= qMP;
		atomIsAValidTerm= isValidTerm;
		atomIsAnEnvelope= isEnvelope;
		isSimple= atomIsSimple;
	}
	public ActorPrologAtom(boolean metaFunctor, int number, Term[] a, boolean hasAsterisk, int aP, boolean beginsWithQuestion, int qMP, boolean isValidTerm, boolean isEnvelope, boolean atomIsSimple) {
		if (metaFunctor) {
			metavariableNumber= number;
		} else {
			functorName= number;
		};
		arguments= a;
		lastArgumentHasAsterisk= hasAsterisk;
		metavariableIsMetaFunctor= metaFunctor;
		metavariableIsMetaPredicate= false;
		atomPosition= aP;
		atomBeginsWithTheQuestionMark= beginsWithQuestion;
		questionMarkPosition= qMP;
		atomIsAValidTerm= isValidTerm;
		atomIsAnEnvelope= isEnvelope;
		isSimple= atomIsSimple;
	}
	public ActorPrologAtom(int var, int aP, boolean beginsWithQuestion, int qMP) {
		metavariableNumber= var;
		metavariableIsMetaFunctor= false;
		metavariableIsMetaPredicate= true;
		atomPosition= aP;
		atomBeginsWithTheQuestionMark= beginsWithQuestion;
		questionMarkPosition= qMP;
		atomIsAValidTerm= true;
		atomIsAnEnvelope= false;
		isSimple= true;
	}
	//
	public void insertArgument(Term argument, ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (metavariableIsMetaPredicate) {
			master.handleError(new MetaPredicateIsNotAllowedInFunctionDeclaration(atomPosition),iX);
		};
		if (arguments != null) {
			Term[] newArguments= new Term[arguments.length+1];
			newArguments[0]= argument;
			System.arraycopy(arguments,0,newArguments,1,arguments.length);
			arguments= newArguments;
		} else {
			master.handleError(new MetaPredicateIsNotAllowedInFunctionDeclaration(atomPosition),iX);
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
	public boolean lastArgumentHasAsterisk() {
		return lastArgumentHasAsterisk;
	}
	public int getArity() {
		int arity= arguments.length;
		if (lastArgumentHasAsterisk) {
			arity--;
		};
		return arity;
	}
	public boolean metavariableIsMetaFunctor() {
		return metavariableIsMetaFunctor;
	}
	public boolean metavariableIsMetaPredicate() {
		return metavariableIsMetaPredicate;
	}
	public boolean hasNoName() {
		return	metavariableIsMetaFunctor ||
			metavariableIsMetaPredicate;
	}
	public int getAtomPosition() {
		return atomPosition;
	}
	public boolean atomBeginsWithTheQuestionMark() {
		return atomBeginsWithTheQuestionMark;
	}
	public int getQuestionMarkPosition() {
		return questionMarkPosition;
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
				master.handleError(new AnonymousVariableCannotBeMetaFunctorInClauseBody(atomPosition),iX);
			}
		} else if (metavariableIsMetaPredicate) {
			if (metavariableNumber==anonymousVariableNumber) {
				master.handleError(new AnonymousVariableCannotBeMetaPredicateInClauseBody(atomPosition),iX);
			}
		}
	}
	//
	public int getPositionOfLastArgument(ChoisePoint iX) {
		int p= atomPosition;
		Term lastArgument= arguments[arguments.length-1];
		try {
			long functor= lastArgument.getStructureFunctor(iX);
			if (functor==SymbolCodes.symbolCode_E_p) {
				Term[] terms= lastArgument.getStructureArguments(iX);
				if (terms.length==2) {
					Term termPosition= terms[1];
					p= termPosition.getSmallIntegerValue(iX);
				}
			}
		} catch (TermIsNotAStructure e) {
		} catch (TermIsNotAnInteger e) {
		};
		return p;
	}
	//
	public Term toPlainTerm(ParserMasterInterface master, ChoisePoint iX) throws ParserError {
		if (lastArgumentHasAsterisk) {
			master.handleError(new TermCannotContainTheAsterisk(atomPosition),iX);
		};
		if (!atomIsAValidTerm) {
			master.handleError(new TermIsExpected(atomPosition),iX);
		};
		if (metavariableIsMetaFunctor) {
			master.handleError(new TermCannotContainAMetafunctor(atomPosition),iX);
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
		internalArray[2]= YesNoConverters.boolean2TermYesNo(lastArgumentHasAsterisk);
		internalArray[3]= YesNoConverters.boolean2TermYesNo(isSimple);
		internalArray[4]= new PrologInteger(atomPosition);
		return new PrologStructure(SymbolCodes.symbolCode_E_atom,internalArray);
	}
}
