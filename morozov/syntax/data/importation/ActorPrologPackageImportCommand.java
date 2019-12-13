// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.importation;

import morozov.syntax.data.parameters.*;
import morozov.terms.*;

abstract public class ActorPrologPackageImportCommand {
	//
	protected String packageName;
	protected ActorPrologPackageParameter[] packageParameters;
	protected int position;
	//
	public ActorPrologPackageImportCommand(String name, ActorPrologPackageParameter[] parameters, int p) {
		packageName= name;
		packageParameters= parameters;
		position= p;
	}
	//
	public static Term arrayToList(ActorPrologPackageImportCommand[] array) {
		Term result= PrologEmptyList.instance;
		for (int k=array.length-1; k >= 0; k--) {
			result= new PrologList(array[k].toActorPrologTerm(),result);
		};
		return result;
	}
	//
	public Term packageParametersToList() {
		Term result= PrologEmptyList.instance;
		for (int k=packageParameters.length-1; k >= 0; k--) {
			result= new PrologList(packageParameters[k].toActorPrologTerm(),result);
		};
		return result;
	}
	//
	public abstract Term toActorPrologTerm();
}
