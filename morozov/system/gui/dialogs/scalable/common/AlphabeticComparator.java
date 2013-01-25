/*
 * @(#)AlphabeticComparator.java 1.0 2011/04/23
 *
 * Copyright 2011 IRE RAS Alexei A. Morozov
 * Thanks to Bruce Eckel Thinking in Java 3rd edition.
*/

package morozov.system.gui.dialogs.scalable.common;

import java.util.Comparator;

public class AlphabeticComparator implements Comparator<Object> {
	public int compare(Object o1, Object o2) {
		String s1= (String)o1;
		String s2= (String)o2;
		return s1.toLowerCase().compareTo(s2.toLowerCase());
	}
}
