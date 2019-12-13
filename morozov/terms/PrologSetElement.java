// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.run.*;
import morozov.terms.signals.*;
import morozov.worlds.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;

public class PrologSetElement extends Term {
	//
	protected Term element;
	//
	private static final long serialVersionUID= 0x3125341A24F90E4BL; // 3541293968890859083L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.terms","PrologSetElement");
	// }
	//
	///////////////////////////////////////////////////////////////
	//
	public PrologSetElement(Term aElement) {
		element= aElement;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public int hashCode() {
		return element.hashCode();
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public Term retrieveSetElementValue(ChoisePoint cp) throws Backtracking, TermIsNotSetElement {
		return element;
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void unifyWithSetElement(Term aElement, Term setElement, ChoisePoint cp) throws Backtracking {
		element.unifyWith(aElement,cp);
	}
	@Override
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithSetElement(element,this,cp);
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		element.registerVariables(process,isSuspending,isProtecting);
	}
	@Override
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		element.registerTargetWorlds(worlds,cp);
	}
	@Override
	public PrologSetElement circumscribe() {
		return new PrologSetElement(element.circumscribe());
	}
	@Override
	public PrologSetElement copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		return new PrologSetElement(element.copyValue(cp,mode));
	}
	@Override
	public PrologSetElement copyGroundValue(ChoisePoint cp) throws TermIsUnboundVariable {
		return new PrologSetElement(element.copyGroundValue(cp));
	}
	@Override
	public PrologSetElement substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		return new PrologSetElement(element.substituteWorlds(map,cp));
	}
	//
	///////////////////////////////////////////////////////////////
	//
	@Override
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, boolean encodeWorlds, CharsetEncoder encoder) {
		return element.toString(cp,true,provideStrictSyntax,encodeWorlds,encoder);
	}
}
