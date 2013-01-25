// (c) 2008 IRE RAS Alexei A. Morozov

package morozov.terms;

import java.nio.charset.CharsetEncoder;

public class PrologEmptyList extends Term {
	public void isEmptyList(ChoisePoint cp) throws Backtracking {
	}
	public boolean thisIsEmptyList() {
		return true;
	}
	public void isOutputEmptyList(ChoisePoint cp) {
	}
	public void unifyWith(Term t, ChoisePoint cp) throws Backtracking {
		t.isEmptyList(cp);
	}
	public Term getNextListHead(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw new EndOfList();
	}
	public Term getNextListTail(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw new EndOfList();
	}
	public Term getNextListHeadSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw new EndOfList();
	}
	public Term getNextListTailSafely(ChoisePoint cp) throws EndOfList, TermIsNotAList {
		throw new EndOfList();
	}
	// Converting Term to String
	// public String toString() {
	public String toString(ChoisePoint cp, boolean isInner, boolean provideStrictSyntax, CharsetEncoder encoder) {
		return "[]";
	}
}
