// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import target.*;

import morozov.classes.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;

public class PrologStructure extends Term {
	private long functor;
	private Term[] arguments;
	public PrologStructure(long aFunctor, Term[] aContents) {
		functor= aFunctor;
		arguments= aContents;
	}
	public int hashCode() {
		int sum= (int)functor;
		for (int i= 0; i < arguments.length; i++) {
			sum+= arguments[i].hashCode();
		};
		return sum;
	}
	public Term[] isStructure(long aFunctor, int aArity, ChoisePoint cp) throws Backtracking {
		if (aFunctor==functor && aArity==arguments.length)
			return arguments;
		throw new Backtracking();
	}
	public long getStructureFunctor(ChoisePoint cp) throws TermIsNotAStructure {
		return functor;
	}
	public Term[] getStructureArguments(ChoisePoint cp) throws TermIsNotAStructure {
		return arguments;
	}
	public void unifyWithStructure(long aFunctor, Term[] values, Term structure, ChoisePoint cp) throws Backtracking {
		if (aFunctor==functor && values.length==arguments.length) {
			for (int i= 0; i < arguments.length; i++) {
				arguments[i].unifyWith(values[i],cp);
			}
		} else {
			throw new Backtracking();
		}
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithStructure(functor,arguments,this,cp);
	}
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		for (int i= 0; i < arguments.length; i++) {
			arguments[i].registerVariables(process,isSuspending,isProtecting);
		}
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		for (int i= 0; i < arguments.length; i++) {
			arguments[i].registerTargetWorlds(worlds,cp);
		}
	}
	public PrologStructure circumscribe() {
		Term[] aContents= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			aContents[i]= arguments[i].circumscribe();
		};
		return new PrologStructure(functor,aContents);
	}
	public PrologStructure copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		Term[] aContents= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			aContents[i]= arguments[i].copyValue(cp,mode);
		};
		return new PrologStructure(functor,aContents);
	}
	public PrologStructure substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		Term[] aContents= new Term[arguments.length];
		for (int i= 0; i < arguments.length; i++) {
			aContents[i]= arguments[i].substituteWorlds(map,cp);
		};
		return new PrologStructure(functor,aContents);
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		StringBuilder buffer= new StringBuilder();
		buffer.append(SymbolNames.retrieveSymbolName(functor).toString(encoder));
		buffer.append("(");
		boolean isFirst= true;
		for (int i= 0; i < arguments.length; i++) {
			if (!isFirst) {
				buffer.append(",");
			};
			buffer.append(arguments[i].toString(cp,true,provideStrictSyntax,encoder));
			isFirst= false;
		};
		buffer.append(")");
		return buffer.toString();
	}
}
