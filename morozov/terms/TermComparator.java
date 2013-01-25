// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.terms;

import morozov.classes.*;
import morozov.system.*;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class TermComparator implements Comparator<Term> {
	boolean ascendingSorting= true;
	public TermComparator() {
	}
	public TermComparator(boolean mode) {
		ascendingSorting= mode;
	}
	public int compare(Term o1, Term o2) {
		// System.out.printf("TermComparator:: %s vs %s = %s\n",o1,o2,compareTwoTerms(o1,o2));
		if (!ascendingSorting) {
			Term o3= o1;
			o1= o2;
			o2= o3;
		};
		return compareTwoTerms(o1,o2);
	}
	public static int compareTwoTerms(Term o1, Term o2) {
		if (o1.thisIsSlotVariable() || o2.thisIsSlotVariable()) {
			if (o1==o2) {
				return 0;
			} else {
				Class class1= o1.getClass();
				Class class2= o2.getClass();
				int c1= class1.hashCode();
				int c2= class2.hashCode();
				// System.out.printf("TermComparator:compare_integers:%s\n",compare_integers(c1,c2));
				int result= compare_integers(c1,c2);
				if (result != 0) {
					return result;
				} else {
					// Different Slot Variables are not
					// equal objects. Do not use this method
					// for Slot Variables!
					throw new SlotVariableCannotBeCompared();
				}
			}
		};
		ChoisePoint cp= null;
		o1= o1.dereferenceValue(cp);
		o2= o2.dereferenceValue(cp);
		if (o1.thisIsFreeVariable() || o2.thisIsFreeVariable()) {
			// System.out.printf("TermComparator:FREE:%s vs %s\n",o1,o2);
			if (o1==o2) {
				return 0;
			} else {
				Class class1= o1.getClass();
				Class class2= o2.getClass();
				int c1= class1.hashCode();
				int c2= class2.hashCode();
				// System.out.printf("TermComparator:compare_integers:%s\n",compare_integers(c1,c2));
				int result= compare_integers(c1,c2);
				if (result != 0) {
					// System.out.printf("TermComparator:FREE:result=:%s\n",result);
					return result;
				} else {
					// Different Variables are not
					// equal objects. Do not use this method
					// for Free Variables!
					throw new FreeVariableCannotBeCompared();
				}
			}
		};
		try {
			BigInteger n1= o1.getIntegerValue(cp);
			BigInteger n2= o2.getIntegerValue(cp);
			// return compare_integers(n1,n2);
			return n1.compareTo(n2);
		} catch (TermIsNotAnInteger e1) {
		try {
			double n1= o1.getRealValue(cp);
			double n2= o2.getRealValue(cp);
			return compare_reals(n1,n2);
		} catch (TermIsNotAReal e2) {
		try {
			long n1= o1.getSymbolValue(cp);
			long n2= o2.getSymbolValue(cp);
			return compare_integers(n1,n2);
		} catch (TermIsNotASymbol e3) {
		try {
			String s1= o1.getStringValue(cp);
			String s2= o2.getStringValue(cp);
			return s1.compareTo(s2);
		} catch (TermIsNotAString e4) {
		try {
			o1.isEmptyList(cp);
			o2.isEmptyList(cp);
			return 0;
		} catch (Backtracking e5) {
		try {
			o1.isEmptySet(cp);
			o2.isEmptySet(cp);
			return 0;
		} catch (Backtracking e6) {
		try {
			o1.isUnknownValue(cp);
			o2.isUnknownValue(cp);
			return 0;
		} catch (Backtracking e7) {
		try {
			o1.isNoValue(cp);
			o2.isNoValue(cp);
			return 0;
		} catch (Backtracking e8) {
		try {
			Term t1= o1;
			Term t2= o2;
			while (true) {
				Term h1= t1.getListHead(cp);
				Term h2= t2.getListHead(cp);
				int result= compareTwoTerms(h1,h2);
				if (result==0) {
					try {
						t1= h1.getNextListTail(cp);
					} catch (EndOfList eol1) {
						try {
							t2= h2.getNextListTail(cp);
							return -1;
						} catch (EndOfList eol2) {
							return 0;
						} catch (TermIsNotAList tnl2) {
							return compareTwoTerms(h1,h2);
						}
					} catch (TermIsNotAList tnl1) {
						return compareTwoTerms(h1,h2);
					};
					try {
						t2= h2.getNextListTail(cp);
						continue;
					} catch (EndOfList eol3) {
						return 1;
					} catch (TermIsNotAList tnl3) {
						return compareTwoTerms(h1,h2);
					}
				} else {
					return result;
				}
			}
		} catch (Backtracking e9) {
		try {
			long f1= o1.getStructureFunctor(cp);
			long f2= o2.getStructureFunctor(cp);
			if (f1==f2) {
				Term[] arguments1= o1.getStructureArguments(cp);
				Term[] arguments2= o2.getStructureArguments(cp);
				return compareTwoTermArrays(arguments1,arguments2);
			} else {
				return compare_integers(f1,f2);
			}
		} catch (TermIsNotAStructure e10) {
		// try {
		if (o1.thisIsUnderdeterminedSet() && o2.thisIsUnderdeterminedSet()) {
			HashMap<Long,Term> leftSetPositiveMap= new HashMap<Long,Term>();
			HashSet<Long> leftSetNegativeMap= new HashSet<Long>();
			Term leftSetEnd= o1.exploreSet(leftSetPositiveMap,leftSetNegativeMap,cp);
			leftSetEnd= leftSetEnd.dereferenceValue(cp);
			if (!leftSetEnd.thisIsEmptySet() && !leftSetEnd.thisIsUnknownValue()) {
				throw new WrongTermIsNotBoundVariable(o1);
			};
			HashMap<Long,Term> rightSetPositiveMap= new HashMap<Long,Term>();
			HashSet<Long> rightSetNegativeMap= new HashSet<Long>();
			Term rightSetEnd= o2.exploreSet(rightSetPositiveMap,rightSetNegativeMap,cp);
			rightSetEnd= rightSetEnd.dereferenceValue(cp);
			if (!rightSetEnd.thisIsEmptySet() && !rightSetEnd.thisIsUnknownValue()) {
				throw new WrongTermIsNotBoundVariable(o2);
			};
			TreeSet<Long> nameList= new TreeSet<Long>();
			insertNamesIntoTree(nameList,leftSetPositiveMap);
			insertNamesIntoTree(nameList,leftSetNegativeMap);
			insertNamesIntoTree(nameList,rightSetPositiveMap);
			insertNamesIntoTree(nameList,rightSetNegativeMap);
			Iterator<Long> iterator= nameList.iterator();
			// System.out.printf("START\n");
			while(iterator.hasNext()) {
				Long name= iterator.next();
				Term leftValue= leftSetPositiveMap.get(name);
				Term rightValue= rightSetPositiveMap.get(name);
				if (leftValue!=null && rightValue!=null) {
					int result= compareTwoTerms(leftValue,rightValue);
					if (result==0) {
						continue;
					} else {
						return result;
					}
				} else if (leftValue==null && rightValue==null) {
					continue;
				} else if (leftValue==null && rightValue!=null) {
					return -1;
				} else if (leftValue!=null && rightValue==null) {
					return 1;
				}
			}
			return 0;
		// } catch (TermIsNotASet e11) {
		} else if (o1.thisIsProcess() && o2.thisIsProcess()) {
			int c1= o1.hashCode();
			int c2= o2.hashCode();
			return compare_integers(c1,c2);
		} else if (o1.thisIsProcess() && o2.thisIsWorld()) {
			Term p2= ((AbstractWorld)o2).currentProcess;
			int pc1= o1.hashCode();
			int pc2= p2.hashCode();
			if (pc1==pc2) {
				Term w1= ((AbstractProcess)o1).getMainWorld();
				if (w1==null) { // Impossible!
					return -1;
				};
				int wc1= w1.hashCode();
				int wc2= o2.hashCode();
				return compare_integers(wc1,wc2);
			} else {
				return compare_integers(pc1,pc2);
			}
		} else if (o1.thisIsWorld() && o2.thisIsProcess()) {
			Term p1= ((AbstractWorld)o1).currentProcess;
			int pc1= p1.hashCode();
			int pc2= o2.hashCode();
			if (pc1==pc2) {
				Term w2= ((AbstractProcess)o2).getMainWorld();
				if (w2==null) { // Impossible!
					return 1;
				};
				int wc1= o1.hashCode();
				int wc2= w2.hashCode();
				return compare_integers(wc1,wc2);
			} else {
				return compare_integers(pc1,pc2);
			}
		} else if (o1.thisIsWorld() && o2.thisIsWorld()) {
			Term p1= ((AbstractWorld)o1).currentProcess;
			Term p2= ((AbstractWorld)o2).currentProcess;
			// System.out.printf("TermComparatoe:p1=%s; o1=%s\n",p1,o1);
			// System.out.printf("TermComparatoe:p2=%s; o2=%s\n",p2,o2);
			int pc1= p1.hashCode();
			int pc2= p2.hashCode();
			if (pc1==pc2) {
				int wc1= o1.hashCode();
				int wc2= o2.hashCode();
				return compare_integers(wc1,wc2);
			} else {
				return compare_integers(pc1,pc2);
			}
		} else { // Objects are of different types
			Class class1= o1.getClass();
			Class class2= o2.getClass();
			int c1= class1.hashCode();
			int c2= class2.hashCode();
			return compare_integers(c1,c2);
		}}}}}}}}}}}
		// }
	}
	public static int compareTwoTermArrays(Term[] arguments1, Term[] arguments2) {
		for (int n= 0; n < arguments1.length; n++) {
			if (n >= arguments2.length) {
				return 1;
			} else {
				int result= compareTwoTerms(arguments1[n],arguments2[n]);
				if (result==0) {
					continue;
				} else {
					return result;
				}
			}
		};
		if (arguments1.length < arguments2.length) {
			return -1;
		} else {
			return 0;
		}
	}
	public static int compare_integers(long n1, long n2) {
		if (n1 == n2) {
			return 0;
		} else if (n1 < n2) {
			return -1;
		} else {
			return 1;
		}
	}
	public static int compare_reals(double n1, double n2) {
		if (Arithmetic.realsAreEqual(n1,n2)) {
			return 0;
		} else if (n1 < n2) {
			return -1;
		} else {
			return 1;
		}
	}
	public static void insertNamesIntoTree(TreeSet<Long> nameList, HashMap<Long,Term> map) {
		Set<Long> set= map.keySet();
		Iterator<Long> iterator= set.iterator();
		while(iterator.hasNext()) {
			Long item= iterator.next();
			nameList.add(item);
		}
	}
	public static void insertNamesIntoTree(TreeSet<Long> nameList, HashSet<Long> set) {
		// Set<Long> set= map.keySet();
		Iterator<Long> iterator= set.iterator();
		while(iterator.hasNext()) {
			Long item= iterator.next();
			nameList.add(item);
		}
	}
}
