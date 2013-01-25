// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.classes.*;

import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.HashSet;

public class PrologList extends Term {
	private Term head;
	public Term tail;
	public PrologList(Term aHead, Term aTail) {
		head= aHead;
		tail= aTail;
	}
	public int hashCode() {
		return head.hashCode() + tail.hashCode();
	}
	public Term getListHead(ChoisePoint cp) throws Backtracking {
		return head;
	}
	public Term getListTail(ChoisePoint cp) throws Backtracking {
		return tail;
	}
	public Term getNextListHead(ChoisePoint cp) throws EndOfList {
		return head;
	}
	public Term getNextListTail(ChoisePoint cp) throws EndOfList {
		return tail;
	}
	public Term getExistentHead() {
		return head;
	}
	public Term getExistentTail() {
		return tail;
	}
	public Term getOutputHead(ChoisePoint cp) {
		return head;
	}
	public Term getOutputTail(ChoisePoint cp) {
		return tail;
	}
	public Term getNextListHeadSafely(ChoisePoint cp) throws EndOfList {
		return head;
	}
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList {
		return tail;
	}
	// "Unify with ..." functions
	public void unifyWithList(Term aHead, Term aTail, Term aList, ChoisePoint cp) throws Backtracking {
		head.unifyWith(aHead,cp);
		tail.unifyWith(aTail,cp);
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.unifyWithList(head,tail,this,cp);
	}
	public void registerVariables(ActiveWorld process, boolean isSuspending, boolean isProtecting) {
		head.registerVariables(process,isSuspending,isProtecting);
		tail.registerVariables(process,isSuspending,isProtecting);
	}
	public void registerTargetWorlds(HashSet<AbstractWorld> worlds, ChoisePoint cp) {
		head.registerTargetWorlds(worlds,cp);
		tail.registerTargetWorlds(worlds,cp);
	}
	public PrologList circumscribe() {
		return new PrologList(
			head.circumscribe(),
			tail.circumscribe());
	}
	public PrologList copyValue(ChoisePoint cp, TermCircumscribingMode mode) {
		return new PrologList(
			head.copyValue(cp,mode),
			tail.copyValue(cp,mode));
	}
	public PrologList substituteWorlds(HashMap<AbstractWorld,Term> map, ChoisePoint cp) {
		return new PrologList(
			head.substituteWorlds(map,cp),
			tail.substituteWorlds(map,cp));
	}
	// Converting Term to String
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		StringBuilder buffer= new StringBuilder("[");
		buffer.append(head.toString(cp,true,provideStrictSyntax,encoder));
		try {
			Term nextHead;
			Term currentTail= tail;
			try {
				while (true) {
					nextHead= currentTail.getNextListHeadSafely(cp);
					buffer.append(",");
					buffer.append(nextHead.toString(cp,true,provideStrictSyntax,encoder));
					currentTail= currentTail.getNextListTailSafely(cp);
				}
			} catch (EndOfList e) {
				buffer.append("]");
				return buffer.toString();
			} catch (TermIsNotAList e) {
				buffer.append("|");
				buffer.append(currentTail.toString(cp,true,provideStrictSyntax,encoder));
				buffer.append("]");
				return buffer.toString();
			}
		} catch (Throwable e) {
			buffer= new StringBuilder();
			return String.format("[%s|%s]",
				head.toString(cp,true,provideStrictSyntax,encoder),
				tail.toString(cp,true,provideStrictSyntax,encoder));
		}
	}
}
