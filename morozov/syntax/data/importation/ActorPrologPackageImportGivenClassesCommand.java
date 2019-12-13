// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.importation;

import target.*;

import morozov.syntax.data.parameters.*;
import morozov.terms.*;

public class ActorPrologPackageImportGivenClassesCommand extends ActorPrologPackageImportCommand {
	//
	protected ActorPrologClassImportCommand[] classImportList;
	//
	public ActorPrologPackageImportGivenClassesCommand(ActorPrologClassImportCommand[] pairs, String name, ActorPrologPackageParameter[] parameters, int p) {
		super(name,parameters,p);
		classImportList= pairs;
	}
	//
	public Term classImportListToList() {
		Term result= PrologEmptyList.instance;
		for (int k=classImportList.length-1; k >= 0; k--) {
			result= new PrologList(classImportList[k].toActorPrologTerm(),result);
		};
		return result;
	}
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[4];
		internalArray[0]= new PrologString(packageName);
		internalArray[1]= packageParametersToList();
		internalArray[2]= classImportListToList();
		internalArray[3]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_import_given_classes_and_domains,internalArray);
	}
}
