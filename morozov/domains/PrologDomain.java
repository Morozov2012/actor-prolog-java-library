// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import target.*;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.terms.*;
import morozov.terms.signals.*;

import java.nio.charset.CharsetEncoder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class PrologDomain implements Serializable {
	//
	protected PrologDomainName name;
	protected DomainAlternative[] alternatives;
	//
	private static final long serialVersionUID= 0x49605D077377F8D3L; // 5287328249116358867L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","PrologDomain");
	// }
	//
	public PrologDomain(PrologDomainName n, DomainAlternative[] list) {
		name= n;
		alternatives= list;
	}
	//
	public void initiate() {
		for (int i= 0; i < alternatives.length; i++) {
			alternatives[i].initiate();
		}
	}
	//
	public boolean coversTerm(Term t, ChoisePoint cp, boolean ignoreFreeVariables) {
		for (int i= 0; i < alternatives.length; i++) {
			if (alternatives[i].coversTerm(t,cp,this,ignoreFreeVariables)) {
				return true;
			}
		};
		return false;
	}
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return false;
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp) throws DomainAlternativeDoesNotCoverTerm {
		long maximalPosition= -1;
		for (int i= 0; i < alternatives.length; i++) {
			try {
				return alternatives[i].checkAndOptimizeTerm(t,cp,this);
			} catch (DomainAlternativeDoesNotCoverTerm e) {
				long p= e.getPosition();
				if (maximalPosition < p) {
					maximalPosition= p;
				};
				continue;
			} catch (Throwable e) {
				continue;
			}
		};
		long p= t.getPosition();
		if (maximalPosition < p) {
			maximalPosition= p;
		};
		throw new DomainAlternativeDoesNotCoverTerm(maximalPosition);
	}
	public Term checkTerm(Term t, ChoisePoint cp) throws DomainAlternativeDoesNotCoverTerm {
		long maximalPosition= -1;
		for (int i= 0; i < alternatives.length; i++) {
			try {
				return alternatives[i].checkTerm(t,cp,this);
			} catch (DomainAlternativeDoesNotCoverTerm e) {
				long p= e.getPosition();
				if (maximalPosition < p) {
					maximalPosition= p;
				};
				continue;
			} catch (Throwable e) {
				continue;
			}
		};
		long p= t.getPosition();
		if (maximalPosition < p) {
			maximalPosition= p;
		};
		throw new DomainAlternativeDoesNotCoverTerm(maximalPosition);
	}
	public Term checkOptimizedSet(long[] keys, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		long maximalPosition= -1;
		for (int i= 0; i < alternatives.length; i++) {
			try {
				return alternatives[i].checkOptimizedSet(keys,elements,tail,initialValue,cp,baseDomain);
			} catch (DomainAlternativeDoesNotCoverTerm e) {
				long p= e.getPosition();
				if (maximalPosition < p) {
					maximalPosition= p;
				};
				continue;
			} catch (Throwable e) {
				continue;
			}
		};
		long p= initialValue.getPosition();
		if (maximalPosition < p) {
			maximalPosition= p;
		};
		throw new DomainAlternativeDoesNotCoverTerm(maximalPosition);
	}
	public PrologDomain getPairDomain(long key) throws UnknownPairName {
		for (int i= 0; i < alternatives.length; i++) {
			try {
				PrologDomain pairDomain= alternatives[i].getPairDomain(key);
				return pairDomain;
			} catch (TermIsNotPairDomainAlternative e) {
				continue;
			}
		};
		throw UnknownPairName.instance;
	}
	//
	public void collectLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
		if (alternatives.length > 0) {
			for (int n=0; n < alternatives.length; n++) {
				alternatives[n].collectLocalDomainTable(localDomainTable);
			}
		}
	}
	public void acceptLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
		if (alternatives.length > 0) {
			for (int n=0; n < alternatives.length; n++) {
				alternatives[n].acceptLocalDomainTable(localDomainTable);
			}
		}
	}
	//
	public void isEqualTo(PrologDomain domain, HashSet<PrologDomainPair> stack) throws PrologDomainsAreNotEqual {
		PrologDomainPair newPair= new PrologDomainPair(this,domain);
		if (!stack.add(newPair)) {
			return;
		} else {
			try {
				boolean[] checkList= new boolean[domain.alternatives.length];
				for (int n= 0; n < alternatives.length; n++) {
					checkList[domain.hasEqualAlternative(alternatives[n],stack)]= true;
				};
				for (int k= 0; k < domain.alternatives.length; k++) {
					if (!checkList[k]) {
						hasEqualAlternative(domain.alternatives[k],stack);
					}
				}
			} catch (PrologDomainsAreNotEqual e1) {
				try {
					for (int n= 0; n < alternatives.length; n++) {
						domain.coversAlternative(alternatives[n],this,stack);
					};
					for (int k= 0; k < domain.alternatives.length; k++) {
						coversAlternative(domain.alternatives[k],domain,stack);
					}
				} catch (PrologDomainsAreNotEqual e2) {
					stack.remove(newPair);
					throw e2;
				}
			}
		}
	}
	public int hasEqualAlternative(DomainAlternative a, HashSet<PrologDomainPair> stack) throws PrologDomainsAreNotEqual {
		for (int i= 0; i < alternatives.length; i++) {
			if (alternatives[i].isEqualTo(a,stack)) {
				return i;
			}
		};
		throw PrologDomainsAreNotEqual.instance;
	}
	public void coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack) throws PrologDomainsAreNotEqual {
		for (int i= 0; i < alternatives.length; i++) {
			if (alternatives[i].coversAlternative(a,ownerDomain,stack)) {
				return;
			}
		};
		throw PrologDomainsAreNotEqual.instance;
	}
	public void isCoveredByDomain(PrologDomain domain, HashSet<PrologDomainPair> stack) throws PrologDomainsAreNotEqual {
		for (int k= 0; k < domain.alternatives.length; k++) {
			domain.coversAlternative(alternatives[k],this,stack);
		}
	}
	//
	public static PrologDomain argumentToPrologDomain(Term value, ChoisePoint iX) throws TermIsNotPrologDomain {
		try {
			long functor= value.getStructureFunctor(iX);
			if (functor==SymbolCodes.symbolCode_E_domain) {
				Term[] arguments= value.getStructureArguments(iX);
				if (arguments.length==2) {
					PrologDomainName domainName= PrologDomainName.termToPrologDomainName(arguments[0],iX);
					ArrayList<DomainAlternative> alternatives= new ArrayList<>();
					Term nextHead;
					Term currentTail= arguments[1];
					try {
						while (true) {
							currentTail= currentTail.dereferenceValue(iX);
							nextHead= currentTail.getNextListHead(iX);
							alternatives.add(DomainAlternative.argumentToDomainAlternative(nextHead,iX));
							currentTail= currentTail.getNextListTail(iX);
						}
					} catch (EndOfList e) {
					} catch (TermIsNotAList e) {
						throw TermIsNotPrologDomain.instance;
					};
					DomainAlternative[] array= alternatives.toArray(new DomainAlternative[alternatives.size()]);
					return new PrologDomain(domainName,array);
				} else {
					throw TermIsNotPrologDomain.instance;
				}
			} else {
				throw TermIsNotPrologDomain.instance;
			}
		} catch (TermIsNotAStructure e1) {
			try {
				long code= value.getSymbolValue(iX);
				if (code==SymbolCodes.symbolCode_E_any) {
					return new PrologAnyDomain();
				} else {
					throw TermIsNotPrologDomain.instance;
				}
			} catch (TermIsNotASymbol e2) {
				throw TermIsNotPrologDomain.instance;
			}
		} catch (TermIsNotPrologDomainName e1) {
			throw TermIsNotPrologDomain.instance;
		} catch (TermIsNotDomainAlternative e1) {
			throw TermIsNotPrologDomain.instance;
		}
	}
	//
	public String toString(CharsetEncoder encoder) {
		StringBuilder buffer= new StringBuilder();
		buffer.append(PrologDomainName.tagDomainItem_Domain);
		buffer.append("(");
		buffer.append(name.toString(encoder));
		buffer.append(",[");
		if (alternatives.length > 0) {
			buffer.append(alternatives[0].toString(encoder));
			for (int n=1; n < alternatives.length; n++) {
				buffer.append(",");
				buffer.append(alternatives[n].toString(encoder));
			}
		};
		buffer.append("])");
		return buffer.toString();
	}
	//
	@Override
	public String toString() {
		return toString(null);
	}
}
