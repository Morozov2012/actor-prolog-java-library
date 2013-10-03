// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.domains.*;
import morozov.domains.errors.*;
import morozov.run.*;
import morozov.system.errors.*;
import morozov.terms.*;
import morozov.terms.errors.*;
import morozov.terms.signals.*;

import java.math.BigInteger;

public class Standard {
	public static void errorExit(ChoisePoint cp, Term value) {
		value= value.dereferenceValue(cp);
		if (value.thisIsFreeVariable()) {
			throw new UndefinedErrorExit(cp);
		} else {
			try {
				long code= value.getSymbolValue(cp);
				throw new NamedErrorExit(cp,code);
			} catch (TermIsNotASymbol e1) {
				try {
					BigInteger number= value.getIntegerValue(cp);
					if (number.compareTo(BigInteger.ZERO) >= 0) {
						throw new CodedErrorExit(cp,number);
					} else {
						throw new UndefinedErrorExit(cp);
					}
				} catch (TermIsNotAnInteger e2) {
					throw new UndefinedErrorExit(cp);
				}
			}
		}
	}
	//
	public static void errorExit(ChoisePoint cp) {
		throw new CodedErrorExit(cp,BigInteger.ZERO);
	}
	//
	public static void unifyAsteriskWithTerm(ChoisePoint cp, Term item, Term list) throws Backtracking {
		Term nextHead;
		Term currentTail= list;
		try {
			while (true) {
				nextHead= currentTail.getNextListHead(cp);
				item.unifyWith(nextHead,cp);
				currentTail= currentTail.getNextListTail(cp);
			}
		} catch (EndOfList e) {
			return;
		} catch (TermIsNotAList e) {
			throw new WrongTermIsNotArgumentList(currentTail);
		}
	}
	//
	public static void unifyAsterisk(ChoisePoint cp, Term list) throws Backtracking {
		Term firstHead= null;
		Term nextHead;
		Term currentTail= list;
		try {
			while (true) {
				if (firstHead==null) {
					firstHead= currentTail.getNextListHead(cp);
				} else {
					nextHead= currentTail.getNextListHead(cp);
					firstHead.unifyWith(nextHead,cp);
				};
				currentTail= currentTail.getNextListTail(cp);
			}
		} catch (EndOfList e) {
			return;
		} catch (TermIsNotAList e) {
			throw new WrongTermIsNotArgumentList(currentTail);
		}
	}
	//
	public static void getbacktrack(ChoisePoint iX, PrologVariable result) {
		long n= iX.getChoisePointNumber();
		result.value= new PrologInteger(n);
	}
	//
	public static void cutbacktrack(ChoisePoint iX, Term n1) {
		try {
			long n= n1.getLongIntegerValue(iX);
			iX.disable(n);
		} catch (TermIsNotAnInteger e1) {
			throw new WrongArgumentIsNotAnInteger(n1);
		}
	}
	//
	public static void checkDomain(ChoisePoint cp, Term domain, Term value) {
		try {
			String entry= domain.getStringValue(cp);
			value= value.dereferenceValue(cp);
			if (value.thisIsFreeVariable()) {
				throw new WrongTermIsNotBoundVariable(value);
			} else {
				// System.out.printf("entry: %s\n",entry);
				PrologDomain domainItem= DomainTable.getDomainAlternatives(entry);
				// System.out.printf("Standard::domainItem: %s\n",domainItem);
				// System.out.printf("Standard::value: %s (%s)\n\n",value,value.getClass());
				if (!value.isCoveredByDomain(domainItem,cp,false)) {
					throw new WrongTermDoesNotBelongToDomain(value);
				}
			}
		} catch (TermIsNotAString e) {
			throw new WrongTermIsNotDomainTableEntry(domain);
		}
	}
	//
	public static void safelyCheckDomain(ChoisePoint cp, Term domain, Term value) throws Backtracking {
		try {
			String entry= domain.getStringValue(cp);
			value= value.dereferenceValue(cp);
			if (value.thisIsFreeVariable()) {
				throw Backtracking.instance;
			} else {
				PrologDomain domainItem= DomainTable.getDomainAlternatives(entry);
				// System.out.printf("Standard:domainItem: %s %s\n",value,value.getClass());
				if (!domainItem.coversTerm(value,cp,false)) {
					throw Backtracking.instance;
				}
			}
		} catch (TermIsNotAString e) {
			throw new WrongTermIsNotDomainTableEntry(domain);
		}
	}
	//
	public static void noOperation(ChoisePoint iX, Term... args) {
	}
}