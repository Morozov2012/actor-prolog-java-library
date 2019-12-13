// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.system.converters.*;
import morozov.terms.*;

public class ActorPrologPredicateDeclaration {
	//
	protected long nameCode;
	protected int arity;
	protected boolean lastArgumentHasAsterisk;
	protected boolean isFunction;
	protected ActorPrologPredicateDeterminancy determinancy;
	protected ActorPrologPredicateArgumentDeclaration[] arguments;
	protected ActorPrologPredicateFlowDirection[][] flowPatterns;
	protected ActorPrologVisibility visibility;
	protected int position;
	//
	protected static Term termSubroutine= new PrologSymbol(SymbolCodes.symbolCode_E_subroutine);
	protected static Term termFunction= new PrologSymbol(SymbolCodes.symbolCode_E_function);
	//
	public ActorPrologPredicateDeclaration(
			long nC,
			boolean hA,
			boolean iF,
			ActorPrologPredicateDeterminancy d,
			ActorPrologPredicateArgumentDeclaration[] a,
			ActorPrologPredicateFlowDirection[][] fP,
			ActorPrologVisibility v,
			int p) {
		nameCode= nC;
		lastArgumentHasAsterisk= hA;
		isFunction= iF;
		determinancy= d;
		arguments= a;
		flowPatterns= fP;
		visibility= v;
		position= p;
		arity= arguments.length;
		if (isFunction) {
			arity--;
		}
	}
	//
	public long getNameCode() {
		return nameCode;
	}
	public int getArity() {
		return arity;
	}
	public boolean lastArgumentHasAsterisk() {
		return lastArgumentHasAsterisk;
	}
	public boolean isFunction() {
		return isFunction;
	}
	public ActorPrologPredicateDeterminancy getDeterminancy() {
		return determinancy;
	}
	public ActorPrologPredicateArgumentDeclaration[] getArguments() {
		return arguments;
	}
	public ActorPrologPredicateFlowDirection[][] flowPatterns() {
		return flowPatterns;
	}
	public ActorPrologVisibility getVisibility() {
		return visibility;
	}
	public int getPosition() {
		return position;
	}
	//
	public static Term arrayToList(ActorPrologPredicateDeclaration[] array) {
		Term result= PrologEmptyList.instance;
		for (int k=array.length-1; k >= 0; k--) {
			result= new PrologList(array[k].toActorPrologTerm(),result);
		};
		return result;
	}
	//
	public Term toActorPrologTerm() {
		Term termArguments= PrologEmptyList.instance;
		for (int k=arguments.length-1; k >= 0; k--) {
			termArguments= new PrologList(arguments[k].toActorPrologTerm(),termArguments);
		};
		Term termFlowPatterns= PrologEmptyList.instance;
		for (int k=flowPatterns.length-1; k >= 0; k--) {
			ActorPrologPredicateFlowDirection[] pattern= flowPatterns[k];
			Term termPattern= PrologEmptyList.instance;
			for (int m=pattern.length-1; m >= 0; m--) {
				Term termFlowDirection= pattern[m].toTerm();
				termPattern= new PrologList(termFlowDirection,termPattern);
			};
			termFlowPatterns= new PrologList(termPattern,termFlowPatterns);
		};
		Term[] internalArray= new Term[9];
		internalArray[0]= new PrologSymbol(nameCode);
		internalArray[1]= new PrologInteger(arity);
		internalArray[2]= YesNoConverters.boolean2TermYesNo(lastArgumentHasAsterisk);
		if (isFunction) {
			internalArray[3]= termFunction;
		} else {
			internalArray[3]= termSubroutine;
		};
		internalArray[4]= determinancy.toTerm();
		internalArray[5]= termArguments;
		internalArray[6]= termFlowPatterns;
		internalArray[7]= visibility.toTerm();
		internalArray[8]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_predicate,internalArray);
	}
}
