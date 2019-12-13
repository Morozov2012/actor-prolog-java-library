// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.domains;

import target.*;

import morozov.domains.signals.*;
import morozov.run.*;
import morozov.system.converters.*;
import morozov.terms.*;
import morozov.terms.signals.*;
import morozov.worlds.remote.*;

import java.nio.charset.CharsetEncoder;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.math.BigInteger;

public abstract class DomainAlternative implements Serializable {
	//
	private static final long serialVersionUID= 0xEEB29AF0B5BDCF5AL; // -1246763788196262054L
	//
	// static {
	//	SerialVersionChecker.check(serialVersionUID,"morozov.domains","DomainAlternative");
	// }
	//
	public void initiate() {
	}
	//
	public boolean coversTerm(Term t, ChoisePoint cp, PrologDomain baseDomain, boolean ignoreFreeVariables) {
		return false;
	}
	public boolean coversOptimizedSet(long[] keys, Term[] elements, Term tail, PrologDomain baseDomain, ChoisePoint cp, boolean ignoreFreeVariables) {
		return false;
	}
	public Term checkAndOptimizeTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (coversTerm(t,cp,baseDomain,false)) {
			return t.dereferenceValue(cp);
		} else {
			throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
		}
	}
	public Term checkTerm(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		if (coversTerm(t,cp,baseDomain,false)) {
			return t.dereferenceValue(cp);
		} else {
			throw new DomainAlternativeDoesNotCoverTerm(t.getPosition());
		}
	}
	public Term checkOptimizedSet(long[] keys, Term[] elements, Term tail, Term initialValue, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		throw new DomainAlternativeDoesNotCoverTerm(initialValue.getPosition());
	}
	protected static Term checkAndCollectSetElements(Term t, ChoisePoint cp, PrologDomain baseDomain) throws DomainAlternativeDoesNotCoverTerm {
		HashMap<Long,Term> setPositiveMap= new HashMap<>();
		Term setEnd= t.exploreSetPositiveElements(setPositiveMap,cp);
		setEnd= setEnd.dereferenceValue(cp);
		if (setEnd.thisIsEmptySet() || setEnd.thisIsUnknownValue()) {
			Set<Long> keys= setPositiveMap.keySet();
			Long[] functors= new Long[keys.size()];
			keys.toArray(functors);
			DomainAlternative[] alternatives= baseDomain.alternatives;
			Term result= PrologEmptySet.instance;
			for (int n=functors.length-1; n > 0; n--) {
				long currentKey= functors[n];
				Term currentElement= setPositiveMap.get(currentKey);
				boolean elementIsFound= false;
				for (int i=0; i < alternatives.length; i++) {
					DomainAlternative alternative= alternatives[i];
					try {
						PrologDomain currentDomain= alternative.getPairDomain(currentKey);
						currentElement= currentDomain.checkTerm(currentElement,cp);
						elementIsFound= true;
						break;
					} catch (TermIsNotPairDomainAlternative e) {
					}
				};
				if (!elementIsFound) {
					throw new DomainAlternativeDoesNotCoverTerm(currentElement.getPosition());
				};
				result= new PrologSet(currentKey,currentElement,result);
			};
			return result;
		} else {
			long position1= t.getPosition();
			long position2= setEnd.getPosition();
			if (position1 < position2) {
				position1= position2;
			};
			throw new DomainAlternativeDoesNotCoverTerm(position1);
		}
	}
	public PrologDomain getPairDomain(long key) throws TermIsNotPairDomainAlternative {
		throw TermIsNotPairDomainAlternative.instance;
	}
	//
	public void collectLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
	}
	public void acceptLocalDomainTable(HashMap<String,PrologDomain> localDomainTable) {
	}
	//
	abstract public boolean isEqualTo(DomainAlternative a, HashSet<PrologDomainPair> stack);
	public boolean isEqualToAny() {
		return false;
	}
	public boolean isEqualToAnySet() {
		return false;
	}
	public boolean isEqualToEmptySet() {
		return false;
	}
	public boolean isEqualToInteger() {
		return false;
	}
	public boolean isEqualToIntegerConstant(BigInteger value) {
		return false;
	}
	public boolean isEqualToIntegerRange(BigInteger value1, BigInteger value2) {
		return false;
	}
	public boolean isEqualToItem(PrologDomain domain, HashSet<PrologDomainPair> stack) {
		return false;
	}
	public boolean isEqualToList(PrologDomain domain, HashSet<PrologDomainPair> stack) {
		return false;
	}
	public boolean isEqualToOptimizedSet(long[] names, PrologDomain[] domains, HashSet<PrologDomainPair> stack) {
		return false;
	}
	public boolean isEqualToReal() {
		return false;
	}
	public boolean isEqualToRealConstant(double value) {
		return false;
	}
	public boolean isEqualToRealRange(double value1, double value2) {
		return false;
	}
	public boolean isEqualToSet(long name, PrologDomain domain, HashSet<PrologDomainPair> stack) {
		return false;
	}
	public boolean isEqualToString() {
		return false;
	}
	public boolean isEqualToBinary() {
		return false;
	}
	public boolean isEqualToStringConstant(String value) {
		return false;
	}
	public boolean isEqualToStructure(long name, PrologDomain[] domains, HashSet<PrologDomainPair> stack) {
		return false;
	}
	public boolean isEqualToSymbol() {
		return false;
	}
	public boolean isEqualToSymbolConstant(long value) {
		return false;
	}
	public boolean isEqualToUnknownValue() {
		return false;
	}
	public boolean isEqualToWorld(long value) {
		return false;
	}
	public boolean isEqualToForeignDomain(ExternalDomainInterface s) {
		return false;
	}
	abstract public boolean coversAlternative(DomainAlternative a, PrologDomain ownerDomain, HashSet<PrologDomainPair> stack);
	public boolean isCoveredBySetAny() {
		return false;
	}
	public boolean isCoveredByInteger() {
		return false;
	}
	public boolean isCoveredByIntegerRange(BigInteger value1, BigInteger value2) {
		return false;
	}
	public boolean isCoveredByOptimizedSet(long[] names, PrologDomain[] domains, HashSet<PrologDomainPair> stack) {
		return false;
	}
	public boolean isCoveredByReal() {
		return false;
	}
	public boolean isCoveredByRealRange(double value1, double value2) {
		return false;
	}
	public boolean isCoveredByString() {
		return false;
	}
	public boolean isCoveredByBinary() {
		return false;
	}
	public boolean isCoveredBySymbol() {
		return false;
	}
	public boolean isCoveredBySet(PrologDomain ownerDomain) {
		return false;
	}
	//
	public static DomainAlternative argumentToDomainAlternative(Term value, ChoisePoint iX) throws TermIsNotDomainAlternative {
		try {
			long code= value.getSymbolValue(iX);
			if (code==SymbolCodes.symbolCode_E_integer) {
				return new DomainInteger();
			} else if (code==SymbolCodes.symbolCode_E_real) {
				return new DomainReal();
			} else if (code==SymbolCodes.symbolCode_E_symbol) {
				return new DomainSymbol();
			} else if (code==SymbolCodes.symbolCode_E_string) {
				return new DomainString();
			} else if (code==SymbolCodes.symbolCode_E_binary) {
				return new DomainBinary();
			} else if (code==SymbolCodes.symbolCode_E_empty_set) {
				return new DomainEmptySet();
			} else if (code==SymbolCodes.symbolCode_E_any_set) {
				return new DomainAnySet();
			} else if (code==SymbolCodes.symbolCode_E_any) {
				return new DomainAny();
			} else {
				throw TermIsNotDomainAlternative.instance;
			}
		} catch (TermIsNotASymbol e1) {
			try {
				long functor= value.getStructureFunctor(iX);
				Term[] arguments= value.getStructureArguments(iX);
				if (arguments.length==1) {
					if (functor==SymbolCodes.symbolCode_E_integer_constant) {
						BigInteger number= arguments[0].getIntegerValue(iX);
						return new DomainIntegerConstant(number);
					} else if (functor==SymbolCodes.symbolCode_E_real_constant) {
						double number= arguments[0].getRealValue(iX);
						return new DomainRealConstant(number);
					} else if (functor==SymbolCodes.symbolCode_E_symbol_constant) {
						long code= arguments[0].getSymbolValue(iX);
						return new DomainSymbolConstant(code);
					} else if (functor==SymbolCodes.symbolCode_E_string_constant) {
						String text= arguments[0].getStringValue(iX);
						return new DomainStringConstant(text);
					} else if (functor==SymbolCodes.symbolCode_E_world) {
						long code= arguments[0].getSymbolValue(iX);
						return new DomainWorld(code);
					} else if (functor==SymbolCodes.symbolCode_E_list) {
						String text= arguments[0].getStringValue(iX);
						return new DomainList(text);
					} else if (functor==SymbolCodes.symbolCode_E_item) {
						String text= arguments[0].getStringValue(iX);
						return new DomainItem(text);
					} else {
						throw TermIsNotDomainAlternative.instance;
					}
				} else if (arguments.length==2) {
					if (functor==SymbolCodes.symbolCode_E_integer_range) {
						BigInteger leftBound= arguments[0].getIntegerValue(iX);
						BigInteger rightBound= arguments[1].getIntegerValue(iX);
						return new DomainIntegerRange(leftBound,rightBound);
					} else if (functor==SymbolCodes.symbolCode_E_real_range) {
						double leftBound= arguments[0].getRealValue(iX);
						double rightBound= arguments[1].getRealValue(iX);
						return new DomainRealRange(leftBound,rightBound);
					} else if (functor==SymbolCodes.symbolCode_E_structure) {
						long code= arguments[0].getSymbolValue(iX);
						String[] domainNames= GeneralConverters.termToStrings(arguments[1],iX);
						return new DomainStructure(code,domainNames);
					} else if (functor==SymbolCodes.symbolCode_E_set) {
						long key= arguments[0].getLongIntegerValue(iX);
						String domainName= arguments[1].getStringValue(iX);
						return new DomainSet(key,domainName);
					} else if (functor==SymbolCodes.symbolCode_E_optimized_set) {
						long[] keys= GeneralConverters.argumentToLongIntegers(arguments[0],iX);
						String[] domainNames= GeneralConverters.termToStrings(arguments[1],iX);
						return new DomainOptimizedSet(keys,domainNames);
					} else {
						throw TermIsNotDomainAlternative.instance;
					}
				} else {
					throw TermIsNotDomainAlternative.instance;
				}
			} catch (TermIsNotAStructure e2) {
				try {
					value.isUnknownValue(iX);
					return new DomainUnknownValue();
				} catch (Backtracking e3) {
					throw TermIsNotDomainAlternative.instance;
				}
			} catch (TermIsNotAnInteger e2) {
				throw TermIsNotDomainAlternative.instance;
			} catch (TermIsNotAReal e2) {
				throw TermIsNotDomainAlternative.instance;
			} catch (TermIsNotASymbol e2) {
				throw TermIsNotDomainAlternative.instance;
			} catch (TermIsNotAString e2) {
				throw TermIsNotDomainAlternative.instance;
			}
		}
	}
	//
	abstract public String toString(CharsetEncoder encoder);
}
