// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.classes.*;
import morozov.run.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;

public class PrologSetElement extends Term {
	private Term element;
	public PrologSetElement(Term aElement) {
		element= aElement;
	}
	public int hashCode() {
		return element.hashCode();
	}
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		return element;
	}
	// "Unify with ..." functions
	public void unifyWithSetElement(Term aElement, Term setElement, ChoisePoint cp) throws Backtracking {
		element.unifyWith(aElement,cp);
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithSetElement(element,this,cp);
	}
	// Special functions
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		element.registerVariables(process,isSuspending,isProtecting);
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		element.registerTargetWorlds(worlds,cp);
	}
	public PrologSetElement circumscribe() {
		return new PrologSetElement(element.circumscribe());
	}
	public PrologSetElement copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		return new PrologSetElement(element.copyValue(cp,mode));
	}
	public PrologSetElement copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		return new PrologSetElement(element.copyGroundValue(cp));
	}
	public PrologSetElement substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		return new PrologSetElement(element.substituteWorlds(map,cp));
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		// return "<" + element.toString(cp,true,provideStrictSyntax,encoder) + ">";
		return element.toString(cp,true,provideStrictSyntax,encoder);
	}
}
