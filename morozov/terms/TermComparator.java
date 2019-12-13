// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.terms;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class TermComparator implements Comparator<Term> {
	//
	protected boolean ascendingSorting= true;
	//
	public TermComparator() {
	}
	public TermComparator(boolean mode) {
		ascendingSorting= mode;
	}
	//
	@Override
	public int compare(Term o1, Term o2) {
		if (!ascendingSorting) {
			Term o3= o1;
			o1= o2;
			o2= o3;
		};
		return o1.compare(o2);
	}
	public static boolean areEqualTermArrays(Term[] arguments1, Term[] arguments2) {
		if (arguments1.length==arguments2.length) {
			for (int n= 0; n < arguments1.length; n++) {
				if (!arguments1[n].equals(arguments2[n])) {
					return false;
				}
			};
			return true;
		} else {
			return false;
		}
	}
	public static int compareTwoTermArrays(Term[] arguments1, Term[] arguments2) {
		for (int n= 0; n < arguments1.length; n++) {
			if (n >= arguments2.length) {
				return 1;
			} else {
				int result= arguments1[n].compare(arguments2[n]);
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
	public static void insertNamesIntoTree(TreeSet<Long> nameList, HashMap<Long,Term> map) {
		Set<Long> set= map.keySet();
		Iterator<Long> iterator= set.iterator();
		while(iterator.hasNext()) {
			Long item= iterator.next();
			nameList.add(item);
		}
	}
	public static void insertNamesIntoTree(TreeSet<Long> nameList, HashSet<Long> set) {
		Iterator<Long> iterator= set.iterator();
		while(iterator.hasNext()) {
			Long item= iterator.next();
			nameList.add(item);
		}
	}
}
