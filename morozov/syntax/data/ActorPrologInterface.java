// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data;

import target.*;

import morozov.system.converters.*;
import morozov.terms.*;

public class ActorPrologInterface extends ActorPrologUnit {
	//
	protected long[] ancestorNameCodes;
	protected boolean isMetaInterface;
	protected ActorPrologSlotDeclaration[] slotDeclarations;
	protected ActorPrologDomainDefinition[] domainDefinitions;
	protected ActorPrologPredicateDeclaration[] predicateDeclarations;
	//
	protected static Term termNoInterface= new PrologSymbol(SymbolCodes.symbolCode_E_no_interface);
	//
	public ActorPrologInterface(
			long cNC,
			long[] aNC,
			boolean iMI,
			ActorPrologSlotDeclaration[] sD,
			ActorPrologDomainDefinition[] dD,
			ActorPrologPredicateDeclaration[] pD,
			int p) {
		super(cNC,p);
		ancestorNameCodes= aNC;
		isMetaInterface= iMI;
		slotDeclarations= sD;
		domainDefinitions= dD;
		predicateDeclarations= pD;
	}
	//
	public long[] getAncestorNameCodes() {
		return ancestorNameCodes;
	}
	public boolean isMetaInterface() {
		return isMetaInterface;
	}
	public ActorPrologSlotDeclaration[] getSlotDeclarations() {
		return slotDeclarations;
	}
	public ActorPrologDomainDefinition[] getDomainDefinitions() {
		return domainDefinitions;
	}
	public ActorPrologPredicateDeclaration[] getPredicateDeclarations() {
		return predicateDeclarations;
	}
	//
	public static Term getTermNoInterface() {
		return termNoInterface;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[7];
		internalArray[0]= new PrologSymbol(classNameCode);
		internalArray[1]= GeneralConverters.codeArrayToSymbolList(ancestorNameCodes);
		internalArray[2]= YesNoConverters.boolean2TermYesNo(isMetaInterface);
		internalArray[3]= ActorPrologSlotDeclaration.arrayToList(slotDeclarations);
		internalArray[4]= ActorPrologDomainDefinition.arrayToList(domainDefinitions);
		internalArray[5]= ActorPrologPredicateDeclaration.arrayToList(predicateDeclarations);
		internalArray[6]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_class_interface,internalArray);
	}
}
