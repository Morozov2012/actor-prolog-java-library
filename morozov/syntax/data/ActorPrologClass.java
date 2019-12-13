// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.system.converters.*;
import morozov.terms.*;

public class ActorPrologClass extends ActorPrologUnit {
	//
	protected long ancestorNameCode;
	protected ActorPrologSlotDefinition[] slotDefinitions;
	protected ActorPrologClause[] actingClauses;
	protected ActorPrologClause[] modelClauses;
	protected Term classSource;
	protected ActorPrologInterface classInterface;
	protected String[] variableNames;
	//
	public ActorPrologClass(
			long cNC,
			long aNC,
			ActorPrologSlotDefinition[] sD,
			ActorPrologClause[] aC,
			ActorPrologClause[] mC,
			Term cS,
			ActorPrologInterface cI,
			String[] vN,
			int p) {
		super(cNC,p);
		ancestorNameCode= aNC;
		slotDefinitions= sD;
		actingClauses= aC;
		modelClauses= mC;
		classSource= cS;
		classInterface= cI;
		variableNames= vN;
	}
	//
	public long getAncestorNameCode() {
		return ancestorNameCode;
	}
	public ActorPrologSlotDefinition[] getSlotDefinitions() {
		return slotDefinitions;
	}
	public ActorPrologClause[] getActingClauses() {
		return actingClauses;
	}
	public ActorPrologClause[] getModelClauses() {
		return modelClauses;
	}
	public Term getClassSource() {
		return classSource;
	}
	public ActorPrologInterface getClassInterface() {
		return classInterface;
	}
	public String[] getVariableNames() {
		return variableNames;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[9];
		internalArray[0]= new PrologSymbol(classNameCode);
		internalArray[1]= getTermAncestorCode(ancestorNameCode);
		internalArray[2]= ActorPrologSlotDefinition.arrayToList(slotDefinitions);
		internalArray[3]= ActorPrologClause.arrayToList(actingClauses);
		internalArray[4]= ActorPrologClause.arrayToList(modelClauses);
		internalArray[5]= classSource;
		Term termInterface;
		if (classInterface != null) {
			termInterface= classInterface.toActorPrologTerm();
		} else {
			termInterface= ActorPrologInterface.getTermNoInterface();
		};
		internalArray[6]= termInterface;
		BalancedNameTreeNode tree= GeneralConverters.stringArrayToBalancedNameTree(variableNames);
		internalArray[7]= tree.toTerm();
		internalArray[8]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_class_definition,internalArray);
	}
}
