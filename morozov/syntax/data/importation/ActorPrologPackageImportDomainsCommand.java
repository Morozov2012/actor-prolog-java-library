// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.importation;

import target.*;

import morozov.syntax.data.parameters.*;
import morozov.terms.*;

public class ActorPrologPackageImportDomainsCommand extends ActorPrologPackageImportCommand {
	//
	public ActorPrologPackageImportDomainsCommand(String name, ActorPrologPackageParameter[] parameters, int p) {
		super(name,parameters,p);
	}
	//
	@Override
	public Term toActorPrologTerm() {
		Term[] internalArray= new Term[3];
		internalArray[0]= new PrologString(packageName);
		internalArray[1]= packageParametersToList();
		internalArray[2]= new PrologInteger(position);
		return new PrologStructure(SymbolCodes.symbolCode_E_import_domains_only,internalArray);
	}
}
