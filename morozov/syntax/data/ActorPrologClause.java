// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.syntax.data.subgoals.*;
import morozov.terms.*;

public class ActorPrologClause {
	//
	protected boolean isFunction;
	protected ActorPrologAtom titleSimpleAtom;
	protected boolean isExternal;
	protected boolean isEntry;
	protected ActorPrologSubgoal[] subgoals;
	protected FunctionCallDefinition[] functionCallDefinitionArray;
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
			FunctionCallDefinition[] functionCallTable,
			int p) {
		isFunction= function;
		titleSimpleAtom= atom;
		isExternal= false;
		isEntry= false;
		subgoals= subgoalArray;
		functionCallDefinitionArray= functionCallTable;
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
		isEntry= false;
		externalProcedureName= name;
		position= p;
	}
	public ActorPrologClause(
			boolean function,
			ActorPrologAtom atom,
			int p) {
		isFunction= function;
		titleSimpleAtom= atom;
		isExternal= false;
		isEntry= true;
		position= p;
	}
	//
	public boolean isFunction() {
		return isFunction;
	}
	public ActorPrologAtom getTitleSimpleAtom() {
		return titleSimpleAtom;
	}
	public boolean isExternal() {
		return isExternal;
	}
	public boolean isEntry() {
		return isEntry;
	}
	public ActorPrologSubgoal[] getSubgoals() {
		return subgoals;
	}
	public FunctionCallDefinition[] getFunctionCallDefinitionArray() {
		return functionCallDefinitionArray;
	}
	public String getExternalProcedureName() {
		return externalProcedureName;
	}
	public int getPosition() {
		return position;
	}
	//
	public boolean hasNoName() {
		return titleSimpleAtom.hasNoName();
	}
	public long getFunctorName() {
		return titleSimpleAtom.getFunctorName();
	}
	public boolean lastArgumentHasAsterisk() {
		return titleSimpleAtom.lastArgumentHasAsterisk();
	}
	public int getArity() {
		int arity= titleSimpleAtom.getArity();
		if (isFunction) {
			arity--;
		};
		return arity;
	}
	//
	public static Term arrayToList(ActorPrologClause[] array) {
		Term result= PrologEmptyList.instance;
		for (int k=array.length-1; k >= 0; k--) {
			result= new PrologList(array[k].toActorPrologTerm(),result);
		};
		return result;
	}
	//
	public Term toActorPrologTerm() {
		Term[] internalArray;
		if (isEntry) {
			internalArray= new Term[3];
		} else {
			internalArray= new Term[5];
		};
		if (isFunction) {
			internalArray[0]= termFunction;
		} else {
			internalArray[0]= termSubroutine;
		};
		internalArray[1]= titleSimpleAtom.toActorPrologTerm();
		if (isEntry) {
			internalArray[2]= new PrologInteger(position);
			return new PrologStructure(SymbolCodes.symbolCode_E_external_call,internalArray);
		} else {
			if (isExternal) {
				Term[] arguments= new Term[]{new PrologString(externalProcedureName)};
				internalArray[2]= new PrologStructure(SymbolCodes.symbolCode_E_external_procedure,arguments);
				internalArray[3]= PrologEmptyList.instance;
			} else {
				Term list1= PrologEmptyList.instance;
				for (int k=subgoals.length-1; k >= 0; k--) {
					list1= new PrologList(subgoals[k].toActorPrologTerm(),list1);
				};
				internalArray[2]= list1;
				Term list2= PrologEmptyList.instance;
				for (int k=functionCallDefinitionArray.length-1; k >= 0; k--) {
					FunctionCallDefinition definition= functionCallDefinitionArray[k];
					list2= new PrologList(definition.toActorPrologTerm(),list2);
				};
				internalArray[3]= list2;
			};
			internalArray[4]= new PrologInteger(position);
			return new PrologStructure(SymbolCodes.symbolCode_E_clause,internalArray);
		}
	}
}
