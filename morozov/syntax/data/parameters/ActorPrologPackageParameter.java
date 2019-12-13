// (c) 2019 IRE RAS Alexei A. Morozov

package morozov.syntax.data.parameters;

import morozov.run.*;
import morozov.system.converters.errors.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.util.ArrayList;

abstract public class ActorPrologPackageParameter {
	//
	protected int position;
	//
	public ActorPrologPackageParameter(int p) {
		position= p;
	}
	//
	public int getPosition() {
		return position;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static Term arrayToList(ActorPrologPackageParameter[] array) {
		Term result= PrologEmptyList.instance;
		for (int k=array.length-1; k >= 0; k--) {
			result= new PrologList(array[k].toActorPrologTerm(),result);
		};
		return result;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public static ActorPrologPackageParameter[] argumentToActorPrologPackageParameters(Term argumentList, ChoisePoint iX) {
		ArrayList<ActorPrologPackageParameter> argumentArray= new ArrayList<>();
		try {
			while (true) {
				Term head= argumentList.getNextListHead(iX);
				ActorPrologPackageParameter argument= argumentToActorPrologPackageParameter(head,iX);
				argumentArray.add(argument);
				argumentList= argumentList.getNextListTail(iX);
			}
		} catch (EndOfList e1) {
			return argumentArray.toArray(new ActorPrologPackageParameter[argumentArray.size()]);
		} catch (TermIsNotAList e2) {
			throw new WrongArgumentIsNotAList(argumentList);
		}
	}
	//
	public static ActorPrologPackageParameter argumentToActorPrologPackageParameter(Term value, ChoisePoint iX) {
		return null;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	public abstract Term toActorPrologTerm();
}
