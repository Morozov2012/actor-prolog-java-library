// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.terms.*;

public class ActorPrologClause {
	//
	protected boolean isFunction;
	protected ActorPrologAtom titleSimpleAtom;
	protected boolean isExternal;
	protected ActorPrologSubgoal[] subgoals;
	protected String externalProcedureName;
	protected int position;
	//
	protected static Term termFunction= new PrologSymbol(SymbolCodes.symbolCode_E_function);
	protected static Term termSubroutine= new PrologSymbol(SymbolCodes.symbolCode_E_subroutine);
	//
	public ActorPrologClause(
			boolean function,
			ActorPrologAtom atom,
			ActorPrologSubgoal[] subgoalArray,
			int p) {
		isFunction= function;
		titleSimpleAtom= atom;
		isExternal= false;
		subgoals= subgoalArray;
		position= p;
	}
	public ActorPrologClause(
			boolean function,
			ActorPrologAtom atom,
			String name,
			int p) {
		isFunction= function;
		titleSimpleAtom= atom;
		isExternal= true;
		externalProcedureName= name;
		position= p;
	}
	//
	public boolean isFunction() {
		return isFunction;
	}
	public ActorPrologAtom getTitleSimpleAtom() {
		return titleSimpleAtom;
	}
	public ActorPrologSubgoal[] getSubgoals() {
		return subgoals;
	}
	public int getPosition() {
		return position;
	}
	//
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[4];
		if (isFunction) {
			internalArray[0]= termFunction;
		} else {
			internalArray[0]= termSubroutine;
		};
		internalArray[1]= titleSimpleAtom.toActorPrologTerm();
		if (isExternal) {
			Term[] arguments= new Term[]{new PrologString(externalProcedureName)};
			internalArray[2]= new PrologStructure(SymbolCodes.symbolCode_E_external,arguments);
		} else {
			Term list= PrologEmptyList.instance;
			for (int n=subgoals.length-1; n >= 0; n--) {
				list= new PrologList(subgoals[n].toActorPrologTerm(),list);
			};
			internalArray[2]= list;
		};
		internalArray[3]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_clause,internalArray);
	}
}
